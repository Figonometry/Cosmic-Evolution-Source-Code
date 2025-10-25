package spacegame.render;

import org.joml.Matrix4d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import spacegame.core.CosmicEvolution;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class RenderEngine {
    public static final int TEXTURE_TYPE_2D = 1;
    public static final int TEXTURE_TYPE_2D_ARRAY = 2;
    public static final int TEXTURE_TYPE_CUBEMAP = 3;


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

    public void setVertexAttribute(int vaoID, int index, int size, int vertexSizeBytes, int pointer, int vboID){
        GL46.glBindVertexArray(vaoID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        GL46.glVertexAttribPointer(index, size, GL46.GL_FLOAT, false, vertexSizeBytes, pointer);
        GL46.glEnableVertexAttribArray(index);
    }

    public int createTexture(String filepath, int textureType, int arraySize){
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
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_CLAMP_TO_EDGE);
                GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_CLAMP_TO_EDGE);
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
        if (!new File(imageFilepath).exists()) {
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
                case 0 -> "grass";
                case 1 -> "dirt";
                case 2 -> "grassSideTop";
                case 3 -> "torch";
                case 4 -> "water";
                case 5 -> "sand";
                case 6 -> "snow";
                case 7 -> "stone";
                case 8 -> "logSide";
                case 9 -> "logTop";
                case 10 -> "leaf";
                case 11 -> "berryBushSideBase";
                case 12 -> "berryBushTopBase";
                case 13 -> "clay";
                case 14 -> "claySideBottom";
                case 15 -> "strawTexture";
                case 16 -> "campFireBase";
                case 17 -> "firedRedClay";
                case 18 -> "fire";
                case 19 -> "emptyColor";
                case 20 -> "grassSideBottom";
                case 21 -> "cactus";
                case 22 -> "cactusTop";
                case 23 -> "cactusBottom";
                case 24 -> "leafTransparent";
                case 25 -> "berryBushSide";
                case 26 -> "berryBushTop";
                case 27 -> "berryBushFlowerSide";
                case 28 -> "berryBushFlowerTop";
                case 29 -> "itemStick";
                case 30 -> "tallGrass";
                case 31 -> "fireWood";
                case 32 -> "strawChest";
                case 33 -> "reedLower";
                case 34 -> "reedUpper";
                default -> "missing";
            };
        } else if(textureFolderpath.contains("item")){
            return switch (textureNumber) {
                case 1 -> "torch";
                case 2 -> "stone";
                case 3 -> "stoneFragments";
                case 4 -> "stoneHandAxe";
                case 5 -> "berry";
                case 6 -> "rawStick";
                case 7 -> "unlitTorch";
                case 8 -> "fireWood";
                case 9 -> "stoneHandKnife";
                case 10 -> "stoneHandShovel";
                case 11 -> "rawVenison";
                case 12 -> "straw";
                case 13 -> "strawBasket";
                case 14 -> "clay";
                case 15 -> "rawClayAdobeBrick";
                case 16 -> "clayAdobeBrick";
                case 17 -> "mud";
                case 18 -> "reeds";
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
        private int texture2DVAO;
        private int texture2DAtlasVAO;
        private int textureCubeMapVAO;
        private int vboID;
        private int eboID;
        private int[] texSlots = new int[256];
        public boolean isOrtho;
        private int boundTexture;

        //top, bottom, north, south, east, west

        private Tessellator() {
            this.vboID = CosmicEvolution.instance.renderEngine.createBuffers();
            this.eboID = CosmicEvolution.instance.renderEngine.createBuffers();

            this.texture2DVAO = CosmicEvolution.instance.renderEngine.createVAO();
            this.texture2DAtlasVAO = CosmicEvolution.instance.renderEngine.createVAO();
            this.textureCubeMapVAO = CosmicEvolution.instance.renderEngine.createVAO();

            int positionsSize = 3;
            int colorSize = 4;
            int texIndexSize = 1;
            int texCoordsSize = 2;
            int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES;

            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DAtlasVAO, 3, texIndexSize, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES, this.vboID);

            vertexSizeBytes = (positionsSize + colorSize + texCoordsSize) * Float.BYTES;

            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.texture2DVAO, 2, texCoordsSize, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES, this.vboID);


            vertexSizeBytes = (positionsSize + colorSize) * Float.BYTES;
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.textureCubeMapVAO, 0, positionsSize, vertexSizeBytes, 0, this.vboID);
            CosmicEvolution.instance.renderEngine.setVertexAttribute(this.textureCubeMapVAO, 1, colorSize, vertexSizeBytes, positionsSize * Float.BYTES, this.vboID);
        }


        public void addVertex2DTextureWithAtlas(int colorValue, float x, float y, float z, int corner, Texture textureID, float blockID, int alphaValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
            final float alpha = alphaValue / 255f;

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

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addVertexCubeMap(int colorValue, float x, float y, float z){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
            final float alpha = 1f;

            this.vertexBuffer.put(x);
            this.vertexBuffer.put(y);
            this.vertexBuffer.put(z);
            this.vertexBuffer.put(red);
            this.vertexBuffer.put(green);
            this.vertexBuffer.put(blue);
            this.vertexBuffer.put(alpha);
        }

        public void addVertexTextureArray(int colorValue, float x, float y, float z, int corner, float blockID, int faceType){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addVertexTextureArrayWithSampling(int colorValue, float x, float y, float z, int corner, float blockID, float xSample, float ySample){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addElements(){
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 1);
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
            shader.uploadIntArray("uTextures", this.texSlots);
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

        public void drawVertexArray(int texID, Shader shader, Camera camera){
            this.boundTexture = texID;

            GL46.glBindVertexArray(this.texture2DAtlasVAO);

            this.vertexBuffer.flip();
            this.elementBuffer.flip();

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

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);


            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, this.boundTexture);

            //bind shader program
            GL46.glUseProgram(shader.shaderProgramID);

            //upload texture to shader

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
            //bind the VAO being used

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
        public static final WorldTessellator instance = new WorldTessellator(8192);
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


            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addVertex2DTexture(int colorValue, float x, float y, float z, int corner, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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


            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addVertexTextureArray(int colorValue, float x, float y, float z, int corner, float blockID, int faceType, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }

            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addVertexTextureArrayWithSampling(int colorValue, float x, float y, float z, int corner, float blockID, float xSample, float ySample, float normalX, float normalY, float normalZ, float skyLightValue){
            if(colorValue > 16777215){
                colorValue = 16777215;
            }
            if(colorValue < 0){
                colorValue = 0;
            }


            final float red = ((colorValue >> 16) & 255) / 255f;
            final float green = ((colorValue >> 8) & 255) / 255f;
            final float blue = (colorValue & 255) / 255f;
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

        public void addElements(){
            this.elementBuffer.put(this.elementOffset + 2);
            this.elementBuffer.put(this.elementOffset + 1);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 0);
            this.elementBuffer.put(this.elementOffset + 1);
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
            shader.uploadIntArray("uTextures", this.texSlots);
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
            //bind the VAO being used



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
