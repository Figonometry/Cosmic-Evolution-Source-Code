package spacegame.item;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.entity.EntityThrownSpear;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;
import spacegame.world.worldtypes.World;

public final class ItemSpear extends ItemTool {
    public ItemSpear(short ID, String modelFilePath, String filepath, Material material) {
        super(ID, modelFilePath, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }


    @Override
    public void onDrawBackRelease(EntityPlayer entityPlayer, World world){
        world.addEntity(new EntityThrownSpear(entityPlayer.x, entityPlayer.y, entityPlayer.z, entityPlayer.getNormalizedVectorFromEye(), entityPlayer.drawbackTimer / 180f, this.ID, entityPlayer.pitch, entityPlayer.yaw, entityPlayer.getHeldItemDurability()));
        CosmicEvolution.instance.soundPlayer.playSound(entityPlayer.x, entityPlayer.y, entityPlayer.z, new Sound(Sound.whoosh, false, 1f), 1f);
        entityPlayer.removeItemFromInventory();
    }

    @Override
    public Sound getEntityHitSound(){
        return new Sound(Sound.stabEntity, false, 1f);
    }

    @Override
    public ModelLoader getItemModel(short itemMetadata){
        if(this.ID == Item.stoneSpear.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        }

        return this.itemModel;
    }

}
