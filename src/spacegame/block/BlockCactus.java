package spacegame.block;

public final class BlockCactus extends Block {
    public BlockCactus(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public int getBlockTexture(int face){
        return switch (face) {
            case 0 -> this.textureID + 1;
            case 1 -> this.textureID + 2;
            default -> this.textureID;
        };
    }
}
