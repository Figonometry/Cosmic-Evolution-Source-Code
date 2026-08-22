package spacegame.render;

import org.joml.Matrix4d;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.render.texturelists.ItemTextureList;
import spacegame.util.MathUtil;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public final class RenderEngine {
    public static final int NULL_TEXTURE = -1;
    public static final int TEXTURE_TYPE_2D = 1;
    public static final int TEXTURE_TYPE_2D_ARRAY = 2;
    public static final int TEXTURE_TYPE_CUBEMAP = 3;
    public ArrayList<LoadableTexture> loadableTexturesForCelestialObjects = new ArrayList<>(); //This is used for textures prepared off the main thread


    public void loadTexturesFromList(){
        LoadableTexture texture;
        synchronized (this.loadableTexturesForCelestialObjects) {
            for (int i = 0; i < this.loadableTexturesForCelestialObjects.size(); i++) {
                texture = this.loadableTexturesForCelestialObjects.get(i);
                texture.object.mappedTexture = this.createTexture(texture.filepath, texture.textureType, texture.arraySize, true);
            }

            this.loadableTexturesForCelestialObjects.clear();
        }
    }


    public int createVAO(){
        return GL46.glGenVertexArrays();
    }

    public int createBuffers(){
        return GL46.glGenBuffers();
    }

    public void deleteTexture(int texID){
        GL46.glDeleteTextures(texID);
    }

    public void deleteVAO(int vaoID){
        GL46.glDeleteVertexArrays(vaoID);
    }

    public void deleteBuffers(int bufferID){
        GL46.glDeleteBuffers(bufferID);
    }

    public Shader reloadShader(Shader shader){
        String filepath = shader.filepath;

        GL46.glDeleteShader(shader.vertexID);
        GL46.glDeleteShader(shader.fragmentID);
        GL46.glDeleteProgram(shader.shaderProgramID);


        return new Shader(filepath);
    }

    public void setVertexAttribute(int vaoID, int index, int size, int vertexSizeBytes, int pointer, int vboID){
        GL46.glBindVertexArray(vaoID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        GL46.glVertexAttribPointer(index, size, GL46.GL_FLOAT, false, vertexSizeBytes, pointer);
        GL46.glEnableVertexAttribArray(index);
    }

    public int createTexture(String filepath, int textureType, int arraySize, boolean clampTextureToEdge){
        if(!GameSettings.usingDefaultAssets) {
            String[] pathContents = filepath.split("/");


            StringBuilder stringBuilder = new StringBuilder();
            boolean buildString = false;
            for(int i = 0; i < pathContents.length; i++){
                if(buildString){
                    stringBuilder.append("/");
                    stringBuilder.append(pathContents[i]);
                }


                if(pathContents[i].equals("assets")){
                    buildString = true;
                }
            }
            String newFilepath = GameSettings.assetPackPath + (buildString ? stringBuilder : pathContents[pathContents.length - 1]);

            if(textureType == TEXTURE_TYPE_2D_ARRAY || textureType == TEXTURE_TYPE_CUBEMAP){
                newFilepath = newFilepath + "/";
            }


            String normalized = newFilepath.replace("\\", "/");

            if(new File(normalized).exists()){
                filepath = normalized;
            }

        }


        switch (textureType){
            case TEXTURE_TYPE_2D -> {

                File file = new File(filepath);
                if (!file.exists()) {
                    filepath = CosmicEvolution.imageFallbackPath;
                }


                //Generate the texture on GPU
                int texID = GL46.glGenTextures();
                GL46.glActiveTexture(GL46.GL_TEXTURE0);
                GL46.glBindTexture(GL46.GL_TEXTURE_2D, texID);

                //set texture parameters
                //repeat image in both directions
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, clampTextureToEdge ? GL46.GL_CLAMP_TO_EDGE : GL46.GL_REPEAT);
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, clampTextureToEdge ? GL46.GL_CLAMP_TO_EDGE : GL46.GL_REPEAT);
                //When stretching the image pixelate
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
                //when shrinking an image, pixelate
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);

                IntBuffer width = BufferUtils.createIntBuffer(1);
                IntBuffer height = BufferUtils.createIntBuffer(1);
                IntBuffer channels = BufferUtils.createIntBuffer(1);
                ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 0);

                if (image != null) {
                    if (channels.get(0) == 3) {
                        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGB, width.get(0), height.get(0),
                                0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                    } else if (channels.get(0) == 4) {
                        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width.get(0), height.get(0),
                                0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                    } else {
                        assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                    }
                } else {
                    assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "'";
                }

                STBImage.stbi_image_free(image);

                return texID;
            }
            case TEXTURE_TYPE_2D_ARRAY -> {
                int textureArray = GL46.glGenTextures();
                GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, textureArray);
                GL46.glTexStorage3D(GL46.GL_TEXTURE_2D_ARRAY, 1, GL46.GL_RGBA8, 32, 32, arraySize);

                GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_WRAP_S, filepath.contains("item") ? GL46.GL_CLAMP_TO_EDGE : GL46.GL_REPEAT);
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_WRAP_T, filepath.contains("item") ? GL46.GL_CLAMP_TO_EDGE : GL46.GL_REPEAT);
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST_MIPMAP_LINEAR);
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);




                for (int i = filepath.contains("item") ? 1 : 0; i < arraySize; i++) {
                    this.loadTextures(i, filepath);
                }

                GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D_ARRAY);

                return textureArray;
            }
            case TEXTURE_TYPE_CUBEMAP -> {
                File file = new File(filepath);
                if (!file.exists()) {
                    filepath = CosmicEvolution.imageFallbackPath;
                }


                //Generate the texture on GPU
                int texID = GL46.glGenTextures();
                GL46.glActiveTexture(GL46.GL_TEXTURE0);
                GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, texID);

                //set texture parameters
                //repeat image in both directions
                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_WRAP_S, GL46.GL_CLAMP_TO_EDGE);
                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_WRAP_T, GL46.GL_CLAMP_TO_EDGE);
                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_WRAP_R, GL46.GL_CLAMP_TO_EDGE);

                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST_MIPMAP_NEAREST);
                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);


                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_BASE_LEVEL, 0);
                GL46.glTexParameteri(GL46.GL_TEXTURE_CUBE_MAP, GL46.GL_TEXTURE_MAX_LEVEL, 5);

                IntBuffer width;
                IntBuffer height;
                IntBuffer channels;
                ByteBuffer image;
                for(int i = 0; i < 6; i++){
                    switch (i){
                        case 0:
                            width = BufferUtils.createIntBuffer(1);
                            height = BufferUtils.createIntBuffer(1);
                            channels = BufferUtils.createIntBuffer(1);
                            image = STBImage.stbi_load(filepath + "/posX.png" ,width, height, channels, 0);

                            if (image != null) {
                                if (channels.get(0) == 3) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL46.GL_RGB8, width.get(0), height.get(0),
                                            0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                                } else if (channels.get(0) == 4) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL46.GL_RGBA8, width.get(0), height.get(0),
                                            0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                                } else {
                                    assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                                }
                            } else {
                                assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "/posX.png" + "'";
                            }

                            STBImage.stbi_image_free(image);
                            break;
                        case 1:
                            width = BufferUtils.createIntBuffer(1);
                            height = BufferUtils.createIntBuffer(1);
                            channels = BufferUtils.createIntBuffer(1);
                            image = STBImage.stbi_load(filepath + "/negX.png" ,width, height, channels, 0);

                            if (image != null) {
                                if (channels.get(0) == 3) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL46.GL_RGB8, width.get(0), height.get(0),
                                            0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                                } else if (channels.get(0) == 4) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL46.GL_RGBA8, width.get(0), height.get(0),
                                            0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                                } else {
                                    assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                                }
                            } else {
                                assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "/posX.png" + "'";
                            }

                            STBImage.stbi_image_free(image);
                            break;
                        case 2:
                            width = BufferUtils.createIntBuffer(1);
                            height = BufferUtils.createIntBuffer(1);
                            channels = BufferUtils.createIntBuffer(1);
                            image = STBImage.stbi_load(filepath + "/posY.png" ,width, height, channels, 0);

                            if (image != null) {
                                if (channels.get(0) == 3) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL46.GL_RGB8, width.get(0), height.get(0),
                                            0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                                } else if (channels.get(0) == 4) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL46.GL_RGBA8, width.get(0), height.get(0),
                                            0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                                } else {
                                    assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                                }
                            } else {
                                assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "/posX.png" + "'";
                            }

                            STBImage.stbi_image_free(image);
                            break;
                        case 3:
                            width = BufferUtils.createIntBuffer(1);
                            height = BufferUtils.createIntBuffer(1);
                            channels = BufferUtils.createIntBuffer(1);
                            image = STBImage.stbi_load(filepath + "/negY.png" ,width, height, channels, 0);

                            if (image != null) {
                                if (channels.get(0) == 3) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL46.GL_RGB8, width.get(0), height.get(0),
                                            0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                                } else if (channels.get(0) == 4) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL46.GL_RGBA8, width.get(0), height.get(0),
                                            0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                                } else {
                                    assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                                }
                            } else {
                                assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "/posX.png" + "'";
                            }

                            STBImage.stbi_image_free(image);
                            break;
                        case 4:
                            width = BufferUtils.createIntBuffer(1);
                            height = BufferUtils.createIntBuffer(1);
                            channels = BufferUtils.createIntBuffer(1);
                            image = STBImage.stbi_load(filepath + "/posZ.png" ,width, height, channels, 0);

                            if (image != null) {
                                if (channels.get(0) == 3) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL46.GL_RGB8, width.get(0), height.get(0),
                                            0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                                } else if (channels.get(0) == 4) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL46.GL_RGBA8, width.get(0), height.get(0),
                                            0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                                } else {
                                    assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                                }
                            } else {
                                assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "/posX.png" + "'";
                            }

                            STBImage.stbi_image_free(image);
                            break;
                        case 5:
                            width = BufferUtils.createIntBuffer(1);
                            height = BufferUtils.createIntBuffer(1);
                            channels = BufferUtils.createIntBuffer(1);
                            image = STBImage.stbi_load(filepath + "/negZ.png" ,width, height, channels, 0);

                            if (image != null) {
                                if (channels.get(0) == 3) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL46.GL_RGB8, width.get(0), height.get(0),
                                            0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
                                } else if (channels.get(0) == 4) {
                                    GL46.glTexImage2D(GL46.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL46.GL_RGBA8, width.get(0), height.get(0),
                                            0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
                                } else {
                                    assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
                                }
                            } else {
                                assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "/posX.png" + "'";
                            }

                            STBImage.stbi_image_free(image);
                            break;
                    }
                }



                GL46.glGenerateMipmap(GL46.GL_TEXTURE_CUBE_MAP);
                return texID;
            }

            default -> {
                throw new RuntimeException("UNSUPPORTED TEXTURE TYPE");
            }
        }
    }

    private void loadTextures(int textureNumber, String filepath) {
        String imageName = getBlockName(textureNumber, filepath);
        String imageFilepath = filepath + imageName + ".png";



        if (!new File(imageFilepath).exists() && !GameSettings.usingDefaultAssets) {
            String[] pathContents = imageFilepath.split("/");
            imageFilepath = CosmicEvolution.imageFallbackPath;
        }

        if(!new File(imageFilepath).exists()){
            imageFilepath = CosmicEvolution.imageFallbackPath;
        }

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(imageFilepath, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                GL46.glTexSubImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, textureNumber, 32, 32, 1, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                GL46.glTexSubImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, textureNumber, 32, 32, 1, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
            }
        } else {
            assert false : "Error: (labyrinthgame.Texture) Could not load image '" + imageFilepath + "'";
        }

        STBImage.stbi_image_free(image);
    }

    public static String getBlockName(int textureNumber, String textureFolderpath) {
        if(textureFolderpath.contains("blocks")){
            return switch (textureNumber) {
                case BlockTextureList.GRASS_FULL_TOP_TEXTURE -> "grassFullTop";
                case BlockTextureList.SOIL_MEDIUM_FERTILITY_TEXTURE -> "soilMediumFertility";
                case BlockTextureList.GRASS_FULL_SIDE_TEXTURE -> "grassFullSide";
                case BlockTextureList.TORCH_TEXTURE -> "torch";
                case BlockTextureList.WATER_TOP_TEXTURE -> "water"; //Top
                case BlockTextureList.SAND_TEXTURE -> "sand";
                case BlockTextureList.SNOW_TEXTURE -> "snow";
                case BlockTextureList.STONE_TEXTURE -> "stone";
                case BlockTextureList.OAK_LOG_SIDE_TEXTURE -> "logSide";
                case BlockTextureList.OAK_LOG_TOP_TEXTURE -> "logTop";
                case BlockTextureList.LEAF_OPAQUE_TEXTURE -> "leafOpaque";
                case BlockTextureList.BERRY_BUSH_TOP_BASE_TEXTURE -> "berryBushTopBase";
                case BlockTextureList.BERRY_BUSH_SIDE_BASE_TEXTURE -> "berryBushSideBase";
                case BlockTextureList.CLAY_TEXTURE -> "clay";
                case BlockTextureList.OBSIDIAN_STONE -> "obsidianStone";
                case BlockTextureList.STRAW_TEXTURE -> "strawTexture";
                case BlockTextureList.CAMPFIRE_BASE_TEXTURE -> "campFireBase";
                case BlockTextureList.FIRED_RED_CLAY_TEXTURE -> "firedRedClay";
                case BlockTextureList.FIRE_TEXTURE -> "fire";
                case BlockTextureList.EMPTY_COLOR_TEXTURE -> "emptyColor";
                case BlockTextureList.OBSIDIAN_GRAVEL -> "obsidianGravel";
                case BlockTextureList.CACTUS_SIDE_TEXTURE -> "cactusSide";
                case BlockTextureList.CACTUS_TOP_TEXTURE -> "cactusTop";
                case BlockTextureList.CACTUS_BOTTOM_TEXTURE -> "cactusBottom";
                case BlockTextureList.LEAF_TRANSPARENT_TEXTURE -> "leafTransparent";
                case BlockTextureList.BERRY_BUSH_SIDE_TEXTURE -> "berryBushSide";
                case BlockTextureList.BERRY_BUSH_TOP_TEXTURE -> "berryBushTop";
                case BlockTextureList.BERRY_BUSH_FLOWER_TOP_TEXTURE -> "berryBushFlowerTop";
                case BlockTextureList.BERRY_BUSH_FLOWER_SIDE_TEXTURE -> "berryBushFlowerSide";
                case BlockTextureList.ITEM_STICK_TEXTURE -> "itemStick";
                case BlockTextureList.TALL_GRASS_TEXTURE -> "tallGrass";
                case BlockTextureList.FIREWOOD_TEXTURE -> "fireWood";
                case BlockTextureList.REED_CHEST_TEXTURE -> "reedChest";
                case BlockTextureList.GRASS_SMALL_PATCHES_TOP_TEXTURE -> "grassSmallPatchesTop";
                case BlockTextureList.GRASS_SMALL_PATCHES_SIDE_TEXTURE -> "grassSmallPatchesSide";
                case BlockTextureList.GRASS_LARGE_PATCHES_TOP_TEXTURE -> "grassLargePatchesTop";
                case BlockTextureList.GRASS_LARGE_PATCHES_SIDE_TEXTURE -> "grassLargePatchesSide";
                case BlockTextureList.SNOWY_GRASS_SMALL_PATCHES_SIDE_TEXTURE -> "snowyGrassSmallPatchesSide";
                case BlockTextureList.TORCH_UNLIT_TEXTURE -> "torchUnlit";
                case BlockTextureList.TORCH_BURNED_OUT_TEXTURE -> "torchBurnedOut";
                case BlockTextureList.PRIMITIVE_CRAFTING_TABLE -> "primitiveCraftingTable";
                case BlockTextureList.SNOWY_GRASS_LARGE_PATCHES_SIDE_TEXTURE -> "snowyGrassLargePatchesSide";
                case BlockTextureList.PRIMITIVE_DOOR_BASE_TEXTURE -> "primitiveDoorBase";
                case BlockTextureList.TWINE_TEXTURE -> "twine";
                //Items
                case BlockTextureList.ITEM_STONE_TEXTURE -> "item-stone";
                case BlockTextureList.ITEM_BERRY_TEXTURE -> "item-berry";
                case BlockTextureList.ITEM_LEAF_TEXTURE -> "item-leaf";
                case BlockTextureList.ITEM_FIREWOOD_TEXTURE -> "item-fireWood";
                case BlockTextureList.ITEM_RAW_GAME_MEAT_TEXTURE -> "item-rawGameMeat";
                case BlockTextureList.ITEM_COOKED_GAME_MEAT_TEXTURE -> "item-cookedGameMeat";
                case BlockTextureList.ITEM_STRAW_TEXTURE -> "item-straw";
                case BlockTextureList.ITEM_TWINE_TEXTURE -> "item-twine";
                case BlockTextureList.ITEM_REED_TOP_TEXTURE -> "item-reedTop";
                case BlockTextureList.ITEM_CLAY_TEXTURE -> "item-clay";
                case BlockTextureList.ITEM_FIRED_RED_CLAY_TEXTURE -> "item-firedRedClay";
                case BlockTextureList.ITEM_MUD_TEXTURE -> "item-mud";
                case BlockTextureList.ITEM_REED_STALK_TEXTURE -> "item-reedStalk";
                case BlockTextureList.ITEM_LOG_TEXTURE -> "item-log";
                case BlockTextureList.ITEM_DEER_PELT_TOP_TEXTURE -> "item-deerPeltTop";
                case BlockTextureList.ITEM_ANIMAL_PELT_UNDER_TEXTURE -> "item-animalPeltUnder";
                case BlockTextureList.ITEM_WOLF_PELT_TOP_TEXTURE -> "item-wolfPeltTop";
                case BlockTextureList.ITEM_ROT_TEXTURE -> "item-rot";
                case BlockTextureList.ITEM_PRIMITIVE_DOOR_BASE_TEXTURE -> "item-primitiveDoorBase";
                case BlockTextureList.WATER_SIDE_TEXTURE -> "waterSide"; //Side
                case BlockTextureList.WATER_BOTTOM_TEXTURE -> "water"; //Bottom
                case BlockTextureList.WATER_NORTH_FLOW_TEXTURE -> "water"; //northFlow
                case BlockTextureList.WATER_SOUTH_FLOW_TEXTURE -> "water"; //southFlow
                case BlockTextureList.WATER_EAST_FLOW_TEXTURE -> "water"; //eastFlow
                case BlockTextureList.WATER_WEST_FLOW_TEXTURE -> "water"; //westFlow
                case BlockTextureList.WATER_SIDE_TEXTURE_2 -> "waterSide";
                case BlockTextureList.TILLED_SOIL_TEXTURE -> "tilledSoil";
                case BlockTextureList.ITEM_SEED_WHEAT_FAMILY_TEXTURE -> "item-seedWheatFamily";
                case BlockTextureList.ITEM_BONE_TEXTURE -> "item-bone";
                case BlockTextureList.ITEM_BONEMEAL_TEXTURE -> "item-boneMeal";
                case BlockTextureList.WILD_GRASS_TEXTURE -> "wildGrass";
                case BlockTextureList.EINKORN_WHEAT_1_TEXTURE -> "einkornWheat-1";
                case BlockTextureList.CROP_SEED_TEXTURE -> "cropSeed";
                case BlockTextureList.FERTILIZER_TEXTURE -> "fertilizer";
                case BlockTextureList.EINKORN_WHEAT_2_TEXTURE -> "einkornWheat-2";
                case BlockTextureList.EINKORN_WHEAT_3_TEXTURE -> "einkornWheat-3";
                case BlockTextureList.EINKORN_WHEAT_4_TEXTURE -> "einkornWheat-4";
                case BlockTextureList.EINKORN_WHEAT_5_TEXTURE -> "einkornWheat-5";
                case BlockTextureList.EINKORN_WHEAT_6_TEXTURE -> "einkornWheat-6";
                case BlockTextureList.EINKORN_WHEAT_7_TEXTURE -> "einkornWheat-7";
                case BlockTextureList.EINKORN_WHEAT_8_TEXTURE -> "einkornWheat-8";
                case BlockTextureList.ITEM_EINKORN_WHEAT -> "item-einkornWheat";
                case BlockTextureList.WHEAT_1_TEXTURE -> "wheat-1";
                case BlockTextureList.WHEAT_2_TEXTURE -> "wheat-2";
                case BlockTextureList.WHEAT_3_TEXTURE -> "wheat-3";
                case BlockTextureList.WHEAT_4_TEXTURE -> "wheat-4";
                case BlockTextureList.WHEAT_5_TEXTURE -> "wheat-5";
                case BlockTextureList.WHEAT_6_TEXTURE -> "wheat-6";
                case BlockTextureList.WHEAT_7_TEXTURE -> "wheat-7";
                case BlockTextureList.WHEAT_8_TEXTURE -> "wheat-8";
                case BlockTextureList.ITEM_WHEAT_TEXTURE -> "item-wheat";
                case BlockTextureList.DEAD_CROP -> "deadCrop";
                case BlockTextureList.SNOWY_GRASS_FULL_SIDE_TEXTURE -> "snowyGrassFullSide";
                case BlockTextureList.SNOWY_GRASS_FULL_SIDE_WITH_CLAY_TEXTURE -> "snowyGrassSideWithClay";
                case BlockTextureList.ICE_TEXTURE -> "ice";
                case BlockTextureList.SOIL_LOW_FERTILITY_TEXTURE -> "soilLowFertility";
                case BlockTextureList.SOIL_BARREN_FERTILITY_TEXTURE -> "soilBarrenFertility";
                case BlockTextureList.SOIL_HIGH_FERTILITY_TEXTURE -> "soilHighFertility";
                case BlockTextureList.SOIL_BARREN_FERTILITY_FULL_GRASS_LOWER -> "soilBarrenFertilityFullGrassLower";
                case BlockTextureList.SOIL_BARREN_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE -> "soilBarrenFertilityLargeGrassPatchesLowerSide";
                case BlockTextureList.SOIL_BARREN_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE -> "soilBarrenFertilitySmallGrassPatchesLowerSide";
                case BlockTextureList.SOIL_BARREN_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP -> "soilBarrenFertilityLargeGrassPatchesLowerTop";
                case BlockTextureList.SOIL_BARREN_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP -> "soilBarrenFertilitySmallGrassPatchesLowerTop";
                case BlockTextureList.SOIL_LOW_FERTILITY_FULL_GRASS_LOWER -> "soilLowFertilityFullGrassLower";
                case BlockTextureList.SOIL_LOW_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE -> "soilLowFertilityLargeGrassPatchesLowerSide";
                case BlockTextureList.SOIL_LOW_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE -> "soilLowFertilitySmallGrassPatchesLowerSide";
                case BlockTextureList.SOIL_LOW_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP -> "soilLowFertilityLargeGrassPatchesLowerTop";
                case BlockTextureList.SOIL_LOW_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP -> "soilLowFertilitySmallGrassPatchesLowerTop";
                case BlockTextureList.SOIL_MEDIUM_FERTILITY_FULL_GRASS_LOWER -> "soilMediumFertilityFullGrassLower";
                case BlockTextureList.SOIL_MEDIUM_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE -> "soilMediumFertilityLargeGrassPatchesLowerSide";
                case BlockTextureList.SOIL_MEDIUM_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE -> "soilMediumFertilitySmallGrassPatchesLowerSide";
                case BlockTextureList.SOIL_MEDIUM_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP -> "soilMediumFertilityLargeGrassPatchesLowerTop";
                case BlockTextureList.SOIL_MEDIUM_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP -> "soilMediumFertilitySmallGrassPatchesLowerTop";
                case BlockTextureList.SOIL_HIGH_FERTILITY_FULL_GRASS_LOWER -> "soilHighFertilityFullGrassLower";
                case BlockTextureList.SOIL_HIGH_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE -> "soilHighFertilityLargeGrassPatchesLowerSide";
                case BlockTextureList.SOIL_HIGH_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE -> "soilHighFertilitySmallGrassPatchesLowerSide";
                case BlockTextureList.SOIL_HIGH_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP -> "soilHighFertilityLargeGrassPatchesLowerTop";
                case BlockTextureList.SOIL_HIGH_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP -> "soilHighFertilitySmallGrassPatchesLowerTop";
                case BlockTextureList.CLAY_FULL_GRASS_LOWER -> "clayFullGrassLower";
                case BlockTextureList.CLAY_LARGE_GRASS_PATCHES_LOWER_SIDE -> "clayLargeGrassPatchesLowerSide";
                case BlockTextureList.CLAY_SMALL_GRASS_PATCHES_LOWER_SIDE -> "claySmallGrassPatchesLowerSide";
                case BlockTextureList.CLAY_LARGE_GRASS_PATCHES_LOWER_TOP -> "clayLargeGrassPatchesLowerTop";
                case BlockTextureList.CLAY_SMALL_GRASS_PATCHES_LOWER_TOP -> "claySmallGrassPatchesLowerTop";
                case BlockTextureList.ANDESITE_STONE -> "andesiteStone";
                case BlockTextureList.ANDESITE_GRAVEL -> "andesiteGravel";
                case BlockTextureList.ANDESITE_SAND -> "andesiteSand";
                case BlockTextureList.GRANITE_STONE -> "graniteStone";
                case BlockTextureList.GRANITE_GRAVEL -> "graniteGravel";
                case BlockTextureList.GRANITE_SAND -> "graniteSand";
                case BlockTextureList.PERIODITE_STONE -> "perioditeStone";
                case BlockTextureList.PERIODITE_GRAVEL -> "perioditeGravel";
                case BlockTextureList.PERIODITE_SAND -> "perioditeSand";
                case BlockTextureList.OBSIDIAN_SAND -> "obsidianSand";
                case BlockTextureList.BASALT_STONE -> "basaltStone";
                case BlockTextureList.BASALT_GRAVEL -> "basaltGravel";
                case BlockTextureList.BASALT_SAND -> "basaltSand";
                case BlockTextureList.GABBRO_STONE -> "gabbroStone";
                case BlockTextureList.GABBRO_GRAVEL -> "gabbroGravel";
                case BlockTextureList.GABBRO_SAND -> "gabbroSand";
                case BlockTextureList.CHALK_STONE -> "chalkStone";
                case BlockTextureList.CHALK_GRAVEL -> "chalkGravel";
                case BlockTextureList.CHALK_SAND -> "chalkSand";
                case BlockTextureList.CHERT_STONE ->  "chertStone";
                case BlockTextureList.CHERT_GRAVEL -> "chertGravel";
                case BlockTextureList.CHERT_SAND -> "chertSand";
                case BlockTextureList.CLAYSTONE_STONE -> "claystoneStone";
                case BlockTextureList.CLAYSTONE_GRAVEL -> "claystoneGravel";
                case BlockTextureList.CLAYSTONE_SAND -> "claystoneSand";
                case BlockTextureList.CONGLOMERATE_STONE -> "conglomerateStone";
                case BlockTextureList.CONGLOMERATE_GRAVEL -> "conglomerateGravel";
                case BlockTextureList.CONGLOMERATE_SAND -> "conglomerateSand";
                case BlockTextureList.SHALE_STONE_SIDE_TEXTURE -> "shaleStoneSide";
                case BlockTextureList.SHALE_STONE_TOP_TEXTURE -> "shaleStoneTop";
                case BlockTextureList.SHALE_GRAVEL_TEXTURE -> "shaleGravel";
                case BlockTextureList.SHALE_SAND_TEXTURE -> "shaleSand";
                case BlockTextureList.SHALE_STONE_BOTTOM_TEXTURE -> "shaleStoneBottom";
                case BlockTextureList.LIMESTONE_STONE -> "limestoneStone";
                case BlockTextureList.LIMESTONE_GRAVEL -> "limestoneGravel";
                case BlockTextureList.LIMESTONE_SAND -> "limestoneSand";
                case BlockTextureList.SANDSTONE_STONE -> "sandstoneStone";
                case BlockTextureList.SANDSTONE_GRAVEL -> "sandstoneGravel";
                case BlockTextureList.SANDSTONE_SAND -> "sandstoneSand";
                case BlockTextureList.MARBLE_STONE -> "marbleStone";
                case BlockTextureList.MARBLE_GRAVEL -> "marbleGravel";
                case BlockTextureList.MARBLE_SAND -> "marbleSand";
                case BlockTextureList.SLATE_STONE -> "slateStone";
                case BlockTextureList.SLATE_GRAVEL -> "slateGravel";
                case BlockTextureList.SLATE_SAND -> "slateSand";
                case BlockTextureList.PHYLLITE_STONE -> "phylliteStone";
                case BlockTextureList.PHYLLITE_GRAVEL -> "phylliteGravel";
                case BlockTextureList.PHYLLITE_SAND -> "phylliteSand";
                case BlockTextureList.SERPENTINITE_STONE -> "serpentiniteStone";
                case BlockTextureList.SERPENTINITE_GRAVEL -> "serpentiniteGravel";
                case BlockTextureList.SERPENTINITE_SAND -> "serpentiniteSand";
                default -> "missing";
            };
        } else if(textureFolderpath.contains("item")){
            return switch (textureNumber) {
                case 0,1 -> "missing";
                case ItemTextureList.STONE_TEXTURE -> "stone";
                case ItemTextureList.BERRY_TEXTURE -> "berry";
                case ItemTextureList.LEAF_TEXTURE -> "leaf";
                case ItemTextureList.FIREWOOD_TEXTURE -> "fireWood";
                case ItemTextureList.RAW_GAME_MEAT_TEXTURE -> "rawGameMeat";
                case ItemTextureList.COOKED_GAME_MEAT_TEXTURE -> "cookedGameMeat";
                case ItemTextureList.STRAW_TEXTURE -> "straw";
                case ItemTextureList.TWINE_TEXTURE -> "twine";
                case ItemTextureList.REED_TOP_TEXTURE -> "reedTop";
                case ItemTextureList.CLAY_TEXTURE -> "clay";
                case ItemTextureList.FIRED_RED_CLAY_TEXTURE -> "firedRedClay";
                case ItemTextureList.MUD_TEXTURE -> "mud";
                case ItemTextureList.REED_STALK_TEXTURE -> "reedStalk";
                case ItemTextureList.LOG_TEXTURE -> "log";
                case ItemTextureList.DEER_PELT_TOP_TEXTURE -> "deerPeltTop";
                case ItemTextureList.ANIMAL_PELT_UNDER_TEXTURE -> "animalPeltUnder";
                case ItemTextureList.WOLF_PELT_TOP_TEXTURE -> "wolfPeltTop";
                case ItemTextureList.ROT_TEXTURE -> "rot";
                case ItemTextureList.PRIMITIVE_DOOR_BASE_TEXTURE -> "primitiveDoorBase";
                case ItemTextureList.SEED_WHEAT_FAMILY_TEXTURE -> "seedWheatFamily";
                case ItemTextureList.BONE_TEXTURE -> "bone";
                case ItemTextureList.BONEMEAL_TEXTURE -> "boneMeal";
                case ItemTextureList.EINKORN_WHEAT_TEXTURE -> "einkornWheat";
                case ItemTextureList.WHEAT_TEXTURE -> "wheat";
                default -> "missing";
            };
        }
        return "missing";
    }

    public TextureAtlas createTextureAtlas(int imageWidth, int imageHeight, int texWidth, int texHeight, int numTex,int spacing){
        return new TextureAtlas(imageWidth, imageHeight, texWidth, texHeight, numTex, spacing);
    }

    public static final class Tessellator {
        public static final Tessellator instance = new Tessellator();
        public FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(524288);
        public IntBuffer elementBuffer = BufferUtils.createIntBuffer(524288);
        private int elementOffset = 0;
        public int texture2DVAO;
        private int texture2DAtlasVAO;
        private int textureCubeMapVAO;
        private int textureCelestialBodyVAO;
        private int toolTipVAO;
        public int vboID;
        public int eboID;
        public boolean isOrtho;
        private int boundTexture;

        //top, bottom, north, south, east, west

        /*
        top, x high z low, x low z high, x high z high, x low z low
        bottom, x low z low, x high z high, x low z high, x high z low
        north y low z low, y high z high, y high z low, y low z high
        south y low z high, y high z low, y high z high, y low z low
        east x high y low, x low y high, x high y high, x low y low
        west x low y low, x high y high, x low y high, x high y low
         */

        private Tessellator() {
            RenderEngine renderEngine = CosmicEvolution.instance.renderEngine;


            this.vboID = renderEngine.createBuffers();
            this.eboID = renderEngine.createBuffers();

            this.texture2DVAO = renderEngine.createVAO();
            this.texture2DAtlasVAO = renderEngine.createVAO();
            this.textureCubeMapVAO = renderEngine.createVAO();
            this.textureCelestialBodyVAO = renderEngine.createVAO();
            this.toolTipVAO = renderEngine.createVAO();

            int positionsSize = 3;
            int colorSize = 4;
            int texIndexSize = 1;
            int texCoordsSize = 2;
            int normalSize = 3;
            int imageSize = 1;
            int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES;

            renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);
            renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 3, texIndexSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES, this.vboID);

            vertexSizeBytes = (positionsSize + colorSize + texCoordsSize) * Float.BYTES;

            renderEngine.setVertexAttribute(this.texture2DVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            renderEngine.setVertexAttribute(this.texture2DVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            renderEngine.setVertexAttribute(this.texture2DVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);


            vertexSizeBytes = positionsSize * Float.BYTES;
            renderEngine.setVertexAttribute(this.textureCubeMapVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);

            vertexSizeBytes = (positionsSize + normalSize) * Float.BYTES;
            renderEngine.setVertexAttribute(this.textureCelestialBodyVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            renderEngine.setVertexAttribute(this.textureCelestialBodyVAO, 1, normalSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);


            vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize + imageSize) * Float.BYTES;
            renderEngine.setVertexAttribute(this.toolTipVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            renderEngine.setVertexAttribute(this.toolTipVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            renderEngine.setVertexAttribute(this.toolTipVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);
            renderEngine.setVertexAttribute(this.toolTipVAO, 3, texIndexSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES, this.vboID);
            renderEngine.setVertexAttribute(this.toolTipVAO, 4, imageSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES, this.vboID);
        }


        public void addVertexTooltipAtlas(int colorValue, float x, float y, float z, int corner, Texture texture, float textureID, int image){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(1f);
            this.vertexBuffer.put(texture.texCoords[corner].x);
            this.vertexBuffer.put(texture.texCoords[corner].y);
            this.vertexBuffer.put(textureID);
            this.vertexBuffer.put(image);
        }

        public void addVertexTooltipArray(int colorValue, float x, float y, float z, float uvX, float uvY, float textureID, int image){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(1f);
            this.vertexBuffer.put(uvX);
            this.vertexBuffer.put(uvY);
            this.vertexBuffer.put(textureID);
            this.vertexBuffer.put(image);
        }


        public void addVertex2DTextureWithAtlas(int colorValue, float x, float y, float z, int corner, Texture textureID, float blockID, int alphaValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = MathUtil.intToFloatRGBA(alphaValue);

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(textureID.texCoords[corner].x);
            this.vertexBuffer.put(textureID.texCoords[corner].y);
            this.vertexBuffer.put(blockID);
        }

        public void addVertex2DTexture(int colorValue, float x, float y, float z, int corner){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);

            switch (corner) {
                case 0 -> {
                    this.vertexBuffer.put(1f);
                    this.vertexBuffer.put(1f);
                }
                case 1 -> {
                    this.vertexBuffer.put(1f);
                    this.vertexBuffer.put(0f);
                }
                case 2 -> {
                    this.vertexBuffer.put(0f);
                    this.vertexBuffer.put(0f);
                }
                case 3 -> {
                    this.vertexBuffer.put(0F);
                    this.vertexBuffer.put(1F);
                }
            }
        }
        //Negative x shifts to the left, positive x shifts to the right, negative y shifts up, positive y shifts down
        //Corner order is Top right, bottom left, bottom right, top left for sampling top face/bottom face
        //Bottom left, Top right, top left, bottom right for n, s, e, w faces
        public void addVertex2DTextureWithSampling(int colorValue, float x, float y, float z, int corner, float xSample, float ySample){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);

            switch (corner) {
                case 0 -> {
                    this.vertexBuffer.put(1f + xSample);
                    this.vertexBuffer.put(1f + ySample);
                }
                case 1 -> {
                    this.vertexBuffer.put(1f + xSample);
                    this.vertexBuffer.put(0f + ySample);
                }
                case 2 -> {
                    this.vertexBuffer.put(0f + xSample);
                    this.vertexBuffer.put(0f + ySample);
                }
                case 3 -> {
                    this.vertexBuffer.put(0f + xSample);
                    this.vertexBuffer.put(1f + ySample);
                }
            }
        }

        public void addVertexCubeMap(float x, float y, float z){
            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
        }

        public void addVertexCubeMapCelestialBody(Vector3f normal, float x, float y, float z){
            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(normal.x);
            this.vertexBuffer.put(normal.y);
            this.vertexBuffer.put(normal.z);
        }

        public void addVertexTextureArrayWithCorner(int colorValue, float x, float y, float z, int corner, float blockID){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            float[] texCoords = this.texCoords(corner);
            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(texCoords[0]);
            this.vertexBuffer.put(texCoords[1]);
            this.vertexBuffer.put(blockID);
        }

        public void addVertexTextureArrayWithUV(int colorValue, float x, float y, float z, float blockID, float uvX, float uvY){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(uvX);
            this.vertexBuffer.put(uvY);
            this.vertexBuffer.put(blockID);
        }

        public void addVertexTextureArrayWithSampling(int colorValue, float x, float y, float z, int corner, float blockID, float xSample, float ySample){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            float[] texCoords = this.texCoords(corner);
            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(texCoords[0] + xSample);
            this.vertexBuffer.put(texCoords[1] + ySample);
            this.vertexBuffer.put(blockID);
        }

        public void addElementsCW(){
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 3);
            this.elementOffset += 4;
        }

        public void addElementsCCW(){
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 3);
            this.elementOffset += 4;
        }


        private float[] texCoords(int corner) {

            switch (corner) {
                case 3 -> {
                    return new float[]{0, 1};
                }
                case 1 -> {
                    return new float[]{1, 0};
                }
                case 2 -> {
                    return new float[2];
                }
                case 0 -> {
                    return new float[]{1, 1};
                }
            }
            return new float[2];
        }

        public void drawTexture2DWithAtlas(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DAtlasVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

            GL46.glUseProgram(shader.shaderProgramID);



            if(this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("uTextures", 0);
            GL46.glEnable(GL46.GL_ALPHA_TEST);
            GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);


            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
            GL46.glDisable(GL46.GL_ALPHA_TEST);
            this.reset();
        }

        public void drawTexture2D(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

            //bind shader program
            GL46.glUseProgram(shader.shaderProgramID);


            //upload texture to shader
            if(this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("uTexture", 0);
            GL46.glEnable(GL46.GL_ALPHA_TEST);
            GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);

            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
            GL46.glDisable(GL46.GL_ALPHA_TEST);
            this.reset();
        }

        public void drawToolTip(){
            GL46.glBindVertexArray(this.toolTipVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, Assets.fontTextureLoader);

            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, Assets.textBox);

            GL46.glActiveTexture(GL46.GL_TEXTURE2);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, Assets.blockTextureArray);

            GL46.glActiveTexture(GL46.GL_TEXTURE3);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, Assets.itemTextureArray);

            GL46.glActiveTexture(GL46.GL_TEXTURE4);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, Assets.mouseIcon);

            GL46.glUseProgram(Shader.toolTipShader.shaderProgramID);

            GL46.glEnable(GL46.GL_ALPHA_TEST);
            GL46.glAlphaFunc(GL46.GL_GREATER, 0f);

            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);
            //Starts bound at texture 4
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

            GL46.glActiveTexture(GL46.GL_TEXTURE3);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

            GL46.glActiveTexture(GL46.GL_TEXTURE2);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

            GL46.glDisable(GL46.GL_ALPHA_TEST);
            this.reset();
        }

        public void drawTextureArray(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DAtlasVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);



            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.boundTexture);

            //bind shader program
            GL46.glUseProgram(shader.shaderProgramID);

            //upload texture to shader
            if(this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("textureArray", 0);

            //enable the vertex attribute pointers


            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);



            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

            this.reset();
        }


        public void drawCubeMapTexture(int texID, Shader shader, Camera camera) {
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.textureCubeMapVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);


            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, this.boundTexture);

            GL46.glUseProgram(shader.shaderProgramID);

            if (this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("cubeTexture", 0);

            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, 0);
            this.reset();
        }

        public void drawCubeMapTextureCelestialBody(int texID, Shader shader, Camera camera) {
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.textureCelestialBodyVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);


            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, this.boundTexture);

            GL46.glUseProgram(shader.shaderProgramID);

            if (this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("cubeTexture", 0);

            GL46.glEnable(GL46.GL_ALPHA_TEST);
            GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);

            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);


            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, 0);
            GL46.glDisable(GL46.GL_ALPHA_TEST);
            this.reset();
        }


        private void reset(){
            if(this.vertexBuffer != null){
                this.vertexBuffer.clear();
            }

            if(this.elementBuffer != null){
                this.elementBuffer.clear();
            }
            this.elementOffset = 0;
        }

        public void toggleOrtho(){
            this.isOrtho = !this.isOrtho;
        }



    }

    //All vertices within this class are assumed to use vertex normals and the skylighting values for lighting, block lighting is encoded within the color values in the vertex buffer
    public static final class WorldTessellator {
        public static final WorldTessellator instance = new WorldTessellator(1048576);
        public FloatBuffer vertexBuffer;
        public IntBuffer elementBuffer;
        private int elementOffset = 0;
        private int texture2DVAO;
        private int texture2DAtlasVAO;
        private int textureCubeMapVAO;
        private int vboID;
        private int eboID;
        private int[] texSlots = new int[256];
        public boolean isOrtho;
        private int boundTexture;



        private WorldTessellator(int quadLimit){
            this.vertexBuffer = BufferUtils.createFloatBuffer(quadLimit * 40);
            this.elementBuffer = BufferUtils.createIntBuffer(quadLimit * 6);
            this.vboID = CosmicEvolution.instance.renderEngine.createBuffers();
            this.eboID = CosmicEvolution.instance.renderEngine.createBuffers();

            this.texture2DVAO = CosmicEvolution.instance.renderEngine.createVAO();
            this.texture2DAtlasVAO = CosmicEvolution.instance.renderEngine.createVAO();
            this.textureCubeMapVAO = CosmicEvolution.instance.renderEngine.createVAO();

            int positionsSize = 3;
            int colorSize = 4;
            int texIndexSize = 1;
            int texCoordsSize = 2;
            int normalSize = 3;
            int skyLightValueSize = 1;
            int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize + skyLightValueSize + normalSize) * Float.BYTES;


            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 3, texIndexSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 4, normalSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 5, skyLightValueSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize + normalSize) * Float.BYTES, this.vboID);

            vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + normalSize + skyLightValueSize) * Float.BYTES;

            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 3, normalSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 4, skyLightValueSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + normalSize) * Float.BYTES, this.vboID);



            vertexSizeBytes = (positionsSize + colorSize + normalSize + skyLightValueSize) * Float.BYTES;
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.textureCubeMapVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.textureCubeMapVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.textureCubeMapVAO, 2, normalSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.textureCubeMapVAO, 3, skyLightValueSize, vertexSizeBytes, (positionsSize + colorSize + normalSize) * Float.BYTES, this.vboID);
        }

        public void addVertex2DTextureWithAtlas(int colorValue, float x, float y, float z, int corner, Texture textureID, float blockID, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }


            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(textureID.texCoords[corner].x);
            this.vertexBuffer.put(textureID.texCoords[corner].y);
            this.vertexBuffer.put(blockID);
            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertex2DTexture(int colorValue, float x, float y, float z, int corner, float normalX, float normalY, float normalZ, float skyLightValue, int alphaValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = MathUtil.intToFloatRGBA(alphaValue);

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);

            switch (corner) {
                case 0 -> {
                    this.vertexBuffer.put(1f);
                    this.vertexBuffer.put(1f);
                }
                case 1 -> {
                    this.vertexBuffer.put(1f);
                    this.vertexBuffer.put(0f);
                }
                case 2 -> {
                    this.vertexBuffer.put(0f);
                    this.vertexBuffer.put(0f);
                }
                case 3 -> {
                    this.vertexBuffer.put(0F);
                    this.vertexBuffer.put(1F);
                }
            }

            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertex2DTextureWithUV(int colorValue, float x, float y, float z, float normalX, float normalY, float normalZ, float skyLightValue, int alphaValue, float uvX, float uvY){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = MathUtil.intToFloatRGBA(alphaValue);

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);

            this.vertexBuffer.put(uvX);
            this.vertexBuffer.put(uvY);

            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertex2DTextureWithSampling(int colorValue, float x, float y, float z, int corner, float xSample, float ySample, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);

            switch (corner) {
                case 0 -> {
                    this.vertexBuffer.put(1f + xSample);
                    this.vertexBuffer.put(1f + ySample);
                }
                case 1 -> {
                    this.vertexBuffer.put(1f + xSample);
                    this.vertexBuffer.put(0f + ySample);
                }
                case 2 -> {
                    this.vertexBuffer.put(0f + xSample);
                    this.vertexBuffer.put(0f + ySample);
                }
                case 3 -> {
                    this.vertexBuffer.put(0f + xSample);
                    this.vertexBuffer.put(1f + ySample);
                }
            }

            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertexCubeMap(int colorValue, float x, float y, float z, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }


            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertexTextureArray(int colorValue, float x, float y, float z, int corner, float blockID, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;


            float[] texCoords = this.texCoords(corner);
            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(texCoords[0]);
            this.vertexBuffer.put(texCoords[1]);
            this.vertexBuffer.put(blockID);
            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertexTextureArrayWithUV(int colorValue, float x, float y, float z, float blockID, float normalX, float normalY, float normalZ, float skyLightValue, float uvX, float uvY){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(uvX);
            this.vertexBuffer.put(uvY);
            this.vertexBuffer.put(blockID);
            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addVertexTextureArrayWithSampling(int colorValue, float x, float y, float z, int corner, float blockID, float xSample, float ySample, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }


            final float red = MathUtil.intToFloatRGBA((colorValue >> 16) & 255);
            final float green = MathUtil.intToFloatRGBA((colorValue >> 8) & 255);
            final float blue = MathUtil.intToFloatRGBA(colorValue & 255);
            final float alpha = 1f;

            float[] texCoords = this.texCoords(corner);
            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
            this.vertexBuffer.put(texCoords[0] + xSample);
            this.vertexBuffer.put(texCoords[1] + ySample);
            this.vertexBuffer.put(blockID);
            this.vertexBuffer.put(normalX);
            this.vertexBuffer.put(normalY);
            this.vertexBuffer.put(normalZ);
            this.vertexBuffer.put(skyLightValue);
        }

        public void addElementsCW(){
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 3);
            this.elementOffset += 4;
        }

        public void addElementsCCW(){
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 3);
            this.elementOffset += 4;
        }

        private float[] texCoords(int corner) {

            switch (corner) {
                case 3 -> {
                    return new float[]{0, 1};
                }
                case 1 -> {
                    return new float[]{1, 0};
                }
                case 2 -> {
                    return new float[2];
                }
                case 0 -> {
                    return new float[]{1, 1};
                }
            }
            return new float[2];
        }

        public void drawTexture2DWithAtlas(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DAtlasVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

            GL46.glUseProgram(shader.shaderProgramID);

            if(this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("uTextures", 0);
            GL46.glEnable(GL46.GL_ALPHA_TEST);
            GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
            //bind the VAO being used


            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);


            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
            GL46.glDisable(GL46.GL_ALPHA_TEST);
            this.reset();
        }

        public void drawTexture2D(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

            if(CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.size() > 0) {
                GL46.glActiveTexture(GL46.GL_TEXTURE1);
                GL46.glBindTexture(GL46.GL_TEXTURE_2D, CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.get(0).shadowMap.depthMap);
            }

            //bind shader program
            GL46.glUseProgram(shader.shaderProgramID);

            //upload texture to shader
            Shader.worldShader2DTexture.uploadInt("shadowMap", 1);
            if(this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("uTexture", 0);
            GL46.glEnable(GL46.GL_ALPHA_TEST);
            GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);



            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);


            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
            GL46.glActiveTexture(GL46.GL_TEXTURE0);

            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
            GL46.glDisable(GL46.GL_ALPHA_TEST);
            this.reset();
        }

        public void drawTextureArray(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DAtlasVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

            int indexCount  = this.elementBuffer.limit();
            int vertexCount = this.vertexBuffer.limit();

            if (indexCount == 0 || vertexCount == 0) {
                this.reset();
                return;
            }


            int maxIndex = -1;
            for (int i = this.elementBuffer.position(); i < this.elementBuffer.limit(); i++) {
                int idx = this.elementBuffer.get(i);
                if (idx > maxIndex) maxIndex = idx;
            }
            if (maxIndex >= vertexCount) {
                this.reset();
                return;
            }

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.boundTexture);

            if(CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.size() > 0) {
                GL46.glActiveTexture(GL46.GL_TEXTURE1);
                GL46.glBindTexture(GL46.GL_TEXTURE_2D, CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.get(0).shadowMap.depthMap);
            }

            //bind shader program
            GL46.glUseProgram(shader.shaderProgramID);

            //upload texture to shader
            Shader.worldShaderTextureArray.uploadInt("shadowMap", 1);
            if(this.isOrtho) {
                shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
                shader.uploadMat4d("uView", new Matrix4d());
            } else {
                shader.uploadMat4d("uProjection", camera.projectionMatrix);
                shader.uploadMat4d("uView", camera.viewMatrix);
            }
            shader.uploadInt("textureArray", 0);


            GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

            this.reset();
        }


        private void reset(){
            if(this.vertexBuffer != null){
                this.vertexBuffer.clear();
            }

            if(this.elementBuffer != null){
                this.elementBuffer.clear();
            }
            this.elementOffset = 0;
        }

        public void toggleOrtho(){
            this.isOrtho = !this.isOrtho;
        }


    }
}
