package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockLog;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenTree extends WorldGen {
    public ArrayList<int[]> blockPos = new ArrayList<>();
    public boolean decayIntoDirt;

    public WorldGenTree(Chunk chunk, WorldEarth worldEarth, int index) {
        if(chunk.blocks[index] != Block.grass.ID && chunk.blocks[index] != Block.dirt.ID)return; //Guard clause to prevent trees from generating when they shouldnt
        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.rand = new Random();
        this.startGenerate();
    }


    @Override
    public void startGenerate() {
        final int startingX = this.chunk.getBlockXFromIndex(index);
        final int startingY = this.chunk.getBlockYFromIndex(index) + 1;
        final int startingZ = this.chunk.getBlockZFromIndex(index);
        final int height = this.rand.nextInt(6,11);
        int x = startingX;
        int y = startingY;
        int z = startingZ;
        int size = this.rand.nextInt(13, 17);
        int block = BlockLog.getIDFromParameters(size, 1);
        if(block == Block.oakLogFullSizeNormal.ID || block == Block.oakLogFullSizeNorthSouth.ID || block == Block.oakLogFullSizeEastWest.ID){
            this.decayIntoDirt = true;
        }
        boolean decreaseLogSize = false;
        for(int i = 0; i < height; i++){
            if(decreaseLogSize){
                block = BlockLog.getIDFromParameters(size - (i/2), 1);
                decreaseLogSize = false;
            } else {
                decreaseLogSize = true;
            }
            this.blockPos.add(new int[]{x, y, z, block});
            y++;
        }


        this.generateLeaves(x, y - 2, z, height);
        this.generate();
    }



    @Override
    public void generate() {
        if (!this.checkAreaClear()) {return;}
        Chunk chunk;
        int[] blockData;
        for (int i = 0; i < this.blockPos.size(); i++) {
            blockData = this.blockPos.get(i);
            this.worldEarth.setBlock(blockData[0], blockData[1], blockData[2], (short)blockData[3]);
            chunk = this.worldEarth.findChunkFromChunkCoordinates(blockData[0] >> 5, blockData[1] >> 5, blockData[2] >> 5);
            this.addChunkToRebuildQueue(chunk);
        }

        for (int i = 0; i < this.blockPos.size(); i++) {
            blockData = this.blockPos.get(i);
            this.worldEarth.notifySurroundingBlockWithoutRebuild(blockData[0], blockData[1], blockData[2]);
        }

        int numberOfSticks = rand.nextInt(4);
        int x = this.chunk.getBlockXFromIndex(index);
        int y;
        int z = this.chunk.getBlockZFromIndex(index);
        boolean negative;
        for(int i = 0; i < numberOfSticks; i++){
            negative = rand.nextBoolean();
            x = x + (negative ? rand.nextInt(-7, -1) : rand.nextInt(1, 7));
            negative = rand.nextBoolean();
            z = z + (negative ? rand.nextInt(-7, -1) : rand.nextInt(1, 7));
            y = this.worldEarth.chunkController.findChunkSkyLightMap(x >> 5, z >> 5).getHeightValue(x,z);

            if(this.worldEarth.getBlockID(x,y,z) == Block.grass.ID && this.worldEarth.getBlockID(x, y + 1, z) == Block.air.ID){
                this.worldEarth.setBlockWithNotify(x, y + 1, z, Block.itemStick.ID);
            }
        }


        this.blockPos.clear();
        markAllChunksInRebuildQueueDirty();
    }

    private void generateLeaves(int x, int y, int z, int height){
        int radius = height/2;
        final int xStart = x;
        final int yStart = y;
        final int zStart = z;
        final int boxStartX = x - radius;
        final int boxStartY = y - radius;
        final int boxStartZ = z - radius;
        final int boxEndX = x + radius;
        final int boxEndY = y + radius;
        final int boxEndZ = z + radius;

        for(x = boxStartX; x <= boxEndX; x++){
            for(y = boxStartY; y <= boxEndY; y++){
                for(z = boxStartZ; z <= boxEndZ; z++){
                    if(this.doesBlockIntersectHemisphere(x,y,z, xStart, yStart, zStart, radius) && !this.isBlockAlreadyInUse(x,y,z)){
                        this.blockPos.add(new int[]{x,y,z, Block.leaf.ID});
                    }
                }
            }
        }
    }

    private boolean doesBlockIntersectHemisphere(int x, int y, int z, int startX, int startY, int startZ, int radius){
        float a = Math.abs(x -startX);
        float b = Math.abs(y - startY);
        float c = Math.abs(z - startZ);
        float distance = (float) Math.sqrt(a * a + b * b + c * c);

        if(y < startY - 1){
            return false;
        } else if(y == startY - 1 && this.rand.nextInt(10) == 0 && distance <= radius){
            return true;
        } else if(y == startY - 1) {
            return false;
        }

        return distance <= radius;
    }


    @Override
    public boolean checkAreaClear() {
        int[] blockPos;
        short block;
        for(int i = 0; i < this.blockPos.size(); i++){
            blockPos = this.blockPos.get(i);
            block = this.worldEarth.getBlockID(blockPos[0], blockPos[1], blockPos[2]);
            if (block != Block.air.ID && block < Block.oakLogFullSizeNormal.ID && block > Block.oakLogSize1EastWest.ID && block != Block.leaf.ID){
                return false;
            }
        }
        this.willGenerate = true;
        return true;
    }

    private boolean isBlockAlreadyInUse(int x, int y, int z){
        int[] blockData;
        for(int i = 0; i < this.blockPos.size(); i++){
            blockData = this.blockPos.get(i);
            if(blockData[0] == x && blockData[1] == y && blockData[2] == z){
                return true;
            }
        }
        return false;
    }
}
