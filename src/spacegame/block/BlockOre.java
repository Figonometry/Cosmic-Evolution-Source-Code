package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.ItemOreChunks;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.OreState;
import spacegame.world.worldtypes.World;

public final class BlockOre extends Block {
    public BlockOre(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public float getHardness(int x, int y, int z, World world){
        OreState oreState = (OreState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.ORE_STATE);
        if(oreState == null)return this.hardness;

        switch (oreState.getOre()){
            case OreState.ORE_NATIVE_COPPER -> {
                return 0.2f; //Default for stone
            }
            default -> {
                return this.hardness;
            }
        }
    }

    @Override
    public int getBlockTexture(int x, int y, int z, int face) {
        OreState oreState = (OreState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.ORE_STATE);
        if(oreState == null)return this.textureID;

        switch (oreState.getOre()){
            case OreState.ORE_NATIVE_COPPER -> {
                return this.getNativeCopperOreTextures(oreState.getParentStoneBlockID(), face);
            }
            default -> {
                return BlockTextureList.WATER_TOP_TEXTURE;
            }
        }

    }

    @Override
    public String getDisplayName(int x, int y, int z){
        OreState oreState = (OreState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.ORE_STATE);
        if(oreState == null)return this.displayName;

        String ore = this.localizeOre(oreState.getOre());
        String richness = this.localizeRichness(oreState.getOreRichness());
        String stone = this.localizeStoneType(oreState.getParentStoneBlockID());

        return richness + " " + ore + " ore in " + stone;
    }

    public String getDisplayTextForOre(int x, int y, int z){
        OreState oreState = (OreState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.ORE_STATE);
        if(oreState == null)return "Error";

        return "Break with a " + this.localizeRequiredTool(oreState.getOre()) + " to obtain ore";
    }


    private String localizeRequiredTool(int ore){
        switch (ore){
            case OreState.ORE_NATIVE_COPPER -> {
                return "Copper Pickaxe";
            }
            default -> {
                return "ERROR";
            }

        }
    }

    private String localizeRichness(int richness){
        switch (richness){
            case OreState.RICHNESS_POOR -> {
                return "Poor";
            }
            case OreState.RICHNESS_MEDIUM -> {
                return "Medium";
            }
            case OreState.RICHNESS_HIGH -> {
                return "High";
            }
            case OreState.RICHNESS_BOUNTIFUL -> {
                return "Bountiful";
            }
            default -> {
                return "NO RICHNESS";
            }
        }
    }

    private String localizeStoneType(short stoneBlockID){
        switch (stoneBlockID){
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

    private String localizeOre(int ore){
        switch (ore){
            case OreState.ORE_NATIVE_COPPER -> {
                return "Native Copper";
            }
            default -> {
                return "NO ORE";
            }
        }
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        OreState oreState = (OreState)world.getBlockState(x,y,z, MultiState.ORE_STATE);
        if(oreState == null)return;

        Item outputItem = ItemOreChunks.getOreChunkFromOreState(oreState);
        if(outputItem == null)return;

        EntityItem entityItem;
        int amount = CosmicEvolution.globalRand.nextInt(2, 4);

        for(int i = 0; i < amount; i++){
            entityItem = new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                    y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                    z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),
                    outputItem.ID, oreState.getParentStoneBlockID(), (byte)1, Item.NULL_ITEM_DURABILITY, 0L, null);
            world.addEntity(entityItem);
        }

        super.onLeftClick(x,y,z, world, player);
    }

    private int getNativeCopperOreTextures(short stoneBlockID, int face){
        switch (stoneBlockID){
            case BlockIDList.ANDESITE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_ANDESITE;
            }
            case BlockIDList.GRANITE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_GRANITE;
            }
            case BlockIDList.PERIODITE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_PERIODITE;
            }
            case BlockIDList.OBSIDIAN_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_OBSIDIAN;
            }
            case BlockIDList.BASALT_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_BASALT;
            }
            case BlockIDList.GABBRO_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_GABBRO;
            }
            case BlockIDList.CHALK_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_CHALK;
            }
            case BlockIDList.CHERT_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_CHERT;
            }
            case BlockIDList.CLAYSTONE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_CLAYSTONE;
            }
            case BlockIDList.CONGLOMERATE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_CONGLOMERATE;
            }
            case BlockIDList.SHALE_STONE -> {
                switch (face){
                    case Block.FACE_UP -> {
                        return BlockTextureList.NATIVE_COPPER_IN_SHALE_TOP;
                    }
                    case Block.FACE_DOWN -> {
                        return BlockTextureList.NATIVE_COPPER_IN_SHALE_BOTTOM;
                    }
                    default -> {
                        return BlockTextureList.NATIVE_COPPER_IN_SHALE_SIDE;
                    }
                }
            }
            case BlockIDList.LIMESTONE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_LIMESTONE;
            }
            case BlockIDList.SANDSTONE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_SANDSTONE;
            }
            case BlockIDList.MARBLE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_MARBLE;
            }
            case BlockIDList.SLATE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_SLATE;
            }
            case BlockIDList.PHYLLITE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_PHYLLITE;
            }
            case BlockIDList.SERPENTINITE_STONE -> {
                return BlockTextureList.NATIVE_COPPER_IN_SERPENTINITE;
            }
            default -> {
                return BlockTextureList.WATER_TOP_TEXTURE;
            }
        }
    }
}
