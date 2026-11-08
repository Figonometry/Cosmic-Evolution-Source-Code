package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.OreState;

public class OreStateIO {
    public void saveOreStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        OreState oreState;
        int totalCount = chunk.getBlockStateCount(MultiState.ORE_STATE);
        OreState[] oreStates = this.getAllOreStatesInArray(chunk, totalCount);
        int oreStateCount = 0;
        NBTTagCompound[] oreStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < oreStateTags.length; i++){

            oreState = oreStates[i];
            oreStateTags[i] = new NBTTagCompound();
            oreStateTags[i].setInteger("index", oreState.getIndex());
            oreStateTags[i].setInteger("ore", oreState.getOre());
            oreStateTags[i].setInteger("oreRichness", oreState.getOreRichness());
            oreStateTags[i].setShort("parentStoneBlockID", oreState.getParentStoneBlockID());

            nbtTagCompound.setTag("oreState" + oreStateCount, oreStateTags[i]);
            oreStateCount++;
        }
        nbtTagCompound.setInteger("oreStateCount", oreStateCount);
    }

    public void loadOreStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int oreStateCount = nbtTagCompound.getInteger("oreStateCount");
        NBTTagCompound oreStateLoadedTag;
        for (int i = 0; i < oreStateCount; i++) {
            oreStateLoadedTag = nbtTagCompound.getCompoundTag("oreState" + i);
            int index = oreStateLoadedTag.getInteger("index");
            int ore = oreStateLoadedTag.getInteger("ore");
            int oreRichness = oreStateLoadedTag.getInteger("oreRichness");
            short parentStoneBlockID = oreStateLoadedTag.getShort("parentStoneBlockID");

            chunk.addBlockState(index, MultiState.ORE_STATE, new OreState(index, ore, oreRichness, parentStoneBlockID));
        }
    }
    private OreState[] getAllOreStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.ORE_STATE, totalCount);
        OreState[] returnArray = new OreState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (OreState) base[i];
        }


        return returnArray;
    }
}
