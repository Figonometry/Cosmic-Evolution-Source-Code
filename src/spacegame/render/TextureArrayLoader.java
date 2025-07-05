package spacegame.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import spacegame.core.SpaceGame;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public final class TextureArrayLoader {
    public String textureFolderpath;
    public int arrayID;
    public int arraySize;


    public TextureArrayLoader(String textureFolderpath, int arraySize) {
        this.textureFolderpath = textureFolderpath;
        this.arraySize = arraySize;

        int textureArray = GL46.glGenTextures();
        this.arrayID = textureArray;
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, textureArray);
        GL46.glTexStorage3D(GL46.GL_TEXTURE_2D_ARRAY, 1, GL46.GL_RGBA8, 32, 32, this.arraySize);

        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST_MIPMAP_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);

        for (int i = 0; i < this.arraySize; i++) {
            this.loadTextures(i);
        }

        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D_ARRAY);
    }

    private void loadTextures(int textureNumber) {
        String imageName = getBlockName(textureNumber, this.textureFolderpath);
        String filepath = this.textureFolderpath + imageName + ".png";
        if (!new File(filepath).exists()) {
            filepath = SpaceGame.imageFallbackPath;
        }

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 0);


        if (image != null) {
            if (channels.get(0) == 3) {
                GL46.glTexSubImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, textureNumber, 32, 32, 1, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                GL46.glTexSubImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, textureNumber, 32, 32, 1, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (labyrinthgame.Texture) Unknown number of channels '" + channels.get(0) + "'";
            }
        } else {
            assert false : "Error: (labyrinthgame.Texture) Could not load image '" + filepath + "'";
        }

        STBImage.stbi_image_free(image);
    }

    public static String getBlockName(int textureNumber, String textureFolderpath) {
       if(textureFolderpath.contains("blocks")){
           return switch (textureNumber) {
               case 0 -> "grass";
               case 1 -> "dirt";
               case 2 -> "grassSide";
               case 3 -> "torch";
               case 4 -> "water";
               case 5 -> "sand";
               case 6 -> "snow";
               case 7 -> "stone";
               case 8 -> "logSide";
               case 9 -> "logTop";
               case 10 -> "leaf";
               case 11 -> "berryBush";
               case 12 -> "berryBushNoBerries";
               case 13 -> "campFire2Sticks";
               case 14 -> "campFire3Sticks";
               case 15 -> "campFire4Sticks";
               case 16 -> "campFireUnlit";
               case 17 -> "campFireLit";
               case 18 -> "fire";
               case 19 -> "campFireBurnedOut";
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
               case 8 -> "woodShards";
               default -> "missing";
           };
       }
       return "missing";
    }
}
