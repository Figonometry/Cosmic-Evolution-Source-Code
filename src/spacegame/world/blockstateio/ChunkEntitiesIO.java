package spacegame.world.blockstateio;

import spacegame.entity.*;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;

public class ChunkEntitiesIO {

    public void saveEntities(Chunk chunk, NBTTagCompound nbtTagCompound){
        Entity savingEntity;
        int entityCount = 0;
        NBTTagCompound[] entities = new NBTTagCompound[chunk.entities.size()];
        for(int i = 0; i < entities.length; i++){
            savingEntity = chunk.entities.get(i);
            entities[i] = new NBTTagCompound();
            entities[i].setDouble("x", savingEntity.x);
            entities[i].setDouble("y", savingEntity.y);
            entities[i].setDouble("z", savingEntity.z);

            savingEntity.saveToNBT(entities[i]);

            nbtTagCompound.setTag("entity" + entityCount, entities[i]);
            entityCount++;
        }
        nbtTagCompound.setInteger("entityCount", entityCount);
    }

    public void loadEntities(Chunk chunk, NBTTagCompound nbtTagCompound){
        int entityCount = nbtTagCompound.getInteger("entityCount");
        NBTTagCompound entityLoadedTag;
        Entity entityLoaded;
        for (int i = 0; i < entityCount; i++) {
            entityLoadedTag = nbtTagCompound.getCompoundTag("entity" + i);
            switch (entityLoadedTag.getString("entityType")) {
                case "EntityBlock" -> {
                    entityLoaded = new EntityBlock(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("blockType"), entityLoadedTag.getByte("count"));
                    entityLoaded.y += 0.1;
                    chunk.addEntityToList(entityLoaded);
                }
                case "EntityItem" -> {
                    entityLoaded = new EntityItem(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("itemType"), (byte) 1, entityLoadedTag.getByte("count"), entityLoadedTag.getShort("durability"), 0, null);
                    entityLoaded.y += 0.1;
                    chunk.addEntityToList(entityLoaded);
                }
                case "EntityDeer" -> {
                    entityLoaded = new EntityDeer(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                    entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                    ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                    ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                    ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                    chunk.addEntityToList(entityLoaded);
                }
                case "EntityWolf" -> {
                    entityLoaded = new EntityWolf(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                    entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                    ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                    ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                    ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                    chunk.addEntityToList(entityLoaded);
                }
            }
        }
    }
}
