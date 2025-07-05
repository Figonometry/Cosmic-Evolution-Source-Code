package spacegame.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import spacegame.core.SpaceGame;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class TextureCubeMapLoader {
    public String folderPath;
    public int texID;
    public int width;
    public int height;

    public TextureCubeMapLoader(String folderPath) {
        this.folderPath = folderPath;

        File file = new File(this.folderPath);
        if (!file.exists()) {
            this.folderPath = SpaceGame.imageFallbackPath;
            this.width = 32;
            this.height = 32;
        }


        //Generate the texture on GPU
        this.texID = GL46.glGenTextures();
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, this.texID);

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
                    image = STBImage.stbi_load(this.folderPath + "/posX.png" ,width, height, channels, 0);

                    if (image != null) {
                        this.width = width.get(0);
                        this.height = height.get(0);

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
                        assert false : "Error: (labyrinthgame.Texture) Could not load image '" + this.folderPath + "/posX.png" + "'";
                    }

                    STBImage.stbi_image_free(image);
                    break;
                case 1:
                    width = BufferUtils.createIntBuffer(1);
                    height = BufferUtils.createIntBuffer(1);
                    channels = BufferUtils.createIntBuffer(1);
                    image = STBImage.stbi_load(this.folderPath + "/negX.png" ,width, height, channels, 0);

                    if (image != null) {
                        this.width = width.get(0);
                        this.height = height.get(0);

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
                        assert false : "Error: (labyrinthgame.Texture) Could not load image '" + this.folderPath + "/posX.png" + "'";
                    }

                    STBImage.stbi_image_free(image);
                    break;
                case 2:
                    width = BufferUtils.createIntBuffer(1);
                    height = BufferUtils.createIntBuffer(1);
                    channels = BufferUtils.createIntBuffer(1);
                    image = STBImage.stbi_load(this.folderPath + "/posY.png" ,width, height, channels, 0);

                    if (image != null) {
                        this.width = width.get(0);
                        this.height = height.get(0);

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
                        assert false : "Error: (labyrinthgame.Texture) Could not load image '" + this.folderPath + "/posX.png" + "'";
                    }

                    STBImage.stbi_image_free(image);
                    break;
                case 3:
                    width = BufferUtils.createIntBuffer(1);
                    height = BufferUtils.createIntBuffer(1);
                    channels = BufferUtils.createIntBuffer(1);
                    image = STBImage.stbi_load(this.folderPath + "/negY.png" ,width, height, channels, 0);

                    if (image != null) {
                        this.width = width.get(0);
                        this.height = height.get(0);

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
                        assert false : "Error: (labyrinthgame.Texture) Could not load image '" + this.folderPath + "/posX.png" + "'";
                    }

                    STBImage.stbi_image_free(image);
                    break;
                case 4:
                    width = BufferUtils.createIntBuffer(1);
                    height = BufferUtils.createIntBuffer(1);
                    channels = BufferUtils.createIntBuffer(1);
                    image = STBImage.stbi_load(this.folderPath + "/posZ.png" ,width, height, channels, 0);

                    if (image != null) {
                        this.width = width.get(0);
                        this.height = height.get(0);

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
                        assert false : "Error: (labyrinthgame.Texture) Could not load image '" + this.folderPath + "/posX.png" + "'";
                    }

                    STBImage.stbi_image_free(image);
                    break;
                case 5:
                    width = BufferUtils.createIntBuffer(1);
                    height = BufferUtils.createIntBuffer(1);
                    channels = BufferUtils.createIntBuffer(1);
                    image = STBImage.stbi_load(this.folderPath + "/negZ.png" ,width, height, channels, 0);

                    if (image != null) {
                        this.width = width.get(0);
                        this.height = height.get(0);

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
                        assert false : "Error: (labyrinthgame.Texture) Could not load image '" + this.folderPath + "/posX.png" + "'";
                    }

                    STBImage.stbi_image_free(image);
                    break;
            }
        }



        GL46.glGenerateMipmap(GL46.GL_TEXTURE_CUBE_MAP);
    }


    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.texID);
    }

    public void unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }


}
