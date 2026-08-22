package spacegame.block;

import spacegame.world.World;

public interface IBurnDamage {
    boolean canDamage(int x, int y, int z, World world);
}
