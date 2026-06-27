package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingStoneTools;
import spacegame.world.World;


public final class BlockItemStone extends Block {
    public BlockItemStone(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased) return;
        short playerHeldBlock = player.getHeldBlock();
        if (playerHeldBlock == Block.itemStone.ID) {
            CosmicEvolution.instance.setNewGui(new GuiCraftingStoneTools(CosmicEvolution.instance, x, y, z));
            MouseListener.rightClickReleased = false;
        }
    }
}
