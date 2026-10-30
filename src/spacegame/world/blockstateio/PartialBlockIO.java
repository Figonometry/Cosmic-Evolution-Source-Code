package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.PartialBlock;

public final class PartialBlockIO {
    public void savePartialBlockStates(Chunk chunk, NBTTagCompound nbtTagCompound) {
        PartialBlock partialBlockState;
        int totalCount = chunk.getBlockStateCount(MultiState.PARTIAL_BLOCK_STATE);
        PartialBlock[] partialBlockStates = this.getAllPartialBlocksInArray(chunk, totalCount);
        int partialBlockStateCount = 0;
        NBTTagCompound[] partialBlockStateTags = new NBTTagCompound[totalCount];
        for (int i = 0; i < partialBlockStateTags.length; i++) {

            partialBlockState = partialBlockStates[i];
            partialBlockStateTags[i] = new NBTTagCompound();
            partialBlockStateTags[i].setShort("blockID", partialBlockState.getBlockID());
            partialBlockStateTags[i].setInteger("index", partialBlockState.getIndex());
            partialBlockStateTags[i].setInteger("numLayers", partialBlockState.getNumLayers());

            nbtTagCompound.setTag("partialBlockState" + partialBlockStateCount, partialBlockStateTags[i]);
            partialBlockStateCount++;
        }
        nbtTagCompound.setInteger("partialBlockStateCount", partialBlockStateCount);
    }

    public void loadPartialBlockStates(Chunk chunk, NBTTagCompound nbtTagCompound) {
        int partialBlockStateCount = nbtTagCompound.getInteger("partialBlockStateCount");
        NBTTagCompound partialBlockStateLoadedTag;
        for (int i = 0; i < partialBlockStateCount; i++) {
            partialBlockStateLoadedTag = nbtTagCompound.getCompoundTag("partialBlockState" + i);
            short blockID = partialBlockStateLoadedTag.getShort("blockID");
            int index = partialBlockStateLoadedTag.getInteger("index");
            int numLayers = partialBlockStateLoadedTag.getInteger("numLayers");

            chunk.addBlockState(index, MultiState.PARTIAL_BLOCK_STATE, new PartialBlock(blockID, numLayers, index));
        }
    }

    private PartialBlock[] getAllPartialBlocksInArray(Chunk chunk, int totalCount) {
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.PARTIAL_BLOCK_STATE, totalCount);
        PartialBlock[] returnArray = new PartialBlock[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (PartialBlock) base[i];
        }


        return returnArray;
    }

}