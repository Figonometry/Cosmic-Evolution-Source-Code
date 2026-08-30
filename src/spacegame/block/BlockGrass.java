package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.World;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public final class BlockGrass extends Block implements ITickable {
    public static final int GRASS_FULL = 0;
    public static final int GRASS_LARGE_PATCHES = 1;
    public static final int GRASS_SMALL_PATCHES = 2;
    public BlockGrass(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        super.onRightClick(x,y,z, world, player);
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem == Item.reedCraftingGridTop.ID && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            world.setBlockAndNotify(x,y,z, Block.primitiveCraftingTable.ID, false);
            player.removeItemFromInventory();
        }
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if (CosmicEvolution.globalRand.nextInt(166) == 0) {
            if (world.getBlockLightValue(x, y + 1, z) <= 4 && this.canBlockDecayGrass(x, y + 1, z, world)) {
                if(world.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                    world.setBlockAndNotify(x, y, z, getBlockIDForDecay(this.ID), false);
                }
            }

            if(getGrassLevel(this.ID) != GRASS_FULL && BlockSoil.getSoilFertility(this.ID) != BlockTextureList.SOIL_BARREN_FERTILITY_TEXTURE) {
                if (world.getBlockLightValue(x, y + 1, z) >= 9 && !this.canBlockDecayGrass(x, y + 1, z, world)) {
                    if (world.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                        world.setBlockAndNotify(x, y, z, getBlockIDForIncrememnt(this.ID), false);
                    }
                }
            }
        }




        if(CosmicEvolution.globalRand.nextInt(100000) == 0){
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

    protected boolean canBlockDecayGrass(int x, int y, int z, World world){
        short blockID = world.getBlockID(x, y, z);
        if(Block.list[blockID] instanceof BlockLog){
            LogState logState = (LogState) world.getBlockState(x,y,z, MultiState.LOG_STATE);
            if(logState == null)return false;
            return logState.size == 16;
        } else {
            return Block.list[blockID].isSolid;
        }
    }


    public static int getGrassLevel(short ID){
        switch (ID){
            case BlockIDList.CLAY_GRASS_FULL, BlockIDList.GRASS_BARREN_FERTILITY_FULL, BlockIDList.GRASS_LOW_FERTILITY_FULL, BlockIDList.GRASS_MEDIUM_FERTILITY_FULL, BlockIDList.GRASS_HIGH_FERTILITY_FULL -> {
                return GRASS_FULL;
            }
            case BlockIDList.CLAY_GRASS_LARGE_PATCHES, BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH -> {
                return GRASS_LARGE_PATCHES;
            }
            case BlockIDList.CLAY_GRASS_SMALL_PATCHES, BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH -> {
                return GRASS_SMALL_PATCHES;
            }
            default -> {
                throw new IllegalStateException("How in the everloving fuck did this grass related error occur");
            }
        }
    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip.addText("with");
        toolTip.addItemID(ItemIDList.STONE_HOE);
        toolTip.addText("to till the soil");

        this.tooltips[0][0].addToolTip(toolTip);
    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return player.getHeldItem() == Item.stoneHoe.ID ? this.tooltips[0] : null;
    }



    public static short getBlockIDForDecay(short grassBlockID){
        //Logic here is to decrease the grass level until it turns into regular soil
        switch (grassBlockID){

            default -> {
                return grassBlockID;
            }

            case BlockIDList.GRASS_HIGH_FERTILITY_FULL -> {
                return BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.HIGH_FERTILITY_SOIL;
            }

            case BlockIDList.GRASS_MEDIUM_FERTILITY_FULL -> {
                return BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.MEDIUM_FERTILITY_SOIL;
            }

            case BlockIDList.GRASS_LOW_FERTILITY_FULL -> {
                return BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.LOW_FERTILITY_SOIL;
            }

            case BlockIDList.GRASS_BARREN_FERTILITY_FULL -> {
                return BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.BARREN_SOIL;
            }
        }

    }


    public static short getBlockIDForIncrememnt(short grassBlockID) {
        switch (grassBlockID) {

            default -> {
                return grassBlockID;
            }

            case BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_HIGH_FERTILITY_FULL;
            }

            case BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_MEDIUM_FERTILITY_FULL;
            }


            case BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_LOW_FERTILITY_FULL;
            }

            case BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH -> {
                return BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH;
            }
            case BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH -> {
                return BlockIDList.GRASS_BARREN_FERTILITY_FULL;
            }
        }

    }
}
