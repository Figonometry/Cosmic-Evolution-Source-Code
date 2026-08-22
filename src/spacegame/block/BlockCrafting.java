package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.World;
import spacegame.world.blockstate.InWorldCraftingItem;
import spacegame.world.blockstate.MultiState;

public final class BlockCrafting extends Block {
    public BlockCrafting(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        InWorldCraftingItem craftingItem = (InWorldCraftingItem) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CRAFTING_ITEM_STATE);
        return craftingItem == null ? "Error" :  Item.list[craftingItem.outputRecipe.itemID].getDisplayName(craftingItem.outputRecipe.itemID) + " (Crafting)";
    }


    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();


        ToolTip rightClick = new ToolTip();
        rightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        rightClick.addText("to add the item in your hand, if required");

        this.tooltips[0][0].addToolTip(rightClick);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }
}
