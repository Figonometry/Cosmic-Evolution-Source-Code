package spacegame.world.blockstate;

public final class BerryBushState extends BlockState {
    public static final int GROWTH_STAGE_1 = 0;
    public static final int GROWTH_STAGE_2 = 1;
    public static final int GROWTH_STAGE_3 = 2;
    public static final int GROWTH_STAGE_4 = 3;
    public static final int GROWTH_STAGE_5 = 4;
    public static final int GROWTH_STAGE_6 = 5;
    public static final int GROWTH_STAGE_MATURE = 6;
    public int growthStage;
    public boolean isFlowering;
    public boolean hasMatureFruit;
    public int index;

    public BerryBushState(int growthStage, boolean isFlowering, boolean hasMatureFruit, int index){
        this.growthStage = growthStage;
        this.isFlowering = isFlowering;
        this.hasMatureFruit = hasMatureFruit;
        this.index = index;
    }


}
