package spacegame.world.blockstate;

public final class DoorState extends BlockState {
    public static final int FACE_DIRECTION_NORTH = 0;
    public static final int FACE_DIRECTION_SOUTH = 1;
    public static final int FACE_DIRECTION_EAST = 2;
    public static final int FACE_DIRECTION_WEST = 3;
    public int facingDirection;
    public boolean isOpen;
    public boolean hingeLeft;
    public boolean hingeRight;
    public int index;

    public DoorState(int facingDirection, boolean isOpen, boolean hingeLeft, boolean hingeRight, int index){
        this.facingDirection = facingDirection;
        this.isOpen = isOpen;
        this.hingeLeft = hingeLeft;
        this.hingeRight = hingeRight;
        this.index = index;
    }
}
