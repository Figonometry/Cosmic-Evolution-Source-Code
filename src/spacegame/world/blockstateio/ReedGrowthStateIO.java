package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.PitKilnState;
import spacegame.world.blockstate.ReedState;

public final class ReedGrowthStateIO {

    public void saveReedGrowthStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        ReedState reedGrowthState;
        int totalCount = chunk.getBlockStateCount(MultiState.REED_GROWTH_STATE);
        ReedState[] reedGrowthStates = this.getAllReedGrowthStatesInArray(chunk, totalCount);
        int reedGrowthStateCount = 0;
        NBTTagCompound[] reedGrowthStatesTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < reedGrowthStatesTags.length; i++){

            reedGrowthState = reedGrowthStates[i];
            reedGrowthStatesTags[i] = new NBTTagCompound();
            reedGrowthStatesTags[i].setInteger("growthStage", reedGrowthState.growthStage);
            reedGrowthStatesTags[i].setInteger("index", reedGrowthState.index);


            nbtTagCompound.setTag("reedGrowthState" + reedGrowthStateCount, reedGrowthStatesTags[i]);
            reedGrowthStateCount++;
        }
        nbtTagCompound.setInteger("reedGrowthStateCount", reedGrowthStateCount);
    }

    public void loadReedGrowthStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int reedGrowthStateCount = nbtTagCompound.getInteger("reedGrowthStateCount");
        NBTTagCompound reedGrowthStateLoadedTag;
        for (int i = 0; i < reedGrowthStateCount; i++) {
            reedGrowthStateLoadedTag = nbtTagCompound.getCompoundTag("reedGrowthState" + i);
            int growthStage = reedGrowthStateLoadedTag.getInteger("growthStage");
            int index = reedGrowthStateLoadedTag.getInteger("index");

            chunk.addBlockState(index, MultiState.REED_GROWTH_STATE, new ReedState(growthStage, index));
        }
    }
    private ReedState[] getAllReedGrowthStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.REED_GROWTH_STATE, totalCount);
        ReedState[] returnArray = new ReedState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (ReedState) base[i];
        }


        return returnArray;
    }
}
