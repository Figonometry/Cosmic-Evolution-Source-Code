package spacegame.item;

import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;

public final class ItemKnife extends ItemTool {

    public ItemKnife(short ID, String modelFilePath, String filepath, Material material) {
        super(ID, modelFilePath, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }


    @Override
    public ModelLoader getItemModel(short itemMetadata){
        if(this.ID == Item.stoneKnife.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        } else if(this.ID == Item.stoneHandKnifeBlade.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        }

        return this.itemModel;
    }
}
