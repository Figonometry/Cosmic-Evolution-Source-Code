package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingStoneTools;
import spacegame.gui.GuiInGame;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.worldtypes.World;

import java.util.Random;


public final class BlockItemStone extends Block {
    public BlockItemStone(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased) return;
        short playerHeldBlock = player.getHeldBlock();
        if(playerHeldBlock != Item.NULL_ITEM_REFERENCE) {
            if (Block.list[playerHeldBlock] instanceof BlockItemStone && this.suitableStoneForKnapping()) {
                CosmicEvolution.instance.setNewGui(new GuiCraftingStoneTools(CosmicEvolution.instance, x, y, z, this.ID));
                MouseListener.rightClickReleased = false;
            }
        }

        if(player.getHeldItem() == Item.NULL_ITEM_REFERENCE){
            player.addItemToInventory(Item.block.ID, this.ID, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, null);
            world.setBlockAndNotify(x,y,z, Block.air.ID, false);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(this.getStepSound(x,y,z), false, 1f), new Random().nextFloat(0.6F, 1));
            MouseListener.rightClickReleased = false;
        }
    }


    private boolean suitableStoneForKnapping(){
        switch (this.ID){
            case BlockIDList.ANDESITE_ITEM_STONE, BlockIDList.GRANITE_ITEM_STONE, BlockIDList.BASALT_ITEM_STONE,
                    BlockIDList.CHERT_ITEM_STONE, BlockIDList.OBSIDIAN_ITEM_STONE, BlockIDList.FLINT_ITEM_STONE -> {
                return true;
            }
        }

        GuiInGame.setMessageText("Stone unsuitable for knapping", 16777215);

        return false;
    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip.addText("with");
        toolTip.addBlockID(this.ID);
        toolTip.addText("to craft");

        this.tooltips[0][0].addToolTip(toolTip);
    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }
}
