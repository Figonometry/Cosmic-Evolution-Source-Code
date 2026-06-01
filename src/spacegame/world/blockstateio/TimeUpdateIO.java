package spacegame.world.blockstateio;

import spacegame.core.CosmicEvolution;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.TimeUpdateEvent;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeUpdateIO {

    public void saveTimeEvents(Chunk chunk, NBTTagCompound nbtTagCompound){
        int numberOfTimeEvents = this.getNumberOfTimeEvents(chunk);
        TimeUpdateEvent[] updateEvents = this.getAllTimeEventsInArray(chunk);
        TimeUpdateEvent timeUpdateEvent;
        int eventCount = 0;
        NBTTagCompound[] events = new NBTTagCompound[numberOfTimeEvents];
        for(int i = 0; i < events.length; i++){
            timeUpdateEvent = updateEvents[i];
            events[i] = new NBTTagCompound();
            events[i].setInteger("index", timeUpdateEvent.index);
            events[i].setLong("updateTime", timeUpdateEvent.updateTime);
            nbtTagCompound.setTag("event" + eventCount, events[i]);
            eventCount++;
        }
        nbtTagCompound.setInteger("eventCount", eventCount);
    }

    public void loadTimeEvents(Chunk chunk, NBTTagCompound nbtTagCompound){
        int eventCount = nbtTagCompound.getInteger("eventCount");
        NBTTagCompound eventLoadedTag;
        for (int i = 0; i < eventCount; i++) {
            eventLoadedTag = nbtTagCompound.getCompoundTag("event" + i);
            int index = eventLoadedTag.getInteger("index");
            long updateTime = eventLoadedTag.getLong("updateTime");
            if(updateTime <= CosmicEvolution.instance.save.time){
                updateTime = CosmicEvolution.instance.save.time + 120;
            }
            chunk.addTimeUpdateEvent(index, updateTime);
        }
    }



    private TimeUpdateEvent[] getAllTimeEventsInArray(Chunk chunk){
        int index = 0;
        TimeUpdateEvent event;
        TimeUpdateEvent[] timeUpdateEvents = new TimeUpdateEvent[this.getNumberOfTimeEvents(chunk)];
        Iterator<Map.Entry<Long, ConcurrentHashMap<Integer, TimeUpdateEvent>>> outerIterator = chunk.updateEvents.entrySet().iterator();
        while(outerIterator.hasNext()){
            Map.Entry<Long, ConcurrentHashMap<Integer, TimeUpdateEvent>> entry = outerIterator.next();
            Iterator<Map.Entry<Integer, TimeUpdateEvent>> innerIterator = entry.getValue().entrySet().iterator();
            while(innerIterator.hasNext()){
                Map.Entry<Integer, TimeUpdateEvent> entry1 = innerIterator.next();
                event = entry1.getValue();
                if(event != null){
                    timeUpdateEvents[index] = event;
                    index++;
                }
            }
        }
        return timeUpdateEvents;
    }

    private int getNumberOfTimeEvents(Chunk chunk){
        int total = 0;
        Iterator<Map.Entry<Long, ConcurrentHashMap<Integer, TimeUpdateEvent>>> outerIterator = chunk.updateEvents.entrySet().iterator();
        while(outerIterator.hasNext()){
            Map.Entry<Long, ConcurrentHashMap<Integer, TimeUpdateEvent>> entry = outerIterator.next();
            Iterator<Map.Entry<Integer, TimeUpdateEvent>> innerIterator = entry.getValue().entrySet().iterator();
            while(innerIterator.hasNext()){
                Map.Entry<Integer, TimeUpdateEvent> entry1 = innerIterator.next();
                total += entry.getValue() == null ? 0 : 1;
            }
        }
        return total;
    }
}
