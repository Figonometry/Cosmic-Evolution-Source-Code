package spacegame.render;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.core.GameSettings;
import spacegame.core.MathUtils;
import spacegame.core.SpaceGame;
import spacegame.gui.GuiInGame;
import spacegame.item.Item;
import spacegame.world.WorldFace;

import java.awt.*;

public final class RenderEntityItem {
    public final double x;
    public final double y;
    public final double z;
    public ModelLoader entityModel;
    public boolean render3D;
    public boolean isBlock;
    public short itemID;
    public short blockID;
    public float red = 1;
    public float green = 1;
    public float blue = 1;
    public float alpha = 1;
    public long currentTime;
    public double entityHeight;
    public double entityWidth;
    public int chunkX;
    public int chunkY;
    public int chunkZ;

    public RenderEntityItem(double x, double y, double z, ModelLoader entityModel, boolean render3D, boolean isBlock, short itemID, short itemMetadata, double entityHeight, double entityWidth){
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityModel = entityModel;
        this.render3D = render3D;
        this.isBlock = isBlock;
        this.itemID = itemID;
        this.blockID = itemMetadata;
        this.entityHeight = entityHeight;
        this.entityWidth = entityWidth;
    }


    public void renderEntity(){
        if(this.render3D){
            this.renderEntityAs3D();
        } else {
            this.renderEntityAsBillboard();
        }
    }

    public void renderEntityAs3D(){
        if(this.isBlock){
            this.renderEntityAsBlock();
        } else {
            //Standard 3d render code, draw with Atlas with an unwrapped image
        }
    }

