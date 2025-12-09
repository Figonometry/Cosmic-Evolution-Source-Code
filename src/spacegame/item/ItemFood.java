package spacegame.item;

import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public class ItemFood extends Item {
    public float saturationIncrease;

    public ItemFood(short ID, int textureID, String filepath, float saturationIncrease) { //nom nom
        super(ID, textureID, filepath);
        this.saturationIncrease = saturationIncrease;
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(MouseListener.rightClickReleased) {
            if (player.saturation < player.maxSaturation) {
                player.saturation += this.saturationIncrease;
                player.removeItemFromInventory();
                if (player.saturation > player.maxSaturation) {
                    player.saturation = player.maxSaturation;
                }
            }
        }
    }
}
