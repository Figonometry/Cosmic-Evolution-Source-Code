package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.TilledSoilState;

import java.util.Iterator;
import java.util.Map;

public class TilledSoilStateIO {

    public void saveTilledSoilStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        TilledSoilState tilledSoilState;
        TilledSoilState[] tilledSoilStates = this.getAllTilledSoilStatesInArray(chunk);
        int tilledSoilStateCount = 0;
        NBTTagCompound[] tilledSoilStateTags = new NBTTagCompound[chunk.tilledSoilStates.size()];
        for(int i = 0; i < tilledSoilStateTags.length; i++){
            tilledSoilState = tilledSoilStates[i];
            tilledSoilStateTags[i] = new NBTTagCompound();
            tilledSoilStateTags[i].setInteger("index", tilledSoilState.index);
            tilledSoilStateTags[i].setFloat("moisturePercent", tilledSoilState.moisturePercent);
            tilledSoilStateTags[i].setFloat("potassiumPercent", tilledSoilState.potassiumPercent);
            tilledSoilStateTags[i].setFloat("nitrogenPercent", tilledSoilState.nitrogenPercent);
            tilledSoilStateTags[i].setFloat("phosphorusPercent", tilledSoilState.phosphorusPercent);
            tilledSoilStateTags[i].setInteger("fertilizerID", tilledSoilState.fertilizerID);

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
            int fertilizerID = tilledSoilStateLoadedTag.getInteger("fertilizerID");
            chunk.addTilledSoilState(new TilledSoilState(index,moisturePercent,potassiumPercent,nitrogenPercent,phosphorusPercent,fertilizerID), index);
        }
    }


    public TilledSoilState[] getAllTilledSoilStatesInArray(Chunk chunk){
        int index = 0;
        TilledSoilState tilledSoilState;
        TilledSoilState[] tilledSoilStates1 = new TilledSoilState[chunk.tilledSoilStates.size()];
        Iterator<Map.Entry<Integer, TilledSoilState>> iterator = chunk.tilledSoilStates.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, TilledSoilState> entry = iterator.next();
            tilledSoilState = entry.getValue();
            if(tilledSoilState != null){
                tilledSoilStates1[index] = tilledSoilState;
                index++;
            }
        }
        return tilledSoilStates1;
    }

}
