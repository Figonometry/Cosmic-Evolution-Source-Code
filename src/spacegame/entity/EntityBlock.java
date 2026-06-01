package spacegame.entity;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.nbt.NBTTagCompound;
import spacegame.util.MathUtil;
import spacegame.core.Sound;
import spacegame.item.Item;
import spacegame.render.RenderEntityItem;
import spacegame.world.AxisAlignedBB;

import java.util.Random;

public final class EntityBlock extends EntityNonLiving {
    public short block;
    public boolean isOnGround;
    public boolean moveEntityUp;
    public double moveEntityUpDistance;
    public AxisAlignedBB lowerBlock = new AxisAlignedBB();
    public int pickupTimer = 0;
    public byte count;
    public EntityBlock(double x, double y, double z, short block, byte count){
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = 0.125F;
        this.height = 0.125F;
        this.depth = 0.125f;
        this.entityModel = Entity.standardBlock.copyModel();
        this.entityModel.scaleModel(0.125f);
        this.block = block;
        this.speed = 0.1;
        this.count = count;
        this.despawnTime = CosmicEvolution.instance.save.time + 54000;
        this.yaw = CosmicEvolution.globalRand.nextInt(360);
    }


    private void setEntityState(){
        this.boundingBox.adjustEntityBoundingBox(this.x, this.y, this.z, this.width, this.height, this.depth);
        if(this.pickupTimer < 120){
            this.pickupTimer++;
        }
        if(this.canMoveWithVector) {
            this.moveWithVector();
        }
        this.boundingBox.scale(0.5);
        if(CosmicEvolution.instance.save.thePlayer.boundingBox != null) {
            if (this.boundingBox.clip(CosmicEvolution.instance.save.thePlayer.boundingBox) && this.pickupTimer >= 60) {
                if (CosmicEvolution.instance.save.thePlayer.addItemToInventory(Item.block.ID, this.block, this.count, Item.NULL_ITEM_DURABILITY, 0, null)) {
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
        } else if(!this.canMoveWithVector){
            this.deltaX = 0;
        }

        if(headBlock >= 159 && headBlock <= 165 && !this.canMoveWithVector){
            this.deltaX = 0.01;
        } else if(!this.canMoveWithVector){
            this.deltaX = 0;
        }

        if(headBlock >= 166 && headBlock <= 172 && !this.canMoveWithVector){
            this.deltaZ = -0.01;
        } else if(!this.canMoveWithVector){
            this.deltaZ = 0;
        }

        if(headBlock >= 173 && headBlock <= 179 && !this.canMoveWithVector){
            this.deltaZ = 0.01;
        } else if(!this.canMoveWithVector){
            this.deltaZ = 0;
        }
        this.moveAndHandleCollision();
    }

    @Override
    public void render() {
        new RenderEntityItem(this.x, this.y, this.z, this.entityModel, true, true, Item.block.ID, this.block, this.height, this.width, this.yaw).renderEntity();
        if(Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y - 0.1), MathUtil.floorDouble(this.z))].isSolid) {
            this.renderShadow();
        }
    }

    @Override
    public void renderForShadowMap(int sunX, int sunY, int sunZ){
        new RenderEntityItem(this.x, this.y, this.z, this.entityModel, true, true, Item.block.ID, this.block, this.height, this.width, this.yaw).renderBlockForShadowMap(sunX,sunY,sunZ);
    }

    @Override
    public String getEntityType(){
        return "EntityBlock";
    }

    @Override
    public void saveToNBT(NBTTagCompound nbtTagCompound){
        nbtTagCompound.setString("entityType", "EntityBlock");
        nbtTagCompound.setShort("blockType", this.block);
        nbtTagCompound.setByte("count", this.count);
    }

}
