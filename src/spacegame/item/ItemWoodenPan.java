package spacegame.item;


import spacegame.block.Block;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.entity.animations.PlayerAnimationPanning;
import spacegame.render.model.ModelLoader;
import spacegame.block.BlockIDList;
import spacegame.render.texturelists.ItemTextureList;
import spacegame.world.worldtypes.World;

public final class ItemWoodenPan extends Item {
    public static final int EMPTY_PAN = NULL_ITEM_METADATA;
    private static final LootTable andesiteSandLootTable = new LootTable();
    private static final LootTable andesiteGravelLootTable = new LootTable();
    private static final LootTable graniteSandLootTable = new LootTable();
    private static final LootTable graniteGravelLootTable = new LootTable();
    private static final LootTable perioditeSandLootTable = new LootTable();
    private static final LootTable perioditeGravelLootTable = new LootTable();
    private static final LootTable obsidianSandLootTable = new LootTable();
    private static final LootTable obsidianGravelLootTable = new LootTable();
    private static final LootTable basaltSandLootTable = new LootTable();
    private static final LootTable basaltGravelLootTable = new LootTable();
    private static final LootTable gabbroSandLootTable = new LootTable();
    private static final LootTable gabbroGravelLootTable = new LootTable();
    private static final LootTable chalkSandLootTable = new LootTable();
    private static final LootTable chalkGravelLootTable = new LootTable();
    private static final LootTable chertSandLootTable = new LootTable();
    private static final LootTable chertGravelLootTable = new LootTable();
    private static final LootTable claystoneSandLootTable = new LootTable();
    private static final LootTable claystoneGravelLootTable = new LootTable();
    private static final LootTable conglomerateSandLootTable = new LootTable();
    private static final LootTable conglomerateGravelLootTable = new LootTable();
    private static final LootTable shaleSandLootTable = new LootTable();
    private static final LootTable shaleGravelLootTable = new LootTable();
    private static final LootTable limestoneSandLootTable = new LootTable();
    private static final LootTable limestoneGravelLootTable = new LootTable();
    private static final LootTable sandstoneSandLootTable = new LootTable();
    private static final LootTable sandstoneGravelLootTable = new LootTable();
    private static final LootTable marbleSandLootTable = new LootTable();
    private static final LootTable marbleGravelLootTable = new LootTable();
    private static final LootTable phylliteSandLootTable = new LootTable();
    private static final LootTable phylliteGravelLootTable = new LootTable();
    private static final LootTable serpentiniteSandLootTable = new LootTable();
    private static final LootTable serpentiniteGravelLootTable = new LootTable();

    public ItemWoodenPan(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }

