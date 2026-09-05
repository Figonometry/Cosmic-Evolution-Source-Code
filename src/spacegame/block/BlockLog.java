package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.item.ItemAxe;
import spacegame.item.ItemIDList;
import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public abstract class BlockLog extends Block {
    public BlockLog(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        this.handleSpecialLeftClickFunctions(x,y,z, world, player);
        world.setBlockAndNotify(x,y,z, Block.air.ID, false);
        this.notifyNearbyLeafBlocks(x,y,z, world);
        player.reduceHeldItemDurability();
    }

    @Override
    protected void handleSpecialLeftClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.NULL_ITEM_REFERENCE && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            if(Item.list[playerHeldItem] instanceof ItemAxe){
                for(int i = 0; i < 4; i++){
                    world.addEntity(new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5, z + CosmicEvolution.globalRand.nextDouble(), Item.fireWood.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null));
                }
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }
        } else {
            if (list[world.getBlockID(x,y,z)].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityBlock(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[world.getBlockID(x,y,z)].itemMetadata, (byte) 1));
            }
        }
    }

    private void notifyNearbyLeafBlocks(int x, int y, int z, World world){
        int minBoxX = x - 1;
        int minBoxY = y - 1;
        int minBoxZ = z - 1;
        int maxBoxX = x + 1;
        int maxBoxY = y + 1;
        int maxBoxZ = z + 1;

        for(x = minBoxX; x <= maxBoxX; x++){
            for(y = minBoxY; y <= maxBoxY; y++){
                for(z = minBoxZ; z <= maxBoxZ; z++){
                    if(Block.list[world.getBlockID(x,y,z)] instanceof BlockLeaf){
                       world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addDecayableLeafToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
                    }
                }
            }
        }
    }


    public static int facingDirectionOfLog(int x, int y, int z, World world){
        LogState logState = (LogState) world.getBlockState(x,y,z, MultiState.LOG_STATE);

        return logState == null ? LogState.FACE_DIRECTION_TOP_AND_BOTTOM : logState.facingDirection;
    }

    public static int sizeOfLog(int x, int y, int z, World world){
        LogState logState = (LogState) world.getBlockState(x,y,z, MultiState.LOG_STATE);
        return logState == null ? 16 : logState.size;
    }

    @Override
    public ModelLoader getBlockModel(int x, int y, int z, World world){
        LogState logState = (LogState) world.getBlockState(x,y,z, MultiState.LOG_STATE);
        if(logState == null)return this.blockModel;

        switch (logState.facingDirection){
            case LogState.FACE_DIRECTION_TOP_AND_BOTTOM -> {
                switch (logState.size){
                    case 16 -> {
                        return this.blockModel;
                    }
                    case 15 -> {
                        return BlockModelList.size15NormalModel;
                    }
                    case 14 -> {
                        return BlockModelList.size14NormalModel;
                    }
                    case 13 -> {
                        return BlockModelList.size13NormalModel;
                    }
                    case 12 -> {
                        return BlockModelList.size12NormalModel;
                    }
                    case 11 -> {
                        return BlockModelList.size11NormalModel;
                    }
                    case 10 -> {
                        return BlockModelList.size10NormalModel;
                    }
                    case 9 -> {
                        return BlockModelList.size9NormalModel;
                    }
                    case 8 -> {
                        return BlockModelList.size8NormalModel;
                    }
                    case 7 -> {
                        return BlockModelList.size7NormalModel;
                    }
                    case 6 -> {
                        return BlockModelList.size6NormalModel;
                    }
                    case 5 -> {
                        return BlockModelList.size5NormalModel;
                    }
                    case 4 ->{
                        return BlockModelList.size4NormalModel;
                    }
                    case 3 ->{
                        return BlockModelList.size3NormalModel;
                    }
                    case 2 -> {
                        return BlockModelList.size2NormalModel;
                    }
                    case 1 -> {
                        return BlockModelList.size1NormalModel;
                    }
                }
            }
            case LogState.FACE_DIRECTION_NORTH_AND_SOUTH -> {
                switch (logState.size){
                    case 16 -> {
                        return this.blockModel;
                    }
                    case 15 -> {
                        return BlockModelList.size15NorthSouthModel;
                    }
                    case 14 -> {
                        return BlockModelList.size14NorthSouthModel;
                    }
                    case 13 -> {
                        return BlockModelList.size13NorthSouthModel;
                    }
                    case 12 -> {
                        return BlockModelList.size12NorthSouthModel;
                    }
                    case 11 -> {
                        return BlockModelList.size11NorthSouthModel;
                    }
                    case 10 -> {
                        return BlockModelList.size10NorthSouthModel;
                    }
                    case 9 -> {
                        return BlockModelList.size9NorthSouthModel;
                    }
                    case 8 -> {
                        return BlockModelList.size8NorthSouthModel;
                    }
                    case 7 -> {
                        return BlockModelList.size7NorthSouthModel;
                    }
                    case 6 -> {
                        return BlockModelList.size6NorthSouthModel;
                    }
                    case 5 -> {
                        return BlockModelList.size5NorthSouthModel;
                    }
                    case 4 ->{
                        return BlockModelList.size4NorthSouthModel;
                    }
                    case 3 ->{
                        return BlockModelList.size3NorthSouthModel;
                    }
                    case 2 -> {
                        return BlockModelList.size2NorthSouthModel;
                    }
                    case 1 -> {
                        return BlockModelList.size1NorthSouthModel;
                    }
                }
            }
            case LogState.FACE_DIRECTION_EAST_AND_WEST -> {
                switch (logState.size){
                    case 16 -> {
                        return this.blockModel;
                    }
                    case 15 -> {
                        return BlockModelList.size15EastWestModel;
                    }
                    case 14 -> {
                        return BlockModelList.size14EastWestModel;
                    }
                    case 13 -> {
                        return BlockModelList.size13EastWestModel;
                    }
                    case 12 -> {
                        return BlockModelList.size12EastWestModel;
                    }
                    case 11 -> {
                        return BlockModelList.size11EastWestModel;
                    }
                    case 10 -> {
                        return BlockModelList.size10EastWestModel;
                    }
                    case 9 -> {
                        return BlockModelList.size9EastWestModel;
                    }
                    case 8 -> {
                        return BlockModelList.size8EastWestModel;
                    }
                    case 7 -> {
                        return BlockModelList.size7EastWestModel;
                    }
                    case 6 -> {
                        return BlockModelList.size6EastWestModel;
                    }
                    case 5 -> {
                        return BlockModelList.size5EastWestModel;
                    }
                    case 4 ->{
                        return BlockModelList.size4EastWestModel;
                    }
                    case 3 ->{
                        return BlockModelList.size3EastWestModel;
                    }
                    case 2 -> {
                        return BlockModelList.size2EastWestModel;
                    }
                    case 1 -> {
                        return BlockModelList.size1EastWestModel;
                    }
                }
            }
        }

        return this.blockModel;
    }


    @Override
    public void addBlockStates(int x, int y, int z, short heldBlock, World world, EntityPlayer player, Chunk chunk){
        chunk.addBlockState(Chunk.getBlockIndexFromCoordinates(x,y,z), MultiState.LOG_STATE, new LogState(LogState.FACE_DIRECTION_TOP_AND_BOTTOM, 16, Chunk.getBlockIndexFromCoordinates(x,y,z)));
    }

    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[1][1];
        this.tooltips[0][0] = new ToolTipGroup();


        ToolTip leftClickWithShift = new ToolTip();
        leftClickWithShift.addKeyWithBoxOutline("SHIFT");
        leftClickWithShift.addText("+");
        leftClickWithShift.addMouseIcon(MouseAndKeyIconTextureList.LEFT_CLICK);
        leftClickWithShift.addText("to harvest");
        leftClickWithShift.addItemID(ItemIDList.FIREWOOD);


        this.tooltips[0][0].addToolTip(leftClickWithShift);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        return this.tooltips[0];
    }

}
