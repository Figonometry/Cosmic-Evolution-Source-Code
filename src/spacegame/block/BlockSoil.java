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
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public final class BlockSoil extends Block implements ITickable {
    public BlockSoil(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if (CosmicEvolution.globalRand.nextInt(166) == 0) {
            byte blockLight = world.getBlockLightValue(x,y + 1, z);
            byte skyLight = world.getBlockSkyLightValue(x, y + 1,z);
            if((blockLight >= 9 || skyLight >= 9) && !this.canBlockDecayGrass(x,y + 1,z,world)){
                world.setBlockAndNotify(x,y,z, getBlockIDForGrassSpread(this.ID), false);
            }
        }
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        switch (this.ID){
            case BlockIDList.BARREN_SOIL -> {
                return "Barren Soil";
            }
            case BlockIDList.LOW_FERTILITY_SOIL -> {
                return "Low Fertility Soil";
            }
            case BlockIDList.MEDIUM_FERTILITY_SOIL -> {
                return "Medium Fertility Soil";
            }
            case BlockIDList.HIGH_FERTILITY_SOIL -> {
                return "High Fertility Soil";
            }

            default -> {
                return this.displayName;
            }
        }

    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[2][1];
        this.tooltips[0][0] = new ToolTipGroup();
        this.tooltips[1][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip.addText("with");
        toolTip.addItemID(ItemIDList.STONE_HOE);
        toolTip.addText("to till the soil");

        this.tooltips[0][0].addToolTip(toolTip);


        ToolTip toolTip2 = new ToolTip();

        toolTip2.addKeyWithBoxOutline("SHIFT");
        toolTip2.addText("+");
        toolTip2.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip2.addText("with");
        toolTip2.addItemID(ItemIDList.REED_CRAFTING_GRID_TOP);
        toolTip2.addText("to create");
        toolTip2.addBlockID(BlockIDList.PRIMITIVE_CRAFTING_TABLE);

        this.tooltips[1][0].addToolTip(toolTip2);
    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return player.getHeldItem() == Item.stoneHoeHead.ID ? this.tooltips[0] : player.getHeldItem() == Item.reedCraftingGridTop.ID ? this.tooltips[1] : null;
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


    public static float getNutrientLevel(short ID){
        //Handles for both grass and soil
        switch (ID){
            case BlockIDList.BARREN_SOIL, BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_BARREN_FERTILITY_FULL ->{
                return 0.05f;
            }
            case BlockIDList.LOW_FERTILITY_SOIL , BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_LOW_FERTILITY_FULL ->{
                return 0.25f;
            }
            case BlockIDList.MEDIUM_FERTILITY_SOIL , BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_MEDIUM_FERTILITY_FULL ->{
                return 0.5f;
            }
            case BlockIDList.HIGH_FERTILITY_SOIL, BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_HIGH_FERTILITY_FULL ->{
                return 0.8f;
            }
            default -> {
                return 0.01f;
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


    public static short getBlockIDForGrassSpread(short ID){
        switch (ID){
            case BlockIDList.BARREN_SOIL -> {
                return BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.LOW_FERTILITY_SOIL -> {
                return BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.MEDIUM_FERTILITY_SOIL -> {
                return BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH;
            }
            case BlockIDList.HIGH_FERTILITY_SOIL -> {
                return BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH;
            }
            default -> {
                return ID;
            }
        }
    }


    public static float getSoilFertility(short ID){
        //Returns the soil texture for grass blocks
        switch (ID){
            case BlockIDList.BARREN_SOIL, BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_BARREN_FERTILITY_FULL ->  {
                return BlockTextureList.SOIL_BARREN_FERTILITY_TEXTURE;
            }
            case BlockIDList.LOW_FERTILITY_SOIL, BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_LOW_FERTILITY_FULL ->  {
                return BlockTextureList.SOIL_LOW_FERTILITY_TEXTURE;
            }
            case BlockIDList.MEDIUM_FERTILITY_SOIL, BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_MEDIUM_FERTILITY_FULL ->  {
                return BlockTextureList.SOIL_MEDIUM_FERTILITY_TEXTURE;
            }
            case BlockIDList.HIGH_FERTILITY_SOIL, BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH, BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH, BlockIDList.GRASS_HIGH_FERTILITY_FULL ->  {
                return BlockTextureList.SOIL_HIGH_FERTILITY_TEXTURE;
            }

            default -> {
                throw new IllegalStateException("Invalid soil fertility state");
            }
        }
    }

    public static float getUnderlyingSoilTextureTop(short ID){
        switch (ID){
            case BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH -> {
                return BlockTextureList.SOIL_BARREN_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP;
            }

            case BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH -> {
                return BlockTextureList.SOIL_LOW_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP;
            }

            case BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH -> {
                return BlockTextureList.SOIL_MEDIUM_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP;
            }

            case BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH -> {
                return BlockTextureList.SOIL_HIGH_FERTILITY_SMALL_GRASS_PATCHES_LOWER_TOP;
            }



            case BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH -> {
                return BlockTextureList.SOIL_BARREN_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP;
            }

            case BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH -> {
                return BlockTextureList.SOIL_LOW_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP;
            }

            case BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH -> {
                return BlockTextureList.SOIL_MEDIUM_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP;
            }

            case BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH -> {
                return BlockTextureList.SOIL_HIGH_FERTILITY_LARGE_GRASS_PATCHES_LOWER_TOP;
            }


            default -> {
                throw new IllegalStateException("Invalid soil fertility state");
            }
        }
    }


    public static float getUnderlyingSoilTextureSide(short ID){
            switch (ID){
                case BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH -> {
                    return BlockTextureList.SOIL_BARREN_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE;
                }

                case BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH -> {
                    return BlockTextureList.SOIL_LOW_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE;
                }

                case BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH -> {
                    return BlockTextureList.SOIL_MEDIUM_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE;
                }

                case BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH -> {
                    return BlockTextureList.SOIL_HIGH_FERTILITY_SMALL_GRASS_PATCHES_LOWER_SIDE;
                }



                case BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH -> {
                    return BlockTextureList.SOIL_BARREN_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE;
                }

                case BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH -> {
                    return BlockTextureList.SOIL_LOW_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE;
                }

                case BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH -> {
                    return BlockTextureList.SOIL_MEDIUM_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE;
                }

                case BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH -> {
                    return BlockTextureList.SOIL_HIGH_FERTILITY_LARGE_GRASS_PATCHES_LOWER_SIDE;
                }


                case BlockIDList.GRASS_BARREN_FERTILITY_FULL -> {
                    return BlockTextureList.SOIL_BARREN_FERTILITY_FULL_GRASS_LOWER;
                }

                case BlockIDList.GRASS_LOW_FERTILITY_FULL -> {
                    return BlockTextureList.SOIL_LOW_FERTILITY_FULL_GRASS_LOWER;
                }

                case BlockIDList.GRASS_MEDIUM_FERTILITY_FULL -> {
                    return BlockTextureList.SOIL_MEDIUM_FERTILITY_FULL_GRASS_LOWER;
                }

                case BlockIDList.GRASS_HIGH_FERTILITY_FULL -> {
                    return BlockTextureList.SOIL_HIGH_FERTILITY_FULL_GRASS_LOWER;
                }

                default -> {
                    throw new IllegalStateException("Invalid soil fertility state");
                }
            }
    }




}
