package spacegame.world.blockstate;

public final class CampfireState {
    public int index;
    public boolean isLit;
    public int logCount;
    public int cookingStickCount;


    public CampfireState(int index, boolean isLit, int logCount, int cookingStickCount){
        this.index = index;
        this.isLit = isLit;
        this.logCount = logCount;
        this.cookingStickCount = cookingStickCount;
    }
}
