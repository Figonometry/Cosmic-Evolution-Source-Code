package spacegame.entity;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.item.Item;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEntityItem;
import spacegame.util.MathUtil;

import java.util.Random;

public final class EntityThrownSpear extends EntityProjectile {
    public short itemID; //Item id needs to be stored to perform model lookup during render
    public short itemDurability;
    public int pickupTimer;


    public EntityThrownSpear(double x, double y, double z, Vector3f movementVector, double speed, short itemID, float pitch, float yaw, short itemDurability){
        this.x = x;
        this.y = y;
        this.z = z;
        this.movementVector = movementVector;
        this.canMoveWithVector = true;
        this.speed = speed;
        this.itemID = itemID;
        this.pitch = pitch;
        this.yaw = yaw;
        this.itemDurability = itemDurability;

        if(this.speed > 0.75f){
            this.speed = 0.75f;
        }
    }


    private void adjustPitch(){
        if(this.y == this.prevY)return;

        double hypotenuse = MathUtil.distance3D(this.x, this.y, this.z, this.prevX, this.prevY, this.prevZ);
        double adjacent = Math.abs(this.y - this.prevY);
        double angleCos = adjacent / hypotenuse;
        double angle = Math.acos(angleCos);

        angle = Math.toDegrees(angle);

        this.pitch = this.y > this.prevY ? (float) (90 - angle) : ((float) -(90 - angle));
    }

    private void setEntityState(){

        if(this.collided != this.prevCollided){
            CosmicEvolution.instance.soundPlayer.playSound(this.x, this.y, this.z, new Sound(Sound.spearImpact, false, 4f), 1f);
        }

        if(this.collided){
            this.canMoveWithVector = false;
        }


        this.boundingBox.adjustEntityBoundingBox(this.x, this.y, this.z, this.width, this.height, this.depth);

        if(this.canMoveWithVector) {
            this.moveWithVector();
        }

        this.pickupTimer++;
        if(this.pickupTimer >= 120) {
            this.boundingBox.scale(0.5);
            if (CosmicEvolution.instance.save.thePlayer.boundingBox != null) {
                if (this.boundingBox.clip(CosmicEvolution.instance.save.thePlayer.boundingBox)) {
                    if (CosmicEvolution.instance.save.thePlayer.addItemToInventory(this.itemID, Item.NULL_ITEM_METADATA, (byte) 1, this.itemDurability, 0, null)) {
                        CosmicEvolution.instance.soundPlayer.playSound(this.x, this.y, this.z, new Sound(Sound.itemPickup, false, 1f), new Random().nextFloat(1.5F, 1.9F));
                        this.despawn = true;
                    }
                }
            }
        }


        this.prevCollided = this.collided;
    }


    public void tick(){
        this.setEntityState();
        if(!this.collided) {
            this.doGravity();
            this.moveAndHandleCollision();
            this.adjustPitch();
        }

        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;

        if(CosmicEvolution.instance.save.activeWorld.hasHitEntity(this.x, this.y, this.z, true, this)){
            CosmicEvolution.instance.soundPlayer.playSound(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, new Sound(Sound.projectilePing, false, 1f), 1f);
            CosmicEvolution.instance.soundPlayer.playSound(this.x, this.y, this.z, new Sound(Sound.stabEntity, false, 1f), 5f);
            this.movementVector = new Vector3f(0, - 1, 0);
            this.itemDurability--;
        }
    }

    @Override
    public float getAttackDamageValue(){
        return 7f;
    }


    @Override
    public void render() {
        new RenderEntityItem(this.x, this.y, this.z, null, false, false, this.itemID, Item.NULL_ITEM_METADATA, this.height, this.width, this.yaw, this.pitch).renderEntity();
        if(Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y - 0.1), MathUtil.floorDouble(this.z))].isSolid) {
            this.renderShadow();
        }
    }

    @Override
    public String getEntityType() {
        return this.getClass().getSimpleName();
    }


    @Override
    public void saveToNBT(NBTTagCompound nbtTagCompound){
        nbtTagCompound.setString("entityType", "EntityThrownSpear");
        nbtTagCompound.setShort("itemID", this.itemID);
        nbtTagCompound.setShort("itemDurability", this.itemDurability);
        nbtTagCompound.setInteger("pickupTimer", this.pickupTimer);

        nbtTagCompound.setFloat("vec3fX", this.movementVector.x);
        nbtTagCompound.setFloat("vec3fY", this.movementVector.y);
        nbtTagCompound.setFloat("vec3fZ", this.movementVector.z);
        nbtTagCompound.setBoolean("canMoveWithVector", this.canMoveWithVector);
        nbtTagCompound.setDouble("speed", this.speed);
        nbtTagCompound.setBoolean("collided", this.collided);
    }
}
