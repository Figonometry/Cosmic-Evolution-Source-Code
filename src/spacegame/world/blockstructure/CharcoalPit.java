package spacegame.world.blockstructure;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.entity.EntityParticle;
import spacegame.item.Item;
import spacegame.world.BlockPos;
import spacegame.world.Chunk;
import spacegame.world.blockstate.CharcoalState;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;
import spacegame.world.worldtypes.World;

import java.util.ArrayList;

//Charcoal pits are stored on the Chunk object using the firepit as the starting point
public final class CharcoalPit {
    private static final long CHARCOAL_PIT_BURN_TIME = Timer.GAME_DAY;
    private BlockPos min;
    private BlockPos max;
    private BlockPos firePit;
    private long startTime = Long.MIN_VALUE;
    private boolean deleteObject = false;
    private boolean isLit = false;

    public CharcoalPit(BlockPos min, BlockPos max, BlockPos firePit){
        this.min = min;
        this.max = max;
        this.firePit = firePit;
    }


    public void checkForUpdate(World world){
        if(CosmicEvolution.instance.save.time % 60 != 0)return;
        if(!this.isLit)return;

        //Generate the smoke particles at each x/z within the block above the top layer
        for(int x = this.min.x(); x <= this.max.x(); x++){
            int y = this.max.y() + 1;
            for(int z = this.min.z(); z <= this.max.z(); z++){
                double xPos = x + 0.5;
                double yPos = y;
                double zPos = z + 0.5;
                int particleCount = CosmicEvolution.globalRand.nextInt(1,5);
                EntityParticle particle;
                Chunk chunk = CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
                for(int i = 0; i < particleCount; i++){
                    particle = new EntityParticle(xPos + CosmicEvolution.globalRand.nextDouble(-0.5, 0.5), yPos + CosmicEvolution.globalRand.nextDouble(-0.25f, 0.25), zPos + CosmicEvolution.globalRand.nextDouble(-0.5, 0.5), false, CosmicEvolution.globalRand.nextInt(480, 960),  Block.charcoal.ID, false, false, false, true, 1, 1);
                    particle.size *= CosmicEvolution.globalRand.nextFloat(5f, 10f);
                    chunk.addEntityToList(particle);
                }
            }
        }

        //Grace period from lighting the pit before it burns away
        if(CosmicEvolution.instance.save.time - this.startTime < Timer.REAL_SECOND * 30)return;

        if(!this.isPitSealed(world)){
            //Fill pit with fire and return, for now fill with air and delete object

            for(int x = this.min.x(); x <= this.max.x(); x++){
                for(int y = this.min.y(); y <= this.max.y(); y++){
                    for(int z = this.min.z(); z <= this.max.z(); z++){
                        world.setBlockAndNotify(x,y,z, Block.air.ID, false);
                        world.clearAllBlockStates(x,y,z);
                    }
                }
            }

            world.setBlockAndNotify(this.firePit.x(), this.firePit.y(), this.firePit.z(), Block.air.ID, false);
            world.clearAllBlockStates(this.firePit.x(), this.firePit.y(), this.firePit.z());
            this.deleteObject = true;
        }

        if(CosmicEvolution.instance.save.time < this.startTime + CHARCOAL_PIT_BURN_TIME)return;

        //Perform logic to replace the blocks in the pit with charcoal
        int totalHeight = (this.max.y() - this.min.y()) + 1;
        int layersOfCharcoal = 0;

        for(int x = this.min.x(); x <= this.max.x(); x++){
            for(int z = this.min.z(); z <= this.max.z(); z++){
                for(int i = 0; i < totalHeight; i++){
                    layersOfCharcoal += (int)(16f * CosmicEvolution.globalRand.nextFloat(0.25f, 1.0f));
                }

                int totalBlocks = layersOfCharcoal / 16;

                for(int i = 0; i < totalBlocks; i++){
                    world.clearAllBlockStates(x, this.min.y() + i, z);
                    world.setBlockAndNotify(x, this.min.y() + i, z, Block.charcoal.ID, false);
                    world.addBlockState(x, this.min.y() + i, z, MultiState.CHARCOAL_STATE, new CharcoalState(CharcoalState.MAX_LAYERS, Chunk.getBlockIndexFromCoordinates(x, this.min.y() + i, z)));
                }

                int excessLayers = (layersOfCharcoal % 16);
                if(excessLayers > 0) {
                    excessLayers++;
                    world.clearAllBlockStates(x, this.min.y() + totalBlocks, z);
                    world.setBlockAndNotify(x, this.min.y() + totalBlocks, z, Block.charcoal.ID, false);
                    world.addBlockState(x, this.min.y() + totalBlocks, z, MultiState.CHARCOAL_STATE, new CharcoalState(excessLayers, Chunk.getBlockIndexFromCoordinates(x, this.min.y() + totalBlocks, z)));
                }

                layersOfCharcoal = 0;
            }
        }
        world.clearAllBlockStates(this.firePit.x(), this.firePit.y(), this.firePit.z());
        world.setBlockAndNotify(this.firePit.x(), this.firePit.y(), this.firePit.z(), Block.air.ID, false);
        this.deleteObject = true;
    }

