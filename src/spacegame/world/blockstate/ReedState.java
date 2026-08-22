package spacegame.world.blockstate;

public final class ReedState extends BlockState {
    public static final int GROWTH_STAGE_1 = 0;
    public static final int GROWTH_STAGE_2 = 1;
    public static final int GROWTH_STAGE_3 = 2;
    public static final int GROWTH_STAGE_4 = 3;
    public static final int GROWTH_STAGE_5 = 4;
    public static final int GROWTH_STAGE_MATURE = 5;
    public int growthStage;
    public int index;

    public ReedState(int growthStage, int index){
        this.growthStage = growthStage;
        this.index = index;
    }

}
