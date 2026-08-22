package spacegame.render;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import spacegame.block.*;
import spacegame.item.Item;
import spacegame.item.ItemTool;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.util.LongHasher;
import spacegame.util.MathUtil;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

public class RenderBlocks {
    public float red = 1.0F;
    public float green = 1.0F;
    public float blue = 1.0F;
    public float redReset = 1f;
    public float greenReset = 1f;
    public float blueReset = 1f;
    public float skyLightReset = 1f;
    public float skyLightValue = 1.0f;
    public short grayScaleImageMultiplier;
    private float highestChannel = 1.0f;
    public ArrayList<int[]> fireWoodPileLogCounts = new ArrayList<>(); //Index then log count, size 2
    public ArrayList<int[]> brickPileBrickCounts = new ArrayList<>();
    public static final int[] craftingTextureOffsetX = new int[144];
    public static final int[] craftingTextureOffsetY = new int[144];
    public static final int TOP_FACE = 0;
    public static final int BOTTOM_FACE = 1;
    public static final int NORTH_FACE = 2;
    public static final int SOUTH_FACE = 3;
    public static final int EAST_FACE = 4;
    public static final int WEST_FACE = 5;

    static {
        for(int i = 0; i < 144; i++){
            Random rand = new Random();
            craftingTextureOffsetX[i] =  rand.nextInt(0, 31);
            craftingTextureOffsetY[i] =  rand.nextInt(0, 31);
        }
    }


    public void renderStandardBlock(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        this.handleWaterLoggedBlocks(chunk, world, block, index, face);

        ModelLoader model = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();
        for (int i = 0; i < model.modelFaces.length; i++) {
            if(face != model.modelFaces[i].faceType)continue;
            renderOpaqueFace(chunk, world, block, index, face, model.modelFaces[i], greedyMeshSize);
        }
    }


