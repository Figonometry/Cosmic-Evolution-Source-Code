package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.World;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public final class BlockClayGrass extends Block implements ITickable {

    public BlockClayGrass(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if (CosmicEvolution.globalRand.nextInt(166) == 0) {
            if (world.getBlockLightValue(x, y + 1, z) <= 4 && this.canBlockDecayGrass(x, y + 1, z, world)) {
                if(world.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                    world.setBlockAndNotify(x, y, z, getBlockIDForDecay(this.ID), false);
                }
            }
        }
        if(this.ID == Block.clayWithGrassLargePatches.ID && CosmicEvolution.globalRand.nextInt(100000) == 0){
            if(world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).tallGrassCount < 500) {
                if ((world.getBlockLightValue(x, y + 1, z) >= 9 || world.getBlockSkyLightValue(x, y + 1, z) >= 9) && world.getBlockID(x, y + 1, z) == Block.air.ID) {
                    if (world.chunkFullySurrounded(x >> 5, (y + 1) >> 5, z >> 5)) {
                        world.setBlockAndNotify(x, y + 1, z, Block.tallGrass.ID, false);
                        world.findChunkFromChunkCoordinates(x >> 5, (y + 1) >> 5, z >> 5).tallGrassCount++;
                    }
                }
            }
        }
    }


    public static short getBlockIDForDecay(short ID){
        switch (ID){
            case BlockIDList.CLAY_GRASS_FULL -> {
                return BlockIDList.CLAY_GRASS_LARGE_PATCHES;
            }
            case BlockIDList.CLAY_GRASS_LARGE_PATCHES -> {
                return BlockIDList.CLAY_GRASS_SMALL_PATCHES;
            }
            case BlockIDList.CLAY_GRASS_SMALL_PATCHES -> {
                return BlockIDList.CLAY;
            }
            default -> {
                return ID;
            }
        }
    }


    public static float getUnderlyingClayTextureTop(short ID){
        switch (ID){
            case BlockIDList.CLAY_GRASS_SMALL_PATCHES -> {
                return BlockTextureList.CLAY_SMALL_GRASS_PATCHES_LOWER_TOP;
            }
            case BlockIDList.CLAY_GRASS_LARGE_PATCHES -> {
                return BlockTextureList.CLAY_LARGE_GRASS_PATCHES_LOWER_TOP;
            }

            default -> {
                throw new IllegalStateException("Invalid soil fertility state");
            }
        }
    }

    public static float getUnderlyingClayTextureSide(short ID){
        switch (ID){
            case BlockIDList.CLAY_GRASS_FULL -> {
                return BlockTextureList.CLAY_FULL_GRASS_LOWER;
            }
            case BlockIDList.CLAY_GRASS_SMALL_PATCHES -> {
                return BlockTextureList.CLAY_SMALL_GRASS_PATCHES_LOWER_SIDE;
            }
            case BlockIDList.CLAY_GRASS_LARGE_PATCHES -> {
                return BlockTextureList.CLAY_LARGE_GRASS_PATCHES_LOWER_SIDE;
            }

            default -> {
                throw new IllegalStateException("Invalid soil fertility state");
            }
        }
    }

    private boolean canBlockDecayGrass(int x, int y, int z, World world){
        short blockID = world.getBlockID(x, y, z);
        if(Block.list[blockID] instanceof BlockLog){
            LogState logState = (LogState) world.getBlockState(x,y,z, MultiState.LOG_STATE);
            if(logState == null)return false;
            return logState.size == 16;
        } else {
            return Block.list[blockID].isSolid;
        }
    }
}
