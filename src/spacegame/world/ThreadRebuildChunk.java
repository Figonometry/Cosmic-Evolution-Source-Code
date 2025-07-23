package spacegame.world;

import org.lwjgl.BufferUtils;
import spacegame.block.Block;
import spacegame.block.ITickable;
import spacegame.core.Logger;
import spacegame.core.SpaceGame;
import spacegame.render.RenderBlocks;

public final class ThreadRebuildChunk implements Runnable {
    public Chunk workingChunk;
    public WorldFace worldFace;

    public ThreadRebuildChunk(Chunk chunk, WorldFace worldFace){
        this.workingChunk = chunk;
        this.worldFace = worldFace;
    }

    @Override
    public void run() {
        try {
            this.rebuildChunk();
        } catch (Exception e){
            new Logger(e);
        }
    }

    public void rebuildChunk() {
        if (this.workingChunk.firstRender) {
            this.workingChunk.notifyAllBlocks();
            this.workingChunk.firstRender = false;
        }
        this.workingChunk.shouldRender = this.workingChunk.checkIfChunkShouldRender();
        if (!this.workingChunk.shouldRender) {return;}
        RenderBlocks renderBlocks = new RenderBlocks();
        if (this.workingChunk.containsWater) {
            this.workingChunk.setLightValueUnderWater();
        }
        if (this.workingChunk.updateSkylight) {
           // this.workingChunk.setSkyLight();
            this.workingChunk.updateSkylight = false;
            //  this.worldFace.updateSkyLightMapChunks(this.x, this.y, this.z);
        }
        int faceNumber = this.workingChunk.calculateFaceNumber();
        int size = (int) (faceNumber * 1.2F);
        this.workingChunk.vertexArrayOpaque = new float[size * 16];
        this.workingChunk.vertexArrayTransparent = new float[size * 16];
        this.workingChunk.vertexBufferOpaque = BufferUtils.createFloatBuffer(size * 16);
        this.workingChunk.elementBufferOpaque = BufferUtils.createIntBuffer(size * 6);
        this.workingChunk.vertexBufferTransparent = BufferUtils.createFloatBuffer(size * 16);
        this.workingChunk.elementBufferTransparent = BufferUtils.createIntBuffer(size * 6);
        this.workingChunk.excludeTopFace = new int[1024];
        this.workingChunk.excludeBottomFace = new int[1024];
        this.workingChunk.excludeNorthFace = new int[1024];
        this.workingChunk.excludeSouthFace = new int[1024];
        this.workingChunk.excludeEastFace = new int[1024];
        this.workingChunk.excludeWestFace = new int[1024];
        int blockX = 0;
        int blockY = 0;
        int blockZ = 0;
        int tickableIndex = 0;
        this.workingChunk.tickableBlockIndex = new short[32768];
        for (int block = 0; block < this.workingChunk.blocks.length; block++) {
            if (Block.list[this.workingChunk.blocks[block]].ID == Block.air.ID) {continue;}
            if(Block.list[this.workingChunk.blocks[block]] instanceof ITickable){
                this.workingChunk.tickableBlockIndex[tickableIndex] = (short) block;
                tickableIndex++;
            }
            blockX = this.workingChunk.getBlockXFromIndex(block);
            blockY = this.workingChunk.getBlockYFromIndex(block);
            blockZ = this.workingChunk.getBlockZFromIndex(block);
            for (int face = 0; face < 6; face++) {
                switch (face) {
                    case 0 -> {
                        if (this.workingChunk.checkBitValue(this.workingChunk.topFaceBitMask[Chunk.calculateBitMaskIndex(blockX, blockZ)], this.workingChunk.createMask(blockY)) != 0 && this.workingChunk.checkBitValue(this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(blockX, blockZ)], this.workingChunk.createMask(blockY)) == 0) {
                            if (Block.list[this.workingChunk.blocks[block]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(block, 0)) {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, this.calculateGreedyMeshSize(blockX, blockY, blockZ, face), renderBlocks);
                            } else {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, new int[2], renderBlocks);
                            }
                        }
                    }
                    case 1 -> {
                        if (this.workingChunk.checkBitValue(this.workingChunk.bottomFaceBitMask[Chunk.calculateBitMaskIndex(blockX, blockZ)], this.workingChunk.createMask(blockY)) != 0 && this.workingChunk.checkBitValue(this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(blockX, blockZ)], this.workingChunk.createMask(blockY)) == 0) {
                            if (Block.list[this.workingChunk.blocks[block]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(block, 1)) {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, this.calculateGreedyMeshSize(blockX, blockY, blockZ, face), renderBlocks);
                            } else {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, new int[2], renderBlocks);
                            }
                        }
                    }
                    case 2 -> {
                        if (this.workingChunk.checkBitValue(this.workingChunk.northFaceBitMask[Chunk.calculateBitMaskIndex(blockZ, blockY)], this.workingChunk.createMask(blockX)) != 0 && this.workingChunk.checkBitValue(this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(blockZ, blockY)], this.workingChunk.createMask(blockX)) == 0) {
                            if (Block.list[this.workingChunk.blocks[block]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(block, 2)) {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, this.calculateGreedyMeshSize(blockX, blockY, blockZ, face), renderBlocks);
                            } else {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, new int[2], renderBlocks);
                            }
                        }
                    }
                    case 3 -> {
                        if (this.workingChunk.checkBitValue(this.workingChunk.southFaceBitMask[Chunk.calculateBitMaskIndex(blockZ, blockY)], this.workingChunk.createMask(blockX)) != 0 && this.workingChunk.checkBitValue(this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(blockZ, blockY)], this.workingChunk.createMask(blockX)) == 0) {
                            if (Block.list[this.workingChunk.blocks[block]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(block, 3)) {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, this.calculateGreedyMeshSize(blockX, blockY, blockZ, face), renderBlocks);
                            } else {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, new int[2], renderBlocks);
                            }
                        }
                    }
                    case 4 -> {
                        if (this.workingChunk.checkBitValue(this.workingChunk.eastFaceBitMask[Chunk.calculateBitMaskIndex(blockX, blockY)], this.workingChunk.createMask(blockZ)) != 0 && this.workingChunk.checkBitValue(this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(blockX, blockY)], this.workingChunk.createMask(blockZ)) == 0) {
                            if (Block.list[this.workingChunk.blocks[block]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(block, 4)) {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, this.calculateGreedyMeshSize(blockX, blockY, blockZ, face), renderBlocks);
                            } else {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, new int[2], renderBlocks);
                            }
                        }
                    }
                    case 5 -> {
                        if (this.workingChunk.checkBitValue(this.workingChunk.westFaceBitMask[Chunk.calculateBitMaskIndex(blockX, blockY)], this.workingChunk.createMask(blockZ)) != 0 && this.workingChunk.checkBitValue(this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(blockX, blockY)], this.workingChunk.createMask(blockZ)) == 0) {
                            if (Block.list[this.workingChunk.blocks[block]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(block, 5)) {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, this.calculateGreedyMeshSize(blockX, blockY, blockZ, face), renderBlocks);
                            } else {
                                this.addBlockToRenderData(this.workingChunk.blocks[block], block, face, new int[2], renderBlocks);
                            }
                        }
                    }
                }
            }
        }
        float[] truncatedVertexArrayOpaque = new float[this.workingChunk.vertexIndexOpaque];
        for (int i = 0; i < truncatedVertexArrayOpaque.length; i++) {
            truncatedVertexArrayOpaque[i] = this.workingChunk.vertexArrayOpaque[i];
        }
        this.workingChunk.vertexArrayOpaque = truncatedVertexArrayOpaque;
        this.workingChunk.elementArrayOpaque = new int[(this.workingChunk.vertexArrayOpaque.length / 16) * 6];
        int elementOffset = 0;
        int index = 0;
        for (int i = 0; i < this.workingChunk.elementArrayOpaque.length / 6; i++) {
            this.workingChunk.elementArrayOpaque[index] = elementOffset + 2;
            this.workingChunk.elementArrayOpaque[index + 1] = elementOffset + 1;
            this.workingChunk.elementArrayOpaque[index + 2] = elementOffset;
            this.workingChunk.elementArrayOpaque[index + 3] = elementOffset;
            this.workingChunk.elementArrayOpaque[index + 4] = elementOffset + 1;
            this.workingChunk.elementArrayOpaque[index + 5] = elementOffset + 3;
            elementOffset += 4;
            index += 6;
        }
        this.workingChunk.vertexBufferOpaque.put(this.workingChunk.vertexArrayOpaque);
        this.workingChunk.elementBufferOpaque.put(this.workingChunk.elementArrayOpaque);
        this.workingChunk.vertexBufferOpaque.flip();
        this.workingChunk.elementBufferOpaque.flip();

        float[] truncatedVertexArrayTransparent = new float[this.workingChunk.vertexIndexTransparent];
        for (int i = 0; i < truncatedVertexArrayTransparent.length; i++) {
            truncatedVertexArrayTransparent[i] = this.workingChunk.vertexArrayTransparent[i];
        }
        this.workingChunk.vertexArrayTransparent = truncatedVertexArrayTransparent;
        this.workingChunk.elementArrayTransparent = new int[(this.workingChunk.vertexArrayTransparent.length / 16) * 6];
        elementOffset = 0;
        index = 0;
        for (int i = 0; i < this.workingChunk.elementArrayTransparent.length / 6; i++) {
            this.workingChunk.elementArrayTransparent[index] = elementOffset + 2;
            this.workingChunk.elementArrayTransparent[index + 1] = elementOffset + 1;
            this.workingChunk.elementArrayTransparent[index + 2] = elementOffset;
            this.workingChunk.elementArrayTransparent[index + 3] = elementOffset;
            this.workingChunk.elementArrayTransparent[index + 4] = elementOffset + 1;
            this.workingChunk.elementArrayTransparent[index + 5] = elementOffset + 3;
            elementOffset += 4;
            index += 6;
        }
        this.workingChunk.vertexBufferTransparent.put(this.workingChunk.vertexArrayTransparent);
        this.workingChunk.elementBufferTransparent.put(this.workingChunk.elementArrayTransparent);
        this.workingChunk.vertexBufferTransparent.flip();
        this.workingChunk.elementBufferTransparent.flip();
        boolean empty = tickableIndex == 0;
        this.workingChunk.truncateTickableIndexArray(tickableIndex + 1, empty);
        this.workingChunk.excludeTopFace = null;
        this.workingChunk.excludeBottomFace = null;
        this.workingChunk.excludeNorthFace = null;
        this.workingChunk.excludeSouthFace = null;
        this.workingChunk.excludeEastFace = null;
        this.workingChunk.excludeWestFace = null;
        this.workingChunk.needsToUpdate = false;
        synchronized (SpaceGame.instance.save.activeWorld.activeWorldFace.chunkController.bindingChunks) {
            SpaceGame.instance.save.activeWorld.activeWorldFace.chunkController.bindingChunks.add(this.workingChunk);
        }
        this.workingChunk.updating = false;
    }

    private void addBlockToRenderData(short block, int index, int face, int[] greedyMeshSize, RenderBlocks renderBlocks) {
        switch (Block.list[block].blockName) {
            case "TORCH" -> renderBlocks.renderTorch(this.workingChunk, this.worldFace, block, index, face);
            case "WATER", "BERRY_BUSH", "CAMPFIRE" ->
                    renderBlocks.renderTransparentBlock(this.workingChunk, this.worldFace, block, index, face, greedyMeshSize);
            case "CAMPFIRE_LIT" -> renderBlocks.renderCampFire(this.workingChunk, this.worldFace, block, index, face);
            default ->
                    renderBlocks.renderStandardBlock(this.workingChunk, this.worldFace, block, index, face, greedyMeshSize);
        }
    }

    private int[] calculateGreedyMeshSize(int x, int y, int z, int face) {
        switch (face) {
            case 0 -> {
                return this.greedyMeshTopFace(x, y, z);
            }
            case 1 -> {
                return this.greedyMeshBottomFace(x, y, z);
            }
            case 2 -> {
                return this.greedyMeshNorthFace(x, y, z);
            }
            case 3 -> {
                return this.greedyMeshSouthFace(x, y, z);
            }
            case 4 -> {
                return this.greedyMeshEastFace(x, y, z);
            }
            case 5 -> {
                return this.greedyMeshWestFace(x, y, z);
            }
        }
        return new int[2];
    }

    private int[] greedyMeshTopFace(int x, int y, int z) {
        int[] returnValue = new int[2];
        x = this.workingChunk.getBlockXFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        y = this.workingChunk.getBlockYFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        z = this.workingChunk.getBlockZFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        int returnX = 31 - (x % 32);
        int returnZ = 31 - (z % 32);

        for (int i = 0; i <= returnX; i++) {
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.topFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z)], this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(x + i, z)], y, this.worldFace.getBlockLightValue(x, y + 1, z), this.worldFace.getBlockLightValue(x + i, y + 1, z), this.worldFace.getBlockLightColorAsInt(x, y + 1, z), this.worldFace.getBlockLightColorAsInt(x + i, y + 1, z), this.worldFace.getSkyLightValue(x, y + 1, z), this.worldFace.getSkyLightValue(x + i, y + 1, z)) || !this.areCornersClear(x + i, y, z, 0)) {
                returnX = i - 1;
                break;
            }
        }

        if (returnX < 0) {
            returnX = 0;
        }

        loop:
        for (int j = 0; j <= returnZ; j++) {
            for (int i = 0; i <= returnX; i++) {
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z + j)], this.workingChunk.topFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z + j)], this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(x + i, z + j)], y, this.worldFace.getBlockLightValue(x, y + 1, z), this.worldFace.getBlockLightValue(x + i, y + 1, z + j), this.worldFace.getBlockLightColorAsInt(x, y + 1, z), this.worldFace.getBlockLightColorAsInt(x + i, y + 1, z + j), this.worldFace.getSkyLightValue(x, y + 1, z), this.worldFace.getSkyLightValue(x + i, y + 1, z + j)) || !this.areCornersClear(x + i, y, z + j, 0)) {
                    returnZ = j - 1;
                    break loop;
                }
            }
        }


        if (returnZ < 0) {
            returnZ = 0;
        }


        returnValue[0] = returnX;
        returnValue[1] = returnZ;
        this.excludeFaces(x, y, z, returnX, 0, returnZ, 0);
        return returnValue;
    }

    private int[] greedyMeshBottomFace(int x, int y, int z) {
        int[] returnValue = new int[2];
        x = this.workingChunk.getBlockXFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        y = this.workingChunk.getBlockYFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        z = this.workingChunk.getBlockZFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        int returnX = 31 - (x % 32);
        int returnZ = 31 - (z % 32);

        for (int i = 0; i <= returnX; i++) {
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.bottomFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z)], this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(x + i, z)], y, this.worldFace.getBlockLightValue(x, y - 1, z), this.worldFace.getBlockLightValue(x + i, y - 1, z), this.worldFace.getBlockLightColorAsInt(x, y - 1, z), this.worldFace.getBlockLightColorAsInt(x + i, y - 1, z), this.worldFace.getSkyLightValue(x, y - 1, z), this.worldFace.getSkyLightValue(x + i, y - 1, z)) || !this.areCornersClear(x + i, y, z, 1)) {
                returnX = i - 1;
                break;
            }
        }

        if (returnX < 0) {
            returnX = 0;
        }

        loop:
        for (int j = 0; j <= returnZ; j++) {
            for (int i = 0; i <= returnX; i++) {
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z + j)], this.workingChunk.bottomFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z + j)], this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(x + i, z + j)], y, this.worldFace.getBlockLightValue(x, y - 1, z + j), this.worldFace.getBlockLightValue(x + i, y - 1, z + j), this.worldFace.getBlockLightColorAsInt(x, y - 1, z + j), this.worldFace.getBlockLightColorAsInt(x + i, y - 1, z + j), this.worldFace.getSkyLightValue(x, y - 1, z + j), this.worldFace.getSkyLightValue(x + i, y - 1, z + j)) || !this.areCornersClear(x + i, y, z + j, 1)) {
                    returnZ = j - 1;
                    break loop;
                }
            }
        }


        if (returnZ < 0) {
            returnZ = 0;
        }
        returnValue[0] = returnX;
        returnValue[1] = returnZ;
        this.excludeFaces(x, y, z, returnX, 0, returnZ, 1);
        return returnValue;
    }

    private int[] greedyMeshNorthFace(int x, int y, int z) { //Z then Y
        int[] returnValue = new int[2];
        x = this.workingChunk.getBlockXFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        y = this.workingChunk.getBlockYFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        z = this.workingChunk.getBlockZFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        int returnZ = 31 - (z % 32);
        int returnY = 31 - (y % 32);

        for (int i = 0; i <= returnZ; i++) {
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z + i)], this.workingChunk.northFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y)], this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(z + i, y)], x, this.worldFace.getBlockLightValue(x - 1, y, z), this.worldFace.getBlockLightValue(x - 1, y, z + i), this.worldFace.getBlockLightColorAsInt(x - 1, y, z), this.worldFace.getBlockLightColorAsInt(x - 1, y, z + i), this.worldFace.getSkyLightValue(x - 1, y, z), this.worldFace.getSkyLightValue(x - 1, y, z + i)) || !this.areCornersClear(x, y, z + i, 2)) {
                returnZ = i - 1;
                break;
            }
        }

        if (returnZ < 0) {
            returnZ = 0;
        }

        loop:
        for (int j = 0; j <= returnY; j++) {
            for (int i = 0; i <= returnZ; i++) {
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y + j, z + i)], this.workingChunk.northFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y + j)], this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(z + i, y + j)], x, this.worldFace.getBlockLightValue(x - 1, y, z), this.worldFace.getBlockLightValue(x - 1, y + j, z + i), this.worldFace.getBlockLightColorAsInt(x - 1, y, z), this.worldFace.getBlockLightColorAsInt(x - 1, y + j, z + i), this.worldFace.getSkyLightValue(x - 1, y, z), this.worldFace.getSkyLightValue(x - 1, y + j, z + i)) || !this.areCornersClear(x, y + j, z + i, 2)) {
                    returnY = j - 1;
                    break loop;
                }
            }
        }

        if (returnY < 0) {
            returnY = 0;
        }

        returnValue[0] = returnZ;
        returnValue[1] = returnY;
        this.excludeFaces(x, y, z, 0, returnY, returnZ, 2);
        return returnValue;
    }


    private int[] greedyMeshSouthFace(int x, int y, int z) {
        int[] returnValue = new int[2];
        x = this.workingChunk.getBlockXFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        y = this.workingChunk.getBlockYFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        z = this.workingChunk.getBlockZFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        int returnZ = 31 - (z % 32);
        int returnY = 31 - (y % 32);

        for (int i = 0; i <= returnZ; i++) {
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z + i)], this.workingChunk.southFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y)], this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(z + i, y)], x, this.worldFace.getBlockLightValue(x + 1, y, z), this.worldFace.getBlockLightValue(x + 1, y, z + i), this.worldFace.getBlockLightColorAsInt(x + 1, y, z), this.worldFace.getBlockLightColorAsInt(x + 1, y, z + i), this.worldFace.getSkyLightValue(x + 1, y, z), this.worldFace.getSkyLightValue(x + 1, y, z + i)) || !this.areCornersClear(x, y, z + i, 3)) {
                returnZ = i - 1;
                break;
            }
        }

        if (returnZ < 0) {
            returnZ = 0;
        }

        loop:
        for (int j = 0; j <= returnY; j++) {
            for (int i = 0; i <= returnZ; i++) {
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y + j, z + i)], this.workingChunk.southFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y + j)], this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(z + i, y + j)], x, this.worldFace.getBlockLightValue(x + 1, y, z), this.worldFace.getBlockLightValue(x + 1, y + j, z + i), this.worldFace.getBlockLightColorAsInt(x + 1, y, z), this.worldFace.getBlockLightColorAsInt(x + 1, y + j, z + i), this.worldFace.getSkyLightValue(x + 1, y, z), this.worldFace.getSkyLightValue(x + 1, y + j, z + i)) || !this.areCornersClear(x, y + j, z + i, 3)) {
                    returnY = j - 1;
                    break loop;
                }
            }
        }

        if (returnY < 0) {
            returnY = 0;
        }

        returnValue[0] = returnZ;
        returnValue[1] = returnY;
        this.excludeFaces(x, y, z, 0, returnY, returnZ, 3);
        return returnValue;
    }

    private int[] greedyMeshEastFace(int x, int y, int z) {//X then Y
        int[] returnValue = new int[2];
        x = this.workingChunk.getBlockXFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        y = this.workingChunk.getBlockYFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        z = this.workingChunk.getBlockZFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        int returnX = 31 - (x % 32);
        int returnY = 31 - (y % 32);

        for (int i = 0; i <= returnX; i++) {
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.eastFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y)], this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(x + i, y)], z, this.worldFace.getBlockLightValue(x, y, z - 1), this.worldFace.getBlockLightValue(x + i, y, z - 1), this.worldFace.getBlockLightColorAsInt(x, y, z - 1), this.worldFace.getBlockLightColorAsInt(x + i, y, z - 1), this.worldFace.getSkyLightValue(x, y, z - 1), this.worldFace.getSkyLightValue(x + i, y, z - 1)) || !this.areCornersClear(x + i, y, z, 4)) {
                returnX = i - 1;
                break;
            }
        }

        if (returnX < 0) {
            returnX = 0;
        }

        loop:
        for (int j = 0; j <= returnY; j++) {
            for (int i = 0; i <= returnX; i++) {
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y + j, z)], this.workingChunk.eastFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y + j)], this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(x + i, y + j)], z, this.worldFace.getBlockLightValue(x, y, z - 1), this.worldFace.getBlockLightValue(x + i, y + j, z - 1), this.worldFace.getBlockLightColorAsInt(x, y, z - 1), this.worldFace.getBlockLightColorAsInt(x + i, y + j, z - 1), this.worldFace.getSkyLightValue(x, y, z - 1), this.worldFace.getSkyLightValue(x + i, y + j, z - 1)) || !this.areCornersClear(x + i, y + j, z, 4)) {
                    returnY = j - 1;
                    break loop;
                }
            }
        }

        if (returnY < 0) {
            returnY = 0;
        }

        returnValue[0] = returnX;
        returnValue[1] = returnY;
        this.excludeFaces(x, y, z, returnX, returnY, 0, 4);
        return returnValue;
    }

    private int[] greedyMeshWestFace(int x, int y, int z) {
        int[] returnValue = new int[2];
        x = this.workingChunk.getBlockXFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        y = this.workingChunk.getBlockYFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        z = this.workingChunk.getBlockZFromIndex(Chunk.getBlockIndexFromCoordinates(x, y, z));
        int returnX = 31 - (x % 32);
        int returnY = 31 - (y % 32);

        for (int i = 0; i <= returnX; i++) {
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.westFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y)], this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(x + i, y)], z, this.worldFace.getBlockLightValue(x, y, z + 1), this.worldFace.getBlockLightValue(x + i, y, z + 1), this.worldFace.getBlockLightColorAsInt(x, y, z + 1), this.worldFace.getBlockLightColorAsInt(x + i, y, z + 1), this.worldFace.getSkyLightValue(x, y, z + 1), this.worldFace.getSkyLightValue(x + i, y, z + 1)) || !this.areCornersClear(x + i, y, z, 5)) {
                returnX = i - 1;
                break;
            }
        }

        if (returnX < 0) {
            returnX = 0;
        }

        loop:
        for (int j = 0; j <= returnY; j++) {
            for (int i = 0; i <= returnX; i++) {
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y + j, z)], this.workingChunk.westFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y + j)], this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(x + i, y + j)], z, this.worldFace.getBlockLightValue(x, y, z + 1), this.worldFace.getBlockLightValue(x + i, y + j, z + 1), this.worldFace.getBlockLightColorAsInt(x, y, z + 1), this.worldFace.getBlockLightColorAsInt(x + i, y + j, z + 1), this.worldFace.getSkyLightValue(x, y, z + 1), this.worldFace.getSkyLightValue(x + i, y + j, z + 1)) || !this.areCornersClear(x + i, y + j, z, 5)) {
                    returnY = j - 1;
                    break loop;
                }
            }
        }

        if (returnY < 0) {
            returnY = 0;
        }

        returnValue[0] = returnX;
        returnValue[1] = returnY;
        this.excludeFaces(x, y, z, returnX, returnY, 0, 5);
        return returnValue;
    }

    private boolean canBlockFaceGreedyMesh(short firstBlock, short secondBlock, int shouldFaceRenderBitMask, int excludeFaceBitMask, int maskBitToCheck, byte firstLightLevel, byte secondLightLevel, int firstLightColor, int secondLightColor, byte firstSkylightLevel, byte secondSkylightLevel) {
        return firstBlock == secondBlock && this.workingChunk.checkBitValue(shouldFaceRenderBitMask, this.workingChunk.createMask(maskBitToCheck)) != 0 && this.workingChunk.checkBitValue(excludeFaceBitMask, this.workingChunk.createMask(maskBitToCheck)) == 0 && firstLightLevel == secondLightLevel && firstLightColor == secondLightColor && firstSkylightLevel == secondSkylightLevel;
    }

    private boolean areCornersClear(int x, int y, int z, int faceType) {
        switch (faceType) {
            case RenderBlocks.TOP_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y, z + 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z + 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y + 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid);
            }
            case RenderBlocks.BOTTOM_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y, z + 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z + 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y - 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid);
            }
            case RenderBlocks.NORTH_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x - 1, y - 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y - 1, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z - 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z + 1)].isSolid);
            }
            case RenderBlocks.SOUTH_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x + 1, y - 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y - 1, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z - 1)].isSolid) && (!Block.list[this.worldFace.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z + 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z + 1)].isSolid);
            }
            case RenderBlocks.EAST_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y - 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y - 1, z)].isSolid) && (!Block.list[this.worldFace.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y + 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y + 1, z)].isSolid) && (!Block.list[this.worldFace.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y + 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y + 1, z)].isSolid);
            }
            case RenderBlocks.WEST_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x + 1, y, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y - 1, z)].isSolid) && (!Block.list[this.worldFace.getBlockID(x - 1, y, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y + 1, z)].isSolid) && (!Block.list[this.worldFace.getBlockID(x + 1, y, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y + 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x + 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y + 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x + 1, y + 1, z)].isSolid);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean shouldStaringBlockGreedyMesh(int index, int faceType) {
        int x = this.workingChunk.getBlockXFromIndex(index);
        int y = this.workingChunk.getBlockYFromIndex(index);
        int z = this.workingChunk.getBlockZFromIndex(index);
        switch (faceType) {
            case RenderBlocks.TOP_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x, y + 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y + 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z - 1)].isSolid);
            }
            case RenderBlocks.BOTTOM_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x, y - 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z - 1)].isSolid);
            }
            case RenderBlocks.NORTH_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y - 1, z - 1)].isSolid);
            }
            case RenderBlocks.SOUTH_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x + 1, y - 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x, y, z - 1)].isSolid && Block.list[this.worldFace.getBlockID(x, y - 1, z - 1)].isSolid);
            }
            case RenderBlocks.EAST_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x, y - 1, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z - 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y - 1, z)].isSolid);
            }
            case RenderBlocks.WEST_FACE -> {
                return (!Block.list[this.worldFace.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y, z + 1)].isSolid && !Block.list[this.worldFace.getBlockID(x - 1, y - 1, z + 1)].isSolid) && (Block.list[this.worldFace.getBlockID(x, y - 1, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y, z)].isSolid && Block.list[this.worldFace.getBlockID(x - 1, y - 1, z)].isSolid);
            }
            default -> {
                return false;
            }
        }
    }

    private void excludeFaces(int x, int y, int z, int meshX, int meshY, int meshZ, int faceType) {
        switch (faceType) {
            case RenderBlocks.TOP_FACE -> {
                for (int i = x; i <= x + meshX; i++) {
                    for (int j = z; j <= z + meshZ; j++) {
                        if (this.workingChunk.checkBitValue(this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(i, j)], this.workingChunk.createMask(y)) == 0) {
                            this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(i, j)] = this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(i, j)] ^ this.workingChunk.createMask(y);
                        }
                    }
                }
            }
            case RenderBlocks.BOTTOM_FACE -> {
                for (int i = x; i <= x + meshX; i++) {
                    for (int j = z; j <= z + meshZ; j++) {
                        if (this.workingChunk.checkBitValue(this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(i, j)], this.workingChunk.createMask(y)) == 0) {
                            this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(i, j)] = this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(i, j)] ^ this.workingChunk.createMask(y);
                        }
                    }
                }
            }
            case RenderBlocks.NORTH_FACE -> {
                for (int i = z; i <= z + meshZ; i++) {
                    for (int j = y; j <= y + meshY; j++) {
                        if (this.workingChunk.checkBitValue(this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(i, j)], this.workingChunk.createMask(x)) == 0) {
                            this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(i, j)] = this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(i, j)] ^ this.workingChunk.createMask(x);
                        }
                    }
                }
            }
            case RenderBlocks.SOUTH_FACE -> {
                for (int i = z; i <= z + meshZ; i++) {
                    for (int j = y; j <= y + meshY; j++) {
                        if (this.workingChunk.checkBitValue(this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(i, j)], this.workingChunk.createMask(x)) == 0) {
                            this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(i, j)] = this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(i, j)] ^ this.workingChunk.createMask(x);
                        }
                    }
                }
            }
            case RenderBlocks.EAST_FACE -> {
                for (int i = x; i <= x + meshX; i++) {
                    for (int j = y; j <= y + meshY; j++) {
                        if (this.workingChunk.checkBitValue(this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(i, j)], this.workingChunk.createMask(z)) == 0) {
                            this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(i, j)] = this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(i, j)] ^ this.workingChunk.createMask(z);
                        }
                    }
                }
            }
            case RenderBlocks.WEST_FACE -> {
                for (int i = x; i <= x + meshX; i++) {
                    for (int j = y; j <= y + meshY; j++) {
                        if (this.workingChunk.checkBitValue(this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(i, j)], this.workingChunk.createMask(z)) == 0) {
                            this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(i, j)] = this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(i, j)] ^ this.workingChunk.createMask(z);
                        }
                    }
                }
            }
        }
    }
}
