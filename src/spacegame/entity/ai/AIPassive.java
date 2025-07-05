package spacegame.entity.ai;

import org.joml.Vector2f;
import org.joml.Vector2i;
import spacegame.core.MathUtils;
import spacegame.entity.Entity;

import java.util.Random;

public abstract class AIPassive {
    private static final Random rand = new Random();


    public static void chooseNewTargetAndSetAngle(Entity entity){
        int x = MathUtils.floorDouble(entity.x);
        int z = MathUtils.floorDouble(entity.z);


        int targetX = x + rand.nextInt(-10, 11);
        int targetZ = z + rand.nextInt(-10, 11);

        Vector2f startPos = new Vector2f(x,z);
        Vector2f targetPos = new Vector2f(targetX, targetZ);

        entity.yaw = Math.abs((float) Math.toDegrees(startPos.angle(targetPos)) * 10000);
        entity.targetX = targetX + 0.5;
        entity.targetZ = targetZ + 0.5;
        entity.shouldMove = true;

        double distance = MathUtils.distance2D(x, z, targetX + 0.5, targetZ + 0.5);
        double distancePerTick = distancePerTick(entity);

        entity.moveTimer = (int) (distance/distancePerTick);
    }

    private static double distancePerTick(Entity entity) {
        double deltaX = 0;
        double deltaZ = 0;
        double distance = entity.rawDeltaX * entity.rawDeltaX; //If distance is ever 0.0 this will result in a NaN return and will cause the moveTimer to become 2^31
        distance = (float) (entity.speed / Math.sqrt(distance));
        entity.rawDeltaX *= distance;
        double sine = Math.sin(Math.toRadians(entity.yaw));
        double cosine = Math.cos(Math.toRadians(entity.yaw));
        deltaX += entity.rawDeltaX * cosine - 0 * sine;
        deltaZ += 0 * cosine + entity.rawDeltaX * sine;

        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }


}
