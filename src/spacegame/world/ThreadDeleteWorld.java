package spacegame.world;

import spacegame.util.GeneralUtil;

import java.io.File;

public final class ThreadDeleteWorld implements Runnable{
    private File saveBeingDeleted;
    public boolean completed;
    public ThreadDeleteWorld(File save){
        this.saveBeingDeleted = save;
    }


    @Override
    public void run() {
        GeneralUtil.deleteDirectory(this.saveBeingDeleted);
        this.completed = true;
    }
}
