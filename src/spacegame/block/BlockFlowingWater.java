package spacegame.block;

import spacegame.core.Timer;
import spacegame.render.model.ModelLoader;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.FlowingWaterState;
import spacegame.world.blockstate.MultiState;

public final class BlockFlowingWater extends BlockFluid implements ITimeUpdate {
    public BlockFlowingWater(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {

        FlowingWaterState flowingWaterState = (FlowingWaterState) world.getBlockState(x,y,z, MultiState.FLOWING_WATER_STATE);

        if(this.shouldBlockReduceFlow(x,y,z, world)){
            if(world.getBlockID(x, y - 1, z) == Block.fullWater.ID){
                world.addTimeEvent(x, y - 1, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            }

            if(flowingWaterState.waterLevel == FlowingWaterState.FLOW_LEVEL_7){
                world.setBlockAndNotify(x,y,z, Block.air.ID, false);
                return;
            }

            flowingWaterState.waterLevel++;
            world.addTimeEvent(x, y , z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.addTimeEvent(x + 1, y, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.addTimeEvent(x , y - 1, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.addTimeEvent(x, y + 1, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            world.notifyChunk(x,y,z);
            return;
        }

        if(this.ID == Block.fullWater.ID){
            if(!(Block.list[world.getBlockID(x, y + 1, z)] instanceof BlockWater)){
                world.setBlockAndNotify(x,y,z, Block.air.ID, false);
            }
        }

        if(this.canBlockSpreadWater(x, y - 1, z, world, x, y, z)){
            world.setBlockAndNotify(x, y - 1, z, Block.fullWater.ID, false);
            world.addTimeEvent(x, y - 1, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            return;
        }

        if(this.ID == Block.water.ID || this.ID == Block.fullWater.ID){
            if(this.canBlockSpreadWater(x - 1, y, z, world, x, y, z)){
                world.setBlockAndNotify(x - 1, y, z, Block.flowingWater.ID, false);
                world.addBlockState(x - 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_NORTH, FlowingWaterState.FLOW_LEVEL_1, Chunk.getBlockIndexFromCoordinates(x - 1, y, z)));
                world.addTimeEvent(x - 1, y, z, world.ce.save.time +  this.getUpdateTime(x,y,z, world));
            }
            if(this.canBlockSpreadWater(x + 1, y, z, world, x, y, z)){
                world.setBlockAndNotify(x + 1, y, z, Block.flowingWater.ID, false);
                world.addBlockState(x + 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_SOUTH, FlowingWaterState.FLOW_LEVEL_1, Chunk.getBlockIndexFromCoordinates(x + 1,y,z)));
                world.addTimeEvent(x + 1, y, z, world.ce.save.time +  this.getUpdateTime(x,y,z, world));
            }
            if(this.canBlockSpreadWater(x, y, z - 1, world, x, y, z)){
                world.setBlockAndNotify(x, y, z - 1, Block.flowingWater.ID, false);
                world.addBlockState(x,y,z-1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_EAST, FlowingWaterState.FLOW_LEVEL_1, Chunk.getBlockIndexFromCoordinates(x,y,z-1)));
                world.addTimeEvent(x, y, z - 1, world.ce.save.time +  this.getUpdateTime(x,y,z, world));
            }
            if(this.canBlockSpreadWater(x, y, z + 1, world, x, y, z)){
                world.setBlockAndNotify(x, y, z + 1, Block.flowingWater.ID, false);
                world.addBlockState(x,y,z+1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_WEST, FlowingWaterState.FLOW_LEVEL_1, Chunk.getBlockIndexFromCoordinates(x,y,z+1)));
                world.addTimeEvent(x, y, z + 1, world.ce.save.time +  this.getUpdateTime(x,y,z, world));
            }
        }


        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_NORTH){
            if(this.canBlockSpreadWater(x - 1, y, z, world, x, y, z)){
                world.setBlockAndNotify(x - 1, y, z, Block.flowingWater.ID, false);
                world.addBlockState(x - 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_NORTH, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x-1,y,z)));
                world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            } else {
                if(this.canBlockSpreadWater(x, y, z - 1, world, x, y, z)){
                    world.setBlockAndNotify(x, y, z - 1, Block.flowingWater.ID, false);
                    world.addBlockState(x,y,z-1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_EAST, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x,y,z-1)));
                    world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
                if(this.canBlockSpreadWater(x, y, z + 1, world, x, y, z)){
                    world.setBlockAndNotify(x, y, z + 1, Block.flowingWater.ID, false);
                    world.addBlockState(x,y,z+1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_WEST, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x,y,z+1)));
                    world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
            }
        }

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_SOUTH){
            if(this.canBlockSpreadWater(x + 1, y, z, world, x, y, z)) {
                world.setBlockAndNotify(x + 1, y, z, Block.flowingWater.ID, false);
                world.addBlockState(x + 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_SOUTH, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x + 1, y, z)));
                world.addTimeEvent(x + 1, y, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            } else {
                if(this.canBlockSpreadWater(x, y, z - 1, world, x, y, z)){
                    world.setBlockAndNotify(x, y, z - 1, Block.flowingWater.ID, false);
                    world.addBlockState(x, y, z - 1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_EAST, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x, y, z - 1)));
                    world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
                if(this.canBlockSpreadWater(x, y, z + 1, world, x, y, z)){
                    world.setBlockAndNotify(x, y, z + 1, Block.flowingWater.ID, false);
                    world.addBlockState(x, y, z + 1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_WEST, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x, y, z + 1)));
                    world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
            }
        }

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_EAST){
            if(this.canBlockSpreadWater(x, y, z - 1, world, x, y, z)) {
                world.setBlockAndNotify(x, y, z - 1, Block.flowingWater.ID, false);
                world.addBlockState(x, y, z - 1, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_EAST, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x, y, z - 1)));
                world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            } else {
                if(this.canBlockSpreadWater(x - 1, y, z, world, x, y, z)){
                    world.setBlockAndNotify(x - 1, y, z, Block.flowingWater.ID, false);
                    world.addBlockState(x - 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_NORTH, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x-1,y,z)));
                    world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
                if(this.canBlockSpreadWater(x + 1, y, z, world, x, y, z)){
                    world.setBlockAndNotify(x + 1, y, z , Block.flowingWater.ID, false);
                    world.addBlockState(x + 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_SOUTH, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x + 1, y, z)));
                    world.addTimeEvent(x + 1, y, z , world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
            }
        }

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_WEST){
            if(this.canBlockSpreadWater(x, y, z + 1, world, x, y, z)) {
                world.setBlockAndNotify(x, y, z + 1, Block.flowingWater.ID, false);
                world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            } else {
                if(this.canBlockSpreadWater(x - 1, y, z, world, x, y, z)){
                    world.setBlockAndNotify(x - 1, y, z, Block.flowingWater.ID, false);
                    world.addBlockState(x - 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_NORTH, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x-1,y,z)));
                    world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
                if(this.canBlockSpreadWater(x + 1, y, z, world, x, y, z)){
                    world.setBlockAndNotify(x + 1, y, z , Block.flowingWater.ID, false);
                    world.addBlockState(x + 1, y, z, MultiState.FLOWING_WATER_STATE, new FlowingWaterState(FlowingWaterState.FACE_DIRECTION_SOUTH, flowingWaterState.waterLevel + 1, Chunk.getBlockIndexFromCoordinates(x + 1, y, z)));
                    world.addTimeEvent(x + 1, y, z , world.ce.save.time + this.getUpdateTime(x,y,z, world));
                }
            }
        }

    }


    private boolean canBlockSpreadWater(int x, int y, int z, World world, int x0, int y0, int z0){
        return !Block.list[world.getBlockID(x,y,z)].isSolid && this.getFlowLevel(x,y,z,world) > this.getFlowLevel(x0, y0, z0, world);
    }

    public int getFlowLevel(int x, int y, int z, World world){
        FlowingWaterState flowingWaterState = (FlowingWaterState) world.getBlockState(x,y,z, MultiState.FLOWING_WATER_STATE);
        if(flowingWaterState == null)return FlowingWaterState.FLOW_LEVEL_1;
        return flowingWaterState.waterLevel;
    }

    private boolean shouldBlockReduceFlow(int x, int y, int z, World world){
        FlowingWaterState flowingWaterState = (FlowingWaterState) world.getBlockState(x,y,z, MultiState.FLOWING_WATER_STATE);

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_NORTH){
            short southID = world.getBlockID(x + 1, y, z);
            if(flowingWaterState.waterLevel == FlowingWaterState.FLOW_LEVEL_1) {
                if (southID != Block.water.ID && southID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(x,y,z,world) != this.getFlowLevel(x,y,z,world) - 1){
                    return true;
                }
            }
        }

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_SOUTH){
            short northID = world.getBlockID(x - 1, y, z);
            if(flowingWaterState.waterLevel == FlowingWaterState.FLOW_LEVEL_1) {
                if (northID != Block.water.ID && northID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(x,y,z,world) != this.getFlowLevel(x,y,z,world) - 1){
                    return true;
                }
            }
        }

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_EAST){
            short westID = world.getBlockID(x, y, z + 1);
            if(flowingWaterState.waterLevel == FlowingWaterState.FLOW_LEVEL_1) {
                if (westID != Block.water.ID && westID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(x,y,z,world) != this.getFlowLevel(x,y,z,world) - 1){
                    return true;
                }
            }
        }

        if(flowingWaterState.facingDirection == FlowingWaterState.FACE_DIRECTION_WEST){
            short eastID = world.getBlockID(x, y, z - 1);
            if(flowingWaterState.waterLevel == FlowingWaterState.FLOW_LEVEL_1) {
                if (eastID != Block.water.ID && eastID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(x,y,z,world) != this.getFlowLevel(x,y,z,world) - 1){
                    return true;
                }
            }
        }



        return false;
    }

    @Override
    public long getUpdateTime(int x, int y, int z, World world) {
        return Timer.REAL_SECOND / 4;
    }

    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        return "";
    }

    @Override
    public ModelLoader getBlockModel(int x, int y, int z, World world){
        FlowingWaterState flowingWaterState = (FlowingWaterState) world.getBlockState(x,y,z, MultiState.FLOWING_WATER_STATE);
        if(flowingWaterState == null)return this.blockModel;

        switch (flowingWaterState.facingDirection){
            case FlowingWaterState.FACE_DIRECTION_NORTH -> {
                switch (flowingWaterState.waterLevel) {
                    case FlowingWaterState.FLOW_LEVEL_1 -> {
                        return BlockModelList.waterFlowNorth1;
                    }
                    case FlowingWaterState.FLOW_LEVEL_2 -> {
                        return BlockModelList.waterFlowNorth2;
                    }
                    case FlowingWaterState.FLOW_LEVEL_3 -> {
                        return BlockModelList.waterFlowNorth3;
                    }
                    case FlowingWaterState.FLOW_LEVEL_4 -> {
                        return BlockModelList.waterFlowNorth4;
                    }
                    case FlowingWaterState.FLOW_LEVEL_5 -> {
                        return BlockModelList.waterFlowNorth5;
                    }
                    case FlowingWaterState.FLOW_LEVEL_6 -> {
                        return BlockModelList.waterFlowNorth6;
                    }
                    case FlowingWaterState.FLOW_LEVEL_7 -> {
                        return BlockModelList.waterFlowNorth7;
                    }
                }
            }
            case FlowingWaterState.FACE_DIRECTION_SOUTH -> {
                switch (flowingWaterState.waterLevel) {
                    case FlowingWaterState.FLOW_LEVEL_1 -> {
                        return BlockModelList.waterFlowSouth1;
                    }
                    case FlowingWaterState.FLOW_LEVEL_2 -> {
                        return BlockModelList.waterFlowSouth2;
                    }
                    case FlowingWaterState.FLOW_LEVEL_3 -> {
                        return BlockModelList.waterFlowSouth3;
                    }
                    case FlowingWaterState.FLOW_LEVEL_4 -> {
                        return BlockModelList.waterFlowSouth4;
                    }
                    case FlowingWaterState.FLOW_LEVEL_5 -> {
                        return BlockModelList.waterFlowSouth5;
                    }
                    case FlowingWaterState.FLOW_LEVEL_6 -> {
                        return BlockModelList.waterFlowSouth6;
                    }
                    case FlowingWaterState.FLOW_LEVEL_7 -> {
                        return BlockModelList.waterFlowSouth7;
                    }
                }
            }
            case FlowingWaterState.FACE_DIRECTION_EAST -> {
                switch (flowingWaterState.waterLevel) {
                    case FlowingWaterState.FLOW_LEVEL_1 -> {
                        return BlockModelList.waterFlowEast1;
                    }
                    case FlowingWaterState.FLOW_LEVEL_2 -> {
                        return BlockModelList.waterFlowEast2;
                    }
                    case FlowingWaterState.FLOW_LEVEL_3 -> {
                        return BlockModelList.waterFlowEast3;
                    }
                    case FlowingWaterState.FLOW_LEVEL_4 -> {
                        return BlockModelList.waterFlowEast4;
                    }
                    case FlowingWaterState.FLOW_LEVEL_5 -> {
                        return BlockModelList.waterFlowEast5;
                    }
                    case FlowingWaterState.FLOW_LEVEL_6 -> {
                        return BlockModelList.waterFlowEast6;
                    }
                    case FlowingWaterState.FLOW_LEVEL_7 -> {
                        return BlockModelList.waterFlowEast7;
                    }
                }
            }
            case FlowingWaterState.FACE_DIRECTION_WEST -> {
                switch (flowingWaterState.waterLevel) {
                    case FlowingWaterState.FLOW_LEVEL_1 -> {
                        return BlockModelList.waterFlowWest1;
                    }
                    case FlowingWaterState.FLOW_LEVEL_2 -> {
                        return BlockModelList.waterFlowWest2;
                    }
                    case FlowingWaterState.FLOW_LEVEL_3 -> {
                        return BlockModelList.waterFlowWest3;
                    }
                    case FlowingWaterState.FLOW_LEVEL_4 -> {
                        return BlockModelList.waterFlowWest4;
                    }
                    case FlowingWaterState.FLOW_LEVEL_5 -> {
                        return BlockModelList.waterFlowWest5;
                    }
                    case FlowingWaterState.FLOW_LEVEL_6 -> {
                        return BlockModelList.waterFlowWest6;
                    }
                    case FlowingWaterState.FLOW_LEVEL_7 -> {
                        return BlockModelList.waterFlowWest7;
                    }
                }
            }
        }


        return this.blockModel;
    }
}
