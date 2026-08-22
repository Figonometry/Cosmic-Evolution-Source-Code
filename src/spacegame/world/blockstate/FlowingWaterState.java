package spacegame.world.blockstate;

public final class FlowingWaterState extends BlockState {
    public static final int FACE_DIRECTION_NORTH = 0;
    public static final int FACE_DIRECTION_SOUTH = 1;
    public static final int FACE_DIRECTION_EAST = 2;
    public static final int FACE_DIRECTION_WEST = 3;
    public static final int FLOW_LEVEL_1 = 1;
    public static final int FLOW_LEVEL_2 = 2;
    public static final int FLOW_LEVEL_3 = 3;
    public static final int FLOW_LEVEL_4 = 4;
    public static final int FLOW_LEVEL_5 = 5;
    public static final int FLOW_LEVEL_6 = 6;
    public static final int FLOW_LEVEL_7 = 7;
    public int facingDirection;
    public int waterLevel;
    public int index;

    public FlowingWaterState(int facingDirection, int waterLevel, int index){
        this.facingDirection = facingDirection;
        this.waterLevel = waterLevel;
        this.index = index;
    }
}
