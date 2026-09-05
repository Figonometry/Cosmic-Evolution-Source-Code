package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingPottery;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.worldtypes.World;

public final class BlockItemClay extends Block {
    public BlockItemClay(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldBlock = player.getHeldBlock();

        if(playerHeldBlock == Block.itemClay.ID){
            CosmicEvolution.instance.setNewGui(new GuiCraftingPottery(CosmicEvolution.instance, x, y, z));
            MouseListener.rightClickReleased = false;
        }
    }


    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip.addText("with");
        toolTip.addBlockID(BlockIDList.ITEM_CLAY);
        toolTip.addText("to craft");

        this.tooltips[0][0].addToolTip(toolTip);
    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }
}
