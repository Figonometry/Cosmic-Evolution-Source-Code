package spacegame.world;

import org.joml.Vector3f;
import spacegame.block.*;
import spacegame.core.*;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.gui.*;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemAxe;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class World {
    public int[] activeBlockLight = new int[3];
    public ArrayList<int[]> lightUpdateQueue = new ArrayList<>();
    public ArrayList<int[]> previouslyQueuedLightUpdate = new ArrayList<>();
    public ArrayList<int[]> darknessUpdateQueue = new ArrayList<>();
    public ArrayList<int[]> previouslyQueuedDarknessUpdate = new ArrayList<>();
    public ArrayList<int[]> lightSearchQueue = new ArrayList<>();
    public ArrayList<int[]> previousLightSearchQueue = new ArrayList<>();
    public int safetyThreshold = 0;
    public int resetLightX;
    public int resetLightY;
    public int resetLightZ;
    public byte delayWhenExitingUI;
    public float sunAngle;
    public SpaceGame sg;
    public byte skyLightLevel;
    public float[] skyColor; //used for glClear on the color buffer
    public float[] defaultSkyColor;
    public float[] skyLightColor; //Do not make any RGB component 0
    public final int size;
    public static int worldLoadPhase = 0;
    public static int noiseMapsCompleted = 0;
    public static int totalMaps;
    public File worldFolder;
    public ChunkController chunkController;
    public boolean paused = false;

    public World(SpaceGame spaceGame, int size) {
        this.sg = spaceGame;
        this.size = size;
        this.chunkController = new ChunkController(this);
    }

    public void tick() {

    }

    public abstract void initNoiseMaps();

    public abstract void saveWorld();

    public abstract void saveWorldWithoutUnload();

    public abstract void loadWorld();

    public void toggleWorldPause(){
        this.paused = !this.paused;
    }

    public synchronized void addEntity(Entity entity){
        Chunk chunk = this.findChunkFromChunkCoordinates((int)entity.x >> 5, (int)entity.y >> 5, (int)entity.z >> 5);
        chunk.addEntityToList(entity);
    }

    public synchronized void setBlock(int x, int y, int z, short blockID) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk.blocks == null){
            chunk.initChunk();
        }
        chunk.setBlock(x, y, z, blockID);
        this.findChunkSkyLightMap(x >> 5, z >> 5).updateLightMap(x, y, z);
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);

        if (Block.list[blockID].isSolid) {
            chunk.lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
            chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
        }

        if (blockID == Block.air.ID) {
            if (lightMap.isHeight(x, y, z)) {
                lightMap.updateLightMap(x, this.findNextHighestSolidBlock(x, y, z), z);
            }
        } else if (Block.list[blockID].isSolid) {
            if (lightMap.isHeightGreater(x, y, z)) {
                lightMap.updateLightMap(x, y, z);
            }
        }
        chunk.updateSkylight = true;
    }

    public synchronized void setBlockWithNotify(int x, int y, int z, short blockID) {
        boolean destroyLight = false;
        if (Block.list[this.getBlockID(x, y, z)].isLightBlock && blockID == Block.air.ID) {
            destroyLight = true;
        }
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        chunk.setBlockWithNotify(x, y, z, blockID);
        if (blockID == Block.air.ID) {
            this.resetNearestLight(x, y, z);
        }
        if (Block.list[blockID].isLightBlock) {
            this.propagateLightSource(x, y, z, Block.list[blockID].lightBlockValue);
        }
        if (Block.list[blockID].isSolid) {
            chunk.lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
            chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
            this.queueSurroundingLightBlocks(x, y, z);
        }
        this.notifySurroundingBlocks(x, y, z);
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);
        if (blockID == Block.air.ID) {
            if (lightMap.isHeight(x, y, z)) {
                lightMap.updateLightMap(x, this.findNextHighestSolidBlock(x, y, z), z);
            }
        } else if (Block.list[blockID].isSolid) {
            if (lightMap.isHeightGreater(x, y, z)) {
                lightMap.updateLightMap(x, y, z);
            }
        }
        if (destroyLight) {
            this.propagateDarkness(x, y, z);
        }
        chunk.updateSkylight = true;
    }

    public Chunk findChunkFromChunkCoordinates(int x, int y, int z) {
        return this.chunkController.findChunkFromChunkCoordinates(x, y, z);
    }

    public ChunkColumnSkylightMap findChunkSkyLightMap(int x, int z) {
        return this.chunkController.findChunkSkyLightMap(x, z);
    }


    public void updateSkyLightMapChunks(int x, int y, int z){
        y >>= 5;
        y -= 1;
        Chunk chunk;
        while (y >= this.sg.save.thePlayer.chunkY - GameSettings.chunkColumnHeight) {
            chunk = this.findChunkFromChunkCoordinates(x, y, z);
            if (chunk != null) {
                chunk.updateSkylight = true;
                chunk.markDirty();
            }
            y--;
        }

    }

    public ArrayList<AxisAlignedBB> getBlockBoundingBoxes(AxisAlignedBB boundingBox, ArrayList<AxisAlignedBB> blockBoundingBoxes){

        int minX = MathUtil.floorDouble(boundingBox.minX);
        int minY = MathUtil.floorDouble(boundingBox.minY);
        int minZ = MathUtil.floorDouble(boundingBox.minZ);
        int maxX = MathUtil.floorDouble(boundingBox.maxX);
        int maxY = MathUtil.floorDouble(boundingBox.maxY);
        int maxZ = MathUtil.floorDouble(boundingBox.maxZ);

        AxisAlignedBB block;
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    block = new AxisAlignedBB();
                    block.adjustBlockBoundingBox(x,y,z, this.getBlockID(x,y,z), (boundingBox.maxX + boundingBox.minX) / 2, (boundingBox.maxZ + boundingBox.minZ) / 2);
                    if(!block.air) {
                        blockBoundingBoxes.add(block);
                    }
                }
            }
        }

        return blockBoundingBoxes;
    }

    public synchronized void notifySurroundingBlocks(int x, int y, int z) {
        this.notifySurroundingBlock(x + 1, y, z);
        this.notifySurroundingBlock(x - 1, y, z);
        this.notifySurroundingBlock(x, y + 1, z);
        this.notifySurroundingBlock(x, y - 1, z);
        this.notifySurroundingBlock(x, y, z + 1);
        this.notifySurroundingBlock(x, y, z - 1);
    }

    public synchronized void notifySurroundingBlocksWithoutRebuild(int x, int y, int z) {
        this.notifySurroundingBlockWithoutRebuild(x + 1, y, z);
        this.notifySurroundingBlockWithoutRebuild(x - 1, y, z);
        this.notifySurroundingBlockWithoutRebuild(x, y + 1, z);
        this.notifySurroundingBlockWithoutRebuild(x, y - 1, z);
        this.notifySurroundingBlockWithoutRebuild(x, y, z + 1);
        this.notifySurroundingBlockWithoutRebuild(x, y, z - 1);
    }

    public void notifyGroundBelow(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, (y - 1) >> 5, z >> 5);
        for (int i = 1; i < 256; i++) {
            if ((y - i) % 32 == 0) {
                chunk = this.findChunkFromChunkCoordinates(x >> 5, (y - i) >> 5, z >> 5);
            }
            if (chunk != null && chunk.blocks != null) {
                if (Block.list[chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y - i, z)]].isSolid) {
                    chunk.markDirty();
                    return;
                }
            }
        }
    }

    public void notifySurroundingChunks(int x, int y, int z) {
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y - 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y - 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y - 1, z - 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y, z - 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y + 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y + 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y + 1, z - 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y - 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y - 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y - 1, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y + 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y + 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y + 1, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y - 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y - 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y - 1, z + 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y, z + 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y + 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y + 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y + 1, z + 1));
    }


    public void notifySurroundingBlock(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk.empty){
            chunk.initChunk();
        }
        chunk.notifyBlock(x, y, z);
    }

    public void notifySurroundingBlockWithoutRebuild(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk.empty){
            chunk.initChunk();
        }
        chunk.notifyBlockWithoutRebuild(x, y, z);

    }

    private void resetNearestLight(int x, int y, int z) {
        this.resetLightX = x;
        this.resetLightY = y;
        this.resetLightZ = z;
        this.checkSurroundingBlocksToSearchForLight(x, y, z);
        ArrayList<int[]> localCopyLightSearchQueue = new ArrayList<>();
        int[] coordinates;
        lightSearch:
        while (!this.lightSearchQueue.isEmpty() && this.safetyThreshold < 100000) {
            for (int i = 0; i < this.lightSearchQueue.size(); i++) {
                coordinates = this.lightSearchQueue.get(i);
                if (Block.list[this.getBlockID(coordinates[0], coordinates[1], coordinates[2])].isLightBlock) {
                    this.propagateDarkness(coordinates[0], coordinates[1], coordinates[2]);
                    break lightSearch;
                }
            }
            localCopyLightSearchQueue.addAll(this.lightSearchQueue);
            this.lightSearchQueue.clear();
            for (int i = 0; i < localCopyLightSearchQueue.size(); i++) {
                this.checkSurroundingBlocksToSearchForLight(localCopyLightSearchQueue.get(i));
            }
            localCopyLightSearchQueue.clear();
            this.safetyThreshold++;
        }
        this.safetyThreshold = 0;
        this.previousLightSearchQueue.clear();
    }

    public void checkSurroundingBlocksToSearchForLight(int[] coordinates) {
        this.checkSurroundingBlocksToSearchForLight(coordinates[0], coordinates[1], coordinates[2]);
    }

    public void checkSurroundingBlocksToSearchForLight(int x, int y, int z) {
        if (this.shouldBlockAddToLightSearchQueue(x + 1, y, z)) {
            this.lightSearchQueue.add(new int[]{x + 1, y, z});
            this.previousLightSearchQueue.add(new int[]{x + 1, y, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x - 1, y, z)) {
            this.lightSearchQueue.add(new int[]{x - 1, y, z});
            this.previousLightSearchQueue.add(new int[]{x - 1, y, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y + 1, z)) {
            this.lightSearchQueue.add(new int[]{x, y + 1, z});
            this.previousLightSearchQueue.add(new int[]{x, y + 1, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y - 1, z)) {
            this.lightSearchQueue.add(new int[]{x, y - 1, z});
            this.previousLightSearchQueue.add(new int[]{x, y - 1, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y, z + 1)) {
            this.lightSearchQueue.add(new int[]{x, y, z + 1});
            this.previousLightSearchQueue.add(new int[]{x, y, z + 1});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y, z - 1)) {
            this.lightSearchQueue.add(new int[]{x, y, z - 1});
            this.previousLightSearchQueue.add(new int[]{x, y, z - 1});
        }
    }

    private boolean shouldBlockAddToLightSearchQueue(int x, int y, int z) {
        return !Block.list[this.getBlockID(x, y, z)].isSolid && !this.hasBlockAlreadyLightSearchQueued(x, y, z) && this.calculateBlockDistance(x, y, z) <= 5;
    }

    private int calculateBlockDistance(int x, int y, int z) {
        int[] returns = new int[3];
        returns[0] = this.resetLightX - x;
        returns[1] = this.resetLightY - y;
        returns[2] = this.resetLightZ - z;

        for (int i = 0; i < returns.length; i++) {
            if (returns[i] < 0) {
                returns[i] *= -1;
            }
        }

        Arrays.sort(returns);
        return returns[2];
    }

    private boolean hasBlockAlreadyLightSearchQueued(int x, int y, int z) {
        int[] comparedArray;
        for (int i = 0; i < this.previousLightSearchQueue.size(); i++) {
            comparedArray = this.previousLightSearchQueue.get(i);
            if (comparedArray[0] == x && comparedArray[1] == y && comparedArray[2] == z) {
                return true;
            }
        }
        return false;
    }

    private void queueSurroundingLightBlocks(int x, int y, int z) {
        final int xCenter = x;
        final int yCenter = y;
        final int zCenter = z;
        int boxDimension = 31;
        int boxXMax = 15 + x;
        int boxYMax = 15 + y;
        int boxZMax = 15 + z;
        ArrayList<int[]> lightSourceQueue = new ArrayList<>();

        for (x = xCenter - boxDimension / 2; x < boxXMax; x++) {
            for (y = yCenter - boxDimension / 2; y < boxYMax; y++) {
                for (z = zCenter - boxDimension / 2; z < boxZMax; z++) {
                    this.clearBlockLight(x, y, z);
                    if (Block.list[this.getBlockID(x, y, z)].isLightBlock) {
                        lightSourceQueue.add(new int[]{x, y, z});
                    }
                }
            }
        }
        int[] lightArray;
        for (int i = 0; i < lightSourceQueue.size(); i++) {
            lightArray = lightSourceQueue.get(i);
            this.propagateLightSource(lightArray[0], lightArray[1], lightArray[2], Block.list[this.getBlockID(lightArray[0], lightArray[1], lightArray[2])].lightBlockValue);
        }
    }

    private void repairDamagedLights(int x, int y, int z, int previousLight) {
        final int xCenter = x;
        final int yCenter = y;
        final int zCenter = z;
        int boxDimension = previousLight * 2 + 1;
        int boxXMax = previousLight + x;
        int boxYMax = previousLight + y;
        int boxZMax = previousLight + z;

        for (x = xCenter - boxDimension / 2; x < boxXMax; x++) {
            for (y = yCenter - boxDimension / 2; y < boxYMax; y++) {
                for (z = zCenter - boxDimension / 2; z < boxZMax; z++) {
                    if (Block.list[this.getBlockID(x, y, z)].isLightBlock) {
                        this.propagateLightSource(x, y, z, Block.list[this.getBlockID(x, y, z)].lightBlockValue);
                    }
                }
            }
        }
    }

    public void propagateDarkness(int x, int y, int z) {
        int previousLight = this.getBlockLightValue(x, y, z);
        this.clearBlockLight(x, y, z);
        this.checkSurroundingBlocksToPropagateDarkness(x, y, z, previousLight);
        ArrayList<int[]> localCopyDarknessQueue = new ArrayList<>();
        while (!this.darknessUpdateQueue.isEmpty() && this.safetyThreshold < 100000) {
            for (int i = 0; i < this.darknessUpdateQueue.size(); i++) {
                this.clearBlockLight(this.darknessUpdateQueue.get(i));
            }
            localCopyDarknessQueue.addAll(this.darknessUpdateQueue);
            this.darknessUpdateQueue.clear();
            for (int i = 0; i < localCopyDarknessQueue.size(); i++) {
                this.checkSurroundingBlocksToPropagateDarkness(localCopyDarknessQueue.get(i));
            }
            localCopyDarknessQueue.clear();
            this.safetyThreshold++;
        }
        this.safetyThreshold = 0;
        int[] updatedBlocks;
        for (int i = 0; i < this.previouslyQueuedDarknessUpdate.size(); i++) {
            updatedBlocks = this.previouslyQueuedDarknessUpdate.get(i);
            this.findChunkFromChunkCoordinates(updatedBlocks[0] >> 5, updatedBlocks[1] >> 5, updatedBlocks[2] >> 5).markDirty();
        }
        this.previouslyQueuedDarknessUpdate.clear();
        this.repairDamagedLights(x, y, z, previousLight);
    }

    public void checkSurroundingBlocksToPropagateDarkness(int[] coordinates) {
        this.checkSurroundingBlocksToPropagateDarkness(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    public void checkSurroundingBlocksToPropagateDarkness(int x, int y, int z, int neighborPreviousLightValue) {
        if (this.shouldBlockAddToDarknessQueue(x + 1, y, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x + 1, y, z, this.getBlockLightValue(x + 1, y, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x + 1, y, z, this.getBlockLightValue(x + 1, y, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x - 1, y, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x - 1, y, z, this.getBlockLightValue(x - 1, y, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x - 1, y, z, this.getBlockLightValue(x - 1, y, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y + 1, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y + 1, z, this.getBlockLightValue(x, y + 1, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y + 1, z, this.getBlockLightValue(x, y + 1, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y - 1, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y - 1, z, this.getBlockLightValue(x, y - 1, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y - 1, z, this.getBlockLightValue(x, y - 1, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y, z + 1, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y, z + 1, this.getBlockLightValue(x, y, z + 1)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y, z + 1, this.getBlockLightValue(x, y, z + 1)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y, z - 1, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y, z - 1, this.getBlockLightValue(x, y, z - 1)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y, z - 1, this.getBlockLightValue(x, y, z - 1)});
        }
    }

    private boolean shouldBlockAddToDarknessQueue(int x, int y, int z, int neighborPreviousLightValue) {
        return !Block.list[this.getBlockID(x, y, z)].isSolid && !this.hasBlockAlreadyDarknessQueued(x, y, z) && (this.getBlockLightValue(x, y, z) < neighborPreviousLightValue);
    }

    private void clearBlockLight(int[] coordinates) {
        this.findChunkFromChunkCoordinates(coordinates[0] >> 5, coordinates[1] >> 5, coordinates[2] >> 5).setBlockLightValue(coordinates[0], coordinates[1], coordinates[2], (byte) 0);
    }

    private void clearBlockLight(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null){
            if(chunk.lighting == null){
                chunk.initChunk();
            }
            chunk.setBlockLightValue(x, y, z, (byte) 0);
            chunk.clearBlockLightColor(x, y, z);
        }
    }

    private boolean hasBlockAlreadyDarknessQueued(int x, int y, int z) {
        int[] comparedArray;
        for (int i = 0; i < this.previouslyQueuedDarknessUpdate.size(); i++) {
            comparedArray = this.previouslyQueuedDarknessUpdate.get(i);
            if (comparedArray[0] == x && comparedArray[1] == y && comparedArray[2] == z) {
                return true;
            }
        }
        return false;
    }

    public void propagateLightSource(int x, int y, int z, byte lightValue) {
        this.activeBlockLight[0] = x;
        this.activeBlockLight[1] = y;
        this.activeBlockLight[2] = z;
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = lightValue;
        this.checkSurroundingBlocksToPropagateLight(x, y, z);
        ArrayList<int[]> localCopyLightQueue = new ArrayList<>();
        while (!this.lightUpdateQueue.isEmpty() && this.safetyThreshold < 100000) {
            for (int i = 0; i < this.lightUpdateQueue.size(); i++) {
                this.setBlockLight(this.lightUpdateQueue.get(i));
            }
            localCopyLightQueue.addAll(this.lightUpdateQueue);
            this.lightUpdateQueue.clear();
            for (int i = 0; i < localCopyLightQueue.size(); i++) {
                this.checkSurroundingBlocksToPropagateLight(localCopyLightQueue.get(i));
            }
            localCopyLightQueue.clear();
            this.safetyThreshold++;
        }

        this.safetyThreshold = 0;
        int[] updatedBlocks;
        for (int i = 0; i < this.previouslyQueuedLightUpdate.size(); i++) {
            updatedBlocks = this.previouslyQueuedLightUpdate.get(i);
            this.findChunkFromChunkCoordinates(updatedBlocks[0] >> 5, updatedBlocks[1] >> 5, updatedBlocks[2] >> 5).markDirty();
        }
        this.previouslyQueuedLightUpdate.clear();
    }

    private void checkSurroundingBlocksToPropagateLight(int[] coordinates) {
        this.checkSurroundingBlocksToPropagateLight(coordinates[0], coordinates[1], coordinates[2]);
    }

    public void checkSurroundingBlocksToPropagateLight(int x, int y, int z) {
        if (this.getBlockLightValue(x, y, z) <= 0) {
            return;
        }
        int blockLightValue = this.getBlockLightValue(x, y, z);

        if (this.shouldBlockAddToLightQueue(x + 1, y, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x + 1, y, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x + 1, y, z});
        }
        if (this.shouldBlockAddToLightQueue(x - 1, y, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x - 1, y, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x - 1, y, z});
        }
        if (this.shouldBlockAddToLightQueue(x, y + 1, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y + 1, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y + 1, z});
        }
        if (this.shouldBlockAddToLightQueue(x, y - 1, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y - 1, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y - 1, z});
        }
        if (this.shouldBlockAddToLightQueue(x, y, z + 1, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y, z + 1});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y, z + 1});
        }
        if (this.shouldBlockAddToLightQueue(x, y, z - 1, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y, z - 1});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y, z - 1});
        }
    }

    private boolean shouldBlockAddToLightQueue(int x, int y, int z, int neighborLightValue) {
        return !Block.list[this.getBlockID(x, y, z)].isSolid && !this.hasBlockAlreadyLightQueued(x, y, z) && (this.getBlockLightValue(x, y, z) < neighborLightValue);
    }

    private boolean hasBlockAlreadyLightQueued(int x, int y, int z) {
        int[] comparedArray;
        for (int i = 0; i < this.previouslyQueuedLightUpdate.size(); i++) {
            comparedArray = this.previouslyQueuedLightUpdate.get(i);
            if (comparedArray[0] == x && comparedArray[1] == y && comparedArray[2] == z) {
                return true;
            }
        }
        return false;
    }


    private void setBlockLight(int[] coordinates) {
        Chunk chunk = this.chunkController.findChunkFromChunkCoordinates(coordinates[0] >> 5, coordinates[1] >> 5, coordinates[2] >> 5);
        if (chunk.lighting == null) {
            chunk.initChunk();
        }
        chunk.setBlockLightValue(coordinates[0], coordinates[1], coordinates[2], this.getPropagatedLightValue(coordinates[0], coordinates[1], coordinates[2]));
        chunk.setBlockLightColor(coordinates[0], coordinates[1], coordinates[2], Block.list[this.getBlockID(this.activeBlockLight[0], this.activeBlockLight[1], this.activeBlockLight[2])].lightColor);
    }

    private byte getPropagatedLightValue(int x, int y, int z) {
        byte[] nearbyLightLevels = new byte[6];
        byte currentLightLevel = this.getBlockLightValue(x, y, z);
        nearbyLightLevels[0] = this.getBlockLightValue(x + 1, y, z);
        nearbyLightLevels[1] = this.getBlockLightValue(x - 1, y, z);
        nearbyLightLevels[2] = this.getBlockLightValue(x, y, z + 1);
        nearbyLightLevels[3] = this.getBlockLightValue(x, y, z - 1);
        nearbyLightLevels[4] = this.getBlockLightValue(x, y + 1, z);
        nearbyLightLevels[5] = this.getBlockLightValue(x, y - 1, z);
        Arrays.sort(nearbyLightLevels);
        nearbyLightLevels[5]--;
        if (nearbyLightLevels[5] <= 0) {
            return 0;
        } else if (nearbyLightLevels[5] <= currentLightLevel) {
            return currentLightLevel;
        } else {
            return nearbyLightLevels[5];
        }
    }

    public int findNextHighestSolidBlock(int x, int y, int z) {
        for (int i = 1; i < 4096; i++) {
            if (Block.list[this.findChunkFromChunkCoordinates(x >> 5, (y - i) >> 5, z >> 5).blocks[Chunk.getBlockIndexFromCoordinates(x, y - i, z)]].isSolid) {
                return y - i;
            }
        }
        return y;
    }

    public synchronized short getBlockID(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.blocks != null) {
                return chunk.getBlockID(x, y, z);
            } else {
                return Block.air.ID;
            }
        } else {
            return Block.air.ID;
        }
    }


    private byte getSurroundingLightLevel(int x, int y, int z) {
        byte[] nearbyLightLevels = new byte[6];
        byte currentLightLevel = this.getBlockSkyLightValue(x, y, z);
        nearbyLightLevels[0] = this.getBlockSkyLightValue(x + 1, y, z);
        nearbyLightLevels[1] = this.getBlockSkyLightValue(x - 1, y, z);
        nearbyLightLevels[2] = this.getBlockSkyLightValue(x, y, z + 1);
        nearbyLightLevels[3] = this.getBlockSkyLightValue(x, y, z - 1);
        nearbyLightLevels[4] = this.getBlockSkyLightValue(x, y + 1, z);
        nearbyLightLevels[5] = this.getBlockSkyLightValue(x, y - 1, z);
        Arrays.sort(nearbyLightLevels);
        if (nearbyLightLevels[5] < 0) {
            return 0;
        } else if (nearbyLightLevels[5] - 1 <= currentLightLevel) {
            return currentLightLevel;
        } else {
            return (byte) (nearbyLightLevels[5] - 1);
        }
    }

    public void setSkyLight(int x, int y, int z, byte lightValue){
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null){
            chunk.setBlockSkyLightValue(x,y,z, lightValue);
        }
    }

    public void propagateSkyLight(int x, int y, int z, ArrayList<int[]> skyLightUpdateQueue, ArrayList<int[]> previousSkyLightUpdateQueue){
        this.setSkyLight(x,y,z, (byte)15);
        if(this.willSkyLightQueueMore(x,y,z, previousSkyLightUpdateQueue)) {
            skyLightUpdateQueue.add(new int[]{x, y, z});
            ArrayList<int[]> localCopySkyLightUpdateQueue = new ArrayList();
            int[] lightCoordinates;
            while (!skyLightUpdateQueue.isEmpty() && this.safetyThreshold < 100000) {
                for (int i = 0; i < skyLightUpdateQueue.size(); i++) {
                    lightCoordinates = skyLightUpdateQueue.get(i);
                    this.setSkyLight(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2], this.getSurroundingLightLevel(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2]));
                }
                localCopySkyLightUpdateQueue.addAll(skyLightUpdateQueue);
                skyLightUpdateQueue.clear();
                for (int i = 0; i < localCopySkyLightUpdateQueue.size(); i++) {
                    this.queueSurroundingBlocksToPropagateSkyLight(localCopySkyLightUpdateQueue.get(i), this.getBlockSkyLightValue(localCopySkyLightUpdateQueue.get(i)), skyLightUpdateQueue, previousSkyLightUpdateQueue);
                }
                localCopySkyLightUpdateQueue.clear();
                this.safetyThreshold++;
            }
            this.safetyThreshold = 0;
        }
    }

    public void queueSurroundingBlocksToPropagateSkyLight(int[] lightCoordinates, byte currentLightLevel, ArrayList<int[]> skyLightUpdateQueue, ArrayList<int[]> previousSkyLightUpdateQueue){
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1, previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1, previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1});
        }
    }

    public boolean willSkyLightQueueMore(int x, int y, int z, ArrayList<int[]> previousSkyLightUpdateQueue){
        return this.willSkyLightQueueMore(new int[]{x,y,z}, this.getSurroundingLightLevel(x,y,z), previousSkyLightUpdateQueue);
    }

    public boolean willSkyLightQueueMore(int[] lightCoordinates, byte currentLightLevel, ArrayList<int[]> previousSkyLightUpdateQueue){
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0  && this.getBlockSkyLightValue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) == 0  &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0  && this.getBlockSkyLightValue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) == 0 &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2])].isSolid && currentLightLevel > 0  && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) == 0 &&   !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) == 0  &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) == 0 &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1, previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) == 0 &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1, previousSkyLightUpdateQueue)){
            return true;
        }
        return false;
    }

    private boolean blockHasEnteredSkyLightQueue(int x, int y, int z, ArrayList<int[]> previousSkyLightUpdateQueue){
        int[] coordinates;
        for(int i = 0; i < previousSkyLightUpdateQueue.size(); i++){
            coordinates = previousSkyLightUpdateQueue.get(i);
            if(coordinates[0] == x && coordinates[1] == y && coordinates[2] == z){
                return true;
            }
        }
        return false;
    }

    public synchronized byte getBlockSkyLightValue(int[] coordinates){
        return this.getBlockSkyLightValue(coordinates[0], coordinates[1], coordinates[2]);
    }

    public synchronized byte getBlockSkyLightValue(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.skyLight != null) {
                return chunk.getSkyLightValue(x, y, z);
            } else {
                return 15;
            }
        } else {
            return 15;
        }
    }

    public synchronized byte getBlockLightValue(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.lighting != null) {
                return chunk.getBlockLightValue(x, y, z);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public double getAverageTemperature(int x, int y, int z){
        if(this instanceof WorldEarth){
            return ((((WorldEarth) this).temperatureNoise1.getNoiseRaw(x / 4, z / 4) + (((WorldEarth)this).temperatureNoise2.getNoiseRaw(x / 4, z / 4))));
        }
        return 0;
    }

    public double getAverageRainfall(int x, int z){
        if(this instanceof WorldEarth){
            return ((((WorldEarth) this).rainfallNoise1.getNoiseRaw(x / 4, z / 4) + (((WorldEarth) this).rainfallNoise2.getNoiseRaw(x / 4, z / 4))));
        }
        return 0;
    }

    public double getRainfall(int x, int z){
        if(this instanceof WorldEarth) {
            return ((((WorldEarth) this).rainfallNoise1.getNoiseRaw(x / 4, z / 4) + (((WorldEarth) this).rainfallNoise2.getNoiseRaw(x / 4, z / 4) )));
        }
        return 0;
    }

    public double getTemperatureWithoutTimeOfDay(int x, int y, int z){
        if(this instanceof WorldEarth) {
            double temp = ((((WorldEarth) this).temperatureNoise1.getNoiseRaw(x / 4, z / 4) + (((WorldEarth)this).temperatureNoise2.getNoiseRaw(x / 4, z / 4))));
            if (y > 0) {
                temp -= ((y / 5d) * 0.01);
            }
            double latitude = Math.abs(x);
            latitude = (int) ((latitude / (this.size / 2f)) * 90f);
            double varianceFromLat = this.getTempVarianceFromLat(latitude);
            double tempChangeFromLat = this.getTempChangeFromLatitude(latitude);
            double partOfOrbit = this.getPortionOfOrbit(); //0 - 0.25 spring, 0.25 - 0.5 summer, 0.5 - 0.75 fall, 0.75 - 1.0/0.0 winter
            double radians = Math.toRadians(360 * partOfOrbit);

            temp += tempChangeFromLat;
            temp = x < 0 ? temp + (MathUtil.sin(radians) * varianceFromLat) : temp + (MathUtil.sin(radians + Math.PI) * varianceFromLat);


            return temp;
        }
        return 0;
    }

    public double getDisplayTemperature(int x, int y, int z){
        return Math.floor((this.getTemperatureWithTimeOfDay(x,y,z) * 100) * 10) / 10.0;
    }

    public double getTemperatureWithTimeOfDay(int x, int y, int z){
        if(this instanceof WorldEarth) {
            double temp = (((WorldEarth) this).temperatureNoise1.getNoiseRaw(x / 4, z / 4) + (((WorldEarth) this).temperatureNoise2.getNoiseRaw(x / 4, z / 4)));
            if (y > 0) {
                temp -= ((y / 5d) * 0.01);
            }
            double latitude = Math.abs(x);
            latitude = (int) ((latitude / (this.size / 2f)) * 90f);
            double varianceFromLat = this.getTempVarianceFromLat(latitude);
            double tempChangeFromLat = this.getTempChangeFromLatitude(latitude);
            double partOfOrbit = this.getPortionOfOrbit(); //0 - 0.25 spring, 0.25 - 0.5 summer, 0.5 - 0.75 fall, 0.75 - 1.0/0.0 winter
            double radians = Math.toRadians(360 * partOfOrbit);

            temp += tempChangeFromLat;
            temp = x < 0 ? temp + (MathUtil.sin(radians) * varianceFromLat) : temp + (MathUtil.sin(radians + Math.PI) * varianceFromLat);


            return temp;
        }
        return 0;
    }


    public double getTemperature(int x, int y, int z){
        if(this instanceof WorldEarth){
            return this.getTemperatureWithoutTimeOfDay(x,y,z);
        }
        return 0;
    }


    public double getPortionOfOrbit(){
        return ((double) (SpaceGame.instance.save.time % SpaceGame.instance.everything.earth.orbitalPeriod) / SpaceGame.instance.everything.earth.orbitalPeriod);
    }

    public double getTempVarianceFromLat(double latitude){
        return  ((latitude) / 90f);
    }

    public double getTempChangeFromLatitude(double latitude){
        return -((latitude - (23.5)) / 65f);
    }


    public synchronized float[] getBlockLightColor(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.lightColor != null && this.getBlockSkyLightValue(x,y,z) < 1) {
                return chunk.getBlockLightColor(x, y, z);
            } else {
                return this.skyLightColor;
            }
        } else {
            return this.skyLightColor;
        }
    }

    public int getBlockLightColorAsInt(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.lightColor != null) {
                return chunk.lightColor[Chunk.getBlockIndexFromCoordinates(x, y, z)];
            } else {
                return new Color(this.skyLightColor[0], this.skyLightColor[1], this.skyLightColor[2],0).getRGB();
            }
        } else {
            return new Color(this.skyLightColor[0], this.skyLightColor[1], this.skyLightColor[2],0).getRGB();
        }
    }

    public synchronized boolean doesBlockHaveSkyAccess(int x, int y, int z) {
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        x %= 32;
        z %= 32;
        if(lightMap.lightMap[x + (z << 5)] < y){
            if(this.isLineOfBlocksClear(x,y,z, lightMap)){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private synchronized boolean isLineOfBlocksClear(int x, int y, int z, ChunkColumnSkylightMap lightMap){
        int endY = lightMap.lightMap[x + (z << 5)];

        for(int i = y; i <= endY; i++){
            if(this.doesBlockStopSkyLight(x,i,z)){
                return false;
            }
        }

        return true;
    }

    private synchronized boolean doesBlockStopSkyLight(int x, int y, int z){
        String blockName = Block.list[this.getBlockID(x,y,z)].blockName;
        switch (blockName){
            case "LEAF", "AIR":
                return false;
            default:
                return true;
        }
    }

    public byte getNearestSkyLightValue(int x, int y, int z) {
        return 0;
    }

    public boolean chunkSurroundedSixSides(int x, int y, int z) {
        Chunk upperChunk = this.findChunkFromChunkCoordinates(x, y + 1, z);
        Chunk lowerChunk = this.findChunkFromChunkCoordinates(x, y - 1, z);
        Chunk northChunk = this.findChunkFromChunkCoordinates(x - 1, y, z);
        Chunk southChunk = this.findChunkFromChunkCoordinates(x + 1, y, z);
        Chunk eastChunk = this.findChunkFromChunkCoordinates(x, y, z - 1);
        Chunk westChunk = this.findChunkFromChunkCoordinates(x, y, z + 1);

        return upperChunk != null && lowerChunk != null && northChunk != null && southChunk != null && eastChunk != null && westChunk != null;
    }

    public boolean chunkFullySurrounded(int x, int y, int z) {
        Chunk chunk1 = this.findChunkFromChunkCoordinates(x - 1, y - 1, z - 1);
        Chunk chunk2 = this.findChunkFromChunkCoordinates(x, y - 1, z - 1);
        Chunk chunk3 = this.findChunkFromChunkCoordinates(x + 1, y - 1, z - 1);

        Chunk chunk4 = this.findChunkFromChunkCoordinates(x - 1, y, z - 1);
        Chunk chunk5 = this.findChunkFromChunkCoordinates(x, y, z - 1);
        Chunk chunk6 = this.findChunkFromChunkCoordinates(x + 1, y, z - 1);

        Chunk chunk7 = this.findChunkFromChunkCoordinates(x - 1, y + 1, z - 1);
        Chunk chunk8 = this.findChunkFromChunkCoordinates(x, y + 1, z - 1);
        Chunk chunk9 = this.findChunkFromChunkCoordinates(x + 1, y + 1, z - 1);

        Chunk chunk10 = this.findChunkFromChunkCoordinates(x - 1, y - 1, z);
        Chunk chunk11 = this.findChunkFromChunkCoordinates(x, y - 1, z);
        Chunk chunk12 = this.findChunkFromChunkCoordinates(x + 1, y - 1, z);

        Chunk chunk13 = this.findChunkFromChunkCoordinates(x - 1, y, z);

        Chunk chunk15 = this.findChunkFromChunkCoordinates(x + 1, y, z);

        Chunk chunk16 = this.findChunkFromChunkCoordinates(x - 1, y + 1, z);
        Chunk chunk17 = this.findChunkFromChunkCoordinates(x, y + 1, z);
        Chunk chunk18 = this.findChunkFromChunkCoordinates(x + 1, y + 1, z);

        Chunk chunk19 = this.findChunkFromChunkCoordinates(x - 1, y - 1, z + 1);
        Chunk chunk20 = this.findChunkFromChunkCoordinates(x, y - 1, z + 1);
        Chunk chunk21 = this.findChunkFromChunkCoordinates(x + 1, y - 1, z + 1);

        Chunk chunk22 = this.findChunkFromChunkCoordinates(x - 1, y, z + 1);
        Chunk chunk23 = this.findChunkFromChunkCoordinates(x, y, z + 1);
        Chunk chunk24 = this.findChunkFromChunkCoordinates(x + 1, y, z + 1);

        Chunk chunk25 = this.findChunkFromChunkCoordinates(x - 1, y + 1, z + 1);
        Chunk chunk26 = this.findChunkFromChunkCoordinates(x, y + 1, z + 1);
        Chunk chunk27 = this.findChunkFromChunkCoordinates(x + 1, y + 1, z + 1);

        return chunk1 != null && chunk2 != null && chunk3 != null && chunk4 != null && chunk5 != null
                && chunk6 != null && chunk7 != null && chunk8 != null && chunk9 != null
                && chunk10 != null && chunk11 != null && chunk12 != null && chunk13 != null
                && chunk15 != null && chunk16 != null && chunk17 != null && chunk18 != null
                && chunk19 != null && chunk20 != null && chunk21 != null && chunk22 != null
                && chunk23 != null && chunk24 != null && chunk25 != null && chunk26 != null && chunk27 != null;
    }


    public void renderWorld() {
        this.chunkController.renderWorld();
    }

    public void handleSpecialBlockRightClickFunctions(Block block, int x, int y, int z){
        if(block == Block.air)return;
        short playerHeldItem = this.sg.save.thePlayer.getHeldItem();

        if(block instanceof BlockBerryBush && MouseListener.rightClickReleased){
            if(this.sg.save.thePlayer.getHeldItem() != Item.block.ID && this.sg.save.thePlayer.getHeldItem() != Item.torch.ID){
                block.onRightClick(x, y, z, this, this.sg.save.thePlayer);
                MouseListener.rightClickReleased = false;
                return;
            }
        }
        if(block instanceof BlockLog && playerHeldItem != Item.NULL_ITEM_REFERENCE && MouseListener.rightClickReleased){
            if(Item.list[playerHeldItem] instanceof ItemAxe){
                Item.list[playerHeldItem].onRightClick(x, y, z, this, this.sg.save.thePlayer);
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block instanceof BlockCampFireUnlit && playerHeldItem != Item.NULL_ITEM_REFERENCE && MouseListener.rightClickReleased){
            if(playerHeldItem == Item.fireWood.ID) {
                int logCount = ((BlockCampFireUnlit) block).getLogCount();
                switch (logCount) {
                    case 0 -> this.setBlockWithNotify(x, y, z, Block.campFire1FireWood.ID);
                    case 1 -> this.setBlockWithNotify(x, y, z, Block.campFire2FireWood.ID);
                    case 2 -> this.setBlockWithNotify(x, y, z, Block.campFire3Firewood.ID);
                    case 3 -> this.setBlockWithNotify(x, y, z, Block.campFire4FireWood.ID);
                }
                if(logCount != 4){
                    this.sg.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block instanceof BlockCampFireUnlit && MouseListener.rightClickReleased){
            if((((BlockCampFireUnlit) block).getLogCount() == 4)){
                if (playerHeldItem == Item.stoneFragments.ID) {
                    this.sg.setNewGui(new GuiLightFire(SpaceGame.instance, x, y, z));
                    MouseListener.rightClickReleased = false;
                    return;
                }
            }
        }

        if(block instanceof BlockCampFire && MouseListener.rightClickReleased){
            if(playerHeldItem == Item.unlitTorch.ID) {
                SpaceGame.instance.save.thePlayer.removeItemFromInventory();
                if (!SpaceGame.instance.save.thePlayer.addItemToInventory(Item.torch.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY)) {
                    this.addEntity(new EntityItem(SpaceGame.instance.save.thePlayer.x, SpaceGame.instance.save.thePlayer.y, SpaceGame.instance.save.thePlayer.z, Item.torch.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY));
                    MouseListener.rightClickReleased = false;
                    return;
                }
            }
        }


        if(block.ID == Block.itemStone.ID && MouseListener.rightClickReleased){
            if(playerHeldItem == Item.stone.ID){
                this.sg.setNewGui(new GuiCraftingStoneTools(this.sg, x,y,z));
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block.ID == Block.itemStick.ID && MouseListener.rightClickReleased){
            if(playerHeldItem == Item.stoneFragments.ID){
                this.setBlockWithNotify(x,y,z, Block.air.ID);
                this.addEntity(new EntityItem(x + 0.5, y + 0.1, z + 0.5, Item.unlitTorch.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY));
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block.ID == Block.campFireNoFirewood.ID && MouseListener.rightClickReleased){
           if(playerHeldItem != Item.NULL_ITEM_REFERENCE){
               if(Item.list[playerHeldItem].toolType.equals(Item.ITEM_TOOL_TYPE_KNIFE) && this.sg.save.thePlayer.containsAmountOfItem(Item.straw.ID, 8)){
                   this.sg.setNewGui(new GuiCraftingStrawStorage(this.sg, x, y, z));
                   for(int i = 0; i < 8; i++){
                       this.sg.save.thePlayer.removeSpecificItemFromInventory(Item.straw.ID);
                   }
                   MouseListener.rightClickReleased = false;
                   return;
               }
           }
        }

        if(block.ID == Block.strawChestTier0.ID && playerHeldItem == Item.straw.ID && MouseListener.rightClickReleased){
            if(this.sg.save.thePlayer.getHeldItemCount() >= 8){
                this.setBlockWithNotify(x,y,z, Block.strawChestTier1.ID);
                for(int i = 0; i < 8; i++){
                    this.sg.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block.ID == Block.strawChestTier1.ID && playerHeldItem == Item.straw.ID && MouseListener.rightClickReleased){
            if(this.sg.save.thePlayer.getHeldItemCount() >= 8){
                this.setBlockWithNotify(x,y,z, Block.strawChest.ID);
                this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addChestLocation(x, y, z,  new Inventory(((BlockContainer)(Block.strawChest)).inventorySize, 9));
                for(int i = 0; i < 8; i++){
                    this.sg.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block.ID == Block.strawBasketTier0.ID && playerHeldItem == Item.straw.ID && MouseListener.rightClickReleased){
            if(this.sg.save.thePlayer.getHeldItemCount() >= 4){
                this.setBlockWithNotify(x,y,z, Block.air.ID);
                this.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.strawBasket.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
                for(int i = 0; i < 4; i++){
                    this.sg.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(block instanceof BlockContainer && MouseListener.rightClickReleased){
            ChestLocation location = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).getChestLocation(x,y,z);
            this.sg.setNewGui(new GuiInventoryStrawChest(this.sg, this.sg.save.thePlayer.inventory, location.inventory));
            MouseListener.rightClickReleased = false;
            return;
        }


    }

    private Chunk[] getSurroundingChunksAndCurrentChunk(int x, int y, int z){
        Chunk[] chunks = new Chunk[27];
        chunks[0] = this.findChunkFromChunkCoordinates(x - 1, y - 1, z - 1);
        chunks[1] = this.findChunkFromChunkCoordinates(x, y - 1, z - 1);
        chunks[2] = this.findChunkFromChunkCoordinates(x + 1, y - 1, z - 1);

        chunks[3] = this.findChunkFromChunkCoordinates(x - 1, y, z - 1);
        chunks[4] = this.findChunkFromChunkCoordinates(x, y, z - 1);
        chunks[5] = this.findChunkFromChunkCoordinates(x + 1, y, z - 1);

        chunks[6] = this.findChunkFromChunkCoordinates(x - 1, y + 1, z - 1);
        chunks[7] = this.findChunkFromChunkCoordinates(x, y + 1, z - 1);
        chunks[8] = this.findChunkFromChunkCoordinates(x + 1, y + 1, z - 1);

        chunks[9] = this.findChunkFromChunkCoordinates(x - 1, y - 1, z);
        chunks[10] = this.findChunkFromChunkCoordinates(x, y - 1, z);
        chunks[11] = this.findChunkFromChunkCoordinates(x + 1, y - 1, z);

        chunks[12] = this.findChunkFromChunkCoordinates(x - 1, y, z);
        chunks[13] = this.findChunkFromChunkCoordinates(x, y, z);
        chunks[14] = this.findChunkFromChunkCoordinates(x + 1, y, z);

        chunks[15] = this.findChunkFromChunkCoordinates(x - 1, y + 1, z);
        chunks[16] = this.findChunkFromChunkCoordinates(x, y + 1, z);
        chunks[17] = this.findChunkFromChunkCoordinates(x + 1, y + 1, z);

        chunks[18] = this.findChunkFromChunkCoordinates(x - 1, y - 1, z + 1);
        chunks[19] = this.findChunkFromChunkCoordinates(x, y - 1, z + 1);
        chunks[20] = this.findChunkFromChunkCoordinates(x + 1, y - 1, z + 1);

        chunks[21] = this.findChunkFromChunkCoordinates(x - 1, y, z + 1);
        chunks[22] = this.findChunkFromChunkCoordinates(x, y, z + 1);
        chunks[23] = this.findChunkFromChunkCoordinates(x + 1, y, z + 1);

        chunks[24] = this.findChunkFromChunkCoordinates(x - 1, y + 1, z + 1);
        chunks[25] = this.findChunkFromChunkCoordinates(x, y + 1, z + 1);
        chunks[26] = this.findChunkFromChunkCoordinates(x + 1, y + 1, z + 1);

        return chunks;
    }

    private ArrayList<Entity> getEntitiesInChunks(Chunk[] chunks){
        ArrayList<Entity> entities = new ArrayList<>();

        for(int i = 0; i < chunks.length; i++){
            entities.addAll(chunks[i].entities);
        }

        return entities;
    }

    private boolean hasHitEntity(double x, double y, double z){
        int chunkX = MathUtil.floorDouble(x) >> 5;
        int chunkY = MathUtil.floorDouble(y) >> 5;
        int chunkZ = MathUtil.floorDouble(z) >> 5;

        Vector3f movementVector = new Vector3f((float) (x - this.sg.save.thePlayer.x), 1f, (float) (z - this.sg.save.thePlayer.z)).normalize();
        movementVector.mul(0.1f);
        ArrayList<Entity> entities = this.getEntitiesInChunks(this.getSurroundingChunksAndCurrentChunk(chunkX, chunkY, chunkZ));

        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i).boundingBox.pointInsideBoundingBox(x,y,z)) { //This determines if you hit an entity
                entities.get(i).damage(movementVector, this.sg.save.thePlayer.getAttackDamageValue());
                entities.get(i).setLastEntityToHit(this.sg.save.thePlayer);
                return true;
            }
        }
        return false;
    }

    public boolean intersectsBlockBoundingBox(Block block, double x, double y, double z){
        return block.standardCollisionBoundingBox.pointInsideBoundingBox(x + (int)-x,y + (int)-y, z + (int)-z); //Localizes the world coords back to model space
    }


    public void handleLeftClick() {
        if (!this.paused && this.sg.currentGui instanceof GuiInGame && this.delayWhenExitingUI <= 0) {
            double[] rayCast = SpaceGame.camera.rayCast(3);
            final double multiplier = 0.05D;
            final double xDif = (rayCast[0] - this.sg.save.thePlayer.x);
            final double yDif = (rayCast[1] - (this.sg.save.thePlayer.y + this.sg.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - this.sg.save.thePlayer.z);

            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            for (int loopPass = 0; loopPass < 30; loopPass++) {
                if(this.hasHitEntity(this.sg.save.thePlayer.x + xDif * multiplier * loopPass, this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass, this.sg.save.thePlayer.z + zDif * multiplier * loopPass))return;
                blockX = MathUtil.floorDouble(this.sg.save.thePlayer.x + xDif * multiplier * loopPass);
                blockY = MathUtil.floorDouble(this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                blockZ = MathUtil.floorDouble(this.sg.save.thePlayer.z + zDif * multiplier * loopPass);

                Block checkedBlock = Block.list[this.getBlockID(blockX, blockY, blockZ)];

                if(!this.intersectsBlockBoundingBox(checkedBlock,this.sg.save.thePlayer.x + xDif * multiplier * loopPass, this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass, this.sg.save.thePlayer.z + zDif * multiplier * loopPass))continue;

                if (GuiInGame.isBlockVisible(blockX, blockY, blockZ) && (checkedBlock.ID != Block.air.ID && checkedBlock.ID != Block.water.ID)) {
                    if(this.sg.save.thePlayer.blockLookingAt[0] == blockX && this.sg.save.thePlayer.blockLookingAt[1] == blockY && this.sg.save.thePlayer.blockLookingAt[2] == blockZ){
                        short playerHeldItem = SpaceGame.instance.save.thePlayer.getHeldItem();
                        if(playerHeldItem == Item.NULL_ITEM_REFERENCE){playerHeldItem = 0;}
                        this.sg.save.thePlayer.breakTimer++;
                        if(checkedBlock.hardness > (SpaceGame.instance.save.thePlayer.hardnessThreshold + Item.list[playerHeldItem].hardness)){
                            this.sg.save.thePlayer.breakTimer = 1;
                        }
                        if(checkedBlock.requiresTool && !checkedBlock.toolType.equals(Item.list[playerHeldItem].toolType)){
                            this.sg.save.thePlayer.breakTimer = 1;
                        }
                        if(this.sg.save.thePlayer.breakTimer >= checkedBlock.getDynamicBreakTimer() && checkedBlock.breakTimer >= 0){
                            checkedBlock.onLeftClick(blockX, blockY, blockZ, this, this.sg.save.thePlayer);
                            if(checkedBlock.droppedItemID != Item.NULL_ITEM_REFERENCE) {
                                if (checkedBlock.droppedItemID != Item.block.ID) {
                                    if(checkedBlock.itemDropChance > SpaceGame.globalRand.nextFloat()) {
                                        this.findChunkFromChunkCoordinates(blockX >> 5, blockY >> 5, blockZ >> 5).addEntityToList(new EntityItem(blockX + 0.5 + new Random().nextDouble(-0.3, 0.3), blockY + 0.5 + new Random().nextDouble(-0.3, 0.3), blockZ + 0.5 + new Random().nextDouble(-0.3, 0.3), checkedBlock.droppedItemID, (short)0, (byte) 1, Item.list[checkedBlock.droppedItemID].durability));
                                    }
                                } else {
                                    this.findChunkFromChunkCoordinates(blockX >> 5, blockY >> 5, blockZ >> 5).addEntityToList(new EntityBlock(blockX + 0.5 + new Random().nextDouble(-0.3, 0.3), blockY + 0.5 + new Random().nextDouble(-0.3, 0.3), blockZ + 0.5 + new Random().nextDouble(-0.3, 0.3), checkedBlock.itemMetadata, (byte) 1));
                                }
                            }
                            this.sg.save.thePlayer.breakTimer = 0;
                        } else if(this.sg.save.time % 20 == 0) {
                            new SoundPlayer(this.sg).playSound(blockX, blockY, blockZ, new Sound(checkedBlock.stepSound, false),  new Random().nextFloat(0.4F, 0.7F));
                        }
                    } else {
                        this.sg.save.thePlayer.breakTimer = 0;
                    }
                }
                if (checkedBlock.canBeBroken) {
                    break;
                }
            }
        }
    }

    public void handleRightClick() {
        if (this.sg.save.thePlayer != null && !this.paused && MouseListener.rightClickReleased && this.sg.currentGui instanceof GuiInGame && this.delayWhenExitingUI <= 0) {
            short item = SpaceGame.instance.save.thePlayer.getHeldItem();
            if(item != Item.NULL_ITEM_REFERENCE) {
                Item.list[item].onRightClick(MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x), MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y),  MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z), this, SpaceGame.instance.save.thePlayer);
            }
            double[] rayCast = SpaceGame.camera.rayCast(3);
            final double multiplier = 0.05D;
            final double xDif = (rayCast[0] - this.sg.save.thePlayer.x);
            final double yDif = (rayCast[1] - (this.sg.save.thePlayer.y + this.sg.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - this.sg.save.thePlayer.z);
            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;

            for (int loopPass = 0; loopPass < 30; loopPass++) {
                blockX = MathUtil.floorDouble(this.sg.save.thePlayer.x + xDif * multiplier * loopPass);
                blockY = MathUtil.floorDouble(this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                blockZ = MathUtil.floorDouble(this.sg.save.thePlayer.z + zDif * multiplier * loopPass);
                Block checkedBlock = Block.list[this.getBlockID(blockX, blockY, blockZ)];
                this.handleSpecialBlockRightClickFunctions(checkedBlock, blockX, blockY, blockZ);
                if (checkedBlock.isSolid) {
                    if(checkedBlock.ID != Block.water.ID) {
                        loopPass--;
                        if(this.sg.save.thePlayer.getHeldItem() != Item.block.ID && this.sg.save.thePlayer.getHeldItem() != Item.torch.ID){
                            checkedBlock.onRightClick(blockX, blockY, blockZ, this, this.sg.save.thePlayer);
                        }
                    }
                    if (loopPass < 10 && Block.list[this.sg.save.thePlayer.getHeldBlock()].isSolid && this.sg.save.thePlayer.pitch > -81 || loopPass < 18 && Block.list[this.sg.save.thePlayer.getHeldBlock()].isSolid && this.sg.save.thePlayer.pitch < -81) {
                        break;
                    } else {
                        if (this.sg.save.thePlayer.getHeldItem() == Item.torch.ID) {
                            if (!Block.list[this.getBlockID((int) (this.sg.save.thePlayer.x + xDif * multiplier * loopPass), (int) (this.sg.save.thePlayer.y + yDif * multiplier * (loopPass + 1)), (int) (this.sg.save.thePlayer.z + zDif * multiplier * (loopPass + 1)))].isSolid) {
                                if ((float) ((this.sg.save.thePlayer.x + xDif * multiplier * (loopPass + 1)) - (this.sg.save.thePlayer.x + xDif * multiplier * loopPass)) > 0) {
                                    Block.facingDirection = 1;
                                } else {
                                    Block.facingDirection = 2;
                                }
                            } else if (!Block.list[this.getBlockID((int) (this.sg.save.thePlayer.x + xDif * multiplier * (loopPass + 1)), (int) (this.sg.save.thePlayer.y + yDif * multiplier * loopPass), (int) (this.sg.save.thePlayer.z + zDif * multiplier * (loopPass + 1)))].isSolid) {
                                if (!((float) ((this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * (loopPass + 1)) - (this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass)) < 0)) {
                                    break;
                                }
                            } else if (!Block.list[this.getBlockID((int) (this.sg.save.thePlayer.x + xDif * multiplier * (loopPass + 1)), (int) (this.sg.save.thePlayer.y + yDif * multiplier * (loopPass + 1)), (int) (this.sg.save.thePlayer.z + zDif * multiplier * loopPass))].isSolid) {
                                if ((float) ((this.sg.save.thePlayer.z + zDif * multiplier * (loopPass + 1)) - (this.sg.save.thePlayer.z + zDif * multiplier * loopPass)) > 0) {
                                    Block.facingDirection = 3;
                                } else {
                                    Block.facingDirection = 4;
                                }
                            }
                        }

                        final int origBlockX = blockX;
                        final int origBlockY = blockY;
                        final int origBlockZ = blockZ;
                        int difCount = 0;

                        blockX = MathUtil.floorDouble(this.sg.save.thePlayer.x + xDif * multiplier * loopPass);
                        blockY = MathUtil.floorDouble(this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                        blockZ = MathUtil.floorDouble(this.sg.save.thePlayer.z + zDif * multiplier * loopPass);

                        boolean xChangePos = false;
                        boolean yChangePos = false;
                        boolean zChangePos = false;
                        boolean xChangeNeg = false;
                        boolean yChangeNeg = false;
                        boolean zChangeNeg = false;
                        if(blockX > origBlockX) {
                            difCount++;
                            xChangePos = true;
                        } else if(blockX < origBlockX){
                            difCount++;
                            xChangeNeg = true;
                        }

                        if(blockY > origBlockY) {
                            difCount++;
                            yChangePos = true;
                        } else if(blockY < origBlockY){
                            difCount++;
                            yChangeNeg = true;
                        }

                        if(blockZ > origBlockZ){
                            difCount++;
                            zChangePos = true;
                        } else if(blockZ < origBlockZ){
                            difCount++;
                            zChangeNeg = true;
                        }

                        if(difCount < 2) {
                            checkedBlock.onRightClick(blockX, blockY, blockZ, this, this.sg.save.thePlayer);
                            break;
                        } else {

                            if(xChangePos){
                                checkedBlock.onRightClick(blockX - 1, blockY, blockZ, this, this.sg.save.thePlayer);
                                break;
                            } else if(xChangeNeg){
                                checkedBlock.onRightClick(blockX + 1, blockY, blockZ, this, this.sg.save.thePlayer);
                                break;
                            }

                            if(yChangePos){
                                checkedBlock.onRightClick(blockX, blockY - 1, blockZ, this, this.sg.save.thePlayer);
                                break;
                            } else if(yChangeNeg){
                                checkedBlock.onRightClick(blockX, blockY + 1, blockZ, this, this.sg.save.thePlayer);
                                break;
                            }

                            if(zChangePos){
                                checkedBlock.onRightClick(blockX, blockY, blockZ - 1, this, this.sg.save.thePlayer);
                                break;
                            } else if(zChangeNeg){
                                checkedBlock.onRightClick(blockX, blockY, blockZ + 1, this, this.sg.save.thePlayer);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
