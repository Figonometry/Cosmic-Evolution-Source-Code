package spacegame.entity;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.MathUtil;
import spacegame.core.Sound;
import spacegame.item.Item;
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
        this.despawnTime = CosmicEvolution.instance.save.time + 54000;
    }

    private void setEntityState() {
        if (this.pickupTimer < 120) {
            this.pickupTimer++;
        }
        if (this.canMoveWithVector) {
            this.moveWithVector();
        }
        this.boundingBox.scale(0.5);
        if (CosmicEvolution.instance.save.thePlayer.boundingBox != null) {
            if (this.boundingBox.clip(CosmicEvolution.instance.save.thePlayer.boundingBox) && this.pickupTimer >= 60) {
                if (CosmicEvolution.instance.save.thePlayer.addItemToInventory(this.item, Item.NULL_ITEM_METADATA, this.count, this.itemDurability)) {
                    CosmicEvolution.instance.soundPlayer.playSound(this.x, this.y, this.z, new Sound(Sound.itemPickup, false), new Random().nextFloat(1.5F, 1.9F));
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
        if(Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y - 0.1), MathUtil.floorDouble(this.z))].isSolid) {
            this.renderShadow();
        }
    }


}