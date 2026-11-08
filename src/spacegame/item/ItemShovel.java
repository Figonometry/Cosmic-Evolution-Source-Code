package spacegame.item;

import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;

public final class ItemShovel extends ItemTool {

    public ItemShovel(short ID, String modelFilePath, String filepath, Material material) {
        super(ID, modelFilePath, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }

    @Override
    public ModelLoader getItemModel(short itemMetadata){
        if(this.ID == Item.stoneShovel.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        } else if(this.ID == Item.stoneHandShovel.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        }

        return this.itemModel;
    }

}
