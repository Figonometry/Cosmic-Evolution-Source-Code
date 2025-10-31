package spacegame.world;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;

import java.util.ArrayList;
import java.util.Random;

public final class WorldGenClayBlob extends WorldGen {
    public ArrayList<int[]> blockPos = new ArrayList<>();


    public WorldGenClayBlob(Chunk chunk, WorldEarth earth, int index){
        if(chunk.blocks[index] != Block.grass.ID)return;
        this.worldEarth = earth;
        this.index = index;
        this.chunk = chunk;
        this.rand = new Random(new LongHasher().hash(CosmicEvolution.instance.save.seed, String.valueOf(chunk.x + chunk.y + chunk.z + index)));
        this.startGenerate();
    }
    @Override
    public void startGenerate() {
        int x = this.chunk.getBlockXFromIndex(index);
        int y = this.chunk.getBlockYFromIndex(index);
        int z = this.chunk.getBlockZFromIndex(index);
        int radius = this.rand.nextInt(3, 6);
        this.generateClay(x,y,z,radius);
        this.generate();
    }

    private void generateClay(int x, int y, int z, int radius){
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
                    if(this.doesBlockIntersectSphere(x,y,z, xStart, yStart, zStart, radius) && !this.isBlockAlreadyInUse(x,y,z)){
                        if(this.worldEarth.getBlockID(x,y,z) == Block.grass.ID){
                            this.blockPos.add(new int[]{x,y,z, Block.grassWithClay.ID});
                        } else if(this.worldEarth.getBlockID(x,y,z) == Block.dirt.ID){
                            this.blockPos.add(new int[]{x,y,z, Block.clay.ID});
                        }
                    }
                }
            }
        }
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
            this.worldEarth.setBlock(blockData[0], blockData[1], blockData[2], (short)blockData[3]);
            chunk = this.worldEarth.findChunkFromChunkCoordinates(blockData[0] >> 5, blockData[1] >> 5, blockData[2] >> 5);
            this.addChunkToRebuildQueue(chunk);
        }

        for (int i = 0; i < this.blockPos.size(); i++) {
            blockData = this.blockPos.get(i);
            this.worldEarth.notifySurroundingBlockWithoutRebuild(blockData[0], blockData[1], blockData[2]);
        }

        this.blockPos.clear();
        markAllChunksInRebuildQueueDirty();
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

    @Override
    public boolean checkAreaClear() {
        return false;
    }
}
