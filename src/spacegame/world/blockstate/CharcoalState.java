package spacegame.world.blockstate;

public final class CharcoalState extends BlockState {
    public static final int MAX_LAYERS = 16;
    public int numLayers;
    public int index;

    public CharcoalState(int numLayers, int index){
        this.numLayers = numLayers;
        this.index = index;
    }

}
