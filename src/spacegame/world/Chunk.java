package spacegame.world;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.block.ITickable;
import spacegame.block.ITimeUpdate;
import spacegame.core.CosmicEvolution;
import spacegame.core.MathUtil;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityParticle;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.render.RenderBlocks;
import spacegame.render.Shader;
import spacegame.render.ShouldFaceRenderSorter;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class Chunk implements Comparable<Chunk> {
    public World parentWorld;
    public boolean needsToUpdate = false;
    public long updateTime;
    public boolean populated = false;
    public boolean shouldRender;
    public boolean updating;
    public boolean containsWater;
    public boolean containsAir;
    public boolean updateSkylight;
    public boolean firstRender = true;
    public float distanceFromPlayer;
    public boolean occluded;
    public int queryID = -10;
    public final int x;
    public final int y;
    public final int z;
    public short[] blocks = new short[32768];
    public byte[] lighting = new byte[32768];
    public byte[] skyLight = new byte[32768];
    public int[] lightColor = new int[32768];
    public short[] tickableBlockIndex = new short[32768];
    public int[] topFaceBitMask = new int[1024]; //This increments x, then z, each int goes up in Y value //reading is done by a mask with &, if it returns a non zero value it is true
    public int[] bottomFaceBitMask = new int[1024]; //writing is done by using a mask with ^ to flip that specific bit, keeping in mind to only update when a state change occurs
    public int[] northFaceBitMask = new int[1024]; //This increments z, then y, each int goes up in X value
    public int[] southFaceBitMask = new int[1024];
    public int[] eastFaceBitMask = new int[1024]; //This increments x, then y, each int goes up in Z value
    public int[] westFaceBitMask = new int[1024];
    public int[] excludeTopFace;
    public int[] excludeBottomFace;
    public int[] excludeNorthFace;
    public int[] excludeSouthFace;
    public int[] excludeEastFace;
    public int[] excludeWestFace;
    public boolean empty = true;
    public int elementOffsetOpaque;
    public int elementOffsetTransparent;
    public Vector3f chunkOffset = new Vector3f();
    public ShouldFaceRenderSorter sorter = new ShouldFaceRenderSorter();
    public ArrayList<LightColorLocation> lightColorLocations = new ArrayList<>();
    public ArrayList<Entity> entities = new ArrayList<>();
    public FloatBuffer vertexBufferOpaque;
    public IntBuffer elementBufferOpaque;
    public FloatBuffer vertexBufferTransparent;
    public IntBuffer elementBufferTransparent;
    public ArrayList<ChestLocation> chestLocations = new ArrayList<>();
    public ArrayList<TimeUpdateEvent> updateEvents = new ArrayList<>();
    public int opaqueVBOID = -10;
    public int opaqueVAOID = -10;
    public int opaqueEBOID = -10;
    public int transparentVBOID = -10;
    public int transparentVAOID = -10;
    public int transparentEBOID = -10;
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
        this.parentWorld = world;
    }

    public void setBlock(int x, int y, int z, short blockID) {
        x %= 32;
        y %= 32;
        z %= 32;
        this.blocks[getBlockIndexFromCoordinates(x, y, z)] = blockID;
    }

    public void setBlockWithNotify(int x, int y, int z, short blockID) {
        x %= 32;
        y %= 32;
        z %= 32;
        this.blocks[getBlockIndexFromCoordinates(x, y, z)] = blockID;
        this.notifyBlock(x, y, z);
        this.markDirty();
    }


    public void notifyBlock(int x, int y, int z) {
        x %= 32;
        y %= 32;
        z %= 32;
        if (x < 0) {
            x += 32;
        }
        if (y < 0) {
            y += 32;
        }
        if (z < 0) {
            z += 32;
        }
        int mask;
        int topFaceBitmask = this.topFaceBitMask[calculateBitMaskIndex(x, z)];
        int bottomFaceBitMask = this.bottomFaceBitMask[calculateBitMaskIndex(x, z)];
        int northFaceBitMask = this.northFaceBitMask[calculateBitMaskIndex(z, y)];
        int southFaceBitMask = this.southFaceBitMask[calculateBitMaskIndex(z, y)];
        int eastFaceBitMask = this.eastFaceBitMask[calculateBitMaskIndex(x, y)];
        int westFaceBitMask = this.westFaceBitMask[calculateBitMaskIndex(x, y)];
        int blockIndex = getBlockIndexFromCoordinates(x, y, z);

        mask = this.createMask(y);
        if (this.shouldTopFaceRender(blockIndex)) {
            if (this.checkBitValue(topFaceBitmask, mask) == 0) {
                topFaceBitmask = topFaceBitmask ^ mask;
            }
        } else {
            if (this.checkBitValue(topFaceBitmask, mask) != 0) {
                topFaceBitmask = topFaceBitmask ^ mask;
            }
        }

        if (this.shouldBottomFaceRender(blockIndex)) {
            if (this.checkBitValue(bottomFaceBitMask, mask) == 0) {
                bottomFaceBitMask = bottomFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(bottomFaceBitMask, mask) != 0) {
                bottomFaceBitMask = bottomFaceBitMask ^ mask;
            }
        }


        mask = this.createMask(x);
        if (this.shouldNorthFaceRender(blockIndex)) {
            if (this.checkBitValue(northFaceBitMask, mask) == 0) {
                northFaceBitMask = northFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(northFaceBitMask, mask) != 0) {
                northFaceBitMask = northFaceBitMask ^ mask;
            }
        }

        if (this.shouldSouthFaceRender(blockIndex)) {
            if (this.checkBitValue(southFaceBitMask, mask) == 0) {
                southFaceBitMask = southFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(southFaceBitMask, mask) != 0) {
                southFaceBitMask = southFaceBitMask ^ mask;
            }
        }


        mask = this.createMask(z);
        if (this.shouldEastFaceRender(blockIndex)) {
            if (this.checkBitValue(eastFaceBitMask, mask) == 0) {
                eastFaceBitMask = eastFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(eastFaceBitMask, mask) != 0) {
                eastFaceBitMask = eastFaceBitMask ^ mask;
            }
        }

        if (this.shouldWestFaceRender(blockIndex)) {
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
        this.markDirty();
    }

    public void notifyBlockWithoutRebuild(int x, int y, int z) {
        x %= 32;
        y %= 32;
        z %= 32;
        if (x < 0) {
            x *= -1;
        }
        if (y < 0) {
            y *= -1;
        }
        if (z < 0) {
            z *= -1;
        }
        int mask;
        int topFaceBitmask = this.topFaceBitMask[calculateBitMaskIndex(x, z)];
        int bottomFaceBitMask = this.bottomFaceBitMask[calculateBitMaskIndex(x, z)];
        int northFaceBitMask = this.northFaceBitMask[calculateBitMaskIndex(z, y)];
        int southFaceBitMask = this.southFaceBitMask[calculateBitMaskIndex(z, y)];
        int eastFaceBitMask = this.eastFaceBitMask[calculateBitMaskIndex(x, y)];
        int westFaceBitMask = this.westFaceBitMask[calculateBitMaskIndex(x, y)];
        int blockIndex = getBlockIndexFromCoordinates(x, y, z);

        mask = this.createMask(y);
        if (this.shouldTopFaceRender(blockIndex)) {
            if (this.checkBitValue(topFaceBitmask, mask) == 0) {
                topFaceBitmask = topFaceBitmask ^ mask;
            }
        } else {
            if (this.checkBitValue(topFaceBitmask, mask) != 0) {
                topFaceBitmask = topFaceBitmask ^ mask;
            }
        }

        if (this.shouldBottomFaceRender(blockIndex)) {
            if (this.checkBitValue(bottomFaceBitMask, mask) == 0) {
                bottomFaceBitMask = bottomFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(bottomFaceBitMask, mask) != 0) {
                bottomFaceBitMask = bottomFaceBitMask ^ mask;
            }
        }


        mask = this.createMask(x);
        if (this.shouldNorthFaceRender(blockIndex)) {
            if (this.checkBitValue(northFaceBitMask, mask) == 0) {
                northFaceBitMask = northFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(northFaceBitMask, mask) != 0) {
                northFaceBitMask = northFaceBitMask ^ mask;
            }
        }

        if (this.shouldSouthFaceRender(blockIndex)) {
            if (this.checkBitValue(southFaceBitMask, mask) == 0) {
                southFaceBitMask = southFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(southFaceBitMask, mask) != 0) {
                southFaceBitMask = southFaceBitMask ^ mask;
            }
        }


        mask = this.createMask(z);
        if (this.shouldEastFaceRender(blockIndex)) {
            if (this.checkBitValue(eastFaceBitMask, mask) == 0) {
                eastFaceBitMask = eastFaceBitMask ^ mask;
            }
        } else {
            if (this.checkBitValue(eastFaceBitMask, mask) != 0) {
                eastFaceBitMask = eastFaceBitMask ^ mask;
            }
        }

        if (this.shouldWestFaceRender(blockIndex)) {
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
        while (x < 0) {
            x += 32;
        }
        while (y < 0) {
            y += 32;
        }
        while (z < 0) {
            z += 32;
        }
        x %= 32;
        y %= 32;
        z %= 32;
        return (x + (y << 10) + (z << 5));
    }

    public short getBlockID(int x, int y, int z){
        return this.blocks[getBlockIndexFromCoordinates(x,y,z)];
    }

    public byte getBlockLightValue(int x, int y, int z) {
        return this.lighting[getBlockIndexFromCoordinates(x, y, z)];
    }

    public byte getSkyLightValue(int x, int y, int z){
        return this.skyLight[getBlockIndexFromCoordinates(x,y,z)];
    }

    public float[] getBlockLightColor(int x, int y, int z) {
        return new Color(this.lightColor[getBlockIndexFromCoordinates(x, y, z)]).getRGBComponents(new float[4]);
    }

    private int getLightBlockColor(int x, int y, int z) {
        LightColorLocation location;
        for (int i = 0; i < this.lightColorLocations.size(); i++) {
            location = this.lightColorLocations.get(i);
            if (location.x == x && location.y == y && location.z == z) {
                return location.colorValue;
            }
        }
        return 16777215;
    }

    public void setSkyLight() {
        Arrays.fill(this.skyLight, (byte)0);
        ArrayList<int[]> skyLightUpdateQueue = new ArrayList<>();
        ArrayList<int[]> previousSkyLightUpdateQueue = new ArrayList<>();
        for (int i = 0; i < this.blocks.length; i++) {
            if (Block.list[this.blocks[i]].ID != Block.water.ID) {
                if (!Block.list[this.blocks[i]].isSolid && this.parentWorld.doesBlockHaveSkyAccess(this.getBlockXFromIndex(i), this.getBlockYFromIndex(i), this.getBlockZFromIndex(i))) {
                    this.parentWorld.propagateSkyLight(this.getBlockXFromIndex(i), this.getBlockYFromIndex(i), this.getBlockZFromIndex(i), skyLightUpdateQueue, previousSkyLightUpdateQueue);
                }
            }
        }
    }


    public synchronized void setBlockLightValue(int x, int y, int z, byte lightLevel) {
        this.lighting[getBlockIndexFromCoordinates(x,y,z)] = lightLevel;
    }

    public synchronized void setBlockSkyLightValue(int x, int y, int z, byte lightLevel){
        this.skyLight[getBlockIndexFromCoordinates(x,y,z)] = lightLevel;
    }

    //Both sky and block light color can never have any component that is 0, things will break
    public synchronized void setBlockLightColor(int x, int y, int z, int lightColor) {
        this.lightColor[getBlockIndexFromCoordinates(x, y, z)] = lightColor;
        float[] color = this.getBlendedLightColor(x, y, z);
        this.lightColor[getBlockIndexFromCoordinates(x, y, z)] = new Color(color[0], color[1], color[2]).getRGB();
    }

    public synchronized void clearBlockLightColor(int x, int y, int z) {
        if (this.parentWorld.doesBlockHaveSkyAccess(x, y, z)) {
            this.lightColor[getBlockIndexFromCoordinates(x, y, z)] = new Color(this.parentWorld.skyLightColor[0], this.parentWorld.skyLightColor[1], this.parentWorld.skyLightColor[2]).getRGB();
        } else {
            this.lightColor[getBlockIndexFromCoordinates(x, y, z)] = 16777215;
        }
    }

    private float[] getBlendedLightColor(int x, int y, int z) {
        if (Block.list[this.blocks[getBlockIndexFromCoordinates(x, y, z)]].isLightBlock) {
            Color color = new Color(this.getLightBlockColor(x, y, z));
            return new float[]{color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F};
        } else if (Block.list[this.blocks[getBlockIndexFromCoordinates(x, y, z)]].isSolid) {
            return new float[3];
        }
        float[] colorArray = this.parentWorld.getBlockLightColor(x, y, z);
        float[] colorArray0 = this.parentWorld.getBlockLightColor(x + 1, y, z);
        float[] colorArray1 = this.parentWorld.getBlockLightColor(x - 1, y, z);
        float[] colorArray2 = this.parentWorld.getBlockLightColor(x, y, z + 1);
        float[] colorArray3 = this.parentWorld.getBlockLightColor(x, y, z - 1);
        float[] colorArray4 = this.parentWorld.getBlockLightColor(x, y + 1, z);
        float[] colorArray5 = this.parentWorld.getBlockLightColor(x, y - 1, z);

        Color color = new Color(colorArray[0], colorArray[1], colorArray[2]);
        Color color0 = new Color(colorArray0[0], colorArray0[1], colorArray0[2]);
        Color color1 = new Color(colorArray1[0], colorArray1[1], colorArray1[2]);
        Color color2 = new Color(colorArray2[0], colorArray2[1], colorArray2[2]);
        Color color3 = new Color(colorArray3[0], colorArray3[1], colorArray3[2]);
        Color color4 = new Color(colorArray4[0], colorArray4[1], colorArray4[2]);
        Color color5 = new Color(colorArray5[0], colorArray5[1], colorArray5[2]);

        int divisor = 0;
        float[] red = new float[7];
        red[0] = (float) color0.getRed() / 255F;
        red[1] = (float) color1.getRed() / 255F;
        red[2] = (float) color2.getRed() / 255F;
        red[3] = (float) color3.getRed() / 255F;
        red[4] = (float) color4.getRed() / 255F;
        red[5] = (float) color5.getRed() / 255F;
        red[6] = (float) color.getRed() / 255F;
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
        green[0] = (float) color0.getGreen() / 255F;
        green[1] = (float) color1.getGreen() / 255F;
        green[2] = (float) color2.getGreen() / 255F;
        green[3] = (float) color3.getGreen() / 255F;
        green[4] = (float) color4.getGreen() / 255F;
        green[5] = (float) color5.getGreen() / 255F;
        green[6] = (float) color.getGreen() / 255F;
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
        blue[0] = (float) color0.getBlue() / 255F;
        blue[1] = (float) color1.getBlue() / 255F;
        blue[2] = (float) color2.getBlue() / 255F;
        blue[3] = (float) color3.getBlue() / 255F;
        blue[4] = (float) color4.getBlue() / 255F;
        blue[5] = (float) color5.getBlue() / 255F;
        blue[6] = (float) color.getBlue() / 255F;
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


    public int getBlockXFromIndex(int index) {
        return ((index % 32) + (this.x << 5));
    }

    public int getBlockYFromIndex(int index) {
        return (index >> 10) + (this.y << 5);
    }

    public int getBlockZFromIndex(int index) {
        return ((index % 1024) >> 5) + (this.z << 5);
    }

    public void markDirty() {
        this.parentWorld.chunkController.addChunkToRebuildQueue(this);
    }
    public void markToPopulate(){
        this.parentWorld.chunkController.addChunkToPopulationQueue(this);
    }


    public boolean checkIfChunkShouldRender() {
        int bitMap = 0;
        for(int i = 0; i <  this.topFaceBitMask.length; i++){
            bitMap = this.topFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    return true;
                }
            }
        }

        for(int i = 0; i <  this.bottomFaceBitMask.length; i++){
            bitMap = this.bottomFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    return true;
                }
            }
        }

        for(int i = 0; i <  this.northFaceBitMask.length; i++){
            bitMap = this.northFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    return true;
                }
            }
        }

        for(int i = 0; i <  this.southFaceBitMask.length; i++){
            bitMap = this.southFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    return true;
                }
            }
        }

        for(int i = 0; i <  this.eastFaceBitMask.length; i++){
            bitMap = this.eastFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    return true;
                }
            }
        }

        for(int i = 0; i <  this.westFaceBitMask.length; i++){
            bitMap = this.westFaceBitMask[i];
            for(int j = 0; j < 32; j++){
                if((bitMap & this.createMask(j)) != 0){
                    return true;
                }
            }
        }


        return false;
    }

    private boolean shouldTopFaceRender(int index) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.TOP_FACE;

        if (index < 31744) {
            firstBlock = this.blocks[index];
            secondBlock = this.blocks[index + 1024];
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y + 1, this.z);
            if(chunk == null)return true;
            firstBlock = this.blocks[index];
            if(chunk.blocks != null) {
                secondBlock = chunk.blocks[index - 31744];
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face);
    }



    private boolean shouldBottomFaceRender(int index) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.BOTTOM_FACE;

        if (index > 1023) {
            firstBlock = this.blocks[index];
            secondBlock = this.blocks[index - 1024];
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y - 1, this.z);
            if(chunk == null)return true;
            firstBlock = this.blocks[index];
            if(chunk.blocks != null) {
                secondBlock = chunk.blocks[index + 31744];
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face);
    }

    private boolean shouldNorthFaceRender(int index) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.NORTH_FACE;

        if (index % 32 != 0) {
            firstBlock = this.blocks[index];
            secondBlock = this.blocks[index - 1];
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x - 1, this.y, this.z);
            if(chunk == null)return true;
            firstBlock = this.blocks[index];
            if(chunk.blocks != null) {
                secondBlock = chunk.blocks[index + 31];
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face);
    }

    private boolean shouldSouthFaceRender(int index) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.SOUTH_FACE;

        if (index % 32 != 31) {
            firstBlock = this.blocks[index];
            secondBlock = this.blocks[index + 1];
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x + 1, this.y, this.z);
            if(chunk == null)return true;
            firstBlock = this.blocks[index];
            if(chunk.blocks != null) {
                secondBlock = chunk.blocks[index - 31];
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face);
    }

    private boolean shouldEastFaceRender(int index) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.EAST_FACE;

        if ((index % 1024) / 32 != 0) {
            firstBlock = this.blocks[index];
            secondBlock = this.blocks[index - 32];
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y, this.z - 1);
            if(chunk == null)return true;
            firstBlock = this.blocks[index];
            if(chunk.blocks != null) {
                secondBlock = chunk.blocks[index + 992];
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face);
    }

    private boolean shouldWestFaceRender(int index) {
        short firstBlock;
        short secondBlock;
        int face = RenderBlocks.WEST_FACE;

        if ((index % 1024) / 32 != 31) {
            firstBlock = this.blocks[index];
            secondBlock = this.blocks[index + 32];
        } else {
            Chunk chunk = this.parentWorld.findChunkFromChunkCoordinates(this.x, this.y, this.z + 1);
            if(chunk == null)return true;
            firstBlock = this.blocks[index];
            if(chunk.blocks != null) {
                secondBlock = chunk.blocks[index - 992];
            } else {
                secondBlock = Block.air.ID;
            }
        }

        return this.sorter.shouldFaceRender(firstBlock,secondBlock,face);
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
        for(int i = 0; i < this.blocks.length; i++){
            if(this.blocks[i] == Block.air.ID){
                return true;
            }
        }
        return false;
    }

    public boolean containsWater(){
        for(int i = 0; i < this.blocks.length; i++){
            if(this.blocks[i] == Block.water.ID){
                return true;
            }
        }
        return false;
    }


    public void notifyAllBlocks() {
        int x = 0;
        int y = 0;
        int z = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            x = this.getBlockXFromIndex(i);
            y = this.getBlockYFromIndex(i);
            z = this.getBlockZFromIndex(i);
            this.notifyBlock(x, y, z);
            if(Block.list[this.blocks[i]].isLightBlock){
                this.parentWorld.propagateLightSource(x,y,z, Block.list[this.blocks[i]].lightBlockValue);
            }
        }
    }


    public void setLightValueUnderWater(){
        byte airAboveWater;
        for(int i = 0; i < this.blocks.length; i++){
            if(this.blocks[i] == Block.water.ID){
                airAboveWater = this.findAirAboveWater(i);
                this.skyLight[i] = airAboveWater;
            }
        }
    }


    private byte findAirAboveWater(int index){
        int x = this.getBlockXFromIndex(index);
        int y = this.getBlockYFromIndex(index);
        int z = this.getBlockZFromIndex(index);
        for(byte i = 1; i < 16; i++){
            if(this.parentWorld.getBlockID(x, y + i, z) == Block.air.ID){
                return (byte) (this.parentWorld.skyLightLevel - i);
            } else if(Block.list[this.parentWorld.getBlockID(x, y + i, z)].isSolid){
                return 0;
            }
        }
        return 0;
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
        GL46.glDeleteVertexArrays(this.opaqueVAOID);
        GL46.glDeleteVertexArrays(this.transparentVAOID);
        GL46.glDeleteBuffers(this.opaqueVBOID);
        GL46.glDeleteBuffers(this.transparentVBOID);
        GL46.glDeleteBuffers(this.opaqueEBOID);
        GL46.glDeleteBuffers(this.transparentEBOID);
    }

    public void bindRenderData(){
        if(this.opaqueVAOID == -10 || this.opaqueVBOID == -10 || this.opaqueEBOID == -10 || this.transparentVAOID == -10 || this.transparentVBOID == -10 || this.transparentEBOID == -10){
            this.createGLObjects();
        }
        GL46.glBindVertexArray(this.opaqueVAOID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.opaqueVBOID);
        GL46.glVertexAttribPointer(0, Chunk.positionsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, Chunk.colorSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, Chunk.positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, Chunk.texCoordsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, (Chunk.positionsSize + Chunk.colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, Chunk.texIndexSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glVertexAttribPointer(4, Chunk.normalSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize + Chunk.texIndexSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(4);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.opaqueEBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBufferOpaque, GL46.GL_STATIC_DRAW);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBufferOpaque, GL46.GL_STATIC_DRAW);

        GL46.glBindVertexArray(this.transparentVAOID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.transparentVBOID);
        GL46.glVertexAttribPointer(0, Chunk.positionsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, Chunk.colorSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, Chunk.positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, Chunk.texCoordsSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, (Chunk.positionsSize + Chunk.colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, Chunk.texIndexSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glVertexAttribPointer(4, Chunk.normalSize, GL46.GL_FLOAT, false, Chunk.vertexSizeBytes, (Chunk.positionsSize + Chunk.colorSize + Chunk.texCoordsSize + Chunk.texIndexSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(4);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.transparentEBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBufferTransparent, GL46.GL_STATIC_DRAW);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBufferTransparent, GL46.GL_STATIC_DRAW);
    }


    public void renderOpaque(int xOffset, int yOffset, int zOffset, int sunX, int sunY, int sunZ) {
        if(this.elementBufferOpaque == null)return;
        this.chunkOffset.x = xOffset;
        this.chunkOffset.y = yOffset;
        this.chunkOffset.z = zOffset;
        Shader.terrainShader.uploadVec3f("chunkOffset", this.chunkOffset);
        Shader.terrainShader.uploadVec3f("sunChunkOffset", new Vector3f((this.x - sunX) << 5, (this.y - sunY) << 5, (this.z - sunZ) << 5));
        GL46.glBindVertexArray(this.opaqueVAOID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.opaqueVBOID);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.opaqueEBOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBufferOpaque.limit(), GL46.GL_UNSIGNED_INT, 0);
    }

    public void renderTransparent(int sunX, int sunY, int sunZ) {
        if(this.elementBufferTransparent == null)return;
        Shader.terrainShader.uploadVec3f("chunkOffset", chunkOffset);
        Shader.terrainShader.uploadVec3f("sunChunkOffset", new Vector3f((this.x - sunX) << 5, (this.y - sunY) << 5, (this.z - sunZ) << 5));
        GL46.glBindVertexArray(this.transparentVAOID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.transparentVBOID);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.transparentEBOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBufferTransparent.limit(), GL46.GL_UNSIGNED_INT, 0);
    }

    public void renderShadowMap(int sunX, int sunY, int sunZ) {
        if (this.elementBufferOpaque == null) return;
        if (this.elementBufferOpaque.limit() == 0) return;

        Shader.shadowMapShader.uploadVec3f("chunkOffset", new Vector3f((this.x - sunX) << 5, (this.y - sunY) << 5, (this.z - sunZ) << 5));

        GL46.glBindVertexArray(this.opaqueVAOID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.opaqueVBOID);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.opaqueEBOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBufferOpaque.limit(), GL46.GL_UNSIGNED_INT, 0);

        if (this.elementBufferTransparent == null) return;
        if (this.elementBufferTransparent.limit() == 0)return;

        GL46.glBindVertexArray(this.transparentVAOID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.transparentVBOID);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.transparentEBOID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBufferTransparent.limit(), GL46.GL_UNSIGNED_INT, 0);
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
        for (int i = 0; i < this.blocks.length; i++) {
            if (this.blocks[i] != Block.air.ID) {
                return false;
            }
        }
        return true;
    }

    public void checkIfChunkShouldUnloadFromDistance(int playerChunkX, int playerChunkY, int playerChunkZ) {
        if (this.distanceFromPlayer(playerChunkX, playerChunkY, playerChunkZ) >= 10) {
            this.emptyChunk();
        }
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

    public void emptyChunk() {
        this.empty = true;
        this.blocks = null;
        this.lighting = null;
        this.lightColor = null;
        this.topFaceBitMask = null;
        this.bottomFaceBitMask = null;
        this.northFaceBitMask = null;
        this.southFaceBitMask = null;
        this.eastFaceBitMask = null;
        this.westFaceBitMask = null;
    }

    public void initChunk() {
        this.empty = false;
        this.blocks = new short[32768];
        this.lighting = new byte[32768];
        this.lightColor = new int[32768];
        this.topFaceBitMask = new int[1024];
        this.bottomFaceBitMask = new int[1024];
        this.northFaceBitMask = new int[1024];
        this.southFaceBitMask = new int[1024];
        this.eastFaceBitMask = new int[1024];
        this.westFaceBitMask = new int[1024];
        Arrays.fill(this.lightColor, new Color(this.parentWorld.skyLightColor[0], this.parentWorld.skyLightColor[1], this.parentWorld.skyLightColor[2]).getRGB());
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
                        if (Block.list[this.blocks[this.tickableBlockIndex[i]]] instanceof ITickable) {
                            ((ITickable) Block.list[this.blocks[this.tickableBlockIndex[i]]]).tick(this.getBlockXFromIndex(this.tickableBlockIndex[i]), this.getBlockYFromIndex(this.tickableBlockIndex[i]), this.getBlockZFromIndex(this.tickableBlockIndex[i]), this.parentWorld);
                        }
                    }
            }
            //Check the time events to see if they need to execute
            TimeUpdateEvent event;
            for(int i = 0; i < this.updateEvents.size(); i++){
                event = this.updateEvents.get(i);
                if(CosmicEvolution.instance.save.time >= event.updateTime) {
                    int x = this.getBlockXFromIndex(event.index);
                    int y = this.getBlockYFromIndex(event.index);
                    int z = this.getBlockZFromIndex(event.index);
                    if (Block.list[this.blocks[event.index]] instanceof ITimeUpdate) {
                        ((ITimeUpdate) Block.list[this.blocks[event.index]]).onTimeUpdate(x, y, z, this.parentWorld);
                    }
                }
            }
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

    public void addChestLocation(int x, int y, int z, Inventory inventory){
        this.chestLocations.add(new ChestLocation((short) getBlockIndexFromCoordinates(x,y,z), inventory));
    }

    public void addChestLocation(short index, Inventory inventory){
        this.chestLocations.add(new ChestLocation(index, inventory));
    }

    public ChestLocation getChestLocation(int x, int y, int z){
        int index = getBlockIndexFromCoordinates(x,y,z);
        for(int i = 0; i < this.chestLocations.size(); i++){
            if(this.chestLocations.get(i).index == index){
                return this.chestLocations.get(i);
            }
        }
        return null;
    }

    public ChestLocation getChestLocation(int index){
        for(int i = 0; i < this.chestLocations.size(); i++){
            if(this.chestLocations.get(i).index == index){
                return this.chestLocations.get(i);
            }
        }
        return null;
    }

    public void removeChestLocation(short index){
        for(int i = 0; i < this.chestLocations.size(); i++){
            if(this.chestLocations.get(i).index == index){
                this.clearInventoryFromChest(this.chestLocations.get(i).inventory, this.chestLocations.get(i).index);
                this.chestLocations.remove(i);
                break;
            }
        }
        this.chestLocations.trimToSize();
    }

    public void addTimeUpdateEvent(int x, int y, int z, long updateTime){
        this.updateEvents.add(new TimeUpdateEvent((short) getBlockIndexFromCoordinates(x,y,z), updateTime));
    }

    public TimeUpdateEvent getTimeUpdateEvent(int x, int y, int z){
        int index = getBlockIndexFromCoordinates(x,y,z);
        for(int i = 0; i < this.updateEvents.size(); i++){
            if(this.updateEvents.get(i).index == index){
                return this.updateEvents.get(i);
            }
        }
        return null;
    }

    public void addTimeUpdateEvent(short index, long updateTime){
        this.updateEvents.add(new TimeUpdateEvent(index, updateTime));
    }

    public void removeTimeUpdateEvent(int x, int y, int z){
        int index = getBlockIndexFromCoordinates(x,y,z);
        for(int i = 0; i < this.updateEvents.size(); i++){
            if(this.updateEvents.get(i).index == index){
                this.updateEvents.remove(i);
                break;
            }
        }
        this.updateEvents.trimToSize();
    }

    public void updateTimeEvent(int x, int y, int z, long updateTime){
        int index = getBlockIndexFromCoordinates(x,y,z);

        for(int i = 0; i < this.updateEvents.size(); i++){
            if(this.updateEvents.get(i).index == index){
                this.updateEvents.get(i).updateTime = updateTime;
                break;
            }
        }
    }

    private void clearInventoryFromChest(Inventory inventory, int index){
        Random rand = new Random();
        for(int i = 0; i < inventory.itemStacks.length; i++) {
            if(inventory.itemStacks[i].item == Item.block) {
                EntityBlock block = new EntityBlock(this.getBlockXFromIndex(index) + 0.5, this.getBlockYFromIndex(index) + 0.5, this.getBlockZFromIndex(index) + 0.5, inventory.itemStacks[i].metadata, inventory.itemStacks[i].count);
                block.setMovementVector(new Vector3f(rand.nextFloat(-1, 1), rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)));
                this.addEntityToList(block);
            } else if(inventory.itemStacks[i].item != null){
                EntityItem item = new EntityItem(this.getBlockXFromIndex(index) + 0.5, this.getBlockYFromIndex(index) + 0.5, this.getBlockZFromIndex(index) + 0.5, inventory.itemStacks[i].item.ID, inventory.itemStacks[i].metadata, inventory.itemStacks[i].count, inventory.itemStacks[i].durability);
                item.setMovementVector(new Vector3f(rand.nextFloat(-1, 1), rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)));
                this.addEntityToList(item);
            }
            inventory.itemStacks[i].item = null;
            inventory.itemStacks[i].count = 0;
            inventory.itemStacks[i].durability = 0;
            inventory.itemStacks[i].metadata = 0;
        }
    }


    public long getChunkSeed(){
        return (long) this.x * this.y + this.z;
    }
}


