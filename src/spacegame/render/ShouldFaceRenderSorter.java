package spacegame.render;

import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;

public final class ShouldFaceRenderSorter {



    public boolean shouldFaceRender(short firstBlock, short secondBlock, int face, int x1, int y1, int z1, int x2, int y2, int z2){
        if(Block.list[firstBlock].alwaysRenderFace){
            return true;
        }
        String firstBlockName = Block.list[firstBlock].blockName;
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (firstBlockName) {
            case "AIR" -> false; //DO NOT EVER MAKE THIS TRUE UNDER ANY CIRCUMSTANCE
            case "WATER" -> this.shouldFaceRenderWater(secondBlockName, face);
            case "WATER_FULL" -> this.shouldFaceRenderWaterFull(secondBlockName, face);
            case "FLOWING_WATER" -> this.shouldFaceRenderFlowingWater(secondBlockName, face);
            case "OAK_LOG" -> this.shouldFaceRenderLog(firstBlock,secondBlock, face, x1, y1, z1,x2,y2,z2);
            case "LEAF" -> this.shouldFaceRenderLeaf(firstBlock, secondBlock, face);
            default -> shouldFaceRenderStandard(firstBlock, secondBlock, face, x1, y1, z1, x2, y2, z2);
        };

    }

    private boolean shouldFaceRenderStandard(short firstBlock, short secondBlock, int face, int x1, int y1, int z1, int x2, int y2, int z2){
        if(Block.list[secondBlock].alwaysRenderFace){
            return true;
        }
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "LEAF" -> GameSettings.transparentLeaves;
            case "AIR", "WATER", "WATER_FULL", "FLOWING_WATER", "DOOR_EAST_CLOSED_HINGE_LEFT", "DOOR_EAST_CLOSED_HINGE_RIGHT", "DOOR_EAST_OPEN_HINGE_LEFT", "DOOR_EAST_OPEN_HINGE_RIGHT",
                    "DOOR_NORTH_CLOSED_HINGE_LEFT", "DOOR_NORTH_CLOSED_HINGE_RIGHT", "DOOR_NORTH_OPEN_HINGE_LEFT", "DOOR_NORTH_OPEN_HINGE_RIGHT",
                    "DOOR_SOUTH_CLOSED_HINGE_LEFT", "DOOR_SOUTH_CLOSED_HINGE_RIGHT", "DOOR_SOUTH_OPEN_HINGE_LEFT", "DOOR_SOUTH_OPEN_HINGE_RIGHT",
                    "DOOR_WEST_CLOSED_HINGE_LEFT", "DOOR_WEST_CLOSED_HINGE_RIGHT", "DOOR_WEST_OPEN_HINGE_LEFT", "DOOR_WEST_OPEN_HINGE_RIGHT", "TILLED_SOIL" -> true;
            case "OAK_LOG" -> BlockLog.sizeOfLog(x2, y2, z2, CosmicEvolution.instance.save.activeWorld) != 16;
            case "SNOW_LAYER" -> Block.list[firstBlock].ID != Block.snowLayer.ID;
            default -> false;
        };
    }

    private boolean shouldFaceRenderLog(short firstBlock, short secondBlock, int face, int x1, int y1, int z1, int x2, int y2, int z2){
        if(Block.list[secondBlock].alwaysRenderFace){
            return true;
        }
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "OAK_LOG" ->
                   BlockLog.facingDirectionOfLog(x1,y1,z1, CosmicEvolution.instance.save.activeWorld) == BlockLog.facingDirectionOfLog(x2,y2,z2, CosmicEvolution.instance.save.activeWorld);
            case "LEAF" -> GameSettings.transparentLeaves;
            case "AIR", "WATER", "WATER_FULL", "DOOR_EAST_CLOSED_HINGE_LEFT", "DOOR_EAST_CLOSED_HINGE_RIGHT", "DOOR_EAST_OPEN_HINGE_LEFT", "DOOR_EAST_OPEN_HINGE_RIGHT",
                    "DOOR_NORTH_CLOSED_HINGE_LEFT", "DOOR_NORTH_CLOSED_HINGE_RIGHT", "DOOR_NORTH_OPEN_HINGE_LEFT", "DOOR_NORTH_OPEN_HINGE_RIGHT",
                    "DOOR_SOUTH_CLOSED_HINGE_LEFT", "DOOR_SOUTH_CLOSED_HINGE_RIGHT", "DOOR_SOUTH_OPEN_HINGE_LEFT", "DOOR_SOUTH_OPEN_HINGE_RIGHT",
                    "DOOR_WEST_CLOSED_HINGE_LEFT", "DOOR_WEST_CLOSED_HINGE_RIGHT", "DOOR_WEST_OPEN_HINGE_LEFT", "DOOR_WEST_OPEN_HINGE_RIGHT", "TILLED_SOIL" -> true;
            default ->
                    firstBlock != secondBlock;
        };
    }


    private boolean shouldFaceRenderWater(String secondBlockName, int face){
        return switch (secondBlockName) {
            case "AIR", "TALL_GRASS", "ITEM_STONE", "ITEM_STICK", "LEAF", "LOG_PILE", "BRICK_PILE", "TORCH", "ITEM_BLOCK", "TILLED_SOIL" -> true;
            case "WATER", "ICE" -> false;
            default -> face == RenderBlocks.TOP_FACE;
        };
    }

    private boolean shouldFaceRenderFlowingWater(String secondBlockName, int face){
        return switch (secondBlockName) {
            case "AIR", "TALL_GRASS", "ITEM_STONE", "ITEM_STICK", "LEAF", "LOG_PILE", "BRICK_PILE", "TORCH", "ITEM_BLOCK", "WATER", "FLOWING_WATER, \"TILLED_SOIL\"" -> true;
            case "" -> false;
            default -> face == RenderBlocks.TOP_FACE;
        };
    }

    private boolean shouldFaceRenderWaterFull(String secondBlockName, int face){
        return switch (secondBlockName) {
            case "AIR", "TALL_GRASS", "ITEM_STONE", "ITEM_STICK", "LEAF", "LOG_PILE", "BRICK_PILE", "TORCH", "ITEM_BLOCK", "WATER", "TILLED_SOIL" -> true;
            case "FLOWING_WATER" -> {
                if (face == RenderBlocks.TOP_FACE) {
                   yield false;
                } else {
                   yield true;
                }
            }
            case "" -> false;
            default -> face == RenderBlocks.TOP_FACE;
        };
    }

    private boolean shouldFaceRenderLeaf(short firstBlock, short secondBlock, int face) {
        if(Block.list[secondBlock].alwaysRenderFace){
            return true;
        }
        String secondBlockName = Block.list[secondBlock].blockName;
        return switch (secondBlockName) {
            case "AIR", "WATER", "DOOR_EAST_CLOSED_HINGE_LEFT", "DOOR_EAST_CLOSED_HINGE_RIGHT", "DOOR_EAST_OPEN_HINGE_LEFT", "DOOR_EAST_OPEN_HINGE_RIGHT",
                    "DOOR_NORTH_CLOSED_HINGE_LEFT", "DOOR_NORTH_CLOSED_HINGE_RIGHT", "DOOR_NORTH_OPEN_HINGE_LEFT", "DOOR_NORTH_OPEN_HINGE_RIGHT",
                    "DOOR_SOUTH_CLOSED_HINGE_LEFT", "DOOR_SOUTH_CLOSED_HINGE_RIGHT", "DOOR_SOUTH_OPEN_HINGE_LEFT", "DOOR_SOUTH_OPEN_HINGE_RIGHT",
                    "DOOR_WEST_CLOSED_HINGE_LEFT", "DOOR_WEST_CLOSED_HINGE_RIGHT", "DOOR_WEST_OPEN_HINGE_LEFT", "DOOR_WEST_OPEN_HINGE_RIGHT", "TILLED_SOIL" -> true;
            case "LEAF" -> GameSettings.transparentLeaves;
            default -> firstBlock != secondBlock;
        };
    }


}
