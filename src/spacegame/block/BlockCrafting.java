package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.item.Item;
import spacegame.item.crafting.InWorld3DCraftingItem;
import spacegame.item.crafting.InWorldCraftingItem;

public final class BlockCrafting extends Block {
    public BlockCrafting(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        InWorldCraftingItem craftingItem = CosmicEvolution.instance.save.activeWorld.getInWorldCraftingItem(x,y,z);
        return craftingItem == null ? "Error" :  Item.list[craftingItem.outputRecipe.itemID].getDisplayName(craftingItem.outputRecipe.itemID) + " (Crafting)";
    }
}
