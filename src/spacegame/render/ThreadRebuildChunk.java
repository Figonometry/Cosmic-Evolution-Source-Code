package spacegame.render;

import org.lwjgl.BufferUtils;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.util.Logger;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class ThreadRebuildChunk implements Runnable {
    public Chunk workingChunk;
    public World parentWorld;

    public ThreadRebuildChunk(Chunk chunk, World world){
        this.workingChunk = chunk;
        this.parentWorld = world;
    }

    @Override
    public void run() {
        try {
            this.rebuildChunk();
        } catch (Throwable e){
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
            this.workingChunk.setSkyLight();
            this.workingChunk.updateSkylight = false;
            //  this.parentWorld.updateSkyLightMapChunks(this.x, this.y, this.z);
        }
        int faceNumber = this.workingChunk.calculateFaceNumber();
        this.workingChunk.vertexBufferOpaque = BufferUtils.createFloatBuffer(faceNumber * 24);
        this.workingChunk.elementBufferOpaque = BufferUtils.createIntBuffer(faceNumber * 6);
        this.workingChunk.vertexBufferTransparent = BufferUtils.createFloatBuffer(faceNumber * 24);
        this.workingChunk.elementBufferTransparent = BufferUtils.createIntBuffer(faceNumber * 6);
        this.workingChunk.excludeTopFace = new int[1024];
        this.workingChunk.excludeBottomFace = new int[1024];
        this.workingChunk.excludeNorthFace = new int[1024];
        this.workingChunk.excludeSouthFace = new int[1024];
        this.workingChunk.excludeEastFace = new int[1024];
        this.workingChunk.excludeWestFace = new int[1024];
        int blockX = 0;
        int blockY = 0;
        int blockZ = 0;
        int index;
        boolean needsToSetUpdateTime = false;

        for(int i = 0; i < this.workingChunk.topFaceBitMask.length; i++){
            if(this.workingChunk.topFaceBitMask[i] == 0)continue;

            for(int j = 0; j < 32; j ++){
                if(this.workingChunk.checkBitValue(this.workingChunk.topFaceBitMask[i], 1 << j) == 0 || this.workingChunk.checkBitValue(this.workingChunk.excludeTopFace[i], 1 << j) != 0)continue;
                index = this.workingChunk.calculateIndexInTopOrBottomFaceBitMasks(i, j);
                if(this.workingChunk.blocks[index] == Block.air.ID)continue;
                blockX = this.workingChunk.getBlockXFromIndex(index);
                blockY = this.workingChunk.getBlockYFromIndex(index);
                blockZ = this.workingChunk.getBlockZFromIndex(index);
                needsToSetUpdateTime = Block.list[this.workingChunk.blocks[index]].ID == Block.grass.ID || Block.list[this.workingChunk.blocks[index]].ID == Block.leaf.ID;
                if (Block.list[this.workingChunk.blocks[index]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(index, RenderBlocks.TOP_FACE)) {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.TOP_FACE, this.calculateGreedyMeshSize(blockX, blockY, blockZ, RenderBlocks.TOP_FACE), renderBlocks);
                } else {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.TOP_FACE, new int[2], renderBlocks);
                }

            }
        }

        for(int i = 0; i < this.workingChunk.bottomFaceBitMask.length; i++){
            if(this.workingChunk.bottomFaceBitMask[i] == 0)continue;

            for(int j = 0; j < 32; j ++){
                if(this.workingChunk.checkBitValue(this.workingChunk.bottomFaceBitMask[i], 1 << j) == 0 || this.workingChunk.checkBitValue(this.workingChunk.excludeBottomFace[i], 1 << j) != 0)continue;
                index = this.workingChunk.calculateIndexInTopOrBottomFaceBitMasks(i, j);
                if(this.workingChunk.blocks[index] == Block.air.ID)continue;
                blockX = this.workingChunk.getBlockXFromIndex(index);
                blockY = this.workingChunk.getBlockYFromIndex(index);
                blockZ = this.workingChunk.getBlockZFromIndex(index);
                needsToSetUpdateTime = Block.list[this.workingChunk.blocks[index]].ID == Block.grass.ID || Block.list[this.workingChunk.blocks[index]].ID == Block.leaf.ID;
                if (Block.list[this.workingChunk.blocks[index]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(index, RenderBlocks.BOTTOM_FACE)) {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.BOTTOM_FACE, this.calculateGreedyMeshSize(blockX, blockY, blockZ, RenderBlocks.BOTTOM_FACE), renderBlocks);
                } else {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.BOTTOM_FACE, new int[2], renderBlocks);
                }

            }
        }

        for(int i = 0; i < this.workingChunk.northFaceBitMask.length; i++){
            if(this.workingChunk.northFaceBitMask[i] == 0)continue;

            for(int j = 0; j < 32; j ++){
                if(this.workingChunk.checkBitValue(this.workingChunk.northFaceBitMask[i], 1 << j) == 0 || this.workingChunk.checkBitValue(this.workingChunk.excludeNorthFace[i], 1 << j) != 0)continue;
                index = this.workingChunk.calculateIndexInNorthOrSouthFaceBitMasks(i, j);
                if(this.workingChunk.blocks[index] == Block.air.ID)continue;
                blockX = this.workingChunk.getBlockXFromIndex(index);
                blockY = this.workingChunk.getBlockYFromIndex(index);
                blockZ = this.workingChunk.getBlockZFromIndex(index);
                needsToSetUpdateTime = Block.list[this.workingChunk.blocks[index]].ID == Block.grass.ID || Block.list[this.workingChunk.blocks[index]].ID == Block.leaf.ID;
                if (Block.list[this.workingChunk.blocks[index]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(index, RenderBlocks.NORTH_FACE)) {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.NORTH_FACE, this.calculateGreedyMeshSize(blockX, blockY, blockZ, RenderBlocks.NORTH_FACE), renderBlocks);
                } else {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.NORTH_FACE, new int[2], renderBlocks);
                }

            }
        }

        for(int i = 0; i < this.workingChunk.southFaceBitMask.length; i++){
            if(this.workingChunk.southFaceBitMask[i] == 0)continue;

            for(int j = 0; j < 32; j ++){
                if(this.workingChunk.checkBitValue(this.workingChunk.southFaceBitMask[i], 1 << j) == 0 || this.workingChunk.checkBitValue(this.workingChunk.excludeSouthFace[i], 1 << j) != 0)continue;
                index = this.workingChunk.calculateIndexInNorthOrSouthFaceBitMasks(i, j);
                if(this.workingChunk.blocks[index] == Block.air.ID)continue;
                blockX = this.workingChunk.getBlockXFromIndex(index);
                blockY = this.workingChunk.getBlockYFromIndex(index);
                blockZ = this.workingChunk.getBlockZFromIndex(index);
                needsToSetUpdateTime = Block.list[this.workingChunk.blocks[index]].ID == Block.grass.ID || Block.list[this.workingChunk.blocks[index]].ID == Block.leaf.ID;
                if (Block.list[this.workingChunk.blocks[index]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(index, RenderBlocks.SOUTH_FACE)) {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.SOUTH_FACE, this.calculateGreedyMeshSize(blockX, blockY, blockZ, RenderBlocks.SOUTH_FACE), renderBlocks);
                } else {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.SOUTH_FACE, new int[2], renderBlocks);
                }

            }
        }

        for(int i = 0; i < this.workingChunk.eastFaceBitMask.length; i++){
            if(this.workingChunk.eastFaceBitMask[i] == 0)continue;

            for(int j = 0; j < 32; j ++){
                if(this.workingChunk.checkBitValue(this.workingChunk.eastFaceBitMask[i], 1 << j) == 0 || this.workingChunk.checkBitValue(this.workingChunk.excludeEastFace[i], 1 << j) != 0)continue;
                index = this.workingChunk.calculateIndexInEastOrWestFaceBitMasks(i, j);
                if(this.workingChunk.blocks[index] == Block.air.ID)continue;
                blockX = this.workingChunk.getBlockXFromIndex(index);
                blockY = this.workingChunk.getBlockYFromIndex(index);
                blockZ = this.workingChunk.getBlockZFromIndex(index);
                needsToSetUpdateTime = Block.list[this.workingChunk.blocks[index]].ID == Block.grass.ID || Block.list[this.workingChunk.blocks[index]].ID == Block.leaf.ID;
                if (Block.list[this.workingChunk.blocks[index]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(index, RenderBlocks.EAST_FACE)) {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.EAST_FACE, this.calculateGreedyMeshSize(blockX, blockY, blockZ, RenderBlocks.EAST_FACE), renderBlocks);
                } else {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.EAST_FACE, new int[2], renderBlocks);
                }
            }
        }

        for(int i = 0; i < this.workingChunk.westFaceBitMask.length; i++){
            if(this.workingChunk.westFaceBitMask[i] == 0)continue;

            for(int j = 0; j < 32; j ++){
                if(this.workingChunk.checkBitValue(this.workingChunk.westFaceBitMask[i], 1 << j) == 0 || this.workingChunk.checkBitValue(this.workingChunk.excludeWestFace[i], 1 << j) != 0)continue;
                index = this.workingChunk.calculateIndexInEastOrWestFaceBitMasks(i, j);
                if(this.workingChunk.blocks[index] == Block.air.ID)continue;
                blockX = this.workingChunk.getBlockXFromIndex(index);
                blockY = this.workingChunk.getBlockYFromIndex(index);
                blockZ = this.workingChunk.getBlockZFromIndex(index);
                needsToSetUpdateTime = Block.list[this.workingChunk.blocks[index]].ID == Block.grass.ID || Block.list[this.workingChunk.blocks[index]].ID == Block.leaf.ID;
                if (Block.list[this.workingChunk.blocks[index]].canGreedyMesh && this.shouldStaringBlockGreedyMesh(index, RenderBlocks.WEST_FACE)) {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.WEST_FACE, this.calculateGreedyMeshSize(blockX, blockY, blockZ, RenderBlocks.WEST_FACE), renderBlocks);
                } else {
                    this.addBlockToRenderData(this.workingChunk.blocks[index], index, RenderBlocks.WEST_FACE, new int[2], renderBlocks);
                }
            }
        }

        this.workingChunk.vertexBufferOpaque.flip();
        this.workingChunk.elementBufferOpaque.flip();

        this.workingChunk.vertexBufferTransparent.flip();
        this.workingChunk.elementBufferTransparent.flip();


        this.workingChunk.excludeTopFace = null;
        this.workingChunk.excludeBottomFace = null;
        this.workingChunk.excludeNorthFace = null;
        this.workingChunk.excludeSouthFace = null;
        this.workingChunk.excludeEastFace = null;
        this.workingChunk.excludeWestFace = null;
        this.workingChunk.needsToUpdate = false;

        if(needsToSetUpdateTime) {
            this.workingChunk.updateTime = CosmicEvolution.globalRand.nextLong(CosmicEvolution.instance.save.time + CosmicEvolution.instance.everything.getObjectAssociatedWithWorld(CosmicEvolution.instance.save.activeWorld).rotationPeriod / 2, CosmicEvolution.instance.save.time + CosmicEvolution.instance.everything.getObjectAssociatedWithWorld(CosmicEvolution.instance.save.activeWorld).rotationPeriod);
        }
        synchronized (this.parentWorld.chunkController.bindingChunks) {
           this.parentWorld.chunkController.bindingChunks.add(this.workingChunk);
        }
        this.workingChunk.updating = false;
    }

    private void addBlockToRenderData(short block, int index, int face, int[] greedyMeshSize, RenderBlocks renderBlocks) {
        switch (Block.list[block].blockName) {
            case "TORCH" -> renderBlocks.renderTorch(this.workingChunk, this.parentWorld, block, index, face);
            case "WATER", "TALL_GRASS", "SAPLING" ->
                    renderBlocks.renderTransparentBlock(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
            case "CAMPFIRE_LIT" -> {
                renderBlocks.renderCampFire(this.workingChunk, this.parentWorld, block, index, face);
                renderBlocks.renderLogOnCampfire(this.workingChunk, this.parentWorld, block, index, face);
            }
            case "CAMPFIRE" -> {
                renderBlocks.renderCampFireUnlit(this.workingChunk, this.parentWorld, block, index, face);
                renderBlocks.renderLogOnCampfire(this.workingChunk, this.parentWorld, block, index, face);
            }
            case "BERRY_BUSH" -> renderBlocks.renderBerryBush(this.workingChunk, this.parentWorld, block, index, face);
            case "GRASS" -> renderBlocks.renderGrassBlock(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
            case "GRASS_WITH_CLAY" -> renderBlocks.renderGrassBlockWithClay(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
            case "UNLIT_PIT_KILN", "PIT_KILN" -> renderBlocks.renderPitKiln(this.workingChunk, this.parentWorld, block, index, face);
            case "ITEM_STONE" -> renderBlocks.renderItemStone(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
            case "ITEM_STICK" -> renderBlocks.renderItemStick(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
            case "LOG_PILE" -> renderBlocks.renderLogPile(this.workingChunk, this.parentWorld, block, index, face);
            case "ITEM_BLOCK" -> renderBlocks.renderItemBlock(this.workingChunk, this.parentWorld, block, index, face);
            case "BRICK_PILE" -> renderBlocks.renderBrickPile(this.workingChunk, this.parentWorld, block, index, face);
            case "BERRY_BUSH_GROWING" -> renderBlocks.renderBerryBushGrowing(this.workingChunk, this.parentWorld, block, index, face);
            case "REEDS_GROWTH" -> renderBlocks.renderReedGrowing(this.workingChunk, this.parentWorld, block, index, face);
            case "OAK_LOG" -> renderBlocks.renderStandardBlockWithoutAutoUV(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
            case "LEAF" -> {
                if(GameSettings.transparentLeaves){
                    renderBlocks.renderTransparentBlock(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
                } else {
                    renderBlocks.renderStandardBlock(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
                }
            }
            default ->
                    renderBlocks.renderStandardBlock(this.workingChunk, this.parentWorld, block, index, face, greedyMeshSize);
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
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.topFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z)], this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(x + i, z)], y, this.parentWorld.getBlockLightValue(x, y + 1, z), this.parentWorld.getBlockLightValue(x + i, y + 1, z), this.parentWorld.getBlockLightColorAsInt(x, y + 1, z), this.parentWorld.getBlockLightColorAsInt(x + i, y + 1, z), this.parentWorld.getBlockSkyLightValue(x, y + 1, z), this.parentWorld.getBlockSkyLightValue(x + i, y + 1, z)) || !this.areCornersClear(x + i, y, z, 0)) {
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
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z + j)], this.workingChunk.topFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z + j)], this.workingChunk.excludeTopFace[Chunk.calculateBitMaskIndex(x + i, z + j)], y, this.parentWorld.getBlockLightValue(x, y + 1, z), this.parentWorld.getBlockLightValue(x + i, y + 1, z + j), this.parentWorld.getBlockLightColorAsInt(x, y + 1, z), this.parentWorld.getBlockLightColorAsInt(x + i, y + 1, z + j), this.parentWorld.getBlockSkyLightValue(x, y + 1, z), this.parentWorld.getBlockSkyLightValue(x + i, y + 1, z + j)) || !this.areCornersClear(x + i, y, z + j, 0)) {
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
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.bottomFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z)], this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(x + i, z)], y, this.parentWorld.getBlockLightValue(x, y - 1, z), this.parentWorld.getBlockLightValue(x + i, y - 1, z), this.parentWorld.getBlockLightColorAsInt(x, y - 1, z), this.parentWorld.getBlockLightColorAsInt(x + i, y - 1, z), this.parentWorld.getBlockSkyLightValue(x, y - 1, z), this.parentWorld.getBlockSkyLightValue(x + i, y - 1, z)) || !this.areCornersClear(x + i, y, z, 1)) {
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
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z + j)], this.workingChunk.bottomFaceBitMask[Chunk.calculateBitMaskIndex(x + i, z + j)], this.workingChunk.excludeBottomFace[Chunk.calculateBitMaskIndex(x + i, z + j)], y, this.parentWorld.getBlockLightValue(x, y - 1, z + j), this.parentWorld.getBlockLightValue(x + i, y - 1, z + j), this.parentWorld.getBlockLightColorAsInt(x, y - 1, z + j), this.parentWorld.getBlockLightColorAsInt(x + i, y - 1, z + j), this.parentWorld.getBlockSkyLightValue(x, y - 1, z + j), this.parentWorld.getBlockSkyLightValue(x + i, y - 1, z + j)) || !this.areCornersClear(x + i, y, z + j, 1)) {
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
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z + i)], this.workingChunk.northFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y)], this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(z + i, y)], x, this.parentWorld.getBlockLightValue(x - 1, y, z), this.parentWorld.getBlockLightValue(x - 1, y, z + i), this.parentWorld.getBlockLightColorAsInt(x - 1, y, z), this.parentWorld.getBlockLightColorAsInt(x - 1, y, z + i), this.parentWorld.getBlockSkyLightValue(x - 1, y, z), this.parentWorld.getBlockSkyLightValue(x - 1, y, z + i)) || !this.areCornersClear(x, y, z + i, 2)) {
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
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y + j, z + i)], this.workingChunk.northFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y + j)], this.workingChunk.excludeNorthFace[Chunk.calculateBitMaskIndex(z + i, y + j)], x, this.parentWorld.getBlockLightValue(x - 1, y, z), this.parentWorld.getBlockLightValue(x - 1, y + j, z + i), this.parentWorld.getBlockLightColorAsInt(x - 1, y, z), this.parentWorld.getBlockLightColorAsInt(x - 1, y + j, z + i), this.parentWorld.getBlockSkyLightValue(x - 1, y, z), this.parentWorld.getBlockSkyLightValue(x - 1, y + j, z + i)) || !this.areCornersClear(x, y + j, z + i, 2)) {
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
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z + i)], this.workingChunk.southFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y)], this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(z + i, y)], x, this.parentWorld.getBlockLightValue(x + 1, y, z), this.parentWorld.getBlockLightValue(x + 1, y, z + i), this.parentWorld.getBlockLightColorAsInt(x + 1, y, z), this.parentWorld.getBlockLightColorAsInt(x + 1, y, z + i), this.parentWorld.getBlockSkyLightValue(x + 1, y, z), this.parentWorld.getBlockSkyLightValue(x + 1, y, z + i)) || !this.areCornersClear(x, y, z + i, 3)) {
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
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y + j, z + i)], this.workingChunk.southFaceBitMask[Chunk.calculateBitMaskIndex(z + i, y + j)], this.workingChunk.excludeSouthFace[Chunk.calculateBitMaskIndex(z + i, y + j)], x, this.parentWorld.getBlockLightValue(x + 1, y, z), this.parentWorld.getBlockLightValue(x + 1, y + j, z + i), this.parentWorld.getBlockLightColorAsInt(x + 1, y, z), this.parentWorld.getBlockLightColorAsInt(x + 1, y + j, z + i), this.parentWorld.getBlockSkyLightValue(x + 1, y, z), this.parentWorld.getBlockSkyLightValue(x + 1, y + j, z + i)) || !this.areCornersClear(x, y + j, z + i, 3)) {
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
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.eastFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y)], this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(x + i, y)], z, this.parentWorld.getBlockLightValue(x, y, z - 1), this.parentWorld.getBlockLightValue(x + i, y, z - 1), this.parentWorld.getBlockLightColorAsInt(x, y, z - 1), this.parentWorld.getBlockLightColorAsInt(x + i, y, z - 1), this.parentWorld.getBlockSkyLightValue(x, y, z - 1), this.parentWorld.getBlockSkyLightValue(x + i, y, z - 1)) || !this.areCornersClear(x + i, y, z, 4)) {
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
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y + j, z)], this.workingChunk.eastFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y + j)], this.workingChunk.excludeEastFace[Chunk.calculateBitMaskIndex(x + i, y + j)], z, this.parentWorld.getBlockLightValue(x, y, z - 1), this.parentWorld.getBlockLightValue(x + i, y + j, z - 1), this.parentWorld.getBlockLightColorAsInt(x, y, z - 1), this.parentWorld.getBlockLightColorAsInt(x + i, y + j, z - 1), this.parentWorld.getBlockSkyLightValue(x, y, z - 1), this.parentWorld.getBlockSkyLightValue(x + i, y + j, z - 1)) || !this.areCornersClear(x + i, y + j, z, 4)) {
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
            if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y, z)], this.workingChunk.westFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y)], this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(x + i, y)], z, this.parentWorld.getBlockLightValue(x, y, z + 1), this.parentWorld.getBlockLightValue(x + i, y, z + 1), this.parentWorld.getBlockLightColorAsInt(x, y, z + 1), this.parentWorld.getBlockLightColorAsInt(x + i, y, z + 1), this.parentWorld.getBlockSkyLightValue(x, y, z + 1), this.parentWorld.getBlockSkyLightValue(x + i, y, z + 1)) || !this.areCornersClear(x + i, y, z, 5)) {
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
                if (!this.canBlockFaceGreedyMesh(this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)], this.workingChunk.blocks[Chunk.getBlockIndexFromCoordinates(x + i, y + j, z)], this.workingChunk.westFaceBitMask[Chunk.calculateBitMaskIndex(x + i, y + j)], this.workingChunk.excludeWestFace[Chunk.calculateBitMaskIndex(x + i, y + j)], z, this.parentWorld.getBlockLightValue(x, y, z + 1), this.parentWorld.getBlockLightValue(x + i, y + j, z + 1), this.parentWorld.getBlockLightColorAsInt(x, y, z + 1), this.parentWorld.getBlockLightColorAsInt(x + i, y + j, z + 1), this.parentWorld.getBlockSkyLightValue(x, y, z + 1), this.parentWorld.getBlockSkyLightValue(x + i, y + j, z + 1)) || !this.areCornersClear(x + i, y + j, z, 5)) {
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
                return (!Block.list[this.parentWorld.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y, z + 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z + 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y + 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid);
            }
            case RenderBlocks.BOTTOM_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y, z + 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z + 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y - 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid);
            }
            case RenderBlocks.NORTH_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y - 1, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z - 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z + 1)].isSolid);
            }
            case RenderBlocks.SOUTH_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y - 1, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z - 1)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z + 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z + 1)].isSolid);
            }
            case RenderBlocks.EAST_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y - 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y + 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y + 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z)].isSolid);
            }
            case RenderBlocks.WEST_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x + 1, y, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x - 1, y, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z)].isSolid) && (!Block.list[this.parentWorld.getBlockID(x + 1, y, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x, y + 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x + 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y + 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x + 1, y + 1, z)].isSolid);
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
                return (!Block.list[this.parentWorld.getBlockID(x, y + 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y + 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z - 1)].isSolid);
            }
            case RenderBlocks.BOTTOM_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x, y - 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z - 1)].isSolid);
            }
            case RenderBlocks.NORTH_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y - 1, z - 1)].isSolid);
            }
            case RenderBlocks.SOUTH_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x + 1, y - 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x, y, z - 1)].isSolid && Block.list[this.parentWorld.getBlockID(x, y - 1, z - 1)].isSolid);
            }
            case RenderBlocks.EAST_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x, y - 1, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y, z - 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z - 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z)].isSolid);
            }
            case RenderBlocks.WEST_FACE -> {
                return (!Block.list[this.parentWorld.getBlockID(x, y - 1, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y, z + 1)].isSolid && !Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z + 1)].isSolid) && (Block.list[this.parentWorld.getBlockID(x, y - 1, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y, z)].isSolid && Block.list[this.parentWorld.getBlockID(x - 1, y - 1, z)].isSolid);
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
