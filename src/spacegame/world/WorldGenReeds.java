package spacegame.world;

import spacegame.block.Block;

import java.util.ArrayList;
import java.util.Random;

public final class WorldGenReeds extends WorldGen {
    public ArrayList<int[]> blockPos = new ArrayList<>();
    public WorldGenReeds(Chunk chunk, WorldEarth worldEarth, int index){
        if(chunk.blocks[index] != Block.water.ID)return;

        int x = chunk.getBlockXFromIndex(index);
        int y = chunk.getBlockYFromIndex(index);
        int z = chunk.getBlockZFromIndex(index);

        if(worldEarth.getBlockID(x, y + 1, z) != Block.air.ID || !Block.list[worldEarth.getBlockID(x, y - 1, z)].isSolid)return;



        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.rand = new Random();
        this.startGenerate();
    }



    @Override
    public void startGenerate() {
        this.generateReedCluster(this.chunk.getBlockXFromIndex(index), this.chunk.getBlockYFromIndex(index), this.chunk.getBlockZFromIndex(index), this.rand.nextInt(3,6));
        this.generate();
    }

    private void generateReedCluster(int x, int y, int z, int radius){
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
                    if(this.doesBlockIntersectSphere(x,y,z, xStart, yStart, zStart, radius) && this.rand.nextInt(4) == 0 && !this.isBlockAlreadyInUse(x,y,z) && this.isBlockSuitableToGenerateReeds(x,y,z)){
                        this.blockPos.add(new int[]{x,y,z, Block.reedLower.ID});
                        this.blockPos.add(new int[]{x, y + 1, z, Block.reedUpper.ID});
                    }
                }
            }
        }
    }


    private boolean isBlockSuitableToGenerateReeds(int x, int y, int z){
      return worldEarth.getBlockID(x, y, z) == Block.water.ID && worldEarth.getBlockID(x, y + 1, z) == Block.air.ID && Block.list[worldEarth.getBlockID(x, y - 1, z)].isSolid;
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

    private boolean doesBlockIntersectSphere(int x, int y, int z, int startX, int startY, int startZ, int radius){
        float a = Math.abs(x -startX);
        float b = Math.abs(y - startY);
        float c = Math.abs(z - startZ);
        float distance = (float) Math.sqrt(a * a + b * b + c * c);
        return distance <= radius;
    }

    @Override
    public void generate() {
        Chunk chunk;
        int[] blockData;
        for (int i = 0; i < this.blockPos.size(); i++) {
            blockData = this.blockPos.get(i);
            this.worldEarth.setBlock(blockData[0], blockData[1], blockData[2], (short) blockData[3]);
            chunk = this.worldEarth.findChunkFromChunkCoordinates(blockData[0] >> 5, blockData[1] >> 5, blockData[2] >> 5);
            this.addChunkToRebuildQueue(chunk);
        }

        for (int i = 0; i < this.blockPos.size(); i++) {
            blockData = this.blockPos.get(i);
            this.worldEarth.notifySurroundingBlockWithoutRebuild(blockData[0], blockData[1] + 1, blockData[2]);
        }
        this.blockPos.clear();
        markAllChunksInRebuildQueueDirty();
    }

    @Override
    public boolean checkAreaClear() {
        return true;
    }
}
