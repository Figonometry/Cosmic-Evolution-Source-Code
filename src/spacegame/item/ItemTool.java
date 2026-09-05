package spacegame.item;

import spacegame.block.Block;
import spacegame.render.model.ModelLoader;
import spacegame.item.StoneToolMetadata;

public class ItemTool extends Item implements IMultiModel {
    public ItemTool(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }



    @Override
    public ModelLoader getItemModel(short itemMetadata){
        return this.getModelLoaderFromItemMetadata(itemMetadata);
    }
    @Override
    public ModelLoader getModelLoaderFromItemMetadata(short itemMetadata) {
        if(this.ID == Item.stoneHoeHead.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneHoeHead;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneHoeHead;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneHoeHead;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneHoeHead;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneHoeHead;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneHoeHead;
                }
            }
        } else if(this.ID == Item.stoneFragments.ID){
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneFragments;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneFragments;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneFragments;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneFragments;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneFragments;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneFragments;
                }
            }
        } else if(this.ID == Item.stoneSpearHead.ID){
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.andesiteStoneSpearHead;
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.graniteStoneSpearHead;
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.basaltStoneSpearHead;
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.chertStoneSpearHead;
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.obsidianStoneSpearHead;
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.flintStoneSpearHead;
                }
            }
        }


        return this.itemModel;
    }


    @Override
    public String getDisplayName(short blockID, short metadata){
        switch (metadata) {
            case StoneToolMetadata.ANDESITE -> {
                return "Andesite " + this.displayName;
            }
            case StoneToolMetadata.GRANITE -> {
                return "Granite " + this.displayName;
            }
            case StoneToolMetadata.BASALT -> {
                return "Basalt " + this.displayName;
            }
            case StoneToolMetadata.CHERT -> {
                return "Chert " + this.displayName;
            }
            case StoneToolMetadata.OBSIDIAN -> {
                return "Obsidian " + this.displayName;
            }
            case StoneToolMetadata.FLINT -> {
                return "Flint " + this.displayName;
            }
        }

        return this.itemName;
    }

    @Override
    public short getDurability(short metadata){
        //This will not be condensed if I wish to change the values later
        switch (metadata) {
            case StoneToolMetadata.ANDESITE -> {
                return (short) (this.durability * 1.5f);
            }
            case StoneToolMetadata.GRANITE -> {
                return (short) (this.durability * 1.5f);
            }
            case StoneToolMetadata.BASALT -> {
                return (short) (this.durability * 2);
            }
            case StoneToolMetadata.CHERT -> {
                return this.durability;
            }
            case StoneToolMetadata.OBSIDIAN -> {
                return (short) (this.durability * 2.5f);
            }
            case StoneToolMetadata.FLINT -> {
                return this.durability;
            }
        }

        return this.durability;
    }

}
