package spacegame.block;

public class BlockCampFire extends Block {
    public BlockCampFire(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
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
