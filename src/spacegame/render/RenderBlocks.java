package spacegame.render;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import spacegame.block.*;
import spacegame.core.MathUtil;
import spacegame.item.Item;
import spacegame.world.ChestLocation;
import spacegame.world.Chunk;
import spacegame.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    public static final int TOP_FACE = 0;
    public static final int BOTTOM_FACE = 1;
    public static final int NORTH_FACE = 2;
    public static final int SOUTH_FACE = 3;
    public static final int EAST_FACE = 4;
    public static final int WEST_FACE = 5;
    public static final int TOP_FACE_UNSORTED = 6;
    public static final int BOTTOM_FACE_UNSORTED = 7;
    public static final int NORTH_FACE_UNSORTED = 8;
    public static final int SOUTH_FACE_UNSORTED = 9;
    public static final int EAST_FACE_UNSORTED = 10;
    public static final int WEST_FACE_UNSORTED = 11;


    public  void renderStandardBlock(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        this.handleWaterLoggedBlocks(chunk, world, block, index, face);

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        float[] UVSamples;
        for (int i = 0; i < modelFaces.length; i++) {
            UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i])) : autoUVNSEW(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i]));
            renderOpaqueFace(chunk, world, block, index, face, modelFaces[i], UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, greedyMeshSize);
        }
    }

    private void handleWaterLoggedBlocks(Chunk chunk, World world, short block, int index, int face){
        if(Block.list[block].waterlogged && block != Block.water.ID){
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

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        for (int i = 0; i < modelFaces.length; i++) {
            renderTransparentFace(chunk, world, block, index, face, modelFaces[i], 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
        }
    }

    public  void renderTorch(Chunk chunk, World world, short block, int index, int face) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        switch (block) {
            case 2 -> {
                switch (face) {
                    case TOP_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == TOP_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, -0.875F, 0.875F, 0, 0, -0.875F, 0, 0, 0.875F, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case NORTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case SOUTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case EAST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case WEST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                }
            }
            case 3, 4 -> {
                switch (face) {
                    case TOP_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == TOP_FACE) { //0, 0, -0.875F, 0.875F, 0, 0.875F, -0.875F, 0
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, -0.875F, 0.875F, 0, 0, -0.875F, 0, 0, 0.875F, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case NORTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case SOUTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case EAST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case WEST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                }
            }
            case 5, 6 -> {
                switch (face) {
                    case TOP_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == TOP_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, -0.875F, 0.875F, 0, 0, -0.875F, 0, 0, 0.875F, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case NORTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case SOUTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case EAST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case WEST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                }
            }
        }
    }

    public void renderCampFire(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        this.renderCampFireUnlit(chunk, world, block, index, face);

        ModelLoader shrunkBlock = Block.fireBlockModel.getScaledModel(0.4f).translateModel(0.3f, 0f, 0.3f);
        ModelFace[] modelFace = shrunkBlock.getModelFaceOfType(face);
        for(int i = 0; i < modelFace.length; i++){
            this.renderTransparentFace(chunk, world, Block.fire.ID, index, face, modelFace[i], 0,0,0,0,0,0,0,0, 3,1,2,0, new int[2]);
        }
    }

    public void renderCampFireUnlit(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        float[] UVSamples;
        for (int i = 0; i < modelFaces.length; i++) {
            UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i])) : autoUVNSEW(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i]));
            renderOpaqueFace(chunk, world, block, index, face, modelFaces[i], UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            if(modelFaces[i] != null){
                if(modelFaces[i].faceType == TOP_FACE){
                    renderOpaqueFace(chunk, world, block, index, face, Block.topFaceBlockModel.getModelFace(TOP_FACE), 0,0,0,0,0,0,0,0, 3,1,2,0, new int[2]); //This is for the staw covering on the ground
                }
            }
        }
    }

    public void renderLogOnCampfire(Chunk chunk, World world, short block, int index, int face) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        int logCount = ((BlockCampFire) Block.list[block]).getLogCount(); //Get log count and draw logs above the base

        block = Block.fireWoodBlock.ID; //Switch to this block id in order to bind the correct image in the texture array

        float log1XRot = (float) Math.toRadians(10);
        float log1YRot = (float) Math.toRadians(45);
        float log1ZRot = (float) Math.toRadians(5);
        Vector3f translationVectorLog1 = new Vector3f(0.5f, 0.135f, 0.5f);

        float log2XRot = (float) Math.toRadians(45);
        float log2YRot = (float) Math.toRadians(-45);
        float log2ZRot = (float) Math.toRadians(70);
        Vector3f translationVectorLog2 = new Vector3f(0.5f, 0.25f, 0.35f);

        float log3XRot = (float) Math.toRadians(45);
        float log3YRot = (float) Math.toRadians(0);
        float log3ZRot = (float) Math.toRadians(60);
        Vector3f translationVectorLog3 = new Vector3f(0.3f, 0.25f, 0.5f);

        float log4XRot = (float) Math.toRadians(-45);
        float log4YRot = (float) Math.toRadians(180);
        float log4ZRot = (float) Math.toRadians(-50);
        Vector3f translationVectorLog4 = new Vector3f(0.6f, 0.25f, 0.6f);


        //16x4 top and bottom, 4x4 north and south, 4x16 east and west sample
        ModelLoader baseModel = Block.fireWood;
        ModelFace modelFace;

        switch (logCount){ //This switch statement is not supposed to have case labels, I'm intentionally using the follow through of a default switch statement to reduce LOC, this is also why it's in reverse order
            case 4:
                ModelLoader log4 = baseModel.copyModel();
                for (int j = 0; j < log4.modelFaces.length; j++) {
                    for (int k = 0; k < log4.modelFaces[j].vertices.length; k++) {
                        log4.modelFaces[j].vertices[k].rotateX(log4XRot);
                        log4.modelFaces[j].vertices[k].rotateY(log4YRot);
                        log4.modelFaces[j].vertices[k].rotateZ(log4ZRot);

                        log4.modelFaces[j].vertices[k].add(translationVectorLog4);
                    }
                }

                modelFace = log4.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 3:
                ModelLoader log3 = baseModel.copyModel();
                for (int j = 0; j < log3.modelFaces.length; j++) {
                    for (int k = 0; k < log3.modelFaces[j].vertices.length; k++) {
                        log3.modelFaces[j].vertices[k].rotateX(log3XRot);
                        log3.modelFaces[j].vertices[k].rotateY(log3YRot);
                        log3.modelFaces[j].vertices[k].rotateZ(log3ZRot);

                        log3.modelFaces[j].vertices[k].add(translationVectorLog3);
                    }
                }

                modelFace = log3.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 2:
                ModelLoader log2 = baseModel.copyModel();
                for (int j = 0; j < log2.modelFaces.length; j++) {
                    for (int k = 0; k < log2.modelFaces[j].vertices.length; k++) {
                        log2.modelFaces[j].vertices[k].rotateX(log2XRot);
                        log2.modelFaces[j].vertices[k].rotateY(log2YRot);
                        log2.modelFaces[j].vertices[k].rotateZ(log2ZRot);

                        log2.modelFaces[j].vertices[k].add(translationVectorLog2);
                    }
                }

                modelFace = log2.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 1:
                ModelLoader log1 = baseModel.copyModel();
                for (int j = 0; j < log1.modelFaces.length; j++) {
                    for (int k = 0; k < log1.modelFaces[j].vertices.length; k++) {
                        log1.modelFaces[j].vertices[k].rotateX(log1XRot);
                        log1.modelFaces[j].vertices[k].rotateY(log1YRot);
                        log1.modelFaces[j].vertices[k].rotateZ(log1ZRot);

                        log1.modelFaces[j].vertices[k].add(translationVectorLog1);
                    }
                }

                modelFace = log1.getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);

        }
    }

    public void renderGrassBlock(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        for (int i = 0; i < modelFaces.length; i++) {
            renderOpaqueFace(chunk, world, block, index, face, modelFaces[i], 0,0,0,0,0,0,0,0, 3, 1, 2, 0, greedyMeshSize);
            if(modelFaces[i] != null) {
                if (modelFaces[i].faceType != TOP_FACE && modelFaces[i].faceType != BOTTOM_FACE) {
                    renderOpaqueFace(chunk, world, Block.grassBlockLower.ID, index, face, modelFaces[i], 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                }
            }
        }
    }

    public void renderGrassBlockWithClay(Chunk chunk, World world, short block, int index, int face, int[] greedyMeshSize) {
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        for (int i = 0; i < modelFaces.length; i++) {
            renderOpaqueFace(chunk, world, block, index, face, modelFaces[i], 0,0,0,0,0,0,0,0, 3, 1, 2, 0, greedyMeshSize);
            if(modelFaces[i] != null) {
                if (modelFaces[i].faceType != TOP_FACE && modelFaces[i].faceType != BOTTOM_FACE) {
                    renderOpaqueFace(chunk, world, Block.grassBlockWithClayLower.ID, index, face, modelFaces[i], 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                }
            }
        }
    }

    public void renderPitKiln(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        if(face == TOP_FACE){
            renderOpaqueFace(chunk, world, block, index, face, Block.list[block].blockModel.copyModel().translateModel(0, -1 + (0.109375f * ((BlockPitKilnUnlit)Block.list[block]).getStrawHeight()), 0).getModelFace(face) , 0,0,0,0,0,0,0,0, 3, 1, 2, 0, new int[2]);
        } else {
            renderOpaqueFace(chunk, world, block, index, face, Block.list[block].blockModel.getModelFace(face), 0,0,0,0,0,0,0,0, 3, 1, 2, 0, new int[2]);
        }

        int numberOfLogs = ((BlockPitKilnUnlit)Block.list[block]).getNumberOfLogs();
        block = Block.largeFireWoodBlock.ID;
        ModelLoader baseModel = Block.largeFireWood;
        ModelFace modelFace;
        switch (numberOfLogs){
            case 4:
                modelFace = baseModel.copyModel().translateModel(0.5f, 1, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 3:
                modelFace = baseModel.copyModel().translateModel(0.5f, 1, 0.625f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 2:
                modelFace = baseModel.copyModel().translateModel(0.5f, 1, 0.375f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 1:
                modelFace = baseModel.copyModel().translateModel(0.5f, 1, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
        }

        ChestLocation chest = chunk.getChestLocation(chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index));
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

        int numberOfLogs = chunk.getChestLocation(index).inventory.itemStacks[0].count / 2;

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
        ModelLoader baseModel = Block.largeFireWood;
        ModelFace modelFace;
        switch (numberOfLogs){
            case 16:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.875f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 15:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.875f, 0.625f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 14:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.875f, 0.375f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 13:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.875f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 12:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.625f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 11:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.625f, 0.625f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 10:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.625f, 0.375f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 9:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.625f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 8:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.375f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 7:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.375f, 0.625f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 6:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.375f, 0.375f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 5:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.375f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 4:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.125f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 3:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.125f, 0.625f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 2:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.125f, 0.375f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
            case 1:
                modelFace = baseModel.copyModel().translateModel(0.5f, 0.125f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
        }
    }


    public void renderBrickPile(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ChestLocation chestLocation = chunk.getChestLocation(index);
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

        ModelLoader baseModel = Block.brick;
        ModelFace modelFace = baseModel.getModelFace(face);
        float[] UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFace), this.getFaceHeight(modelFace)) : autoUVNSEW(this.getFaceWidth(modelFace), this.getFaceHeight(modelFace));;

        switch (numberOfBricks){ //alternate pushing the stacks inwards by 1 voxel to give depth to the stack
            case 48:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.9375f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 47:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.9375f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 46:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.9375f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 45:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.9375f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 44:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.9375f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 43:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.9375f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 42:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.8125f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 41:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.8125f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 40:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.8125f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 39:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.8125f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 38:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.8125f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 37:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.8125f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 36:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.6875f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 35:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.6875f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 34:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.6875f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 33:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.6875f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 32:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.6875f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 31:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.6875f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 30:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.5625f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 29:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.5625f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 28:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.5625f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 27:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.5625f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 26:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.5625f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 25:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.5625f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 24:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.4375f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 23:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.4375f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 22:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.4375f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 21:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.4375f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 20:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.4375f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 19:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.4375f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 18:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.3125f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 17:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.3125f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 16:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.3125f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 15:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.3125f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 14:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.3125f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 13:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.3125f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 12:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.1875f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 11:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.1875f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 10:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.1875f, 0.75f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 9:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.15625f, 0.1875f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 8:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.5f, 0.1875f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 7:
                modelFace = baseModel.copyModel().rotateModel(90,0, 1, 0).translateModel(0.84375f, 0.1875f, 0.25f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);

            case 6:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.0625f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 5:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.0625f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 4:
                modelFace = baseModel.copyModel().translateModel(0.78125f, 0.0625f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 3:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.0625f, 0.875f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 2:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.0625f, 0.5f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
            case 1:
                modelFace = baseModel.copyModel().translateModel(0.21875f, 0.0625f, 0.125f).getModelFace(face);
                renderOpaqueFace(chunk, world, block, index, face, modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, new int[2]);
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

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        float[] UVSamples;
        for (int i = 0; i < modelFaces.length; i++) {
            UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i])) : autoUVNSEW(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i]));
            if(modelFaces[i] == null)return;
            ModelFace modelFace = modelFaces[i].getScaledFace(1f);
            modelFace.normal.rotateY(rotation);
            for(int j = 0; j <  modelFaces[i].vertices.length; j++){
                modelFace.vertices[j].rotateY(rotation);
            }
            for(int j = 0; j <  modelFace.vertices.length; j++){
                modelFace.vertices[j].add(offset);
            }
            renderOpaqueFace(chunk, world, block, index, face,  modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, greedyMeshSize);

            if(secondRockHasHitTheTowers){
                modelFace = modelFaces[i].getScaledFace(0.5f);
                UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFace), this.getFaceHeight(modelFace)) : autoUVNSEW(this.getFaceWidth(modelFace), this.getFaceHeight(modelFace));
                modelFace.normal.rotateY(secondRockRotation);
                for(int j = 0; j < modelFace.vertices.length; j++){
                    modelFace.vertices[j].rotateY(secondRockRotation);
                }
                for(int j = 0; j <  modelFace.vertices.length; j++){
                    modelFace.vertices[j].add(secondOffset);
                }
                renderOpaqueFace(chunk, world, block, index, face,  modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, greedyMeshSize);
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

        ModelFace[] modelFaces = Block.list[block].blockModel.getModelFaceOfType(face);
        float[] UVSamples;
        for (int i = 0; i < modelFaces.length; i++) {
            UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i])) : autoUVNSEW(this.getFaceWidth(modelFaces[i]), this.getFaceHeight(modelFaces[i]));
            if(modelFaces[i] == null)return;
            ModelFace modelFace = modelFaces[i].getScaledFace(stickScale);
            modelFace.normal.rotateY(rotation);
            for(int j = 0; j <  modelFaces[i].vertices.length; j++){
                modelFace.vertices[j].rotateY(rotation);
            }
            for(int j = 0; j <  modelFace.vertices.length; j++){
                modelFace.vertices[j].add(offset);
            }
            renderOpaqueFace(chunk, world, block, index, face,  modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, greedyMeshSize);

            if(secondStick){
                modelFace = modelFaces[i].getScaledFace(secondStickScale);
                UVSamples = face == TOP_FACE || face == BOTTOM_FACE ? autoUVTopBottom(this.getFaceWidth(modelFace), this.getFaceHeight(modelFace)) : autoUVNSEW(this.getFaceWidth(modelFace), this.getFaceHeight(modelFace));
                modelFace.normal.rotateY(secondStickRotation);
                for(int j = 0; j < modelFace.vertices.length; j++){
                    modelFace.vertices[j].rotateY(secondStickRotation);
                }
                for(int j = 0; j <  modelFace.vertices.length; j++){
                    modelFace.vertices[j].add(secondOffset);
                }
                renderOpaqueFace(chunk, world, block, index, face,  modelFace, UVSamples[0], UVSamples[1], UVSamples[2], UVSamples[3], UVSamples[4], UVSamples[5], UVSamples[6], UVSamples[7], 3, 1, 2, 0, greedyMeshSize);
            }
        }
    }


    public void renderBerryBush(Chunk chunk, World world, short block, int index, int face){
        this.redReset = 1f;
        this.greenReset = 1f;
        this.blueReset = 1f;
        this.skyLightReset = 1f;

        ModelFace modelFace = Block.list[Block.berryBushNoBerries.ID].blockModel.getModelFace(face);
        this.renderTransparentFace(chunk, world, Block.berryBushNoBerries.ID, index, face, modelFace, 0,0,0,0,0,0,0,0,3,1,2,0, new int[2]);

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
        if(block == Block.berryBush.ID){
            modelFace = modelFace.translateFace(translation.x, translation.y, translation.z);
            this.renderTransparentFace(chunk, world, Block.berryBush.ID, index, face, modelFace, 0,0,0,0,0,0,0,0,3,1,2,0, new int[2]);
        } else if(block == Block.berryBushFlower.ID){
            modelFace = modelFace.translateFace(translation.x, translation.y, translation.z);
            this.renderTransparentFace(chunk, world, Block.berryBushFlower.ID, index, face, modelFace, 0,0,0,0,0,0,0,0,3,1,2,0, new int[2]);
        }
    }

    public static int getFaceHeight(ModelFace modelFace){
        if(modelFace == null)return 0;

        return switch (modelFace.faceType){
            case TOP_FACE, BOTTOM_FACE -> modelFace.getFaceWidthZ();
            case NORTH_FACE, SOUTH_FACE, EAST_FACE, WEST_FACE -> modelFace.getFaceWidthY();
            default -> throw new IllegalStateException("Unexpected value: " + modelFace.faceType);
        };
    }

    public static int getFaceWidth(ModelFace modelFace){
        if(modelFace == null)return 0;

        return switch (modelFace.faceType){
            case TOP_FACE, BOTTOM_FACE, EAST_FACE, WEST_FACE -> modelFace.getFaceWidthX();
            case NORTH_FACE, SOUTH_FACE -> modelFace.getFaceWidthZ();
            default -> throw new IllegalStateException("Unexpected value: " + modelFace.faceType);
        };

    }

    public void renderItemBlock(Chunk chunk, World world, short block, int index, int face){
        if(face == BOTTOM_FACE)return;
        int[] pixels = new int[1024];
        String filepath = "src/spacegame/assets/textures/item/" +
                RenderEngine.getBlockName(chunk.getChestLocation(index).inventory.itemStacks[0].item.textureID, "src/spacegame/assets/textures/item/") + ".png";
        BufferedImage image = null;
        ModelLoader baseModel = Block.itemVoxelModel;

        try {
            image = ImageIO.read(new File(filepath));
            BufferedImage argbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = argbImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = argbImage;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        image.getRGB(0, 0, 32, 32, pixels, 0, 32);

        float xShift;
        float zShift;
        final float voxelShift = 0.03125f;
        for(int i = 0; i < pixels.length; i++){
            if((pixels[i] >> 24 & 255) != 255)continue;

            if(i < 992 && face == WEST_FACE) {
                if ((pixels[i + 32] >> 24 & 255) == 255) continue;
            }

            if(i > 31 && face == EAST_FACE) {
                if ((pixels[i - 32] >> 24 & 255) == 255) continue;
            }

            if(i < 1023 && (i % 32 != 31) && face == SOUTH_FACE){
                if((pixels[i + 1] >> 24 & 255) == 255)continue;
            }

            if(i > 0 && (i % 32) != 0 && face == NORTH_FACE){
                 if((pixels[i - 1] >> 24 & 255) == 255)continue;
            }

            this.redReset = MathUtil.intToFloatRGBA(pixels[i] >> 16 & 255);
            this.greenReset = MathUtil.intToFloatRGBA(pixels[i] >> 8 & 255);
            this.blueReset = MathUtil.intToFloatRGBA(pixels[i] & 255);
            this.skyLightReset = Math.max(Math.max(this.redReset, this.greenReset), this.blueReset);

            xShift = voxelShift * (i % 32);
            zShift = voxelShift * (i >> 5);

            renderOpaqueFace(chunk, world, block, index, face, baseModel.copyModel().translateModel(xShift, 0, zShift).getModelFace(face), 0,0,0,0,0,0,0,0, 3, 1, 2, 0, new int[2]);
        }
    }


    public void renderBerryBushGrowing(Chunk chunk, World world, short block, int index, int face){

        ModelLoader baseModel = Block.list[block].blockModel.copyModel();

        float scaleFactor = ((BlockBerryBushGrowing)Block.list[block]).getBerryBushScale();

        float translateVal = ((BlockBerryBushGrowing)Block.list[block]).getBerryBushTranslation();

        ModelFace modelFace = baseModel.getScaledModel(scaleFactor).translateModel(translateVal, 0, translateVal).getModelFace(face);


        renderTransparentFace(chunk, world, block, index, face, modelFace, 0,0,0,0,0,0,0,0, 3,1,2, 0, new int[2]);
    }

    public void renderReedGrowing(Chunk chunk, World world, short block, int index, int face){

        ModelLoader baseModel = Block.list[block].blockModel.copyModel();

        float scaleFactor = ((BlockReedGrowing)Block.list[block]).getReedGrowthScale();

        float translateVal = ((BlockReedGrowing)Block.list[block]).getReedTranslation();

        ModelFace modelFace = baseModel.getScaledModel(scaleFactor).translateModel(translateVal, 0, translateVal).getModelFace(face);

        this.handleWaterLoggedBlocks(chunk, world, block, index, face);

        renderOpaqueFace(chunk, world, block, index, face, modelFace, 0,0,0,0,0,0,0,0, 3,1,2, 0, new int[2]);
    }




    public static float[] autoUVNSEW(int width, int height){
        float[] UVSamples = new float[8]; //Order of x and y sample for corners 1 - 4, this will be centered on the actual texture image
        final float pixelWidth = 0.03125f;
        int pixelsFromLeftSide;
        int pixelsFromRightSide;
        int pixelsFromTopSide;
        int pixelsFromBottomSide;

        width = 32 - width;
        height = 32 - height;

        pixelsFromLeftSide = (width & 1) == 0 ? width >> 1 : (width >> 1) + 1;
        pixelsFromRightSide = width >> 1;

        pixelsFromTopSide = (height & 1) == 0 ? height >> 1 : (height >> 1) + 1;
        pixelsFromBottomSide = height >> 1;

        UVSamples[0] = pixelsFromLeftSide * pixelWidth;
        UVSamples[1] = pixelsFromBottomSide * -pixelWidth;

        UVSamples[2] = pixelsFromRightSide * -pixelWidth;
        UVSamples[3] = pixelsFromTopSide * pixelWidth;

        UVSamples[4] = pixelsFromLeftSide * pixelWidth;
        UVSamples[5] = pixelsFromTopSide * pixelWidth;

        UVSamples[6] = pixelsFromRightSide * -pixelWidth;
        UVSamples[7] = pixelsFromBottomSide * -pixelWidth;

        return UVSamples;
    }

    public static float[] autoUVTopBottom(int width, int height){
        float[] UVSamples = new float[8]; //Order of x and y sample for corners 1 - 4, this will be centered on the actual texture image
        final float pixelWidth = 0.03125f;
        int pixelsFromLeftSide;
        int pixelsFromRightSide;
        int pixelsFromTopSide;
        int pixelsFromBottomSide;

        width = 32 - width;
        height = 32 - height;

        pixelsFromLeftSide = (width & 1) == 0 ? width >> 1 : (width >> 1) + 1;
        pixelsFromRightSide = width >> 1;

        pixelsFromTopSide = (height & 1) == 0 ? height >> 1 : (height >> 1) + 1;
        pixelsFromBottomSide = height >> 1;

        UVSamples[0] = pixelsFromRightSide * -pixelWidth;
        UVSamples[1] = pixelsFromTopSide * pixelWidth;

        UVSamples[2] = pixelsFromLeftSide * pixelWidth;
        UVSamples[3] = pixelsFromBottomSide * -pixelWidth;

        UVSamples[4] = pixelsFromRightSide * -pixelWidth;
        UVSamples[5] = pixelsFromBottomSide * -pixelWidth;

        UVSamples[6] = pixelsFromLeftSide * pixelWidth;
        UVSamples[7] = pixelsFromTopSide * pixelWidth;

        return UVSamples;
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

    private void setPlantColorValues(int x, int y, int z, World world, short blockID){
        if(blockID == Block.itemBlock.ID){
            float highestChannel = 0;
            highestChannel = Math.max(this.red, this.green);
            highestChannel = Math.max(highestChannel, this.blue);

            this.highestChannel = highestChannel != 0.0 ? highestChannel : 0.01f;
        }
        if(!Block.list[blockID].colorize)return;


        int color = blockID == Block.grass.ID  || blockID == Block.tallGrass.ID || blockID == Block.grassWithClay.ID ? PlantColorizer.getGrassColor(world.getTemperature(x,y,z), world.getRainfall(x,z)) : PlantColorizer.getOakLeafColor(world.getTemperature(x,y,z), world.getRainfall(x,z));
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

    private  void setLight(float x, float y, float z, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, int index, int face, Chunk chunk, World world, int[] greedyMeshSize, short blockID) {
        int xBlock = chunk.getBlockXFromIndex(index);
        int yBlock = chunk.getBlockYFromIndex(index);
        int zBlock = chunk.getBlockZFromIndex(index);

        this.setPlantColorValues(xBlock, yBlock, zBlock, world, blockID);

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
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock - 1), world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock - 1), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1), world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock + 1), world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock + 1), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMin) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1), world.getBlockLightColor(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1), world.getBlockLightColor(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock), world.getBlockLightValue(xBlock, yBlock - 1, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock), world.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMin) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock + 1, zBlock - 1), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock - 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock - 1), world.getBlockLightColor(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock - 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock - 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock - 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock, yBlock + 1, zBlock + 1), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock + 1), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock + 1), world.getBlockLightColor(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock + 1), world.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(world.getBlockLightValue(xBlock + 1, yBlock, zBlock), world.getBlockLightValue(xBlock, yBlock, zBlock), world.getBlockLightValue(xBlock + 1, yBlock + 1, zBlock), world.getBlockLightValue(xBlock, yBlock + 1, zBlock), world.getBlockLightColor(xBlock + 1, yBlock, zBlock), world.getBlockLightColor(xBlock, yBlock, zBlock), world.getBlockLightColor(xBlock + 1, yBlock + 1, zBlock), world.getBlockLightColor(xBlock, yBlock + 1, zBlock));
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


    private float compressTextureCoordinates(float x, float y){
        int xAsIntLessThanOne = 0;
        int yAsIntLessThanOne = 0;
        int xAsIntGreaterThanOne = 0;
        int yAsIntGreaterThanOne = 0;

        if(x <= 1.0){
            xAsIntLessThanOne = (int)(x * 32f);
        }

        if(y <= 1.0){
            yAsIntLessThanOne = (int)(y * 32f);
        }

        if(x > 1.0){
            xAsIntGreaterThanOne = (int)x;
        }

        if(y > 1.0){
            yAsIntGreaterThanOne = (int)y;
        }

        int combinedInt = (((this.grayScaleImageMultiplier & 255) << 24) | (xAsIntLessThanOne << 18) | (xAsIntGreaterThanOne << 12) | (yAsIntLessThanOne << 6) | yAsIntGreaterThanOne);
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


    private void renderOpaqueFace(Chunk chunk, World world, short block, int index, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, int[] greedyMeshSize) {
        if(blockFace == null)return;
        int x = (index % 32);
        int y = (index >> 10);
        int z = ((index % 1024) >> 5);

        float blockTextureID = getBlockTextureID(block, face);

        ModelLoader modelLoader = Block.list[block].blockModel;

        Vector3f minVertex = new Vector3f(x,y,z);
        Vector3f maxVertex = new Vector3f(x + 1, y + 1, z + 1);
        Vector3f blockPosition = new Vector3f(x, y, z);

        if(block >= Block.oakLogFullSizeNormal.ID && block <= Block.oakLogSize1EastWest.ID){
            int faceDirectionCurrentLog = BlockLog.facingDirectionOfLog(block);
            int sizeOfCurrentLog = BlockLog.sizeOfLog(block);
            final int xBlock = chunk.getBlockXFromIndex(index);
            final int yBlock = chunk.getBlockYFromIndex(index);
            final int zBlock = chunk.getBlockZFromIndex(index);
            if(world.getBlockID(xBlock + 1, yBlock, zBlock) >= Block.oakLogFullSizeNormal.ID && world.getBlockID(xBlock + 1, yBlock, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(world.getBlockID(xBlock + 1, yBlock, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(world.getBlockID(xBlock + 1, yBlock, zBlock)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendSouthFace();
            } else if(world.getBlockID(xBlock - 1, yBlock, zBlock) >= Block.oakLogFullSizeNormal.ID && world.getBlockID(xBlock - 1, yBlock, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(world.getBlockID(xBlock - 1, yBlock, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(world.getBlockID(xBlock - 1, yBlock, zBlock)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendNorthFace();
            } else if(world.getBlockID(xBlock , yBlock, zBlock + 1) >= Block.oakLogFullSizeNormal.ID && world.getBlockID(xBlock, yBlock, zBlock + 1) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(world.getBlockID(xBlock, yBlock, zBlock + 1)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(world.getBlockID(xBlock, yBlock, zBlock + 1)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendWestFace();
            } else if(world.getBlockID(xBlock , yBlock, zBlock - 1) >= Block.oakLogFullSizeNormal.ID && world.getBlockID(xBlock, yBlock, zBlock - 1) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(world.getBlockID(xBlock, yBlock, zBlock - 1)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(world.getBlockID(xBlock, yBlock, zBlock - 1)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendEastFace();
            } else if(world.getBlockID(xBlock , yBlock + 1, zBlock) >= Block.oakLogFullSizeNormal.ID && world.getBlockID(xBlock, yBlock + 1, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(world.getBlockID(xBlock, yBlock + 1, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(world.getBlockID(xBlock, yBlock + 1, zBlock)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendTopFace();
            } else if(world.getBlockID(xBlock , yBlock - 1, zBlock) >= Block.oakLogFullSizeNormal.ID && world.getBlockID(xBlock, yBlock - 1, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(world.getBlockID(xBlock, yBlock - 1, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(world.getBlockID(xBlock, yBlock - 1, zBlock)) != faceDirectionCurrentLog) {
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

        Vector3f vertex1 = new Vector3f(blockFace.vertices[0].x, blockFace.vertices[0].y, blockFace.vertices[0].z).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1].x, blockFace.vertices[1].y, blockFace.vertices[1].z).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2].x, blockFace.vertices[2].y, blockFace.vertices[2].z).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3].x, blockFace.vertices[3].y, blockFace.vertices[3].z).add(blockPosition);
        Vector3f normal = new Vector3f(blockFace.normal.x, blockFace.normal.y, blockFace.normal.z);


        switch (blockFace.faceType) {
            case TOP_FACE, TOP_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
              //  if(block == Block.itemBlock.ID){
              //      System.out.println(this.grayScaleImageMultiplier);
              //  }
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample1, 0 + ySample1));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex1.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex2.x, vertex2.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample2, 1 + greedyMeshSize[1] + ySample2));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex2.z + greedyMeshSize[1], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample3, 1 + greedyMeshSize[1] + ySample3));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample4, 0 + ySample4));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex4.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

            }

            case BOTTOM_FACE, BOTTOM_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample1, 0 + ySample1));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex1.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 1 + greedyMeshSize[1] + ySample2));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex2.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex3.x, vertex3.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample3, 1 + greedyMeshSize[1] + ySample3));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 0 + ySample4));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex4.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }
            case NORTH_FACE, NORTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex1.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex2.z + greedyMeshSize[0], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex3.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex4.z + greedyMeshSize[0], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }

            case SOUTH_FACE, SOUTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex1.z + greedyMeshSize[0], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex2.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex3.z + greedyMeshSize[0], blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex4.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }

            case EAST_FACE, EAST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex1.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex2.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex3.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex4.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

            }

            case WEST_FACE, WEST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex1.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex2.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex3.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatOpaque(chunk, this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                this.addVertexFloatOpaque(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatOpaque(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatOpaque(chunk, this.compressPosZAndTexId(vertex4.z, blockTextureID));
                this.addVertexFloatOpaque(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatOpaque(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

            }
        }
        resetLight();
        this.addElementsOpaque(chunk);
    }

    private  void renderTransparentFace(Chunk chunk, World world, short block, int index, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, int[] greedyMeshSize) {
        if(blockFace == null)return;
        int x = (index % 32);
        int y = (index >> 10);
        int z = ((index % 1024) >> 5);

        float blockID = getBlockTextureID(block, face);

        Vector3f blockPosition = new Vector3f(x, y, z);
        Vector3f vertex1 = new Vector3f(blockFace.vertices[0].x, blockFace.vertices[0].y, blockFace.vertices[0].z).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1].x, blockFace.vertices[1].y, blockFace.vertices[1].z).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2].x, blockFace.vertices[2].y, blockFace.vertices[2].z).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3].x, blockFace.vertices[3].y, blockFace.vertices[3].z).add(blockPosition);
        Vector3f normal = new Vector3f(blockFace.normal.x, blockFace.normal.y, blockFace.normal.z);

        switch (blockFace.faceType) {
            case TOP_FACE, TOP_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample1, 0 + ySample1));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex1.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex2.x, vertex2.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample2, 1 + greedyMeshSize[1] + ySample2));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex2.z + greedyMeshSize[1], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample3, 1 + greedyMeshSize[1] + ySample3));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample4, 0 + ySample4));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex4.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }

            case BOTTOM_FACE, BOTTOM_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample1, 0 + ySample1));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex1.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 1 + greedyMeshSize[1] + ySample2));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex2.z + greedyMeshSize[1], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex3.x, vertex3.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample3, 1 + greedyMeshSize[1] + ySample3));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 0 + ySample4));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex4.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }
            case NORTH_FACE, NORTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex1.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex2.z + greedyMeshSize[0], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex3.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex4.z + greedyMeshSize[0], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }

            case SOUTH_FACE, SOUTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex1.z + greedyMeshSize[0], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex2.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex3.z + greedyMeshSize[0], blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex4.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }

            case EAST_FACE, EAST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex1.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex2.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex3.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex4.x, vertex4.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex4.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }

            case WEST_FACE, WEST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex1.x, vertex1.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex1.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
               this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y + greedyMeshSize[1]));
               this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
               this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2));
               this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex2.z, blockID));
               this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
               this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(0 + xSample3, 0 + ySample3));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex3.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, world, greedyMeshSize, block);
                this.addVertexFloatTransparent(chunk, this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                this.addVertexFloatTransparent(chunk, this.compressColor(red, green, blue));
                this.addVertexFloatTransparent(chunk, this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4));
                this.addVertexFloatTransparent(chunk, this.compressPosZAndTexId(vertex4.z, blockID));
                this.addVertexFloatTransparent(chunk, this.compressNormalXY(normal.x, normal.y));
                this.addVertexFloatTransparent(chunk, this.compressNormalZAndSkyLightValue(normal.z, this.skyLightValue));
            }
        }
        resetLight();
        this.addElementsTransparent(chunk);
    }

    private void addElementsOpaque(Chunk chunk) {
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 2);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 1);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 0);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 0);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 1);
        this.addElementOpaque(chunk, chunk.elementOffsetOpaque + 3);
        chunk.elementOffsetOpaque += 4;
    }

    private void addElementsTransparent(Chunk chunk) {
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 2);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 1);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 0);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 0);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 1);
        this.addElementTransparent(chunk, chunk.elementOffsetTransparent + 3);
        chunk.elementOffsetTransparent += 4;
    }

    public void addVertexFloatOpaque(Chunk chunk, float value){
        if(chunk.vertexBufferOpaque.position() == chunk.vertexBufferOpaque.limit()){
            FloatBuffer oldBuffer = chunk.vertexBufferOpaque;
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.vertexBufferOpaque = newBuffer;
        }
        chunk.vertexBufferOpaque.put(value);
    }

    public void addVertexFloatTransparent(Chunk chunk, float value){
        if(chunk.vertexBufferTransparent.position() == chunk.vertexBufferTransparent.limit()){
            FloatBuffer oldBuffer = chunk.vertexBufferTransparent;
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.vertexBufferTransparent = newBuffer;
        }
        chunk.vertexBufferTransparent.put(value);
    }

    public void addElementOpaque(Chunk chunk, int value){
        if(chunk.elementBufferOpaque.position() == chunk.elementBufferOpaque.limit()){
            IntBuffer oldBuffer = chunk.elementBufferOpaque;
            IntBuffer newBuffer = BufferUtils.createIntBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.elementBufferOpaque = newBuffer;
        }
        chunk.elementBufferOpaque.put(value);
    }

    public void addElementTransparent(Chunk chunk, int value){
        if(chunk.elementBufferTransparent.position() == chunk.elementBufferTransparent.limit()){
            IntBuffer oldBuffer = chunk.elementBufferTransparent;
            IntBuffer newBuffer = BufferUtils.createIntBuffer(oldBuffer.capacity() * 2);
            oldBuffer.flip(); // prepare for reading
            newBuffer.put(oldBuffer);
            chunk.elementBufferTransparent = newBuffer;
        }
        chunk.elementBufferTransparent.put(value);
    }


    public static float getBlockTextureID(short block, int face) {
        return Block.list[block].getBlockTexture(block, face);
    }

}
