package spacegame.render.model;

import org.joml.Vector3f;
import spacegame.core.CosmicEvolution;
import spacegame.entity.*;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.MathUtil;

public final class ModelWolf extends Model {
    public static final int BODY = 0;
    public static final int FRONT_LEFT_LEG = 1;
    public static final int FRONT_RIGHT_LEG = 2;
    public static final int BACK_LEFT_LEG = 3;
    public static final int BACK_RIGHT_LEG = 4;
    public static final int NECK = 5;
    public static final int HEAD_1 = 6;
    public static final int HEAD_2 = 7;
    public static final int HEAD_3 = 8;
    public static final int TAIL = 9;
    public static final int LEFT_EAR = 10;
    public static final int RIGHT_EAR = 11;


    public ModelWolf(){
        this.segments = new ModelSegment[12];
        ModelSegment body = new ModelSegment(0.4f, 0.4f, 1.5f, true, null, new Vector3f(), new Vector3f());
        ModelSegment frontLeftLeg = new ModelSegment(0.125f, 0.5f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(0.13f,-0.4f, 0.5f));
        ModelSegment frontRightLeg = new ModelSegment(0.125f, 0.5f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(-0.13f,-0.4f, 0.5f));
        ModelSegment backLeftLeg = new ModelSegment(0.125f, 0.5f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(0.13f,-0.4f, -0.5f));
        ModelSegment backRightLeg = new ModelSegment(0.125f, 0.5f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(-0.13f,-0.4f, -0.5f));
        ModelSegment neck = new ModelSegment(0.39f, 0.39f, 0.25f, false, body, new Vector3f(0,0, -0.25f), new Vector3f(0, 0f, 0.75f));
        ModelSegment head1 = new ModelSegment(0.38f, 0.38f, 0.25f, false, neck, new Vector3f(), new Vector3f(0, -0.05f, 0.2f));
        ModelSegment head2 = new ModelSegment(0.18f, 0.18f, 0.25f, false, head1, new Vector3f(), new Vector3f(0, 0, 0.15f));
        ModelSegment head3 = new ModelSegment(0.15f, 0.15f, 0.25f, false, head2, new Vector3f(), new Vector3f(0, 0, 0.15f));
        ModelSegment tail = new ModelSegment(0.125f, 0.125f, 0.65f, false ,body, new Vector3f(0,0, 0.175f), new Vector3f(0, 0.05f, -1f));
        ModelSegment leftEar = new ModelSegment(0.125f, 0.25f, 0.03125f, false, head1, new Vector3f(), new Vector3f(0.1f, 0.2f, 0));
        ModelSegment rightEar = new ModelSegment(0.125f, 0.25f, 0.03125f, false, head1, new Vector3f(), new Vector3f(-0.1f, 0.2f, 0));

        body.setChildSegments(new ModelSegment[]{frontLeftLeg, frontRightLeg, backLeftLeg, backRightLeg, tail, neck});
        neck.setChildSegments(new ModelSegment[]{head1});
        head1.setChildSegments(new ModelSegment[]{head2, leftEar, rightEar});
        head2.setChildSegments(new ModelSegment[]{head3});
        neck.rotateModelSegmentX(-20);
        head1.rotateSegmentX(20f, true);
        tail.rotateModelSegmentX(-20f);


        this.segments[0] = body;
        this.segments[1] = frontLeftLeg;
        this.segments[2] = frontRightLeg;
        this.segments[3] = backLeftLeg;
        this.segments[4] = backRightLeg;
        this.segments[5] = neck;
        this.segments[6] = head1;
        this.segments[7] = head2;
        this.segments[8] = head3;
        this.segments[9] = tail;
        this.segments[10] = leftEar;
        this.segments[11] = rightEar;
    }

    public static ModelWolf getBaseModel(){
        return new ModelWolf();
    }

    @Override
    public void animate(int stepInCycle, boolean continueAnimation, Entity entity){ //120 ticks per cycle
        if(((EntityLiving)entity).isDead){
            this.animateCorpse((EntityWolf) entity);
        } else {
            this.animateWalkCycle(stepInCycle,continueAnimation, (EntityWolf)entity);
        }
    }

