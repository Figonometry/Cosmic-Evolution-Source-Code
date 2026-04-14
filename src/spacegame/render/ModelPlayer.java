package spacegame.render;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.entity.Entity;
import spacegame.entity.EntityModelTest;
import spacegame.entity.EntityPlayer;
import spacegame.util.MathUtil;


public final class ModelPlayer extends Model {
    public static final int BODY = 0;
    public static final int HEAD = 1;
    public static final int LEFT_LEG = 2;
    public static final int RIGHT_LEG = 3;
    public static final int LEFT_ARM = 4;
    public static final int RIGHT_ARM = 5;
    //Player texture is 88x96 pixels




    public ModelPlayer(){
        this.segments = new ModelSegment[6];
        ModelSegment body = new ModelSegment(0.35f, 0.75f, 0.5f, true, null, new Vector3f(), new Vector3f());
        ModelSegment head = new ModelSegment(0.5f, 0.5f, 0.5f, false, body, new Vector3f(0, -0.25f, 0), new Vector3f(0, 0.625f, 0));
        ModelSegment leftLeg = new ModelSegment(0.25f, 0.75f, 0.25f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(0, -0.7f, -0.124f));
        ModelSegment rightLeg = new ModelSegment(0.25f, 0.75f, 0.25f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(0, -0.7f, 0.124f));
        ModelSegment leftArm = new ModelSegment(0.25f, 0.75f, 0.25f, false, body, new Vector3f(0, 0.2f, 0), new Vector3f(0, 0, -0.375f));
        ModelSegment rightArm = new ModelSegment(0.25f, 0.75f, 0.25f, false, body, new Vector3f(0, 0.2f, 0), new Vector3f(0, 0, 0.375f));

        body.setChildSegments(new ModelSegment[]{head, leftLeg, rightLeg, leftArm, rightArm});

        this.segments[0] = body;
        this.segments[1] = head;
        this.segments[2] = leftLeg;
        this.segments[3] = rightLeg;
        this.segments[4] = leftArm;
        this.segments[5] = rightArm;

    }

    @Override
    public void animate(int stepInCycle, boolean continueAnimation, Entity entity){
        this.animateWalkCycle(stepInCycle, continueAnimation, (EntityPlayer) entity);
        this.animatePunching((EntityPlayer) entity);
    }

    private void animateWalkCycle(int stepInCycle, boolean continueAnimation, EntityPlayer thePlayer){
        float legAngleMax = 10f;
        float angleRightLeg = 0;
        float angleLeftArm = 0;
        float angleLeftLeg = 0;
        float angleRightArm = 0;
        if(!thePlayer.stopRightLeg) {
            angleRightLeg = (float) (MathUtil.sin((stepInCycle / 4.775)) * legAngleMax);
        }
        if(!thePlayer.stopLeftArm) {
            angleLeftArm = (float) (MathUtil.sin(((stepInCycle - 7.5) / 4.775)) * legAngleMax);
        }
        if(!thePlayer.stopLeftLeg) {
            angleLeftLeg = (float) (MathUtil.sin(((stepInCycle - 15) / 4.775)) * legAngleMax);
        }
        if(!thePlayer.stopRightArm) {
            angleRightArm = (float) (MathUtil.sin(((stepInCycle - 22.5) / 4.775)) * legAngleMax);
        }

        if(!continueAnimation){
            if(angleLeftArm > -0.1 || angleLeftArm < 0.1){
                thePlayer.stopLeftArm = true;
            }
            if(angleRightArm > -0.1 || angleRightArm < 0.1){
                thePlayer.stopRightArm = true;
            }
            if(angleLeftLeg > -0.1 || angleLeftLeg < 0.1){
                thePlayer.stopLeftLeg = true;
            }
            if(angleRightLeg > -0.1 || angleRightLeg < 0.1){
                thePlayer.stopRightLeg = true;
            }
        }

        this.rotateSegment(LEFT_ARM, 0, 0, 1, angleLeftArm);
        this.rotateSegment(RIGHT_ARM, 0, 0, 1, angleRightArm);
        this.rotateSegment(LEFT_LEG, 0, 0, 1, angleLeftLeg);
        this.rotateSegment(RIGHT_LEG, 0, 0 , 1, angleRightLeg);
    }

