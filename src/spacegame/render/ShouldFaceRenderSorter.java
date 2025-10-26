package spacegame.render;

import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.GameSettings;

public final class ShouldFaceRenderSorter {



    public boolean shouldFaceRender(short firstBlock, short secondBlock, int face){
        if(Block.list[firstBlock].alwaysRenderFace){
            return true;
        }
        String firstBlockName = Block.list[firstBlock].blockName;
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (firstBlockName) {
            case "AIR" -> false; //DO NOT EVER MAKE THIS TRUE UNDER ANY CIRCUMSTANCE
            case "WATER" -> this.shouldFaceRenderWater(secondBlockName, face);
            case "OAK_LOG" -> this.shouldFaceRenderLog(firstBlock,secondBlock, face);
            case "LEAF" -> this.shouldFaceRenderLeaf(firstBlock, secondBlock, face);
            default -> shouldFaceRenderStandard(secondBlock, face);
        };

    }

    private boolean shouldFaceRenderStandard(short secondBlock, int face){
        if(Block.list[secondBlock].alwaysRenderFace){
            return true;
        }
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "LEAF" -> GameSettings.transparentLeaves;
            case "AIR", "WATER" -> true;
            case "OAK_LOG" ->
                    (secondBlock != Block.oakLogFullSizeNormal.ID && secondBlock != Block.oakLogFullSizeNorthSouth.ID && secondBlock != Block.oakLogFullSizeEastWest.ID);
            default -> false;
        };
    }

    private boolean shouldFaceRenderLog(short firstBlock, short secondBlock, int face){
        if(Block.list[secondBlock].alwaysRenderFace){
            return true;
        }
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "OAK_LOG" ->
                    firstBlock != secondBlock && BlockLog.facingDirectionOfLog(firstBlock) == BlockLog.facingDirectionOfLog(secondBlock);
            case "LEAF" -> GameSettings.transparentLeaves;
            case "AIR", "WATER" -> true;
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
        if(Block.list[secondBlock].alwaysRenderFace){
            return true;
        }
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "WATER" -> true;
            case "LEAF" -> GameSettings.transparentLeaves;
            default -> firstBlock != secondBlock;
        };
    }

}
