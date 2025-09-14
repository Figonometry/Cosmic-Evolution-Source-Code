package spacegame.render;

import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.render.RenderBlocks;

public final class ShouldFaceRenderSorter {



    public boolean shouldFaceRender(short firstBlock, short secondBlock, int face){
        String firstBlockName = Block.list[firstBlock].blockName;
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (firstBlockName) {
            case "AIR" -> false; //DO NOT EVER MAKE THIS TRUE UNDER ANY CIRCUMSTANCE
            case "TORCH", "BERRY_BUSH", "CAMPFIRE", "CAMPFIRE_LIT", "FIRE" -> true;
            case "WATER" -> shouldFaceRenderWater(secondBlockName, face);
            case "OAK_LOG" -> shouldFaceRenderLog(firstBlock,secondBlock, face);
            default -> shouldFaceRenderStandard(secondBlock, face);
        };

    }

    private boolean shouldFaceRenderStandard(short secondBlock, int face){
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "TORCH", "WATER", "BERRY_BUSH", "CAMPFIRE", "CAMPFIRE_LIT", "FIRE" -> true;
            case "OAK_LOG" ->
                    (secondBlock != Block.oakLogFullSizeNormal.ID && secondBlock != Block.oakLogFullSizeNorthSouth.ID && secondBlock != Block.oakLogFullSizeEastWest.ID);
            default -> false;
        };
    }

    private boolean shouldFaceRenderLog(short firstBlock, short secondBlock, int face){
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "TORCH", "WATER", "LEAF", "BERRY_BUSH",  "CAMPFIRE", "CAMPFIRE_LIT", "FIRE" -> true;
            case "OAK_LOG" ->
                    firstBlock != secondBlock && BlockLog.facingDirectionOfLog(firstBlock) == BlockLog.facingDirectionOfLog(secondBlock);
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

}
