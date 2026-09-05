package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.render.RenderBlocks;
import spacegame.world.worldtypes.World;

public final class BlockWater extends BlockFluid {
    public BlockWater(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;

        short playerHeldItem = player.getHeldItem();

        short playerHeldBlock = player.getHeldBlock();

        if(playerHeldItem == Item.block.ID && playerHeldBlock != Item.NULL_ITEM_REFERENCE && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            if(Block.list[playerHeldBlock] instanceof BlockSoil && player.addItemToInventory(Item.mud.ID, Item.NULL_ITEM_METADATA, (byte)8, Item.NULL_ITEM_DURABILITY, 0, null)){
                player.removeItemFromInventory();
                MouseListener.rightClickReleased = false;
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }
        }
    }

    //There are multiple copies of the water texture to allow for texture coordinate movements in the shader under the different water states
    @Override
    public int getBlockTexture(int x, int y, int z, int face){
        switch (face){
            case RenderBlocks.NORTH_FACE, RenderBlocks.SOUTH_FACE, RenderBlocks.EAST_FACE, RenderBlocks.WEST_FACE -> {
                if(this.ID != Block.fullWater.ID){
                    return BlockTextureList.WATER_SIDE_TEXTURE; //Side texture
                } else {
                    return BlockTextureList.WATER_SIDE_TEXTURE_2; //Side texture
                }
            }
            case RenderBlocks.BOTTOM_FACE -> {
                return BlockTextureList.WATER_BOTTOM_TEXTURE; //Bottom texture
            }

            //Default top texture
            default -> {
                return this.textureID;
            }
        }
    }




}
