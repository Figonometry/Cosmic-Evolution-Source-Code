package spacegame.block;

import spacegame.world.AxisAlignedBB;

public class BlockAxisAlignedBBList {
    public static final AxisAlignedBB standardBlock = new AxisAlignedBB(0,0,0,1,1,1);
    public static final AxisAlignedBB fullBlock = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB quarterBlock = new AxisAlignedBB(0, 0, 0, 1, 0.25f, 1);
    public static final AxisAlignedBB slab = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
    public static final AxisAlignedBB threeQuartersBlock = new AxisAlignedBB(0, 0, 0, 1, 0.75f, 1);
    public static final AxisAlignedBB oneVoxelHighBlock = new AxisAlignedBB(0, 0, 0, 1, 0.03125f, 1);
    public static final AxisAlignedBB northDoor = new AxisAlignedBB(0, 0, 0, 0.125, 1, 1);
    public static final AxisAlignedBB southDoor = new AxisAlignedBB(0.875,0,0, 1, 1, 1);
    public static final AxisAlignedBB eastDoor = new AxisAlignedBB(0,0,0,1,1,0.125);
    public static final AxisAlignedBB westDoor = new AxisAlignedBB(0, 0, 0.875, 1, 1, 1);
    public static final AxisAlignedBB snowLayerBB = new AxisAlignedBB(0,0,0,1,0.0625,1);
}
