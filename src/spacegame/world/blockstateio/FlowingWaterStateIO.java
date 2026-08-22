package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.DoorState;
import spacegame.world.blockstate.FlowingWaterState;
import spacegame.world.blockstate.MultiState;

public final class FlowingWaterStateIO {

    public void saveFlowingWaterStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        FlowingWaterState flowingWaterState;
        int totalCount = chunk.getBlockStateCount(MultiState.FLOWING_WATER_STATE);
        FlowingWaterState[] flowingWaterStates = this.getAllFlowingWaterStatesInArray(chunk, totalCount);
        int flowingWaterStateCount = 0;
        NBTTagCompound[] flowingWaterStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < flowingWaterStateTags.length; i++){

            flowingWaterState = flowingWaterStates[i];
            flowingWaterStateTags[i] = new NBTTagCompound();
            flowingWaterStateTags[i].setInteger("facingDirection", flowingWaterState.facingDirection);
            flowingWaterStateTags[i].setInteger("waterLevel", flowingWaterState.waterLevel);
            flowingWaterStateTags[i].setInteger("index", flowingWaterState.index);


            nbtTagCompound.setTag("flowingWaterState" + flowingWaterStateCount, flowingWaterStateTags[i]);
            flowingWaterStateCount++;
        }
        nbtTagCompound.setInteger("flowingWaterStateCount", flowingWaterStateCount);
    }

    public void loadFlowingWaterStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int flowingWaterStateCount = nbtTagCompound.getInteger("flowingWaterStateCount");
        NBTTagCompound flowingWaterStateLoadedTag;
        for (int i = 0; i < flowingWaterStateCount; i++) {
            flowingWaterStateLoadedTag = nbtTagCompound.getCompoundTag("flowingWaterState" + i);

            int facingDirection = flowingWaterStateLoadedTag.getInteger("facingDirection");
            int waterLevel = flowingWaterStateLoadedTag.getInteger("waterLevel");
            int index = flowingWaterStateLoadedTag.getInteger("index");


            chunk.addBlockState(index, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(facingDirection,waterLevel,index));
        }
    }
    private FlowingWaterState[] getAllFlowingWaterStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.FLOWING_WATER_STATE, totalCount);
        FlowingWaterState[] returnArray = new FlowingWaterState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (FlowingWaterState) base[i];
        }


        return returnArray;
    }

}
