package spacegame.block;

import spacegame.item.ItemIDList;
import spacegame.item.ItemOreChunks;
import spacegame.render.texturelists.BlockTextureList;

public final class BlockStone extends Block {
    public BlockStone(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }
    public int getBlockTexture(int x, int y, int z, int face) {
        if(this.ID == BlockIDList.SHALE_STONE) {
            if (face == Block.FACE_UP ) {
                return BlockTextureList.SHALE_STONE_TOP_TEXTURE;
            }
            if(face == Block.FACE_DOWN){
                return BlockTextureList.SHALE_STONE_BOTTOM_TEXTURE;
            }
        }

        return this.textureID;
    }


    public static short getItemStoneBlockIDFromStoneBlockID(short stoneBlockID){

        switch (stoneBlockID){
            case BlockIDList.ANDESITE_STONE -> {
                return BlockIDList.ANDESITE_ITEM_STONE;
            }
            case BlockIDList.GRANITE_STONE -> {
                return BlockIDList.GRANITE_ITEM_STONE;
            }
            case BlockIDList.PERIODITE_STONE -> {
                return BlockIDList.PERIODITE_ITEM_STONE;
            }
            case BlockIDList.OBSIDIAN_STONE -> {
                return BlockIDList.OBSIDIAN_ITEM_STONE;
            }
            case BlockIDList.BASALT_STONE -> {
                return BlockIDList.BASALT_ITEM_STONE;
            }
            case BlockIDList.GABBRO_STONE -> {
                return BlockIDList.GABBRO_ITEM_STONE;
            }
            case BlockIDList.CHALK_STONE -> {
                return BlockIDList.CHALK_ITEM_STONE;
            }
            case BlockIDList.CHERT_STONE -> {
                return BlockIDList.CHERT_ITEM_STONE;
            }
            case BlockIDList.CLAYSTONE_STONE -> {
                return BlockIDList.CLAYSTONE_ITEM_STONE;
            }
            case BlockIDList.CONGLOMERATE_STONE -> {
                return BlockIDList.CONGLOMERATE_ITEM_STONE;
            }
            case BlockIDList.SHALE_STONE -> {
                return BlockIDList.SHALE_ITEM_STONE;
            }
            case BlockIDList.LIMESTONE_STONE -> {
                return BlockIDList.LIMESTONE_ITEM_STONE;
            }
            case BlockIDList.SANDSTONE_STONE -> {
                return BlockIDList.SANDSTONE_ITEM_STONE;
            }
            case BlockIDList.MARBLE_STONE -> {
                return BlockIDList.MARBLE_ITEM_STONE;
            }
            case BlockIDList.SLATE_STONE -> {
                return BlockIDList.SLATE_ITEM_STONE;
            }
            case BlockIDList.PHYLLITE_STONE -> {
                return BlockIDList.PHYLLITE_ITEM_STONE;
            }
            case BlockIDList.SERPENTINITE_STONE -> {
                return BlockIDList.SERPENTINITE_ITEM_STONE;
            }
            default -> {
                return BlockIDList.ANDESITE_ITEM_STONE;
            }
        }


    }

    public static byte getItemStoneCountFromOreChunks(ItemOreChunks oreChunks){
        switch (oreChunks.ID){
            case ItemIDList.POOR_NATIVE_COPPER_CHUNKS -> {
                return 1;
            }
            case ItemIDList.MEDIUM_NATIVE_COPPER_CHUNKS, ItemIDList.RICH_NATIVE_COPPER_CHUNKS -> {
                return 2;
            }
            case ItemIDList.BOUNTIFUL_NATIVE_COPPER_CHUNKS -> {
                return 5;
            }
            default -> {
                return 0;
            }
        }
    }
}