    private void animatePunching(EntityPlayer player){
        if(!player.isSwinging)return;


        int phase = (player.swingTimer * 3) / 16;

        float phaseRatio = ((player.swingTimer * 3) % 16) / 16f;

        phase++;

        float phase1TargetX = -20;
        float phase1TargetZ = 70;

        float phase2TargetX = 0;
        float phase2TargetZ = 70;

        float phase3TargetX = 0;
        float phase3TargetZ = 0;

        float angleX = 0;
        float angleZ = 0;

        float angleDifX = 0;
        float angleDifZ = 0;

        switch (phase){
            case 1 ->{
                  angleDifX = (phase1TargetX - phase3TargetX) * phaseRatio;
                  angleDifZ = (phase1TargetZ - phase3TargetZ) * phaseRatio;
                  angleX = phase3TargetX + angleDifX;
                  angleZ = phase3TargetZ + angleDifZ;
            }
            case 2 ->{
                angleDifX = (phase2TargetX - phase1TargetX) * phaseRatio;
                angleDifZ = (phase2TargetZ - phase1TargetZ) * phaseRatio;
                angleX = phase1TargetX + angleDifX;
                angleZ = phase1TargetZ + angleDifZ;
            }
            case 3 ->{
                angleDifX = (phase3TargetX - phase2TargetX) * phaseRatio;
                angleDifZ = (phase3TargetZ - phase2TargetZ) * phaseRatio;
                angleX = phase2TargetX + angleDifX;
                angleZ = phase2TargetZ + angleDifZ;
            }
        }


        this.rotateSegment(RIGHT_ARM, 1, 0, 0, angleX);
        this.rotateSegment(RIGHT_ARM, 0, 0, 1, angleZ);
    }

    public static ModelPlayer getBaseModel(){
        return new ModelPlayer();
    }

    public void scale(float factor){
        for(int i = 0; i < this.segments.length; i++){
            this.segments[i].position.mul(factor);
            this.segments[i].rotationPoint.mul(factor);
            for(int j = 0; j < 4; j++){
                this.segments[i].topFace[j].mul(factor);
                this.segments[i].bottomFace[j].mul(factor);
                this.segments[i].northFace[j].mul(factor);
                this.segments[i].southFace[j].mul(factor);
                this.segments[i].eastFace[j].mul(factor);
                this.segments[i].westFace[j].mul(factor);
            }
        }
    }

