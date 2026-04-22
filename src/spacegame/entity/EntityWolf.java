package spacegame.entity;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.core.Timer;
import spacegame.entity.ai.AIHostile;
import spacegame.entity.ai.AIPassive;
import spacegame.item.IDecayItem;
import spacegame.item.Item;
import spacegame.render.RenderEngine;
import spacegame.render.model.Model;
import spacegame.render.model.ModelDeer;
import spacegame.render.model.ModelWolf;
import spacegame.util.MathUtil;
import spacegame.world.World;

import java.util.Random;

public final class EntityWolf extends EntityLiving implements IDecayable, IHarvestable {
    public boolean stopFrontLeftLeg;
    public boolean stopFrontRightLeg;
    public boolean stopBackLeftLeg;
    public boolean stopBackRightLeg;
    public boolean isChild;
    public boolean isMale;
    public int animationTimer;
    public boolean animate;
    public short blockUnderWolf;
    public int outOfWaterJumpDelay;
    public boolean isLeavingWater;
    public boolean alertedToPlayer;
    public static int ticksSinceLastRender;
    public Model model;
    public static int texture = RenderEngine.NULL_TEXTURE;
    public EntityWolf(double x, double y, double z, boolean isChild, boolean isMale) {
        super(54000);
        this.x = x;
        this.y = y;
        this.z = z;
        this.isChild = isChild;
        this.isMale = isMale;
        this.health = 100f;
        this.maxHealth = 100f;
        this.height = 1.4;
        this.width = 0.5;
        this.depth = 0.5;
        this.deathWidth = 0.5;
        this.deathHeight = 0.5;
        this.deathDepth = 0.5;
        this.rawDeltaX = -0.1f;
    }


