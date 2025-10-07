package spacegame.entity;

import spacegame.core.MathUtil;
import spacegame.core.Sound;
import spacegame.core.SpaceGame;
import spacegame.entity.ai.AIPassive;
import spacegame.item.Item;
import spacegame.render.Model;
import spacegame.render.ModelDeer;
import spacegame.render.RenderEngine;
import spacegame.world.Tech;

import java.io.File;
import java.util.Random;

public final class EntityDeer extends EntityLiving {
    public static int texture;
    public int animationTimer = 0;
    public boolean animate = true;
    public boolean isMale;
    public boolean isChild;
    public boolean stopFrontLeftLeg;
    public boolean stopFrontRightLeg;
    public boolean stopBackLeftLeg;
    public boolean stopBackRightLeg;
    public int growthTimer;
    public Model model = new ModelDeer();

    public EntityDeer(double x, double y, double z, boolean isChild, boolean isMale){
        super(54000);
        this.x = x;
        this.y = y;
        this.z = z;
        this.isMale = isMale;
        this.isChild = isChild;
        this.health = 100f;
        this.maxHealth = 100f;
        this.height = 2;
        this.width = 0.5;
        this.depth = 0.5;
        this.rawDeltaX = -0.1f;
    }


    public static void loadTexture(){
        texture = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/entity/deer.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void tick(){
        this.checkToDespawn();
        this.updateAI();
        this.updateYawAndPitch();
        this.setMovementAmount();
        this.doGravity();
        this.setEntityState();
        this.moveAndHandleCollision();
        this.performStepSound();
        this.playAmbientSound();
        this.setCanEntityJump();

        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;

        this.checkHealth();
        this.animationTimer++;
        if(!this.stopFrontLeftLeg && !this.stopFrontRightLeg && !this.stopBackRightLeg && !this.stopBackLeftLeg && !this.animate){
            this.animationTimer = 0;
        }
    }

    private void updateAI(){
        this.moveTimer--;
        if(Math.abs(this.x - this.targetX) < 0.5 && Math.abs(this.z - this.targetZ) < 0.5 || this.moveTimer <= 0 && this.shouldMove){
            this.shouldMove = false;
            if(this.alerted){
                this.waitTimer = new Random().nextInt(15,30);
            } else {
                this.waitTimer = new Random().nextInt(180, 600);
            }
        }

        this.waitTimer--;
        if(this.waitTimer <= 0 && !this.shouldMove) {
            this.rawDeltaX = -0.1f;
            AIPassive.chooseNewTargetAndSetAngle(this);
        }
    }

    private void setCanEntityJump(){
        if(this.shouldMove && (this.deltaX == 0.0f || this.deltaZ == 0.0f) && this.isOnGround){
            this.moveEntityUp = true;
            this.moveEntityUpDistance = 1.1D;
            this.isOnGround = false;
            this.isJumping = true;
        }
    }

    private void setEntityState(){
        if(this.inWater){
            this.isOnGround = false;
        }

        if(this.inWater){
            if(!this.isOnGround && !this.moveEntityUp) {
                this.deltaY -= 0.05D;
                this.timeFalling = 0;
            }
            this.speed = 0.05D;
        } else {
            this.speed = 0.04D;
        }

        if (this.moveEntityUp) {
            if(!this.isJumping) {
                this.speed = 0.05D;
            }
            if (this.moveEntityUpDistance <= 0D) {
                this.moveEntityUp = false;
                this.moveEntityUpDistance = 0;
                this.speed = 0.05D;
                this.isJumping = false;
            } else {
                this.deltaY = 0.05D;
                this.moveEntityUpDistance -= 0.05D;
            }
        }

        this.damageTimer--;
        if(this.damageTimer <= 0){
            this.canDamage = true;
        }

        this.alertTimer--;
        if(this.alertTimer <= 0){
            this.alerted = false;
        }
    }

    private void updateYawAndPitch(){
        if (this.yaw >= 360) {
            this.yaw %= 360;
        }

    }

    private void setMovementAmount(){
        if(this.shouldMove) {
            this.rawDeltaX -= 0.1f;
        } else {
            this.rawDeltaX = 0.0f;
        }

        if(this.rawDeltaX < 0.0){
            this.animate = true;
            this.stopBackLeftLeg = false;
            this.stopBackRightLeg = false;
            this.stopFrontRightLeg = false;
            this.stopFrontLeftLeg = false;
        } else {
            this.animate = false;
        }

        this.updateGroundPosition(this.rawDeltaX, 0, 0);

        if(this.canMoveWithVector){
            this.moveWithVector();
        }
    }




    @Override
    public void checkHealth() {
        if(this.health <= 0){
            this.handleDeath();
        }
    }

    @Override
    public void handleDeath() {
        this.despawn = true;
        SpaceGame.instance.save.activeWorld.addEntity(new EntityItem(this.x, this.y, this.z, Item.rawVenison.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY));
        if(this.lastEntityToHit instanceof EntityPlayer){
            Tech.techUpdateEvent(Tech.UPDATE_EVENT_HUNT_ANIMAL);
        }
    }

    @Override
    public String getHurtSound() {
        return Sound.deerHurt;
    }

    @Override
    public String getAmbientSound() {
        return Sound.deerAmbient;
    }

    @Override
    public void render(){
        this.model = ModelDeer.getBaseModel();
        this.model.animate(this.animationTimer, this.animate, this);
        this.model.renderModel(this);
        this.renderShadow();
    }
}
