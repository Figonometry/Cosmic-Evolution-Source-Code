package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.*;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityParticle;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.PitKilnState;

import java.util.Random;

public final class BlockPitKiln extends BlockContainer implements ITimeUpdate, ITickable, IParticleGenerator, IBurnDamage {
    public BlockPitKiln(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;;
        short playerHeldItem = player.getHeldItem();

        PitKilnState pitKilnState = (PitKilnState) world.getBlockState(x,y,z, MultiState.PIT_KILN_STATE);
        if(pitKilnState == null)return;


        if((playerHeldItem == Item.straw.ID || playerHeldItem == Item.fireWood.ID) && world.isBlockSuitableForPitKiln(x,y,z) && MouseListener.rightClickReleased){

            if(playerHeldItem == Item.straw.ID && pitKilnState.strawCount < 8){ //Adds straw
                pitKilnState.strawCount++;
                player.removeItemFromInventory();
                CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.grass, false, 1f), new Random().nextFloat(0.6F, 1));
            }

            if(playerHeldItem == Item.fireWood.ID && pitKilnState.logCount < 4){ //Adds logs
                pitKilnState.logCount++;
                player.removeItemFromInventory();
                CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
            }

            world.notifyChunk(x,y,z);
            MouseListener.rightClickReleased = false;
            return;
        }


        if(pitKilnState.isPitKilnComplete() && player.getHeldBlock() == Block.torch.ID && KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)){
            world.addTimeEvent(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime(x,y,z,world));

            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
            } else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }

            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addTickableBlockToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        }
    }

    public int getStrawHeight(PitKilnState pitKilnState) {
        return pitKilnState == null ? 0 : pitKilnState.logCount;
    }

    public int getNumberOfLogs(PitKilnState pitKilnState) {
        return pitKilnState == null ? 0 : pitKilnState.strawCount;
    }

    @Override
    public void generateParticles(int x, int y, int z) {
        double xPos = x + 0.5;
        double yPos = y + 1;
        double zPos = z + 0.5;
        int particleCount = 5;
        EntityParticle particle;
        Chunk chunk = CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        for(int i = 0; i < particleCount; i++){
            particle = new EntityParticle(xPos + CosmicEvolution.globalRand.nextDouble(-0.125, 0.125), yPos + CosmicEvolution.globalRand.nextDouble(0.125), zPos + CosmicEvolution.globalRand.nextDouble(-0.125, 0.125), false, CosmicEvolution.globalRand.nextInt(120, 240),  Block.fire.ID, false, false, false, true, CosmicEvolution.globalRand.nextInt(31), CosmicEvolution.globalRand.nextInt(15,31));
            particle.size *= CosmicEvolution.globalRand.nextFloat(1f, 5f);
            chunk.addEntityToList(particle);
        }
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        this.generateParticles(x, y, z);
        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.fireCrackling, false, 1f), CosmicEvolution.globalRand.nextFloat(0.75f, 1));
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
        short itemID = chestLocation.inventory.itemStacks[0].item.ID;
        short blockID = chestLocation.inventory.itemStacks[0].metadata;
        byte itemQuantity = chestLocation.inventory.itemStacks[0].count;
        chestLocation.inventory.itemStacks[0].count = 0;
        chestLocation.inventory.itemStacks[0].item = null;
        chestLocation.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
        chestLocation.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;


        world.removeBlockState(x,y,z, MultiState.CHEST_STATE);

        if(itemID == Item.block.ID){
            if(blockID == Block.rawRedClayCookingPot.ID) {
                world.setBlockWithNotify(x, y, z, Block.redClayCookingPot.ID, false);
            }
        }

        if(itemID == Item.rawClayAdobeBrick.ID){
            world.setBlockWithNotify(x,y,z, Block.brickPile.ID, false);
            Inventory pileInventory = new Inventory(1,1);
            pileInventory.itemStacks[0].item = Item.firedRedClayAdobeBrick;
            pileInventory.itemStacks[0].count = itemQuantity;
            pileInventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
            pileInventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;

            ChestLocation chestLocation1 = new ChestLocation(Chunk.getBlockIndexFromCoordinates(x,y,z), pileInventory, world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5));
            world.addBlockState(x,y,z, MultiState.CHEST_STATE, chestLocation1);
        }
    }

    @Override
    public long getUpdateTime(int x, int y, int z, World world) {
        return 10 * Timer.GAME_HOUR; //10 in game hours
    }

    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        return "Time left: ";
    }
    @Override
    public boolean isLightBlock(int x, int y, int z, World world){
       PitKilnState pitKilnState = (PitKilnState) world.getBlockState(x,y,z, MultiState.PIT_KILN_STATE);
       return pitKilnState != null && pitKilnState.isLit;
    }

    @Override
    public boolean canDamage(int x, int y, int z, World world) {
        PitKilnState pitKilnState = (PitKilnState) world.getBlockState(x,y,z, MultiState.PIT_KILN_STATE);
        return pitKilnState != null && pitKilnState.isLit;
    }


    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[3][1];
        this.tooltips[0][0] = new ToolTipGroup();
        this.tooltips[1][0] = new ToolTipGroup();
        this.tooltips[2][0] = new ToolTipGroup();


        ToolTip addStraw = new ToolTip();
        addStraw.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        addStraw.addText("with");
        addStraw.addItemID(ItemIDList.STRAW);


        ToolTip addLogs = new ToolTip();
        addLogs.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        addLogs.addText("with");
        addLogs.addItemID(ItemIDList.FIREWOOD);

        ToolTip light = new ToolTip();
        light.addKeyWithBoxOutline("SHIFT");
        light.addText("+");
        light.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        light.addText("with");
        light.addBlockID(BlockIDList.TORCH);
        light.addText("to light");


        this.tooltips[0][0].addToolTip(addStraw);
        this.tooltips[1][0].addToolTip(addLogs);
        this.tooltips[2][0].addToolTip(light);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        PitKilnState pitKilnState = (PitKilnState) world.getBlockState(x,y,z, MultiState.PIT_KILN_STATE);
        if(pitKilnState == null)return null;


        if(!pitKilnState.isLit){
            if(pitKilnState.strawCount < 8){
                //show to add straw
            }

            if(pitKilnState.logCount < 4){
                //Show to add logs
            }

            //Show to light with torch
        }

        return null;
    }
}
