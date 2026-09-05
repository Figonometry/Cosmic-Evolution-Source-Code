package spacegame.world.worldtypes.testworld;

import spacegame.block.Block;
import spacegame.world.Chunk;
import spacegame.world.ChunkController;
import spacegame.world.GeologicProvince;
import spacegame.world.worldtypes.ChunkTerrainHandler;
import spacegame.world.worldtypes.World;

public final class ChunkTestWorldHandler extends ChunkTerrainHandler {
    public ChunkTestWorldHandler(ChunkController chunkController, World world) {
        super(chunkController, world);
    }

    @Override
    public void setTerrain(short[] blocks, Chunk chunk) {
        int y;
        chunk.empty = false;
        for(int i = 0; i < blocks.length; i++){
            y = chunk.getBlockYFromIndex(i);
            if(y <= 10){
                blocks[i] = Block.sandstoneStone.ID;
            }
        }
    }

    @Override
    public double getTerrainNoise(int x, int y, int z) {
        return 0;
    }

    @Override
    public void populateChunk(Chunk chunk) {
        chunk.populated = true;
    }

    @Override
    protected GeologicProvince getGeologicProvince(int x, int y, int z) {
        return null;
    }

    @Override
    protected short getStoneType(int x, int y, int z) {
        return 0;
    }

    @Override
    protected short getSandType(int x, int y, int z) {
        return 0;
    }

    @Override
    protected short getGravelType(int x, int y, int z) {
        return 0;
    }
}
