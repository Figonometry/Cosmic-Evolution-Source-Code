package spacegame.world;

public final class CoordinatePairs {
    public int x;
    public int z;

    public CoordinatePairs(int x, int z){
        this.x = x;
        this.z = z;
    }

    public boolean isEqual(int x, int z){
        return this.x == x && this.z == z;
    }
}
