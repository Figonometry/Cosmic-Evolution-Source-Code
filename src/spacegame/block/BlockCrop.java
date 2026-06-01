package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.IDecayItem;
import spacegame.item.Item;
import spacegame.item.ItemSeed;
import spacegame.item.itemstate.SeedState;
import spacegame.world.World;
import spacegame.world.blockstate.Crop;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.TilledSoilState;

public class BlockCrop extends Block implements ITimeUpdate {
    public BlockCrop(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        CropState cropState = world.getCropState(x,y,z);

        Crop targetCrop = Crop.getCropFromName(cropState.name);
        EntityItem entityItem;
        EntityBlock entityBlock;
        SeedState outputSeedState;
        boolean mutationCompleted = false;
        long decayTime = 0L;

        if(cropState.growthStage < Crop.getCropFromName(cropState.name).maxStages){
            super.onLeftClick(x,y,z, world, player);
            return;
        }

        if(cropState.canMutate){
            cropState.percentToTargetCrop += CosmicEvolution.globalRand.nextFloat(0.05f, 0.1f);
            if(cropState.percentToTargetCrop >= 1f){
                targetCrop = Crop.getCropFromName(cropState.targetCrop);
                mutationCompleted = true;
            } else {
                targetCrop = Crop.getCropFromName(cropState.name);
            }
        }

        for(int i = 0; i < targetCrop.finishedItemQuantities.length; i++){
            if(targetCrop.finishedBlockIDs == null) {

                if(Item.list[targetCrop.finishedItemIDs[i]] instanceof ItemSeed){
                    outputSeedState = new SeedState(cropState.canMutate, cropState.percentToTargetCrop, cropState.targetCrop);
                    if(mutationCompleted){
                        outputSeedState = new SeedState(false, 0, "no target");
                    }
                } else {
                    outputSeedState = null;
                }

                if(Item.list[targetCrop.finishedItemIDs[i]] instanceof IDecayItem){
                    decayTime = ((IDecayItem) Item.list[targetCrop.finishedItemIDs[i]]).getDecayTime() + world.ce.save.time;
                }

                byte quantity = (byte) CosmicEvolution.globalRand.nextInt(2,5);

                 boolean extraSeed = CosmicEvolution.globalRand.nextInt(100) <= 5;
                entityItem = new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5,
                        z + CosmicEvolution.globalRand.nextDouble(), targetCrop.finishedItemIDs[i],
                        Item.NULL_ITEM_METADATA, i > 0 ? quantity : (byte) 1, Item.NULL_ITEM_DURABILITY, decayTime, outputSeedState);

                entityItem.yaw = CosmicEvolution.globalRand.nextFloat(360f);
                world.addEntity(entityItem);

                if(extraSeed && i == 0){
                    entityItem = new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5,
                            z + CosmicEvolution.globalRand.nextDouble(), targetCrop.finishedItemIDs[i],
                            Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY, decayTime, outputSeedState);

                    entityItem.yaw = CosmicEvolution.globalRand.nextFloat(360f);
                    world.addEntity(entityItem);
                }


            } else {
                entityBlock = new EntityBlock(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5, z + + CosmicEvolution.globalRand.nextDouble(),
                        targetCrop.finishedBlockIDs[i], (byte)1);

                entityBlock.yaw = CosmicEvolution.globalRand.nextFloat(360f);
                world.addEntity(entityBlock);
            }

        }

        super.onLeftClick(x,y,z, world, player);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        TilledSoilState tilledSoilState = world.getTilledSoilState(x,y - 1,z);
        if(tilledSoilState == null){
            world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime());
            return;
        }

        CropState cropState = world.getCropState(x,y,z);
        if(cropState == null){
            world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime());
            return;
        }

        Crop crop = Crop.getCropFromName(cropState.name);
        cropState.growthStage++;
        world.notifyChunk(x,y,z);

        if(cropState.growthStage >= crop.maxStages){
            cropState.growthStage = crop.maxStages;
            return;
        }

        float potassiumAmount = tilledSoilState.potassiumPercent - crop.potassiumThreshold;
        float nitrogenAmount = tilledSoilState.nitrogenPercent - crop.nitrogenThreshold;
        float phosphorusAmount = tilledSoilState.phosphorusPercent - crop.phosphorusThreshold;
        float moistureAmount = tilledSoilState.moisturePercent - crop.moistureThreshold;

        float average = (potassiumAmount + nitrogenAmount + phosphorusAmount + moistureAmount) / 4f;
        long stageTime = crop.totalGrowthTime / crop.maxStages;

        long finalTime = (long) (stageTime - (stageTime * average));

        world.addTimeEvent(x,y,z, world.ce.save.time + finalTime);
    }

    @Override
    public long getUpdateTime() {
        return Timer.GAME_DAY;
    }

    @Override
    public String getDisplayStringText() {
        return "";
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        CropState cropState = CosmicEvolution.instance.save.activeWorld.getCropState(x,y,z);
        if(cropState == null)return "";
        Crop crop = Crop.getCropFromName(cropState.name);
        if(crop == null)return "";


        return crop.displayName + " Growth Stage: " + cropState.growthStage + "/" + crop.maxStages;
    }

    public int getBlockTexture(int x, int y, int z, World world){
        CropState cropState = world.getCropState(x,y,z);
        if(cropState == null)return 4; //if this fails for whatever reason it should be really obvious because this is the water texture

        Crop crop = Crop.getCropFromName(cropState.name);
        if(crop == null)return 4;

        if(cropState.growthStage == 0)return 76;

        switch (crop.displayName){
            case "Wild Grass" -> {
                return  74;
            }
            case "Einkorn Wheat" -> {
                switch (cropState.growthStage){
                    case 1 -> {
                        return 75;
                    }
                    case 2 -> {
                        return 78;
                    }
                    case 3 -> {
                        return 79;
                    }
                    case 4 -> {
                        return  80;
                    }
                    case 5 -> {
                        return 81;
                    }
                    case 6 -> {
                        return 82;
                    }
                    case 7 -> {
                        return 83;
                    }
                    case 8 -> {
                        return 84;
                    }
                    default -> {
                        return 4;
                    }
                }
            }
            default -> {
                return  4;
            }
        }

    }


}
