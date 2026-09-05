package spacegame.item;

import spacegame.render.model.ModelLoader;

public final class ItemShovel extends ItemTool implements IMultiModel {

    public ItemShovel(short ID, String modelFilePath, String filepath, Material material) {
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
        if(this.ID == Item.stoneShovel.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneShovel;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneShovel;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneShovel;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneShovel;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneShovel;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneShovel;
                }
            }
        } else if(this.ID == Item.stoneHandShovel.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneHandShovel;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneHandShovel;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneHandShovel;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneHandShovel;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneHandShovel;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneHandShovel;
                }
            }
        }

        return this.itemModel;
    }
}