    private void animateCorpse(EntityWolf wolf){
        if(CosmicEvolution.instance.save.time - wolf.timeDied >= 15){
            this.segments[BODY].rotateSegmentZ(90, false);
            this.segments[NECK].rotateModelSegmentX(10);
            this.segments[TAIL].rotateSegmentX(-45, true);
            this.segments[FRONT_LEFT_LEG].rotateSegmentY(-30, true);
            this.segments[FRONT_LEFT_LEG].rotateSegmentX(45, true);
            this.segments[FRONT_RIGHT_LEG].rotateSegmentY(30, true);
            this.segments[BACK_LEFT_LEG].rotateSegmentY(-30, true);
            this.segments[BACK_LEFT_LEG].rotateSegmentX(45, true);
            this.segments[BACK_RIGHT_LEG].rotateSegmentY(30, true);
        } else {
            float ratio = (CosmicEvolution.instance.save.time - wolf.timeDied) / 15f;
            this.segments[BODY].rotateSegmentZ(90 * ratio, false);
            this.segments[NECK].rotateModelSegmentX(10 * ratio);
            this.segments[TAIL].rotateSegmentX(-45 * ratio, true);
            this.segments[FRONT_LEFT_LEG].rotateSegmentY(-30 * ratio, true);
            this.segments[FRONT_LEFT_LEG].rotateSegmentX(45 * ratio, true);
            this.segments[FRONT_RIGHT_LEG].rotateSegmentY(30 * ratio, true);
            this.segments[BACK_LEFT_LEG].rotateSegmentY(-30 * ratio, true);
            this.segments[BACK_LEFT_LEG].rotateSegmentX(45 * ratio, true);
            this.segments[BACK_RIGHT_LEG].rotateSegmentY(30 * ratio, true);
        }
    }

    public void getLegAnglesAtTimeOfDeath(int stepInCycle, boolean continueAnimation, EntityWolf wolf){
        float legAngleMax = 10f;
        float angleBackRightLeg = 0;
        float angleFrontLeftLeg = 0;
        float angleBackLeftLeg = 0;
        float angleFrontRightLeg = 0;
        if(!wolf.stopBackRightLeg) {
            angleBackRightLeg = (float) (MathUtil.sin((stepInCycle / 4.775)) * legAngleMax);
        }
        if(!wolf.stopFrontLeftLeg) {
            angleFrontLeftLeg = (float) (MathUtil.sin(((stepInCycle - 7.5) / 4.775)) * legAngleMax);
        }
        if(!wolf.stopBackLeftLeg) {
            angleBackLeftLeg = (float) (MathUtil.sin(((stepInCycle - 15) / 4.775)) * legAngleMax);
        }
        if(!wolf.stopFrontRightLeg) {
            angleFrontRightLeg = (float) (MathUtil.sin(((stepInCycle - 22.5) / 4.775)) * legAngleMax);
        }

        if(!continueAnimation){
            if(angleFrontLeftLeg > -0.1 || angleFrontLeftLeg < 0.1){
                wolf.stopFrontLeftLeg = true;
            }
            if(angleFrontRightLeg > -0.1 || angleFrontRightLeg < 0.1){
                wolf.stopFrontRightLeg = true;
            }
            if(angleBackLeftLeg > -0.1 || angleBackLeftLeg < 0.1){
                wolf.stopBackLeftLeg = true;
            }
            if(angleBackRightLeg > -0.1 || angleBackRightLeg < 0.1){
                wolf.stopBackRightLeg = true;
            }
        }

      //  if(wolf.isDead){//It was easier to hook into this and return so I have the angles at time of death
      //      wolf.frontLeftLegAngleAtDeath = new Vector3f(angleFrontLeftLeg, 0, 0);
      //      wolf.frontRightLegAngleAtDeath = new Vector3f(angleFrontRightLeg, 0, 0);
      //      wolf.backLeftLegAngleAtDeath = new Vector3f(angleBackLeftLeg, 0, 0);
      //      wolf.backRightLegAngleAtDeath = new Vector3f(angleBackRightLeg, 0, 0);
      //  }
    }

