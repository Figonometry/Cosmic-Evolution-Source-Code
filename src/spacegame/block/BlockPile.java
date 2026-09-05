package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;

public class BlockPile extends BlockContainer {
    public short itemInPile;
    public BlockPile(short ID, int textureID, String filepath, short itemInPile, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
        this.itemInPile = itemInPile;
    }

    @Override
    public String getStepSound(int x, int y, int z){
        ChestLocation chestLocation = (ChestLocation) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CHEST_STATE);
        if(chestLocation != null){
            return chestLocation.inventory.itemStacks[0].item == Item.firedRedClayAdobeBrick ? Sound.stone : this.stepSound;
        }

        return this.stepSound;
    }


    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip rightClick = new ToolTip();
        rightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        rightClick.addText("to add to pile");

        ToolTip rightClickWithShift = new ToolTip();
        rightClickWithShift.addKeyWithBoxOutline("SHIFT");
        rightClickWithShift.addText("+");
        rightClickWithShift.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        rightClickWithShift.addText("to remove from pile");


        this.tooltips[0][0].addToolTip(rightClick);
        this.tooltips[0][0].addToolTip(rightClickWithShift);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }
}
