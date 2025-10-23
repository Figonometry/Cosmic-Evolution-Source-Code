package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingPottery;
import spacegame.item.Item;
import spacegame.world.World;

public final class BlockItemClay extends Block {
    public BlockItemClay(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem == Item.clay.ID){
            CosmicEvolution.instance.setNewGui(new GuiCraftingPottery(CosmicEvolution.instance, x, y, z));
            MouseListener.rightClickReleased = false;
        }
    }
}
