package spacegame.world;

import java.util.ArrayList;
import java.util.Random;

public abstract class WorldGen {
    public Chunk chunk;
    public int index;
    public Random rand;
    public WorldEarth worldEarth;
    public static ArrayList<Chunk> chunksToRebuild = new ArrayList<>();
    public boolean willGenerate;
    public ArrayList<int[]> blockPos = new ArrayList<>();

    public abstract void startGenerate();

    public abstract void generate();

    public abstract boolean checkAreaClear();


    public void addChunkToRebuildQueue(Chunk addedChunk){
        if (addedChunk == null) {
            return;
        }
        Chunk chunk;
        boolean canAdd = true;
        for (int i = 0; i < chunksToRebuild.size(); i++) {
            chunk = chunksToRebuild.get(i);
            if (chunk != null) {
                if (chunk.equals(addedChunk)) {
                    canAdd = false;
                    break;
                }
            }
        }
        if (canAdd) {
            chunksToRebuild.add(addedChunk);
        }
    }

    public static void markAllChunksInRebuildQueueDirty(){
        Chunk chunk;
        for(int i = 0; i < chunksToRebuild.size(); i++){
            chunk = chunksToRebuild.get(i);
            if(chunk != null){
                chunk.markDirty();
                chunksToRebuild.remove(chunk);
            }
        }
        chunksToRebuild.trimToSize();
    }
}
