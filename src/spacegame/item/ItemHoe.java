package spacegame.item;

import spacegame.block.Block;
import spacegame.block.BlockGrass;
import spacegame.block.BlockSoil;
import spacegame.block.BlockTilledSoil;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.entity.animations.PlayerAnimationTillingSoil;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.TilledSoilState;

public final class ItemHoe extends ItemTool {
    public ItemHoe(short ID, String modelFilePath, String filepath, Material material) {
        super(ID, modelFilePath, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }



    public void onRightClick(int x, int y, int z, World world, EntityPlayer player) {
        if(!MouseListener.rightClickReleased)return;
        player.playerAnimation = new PlayerAnimationTillingSoil(false, true, false, 60, -15);
        MouseListener.rightClickReleased = false;
    }

    public void onFinishRightClickAnimation(int x, int y, int z, World world, EntityPlayer player){
        int[] coordinatesPlayerIsLookingAt = player.getPlayerLookingAtBlockCoords();
        x = coordinatesPlayerIsLookingAt[0];
        y = coordinatesPlayerIsLookingAt[1];
        z = coordinatesPlayerIsLookingAt[2];
        short blockID = world.getBlockID(x,y,z);

        if(!(Block.list[blockID] instanceof BlockSoil) && !(Block.list[blockID] instanceof BlockGrass))return;
        if(world.getBlockID(x, y + 1, z) != Block.air.ID)return;

        float nutrientPercent = BlockSoil.getNutrientLevel(blockID);

        world.setBlockAndNotify(x,y,z, Block.tilledSoil.ID, true);
        world.addTimeEvent(x,y,z, world.ce.save.time + ((BlockTilledSoil)Block.tilledSoil).getUpdateTime(x,y,z,world));
        world.addBlockState(x,y,z, MultiState.TILLED_SOIL_STATE, new TilledSoilState(Chunk.getBlockIndexFromCoordinates(x,y,z), 0.5f, nutrientPercent, nutrientPercent, nutrientPercent, TilledSoilState.NO_FERTILIZER));
        CosmicEvolution.instance.soundPlayer.playSound(player.x, player.y, player.z, new Sound(Sound.dirt, false, 1f), 1f);
        player.reduceHeldItemDurability();
    }

    @Override
    public ModelLoader getItemModel(short itemMetadata){
        if(this.ID == Item.stoneHoe.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        }

        return this.itemModel;
    }
}
