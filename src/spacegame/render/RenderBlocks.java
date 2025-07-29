package spacegame.render;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.MathUtils;
import spacegame.world.Chunk;
import spacegame.world.WorldFace;

public class RenderBlocks {
    public float red = 1.0F;
    public float green = 1.0F;
    public float blue = 1.0F;
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

    private  void setLight(float x, float y, float z, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, int index, int face, Chunk chunk, WorldFace worldFace, int[] greedyMeshSize) {
        int xBlock = chunk.getBlockXFromIndex(index);
        int yBlock = chunk.getBlockYFromIndex(index);
        int zBlock = chunk.getBlockZFromIndex(index);

        if (x == xMin && y == yMin && z == zMin) {
            final Block[] blocks = Block.list;

            switch (face) {
                case BOTTOM_FACE -> {
                    int idA = worldFace.getBlockID(xBlock - 1, yBlock - 1, zBlock);
                    int idB = worldFace.getBlockID(xBlock, yBlock - 1, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int idA = worldFace.getBlockID(xBlock - 1, yBlock - 1, zBlock);
                    int idB = worldFace.getBlockID(xBlock - 1, yBlock, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock)
                        );
                    }
                }

                case EAST_FACE -> {
                    int idA = worldFace.getBlockID(xBlock - 1, yBlock, zBlock - 1);
                    int idB = worldFace.getBlockID(xBlock, yBlock - 1, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock - 1),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1)
                        );
                    }
                }
            }
        } else if (x == xMax && y == yMin && z == zMin) {
            final Block[] blocks = Block.list;
            final int meshX = greedyMeshSize[0];

            switch (face) {
                case BOTTOM_FACE -> {
                    int idA = worldFace.getBlockID(xBlock + 1 + meshX, yBlock - 1, zBlock);
                    int idB = worldFace.getBlockID(xBlock + meshX, yBlock, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    if (solidA && solidB) {
                        int lx = xBlock + meshX, ly = yBlock - 1, lz = zBlock;
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, ly, lz - 1),
                                worldFace.getBlockLightValue(lx + 1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz - 1),
                                worldFace.getBlockLightColor(lx + 1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz)
                        );
                    } else {
                        int lx = xBlock + meshX, ly = yBlock - 1, lz = zBlock;
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx + 1, ly, lz - 1),
                                worldFace.getBlockLightValue(lx, ly, lz - 1),
                                worldFace.getBlockLightValue(lx + 1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightColor(lx + 1, ly, lz - 1),
                                worldFace.getBlockLightColor(lx, ly, lz - 1),
                                worldFace.getBlockLightColor(lx + 1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    int idA = worldFace.getBlockID(xBlock + 1, yBlock - 1, zBlock);
                    int idB = worldFace.getBlockID(xBlock + 1, yBlock, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    int lx = xBlock + 1, lz = zBlock;
                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, yBlock, lz - 1),
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz - 1),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, yBlock, lz - 1),
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz - 1),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz - 1),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz - 1),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    }
                }

                case EAST_FACE -> {
                    int lx = xBlock + meshX;
                    int idA = worldFace.getBlockID(lx + 1, yBlock, zBlock - 1);
                    int idB = worldFace.getBlockID(lx, yBlock - 1, zBlock - 1);

                    boolean solidA = blocks[idA].isSolid;
                    boolean solidB = blocks[idB].isSolid;

                    int lz = zBlock - 1;
                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx + 1, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx + 1, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx + 1, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(lx + 1, yBlock - 1, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx + 1, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(lx + 1, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
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

                    boolean solidA = blocks[worldFace.getBlockID(lx1, ly1, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx2, ly2, lz1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx2, ly2, lz1),
                                worldFace.getBlockLightValue(lx1, ly1, lz),
                                worldFace.getBlockLightValue(lx2, ly2, lz),
                                worldFace.getBlockLightColor(lx2, ly2, lz1),
                                worldFace.getBlockLightColor(lx1, ly1, lz),
                                worldFace.getBlockLightColor(lx2, ly2, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx1, ly1, lz1),
                                worldFace.getBlockLightValue(lx2, ly2, lz1),
                                worldFace.getBlockLightValue(lx1, ly1, lz),
                                worldFace.getBlockLightValue(lx2, ly2, lz),
                                worldFace.getBlockLightColor(lx1, ly1, lz1),
                                worldFace.getBlockLightColor(lx2, ly2, lz1),
                                worldFace.getBlockLightColor(lx1, ly1, lz),
                                worldFace.getBlockLightColor(lx2, ly2, lz)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int lx = xBlock - 1, baseY = yBlock + meshY, ly = baseY, ly1 = baseY + 1, lz = zBlock;
                    boolean solidA = blocks[worldFace.getBlockID(lx, ly1, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, ly, lz - 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, ly, lz - 1),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightValue(lx, ly1, lz),
                                worldFace.getBlockLightColor(lx, ly, lz - 1),
                                worldFace.getBlockLightColor(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, ly, lz - 1),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightValue(lx, ly1, lz - 1),
                                worldFace.getBlockLightValue(lx, ly1, lz),
                                worldFace.getBlockLightColor(lx, ly, lz - 1),
                                worldFace.getBlockLightColor(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, ly1, lz - 1),
                                worldFace.getBlockLightColor(lx, ly1, lz)
                        );
                    }
                }

                case EAST_FACE -> {
                    int lx = xBlock, lz = zBlock - 1, baseY = yBlock + meshY, ly = baseY, ly1 = baseY + 1;
                    int lx1 = xBlock - 1;

                    boolean solidA = blocks[worldFace.getBlockID(lx1, ly, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, ly1, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightValue(lx, ly1, lz),
                                worldFace.getBlockLightColor(lx1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightValue(lx1, ly1, lz),
                                worldFace.getBlockLightValue(lx, ly1, lz),
                                worldFace.getBlockLightColor(lx1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz),
                                worldFace.getBlockLightColor(lx1, ly1, lz),
                                worldFace.getBlockLightColor(lx, ly1, lz)
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
                    boolean solidA = blocks[worldFace.getBlockID(xBlock - 1, ly, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, ly, lz + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, ly, lz + 1),
                                worldFace.getBlockLightValue(xBlock - 1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz + 1),
                                worldFace.getBlockLightColor(xBlock - 1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xBlock - 1, ly, lz + 1),
                                worldFace.getBlockLightValue(lx, ly, lz + 1),
                                worldFace.getBlockLightValue(xBlock - 1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightColor(xBlock - 1, ly, lz + 1),
                                worldFace.getBlockLightColor(lx, ly, lz + 1),
                                worldFace.getBlockLightColor(xBlock - 1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int lx = xBlock - 1, lz = zBlock + meshX;
                    int ly = yBlock;
                    boolean solidA = blocks[worldFace.getBlockID(lx, yBlock - 1, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, ly, lz + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, ly, lz + 1),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, ly, lz + 1),
                                worldFace.getBlockLightColor(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, ly, lz + 1),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz + 1),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, ly, lz + 1),
                                worldFace.getBlockLightColor(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz + 1),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    }
                }

                case WEST_FACE -> {
                    int lx = xBlock - 1, lz = zBlock + 1;
                    boolean solidA = blocks[worldFace.getBlockID(lx, yBlock, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(xBlock, yBlock - 1, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(xBlock, yBlock, lz),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(xBlock, yBlock, lz),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(xBlock, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightValue(xBlock, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(xBlock, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(xBlock, yBlock - 1, lz)
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

                    boolean solidA = blocks[worldFace.getBlockID(lx + 1, ly, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, ly, lz - 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, ly, lz - 1),
                                worldFace.getBlockLightValue(lx + 1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz - 1),
                                worldFace.getBlockLightColor(lx + 1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx + 1, ly, lz - 1),
                                worldFace.getBlockLightValue(lx, ly, lz - 1),
                                worldFace.getBlockLightValue(lx + 1, ly, lz),
                                worldFace.getBlockLightValue(lx, ly, lz),
                                worldFace.getBlockLightColor(lx + 1, ly, lz - 1),
                                worldFace.getBlockLightColor(lx, ly, lz - 1),
                                worldFace.getBlockLightColor(lx + 1, ly, lz),
                                worldFace.getBlockLightColor(lx, ly, lz)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    int lx = xBlock + 1, lz = zBlock;
                    int baseY = yBlock + meshY, ly1 = baseY + 1;

                    boolean solidA = blocks[worldFace.getBlockID(lx, ly1, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, baseY, lz - 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, baseY, lz - 1),
                                worldFace.getBlockLightValue(lx, baseY, lz),
                                worldFace.getBlockLightValue(lx, ly1, lz),
                                worldFace.getBlockLightColor(lx, baseY, lz - 1),
                                worldFace.getBlockLightColor(lx, baseY, lz),
                                worldFace.getBlockLightColor(lx, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, baseY, lz - 1),
                                worldFace.getBlockLightValue(lx, baseY, lz),
                                worldFace.getBlockLightValue(lx, ly1, lz - 1),
                                worldFace.getBlockLightValue(lx, ly1, lz),
                                worldFace.getBlockLightColor(lx, baseY, lz - 1),
                                worldFace.getBlockLightColor(lx, baseY, lz),
                                worldFace.getBlockLightColor(lx, ly1, lz - 1),
                                worldFace.getBlockLightColor(lx, ly1, lz)
                        );
                    }
                }

                case EAST_FACE -> {
                    int baseX = xBlock + meshX, baseY = yBlock + meshY, ly1 = baseY + 1, lz = zBlock - 1;

                    boolean solidA = blocks[worldFace.getBlockID(baseX + 1, baseY, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(baseX, ly1, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(baseX + 1, baseY, lz),
                                worldFace.getBlockLightValue(baseX, baseY, lz),
                                worldFace.getBlockLightValue(baseX, ly1, lz),
                                worldFace.getBlockLightColor(baseX + 1, baseY, lz),
                                worldFace.getBlockLightColor(baseX, baseY, lz),
                                worldFace.getBlockLightColor(baseX, ly1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(baseX + 1, baseY, lz),
                                worldFace.getBlockLightValue(baseX, baseY, lz),
                                worldFace.getBlockLightValue(baseX + 1, ly1, lz),
                                worldFace.getBlockLightValue(baseX, ly1, lz),
                                worldFace.getBlockLightColor(baseX + 1, baseY, lz),
                                worldFace.getBlockLightColor(baseX, baseY, lz),
                                worldFace.getBlockLightColor(baseX + 1, ly1, lz),
                                worldFace.getBlockLightColor(baseX, ly1, lz)
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

                    boolean solidA = blocks[worldFace.getBlockID(lx1, ly, lzBase)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx2, ly, lzNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx2, ly, lzNext),
                                worldFace.getBlockLightValue(lx1, ly, lzBase),
                                worldFace.getBlockLightValue(lx2, ly, lzBase),
                                worldFace.getBlockLightColor(lx2, ly, lzNext),
                                worldFace.getBlockLightColor(lx1, ly, lzBase),
                                worldFace.getBlockLightColor(lx2, ly, lzBase)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx1, ly, lzNext),
                                worldFace.getBlockLightValue(lx2, ly, lzNext),
                                worldFace.getBlockLightValue(lx1, ly, lzBase),
                                worldFace.getBlockLightValue(lx2, ly, lzBase),
                                worldFace.getBlockLightColor(lx1, ly, lzNext),
                                worldFace.getBlockLightColor(lx2, ly, lzNext),
                                worldFace.getBlockLightColor(lx1, ly, lzBase),
                                worldFace.getBlockLightColor(lx2, ly, lzBase)
                        );
                    }
                }

                case NORTH_FACE -> {
                    int lx = xBlock - 1;
                    int lzBase = zBlock + meshX, lzNext = lzBase + 1;
                    int lyBase = yBlock + meshZ, lyNext = lyBase + 1;

                    boolean solidA = blocks[worldFace.getBlockID(lx, lyNext, lzBase)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, lyBase, lzNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, lyBase, lzNext),
                                worldFace.getBlockLightValue(lx, lyBase, lzBase),
                                worldFace.getBlockLightValue(lx, lyNext, lzBase),
                                worldFace.getBlockLightColor(lx, lyBase, lzNext),
                                worldFace.getBlockLightColor(lx, lyBase, lzBase),
                                worldFace.getBlockLightColor(lx, lyNext, lzBase)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, lyBase, lzNext),
                                worldFace.getBlockLightValue(lx, lyBase, lzBase),
                                worldFace.getBlockLightValue(lx, lyNext, lzNext),
                                worldFace.getBlockLightValue(lx, lyNext, lzBase),
                                worldFace.getBlockLightColor(lx, lyBase, lzNext),
                                worldFace.getBlockLightColor(lx, lyBase, lzBase),
                                worldFace.getBlockLightColor(lx, lyNext, lzNext),
                                worldFace.getBlockLightColor(lx, lyNext, lzBase)
                        );
                    }
                }

                case WEST_FACE -> {
                    int lx1 = xBlock - 1, lx2 = xBlock;
                    int lz = zBlock + 1, lyBase = yBlock + meshZ, lyNext = lyBase + 1;

                    boolean solidA = blocks[worldFace.getBlockID(lx1, lyBase, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx2, lyNext, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx1, lyBase, lz),
                                worldFace.getBlockLightValue(lx2, lyBase, lz),
                                worldFace.getBlockLightValue(lx2, lyNext, lz),
                                worldFace.getBlockLightColor(lx1, lyBase, lz),
                                worldFace.getBlockLightColor(lx2, lyBase, lz),
                                worldFace.getBlockLightColor(lx2, lyNext, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx1, lyBase, lz),
                                worldFace.getBlockLightValue(lx2, lyBase, lz),
                                worldFace.getBlockLightValue(lx1, lyNext, lz),
                                worldFace.getBlockLightValue(lx2, lyNext, lz),
                                worldFace.getBlockLightColor(lx1, lyBase, lz),
                                worldFace.getBlockLightColor(lx2, lyBase, lz),
                                worldFace.getBlockLightColor(lx1, lyNext, lz),
                                worldFace.getBlockLightColor(lx2, lyNext, lz)
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
                    boolean solidA = blocks[worldFace.getBlockID(lxNext, ly, lzBase)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lxBase, ly, lzNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lxBase, ly, lzNext),
                                worldFace.getBlockLightValue(lxNext, ly, lzBase),
                                worldFace.getBlockLightValue(lxBase, ly, lzBase),
                                worldFace.getBlockLightColor(lxBase, ly, lzNext),
                                worldFace.getBlockLightColor(lxNext, ly, lzBase),
                                worldFace.getBlockLightColor(lxBase, ly, lzBase)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lxNext, ly, lzNext),
                                worldFace.getBlockLightValue(lxBase, ly, lzNext),
                                worldFace.getBlockLightValue(lxNext, ly, lzBase),
                                worldFace.getBlockLightValue(lxBase, ly, lzBase),
                                worldFace.getBlockLightColor(lxNext, ly, lzNext),
                                worldFace.getBlockLightColor(lxBase, ly, lzNext),
                                worldFace.getBlockLightColor(lxNext, ly, lzBase),
                                worldFace.getBlockLightColor(lxBase, ly, lzBase)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    int lx = xBlock + 1, lz = zBlock + meshX;
                    boolean solidA = blocks[worldFace.getBlockID(lx, yBlock - 1, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lx, yBlock, lz + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lx, yBlock, lz + 1),
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz + 1),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lx, yBlock, lz + 1),
                                worldFace.getBlockLightValue(lx, yBlock, lz),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz + 1),
                                worldFace.getBlockLightValue(lx, yBlock - 1, lz),
                                worldFace.getBlockLightColor(lx, yBlock, lz + 1),
                                worldFace.getBlockLightColor(lx, yBlock, lz),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz + 1),
                                worldFace.getBlockLightColor(lx, yBlock - 1, lz)
                        );
                    }
                }

                case WEST_FACE -> {
                    int lyBase = yBlock, lyNext = yBlock - 1, lz = zBlock + 1;

                    boolean solidA = blocks[worldFace.getBlockID(lxNext, lyBase, lz)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(lxBase, lyNext, lz)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(lxNext, lyBase, lz),
                                worldFace.getBlockLightValue(lxBase, lyBase, lz),
                                worldFace.getBlockLightValue(lxBase, lyNext, lz),
                                worldFace.getBlockLightColor(lxNext, lyBase, lz),
                                worldFace.getBlockLightColor(lxBase, lyBase, lz),
                                worldFace.getBlockLightColor(lxBase, lyNext, lz)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(lxNext, lyBase, lz),
                                worldFace.getBlockLightValue(lxBase, lyBase, lz),
                                worldFace.getBlockLightValue(lxNext, lyNext, lz),
                                worldFace.getBlockLightValue(lxBase, lyNext, lz),
                                worldFace.getBlockLightColor(lxNext, lyBase, lz),
                                worldFace.getBlockLightColor(lxBase, lyBase, lz),
                                worldFace.getBlockLightColor(lxNext, lyNext, lz),
                                worldFace.getBlockLightColor(lxBase, lyNext, lz)
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
                    boolean solidA = blocks[worldFace.getBlockID(xMeshNext, yUp, zMesh)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(xMesh, yUp, zMeshNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(xMesh, yUp, zMeshNext),
                                worldFace.getBlockLightValue(xMeshNext, yUp, zMesh),
                                worldFace.getBlockLightValue(xMesh, yUp, zMesh),
                                worldFace.getBlockLightColor(xMesh, yUp, zMeshNext),
                                worldFace.getBlockLightColor(xMeshNext, yUp, zMesh),
                                worldFace.getBlockLightColor(xMesh, yUp, zMesh)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xMeshNext, yUp, zMeshNext),
                                worldFace.getBlockLightValue(xMesh, yUp, zMeshNext),
                                worldFace.getBlockLightValue(xMeshNext, yUp, zMesh),
                                worldFace.getBlockLightValue(xMesh, yUp, zMesh),
                                worldFace.getBlockLightColor(xMeshNext, yUp, zMeshNext),
                                worldFace.getBlockLightColor(xMesh, yUp, zMeshNext),
                                worldFace.getBlockLightColor(xMeshNext, yUp, zMesh),
                                worldFace.getBlockLightColor(xMesh, yUp, zMesh)
                        );
                    }
                }

                case SOUTH_FACE -> {
                    boolean solidA = blocks[worldFace.getBlockID(xBlock + 1, yMeshNext, zMeshX)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(xBlock + 1, yMesh, zMeshXNext)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(xBlock + 1, yMesh, zMeshXNext),
                                worldFace.getBlockLightValue(xBlock + 1, yMesh, zMeshX),
                                worldFace.getBlockLightValue(xBlock + 1, yMeshNext, zMeshX),
                                worldFace.getBlockLightColor(xBlock + 1, yMesh, zMeshXNext),
                                worldFace.getBlockLightColor(xBlock + 1, yMesh, zMeshX),
                                worldFace.getBlockLightColor(xBlock + 1, yMeshNext, zMeshX)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xBlock + 1, yMesh, zMeshXNext),
                                worldFace.getBlockLightValue(xBlock + 1, yMesh, zMeshX),
                                worldFace.getBlockLightValue(xBlock + 1, yMeshNext, zMeshXNext),
                                worldFace.getBlockLightValue(xBlock + 1, yMeshNext, zMeshX),
                                worldFace.getBlockLightColor(xBlock + 1, yMesh, zMeshXNext),
                                worldFace.getBlockLightColor(xBlock + 1, yMesh, zMeshX),
                                worldFace.getBlockLightColor(xBlock + 1, yMeshNext, zMeshXNext),
                                worldFace.getBlockLightColor(xBlock + 1, yMeshNext, zMeshX)
                        );
                    }
                }

                case WEST_FACE -> {
                    boolean solidA = blocks[worldFace.getBlockID(xMeshNext, yMesh, zBlock + 1)].isSolid;
                    boolean solidB = blocks[worldFace.getBlockID(xMesh, yMeshNext, zBlock + 1)].isSolid;

                    if (solidA && solidB) {
                        setVertexLight3Args(
                                worldFace.getBlockLightValue(xMeshNext, yMesh, zBlock + 1),
                                worldFace.getBlockLightValue(xMesh, yMesh, zBlock + 1),
                                worldFace.getBlockLightValue(xMesh, yMeshNext, zBlock + 1),
                                worldFace.getBlockLightColor(xMeshNext, yMesh, zBlock + 1),
                                worldFace.getBlockLightColor(xMesh, yMesh, zBlock + 1),
                                worldFace.getBlockLightColor(xMesh, yMeshNext, zBlock + 1)
                        );
                    } else {
                        setVertexLight4Args(
                                worldFace.getBlockLightValue(xMeshNext, yMesh, zBlock + 1),
                                worldFace.getBlockLightValue(xMesh, yMesh, zBlock + 1),
                                worldFace.getBlockLightValue(xMeshNext, yMeshNext, zBlock + 1),
                                worldFace.getBlockLightValue(xMesh, yMeshNext, zBlock + 1),
                                worldFace.getBlockLightColor(xMeshNext, yMesh, zBlock + 1),
                                worldFace.getBlockLightColor(xMesh, yMesh, zBlock + 1),
                                worldFace.getBlockLightColor(xMeshNext, yMeshNext, zBlock + 1),
                                worldFace.getBlockLightColor(xMesh, yMeshNext, zBlock + 1)
                        );
                    }
                }
            }
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock - 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMin) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMin && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z == zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else if (x == xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight4Args(worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock + 1, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMin && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock - 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock - 1, zBlock));
        } else if (x >= xMin && x <= xMax && y == yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock + 1, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock + 1, zBlock));
        } else if (x == xMin && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock - 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock - 1, yBlock, zBlock));
        } else if (x == xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock + 1, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock + 1, yBlock, zBlock));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z == zMin) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock - 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock - 1));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z == zMax) {
            setVertexLight2Args(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightValue(xBlock, yBlock, zBlock + 1), worldFace.getBlockLightColor(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock + 1));
        } else if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
            setVertexLight1Arg(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        } else {
            setVertexLight1Arg(worldFace.getBlockLightValue(xBlock, yBlock, zBlock), worldFace.getBlockLightColor(xBlock, yBlock, zBlock));
        }
    }

    private  void resetLight() {
        red = 1F;
        green = 1F;
        blue = 1F;
    }

    private void setVertexLight1Arg(byte light,
                                    float[] lightColor) {

        final float finalLight = getLightValueFromMap(light);

        float r = lightColor[0];
        float g = lightColor[1];
        float b = lightColor[2];

        red   = r * finalLight;
        green = g * finalLight;
        blue  = b * finalLight;
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

        red   = (rCount > 0 ? rSum / rCount : 0F) * finalLight;
        green = (gCount > 0 ? gSum / gCount : 0F) * finalLight;
        blue  = (bCount > 0 ? bSum / bCount : 0F) * finalLight;
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

        red   = (rCount > 0 ? rSum / rCount : 0F) * finalLight;
        green = (gCount > 0 ? gSum / gCount : 0F) * finalLight;
        blue  = (bCount > 0 ? bSum / bCount : 0F) * finalLight;
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

        red   = (rCount > 0 ? rSum / rCount : 0F) * finalLight;
        green = (gCount > 0 ? gSum / gCount : 0F) * finalLight;
        blue  = (bCount > 0 ? bSum / bCount : 0F) * finalLight;
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

    private float compressTextureCoordinates(float x, float y, byte vertexNormalInOctahedralY){
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

        int combinedInt = ((vertexNormalInOctahedralY << 24) | (xAsIntLessThanOne << 18) | (xAsIntGreaterThanOne << 12) | (yAsIntLessThanOne << 6) | yAsIntGreaterThanOne);
        return Float.intBitsToFloat(combinedInt);
    }

    private float compressColor(float red, float green, float blue, byte vertexNormalInOctahedralX){
        return Float.intBitsToFloat(((vertexNormalInOctahedralX << 24) |(MathUtils.floatToIntRGBA(red) << 16) | (MathUtils.floatToIntRGBA(green) << 8) | MathUtils.floatToIntRGBA(blue)));
    }

    private float compressPosXY(float x, float y){
        return Float.intBitsToFloat(MathUtils.floatToHalf(x) << 16 |  MathUtils.floatToHalf(y));
    }

    private float compressPosZAndTexId(float z, float texID){
        return Float.intBitsToFloat(MathUtils.floatToHalf(z) << 16 | (int)texID);
    }


    private void renderOpaqueFace(Chunk chunk, WorldFace worldFace, short block, int index, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, int[] greedyMeshSize) {
        int x = (index % 32);
        int y = (index >> 10);
        int z = ((index % 1024) >> 5);

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

        byte[] vertexNormalsInOctahedral = MathUtils.encodeNormalOctahedral(new Vector3f(blockFace.vertices[4][0], blockFace.vertices[4][1], blockFace.vertices[4][2]));

        switch (blockFace.faceType) {
            case TOP_FACE, TOP_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample1, 0 + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (this.compressPosXY(vertex2.x, vertex2.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (this.compressTextureCoordinates(0 + xSample2, 1 + greedyMeshSize[1] + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (this.compressPosZAndTexId(vertex2.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample3, 1 + greedyMeshSize[1] + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (this.compressTextureCoordinates(0 + xSample4, 0 + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexOpaque += 16;
            }

            case BOTTOM_FACE, BOTTOM_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (this.compressTextureCoordinates(0 + xSample1, 0 + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 1 + greedyMeshSize[1] + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (this.compressPosXY(vertex3.x, vertex3.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (this.compressTextureCoordinates(0 + xSample3, 1 + greedyMeshSize[1] + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 0 + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexOpaque += 16;
            }
            case NORTH_FACE, NORTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (this.compressPosZAndTexId(vertex2.z + greedyMeshSize[0], blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (this.compressPosZAndTexId(vertex3.z, blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (this.compressPosZAndTexId(vertex4.z + greedyMeshSize[0], blockID));
                chunk.vertexIndexOpaque += 16;
            }

            case SOUTH_FACE, SOUTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (this.compressPosZAndTexId(vertex1.z + greedyMeshSize[0], blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (this.compressPosZAndTexId(vertex3.z + greedyMeshSize[0], blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexOpaque += 16;
            }

            case EAST_FACE, EAST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (this.compressPosZAndTexId(vertex3.z, blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexOpaque += 16;
            }

            case WEST_FACE, WEST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 4] = (this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 8] = (this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 11] = (this.compressPosZAndTexId(vertex3.z, blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, minVertex.x, minVertex.y, minVertex.z, maxVertex.x, maxVertex.y, maxVertex.z, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 12] = (this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayOpaque[chunk.vertexIndexOpaque + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexOpaque += 16;
            }
        }
        resetLight();
    }

    private  void renderTransparentFace(Chunk chunk, WorldFace worldFace, short block, int index, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, int[] greedyMeshSize) {
        int x = (index % 32);
        int y = (index >> 10);
        int z = ((index % 1024) >> 5);

        float blockID = getBlockTextureID(block, face);

        Vector3f blockPosition = new Vector3f(x, y, z);
        Vector3f vertex1 = new Vector3f(blockFace.vertices[0][0], blockFace.vertices[0][1], blockFace.vertices[0][2]).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1][0], blockFace.vertices[1][1], blockFace.vertices[1][2]).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2][0], blockFace.vertices[2][1], blockFace.vertices[2][2]).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3][0], blockFace.vertices[3][1], blockFace.vertices[3][2]).add(blockPosition);

        byte[] vertexNormalsInOctahedral = MathUtils.encodeNormalOctahedral(new Vector3f(blockFace.vertices[4][0], blockFace.vertices[4][1], blockFace.vertices[4][2]));

        switch (blockFace.faceType) {
            case TOP_FACE, TOP_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample1, 0 + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (this.compressPosXY(vertex2.x, vertex2.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (this.compressTextureCoordinates(0 + xSample2, 1 + greedyMeshSize[1] + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (this.compressPosZAndTexId(vertex2.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample3, 1 + greedyMeshSize[1] + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (this.compressTextureCoordinates(0 + xSample4, 0 + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexTransparent += 16;
            }

            case BOTTOM_FACE, BOTTOM_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (this.compressTextureCoordinates(0 + xSample1, 0 + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 1 + greedyMeshSize[1] + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (this.compressPosZAndTexId(vertex2.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (this.compressPosXY(vertex3.x, vertex3.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (this.compressTextureCoordinates(0 + xSample3, 1 + greedyMeshSize[1] + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (this.compressPosZAndTexId(vertex3.z + greedyMeshSize[1], blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 0 + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexTransparent += 16;
            }
            case NORTH_FACE, NORTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (this.compressPosZAndTexId(vertex2.z + greedyMeshSize[0], blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (this.compressPosZAndTexId(vertex3.z, blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (this.compressPosZAndTexId(vertex4.z + greedyMeshSize[0], blockID));
                chunk.vertexIndexTransparent += 16;
            }

            case SOUTH_FACE, SOUTH_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (this.compressPosZAndTexId(vertex1.z + greedyMeshSize[0], blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (this.compressPosZAndTexId(vertex3.z + greedyMeshSize[0], blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexTransparent += 16;
            }

            case EAST_FACE, EAST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (this.compressPosXY(vertex1.x + greedyMeshSize[0], vertex1.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (this.compressPosXY(vertex2.x, vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (this.compressPosXY(vertex3.x + greedyMeshSize[0], vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (this.compressPosZAndTexId(vertex3.z, blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (this.compressPosXY(vertex4.x, vertex4.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexTransparent += 16;
            }

            case WEST_FACE, WEST_FACE_UNSORTED -> {
                resetLight();
                setLight(vertex1.x, vertex1.y, vertex1.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 0] = (this.compressPosXY(vertex1.x, vertex1.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 1] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 2] = (this.compressTextureCoordinates(0 + xSample1, 1 + greedyMeshSize[1] + ySample1, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 3] = (this.compressPosZAndTexId(vertex1.z, blockID));

                resetLight();
                setLight(vertex2.x, vertex2.y, vertex2.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 4] = (this.compressPosXY(vertex2.x + greedyMeshSize[0], vertex2.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 5] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 6] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample2, 0 + ySample2, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 7] = (this.compressPosZAndTexId(vertex2.z, blockID));

                resetLight();
                setLight(vertex3.x, vertex3.y, vertex3.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 8] = (this.compressPosXY(vertex3.x, vertex3.y + greedyMeshSize[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 9] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 10] = (this.compressTextureCoordinates(0 + xSample3, 0 + ySample3, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 11] = (this.compressPosZAndTexId(vertex3.z, blockID));

                resetLight();
                setLight(vertex4.x, vertex4.y, vertex4.z, x, y, z, x + 1, y + 1, z + 1, index, face, chunk, worldFace, greedyMeshSize);
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 12] = (this.compressPosXY(vertex4.x + greedyMeshSize[0], vertex4.y));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 13] = (this.compressColor(red, green, blue, vertexNormalsInOctahedral[0]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 14] = (this.compressTextureCoordinates(1 + greedyMeshSize[0] + xSample4, 1 + greedyMeshSize[1] + ySample4, vertexNormalsInOctahedral[1]));
                chunk.vertexArrayTransparent[chunk.vertexIndexTransparent + 15] = (this.compressPosZAndTexId(vertex4.z, blockID));
                chunk.vertexIndexTransparent += 16;
            }
        }
        resetLight();
    }


    public static float getBlockTextureID(short block, int face) {
        return Block.list[block].getBlockTexture(block, face);
    }

}