    private boolean isPitSealed(World world){

        //Check each side of the box

        for(int x = this.min.x(); x <= this.max.x(); x++){
            for(int z = this.min.z(); z <= this.max.z(); z++){
                int y = this.max.y();

                if(x == this.firePit.x() && z == this.firePit.z())continue;

                if(!doesBlockMeetPitBoundaryRequirements(x, y + 1, z, world))return false;
            }
        }

        for(int x = this.min.x(); x <= this.max.x(); x++){
            for(int z = this.min.z(); z <= this.max.z(); z++){
                int y = this.min.y();

                if(!doesBlockMeetPitBoundaryRequirements(x, y - 1, z, world))return false;
            }
        }

        for(int x = this.min.x(); x <= this.max.x(); x++){
            for(int y = this.min.y(); y <= this.max.y(); y++){
                int z = this.max.z();

                if(!doesBlockMeetPitBoundaryRequirements(x, y, z + 1, world))return false;
            }
        }

        for(int x = this.min.x(); x <= this.max.x(); x++){
            for(int y = this.min.y(); y <= this.max.y(); y++){
                int z = this.min.z();

                if(!doesBlockMeetPitBoundaryRequirements(x, y, z - 1, world))return false;
            }
        }

        for(int z = this.min.z(); z <= this.max.z(); z++){
            for(int y = this.min.y(); y <= this.max.y(); y++){
                int x = this.max.x();

                if(!doesBlockMeetPitBoundaryRequirements(x + 1, y, z, world))return false;
            }
        }

        for(int z = this.min.z(); z <= this.max.z(); z++){
            for(int y = this.min.y(); y <= this.max.y(); y++){
                int x = this.min.x();

                if(!doesBlockMeetPitBoundaryRequirements(x - 1, y, z, world))return false;
            }
        }


        //Check around the firepit
        if(!Block.list[world.getBlockID(this.firePit.x() - 1, this.firePit.y(), this.firePit.z())].isSolid)return false;
        if(!Block.list[world.getBlockID(this.firePit.x() + 1, this.firePit.y(), this.firePit.z())].isSolid)return false;
        if(!Block.list[world.getBlockID(this.firePit.x(), this.firePit.y(), this.firePit.z() - 1)].isSolid)return false;
        if(!Block.list[world.getBlockID(this.firePit.x(), this.firePit.y(), this.firePit.z() + 1)].isSolid)return false;
        if(!Block.list[world.getBlockID(this.firePit.x(), this.firePit.y() + 1, this.firePit.z())].isSolid)return false;

        return true;
    }



    public long getStartTime(){
        return this.startTime;
    }

    public boolean getDeleteObject(){
        return this.deleteObject;
    }

    public BlockPos getMin(){
        return this.min;
    }

    public BlockPos getMax(){
        return this.max;
    }

    public BlockPos getFirePit(){
        return this.firePit;
    }

    public boolean getIsLit(){
        return this.isLit;
    }

    public void setIsLit(boolean isLit){
        this.isLit = isLit;
    }

    public void setStartTime(long startTime){
        if(startTime < CosmicEvolution.instance.save.time){
            System.out.println("Unable to set end time of " + startTime + " due to it being before the current time " + CosmicEvolution.instance.save.time);
            return;
        }
        this.startTime = startTime;
    }



