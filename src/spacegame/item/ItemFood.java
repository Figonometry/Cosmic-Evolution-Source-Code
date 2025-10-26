package spacegame.item;

import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public class ItemFood extends Item {
    public float healthIncrease;

    public ItemFood(short ID, int textureID, String filepath, float healthIncrease) { //nom nom
        super(ID, textureID, filepath);
        this.healthIncrease = healthIncrease;
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(MouseListener.rightClickReleased) {
            if (player.health < player.maxHealth) {
                player.health += this.healthIncrease;
                player.removeItemFromInventory();
                if (player.health > player.maxHealth) {
                    player.health = player.maxHealth;
                }
            }
        }
    }
}
