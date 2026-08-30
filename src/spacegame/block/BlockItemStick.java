package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.World;

import java.util.Random;

public final class BlockItemStick extends Block {
    public BlockItemStick(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased) return;
        short playerHeldItem = player.getHeldItem();

        if (playerHeldItem == Item.stoneFragments.ID) {
            world.setBlockAndNotify(x, y, z, Block.air.ID, false);
            world.addEntity(new EntityBlock(x + 0.5, y + 0.1, z + 0.5, Block.torchUnlit.ID, (byte) 1));
            MouseListener.rightClickReleased = false;
        }

        if(playerHeldItem == Item.NULL_ITEM_REFERENCE){
            player.addItemToInventory(Item.block.ID, this.ID, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, null);
            world.setBlockAndNotify(x,y,z, Block.air.ID, false);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(this.getStepSound(x,y,z), false, 1f), new Random().nextFloat(0.6F, 1));
            MouseListener.rightClickReleased = false;
        }
    }

    @Override
    public void registerBlockTooltips() {
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip.addText("with");
        toolTip.addItemID(ItemIDList.STONE_FRAGMENTS);
        toolTip.addText("to craft an unlit torch");

        this.tooltips[0][0].addToolTip(toolTip);
    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player) {
        return this.tooltips[0];
    }
}