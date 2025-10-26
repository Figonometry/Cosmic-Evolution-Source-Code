package spacegame.world;

import spacegame.block.Block;
import spacegame.block.ITimeUpdate;
import spacegame.core.CosmicEvolution;

import java.util.ArrayList;
import java.util.Random;

public final class WorldGenBerryBush extends WorldGen{
    public ArrayList<int[]> blockPos = new ArrayList<>();

    public WorldGenBerryBush(Chunk chunk, WorldEarth worldEarth, int index) {
        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.rand = new Random();
        this.startGenerate();
    }

    @Override
    public void startGenerate() {
        int x = this.chunk.getBlockXFromIndex(this.index);
        int y = this.chunk.getBlockYFromIndex(this.index);
        int z = this.chunk.getBlockZFromIndex(this.index);
        int radius = this.rand.nextInt(4, 9);
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
                    if(this.doesBlockIntersectSphere(x,y,z, xStart, yStart, zStart, radius) && this.isBlockValid(x,y,z) && this.rand.nextInt(15) == 0){
                        this.blockPos.add(new int[]{x,y,z, Block.berryBush.ID, (byte)0});
                    }
                }
            }
        }
        this.generate();
    }

    private boolean isBlockValid(int x, int y, int z){
        return this.worldEarth.getBlockID(x,y,z) == Block.grass.ID && this.worldEarth.getBlockID(x, y + 1, z) == Block.air.ID;
    }

    private boolean doesBlockIntersectSphere(int x, int y, int z, int startX, int startY, int startZ, int radius){
        float a = Math.abs(x -startX);
        float b = Math.abs(y - startY);
        float c = Math.abs(z - startZ);
        return (float) Math.sqrt(a * a + b * b + c * c) <= radius;
    }

    @Override
    public void generate() {
        Chunk chunk;
        int[] blockData;
        for (int i = 0; i < this.blockPos.size(); i++) {
            blockData = this.blockPos.get(i);
            this.worldEarth.setBlock(blockData[0], blockData[1] + 1, blockData[2], (short) blockData[3]);
            chunk = this.worldEarth.findChunkFromChunkCoordinates(blockData[0] >> 5, blockData[1] + 1 >> 5, blockData[2] >> 5);
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
