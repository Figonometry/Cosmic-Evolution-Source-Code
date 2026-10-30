package spacegame.world.blockstate;

public final class PartialBlock extends BlockState {
    public static final int MAX_LAYERS = 16;
    private final short blockID;
    private int numLayers;
    private final int index;


    public PartialBlock(short blockID, int numLayers, int index){
        this.blockID = blockID;
        this.numLayers = numLayers;
        this.index = index;
    }

    public void setNumLayers(int numLayers){
        this.numLayers = numLayers;
    }

    public void incrementNumLayers(){
        this.numLayers++;
    }

    public void decrementNumLayers(){
        this.numLayers--;
    }

    public short getBlockID(){
        return this.blockID;
    }

    public int getNumLayers(){
        return this.numLayers;
    }

    public int getIndex(){
        return this.index;
    }
}
