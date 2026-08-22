package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.*;

public final class BerryBushStateIO {
    public void saveBerryBushStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        BerryBushState berryBushState;
        int totalCount = chunk.getBlockStateCount(MultiState.BERRY_BUSH_STATE);
        BerryBushState[] berryBushStates = this.getAllBerryBushStatesInArray(chunk, totalCount);
        int berryBushStateCount = 0;
        NBTTagCompound[] berryBushStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < berryBushStateTags.length; i++){

            berryBushState = berryBushStates[i];
            berryBushStateTags[i] = new NBTTagCompound();
            berryBushStateTags[i].setInteger("growthStage", berryBushState.growthStage);
            berryBushStateTags[i].setBoolean("isFlowering", berryBushState.isFlowering);
            berryBushStateTags[i].setBoolean("hasMatureFruit", berryBushState.hasMatureFruit);
            berryBushStateTags[i].setInteger("index", berryBushState.index);


            nbtTagCompound.setTag("berryBushState" + berryBushStateCount, berryBushStateTags[i]);
            berryBushStateCount++;
        }
        nbtTagCompound.setInteger("berryBushStateCount", berryBushStateCount);
    }

    public void loadBerryBushStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int berryBushStateCount = nbtTagCompound.getInteger("berryBushStateCount");
        NBTTagCompound berryBushLoadedTag;
        for (int i = 0; i < berryBushStateCount; i++) {
            berryBushLoadedTag = nbtTagCompound.getCompoundTag("berryBushState" + i);

            int growthStage = berryBushLoadedTag.getInteger("growthStage");
            boolean isFlowering = berryBushLoadedTag.getBoolean("isFlowering");
            boolean hasMatureFruit = berryBushLoadedTag.getBoolean("hasMatureFruit");
            int index = berryBushLoadedTag.getInteger("index");


            chunk.addBlockState(index, MultiState.BERRY_BUSH_STATE, new BerryBushState(growthStage,isFlowering,hasMatureFruit,index));
        }
    }
    private BerryBushState[] getAllBerryBushStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.BERRY_BUSH_STATE, totalCount);
        BerryBushState[] returnArray = new BerryBushState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (BerryBushState) base[i];
        }


        return returnArray;
    }
}
