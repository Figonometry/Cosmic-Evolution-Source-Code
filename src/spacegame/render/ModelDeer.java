package spacegame.render;

import org.joml.Vector3f;
import spacegame.core.SpaceGame;
import spacegame.entity.Entity;
import spacegame.entity.EntityDeer;

import java.awt.*;

public final class ModelDeer extends Model {
    public ModelSegment[] segments;
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


    public ModelDeer(){
        this.segments = new ModelSegment[12];
        ModelSegment body = new ModelSegment(0.5f, 0.5f, 1.5f, true, null, new Vector3f(), new Vector3f());
        ModelSegment frontLeftLeg = new ModelSegment(0.125f, 1f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(0.15f,-0.5f, 0.5f));
        ModelSegment frontRightLeg = new ModelSegment(0.125f, 1f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(-0.15f,-0.5f, 0.5f));
        ModelSegment backLeftLeg = new ModelSegment(0.125f, 1f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(0.15f,-0.5f, -0.5f));
        ModelSegment backRightLeg = new ModelSegment(0.125f, 1f, 0.125f, false, body, new Vector3f(0, 0.5f, 0), new Vector3f(-0.15f,-0.5f, -0.5f));
        ModelSegment neck = new ModelSegment(0.25f, 0.25f, 0.5f, false, body, new Vector3f(0,0, -0.25f), new Vector3f(0, 0.1f, 0.85f));
        ModelSegment head1 = new ModelSegment(0.3f, 0.3f, 0.125f, false, neck, new Vector3f(), new Vector3f(0, 0f, 0.25f));
        ModelSegment head2 = new ModelSegment(0.18f, 0.18f, 0.25f, false, head1, new Vector3f(), new Vector3f(0, 0, 0.15f));
        ModelSegment head3 = new ModelSegment(0.15f, 0.15f, 0.25f, false, head2, new Vector3f(), new Vector3f(0, 0, 0.15f));
        ModelSegment tail = new ModelSegment(0.125f, 0.125f, 0.35f, false ,body, new Vector3f(0,0, 0.175f), new Vector3f(0, 0.15f, -0.85f));
        ModelSegment leftEar = new ModelSegment(0.25f, 0.125f, 0.03125f, false, head1, new Vector3f(), new Vector3f(0.15f, 0.05f, 0));
        ModelSegment rightEar = new ModelSegment(0.25f, 0.125f, 0.03125f, false, head1, new Vector3f(), new Vector3f(-0.15f, 0.05f, 0));
        body.setChildSegments(new ModelSegment[]{frontLeftLeg, frontRightLeg, backLeftLeg, backRightLeg, tail, neck});
        neck.setChildSegments(new ModelSegment[]{head1});
        head1.setChildSegments(new ModelSegment[]{head2, leftEar, rightEar});
        head2.setChildSegments(new ModelSegment[]{head3});
        neck.rotateModelSegmentX(-20);
        head1.rotateSegmentX(20f);
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

    public static ModelDeer getBaseModel(){
        return new ModelDeer();
    }

    @Override
    public void animate(int stepInCycle, boolean continueAnimation, Entity entity){ //120 ticks per cycle
        this.animateWalkCycle(stepInCycle,continueAnimation, (EntityDeer)entity);
    }

    private void animateWalkCycle(int stepInCycle, boolean continueAnimation, EntityDeer deer){
        float legAngleMax = 10f;
        float angleBackRightLeg = 0;
        float angleFrontLeftLeg = 0;
        float angleBackLeftLeg = 0;
        float angleFrontRightLeg = 0;
        if(!deer.stopBackRightLeg) {
            angleBackRightLeg = (float) (Math.sin((stepInCycle / 4.775)) * legAngleMax);
        }
        if(!deer.stopFrontLeftLeg) {
            angleFrontLeftLeg = (float) (Math.sin(((stepInCycle - 7.5) / 4.775)) * legAngleMax);
        }
        if(!deer.stopBackLeftLeg) {
            angleBackLeftLeg = (float) (Math.sin(((stepInCycle - 15) / 4.775)) * legAngleMax);
        }
        if(!deer.stopFrontRightLeg) {
            angleFrontRightLeg = (float) (Math.sin(((stepInCycle - 22.5) / 4.775)) * legAngleMax);
        }

        if(!continueAnimation){
            if(angleFrontLeftLeg > -0.1 || angleFrontLeftLeg < 0.1){
                deer.stopFrontLeftLeg = true;
            }
            if(angleFrontRightLeg > -0.1 || angleFrontRightLeg < 0.1){
                deer.stopFrontRightLeg = true;
            }
            if(angleBackLeftLeg > -0.1 || angleBackLeftLeg < 0.1){
                deer.stopBackLeftLeg = true;
            }
            if(angleBackRightLeg > -0.1 || angleBackRightLeg < 0.1){
                deer.stopBackRightLeg = true;
            }
        }

        this.rotateSegment(BACK_RIGHT_LEG, 1, 0, 0, angleBackRightLeg);
        this.rotateSegment(FRONT_LEFT_LEG, 1, 0, 0, angleFrontLeftLeg);
        this.rotateSegment(BACK_LEFT_LEG, 1, 0, 0, angleBackLeftLeg);
        this.rotateSegment(FRONT_RIGHT_LEG, 1, 0 , 0, angleFrontRightLeg);
    }

    public void rotateYaw(float angleDeg, Vector3f[] face){
        float angleRad = (float) Math.toRadians(angleDeg);
        for(int i = 0; i < face.length; i++){
            face[i].rotateY(angleRad);
        }
    }

    public void renderModel(Entity associatedEntity){
        Tessellator tessellator = Tessellator.instance;
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

            for(int j = 0; j < 4; j++){
                topFace[j].add(new Vector3f((float) associatedEntity.x % 32, (float) associatedEntity.y % 32, (float) associatedEntity.z % 32));
                bottomFace[j].add(new Vector3f((float) associatedEntity.x % 32, (float) associatedEntity.y % 32, (float) associatedEntity.z % 32));
                northFace[j].add(new Vector3f((float) associatedEntity.x % 32, (float) associatedEntity.y % 32, (float) associatedEntity.z % 32));
                southFace[j].add(new Vector3f((float) associatedEntity.x % 32, (float) associatedEntity.y % 32, (float) associatedEntity.z % 32));
                eastFace[j].add(new Vector3f((float) associatedEntity.x % 32, (float) associatedEntity.y % 32, (float) associatedEntity.z % 32));
                westFace[j].add(new Vector3f((float) associatedEntity.x % 32, (float) associatedEntity.y % 32, (float) associatedEntity.z % 32));
            }

            switch (i) {
                case 0 ->
                        this.bodySegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 1, 2, 3, 4 ->
                        this.legSegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 5 ->
                        this.neckSegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 6 ->
                        this.head1SegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 7 ->
                        this.head2SegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 8 ->
                        this.head3SegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 9 ->
                        this.tailSegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
                case 10, 11 ->
                        this.earSegmentUV(tessellator, topFace, bottomFace, northFace, southFace, eastFace, westFace, associatedEntity);
            }

        }

        int playerChunkX = (int) (SpaceGame.instance.save.thePlayer.x) >> 5;
        int playerChunkY = (int) (SpaceGame.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = (int) (SpaceGame.instance.save.thePlayer.z) >> 5;

        int chunkX = (int) (associatedEntity.x) >> 5;
        int chunkY = (int) (associatedEntity.y) >> 5;
        int chunkZ = (int) (associatedEntity.z) >> 5;

        float offsetX = chunkX - playerChunkX;
        float offsetY = chunkY - playerChunkY;
        float offsetZ = chunkZ - playerChunkZ;

        offsetX *= 32;
        offsetY *= 32;
        offsetZ *= 32;

        Shader.worldShader2DTexture.uploadVec3f("chunkOffset", new Vector3f(offsetX, offsetY, offsetZ));
        Shader.worldShader2DTexture.uploadBoolean("useFog", true);
        tessellator.drawTexture2D(EntityDeer.texture.texID, Shader.worldShader2DTexture, SpaceGame.camera);
    }

    public void rotateModelSegment(int segmentInArray, float x, float y, float z, float angleDeg){
        if(segmentInArray < BODY || segmentInArray > RIGHT_EAR){return;}
       boolean rotateX = x == 1;
       boolean rotateY = y == 1;
       boolean rotateZ = z == 1;
       if(rotateX){
           this.segments[segmentInArray].rotateModelSegmentX(angleDeg);
           return;
       }
       if(rotateY){
           this.segments[segmentInArray].rotateModelSegmentY(angleDeg);
           return;
       }
       if(rotateZ){
           this.segments[segmentInArray].rotateModelSegmentZ(angleDeg);
       }
    }


    public void rotateSegment(int segmentInArray, float x, float y, float z, float angleDeg){
        if(segmentInArray < BODY || segmentInArray > RIGHT_EAR){return;}
        boolean rotateX = x == 1;
        boolean rotateY = y == 1;
        boolean rotateZ = z == 1;
        if(rotateX){
            this.segments[segmentInArray].rotateSegmentX(angleDeg);
            return;
        }
        if(rotateY){
            this.segments[segmentInArray].rotateSegmentY(angleDeg);
            return;
        }
        if(rotateZ){
            this.segments[segmentInArray].rotateSegmentZ(angleDeg);
        }
    }

    public float varyLightReductionAmount(float input, int sineFunction, Entity associatedEntity){
        float angleDeg = (float) Math.toRadians(associatedEntity.yaw);
        float ratio = switch (sineFunction) {
            case 0 -> (float) ((Math.sin(angleDeg + 0.5 * Math.PI) * 0.5) + 0.5f);
            case 1 -> (float) ((Math.sin(angleDeg * Math.PI) * 0.5) + 0.5f);
            case 2 -> (float) ((Math.sin(angleDeg - 0.5 * Math.PI) * 0.5) + 0.5f);
            default -> 0;
        };
        return input * ratio;
    }

    private int calculateVertexLightColor(Vector3f vertex, Entity associatedEntity, float lightReduction){
        this.resetLight();
        float x = (float) (Math.abs((associatedEntity.x % 32f) - vertex.x) + associatedEntity.x);
        float y = (float) (Math.abs((associatedEntity.y % 32f) - vertex.y) + associatedEntity.y);
        float z = (float) (Math.abs((associatedEntity.z % 32f) - vertex.z) + associatedEntity.z);
        this.setVertexLight1Arg(SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockLightValue((int) x, (int) y, (int) z), x, y, z, SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockLightColor((int) x, (int) y, (int) z));
        this.red -= lightReduction;
        this.green -= lightReduction;
        this.blue -= lightReduction;
        this.red = this.red <= 0 ? 0.1f : this.red;
        this.green = this.green <= 0 ? 0.1f : this.green;
        this.blue = this.blue <= 0 ? 0.1f : this.blue;
        Color color = new Color(this.red, this.green, this.blue, 0);
        return color.getRGB();
    }

    private void bodySegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,0,-0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0,-0.75757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0,0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,0,0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0,0);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0,-0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,0,-0.75757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0,-0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,0,-0.75757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.66666666666666666666666666666667f,-0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.66666666666666666666666666666667f,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,0,-0.75757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0,-0.75757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.66666666666666666666666666666667f,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0,0);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.66666666666666666666666666666667f,-0.75757575757575757575757575757576f);
        tessellator.addElements();
    }

    private void legSegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0.10416666666666666666666666666667f,0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,-0.8125f,-0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,-0.8125f,0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0.10416666666666666666666666666667f,-0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0.10416666666666666666666666666667f,0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,-0.8125f,-0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,-0.8125f,0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0.10416666666666666666666666666667f,-0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0,-0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,-0.91666666666666666666666666666667f,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,-0.91666666666666666666666666666667f,-0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0,-0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,-0.91666666666666666666666666666667f,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,-0.91666666666666666666666666666667f,-0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0,-0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,-0.91666666666666666666666666666667f,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,-0.91666666666666666666666666666667f,-0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0,-0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.91666666666666666666666666666667f,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0,0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.91666666666666666666666666666667f,-0.25757575757575757575757575757576f);
        tessellator.addElements();
    }

    private void neckSegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0.66666666666666666666666666666667f,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,0,-0.36363636363636363636363636363636f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,0,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0.66666666666666666666666666666667f,-0.36363636363636363636363636363636f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0.66666666666666666666666666666667f,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,0,-0.36363636363636363636363636363636f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,0,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0.66666666666666666666666666666667f,-0.36363636363636363636363636363636f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0.66666666666666666666666666666667f,-0.36363636363636363636363636363636f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,0,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0.66666666666666666666666666666667f,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,0,-0.36363636363636363636363636363636f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0.66666666666666666666666666666667f,-0.36363636363636363636363636363636f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,0,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0.66666666666666666666666666666667f,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,0,-0.36363636363636363636363636363636f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.83333333333333333333333333333333f,-0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,0,0.37878787878787878787878787878788f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.83333333333333333333333333333333f,0.37878787878787878787878787878788f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,0,-0.5f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0.83333333333333333333333333333333f,-0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,0,0.37878787878787878787878787878788f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0.83333333333333333333333333333333f,0.37878787878787878787878787878788f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,0,-0.5f);
        tessellator.addElements();
    }

    private void head1SegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0.33333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,-0.58333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,-0.58333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0.33333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0.33333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,-0.58333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,-0.58333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0.33333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0.33333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,-0.58333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0.33333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,-0.58333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0.33333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,-0.58333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0.33333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,-0.58333333333333333333333333333333f, -0.59090909090909090909090909090909f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.4375f, -0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,-0.35416666666666666666666666666667f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.4375f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,-0.35416666666666666666666666666667f, -0.59090909090909090909090909090909f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0.10416666666666666666666666666667f, -0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.6875f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0.10416666666666666666666666666667f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.6875f, -0.59090909090909090909090909090909f);
        tessellator.addElements();
    }

    private void head2SegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0.83333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,0, -0.63636363636363636363636363636364f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,0, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0.83333333333333333333333333333333f, -0.63636363636363636363636363636364f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0.83333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,0, -0.63636363636363636363636363636364f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,0, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0.83333333333333333333333333333333f, -0.63636363636363636363636363636364f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0.83333333333333333333333333333333f, -0.63636363636363636363636363636364f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,0, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0.83333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,0, -0.63636363636363636363636363636364f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0.83333333333333333333333333333333f, -0.63636363636363636363636363636364f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,0, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0.83333333333333333333333333333333f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,0, -0.63636363636363636363636363636364f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.66666666666666666666666666666667f, -0.63636363636363636363636363636364f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,-0.1875f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.66666666666666666666666666666667f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,-0.1875f, -0.63636363636363636363636363636364f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0.66666666666666666666666666666667f, -0.63636363636363636363636363636364f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.1875f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0.66666666666666666666666666666667f, 0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.1875f, -0.63636363636363636363636363636364f);
        tessellator.addElements();
    }

    private void head3SegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,-0.41666666666666666666666666666667f, -0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,-0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0.41666666666666666666666666666667f, -0.5f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,-0.41666666666666666666666666666667f, -0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,-0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0.41666666666666666666666666666667f, -0.5f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0.41666666666666666666666666666667f, -0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,-0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,-0.41666666666666666666666666666667f, -0.5f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0.41666666666666666666666666666667f, -0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,-0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0.41666666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,-0.41666666666666666666666666666667f, -0.5f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.89583333333333333333333333333333f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,0, 0.66666666666666666666666666666667f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.89583333333333333333333333333333f, 0.66666666666666666666666666666667f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,0, -0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0.29166666666666666666666666666667f, -0.5f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.60416666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0.29166666666666666666666666666667f, 0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.60416666666666666666666666666667f, -0.5f);
        tessellator.addElements();
    }

    private void tailSegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 2,0.20833333333333333333333333333333f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 0,-0.5625f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 1,-0.5625f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 3,0.20833333333333333333333333333333f, -0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2,0.45833333333333333333333333333333f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0,-0.3125f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1,-0.3125f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3,0.45833333333333333333333333333333f, -0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0.20833333333333333333333333333333f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,-0.5625f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0.20833333333333333333333333333333f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,-0.5625f, -0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0.20833333333333333333333333333333f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,-0.5625f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0.20833333333333333333333333333333f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,-0.5625f, -0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.10416666666666666666666666666667f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,-0.8125f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.10416666666666666666666666666667f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,-0.8125f, -0.25757575757575757575757575757576f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0.10416666666666666666666666666667f, -0.25757575757575757575757575757576f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.8125f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0.10416666666666666666666666666667f, 0.68181818181818181818181818181818f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.8125f, -0.25757575757575757575757575757576f);
        tessellator.addElements();
    }

    private void earSegmentUV(Tessellator tessellator, Vector3f[] topFace, Vector3f[] bottomFace, Vector3f[] northFace, Vector3f[] southFace, Vector3f[] eastFace, Vector3f[] westFace, Entity associatedEntity){
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[0], associatedEntity, 0), topFace[0].x, topFace[0].y, topFace[0].z, 3,0.47916666666666666666666666666667f, -0.46969696969696969696969696969697f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[1], associatedEntity, 0), topFace[1].x, topFace[1].y, topFace[1].z, 1,-0.35416666666666666666666666666667f, 0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[2], associatedEntity, 0), topFace[2].x, topFace[2].y, topFace[2].z, 2,0.47916666666666666666666666666667f, 0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(topFace[3], associatedEntity, 0), topFace[3].x, topFace[3].y, topFace[3].z, 0,-0.35416666666666666666666666666667f, -0.46969696969696969696969696969697f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[0], associatedEntity, 0.2f), bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 3,0.47916666666666666666666666666667f, -0.46969696969696969696969696969697f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[1], associatedEntity, 0.2f), bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 1,-0.35416666666666666666666666666667f, 0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[2], associatedEntity, 0.2f), bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 2,0.47916666666666666666666666666667f, 0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(bottomFace[3], associatedEntity, 0.2f), bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 0,-0.35416666666666666666666666666667f, -0.46969696969696969696969696969697f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[0].x, northFace[0].y, northFace[0].z, 3,0.47916666666666666666666666666667f,-0.34848484848484848484848484848485f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[1].x, northFace[1].y, northFace[1].z, 1,-0.5f,0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[2].x, northFace[2].y, northFace[2].z, 2,0.47916666666666666666666666666667f,0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(northFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 0, associatedEntity)), northFace[3].x, northFace[3].y, northFace[3].z, 0,-0.5f,-0.34848484848484848484848484848485f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[0].x, southFace[0].y, southFace[0].z, 3,0.47916666666666666666666666666667f,-0.34848484848484848484848484848485f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[1].x, southFace[1].y, southFace[1].z, 1,-0.5f,0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[2].x, southFace[2].y, southFace[2].z, 2,0.47916666666666666666666666666667f,0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(southFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), southFace[3].x, southFace[3].y, southFace[3].z, 0,-0.5f,-0.34848484848484848484848484848485f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[0].x, eastFace[0].y, eastFace[0].z, 3,0.29166666666666666666666666666667f, -0.34848484848484848484848484848485f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[1].x, eastFace[1].y, eastFace[1].z, 1,-0.54166666666666666666666666666667f, 0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[2].x, eastFace[2].y, eastFace[2].z, 2,0.29166666666666666666666666666667f, 0.59090909090909090909090909090909f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(eastFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 1, associatedEntity)), eastFace[3].x, eastFace[3].y, eastFace[3].z, 0,-0.54166666666666666666666666666667f, -0.34848484848484848484848484848485f);
        tessellator.addElements();

        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[0], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[0].x, westFace[0].y, westFace[0].z, 3,0.29166666666666666666666666666667f,-0.42424242424242424242424242424242f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[1], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[1].x, westFace[1].y, westFace[1].z, 1,-0.54166666666666666666666666666667f,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[2], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[2].x, westFace[2].y, westFace[2].z, 2,0.29166666666666666666666666666667f,0.51515151515151515151515151515152f);
        tessellator.addVertex2DTextureWithSampling(this.calculateVertexLightColor(westFace[3], associatedEntity, this.varyLightReductionAmount(0.2f, 2, associatedEntity)), westFace[3].x, westFace[3].y, westFace[3].z, 0,-0.54166666666666666666666666666667f,-0.42424242424242424242424242424242f);
        tessellator.addElements();
    }
}
