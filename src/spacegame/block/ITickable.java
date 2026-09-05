package spacegame.block;

import spacegame.world.worldtypes.World;

public interface ITickable {

    void tick(int x, int y, int z, World world);
}
