package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.AxisAlignedBB;
import spacegame.world.World;
import spacegame.world.blockstate.DoorState;
import spacegame.world.blockstate.DoorTransition;
import spacegame.world.blockstate.MultiState;

public final class BlockDoor extends Block {

    //All door models by default face north and are centered
    public BlockDoor(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer thePlayer){
        //If it's the facing block we need to get the information from the above block
        if(this.ID == Block.doorPrimitiveLower.ID){
            short block = world.getBlockID(x, y + 1, z);

            world.addEntity(new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[block].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[block].droppedItemID].durability, 0, null));

            world.setBlockWithNotify(x,y,z, Block.air.ID, true);
            world.setBlockWithNotify(x, y + 1, z, Block.air.ID, true);
        } else {
            super.onLeftClick(x,y,z, world, thePlayer);
            world.setBlockWithNotify(x,y - 1, z, Block.air.ID, true);
        }
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){

        if(this.ID == Block.doorPrimitiveUpper.ID) {
            Block.list[world.getBlockID(x, y - 1, z)].handleSpecialRightClickFunctions(x, y - 1, z, world, player);
            return;
        }


        if(world.getBlockState(x,y,z, MultiState.DOOR_TRANSITION_STATE) != null)return;
        if(!MouseListener.rightClickReleased)return;

        if(world.getBlockID(x, y + 1, z) == Block.doorPrimitiveUpper.ID){
            if(CosmicEvolution.globalRand.nextInt(100) < 10){
                this.onLeftClick(x,y,z, world, player);
                CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.crunch, false, 1), 0.5f);
                world.generateParticlesOnBlockBreak(Block.doorPrimitiveUpper.ID, x,y,z);
                return;
            }
        }

        DoorState doorState = (DoorState) world.getBlockState(x,y,z, MultiState.DOOR_STATE);
        if(doorState == null)return;
        world.addBlockState(x,y,z, MultiState.DOOR_TRANSITION_STATE,
                new DoorTransition(x,y,z, CosmicEvolution.instance.save.time, doorState.isOpen, !doorState.isOpen, doorState.hingeLeft, doorState.hingeRight));

        doorState.isOpen = !doorState.isOpen;

        short northID = world.getBlockID(x - 1, y,z);
        short southID = world.getBlockID(x + 1, y,z);
        short eastID = world.getBlockID(x,y, z - 1);
        short westID = world.getBlockID(x,y, z + 1);

        if(Block.list[northID] instanceof BlockDoor){
            DoorState northState = (DoorState) world.getBlockState(x - 1, y,z, MultiState.DOOR_STATE);
            world.addBlockState(x,y,z, MultiState.DOOR_TRANSITION_STATE,
                    new DoorTransition(x,y,z, CosmicEvolution.instance.save.time, northState.isOpen, !northState.isOpen, northState.hingeLeft, northState.hingeRight));

            northState.isOpen = !northState.isOpen;
        }

        if(Block.list[southID] instanceof BlockDoor){
            DoorState southState = (DoorState) world.getBlockState(x + 1, y,z, MultiState.DOOR_STATE);
            world.addBlockState(x,y,z, MultiState.DOOR_TRANSITION_STATE,
                    new DoorTransition(x,y,z, CosmicEvolution.instance.save.time, southState.isOpen, !southState.isOpen, southState.hingeLeft, southState.hingeRight));

            southState.isOpen = !southState.isOpen;
        }

        if(Block.list[eastID] instanceof BlockDoor){
            DoorState eastState = (DoorState) world.getBlockState(x, y,z - 1, MultiState.DOOR_STATE);
            world.addBlockState(x,y,z, MultiState.DOOR_TRANSITION_STATE,
                    new DoorTransition(x,y,z, CosmicEvolution.instance.save.time, eastState.isOpen, !eastState.isOpen, eastState.hingeLeft, eastState.hingeRight));

            eastState.isOpen = !eastState.isOpen;
        }

        if(Block.list[westID] instanceof BlockDoor){
            DoorState westState = (DoorState) world.getBlockState(x, y,z + 1, MultiState.DOOR_STATE);
            world.addBlockState(x,y,z, MultiState.DOOR_TRANSITION_STATE,
                    new DoorTransition(x,y,z, CosmicEvolution.instance.save.time, westState.isOpen, !westState.isOpen, westState.hingeLeft, westState.hingeRight));

            westState.isOpen = !westState.isOpen;
        }

        world.notifyChunk(x,y,z);


        MouseListener.rightClickReleased = false;
        if(!doorState.isOpen){//Inverted due to the state change
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.doorOpen, false, 1), 0.75f);
        } else {
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.doorOpen, false, 1), 1f);
        }
    }



    @Override
    public void adjustBoundingBox(int x, int y, int z, AxisAlignedBB axisAlignedBB) {
        short blockID = CosmicEvolution.instance.save.activeWorld.getBlockID(x, y, z);

        DoorState doorState = (DoorState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.DOOR_STATE);
        if (blockID == Block.doorPrimitiveUpper.ID) {
            doorState = (DoorState) CosmicEvolution.instance.save.activeWorld.getBlockState(x, y -1, z, MultiState.DOOR_STATE);
        }

        if (!doorState.isOpen) {
            switch (doorState.facingDirection) {
                case DoorState.FACE_DIRECTION_NORTH -> {
                    axisAlignedBB.minX = x + BlockAxisAlignedBBList.northDoor.minX;
                    axisAlignedBB.minY = y + BlockAxisAlignedBBList.northDoor.minY;
                    axisAlignedBB.minZ = z + BlockAxisAlignedBBList.northDoor.minZ;
                    axisAlignedBB.maxX = x + BlockAxisAlignedBBList.northDoor.maxX;
                    axisAlignedBB.maxY = y + BlockAxisAlignedBBList.northDoor.maxY;
                    axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.northDoor.maxZ;
                }

                case  DoorState.FACE_DIRECTION_SOUTH -> {
                    axisAlignedBB.minX = x + BlockAxisAlignedBBList.southDoor.minX;
                    axisAlignedBB.minY = y + BlockAxisAlignedBBList.southDoor.minY;
                    axisAlignedBB.minZ = z + BlockAxisAlignedBBList.southDoor.minZ;
                    axisAlignedBB.maxX = x + BlockAxisAlignedBBList.southDoor.maxX;
                    axisAlignedBB.maxY = y + BlockAxisAlignedBBList.southDoor.maxY;
                    axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.southDoor.maxZ;
                }
                case  DoorState.FACE_DIRECTION_EAST -> {
                    axisAlignedBB.minX = x + BlockAxisAlignedBBList.eastDoor.minX;
                    axisAlignedBB.minY = y + BlockAxisAlignedBBList.eastDoor.minY;
                    axisAlignedBB.minZ = z + BlockAxisAlignedBBList.eastDoor.minZ;
                    axisAlignedBB.maxX = x + BlockAxisAlignedBBList.eastDoor.maxX;
                    axisAlignedBB.maxY = y + BlockAxisAlignedBBList.eastDoor.maxY;
                    axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.eastDoor.maxZ;
                }
                case  DoorState.FACE_DIRECTION_WEST -> {
                    axisAlignedBB.minX = x + BlockAxisAlignedBBList.westDoor.minX;
                    axisAlignedBB.minY = y + BlockAxisAlignedBBList.westDoor.minY;
                    axisAlignedBB.minZ = z + BlockAxisAlignedBBList.westDoor.minZ;
                    axisAlignedBB.maxX = x + BlockAxisAlignedBBList.westDoor.maxX;
                    axisAlignedBB.maxY = y + BlockAxisAlignedBBList.westDoor.maxY;
                    axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.westDoor.maxZ;
                }
            }
        } else {
            switch (doorState.facingDirection) {
                case DoorState.FACE_DIRECTION_NORTH -> {
                    if(doorState.hingeLeft){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.westDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.westDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.westDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.westDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.westDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.westDoor.maxZ;
                    } else if(doorState.hingeRight){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.eastDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.eastDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.eastDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.eastDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.eastDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.eastDoor.maxZ;
                    }
                }

                case DoorState.FACE_DIRECTION_SOUTH -> {
                    if(doorState.hingeLeft){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.eastDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.eastDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.eastDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.eastDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.eastDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.eastDoor.maxZ;
                    } else if(doorState.hingeRight){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.westDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.westDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.westDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.westDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.westDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.westDoor.maxZ;
                    }
                }
                case DoorState.FACE_DIRECTION_EAST -> {
                    if(doorState.hingeLeft){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.northDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.northDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.northDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.northDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.northDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.northDoor.maxZ;
                    } else if(doorState.hingeRight){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.southDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.southDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.southDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.southDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.southDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.southDoor.maxZ;
                    }
                }
                case DoorState.FACE_DIRECTION_WEST -> {
                    if(doorState.hingeLeft){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.southDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.southDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.southDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.southDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.southDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.southDoor.maxZ;
                    } else if(doorState.hingeRight){
                        axisAlignedBB.minX = x + BlockAxisAlignedBBList.northDoor.minX;
                        axisAlignedBB.minY = y + BlockAxisAlignedBBList.northDoor.minY;
                        axisAlignedBB.minZ = z + BlockAxisAlignedBBList.northDoor.minZ;
                        axisAlignedBB.maxX = x + BlockAxisAlignedBBList.northDoor.maxX;
                        axisAlignedBB.maxY = y + BlockAxisAlignedBBList.northDoor.maxY;
                        axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.northDoor.maxZ;
                    }
                }
            }
        }
    }

    @Override
    public int getBlockTexture(int x, int y, int z, int face){
        //The door top contains the texture and the lower block contains the direction, hinge side, and open/close state
        return this.textureID;
    }




}
