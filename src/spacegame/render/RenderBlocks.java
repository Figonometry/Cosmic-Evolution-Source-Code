package spacegame.render;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.world.Chunk;
import spacegame.world.WorldFace;

public class RenderBlocks {
    public float red = 1.0F;
    public float green = 1.0F;
    public float blue = 1.0F;
    public float alpha = 1.0F;
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


    public  void renderStandardBlock(Chunk chunk, WorldFace worldFace, short block, int index, int face, int[] greedyMeshSize) {
        switch (face) {
            case TOP_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == TOP_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case BOTTOM_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == BOTTOM_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case NORTH_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case SOUTH_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case EAST_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case WEST_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
        }
    }

    public  void renderTransparentBlock(Chunk chunk, WorldFace worldFace, short block, int index, int face, int[] greedyMeshSize) {
        switch (face) {
            case TOP_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == TOP_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderTransparentFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case BOTTOM_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == BOTTOM_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderTransparentFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case NORTH_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderTransparentFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case SOUTH_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderTransparentFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case EAST_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderTransparentFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
            case WEST_FACE -> {
                for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                    if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE) {
                        ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                        renderTransparentFace(chunk, worldFace, block, index, face, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, greedyMeshSize);
                    }
                }
            }
        }
    }

    public  void renderTorch(Chunk chunk, WorldFace worldFace, short block, int index, int face) {
        switch (block) {
            case 2 -> {
                switch (face) {
                    case TOP_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == TOP_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, -0.875F, 0.875F, 0, 0, -0.875F, 0, 0, 0.875F, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case NORTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case SOUTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case EAST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case WEST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
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
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, -0.875F, 0.875F, 0, 0, -0.875F, 0, 0, 0.875F, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case NORTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case SOUTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case EAST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case WEST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
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
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, -0.875F, 0.875F, 0, 0, -0.875F, 0, 0, 0.875F, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case NORTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == NORTH_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case SOUTH_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == SOUTH_FACE_UNSORTED) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case EAST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == EAST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                    case WEST_FACE -> {
                        for (int i = 0; i < Block.list[block].blockModel.modelFaces.length; i++) {
                            if (Block.list[block].blockModel.modelFaces[i].faceType == WEST_FACE) {
                                ModelFace modelFace = Block.list[block].blockModel.modelFaces[i];
                                renderOpaqueFace(chunk, worldFace, block, index, face, modelFace, 0.4375F, 0, -0.4375F, 0.1875F, 0.4375F, 0.1875F, -0.4375F, 0, 3, 1, 2, 0, new int[2]);
                            }
                        }
                    }
                }
            }
        }
    }

    public void renderCampFire(Chunk chunk, WorldFace worldFace, short block, int index, int face){
        float scaleReduction = 0.25f * (Block.list[block].lightBlockValue / 14f);
        ModelLoader shrunkBlock = Block.fireBlockModel.getScaledModel(scaleReduction).translateModel(0.5f - scaleReduction/2f,0f,0.5f - scaleReduction/2f);
        this.renderTransparentFace(chunk, worldFace, block, index, face, Block.list[block].blockModel.modelFaces[0], 0,0,0,0,0,0,0,0, 3,1,2,0, new int[2]);
        for(int i = 0; i < shrunkBlock.modelFaces.length; i++) {
            if (shrunkBlock.modelFaces[i].faceType != TOP_FACE && shrunkBlock.modelFaces[i].faceType != BOTTOM_FACE)
                this.renderTransparentFace(chunk, worldFace, Block.fire.ID, index, face, shrunkBlock.modelFaces[i], 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, new int[2]);
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

    private synchronized void setLight(float x, float y, float z, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, int index, int face, Chunk chunk, WorldFace worldFace, int[] greedyMeshSize) {
        int xBlock = chunk.getBlockXFromIndex(index);
        int yBlock = chunk.getBlockYFromIndex(index);
        int zBlock = chunk.getBlockZFromIndex(index);

        if (x == xMin && y == yMin && z == zMin) {
            switch (face) {
                case BOTTOM_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock - 1, zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock - 1, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
                    }
                }
                case NORTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock - 1, zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock - 1, yBlock, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock));
                    }
                }
                case EAST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock, zBlock - 1)].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock - 1, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1));
                    }
                }
            }
        } else if (x == xMax && y == yMin && z == zMin) {
            switch (face) {
                case BOTTOM_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock));
                    }
                }
                case SOUTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1, yBlock - 1, zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock + 1, yBlock, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock));
                    }
                }
                case EAST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock - 1)].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock - 1));
                    }
                }
            }
        } else if (x == xMin && y == yMax && z == zMin) {
            switch (face) {
                case TOP_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock + 1, zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock + 1, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
                    }
                }
                case NORTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock));
                    }
                }
                case EAST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1)].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock - 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock - 1));
                    }
                }
            }
        } else if (x == xMin && y == yMin && z == zMax) {
            switch (face) {
                case BOTTOM_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[1])].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock - 1, zBlock + 1 + greedyMeshSize[1])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + greedyMeshSize[1]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + greedyMeshSize[1]));
                    }
                }
                case NORTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[0])].isSolid && Block.list[worldFace.getBlockID(xBlock - 1, yBlock, zBlock + 1 + greedyMeshSize[0])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[0]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + greedyMeshSize[0]));
                    }
                }
                case WEST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock, zBlock + 1)].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock - 1, zBlock + 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1));
                    }
                }
            }
        } else if (x == xMax && y == yMax && z == zMin) {
            switch (face) {
                case TOP_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock + 1, zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock));
                    }
                }
                case SOUTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock)].isSolid && Block.list[worldFace.getBlockID(xBlock + 1, yBlock + greedyMeshSize[1], zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock));
                    }
                }
                case EAST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1)].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock - 1));
                    }
                }
            }
        } else if (x == xMin && y == yMax && z == zMax) {
            switch (face) {
                case TOP_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock + 1, zBlock + greedyMeshSize[1])].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock + 1, zBlock + 1 + greedyMeshSize[1])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock + greedyMeshSize[1]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock + greedyMeshSize[1]));
                    }
                }
                case NORTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0])].isSolid && Block.list[worldFace.getBlockID(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]));
                    }
                }
                case WEST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1)].isSolid && Block.list[worldFace.getBlockID(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock + 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock + 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1 + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock + 1 + greedyMeshSize[1], zBlock + 1));
                    }
                }
            }
        } else if (x == xMax && y == yMin && z == zMax) {
            switch (face) {
                case BOTTOM_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1])].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + greedyMeshSize[1]));
                    }
                }
                case SOUTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1, yBlock - 1, zBlock + greedyMeshSize[0])].isSolid && Block.list[worldFace.getBlockID(xBlock + 1, yBlock, zBlock + 1 + greedyMeshSize[0])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock + greedyMeshSize[0]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock + greedyMeshSize[0]));
                    }
                }
                case WEST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock + 1)].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock - 1, zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock - 1, zBlock + 1));
                    }
                }
            }
        } else if (x == xMax && y == yMax && z == zMax) {
            switch (face) {
                case TOP_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1])].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + 1 + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1, zBlock + greedyMeshSize[1]));
                    }
                }
                case SOUTH_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0])].isSolid && Block.list[worldFace.getBlockID(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0])].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock + greedyMeshSize[1], zBlock + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + 1 + greedyMeshSize[0]), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1 + greedyMeshSize[1], zBlock + greedyMeshSize[0]));
                    }
                }
                case WEST_FACE -> {
                    if (Block.list[worldFace.getBlockID(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1)].isSolid && Block.list[worldFace.getBlockID(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1)].isSolid) {
                        setVertexLight3Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1));
                    } else {
                        setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock + 1 + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightValue(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock + 1 + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1), worldFace.getBlockLightColor(xBlock + greedyMeshSize[0], yBlock + 1 + greedyMeshSize[1], zBlock + 1));
                    }
                }
            }
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight1Arg(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else {
            setVertexLight1Arg(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), x, y, z, worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        }
    }

    private  void resetLight() {
        red = 1F;
        green = 1F;
        blue = 1F;
    }

    private  void setVertexLight1Arg(byte light, float x, float y, float z, float[] lightColor) {
        float finalLight = getLightValueFromMap(light);

        red = lightColor[0];
        green = lightColor[1];
        blue = lightColor[2];

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }

    private  void setVertexLight2Args(byte light1, byte light2, float x, float y, float z, float[] lightColor, float[] lightColor2) {
        float light1Float = getLightValueFromMap(light1);
        float light2Float = getLightValueFromMap(light2);

        float lightTotal = light1Float + light2Float;
        float finalLight = lightTotal / 2F;

        int divisor = 0;
        if (lightColor[0] != 0F) {
            divisor++;
        }
        if (lightColor2[0] != 0F) {
            divisor++;
        }
        red = (lightColor[0] + lightColor2[0]) / divisor;

        divisor = 0;
        if (lightColor[1] != 0F) {
            divisor++;
        }
        if (lightColor2[1] != 0F) {
            divisor++;
        }
        green = (lightColor[1] + lightColor2[1]) / divisor;

        divisor = 0;
        if (lightColor[2] != 0F) {
            divisor++;
        }
        if (lightColor2[2] != 0F) {
            divisor++;
        }
        blue = (lightColor[2] + lightColor2[2]) / divisor;

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }

    private  void setVertexLight3Args(byte light1, byte light2, byte light3, float x, float y, float z, float[] lightColor, float[] lightColor2, float[] lightColor3) {
        float light1Float = getLightValueFromMap(light1);
        float light2Float = getLightValueFromMap(light2);
        float light3Float = getLightValueFromMap(light3);

        float lightTotal = light1Float + light2Float + light3Float;
        float finalLight = lightTotal / 3F;

        int divisor = 0;
        if (lightColor[0] != 0F) {
            divisor++;
        }
        if (lightColor2[0] != 0F) {
            divisor++;
        }
        if (lightColor3[0] != 0F) {
            divisor++;
        }
        red = (lightColor[0] + lightColor2[0] + lightColor3[0]) / divisor;

        divisor = 0;
        if (lightColor[1] != 0F) {
            divisor++;
        }
        if (lightColor2[1] != 0F) {
            divisor++;
        }
        if (lightColor3[1] != 0F) {
            divisor++;
        }
        green = (lightColor[1] + lightColor2[1] + lightColor3[1]) / divisor;

        divisor = 0;
        if (lightColor[2] != 0F) {
            divisor++;
        }
        if (lightColor2[2] != 0F) {
            divisor++;
        }
        if (lightColor3[2] != 0F) {
            divisor++;
        }
        blue = (lightColor[2] + lightColor2[2] + lightColor3[2]) / divisor;


        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }


    private  void setVertexLight4Args(byte light1, byte light2, byte light3, byte light4, float x, float y, float z, float[] lightColor, float[] lightColor2, float[] lightColor3, float[] lightColor4) {
        float light1Float = getLightValueFromMap(light1);
        float light2Float = getLightValueFromMap(light2);
        float light3Float = getLightValueFromMap(light3);
        float light4Float = getLightValueFromMap(light4);

        float lightTotal = light1Float + light2Float + light3Float + light4Float;
        float finalLight = lightTotal / 4F;

        int divisor = 0;
        if (lightColor[0] != 0F) {
            divisor++;
        }
        if (lightColor2[0] != 0F) {
            divisor++;
        }
        if (lightColor3[0] != 0F) {
            divisor++;
        }
        if (lightColor4[0] != 0F) {
            divisor++;
        }
        red = (lightColor[0] + lightColor2[0] + lightColor3[0] + lightColor4[0]) / divisor;

        divisor = 0;
        if (lightColor[1] != 0F) {
            divisor++;
        }
        if (lightColor2[1] != 0F) {
            divisor++;
        }
        if (lightColor3[1] != 0F) {
            divisor++;
        }
        if (lightColor4[1] != 0F) {
            divisor++;
        }
        green = (lightColor[1] + lightColor2[1] + lightColor3[1] + lightColor4[1]) / divisor;

        divisor = 0;
        if (lightColor[2] != 0F) {
            divisor++;
        }
        if (lightColor2[2] != 0F) {
            divisor++;
        }
        if (lightColor3[2] != 0F) {
            divisor++;
        }
        if (lightColor4[2] != 0F) {
            divisor++;
        }
        blue = (lightColor[2] + lightColor2[2] + lightColor3[2] + lightColor4[2]) / divisor;

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;

    }

    private  void setVertexLight7Args(byte light1, byte light2, byte light3, byte light4, byte light5, byte light6, byte light7, float x, float y, float z, float[] lightColor) {
        float light1Float = getLightValueFromMap(light1);
        float light2Float = getLightValueFromMap(light2);
        float light3Float = getLightValueFromMap(light3);
        float light4Float = getLightValueFromMap(light4);
        float light5Float = getLightValueFromMap(light5);
        float light6Float = getLightValueFromMap(light6);
        float light7Float = getLightValueFromMap(light7);

        float lightTotal = light1Float + light2Float + light3Float + light4Float + light5Float + light6Float + light7Float;
        float finalLight = lightTotal / 7F;

        red = lightColor[0];
        green = lightColor[1];
        blue = lightColor[2];

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }


    private void renderOpaqueFace(Chunk chunk, WorldFace worldFace, short block, int index, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, int[] greedyMeshSize) {
        int x = (index % 32);
        int y = (index / 1024);
        int z = ((index % 1024) / 32);

        float blockID = getBlockTextureID(block, face);

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
            if(worldFace.getBlockID(xBlock + 1, yBlock, zBlock) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(xBlock + 1, yBlock, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(worldFace.getBlockID(xBlock + 1, yBlock, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(worldFace.getBlockID(xBlock + 1, yBlock, zBlock)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendSouthFace();
            } else if(worldFace.getBlockID(xBlock - 1, yBlock, zBlock) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(xBlock - 1, yBlock, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(worldFace.getBlockID(xBlock - 1, yBlock, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(worldFace.getBlockID(xBlock - 1, yBlock, zBlock)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendNorthFace();
            } else if(worldFace.getBlockID(xBlock , yBlock, zBlock + 1) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(xBlock, yBlock, zBlock + 1) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(worldFace.getBlockID(xBlock, yBlock, zBlock + 1)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(worldFace.getBlockID(xBlock, yBlock, zBlock + 1)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendWestFace();
            } else if(worldFace.getBlockID(xBlock , yBlock, zBlock - 1) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(xBlock, yBlock, zBlock - 1) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(worldFace.getBlockID(xBlock, yBlock, zBlock - 1)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(worldFace.getBlockID(xBlock, yBlock, zBlock - 1)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendEastFace();
            } else if(worldFace.getBlockID(xBlock , yBlock + 1, zBlock) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(xBlock, yBlock + 1, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(worldFace.getBlockID(xBlock, yBlock + 1, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(worldFace.getBlockID(xBlock, yBlock + 1, zBlock)) != faceDirectionCurrentLog){
                modelLoader = modelLoader.extendTopFace();
            } else if(worldFace.getBlockID(xBlock , yBlock - 1, zBlock) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(xBlock, yBlock - 1, zBlock) <= Block.oakLogFullSizeNormal.ID && BlockLog.sizeOfLog(worldFace.getBlockID(xBlock, yBlock - 1, zBlock)) > sizeOfCurrentLog && BlockLog.facingDirectionOfLog(worldFace.getBlockID(xBlock, yBlock - 1, zBlock)) != faceDirectionCurrentLog) {
                modelLoader = modelLoader.extendBottomFace();
            }
            for(int i = 0; i < modelLoader.modelFaces.length; i++){
                if(modelLoader.modelFaces[i].faceType == TOP_FACE){
                    maxVertex = new Vector3f(modelLoader.modelFaces[i].vertices[2][0], modelLoader.modelFaces[i].vertices[2][1], modelLoader.modelFaces[i].vertices[2][2]).add(blockPosition);
                }
                if(modelLoader.modelFaces[i].faceType == BOTTOM_FACE){
                    minVertex = new Vector3f(modelLoader.modelFaces[i].vertices[0][0], modelLoader.modelFaces[i].vertices[0][1], modelLoader.modelFaces[i].vertices[0][2]).add(blockPosition);
                }
            }
        }

        switch (blockFace.faceType){
            case TOP_FACE -> {
                for(int i = 0; i < modelLoader.modelFaces.length; i++){
                    if(modelLoader.modelFaces[i].faceType == TOP_FACE){
                        blockFace = modelLoader.modelFaces[i];
                    }
                }
            }
            case BOTTOM_FACE -> {
                for(int i = 0; i < modelLoader.modelFaces.length; i++){
                    if(modelLoader.modelFaces[i].faceType == BOTTOM_FACE){
                        blockFace = modelLoader.modelFaces[i];
                    }
                }
            }
            case NORTH_FACE -> {
                for(int i = 0; i < modelLoader.modelFaces.length; i++){
                    if(modelLoader.modelFaces[i].faceType == NORTH_FACE){
                        blockFace = modelLoader.modelFaces[i];
                    }
                }
            }
            case SOUTH_FACE -> {
                for(int i = 0; i < modelLoader.modelFaces.length; i++){
                    if(modelLoader.modelFaces[i].faceType == SOUTH_FACE){
                        blockFace = modelLoader.modelFaces[i];
                    }
                }
            }
            case EAST_FACE -> {
                for(int i = 0; i < modelLoader.modelFaces.length; i++){
                    if(modelLoader.modelFaces[i].faceType == EAST_FACE){
                        blockFace = modelLoader.modelFaces[i];
                    }
                }
            }
            case WEST_FACE -> {
                for(int i = 0; i < modelLoader.modelFaces.length; i++){
                    if(modelLoader.modelFaces[i].faceType == WEST_FACE){
                        blockFace = modelLoader.modelFaces[i];
                    }
                }
            }
        }

        Vector3f vertex1 = new Vector3f(blockFace.vertices[0][0], blockFace.vertices[0][1], blockFace.vertices[0][2]).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1][0], blockFace.vertices[1][1], blockFace.vertices[1][2]).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2][0], blockFace.vertices[2][1], blockFace.vertices[2][2]).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3][0], blockFace.vertices[3][1], blockFace.vertices[3][2]).add(blockPosition);



        switch (blockFace.faceType) {
            case TOP_FACE, TOP_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (vertex1.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (vertex1.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (vertex1.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (1 + greedyMeshSize[0] + xSample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (0 + ySample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (vertex2.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (vertex2.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (vertex2.z + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 16] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 17] = (0 + xSample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 18] = (1 + greedyMeshSize[1] + ySample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 20] = (vertex3.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 21] = (vertex3.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 22] = (vertex3.z + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 23] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 24] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 25] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 26] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 27] = (1 + greedyMeshSize[0] + xSample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 28] = (1 + greedyMeshSize[1] + ySample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 30] = (vertex4.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 31] = (vertex4.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 32] = (vertex4.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 33] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 34] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 35] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 36] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 37] = (0 + xSample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 38] = (0 + ySample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 39] = (blockID);
                chunk.vertexIndexOpaque += 40;
            }

            case BOTTOM_FACE, BOTTOM_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (vertex1.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (vertex1.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (vertex1.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (0 + xSample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (0 + ySample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (vertex2.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (vertex2.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (vertex2.z + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 16] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 18] = (1 + greedyMeshSize[1] + ySample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 20] = (vertex3.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 21] = (vertex3.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 22] = (vertex3.z + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 23] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 24] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 25] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 26] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 27] = (0 + xSample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 28] = (1 + greedyMeshSize[1] + ySample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 30] = (vertex4.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 31] = (vertex4.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 32] = (vertex4.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 33] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 34] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 35] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 36] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 38] = (0 + ySample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 39] = (blockID);
                chunk.vertexIndexOpaque += 40;
            }
            case NORTH_FACE, NORTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (vertex1.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (vertex1.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (vertex1.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (0 + xSample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (vertex2.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (vertex2.z + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 16] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 18] = (0 + ySample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 20] = (vertex3.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 22] = (vertex3.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 23] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 24] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 25] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 26] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 27] = (0 + xSample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 28] = (0 + ySample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 30] = (vertex4.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 31] = (vertex4.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 32] = (vertex4.z + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 33] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 34] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 35] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 36] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 39] = (blockID);
                chunk.vertexIndexOpaque += 40;
            }

            case SOUTH_FACE, SOUTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (vertex1.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (vertex1.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (vertex1.z + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (0 + xSample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (vertex2.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (vertex2.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 16] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 18] = (0 + ySample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 20] = (vertex3.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 22] = (vertex3.z + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 23] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 24] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 25] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 26] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 27] = (0 + xSample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 28] = (0 + ySample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 30] = (vertex4.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 31] = (vertex4.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 32] = (vertex4.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 33] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 34] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 35] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 36] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 39] = (blockID);
                chunk.vertexIndexOpaque += 40;
            }

            case EAST_FACE, EAST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (vertex1.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (vertex1.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (vertex1.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (0 + xSample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (vertex2.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (vertex2.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 16] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 18] = (0 + ySample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 20] = (vertex3.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 22] = (vertex3.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 23] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 24] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 25] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 26] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 27] = (0 + xSample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 28] = (0 + ySample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 30] = (vertex4.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 31] = (vertex4.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 32] = (vertex4.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 33] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 34] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 35] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 36] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 39] = (blockID);
                chunk.vertexIndexOpaque += 40;
            }

            case WEST_FACE, WEST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (vertex1.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (vertex1.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (vertex1.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (0 + xSample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (vertex2.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (vertex2.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 16] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 18] = (0 + ySample2);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 20] = (vertex3.x);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 22] = (vertex3.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 23] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 24] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 25] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 26] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 27] = (0 + xSample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 28] = (0 + ySample3);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 30] = (vertex4.x + greedyMeshSize[0]);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 31] = (vertex4.y);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 32] = (vertex4.z);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 33] = (red);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 34] = (green);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 35] = (blue);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 36] = (alpha);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 39] = (blockID);
                chunk.vertexIndexOpaque += 40;
            }
        }
        resetLight();
    }

    private  void renderTransparentFace(Chunk chunk, WorldFace worldFace, short block, int index, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, int[] greedyMeshSize) {
        int x = (index % 32);
        int y = (index / 1024);
        int z = ((index % 1024) / 32);

        float blockID = getBlockTextureID(block, face);

        Vector3f blockPosition = new Vector3f(x, y, z);
        Vector3f vertex1 = new Vector3f(blockFace.vertices[0][0], blockFace.vertices[0][1], blockFace.vertices[0][2]).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1][0], blockFace.vertices[1][1], blockFace.vertices[1][2]).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2][0], blockFace.vertices[2][1], blockFace.vertices[2][2]).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3][0], blockFace.vertices[3][1], blockFace.vertices[3][2]).add(blockPosition);

        switch (blockFace.faceType) {
            case TOP_FACE, TOP_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (vertex1.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (vertex1.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (vertex1.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (1 + greedyMeshSize[0] + xSample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (0 + ySample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (vertex2.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (vertex2.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (vertex2.z + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 16] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 17] = (0 + xSample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 18] = (1 + greedyMeshSize[1] + ySample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 20] = (vertex3.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 21] = (vertex3.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 22] = (vertex3.z + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 23] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 24] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 25] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 26] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 27] = (1 + greedyMeshSize[0] + xSample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 28] = (1 + greedyMeshSize[1] + ySample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 30] = (vertex4.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 31] = (vertex4.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 32] = (vertex4.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 33] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 34] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 35] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 36] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 37] = (0 + xSample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 38] = (0 + ySample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 39] = (blockID);
                chunk.vertexIndexTransparent += 40;
            }

            case BOTTOM_FACE, BOTTOM_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (vertex1.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (vertex1.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (vertex1.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (0 + xSample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (0 + ySample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (vertex2.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (vertex2.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (vertex2.z + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 16] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 18] = (1 + greedyMeshSize[1] + ySample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 20] = (vertex3.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 21] = (vertex3.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 22] = (vertex3.z + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 23] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 24] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 25] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 26] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 27] = (0 + xSample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 28] = (1 + greedyMeshSize[1] + ySample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 30] = (vertex4.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 31] = (vertex4.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 32] = (vertex4.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 33] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 34] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 35] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 36] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 38] = (0 + ySample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 39] = (blockID);
                chunk.vertexIndexTransparent += 40;
            }
            case NORTH_FACE, NORTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (vertex1.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (vertex1.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (vertex1.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (0 + xSample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (vertex2.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (vertex2.z + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 16] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 18] = (0 + ySample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 20] = (vertex3.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 22] = (vertex3.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 23] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 24] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 25] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 26] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 27] = (0 + xSample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 28] = (0 + ySample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 30] = (vertex4.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 31] = (vertex4.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 32] = (vertex4.z + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 33] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 34] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 35] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 36] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 39] = (blockID);
                chunk.vertexIndexTransparent += 40;
            }

            case SOUTH_FACE, SOUTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (vertex1.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (vertex1.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (vertex1.z + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (0 + xSample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (vertex2.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (vertex2.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 16] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 18] = (0 + ySample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 20] = (vertex3.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 22] = (vertex3.z + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 23] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 24] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 25] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 26] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 27] = (0 + xSample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 28] = (0 + ySample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 30] = (vertex4.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 31] = (vertex4.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 32] = (vertex4.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 33] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 34] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 35] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 36] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 39] = (blockID);
                chunk.vertexIndexTransparent += 40;
            }

            case EAST_FACE, EAST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (vertex1.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (vertex1.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (vertex1.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (0 + xSample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (vertex2.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (vertex2.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 16] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 18] = (0 + ySample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 20] = (vertex3.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 22] = (vertex3.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 23] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 24] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 25] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 26] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 27] = (0 + xSample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 28] = (0 + ySample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 30] = (vertex4.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 31] = (vertex4.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 32] = (vertex4.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 33] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 34] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 35] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 36] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 39] = (blockID);
                chunk.vertexIndexTransparent += 40;
            }

            case WEST_FACE, WEST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (vertex1.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (vertex1.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (vertex1.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (0 + xSample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (1 + greedyMeshSize[1] + ySample1);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (blockID);

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (vertex2.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (vertex2.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (vertex2.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 16] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 17] = (1 + greedyMeshSize[0] + xSample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 18] = (0 + ySample2);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 19] = (blockID);

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 20] = (vertex3.x);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 21] = (vertex3.y + greedyMeshSize[1]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 22] = (vertex3.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 23] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 24] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 25] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 26] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 27] = (0 + xSample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 28] = (0 + ySample3);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 29] = (blockID);

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 30] = (vertex4.x + greedyMeshSize[0]);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 31] = (vertex4.y);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 32] = (vertex4.z);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 33] = (red);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 34] = (green);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 35] = (blue);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 36] = (alpha);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 37] = (1 + greedyMeshSize[0] + xSample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 38] = (1 + greedyMeshSize[1] + ySample4);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 39] = (blockID);
                chunk.vertexIndexTransparent += 40;
            }
        }
        resetLight();
    }


    public static float getBlockTextureID(short block, int face) {
        return Block.list[block].getBlockTexture(block, face);
    }

}
