package spacegame.item;

import spacegame.block.Block;
import spacegame.render.model.ModelLoader;
import spacegame.item.StoneToolMetadata;
import spacegame.render.texturelists.ItemTextureList;

public class ItemTool extends Item {
    public ItemTool(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }



    @Override
    public ModelLoader getItemModel(short itemMetadata){
        if(this.ID == Item.stoneHoeHead.ID) {
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        } else if(this.ID == Item.stoneFragments.ID){
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
                }
            }
        } else if(this.ID == Item.stoneSpearHead.ID){
            switch (itemMetadata) {
                case StoneToolMetadata.ANDESITE -> {
                    return ItemModelList.stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
                }
                case StoneToolMetadata.GRANITE -> {
                    return ItemModelList.stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
                }
                case StoneToolMetadata.BASALT -> {
                    return ItemModelList.stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
                }
                case StoneToolMetadata.CHERT -> {
                    return ItemModelList.stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
                }
                case StoneToolMetadata.OBSIDIAN -> {
                    return ItemModelList.stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
                }
                case StoneToolMetadata.FLINT -> {
                    return ItemModelList.stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
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
