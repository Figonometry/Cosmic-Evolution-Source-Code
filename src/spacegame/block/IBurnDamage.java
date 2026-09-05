package spacegame.block;

import spacegame.world.worldtypes.World;

public interface IBurnDamage {
    boolean canDamage(int x, int y, int z, World world);
}