    public static CharcoalPit createCharcoalPit(int campFireX, int campFireY, int campFireZ){
        World world = CosmicEvolution.instance.save.activeWorld;

        //Establish the horizontal bounds of the box by checking along each axis from the campfire
        //This will start from the firepit and on each axis the block will be checked to see if it meets requirements, once the bounds of the rectangle are established check the whole
        //layer for req, then check each subsequent layer down


        //Check the layer immediately below the firepit to get the x and z size
        int layerY = campFireY - 1;
        int northBound = Integer.MIN_VALUE;
        int southBound = Integer.MIN_VALUE;
        int eastBound = Integer.MIN_VALUE;
        int westBound = Integer.MIN_VALUE;

        //Check each axis 7 blocks from the firepit once a block is found that meets the boundary requirements record the coordinate for each side
        for(int layerX = campFireX; layerX > campFireX - 7; layerX--){
            if(doesBlockMeetPitContentRequirements(layerX, layerY, campFireZ, world))continue;
            if(doesBlockMeetPitBoundaryRequirements(layerX, layerY, campFireZ, world)){
                northBound = layerX + 1;
                break;
            }
        }

        for(int layerX = campFireX; layerX < campFireX + 7; layerX++){
            if(doesBlockMeetPitContentRequirements(layerX, layerY, campFireZ, world))continue;
            if(doesBlockMeetPitBoundaryRequirements(layerX, layerY, campFireZ, world)){
                southBound = layerX - 1;
                break;
            }
        }

        for(int layerZ = campFireZ; layerZ > campFireZ - 7; layerZ--){
            if(doesBlockMeetPitContentRequirements(campFireX, layerY, layerZ, world))continue;
            if(doesBlockMeetPitBoundaryRequirements(campFireX, layerY, layerZ, world)){
                eastBound = layerZ + 1;
                break;
            }
        }

        for(int layerZ = campFireZ; layerZ < campFireZ + 7; layerZ++){
            if(doesBlockMeetPitContentRequirements(campFireX, layerY, layerZ, world))continue;
            if(doesBlockMeetPitBoundaryRequirements(campFireX, layerY, layerZ, world)){
                westBound = layerZ - 1;
                break;
            }
        }



        //Check if any of the bounds are open, this will be the case if any are 0, abort and return null
        if(southBound == Integer.MIN_VALUE || northBound == Integer.MIN_VALUE || eastBound == Integer.MIN_VALUE || westBound == Integer.MIN_VALUE)return null;
        //Ensure the difference in bounds does not exceed 7 for a max of 7x7
        if(southBound - northBound > 7 || westBound - eastBound > 7)return null;


        int minX = northBound;
        int minZ = eastBound;
        int minY = layerY - 7; //This is the max minY can be

        int maxX = southBound;
        int maxY = layerY;
        int maxZ = westBound;

        //Check each layer and ensure it meets requirements for pit contents, decrement until at most 7 layers, abort at the first layer that doesnt fully contain log piles
        //Check the layer underneath the last full layer for sealing and then record the y value as minY, if not sealed or if it contains log piles return null
        //Check the remaining 5 sides of the defined box for sealing (disregard the firepit) return null if any side is unsealed
        //If all checks pass return a new charcoal pit object



        layer:
        for(int blockY = maxY; blockY >= minY; blockY--) {
            for (int blockX = minX; blockX <= maxX; blockX++) {
                for (int blockZ = minZ; blockZ <= maxZ; blockZ++) {
                    if(!doesBlockMeetPitContentRequirements(blockX, blockY, blockZ, world)){
                        minY = blockY + 1;
                        break layer;
                    }
                }
            }
        }

        //If the minY exceeded the maxY, which can occur if the first layer fails return null
        if(minY > maxY)return null;


        //Check each side of the box for seal, break and return false if any of them fail

        //Top
        for(int x = minX; x <= maxX; x++){
            for(int z = minZ; z <= maxZ; z++){
                int y = maxY;

                if(x == campFireX  && z == campFireZ)continue;

                if(!doesBlockMeetPitBoundaryRequirements(x, y + 1, z, world))return null;
            }
        }

        //Bottom
        for(int x = minX; x <= maxX; x++){
            for(int z = minZ; z <= maxZ; z++){
                int y = minY;

                if(!doesBlockMeetPitBoundaryRequirements(x, y - 1, z, world))return null;
            }
        }

        //West
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                int z = maxZ;

                if(!doesBlockMeetPitBoundaryRequirements(x, y, z + 1, world))return null;
            }
        }

        //East
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                int z = minZ;

                if(!doesBlockMeetPitBoundaryRequirements(x, y, z - 1, world))return null;
            }
        }

        //South
        for(int z = minZ; z <= maxZ; z++){
            for(int y = minY; y <= maxY; y++){
                int x = maxX;

                if(!doesBlockMeetPitBoundaryRequirements(x + 1, y, z, world))return null;
            }
        }

        //North
        for(int z = minZ; z <= maxZ; z++){
            for(int y = minY; y <= maxY; y++){
                int x = minX;

                if(!doesBlockMeetPitBoundaryRequirements(x - 1, y, z, world))return null;
            }
        }

        return new CharcoalPit(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ), new BlockPos(campFireX, campFireY, campFireZ));
    }

    private static boolean doesBlockMeetPitContentRequirements(int blockX, int blockY, int blockZ, World world){
        if(world.getBlockID(blockX, blockY, blockZ) != Block.logPile.ID)return false;

        ChestLocation chestLocation = (ChestLocation) world.getBlockState(blockX, blockY, blockZ, MultiState.CHEST_STATE);
        if(chestLocation == null)return false;

        return chestLocation.inventory.itemStacks[0].count == 32 && chestLocation.inventory.itemStacks[0].item.ID == Item.fireWood.ID;
    }

    private static boolean doesBlockMeetPitBoundaryRequirements(int blockX, int blockY, int blockZ, World world){
        return Block.list[world.getBlockID(blockX, blockY, blockZ)].isSolid && world.getBlockID(blockX, blockY, blockZ) != Block.logPile.ID;
    }


}
