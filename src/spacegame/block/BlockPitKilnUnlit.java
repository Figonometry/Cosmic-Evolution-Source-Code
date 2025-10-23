package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.Chunk;
import spacegame.world.World;

import java.util.Random;

public class BlockPitKilnUnlit extends BlockContainer {
    public BlockPitKilnUnlit(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;;
        short playerHeldItem = player.getHeldItem();


        if((playerHeldItem == Item.straw.ID || playerHeldItem == Item.fireWood.ID) && world.isBlockSuitableForPitKiln(x,y,z) && MouseListener.rightClickReleased){

            if(playerHeldItem == Item.straw.ID){ //Adds straw
                switch (this.ID) {
                    case 74, 75, 76, 77, 78, 79, 80 -> {
                        world.setBlockWithNotify(x, y, z, (short) (this.ID + 1));
                        player.removeItemFromInventory();
                        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.grass, false), new Random().nextFloat(0.6F, 1));
                    }
                }
            }

            if(playerHeldItem == Item.fireWood.ID){ //Adds logs
                switch (this.ID) {
                    case 81 -> {
                        world.setBlockWithNotify(x, y, z, Block.pitKilnUnlitLog1.ID);
                        player.removeItemFromInventory();
                        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false), new Random().nextFloat(0.6F, 1));
                    }
                    case 95, 96, 97 -> {
                        world.setBlockWithNotify(x, y, z, (short) (this.ID + 1));
                        player.removeItemFromInventory();
                        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false), new Random().nextFloat(0.6F, 1));
                    }
                }
            }
            MouseListener.rightClickReleased = false;
            return;
        }


        if(this.ID == Block.pitKilnUnlit.ID && playerHeldItem == Item.torch.ID && KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)){
            world.setBlockWithNotify(x,y,z, Block.pitKilnLit.ID);

            world.addTimeEvent(x,y,z, CosmicEvolution.instance.save.time + ((ITimeUpdate) Block.pitKilnLit).getUpdateTime());

            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
            } else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }

            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addTickableBlockToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        }
    }

    public int getStrawHeight() {
        return switch (this.ID) {
            case 74 -> 1;
            case 75 -> 2;
            case 76 -> 3;
            case 77 -> 4;
            case 78 -> 5;
            case 79 -> 6;
            case 80 -> 7;
            default -> 8;
        };
    }

    public int getNumberOfLogs() {
        return switch (this.ID) {
            case 95 -> 1;
            case 96 -> 2;
            case 97 -> 3;
            case 98, 99 -> 4;
            default -> 0;
        };
    }
}
