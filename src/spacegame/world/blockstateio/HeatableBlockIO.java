package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.HeatableBlockLocation;

import java.util.Iterator;
import java.util.Map;

public class HeatableBlockIO {

    public void saveHeatableBlocks(Chunk chunk, NBTTagCompound nbtTagCompound){
        HeatableBlockLocation heatableBlockLocation;
        HeatableBlockLocation[] heatableBlockLocations = this.getAllHeatableBlockLocationsInArray(chunk);
        int heatableBlockCount = 0;
        NBTTagCompound[] heatableBlockTags = new NBTTagCompound[chunk.heatableBlocks.size()];
        for(int i = 0; i < heatableBlockTags.length; i++){

            heatableBlockLocation = heatableBlockLocations[i];
            heatableBlockTags[i] = new NBTTagCompound();
            heatableBlockTags[i].setInteger("index", heatableBlockLocation.index);
            heatableBlockTags[i].setFloat("currentTemperature", heatableBlockLocation.currentTemperature);
            heatableBlockTags[i].setShort("currentFuelBurning", heatableBlockLocation.currentFuelBurning);
            heatableBlockTags[i].setLong("fuelBurnoutTime", heatableBlockLocation.fuelBurnoutTime);
            heatableBlockTags[i].setLong("heatingFinishTime", heatableBlockLocation.heatingFinishTime);
            heatableBlockTags[i].setBoolean("heating", heatableBlockLocation.heating);
            heatableBlockTags[i].setLong("heatStartTime", heatableBlockLocation.heatStartTime);
            heatableBlockTags[i].setLong("fuelStartTime", heatableBlockLocation.fuelStartTime);

            nbtTagCompound.setTag("heatableBlock" + heatableBlockCount, heatableBlockTags[i]);
            heatableBlockCount++;
        }
        nbtTagCompound.setInteger("heatableBlockCount", heatableBlockCount);
    }

    public void loadHeatableBlocks(Chunk chunk, NBTTagCompound nbtTagCompound) {
        int heatableBlockCount = nbtTagCompound.getInteger("heatableBlockCount");
        NBTTagCompound heatableBlockLoadedTag;
        HeatableBlockLocation heatableBlockLocation;
        for (int i = 0; i < heatableBlockCount; i++) {
            heatableBlockLoadedTag = nbtTagCompound.getCompoundTag("heatableBlock" + i);
            int index = heatableBlockLoadedTag.getInteger("index");
            float currentTemperature = heatableBlockLoadedTag.getFloat("currentTemperature");
            short currentFuelBurning = heatableBlockLoadedTag.getShort("currentFuelBurning");
            long fuelBurnoutTime = heatableBlockLoadedTag.getLong("fuelBurnoutTime");
            long heatingFinishTime = heatableBlockLoadedTag.getLong("heatingFinishTime");
            long fuelStartTime = heatableBlockLoadedTag.getLong("fuelStartTime");
            long heatStartTime = heatableBlockLoadedTag.getLong("heatStartTime");
            boolean heating = heatableBlockLoadedTag.getBoolean("heating");
            heatableBlockLocation = new HeatableBlockLocation(index);
            heatableBlockLocation.currentTemperature = currentTemperature;
            heatableBlockLocation.currentFuelBurning = currentFuelBurning;
            heatableBlockLocation.fuelBurnoutTime = fuelBurnoutTime;
            heatableBlockLocation.heatingFinishTime = heatingFinishTime;
            heatableBlockLocation.heating = heating;
            heatableBlockLocation.fuelStartTime = fuelStartTime;
            heatableBlockLocation.heatStartTime = heatStartTime;
            chunk.addHeatableBlock(heatableBlockLocation);
        }
    }

    private HeatableBlockLocation[] getAllHeatableBlockLocationsInArray(Chunk chunk){
        int index = 0;
        HeatableBlockLocation heatableBlockLocation;
        HeatableBlockLocation[] heatableBlockLocations = new HeatableBlockLocation[chunk.heatableBlocks.size()];
        Iterator<Map.Entry<Integer, HeatableBlockLocation>> iterator = chunk.heatableBlocks.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, HeatableBlockLocation> entry = iterator.next();
            heatableBlockLocation = entry.getValue();
            if(heatableBlockLocation != null){
                heatableBlockLocations[index] = heatableBlockLocation;
                index++;
            }
        }
        return heatableBlockLocations;
    }
}
