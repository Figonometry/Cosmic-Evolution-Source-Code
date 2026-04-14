package spacegame.render;

import org.joml.Vector3f;
import spacegame.core.CosmicEvolution;
import spacegame.entity.Entity;
import spacegame.gui.GuiInGame;
import spacegame.util.MathUtil;

import java.awt.*;

public abstract class Model {
    public float red;
    public float green;
    public float blue;
    public float skyLightValue;
    public ModelSegment[] segments;

    public abstract void renderModel(Entity associatedEntity);

    public abstract void renderModelForShadowMap(Entity associatedEntity, int sunX, int sunY, int sunZ);

    public void animate(int stepInCycle, boolean continueAnimation, Entity entity){
    }

    public void resetLight() {
        red = 1F;
        green = 1F;
        blue = 1F;
    }

    public void setVertexLight1Arg(byte light, float x, float y, float z, float[] lightColor) {
        float finalLight = getLightValueFromMap(light);

        red = lightColor[0];
        green = lightColor[1];
        blue = lightColor[2];

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }

    public float getLightValueFromMap(byte lightValue) {
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

    public void rotateSegment(int segmentInArray, float x, float y, float z, float angleDeg){
        if(segmentInArray < 0 || segmentInArray > this.segments.length - 1){return;}
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

    public void rotateYaw(float angleDeg, Vector3f[] face){
        float angleRad = (float) Math.toRadians(angleDeg);
        for(int i = 0; i < face.length; i++){
            face[i].rotateY(angleRad);
        }
    }

    protected int calculateVertexLightColor(Vector3f vertex, Entity associatedEntity){
        this.resetLight();
        float x = (float) (Math.abs(MathUtil.positiveMod(associatedEntity.x,32f) - vertex.x) + associatedEntity.x);
        float y = (float) (Math.abs(MathUtil.positiveMod(associatedEntity.y,32f) - vertex.y) + associatedEntity.y);
        float z = (float) (Math.abs(MathUtil.positiveMod(associatedEntity.z,32f) - vertex.z) + associatedEntity.z);
        int xInt = MathUtil.floorDouble(x);
        int yInt = MathUtil.floorDouble(y);
        int zInt = MathUtil.floorDouble(z);
        float[] lightColor =  !associatedEntity.canDamage ? new float[]{1,0.65f,0.65f}  : CosmicEvolution.instance.save.activeWorld.getBlockLightColor(xInt, yInt, zInt);

        byte lightVal = CosmicEvolution.instance.save.activeWorld.getBlockLightValue(xInt, yInt, zInt);
        byte skyLightVal = CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(xInt, yInt, zInt);

        this.setVertexLight1Arg(lightVal > skyLightVal ? lightVal : skyLightVal, x, y, z, lightColor);
        this.skyLightValue = GuiInGame.getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(xInt, yInt, zInt));
        return MathUtil.floatToIntRGBA(red) << 16 | MathUtil.floatToIntRGBA(green) << 8 | MathUtil.floatToIntRGBA(blue);
    }
}
