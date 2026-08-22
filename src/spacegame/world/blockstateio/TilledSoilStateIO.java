package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.ReedState;
import spacegame.world.blockstate.TilledSoilState;

import java.util.Map;

public class TilledSoilStateIO {

    public void saveTilledSoilStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        TilledSoilState tilledSoilState;
        int totalCount = chunk.getBlockStateCount(MultiState.TILLED_SOIL_STATE);
        TilledSoilState[] tilledSoilStates = this.getAllTilledSoilStatesInArray(chunk, totalCount);
        int tilledSoilStateCount = 0;
        NBTTagCompound[] tilledSoilStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < tilledSoilStateTags.length; i++){
            tilledSoilState = tilledSoilStates[i];
            tilledSoilStateTags[i] = new NBTTagCompound();
            tilledSoilStateTags[i].setInteger("index", tilledSoilState.index);
            tilledSoilStateTags[i].setFloat("moisturePercent", tilledSoilState.moisturePercent);
            tilledSoilStateTags[i].setFloat("potassiumPercent", tilledSoilState.potassiumPercent);
            tilledSoilStateTags[i].setFloat("nitrogenPercent", tilledSoilState.nitrogenPercent);
            tilledSoilStateTags[i].setFloat("phosphorusPercent", tilledSoilState.phosphorusPercent);
            tilledSoilStateTags[i].setInteger("fertilizerID", tilledSoilState.fertilizerID);
            tilledSoilStateTags[i].setFloat("maxNutrientLevel", tilledSoilState.maxNutrientLevel);

            nbtTagCompound.setTag("tilledSoilState" + tilledSoilStateCount, tilledSoilStateTags[i]);
            tilledSoilStateCount++;
        }
        nbtTagCompound.setInteger("tilledSoilStateCount", tilledSoilStateCount);
    }

    public void loadTilledSoilStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int tilledSoilStateCount = nbtTagCompound.getInteger("tilledSoilStateCount");
        NBTTagCompound tilledSoilStateLoadedTag;
        for (int i = 0; i < tilledSoilStateCount; i++) {
            tilledSoilStateLoadedTag = nbtTagCompound.getCompoundTag("tilledSoilState" + i);
            int index = tilledSoilStateLoadedTag.getInteger("index");
            float moisturePercent = tilledSoilStateLoadedTag.getFloat("moisturePercent");
            float potassiumPercent = tilledSoilStateLoadedTag.getFloat("potassiumPercent");
            float nitrogenPercent = tilledSoilStateLoadedTag.getFloat("nitrogenPercent");
            float phosphorusPercent = tilledSoilStateLoadedTag.getFloat("phosphorusPercent");
            float maxNutrientLevel = tilledSoilStateLoadedTag.getFloat("maxNutrientLevel");
            int fertilizerID = tilledSoilStateLoadedTag.getInteger("fertilizerID");
            TilledSoilState tilledSoilState = new TilledSoilState(index,moisturePercent,potassiumPercent,nitrogenPercent,phosphorusPercent,fertilizerID);
            tilledSoilState.maxNutrientLevel = maxNutrientLevel;
            chunk.addBlockState(index, MultiState.TILLED_SOIL_STATE, tilledSoilState);
        }
    }


    public TilledSoilState[] getAllTilledSoilStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.TILLED_SOIL_STATE, totalCount);
        TilledSoilState[] returnArray = new TilledSoilState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (TilledSoilState) base[i];
        }


        return returnArray;
    }

}
