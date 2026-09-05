package spacegame.block;

import spacegame.world.worldtypes.World;

public final class BlockClay extends Block implements ITickable {
    public BlockClay(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void tick(int x, int y, int z, World world) {

    }
}
