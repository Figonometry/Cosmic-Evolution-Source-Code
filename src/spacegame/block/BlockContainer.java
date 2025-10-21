package spacegame.block;

import spacegame.entity.EntityPlayer;
import spacegame.world.Chunk;
import spacegame.world.World;

public abstract class BlockContainer extends Block {
    public int inventoryWidth;
    public int inventoryHeight;
    public BlockContainer(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath);
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
    }



    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        super.onLeftClick(x,y,z, world, player);
        world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeChestLocation((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
    }

}
