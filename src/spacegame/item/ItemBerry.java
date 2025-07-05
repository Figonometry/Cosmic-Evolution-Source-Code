package spacegame.item;

import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.world.WorldFace;

public final class ItemBerry extends Item {


    public ItemBerry(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onRightClick(int x, int y, int z, WorldFace worldFace, EntityPlayer player){
        if(MouseListener.rightClickReleased) {
            if (player.health < player.maxHealth) {
                player.health += 5;
                player.removeItemFromInventory();
                if (player.health > player.maxHealth) {
                    player.health = player.maxHealth;
                }
            }
        }
    }
}
