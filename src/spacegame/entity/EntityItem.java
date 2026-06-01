package spacegame.entity;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.item.itemstate.ItemState;
import spacegame.nbt.NBTTagCompound;
import spacegame.util.MathUtil;
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
    public long decayTime;
    public ItemState itemState;


    public EntityItem(double x, double y, double z, short item, short itemMetadata, byte count, short itemDurability, long decayTime, ItemState itemState){
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
        this.decayTime = decayTime;
        this.despawnTime = CosmicEvolution.instance.save.time + 54000;
        this.yaw = CosmicEvolution.globalRand.nextInt(360);
        this.itemState = itemState;
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
                if (CosmicEvolution.instance.save.thePlayer.addItemToInventory(this.item, Item.NULL_ITEM_METADATA, this.count, this.itemDurability, this.decayTime, this.itemState)) {
                    CosmicEvolution.instance.soundPlayer.playSound(this.x, this.y, this.z, new Sound(Sound.itemPickup, false, 1f), new Random().nextFloat(1.5F, 1.9F));
                    this.despawn = true;
                }
            }
        }
    }

    @Override
    public void tick(){
        this.setEntityState();
        this.doGravity();
        int x = MathUtil.floorDouble(this.x);
        int y = MathUtil.floorDouble(this.y);
        int z = MathUtil.floorDouble(this.z);

        short headBlock = CosmicEvolution.instance.save.activeWorld.getBlockID(x, y, z);

        if(headBlock >= 152 && headBlock <= 158 && !this.canMoveWithVector){
            this.deltaX = -0.01;
        }

        if(headBlock >= 159 && headBlock <= 165 && !this.canMoveWithVector){
            this.deltaX = 0.01;
        }

        if(headBlock >= 166 && headBlock <= 172 && !this.canMoveWithVector){
            this.deltaZ = -0.01;
        }

        if(headBlock >= 173 && headBlock <= 179 && !this.canMoveWithVector){
            this.deltaZ = 0.01;
        }
        this.moveAndHandleCollision();
    }


    @Override
    public void render() {
        new RenderEntityItem(this.x, this.y, this.z, null, false, false, this.item, this.itemMetadata, this.height, this.width, this.yaw).renderEntity();
        if(Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y - 0.1), MathUtil.floorDouble(this.z))].isSolid) {
            this.renderShadow();
        }
    }

    @Override
    public void renderForShadowMap(int sunX, int sunY, int sunZ){
        new RenderEntityItem(this.x, this.y, this.z, null, false, false, this.item, this.itemMetadata, this.height, this.width, this.yaw).renderItemForShadowMap(sunX,sunY,sunZ);
    }

    @Override
    public String getEntityType(){
        return "EntityItem";
    }

    @Override
    public void saveToNBT(NBTTagCompound nbtTagCompound){
        nbtTagCompound.setString("entityType", "EntityItem");
        nbtTagCompound.setShort("itemType", this.item);
        nbtTagCompound.setByte("count", this.count);
        nbtTagCompound.setShort("durability", this.itemDurability);
    }

}