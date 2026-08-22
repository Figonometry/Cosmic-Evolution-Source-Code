package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiInventoryStrawChest;
import spacegame.world.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;

public final class BlockReedChest extends BlockContainer {
    public BlockReedChest(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        if(this.ID == Block.reedChest.ID){
            ChestLocation location = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            CosmicEvolution.instance.setNewGui(new GuiInventoryStrawChest(CosmicEvolution.instance, CosmicEvolution.instance.save.thePlayer.inventory, location.inventory));
            MouseListener.rightClickReleased = false;
        }
    }


}
