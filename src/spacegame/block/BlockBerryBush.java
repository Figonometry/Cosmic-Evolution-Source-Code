package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Timer;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.IDecayItem;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.BerryBushState;
import spacegame.world.blockstate.MultiState;

public final class BlockBerryBush extends Block implements ITimeUpdate {
    public BlockBerryBush(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);


        //Sets the state to flowering only if the plant is mature and does not have fruit
        if(!berryBushState.hasMatureFruit && !berryBushState.isFlowering && berryBushState.growthStage == BerryBushState.GROWTH_STAGE_MATURE){
            berryBushState.isFlowering = true;
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
        }

        //Sets the state to have fruit and no longer flowering only if the plant is mature and does not have already fruit
        if(!berryBushState.hasMatureFruit && berryBushState.isFlowering && berryBushState.growthStage == BerryBushState.GROWTH_STAGE_MATURE){
            berryBushState.isFlowering = false;
            berryBushState.hasMatureFruit = true;
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
        }

        //Sets the state to no longer have fruit and also ensures it is not flowering only if the plant is mature
        if(berryBushState.hasMatureFruit && berryBushState.growthStage == BerryBushState.GROWTH_STAGE_MATURE){
            berryBushState.isFlowering = false;
            berryBushState.hasMatureFruit = false;
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
        }

        //Increments the state's growth stage only if the plant is not mature
        if(berryBushState.growthStage != BerryBushState.GROWTH_STAGE_MATURE){
            berryBushState.growthStage++;
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
        }

        world.notifyChunk(x,y,z);
    }


    public float getBerryBushScale(int x, int y, int z, World world){
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return 1;

        return switch (berryBushState.growthStage) {
            case BerryBushState.GROWTH_STAGE_2 -> 0.16f; //growth 1
            case BerryBushState.GROWTH_STAGE_3 -> 0.32f; //growth 2
            case BerryBushState.GROWTH_STAGE_4 -> 0.46f; //growth 3
            case BerryBushState.GROWTH_STAGE_5 -> 0.64f; //growth 4
            case BerryBushState.GROWTH_STAGE_6 -> 0.8f; //growth 5
            default -> 1;
        };
    }

    public float getBerryBushTranslation(int x, int y, int z, World world){
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return 1;


        return switch (berryBushState.growthStage){
            case BerryBushState.GROWTH_STAGE_2 -> 0.42f; //growth 1
            case BerryBushState.GROWTH_STAGE_3 -> 0.34f; //growth 2
            case BerryBushState.GROWTH_STAGE_4 -> 0.26f; //growth 3
            case BerryBushState.GROWTH_STAGE_5 -> 0.18f; //growth 4
            case BerryBushState.GROWTH_STAGE_6 -> 0.1f; //growth 5
            default -> 0;
        };
    }

    @Override
    public long getUpdateTime(int x, int y, int z, World world) {
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return Timer.GAME_DAY;

        return berryBushState.growthStage == BerryBushState.GROWTH_STAGE_MATURE ? Timer.GAME_DAY * 7 : Timer.GAME_DAY;
    }


    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return "null";
        if(berryBushState.hasMatureFruit)return "";

        if(berryBushState.growthStage != BerryBushState.GROWTH_STAGE_MATURE){
            return berryBushState.growthStage == BerryBushState.GROWTH_STAGE_1 ?  "Will Sprout In: "  : "Next Growth Stage: ";
        } else {
            return berryBushState.isFlowering ? "Will Ripen In: " : "Will Flower In: ";
        }

    }
    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;

        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return;


        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem != Item.block.ID && player.getHeldBlock() != Block.torch.ID && player.getHeldBlock() != Block.torchUnlit.ID){
            if(world.getBlockID(x,y,z) == Block.berryBush.ID){
                berryBushState.hasMatureFruit = false;
                world.notifyChunk(x,y,z);
                world.addEntity(new EntityItem(x + CosmicEvolution.globalRand.nextFloat(), y + 0.5, z + CosmicEvolution.globalRand.nextFloat(), Item.berry.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, world.ce.save.time + ((IDecayItem)Item.berry).getDecayTime(), null));
                world.addTimeEvent(x,y,z,CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z, world));
            }
            MouseListener.rightClickReleased = false;
        }
    }


    @Override
    public int getBlockTexture(int x, int y, int z, int face){
        BerryBushState berryBushState = (BerryBushState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return this.textureID;


        if(berryBushState.hasMatureFruit){
            return face != Block.FACE_UP ? BlockTextureList.BERRY_BUSH_SIDE_TEXTURE : BlockTextureList.BERRY_BUSH_TOP_TEXTURE;
        }

        if(berryBushState.isFlowering){
            return face != Block.FACE_UP ? BlockTextureList.BERRY_BUSH_FLOWER_SIDE_TEXTURE : BlockTextureList.BERRY_BUSH_FLOWER_TOP_TEXTURE;
        }

        return face != Block.FACE_UP ? BlockTextureList.BERRY_BUSH_SIDE_BASE_TEXTURE : BlockTextureList.BERRY_BUSH_TOP_BASE_TEXTURE;
    }

    @Override
    public ModelLoader getBlockModel(int x, int y, int z, World world){
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z,MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return this.blockModel;

        return berryBushState.growthStage == BerryBushState.GROWTH_STAGE_1 ? BlockModelList.seedModel : this.blockModel;
    }

    @Override
    public void addBlockStates(int x, int y, int z, World world, EntityPlayer player, Chunk chunk){
        int key = Chunk.getBlockIndexFromCoordinates(x,y,z);
        chunk.addBlockState(key, MultiState.BERRY_BUSH_STATE, new BerryBushState(BerryBushState.GROWTH_STAGE_1, false, false, key));
    }


    @Override
    public String getDisplayName(int x, int y, int z){
        BerryBushState berryBushState = (BerryBushState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return "null";


        return berryBushState.hasMatureFruit ? "Berry Bush (Ripe)" : berryBushState.isFlowering ? "Berry Bush (Flowering)" : this.displayName;
    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();

        ToolTip toolTip = new ToolTip();

        toolTip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        toolTip.addText("to harvest");
        toolTip.addItemID(ItemIDList.BERRY);

        this.tooltips[0][0].addToolTip(toolTip);
    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        BerryBushState berryBushState = (BerryBushState) world.getBlockState(x,y,z, MultiState.BERRY_BUSH_STATE);
        if(berryBushState == null)return null;

        //Return the tooltip only when the plant has harvestable fruit
        return berryBushState.hasMatureFruit ? this.tooltips[0] : null;
    }


}
