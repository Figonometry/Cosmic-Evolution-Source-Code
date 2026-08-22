package spacegame.world.blockstate;

public final class MultiStateWrapper {
    public volatile MultiState value;


    public MultiStateWrapper(MultiState value){
        this.value = value;
    }
}