    //Metadata stores the blockID of the block material contained in the pan, metadata 0 means the pan is empty
    @Override
    public ModelLoader getItemModel(short itemMetadata){
        if(itemMetadata == EMPTY_PAN)return this.itemModel;



        //Copy the filled pan model and replace the textureID for the material with whatever block texture the metadata corresponds to
        ModelLoader filledPan = ItemModelList.woodenPanFilled.copyModel();
        int replacementTexture = 0;

        //Texture comes from the item texture list, converted into the block texture list as needed by the render pipeline
        switch (itemMetadata){
            case BlockIDList.ANDESITE_SAND -> {
                replacementTexture = ItemTextureList.ANDESITE_SAND;
            }
            case BlockIDList.ANDESITE_GRAVEL -> {
                replacementTexture = ItemTextureList.ANDESITE_GRAVEL;
            }


            case BlockIDList.GRANITE_SAND -> {
                replacementTexture = ItemTextureList.GRANITE_SAND;
            }
            case BlockIDList.GRANITE_GRAVEL -> {
                replacementTexture = ItemTextureList.GRANITE_GRAVEL;
            }


            case BlockIDList.PERIODITE_SAND -> {
                replacementTexture = ItemTextureList.PERIODITE_SAND;
            }
            case BlockIDList.PERIODITE_GRAVEL -> {
                replacementTexture = ItemTextureList.PERIODITE_GRAVEL;
            }


            case BlockIDList.OBSIDIAN_SAND -> {
                replacementTexture = ItemTextureList.OBSIDIAN_SAND;
            }
            case BlockIDList.OBSIDIAN_GRAVEL -> {
                replacementTexture = ItemTextureList.OBSIDIAN_GRAVEL;
            }


            case BlockIDList.BASALT_SAND -> {
                replacementTexture = ItemTextureList.BASALT_SAND;
            }
            case BlockIDList.BASALT_GRAVEL -> {
                replacementTexture = ItemTextureList.BASALT_GRAVEL;
            }


            case BlockIDList.GABBRO_SAND -> {
                replacementTexture = ItemTextureList.GABBRO_SAND;
            }
            case BlockIDList.GABBRO_GRAVEL -> {
                replacementTexture = ItemTextureList.GABBRO_GRAVEL;
            }


            case BlockIDList.CHALK_SAND -> {
                replacementTexture = ItemTextureList.CHALK_SAND;
            }
            case BlockIDList.CHALK_GRAVEL -> {
                replacementTexture = ItemTextureList.CHALK_GRAVEL;
            }


            case BlockIDList.CHERT_SAND -> {
                replacementTexture = ItemTextureList.CHERT_SAND;
            }
            case BlockIDList.CHERT_GRAVEL -> {
                replacementTexture = ItemTextureList.CHERT_GRAVEL;
            }


            case BlockIDList.CLAYSTONE_SAND -> {
                replacementTexture = ItemTextureList.CLAYSTONE_SAND;
            }
            case BlockIDList.CLAYSTONE_GRAVEL -> {
                replacementTexture = ItemTextureList.CLAYSTONE_GRAVEL;
            }

            case BlockIDList.CONGLOMERATE_SAND -> {
                replacementTexture = ItemTextureList.CONGLOMERATE_SAND;
            }
            case BlockIDList.CONGLOMERATE_GRAVEL -> {
                replacementTexture = ItemTextureList.CONGLOMERATE_GRAVEL;
            }


            case BlockIDList.SHALE_SAND -> {
                replacementTexture = ItemTextureList.SHALE_SAND;
            }
            case BlockIDList.SHALE_GRAVEL -> {
                replacementTexture = ItemTextureList.SHALE_GRAVEL;
            }


            case BlockIDList.LIMESTONE_SAND -> {
                replacementTexture = ItemTextureList.LIMESTONE_SAND;
            }
            case BlockIDList.LIMESTONE_GRAVEL -> {
                replacementTexture = ItemTextureList.LIMESTONE_GRAVEL;
            }


            case BlockIDList.SANDSTONE_SAND -> {
                replacementTexture = ItemTextureList.SANDSTONE_SAND;
            }
            case BlockIDList.SANDSTONE_GRAVEL -> {
                replacementTexture = ItemTextureList.SANDSTONE_GRAVEL;
            }


            case BlockIDList.MARBLE_SAND -> {
                replacementTexture = ItemTextureList.MARBLE_SAND;
            }
            case BlockIDList.MARBLE_GRAVEL -> {
                replacementTexture = ItemTextureList.MARBLE_GRAVEL;
            }


            case BlockIDList.SLATE_SAND -> {
                replacementTexture = ItemTextureList.SLATE_SAND;
            }
            case BlockIDList.SLATE_GRAVEL -> {
                replacementTexture = ItemTextureList.SLATE_GRAVEL;
            }


            case BlockIDList.PHYLLITE_SAND -> {
                replacementTexture = ItemTextureList.PHYLLITE_SAND;
            }
            case BlockIDList.PHYLLITE_GRAVEL -> {
                replacementTexture = ItemTextureList.PHYLLITE_GRAVEL;
            }

            case BlockIDList.SERPENTINITE_SAND -> {
                replacementTexture = ItemTextureList.SERPENTINITE_SAND;
            }
            case BlockIDList.SERPENTINITE_GRAVEL -> {
                replacementTexture = ItemTextureList.SERPENTINITE_GRAVEL;
            }
        }

        filledPan.replaceTargetTexture(0, replacementTexture);

        return filledPan;
    }


    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(player.getHeldMetadata() == Item.NULL_ITEM_METADATA)return;
        if(!player.inWater)return;

