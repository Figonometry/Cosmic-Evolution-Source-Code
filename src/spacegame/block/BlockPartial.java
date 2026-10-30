package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.ItemWoodenPan;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.AxisAlignedBB;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.PartialBlock;
import spacegame.world.worldtypes.World;

//Class is primarily for gravel/sand being decremented by using the panning system, the model is assumed to be a standard block
public final class BlockPartial extends Block {
    public BlockPartial(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.woodenPan.ID)return;
        if(player.getHeldMetadata() != ItemWoodenPan.EMPTY_PAN)return;

        //Grab the existing state block, decrement the layers, check for no layers remaining and notify chunk
        PartialBlock partialBlock = (PartialBlock) world.getBlockState(x,y,z, MultiState.PARTIAL_BLOCK_STATE);
        if(partialBlock == null){
            return;
        }

        //Set the metadata in the slot equal to this blockID for rendering purposes on the pan and for the loot table to pull
        player.inventory.itemStacks[EntityPlayer.selectedInventorySlot].metadata = partialBlock.getBlockID();

        partialBlock.decrementNumLayers();
        if(partialBlock.getNumLayers() == 0){
            world.setBlockAndNotify(x,y,z, Block.air.ID, false);
        }

        world.notifyChunk(x,y,z);
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        PartialBlock partialBlock = (PartialBlock) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.PARTIAL_BLOCK_STATE);
        if(partialBlock == null)return this.displayName;

       return Block.list[partialBlock.getBlockID()].getDisplayName(x,y,z);
    }

    @Override
    public int getBlockTexture(int x, int y, int z, int face){
        PartialBlock partialBlockState = (PartialBlock) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.PARTIAL_BLOCK_STATE);
        if(partialBlockState == null){
            //If for whatever reason this fails it should be obvious
            return BlockTextureList.WATER_TOP_TEXTURE;
        }

        short blockID = partialBlockState.getBlockID();
        if(blockID == Block.NULL_BLOCK_REFERENCE){
            return BlockTextureList.WATER_TOP_TEXTURE;
        }

        //Find the stored blockID in the block state and delegate to the appropriate block object
        return Block.list[blockID].getBlockTexture(x,y,z, face);
    }

    //Decrement layers, once 0 layers reached replace with air
    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        PartialBlock partialBlockState = (PartialBlock) world.getBlockState(x, y, z, MultiState.PARTIAL_BLOCK_STATE);
        if(partialBlockState == null){
            world.setBlockAndNotify(x,y,z, Block.air.ID, true);
            return;
        }

        partialBlockState.decrementNumLayers();

        if(partialBlockState.getNumLayers() == 0){
            super.onLeftClick(x,y,z, world, player);
        }

        world.notifyChunk(x, y, z);
    }


    @Override
    public ModelLoader getBlockModel(int x, int y , int z, World world){
        PartialBlock partialBlockState = (PartialBlock) world.getBlockState(x,y,z, MultiState.PARTIAL_BLOCK_STATE);
        if(partialBlockState == null)return this.blockModel;

        float maxY = partialBlockState.getNumLayers() / 16.0f;

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
        PartialBlock partialBlockState = (PartialBlock) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.PARTIAL_BLOCK_STATE);
        if(partialBlockState == null)return;

        double maxY = partialBlockState.getNumLayers() / 16.0f;

        boundingBox.minX = x + this.standardCollisionBoundingBox.minX;
        boundingBox.maxX = x + this.standardCollisionBoundingBox.maxX;
        boundingBox.minY = y + this.standardCollisionBoundingBox.minY;
        boundingBox.maxY = y + maxY;
        boundingBox.minZ = z + this.standardCollisionBoundingBox.minZ;
        boundingBox.maxZ = z + this.standardCollisionBoundingBox.maxZ;
    }
}
