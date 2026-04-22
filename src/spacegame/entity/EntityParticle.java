package spacegame.entity;

import org.joml.Vector3d;
import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.render.*;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.world.World;

public final class EntityParticle extends EntityNonLiving {
    public boolean useGravity;
    public boolean useLight;
    public boolean despawnOnGroundContact;
    public boolean collideWithGround;
    public int timer;
    public short associatedBlock;
    public int chunkX;
    public int chunkY;
    public int chunkZ;
    public int fillableColor;
    public boolean stopParticle;
    public boolean renderAsCube;
    public float size = 0.015625f;
    public float sizeSegment;
    public float red;
    public float green;
    public float blue;
    public float shiftXRight;
    public float shiftYUp;
    public float shiftXLeft;
    public float shiftYDown;


    public EntityParticle(double x, double y, double z, boolean useGravity, int timer,
                          short associatedBlock, boolean useLight, boolean despawnOnGroundContact, boolean collideWithGround, boolean renderAsCube, float shiftX, float shiftY){
        this.x = x;
        this.y = y;
        this.z = z;
        this.useGravity = useGravity;
        this.timer = timer;
        this.associatedBlock = associatedBlock;
        this.useLight = useLight;
        this.despawnOnGroundContact = despawnOnGroundContact;
        this.collideWithGround = collideWithGround;
        this.speed = 0.05f;
        this.acceleration = 0.005;
        this.renderAsCube = renderAsCube;

        this.shiftXRight = shiftX;
        this.shiftYDown = shiftY;

        this.shiftXLeft = 1.0f - (this.shiftXRight + 0.03125f);
        this.shiftYUp =  1.0f - (this.shiftYDown + 0.03125f);

        this.shiftXLeft *= -1;
        this.shiftYUp *= -1;

        this.sizeSegment = this.size / timer;
    }



