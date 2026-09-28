package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.*;

public final class TorchStateIO {
    public void saveTorchStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        TorchState torchState;
        int totalCount = chunk.getBlockStateCount(MultiState.TORCH_STATE);
        TorchState[] torchStates = this.getAllTorchStatesInArray(chunk, totalCount);
        int torchStateCount = 0;
        NBTTagCompound[] torchStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < torchStateTags.length; i++){

            torchState = torchStates[i];
            torchStateTags[i] = new NBTTagCompound();
            torchStateTags[i].setBoolean("isLit", torchState.isLit);
            torchStateTags[i].setBoolean("isBurnedOut", torchState.isBurnedOut);
            torchStateTags[i].setInteger("facingDirection", torchState.facingDirection);
            torchStateTags[i].setInteger("index", torchState.index);
            nbtTagCompound.setTag("torchState" + torchStateCount, torchStateTags[i]);
            torchStateCount++;
        }
        nbtTagCompound.setInteger("torchStateCount", torchStateCount);
    }

    public void loadTorchStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int torchStateCount = nbtTagCompound.getInteger("torchStateCount");
        NBTTagCompound torchStateLoadedTag;
        for (int i = 0; i < torchStateCount; i++) {
            torchStateLoadedTag = nbtTagCompound.getCompoundTag("torchState" + i);
            int index = torchStateLoadedTag.getInteger("index");
            boolean isLit = torchStateLoadedTag.getBoolean("isLit");
            boolean isBurnedOut = torchStateLoadedTag.getBoolean("isBurnedOut");
            int facingDirection = torchStateLoadedTag.getInteger("facingDirection");

            chunk.addBlockState(index, MultiState.TORCH_STATE, new TorchState(isLit,isBurnedOut,facingDirection,index));
        }
    }
    private TorchState[] getAllTorchStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.TORCH_STATE, totalCount);
        TorchState[] returnArray = new TorchState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (TorchState) base[i];
        }


        return returnArray;
    }
}
