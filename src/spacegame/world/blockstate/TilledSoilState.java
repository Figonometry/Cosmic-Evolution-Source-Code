package spacegame.world.blockstate;

public final class TilledSoilState {
    public static final int NO_FERTILIZER = -1;
    public static final int BONEMEAL = 0;
    public int index;
    public float moisturePercent;
    public float potassiumPercent;
    public float nitrogenPercent;
    public float phosphorusPercent;
    public int fertilizerID = NO_FERTILIZER;


    public TilledSoilState(int index, float moisturePercent, float potassiumPercent, float nitrogenPercent, float phosphorusPercent, int fertilizerID){
        this.index = index;
        this.moisturePercent = moisturePercent;
        this.potassiumPercent = potassiumPercent;
        this.nitrogenPercent = nitrogenPercent;
        this.phosphorusPercent = phosphorusPercent;
        this.fertilizerID = fertilizerID;
    }
}
