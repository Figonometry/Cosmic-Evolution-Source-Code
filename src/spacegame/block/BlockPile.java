package spacegame.block;

import spacegame.core.Sound;
import spacegame.core.SpaceGame;
import spacegame.item.Item;
import spacegame.world.ChestLocation;

public final class BlockPile extends BlockContainer {
    public short itemInPile;
    public BlockPile(short ID, int textureID, String filepath, short itemInPile, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
        this.itemInPile = itemInPile;
    }

    @Override
    public String getStepSound(int x, int y, int z){
        ChestLocation chestLocation = SpaceGame.instance.save.activeWorld.getChestLocation(x,y,z);
        if(chestLocation != null){
            return chestLocation.inventory.itemStacks[0].item == Item.firedRedClayAdobeBrick ? Sound.stone : this.stepSound;
        }

        return this.stepSound;
    }
}
