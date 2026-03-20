package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.item.Item;
import spacegame.world.InWorldCraftingBlock;

public final class BlockCrafting extends Block {
    public BlockCrafting(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public String getStepSound(int x, int y, int z) {
        InWorldCraftingBlock craftingBlock = CosmicEvolution.instance.save.activeWorld.getInWorldCraftingBlock(x,y,z);

        if(craftingBlock == null)return this.stepSound; //This shouldnt be null but I'm checking it anyways


        return Block.list[craftingBlock.materialBlockID].getStepSound(x,y,z);
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        InWorldCraftingBlock craftingBlock = CosmicEvolution.instance.save.activeWorld.getInWorldCraftingBlock(x,y,z);

        if(craftingBlock == null)return this.displayName; //This shouldnt be null but I'm checking it anyways


        return Item.list[craftingBlock.craftingRecipe.outputItemID].getDisplayName(craftingBlock.craftingRecipe.outputBlockID) + " (Crafting)";
    }


}
