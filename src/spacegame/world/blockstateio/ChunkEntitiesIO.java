package spacegame.world.blockstateio;

import org.joml.Vector3f;
import spacegame.entity.*;
import spacegame.item.itemstate.ItemState;
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
            entities[i].setFloat("pitch", savingEntity.pitch);
            entities[i].setFloat("yaw", savingEntity.yaw);

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
                    ItemState itemState = ItemState.loadFromCompoundTag(entityLoadedTag.getCompoundTag("ItemState"));
                    entityLoaded = new EntityItem(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("itemType"), (byte) 1, entityLoadedTag.getByte("count"), entityLoadedTag.getShort("durability"), entityLoadedTag.getLong("decayTime"), itemState);
                    entityLoaded.y += 0.1;
                    chunk.addEntityToList(entityLoaded);
                }
                case "EntityDeer" -> {
                    entityLoaded = new EntityDeer(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                    entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                    ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                    ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                    ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                    entityLoaded.y += 0.1;
                    chunk.addEntityToList(entityLoaded);
                }
                case "EntityWolf" -> {
                    entityLoaded = new EntityWolf(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                    entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                    ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                    ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                    ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                    entityLoaded.y += 0.1;
                    chunk.addEntityToList(entityLoaded);
                }
                case "EntityThrownSpear" -> {
                    entityLoaded = new EntityThrownSpear(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"),
                            new Vector3f(entityLoadedTag.getFloat("vec3fX"), entityLoadedTag.getFloat("vec3fY"), entityLoadedTag.getFloat("vec3fZ")),
                            entityLoadedTag.getDouble("speed"), entityLoadedTag.getShort("itemID"), entityLoadedTag.getFloat("pitch"),
                            entityLoadedTag.getFloat("yaw"), entityLoadedTag.getShort("itemDurability"));

                    entityLoaded.canMoveWithVector = entityLoadedTag.getBoolean("canMoveWithVector");
                    entityLoaded.collided = entityLoadedTag.getBoolean("collided");


                    chunk.addEntityToList(entityLoaded);
                }
            }
        }
    }
}