    public void renderEntityAsBillboard(){
        WorldTessellator worldTessellator = WorldTessellator.instance;
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
        this.currentTime = SpaceGame.instance.save.time;
        float x = (float) (this.x % 32);
        float y = (float) ((float) (this.y % 32) + 0.05F + (0.05F * ((MathUtils.sin((((double) this.currentTime / 120) * Math.PI * 2) - (0.5 * Math.PI)) * 0.5) + 0.5f)));
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

        float blockID = getItemTextureID(this.itemID, this.blockID, RenderBlocks.WEST_FACE);

        Vector3d blockPosition = new Vector3d(x, (float) (y + this.entityHeight/2) + 0.125, z);
        Vector3d vertex1 = new Vector3d(0,  -0.125, -0.125).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d vertex2 = new Vector3d(0, 0.125, 0.125).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d vertex3 = new Vector3d(0, 0.125, -0.125).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d vertex4 = new Vector3d(0, -0.125, 0.125).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw)).add(blockPosition);
        Vector3d normal = new Vector3d(0, 0, 1).rotateY(-Math.toRadians(SpaceGame.instance.save.thePlayer.yaw));
        this.resetLight();
        this.setVertexLight1Arg(worldFace.getBlockLightValue((int)(this.chunkX * 32 + x), (int)(this.chunkY * 32 + y), (int)(this.chunkZ * 32 + z)),  x, y, z, worldFace.getBlockLightColor((int)(this.chunkX * 32 + x), (int)(this.chunkY * 32 + y), (int)(this.chunkZ * 32 + z)));
        float skyLightValue = GuiInGame.getLightValueFromMap(worldFace.getBlockSkyLightValue((int)(this.chunkX * 32 + x), (int)(this.chunkY * 32 + y), (int)(this.chunkZ * 32 + z)));
        int colorValue = new Color(this.red, this.green, this.blue, 0).getRGB();
        worldTessellator.addVertexTextureArray(colorValue, (float) vertex1.x, (float) vertex1.y, (float) vertex1.z, 3, blockID, RenderBlocks.WEST_FACE, (float) normal.x, (float) normal.y, (float) normal.z, skyLightValue);
        worldTessellator.addVertexTextureArray(colorValue, (float) vertex2.x, (float) vertex2.y, (float) vertex2.z, 1, blockID, RenderBlocks.WEST_FACE, (float) normal.x, (float) normal.y, (float) normal.z, skyLightValue);
        worldTessellator.addVertexTextureArray(colorValue, (float) vertex3.x, (float) vertex3.y, (float) vertex3.z, 2, blockID, RenderBlocks.WEST_FACE, (float) normal.x, (float) normal.y, (float) normal.z, skyLightValue);
        worldTessellator.addVertexTextureArray(colorValue, (float) vertex4.x, (float) vertex4.y, (float) vertex4.z, 0, blockID, RenderBlocks.WEST_FACE, (float) normal.x, (float) normal.y, (float) normal.z, skyLightValue);
        worldTessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        GL46.glDepthMask(false);
        worldTessellator.drawTextureArray(Assets.itemTextureArray.arrayID, Shader.worldShaderTextureArray, SpaceGame.camera);
        GL46.glDepthMask(true);
        GL46.glDisable(GL46.GL_BLEND);
    }

    public void renderEntityAsBlock(){
        WorldTessellator worldTessellator = WorldTessellator.instance;
        ModelFace modelFace;
        this.currentTime = SpaceGame.instance.save.time;
        this.chunkX = MathUtils.floorDouble(this.x) >> 5;
        this.chunkY = MathUtils.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtils.floorDouble(this.z) >> 5;
        for(int i = 0; i < this.entityModel.modelFaces.length; i++){
            modelFace = this.entityModel.modelFaces[i];
            this.renderOpaqueFace(worldTessellator, SpaceGame.instance.save.activeWorld.activeWorldFace, this.blockID, modelFace.faceType, modelFace, 0,0,0,0,0,0,0,0,0,0,0,0);
            worldTessellator.addElements();
        }
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
        worldTessellator.drawTextureArray(Assets.blockTextureArray.arrayID, Shader.worldShaderTextureArray, SpaceGame.camera);
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


    private void renderOpaqueFace(WorldTessellator worldTessellator, WorldFace worldFace, short block, int face, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4) {
        float x = (float) (this.x % 32);
        float y = (float) ((float) (this.y % 32) + 0.05F + (0.05F * ((MathUtils.sin((((double) this.currentTime / 120) * Math.PI * 2) - (0.5 * Math.PI)) * 0.5) + 0.5f)));
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

        float chunkX = this.chunkX * 32;
        float chunkY = this.chunkY * 32;
        float chunkZ = this.chunkZ * 32;

        float blockID = RenderBlocks.getBlockTextureID(block, face);

        Vector3f blockPosition = new Vector3f(x, (float) (y + this.entityHeight/2), z);

        Vector3f vertex1 = new Vector3f(blockFace.vertices[0][0], blockFace.vertices[0][1], blockFace.vertices[0][2]).rotateY((float) Math.toRadians(this.currentTime % 720)).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1][0], blockFace.vertices[1][1], blockFace.vertices[1][2]).rotateY((float) Math.toRadians(this.currentTime % 720)).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2][0], blockFace.vertices[2][1], blockFace.vertices[2][2]).rotateY((float) Math.toRadians(this.currentTime % 720)).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3][0], blockFace.vertices[3][1], blockFace.vertices[3][2]).rotateY((float) Math.toRadians(this.currentTime % 720)).add(blockPosition);
        Vector3f normal = new Vector3f(blockFace.normal[0], blockFace.normal[1], blockFace.normal[2]).rotateY((float) Math.toRadians(this.currentTime % 720));

        float skyLightValue = this.getLightValueFromMap(worldFace.getBlockSkyLightValue(MathUtils.floorDouble(chunkX + x), MathUtils.floorDouble(chunkY + y), MathUtils.floorDouble(chunkZ + z)));
        this.resetLight();
        this.setVertexLight1Arg(worldFace.getBlockLightValue(MathUtils.floorDouble(chunkX + x), MathUtils.floorDouble(chunkY + y), MathUtils.floorDouble(chunkZ + z)), x, y, z, worldFace.getBlockLightColor(MathUtils.floorDouble(chunkX + x), MathUtils.floorDouble(chunkY + y), MathUtils.floorDouble(chunkZ + z)));
        int lightColor = MathUtils.RGBToInt(this.red, this.green, this.blue);
        switch (blockFace.faceType) {
            case RenderBlocks.TOP_FACE, RenderBlocks.TOP_FACE_UNSORTED -> {
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex1.x, vertex1.y, vertex1.z, 1, blockID, xSample1, ySample1, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex2.x, vertex2.y, vertex2.z, 3, blockID, xSample2, ySample2, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex3.x, vertex3.y, vertex3.z, 0, blockID, xSample3, ySample3, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex4.x, vertex4.y, vertex4.z, 2, blockID, xSample4, ySample4, normal.x, normal.y, normal.z, skyLightValue);
            }

            case RenderBlocks.BOTTOM_FACE, RenderBlocks.BOTTOM_FACE_UNSORTED -> {
                this.red -= 0.1f;
                this.green -= 0.1;
                this.blue -= 0.1f;
                if(this.red < 0.1f){
                    this.red = 0.1f;
                }
                if(this.green < 0.1f){
                    this.green = 0.1f;
                }
                if(this.blue < 0.1f){
                    this.blue = 0.1f;
                }
                lightColor = MathUtils.RGBToInt(this.red, this.green, this.blue);

                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex1.x, vertex1.y, vertex1.z, 2, blockID, xSample1, ySample1, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex2.x, vertex2.y, vertex2.z, 0, blockID, xSample2, ySample2, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex3.x, vertex3.y, vertex3.z, 3, blockID, xSample3, ySample3, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex4.x, vertex4.y, vertex4.z, 1, blockID, xSample4, ySample4, normal.x, normal.y, normal.z, skyLightValue);

            }
            case RenderBlocks.NORTH_FACE, RenderBlocks.NORTH_FACE_UNSORTED, RenderBlocks.EAST_FACE, RenderBlocks.EAST_FACE_UNSORTED, RenderBlocks.WEST_FACE, RenderBlocks.WEST_FACE_UNSORTED, RenderBlocks.SOUTH_FACE, RenderBlocks.SOUTH_FACE_UNSORTED -> {
                switch (face){
                    case RenderBlocks.NORTH_FACE -> {
                        this.red -= 0.15f;
                        this.green -= 0.15;
                        this.blue -= 0.15f;
                        if(this.red < 0.1f){
                            this.red = 0.1f;
                        }
                        if(this.green < 0.1f){
                            this.green = 0.1f;
                        }
                        if(this.blue < 0.1f){
                            this.blue = 0.1f;
                        }
                    }
                    case RenderBlocks.SOUTH_FACE -> {
                        this.red -= 0.05f;
                        this.green -= 0.05;
                        this.blue -= 0.05f;
                        if(this.red < 0.1f){
                            this.red = 0.1f;
                        }
                        if(this.green < 0.1f){
                            this.green = 0.1f;
                        }
                        if(this.blue < 0.1f){
                            this.blue = 0.1f;
                        }
                    }
                    case RenderBlocks.EAST_FACE -> {
                        this.red -= 0.1f;
                        this.green -= 0.1;
                        this.blue -= 0.1f;
                        if(this.red < 0.1f){
                            this.red = 0.1f;
                        }
                        if(this.green < 0.1f){
                            this.green = 0.1f;
                        }
                        if(this.blue < 0.1f){
                            this.blue = 0.1f;
                        }
                    }
                    case RenderBlocks.WEST_FACE -> {
                        this.red -= 0.12f;
                        this.green -= 0.12f;
                        this.blue -= 0.12f;
                        if(this.red < 0.1f){
                            this.red = 0.1f;
                        }
                        if(this.green < 0.1f){
                            this.green = 0.1f;
                        }
                        if(this.blue < 0.1f){
                            this.blue = 0.1f;
                        }
                    }
                }

                lightColor = MathUtils.RGBToInt(this.red, this.green, this.blue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex1.x, vertex1.y, vertex1.z, 3, blockID, xSample1, ySample1, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex2.x, vertex2.y, vertex2.z, 1, blockID, xSample2, ySample2, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex3.x, vertex3.y, vertex3.z, 2, blockID, xSample3, ySample3, normal.x, normal.y, normal.z, skyLightValue);
                worldTessellator.addVertexTextureArrayWithSampling(lightColor ,vertex4.x, vertex4.y, vertex4.z, 0, blockID, xSample4, ySample4, normal.x, normal.y, normal.z, skyLightValue);
            }

        }
    }




    public float getItemTextureID(short ID, short metadata, int face){
        return Item.list[ID].getTextureID(ID, metadata, face);
    }


}
