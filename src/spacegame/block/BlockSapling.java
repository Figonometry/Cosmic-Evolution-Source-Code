package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.WorldEarth;
import spacegame.world.WorldGenTree;

public final class BlockSapling extends Block implements ITimeUpdate {
    public BlockSapling(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        switch (this.ID){
            case 119 -> {
                world.setBlockWithNotify(x,y,z, Block.sapling.ID, false);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 120 -> {
                world.removeTimeEvent(x,y,z);
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
                new WorldGenTree(world.findChunkFromChunkCoordinates(x >> 5, (y - 1) >> 5, z >> 5), (WorldEarth)world, Chunk.getBlockIndexFromCoordinates(x,y - 1,z));
            }
        }
    }

    @Override
    public long getUpdateTime() {
        return Timer.GAME_DAY;
    }

    @Override
    public String getDisplayStringText() {
        return this.ID == Block.treeSeed.ID ? "Will Sprout In: " : "Will Grow In: ";
    }
}
