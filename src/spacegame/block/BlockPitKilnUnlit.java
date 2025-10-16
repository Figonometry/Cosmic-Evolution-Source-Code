package spacegame.block;

import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.World;

public class BlockPitKilnUnlit extends BlockContainer {
    public BlockPitKilnUnlit(short ID, int textureID, String filepath, int inventorySize) {
        super(ID, textureID, filepath, inventorySize);
    }

    public int getStrawHeight() {
        return switch (this.ID) {
            case 74 -> 1;
            case 75 -> 2;
            case 76 -> 3;
            case 77 -> 4;
            case 78 -> 5;
            case 79 -> 6;
            case 80 -> 7;
            default -> 8;
        };
    }

    public int getNumberOfLogs() {
        return switch (this.ID) {
            case 95 -> 1;
            case 96 -> 2;
            case 97 -> 3;
            case 98, 99 -> 4;
            default -> 0;
        };
    }
}