    private void animateWalkCycle(int stepInCycle, boolean continueAnimation, EntityWolf wolf){
        float legAngleMax = 10f;
        float angleBackRightLeg = 0;
        float angleFrontLeftLeg = 0;
        float angleBackLeftLeg = 0;
        float angleFrontRightLeg = 0;
        if(!wolf.stopBackRightLeg) {
            angleBackRightLeg = (float) (MathUtil.sin((stepInCycle / 4.775)) * legAngleMax);
        }
        if(!wolf.stopFrontLeftLeg) {
            angleFrontLeftLeg = (float) (MathUtil.sin(((stepInCycle - 7.5) / 4.775)) * legAngleMax);
        }
        if(!wolf.stopBackLeftLeg) {
            angleBackLeftLeg = (float) (MathUtil.sin(((stepInCycle - 15) / 4.775)) * legAngleMax);
        }
        if(!wolf.stopFrontRightLeg) {
            angleFrontRightLeg = (float) (MathUtil.sin(((stepInCycle - 22.5) / 4.775)) * legAngleMax);
        }

        if(!continueAnimation){
            if(angleFrontLeftLeg > -0.1 || angleFrontLeftLeg < 0.1){
                wolf.stopFrontLeftLeg = true;
            }
            if(angleFrontRightLeg > -0.1 || angleFrontRightLeg < 0.1){
                wolf.stopFrontRightLeg = true;
            }
            if(angleBackLeftLeg > -0.1 || angleBackLeftLeg < 0.1){
                wolf.stopBackLeftLeg = true;
            }
            if(angleBackRightLeg > -0.1 || angleBackRightLeg < 0.1){
                wolf.stopBackRightLeg = true;
            }
        }

        this.rotateSegment(BACK_RIGHT_LEG, 1, 0, 0, angleBackRightLeg, true);
        this.rotateSegment(FRONT_LEFT_LEG, 1, 0, 0, angleFrontLeftLeg, true);
        this.rotateSegment(BACK_LEFT_LEG, 1, 0, 0, angleBackLeftLeg, true);
        this.rotateSegment(FRONT_RIGHT_LEG, 1, 0 , 0, angleFrontRightLeg, true);
    }


    public void renderModel(Entity associatedEntity){
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
            this.rotateYaw(-90, topFace);
            this.rotateYaw(-90, bottomFace);
            this.rotateYaw(-90, northFace);
            this.rotateYaw(-90, southFace);
            this.rotateYaw(-90, eastFace);
            this.rotateYaw(-90, westFace);

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
                case 0 ->
                        this.bodySegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 1 ->
                        this.frontLeftLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 2 ->
                        this.frontRightLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 3 ->
                        this.backLeftLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 4 ->
                        this.backRightLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 5 ->
                        this.neckSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 6 ->
                        this.head1SegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 7 ->
                        this.head2SegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 8 ->
                        this.head3SegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 9 ->
                        this.tailSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 10, 11 ->
                        this.earSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
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
        worldTessellator.drawTexture2D(EntityWolf.texture, Shader.worldShader2DTexture, CosmicEvolution.camera);
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
            this.rotateYaw(-90, topFace);
            this.rotateYaw(-90, bottomFace);
            this.rotateYaw(-90, northFace);
            this.rotateYaw(-90, southFace);
            this.rotateYaw(-90, eastFace);
            this.rotateYaw(-90, westFace);

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
                case 0 ->
                        this.bodySegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 1 ->
                        this.frontLeftLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 2 ->
                        this.frontRightLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 3 ->
                        this.backLeftLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 4 ->
                        this.backRightLegSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 5 ->
                        this.neckSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 6 ->
                        this.head1SegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 7 ->
                        this.head2SegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 8 ->
                        this.head3SegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 9 ->
                        this.tailSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 10, 11 ->
                        this.earSegmentUV(worldTessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
            }

        }

