package spacegame.entity.ai;

import org.joml.Vector2f;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityLiving;
import spacegame.util.MathUtil;

public abstract class AIPassive { ;


    public static void chooseNewTargetAndSetAngle(EntityLiving entity){
        if(!entity.isAIEnabled)return;
        int x = MathUtil.floorDouble(entity.x);
        int z = MathUtil.floorDouble(entity.z);


        int targetX = x + CosmicEvolution.globalRand.nextInt(-10, 11);
        int targetZ = z + CosmicEvolution.globalRand.nextInt(-10, 11);

        Vector2f startPos = new Vector2f(x,z);
        Vector2f targetPos = new Vector2f(targetX, targetZ);

        entity.yaw = Math.abs((float) Math.toDegrees(startPos.angle(targetPos)) * 10000);
        entity.targetX = targetX + 0.5;
        entity.targetZ = targetZ + 0.5;
        entity.shouldMove = true;

        double distance = MathUtil.distance2D(x, z, targetX + 0.5, targetZ + 0.5);
        double distancePerTick = distancePerTick(entity);

        entity.moveTimer = (int) (distance/distancePerTick);
    }

    public static void targetAwayFromPlayer(EntityLiving entityLiving){
        if(!entityLiving.isAIEnabled) return;

        double x = entityLiving.x;
        double z = entityLiving.z;

        double targetX = CosmicEvolution.instance.save.thePlayer.x;
        double targetZ = CosmicEvolution.instance.save.thePlayer.z;

        double dx = targetX - x;
        double dz = targetZ - z;

        entityLiving.yaw = (float) Math.toDegrees(Math.atan2(dz, dx));



        entityLiving.targetX = targetX;
        entityLiving.targetZ = targetZ;
        entityLiving.shouldMove = true;

        double distance = MathUtil.distance2D(x, z, targetX, targetZ);
        double distancePerTick = distancePerTick(entityLiving);

        entityLiving.moveTimer = (int) (distance / distancePerTick);
    }

    public static void targetAwayFromEntity(EntityLiving entityLiving, EntityLiving entityThatAlerted){
        if(!entityLiving.isAIEnabled) return;

        double x = entityLiving.x;
        double z = entityLiving.z;

        double targetX = entityThatAlerted.x;
        double targetZ = entityThatAlerted.z;

        double dx = targetX - x;
        double dz = targetZ - z;

        entityLiving.yaw = (float) Math.toDegrees(Math.atan2(dz, dx));



        entityLiving.targetX = targetX;
        entityLiving.targetZ = targetZ;
        entityLiving.shouldMove = true;

        double distance = MathUtil.distance2D(x, z, targetX, targetZ);
        double distancePerTick = distancePerTick(entityLiving);

        entityLiving.moveTimer = (int) (distance / distancePerTick);
    }


    private static double distancePerTick(EntityLiving entity) {
        double deltaX = 0;
        double deltaZ = 0;
        double distance = entity.rawDeltaX * entity.rawDeltaX; //If distance is ever 0.0 this will result in a NaN return and will cause the moveTimer to become 2^31
        distance = (float) (entity.speed / Math.sqrt(distance));
        entity.rawDeltaX *= distance;
        double sine = MathUtil.sin((float) Math.toRadians(entity.yaw));
        double cosine = MathUtil.cos((float) Math.toRadians(entity.yaw));
        deltaX += entity.rawDeltaX * cosine - 0 * sine;
        deltaZ += 0 * cosine + entity.rawDeltaX * sine;

        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }


}
