package spacegame.block;

import spacegame.entity.EntityPlayer;
import spacegame.world.Chunk;
import spacegame.world.World;

public abstract class BlockContainer extends Block {
    public int inventorySize;
    public BlockContainer(short ID, int textureID, String filepath, int inventorySize) {
        super(ID, textureID, filepath);
        this.inventorySize = inventorySize;
    }



    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        super.onLeftClick(x,y,z, world, player);
        world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeChestLocation((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
    }

}
