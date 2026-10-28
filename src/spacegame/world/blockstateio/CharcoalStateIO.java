package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.CharcoalState;
import spacegame.world.blockstate.MultiState;

public final class CharcoalStateIO {
    public void saveCharcoalStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        CharcoalState charcoalState;
        int totalCount = chunk.getBlockStateCount(MultiState.CHARCOAL_STATE);
        CharcoalState[] charcoalStates = this.getAllCharcoalStatesInArray(chunk, totalCount);
        int charcoalStateCount = 0;
        NBTTagCompound[] charcoalStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < charcoalStateTags.length; i++){

            charcoalState = charcoalStates[i];
            charcoalStateTags[i] = new NBTTagCompound();
            charcoalStateTags[i].setInteger("index", charcoalState.index);
            charcoalStateTags[i].setInteger("numLayers", charcoalState.numLayers);

            nbtTagCompound.setTag("charcoalState" + charcoalStateCount, charcoalStateTags[i]);
            charcoalStateCount++;
        }
        nbtTagCompound.setInteger("charcoalStateCount", charcoalStateCount);
    }

    public void loadCharcoalStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int charcoalStateCount = nbtTagCompound.getInteger("charcoalStateCount");
        NBTTagCompound charcoalStateLoadedTag;
        for (int i = 0; i < charcoalStateCount; i++) {
            charcoalStateLoadedTag = nbtTagCompound.getCompoundTag("charcoalState" + i);
            int index = charcoalStateLoadedTag.getInteger("index");
            int numLayers = charcoalStateLoadedTag.getInteger("numLayers");

            chunk.addBlockState(index, MultiState.CHARCOAL_STATE, new CharcoalState(numLayers, index));
        }
    }
    private CharcoalState[] getAllCharcoalStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.CHARCOAL_STATE, totalCount);
        CharcoalState[] returnArray = new CharcoalState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (CharcoalState) base[i];
        }


        return returnArray;
    }
}
