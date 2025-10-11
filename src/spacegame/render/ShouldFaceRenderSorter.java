package spacegame.render;

import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.GameSettings;
import spacegame.render.RenderBlocks;

public final class ShouldFaceRenderSorter {



    public boolean shouldFaceRender(short firstBlock, short secondBlock, int face){
        String firstBlockName = Block.list[firstBlock].blockName;
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (firstBlockName) {
            case "AIR" -> false; //DO NOT EVER MAKE THIS TRUE UNDER ANY CIRCUMSTANCE
            case "TORCH", "BERRY_BUSH", "CAMPFIRE", "CAMPFIRE_LIT", "FIRE", "ITEM_STONE", "ITEM_STICK", "TALL_GRASS", "STRAW_CHEST" -> true;
            case "WATER" -> this.shouldFaceRenderWater(secondBlockName, face);
            case "OAK_LOG" -> this.shouldFaceRenderLog(firstBlock,secondBlock, face);
            case "LEAF" -> this.shouldFaceRenderLeaf(firstBlock, secondBlock, face);
            default -> shouldFaceRenderStandard(secondBlock, face);
        };

    }

    private boolean shouldFaceRenderStandard(short secondBlock, int face){
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "TORCH", "WATER", "BERRY_BUSH", "CAMPFIRE", "CAMPFIRE_LIT", "FIRE", "ITEM_STONE", "ITEM_STICK", "TALL_GRASS", "STRAW_CHEST" -> true;
            case "LEAF" -> GameSettings.transparentLeaves;
            case "OAK_LOG" ->
                    (secondBlock != Block.oakLogFullSizeNormal.ID && secondBlock != Block.oakLogFullSizeNorthSouth.ID && secondBlock != Block.oakLogFullSizeEastWest.ID);
            default -> false;
        };
    }

    private boolean shouldFaceRenderLog(short firstBlock, short secondBlock, int face){
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "TORCH", "WATER", "BERRY_BUSH",  "CAMPFIRE", "CAMPFIRE_LIT", "FIRE", "ITEM_STONE", "ITEM_STICK", "TALL_GRASS", "STRAW_CHEST" -> true;
            case "OAK_LOG" ->
                    firstBlock != secondBlock && BlockLog.facingDirectionOfLog(firstBlock) == BlockLog.facingDirectionOfLog(secondBlock);
            case "LEAF" -> GameSettings.transparentLeaves;
            default ->
                    firstBlock != secondBlock;
        };
    }


    private boolean shouldFaceRenderWater(String secondBlockName, int face){
        return switch (secondBlockName) {
            case "AIR" -> true;
            case "WATER" -> false;
            default -> face == RenderBlocks.TOP_FACE;
        };
    }

    private boolean shouldFaceRenderLeaf(short firstBlock, short secondBlock, int face) {
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "TORCH", "WATER", "BERRY_BUSH", "CAMPFIRE", "CAMPFIRE_LIT", "FIRE", "ITEM_STONE", "ITEM_STICK", "TALL_GRASS", "STRAW_CHEST" -> true;
            case "LEAF" -> GameSettings.transparentLeaves;
            default -> firstBlock != secondBlock;
        };
    }

}
