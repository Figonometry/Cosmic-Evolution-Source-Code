package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.AxisAlignedBB;
import spacegame.world.blockstate.CharcoalState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.worldtypes.World;

public final class BlockCharcoal extends Block {
    public BlockCharcoal(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public int getBlockTexture(int x, int y, int z, int face){

        if(face == FACE_UP || face == FACE_DOWN){
            return BlockTextureList.CHARCOAL_TEXTURE;
        }

        return BlockTextureList.CHARCOAL_SIDE;
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        CharcoalState charcoalState = (CharcoalState) world.getBlockState(x, y, z, MultiState.CHARCOAL_STATE);
        if(charcoalState == null){
            world.setBlockAndNotify(x,y,z, Block.air.ID, true);
            return;
        }

        charcoalState.numLayers--;
        double localY = (charcoalState.numLayers / 16.0);
        EntityItem charcoalItem = new EntityItem(x + 0.5, y + localY + 0.1, z + 0.5, Item.charcoal.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, null);
        world.addEntity(charcoalItem);

        if(charcoalState.numLayers == 0){
            super.onLeftClick(x,y,z, world, player);
        }

        world.notifyChunk(x, y, z);
    }

    @Override
    public ModelLoader getBlockModel(int x, int y , int z, World world){
        CharcoalState charcoalState = (CharcoalState) world.getBlockState(x,y,z, MultiState.CHARCOAL_STATE);
        if(charcoalState == null)return this.blockModel;

        float maxY = charcoalState.numLayers / 16.0f;

        ModelLoader charcoalModel = BlockModelList.standardBlockModel.copyModel();

        ModelFace topFace = charcoalModel.getModelFace(Block.FACE_UP);
        ModelFace northFace = charcoalModel.getModelFace(Block.FACE_NORTH);
        ModelFace southFace = charcoalModel.getModelFace(Block.FACE_SOUTH);
        ModelFace eastFace = charcoalModel.getModelFace(Block.FACE_EAST);
        ModelFace westFace = charcoalModel.getModelFace(Block.FACE_WEST);


        for(int i = 0; i < topFace.vertices.length; i++){
            topFace.vertices[i].y = maxY;
        }


        for(int i = 0; i < northFace.vertices.length; i++){
            if(northFace.vertices[i].y > 0.0f){
                northFace.vertices[i].y = maxY;
            }

            if(northFace.UVs[i][1] > 0.0f){
                northFace.UVs[i][1] = maxY;
            }
        }

        for(int i = 0; i < southFace.vertices.length; i++){
            if(southFace.vertices[i].y > 0.0f){
                southFace.vertices[i].y = maxY;
            }

            if(southFace.UVs[i][1] > 0.0f){
                southFace.UVs[i][1] = maxY;
            }
        }

        for(int i = 0; i < eastFace.vertices.length; i++){
            if(eastFace.vertices[i].y > 0.0f){
                eastFace.vertices[i].y = maxY;
            }

            if(eastFace.UVs[i][1] > 0.0f){
                eastFace.UVs[i][1] = maxY;
            }
        }
        for(int i = 0; i < westFace.vertices.length; i++){
            if(westFace.vertices[i].y > 0.0f){
                westFace.vertices[i].y = maxY;
            }

            if(westFace.UVs[i][1] > 0.0f){
                westFace.UVs[i][1] = maxY;
            }
        }


        return charcoalModel;
    }


    @Override
    public void adjustBoundingBox(int x, int y, int z, AxisAlignedBB boundingBox){
        CharcoalState charcoalState = (CharcoalState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CHARCOAL_STATE);
        if(charcoalState == null)return;

        double maxY = charcoalState.numLayers / 16.0f;

        boundingBox.minX = x + this.standardCollisionBoundingBox.minX;
        boundingBox.maxX = x + this.standardCollisionBoundingBox.maxX;
        boundingBox.minY = y + this.standardCollisionBoundingBox.minY;
        boundingBox.maxY = y + maxY;
        boundingBox.minZ = z + this.standardCollisionBoundingBox.minZ;
        boundingBox.maxZ = z + this.standardCollisionBoundingBox.maxZ;
    }
}