    public void loadTexture(){
        texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/entity/wolf.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void tick(){
        if (CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(MathUtil.floorDouble(this.x) >> 5, MathUtil.floorDouble(this.y) >> 5, MathUtil.floorDouble(this.z) >> 5) != null && !CosmicEvolution.instance.save.activeWorld.paused) {

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
            if (!this.stopFrontLeftLeg && !this.stopFrontRightLeg && !this.stopBackRightLeg && !this.stopBackLeftLeg && !this.animate) {
                this.animationTimer = 0;
            }
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

        if(this.isPlayerInRange() && !this.alertedToPlayer){
            this.alertedToPlayer = true;
        }

        this.waitTimer--;
        if(this.waitTimer <= 0 && !this.shouldMove && !this.alertedToPlayer) {
            this.rawDeltaX = -0.1f;
            AIPassive.chooseNewTargetAndSetAngle(this);
        } else if(this.alertedToPlayer){
            this.rawDeltaX = -0.1f;
            this.speed = 0.1f;
            AIHostile.targetPlayer(this);
        }

        if(!this.isPlayerInRange() && this.alertedToPlayer){
            this.alertedToPlayer = false;
        }
    }

    private boolean isPlayerInRange(){
        return MathUtil.distance3DSquared(this.x, this.y, this.z, CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z) <= 40;
    }

    private void setCanEntityJump(){
        if(this.shouldMove && (this.deltaX == 0.0f || this.deltaZ == 0.0f) && this.isOnGround && !this.isDead){
            this.moveEntityUp = true;
            this.moveEntityUpDistance = 1.1D;
            this.isOnGround = false;
            this.isJumping = true;
        }
    }

    private void setEntityState(){
        int deerX = MathUtil.floorDouble(this.x);
        int deerY = MathUtil.floorDouble(this.y);
        int deerUpper = MathUtil.floorDouble(this.y + (this.height * 0.25));
        int deerLower = MathUtil.floorDouble(this.y - (this.height * 0.25));
        int deerZ = MathUtil.floorDouble(this.z);
        short headBlock = CosmicEvolution.instance.save.activeWorld.getBlockID(deerX, deerUpper, deerZ);
        short footBlock = CosmicEvolution.instance.save.activeWorld.getBlockID(deerX, deerLower, deerZ);
        this.inWater = Block.list[headBlock].waterlogged || Block.list[footBlock].waterlogged;
        this.blockUnderWolf = CosmicEvolution.instance.save.activeWorld.getBlockID(deerX, MathUtil.floorDouble(this.y - (this.height/2) - 0.1), deerZ);
        if(this.inWater){
            this.isOnGround = false;
        }

        this.swimming = this.inWater || this.prevInWater; //Seems counterintuitive but it's needed to trigger the jump out of the water

        if(this.swimming && this.outOfWaterJumpDelay <= 0){
            this.isOnGround = false;
            this.isJumping = false;
            this.jumpTime = 0;
            this.deltaY = 0.05;
        }

        if(this.inWater){
            this.isOnGround = false;
            if(!this.isOnGround && !this.moveEntityUp && !this.swimming) { //Leave this in case we ever want to remove the ability to always swim
                this.deltaY -= 0.05D;
                this.timeFalling = 0;
            }
            this.speed = 0.025;
        } else {
            this.swimming = false;
            this.speed = 0.04D;
        }

        if (this.moveEntityUp) {
            if(!this.isJumping) {
                this.speed = 0.025;
            }
            if (this.moveEntityUpDistance <= 0D) {
                this.moveEntityUp = false;
                this.moveEntityUpDistance = 0;
                this.speed = 0.025;
                this.isJumping = false;
            } else {
                this.deltaY = 0.05;
                this.moveEntityUpDistance -= 0.05;
            }
        }


        if(this.prevInWater && !this.inWater && !this.moveEntityUp && this.deltaY > 0.0 && this.outOfWaterJumpDelay <= 0){
            this.isLeavingWater = true;
            this.moveEntityUp = true;
            this.moveEntityUpDistance = 0.6;
            this.timeFalling = 0;
            this.isJumping = false;
            this.outOfWaterJumpDelay = 45;
        } else {
            this.outOfWaterJumpDelay--;
        }

        if(this.isLeavingWater){
            if(!this.moveEntityUp && ((Block.list[headBlock].waterlogged && Block.list[footBlock].waterlogged) ||
                    (Block.list[footBlock].waterlogged && Block.list[this.blockUnderWolf].isSolid))){
                this.isLeavingWater = false;
            }
            if(this.isOnGround){
                this.isLeavingWater = false;
            }
        }

        this.damageTimer--;
        if(this.damageTimer <= 0){
            this.canDamage = true;
        }

        this.prevInWater = this.inWater;

        this.alertTimer--;
        if(this.alertTimer <= 0){
            this.alerted = false;
        }
        this.handleFallDamage();


        if(this.timeDied + 60 <= CosmicEvolution.instance.save.time){

        }

        if(this.isDead){
            this.width = this.deathWidth;
            this.height = this.deathHeight;
            this.depth = this.deathDepth;
        }
    }

    private void updateYawAndPitch(){
        if (this.yaw >= 360) {
            this.yaw %= 360;
        }

    }

    private void setMovementAmount(){
        if(this.shouldMove && !this.isDead) {
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

        if(this.boundingBox.clip(CosmicEvolution.instance.save.thePlayer.boundingBox) && !this.isDead){
            CosmicEvolution.instance.save.thePlayer.damage(5);
            //Play sound of the wolf attacking
        }

        if(this.canMoveWithVector){
            this.moveWithVector();
        }
    }




    @Override
    public void checkHealth() {
        if(this.health <= 0 && !this.isDead){
            this.handleDeath();
        }
    }

    @Override
    public void handleDeath() {
        this.isDead = true;
        this.isAIEnabled = false;
        ModelWolf.getBaseModel().getLegAnglesAtTimeOfDeath(this.animationTimer, this.animate, this);
        this.timeDied = CosmicEvolution.instance.save.time;
        this.animate = false;
    }

    @Override
    public void destroyOnDecay(){
        this.despawn = true;
        //Replace entity with a corpse block of some kind
    }

    public long getDecayTime(){
        return Timer.GAME_DAY * 2;
    }

    @Override
    public String getHurtSound() {
        return this.isDead ? null : Sound.wolfHurt;
    }

    @Override
    public String getAmbientSound() {
        return this.isDead ? null : Sound.wolfAmbient;
    }

    @Override
    public void render(){
        if(texture == RenderEngine.NULL_TEXTURE){
            this.loadTexture();
        }
        this.model = ModelWolf.getBaseModel();
        this.model.animate(this.animationTimer, this.animate, this);
        this.model.renderModel(this);
        ticksSinceLastRender = 0;
        this.renderShadow();
    }
    @Override
    public void renderForShadowMap(int sunX, int sunY, int sunZ){
        this.model = ModelWolf.getBaseModel();
        this.model.animate(this.animationTimer, this.animate, this);
        this.model.renderModelForShadowMap(this, sunX, sunY, sunZ);
    }

    @Override
    public void dropItems(double x, double y, double z, World world, EntityPlayer player) {

        if(!player.addItemToInventory(Item.rawVenison.ID, Item.NULL_ITEM_METADATA, (byte) CosmicEvolution.globalRand.nextInt(1,5), Item.NULL_ITEM_DURABILITY, CosmicEvolution.instance.save.time + ((IDecayItem)(Item.rawVenison)).getDecayTime())){
            world.addEntity(new EntityItem(this.x, this.y, this.z, Item.rawVenison.ID, Item.NULL_ITEM_METADATA, (byte) CosmicEvolution.globalRand.nextInt(1, 5), Item.NULL_ITEM_DURABILITY, world.ce.save.time + ((IDecayItem)Item.rawVenison).getDecayTime()));
        }
            //Change to appropriate wolf drops
        if(!player.addItemToInventory(Item.deerHide.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY, 0)){
            world.addEntity(new EntityItem(this.x, this.y, this.z, Item.deerHide.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY, 0));
        }


        this.destroyOnDecay();
    }
}
