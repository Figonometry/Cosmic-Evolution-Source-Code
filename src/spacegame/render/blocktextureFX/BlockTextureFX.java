package spacegame.render.blocktextureFX;

import org.lwjgl.BufferUtils;
import spacegame.core.CosmicEvolution;
import spacegame.render.Assets;

import java.nio.IntBuffer;

public abstract class BlockTextureFX {
    private static int textureFXIndex = 0;
    public static final BlockTextureFX[] blockTextureFXList = new BlockTextureFX[10];
    private static final BlockTextureFX waterTextureFX = new WaterTextureFX();
    private static final BlockTextureFX fireTextureFX = new FireTextureFX();
    private final IntBuffer image = BufferUtils.createIntBuffer(1024);

    public BlockTextureFX(){
        blockTextureFXList[textureFXIndex] = this;
        textureFXIndex++;
    }

    public abstract void update();


    //The parameter is used to update the various copies of water without needing to have 8 different textureFX loops,
    // can be used to target specific textures, default passed in is a min value int
    protected void updateTexture(int textureIndex, int[] red, int[] green, int[] blue, int[] alpha){

        for(int i = 0; i <red.length; i++){
            this.image.put(red[i] << 24 | green[i] << 16 | blue[i] << 8 | alpha[i]);
        }

        this.image.flip();

        CosmicEvolution.instance.renderEngine.updateTextureInTextureArray(
                Assets.blockTextureArray, textureIndex, 32,32, image);

        this.image.clear();
    }

}
