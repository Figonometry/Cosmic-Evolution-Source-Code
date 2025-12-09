package spacegame.block;

import spacegame.core.Timer;
import spacegame.world.World;

public final class BlockTorch extends Block implements ITimeUpdate {
    public BlockTorch(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        world.setBlockWithNotify(x,y,z, Block.air.ID, false);
        switch (this.ID){
            case 2 -> {
                world.setBlockWithNotify(x,y,z, Block.torchStandardBurnedOut.ID, false);
            }
            case 3 -> {
                world.setBlockWithNotify(x,y,z, Block.torchNorthBurnedOut.ID, false);
            }
            case 4 -> {
                world.setBlockWithNotify(x,y,z, Block.torchSouthBurnedOut.ID, false);
            }
            case 5 -> {
                world.setBlockWithNotify(x,y,z, Block.torchEastBurnedOut.ID, false);
            }
            case 6 -> {
                world.setBlockWithNotify(x,y,z, Block.torchWestBurnedOut.ID, false);
            }
        }
    }

    @Override
    public long getUpdateTime() {
        return Timer.GAME_DAY * 2;
    }

    @Override
    public String getDisplayStringText() {
        return null;
    }
}
