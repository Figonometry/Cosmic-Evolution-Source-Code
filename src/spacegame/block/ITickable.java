package spacegame.block;

import spacegame.world.World;

public interface ITickable {

    void tick(int x, int y, int z, World world);
}
