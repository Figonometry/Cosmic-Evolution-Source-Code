package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingTableRecipeSelection;
import spacegame.item.Item;
import spacegame.item.crafting.CraftingBlockRecipes;
import spacegame.render.RenderBlocks;
import spacegame.world.World;

public final class BlockCraftingTable extends Block {
    public BlockCraftingTable(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public int getBlockTexture(int face) {
        switch (this.ID){
            case 132 -> { //Primitive crafting table
                switch (face){ //40 is the side, 41 is the top
                    case RenderBlocks.TOP_FACE ->{
                        return this.textureID + 1;
                    }
                    case RenderBlocks.BOTTOM_FACE -> {
                        return Block.dirt.textureID;
                    }
                }
            }
        }

        return this.textureID;
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.reedCraftingGridTop.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0));
        super.onLeftClick(x,y,z, world, player);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(world.getBlockID(x, y + 1, z) == Block.air.ID) {
            int techLevel = this.getTechLevel();
            CosmicEvolution.instance.setNewGui(new GuiCraftingTableRecipeSelection(CosmicEvolution.instance, x, y, z, techLevel));
        }
    }

    private int getTechLevel(){
        switch (this.ID){
            case 132 -> {
                return CraftingBlockRecipes.TECH_LEVEL_PRIMITIVE;
            }
            default -> {
                return CraftingBlockRecipes.TECH_LEVEL_PRIMITIVE;
            }
        }
    }

}
