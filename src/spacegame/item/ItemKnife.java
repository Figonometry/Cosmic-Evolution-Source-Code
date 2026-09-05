package spacegame.item;

import spacegame.render.model.ModelLoader;

public final class ItemKnife extends ItemTool {

    public ItemKnife(short ID, String modelFilePath, String filepath, Material material) {
        super(ID, modelFilePath, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }


    @Override
    public ModelLoader getItemModel(short itemMetadata){
        return this.getModelLoaderFromItemMetadata(itemMetadata);
    }
    @Override
    public ModelLoader getModelLoaderFromItemMetadata(short itemMetadata) {
        if(this.ID == Item.stoneKnife.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneKnife;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneKnife;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneKnife;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneKnife;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneKnife;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneKnife;
                }
            }
        } else if(this.ID == Item.stoneHandKnifeBlade.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneKnifeBlade;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneKnifeBlade;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneKnifeBlade;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneKnifeBlade;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneKnifeBlade;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneKnifeBlade;
                }
            }
        }

        return this.itemModel;
    }
}
