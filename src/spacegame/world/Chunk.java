package spacegame.world;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.*;
import spacegame.core.CosmicEvolution;
import spacegame.entity.Entity;
import spacegame.entity.EntityLiving;
import spacegame.entity.EntityParticle;
import spacegame.entity.IDecayable;
import spacegame.render.RenderBlocks;
import spacegame.render.Shader;
import spacegame.render.ShouldFaceRenderSorter;
import spacegame.util.MathUtil;
import spacegame.world.blockstate.*;
import spacegame.world.worldtypes.World;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Chunk implements Comparable<Chunk> {
    public static final int NUMBER_OF_BLOCKS = 32768;
    public World parentWorld;
    public volatile boolean needsToUpdate = false;
    public volatile boolean hasDirtyLighting = true;
    public long updateTime;
    public volatile boolean isPopulated = false;
    public boolean shouldRender = false;
    public boolean chunkWillUnload;
    public boolean isUpdating;
    public boolean containsWater;
    public boolean containsAir;
    public boolean chunkContainsOnlyAir;
    public boolean updateSkylight;
    public boolean firstRender = true;
    public int tallGrassCount;
    public float distanceFromPlayer;
    public boolean occluded;
    public int queryID = -10;
    public final int chunkMinX;
    public final int chunkMinY;
    public final int chunkMinZ;
    public final int chunkMaxX;
    public final int chunkMaxY;
    public final int chunkMaxZ;
    public final int x;
    public final int y;
    public final int z;
    public boolean modifiedSinceLastSave;
    public short[] blocks; //DO NOT DIRECT READ/WRITE, You must call set/getBlockID in order to properly manipulate the compressed data
    public ChunkBlockPallette blockPallette = new ChunkBlockPallette(this);
    public byte[] light = new byte[32768]; //The skylight is stored in the upper half of the byte, the block lighting is stored in the lower half
    public short[] lightColorRedAndGreen;
    public byte[] lightColorBlue;
    public short[] tickableBlockIndex = new short[0];
    public short[] decayableLeaves = new short[0];
    public int[] topFaceBitMask; //This increments x, then z, each int goes up in Y value //reading is done by a mask with &, if it returns a non zero value it is true
    public int[] bottomFaceBitMask; //writing is done by using a mask with ^ to flip that specific bit, keeping in mind to only update when a state change occurs
    public int[] northFaceBitMask; //This increments z, then y, each int goes up in X value
    public int[] southFaceBitMask;
    public int[] eastFaceBitMask; //This increments x, then y, each int goes up in Z value
    public int[] westFaceBitMask;
    public int[] excludeTopFace;
    public int[] excludeBottomFace;
    public int[] excludeNorthFace;
    public int[] excludeSouthFace;
    public int[] excludeEastFace;
    public int[] excludeWestFace;
    public int elementOffsetOpaque;
    public int elementOffsetTransparent;
    public Vector3f chunkOffset = new Vector3f();
    public ShouldFaceRenderSorter sorter = new ShouldFaceRenderSorter();
    public ArrayList<Entity> entities = new ArrayList<>();
    public FloatBuffer vertexBufferOpaque;
    public IntBuffer elementBufferOpaque;
    public FloatBuffer vertexBufferTransparent;
    public IntBuffer elementBufferTransparent;
    public ConcurrentHashMap<Integer, MultiStateWrapper> blockStates = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Long, ConcurrentHashMap<Integer, TimeUpdateEventSafe>>  updateEvents = new ConcurrentHashMap<>();
    public boolean updateImmediately;
    public int opaqueVBOID = -10;
    public int opaqueVAOID = -10;
    public int opaqueEBOID = -10;
    public int transparentVBOID = -10;
    public int transparentVAOID = -10;
    public int transparentEBOID = -10;
    public int opaqueIndexCount = 0;
    public int transparentIndexCount = 0;
    public int opaqueVertexCount = 0;
    public int transparentVertexCount = 0;
    public int opaqueMaxIndex = 0;
    public int transparentMaxIndex = 0;
    public boolean opaqueReady = false;
    public boolean transparentReady = false;
    public static final int positionsSize = 1;
    public static final int colorSize = 1;
    public static final int texIndexSize = 1;
    public static final int texCoordsSize = 1;
    public static final int normalSize = 2;
    public static final int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize + normalSize) * Float.BYTES;

    public Chunk(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkMinX = this.x << 5;
        this.chunkMinY = this.y << 5;
        this.chunkMinZ = this.z << 5;
        this.chunkMaxX = this.chunkMinX + 31;
        this.chunkMaxY = this.chunkMinY + 31;
        this.chunkMaxZ = this.chunkMinZ + 31;
        this.parentWorld = world;
    }

    public boolean isBlockInCallingChunkExcludeEdge(int x, int y, int z){
        return x > this.chunkMinX && x < this.chunkMaxX && y > this.chunkMinY && y < this.chunkMaxY && z > this.chunkMinZ && z < this.chunkMaxZ;
    }

    protected boolean isBlockInCallingChunk(int x, int y, int z){
        return x >= this.chunkMinX && x <= this.chunkMaxX && y >= this.chunkMinY && y <= this.chunkMaxY && z >= this.chunkMinZ && z <= this.chunkMaxZ;
    }

    public void setBlock(int x, int y, int z, short blockID) {
        x = (int) MathUtil.positiveMod(x, 32);
        y = (int) MathUtil.positiveMod(y, 32);
        z = (int) MathUtil.positiveMod(z, 32);
        this.blockPallette.setBlockID(getBlockIndexFromCoordinates(x,y,z), blockID);
        this.modifiedSinceLastSave = true;
    }

    public void setBlock(int index, short blockID) {
        this.blockPallette.setBlockID(index, blockID);
        this.modifiedSinceLastSave = true;
    }

    public void setBlockAndNotify(int x, int y, int z, short blockID) {
        x = (int) MathUtil.positiveMod(x, 32);
        y = (int) MathUtil.positiveMod(y, 32);
        z = (int) MathUtil.positiveMod(z, 32);
        this.blockPallette.setBlockID(getBlockIndexFromCoordinates(x,y,z), blockID);
        this.markDirty();
        this.modifiedSinceLastSave = true;
    }


    public void notifyBlock(int x, int y, int z) {
        int x1 = x + (this.x << 5);
        int y1 = y + (this.y << 5);
        int z1 = z + (this.z << 5);
        x = (int) MathUtil.positiveMod(x, 32);
        y = (int) MathUtil.positiveMod(y, 32);
        z = (int) MathUtil.positiveMod(z, 32);
        int mask;
        int topFaceBitmask = this.topFaceBitMask[calculateBitMaskIndex(x, z)];
        int bottomFaceBitMask = this.bottomFaceBitMask[calculateBitMaskIndex(x, z)];
        int northFaceBitMask = this.northFaceBitMask[calculateBitMaskIndex(z, y)];
        int southFaceBitMask = this.southFaceBitMask[calculateBitMaskIndex(z, y)];
        int eastFaceBitMask = this.eastFaceBitMask[calculateBitMaskIndex(x, y)];
        int westFaceBitMask = this.westFaceBitMask[calculateBitMaskIndex(x, y)];
        int blockIndex = getBlockIndexFromCoordinates(x, y, z);

        mask = this.createMask(y);
        if (this.shouldTopFaceRender(blockIndex,x1,y1,z1)) {
            if (this.checkBitValue(topFaceBitmask, mask) == 0) {
                topFaceBitmask = topFaceBitmask ^ mask;
            }
        } else {
            if (this.checkBitValue(topFaceBitmask, mask) != 0) {
                topFaceBitmask = topFaceBitmask ^ mask;
            }
        }

        if (this.shouldBottomFaceRender(blockIndex,x1,y1,z1)) {
            if (this.checkBitValue(bottomFaceBitMask, mask) == 0) {
                bottomFaceBitMask = bottomFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(bottomFaceBitMask, mask) != 0) {
                bottomFaceBitMask = bottomFaceBitMask ^ mask;
            }
        }


        mask = this.createMask(x);
        if (this.shouldNorthFaceRender(blockIndex,x1,y1,z1)) {
            if (this.checkBitValue(northFaceBitMask, mask) == 0) {
                northFaceBitMask = northFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(northFaceBitMask, mask) != 0) {
                northFaceBitMask = northFaceBitMask ^ mask;
            }
        }

        if (this.shouldSouthFaceRender(blockIndex,x1,y1,z1)) {
            if (this.checkBitValue(southFaceBitMask, mask) == 0) {
                southFaceBitMask = southFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(southFaceBitMask, mask) != 0) {
                southFaceBitMask = southFaceBitMask ^ mask;
            }
        }


        mask = this.createMask(z);
        if (this.shouldEastFaceRender(blockIndex,x1,y1,z1)) {
            if (this.checkBitValue(eastFaceBitMask, mask) == 0) {
                eastFaceBitMask = eastFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(eastFaceBitMask, mask) != 0) {
                eastFaceBitMask = eastFaceBitMask ^ mask;
            }
        }

        if (this.shouldWestFaceRender(blockIndex,x1,y1,z1)) {
            if (this.checkBitValue(westFaceBitMask, mask) == 0) {
                westFaceBitMask = westFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(westFaceBitMask, mask) != 0) {
                westFaceBitMask = westFaceBitMask ^ mask;
            }
        }

        this.topFaceBitMask[calculateBitMaskIndex(x, z)] = topFaceBitmask;
        this.bottomFaceBitMask[calculateBitMaskIndex(x, z)] = bottomFaceBitMask;
        this.northFaceBitMask[calculateBitMaskIndex(z, y)] = northFaceBitMask;
        this.southFaceBitMask[calculateBitMaskIndex(z, y)] = southFaceBitMask;
        this.eastFaceBitMask[calculateBitMaskIndex(x, y)] = eastFaceBitMask;
        this.westFaceBitMask[calculateBitMaskIndex(x, y)] = westFaceBitMask;
    }

    public boolean shouldFaceRender(int x, int y, int z, int faceType) {
        int x1 = x + (this.x << 5);
        int y1 = y + (this.y << 5);
        int z1 = z + (this.z << 5);
        x = (int) MathUtil.positiveMod(x, 32);
        y = (int) MathUtil.positiveMod(y, 32);
        z = (int) MathUtil.positiveMod(z, 32);
        int blockIndex = getBlockIndexFromCoordinates(x, y, z);

        switch (faceType){
            case Block.FACE_UP -> {
                return this.shouldTopFaceRender(blockIndex, x1, y1, z1);
            }
            case Block.FACE_DOWN -> {
                return this.shouldBottomFaceRender(blockIndex, x1, y1, z1);
            }
            case Block.FACE_NORTH -> {
                return this.shouldNorthFaceRender(blockIndex, x1, y1, z1);
            }
            case Block.FACE_SOUTH -> {
                return this.shouldSouthFaceRender(blockIndex, x1, y1, z1);
            }
            case Block.FACE_EAST -> {
                return this.shouldEastFaceRender(blockIndex, x1, y1, z1);
            }
            case Block.FACE_WEST -> {
                return this.shouldWestFaceRender(blockIndex, x1, y1, z1);
            }
            default -> {
                return true;
            }
        }
    }



    public static int calculateBitMaskIndex(int firstIncrement, int secondIncrement) {
        firstIncrement %= 32;
        secondIncrement %= 32;
        if (firstIncrement < 0) {
            firstIncrement += 32;
        }
        if (secondIncrement < 0) {
            secondIncrement += 32;
        }
        return firstIncrement + (secondIncrement << 5);
    }

    public int checkBitValue(int value, int mask) {
        return value & mask;
    }

    public int calculateIndexInTopOrBottomFaceBitMasks(int positionInBitMaskArray, int bit){
        return positionInBitMaskArray + (bit << 10);
    }

    public int calculateIndexInNorthOrSouthFaceBitMasks(int positionInBitMaskArray, int bit){
        return (positionInBitMaskArray << 5) + bit;
    }

    public int calculateIndexInEastOrWestFaceBitMasks(int positionInBitMaskArray, int bit){
        return ((positionInBitMaskArray & 31) + (( positionInBitMaskArray >> 5) << 10) + (bit << 5));
    }

    public int createMask(int bitToCheck) {
        bitToCheck %= 32;
        while (bitToCheck < 0) {
            bitToCheck += 32;
        }
        return 0b1 << bitToCheck;
    }


    public static int getBlockIndexFromCoordinates(int x, int y, int z) {
        return (int) ((MathUtil.positiveMod(x, 32)) + (((int)MathUtil.positiveMod(y, 32)) << 10) + (((int)MathUtil.positiveMod(z, 32)) << 5));
    }

    public short getBlockID(int x, int y, int z){
        return this.blockPallette.getBlockID(getBlockIndexFromCoordinates(x,y,z));
    }

    public short getBlockID(int index){
        return this.blockPallette.getBlockID(index);
    }


    public  byte getBlockLightValue(int x, int y, int z) {
        return (byte) (this.light[getBlockIndexFromCoordinates(x, y, z)] & 15);
    }

    public byte getSkyLightValue(int x, int y, int z){
        return (byte) ((this.light[getBlockIndexFromCoordinates(x,y,z)] >> 4) & 15);
    }


    //The value in colorBlue needs to have 256 added to it to get it back to the range of 0-255
    public int getBlockLightColor(int x, int y, int z) {
        int index = getBlockIndexFromCoordinates(x,y,z);
        if (this.lightColorRedAndGreen == null || this.lightColorBlue == null) return 16777215;

        int rg = lightColorRedAndGreen[index];     // red (high byte), green (low byte)
        int b  = lightColorBlue[index] & 0xFF;     // blue channel, unsigned

        return (rg << 8) | b;
    }


    public void setSkyLight() {

        // 0. Clear skylight (upper nibble)
        for (int i = 0; i < this.light.length; i++) {
            this.light[i] = (byte)(this.light[i] & 0x0F);
        }

        // 1. Top-down seeding
        int x, y, z, index;
        byte currentSky;

        for (x = this.chunkMinX; x <= this.chunkMaxX; x++) {
            for (z = this.chunkMinZ; z <= this.chunkMaxZ; z++) {
                if(!this.parentWorld.doesBlockHaveSkyAccess(x, chunkMaxY, z))continue;

                currentSky = 15;

                for (y = this.chunkMaxY; y >= this.chunkMinY; y--) {

                    index = getBlockIndexFromCoordinates(x, y, z);

                    if (Block.list[this.getBlockID(index)].isSolid)
                        break;

                    if (Block.list[this.getBlockID(index)] instanceof BlockWater)
                        currentSky--;

                    if (currentSky <= 0)
                        break;

                    // write skylight (upper nibble), preserve blocklight (lower nibble)
                    this.light[index] = (byte)((this.light[index] & 0x0F) | (currentSky << 4));
                    this.setBlockLightColor(x,y,z, this.parentWorld.skyLightColor);
                }
            }
        }

        // 2. BFS propagation (bright diffusion)
        ArrayDeque<int[]> queue = new ArrayDeque<>();

        // enqueue all blocks with skylight 15
        for (int i = 0; i < this.light.length; i++) {
            if (((this.light[i] >> 4) & 15) == 15)
                queue.add(this.getBlockCoordinatesFromIndex(i));
        }

        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            x = p[0];
            y = p[1];
            z = p[2];

            byte current = (byte)((this.light[getBlockIndexFromCoordinates(x, y, z)] >> 4) & 15);
            byte newValue = (byte)(current - 1);
            if (newValue <= 0) continue;

            int[][] neighbors = {
                    {x - 1, y, z},
                    {x + 1, y, z},
                    {x, y - 1, z},
                    {x, y + 1, z},
                    {x, y, z - 1},
                    {x, y, z + 1}
            };

            for (int[] n : neighbors) {
                int nx = n[0], ny = n[1], nz = n[2];

                if (Block.list[this.parentWorld.getBlockID(nx, ny, nz)].isSolid)
                    continue;

                // chunk boundary check
                if (nx < this.chunkMinX || nx > this.chunkMaxX ||
                        ny < this.chunkMinY || ny > this.chunkMaxY ||
                        nz < this.chunkMinZ || nz > this.chunkMaxZ)
                    continue;

                index = getBlockIndexFromCoordinates(nx, ny, nz);
                byte neighborSky = (byte)((this.light[index] >> 4) & 15);


                if (newValue > neighborSky) {
                    this.light[index] = (byte)((this.light[index] & 0x0F) | (newValue << 4));
                    this.setBlockLightColor(nx, ny, nz, this.parentWorld.skyLightColor);
                    queue.add(new int[]{nx, ny, nz});
                }
            }
        }
    }






    public void setBlockLightValue(int x, int y, int z, byte lightLevel) {
        this.light[getBlockIndexFromCoordinates(x,y,z)] = (byte) ((this.light[getBlockIndexFromCoordinates(x,y,z)] >> 4 & 15) << 4 | lightLevel);
    }

    public void setBlockSkyLightValue(int x, int y, int z, byte lightLevel){
        this.light[getBlockIndexFromCoordinates(x,y,z)] = (byte) (lightLevel << 4 | this.light[getBlockIndexFromCoordinates(x,y,z)] & 15);
    }


    //Both sky and block light color can never have any component that is 0, things will break
    public void setBlockLightColor(int x, int y, int z, int lightColor) {
        if(this.lightColorRedAndGreen == null || this.lightColorBlue == null){
            this.lightColorRedAndGreen = new short[NUMBER_OF_BLOCKS];
            this.lightColorBlue = new byte[NUMBER_OF_BLOCKS];

            for(int i = 0; i < NUMBER_OF_BLOCKS; i++){
                if(this.parentWorld.doesBlockHaveSkyAccess(this.getBlockXFromIndex(i), this.getBlockYFromIndex(i), this.getBlockZFromIndex(i))){
                    this.lightColorRedAndGreen[i] = (short) (((this.parentWorld.skyLightColor >> 8) & 65535));
                    this.lightColorBlue[i] = (byte) ((this.parentWorld.skyLightColor) & 255);
                }
            }
        }


        int index = getBlockIndexFromCoordinates(x,y,z);
        this.lightColorRedAndGreen[index] = (short) ((lightColor >> 8) & 65535);
        this.lightColorBlue[index] = (byte) (lightColor & 255);
        float[] color = this.getBlendedLightColor(x, y, z);
        this.lightColorRedAndGreen[index] = (short) (MathUtil.floatToIntRGBA(color[0]) << 8 | MathUtil.floatToIntRGBA(color[1]));
        this.lightColorBlue[index] = (byte) MathUtil.floatToIntRGBA(color[2]);
    }

    public void clearBlockLightColor(int x, int y, int z) {
        int index = getBlockIndexFromCoordinates(x,y,z);
        if(this.lightColorRedAndGreen == null || this.lightColorBlue == null){
            this.lightColorRedAndGreen = new short[NUMBER_OF_BLOCKS];
            this.lightColorBlue = new byte[NUMBER_OF_BLOCKS];

            for(int i = 0; i < NUMBER_OF_BLOCKS; i++){
                if(this.parentWorld.doesBlockHaveSkyAccess(this.getBlockXFromIndex(i), this.getBlockYFromIndex(i), this.getBlockZFromIndex(i))){
                    this.lightColorRedAndGreen[i] = (short) (((this.parentWorld.skyLightColor >> 8) & 65535));
                    this.lightColorBlue[i] = (byte) ((this.parentWorld.skyLightColor) & 255);
                }
            }
        }


        if (this.parentWorld.doesBlockHaveSkyAccess(x, y, z)) {
            this.lightColorRedAndGreen[index] = (short) (((this.parentWorld.skyLightColor >> 8) & 65535));
            this.lightColorBlue[index] = (byte) ((this.parentWorld.skyLightColor) & 255);
        } else {
            this.lightColorRedAndGreen[index] = 0;
            this.lightColorBlue[index] = 0;
        }
    }

    private float[] getBlendedLightColor(int x, int y, int z) {
        if (Block.list[this.getBlockID(x,y,z)].isLightBlock(x,y,z, this.parentWorld)) {
            Color color = new Color(this.getBlockLightColor(x, y, z));
            return new float[]{color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F};
        } else if (Block.list[this.getBlockID(x,y,z)].isSolid) {
            return new float[3];
        }
        int colorArray = this.parentWorld.getBlockLightColor(x, y, z);
        int colorArray0 = this.parentWorld.getBlockLightColor(x + 1, y, z);
        int colorArray1 = this.parentWorld.getBlockLightColor(x - 1, y, z);
        int colorArray2 = this.parentWorld.getBlockLightColor(x, y, z + 1);
        int colorArray3 = this.parentWorld.getBlockLightColor(x, y, z - 1);
        int colorArray4 = this.parentWorld.getBlockLightColor(x, y + 1, z);
        int colorArray5 = this.parentWorld.getBlockLightColor(x, y - 1, z);


        int divisor = 0;
        float[] red = new float[7];
        red[0] = MathUtil.intToFloatRGBA(colorArray >> 16 & 255);
        red[1] = MathUtil.intToFloatRGBA(colorArray0 >> 16 & 255);
        red[2] = MathUtil.intToFloatRGBA(colorArray1 >> 16 & 255);
        red[3] = MathUtil.intToFloatRGBA(colorArray2 >> 16 & 255);
        red[4] = MathUtil.intToFloatRGBA(colorArray3 >> 16 & 255);
        red[5] = MathUtil.intToFloatRGBA(colorArray4 >> 16 & 255);
        red[6] = MathUtil.intToFloatRGBA(colorArray5 >> 16 & 255);
        for (int i = 0; i < red.length; i++) {
            if (red[i] != 0) {
                divisor++;
            }
        }
        float redFinal = 0;
        if (divisor != 0) {
            redFinal = (red[0] + red[1] + red[2] + red[3] + red[4] + red[5] + red[6]) / divisor;
        }

        float[] green = new float[7];
        green[0] = MathUtil.intToFloatRGBA(colorArray >> 8 & 255);
        green[1] = MathUtil.intToFloatRGBA(colorArray0 >> 8 & 255);
        green[2] = MathUtil.intToFloatRGBA(colorArray1 >> 8 & 255);
        green[3] = MathUtil.intToFloatRGBA(colorArray2 >> 8 & 255);
        green[4] = MathUtil.intToFloatRGBA(colorArray3 >> 8 & 255);
        green[5] = MathUtil.intToFloatRGBA(colorArray4 >> 8 & 255);
        green[6] = MathUtil.intToFloatRGBA(colorArray5 >> 8 & 255);
        divisor = 0;
        for (int i = 0; i < green.length; i++) {
            if (green[i] != 0) {
                divisor++;
            }
        }
        float greenFinal = 0;
        if (divisor != 0) {
            greenFinal = (green[0] + green[1] + green[2] + green[3] + green[4] + green[5] + green[6]) / divisor;
        }

        float[] blue = new float[7];
        blue[0] = MathUtil.intToFloatRGBA((colorArray & 255) + 256);
        blue[1] = MathUtil.intToFloatRGBA((colorArray0 & 255) + 256);
        blue[2] = MathUtil.intToFloatRGBA((colorArray1 & 255) + 256);
        blue[3] = MathUtil.intToFloatRGBA((colorArray2 & 255) + 256);
        blue[4] = MathUtil.intToFloatRGBA((colorArray3 & 255) + 256);
        blue[5] = MathUtil.intToFloatRGBA((colorArray4 & 255) + 256);
        blue[6] = MathUtil.intToFloatRGBA((colorArray5 & 255) + 256);
        divisor = 0;
        for (int i = 0; i < blue.length; i++) {
            if (blue[i] != 0) {
                divisor++;
            }
        }
        float blueFinal = 0;
        if (divisor != 0) {
            blueFinal = (blue[0] + blue[1] + blue[2] + blue[3] + blue[4] + blue[5] + blue[6]) / divisor;
        }

        return new float[]{redFinal, greenFinal, blueFinal};
    }

    public int [] getBlockCoordinatesFromIndex(int index){
        return new int[]{this.getBlockXFromIndex(index), this.getBlockYFromIndex(index), this.getBlockZFromIndex(index)};
    }


    public int getBlockXFromIndex(int index) {
        return ((index & 31) + (this.x << 5));
    }

    public int getBlockYFromIndex(int index) {
        return (index >> 10) + (this.y << 5);
    }

    public int getBlockZFromIndex(int index) {
        return ((index & 1023) >> 5) + (this.z << 5);
    }

    public void markDirty() {
        this.needsToUpdate = true;
        this.parentWorld.chunkController.addChunkToRebuildQueue(this);
    }
    public void markToPopulate(){
        this.parentWorld.chunkController.addChunkToPopulationQueue(this);
    }

    public int getLivingEntitiesInChunk(){
        int result = 0;
        for(int i = 0; i < this.entities.size(); i++){
            if(this.entities.get(i) instanceof EntityLiving){
                result++;
            }
        }
        return result;
    }


    public boolean checkIfChunkShouldRender() {
        for(int i = 0; i <  this.topFaceBitMask.length; i++){
            if(this.topFaceBitMask[i] != 0 || this.bottomFaceBitMask[i] != 0 || this.northFaceBitMask[i] != 0 || this.southFaceBitMask[i] != 0 || this.eastFaceBitMask[i] != 0 || this.westFaceBitMask[i] != 0){
                return true;
            }
        }

        return false;
    }

    public boolean shouldTopFaceRender(int index, int x1, int y1, int z1) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.TOP_FACE;

        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        if (index < 31744) {
            firstBlock = this.getBlockID(index);
            secondBlock = this.getBlockID(index + 1024);
            x2 = this.getBlockXFromIndex(index + 1024);
            y2 = this.getBlockYFromIndex(index + 1024);
            z2 = this.getBlockZFromIndex(index + 1024);
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y + 1, this.z);
            if(chunk == null)return false;
            firstBlock = this.getBlockID(index);
            if(chunk.blocks != null) {
                secondBlock = chunk.getBlockID(index - 31744);
                x2 = chunk.getBlockXFromIndex(index - 31744);
                y2 = chunk.getBlockYFromIndex(index - 31744);
                z2 = chunk.getBlockZFromIndex(index - 31744);
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face,x1,y1,z1,x2,y2,z2);
    }



    public boolean shouldBottomFaceRender(int index, int x1, int y1, int z1) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.BOTTOM_FACE;

        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        if (index > 1023) {
            firstBlock = this.getBlockID(index);
            secondBlock = this.getBlockID(index - 1024);
            x2 = this.getBlockXFromIndex(index - 1024);
            y2 = this.getBlockYFromIndex(index - 1024);
            z2 = this.getBlockZFromIndex(index - 1024);
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y - 1, this.z);
            if(chunk == null)return false;
            firstBlock = this.getBlockID(index);
            if(chunk.blocks != null) {
                secondBlock = chunk.getBlockID(index + 31744);
                x2 = chunk.getBlockXFromIndex(index + 31744);
                y2 = chunk.getBlockYFromIndex(index + 31744);
                z2 = chunk.getBlockZFromIndex(index + 31744);
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face,x1,y1,z1,x2,y2,z2);
    }

    private boolean shouldNorthFaceRender(int index, int x1, int y1, int z1) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.NORTH_FACE;

        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        if (index % 32 != 0) {
            firstBlock = this.getBlockID(index);
            secondBlock = this.getBlockID(index - 1);
            x2 = this.getBlockXFromIndex(index - 1);
            y2 = this.getBlockYFromIndex(index - 1);
            z2 = this.getBlockZFromIndex(index - 1);
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x - 1, this.y, this.z);
            if(chunk == null)return false;
            firstBlock = this.getBlockID(index);
            if(chunk.blocks != null) {
                secondBlock = chunk.getBlockID(index + 31);
                x2 = chunk.getBlockXFromIndex(index + 31);
                y2 = chunk.getBlockYFromIndex(index + 31);
                z2 = chunk.getBlockZFromIndex(index + 31);
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face,x1,y1,z1,x2,y2,z2);
    }

    private boolean shouldSouthFaceRender(int index, int x1, int y1, int z1) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.SOUTH_FACE;

        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        if (index % 32 != 31) {
            firstBlock = this.getBlockID(index);
            secondBlock = this.getBlockID(index + 1);
            x2 = this.getBlockXFromIndex(index + 1);
            y2 = this.getBlockYFromIndex(index + 1);
            z2 = this.getBlockZFromIndex(index + 1);
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x + 1, this.y, this.z);
            if(chunk == null)return false;
            firstBlock = this.getBlockID(index);
            if(chunk.blocks != null) {
                secondBlock = chunk.getBlockID(index - 31);
                x2 = chunk.getBlockXFromIndex(index - 31);
                y2 = chunk.getBlockYFromIndex(index - 31);
                z2 = chunk.getBlockZFromIndex(index - 31);
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face,x1,y1,z1,x2,y2,z2);
    }

    private boolean shouldEastFaceRender(int index, int x1, int y1, int z1) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.EAST_FACE;

        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        if ((index % 1024) / 32 != 0) {
            firstBlock = this.getBlockID(index);
            secondBlock = this.getBlockID(index - 32);
            x2 = this.getBlockXFromIndex(index - 32);
            y2 = this.getBlockYFromIndex(index - 32);
            z2 = this.getBlockZFromIndex(index - 32);
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y, this.z - 1);
            if(chunk == null)return false;
            firstBlock = this.getBlockID(index);
            if(chunk.blocks != null) {
                secondBlock = chunk.getBlockID(index + 992);
                x2 = chunk.getBlockXFromIndex(index + 992);
                y2 = chunk.getBlockYFromIndex(index + 992);
                z2 = chunk.getBlockZFromIndex(index + 992);
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face,x1,y1,z1,x2,y2,z2);
    }

    private boolean shouldWestFaceRender(int index, int x1, int y1, int z1) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.WEST_FACE;

        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        if ((index % 1024) / 32 != 31) {
            firstBlock = this.getBlockID(index);
            secondBlock = this.getBlockID(index + 32);
            x2 = this.getBlockXFromIndex(index + 32);
            y2 = this.getBlockYFromIndex(index + 32);
            z2 = this.getBlockZFromIndex(index + 32);
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y, this.z + 1);
            if(chunk == null)return false;
            firstBlock = this.getBlockID(index);
            if(chunk.blocks != null) {
                secondBlock = chunk.getBlockID(index - 992);
                x2 = chunk.getBlockXFromIndex(index - 992);
                y2 = chunk.getBlockYFromIndex(index - 992);
                z2 = chunk.getBlockZFromIndex(index - 992);
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face,x1,y1,z1,x2,y2,z2);
    }

    public int calculateFaceNumber() {
        int result = 0;
        int bitMap = 0;
        for(int i = 0; i <  this.topFaceBitMask.length; i++){
            bitMap = this.topFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    result++;
                }
            }
        }

        for(int i = 0; i <  this.bottomFaceBitMask.length; i++){
            bitMap = this.bottomFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    result++;
                }
            }
        }

        for(int i = 0; i <  this.northFaceBitMask.length; i++){
            bitMap = this.northFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    result++;
                }
            }
        }

        for(int i = 0; i <  this.southFaceBitMask.length; i++){
            bitMap = this.southFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    result++;
                }
            }
        }

        for(int i = 0; i <  this.eastFaceBitMask.length; i++){
            bitMap = this.eastFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    result++;
                }
            }
        }

        for(int i = 0; i <  this.westFaceBitMask.length; i++){
            bitMap = this.westFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    result++;
                }
            }
        }


        return result;
    }

    public boolean containsAir(){
        for(int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++){
            if(this.getBlockID(i) == Block.air.ID){
                return true;
            }
        }
        return false;
    }

    public boolean containsWater(){
        for(int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++){
            if(Block.list[this.getBlockID(i)] instanceof BlockWater){
                return true;
            }
        }
        return false;
    }


    public void notifyAllBlocks() {
        int x = 0;
        int y = 0;
        int z = 0;

        for (int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++) {
            x = this.getBlockXFromIndex(i);
            y = this.getBlockYFromIndex(i);
            z = this.getBlockZFromIndex(i);
            this.notifyBlock(x, y, z);
        }
    }


    public void floodFillBlockLightArray() {
        int x = 0;
        int y = 0;
        int z = 0;

        ChunkColumnSkylightMap skylightMap = this.parentWorld.findChunkSkyLightMap(this.x >> 5, this.z >> 5);

        for (int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++) {
            x = this.getBlockXFromIndex(i);
            y = this.getBlockYFromIndex(i);
            z = this.getBlockZFromIndex(i);

            if(Block.list[this.getBlockID(i)].isSolid){
                this.light[i] = 0;
            }

            if(this.getBlockID(i) != Block.air.ID){
                if(skylightMap.isHeightGreater(x,y,z)){
                    skylightMap.updateLightMap(x,y,z);
                }
            }


            if(Block.list[this.getBlockID(i)].isLightBlock(x,y,z, this.parentWorld)){
                this.parentWorld.propagateLightSource(x,y,z, Block.list[this.getBlockID(i)].lightBlockValue);
            }
        }
    }






    public void createGLObjects(){
        this.opaqueVAOID = GL46.glGenVertexArrays();
        this.transparentVAOID = GL46.glGenVertexArrays();
        this.opaqueVBOID = GL46.glGenBuffers();
        this.transparentVBOID = GL46.glGenBuffers();
        this.opaqueEBOID = GL46.glGenBuffers();
        this.transparentEBOID = GL46.glGenBuffers();
    }

    public void deleteGLObjects() {
        if(GL46.glIsVertexArray(this.opaqueVAOID)){
            CosmicEvolution.instance.renderEngine.deleteVAO(this.opaqueVAOID);
        }

        if(GL46.glIsVertexArray(this.transparentVAOID)){
            CosmicEvolution.instance.renderEngine.deleteVAO(this.transparentVAOID);
        }

        if(GL46.glIsBuffer(this.opaqueVBOID)){
            CosmicEvolution.instance.renderEngine.deleteBuffers(this.opaqueVBOID);
        }

        if(GL46.glIsBuffer(this.transparentVBOID)){
            CosmicEvolution.instance.renderEngine.deleteBuffers(this.transparentVBOID);
        }

        if(GL46.glIsBuffer(this.opaqueEBOID)){
            CosmicEvolution.instance.renderEngine.deleteBuffers(this.opaqueEBOID);
        }

        if(GL46.glIsBuffer(this.transparentEBOID)){
            CosmicEvolution.instance.renderEngine.deleteBuffers(this.transparentEBOID);
        }

    }

    public void bindRenderData() {
        // 1. Create VAOs/VBOs/EBOs and set attribute layout
        if (this.opaqueVAOID == -10 || this.opaqueVBOID == -10 || this.opaqueEBOID == -10
                || this.transparentVAOID == -10 || this.transparentVBOID == -10 || this.transparentEBOID == -10) {

            this.createGLObjects();

            // Opaque VAO
            GL46.glBindVertexArray(this.opaqueVAOID);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.opaqueVBOID);

            GL46.glVertexAttribPointer(0, Chunk.positionsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, 0);
            GL46.glEnableVertexAttribArray(0);

            GL46.glVertexAttribPointer(1, Chunk.colorSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    Chunk.positionsSize * Float.BYTES);
            GL46.glEnableVertexAttribArray(1);

            GL46.glVertexAttribPointer(2, Chunk.texCoordsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    (Chunk.positionsSize + Chunk.colorSize) * Float.BYTES);
            GL46.glEnableVertexAttribArray(2);

            GL46.glVertexAttribPointer(3, Chunk.texIndexSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize) * Float.BYTES);
            GL46.glEnableVertexAttribArray(3);

            GL46.glVertexAttribPointer(4, Chunk.normalSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize + Chunk.texIndexSize) * Float.BYTES);
            GL46.glEnableVertexAttribArray(4);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.opaqueEBOID);
            GL46.glBindVertexArray(0);

            // Transparent VAO
            GL46.glBindVertexArray(this.transparentVAOID);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.transparentVBOID);

            GL46.glVertexAttribPointer(0, Chunk.positionsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, 0);
            GL46.glEnableVertexAttribArray(0);

            GL46.glVertexAttribPointer(1, Chunk.colorSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    Chunk.positionsSize * Float.BYTES);
            GL46.glEnableVertexAttribArray(1);

            GL46.glVertexAttribPointer(2, Chunk.texCoordsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    (Chunk.positionsSize + Chunk.colorSize) * Float.BYTES);
            GL46.glEnableVertexAttribArray(2);

            GL46.glVertexAttribPointer(3, Chunk.texIndexSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize) * Float.BYTES);
            GL46.glEnableVertexAttribArray(3);

            GL46.glVertexAttribPointer(4, Chunk.normalSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes,
                    (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize + Chunk.texIndexSize) * Float.BYTES);
            GL46.glEnableVertexAttribArray(4);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.transparentEBOID);
            GL46.glBindVertexArray(0);
        }


        // 3. Allocate and copy OPAQUE final buffers
        if (this.vertexBufferOpaque != null) {
            this.vertexBufferOpaque.flip();
        }

        if (this.elementBufferOpaque != null) {
            this.elementBufferOpaque.flip();
        }

        // 4. Allocate and copy TRANSPARENT final buffers
        if (this.vertexBufferTransparent != null) {
            this.vertexBufferTransparent.flip();
        }

        if (this.elementBufferTransparent != null) {
            this.elementBufferTransparent.flip();
        }

        // 5. Clear temp buffers

        // 6. Null out truly empty final buffers
        if (this.vertexBufferOpaque != null && this.vertexBufferOpaque.limit() == 0) {
            this.vertexBufferOpaque = null;
        }
        if (this.elementBufferOpaque != null && this.elementBufferOpaque.limit() == 0) {
            this.elementBufferOpaque = null;
        }
        if (this.vertexBufferTransparent != null && this.vertexBufferTransparent.limit() == 0) {
            this.vertexBufferTransparent = null;
        }
        if (this.elementBufferTransparent != null && this.elementBufferTransparent.limit() == 0) {
            this.elementBufferTransparent = null;
        }

        // 7. Upload to GL + record counts/maxIndex
        // OPAQUE
        if (this.vertexBufferOpaque != null) {
            GL46.glBindVertexArray(this.opaqueVAOID);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.opaqueVBOID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBufferOpaque, GL46.GL_STATIC_DRAW);

            this.opaqueVertexCount = this.vertexBufferOpaque.limit() / 6; // assuming 6 components per vertex
        }

        if (this.elementBufferOpaque != null) {
            GL46.glBindVertexArray(this.opaqueVAOID);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.opaqueEBOID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBufferOpaque, GL46.GL_STATIC_DRAW);

            this.opaqueIndexCount = this.elementBufferOpaque.limit();

            int max = 0;
            for (int i = 0; i < this.elementBufferOpaque.limit(); i++) {
                max = Math.max(max, this.elementBufferOpaque.get(i));
            }
            this.opaqueMaxIndex = max;
            this.opaqueReady = true;
        }

        // TRANSPARENT
        if (this.vertexBufferTransparent != null) {
            GL46.glBindVertexArray(this.transparentVAOID);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.transparentVBOID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBufferTransparent, GL46.GL_STATIC_DRAW);

            this.transparentVertexCount = this.vertexBufferTransparent.limit() / 6; // same layout
        }

        if (this.elementBufferTransparent != null) {
            GL46.glBindVertexArray(this.transparentVAOID);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.transparentEBOID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBufferTransparent, GL46.GL_STATIC_DRAW);

            this.transparentIndexCount = this.elementBufferTransparent.limit();

            int maxT = 0;
            for (int i = 0; i < this.elementBufferTransparent.limit(); i++) {
                maxT = Math.max(maxT, this.elementBufferTransparent.get(i));
            }
            this.transparentMaxIndex = maxT;
            this.transparentReady = true;
        }

        // Optionally: drop CPU buffers now to avoid any accidental use
         this.vertexBufferOpaque = null;
         this.elementBufferOpaque = null;
         this.vertexBufferTransparent = null;
         this.elementBufferTransparent = null;

        GL46.glBindVertexArray(0);

        this.isUpdating = false;
        this.parentWorld.chunkController.renderWorldScene.recalculateQueries = true;
    }





    public void renderOpaque(int sunX, int sunY, int sunZ) {
        // GPU-side readiness check
        if (!opaqueReady) return;
        if (opaqueIndexCount == 0 || opaqueVertexCount == 0) return;

        // Safety: ensure indices reference valid vertices
        if (opaqueMaxIndex >= opaqueVertexCount) return;

        // Upload uniforms
        Shader.terrainShader.uploadVec3f("chunkOffset", this.chunkOffset);
        Shader.terrainShader.uploadVec3f("sunChunkOffset", new Vector3f(
                (this.x - sunX) << 5,
                (this.y - sunY) << 5,
                (this.z - sunZ) << 5
        ));

        // Bind VAO and draw
        GL46.glBindVertexArray(this.opaqueVAOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.opaqueIndexCount, GL46.GL_UNSIGNED_INT, 0);
    }


    public void renderTransparent(int sunX, int sunY, int sunZ) {
        if (!transparentReady) return;
        if (transparentIndexCount == 0 || transparentVertexCount == 0) return;
        if (transparentMaxIndex >= transparentVertexCount) return;

        Shader.terrainShader.uploadVec3f("chunkOffset", this.chunkOffset);
        Shader.terrainShader.uploadVec3f("sunChunkOffset",
                new Vector3f((this.x - sunX) << 5, (this.y - sunY) << 5, (this.z - sunZ) << 5));

        GL46.glBindVertexArray(this.transparentVAOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.transparentIndexCount, GL46.GL_UNSIGNED_INT, 0);
    }


    public void renderShadowMap(int sunX, int sunY, int sunZ) {

        // ----- OPAQUE PASS -----
        if (!opaqueReady) return;
        if (opaqueIndexCount == 0 || opaqueVertexCount == 0) return;
        if (opaqueMaxIndex >= opaqueVertexCount) return;

        Shader.shadowMapShaderTerrain.uploadVec3f(
                "chunkOffset",
                new Vector3f((this.x - sunX) << 5,
                        (this.y - sunY) << 5,
                        (this.z - sunZ) << 5)
        );

        GL46.glBindVertexArray(this.opaqueVAOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.opaqueIndexCount, GL46.GL_UNSIGNED_INT, 0);


        // ----- TRANSPARENT PASS -----
        if (!transparentReady) return;
        if (transparentIndexCount == 0 || transparentVertexCount == 0) return;
        if (transparentMaxIndex >= transparentVertexCount) return;

        GL46.glBindVertexArray(this.transparentVAOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.transparentIndexCount, GL46.GL_UNSIGNED_INT, 0);


        // ----- ENTITY PASS -----
        Entity entity;
        for (int i = 0; i < this.entities.size(); i++) {
            entity = this.entities.get(i);

            float dist = (float) MathUtil.distance3D(
                    entity.x, entity.y, entity.z,
                    CosmicEvolution.instance.save.thePlayer.x,
                    CosmicEvolution.instance.save.thePlayer.y,
                    CosmicEvolution.instance.save.thePlayer.z
            );

            if (entity instanceof EntityParticle) {
                if (dist <= 32) entity.renderForShadowMap(sunX, sunY, sunZ);
            } else {
                if (dist <= 128) entity.renderForShadowMap(sunX, sunY, sunZ);
            }
        }
    }




    private float getLightValueFromMap(byte lightValue) {
        return switch (lightValue) {
            case 0, 1 -> 0.1F;
            case 2 -> 0.11F;
            case 3 -> 0.13F;
            case 4 -> 0.16F;
            case 5 -> 0.2F;
            case 6 -> 0.24F;
            case 7 -> 0.29F;
            case 8 -> 0.35F;
            case 9 -> 0.42F;
            case 10 -> 0.5F;
            case 11 -> 0.58F;
            case 12 -> 0.67F;
            case 13 -> 0.77F;
            case 14 -> 0.88F;
            case 15 -> 1.0F;
            default -> 0.1F;
        };
    }

    public boolean chunkIsEmpty() {
        for (int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++) {
            if (this.getBlockID(i) != Block.air.ID) {
                return false;
            }
        }
        return true;
    }


    public int distanceFromPlayer(int playerChunkX, int playerChunkY, int playerChunkZ) {
        int[] distance = new int[3];
        distance[0] = playerChunkX - this.x;
        distance[1] = playerChunkY - this.y;
        distance[2] = playerChunkZ - this.z;
        if (distance[0] < 0) {
            distance[0] *= -1;
        }
        if (distance[1] < 0) {
            distance[1] *= -1;
        }
        if (distance[2] < 0) {
            distance[2] *= -1;
        }

        Arrays.sort(distance);
        return distance[1];
    }



    public void addEntityToList(Entity entity){
        for(int i = 0; i < this.entities.size(); i++){
            if(entity.equals(this.entities.get(i))){
                return;
            }
        }
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity){
        this.entities.remove(entity);
        this.entities.trimToSize();
    }

    public void checkIfEntitiesAreStillInChunk(){
        int chunkX;
        int chunkY;
        int chunkZ;
        Entity entity;
        for(int i = 0; i < this.entities.size(); i++){
            entity = this.entities.get(i);
            chunkX = MathUtil.floorDouble(entity.x) >> 5;
            chunkY = MathUtil.floorDouble(entity.y) >> 5;
            chunkZ = MathUtil.floorDouble(entity.z) >> 5;
            if(chunkX != this.x || chunkY != this.y || chunkZ != this.z){
                Chunk chunk = this.parentWorld.chunkController.findChunkFromChunkCoordinates(chunkX, chunkY, chunkZ);
                if(chunk == null)continue;
                chunk.addEntityToList(entity);
                this.removeEntity(entity);
            }
        }
    }

    public void renderEntities(int sunX, int sunY, int sunZ){
        Shader.worldShader2DTexture.uploadVec3f("sunChunkOffset", new Vector3f((this.x - sunX) << 5, (this.y - sunY) << 5, (this.z - sunZ) << 5));
        Shader.worldShaderTextureArray.uploadVec3f("sunChunkOffset", new Vector3f((this.x - sunX) << 5, (this.y - sunY) << 5, (this.z - sunZ) << 5));
        Entity entity;
        for(int i = 0; i < this.entities.size(); i++){
            entity = this.entities.get(i);
            if(entity instanceof EntityParticle){
                if(MathUtil.distance3D(entity.x, entity.y, entity.z, CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z) <= 32){
                    entity.render();
                }
            } else {
                if(MathUtil.distance3D(entity.x, entity.y, entity.z, CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z) <= 128){
                    entity.render();
                }
            }
        }
    }

    public boolean doesChunkContainEntities(){
        return this.entities.size() != 0;
    }

    public void tickEntities(){
        Entity entity;
        for(int i = 0; i < this.entities.size(); i++){
            entity = this.entities.get(i);
            if(entity != null){
                if(entity instanceof IDecayable && entity instanceof EntityLiving){
                    if(((EntityLiving) entity).isDead) {
                        if (((EntityLiving) entity).timeDied + ((IDecayable) entity).getDecayTime() <= CosmicEvolution.instance.save.time) {
                            ((IDecayable) entity).destroyOnDecay();
                        }
                    }
                }

                entity.tick();
                if(entity.despawn){
                    this.removeEntity(entity);
                }
            }
        }
    }

    public void tick() {
        if (CosmicEvolution.instance.save.time % 60 == 0) {
            if (this.blocks != null && this.tickableBlockIndex != null) {
                    for (int i = 0; i < this.tickableBlockIndex.length; i++) {
                        if (Block.list[this.getBlockID(this.tickableBlockIndex[i])] instanceof ITickable) {
                            ((ITickable) Block.list[this.getBlockID(this.tickableBlockIndex[i])]).tick(this.getBlockXFromIndex(this.tickableBlockIndex[i]), this.getBlockYFromIndex(this.tickableBlockIndex[i]), this.getBlockZFromIndex(this.tickableBlockIndex[i]), this.parentWorld);
                        }
                    }
            }

            if(this.blocks != null && this.decayableLeaves != null) {
                for (int i = 0; i < this.decayableLeaves.length; i++) {
                    if (Block.list[this.getBlockID(this.decayableLeaves[i])] instanceof BlockLeaf) {
                        ((BlockLeaf) Block.list[this.getBlockID(this.decayableLeaves[i])]).decayLeaf(this.getBlockXFromIndex(this.decayableLeaves[i]), this.getBlockYFromIndex(this.decayableLeaves[i]), this.getBlockZFromIndex(this.decayableLeaves[i]), this.parentWorld);
                    }
                }
            }
        }

        TimeUpdateEventSafe event;
        ConcurrentHashMap<Integer, TimeUpdateEventSafe> outerMap = this.updateEvents.get(this.parentWorld.ce.save.time);
        if(outerMap != null) {
            for (Map.Entry<Integer, TimeUpdateEventSafe> entry1 : outerMap.entrySet()) {
                event = entry1.getValue();
                int x = this.getBlockXFromIndex(event.value.index);
                int y = this.getBlockYFromIndex(event.value.index);
                int z = this.getBlockZFromIndex(event.value.index);
                if (Block.list[this.getBlockID(event.value.index)] instanceof ITimeUpdate) {
                    ((ITimeUpdate) Block.list[this.getBlockID(event.value.index)]).onTimeUpdate(x, y, z, this.parentWorld);
                }
            }
        }

        MultiStateWrapper multiStateWrapper;
        for(Map.Entry<Integer, MultiStateWrapper> entry : this.blockStates.entrySet()){
            multiStateWrapper = entry.getValue();
            multiStateWrapper.value.onTick(this);
        }

    }

    @Override
    public int compareTo(Chunk chunk) {
        return Float.compare(this.distanceFromPlayer, chunk.distanceFromPlayer);
    }




    public void truncateTickableIndexArray(int maxLength, boolean empty){
        if(!empty) {
            short[] newArray = new short[maxLength];
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = this.tickableBlockIndex[i];
            }
            this.tickableBlockIndex = newArray;
        } else {
            this.tickableBlockIndex = new short[0];
        }
    }

    public void addTickableBlockToArray(short index){
        short[] newArray = new short[this.tickableBlockIndex.length + 1];
        for(int i = 0; i < this.tickableBlockIndex.length; i++){
            newArray[i] = this.tickableBlockIndex[i];
        }
        newArray[this.tickableBlockIndex.length] = index;
        this.tickableBlockIndex = newArray;
    }

    public void removeTickableBlockFromArray(short index){
        short[] newArray = new short[this.tickableBlockIndex.length - 1];
        for(int i = 0; i < newArray.length; i++){
            if(this.tickableBlockIndex[i] != index) {
                newArray[i] = this.tickableBlockIndex[i];
            }
        }
        this.tickableBlockIndex = newArray;
    }

    public void addDecayableLeafToArray(short index){
        if(this.decayableLeaves == null){
            this.decayableLeaves = new short[]{index};
            return;
        }


        short[] newArray = new short[this.decayableLeaves.length + 1];
        for(int i = 0; i < this.decayableLeaves.length; i++){
            newArray[i] = this.decayableLeaves[i];
        }
        newArray[this.decayableLeaves.length] = index;
        this.decayableLeaves = newArray;
    }

    public void removeDecayableLeafFromArray(short index){
        short[] newArray = new short[this.decayableLeaves.length - 1];
        for(int i = 0; i < newArray.length; i++){
            if(this.decayableLeaves[i] != index) {
                newArray[i] = this.decayableLeaves[i];
            }
        }
        this.decayableLeaves = newArray;
    }


    public void addTimeUpdateEvent(int x, int y, int z, long updateTime){
        this.addTimeUpdateEvent(getBlockIndexFromCoordinates(x,y,z), updateTime);
    }

    public TimeUpdateEvent getTimeUpdateEvent(int x, int y, int z){
        ConcurrentHashMap<Integer, TimeUpdateEventSafe> updateTimes;
        TimeUpdateEventSafe returnVal;
        for(Map.Entry<Long, ConcurrentHashMap<Integer,  TimeUpdateEventSafe>> entry : updateEvents.entrySet()){
            updateTimes = entry.getValue();
            for(Map.Entry<Integer, TimeUpdateEventSafe> entry1 : updateTimes.entrySet()){
                returnVal = entry1.getValue();
                if(returnVal == null)continue;
                if(returnVal.value.index == getBlockIndexFromCoordinates(x,y,z)){
                    return returnVal.value;
                }
            }
        }
        return null;
    }

    public void addTimeUpdateEvent(int index, long updateTime){
        ConcurrentHashMap<Integer, TimeUpdateEventSafe> updateTimes = this.updateEvents.get(updateTime);
        if(updateTimes == null){
            updateTimes = new ConcurrentHashMap<>();
            this.updateEvents.put(updateTime, updateTimes);
        }

        updateTimes.put(index, new TimeUpdateEventSafe(new TimeUpdateEvent(index, updateTime)));
    }

    public void removeTimeUpdateEvent(int x, int y, int z){
        ConcurrentHashMap<Integer, TimeUpdateEventSafe> updateTimes;
        for(Map.Entry<Long, ConcurrentHashMap<Integer,  TimeUpdateEventSafe>> entry : updateEvents.entrySet()){
            updateTimes = entry.getValue();
            updateTimes.remove(getBlockIndexFromCoordinates(x,y,z));
            break;
        }
    }

    public void updateTimeEvent(int x, int y, int z, long updateTime){
        ConcurrentHashMap<Integer, TimeUpdateEventSafe> updateTimes;
        for(Map.Entry<Long, ConcurrentHashMap<Integer,  TimeUpdateEventSafe>> entry : updateEvents.entrySet()){
            updateTimes = entry.getValue();
            if(updateTimes == null)continue;
            TimeUpdateEventSafe timeUpdateEventSafe = updateTimes.get(getBlockIndexFromCoordinates(x,y,z));
            if(timeUpdateEventSafe == null)continue;
            timeUpdateEventSafe.value.updateTime = updateTime;
            break;
        }
    }

    public void addBlockState(int x, int y, int z, int stateType, BlockState blockState){
        this.addBlockState(getBlockIndexFromCoordinates(x,y,z), stateType, blockState);
    }

    public void addBlockState(int index, int stateType, BlockState blockState){
        MultiStateWrapper multiStateWrapper = this.blockStates.get(index);
        if(multiStateWrapper == null) {
            multiStateWrapper = new MultiStateWrapper(new MultiState(index));
            this.blockStates.put(index, multiStateWrapper);
        }

        multiStateWrapper.value.addBlockState(stateType, blockState);
    }


    public BlockState getBlockState(int x, int y, int z, int stateType){
        return this.getBlockState(getBlockIndexFromCoordinates(x,y,z), stateType);
    }

    public BlockState getBlockState(int index, int stateType){
        MultiStateWrapper multiStateWrapper = this.blockStates.get(index);
        if(multiStateWrapper == null) return null;

        return multiStateWrapper.value.getBlockState(stateType);
    }

    public void removeBlockState(int x, int y, int z, int stateType){
        this.removeBlockState(getBlockIndexFromCoordinates(x,y,z), stateType);
    }

    public void removeBlockState(int index, int stateType){
        MultiStateWrapper multiStateWrapper = this.blockStates.get(index);
        if(multiStateWrapper == null)return;

        multiStateWrapper.value.removeBlockState(stateType);
    }

    public boolean doesChunkContainStateOfType(int stateType){
        for(Map.Entry<Integer, MultiStateWrapper> entry : this.blockStates.entrySet()){
            if(entry.getValue().value.mapContainsKeyOfType(stateType)){
                return true;
            }
        }

        return false;
    }

    public int getBlockStateCount(int stateType){
        int total = 0;
        for(Map.Entry<Integer, MultiStateWrapper> entry : this.blockStates.entrySet()){
            if(entry.getValue().value.mapContainsKeyOfType(stateType)){
                total++;
            }
        }
        return total;
    }


    public BlockState[] getAllBlockStatesOfType(int stateType, int totalCount){
        BlockState[] returnArray = new BlockState[totalCount];
        int index = 0;


        for(Map.Entry<Integer, MultiStateWrapper> entry : this.blockStates.entrySet()){
            if(!entry.getValue().value.mapContainsKeyOfType(stateType))continue;

            returnArray[index] = entry.getValue().value.getBlockState(stateType);
            index++;
        }

        return returnArray;
    }

    public void clearAllBlockStates(int x, int y, int z){
        int key = getBlockIndexFromCoordinates(x,y,z);
        MultiStateWrapper multiStateWrapper = this.blockStates.get(key);
        if(multiStateWrapper == null){
            System.out.println("Unable to clear block states at " + x + " " + y + " " + z + ". States already cleared or no states existed");
            return;
        }


        multiStateWrapper.value.clearAllStates();

        this.blockStates.remove(key);
    }

}


