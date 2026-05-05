package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.AxisAlignedBB;
import spacegame.world.DoorTransition;
import spacegame.world.World;

public final class BlockDoor extends Block {

    //All door models by default face north and are centered
    public BlockDoor(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer thePlayer){
        //If it's the facing block we need to get the information from the above block
        if((this.ID >= Block.doorNorthDoorHingeLeftClosed.ID && this.ID <= Block.doorWestDoorHingeRightOpen.ID)){
            short block = world.getBlockID(x, y + 1, z);

            world.addEntity(new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[block].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[block].droppedItemID].durability, 0));

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


        if(world.getDoorTransition(x,y,z) != null)return;
        if(!MouseListener.rightClickReleased)return;

        if(world.getBlockID(x, y + 1, z) == Block.doorPrimitiveUpper.ID){
            if(CosmicEvolution.globalRand.nextInt(100) < 10){
                this.onLeftClick(x,y,z, world, player);
                CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.crunch, false, 1), 0.5f);
                world.generateParticlesOnBlockBreak(Block.doorPrimitiveUpper.ID, x,y,z);
                return;
            }
        }

        if(this.isDoorOpen){
            if(this.ID == Block.doorNorthDoorHingeLeftOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorNorthDoorHingeLeftClosed.ID, true);

                if(world.getBlockID(x,y,z - 1) == Block.doorNorthDoorHingeRightOpen.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z - 1, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                    world.setBlockWithNotify(x,y,z - 1, Block.doorNorthDoorHingeRightClosed.ID, true);
                }

            } else if(this.ID == Block.doorNorthDoorHingeRightOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorNorthDoorHingeRightClosed.ID, true);

                if(world.getBlockID(x,y,z + 1) == Block.doorNorthDoorHingeLeftOpen.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z + 1, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                    world.setBlockWithNotify(x,y,z + 1, Block.doorNorthDoorHingeLeftClosed.ID, true);
                }
            } else if(this.ID == Block.doorSouthDoorHingeLeftOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorSouthDoorHingeLeftClosed.ID, true);

                if(world.getBlockID(x,y,z + 1) == Block.doorSouthDoorHingeRightOpen.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z + 1, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                    world.setBlockWithNotify(x,y,z + 1, Block.doorSouthDoorHingeRightClosed.ID, true);
                }
            } else if(this.ID == Block.doorSouthDoorHingeRightOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorSouthDoorHingeRightClosed.ID, true);

                if(world.getBlockID(x,y,z - 1) == Block.doorSouthDoorHingeLeftOpen.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z - 1, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                    world.setBlockWithNotify(x,y,z - 1, Block.doorSouthDoorHingeLeftClosed.ID, true);
                }
            }else if(this.ID == Block.doorEastDoorHingeLeftOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorEastDoorHingeLeftClosed.ID, true);

                if(world.getBlockID(x + 1, y, z) == Block.doorEastDoorHingeRightOpen.ID){
                    world.addDoorTransition(new DoorTransition(x + 1, y, z, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                    world.setBlockWithNotify(x + 1,y,z, Block.doorEastDoorHingeRightClosed.ID, true);
                }
            } else if(this.ID == Block.doorEastDoorHingeRightOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorEastDoorHingeRightClosed.ID, true);

                if(world.getBlockID(x - 1, y, z) == Block.doorEastDoorHingeLeftOpen.ID){
                    world.addDoorTransition(new DoorTransition(x - 1, y, z, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                    world.setBlockWithNotify(x - 1,y,z, Block.doorEastDoorHingeLeftClosed.ID, true);
                }
            }else if(this.ID == Block.doorWestDoorHingeLeftOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorWestDoorHingeLeftClosed.ID, true);

                if(world.getBlockID(x - 1, y, z) == Block.doorWestDoorHingeRightOpen.ID){
                    world.addDoorTransition(new DoorTransition(x - 1, y, z, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                    world.setBlockWithNotify(x - 1,y,z, Block.doorWestDoorHingeRightClosed.ID, true);
                }
            } else if(this.ID == Block.doorWestDoorHingeRightOpen.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, true, false, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorWestDoorHingeRightClosed.ID, true);

                if(world.getBlockID(x + 1, y, z) == Block.doorWestDoorHingeLeftOpen.ID){
                    world.addDoorTransition(new DoorTransition(x + 1, y, z, CosmicEvolution.instance.save.time, true, false, true, false), x, y, z);
                    world.setBlockWithNotify(x + 1,y,z, Block.doorWestDoorHingeLeftClosed.ID, true);
                }
            }
        } else {
            if(this.ID == Block.doorNorthDoorHingeLeftClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorNorthDoorHingeLeftOpen.ID, true);

                if(world.getBlockID(x, y, z - 1) == Block.doorNorthDoorHingeRightClosed.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z - 1, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                    world.setBlockWithNotify(x,y,z - 1, Block.doorNorthDoorHingeRightOpen.ID, true);
                }
            } else if(this.ID == Block.doorNorthDoorHingeRightClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorNorthDoorHingeRightOpen.ID, true);

                if(world.getBlockID(x, y, z + 1) == Block.doorNorthDoorHingeLeftClosed.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z + 1, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                    world.setBlockWithNotify(x,y,z + 1, Block.doorNorthDoorHingeLeftOpen.ID, true);
                }
            } else if(this.ID == Block.doorSouthDoorHingeLeftClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorSouthDoorHingeLeftOpen.ID, true);

                if(world.getBlockID(x, y, z + 1) == Block.doorSouthDoorHingeRightClosed.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z + 1, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                    world.setBlockWithNotify(x,y,z + 1, Block.doorSouthDoorHingeRightOpen.ID, true);
                }
            } else if(this.ID == Block.doorSouthDoorHingeRightClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorSouthDoorHingeRightOpen.ID, true);

                if(world.getBlockID(x, y, z - 1) == Block.doorSouthDoorHingeLeftClosed.ID){
                    world.addDoorTransition(new DoorTransition(x, y, z - 1, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                    world.setBlockWithNotify(x,y,z - 1, Block.doorSouthDoorHingeLeftOpen.ID, true);
                }
            }else if(this.ID == Block.doorEastDoorHingeLeftClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorEastDoorHingeLeftOpen.ID, true);

                if(world.getBlockID(x + 1, y, z) == Block.doorEastDoorHingeRightClosed.ID){
                    world.addDoorTransition(new DoorTransition(x + 1, y, z, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                    world.setBlockWithNotify(x + 1,y,z, Block.doorEastDoorHingeRightOpen.ID, true);
                }
            } else if(this.ID == Block.doorEastDoorHingeRightClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorEastDoorHingeRightOpen.ID, true);

                if(world.getBlockID(x - 1, y, z) == Block.doorEastDoorHingeLeftClosed.ID){
                    world.addDoorTransition(new DoorTransition(x - 1, y, z, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                    world.setBlockWithNotify(x - 1,y,z, Block.doorEastDoorHingeLeftOpen.ID, true);
                }
            }else if(this.ID == Block.doorWestDoorHingeLeftClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorWestDoorHingeLeftOpen.ID, true);

                if(world.getBlockID(x - 1, y, z) == Block.doorWestDoorHingeRightClosed.ID){
                    world.addDoorTransition(new DoorTransition(x - 1, y, z, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                    world.setBlockWithNotify(x - 1,y,z, Block.doorWestDoorHingeRightOpen.ID, true);
                }
            } else if(this.ID == Block.doorWestDoorHingeRightClosed.ID){
                world.addDoorTransition(new DoorTransition(x, y, z, CosmicEvolution.instance.save.time, false, true, false, true), x, y, z);
                world.setBlockWithNotify(x,y,z, Block.doorWestDoorHingeRightOpen.ID, true);

                if(world.getBlockID(x + 1, y, z) == Block.doorWestDoorHingeLeftClosed.ID){
                    world.addDoorTransition(new DoorTransition(x + 1, y, z, CosmicEvolution.instance.save.time, false, true, true, false), x, y, z);
                    world.setBlockWithNotify(x + 1,y,z, Block.doorWestDoorHingeLeftOpen.ID, true);
                }
            }
        }


        MouseListener.rightClickReleased = false;
        if(this.isDoorOpen){//Inverted due to the state change
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.doorOpen, false, 1), 0.75f);
        } else {
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.doorOpen, false, 1), 1f);
        }
    }



    @Override
    public void adjustBoundingBox(int x, int y, int z, AxisAlignedBB axisAlignedBB) {
        short blockID = CosmicEvolution.instance.save.activeWorld.getBlockID(x, y, z);

        Block block = this;
        if (blockID == Block.doorPrimitiveUpper.ID) {
            block = Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(x, y - 1, z)];
        }

        if (!block.isDoorOpen) {
            switch (block.faceDirection) {
                case "North" -> {
                    axisAlignedBB.minX = x + Block.northDoor.minX;
                    axisAlignedBB.minY = y + Block.northDoor.minY;
                    axisAlignedBB.minZ = z + Block.northDoor.minZ;
                    axisAlignedBB.maxX = x + Block.northDoor.maxX;
                    axisAlignedBB.maxY = y + Block.northDoor.maxY;
                    axisAlignedBB.maxZ = z + Block.northDoor.maxZ;
                }

                case "South" -> {
                    axisAlignedBB.minX = x + Block.southDoor.minX;
                    axisAlignedBB.minY = y + Block.southDoor.minY;
                    axisAlignedBB.minZ = z + Block.southDoor.minZ;
                    axisAlignedBB.maxX = x + Block.southDoor.maxX;
                    axisAlignedBB.maxY = y + Block.southDoor.maxY;
                    axisAlignedBB.maxZ = z + Block.southDoor.maxZ;
                }
                case "East" -> {
                    axisAlignedBB.minX = x + Block.eastDoor.minX;
                    axisAlignedBB.minY = y + Block.eastDoor.minY;
                    axisAlignedBB.minZ = z + Block.eastDoor.minZ;
                    axisAlignedBB.maxX = x + Block.eastDoor.maxX;
                    axisAlignedBB.maxY = y + Block.eastDoor.maxY;
                    axisAlignedBB.maxZ = z + Block.eastDoor.maxZ;
                }
                case "West" -> {
                    axisAlignedBB.minX = x + Block.westDoor.minX;
                    axisAlignedBB.minY = y + Block.westDoor.minY;
                    axisAlignedBB.minZ = z + Block.westDoor.minZ;
                    axisAlignedBB.maxX = x + Block.westDoor.maxX;
                    axisAlignedBB.maxY = y + Block.westDoor.maxY;
                    axisAlignedBB.maxZ = z + Block.westDoor.maxZ;
                }
            }
        } else {
            switch (block.faceDirection) {
                case "North" -> {
                    if(this.ID == Block.doorNorthDoorHingeLeftOpen.ID){
                        axisAlignedBB.minX = x + Block.westDoor.minX;
                        axisAlignedBB.minY = y + Block.westDoor.minY;
                        axisAlignedBB.minZ = z + Block.westDoor.minZ;
                        axisAlignedBB.maxX = x + Block.westDoor.maxX;
                        axisAlignedBB.maxY = y + Block.westDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.westDoor.maxZ;
                    } else if(this.ID == Block.doorNorthDoorHingeRightOpen.ID){
                        axisAlignedBB.minX = x + Block.eastDoor.minX;
                        axisAlignedBB.minY = y + Block.eastDoor.minY;
                        axisAlignedBB.minZ = z + Block.eastDoor.minZ;
                        axisAlignedBB.maxX = x + Block.eastDoor.maxX;
                        axisAlignedBB.maxY = y + Block.eastDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.eastDoor.maxZ;
                    }
                }

                case "South" -> {
                    if(this.ID == Block.doorSouthDoorHingeLeftOpen.ID){
                        axisAlignedBB.minX = x + Block.eastDoor.minX;
                        axisAlignedBB.minY = y + Block.eastDoor.minY;
                        axisAlignedBB.minZ = z + Block.eastDoor.minZ;
                        axisAlignedBB.maxX = x + Block.eastDoor.maxX;
                        axisAlignedBB.maxY = y + Block.eastDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.eastDoor.maxZ;
                    } else if(this.ID == Block.doorSouthDoorHingeRightOpen.ID){
                        axisAlignedBB.minX = x + Block.westDoor.minX;
                        axisAlignedBB.minY = y + Block.westDoor.minY;
                        axisAlignedBB.minZ = z + Block.westDoor.minZ;
                        axisAlignedBB.maxX = x + Block.westDoor.maxX;
                        axisAlignedBB.maxY = y + Block.westDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.westDoor.maxZ;
                    }
                }
                case "East" -> {
                    if(this.ID == Block.doorEastDoorHingeLeftOpen.ID){
                        axisAlignedBB.minX = x + Block.northDoor.minX;
                        axisAlignedBB.minY = y + Block.northDoor.minY;
                        axisAlignedBB.minZ = z + Block.northDoor.minZ;
                        axisAlignedBB.maxX = x + Block.northDoor.maxX;
                        axisAlignedBB.maxY = y + Block.northDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.northDoor.maxZ;
                    } else if(this.ID == Block.doorEastDoorHingeRightOpen.ID){
                        axisAlignedBB.minX = x + Block.southDoor.minX;
                        axisAlignedBB.minY = y + Block.southDoor.minY;
                        axisAlignedBB.minZ = z + Block.southDoor.minZ;
                        axisAlignedBB.maxX = x + Block.southDoor.maxX;
                        axisAlignedBB.maxY = y + Block.southDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.southDoor.maxZ;
                    }
                }
                case "West" -> {
                    if(this.ID == Block.doorWestDoorHingeLeftOpen.ID){
                        axisAlignedBB.minX = x + Block.southDoor.minX;
                        axisAlignedBB.minY = y + Block.southDoor.minY;
                        axisAlignedBB.minZ = z + Block.southDoor.minZ;
                        axisAlignedBB.maxX = x + Block.southDoor.maxX;
                        axisAlignedBB.maxY = y + Block.southDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.southDoor.maxZ;
                    } else if(this.ID == Block.doorWestDoorHingeRightOpen.ID){
                        axisAlignedBB.minX = x + Block.northDoor.minX;
                        axisAlignedBB.minY = y + Block.northDoor.minY;
                        axisAlignedBB.minZ = z + Block.northDoor.minZ;
                        axisAlignedBB.maxX = x + Block.northDoor.maxX;
                        axisAlignedBB.maxY = y + Block.northDoor.maxY;
                        axisAlignedBB.maxZ = z + Block.northDoor.maxZ;
                    }
                }
            }
        }
    }

    @Override
    public int getBlockTexture(int face){

        //The door top contains the texture and the lower block contains the orientation
        return this.textureID;
    }


}