    @Override
    public void renderModel(Entity associatedEntity) {
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        Vector3f position;
        Vector3f[] topFace;
        Vector3f[] bottomFace;
        Vector3f[] northFace;
        Vector3f[] southFace;
        Vector3f[] eastFace;
        Vector3f[] westFace;

        float angleDeg = associatedEntity.yaw;
        for(int i = 0; i < this.segments.length; i++){
            position = new Vector3f();
            if(!this.segments[i].isRoot) {
                ModelSegment workingSegment = this.segments[i];
                while (!workingSegment.isRoot) {
                    position.add(workingSegment.position);
                    workingSegment = workingSegment.parentSegment;
                }
            }

            position.y += 0.23f; //This is a magic number, I can't figure out why exactly it's 0.23,
            // my original assumption was that it was half the body's height since that's the root part and it's definitely not the collision system


            topFace = this.segments[i].getFaceClone(0);
            bottomFace = this.segments[i].getFaceClone(1);
            northFace = this.segments[i].getFaceClone(2);
            southFace = this.segments[i].getFaceClone(3);
            eastFace = this.segments[i].getFaceClone(4);
            westFace = this.segments[i].getFaceClone(5);

            for(int j = 0; j < 4; j++){
                topFace[j].add(position);
                bottomFace[j].add(position);
                northFace[j].add(position);
                southFace[j].add(position);
                eastFace[j].add(position);
                westFace[j].add(position);
            }

            this.rotateYaw(-angleDeg, topFace);
            this.rotateYaw(-angleDeg, bottomFace);
            this.rotateYaw(-angleDeg, northFace);
            this.rotateYaw(-angleDeg, southFace);
            this.rotateYaw(-angleDeg, eastFace);
            this.rotateYaw(-angleDeg, westFace);

            float entityModX = MathUtil.positiveMod(associatedEntity.x, 32);
            float entityModY = MathUtil.positiveMod(associatedEntity.y, 32);
            float entityModZ = MathUtil.positiveMod(associatedEntity.z, 32);

            for(int j = 0; j < 4; j++){
                topFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                bottomFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                northFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                southFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                eastFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                westFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
            }

            switch (i) {
                case 0 ->{
                    this.bodySegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 1 ->{
                    this.headSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 2 ->{
                    this.leftLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 3 ->{
                    this.rightLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 4 -> {
                    this.leftArmSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 5 -> {
                    this.rightArmSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
            }

        }

        int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
        int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;

        int chunkX = MathUtil.floorDouble(associatedEntity.x) >> 5;
        int chunkY = MathUtil.floorDouble(associatedEntity.y) >> 5;
        int chunkZ = MathUtil.floorDouble(associatedEntity.z) >> 5;

        int offsetX = (chunkX - playerChunkX) << 5;
        int offsetY = (chunkY - playerChunkY) << 5;
        int offsetZ = (chunkZ - playerChunkZ) << 5;

        Shader.worldShader2DTexture.uploadVec3f("chunkOffset", new Vector3f(offsetX, offsetY, offsetZ));
        Shader.worldShader2DTexture.uploadBoolean("performNormals", true);
        worldTessellator.drawTexture2D(EntityModelTest.texture, Shader.worldShader2DTexture, CosmicEvolution.camera);
        Shader.worldShader2DTexture.uploadBoolean("performNormals", false);
    }

    @Override
    public void renderModelForShadowMap(Entity associatedEntity, int sunX, int sunY, int sunZ) {
        RenderEngine.WorldTessellator worldTessellator = RenderEngine.WorldTessellator.instance;
        Vector3f position;
        Vector3f[] topFace;
        Vector3f[] bottomFace;
        Vector3f[] northFace;
        Vector3f[] southFace;
        Vector3f[] eastFace;
        Vector3f[] westFace;

        this.rotateSegment(BODY, 0, 1, 0, 180);

        float angleDeg = associatedEntity.yaw;
        for(int i = 0; i < this.segments.length; i++){
            position = new Vector3f();
            if(!this.segments[i].isRoot) {
                ModelSegment workingSegment = this.segments[i];
                while (!workingSegment.isRoot) {
                    position.add(workingSegment.position);
                    workingSegment = workingSegment.parentSegment;
                }
            }

            position.y += 0.23f; //This is a magic number, I can't figure out why exactly it's 0.23,
            // my original assumption was that it was half the body's height since that's the root part and it's definitely not the collision system


            topFace = this.segments[i].getFaceClone(0);
            bottomFace = this.segments[i].getFaceClone(1);
            northFace = this.segments[i].getFaceClone(2);
            southFace = this.segments[i].getFaceClone(3);
            eastFace = this.segments[i].getFaceClone(4);
            westFace = this.segments[i].getFaceClone(5);

            for(int j = 0; j < 4; j++){
                topFace[j].add(position);
                bottomFace[j].add(position);
                northFace[j].add(position);
                southFace[j].add(position);
                eastFace[j].add(position);
                westFace[j].add(position);
            }

            this.rotateYaw(-angleDeg, topFace);
            this.rotateYaw(-angleDeg, bottomFace);
            this.rotateYaw(-angleDeg, northFace);
            this.rotateYaw(-angleDeg, southFace);
            this.rotateYaw(-angleDeg, eastFace);
            this.rotateYaw(-angleDeg, westFace);

            float entityModX = MathUtil.positiveMod(associatedEntity.x, 32);
            float entityModY = MathUtil.positiveMod(associatedEntity.y, 32);
            float entityModZ = MathUtil.positiveMod(associatedEntity.z, 32);

            for(int j = 0; j < 4; j++){
                topFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                bottomFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                northFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                southFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                eastFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
                westFace[j].add(new Vector3f(entityModX, entityModY, entityModZ));
            }

            switch (i) {
                case 0 ->{
                    this.bodySegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 1 ->{
                    this.headSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 2 ->{
                    this.leftLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 3 ->{
                    this.rightLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 4 -> {
                    this.leftArmSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 5 -> {
                    this.rightArmSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
            }

        }

        int chunkX = MathUtil.floorDouble(associatedEntity.x) >> 5;
        int chunkY = MathUtil.floorDouble(associatedEntity.y) >> 5;
        int chunkZ = MathUtil.floorDouble(associatedEntity.z) >> 5;


        Shader.shadowMapShaderTexture2D.uploadVec3f("chunkOffset", new Vector3f((chunkX - sunX) << 5, (chunkY - sunY) << 5, (chunkZ - sunZ) << 5));
        worldTessellator.drawTexture2D(0, Shader.shadowMapShaderTexture2D, CosmicEvolution.camera);
    }


    private void bodySegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 50, 46, 66, 46,50, 35, 66, 35);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 50, 35, 66, 35,50, 24, 66, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 72, 24, 88, 24,72, 0, 88, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 56, 24, 72, 24,56, 0, 72, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 66, 48, 77, 48,66, 24, 77, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 77, 48, 88, 48,77, 24, 88, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void headSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 40, 96, 56, 96,40, 80, 56, 80);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 3,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 1,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 2,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 0,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 40, 80, 56, 80,40, 64, 56, 64);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 3,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 1,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 2,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 0,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 56, 96, 72, 96,56, 80, 72, 80);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 56, 80, 72, 80,56, 64, 72, 64);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 72, 96, 88, 96,72, 80, 88, 80);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 72, 80, 88, 80,72, 64, 88, 64);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void leftLegSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 96, 40, 96,32, 88, 40, 88);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 88, 40, 88,32, 80, 40, 80);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 96, 8, 96,0, 72, 8, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 96, 16, 96,8, 72, 16, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 96, 24, 96,16, 72, 24, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 96, 32, 96,24, 72, 32, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void rightLegSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 72, 40, 72,32, 64, 40, 64);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 64, 40, 64,32, 56, 40, 56);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 72, 8, 72,0, 48, 8, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 72, 16, 72,8, 48, 16, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 72, 24, 72,16, 48, 24, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 72, 32, 72,24, 48, 32, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void leftArmSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 48, 40, 48,32, 40, 40, 40);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 40, 40, 40,32, 32, 40, 32);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 48, 8, 48,0, 24, 8, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 48, 16, 48,8, 24, 16, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 48, 24, 48,16, 24, 24, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 48, 32, 48,24, 24, 32, 24);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void rightArmSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 24, 40, 24,32, 16, 40, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 16, 40, 16,32, 8, 40, 8);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 24, 8, 24,0, 0, 8, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 24, 16, 24,8, 0, 16, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 24, 24, 24,16, 0, 24, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 24, 32, 24,24, 0, 32, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }


    public void renderModelFromInventory(Entity associatedEntity, float xPos, float yPos, float zPos, int texture) {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        Vector3f position;
        Vector3f[] topFace;
        Vector3f[] bottomFace;
        Vector3f[] northFace;
        Vector3f[] southFace;
        Vector3f[] eastFace;
        Vector3f[] westFace;


        float angleDeg = associatedEntity.yaw;
        for(int i = 0; i < this.segments.length; i++){
            position = new Vector3f();
            if(!this.segments[i].isRoot) {
                ModelSegment workingSegment = this.segments[i];
                while (!workingSegment.isRoot) {
                    position.add(workingSegment.position);
                    workingSegment = workingSegment.parentSegment;
                }
            }

            position.x += xPos;
            position.y += yPos;
            position.z += zPos;


            topFace = this.segments[i].getFaceClone(0);
            bottomFace = this.segments[i].getFaceClone(1);
            northFace = this.segments[i].getFaceClone(2);
            southFace = this.segments[i].getFaceClone(3);
            eastFace = this.segments[i].getFaceClone(4);
            westFace = this.segments[i].getFaceClone(5);

            for(int j = 0; j < 4; j++){
                topFace[j].add(position);
                bottomFace[j].add(position);
                northFace[j].add(position);
                southFace[j].add(position);
                eastFace[j].add(position);
                westFace[j].add(position);
            }

            switch (i) {
                case 0 ->{
                    this.bodySegmentUVFromInventory(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 1 ->{
                    this.headSegmentUVFromInventory(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 2 ->{
                    this.leftLegSegmentUVFromInventory(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 3 ->{
                    this.rightLegSegmentUVFromInventory(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 4 -> {
                    this.leftArmSegmentUVFromInventory(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
                case 5 -> {
                    this.rightArmSegmentUVFromInventory(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                }
            }

        }

        tessellator.toggleOrtho();
        tessellator.drawTexture2D(texture, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }



    private void bodySegmentUVFromInventory(RenderEngine.Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 50, 46, 66, 46,50, 35, 66, 35);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 50, 35, 66, 35,50, 24, 66, 24);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 72, 24, 88, 24,72, 0, 88, 0);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 56, 24, 72, 24,56, 0, 72, 0);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 66, 48, 77, 48,66, 24, 77, 24);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 77, 48, 88, 48,77, 24, 88, 24);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();
    }

    private void headSegmentUVFromInventory(RenderEngine.Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 40, 96, 56, 96,40, 80, 56, 80);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[0].x, topFace[0].y, topFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[1].x, topFace[1].y, topFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[2].x, topFace[2].y, topFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[3].x, topFace[3].y, topFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 40, 80, 56, 80,40, 64, 56, 64);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 56, 96, 72, 96,56, 80, 72, 80);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 56, 80, 72, 80,56, 64, 72, 64);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 72, 96, 88, 96,72, 80, 88, 80);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 72, 80, 88, 80,72, 64, 88, 64);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();
    }

    private void leftLegSegmentUVFromInventory(RenderEngine.Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 96, 40, 96,32, 88, 40, 88);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 88, 40, 88,32, 80, 40, 80);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 96, 8, 96,0, 72, 8, 72);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 96, 16, 96,8, 72, 16, 72);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 96, 24, 96,16, 72, 24, 72);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 96, 32, 96,24, 72, 32, 72);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();
    }

    private void rightLegSegmentUVFromInventory(RenderEngine.Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 72, 40, 72,32, 64, 40, 64);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 64, 40, 64,32, 56, 40, 56);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 72, 8, 72,0, 48, 8, 48);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 72, 16, 72,8, 48, 16, 48);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 72, 24, 72,16, 48, 24, 48);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 72, 32, 72,24, 48, 32, 48);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();
    }

    private void leftArmSegmentUVFromInventory(RenderEngine.Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 48, 40, 48,32, 40, 40, 40);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 40, 40, 40,32, 32, 40, 32);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 48, 8, 48,0, 24, 8, 24);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 48, 16, 48,8, 24, 16, 24);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 48, 24, 48,16, 24, 24, 24);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 48, 32, 48,24, 24, 32, 24);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();
    }

    private void rightArmSegmentUVFromInventory(RenderEngine.Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 24, 40, 24,32, 16, 40, 16);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(200 << 16 | 200 << 8 | 200, topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 16, 40, 16,32, 8, 40, 8);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(100 << 16 | 100 << 8 | 100, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 24, 8, 24,0, 0, 8, 0);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 24, 16, 24,8, 0, 16, 0);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(16777215, southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 24, 24, 24,16, 0, 24, 0);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 24, 32, 24,24, 0, 32, 0);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5]);
        tessellator.addVertex2DTextureWithSampling(175 << 16 | 175 << 8 | 175, westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7]);
        tessellator.addElements();
    }

}
