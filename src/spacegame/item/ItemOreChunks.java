package spacegame.item;


import spacegame.block.Block;
import spacegame.block.BlockIDList;
import spacegame.block.BlockStone;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.entity.animations.PlayerAnimationPanning;
import spacegame.entity.animations.PlayerAnimationSwingHammer;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.OreState;
import spacegame.world.worldtypes.World;

public class ItemOreChunks extends Item {
    public ItemOreChunks(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }


    @Override
    public ModelLoader getItemModel(short itemMetadata){
        //metadata in this case is the stone block ID

        int replacementTexture = 0;

        switch (itemMetadata){
            case BlockIDList.ANDESITE_STONE -> {
                replacementTexture = ItemTextureList.ANDESITE_TEXTURE;
            }

            case BlockIDList.GRANITE_STONE -> {
                replacementTexture = ItemTextureList.GRANITE_TEXTURE;
            }

            case BlockIDList.PERIODITE_STONE -> {
                replacementTexture = ItemTextureList.PERIODITE_TEXTURE;
            }

            case BlockIDList.OBSIDIAN_STONE -> {
                replacementTexture = ItemTextureList.OBSIDIAN_TEXTURE;
            }

            case BlockIDList.BASALT_STONE -> {
                replacementTexture = ItemTextureList.BASALT_TEXTURE;
            }

            case BlockIDList.GABBRO_STONE -> {
                replacementTexture = ItemTextureList.GABBRO_TEXTURE;
            }

            case BlockIDList.CHALK_STONE -> {
                replacementTexture = ItemTextureList.CHALK_TEXTURE;
            }

            case BlockIDList.CHERT_STONE -> {
                replacementTexture = ItemTextureList.CHERT_TEXTURE;
            }

            case BlockIDList.CLAYSTONE_STONE -> {
                replacementTexture = ItemTextureList.CLAYSTONE_TEXTURE;
            }

            case BlockIDList.CONGLOMERATE_STONE -> {
                replacementTexture = ItemTextureList.CONGLOMERATE_TEXTURE;
            }

            case BlockIDList.SHALE_STONE -> {
                replacementTexture = ItemTextureList.SHALE_BOTTOM_TEXTURE;
            }

            case BlockIDList.LIMESTONE_STONE -> {
                replacementTexture = ItemTextureList.LIMESTONE_TEXTURE;
            }

            case BlockIDList.SANDSTONE_STONE -> {
                replacementTexture = ItemTextureList.SANDSTONE_TEXTURE;
            }

            case BlockIDList.MARBLE_STONE -> {
                replacementTexture = ItemTextureList.MARBLE_TEXTURE;
            }

            case BlockIDList.SLATE_STONE -> {
                replacementTexture = ItemTextureList.STONE_TEXTURE;
            }

            case BlockIDList.PHYLLITE_STONE -> {
                replacementTexture = ItemTextureList.PHYLLITE_TEXTURE;
            }

            case BlockIDList.SERPENTINITE_STONE -> {
                replacementTexture = ItemTextureList.SERPENTINITE_TEXTURE;
            }

        }


        switch (this.ID){
            case ItemIDList.POOR_NATIVE_COPPER_CHUNKS -> {
                return ItemModelList.andesitePoorCopperOreChunks.copyModel().replaceTargetTexture(ItemTextureList.ANDESITE_TEXTURE, replacementTexture);
            }
            case ItemIDList.MEDIUM_NATIVE_COPPER_CHUNKS -> {
                return ItemModelList.andesiteMediumCopperOreChunks.copyModel().replaceTargetTexture(ItemTextureList.ANDESITE_TEXTURE, replacementTexture);
            }
            case ItemIDList.RICH_NATIVE_COPPER_CHUNKS -> {
                return ItemModelList.andesiteRichCopperOreChunks.copyModel().replaceTargetTexture(ItemTextureList.ANDESITE_TEXTURE, replacementTexture);
            }
            case ItemIDList.BOUNTIFUL_NATIVE_COPPER_CHUNKS -> {
                return ItemModelList.andesiteBountifulCopperOreChunks.copyModel().replaceTargetTexture(ItemTextureList.ANDESITE_TEXTURE, replacementTexture);
            }
        }

        return this.itemModel;
    }


    @Override
    public String getDisplayName(short blockID, short metadata){
        return this.localizeRichness() + " " + this.localizeOre() + " chunks in " + this.localizeStoneType(metadata);
    }


    public static Item getOreChunkFromOreState(OreState oreState){
        switch (oreState.getOre()){
            case OreState.ORE_NATIVE_COPPER -> {
                switch (oreState.getOreRichness()){
                    case OreState.RICHNESS_POOR -> {
                        return Item.poorCopperChunks;
                    }
                    case OreState.RICHNESS_MEDIUM -> {
                        return Item.mediumCopperChunks;
                    }
                    case OreState.RICHNESS_HIGH -> {
                        return Item.richCopperChunks;
                    }
                    case OreState.RICHNESS_BOUNTIFUL -> {
                        return Item.bountifulCopperChunks;
                    }
                }
            }
        }



        return null;
    }


