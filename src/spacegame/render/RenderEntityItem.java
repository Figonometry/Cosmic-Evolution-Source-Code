package spacegame.render;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.item.ItemTool;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.item.Item;

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
    public double entityHeight;
    public double entityWidth;
    public int chunkX;
    public int chunkY;
    public int chunkZ;
    public float entityYaw;

    public RenderEntityItem(double x, double y, double z, ModelLoader entityModel, boolean render3D, boolean isBlock, short itemID, short itemMetadata, double entityHeight, double entityWidth, float entityYaw){
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
        this.entityYaw = entityYaw;
    }


    public void renderEntity(){
        if(this.render3D){
            this.renderEntityAsBlock();
        } else {
            this.renderEntityAsItemModel();
        }
    }


    public void renderEntityAsItemModel() {
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;
        Shader.worldShaderTextureArray.uploadBoolean("useFog", true);
        Shader.worldShaderTextureArray.uploadFloat("fogRed", CosmicEvolution.instance.save.activeWorld.skyColor[0]);
        Shader.worldShaderTextureArray.uploadFloat("fogGreen", CosmicEvolution.instance.save.activeWorld.skyColor[1]);
        Shader.worldShaderTextureArray.uploadFloat("fogBlue", CosmicEvolution.instance.save.activeWorld.skyColor[2]);
        Shader.worldShaderTextureArray.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
        int playerChunkX = (MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5);
        int playerChunkY = (MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5);
        int playerChunkZ = (MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5);
        int xOffset = (MathUtil.floorDouble(this.x) >> 5) - playerChunkX;
        int yOffset = (MathUtil.floorDouble(this.y) >> 5) - playerChunkY;
        int zOffset = (MathUtil.floorDouble(this.z) >> 5) - playerChunkZ;
        xOffset *= 32;
        yOffset *= 32;
        zOffset *= 32;
        Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
        Shader.worldShaderTextureArray.uploadVec3f("chunkOffset", chunkOffset);
        float x = MathUtil.positiveMod(this.x, 32);
        float y = MathUtil.positiveMod(this.y, 32);
        float z = MathUtil.positiveMod(this.z, 32);

        if (x < 0) {
            x += 32;
        }

        if (y < 0) {
            y += 32;
        }

        if (z < 0) {
            z += 32;
        }

        float skyLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z)));

        float textureID;

        int colorRGB;

        ModelLoader model = Item.list[this.itemID].itemModel.copyModel();
        if(Item.list[this.itemID] instanceof ItemTool){
            model = model.rotateModel(90, 0, 0, 1);
        }
        model = model.rotateModel(this.entityYaw, 0, 1, 0);
        model = model.translateModel(x,y,z);


        for (int i = 0; i < model.modelFaces.length; i++) {
            if (model.modelFaces[i] == null) continue;

            textureID = model.modelFaces[i].texture;


            for(int j = 0; j < model.modelFaces[i].vertices.length; j++){
                this.setVertexLight1Arg(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(MathUtil.floorDouble(model.modelFaces[i].vertices[j].x + (this.chunkX << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].y + (this.chunkY << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].z + (this.chunkZ << 5))),
                        CosmicEvolution.instance.save.activeWorld.getBlockLightColor(MathUtil.floorDouble(model.modelFaces[i].vertices[j].x + (this.chunkX << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].y + (this.chunkY << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].z + (this.chunkZ << 5))));

                colorRGB = MathUtil.floatToIntRGBA(this.red) << 16 | MathUtil.floatToIntRGBA(this.green) << 8 | MathUtil.floatToIntRGBA(this.blue);

                worldTessellator.addVertexTextureArrayWithUV(colorRGB, model.modelFaces[i].vertices[j].x, model.modelFaces[i].vertices[j].y, model.modelFaces[i].vertices[j].z, textureID, model.modelFaces[i].normal.x, model.modelFaces[i].normal.y, model.modelFaces[i].normal.z, skyLight ,model.modelFaces[i].UVs[j][0], model.modelFaces[i].UVs[j][1]);
            }

            worldTessellator.addElementsCCW();
        }

        GL46.glCullFace(GL46.GL_BACK);
        worldTessellator.drawTextureArray(Assets.itemTextureArray, Shader.worldShaderTextureArray, CosmicEvolution.camera);
        GL46.glCullFace(GL46.GL_FRONT);
    }

    public void renderEntityAsBlock(){
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;

        ModelLoader model = Block.list[this.blockID].blockModel.copyModel().translateModel( -0.5f, 0, -0.5f).getScaledModel(0.25f);
        Shader.worldShaderTextureArray.uploadBoolean("useFog", true);
        Shader.worldShaderTextureArray.uploadFloat("fogRed", CosmicEvolution.instance.save.activeWorld.skyColor[0]);
        Shader.worldShaderTextureArray.uploadFloat("fogGreen", CosmicEvolution.instance.save.activeWorld.skyColor[1]);
        Shader.worldShaderTextureArray.uploadFloat("fogBlue", CosmicEvolution.instance.save.activeWorld.skyColor[2]);
        Shader.worldShaderTextureArray.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
        int playerChunkX = (MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5);
        int playerChunkY = (MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5);
        int playerChunkZ = (MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5);
        int xOffset = (MathUtil.floorDouble(this.x) >> 5) - playerChunkX;
        int yOffset = (MathUtil.floorDouble(this.y) >> 5) - playerChunkY;
        int zOffset = (MathUtil.floorDouble(this.z) >> 5) - playerChunkZ;
        xOffset <<= 5;
        yOffset <<= 5;
        zOffset <<= 5;
        Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
        Shader.worldShaderTextureArray.uploadVec3f("chunkOffset", chunkOffset);

        float textureID;
        int colorRGB;

        float x = MathUtil.positiveMod(this.x, 32);
        float y = MathUtil.positiveMod(this.y, 32);
        float z = MathUtil.positiveMod(this.z, 32);

        if (x < 0) {
            x += 32;
        }

        if (y < 0) {
            y += 32;
        }

        if (z < 0) {
            z += 32;
        }

        model = model.rotateModel(this.entityYaw, 0, 1, 0);
        model = model.translateModel(x,y,z);

        float skyLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z)));

        for (int i = 0; i < model.modelFaces.length; i++) {
            if (model.modelFaces[i] == null) continue;

            textureID = model.usesMultipleTextures ? model.modelFaces[i].texture : Block.list[this.blockID].getBlockTexture(this.blockID, model.modelFaces[i].faceType);


            for(int j = 0; j < model.modelFaces[i].vertices.length; j++){
                this.setVertexLight1Arg(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(MathUtil.floorDouble(model.modelFaces[i].vertices[j].x + (this.chunkX << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].y + (this.chunkY << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].z + (this.chunkZ << 5))),
                        CosmicEvolution.instance.save.activeWorld.getBlockLightColor(MathUtil.floorDouble(model.modelFaces[i].vertices[j].x + (this.chunkX << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].y + (this.chunkY << 5)),
                                MathUtil.floorDouble(model.modelFaces[i].vertices[j].z + (this.chunkZ << 5))));

                colorRGB = MathUtil.floatToIntRGBA(this.red) << 16 | MathUtil.floatToIntRGBA(this.green) << 8 | MathUtil.floatToIntRGBA(this.blue);

                worldTessellator.addVertexTextureArrayWithUV(colorRGB, model.modelFaces[i].vertices[j].x, model.modelFaces[i].vertices[j].y, model.modelFaces[i].vertices[j].z, textureID, model.modelFaces[i].normal.x, model.modelFaces[i].normal.y, model.modelFaces[i].normal.z, skyLight ,model.modelFaces[i].UVs[j][0], model.modelFaces[i].UVs[j][1]);
            }

            worldTessellator.addElementsCCW();
        }

        GL46.glCullFace(GL46.GL_BACK);
        worldTessellator.drawTextureArray(Assets.blockTextureArray, Shader.worldShaderTextureArray, CosmicEvolution.camera);
        GL46.glCullFace(GL46.GL_FRONT);
    }


    public void renderItemForShadowMap(int sunX, int sunY, int sunZ){
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;

        float x = MathUtil.positiveMod(this.x, 32);
        float y = MathUtil.positiveMod(this.y, 32);
        float z = MathUtil.positiveMod(this.z,32);

        if(x < 0){
            x += 32;
        }

        if(y < 0){
            y += 32;
        }

        if(z < 0){
            z += 32;
        }

        ModelLoader model = Item.list[this.itemID].itemModel.copyModel();
        if(Item.list[this.itemID] instanceof ItemTool){
            model = model.rotateModel(90, 0, 0, 1);
        }
        model = model.rotateModel(this.entityYaw, 0, 1, 0);
        model = model.translateModel(x,y + 0.4f,z);

        for (int i = 0; i < model.modelFaces.length; i++) {
            if (model.modelFaces[i] == null) continue;

            for(int j = 0; j < model.modelFaces[i].vertices.length; j++){
                worldTessellator.addVertexTextureArrayWithUV(0, model.modelFaces[i].vertices[j].x, model.modelFaces[i].vertices[j].y, model.modelFaces[i].vertices[j].z, 0,0, 0, 0, 0 , 0,0);
            }

            worldTessellator.addElementsCCW();
        }

        Shader.shadowMapShaderTextureArray.uploadVec3f("chunkOffset", new Vector3f((chunkX - sunX) << 5, (chunkY - sunY) << 5, (chunkZ - sunZ) << 5));
        worldTessellator.drawTextureArray(Assets.blockTextureArray, Shader.shadowMapShaderTextureArray, CosmicEvolution.camera); //For whatever reason the item texture array doesn't draw properly from this
    }

    public void renderBlockForShadowMap(int sunX, int sunY, int sunZ){
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;

        float x = MathUtil.positiveMod(this.x, 32);
        float y = MathUtil.positiveMod(this.y, 32);
        float z = MathUtil.positiveMod(this.z,32);

        if(x < 0){
            x += 32;
        }

        if(y < 0){
            y += 32;
        }

        if(z < 0){
            z += 32;
        }

        ModelLoader model = Block.list[this.blockID].blockModel.copyModel().translateModel( -0.5f, 0, -0.5f).getScaledModel(0.25f);

        model = model.rotateModel(this.entityYaw, 0, 1, 0);
        model = model.translateModel(x,y + 0.4f,z);

        float skyLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z)));

        for (int i = 0; i < model.modelFaces.length; i++) {
            if (model.modelFaces[i] == null) continue;



            for(int j = 0; j < model.modelFaces[i].vertices.length; j++){
                worldTessellator.addVertexTextureArrayWithUV(0, model.modelFaces[i].vertices[j].x, model.modelFaces[i].vertices[j].y, model.modelFaces[i].vertices[j].z, 0, 0,0,0,0,0,0);
            }

            worldTessellator.addElementsCCW();
        }


        Shader.shadowMapShaderTextureArray.uploadVec3f("chunkOffset", new Vector3f((chunkX - sunX) << 5, (chunkY - sunY) << 5, (chunkZ - sunZ) << 5));
        worldTessellator.drawTextureArray(Assets.blockTextureArray, Shader.shadowMapShaderTextureArray, CosmicEvolution.camera);
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

    private  void setVertexLight1Arg(byte light, float[] lightColor) {
        this.resetLight();
        float finalLight = getLightValueFromMap(light);

        red = lightColor[0];
        green = lightColor[1];
        blue = lightColor[2];

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }


    public float getItemTextureID(short ID, short metadata, int face){
        return Item.list[ID].getTextureID(ID, metadata, face);
    }


}
