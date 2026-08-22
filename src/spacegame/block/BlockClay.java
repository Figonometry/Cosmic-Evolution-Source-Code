package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.world.World;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public final class BlockClay extends Block implements ITickable {
    public BlockClay(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void tick(int x, int y, int z, World world) {

    }
}
