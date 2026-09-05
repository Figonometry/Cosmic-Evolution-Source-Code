package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.world.blockstate.MultiState;

public final class BlockCrafting3D extends Block {
    public BlockCrafting3D(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public String getStepSound(int x, int y, int z) {
        InWorld3DCraftingItem craftingBlock = (InWorld3DCraftingItem) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CRAFTING_3D_ITEM_STATE);

        if(craftingBlock == null)return this.stepSound; //This shouldnt be null but I'm checking it anyways


        return Block.list[craftingBlock.materialBlockID].getStepSound(x,y,z);
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        InWorld3DCraftingItem craftingBlock = (InWorld3DCraftingItem) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CRAFTING_3D_ITEM_STATE);

        if(craftingBlock == null)return this.displayName; //This shouldnt be null but I'm checking it anyways


        return Item.list[craftingBlock.craftingRecipe.outputItemID].getDisplayName(craftingBlock.craftingRecipe.outputBlockID, craftingBlock.calculateOutPutMetadata()) + " (Crafting)";
    }


    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();


        ToolTip leftClick = new ToolTip();
        leftClick.addMouseIcon(MouseAndKeyIconTextureList.LEFT_CLICK);
        leftClick.addText("to remove material");

        ToolTip rightClick = new ToolTip();
        rightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        rightClick.addText("to add material, if possible");

        this.tooltips[0][0].addToolTip(leftClick);
        this.tooltips[0][0].addToolTip(rightClick);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }


}