    @Override
    public void tick(){
        this.timer--;
        if(this.renderAsCube){
            this.size -= this.sizeSegment * 2;
        }

        World world = CosmicEvolution.instance.save.activeWorld;

        if(!this.stopParticle) {
            if (this.canMoveWithVector) {
                this.moveWithVector();
            }
        }


            if (this.useGravity) {
                this.doGravity();
                this.moveOnly();
                if (this.collideWithGround) {
                    if (Block.list[world.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z))].isSolid) {
                        this.y = Block.list[world.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z))].standardCollisionBoundingBox.maxY + MathUtil.floorDouble(this.y);
                        this.timeFalling = 0;
                        this.canMoveWithVector = false;
                        this.deltaX = 0;
                        this.deltaZ = 0;
                        this.deltaY = 0;
                    }
                }
            } else {
                this.y += 0.00666f;
            }

        if(this.timer <= 0){
            this.despawn = true;
        }

        if(this.despawnOnGroundContact){
            if(Block.list[world.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z))].isSolid){
                this.despawn = true;
            }
        }
    }


    @Override
    public void render(){
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;
        Shader.worldShaderTextureArray.uploadBoolean("useFog", true);
        Shader.worldShaderTextureArray.uploadFloat("fogRed", CosmicEvolution.instance.save.activeWorld.skyColor[0]);
        Shader.worldShaderTextureArray.uploadFloat("fogGreen", CosmicEvolution.instance.save.activeWorld.skyColor[1]);
        Shader.worldShaderTextureArray.uploadFloat("fogBlue", CosmicEvolution.instance.save.activeWorld.skyColor[2]);
        Shader.worldShaderTextureArray.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
        int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
        int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
        int xOffset = this.chunkX - playerChunkX;
        int yOffset = this.chunkY - playerChunkY;
        int zOffset = this.chunkZ - playerChunkZ;

        xOffset *= 32;
        yOffset *= 32;
        zOffset *= 32;

        Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
        Shader.worldShaderTextureArray.uploadVec3f("chunkOffset", chunkOffset);
        World world = CosmicEvolution.instance.save.activeWorld;
        float x = MathUtil.positiveMod(this.x, 32);
        float y = MathUtil.positiveMod(this.y, 32);
        float z = MathUtil.positiveMod(this.z, 32);

        float blockID = Block.list[this.associatedBlock].textureID;

        if(this.renderAsCube){
            Vector3d blockPosition = new Vector3d(x, (float) (y + this.height/2) + 0.125, z);
            Vector3d vertex1;
            Vector3d vertex2;
            Vector3d vertex3;
            Vector3d vertex4;
            Vector3d vertex5;
            this.resetLight();

            if(Block.list[this.associatedBlock].colorize){
                blockID = 0;
            }

            if(this.associatedBlock == Block.itemStick.ID){
                blockID = Block.oakLogFullSizeNormal.textureID;
            }

            if(this.useLight) {
                byte lightLevel = world.getBlockLightValue(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z));
                this.setVertexLight1Arg(lightLevel, x, y, z, world.getBlockLightColor(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z)));
            }
            int colorValue = (int)(this.red * 255) << 16 | (int)(this.green * 255) << 8 | (int)(this.blue * 255);


            ModelLoader blockModel = Block.grass.blockModel.copyModel();

            float skyLightValue = this.getLightValueFromMap(world.getBlockSkyLightValue(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z)));

            for(int i = 0 ; i < blockModel.modelFaces.length; i++){
                blockModel =  Block.grass.blockModel.copyModel().getScaledModel(this.size);

                vertex1 =  new Vector3d(blockModel.modelFaces[i].vertices[0]).add(blockPosition);
                vertex2 =  new Vector3d(blockModel.modelFaces[i].vertices[1]).add(blockPosition);
                vertex3 =  new Vector3d(blockModel.modelFaces[i].vertices[2]).add(blockPosition);
                vertex4 =  new Vector3d(blockModel.modelFaces[i].vertices[3]).add(blockPosition);
                vertex5 = new Vector3d(blockModel.modelFaces[i].normal);


                    //Left and up are negatives

                    worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex1.x, (float) vertex1.y, (float) vertex1.z, 3, blockID, this.shiftXRight, this.shiftYUp, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, skyLightValue);
                    worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex2.x, (float) vertex2.y, (float) vertex2.z, 1, blockID, this.shiftXLeft, this.shiftYDown, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, skyLightValue);
                    worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex3.x, (float) vertex3.y, (float) vertex3.z, 2, blockID, this.shiftXRight, this.shiftYDown, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, skyLightValue);
                    worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex4.x, (float) vertex4.y, (float) vertex4.z, 0, blockID, this.shiftXLeft, this.shiftYUp, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, skyLightValue);
                    worldTessellator.addElements();
            }


            Shader.worldShaderTextureArray.uploadBoolean("performNormals", true);
            Shader.worldShaderTextureArray.uploadBoolean("isColorCorrected", Block.list[this.associatedBlock].colorize);
            worldTessellator.drawTextureArray(Assets.blockTextureArray, Shader.worldShaderTextureArray, CosmicEvolution.camera);
            Shader.worldShaderTextureArray.uploadBoolean("performNormals", false);
            Shader.worldShaderTextureArray.uploadBoolean("isColorCorrected", false);
        } else {
            Vector3d blockPosition = new Vector3d(x, (float) (y + this.height/2) + 0.125, z);
            Vector3d vertex1 = new Vector3d(0,  -size, -size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            Vector3d vertex2 = new Vector3d(0, size, size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            Vector3d vertex3 = new Vector3d(0, size, -size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            Vector3d vertex4 = new Vector3d(0, -size, size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            this.resetLight();
            if(this.useLight) {
                byte lightLevel = world.getBlockLightValue(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z));
                this.setVertexLight1Arg(lightLevel, x, y, z, world.getBlockLightColor(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z)));
            }
            int colorValue = (int)(this.red * 255) << 16 | (int)(this.green * 255) << 8 | (int)(this.blue * 255);
            worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex1.x, (float) vertex1.y, (float) vertex1.z, 3, blockID, 0.46875f, -0.46875f, 0, 0, 0, 0);
            worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex2.x, (float) vertex2.y, (float) vertex2.z, 1, blockID, -0.46875f, 0.46875f, 0, 0, 0, 0);
            worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex3.x, (float) vertex3.y, (float) vertex3.z, 2, blockID, 0.46875f, 0.46875f, 0, 0, 0, 0);
            worldTessellator.addVertexTextureArrayWithSampling(colorValue, (float) vertex4.x, (float) vertex4.y, (float) vertex4.z, 0, blockID, -0.46875f, -0.46875f, 0, 0, 0, 0);
            worldTessellator.addElements();
            Shader.worldShaderTextureArray.uploadBoolean("performNormals", false);
            worldTessellator.drawTextureArray(Assets.blockTextureArray, Shader.worldShaderTextureArray, CosmicEvolution.camera);
        }
    }


    @Override
    public void renderForShadowMap(int sunX, int sunY, int sunZ){
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;

        Shader.shadowMapShaderTextureArray.uploadVec3f("chunkOffset", new Vector3f((chunkX - sunX) << 5, (chunkY - sunY) << 5, (chunkZ - sunZ) << 5));
        float x = MathUtil.positiveMod(this.x, 32);
        float y = MathUtil.positiveMod(this.y, 32);
        float z = MathUtil.positiveMod(this.z, 32);

        float blockID = Block.list[this.associatedBlock].textureID;

        if(this.renderAsCube){
            Vector3d blockPosition = new Vector3d(x, (float) (y + this.height/2) + 0.125, z);
            Vector3d vertex1;
            Vector3d vertex2;
            Vector3d vertex3;
            Vector3d vertex4;
            Vector3d vertex5;

            ModelLoader blockModel = Block.grass.blockModel.copyModel();

            for(int i = 0 ; i < blockModel.modelFaces.length; i++){
                blockModel =  Block.grass.blockModel.copyModel().getScaledModel(this.size);

                vertex1 =  new Vector3d(blockModel.modelFaces[i].vertices[0]).add(blockPosition);
                vertex2 =  new Vector3d(blockModel.modelFaces[i].vertices[1]).add(blockPosition);
                vertex3 =  new Vector3d(blockModel.modelFaces[i].vertices[2]).add(blockPosition);
                vertex4 =  new Vector3d(blockModel.modelFaces[i].vertices[3]).add(blockPosition);
                vertex5 = new Vector3d(blockModel.modelFaces[i].normal);


                //Left and up are negatives

                worldTessellator.addVertexTextureArrayWithSampling(16777215, (float) vertex1.x, (float) vertex1.y, (float) vertex1.z, 3, blockID, this.shiftXRight, this.shiftYUp, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, 1);
                worldTessellator.addVertexTextureArrayWithSampling(16777215, (float) vertex2.x, (float) vertex2.y, (float) vertex2.z, 1, blockID, this.shiftXLeft, this.shiftYDown, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, 1);
                worldTessellator.addVertexTextureArrayWithSampling(16777215, (float) vertex3.x, (float) vertex3.y, (float) vertex3.z, 2, blockID, this.shiftXRight, this.shiftYDown, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, 1);
                worldTessellator.addVertexTextureArrayWithSampling(16777215, (float) vertex4.x, (float) vertex4.y, (float) vertex4.z, 0, blockID, this.shiftXLeft, this.shiftYUp, (float) vertex5.x, (float) vertex5.y, (float) vertex5.z, 1);
                worldTessellator.addElements();
            }

            worldTessellator.drawTextureArray(0, Shader.shadowMapShaderTextureArray, CosmicEvolution.camera);
        } else {
            Vector3d blockPosition = new Vector3d(x, (float) (y + this.height/2) + 0.125, z);
            Vector3d vertex1 = new Vector3d(0,  -size, -size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            Vector3d vertex2 = new Vector3d(0, size, size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            Vector3d vertex3 = new Vector3d(0, size, -size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);
            Vector3d vertex4 = new Vector3d(0, -size, size).rotateY(-Math.toRadians(CosmicEvolution.instance.save.thePlayer.yaw)).add(blockPosition);

            worldTessellator.addVertexTextureArrayWithSampling(0, (float) vertex1.x, (float) vertex1.y, (float) vertex1.z, 3, blockID, 0.46875f, -0.46875f, 0, 0, 0, 0);
            worldTessellator.addVertexTextureArrayWithSampling(0, (float) vertex2.x, (float) vertex2.y, (float) vertex2.z, 1, blockID, -0.46875f, 0.46875f, 0, 0, 0, 0);
            worldTessellator.addVertexTextureArrayWithSampling(0, (float) vertex3.x, (float) vertex3.y, (float) vertex3.z, 2, blockID, 0.46875f, 0.46875f, 0, 0, 0, 0);
            worldTessellator.addVertexTextureArrayWithSampling(0, (float) vertex4.x, (float) vertex4.y, (float) vertex4.z, 0, blockID, -0.46875f, -0.46875f, 0, 0, 0, 0);
            worldTessellator.addElements();
            worldTessellator.drawTextureArray(Assets.blockTextureArray, Shader.shadowMapShaderTextureArray, CosmicEvolution.camera);
        }
    }


    private  void resetLight() {
        red = 1F;
        green = 1F;
        blue = 1F;
    }

    private  void setVertexLight1Arg(byte light, float x, float y, float z, float[] lightColor) {
        float finalLight = getLightValueFromMap(light);

        if(Block.list[this.associatedBlock].colorize){
            World world = CosmicEvolution.instance.save.activeWorld;
            int bx = MathUtil.floorDouble(this.x);
            int by = MathUtil.floorDouble(this.y);
            int bz = MathUtil.floorDouble(this.z);
            int color = this.associatedBlock == Block.grass.ID  || this.associatedBlock == Block.tallGrass.ID || this.associatedBlock == Block.grassWithClay.ID ? PlantColorizer.getGrassColor(world.getTemperature(bx, by, bz), world.getRainfall(bx, bz)) : PlantColorizer.getOakLeafColor(world.getTemperature(bx, by, bz), world.getRainfall(bx, bz));
            this.red = ((color >> 16) & 255) / 255f;
            this.green = ((color >> 8) & 255) / 255f;
            this.blue = (color & 255) / 255f;

            float highestChannel = 0;
            highestChannel = Math.max(this.red, this.green);
            highestChannel = Math.max(highestChannel, this.blue);

            highestChannel = highestChannel != 0.0 ? highestChannel : 0.01f;

            red *= lightColor[0];
            green *= lightColor[1];
            blue *= lightColor[2];

            red *= finalLight;
            green *= finalLight;
            blue *= finalLight;

            float highestChannelAfter = 0;
            highestChannelAfter = Math.max(this.red, this.green);
            highestChannelAfter = Math.max(highestChannelAfter, this.blue);

            highestChannelAfter = highestChannelAfter != 0.0 ? highestChannelAfter : 0.01f;

            Shader.worldShaderTextureArray.uploadFloat("colorMultiplier", highestChannel / highestChannelAfter);
        } else {
            red = lightColor[0];
            green = lightColor[1];
            blue = lightColor[2];

            red *= finalLight;
            green *= finalLight;
            blue *= finalLight;
        }

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
