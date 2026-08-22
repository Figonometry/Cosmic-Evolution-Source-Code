package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public final class LogStateIO {
    public void saveLogStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        LogState logState;
        int totalCount = chunk.getBlockStateCount(MultiState.LOG_STATE);
        LogState[] logStates = this.getAllLogStatesInArray(chunk, totalCount);
        int logStateCount = 0;
        NBTTagCompound[] logStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < logStateTags.length; i++){

            logState = logStates[i];
            logStateTags[i] = new NBTTagCompound();
            logStateTags[i].setInteger("facingDirection", logState.facingDirection);
            logStateTags[i].setInteger("size", logState.facingDirection);
            logStateTags[i].setInteger("index", logState.index);

            nbtTagCompound.setTag("logState" + logStateCount, logStateTags[i]);
            logStateCount++;
        }
        nbtTagCompound.setInteger("logStateCount", logStateCount);
    }

    public void loadLogStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int logStateCount = nbtTagCompound.getInteger("logStateCount");
        NBTTagCompound logStateLoadedTag;
        for (int i = 0; i < logStateCount; i++) {
            logStateLoadedTag = nbtTagCompound.getCompoundTag("logState" + i);
            int facingDirection = logStateLoadedTag.getInteger("facingDirection");
            int size = logStateLoadedTag.getInteger("size");
            int index = logStateLoadedTag.getInteger("index");

            chunk.addBlockState(index, MultiState.LOG_STATE, new LogState(facingDirection,size,index));
        }
    }
    private LogState[] getAllLogStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.LOG_STATE, totalCount);
        LogState[] returnArray = new LogState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (LogState) base[i];
        }


        return returnArray;
    }
}
