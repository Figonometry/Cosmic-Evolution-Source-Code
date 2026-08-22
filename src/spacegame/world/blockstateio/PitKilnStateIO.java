package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.*;

public final class PitKilnStateIO {
    public void savePitKilnStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        PitKilnState pitKilnState;
        int totalCount = chunk.getBlockStateCount(MultiState.PIT_KILN_STATE);
        PitKilnState[] pitKilnStates = this.getAllPitKilnStatesInArray(chunk, totalCount);
        int pitKilnStateCount = 0;
        NBTTagCompound[] pitKilnStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < pitKilnStateTags.length; i++){

            pitKilnState = pitKilnStates[i];
            pitKilnStateTags[i] = new NBTTagCompound();
            pitKilnStateTags[i].setInteger("strawCount", pitKilnState.strawCount);
            pitKilnStateTags[i].setInteger("logCount", pitKilnState.logCount);
            pitKilnStateTags[i].setBoolean("isLit", pitKilnState.isLit);
            pitKilnStateTags[i].setInteger("index", pitKilnState.index);

            nbtTagCompound.setTag("pitKilnState" + pitKilnStateCount, pitKilnStateTags[i]);
            pitKilnStateCount++;
        }
        nbtTagCompound.setInteger("pitKilnStateCount", pitKilnStateCount);
    }

    public void loadPitKilnStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int pitKilnStateCount = nbtTagCompound.getInteger("pitKilnStateCount");
        NBTTagCompound pitKilnStateLoadedTag;
        for (int i = 0; i < pitKilnStateCount; i++) {
            pitKilnStateLoadedTag = nbtTagCompound.getCompoundTag("pitKilnState" + i);

            int strawCount = pitKilnStateLoadedTag.getInteger("strawCount");
            int logCount = pitKilnStateLoadedTag.getInteger("logCount");
            boolean isLit = pitKilnStateLoadedTag.getBoolean("isLit");
            int index = pitKilnStateLoadedTag.getInteger("index");


            chunk.addBlockState(index, MultiState.PIT_KILN_STATE, new PitKilnState(strawCount,logCount,isLit,index));
        }
    }
    private PitKilnState[] getAllPitKilnStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.PIT_KILN_STATE, totalCount);
        PitKilnState[] returnArray = new PitKilnState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (PitKilnState) base[i];
        }


        return returnArray;
    }
}
