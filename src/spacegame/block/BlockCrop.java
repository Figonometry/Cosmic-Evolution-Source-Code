package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiMutateCrop;
import spacegame.item.IDecayItem;
import spacegame.item.Item;
import spacegame.item.ItemSeed;
import spacegame.item.itemstate.SeedState;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.Crop;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.TilledSoilState;

public class BlockCrop extends Block implements ITimeUpdate {
    public BlockCrop(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        CropState cropState = (CropState) world.getBlockState(x,y,z, MultiState.CROP_STATE);

        Crop targetCrop = Crop.getCropFromName(cropState.name);
        EntityItem entityItem;
        EntityBlock entityBlock;
        SeedState outputSeedState;
        boolean mutationCompleted = false;
        long decayTime = 0L;

        if(this.ID == Block.deadCrop.ID){
            Crop thisCrop = Crop.getCropFromName(cropState.name);
            entityItem = new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5,
                    z + CosmicEvolution.globalRand.nextDouble(), thisCrop.finishedItemIDs[0],
                    Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY, decayTime, new SeedState(cropState.canMutate, cropState.percentToTargetCrop, cropState.targetCrop));

            entityItem.yaw = CosmicEvolution.globalRand.nextFloat(360f);
            world.addEntity(entityItem);
        }

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

                byte quantity = (byte) CosmicEvolution.globalRand.nextInt(3,8);

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
        TilledSoilState tilledSoilState = (TilledSoilState) world.getBlockState(x,y - 1,z, MultiState.TILLED_SOIL_STATE);
        if(tilledSoilState == null){
            world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime(x,y,z,world));
            return;
        }

        CropState cropState = (CropState) world.getBlockState(x,y,z, MultiState.CROP_STATE);
        if(cropState == null){
            world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime(x,y,z,world));
            return;
        }


        Crop crop = Crop.getCropFromName(cropState.name);

        double temperature = world.getDisplayTemperature(x,y,z);

        if(temperature > crop.maxTemp || temperature < crop.minTemp)cropState.damageValue++;

        if(cropState.damageValue >= 7){
            world.setBlockAndNotify(x,y,z, Block.deadCrop.ID, false);
        }

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
    public long getUpdateTime(int x, int y, int z, World world) {
        return Timer.GAME_DAY;
    }

    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        return "";
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        CropState cropState = (CropState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CROP_STATE);
        if(cropState == null)return "";
        Crop crop = Crop.getCropFromName(cropState.name);
        if(crop == null)return "";


        return crop.displayName + " Growth Stage: " + cropState.growthStage + "/" + crop.maxStages;
    }

    @Override
    public void addBlockStates(int x, int y, int z, short heldBlock, World world, EntityPlayer player, Chunk chunk){
        short heldItem = player.getHeldItem();
        TilledSoilState tilledSoilState = (TilledSoilState) world.getBlockState(x, y - 1, z, MultiState.TILLED_SOIL_STATE);

        SeedState seedState = (SeedState)player.getHeldItemState();

        if(seedState == null)throw new IllegalStateException("Itemstate in the player's hand is null");

        String cropName = ((ItemSeed)Item.list[heldItem]).getCropName();

        boolean canMutate = tilledSoilState.fertilizerID == TilledSoilState.BONEMEAL;
        if(canMutate && !seedState.canMutate){
            GuiMutateCrop guiMutateCrop = new GuiMutateCrop(world.ce, x, y, z, world, player, Crop.getCropFromName(cropName));
            if(!guiMutateCrop.close) {
                world.ce.setNewGui(guiMutateCrop);
            } else {
                tilledSoilState.fertilizerID = TilledSoilState.NO_FERTILIZER;
            }
        } else {
            chunk.addBlockState(x,y,z, MultiState.CROP_STATE, new CropState(Chunk.getBlockIndexFromCoordinates(x,y,z), cropName, canMutate, seedState.targetCrop, 0, seedState.percentToTargetCrop));
        }
    }

    public int getBlockTexture(int x, int y, int z, World world){
        CropState cropState = (CropState) world.getBlockState(x,y,z, MultiState.CROP_STATE);
        if(cropState == null)return BlockTextureList.WATER_TOP_TEXTURE; //if this fails for whatever reason it should be really obvious because this is the water texture

        Crop crop = Crop.getCropFromName(cropState.name);
        if(crop == null)return BlockTextureList.WATER_TOP_TEXTURE;

        if(cropState.growthStage == 0)return BlockTextureList.CROP_SEED_TEXTURE;

        switch (crop.displayName){
            case "Wild Grass" -> {
                return  BlockTextureList.WILD_GRASS_TEXTURE;
            }
            case "Einkorn Wheat", "Emmer Wheat" -> {
                switch (cropState.growthStage){
                    case 1 -> {
                        return BlockTextureList.EINKORN_WHEAT_1_TEXTURE;
                    }
                    case 2 -> {
                        return BlockTextureList.EINKORN_WHEAT_2_TEXTURE;
                    }
                    case 3 -> {
                        return BlockTextureList.EINKORN_WHEAT_3_TEXTURE;
                    }
                    case 4 -> {
                        return  BlockTextureList.EINKORN_WHEAT_4_TEXTURE;
                    }
                    case 5 -> {
                        return BlockTextureList.EINKORN_WHEAT_5_TEXTURE;
                    }
                    case 6 -> {
                        return BlockTextureList.EINKORN_WHEAT_6_TEXTURE;
                    }
                    case 7 -> {
                        return BlockTextureList.EINKORN_WHEAT_7_TEXTURE;
                    }
                    case 8 -> {
                        return BlockTextureList.EINKORN_WHEAT_8_TEXTURE;
                    }
                    default -> {
                        return 4;
                    }
                }
            }
            case "Standard Wheat", "Spelt Wheat" -> {
                switch (cropState.growthStage) {
                    case 1 -> {
                        return BlockTextureList.WHEAT_1_TEXTURE;
                    }
                    case 2 -> {
                        return BlockTextureList.WHEAT_2_TEXTURE;
                    }
                    case 3 -> {
                        return BlockTextureList.WHEAT_3_TEXTURE;
                    }
                    case 4 -> {
                        return BlockTextureList.WHEAT_4_TEXTURE;
                    }
                    case 5 -> {
                        return BlockTextureList.WHEAT_5_TEXTURE;
                    }
                    case 6 -> {
                        return BlockTextureList.WHEAT_6_TEXTURE;
                    }
                    case 7 -> {
                        return BlockTextureList.WHEAT_7_TEXTURE;
                    }
                    case 8 -> {
                        return BlockTextureList.WHEAT_8_TEXTURE;
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
