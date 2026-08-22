package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.DoorState;
import spacegame.world.blockstate.MultiState;

public final class DoorStateIO {
    public void saveDoorStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        DoorState doorState;
        int totalCount = chunk.getBlockStateCount(MultiState.DOOR_STATE);
        DoorState[] doorStates = this.getAllDoorStatesInArray(chunk, totalCount);
        int doorStateCount = 0;
        NBTTagCompound[] doorStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < doorStateTags.length; i++){

            doorState = doorStates[i];
            doorStateTags[i] = new NBTTagCompound();
            doorStateTags[i].setInteger("facingDirection", doorState.facingDirection);
            doorStateTags[i].setBoolean("isOpen", doorState.isOpen);
            doorStateTags[i].setBoolean("hingeLeft", doorState.hingeLeft);
            doorStateTags[i].setBoolean("hingeRight", doorState.hingeRight);
            doorStateTags[i].setInteger("index", doorState.index);


            nbtTagCompound.setTag("doorState" + doorStateCount, doorStateTags[i]);
            doorStateCount++;
        }
        nbtTagCompound.setInteger("cropStateCount", doorStateCount);
    }

    public void loadDoorStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int doorStateCount = nbtTagCompound.getInteger("doorStateCount");
        NBTTagCompound doorStateLoadedTag;
        for (int i = 0; i < doorStateCount; i++) {
            doorStateLoadedTag = nbtTagCompound.getCompoundTag("doorStateCount" + i);

            int facingDirection = doorStateLoadedTag.getInteger("facingDirection");
            boolean isOpen = doorStateLoadedTag.getBoolean("isOpen");
            boolean hingeLeft = doorStateLoadedTag.getBoolean("hingeLeft");
            boolean hingeRight = doorStateLoadedTag.getBoolean("hingeRight");
            int index = doorStateLoadedTag.getInteger("index");

            chunk.addBlockState(index, MultiState.DOOR_STATE, new DoorState(facingDirection,isOpen,hingeLeft,hingeRight,index));
        }
    }
    private DoorState[] getAllDoorStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.DOOR_STATE, totalCount);
        DoorState[] returnArray = new DoorState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (DoorState) base[i];
        }


        return returnArray;
    }
}