    private void handleWaterLoggedBlocks(Chunk chunk, World world, short block, int index, int face){
        if(Block.list[block].waterlogged && !(Block.list[block] instanceof BlockWater)){
            if((face == TOP_FACE && !Block.list[world.getBlockID(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) + 1, chunk.getBlockZFromIndex(index))].isSolid) ||
                    (face == BOTTOM_FACE && !Block.list[world.getBlockID(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) - 1, chunk.getBlockZFromIndex(index))].isSolid) ||
                    (face == NORTH_FACE && !Block.list[world.getBlockID(chunk.getBlockXFromIndex(index) - 1, chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index))].isSolid) ||
                    (face == SOUTH_FACE && !Block.list[world.getBlockID(chunk.getBlockXFromIndex(index) + 1, chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index))].isSolid) ||
                    (face == EAST_FACE && !Block.list[world.getBlockID(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index) - 1)].isSolid)  ||
                    (face == WEST_FACE && !Block.list[world.getBlockID(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index) + 1)].isSolid)){
                this.renderTransparentBlock(chunk, world, Block.water.ID, index, face, new int[2]);
            }
        }
    }

    public  void renderTransparentBlock(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelLoader model = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();
        for (int i = 0; i < model.modelFaces.length; i++) {
            if(face != model.modelFaces[i].faceType)continue;
            renderTransparentFace(chunk, world, block, index, face, model.modelFaces[i], greedyMeshSize);
        }
    }

    public void renderFullWater(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelLoader model = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();

        int[] coordinates = chunk.getBlockCoordinatesFromIndex(index);


        switch (face){
            case NORTH_FACE -> {
                if(world.getBlockID(coordinates[0] - 1, coordinates[1], coordinates[2]) == Block.flowingWater.ID){
                    model = BlockModelList.topOfFullWater.copyModel();
                }
            }
            case SOUTH_FACE -> {
                if(world.getBlockID(coordinates[0] + 1, coordinates[1], coordinates[2]) == Block.flowingWater.ID){
                    model = BlockModelList.topOfFullWater.copyModel();
                }
            }
            case EAST_FACE -> {
                if(world.getBlockID(coordinates[0], coordinates[1], coordinates[2] - 1) == Block.flowingWater.ID){
                    model = BlockModelList.topOfFullWater.copyModel();
                }
            }
            case WEST_FACE -> {
                if(world.getBlockID(coordinates[0], coordinates[1], coordinates[2] + 1) == Block.flowingWater.ID){
                    model = BlockModelList.topOfFullWater.copyModel();
                }
            }
        }

        for (int i = 0; i < model.modelFaces.length; i++) {
            if(face != model.modelFaces[i].faceType)continue;
            renderTransparentFace(chunk, world, block, index, face, model.modelFaces[i], new int[2]);
        }
    }



    public void renderCampFire(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        this.renderCampFireUnlit(chunk, world, block, index, face);

        CampfireState campfireState = (CampfireState) chunk.getBlockState(index, MultiState.CAMPFIRE_STATE);
        if(campfireState == null)return;

        if(campfireState.isLit) {
            ModelLoader shrunkBlock = BlockModelList.fireBlockModel.copyModel();
            shrunkBlock.scaleModel(0.4f);
            shrunkBlock.translateModel(0.3f, 0f, 0.3f);
            ModelFace[] modelFace = shrunkBlock.getModelFaceOfType(face);
            for (int i = 0; i < modelFace.length; i++) {
                if (modelFace[i] == null) continue;
                if (modelFace[i].faceType != face) continue;
                this.renderOpaqueFace(chunk, world, Block.fire.ID, index, face, modelFace[i], new int[2]);
            }
        }

        //Render stick model
        ModelLoader stickModel = BlockModelList.campFireStickFull.copyModel();
        switch (campfireState.cookingStickCount){
            case 1 -> {
                stickModel = BlockModelList.campFireStick1.copyModel();
            }
            case 2 -> {
                stickModel = BlockModelList.campFireStick2.copyModel();
            }
            case 3 -> {
                stickModel = BlockModelList.campFireStick3.copyModel();
            }
            case 4 -> {
                stickModel = BlockModelList.campFireStick4.copyModel();
            }
            case 5 -> {
                stickModel = BlockModelList.campFireStickFull.copyModel();
            }
        }

        if(campfireState.cookingStickCount == 0)return;

        for(int i = 0; i < stickModel.modelFaces.length; i++){
            if(stickModel.modelFaces[i].faceType != face)continue;
            this.renderOpaqueFace(chunk, world, Block.itemStick.ID, index, face, stickModel.modelFaces[i], new int[2]);
        }

        ChestLocation chestLocation = (ChestLocation) chunk.getBlockState(index, MultiState.CHEST_STATE);
        if(chestLocation == null)return;
        if(chestLocation.inventory.itemStacks[0].item == null)return;

        ModelLoader itemModel = chestLocation.inventory.itemStacks[0].item.itemModel.copyModel();
        itemModel.translateModel(0.5f,0.75f, 0.5f);
        for(int i = 0; i < itemModel.modelFaces.length; i++){
            if(itemModel.modelFaces[i].faceType != face)continue;
            this.renderOpaqueFace(chunk, world, Block.itemBlock.ID, index, face, itemModel.modelFaces[i], new int[2]); //Renders item
        }

    }

    public void renderCampFireUnlit(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelFace[] modelFaces = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).getModelFaceOfType(face);
        for (int i = 0; i < modelFaces.length; i++) {
            renderOpaqueFace(chunk, world, block, index, face, modelFaces[i], new int[2]);
            if(modelFaces[i] != null){
                if(modelFaces[i].faceType == TOP_FACE){
                    renderOpaqueFace(chunk, world, block, index, face, BlockModelList.topFaceBlockModel.getModelFace(TOP_FACE), new int[2]); //This is for the staw covering on the ground
                }
            }
        }
    }

    public void renderLogOnCampfire(Chunk chunk, World world, short block, int index, int face) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        CampfireState campfireState = (CampfireState) chunk.getBlockState(index, MultiState.CAMPFIRE_STATE);
        if(campfireState == null)return;
        int logCount = campfireState.logCount; //Get log count and draw logs above the base

        block = Block.fireWoodBlock.ID; //Switch to this block id in order to bind the correct image in the texture array

        float log1XRot = 10;
        float log1YRot = 45;
        float log1ZRot = 5;
        Vector3f translationVectorLog1 = new Vector3f(0.5f, 0.135f, 0.5f);

        float log2XRot = 45;
        float log2YRot = -45;
        float log2ZRot = 70;
        Vector3f translationVectorLog2 = new Vector3f(0.5f, 0.25f, 0.35f);

        float log3XRot = 45;
        float log3YRot = 0;
        float log3ZRot = 60;
        Vector3f translationVectorLog3 = new Vector3f(0.3f, 0.25f, 0.5f);

        float log4XRot = -45;
        float log4YRot = 180;
        float log4ZRot = -50;
        Vector3f translationVectorLog4 = new Vector3f(0.6f, 0.25f, 0.6f);


        //16x4 top and bottom, 4x4 north and south, 4x16 east and west sample
        ModelLoader baseModel = BlockModelList.fireWood;
        ModelFace modelFace;

        switch (logCount) { //This switch statement is not supposed to have case labels, I'm intentionally using the follow through of a default switch statement to reduce LOC, this is also why it's in reverse order
            case 4:
                ModelLoader log4 = baseModel.copyModel();
                log4.rotateModel(log4XRot, 1, 0, 0);
                log4.rotateModel(log4YRot, 0, 1, 0);
                log4.rotateModel(log4ZRot, 0, 0, 1);
                log4.translateModel(translationVectorLog4.x, translationVectorLog4.y, translationVectorLog4.z);


                modelFace = log4.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 3:
                ModelLoader log3 = baseModel.copyModel();

                log3.rotateModel(log3XRot, 1, 0, 0);
                log3.rotateModel(log3YRot, 0, 1, 0);
                log3.rotateModel(log3ZRot, 0, 0, 1);
                log3.translateModel(translationVectorLog3.x, translationVectorLog3.y, translationVectorLog3.z);

                modelFace = log3.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 2:
                ModelLoader log2 = baseModel.copyModel();

                log2.rotateModel(log2XRot, 1, 0, 0);
                log2.rotateModel(log2YRot, 0, 1, 0);
                log2.rotateModel(log2ZRot, 0, 0, 1);
                log2.translateModel(translationVectorLog2.x, translationVectorLog2.y, translationVectorLog2.z);

                modelFace = log2.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 1:
                ModelLoader log1 = baseModel.copyModel();

                log1.rotateModel(log1XRot, 1, 0, 0);
                log1.rotateModel(log1YRot, 0, 1, 0);
                log1.rotateModel(log1ZRot, 0, 0, 1);
                log1.translateModel(translationVectorLog1.x, translationVectorLog1.y, translationVectorLog1.z);

                modelFace = log1.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

        }
    }

    public void renderGrassBlock(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelLoader blockModel = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();
        ModelFace[] modelFaces = blockModel.getModelFaceOfType(face);

        int soilGrassLevel = BlockGrass.getGrassLevel(block);
        float underlyingSoilFertility = 0;


        for (int i = 0; i < modelFaces.length; i++) {
            if (modelFaces[i] == null) continue;
            if (modelFaces[i].faceType != face) continue;

            //Render the underlying soil
            if (face == TOP_FACE && soilGrassLevel != BlockGrass.GRASS_FULL) {
                underlyingSoilFertility = BlockSoil.getUnderlyingSoilTextureTop(block);
                this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, underlyingSoilFertility);
            } else if (face == NORTH_FACE || face == SOUTH_FACE || face == EAST_FACE || face == WEST_FACE) {
                underlyingSoilFertility = BlockSoil.getUnderlyingSoilTextureSide(block);
                this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, underlyingSoilFertility);
            } else if(face == BOTTOM_FACE){
                underlyingSoilFertility = BlockSoil.getSoilFertility(block);
                this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, underlyingSoilFertility);
            }

            if(face == BOTTOM_FACE)continue;

            //Render the grass/snow texture
            boolean snowAbove = Block.list[world.getBlockID(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) + 1, chunk.getBlockZFromIndex(index))] instanceof BlockSnow;
            boolean belowFreezing = world.getTemperatureWithTimeOfDay(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index)) <= 0.3;
            float textureID = BlockTextureList.WATER_TOP_TEXTURE; //If this fails this should be very obvious
            switch (soilGrassLevel) {
                case BlockGrass.GRASS_SMALL_PATCHES -> {
                    textureID = face == TOP_FACE ? BlockTextureList.GRASS_SMALL_PATCHES_TOP_TEXTURE : snowAbove ? BlockTextureList.SNOWY_GRASS_SMALL_PATCHES_SIDE_TEXTURE : BlockTextureList.GRASS_SMALL_PATCHES_SIDE_TEXTURE;
                }
                case BlockGrass.GRASS_LARGE_PATCHES -> {
                    textureID = face == TOP_FACE ? BlockTextureList.GRASS_LARGE_PATCHES_TOP_TEXTURE : snowAbove ? BlockTextureList.SNOWY_GRASS_LARGE_PATCHES_SIDE_TEXTURE : BlockTextureList.GRASS_LARGE_PATCHES_SIDE_TEXTURE;
                }
                case BlockGrass.GRASS_FULL -> {
                    textureID = face == TOP_FACE ? BlockTextureList.GRASS_FULL_TOP_TEXTURE : snowAbove ? BlockTextureList.SNOWY_GRASS_FULL_SIDE_TEXTURE : BlockTextureList.GRASS_FULL_SIDE_TEXTURE;
                }
            }

            if(belowFreezing && face == TOP_FACE){
                textureID = BlockTextureList.SNOW_TEXTURE;
            }

            this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, textureID);
        }
    }

    public void renderClayGrass(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        int soilGrassLevel = BlockGrass.getGrassLevel(block);
        float underlyingClayTexture = 0;

        ModelLoader blockModel = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();
        ModelFace[] modelFaces = blockModel.getModelFaceOfType(face);

        for (int i = 0; i < modelFaces.length; i++) {
            if (modelFaces[i] == null) continue;
            if (modelFaces[i].faceType != face) continue;

            if (face == TOP_FACE && soilGrassLevel != BlockGrass.GRASS_FULL) {
                underlyingClayTexture = BlockClayGrass.getUnderlyingClayTextureTop(block);
                this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, underlyingClayTexture);
            } else if (face == NORTH_FACE || face == SOUTH_FACE || face == EAST_FACE || face == WEST_FACE) {
                underlyingClayTexture = BlockClayGrass.getUnderlyingClayTextureSide(block);
                this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, underlyingClayTexture);
            } else if(face == BOTTOM_FACE){
                this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, BlockTextureList.CLAY_TEXTURE);
            }

            if(face == BOTTOM_FACE)continue;

            boolean snowAbove = Block.list[world.getBlockID(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) + 1, chunk.getBlockZFromIndex(index))] instanceof BlockSnow;
            float textureID = BlockTextureList.WATER_TOP_TEXTURE; //If this fails this should be very obvious
            switch (soilGrassLevel) {
                case BlockGrass.GRASS_SMALL_PATCHES -> {
                    textureID = face == TOP_FACE ? BlockTextureList.GRASS_SMALL_PATCHES_TOP_TEXTURE : snowAbove ? BlockTextureList.SNOWY_GRASS_SMALL_PATCHES_SIDE_TEXTURE : BlockTextureList.GRASS_SMALL_PATCHES_SIDE_TEXTURE;
                }
                case BlockGrass.GRASS_LARGE_PATCHES -> {
                    textureID = face == TOP_FACE ? BlockTextureList.GRASS_LARGE_PATCHES_TOP_TEXTURE : snowAbove ? BlockTextureList.SNOWY_GRASS_LARGE_PATCHES_SIDE_TEXTURE : BlockTextureList.GRASS_LARGE_PATCHES_SIDE_TEXTURE;
                }
                case BlockGrass.GRASS_FULL -> {
                    textureID = face == TOP_FACE ? BlockTextureList.GRASS_FULL_TOP_TEXTURE : snowAbove ? BlockTextureList.SNOWY_GRASS_FULL_SIDE_TEXTURE : BlockTextureList.GRASS_FULL_SIDE_TEXTURE;
                }
            }

            this.renderOpaqueFaceDirect(chunk, world, block, index, face, modelFaces[i], greedyMeshSize, textureID);
        }
    }

    public void renderPitKiln(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        if(face == TOP_FACE){
            ModelLoader model = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();
            model.translateModel(0, -1 + (0.109375f * ((BlockPitKiln)Block.list[block]).getStrawHeight((PitKilnState) chunk.getBlockState(index, MultiState.PIT_KILN_STATE))), 0);

            renderOpaqueFace(chunk, world, block, index, face, model.getModelFace(face) , new int[2]);
        } else {
            renderOpaqueFace(chunk, world, block, index, face, Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).getModelFace(face), new int[2]);
        }

        int numberOfLogs = ((BlockPitKiln)Block.list[block]).getNumberOfLogs((PitKilnState) chunk.getBlockState(index, MultiState.PIT_KILN_STATE));
        block = Block.largeFireWoodBlock.ID;
        ModelLoader baseModel = BlockModelList.largeFireWood;
        ModelFace modelFace;
        switch (numberOfLogs){
            case 4:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 1, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 3:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 1, 0.625f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 2:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 1, 0.375f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 1:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 1, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
        }

        ChestLocation chest = (ChestLocation) chunk.getBlockState(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), MultiState.CHEST_STATE);
        block = chest.inventory.itemStacks[0].metadata;
        if(block == Block.rawRedClayCookingPot.ID) {
            this.renderStandardBlock(chunk, world, block, index, face, new int[2]);
        } else if(block == Block.brickPile.ID){
            this.renderBrickPile(chunk, world, block, index, face);
        }
    }

    public void renderLogPile(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ChestLocation chestLocation = (ChestLocation) chunk.getBlockState(index, MultiState.CHEST_STATE);
        if(chestLocation == null)return;

        int numberOfLogs = chestLocation.inventory.itemStacks[0].count / 2;

        int[] recordedLogCount = null;
        int[] storedLogCount = null;
        for(int i = 0; i < this.fireWoodPileLogCounts.size(); i++){
            storedLogCount = this.fireWoodPileLogCounts.get(i);
            if(storedLogCount[0] != index)continue;

            if(storedLogCount[1] != numberOfLogs){
                numberOfLogs = storedLogCount[1];
                recordedLogCount = storedLogCount;
                break;
            }
        }

        if(recordedLogCount == null){
            this.fireWoodPileLogCounts.add(new int[]{index, numberOfLogs});
        }

        block = Block.largeFireWoodBlock.ID;
        ModelLoader baseModel = BlockModelList.largeFireWood;
        ModelFace modelFace;
        switch (numberOfLogs){
            case 16:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.875f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 15:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.875f, 0.625f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 14:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.875f, 0.375f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 13:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.875f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 12:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.625f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 11:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.625f, 0.625f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 10:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.625f, 0.375f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 9:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.625f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 8:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.375f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 7:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.375f, 0.625f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 6:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.375f, 0.375f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 5:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.375f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 4:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.125f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 3:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.125f, 0.625f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 2:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.125f, 0.375f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 1:
                baseModel = BlockModelList.largeFireWood.copyModel();
                baseModel.translateModel(0.5f, 0.125f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
        }
    }


    public void renderBrickPile(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ChestLocation chestLocation = (ChestLocation) chunk.getBlockState(index, MultiState.CHEST_STATE);
        int numberOfBricks = chestLocation.inventory.itemStacks[0].count;

        if(chestLocation.inventory.itemStacks[0].item.ID == Item.rawClayAdobeBrick.ID){
            block = Block.clay.ID;
        }

        if(chestLocation.inventory.itemStacks[0].item.ID == Item.firedRedClayAdobeBrick.ID){
            block = Block.redClayCookingPot.ID;
        }

        int[] recordedBrickCount = null;
        int[] storedBrickCount = null;
        for(int i = 0; i < this.brickPileBrickCounts.size(); i++){
            storedBrickCount = this.brickPileBrickCounts.get(i);
            if(storedBrickCount[0] != index)continue;

            if(storedBrickCount[1] != numberOfBricks){
                numberOfBricks = storedBrickCount[1];
                recordedBrickCount = storedBrickCount;
                break;
            }
        }

        if(recordedBrickCount == null){
            this.brickPileBrickCounts.add(new int[]{index, numberOfBricks});
        }

        ModelLoader baseModel = BlockModelList.brick;
        ModelFace modelFace;

        switch (numberOfBricks){ //alternate pushing the stacks inwards by 1 voxel to give depth to the stack
            case 48:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.9375f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 47:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.9375f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 46:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.9375f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 45:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.9375f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 44:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.9375f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 43:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.9375f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 42:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.8125f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 41:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.8125f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 40:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.8125f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 39:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.8125f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 38:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.8125f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 37:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.8125f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 36:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.6875f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 35:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.6875f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 34:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.6875f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 33:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.6875f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 32:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.6875f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 31:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.6875f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 30:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.5625f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 29:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.5625f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 28:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.5625f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 27:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.5625f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 26:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.5625f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 25:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.5625f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 24:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.4375f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 23:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.4375f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 22:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.4375f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 21:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.4375f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 20:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.4375f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 19:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.4375f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 18:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.3125f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 17:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.3125f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 16:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.3125f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 15:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.3125f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 14:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.3125f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 13:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.3125f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 12:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.1875f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 11:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.1875f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 10:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.1875f, 0.75f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 9:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.15625f, 0.1875f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 8:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.5f, 0.1875f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 7:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.rotateModel(90,0, 1, 0);
                baseModel.translateModel(0.84375f, 0.1875f, 0.25f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);

            case 6:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.0625f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 5:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.0625f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 4:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.78125f, 0.0625f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 3:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.0625f, 0.875f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 2:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.0625f, 0.5f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            case 1:
                baseModel = BlockModelList.brick.copyModel();
                baseModel.translateModel(0.21875f, 0.0625f, 0.125f);
                modelFace = baseModel.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
        }
    }

    public void renderItemStone(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        Random rand = new Random(index);
        float rotation = rand.nextFloat((float) 0, (float) (2 * Math.PI));
        float translateX = rand.nextFloat(0.25f, 0.75f);
        float translateZ = rand.nextFloat(0.25f, 0.75f);
        Vector3f offset = new Vector3f(translateX, 0f, translateZ);

        boolean secondRockHasHitTheTowers = rand.nextBoolean();
        float secondRockRotation = rand.nextFloat((float)0, (float) (2 * Math.PI));
        float secondTranslateX = rand.nextBoolean() ? translateX - 0.25f : translateX + 0.25f;
        float secondTranslateZ = rand.nextBoolean() ? translateZ - 0.25f : translateZ + 0.25f;
        Vector3f secondOffset = new Vector3f(secondTranslateX, 0f, secondTranslateZ);

        ModelFace[] modelFaces = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).getModelFaceOfType(face);

        for (int i = 0; i < modelFaces.length; i++) {
            if(modelFaces[i] == null)return;
            ModelFace modelFace = modelFaces[i].getScaledFace(1f);
            modelFace.normal.rotateY(rotation);
            for(int j = 0; j <  modelFaces[i].vertices.length; j++){
                modelFace.vertices[j].rotateY(rotation);
            }
            for(int j = 0; j <  modelFace.vertices.length; j++){
                modelFace.vertices[j].add(offset);
            }
            renderOpaqueFace(chunk, world, block, index, face,  modelFace, greedyMeshSize);

            if(secondRockHasHitTheTowers){
                modelFace = modelFaces[i].getScaledFace(0.5f);
                modelFace.normal.rotateY(secondRockRotation);
                for(int j = 0; j < modelFace.vertices.length; j++){
                    modelFace.vertices[j].rotateY(secondRockRotation);
                }
                for(int j = 0; j <  modelFace.vertices.length; j++){
                    modelFace.vertices[j].add(secondOffset);
                }
                renderOpaqueFace(chunk, world, block, index, face,  modelFace, greedyMeshSize);
            }
        }
    }
    public void renderItemStick(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        Random rand = new Random(index);
        float rotation = rand.nextFloat((float) 0, (float) (2 * Math.PI));
        float stickScale = rand.nextFloat(0.5f, 0.9f);
        float translateX = rand.nextFloat(0.25f, 0.75f);
        float translateZ = rand.nextFloat(0.25f, 0.75f);
        Vector3f offset = new Vector3f(translateX, 0f, translateZ);

        boolean secondStick = rand.nextBoolean();
        float secondStickRotation = rand.nextFloat((float)0, (float) (2 * Math.PI));
        float secondStickScale = rand.nextFloat(0.5f, 0.9f);
        float secondTranslateX = rand.nextBoolean() ? translateX - 0.25f : translateX + 0.25f;
        float secondTranslateZ = rand.nextBoolean() ? translateZ - 0.25f : translateZ + 0.25f;
        Vector3f secondOffset = new Vector3f(secondTranslateX, 0f, secondTranslateZ);

        ModelFace[] modelFaces = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).getModelFaceOfType(face);

        for (int i = 0; i < modelFaces.length; i++) {
            if(modelFaces[i] == null)return;
            ModelFace modelFace = modelFaces[i].getScaledFace(stickScale);
            modelFace.normal.rotateY(rotation);
            for(int j = 0; j <  modelFaces[i].vertices.length; j++){
                modelFace.vertices[j].rotateY(rotation);
            }
            for(int j = 0; j <  modelFace.vertices.length; j++){
                modelFace.vertices[j].add(offset);
            }
            renderOpaqueFace(chunk, world, block, index, face,  modelFace, greedyMeshSize);

            if(secondStick){
                modelFace = modelFaces[i].getScaledFace(secondStickScale);
                modelFace.normal.rotateY(secondStickRotation);
                for(int j = 0; j < modelFace.vertices.length; j++){
                    modelFace.vertices[j].rotateY(secondStickRotation);
                }
                for(int j = 0; j <  modelFace.vertices.length; j++){
                    modelFace.vertices[j].add(secondOffset);
                }
                renderOpaqueFace(chunk, world, block, index, face,  modelFace, greedyMeshSize);
            }
        }
    }


    public void renderBerryBush(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        int x = chunk.getBlockXFromIndex(index);
        int y = chunk.getBlockYFromIndex(index);
        int z = chunk.getBlockZFromIndex(index);

        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return;


        ModelLoader baseModel = Block.list[block].getBlockModel(x,y,z, world).copyModel();

        float scaleFactor = ((BlockBerryBush)Block.list[block]).getBerryBushScale(x,y,z,world);

        float translateVal = ((BlockBerryBush)Block.list[block]).getBerryBushTranslation(x,y,z,world);
        baseModel.scaleModel(scaleFactor);
        baseModel.translateModel(translateVal, 0, translateVal);

        ModelFace modelFace = baseModel.getModelFace(face);
        this.renderOpaqueFaceDirect(chunk, world, Block.berryBush.ID, index, face, modelFace, new int[2], face == TOP_FACE ? BlockTextureList.BERRY_BUSH_TOP_BASE_TEXTURE : BlockTextureList.BERRY_BUSH_SIDE_BASE_TEXTURE);

        Vector3f translation = new Vector3f();
        switch (face){
            case TOP_FACE -> {
                translation.y = 0.02f;
            }
            case BOTTOM_FACE -> {
                translation.y = -0.02f;
            }
            case NORTH_FACE -> {
                translation.x = -0.02f;
            }
            case SOUTH_FACE -> {
                translation.x = 0.02f; //You'd think this would also be 0.01 but fun fact, no!. I assume this is due to the float to half-float conversion later on
            }
            case EAST_FACE -> {
                translation.z = -0.02f;
            }
            case WEST_FACE -> {
                translation.z = 0.02f;
            }
        }


        //Flower/Fruit overlay
        if(berryBushState.hasMatureFruit || berryBushState.isFlowering){
            modelFace = modelFace.translateFace(translation.x, translation.y, translation.z);
            this.renderOpaqueFace(chunk, world, Block.berryBush.ID, index, face, modelFace, new int[2]);
        }
    }


    public void renderItemBlock(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        if(face != TOP_FACE)return;

        block = Block.itemBlock.ID;

        ChestLocation chestLocation = (ChestLocation) chunk.getBlockState(index, MultiState.CHEST_STATE);
        if(chestLocation == null)return;

        ModelLoader model = chestLocation.inventory.itemStacks[0].item.itemModel.copyModel();

        if(chestLocation.inventory.itemStacks[0].item instanceof ItemTool){
            model.rotateModel(90, 0, 0, 1);
        }

        model.translateModel(0.5f, model.getModelHeight() / 2f, 0.5f);

        for(int i = 0; i < model.modelFaces.length; i++){
            this.renderOpaqueFace(chunk, world, block, index, face, model.modelFaces[i], new int[2]);
        }
    }



    public void renderReeds(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelLoader baseModel = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();

        float scaleFactor = ((BlockReed)Block.list[block]).getReedGrowthScale();

        float translateVal = ((BlockReed)Block.list[block]).getReedTranslation();

        baseModel.scaleModel(scaleFactor);
        baseModel.translateModel(translateVal, 0, translateVal);

        ModelFace modelFace = baseModel.getModelFace(face);

        this.handleWaterLoggedBlocks(chunk, world, block, index, face);

        this.renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
    }


    public void render3DCraftingItem(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;


        ModelLoader blockModel;
        ModelFace modelFace;

        InWorld3DCraftingItem craftingBlock = (InWorld3DCraftingItem) chunk.getBlockState(index, MultiState.CRAFTING_3D_ITEM_STATE);
        block = craftingBlock.materialBlockID != RenderEngine.NULL_TEXTURE ? craftingBlock.materialBlockID : Block.crafting3DItem.ID;

        Vector3f translationVector = new Vector3f();
        for(int i = 0; i < 16; i++){

            translationVector.y = i / 16f;

            for(int j = 0; j < 144; j++){
                if(craftingBlock.subVoxelIndices[i][j] == 0)continue;



                translationVector.x = ((j % 12) * 0.0625f) + 0.125f;
                translationVector.z = ((j / 12) * 0.0625f) + 0.125f; //0.046875 is the portion of the used block space divided by 16 for each voxel
                blockModel = BlockModelList.crafting3DItemVoxelModel.copyModel();
                blockModel.translateModel(translationVector.x, translationVector.y, translationVector.z);
                modelFace = blockModel.getModelFace(face);
                modelFace = modelFace.copyFace();


                //alter the copy's UVs and pass to the renderer method
                int pixelShiftX = craftingTextureOffsetX[j];
                int pixelShiftY = craftingTextureOffsetY[j];
                float xUVLow = pixelShiftX / 32f;
                float yUVLow = pixelShiftY / 32f;
                float xUVHigh = xUVLow + 2/32f;
                float yUVHigh = yUVLow + 2/32f;
                for(int k = 0; k < modelFace.UVs.length; k++){
                    modelFace.UVs[k][0] = modelFace.UVs[k][0] == 0.0f ? xUVLow : xUVHigh;
                    modelFace.UVs[k][1] = modelFace.UVs[k][1] == 0.0f ? yUVLow : yUVHigh;
                }


                this.renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
            }
        }

    }

    public void renderCraftingItem(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        if(face != TOP_FACE)return;

        //Swap texture in the return method, pass the texture index from the item array into the method as the "face"

        InWorldCraftingItem craftingItem = (InWorldCraftingItem) chunk.getBlockState(index, MultiState.CRAFTING_ITEM_STATE);

        if(craftingItem == null)return;

        for(int i = 0; i < craftingItem.itemsFilled.length; i++){

            if(!craftingItem.itemsFilled[i])continue;

            ModelLoader model;
            if(craftingItem.outputRecipe.requiredItems[i] == Item.block.ID){
                model = Block.list[craftingItem.outputRecipe.requiredItemMetadata[i]].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();
                block = craftingItem.outputRecipe.requiredItemMetadata[i];
            } else {
                model = Item.list[craftingItem.outputRecipe.requiredItems[i]].itemModel.copyModel();
                block = Block.itemBlock.ID;
            }

            ModelFace modelFace;

            model.scaleModel(0.5f);
            if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandAxe.ID ||
            craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandShovel.ID ||
            craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandKnifeBlade.ID){
                model.scaleModel(0.75f);
                model.rotateModel(90, 0, 0, 1);
                if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandShovel.ID){
                    model.rotateModel(-90, 0, 1, 0);
                }
                if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandKnifeBlade.ID){
                    model.rotateModel(135f, 0, 1, 0);
                    model.scaleModel(2.1f);
                }
            }

            if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneSpearHead.ID){
                model.rotateModel(90, 0, 0, 1);
            }

            if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHoeHead.ID){
                model.rotateModel(90f, 0, 0, 1);
            }
            model.rotateModel((float) craftingItem.outputRecipe.requiredItemAngles[i], 0, 1, 0);
            model.translateModel((float) craftingItem.outputRecipe.requiredItemPositions[i][0], (float) craftingItem.outputRecipe.requiredItemPositions[i][1], (float) craftingItem.outputRecipe.requiredItemPositions[i][2]);


            for(int j = 0; j < model.modelFaces.length; j++){
                modelFace = model.modelFaces[j];
                this.renderOpaqueFace(chunk, world, block, index, modelFace.faceType, modelFace, new int[2]);
            }


        }
    }

    private ModelLoader[] rotateDoorModelsNorth(ModelLoader upperModel, ModelLoader lowerModel, DoorTransition doorTransition, DoorState doorState, World world) {
        if(doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeLeft){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    float degrees = -90 * ratio;

                    upperModel.copyModel().translateModel(0, 0, -0.4375f);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0, 0, 0.4375f);

                    upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

                    lowerModel.copyModel().translateModel(0, 0, -0.4375f);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0, 0, 0.4375f);

                    lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
                }
            } else {
                upperModel.copyModel().rotateModel(-90, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);

                lowerModel.copyModel().rotateModel(-90, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
            }
        } else if(doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    float degrees = 90 * ratio;

                    upperModel.copyModel().translateModel(0, 0, 0.4375f);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0, 0, -0.4375f);

                    upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

                    lowerModel.copyModel().translateModel(0, 0, 0.4375f);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0, 0, -0.4375f);

                    lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
                }
            } else {
                upperModel.copyModel().rotateModel(90, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);

                lowerModel.copyModel().rotateModel(90, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
            }
        } else if(!doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                float degrees = -90 *  (1 -ratio);

                upperModel.copyModel().translateModel(0, 0, -0.4375f);
                upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                upperModel.copyModel().translateModel(0, 0, 0.4375f);

                upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

                lowerModel.copyModel().translateModel(0, 0, -0.4375f);
                lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                lowerModel.copyModel().translateModel(0, 0, 0.4375f);

                lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
            } else {
                upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);
                lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
            }
        } else if(!doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                float degrees = 90 * (1 - ratio);

                upperModel.copyModel().translateModel(0, 0, 0.4375f);
                upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                upperModel.copyModel().translateModel(0, 0, -0.4375f);

                upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

                lowerModel.copyModel().translateModel(0, 0, 0.4375f);
                lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                lowerModel.copyModel().translateModel(0, 0, -0.4375f);

                lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
            } else {
                upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);
                lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
            }
        } else { //Closed state
            upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

            lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
        }

        return new ModelLoader[]{upperModel, lowerModel};
    }


    private ModelLoader[] rotateDoorModelsSouth(ModelLoader upperModel, ModelLoader lowerModel, DoorTransition doorTransition, DoorState doorState, World world){
        if(doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeLeft){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = -90 * ratio;

                    upperModel.copyModel().rotateModel(180, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(180, 0, 1, 0);

                    upperModel.copyModel().translateModel(0, 0, 0.4375f);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0, 0, -0.4375f);

                    lowerModel.copyModel().translateModel(0, 0, 0.4375f);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0, 0, -0.4375f);

                    upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                    lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                }
            } else {
                upperModel.copyModel().rotateModel(90, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);

                lowerModel.copyModel().rotateModel(90, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
            }
        } else if(doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = 90 * ratio;

                    upperModel.copyModel().rotateModel(180, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(180, 0, 1, 0);

                    upperModel.copyModel().translateModel(0, 0, -0.4375f);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0, 0, 0.4375f);

                    lowerModel.copyModel().translateModel(0, 0, -0.4375f);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0, 0, 0.4375f);

                    upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                    lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                }
            } else {
                upperModel.copyModel().rotateModel(270, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);

                lowerModel.copyModel().rotateModel(270, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
            }
        } else if(!doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedOpen && doorTransition.hingeLeft){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = -90 * (1 - ratio);

                    upperModel.copyModel().rotateModel(180, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(180, 0, 1, 0);

                    upperModel.copyModel().translateModel(0, 0, 0.4375f);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0, 0, -0.4375f);

                    lowerModel.copyModel().translateModel(0, 0, 0.4375f);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0, 0, -0.4375f);

                    upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                    lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                }
            } else {
                upperModel.copyModel().rotateModel(180, 0, 1, 0);
                upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);

                lowerModel.copyModel().rotateModel(180, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
            }
        } else if(!doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedOpen && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = 90 * (1 - ratio);

                    upperModel.copyModel().rotateModel(180, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(180, 0, 1, 0);

                    upperModel.copyModel().translateModel(0, 0, -0.4375f);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0, 0, 0.4375f);

                    lowerModel.copyModel().translateModel(0, 0, -0.4375f);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0, 0, 0.4375f);

                    upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                    lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                }
            } else {
                upperModel.copyModel().rotateModel(180, 0, 1, 0);
                upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);

                lowerModel.copyModel().rotateModel(180, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
            }
        } else {
            upperModel.copyModel().rotateModel(180, 0, 1, 0);
            upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);

            lowerModel.copyModel().rotateModel(180, 0, 1, 0);
            lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
        }
        return new ModelLoader[]{upperModel, lowerModel};
    }

    private ModelLoader[] rotateDoorModelsEast(ModelLoader upperModel, ModelLoader lowerModel, DoorTransition doorTransition, DoorState doorState, World world){
        if(doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeLeft){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = -90 * ratio;

                    upperModel.copyModel().rotateModel(270, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(270, 0, 1, 0);

                    upperModel.copyModel().translateModel(0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                }
            } else {
                upperModel.copyModel().rotateModel(180, 0, 1, 0);
                upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

                lowerModel.copyModel().rotateModel(180, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
            }
        } else if(doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = 90 * ratio;

                    upperModel.copyModel().rotateModel(270, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(270, 0, 1, 0);

                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                }
            } else {
                upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
            }
        } else if(!doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedOpen && doorTransition.hingeLeft){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = -90 * (1 - ratio);

                    upperModel.copyModel().rotateModel(270, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(270, 0, 1, 0);

                    upperModel.copyModel().translateModel(0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                }
            } else {
                upperModel.copyModel().rotateModel(270, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);

                lowerModel.copyModel().rotateModel(270, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
            }
        } else if(!doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedOpen && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = 90 * (1 - ratio);

                    upperModel.copyModel().rotateModel(270, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(270, 0, 1, 0);

                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
                }
            } else {
                upperModel.copyModel().rotateModel(270, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);

                lowerModel.copyModel().rotateModel(270, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
            }
        } else {
            upperModel.copyModel().rotateModel(270, 0, 1, 0);
            upperModel.copyModel().translateModel(0.5f, 0, 0.0625f);

            lowerModel.copyModel().rotateModel(270, 0, 1, 0);
            lowerModel.copyModel().translateModel(0.5f, 0, 0.0625f);
        }


        return new ModelLoader[]{upperModel, lowerModel};
    }

    private ModelLoader[] rotateDoorModelsWest(ModelLoader upperModel, ModelLoader lowerModel, DoorTransition doorTransition, DoorState doorState, World world){
        if(doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeLeft){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = -90 * ratio;

                    upperModel.copyModel().rotateModel(90, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(90, 0, 1, 0);

                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                }
            } else {
                upperModel.copyModel().translateModel(0.9375f, 0, 0.5f);
                lowerModel.copyModel().translateModel(0.9375f, 0, 0.5f);
            }
        } else if(doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedClosed && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = 90 * ratio;
                    upperModel.copyModel().rotateModel(90, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(90, 0, 1, 0);

                    upperModel.copyModel().translateModel(0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                }
            } else {
                upperModel.copyModel().rotateModel(180, 0, 1, 0);
                upperModel.copyModel().translateModel(0.0625f, 0, 0.5f);

                lowerModel.copyModel().rotateModel(180, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.0625f, 0, 0.5f);
            }
        } else if(!doorState.isOpen && doorState.hingeLeft){
            if(doorTransition != null){
                if(doorTransition.startedOpen && doorTransition.hingeLeft) {
                    float ratio = (float) (world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = -90 * (1 - ratio);

                    upperModel.copyModel().rotateModel(90, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(90, 0, 1, 0);

                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                }
            } else {
                upperModel.copyModel().rotateModel(90, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);

                lowerModel.copyModel().rotateModel(90, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
            }
        } else if(!doorState.isOpen && doorState.hingeRight){
            if(doorTransition != null){
                if(doorTransition.startedOpen && doorTransition.hingeRight){
                    float ratio = (float)(world.ce.save.time - doorTransition.timeStarted) / DoorTransition.timeToComplete;
                    ratio = Math.min(ratio, 1);
                    float degrees = 90 * (1- ratio);
                    upperModel.copyModel().rotateModel(90, 0, 1, 0);
                    lowerModel.copyModel().rotateModel(90, 0, 1, 0);

                    upperModel.copyModel().translateModel(0.4375f, 0, 0);
                    upperModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    upperModel.copyModel().translateModel(-0.4375f, 0, 0);

                    lowerModel.copyModel().translateModel(0.4375f, 0, 0);
                    lowerModel.copyModel().rotateModel(degrees, 0, 1, 0);
                    lowerModel.copyModel().translateModel(-0.4375f, 0, 0);

                    upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                    lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
                }
            } else {
                upperModel.copyModel().rotateModel(90, 0, 1, 0);
                upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);

                lowerModel.copyModel().rotateModel(90, 0, 1, 0);
                lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
            }
        } else {
            upperModel.copyModel().rotateModel(90, 0, 1, 0);
            upperModel.copyModel().translateModel(0.5f, 0, 0.9375f);

            lowerModel.copyModel().rotateModel(90, 0, 1, 0);
            lowerModel.copyModel().translateModel(0.5f, 0, 0.9375f);
        }


        return new ModelLoader[]{upperModel,lowerModel};
    }


    public void renderDoorPrimitive(Chunk chunk, World world, short block, int index, int face){
        //the upper block contains the texture lookup along with the model the lower block contains the direction reference

        if(face != TOP_FACE)return;

        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        DoorState doorState = (DoorState) world.getBlockState(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) - 1, chunk.getBlockZFromIndex(index), MultiState.DOOR_STATE);
        DoorTransition doorTransition = (DoorTransition) world.getBlockState(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) - 1, chunk.getBlockZFromIndex(index), MultiState.DOOR_TRANSITION_STATE);

        ModelLoader upperModel = BlockModelList.primitiveDoorUpper.copyModel();
        ModelLoader lowerModel = BlockModelList.primitiveDoorLower.copyModel();

        ModelLoader[] rotatedModels;
        switch (doorState.facingDirection){
            case DoorState.FACE_DIRECTION_NORTH -> {
                rotatedModels = this.rotateDoorModelsNorth(upperModel, lowerModel, doorTransition, doorState, world);
                upperModel = rotatedModels[0];
                lowerModel = rotatedModels[1];
            }
            case DoorState.FACE_DIRECTION_SOUTH -> {
                rotatedModels = this.rotateDoorModelsSouth(upperModel, lowerModel, doorTransition, doorState, world);
                upperModel = rotatedModels[0];
                lowerModel = rotatedModels[1];
            }
            case DoorState.FACE_DIRECTION_EAST -> {
                rotatedModels = this.rotateDoorModelsEast(upperModel, lowerModel, doorTransition, doorState, world);
                upperModel = rotatedModels[0];
                lowerModel = rotatedModels[1];
            }
            case DoorState.FACE_DIRECTION_WEST -> {
                rotatedModels = this.rotateDoorModelsWest(upperModel, lowerModel, doorTransition, doorState, world);
                upperModel = rotatedModels[0];
                lowerModel = rotatedModels[1];
            }
            default -> {
                throw new IllegalStateException("Unknown Face Direction on door");
            }
        }


        ModelFace modelFace;
        for(int i = 0; i < upperModel.modelFaces.length; i++){
            modelFace = upperModel.modelFaces[i];
            this.renderOpaqueFace(chunk, world, block, index, modelFace.faceType, modelFace, new int[2]);
        }

        block = Block.doorPrimitiveLower.ID;
        index = Chunk.getBlockIndexFromCoordinates(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index) - 1, chunk.getBlockZFromIndex(index));
        for(int i = 0; i < lowerModel.modelFaces.length; i++){
            modelFace = lowerModel.modelFaces[i];
            this.renderOpaqueFace(chunk, world, block, index, modelFace.faceType, modelFace, new int[2]);
        }
    }

    public void renderLeaf(Chunk chunk, World world, short block, int index, int face){
        if(face != TOP_FACE)return;

        ModelLoader model;

        int x = chunk.getBlockXFromIndex(index);
        int y = chunk.getBlockYFromIndex(index);
        int z = chunk.getBlockZFromIndex(index);

        if(world.getBlockID(x - 1, y, z) == Block.leaf.ID && world.getBlockID(x + 1, y, z) == Block.leaf.ID &&
        world.getBlockID(x, y - 1, z) == Block.leaf.ID && world.getBlockID(x, y + 1, z) == Block.leaf.ID &&
        world.getBlockID(x, y, z - 1) == Block.leaf.ID && world.getBlockID(x, y, z + 1) == Block.leaf.ID){
            model = BlockModelList.standardBlockModel.copyModel();
        } else {

            Random rand = new Random(new LongHasher().hash(index, String.valueOf(chunk.x + chunk.y + chunk.z)));

            float xOffset = rand.nextFloat(-0.25f, 0.25f);
            float zOffset = rand.nextFloat(-0.25f, 0.25f);
            float yaw = rand.nextFloat(360f);

            model = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();

            model.translateModel(-0.5f, 0, -0.5f);
            model.rotateModel(yaw, 0, 1, 0);
            model.translateModel(0.5f, 0, 0.5f);
            model.translateModel(xOffset, 0, zOffset);
        }

        ModelFace modelFace;

        for(int i = 0; i < model.modelFaces.length; i++){
            modelFace = model.modelFaces[i];
            this.renderOpaqueFace(chunk, world, block, index, face, modelFace, new int[2]);
        }
    }

    public void renderSapling(Chunk chunk, World world, short block, int index, int face){
        if(face != TOP_FACE)return;


        Random rand = new Random((new LongHasher().hash(index, String.valueOf(chunk.x + chunk.y + chunk.z))));

        float yaw = rand.nextFloat(360f);

        ModelLoader model = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world).copyModel();

        model.translateModel(-0.5f, 0, -0.5f);
        model.rotateModel(yaw, 0, 1, 0);
        model.translateModel(0.5f, 0, 0.5f);

        for(int i = 0; i < model.modelFaces.length; i++){
            this.renderOpaqueFace(chunk, world, block, index, face, model.modelFaces[i], new int[2]);  //Trunk
        }
    }

    public void renderCrop(Chunk chunk, World world, short block, int index, int face) {
        if (face != TOP_FACE) return;

        CropState cropState = (CropState) chunk.getBlockState(index, MultiState.CROP_STATE);
        if (cropState == null) {
            System.out.println("Unable to get crop information in rendering thread");
            return;
        }

        ModelLoader model = null;

        switch (cropState.growthStage){
            case 0 -> model = BlockModelList.topFaceBlockModel.copyModel();
            case 1 -> model = BlockModelList.cropGrowth1Model.copyModel();
            case 2 -> model = BlockModelList.cropGrowth2Model.copyModel();
            case 3 -> model = BlockModelList.cropGrowth3Model.copyModel();
            case 4 -> model = BlockModelList.cropGrowth4Model.copyModel();
            case 5 -> model = BlockModelList.cropGrowth5Model.copyModel();
            case 6 -> model = BlockModelList.cropGrowth6Model.copyModel();
            case 7 -> model = BlockModelList.cropGrowth7Model.copyModel();
            case 8 -> model = BlockModelList.cropGrowth8Model.copyModel();
        }

        if(model == null)return;

        model.translateModel(0, -0.09375f, 0); //all models need to be translated down to match the height of the tilled soil
        model.translateModel(-0.5f, 0, -0.5f);
        model.rotateModel(new Random((new LongHasher().hash(index, String.valueOf(chunk.x + chunk.y + chunk.z)))).nextFloat(-10, 10), 0, 1, 0);
        model.translateModel(0.5f, 0, 0.5f);


        for (int i = 0; i < model.modelFaces.length; i++) {
            this.renderOpaqueFace(chunk, world, block, index, face, model.modelFaces[i], new int[2]);
        }
    }





    //Formula is (y = x * x / 250 ) + 0.1F
    private  float getLightValueFromMap(byte lightValue) {
        return switch (lightValue) {
            case 0, 1 -> 0.1F;
            case 2 -> 0.11F;
            case 3 -> 0.13F;
            case 4 -> 0.16F;
            case 5 -> 0.2F;
            case 6 -> 0.24F;
            case 7 -> 0.29F;
            case 8 -> 0.35F;
            case 9 -> 0.42F;
            case 10 -> 0.5F;
            case 11 -> 0.58F;
            case 12 -> 0.67F;
            case 13 -> 0.77F;
            case 14 -> 0.88F;
            case 15 -> 1.0F;
            default -> 0.1F;
        };
    }

    private void setPlantColorValues(int x, int y, int z, World world, short blockID, int textureID){
        if(blockID == Block.itemBlock.ID){
            float highestChannel = 0;
            highestChannel = Math.max(this.red, this.green);
            highestChannel = Math.max(highestChannel, this.blue);

            this.highestChannel = highestChannel != 0.0 ? highestChannel : 0.01f;
        }
        if(blockID == Block.sapling.ID && textureID != 24)return; //24 is the ID for transparent leaves
        if(!Block.list[blockID].isColorized(x,y,z,world))return;


        int color =
                Block.list[blockID] instanceof BlockGrass  ||
                blockID == Block.tallGrass.ID ||
                        Block.list[blockID] instanceof BlockClayGrass ?
                PlantColorizer.getGrassColor(world.getTemperature(x,y,z), world.getRainfall(x,z)) :
                PlantColorizer.getOakLeafColor(world.getTemperature(x,y,z), world.getRainfall(x,z));
        this.red = ((color >> 16) & 255) / 255f;
        this.green = ((color >> 8) & 255) / 255f;
        this.blue = (color & 255) / 255f;

        float highestChannel = 0;
        highestChannel = Math.max(this.red, this.green);
        highestChannel = Math.max(highestChannel, this.blue);

        this.highestChannel = highestChannel != 0.0 ? highestChannel : 0.01f;
    }

    private void setGrayScaleImageMultiplier(){
        //Determine the highest value within the 3 color channels to prevent the value from exceeding 1
        float highestChannel = 0;
        highestChannel = Math.max(this.red, this.green);
        highestChannel = Math.max(highestChannel, this.blue);


       highestChannel = highestChannel != 0.0 ? highestChannel : 0.01f;

       this.grayScaleImageMultiplier = MathUtil.floatToHalf(this.highestChannel / highestChannel); //This division gives the number to multiply the vertex color by in the terrain shader to restore to the normal fully lit grass color
    }

    private void setLight(float x, float y, float z, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, int index, int face, Chunk chunk, World world, int[] greedyMeshSize, short blockID, int textureID) {
        int xBlock = chunk.getBlockXFromIndex(index);
        int yBlock = chunk.getBlockYFromIndex(index);
        int zBlock = chunk.getBlockZFromIndex(index);

        this.setPlantColorValues(xBlock, yBlock, zBlock, world, blockID, textureID);

        if(blockID == Block.tilledSoil.ID && face == TOP_FACE){
            y = yMax; //This shifts the vertex up to the top of the block so it doesnt detect zero sky lighting
        }

        if(blockID == Block.itemBlock.ID){
            this.setVertexLight1Arg(world.getBlockLightValue(xBlock, yBlock , zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue1Args(world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
            this.setGrayScaleImageMultiplier();
            return;
        }

        if (x == xMin && y == yMin && z == zMin) {
            final Block[] blocks = Block.list;

            switch (face) {
                case BOTTOM_FACE -> {
                    int idA = world.getBlockID(xBlock - 1, yBlock - 1, zBlock);
                    int idB = world.getBlockID(xBlock, yBlock - 1, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockLightValue(xBlock, yBlock - 1, zBlock),
                                world.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockLightColor(xBlock, yBlock - 1, zBlock)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockLightValue(xBlock, yBlock - 1, zBlock),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockLightColor(xBlock, yBlock - 1, zBlock)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int idA = world.getBlockID(xBlock - 1, yBlock - 1, zBlock);
                    int idB = world.getBlockID(xBlock - 1, yBlock, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightValue(xBlock - 1, yBlock, zBlock),
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock, zBlock),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightValue(xBlock - 1, yBlock, zBlock),
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                world.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock, zBlock),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock)
                        );
                    }
                }

                case EAST_FACE -> {
                    int idA = world.getBlockID(xBlock - 1, yBlock, zBlock - 1);
                    int idB = world.getBlockID(xBlock, yBlock - 1, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightValue(xBlock, yBlock, zBlock - 1),
                                world.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightColor(xBlock, yBlock, zBlock - 1),
                                world.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock, yBlock, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock - 1)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightValue(xBlock, yBlock, zBlock - 1),
                                world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockLightColor(xBlock, yBlock, zBlock - 1),
                                world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock, yBlock, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock - 1)
                        );
                    }
                }
            }
        } else if (x == xMax && y == yMin && z == zMin) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];

            switch (face) {
                case BOTTOM_FACE -> {
                    int idA = world.getBlockID(xBlock + 1 + meshX, yBlock - 1, zBlock);
                    int idB = world.getBlockID(xBlock + meshX, yBlock, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        int lx = xBlock + meshX, ly = yBlock - 1, lz = zBlock;
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, ly, lz - 1),
                                world.getBlockLightValue(lx + 1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightColor(lx, ly, lz - 1),
                                world.getBlockLightColor(lx + 1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, ly, lz - 1),
                                world.getBlockSkyLightValue(lx + 1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz)
                        );
                    } else {
                        int lx = xBlock + meshX, ly = yBlock - 1, lz = zBlock;
                        setVertexLight4Args(
                                world.getBlockLightValue(lx + 1, ly, lz - 1),
                                world.getBlockLightValue(lx, ly, lz - 1),
                                world.getBlockLightValue(lx + 1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightColor(lx + 1, ly, lz - 1),
                                world.getBlockLightColor(lx, ly, lz - 1),
                                world.getBlockLightColor(lx + 1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx + 1, ly, lz - 1),
                                world.getBlockSkyLightValue(lx, ly, lz - 1),
                                world.getBlockSkyLightValue(lx + 1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    int idA = world.getBlockID(xBlock + 1, yBlock - 1, zBlock);
                    int idB = world.getBlockID(xBlock + 1, yBlock, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    int lx = xBlock + 1, lz = zBlock;
                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, yBlock, lz - 1),
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock, lz - 1),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, yBlock, lz - 1),
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, yBlock, lz - 1),
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz - 1),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock, lz - 1),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz - 1),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, yBlock, lz - 1),
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz - 1),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    }
                }

                case EAST_FACE -> {
                    int lx = xBlock + meshX;
                    int idA = world.getBlockID(lx + 1, yBlock, zBlock - 1);
                    int idB = world.getBlockID(lx, yBlock - 1, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    int lz = zBlock - 1;
                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx + 1, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx + 1, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx + 1, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx + 1, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(lx + 1, yBlock - 1, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx + 1, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(lx + 1, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx + 1, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(lx + 1, yBlock - 1, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    }
                }
            }
        } else if (x == xMin && y == yMax && z == zMin) {
            final Block[] blocks = Block.list;
            final int meshY = greedyMeshSize[1];

            switch (face) {
                case TOP_FACE -> {
                    int lx1 = xBlock - 1, ly1 = yBlock + 1, lz = zBlock;
                    int lx2 = xBlock, ly2 = yBlock + 1, lz1 = zBlock - 1;

                    boolean solidA = blocks[world.getBlockID(lx1, ly1, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx2, ly2, lz1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx2, ly2, lz1),
                                world.getBlockLightValue(lx1, ly1, lz),
                                world.getBlockLightValue(lx2, ly2, lz),
                                world.getBlockLightColor(lx2, ly2, lz1),
                                world.getBlockLightColor(lx1, ly1, lz),
                                world.getBlockLightColor(lx2, ly2, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx2, ly2, lz1),
                                world.getBlockSkyLightValue(lx1, ly1, lz),
                                world.getBlockSkyLightValue(lx2, ly2, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx1, ly1, lz1),
                                world.getBlockLightValue(lx2, ly2, lz1),
                                world.getBlockLightValue(lx1, ly1, lz),
                                world.getBlockLightValue(lx2, ly2, lz),
                                world.getBlockLightColor(lx1, ly1, lz1),
                                world.getBlockLightColor(lx2, ly2, lz1),
                                world.getBlockLightColor(lx1, ly1, lz),
                                world.getBlockLightColor(lx2, ly2, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx1, ly1, lz1),
                                world.getBlockSkyLightValue(lx2, ly2, lz1),
                                world.getBlockSkyLightValue(lx1, ly1, lz),
                                world.getBlockSkyLightValue(lx2, ly2, lz)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int lx = xBlock - 1, baseY = yBlock + meshY, ly = baseY, ly1 = baseY + 1, lz = zBlock;
                    boolean solidA = blocks[world.getBlockID(lx, ly1, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, ly, lz - 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, ly, lz - 1),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightValue(lx, ly1, lz),
                                world.getBlockLightColor(lx, ly, lz - 1),
                                world.getBlockLightColor(lx, ly, lz),
                                world.getBlockLightColor(lx, ly1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, ly, lz - 1),
                                world.getBlockSkyLightValue(lx, ly, lz),
                                world.getBlockSkyLightValue(lx, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, ly, lz - 1),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightValue(lx, ly1, lz - 1),
                                world.getBlockLightValue(lx, ly1, lz),
                                world.getBlockLightColor(lx, ly, lz - 1),
                                world.getBlockLightColor(lx, ly, lz),
                                world.getBlockLightColor(lx, ly1, lz - 1),
                                world.getBlockLightColor(lx, ly1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, ly, lz - 1),
                                world.getBlockSkyLightValue(lx, ly, lz),
                                world.getBlockSkyLightValue(lx, ly1, lz - 1),
                                world.getBlockSkyLightValue(lx, ly1, lz)
                        );
                    }
                }

                case EAST_FACE -> {
                    int lx = xBlock, lz = zBlock - 1, baseY = yBlock + meshY, ly = baseY, ly1 = baseY + 1;
                    int lx1 = xBlock - 1;

                    boolean solidA = blocks[world.getBlockID(lx1, ly, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, ly1, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightValue(lx, ly1, lz),
                                world.getBlockLightColor(lx1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz),
                                world.getBlockLightColor(lx, ly1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz),
                                world.getBlockSkyLightValue(lx, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightValue(lx1, ly1, lz),
                                world.getBlockLightValue(lx, ly1, lz),
                                world.getBlockLightColor(lx1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz),
                                world.getBlockLightColor(lx1, ly1, lz),
                                world.getBlockLightColor(lx, ly1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz),
                                world.getBlockSkyLightValue(lx1, ly1, lz),
                                world.getBlockSkyLightValue(lx, ly1, lz)
                        );
                    }
                }
            }
        } else if (x == xMin && y == yMin && z == zMax) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];
            final int meshZ = greedyMeshSize[1];

            switch (face) {
                case BOTTOM_FACE -> {
                    int lx = xBlock, ly = yBlock - 1, lz = zBlock + meshZ;
                    boolean solidA = blocks[world.getBlockID(xBlock - 1, ly, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, ly, lz + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, ly, lz + 1),
                                world.getBlockLightValue(xBlock - 1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightColor(lx, ly, lz + 1),
                                world.getBlockLightColor(xBlock - 1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, ly, lz + 1),
                                world.getBlockSkyLightValue(xBlock - 1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xBlock - 1, ly, lz + 1),
                                world.getBlockLightValue(lx, ly, lz + 1),
                                world.getBlockLightValue(xBlock - 1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightColor(xBlock - 1, ly, lz + 1),
                                world.getBlockLightColor(lx, ly, lz + 1),
                                world.getBlockLightColor(xBlock - 1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xBlock - 1, ly, lz + 1),
                                world.getBlockSkyLightValue(lx, ly, lz + 1),
                                world.getBlockSkyLightValue(xBlock - 1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int lx = xBlock - 1, lz = zBlock + meshX;
                    int ly = yBlock;
                    boolean solidA = blocks[world.getBlockID(lx, yBlock - 1, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, ly, lz + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, ly, lz + 1),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx, ly, lz + 1),
                                world.getBlockLightColor(lx, ly, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, ly, lz + 1),
                                world.getBlockSkyLightValue(lx, ly, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, ly, lz + 1),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz + 1),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx, ly, lz + 1),
                                world.getBlockLightColor(lx, ly, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz + 1),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, ly, lz + 1),
                                world.getBlockSkyLightValue(lx, ly, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz + 1),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    }
                }

                case WEST_FACE -> {
                    int lx = xBlock - 1, lz = zBlock + 1;
                    boolean solidA = blocks[world.getBlockID(lx, yBlock, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(xBlock, yBlock - 1, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(xBlock, yBlock, lz),
                                world.getBlockLightValue(xBlock, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(xBlock, yBlock, lz),
                                world.getBlockLightColor(xBlock, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(xBlock, yBlock, lz),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(xBlock, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightValue(xBlock, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(xBlock, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz),
                                world.getBlockLightColor(xBlock, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(xBlock, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz),
                                world.getBlockSkyLightValue(xBlock, yBlock - 1, lz)
                        );
                    }
                }
            }
        } else if (x == xMax && y == yMax && z == zMin) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];
            final int meshY = greedyMeshSize[1];

            switch (face) {
                case TOP_FACE -> {
                    int lx = xBlock + meshX, ly = yBlock + 1, lz = zBlock;

                    boolean solidA = blocks[world.getBlockID(lx + 1, ly, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, ly, lz - 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, ly, lz - 1),
                                world.getBlockLightValue(lx + 1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightColor(lx, ly, lz - 1),
                                world.getBlockLightColor(lx + 1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, ly, lz - 1),
                                world.getBlockSkyLightValue(lx + 1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx + 1, ly, lz - 1),
                                world.getBlockLightValue(lx, ly, lz - 1),
                                world.getBlockLightValue(lx + 1, ly, lz),
                                world.getBlockLightValue(lx, ly, lz),
                                world.getBlockLightColor(lx + 1, ly, lz - 1),
                                world.getBlockLightColor(lx, ly, lz - 1),
                                world.getBlockLightColor(lx + 1, ly, lz),
                                world.getBlockLightColor(lx, ly, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx + 1, ly, lz - 1),
                                world.getBlockSkyLightValue(lx, ly, lz - 1),
                                world.getBlockSkyLightValue(lx + 1, ly, lz),
                                world.getBlockSkyLightValue(lx, ly, lz)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    int lx = xBlock + 1, lz = zBlock;
                    int baseY = yBlock + meshY, ly1 = baseY + 1;

                    boolean solidA = blocks[world.getBlockID(lx, ly1, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, baseY, lz - 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, baseY, lz - 1),
                                world.getBlockLightValue(lx, baseY, lz),
                                world.getBlockLightValue(lx, ly1, lz),
                                world.getBlockLightColor(lx, baseY, lz - 1),
                                world.getBlockLightColor(lx, baseY, lz),
                                world.getBlockLightColor(lx, ly1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, baseY, lz - 1),
                                world.getBlockSkyLightValue(lx, baseY, lz),
                                world.getBlockSkyLightValue(lx, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, baseY, lz - 1),
                                world.getBlockLightValue(lx, baseY, lz),
                                world.getBlockLightValue(lx, ly1, lz - 1),
                                world.getBlockLightValue(lx, ly1, lz),
                                world.getBlockLightColor(lx, baseY, lz - 1),
                                world.getBlockLightColor(lx, baseY, lz),
                                world.getBlockLightColor(lx, ly1, lz - 1),
                                world.getBlockLightColor(lx, ly1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, baseY, lz - 1),
                                world.getBlockSkyLightValue(lx, baseY, lz),
                                world.getBlockSkyLightValue(lx, ly1, lz - 1),
                                world.getBlockSkyLightValue(lx, ly1, lz)
                        );
                    }
                }

                case EAST_FACE -> {
                    int baseX = xBlock + meshX, baseY = yBlock + meshY, ly1 = baseY + 1, lz = zBlock - 1;

                    boolean solidA = blocks[world.getBlockID(baseX + 1, baseY, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(baseX, ly1, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(baseX + 1, baseY, lz),
                                world.getBlockLightValue(baseX, baseY, lz),
                                world.getBlockLightValue(baseX, ly1, lz),
                                world.getBlockLightColor(baseX + 1, baseY, lz),
                                world.getBlockLightColor(baseX, baseY, lz),
                                world.getBlockLightColor(baseX, ly1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(baseX + 1, baseY, lz),
                                world.getBlockSkyLightValue(baseX, baseY, lz),
                                world.getBlockSkyLightValue(baseX, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(baseX + 1, baseY, lz),
                                world.getBlockLightValue(baseX, baseY, lz),
                                world.getBlockLightValue(baseX + 1, ly1, lz),
                                world.getBlockLightValue(baseX, ly1, lz),
                                world.getBlockLightColor(baseX + 1, baseY, lz),
                                world.getBlockLightColor(baseX, baseY, lz),
                                world.getBlockLightColor(baseX + 1, ly1, lz),
                                world.getBlockLightColor(baseX, ly1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(baseX + 1, baseY, lz),
                                world.getBlockSkyLightValue(baseX, baseY, lz),
                                world.getBlockSkyLightValue(baseX + 1, ly1, lz),
                                world.getBlockSkyLightValue(baseX, ly1, lz)
                        );
                    }
                }
            }
        } else if (x == xMin && y == yMax && z == zMax) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];
            final int meshZ = greedyMeshSize[1];
            final int yUp = yBlock + 1;

            switch (face) {
                case TOP_FACE -> {
                    int lx1 = xBlock - 1, lx2 = xBlock;
                    int ly = yUp, lzBase = zBlock + meshZ, lzNext = lzBase + 1;

                    boolean solidA = blocks[world.getBlockID(lx1, ly, lzBase)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx2, ly, lzNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx2, ly, lzNext),
                                world.getBlockLightValue(lx1, ly, lzBase),
                                world.getBlockLightValue(lx2, ly, lzBase),
                                world.getBlockLightColor(lx2, ly, lzNext),
                                world.getBlockLightColor(lx1, ly, lzBase),
                                world.getBlockLightColor(lx2, ly, lzBase)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx2, ly, lzNext),
                                world.getBlockSkyLightValue(lx1, ly, lzBase),
                                world.getBlockSkyLightValue(lx2, ly, lzBase)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx1, ly, lzNext),
                                world.getBlockLightValue(lx2, ly, lzNext),
                                world.getBlockLightValue(lx1, ly, lzBase),
                                world.getBlockLightValue(lx2, ly, lzBase),
                                world.getBlockLightColor(lx1, ly, lzNext),
                                world.getBlockLightColor(lx2, ly, lzNext),
                                world.getBlockLightColor(lx1, ly, lzBase),
                                world.getBlockLightColor(lx2, ly, lzBase)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx1, ly, lzNext),
                                world.getBlockSkyLightValue(lx2, ly, lzNext),
                                world.getBlockSkyLightValue(lx1, ly, lzBase),
                                world.getBlockSkyLightValue(lx2, ly, lzBase)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int lx = xBlock - 1;
                    int lzBase = zBlock + meshX, lzNext = lzBase + 1;
                    int lyBase = yBlock + meshZ, lyNext = lyBase + 1;

                    boolean solidA = blocks[world.getBlockID(lx, lyNext, lzBase)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, lyBase, lzNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, lyBase, lzNext),
                                world.getBlockLightValue(lx, lyBase, lzBase),
                                world.getBlockLightValue(lx, lyNext, lzBase),
                                world.getBlockLightColor(lx, lyBase, lzNext),
                                world.getBlockLightColor(lx, lyBase, lzBase),
                                world.getBlockLightColor(lx, lyNext, lzBase)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, lyBase, lzNext),
                                world.getBlockSkyLightValue(lx, lyBase, lzBase),
                                world.getBlockSkyLightValue(lx, lyNext, lzBase)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, lyBase, lzNext),
                                world.getBlockLightValue(lx, lyBase, lzBase),
                                world.getBlockLightValue(lx, lyNext, lzNext),
                                world.getBlockLightValue(lx, lyNext, lzBase),
                                world.getBlockLightColor(lx, lyBase, lzNext),
                                world.getBlockLightColor(lx, lyBase, lzBase),
                                world.getBlockLightColor(lx, lyNext, lzNext),
                                world.getBlockLightColor(lx, lyNext, lzBase)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, lyBase, lzNext),
                                world.getBlockSkyLightValue(lx, lyBase, lzBase),
                                world.getBlockSkyLightValue(lx, lyNext, lzNext),
                                world.getBlockSkyLightValue(lx, lyNext, lzBase)
                        );
                    }
                }

                case WEST_FACE -> {
                    int lx1 = xBlock - 1, lx2 = xBlock;
                    int lz = zBlock + 1, lyBase = yBlock + meshZ, lyNext = lyBase + 1;

                    boolean solidA = blocks[world.getBlockID(lx1, lyBase, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx2, lyNext, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx1, lyBase, lz),
                                world.getBlockLightValue(lx2, lyBase, lz),
                                world.getBlockLightValue(lx2, lyNext, lz),
                                world.getBlockLightColor(lx1, lyBase, lz),
                                world.getBlockLightColor(lx2, lyBase, lz),
                                world.getBlockLightColor(lx2, lyNext, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx1, lyBase, lz),
                                world.getBlockSkyLightValue(lx2, lyBase, lz),
                                world.getBlockSkyLightValue(lx2, lyNext, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx1, lyBase, lz),
                                world.getBlockLightValue(lx2, lyBase, lz),
                                world.getBlockLightValue(lx1, lyNext, lz),
                                world.getBlockLightValue(lx2, lyNext, lz),
                                world.getBlockLightColor(lx1, lyBase, lz),
                                world.getBlockLightColor(lx2, lyBase, lz),
                                world.getBlockLightColor(lx1, lyNext, lz),
                                world.getBlockLightColor(lx2, lyNext, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx1, lyBase, lz),
                                world.getBlockSkyLightValue(lx2, lyBase, lz),
                                world.getBlockSkyLightValue(lx1, lyNext, lz),
                                world.getBlockSkyLightValue(lx2, lyNext, lz)
                        );
                    }
                }
            }
        } else if (x == xMax && y == yMin && z == zMax) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];
            final int meshZ = greedyMeshSize[1];
            final int lxBase = xBlock + meshX;
            final int lzBase = zBlock + meshZ;
            final int ly = yBlock - 1;
            final int lzNext = lzBase + 1;
            final int lxNext = lxBase + 1;

            switch (face) {
                case BOTTOM_FACE -> {
                    boolean solidA = blocks[world.getBlockID(lxNext, ly, lzBase)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lxBase, ly, lzNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lxBase, ly, lzNext),
                                world.getBlockLightValue(lxNext, ly, lzBase),
                                world.getBlockLightValue(lxBase, ly, lzBase),
                                world.getBlockLightColor(lxBase, ly, lzNext),
                                world.getBlockLightColor(lxNext, ly, lzBase),
                                world.getBlockLightColor(lxBase, ly, lzBase)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lxBase, ly, lzNext),
                                world.getBlockSkyLightValue(lxNext, ly, lzBase),
                                world.getBlockSkyLightValue(lxBase, ly, lzBase)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lxNext, ly, lzNext),
                                world.getBlockLightValue(lxBase, ly, lzNext),
                                world.getBlockLightValue(lxNext, ly, lzBase),
                                world.getBlockLightValue(lxBase, ly, lzBase),
                                world.getBlockLightColor(lxNext, ly, lzNext),
                                world.getBlockLightColor(lxBase, ly, lzNext),
                                world.getBlockLightColor(lxNext, ly, lzBase),
                                world.getBlockLightColor(lxBase, ly, lzBase)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lxNext, ly, lzNext),
                                world.getBlockSkyLightValue(lxBase, ly, lzNext),
                                world.getBlockSkyLightValue(lxNext, ly, lzBase),
                                world.getBlockSkyLightValue(lxBase, ly, lzBase)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    int lx = xBlock + 1, lz = zBlock + meshX;
                    boolean solidA = blocks[world.getBlockID(lx, yBlock - 1, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lx, yBlock, lz + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lx, yBlock, lz + 1),
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock, lz + 1),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lx, yBlock, lz + 1),
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lx, yBlock, lz + 1),
                                world.getBlockLightValue(lx, yBlock, lz),
                                world.getBlockLightValue(lx, yBlock - 1, lz + 1),
                                world.getBlockLightValue(lx, yBlock - 1, lz),
                                world.getBlockLightColor(lx, yBlock, lz + 1),
                                world.getBlockLightColor(lx, yBlock, lz),
                                world.getBlockLightColor(lx, yBlock - 1, lz + 1),
                                world.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lx, yBlock, lz + 1),
                                world.getBlockSkyLightValue(lx, yBlock, lz),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz + 1),
                                world.getBlockSkyLightValue(lx, yBlock - 1, lz)
                        );
                    }
                }

                case WEST_FACE -> {
                    int lyBase = yBlock, lyNext = yBlock - 1, lz = zBlock + 1;

                    boolean solidA = blocks[world.getBlockID(lxNext, lyBase, lz)].isSolid;
                    boolean solidB = blocks[world.getBlockID(lxBase, lyNext, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(lxNext, lyBase, lz),
                                world.getBlockLightValue(lxBase, lyBase, lz),
                                world.getBlockLightValue(lxBase, lyNext, lz),
                                world.getBlockLightColor(lxNext, lyBase, lz),
                                world.getBlockLightColor(lxBase, lyBase, lz),
                                world.getBlockLightColor(lxBase, lyNext, lz)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(lxNext, lyBase, lz),
                                world.getBlockSkyLightValue(lxBase, lyBase, lz),
                                world.getBlockSkyLightValue(lxBase, lyNext, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(lxNext, lyBase, lz),
                                world.getBlockLightValue(lxBase, lyBase, lz),
                                world.getBlockLightValue(lxNext, lyNext, lz),
                                world.getBlockLightValue(lxBase, lyNext, lz),
                                world.getBlockLightColor(lxNext, lyBase, lz),
                                world.getBlockLightColor(lxBase, lyBase, lz),
                                world.getBlockLightColor(lxNext, lyNext, lz),
                                world.getBlockLightColor(lxBase, lyNext, lz)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(lxNext, lyBase, lz),
                                world.getBlockSkyLightValue(lxBase, lyBase, lz),
                                world.getBlockSkyLightValue(lxNext, lyNext, lz),
                                world.getBlockSkyLightValue(lxBase, lyNext, lz)
                        );
                    }
                }
            }
        } else if (x == xMax && y == yMax && z == zMax) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];
            final int meshY = greedyMeshSize[1];

            final int xMesh = xBlock + meshX;
            final int xMeshNext = xMesh + 1;
            final int yUp = yBlock + 1;
            final int yMesh = yBlock + meshY;
            final int yMeshNext = yMesh + 1;
            final int zMesh = zBlock + meshY;
            final int zMeshNext = zMesh + 1;
            final int zMeshX = zBlock + meshX;
            final int zMeshXNext = zMeshX + 1;

            switch (face) {
                case TOP_FACE -> {
                    boolean solidA = blocks[world.getBlockID(xMeshNext, yUp, zMesh)].isSolid;
                    boolean solidB = blocks[world.getBlockID(xMesh, yUp, zMeshNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(xMesh, yUp, zMeshNext),
                                world.getBlockLightValue(xMeshNext, yUp, zMesh),
                                world.getBlockLightValue(xMesh, yUp, zMesh),
                                world.getBlockLightColor(xMesh, yUp, zMeshNext),
                                world.getBlockLightColor(xMeshNext, yUp, zMesh),
                                world.getBlockLightColor(xMesh, yUp, zMesh)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(xMesh, yUp, zMeshNext),
                                world.getBlockSkyLightValue(xMeshNext, yUp, zMesh),
                                world.getBlockSkyLightValue(xMesh, yUp, zMesh)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xMeshNext, yUp, zMeshNext),
                                world.getBlockLightValue(xMesh, yUp, zMeshNext),
                                world.getBlockLightValue(xMeshNext, yUp, zMesh),
                                world.getBlockLightValue(xMesh, yUp, zMesh),
                                world.getBlockLightColor(xMeshNext, yUp, zMeshNext),
                                world.getBlockLightColor(xMesh, yUp, zMeshNext),
                                world.getBlockLightColor(xMeshNext, yUp, zMesh),
                                world.getBlockLightColor(xMesh, yUp, zMesh)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xMeshNext, yUp, zMeshNext),
                                world.getBlockSkyLightValue(xMesh, yUp, zMeshNext),
                                world.getBlockSkyLightValue(xMeshNext, yUp, zMesh),
                                world.getBlockSkyLightValue(xMesh, yUp, zMesh));
                    }
                }

                case SOUTH_FACE -> {
                    boolean solidA = blocks[world.getBlockID(xBlock + 1, yMeshNext, zMeshX)].isSolid;
                    boolean solidB = blocks[world.getBlockID(xBlock + 1, yMesh, zMeshXNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(xBlock + 1, yMesh, zMeshXNext),
                                world.getBlockLightValue(xBlock + 1, yMesh, zMeshX),
                                world.getBlockLightValue(xBlock + 1, yMeshNext, zMeshX),
                                world.getBlockLightColor(xBlock + 1, yMesh, zMeshXNext),
                                world.getBlockLightColor(xBlock + 1, yMesh, zMeshX),
                                world.getBlockLightColor(xBlock + 1, yMeshNext, zMeshX)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(xBlock + 1, yMesh, zMeshXNext),
                                world.getBlockSkyLightValue(xBlock + 1, yMesh, zMeshX),
                                world.getBlockSkyLightValue(xBlock + 1, yMeshNext, zMeshX)
                        );
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xBlock + 1, yMesh, zMeshXNext),
                                world.getBlockLightValue(xBlock + 1, yMesh, zMeshX),
                                world.getBlockLightValue(xBlock + 1, yMeshNext, zMeshXNext),
                                world.getBlockLightValue(xBlock + 1, yMeshNext, zMeshX),
                                world.getBlockLightColor(xBlock + 1, yMesh, zMeshXNext),
                                world.getBlockLightColor(xBlock + 1, yMesh, zMeshX),
                                world.getBlockLightColor(xBlock + 1, yMeshNext, zMeshXNext),
                                world.getBlockLightColor(xBlock + 1, yMeshNext, zMeshX)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xBlock + 1, yMesh, zMeshXNext),
                                world.getBlockSkyLightValue(xBlock + 1, yMesh, zMeshX),
                                world.getBlockSkyLightValue(xBlock + 1, yMeshNext, zMeshXNext),
                                world.getBlockSkyLightValue(xBlock + 1, yMeshNext, zMeshX)
                        );
                    }
                }

                case WEST_FACE -> {
                    boolean solidA = blocks[world.getBlockID(xMeshNext, yMesh, zBlock + 1)].isSolid;
                    boolean solidB = blocks[world.getBlockID(xMesh, yMeshNext, zBlock + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                world.getBlockLightValue(xMeshNext, yMesh, zBlock + 1),
                                world.getBlockLightValue(xMesh, yMesh, zBlock + 1),
                                world.getBlockLightValue(xMesh, yMeshNext, zBlock + 1),
                                world.getBlockLightColor(xMeshNext, yMesh, zBlock + 1),
                                world.getBlockLightColor(xMesh, yMesh, zBlock + 1),
                                world.getBlockLightColor(xMesh, yMeshNext, zBlock + 1)
                        );
                        this.setVertexSkylightValue3Args(
                                world.getBlockSkyLightValue(xMeshNext, yMesh, zBlock + 1),
                                world.getBlockSkyLightValue(xMesh, yMesh, zBlock + 1),
                                world.getBlockSkyLightValue(xMesh, yMeshNext, zBlock + 1));
                    } else {
                        setVertexLight4Args(
                                world.getBlockLightValue(xMeshNext, yMesh, zBlock + 1),
                                world.getBlockLightValue(xMesh, yMesh, zBlock + 1),
                                world.getBlockLightValue(xMeshNext, yMeshNext, zBlock + 1),
                                world.getBlockLightValue(xMesh, yMeshNext, zBlock + 1),
                                world.getBlockLightColor(xMeshNext, yMesh, zBlock + 1),
                                world.getBlockLightColor(xMesh, yMesh, zBlock + 1),
                                world.getBlockLightColor(xMeshNext, yMeshNext, zBlock + 1),
                                world.getBlockLightColor(xMesh, yMeshNext, zBlock + 1)
                        );
                        this.setVertexSkylightValue4Args(
                                world.getBlockSkyLightValue(xMeshNext, yMesh, zBlock + 1),
                                world.getBlockSkyLightValue(xMesh, yMesh, zBlock + 1),
                                world.getBlockSkyLightValue(xMeshNext, yMeshNext, zBlock + 1),
                                world.getBlockSkyLightValue(xMesh, yMeshNext, zBlock + 1));
                    }
                }
            }
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock - 1), world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock - 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock - 1), world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock - 1), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock + 1, yBlock, zBlock - 1), world.getBlockSkyLightValue(xBlock + 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock - 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1), world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock + 1), world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock + 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock + 1), world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock + 1), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock + 1, yBlock, zBlock + 1), world.getBlockSkyLightValue(xBlock + 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock + 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMin) {

            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1), world.getBlockLightColor(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock - 1), world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock - 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));


        } else if (x == xMin && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock), world.getBlockSkyLightValue(xBlock - 1, yBlock - 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1), world.getBlockLightColor(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock + 1), world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock + 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock + 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock), world.getBlockSkyLightValue(xBlock + 1, yBlock - 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMin) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock + 1, zBlock - 1), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock - 1), world.getBlockLightColor(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock, yBlock + 1, zBlock - 1), world.getBlockSkyLightValue(xBlock, yBlock + 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock - 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock - 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock), world.getBlockSkyLightValue(xBlock - 1, yBlock + 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock + 1, zBlock + 1), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock + 1), world.getBlockLightColor(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock, yBlock + 1, zBlock + 1), world.getBlockSkyLightValue(xBlock, yBlock + 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock + 1), world.getBlockSkyLightValue(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock + 1, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock));
            this.setVertexSkylightValue4Args(world.getBlockSkyLightValue(xBlock + 1, yBlock, zBlock), world.getBlockSkyLightValue(xBlock, yBlock, zBlock), world.getBlockSkyLightValue(xBlock + 1, yBlock + 1, zBlock), world.getBlockSkyLightValue(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight2Args(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight2Args(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight2Args(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight1Arg(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else {
            setVertexLight1Arg(world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock));
        }

        this.setGrayScaleImageMultiplier();
    }

    private  void resetLight() {
        this.red = this.redReset;
        this.green = this.greenReset;
        this.blue = this.blueReset;
        this.skyLightValue = this.skyLightReset;
    }

    private void setVertexLight1Arg(byte light,
                                    float[] lightColor) {

        final float finalLight = getLightValueFromMap(light);

        float r = lightColor[0];
        float g = lightColor[1];
        float b = lightColor[2];

        red   *= r * finalLight;
        green *= g * finalLight;
        blue  *= b * finalLight;
    }

    private void setVertexLight2Args(byte light1, byte light2,
                                     float[] lightColor, float[] lightColor2) {

        final float finalLight = (getLightValueFromMap(light1) +
                getLightValueFromMap(light2)) * 0.5F; // Faster than /2F

        float rSum = 0F, gSum = 0F, bSum = 0F;
        int rCount = 0, gCount = 0, bCount = 0;

        float r, g, b;

        // lightColor
        r = lightColor[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor[2]; if (b != 0F) { bSum += b; bCount++; }

        // lightColor2
        r = lightColor2[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor2[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor2[2]; if (b != 0F) { bSum += b; bCount++; }

        red   *= (rCount > 0 ? rSum / rCount : 0F) * finalLight;
        green *= (gCount > 0 ? gSum / gCount : 0F) * finalLight;
        blue  *= (bCount > 0 ? bSum / bCount : 0F) * finalLight;
    }

    private void setVertexLight3Args(byte light1, byte light2, byte light3,
                                     float[] lightColor, float[] lightColor2,
                                     float[] lightColor3) {

        final float finalLight = (getLightValueFromMap(light1) +
                getLightValueFromMap(light2) +
                getLightValueFromMap(light3)) * 0.3333333F; // Avoids division

        float rSum = 0F, gSum = 0F, bSum = 0F;
        int rCount = 0, gCount = 0, bCount = 0;

        float r, g, b;

        // lightColor
        r = lightColor[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor[2]; if (b != 0F) { bSum += b; bCount++; }

        // lightColor2
        r = lightColor2[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor2[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor2[2]; if (b != 0F) { bSum += b; bCount++; }

        // lightColor3
        r = lightColor3[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor3[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor3[2]; if (b != 0F) { bSum += b; bCount++; }

        red   *= (rCount > 0 ? rSum / rCount : 0F) * finalLight;
        green *= (gCount > 0 ? gSum / gCount : 0F) * finalLight;
        blue  *= (bCount > 0 ? bSum / bCount : 0F) * finalLight;
    }


    private void setVertexLight4Args(byte light1, byte light2, byte light3, byte light4,
                                     float[] lightColor, float[] lightColor2,
                                     float[] lightColor3, float[] lightColor4) {

        final float finalLight = (getLightValueFromMap(light1) +
                getLightValueFromMap(light2) +
                getLightValueFromMap(light3) +
                getLightValueFromMap(light4)) * 0.25F;

        float rSum = 0F, gSum = 0F, bSum = 0F;
        int rCount = 0, gCount = 0, bCount = 0;

        float r, g, b;

        r = lightColor[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor[2]; if (b != 0F) { bSum += b; bCount++; }

        r = lightColor2[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor2[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor2[2]; if (b != 0F) { bSum += b; bCount++; }

        r = lightColor3[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor3[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor3[2]; if (b != 0F) { bSum += b; bCount++; }

        r = lightColor4[0]; if (r != 0F) { rSum += r; rCount++; }
        g = lightColor4[1]; if (g != 0F) { gSum += g; gCount++; }
        b = lightColor4[2]; if (b != 0F) { bSum += b; bCount++; }

        red   *= (rCount > 0 ? rSum / rCount : 0F) * finalLight;
        green *= (gCount > 0 ? gSum / gCount : 0F) * finalLight;
        blue  *= (bCount > 0 ? bSum / bCount : 0F) * finalLight;
    }

    private void setVertexSkylightValue3Args(byte light1, byte light2, byte light3){
        float light1Float = this.getLightValueFromMap(light1);
        float light2Float = this.getLightValueFromMap(light2);
        float light3Float = this.getLightValueFromMap(light3);
        this.skyLightValue = (light1Float + light2Float + light3Float) * 0.33f;
    }

    private void setVertexSkylightValue1Args(byte light1){
        float light1Float = this.getLightValueFromMap(light1);
        this.skyLightValue = light1Float;
    }

    private void setVertexSkylightValue4Args(byte light1, byte light2, byte light3, byte light4){
        float light1Float = this.getLightValueFromMap(light1);
        float light2Float = this.getLightValueFromMap(light2);
        float light3Float = this.getLightValueFromMap(light3);
        float light4Float = this.getLightValueFromMap(light4);
        this.skyLightValue = (light1Float + light2Float + light3Float + light4Float) * 0.25f;
    }

    private int calculateFrostAmount(double temperature){
        //0 above 40
        //1 35-40
        //2 35-30
        //3 below 30, snow generates at 30

        if(temperature > 0.4)return 0;
        if(temperature > 0.35)return 1;
        if(temperature > 0.3)return 2;

        return 3;
    }


    private float compressTextureCoordinatesAndFrostFactor(float x, float y, double temperature){
        int xUV = 0;
        int yUV = 0;

        if(x <= 1.0){
            xUV = (int)(x * 32f);
            xUV <<= 1;
            xUV |= 1;
        }

        if(y <= 1.0){
            yUV = (int)(y * 32f);
            yUV <<= 1;
            yUV |= 1;
        }

        if(x > 1.0){
            xUV = (int)x;
            xUV <<= 1;
        }

        if(y > 1.0){
            yUV = (int)y;
            yUV <<= 1;
        }

        //Re encode each section to be 6 bits for UV values plus 1 bit for fractional bool, this leaves 10 bits at the end to encode frost state
        //None, Light, Medium, Heavy, this leaves one additional byte to encode future information with

        //00000000 0000000 0000000 00 00000000
        //In order, grayscale image mult, xUV, yUV, frost factor, unused byte

        int combinedInt = (((this.grayScaleImageMultiplier & 255) << 24) | (xUV << 17) | (yUV << 10) | (this.calculateFrostAmount(temperature) << 8));
        return Float.intBitsToFloat(combinedInt);
    }

    private float compressColor(float red, float green, float blue){
        return Float.intBitsToFloat((((this.grayScaleImageMultiplier >> 8 & 255) << 24) | (MathUtil.floatToIntRGBA(red) << 16) | (MathUtil.floatToIntRGBA(green) << 8) | MathUtil.floatToIntRGBA(blue)));
    }

    private float compressPosXY(float x, float y){
        return Float.intBitsToFloat(MathUtil.floatToHalf(x) << 16 |  MathUtil.floatToHalf(y));
    }

    private float compressPosZAndTexId(float z, float texID){
        return Float.intBitsToFloat(MathUtil.floatToHalf(z) << 16 | (int)texID);
    }

    private float compressNormalXY(float normalX, float normalY){
        return Float.intBitsToFloat(MathUtil.floatToHalf(normalX) << 16 | MathUtil.floatToHalf(normalY));
    }

    private float compressNormalZAndSkyLightValue(float normalZ, float skyLightValue){
        return Float.intBitsToFloat(MathUtil.floatToHalf(normalZ) << 16 | MathUtil.floatToHalf(skyLightValue));
    }

    private void renderOpaqueFaceDirect(Chunk chunk, World world, short block, int index, int face, ModelFace blockFace, int[] greedyMeshSize, float blockTextureID) {
        if(blockFace == null)return;
        int x = (index & 31);
        int y = (index >> 10);
        int z = ((index & 1023) >> 5);



        Vector3f minVertex = new Vector3f(x,y,z);
        Vector3f maxVertex = new Vector3f(x + 1, y + 1, z + 1);
        Vector3f blockPosition = new Vector3f(x, y, z);


        Vector3f normal = new Vector3f(blockFace.normal.x, blockFace.normal.y, blockFace.normal.z);

        for (int i = 0; i < 4; i++) {
            Vector3f v = blockFace.vertices[i];
            float[] uv = blockFace.UVs[i];

            float wx = v.x + blockPosition.x;
            float wy = v.y + blockPosition.y;
            float wz = v.z + blockPosition.z;

            resetLight();
            setLight(wx, wy, wz, minVertex.x, minVertex.y, minVertex.z,
                    maxVertex.x, maxVertex.y, maxVertex.z, index, face,
                    chunk, world, greedyMeshSize, block, (int) blockTextureID);

            addVertexFloatOpaque(chunk, compressPosXY(wx, wy));
            addVertexFloatOpaque(chunk, compressColor(red, green, blue));
            addVertexFloatOpaque(chunk, compressTextureCoordinatesAndFrostFactor(uv[0], uv[1], world.getTemperatureWithTimeOfDay(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index))));
            addVertexFloatOpaque(chunk, compressPosZAndTexId(wz, blockTextureID));
            addVertexFloatOpaque(chunk, compressNormalXY(normal.x, normal.y));
            addVertexFloatOpaque(chunk, compressNormalZAndSkyLightValue(normal.z, skyLightValue));
        }


        resetLight();
        this.addElementsOpaque(chunk);
    }


    private void renderOpaqueFace(Chunk chunk, World world, short block, int index, int face, ModelFace blockFace, int[] greedyMeshSize) {
        if(blockFace == null)return;
        int x = (index & 31);
        int y = (index >> 10);
        int z = ((index & 1023) >> 5);

        float blockTextureID = blockFace.texture == RenderEngine.NULL_TEXTURE ? getBlockTextureID(block, face, chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index)) : blockFace.texture;

        if(block == Block.itemBlock.ID){
            blockTextureID = Block.itemBlock.getBlockTexture(0, 0, 0, blockFace.texture);
        }

        if(block == Block.tilledSoil.ID){
            blockTextureID = ((BlockTilledSoil)Block.tilledSoil).getBlockTexture(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), face, chunk.getBlockZFromIndex(index));
        }

        if(block == Block.crafting3DItem.ID){
            InWorld3DCraftingItem craftingItem = (InWorld3DCraftingItem) chunk.getBlockState(index, MultiState.CRAFTING_3D_ITEM_STATE);
            if(craftingItem == null)return;
            blockTextureID = craftingItem.itemTextureID;
        }

        ModelLoader modelLoader = Block.list[block].getBlockModel(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), world);

        Vector3f minVertex = new Vector3f(x,y,z);
        Vector3f maxVertex = new Vector3f(x + 1, y + 1, z + 1);
        Vector3f blockPosition = new Vector3f(x, y, z);

        if(block == Block.oakLog.ID){
            int faceDirectionCurrentLog = BlockLog.facingDirectionOfLog(chunk.getBlockXFromIndex(index),chunk.getBlockYFromIndex(index),chunk.getBlockZFromIndex(index),world);
            int sizeOfCurrentLog = BlockLog.sizeOfLog(chunk.getBlockXFromIndex(index),chunk.getBlockYFromIndex(index),chunk.getBlockZFromIndex(index),world);
            final int xBlock = chunk.getBlockXFromIndex(index);
            final int yBlock = chunk.getBlockYFromIndex(index);
            final int zBlock = chunk.getBlockZFromIndex(index);
            if(world.getBlockID(xBlock + 1, yBlock, zBlock) >= Block.oakLog.ID && BlockLog.sizeOfLog(xBlock + 1, yBlock, zBlock, world) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(xBlock + 1, yBlock, zBlock, world) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendSouthFace();
            } else if(world.getBlockID(xBlock - 1, yBlock, zBlock) >= Block.oakLog.ID && BlockLog.sizeOfLog(xBlock - 1, yBlock, zBlock, world) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(xBlock - 1, yBlock, zBlock, world) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendNorthFace();
            } else if(world.getBlockID(xBlock , yBlock, zBlock + 1) >= Block.oakLog.ID && BlockLog.sizeOfLog(xBlock, yBlock, zBlock + 1, world) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(xBlock, yBlock, zBlock + 1, world) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendWestFace();
            } else if(world.getBlockID(xBlock , yBlock, zBlock - 1) >= Block.oakLog.ID && BlockLog.sizeOfLog(xBlock, yBlock, zBlock - 1, world) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(xBlock, yBlock, zBlock - 1, world) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendEastFace();
            } else if(world.getBlockID(xBlock , yBlock + 1, zBlock) >= Block.oakLog.ID && BlockLog.sizeOfLog(xBlock, yBlock + 1, zBlock, world) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(xBlock, yBlock + 1, zBlock, world) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendTopFace();
            } else if(world.getBlockID(xBlock , yBlock - 1, zBlock) >= Block.oakLog.ID && BlockLog.sizeOfLog(xBlock, yBlock - 1, zBlock, world) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(xBlock, yBlock - 1, zBlock, world) != faceDirectionCurrentLog) {
                modelLoader = modelLoader.extendBottomFace();
            }
            for(int i = 0; i < modelLoader.modelFaces.length; i++){
                if(modelLoader.modelFaces[i].faceType == TOP_FACE){
                    maxVertex = new Vector3f(modelLoader.modelFaces[i].vertices[2].x, modelLoader.modelFaces[i].vertices[2].y, modelLoader.modelFaces[i].vertices[2].z).add(blockPosition);
                }
                if(modelLoader.modelFaces[i].faceType == BOTTOM_FACE){
                    minVertex = new Vector3f(modelLoader.modelFaces[i].vertices[0].x, modelLoader.modelFaces[i].vertices[0].y, modelLoader.modelFaces[i].vertices[0].z).add(blockPosition);
                }
            }
        }

        if(Block.list[block].canGreedyMesh){
            blockFace = blockFace.copyFace();
            blockFace.applyGreedyMeshing(greedyMeshSize);
        }

        Vector3f normal = new Vector3f(blockFace.normal.x, blockFace.normal.y, blockFace.normal.z);

        for (int i = 0; i < 4; i++) {
            Vector3f v = blockFace.vertices[i];
            float[] uv = blockFace.UVs[i];

            float wx = v.x + blockPosition.x;
            float wy = v.y + blockPosition.y;
            float wz = v.z + blockPosition.z;

            resetLight();
            setLight(wx, wy, wz, minVertex.x, minVertex.y, minVertex.z,
                    maxVertex.x, maxVertex.y, maxVertex.z, index, face,
                    chunk, world, greedyMeshSize, block, (int) blockTextureID);

            addVertexFloatOpaque(chunk, compressPosXY(wx, wy));
            addVertexFloatOpaque(chunk, compressColor(red, green, blue));
            addVertexFloatOpaque(chunk, compressTextureCoordinatesAndFrostFactor(uv[0], uv[1], world.getTemperatureWithTimeOfDay(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index))));
            addVertexFloatOpaque(chunk, compressPosZAndTexId(wz, blockTextureID));
            addVertexFloatOpaque(chunk, compressNormalXY(normal.x, normal.y));
            addVertexFloatOpaque(chunk, compressNormalZAndSkyLightValue(normal.z, skyLightValue));
        }


        resetLight();
        this.addElementsOpaque(chunk);
    }

    private  void renderTransparentFace(Chunk chunk, World world, short block, int index, int face, ModelFace blockFace, int[] greedyMeshSize) {
        if(blockFace == null)return;
        int x = (index & 31);
        int y = (index >> 10);
        int z = ((index & 1023) >> 5);

        float blockID = blockFace.texture == RenderEngine.NULL_TEXTURE ? getBlockTextureID(block, face, chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index)) : blockFace.texture;

        Vector3f minVertex = new Vector3f(x,y,z);
        Vector3f maxVertex = new Vector3f(x + 1, y + 1, z + 1);
        Vector3f blockPosition = new Vector3f(x, y, z);

        if(block == Block.cropGrowth.ID){
            blockID = ((BlockCrop)(Block.cropGrowth)).getBlockTexture(chunk.getBlockXFromIndex(index),chunk.getBlockYFromIndex(index),chunk.getBlockZFromIndex(index), world);
        }


        Vector3f normal = new Vector3f(blockFace.normal.x, blockFace.normal.y, blockFace.normal.z);


        for (int i = 0; i < 4; i++) {
            Vector3f v = blockFace.vertices[i];
            float[] uv = blockFace.UVs[i];

            float wx = v.x + blockPosition.x;
            float wy = v.y + blockPosition.y;
            float wz = v.z + blockPosition.z;

            resetLight();
            setLight(wx, wy, wz, minVertex.x, minVertex.y, minVertex.z,
                    maxVertex.x, maxVertex.y, maxVertex.z, index, face,
                    chunk, world, greedyMeshSize, block, (int) blockID);

            addVertexFloatTransparent(chunk, compressPosXY(wx, wy));
            addVertexFloatTransparent(chunk, compressColor(red, green, blue));
            addVertexFloatTransparent(chunk, compressTextureCoordinatesAndFrostFactor(uv[0], uv[1], world.getTemperatureWithTimeOfDay(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index))));
            addVertexFloatTransparent(chunk, compressPosZAndTexId(wz, blockID));
            addVertexFloatTransparent(chunk, compressNormalXY(normal.x, normal.y));
            addVertexFloatTransparent(chunk, compressNormalZAndSkyLightValue(normal.z, skyLightValue));
        }


        resetLight();
        this.addElementsTransparent(chunk);
    }

    private void addElementsOpaque(Chunk chunk) {
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 0);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 1);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 2);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 0);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 2);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 3);
        chunk.elementOffsetOpaque += 4;
    }

    private void addElementsTransparent(Chunk chunk) {
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 0);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 1);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 2);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 0);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 2);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 3);
        chunk.elementOffsetTransparent += 4;
    }

    public void addVertexFloatOpaque(Chunk chunk, float value){
        if(chunk.tempVertexBufferOpaque.position() == chunk.tempVertexBufferOpaque.limit()){
            FloatBuffer oldBuffer = chunk.tempVertexBufferOpaque;
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.tempVertexBufferOpaque = newBuffer;
        }
        chunk.tempVertexBufferOpaque.put(value);
    }

    public void addVertexFloatTransparent(Chunk chunk, float value){
        if(chunk.tempVertexBufferTransparent.position() == chunk.tempVertexBufferTransparent.limit()){
            FloatBuffer oldBuffer = chunk.tempVertexBufferTransparent;
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.tempVertexBufferTransparent = newBuffer;
        }
        chunk.tempVertexBufferTransparent.put(value);
    }

    public void addElementOpaque(Chunk chunk, int value){
        if(chunk.tempElementBufferOpaque.position() == chunk.tempElementBufferOpaque.limit()){
            IntBuffer oldBuffer = chunk.tempElementBufferOpaque;
            IntBuffer newBuffer = BufferUtils.createIntBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.tempElementBufferOpaque = newBuffer;
        }
        chunk.tempElementBufferOpaque.put(value);
    }

    public void addElementTransparent(Chunk chunk, int value){
        if(chunk.tempElementBufferTransparent.position() == chunk.tempElementBufferTransparent.limit()){
            IntBuffer oldBuffer = chunk.tempElementBufferTransparent;
            IntBuffer newBuffer = BufferUtils.createIntBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.tempElementBufferTransparent = newBuffer;
        }
        chunk.tempElementBufferTransparent.put(value);
    }


    public static float getBlockTextureID(short block, int face, int x, int y, int z) {
        return Block.list[block].getBlockTexture(block, x,y,z, face);
    }

}
