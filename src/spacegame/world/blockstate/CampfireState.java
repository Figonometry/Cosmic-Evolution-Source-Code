package spacegame.world.blockstate;

public final class CampfireState extends BlockState {
    public int index;
    public boolean isLit;
    public int logCount;
    public int cookingStickCount;
    public boolean isForCharcoalPit = false;


    public CampfireState(int index, boolean isLit, int logCount, int cookingStickCount, boolean isForCharcoalPit){
        this.index = index;
        this.isLit = isLit;
        this.logCount = logCount;
        this.cookingStickCount = cookingStickCount;
        this.isForCharcoalPit = isForCharcoalPit;
    }
}
