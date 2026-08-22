package spacegame.world.blockstate;

public final class LogState extends BlockState {
    public static final int FACE_DIRECTION_TOP_AND_BOTTOM = 0;
    public static final int FACE_DIRECTION_NORTH_AND_SOUTH = 1;
    public static final int FACE_DIRECTION_EAST_AND_WEST = 2;
    public int facingDirection;
    public int size;
    public int index;

    public LogState(int facingDirection, int size, int index){
        this.facingDirection = facingDirection;
        this.size = size;
        this.index = index;
    }

}
