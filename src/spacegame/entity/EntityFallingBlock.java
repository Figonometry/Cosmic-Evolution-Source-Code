package spacegame.entity;

import spacegame.core.CosmicEvolution;
import spacegame.item.Item;
import spacegame.render.RenderEntityItem;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;


public final class EntityFallingBlock extends EntityNonLiving {
    public short blockID;
    public ModelLoader blockModel;


    public EntityFallingBlock(double x, double y, double z, short blockID, ModelLoader blockModel){
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockID = blockID;
        this.blockModel = blockModel;
        this.height = 1;
        this.width = 1;
    }

    private void setEntityState(){

        this.boundingBox.adjustEntityBoundingBox(this.x, this.y, this.z, this.width, this.height, this.depth);


        if(this.collided){
            CosmicEvolution.instance.save.activeWorld.setBlockWithNotify(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z), this.blockID, false);
            this.despawn = true;
        }

    }


    public void tick(){
        this.setEntityState();
        if(!this.collided && (CosmicEvolution.instance.save.time & 1) == 0) {
            this.doGravity();
            this.moveAndHandleCollision();
        }

        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;

    }


    @Override
    public void render() {
        new RenderEntityItem(this.x, this.y, this.z, this.blockModel, true, true, Item.NULL_ITEM_REFERENCE, this.blockID, this.height, this.width, this.yaw, this.pitch, true).renderEntity();
    }



    @Override
    public String getEntityType() {
        return this.getClass().getSimpleName();
    }
}