    @Override
    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player){
        //If player animation is null create a new hammer swinging animation,
        // if the player animation is an instance of swing hammer and timer is equal to 0 break the chunk open into subsequent items
        if(player.playerAnimation == null){
            player.playerAnimation = new PlayerAnimationSwingHammer(false, true, false, 60, -15);
        }


        if(player.playerAnimation instanceof PlayerAnimationSwingHammer && player.playerAnimation.timer == player.playerAnimation.minTimer){
            //Grab the item from the chest state

            ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            if(chestLocation == null)return;

            ItemOreChunks oreChunks = (ItemOreChunks) chestLocation.inventory.itemStacks[0].item;
            short stoneBlockID = chestLocation.inventory.itemStacks[0].metadata;

            short itemStoneBlockID = BlockStone.getItemStoneBlockIDFromStoneBlockID(stoneBlockID);
            short oreNuggetID = this.getOreNuggetsFromOre();
            byte itemStoneCount = BlockStone.getItemStoneCountFromOreChunks(oreChunks);
            byte oreNuggetCount = this.getOreNuggetCount();

            EntityItem entityItem;
            for(int i = 0; i < itemStoneCount; i++){
                entityItem = new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                        y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                        z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                        Item.block.ID, itemStoneBlockID, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, null);
                world.addEntity(entityItem);
            }

            for(int i = 0; i < oreNuggetCount; i++){
                entityItem = new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                        y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                        z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                        oreNuggetID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, null);
                world.addEntity(entityItem);
            }


            world.setBlockAndNotify(x,y,z, Block.air.ID, false);
            world.clearAllBlockStates(x,y,z);
        }
    }

    private short getOreNuggetsFromOre(){
        switch (this.ID){
            case ItemIDList.POOR_NATIVE_COPPER_CHUNKS, ItemIDList.MEDIUM_NATIVE_COPPER_CHUNKS, ItemIDList.RICH_NATIVE_COPPER_CHUNKS, ItemIDList.BOUNTIFUL_NATIVE_COPPER_CHUNKS -> {
                return ItemIDList.NATIVE_COPPER_NUGGETS;
            }
            default -> {
                return ItemIDList.NATIVE_COPPER_NUGGETS;
            }
        }
    }

    private byte getOreNuggetCount(){
        switch (this.ID){
            case ItemIDList.POOR_NATIVE_COPPER_CHUNKS -> {
                return 1;
            }
            case ItemIDList.MEDIUM_NATIVE_COPPER_CHUNKS -> {
                return 5;
            }
            case ItemIDList.RICH_NATIVE_COPPER_CHUNKS -> {
                return 10;
            }
            case ItemIDList.BOUNTIFUL_NATIVE_COPPER_CHUNKS -> {
                return 20;
            }
            default -> {
                return 0;
            }
        }
    }


    private String localizeOre(){
        switch (this.ID){
            case ItemIDList.POOR_NATIVE_COPPER_CHUNKS, ItemIDList.MEDIUM_NATIVE_COPPER_CHUNKS, ItemIDList.RICH_NATIVE_COPPER_CHUNKS, ItemIDList.BOUNTIFUL_NATIVE_COPPER_CHUNKS -> {
                return "Native Copper";
            }
            default -> {
                return "NO ORE";
            }
        }
    }

    private String localizeRichness(){
        switch (this.ID){
            case ItemIDList.POOR_NATIVE_COPPER_CHUNKS -> {
                return "Poor";
            }
            case ItemIDList.MEDIUM_NATIVE_COPPER_CHUNKS -> {
                return "Medium";
            }
            case ItemIDList.RICH_NATIVE_COPPER_CHUNKS -> {
                return "High";
            }
            case ItemIDList.BOUNTIFUL_NATIVE_COPPER_CHUNKS -> {
                return "Bountiful";
            }
            default -> {
                return "NO RICHNESS";
            }
        }
    }


    private String localizeStoneType(short metadata){
        switch (metadata){
            case BlockIDList.ANDESITE_STONE -> {
                return "Andesite";
            }
            case BlockIDList.GRANITE_STONE -> {
                return "Granite";
            }
            case BlockIDList.PERIODITE_STONE -> {
                return "Periodite";
            }
            case BlockIDList.OBSIDIAN_STONE -> {
                return "Obsidian";
            }
            case BlockIDList.BASALT_STONE -> {
                return "Basalt";
            }
            case BlockIDList.GABBRO_STONE -> {
                return "Gabbro";
            }
            case BlockIDList.CHALK_STONE -> {
                return "Chalk";
            }
            case BlockIDList.CHERT_STONE -> {
                return "Chert";
            }
            case BlockIDList.CLAYSTONE_STONE -> {
                return "Claystone";
            }
            case BlockIDList.CONGLOMERATE_STONE -> {
                return "Conglomerate";
            }
            case BlockIDList.SHALE_STONE -> {
                return "Shale";
            }
            case BlockIDList.LIMESTONE_STONE -> {
                return "Limestone";
            }
            case BlockIDList.SANDSTONE_STONE -> {
                return "Sandstone";
            }
            case BlockIDList.MARBLE_STONE -> {
                return "Marble";
            }
            case BlockIDList.SLATE_STONE -> {
                return "Slate";
            }
            case BlockIDList.PHYLLITE_STONE -> {
                return "Phyllite";
            }
            case BlockIDList.SERPENTINITE_STONE -> {
                return "Serpentinite";
            }
            default -> {
                return "NO STONE";
            }
        }
    }



}
