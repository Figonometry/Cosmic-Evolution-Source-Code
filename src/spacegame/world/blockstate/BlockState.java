package spacegame.world.blockstate;

import spacegame.world.Chunk;

public abstract class BlockState {


    public void onStateRemoval(){
        //Intentionally empty, override in subclasses for specific behvarior, such as dumping chest contents
    }

    public void onTick(Chunk callingChunk){
        //Intentionally empty, not every state will need to check during ticks
    }
}
