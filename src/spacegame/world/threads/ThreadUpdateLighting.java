package spacegame.world.threads;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.world.Chunk;
import spacegame.world.ChunkColumnSkylightMap;
import spacegame.world.worldtypes.World;

public final class ThreadUpdateLighting implements Runnable {
    public World world;
    public Chunk chunk;
    public int x;
    public int y;
    public int z;
    public boolean triggeredFromBlockUpdate;
    public short triggeringBlock;
    public boolean destroyLight;

    //Using an arraylist is the wrong data structure for the lighting updates, the list has to use linear search in order to check it there's something in the queue already
    //Using a hashmap might be better, the chunk index already acts as a unique key, it would need to be hashed with the chunk coordinates, avoid the longhasher if possible
    //The lookup would be a simple query of calling containsKey() on the map
    //Pull lighting logic out of the world class and store here, any functions called referencing this can be swapped for the world object here
    public ThreadUpdateLighting(World world, Chunk chunk, int x, int y, int z, short triggeringBlock, boolean destroyLight) {
        this.world = world;
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
        this.triggeredFromBlockUpdate = x != Integer.MIN_VALUE && y != Integer.MIN_VALUE && z != Integer.MIN_VALUE;
        this.triggeringBlock = triggeringBlock;
        this.destroyLight = destroyLight;
    }

    @Override
    public void run() {
        if (this.triggeredFromBlockUpdate) {
            if (triggeringBlock == Block.air.ID) {
                world.resetNearestLight(x, y, z);
            }


            if (Block.list[triggeringBlock].isLightBlock(x, y, z, world)) {
                world.propagateLightSource(x, y, z, Block.list[triggeringBlock].lightBlockValue);
            }


            if (Block.list[triggeringBlock].isSolid) {
                chunk.light[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
                world.queueSurroundingLightBlocks(x, y, z);
            }


            ChunkColumnSkylightMap lightMap = world.findChunkSkyLightMap(x >> 5, z >> 5);
            if (triggeringBlock == Block.air.ID) {
                if (lightMap.isHeight(x, y, z)) {
                    lightMap.updateLightMap(x, world.findNextHighestSolidBlock(x, y, z), z);
                }
            } else if (Block.list[triggeringBlock].isSolid) {
                if (lightMap.isHeightGreater(x, y, z)) {
                    lightMap.updateLightMap(x, y, z);
                }
            }
            if (destroyLight) {
                world.propagateDarkness(x, y, z);
            }

        } else {
            if (this.chunk.firstRender) {
                this.chunk.floodFillBlockLightArray();
            }
        }

        this.chunk.setSkyLight();


        this.chunk.dirtyLighting = false;
        CosmicEvolution.threadJobs.decrementAndGet();
    }

}
