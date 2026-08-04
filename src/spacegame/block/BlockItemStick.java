package spacegame.block;

import spacegame.core.MouseListener;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.World;

public final class BlockItemStick extends Block {
    public BlockItemStick(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

            if(playerHeldItem == Item.stoneFragments.ID){
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
                world.addEntity(new EntityBlock(x + 0.5, y + 0.1, z + 0.5, Block.torchStandardUnlit.ID, (byte) 1));
                MouseListener.rightClickReleased = false;
            }
        }
    }
