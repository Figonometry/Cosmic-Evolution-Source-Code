package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.RenderBlocks;
import spacegame.world.World;

public final class BlockWater extends BlockFluid implements ITimeUpdate {
    public BlockWater(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;

        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem == Item.block.ID && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            if(player.getHeldBlock() == Block.dirt.ID && player.addItemToInventory(Item.mud.ID, Item.NULL_ITEM_METADATA, (byte)8, Item.NULL_ITEM_DURABILITY, 0, null)){
                player.removeItemFromInventory();
                MouseListener.rightClickReleased = false;
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }
        }
    }

    //There are multiple copies of the water texture to allow for texture coordinate movements in the shader under the different water states
    @Override
    public int getBlockTexture(int face){
        switch (face){
            case RenderBlocks.NORTH_FACE, RenderBlocks.SOUTH_FACE, RenderBlocks.EAST_FACE, RenderBlocks.WEST_FACE -> {
                if(this.ID != Block.fullWater.ID){
                    return 63; //Side texture
                } else {
                    return 69; //Side texture
                }
            }
            case RenderBlocks.BOTTOM_FACE -> {
                return 64; //Bottom texture
            }

            //Default top texture
            default -> {
                return this.textureID;
            }
        }
    }


    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {

        if(this.shouldBlockReduceFlow(x,y,z, world)){
            if(world.getBlockID(x, y - 1, z) == Block.fullWater.ID){
                world.addTimeEvent(x, y - 1, z, world.ce.save.time + this.getUpdateTime());
            }

            if(this.ID == Block.waterFlowNorth7Block.ID || this.ID == Block.waterFlowSouth7Block.ID ||
                    this.ID == Block.waterFlowEast7Block.ID || this.ID == Block.waterFlowWest7Block.ID){
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
                return;
            }

            world.setBlockWithNotify(x, y , z, (short) (this.ID + 1), false);
            world.addTimeEvent(x, y , z, world.ce.save.time + this.getUpdateTime());
            world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime());
            world.addTimeEvent(x + 1, y, z, world.ce.save.time + this.getUpdateTime());
            world.addTimeEvent(x , y - 1, z, world.ce.save.time + this.getUpdateTime());
            world.addTimeEvent(x, y + 1, z, world.ce.save.time + this.getUpdateTime());
            world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime());
            world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime());
            return;
        }

        if(this.ID == Block.fullWater.ID){
            if(!(Block.list[world.getBlockID(x, y + 1, z)] instanceof BlockWater)){
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
            }
        }

        if(this.canBlockSpreadWater(x, y - 1, z, world)){
            world.setBlockWithNotify(x, y - 1, z, Block.fullWater.ID, false);
            world.addTimeEvent(x, y - 1, z, world.ce.save.time + this.getUpdateTime());
            return;
        }

        if(this.ID == Block.water.ID || this.ID == Block.fullWater.ID){
            if(this.canBlockSpreadWater(x - 1, y, z, world)){
                if(world.getBlockID(x - 2, y, z) == Block.water.ID || world.getBlockID(x - 1, y, z - 1) == Block.water.ID || world.getBlockID(x - 1, y, z + 1) == Block.water.ID){
                    world.setBlockWithNotify(x - 1, y, z, Block.water.ID, false);
                    world.addTimeEvent(x - 1, y, z, world.ce.save.time +  this.getUpdateTime());
                    return;
                }

                world.setBlockWithNotify(x - 1, y, z, Block.waterFlowNorth1Block.ID, false);
                world.addTimeEvent(x - 1, y, z, world.ce.save.time +  this.getUpdateTime());
            }
            if(this.canBlockSpreadWater(x + 1, y, z, world)){
                if(world.getBlockID(x + 2, y, z) == Block.water.ID || world.getBlockID(x + 1, y, z - 1) == Block.water.ID || world.getBlockID(x + 1, y, z + 1) == Block.water.ID){
                    world.setBlockWithNotify(x + 1, y, z, Block.water.ID, false);
                    world.addTimeEvent(x + 1, y, z, world.ce.save.time +  this.getUpdateTime());
                    return;
                }

                world.setBlockWithNotify(x + 1, y, z, Block.waterFlowSouth1Block.ID, false);
                world.addTimeEvent(x + 1, y, z, world.ce.save.time +  this.getUpdateTime());
            }
            if(this.canBlockSpreadWater(x, y, z - 1, world)){
                if(world.getBlockID(x, y, z - 2) == Block.water.ID || world.getBlockID(x - 1, y, z - 1) == Block.water.ID || world.getBlockID(x + 1, y, z - 1) == Block.water.ID){
                    world.setBlockWithNotify(x, y, z - 1, Block.water.ID, false);
                    world.addTimeEvent(x, y, z - 1, world.ce.save.time +  this.getUpdateTime());
                    return;
                }

                world.setBlockWithNotify(x, y, z - 1, Block.waterFlowEast1Block.ID, false);
                world.addTimeEvent(x, y, z - 1, world.ce.save.time +  this.getUpdateTime());
            }
            if(this.canBlockSpreadWater(x, y, z + 1, world)){
                if(world.getBlockID(x, y, z + 2) == Block.water.ID || world.getBlockID(x - 1, y, z + 1) == Block.water.ID || world.getBlockID(x + 1, y, z + 1) == Block.water.ID){
                    world.setBlockWithNotify(x, y, z + 1, Block.water.ID, false);
                    world.addTimeEvent(x, y, z + 1, world.ce.save.time +  this.getUpdateTime());
                    return;
                }
                world.setBlockWithNotify(x, y, z + 1, Block.waterFlowWest1Block.ID, false);
                world.addTimeEvent(x, y, z + 1, world.ce.save.time +  this.getUpdateTime());
            }
        }

        if(this.ID >= Block.waterFlowNorth1Block.ID && this.ID < Block.waterFlowNorth7Block.ID){
            if(this.canBlockSpreadWater(x - 1, y, z, world)){
                world.setBlockWithNotify(x - 1, y, z, (short) (this.ID + 1), false);
                world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime());
            } else {
                if(this.canBlockSpreadWater(x, y, z - 1, world)){
                    world.setBlockWithNotify(x, y, z - 1, (short) (this.ID + 15), false);
                    world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime());
                }
                if(this.canBlockSpreadWater(x, y, z + 1, world)){
                    world.setBlockWithNotify(x, y, z + 1, (short) (this.ID + 22), false);
                    world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime());
                }
            }
        }

        if(this.ID >= Block.waterFlowSouth1Block.ID && this.ID < Block.waterFlowSouth7Block.ID){
            if(this.canBlockSpreadWater(x + 1, y, z, world)) {
                world.setBlockWithNotify(x + 1, y, z, (short) (this.ID + 1), false);
                world.addTimeEvent(x + 1, y, z, world.ce.save.time + this.getUpdateTime());
            } else {
                if(this.canBlockSpreadWater(x, y, z - 1, world)){
                    world.setBlockWithNotify(x, y, z - 1, (short) (this.ID + 8), false);
                    world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime());
                }
                if(this.canBlockSpreadWater(x, y, z + 1, world)){
                    world.setBlockWithNotify(x, y, z + 1, (short) (this.ID + 15), false);
                    world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime());
                }
            }
        }

        if(this.ID >= Block.waterFlowEast1Block.ID && this.ID < Block.waterFlowEast7Block.ID){
            if(this.canBlockSpreadWater(x, y, z - 1, world)) {
                world.setBlockWithNotify(x, y, z - 1, (short) (this.ID + 1), false);
                world.addTimeEvent(x, y, z - 1, world.ce.save.time + this.getUpdateTime());
            } else {
                if(this.canBlockSpreadWater(x - 1, y, z, world)){
                    world.setBlockWithNotify(x - 1, y, z, (short) (this.ID - 13), false);
                    world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime());
                }
                if(this.canBlockSpreadWater(x + 1, y, z, world)){
                    world.setBlockWithNotify(x + 1, y, z , (short) (this.ID - 6), false);
                    world.addTimeEvent(x + 1, y, z , world.ce.save.time + this.getUpdateTime());
                }
            }
        }

        if(this.ID >= Block.waterFlowWest1Block.ID && this.ID < Block.waterFlowWest7Block.ID){
            if(this.canBlockSpreadWater(x, y, z + 1, world)) {
                world.setBlockWithNotify(x, y, z + 1, (short) (this.ID + 1), false);
                world.addTimeEvent(x, y, z + 1, world.ce.save.time + this.getUpdateTime());
            } else {
                if(this.canBlockSpreadWater(x - 1, y, z, world)){
                    world.setBlockWithNotify(x - 1, y, z, (short) (this.ID - 13), false);
                    world.addTimeEvent(x - 1, y, z, world.ce.save.time + this.getUpdateTime());
                }
                if(this.canBlockSpreadWater(x + 1, y, z, world)){
                    world.setBlockWithNotify(x + 1, y, z , (short) (this.ID - 6), false);
                    world.addTimeEvent(x + 1, y, z , world.ce.save.time + this.getUpdateTime());
                }
            }
        }

    }


    private boolean canBlockSpreadWater(int x, int y, int z, World world){
        return !Block.list[world.getBlockID(x,y,z)].isSolid && this.getFlowLevel(world.getBlockID(x,y,z) ) > this.getFlowLevel(this.ID);
    }

    public int getFlowLevel(short ID){
        switch (ID){
            case 8, 180 -> {
                return 0;
            }

            case 152, 159, 166, 173 ->{
                return 1;
            }
            case 153, 160, 167, 174 ->{
                return 2;
            }
            case 154, 161, 168, 175 ->{
                return 3;
            }
            case 155, 162, 169, 176 ->{
                return 4;
            }
            case 156, 163, 170, 177 ->{
                return 5;
            }
            case 157, 164, 171, 178 ->{
                return 6;
            }
            case 158, 165, 172, 179 ->{
                return 7;
            }
            default -> {
                return 8;
            }
        }
    }

    private boolean shouldBlockReduceFlow(int x, int y, int z, World world){
        short checkingID = world.getBlockID(x,y,z);

        if(checkingID >= Block.waterFlowNorth1Block.ID && checkingID <= Block.waterFlowNorth7Block.ID){
            short southID = world.getBlockID(x + 1, y, z);
            if(checkingID == Block.waterFlowNorth1Block.ID) {
                if (southID != Block.water.ID && southID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(southID) != this.getFlowLevel(checkingID) - 1){
                    return true;
                }
            }
        }

        if(checkingID >= Block.waterFlowSouth1Block.ID && checkingID <= Block.waterFlowSouth7Block.ID){
            short northID = world.getBlockID(x - 1, y, z);
            if(checkingID == Block.waterFlowSouth1Block.ID) {
                if (northID != Block.water.ID && northID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(northID) != this.getFlowLevel(checkingID) - 1){
                    return true;
                }
            }
        }

        if(checkingID >= Block.waterFlowEast1Block.ID && checkingID <= Block.waterFlowEast7Block.ID){
            short westID = world.getBlockID(x, y, z + 1);
            if(checkingID == Block.waterFlowEast1Block.ID) {
                if (westID != Block.water.ID && westID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(westID) != this.getFlowLevel(checkingID) - 1){
                    return true;
                }
            }
        }

        if(checkingID >= Block.waterFlowWest1Block.ID && checkingID <= Block.waterFlowWest7Block.ID){
            short eastID = world.getBlockID(x, y, z - 1);
            if(checkingID == Block.waterFlowWest1Block.ID) {
                if (eastID != Block.water.ID && eastID != Block.fullWater.ID) {
                    return true;
                }
            } else {
                if(this.getFlowLevel(eastID) != this.getFlowLevel(checkingID) - 1){
                    return true;
                }
            }
        }



        return false;
    }

    @Override
    public long getUpdateTime() {
        return 15;
    }

    @Override
    public String getDisplayStringText() {
        return null;
    }
}
