package spacegame.world.blockstate;

public final class TorchState extends BlockState {
    public static final int TORCH_FACE_STANDARD = 0;
    public static final int TORCH_FACE_NORTH = 1;
    public static final int TORCH_FACE_SOUTH = 2;
    public static final int TORCH_FACE_EAST = 3;
    public static final int TORCH_FACE_WEST = 4;
    public boolean isLit;
    public boolean isBurnedOut;
    public int facingDirection;
    public int index;


    public TorchState(boolean isLit, boolean isBurnedOut, int facingDirection, int index){
        this.isLit = isLit;
        this.isBurnedOut = isBurnedOut;
        this.facingDirection = facingDirection;
        this.index = index;
    }
}
