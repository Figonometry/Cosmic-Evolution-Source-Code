package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingStrawStorage;
import spacegame.gui.GuiLightFire;
import spacegame.item.Item;
import spacegame.world.World;

import java.util.Random;

public final class BlockCampFireUnlit extends BlockCampFire {
    public BlockCampFireUnlit(short ID, int textureID, String filpath) {
        super(ID, textureID, filpath);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased) return;

        short playerHeldItem = player.getHeldItem();
        if (playerHeldItem != Item.NULL_ITEM_REFERENCE) {
            if (playerHeldItem == Item.fireWood.ID) {
                int logCount = this.getLogCount();
                switch (logCount) {
                    case 0 -> world.setBlockWithNotify(x, y, z, Block.campFire1FireWood.ID);
                    case 1 -> world.setBlockWithNotify(x, y, z, Block.campFire2FireWood.ID);
                    case 2 -> world.setBlockWithNotify(x, y, z, Block.campFire3Firewood.ID);
                    case 3 -> world.setBlockWithNotify(x, y, z, Block.campFire4FireWood.ID);
                }
                if (logCount != 4) {
                    player.removeItemFromInventory();
                    CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false), new Random().nextFloat(0.6F, 1));
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if (this.getLogCount() == 4) {
            if (playerHeldItem == Item.stoneFragments.ID) {
                CosmicEvolution.instance.setNewGui(new GuiLightFire(CosmicEvolution.instance, x, y, z));
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(this.ID == Block.campFireNoFirewood.ID){
            if(playerHeldItem != Item.NULL_ITEM_REFERENCE){
                if(Item.list[playerHeldItem].toolType.equals(Item.ITEM_TOOL_TYPE_KNIFE)){
                    CosmicEvolution.instance.setNewGui(new GuiCraftingStrawStorage(CosmicEvolution.instance, x, y, z));
                    MouseListener.rightClickReleased = false;
                }
            }
        }
    }

}
