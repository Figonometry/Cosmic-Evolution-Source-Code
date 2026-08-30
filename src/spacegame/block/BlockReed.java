package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.item.ItemKnife;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.ReedState;

public final class BlockReed extends Block implements ITimeUpdate {
    //Lower block of this type should be assumed to be water logged
    public BlockReed(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem != Item.NULL_ITEM_REFERENCE && Item.list[playerHeldItem] instanceof ItemKnife && world.getBlockID(x,y,z) == Block.reedLower.ID && world.getBlockID(x, y + 1, z) == Block.reedUpper.ID){
            world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.reeds.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null));
            world.addTimeEvent(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
            world.setBlockAndNotify(x, y + 1, z, Block.air.ID, false);
        } else {
            world.removeTimeEvent(x,y,z);
            world.setBlockAndNotify(x, y + 1, z, Block.air.ID, false);
            world.setBlockAndNotify(x, y, z, list[world.getBlockID(x,y,z)].waterlogged ? water.ID : air.ID, false);
        }

        player.reduceHeldItemDurability();

    }
    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        ReedState reedState = (ReedState) world.getBlockState(x,y,z, MultiState.REED_GROWTH_STATE);
        if(reedState == null){
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
            return;
        }

        if(reedState.growthStage < ReedState.GROWTH_STAGE_MATURE){
            reedState.growthStage++;
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
            world.notifyChunk(x,y,z);
            return;
        }

        if(world.getBlockID(x, y + 1, z) != Block.air.ID){
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
            return;
        }

        world.setBlockAndNotify(x, y + 1, z, Block.reedUpper.ID, false);
        world.removeTimeEvent(x,y,z);
    }

    @Override
    public long getUpdateTime(int x, int y, int z, World world) {
        ReedState reedState = (ReedState) world.getBlockState(x,y,z, MultiState.REED_GROWTH_STATE);
        if(reedState == null)return Timer.GAME_DAY;

        return reedState.growthStage != ReedState.GROWTH_STAGE_MATURE ? Timer.GAME_DAY : Timer.GAME_DAY * 7;
    }

    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        ReedState reedState = (ReedState) world.getBlockState(x,y,z, MultiState.REED_GROWTH_STATE);
        if(reedState == null)return "null";


        if(reedState.growthStage == ReedState.GROWTH_STAGE_1){
            return "Will Sprout In: ";
        } else if(reedState.growthStage != ReedState.GROWTH_STAGE_MATURE) {
            return  "Next Growth Stage: ";
        }

        return world.getBlockID(x,y + 1,z) == Block.air.ID ? "Will Grow In: " : "";
    }


    public float getReedGrowthScale(){
        return switch (this.ID) {
            case  BlockIDList.REED_GROW_1 -> 0.16f; //growth 1
            case  BlockIDList.REED_GROW_2 -> 0.32f; //growth 2
            case  BlockIDList.REED_GROW_3 -> 0.46f; //growth 3
            case  BlockIDList.REED_GROW_4 -> 0.64f; //growth 4
            case  BlockIDList.REED_GROW_5 -> 0.8f; //growth 5
            default -> 1;
        };
    }

    public float getReedTranslation(){
        return switch (this.ID){
            case BlockIDList.REED_GROW_1 -> 0.42f; //growth 1
            case BlockIDList.REED_GROW_2 -> 0.34f; //growth 2
            case BlockIDList.REED_GROW_3 -> 0.26f; //growth 3
            case BlockIDList.REED_GROW_4 -> 0.18f; //growth 4
            case BlockIDList.REED_GROW_5 -> 0.1f; //growth 5
            default -> 0;
        };
    }


    @Override
    public void addBlockStates(int x, int y, int z, World world, EntityPlayer player, Chunk chunk){
        int key = Chunk.getBlockIndexFromCoordinates(x,y,z);
        chunk.addBlockState(key, MultiState.REED_GROWTH_STATE, new ReedState(ReedState.GROWTH_STAGE_1, key));
    }


    @Override
    public ModelLoader getBlockModel(int x, int y, int z, World world){
        ReedState reedState = (ReedState) world.getBlockState(x,y,z, MultiState.REED_GROWTH_STATE);
        if(reedState == null)return this.blockModel;
        return reedState.growthStage == ReedState.GROWTH_STAGE_1 ? BlockModelList.seedModel : this.blockModel;
    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.LEFT_CLICK);
        toolTip.addText("with");
        toolTip.addItemID(ItemIDList.STONE_KNIFE);
        toolTip.addText("to harvest");
        toolTip.addItemID(ItemIDList.REEDS);

        this.tooltips[0][0].addToolTip(toolTip);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        ReedState reedState = (ReedState) world.getBlockState(x,y,z, MultiState.REED_GROWTH_STATE);
        if(reedState == null)return null;

        //Left click with knife in hand to harvest but only if block above is upper reed
        if(reedState.growthStage == ReedState.GROWTH_STAGE_MATURE && world.getBlockID(x, y + 1, z) == Block.reedUpper.ID){
            return this.tooltips[0];
        }

        return null;
    }


}
