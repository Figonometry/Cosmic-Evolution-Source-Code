package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiInventoryStrawChest;
import spacegame.world.ChestLocation;
import spacegame.world.World;

public final class BlockStrawChest extends BlockContainer {
    public BlockStrawChest(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        if(this.ID == Block.strawChest.ID){
            ChestLocation location = world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).getChestLocation(x,y,z);
            CosmicEvolution.instance.setNewGui(new GuiInventoryStrawChest(CosmicEvolution.instance, CosmicEvolution.instance.save.thePlayer.inventory, location.inventory));
            MouseListener.rightClickReleased = false;
        }
    }


}
