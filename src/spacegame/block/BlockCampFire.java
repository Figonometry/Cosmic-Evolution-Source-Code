package spacegame.block;

import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public class BlockCampFire extends BlockHeating {


    public BlockCampFire(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }


    public int getLogCount(){
        return switch (this.ID) {
            case 63 -> 1;
            case 64 -> 2;
            case 65 -> 3;
            case 89, 68 -> 4;
            default -> 0;
        };
    }

}
