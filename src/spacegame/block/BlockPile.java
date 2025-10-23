package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.item.Item;
import spacegame.world.ChestLocation;

public class BlockPile extends BlockContainer {
    public short itemInPile;
    public BlockPile(short ID, int textureID, String filepath, short itemInPile, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
        this.itemInPile = itemInPile;
    }

    @Override
    public String getStepSound(int x, int y, int z){
        ChestLocation chestLocation = CosmicEvolution.instance.save.activeWorld.getChestLocation(x,y,z);
        if(chestLocation != null){
            return chestLocation.inventory.itemStacks[0].item == Item.firedRedClayAdobeBrick ? Sound.stone : this.stepSound;
        }

        return this.stepSound;
    }
}
