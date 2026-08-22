package spacegame.world.blockstate;

import spacegame.world.Chunk;

import java.util.HashMap;
import java.util.Map;

public final class MultiState {
    public static final int CAMPFIRE_STATE = 0;
    public static final int CHEST_STATE = 1;
    public static final int CROP_STATE = 2;
    public static final int DOOR_TRANSITION_STATE = 3;
    public static final int HEATABLE_BLOCK_STATE = 4;
    public static final int CRAFTING_3D_ITEM_STATE = 5;
    public static final int CRAFTING_ITEM_STATE = 6;
    public static final int TILLED_SOIL_STATE = 7;
    public static final int TORCH_STATE = 8;
    public static final int LOG_STATE = 9;
    public static final int PIT_KILN_STATE = 10;
    public static final int FLOWING_WATER_STATE = 11;
    public static final int DOOR_STATE = 12;
    public static final int REED_GROWTH_STATE = 13;
    public static final int BERRY_BUSH_STATE = 14;
    private final HashMap<Integer, BlockState> blockStates = new HashMap<>();
    public final int index;


    public MultiState(int index){
        this.index = index;
    }

    public void onTick(Chunk callingChunk){
        for(Map.Entry<Integer, BlockState> entry : this.blockStates.entrySet()){
            entry.getValue().onTick(callingChunk);
        }
    }

    public void addBlockState(int key, BlockState blockState){
        this.blockStates.put(key, blockState);
    }

    public BlockState getBlockState(int key){
        return this.blockStates.get(key);
    }


    public void removeBlockState(int key){
        BlockState blockState = this.blockStates.get(key);
        if(blockState != null){
            blockState.onStateRemoval();
        }


        this.blockStates.remove(key);
    }


    public boolean mapContainsKeyOfType(int key){
        return this.blockStates.containsKey(key);
    }


    public void clearAllStates(){
        this.blockStates.clear();
    }
}
