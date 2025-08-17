package spacegame.entity;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.core.MathUtil;
import spacegame.core.Sound;
import spacegame.core.SoundPlayer;
import spacegame.core.SpaceGame;
import spacegame.render.RenderEntityItem;
import spacegame.world.AxisAlignedBB;

import java.util.Random;

public final class EntityItem extends EntityNonLiving {
    public short item;
    public boolean isOnGround;
    public boolean moveEntityUp;
    public double moveEntityUpDistance;
    public short itemMetadata;
    public AxisAlignedBB lowerBlock = new AxisAlignedBB();
    public int pickupTimer = 0;
    public byte count;
    public short itemDurability;


    public EntityItem(double x, double y, double z, short item, short itemMetadata, byte count, short itemDurability){
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = 0.125F;
        this.height = 0.125F;
        this.depth = 0.125f;
        this.item = item;
        this.itemMetadata = itemMetadata;
        this.speed = 0.1;
        this.count = count;
        this.itemDurability = itemDurability;
    }

    private void setEntityState() {
        if (this.pickupTimer < 120) {
            this.pickupTimer++;
        }
        if (this.canMoveWithVector) {
            this.moveWithVector();
        }
        this.boundingBox.scale(0.5);
        if (SpaceGame.instance.save.thePlayer.boundingBox != null) {
            if (this.boundingBox.clip(SpaceGame.instance.save.thePlayer.boundingBox) && this.pickupTimer >= 120) {
                if (SpaceGame.instance.save.thePlayer.addItemToInventory(this.item, (byte) 1, this.count, this.itemDurability)) {
                    new SoundPlayer(SpaceGame.instance).playSound(this.x, this.y, this.z, new Sound(Sound.itemPickup, false), new Random().nextFloat(1.5F, 1.9F));
                    this.despawn = true;
                }
            }
        }
    }

    @Override
    public void tick(){ //Phyis is broken
        this.setEntityState();
        this.doGravity();
        this.moveAndHandleCollision();
    }


    @Override
    public void render() {
        new RenderEntityItem(this.x, this.y, this.z, null, false, false, this.item, this.itemMetadata, this.height, this.width).renderEntity();
        if(Block.list[SpaceGame.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y - 0.1), MathUtil.floorDouble(this.z))].isSolid) {
            this.renderShadow();
        }
    }


    public void setMovementVector(Vector3f movementVector) {
        this.movementVector = movementVector;
        this.canMoveWithVector = true;
    }

}