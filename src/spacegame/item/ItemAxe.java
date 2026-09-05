package spacegame.item;

import spacegame.render.model.ModelLoader;

public final class ItemAxe extends ItemTool {
    public ItemAxe(short ID, String modelFilePath, String filepath, Material material) {
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
        if(this.ID == Item.stoneAxe.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneAxe;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneAxe;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneAxe;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneAxe;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneAxe;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneAxe;
                }
            }
        } else if(this.ID == Item.stoneHandAxe.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneHandAxe;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneHandAxe;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneHandAxe;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneHandAxe;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneHandAxe;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneHandAxe;
                }
            }
        }

        return this.itemModel;
    }

}
