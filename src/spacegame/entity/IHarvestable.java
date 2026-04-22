package spacegame.entity;

import spacegame.world.World;

public interface IHarvestable {

    void dropItems(double x, double y, double z, World world, EntityPlayer player);
}
