package spacegame.world;

import spacegame.block.Block;

import java.util.ArrayList;
import java.util.Random;

public final class WorldGenCactus extends WorldGen{
    public ArrayList<int[]> blockPos = new ArrayList<>();
    public WorldGenCactus(Chunk chunk, WorldEarth worldEarth, int index){
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
        int height = this.rand.nextInt(1,6);
        for(int i = 0; i < height; i++){
            this.blockPos.add(new int[]{x, y + i, z, Block.cactus.ID, (byte)0});
        }

        this.generate();
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
