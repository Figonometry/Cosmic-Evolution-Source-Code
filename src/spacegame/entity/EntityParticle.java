package spacegame.entity;

import org.joml.Vector3d;
import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.core.GameSettings;
import spacegame.core.SpaceGame;
import spacegame.render.Assets;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.world.WorldFace;

import java.awt.*;

public final class EntityParticle extends EntityNonLiving {
    public boolean useGravity;
    public boolean useLight;
    public int timer;
    public short associatedBlock;
    public int chunkX;
    public int chunkY;
    public int chunkZ;
    public float red;
    public float green;
    public float blue;

    public EntityParticle(double x, double y, double z, boolean useGravity, int timer, short associatedBlock, boolean useLight){
        this.x = x;
        this.y = y;
        this.z = z;
        this.useGravity = useGravity;
        this.timer = timer;
        this.associatedBlock = associatedBlock;
        this.useLight = useLight;
    }



    @Override
    public void tick(){
        this.timer--;
        if(this.useGravity){
            this.doGravity();
        } else {
            this.y += 0.00666f;
        }

        if(this.timer <= 0){
            this.despawn = true;
        }
    }


    @Override
    public void render(){
        Tessellator tessellator = Tessellator.instance;
        this.chunkX = (int)this.x >> 5;
        this.chunkY = (int)this.y >> 5;
        this.chunkZ = (int)this.z >> 5;
        Shader.worldShaderTextureArray.uploadBoolean("useFog", true);
        Shader.worldShaderTextureArray.uploadFloat("fogRed", SpaceGame.instance.save.activeWorld.skyColor[0]);
        Shader.worldShaderTextureArray.uploadFloat("fogGreen", SpaceGame.instance.save.activeWorld.skyColor[1]);
        Shader.worldShaderTextureArray.uploadFloat("fogBlue", SpaceGame.instance.save.activeWorld.skyColor[2]);
        Shader.worldShaderTextureArray.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
        int playerChunkX = ((int)SpaceGame.instance.save.thePlayer.x >> 5);
        int playerChunkY = ((int)SpaceGame.instance.save.thePlayer.y >> 5);
        int playerChunkZ = ((int)SpaceGame.instance.save.thePlayer.z >> 5);
        int xOffset = ((int)this.x >> 5) - playerChunkX;
        int yOffset = ((int)this.y >> 5) - playerChunkY;
        int zOffset = ((int)this.z >> 5) - playerChunkZ;
        xOffset *= 32;
        yOffset *= 32;
        zOffset *= 32;
        Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
        Shader.worldShaderTextureArray.uploadVec3f("chunkOffset", chunkOffset);
        WorldFace worldFace = SpaceGame.instance.save.activeWorld.activeWorldFace;
        float x = (float) (this.x % 32);
        float y = (float) (this.y % 32);
        float z = (float) (this.z % 32);

        if(x < 0){
            x += 32;
        }

        if(y < 0){
            y += 32;
        }

        if(z < 0){
            z += 32;
        }

        float blockID = Block.list[this.associatedBlock].textureID;

        float size = 0.015625f;
        Vector3d blockPosition = new Vector3d(x, (float) (y + this.height/2) + 0.125, z);
        Vector3d vertex1 = new Vector3d(0,  -size, -size).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d vertex2 = new Vector3d(0, size, size).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d vertex3 = new Vector3d(0, size, -size).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d vertex4 = new Vector3d(0, -size, size).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        this.resetLight();
        if(this.useLight) {
            this.setVertexLight1Arg(worldFace.getBlockLightValue((int) (this.chunkX * 32 + x), (int) (this.chunkY * 32 + y), (int) (this.chunkZ * 32 + z)), x, y, z, worldFace.getBlockLightColor((int) (this.chunkX * 32 + x), (int) (this.chunkY * 32 + y), (int) (this.chunkZ * 32 + z)));
        }
        int colorValue = new Color(this.red, this.green, this.blue, 0).getRGB();
        tessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex1.x, (float) vertex1.y, (float) vertex1.z, 3, blockID, 0.46875f, -0.46875f);
        tessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex2.x, (float) vertex2.y, (float) vertex2.z, 1, blockID, -0.46875f, 0.46875f);
        tessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex3.x, (float) vertex3.y, (float) vertex3.z, 2, blockID, 0.46875f, 0.46875f);
        tessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex4.x, (float) vertex4.y, (float) vertex4.z, 0, blockID, -0.46875f, -0.46875f);
        tessellator.addElements();
        Shader.worldShaderTextureArray.uploadBoolean("blocks", false);
        tessellator.drawVertexArray(Assets.blockTextureArray.arrayID, Shader.worldShaderTextureArray, SpaceGame.camera);
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

    private float getLightValueFromMap(byte lightValue) {
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
}
