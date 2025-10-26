package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.world.World;

public final class BlockReedGrowing extends Block implements ITimeUpdate {
    public BlockReedGrowing(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        world.setBlockWithNotify(x,y,z, Block.reedLower.ID);
        world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + ((ITimeUpdate)Block.reedLower).getUpdateTime());
    }

    @Override
    public long getUpdateTime() {
        return 7 * Timer.DAY;
    }

    @Override
    public String getDisplayStringText() {
        return "Will Sprout In: ";
    }
}
