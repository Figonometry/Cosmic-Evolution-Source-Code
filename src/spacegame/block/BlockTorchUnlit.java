package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.World;

public final class BlockTorchUnlit extends Block {
    public BlockTorchUnlit(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))return;
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem != Item.torch.ID)return;

        switch (this.ID){
            case 121 -> { //Standard unlit torch
                world.setBlockWithNotify(x,y,z, Block.torchStandard.ID, false);
            }
            case 122 -> { //North unlit torch
                world.setBlockWithNotify(x,y,z, Block.torchNorth.ID, false);
            }
            case 123 -> { //South unlit torch
                world.setBlockWithNotify(x,y,z, Block.torchSouth.ID, false);
            }
            case 124 -> { //East unlit torch
                world.setBlockWithNotify(x,y,z, Block.torchEast.ID, false);
            }
            case 125 -> { //West unlit torch
                world.setBlockWithNotify(x,y,z, Block.torchWest.ID, false);
            }
        }
    }
}
