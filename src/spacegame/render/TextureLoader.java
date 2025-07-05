package spacegame.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import spacegame.core.SpaceGame;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public final class TextureLoader {
    public String filepath;
    public int texID;
    public int width;
    public int height;

    public TextureLoader(String filepath, int imageWidth, int imageHeight) {
        this.filepath = filepath;
        this.width = imageWidth;
        this.height = imageHeight;

        File file = new File(this.filepath);
        if (!file.exists()) {
            this.filepath = SpaceGame.imageFallbackPath;
            this.width = 32;
            this.height = 32;
        }


        //Generate the texture on GPU
        this.texID = GL46.glGenTextures();
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.texID);

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
        ByteBuffer image = STBImage.stbi_load(this.filepath, width, height, channels, 0);


        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

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
    }

    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.texID);
    }

    public void unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }


}