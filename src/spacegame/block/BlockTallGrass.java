package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.item.ItemKnife;
import spacegame.item.itemstate.SeedState;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.worldtypes.World;

public final class BlockTallGrass extends Block {
    public BlockTallGrass(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
       if(CosmicEvolution.globalRand.nextInt(100) > 10){
           super.onLeftClick(x,y,z, world, player);
           return;
       }

        SeedState seedState = new SeedState(false, 0, "no target");


        EntityItem entityItem = new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5, z + CosmicEvolution.globalRand.nextDouble(),
                Item.seedWildGrass.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, seedState);

        world.addEntity(entityItem);
        super.onLeftClick(x,y,z, world, player);
    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addKeyWithBoxOutline("SHIFT");
        toolTip.addText("+");
        toolTip.addMouseIcon(MouseAndKeyIconTextureList.LEFT_CLICK);
        toolTip.addText("with");
        toolTip.addItemID(ItemIDList.STONE_KNIFE);
        toolTip.addText("to harvest");
        toolTip.addItemID(ItemIDList.STRAW);

        this.tooltips[0][0].addToolTip(toolTip);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }

    @Override
    protected void handleSpecialLeftClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.NULL_ITEM_REFERENCE) {
            if (Item.list[playerHeldItem] instanceof ItemKnife && playerHeldItem != Item.stoneHandKnifeBlade.ID) {
                world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.straw.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null));
            }
        }
    }
}