        if(player.playerAnimation == null){
            player.playerAnimation = new PlayerAnimationPanning(false, true, false, 120);
        }
    }

    @Override
    public void onFinishRightClickAnimation(int x, int y, int z, World world, EntityPlayer player){
        //Pull from loot table and spawn items
        short metadata = player.getHeldMetadata();
        if(metadata == Item.NULL_ITEM_METADATA)return;
        player.inventory.itemStacks[EntityPlayer.selectedInventorySlot].metadata = Item.NULL_ITEM_METADATA;

        LootTable lootTable = this.getLootTableFromMetadata(metadata);
        if(lootTable == null)return;

        ItemStack itemStack = lootTable.getLoot();
        if(itemStack == null)return;

        if(!player.addItemToInventory(itemStack.item.ID, itemStack.metadata, itemStack.count, itemStack.durability, itemStack.decayTime, itemStack.itemState)){
            EntityItem entityItem = new EntityItem(player.x, player.y, player.z, itemStack.item.ID, itemStack.metadata, itemStack.count, itemStack.durability, itemStack.decayTime, itemStack.itemState);
            world.addEntity(entityItem);
        }

    }


    private LootTable getLootTableFromMetadata(short metadata){
        switch (metadata){
            case BlockIDList.ANDESITE_SAND -> {
                return andesiteSandLootTable;
            }
            case BlockIDList.ANDESITE_GRAVEL -> {
                return andesiteGravelLootTable;
            }

            case BlockIDList.GRANITE_SAND -> {
                return graniteSandLootTable;
            }
            case BlockIDList.GRANITE_GRAVEL -> {
                return graniteGravelLootTable;
            }

            case BlockIDList.PERIODITE_SAND -> {
                return perioditeSandLootTable;
            }
            case BlockIDList.PERIODITE_GRAVEL -> {
                return perioditeGravelLootTable;
            }


            case BlockIDList.OBSIDIAN_SAND -> {
                return obsidianSandLootTable;
            }
            case BlockIDList.OBSIDIAN_GRAVEL -> {
                return obsidianGravelLootTable;
            }


            case BlockIDList.BASALT_SAND -> {
                return basaltSandLootTable;
            }
            case BlockIDList.BASALT_GRAVEL -> {
                return basaltGravelLootTable;
            }


            case BlockIDList.GABBRO_SAND -> {
                return gabbroSandLootTable;
            }
            case BlockIDList.GABBRO_GRAVEL -> {
                return gabbroGravelLootTable;
            }


            case BlockIDList.CHALK_SAND -> {
                return chalkSandLootTable;
            }
            case BlockIDList.CHALK_GRAVEL -> {
                return chalkGravelLootTable;
            }


            case BlockIDList.CHERT_SAND -> {
                return chertSandLootTable;
            }
            case BlockIDList.CHERT_GRAVEL -> {
                return chertGravelLootTable;
            }


            case BlockIDList.CLAYSTONE_SAND -> {
                return claystoneSandLootTable;
            }
            case BlockIDList.CLAYSTONE_GRAVEL -> {
                return claystoneGravelLootTable;
            }


            case BlockIDList.CONGLOMERATE_SAND -> {
                return conglomerateSandLootTable;
            }
            case BlockIDList.CONGLOMERATE_GRAVEL -> {
                return conglomerateGravelLootTable;
            }


            case BlockIDList.SHALE_SAND -> {
                return shaleSandLootTable;
            }
            case BlockIDList.SHALE_GRAVEL -> {
                return shaleGravelLootTable;
            }


            case BlockIDList.LIMESTONE_SAND -> {
                return limestoneSandLootTable;
            }
            case BlockIDList.LIMESTONE_GRAVEL -> {
                return limestoneGravelLootTable;
            }


            case BlockIDList.SANDSTONE_SAND -> {
                return sandstoneSandLootTable;
            }
            case BlockIDList.SANDSTONE_GRAVEL -> {
                return sandstoneGravelLootTable;
            }


            case BlockIDList.MARBLE_SAND -> {
                return marbleSandLootTable;
            }
            case BlockIDList.MARBLE_GRAVEL -> {
                return marbleGravelLootTable;
            }


            case BlockIDList.PHYLLITE_SAND -> {
                return phylliteSandLootTable;
            }
            case BlockIDList.PHYLLITE_GRAVEL -> {
                return phylliteGravelLootTable;
            }


            case BlockIDList.SERPENTINITE_SAND -> {
                return serpentiniteSandLootTable;
            }
            case BlockIDList.SERPENTINITE_GRAVEL -> {
                return serpentiniteGravelLootTable;
            }
        }

        return null;
    }



    //Initialize the loot tables
        public static void initializeWoodenPanLootTables() {
            andesiteSandLootTable.addLoot(Item.block.ID, Block.andesiteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            andesiteGravelLootTable.addLoot(Item.block.ID, Block.andesiteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            graniteSandLootTable.addLoot(Item.block.ID, Block.graniteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            graniteGravelLootTable.addLoot(Item.block.ID, Block.graniteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            perioditeSandLootTable.addLoot(Item.block.ID, Block.perioditeItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            perioditeGravelLootTable.addLoot(Item.block.ID, Block.perioditeItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            obsidianSandLootTable.addLoot(Item.block.ID, Block.obsidianItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            obsidianGravelLootTable.addLoot(Item.block.ID, Block.obsidianItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            basaltSandLootTable.addLoot(Item.block.ID, Block.basaltItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            basaltGravelLootTable.addLoot(Item.block.ID, Block.basaltItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            gabbroSandLootTable.addLoot(Item.block.ID, Block.gabbroItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            gabbroGravelLootTable.addLoot(Item.block.ID, Block.gabbroItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            chalkSandLootTable.addLoot(Item.block.ID, Block.chalkItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            chalkGravelLootTable.addLoot(Item.block.ID, Block.chalkItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            chertSandLootTable.addLoot(Item.block.ID, Block.chertItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            chertGravelLootTable.addLoot(Item.block.ID, Block.chertItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            claystoneSandLootTable.addLoot(Item.block.ID, Block.claystoneItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            claystoneGravelLootTable.addLoot(Item.block.ID, Block.claystoneItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            conglomerateSandLootTable.addLoot(Item.block.ID, Block.conglomerateItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            conglomerateGravelLootTable.addLoot(Item.block.ID, Block.conglomerateItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            shaleSandLootTable.addLoot(Item.block.ID, Block.shaleItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            shaleGravelLootTable.addLoot(Item.block.ID, Block.shaleItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            limestoneSandLootTable.addLoot(Item.block.ID, Block.limestoneItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            limestoneGravelLootTable.addLoot(Item.block.ID, Block.limestoneItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            sandstoneSandLootTable.addLoot(Item.block.ID, Block.sandstoneItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            sandstoneGravelLootTable.addLoot(Item.block.ID, Block.sandstoneItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            marbleSandLootTable.addLoot(Item.block.ID, Block.marbleItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            marbleGravelLootTable.addLoot(Item.block.ID, Block.marbleItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            phylliteSandLootTable.addLoot(Item.block.ID, Block.phylliteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            phylliteGravelLootTable.addLoot(Item.block.ID, Block.phylliteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);

            serpentiniteSandLootTable.addLoot(Item.block.ID, Block.serpentiniteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 14);
            serpentiniteGravelLootTable.addLoot(Item.block.ID, Block.serpentiniteItemStone.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0L, null, 0, 24);
    }
}