        int chunkX = MathUtil.floorDouble(associatedEntity.x) >> 5;
        int chunkY = MathUtil.floorDouble(associatedEntity.y) >> 5;
        int chunkZ = MathUtil.floorDouble(associatedEntity.z) >> 5;


        Shader.shadowMapShaderTexture2D.uploadVec3f("chunkOffset", new Vector3f((chunkX - sunX) << 5, (chunkY - sunY) << 5, (chunkZ - sunZ) << 5));
        worldTessellator.drawTexture2D(0, Shader.shadowMapShaderTexture2D, CosmicEvolution.camera);
    }


    private void bodySegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 0, 88, 48, 88, 0, 76, 48, 76);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2, UVSamples[0], UVSamples[1],topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0, UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1, UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3, UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 0, 76, 48, 76, 0, 64, 48, 64);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2, UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0, UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1, UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3, UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 0, 64, 48, 64, 0, 52, 48, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 0, 52, 48, 52, 0, 40, 48, 40);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 48, 76, 60, 76, 48, 64, 60, 64);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 48, 92, 60, 92, 48, 76, 60, 76);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void frontLeftLegSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 60, 72, 64, 72, 60, 68, 64, 68);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 64, 72, 68, 72, 64, 68, 68, 68);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 60, 92, 64, 92, 60, 72, 64, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 64, 92, 68, 92, 64, 72, 68, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 68, 92, 72, 92, 68, 72, 72, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 72, 92, 76, 92, 72, 72, 76, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void frontRightLegSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 60, 52, 64, 52, 60, 48, 64, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 64, 52, 68, 52, 64, 48, 68, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 60, 68, 64, 68, 60, 52, 64, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 64, 68, 68, 68, 64, 52, 68, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 68, 68, 72, 68, 68, 52, 72, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 72, 68, 76, 68, 72, 52, 76, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void backLeftLegSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 76, 72, 80, 72, 76, 68, 80, 68);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 80, 72, 84, 72, 80, 68, 84, 68);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 76, 88, 80, 88, 76, 72, 80, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 80, 88, 84, 88, 80, 72, 84, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 84, 88, 88, 88, 84, 72, 88, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 88, 88, 92, 88, 88, 72, 92, 72);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void backRightLegSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 76, 52, 80, 52, 76, 48, 80, 48);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 80, 52, 84, 52, 80, 48, 84, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 76, 68, 80, 68, 76, 52, 80, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 80, 68, 84, 68, 80, 52, 84, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 84, 68, 88, 68, 84, 52, 88, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 88, 68, 92, 68, 88, 52, 92, 52);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void neckSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 12, 40, 20, 40, 12, 28, 20, 28);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 12, 28, 20, 28, 12, 16, 20, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 20, 40, 28, 40, 20, 28, 28, 28);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 20, 28, 28, 28, 20, 16, 28, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 0, 40, 12, 40, 0, 28, 12, 28);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 0, 28, 12, 28, 0, 16, 12, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void head1SegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 40, 40, 48, 40, 40, 28, 48, 28);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 40, 28, 48, 28, 40, 16, 48, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 48, 40, 56, 40, 48, 28, 56, 28);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 48, 28, 56, 28, 48, 16, 56, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 28, 40, 40, 40, 28, 28, 40, 28);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 28, 28, 40, 28, 28, 16, 40, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void head2SegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 60, 25, 68, 25, 60, 21, 68, 21);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 68, 25, 76, 25, 68, 21, 76, 21);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 76, 25, 84, 25, 76, 21, 84, 21);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 84, 25, 92, 25, 84, 21, 92, 21);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 82, 21, 87, 21, 82, 16, 87, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 87, 21, 92, 21, 87, 16, 92, 16);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void head3SegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 60, 48, 68, 48, 60, 44, 68, 44);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 68, 48, 76, 48, 68, 44, 76, 44);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 76, 48, 84, 48, 76, 44, 84, 44);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 84, 48, 92, 48, 84, 44, 92, 44);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 82, 44, 87, 44, 82, 39, 87, 39);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 87, 44, 92, 44, 87, 39, 92, 39);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void tailSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        float[] UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 52, 12, 72, 12, 52, 8, 72, 8);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 2,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 0,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 1,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 3,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesTopBottom(92, 88, 72, 12, 92, 12, 72, 8, 92, 8);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 52, 8, 72, 8, 52, 4, 72, 4);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 72, 8, 92, 8, 72, 4, 92, 4);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 84, 4, 88, 4, 84, 0, 88, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 88, 4, 92, 4, 88, 0, 92, 0);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7], westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }

    private void earSegmentUV(RenderEngine.WorldTessellator worldTessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){

        //I'm not sure why this is reversed from the normal
        float[] UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 81, 39, 82, 39, 81, 35, 82, 35);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity), topFace[0].x, topFace[0].y, topFace[0].z, 3,UVSamples[0], UVSamples[1], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity), topFace[1].x, topFace[1].y, topFace[1].z, 1,UVSamples[2], UVSamples[3], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity), topFace[2].x, topFace[2].y, topFace[2].z, 2,UVSamples[4], UVSamples[5], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity), topFace[3].x, topFace[3].y, topFace[3].z, 0,UVSamples[6], UVSamples[7], topFace[4].x, topFace[4].y, topFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 81, 35, 82, 35, 81, 31, 82, 31);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 3,UVSamples[0], UVSamples[1], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 1,UVSamples[2], UVSamples[3], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 2,UVSamples[4], UVSamples[5], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 0,UVSamples[6], UVSamples[7], bottomFace[4].x, bottomFace[4].y, bottomFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 82, 39, 83, 39, 82, 31, 83, 31);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity), northFace[0].x, northFace[0].y, northFace[0].z, 3,UVSamples[0], UVSamples[1], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity), northFace[1].x, northFace[1].y, northFace[1].z, 1,UVSamples[2], UVSamples[3], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity), northFace[2].x, northFace[2].y, northFace[2].z, 2,UVSamples[4], UVSamples[5], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity), northFace[3].x, northFace[3].y, northFace[3].z, 0,UVSamples[6], UVSamples[7], northFace[4].x, northFace[4].y, northFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 83, 39, 84, 39, 83, 31, 84, 31);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity), southFace[0].x, southFace[0].y, southFace[0].z, 3,UVSamples[0], UVSamples[1], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity), southFace[1].x, southFace[1].y, southFace[1].z, 1,UVSamples[2], UVSamples[3], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity), southFace[2].x, southFace[2].y, southFace[2].z, 2,UVSamples[4], UVSamples[5], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity), southFace[3].x, southFace[3].y, southFace[3].z, 0,UVSamples[6], UVSamples[7], southFace[4].x, southFace[4].y, southFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 84, 39, 88, 39, 84, 31, 88, 31);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,UVSamples[0], UVSamples[1], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,UVSamples[2], UVSamples[3], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,UVSamples[4], UVSamples[5], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,UVSamples[6], UVSamples[7], eastFace[4].x, eastFace[4].y, eastFace[4].z, this.skyLightValue);
        worldTessellator.addElements();

        UVSamples = MathUtil.mapUVCoordinatesNSEW(92, 88, 88, 39, 92, 39, 88, 31, 92, 31);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity), westFace[0].x, westFace[0].y, westFace[0].z, 3,UVSamples[0], UVSamples[1],westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity), westFace[1].x, westFace[1].y, westFace[1].z, 1,UVSamples[2], UVSamples[3],westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity), westFace[2].x, westFace[2].y, westFace[2].z, 2,UVSamples[4], UVSamples[5],westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity), westFace[3].x, westFace[3].y, westFace[3].z, 0,UVSamples[6], UVSamples[7],westFace[4].x, westFace[4].y, westFace[4].z, this.skyLightValue);
        worldTessellator.addElements();
    }
}

