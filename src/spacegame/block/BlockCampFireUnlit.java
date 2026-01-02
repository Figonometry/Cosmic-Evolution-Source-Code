package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCampfire;
import spacegame.gui.GuiLightFire;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.world.ChestLocation;
import spacegame.world.World;

import java.util.Random;

public final class BlockCampFireUnlit extends BlockCampFire {


    public BlockCampFireUnlit(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased) return;

        short playerHeldItem = player.getHeldItem();
        if (playerHeldItem != Item.NULL_ITEM_REFERENCE && playerHeldItem == Item.fireWood.ID) {
                int logCount = this.getLogCount();
                switch (logCount) {
                    case 0 -> world.setBlockWithNotify(x, y, z, Block.campFire1FireWood.ID, false);
                    case 1 -> world.setBlockWithNotify(x, y, z, Block.campFire2FireWood.ID, false);
                    case 2 -> world.setBlockWithNotify(x, y, z, Block.campFire3Firewood.ID, false);
                    case 3 -> {
                        world.setBlockWithNotify(x, y, z, Block.campFire4FireWood.ID, false);

                        Inventory inventory = new Inventory(1,3);
                        inventory.itemStacks[0].item = Item.fireWood;
                        inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                        inventory.itemStacks[0].count = 4;

                        world.addChestLocation(x,y,z, inventory);
                        MouseListener.rightClickReleased = false;
                    }
                }
                if (logCount != 4) {
                    player.removeItemFromInventory();
                    CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
                }
                return;
        } else {
            CosmicEvolution.instance.setNewGui(new GuiCampfire(CosmicEvolution.instance, CosmicEvolution.instance.save.thePlayer.inventory, world.getChestLocation(x,y,z).inventory, null, x, y, z));
        }

        if (this.getLogCount() == 4) {
            if (playerHeldItem == Item.stoneFragments.ID) {
                CosmicEvolution.instance.setNewGui(new GuiLightFire(CosmicEvolution.instance, x, y, z));
            } else if(playerHeldItem == Item.torch.ID){
                world.setBlockWithNotify(x,y,z, Block.campfireLit.ID, true);
                world.addHeatableBlock(x,y,z);
            }
        }

        MouseListener.rightClickReleased = false;
    }

}
