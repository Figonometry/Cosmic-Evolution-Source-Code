package spacegame.world.blockstate;

public final class PitKilnState extends BlockState {
    public int strawCount;
    public int logCount;
    public boolean isLit;
    public int index;

    public PitKilnState(int strawCount, int logCount, boolean isLit, int index){
        this.strawCount = strawCount;
        this.logCount = logCount;
        this.isLit = isLit;
        this.index = index;
    }

    public boolean isPitKilnComplete(){
        return this.logCount == 4 && this.strawCount == 8;
    }
}
