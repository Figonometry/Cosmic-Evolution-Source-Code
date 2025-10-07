package spacegame.entity;

import spacegame.block.Block;
import spacegame.core.MathUtil;
import spacegame.core.Sound;
import spacegame.core.SoundPlayer;
import spacegame.core.SpaceGame;
import spacegame.entity.ai.AIPassive;
import spacegame.gui.GuiWorldLoadingScreen;
import spacegame.world.AxisAlignedBB;

import java.util.Random;

public abstract class EntityLiving extends Entity {
    public float prevDeltaYaw;
    public float prevDeltaPitch;
    public int jumpTime;
    public double moveEntityUpDistance;
    public int stepTimer = 180;
    public float health;
    public float maxHealth;
    public int damageTimer;
    public boolean alerted;
    public int alertTimer;
    public AxisAlignedBB lowerBlock = new AxisAlignedBB();
    public AxisAlignedBB upperBlock = new AxisAlignedBB();
    public AxisAlignedBB northBlock = new AxisAlignedBB();
    public AxisAlignedBB southBlock = new AxisAlignedBB();
    public AxisAlignedBB eastBlock = new AxisAlignedBB();
    public AxisAlignedBB westBlock = new AxisAlignedBB();

    public EntityLiving(int maxTimeAlive){
        if(!(this instanceof EntityPlayer)) {
            this.despawnTime = SpaceGame.instance.save.time + SpaceGame.globalRand.nextLong(maxTimeAlive);
        }
    }

    public abstract void checkHealth();

    public abstract void handleDeath();

    public abstract String getHurtSound();

    public abstract String getAmbientSound();

    public void damage(float healthReduction){
        this.health -= healthReduction;
        this.damageTimer = 30;
        this.canDamage = false;
        this.alerted = true;
        this.alertTimer = new Random().nextInt(300, 600);
        AIPassive.chooseNewTargetAndSetAngle(this);
        new SoundPlayer(SpaceGame.instance).playSound(this.x, this.y, this.z, new Sound(this.getHurtSound(), false), new Random().nextFloat(0.9F, 1));
    }

    public void playAmbientSound(){
        if(SpaceGame.globalRand.nextInt(1200) == 0){
            new SoundPlayer(SpaceGame.instance).playSound(this.x, this.y, this.z, new Sound(this.getAmbientSound(), false), new Random().nextFloat(0.9F, 1));
        }
    }


    protected void performStepSound(){
        if((SpaceGame.instance.currentGui instanceof GuiWorldLoadingScreen))return;
            int x = MathUtil.floorDouble(this.x);
            int z = MathUtil.floorDouble(this.z);
            int prevX = MathUtil.floorDouble(this.prevX);
            int prevZ = MathUtil.floorDouble(this.prevZ);
            if ((x != prevX || z != prevZ) && this.stepTimer <= 0) {
                int lowerY = MathUtil.floorDouble(this.y - (this.height/2) - 0.1);
                new SoundPlayer(SpaceGame.instance).playSound(this.x, lowerY, this.z, new Sound(Block.list[SpaceGame.instance.save.activeWorld.getBlockID(x, lowerY, z)].stepSound, false), new Random().nextFloat(0.6F, 1));
                this.stepTimer = 30;
            } else {
                this.stepTimer--;
            }
    }


}
