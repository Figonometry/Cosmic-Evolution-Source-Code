package spacegame.item;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.entity.EntityThrownSpear;
import spacegame.render.model.ModelLoader;
import spacegame.world.worldtypes.World;

public final class ItemSpear extends ItemTool implements IMultiModel {
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
        return this.getModelLoaderFromItemMetadata(itemMetadata);
    }
    @Override
    public ModelLoader getModelLoaderFromItemMetadata(short itemMetadata) {
        if(this.ID == Item.stoneSpear.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneSpear;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneSpear;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneSpear;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneSpear;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneSpear;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneSpear;
                }
            }
        }

        return this.itemModel;
    }

}
