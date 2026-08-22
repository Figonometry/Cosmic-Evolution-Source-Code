package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.*;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityParticle;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiInGame;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.*;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.CampfireState;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.HeatableBlockLocation;
import spacegame.world.blockstate.MultiState;

import java.util.Random;

public final class BlockCampFire extends BlockHeating implements ITickable, IParticleGenerator, IBurnDamage {


    public BlockCampFire(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }



    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased || CosmicEvolution.instance.save.time - MouseListener.lastTimeRightClicked < Timer.REAL_SECOND) return;

        CampfireState campfireState = (CampfireState) world.getBlockState(x,y,z, MultiState.CAMPFIRE_STATE);

        if(campfireState == null)return;

        short playerHeldItem = player.getHeldItem();
        short playerHeldBlock = player.getHeldBlock();
        if (playerHeldItem != Item.NULL_ITEM_REFERENCE && playerHeldItem == Item.fireWood.ID) {
            int logCount = campfireState.logCount;
            switch (logCount) {
                case 0, 2, 1 -> campfireState.logCount++;
                case 3 -> {
                    campfireState.logCount++;

                    ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);

                    chestLocation.inventory.itemStacks[1].item = Item.fireWood;
                    chestLocation.inventory.itemStacks[1].metadata = Item.NULL_ITEM_METADATA;
                    chestLocation.inventory.itemStacks[1].count = 4;

                    MouseListener.rightButtonClicked();
                }
            }
            if (logCount != 4) {
                player.removeItemFromInventory();
                CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
            }
            world.notifyChunk(x,y,z);
            return;
        }

        if (campfireState.logCount == 4) {
            if (playerHeldItem == Item.stoneFragments.ID || player.getHeldBlock() == Block.torch.ID) {
                campfireState.isLit = true;
                world.propagateLightSource(x,y,z, this.lightBlockValue);
                world.addBlockState(x,y,z, MultiState.HEATABLE_BLOCK_STATE, new HeatableBlockLocation(Chunk.getBlockIndexFromCoordinates(x,y,z)));
                world.notifyChunk(x,y,z);
            }
        }


        if(campfireState.cookingStickCount < 5 && playerHeldBlock == Block.itemStick.ID){
            campfireState.cookingStickCount++;
            player.removeItemFromInventory();
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
            world.notifyChunk(x,y,z);
        }



        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT] && playerHeldBlock == Block.torchUnlit.ID) {
            CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
            if (!CosmicEvolution.instance.save.thePlayer.addItemToInventory(Item.block.ID, Block.torch.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0, null)) {
                world.addEntity(new EntityItem(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, Item.block.ID, Block.torch.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0, null));
            }
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT] && campfireState.isLit){
            player.spawnX = x;
            player.spawnY = y;
            player.spawnZ = z;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            GuiInGame.setMessageText("Spawn Point Set", 16777215);
        }

        ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);

        if(playerHeldItem != Item.NULL_ITEM_REFERENCE) {
            if (Item.list[playerHeldItem] instanceof ItemRawGameMeat && chestLocation.inventory.itemStacks[0].item == null && campfireState.cookingStickCount == 5) {

                chestLocation.inventory.itemStacks[0].item = Item.list[playerHeldItem];
                chestLocation.inventory.itemStacks[0].itemState = player.getHeldItemState();
                chestLocation.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chestLocation.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                chestLocation.inventory.itemStacks[0].decayTime = player.getHeldItemDecayTime();
                chestLocation.inventory.itemStacks[0].count = 1;

                player.removeItemFromInventory();
                world.notifyChunk(x,y,z);
                MouseListener.rightButtonClicked();
                return;
            }


            if (Item.list[playerHeldItem] instanceof IFuel && chestLocation.inventory.itemStacks[1].count < 64) {

                chestLocation.inventory.itemStacks[1].item = Item.list[playerHeldItem];
                chestLocation.inventory.itemStacks[1].itemState = player.getHeldItemState();
                chestLocation.inventory.itemStacks[1].metadata = Item.NULL_ITEM_METADATA;
                chestLocation.inventory.itemStacks[1].durability = Item.NULL_ITEM_DURABILITY;
                chestLocation.inventory.itemStacks[1].count++;

                player.removeItemFromInventory();
                world.notifyChunk(x,y,z);
                MouseListener.rightButtonClicked();
                return;
            }
        }

        if(chestLocation.inventory.itemStacks[0].item != null) {
            if (playerHeldItem == Item.NULL_ITEM_REFERENCE || chestLocation.inventory.itemStacks[0].item.ID == playerHeldItem) {
                player.addItemToInventory(chestLocation.inventory.itemStacks[0].item.ID, chestLocation.inventory.itemStacks[0].metadata,
                        chestLocation.inventory.itemStacks[0].count, chestLocation.inventory.itemStacks[0].durability, chestLocation.inventory.itemStacks[0].decayTime,
                        chestLocation.inventory.itemStacks[0].itemState);


                chestLocation.inventory.itemStacks[0].item = null;
                chestLocation.inventory.itemStacks[0].itemState = null;
                chestLocation.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chestLocation.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                chestLocation.inventory.itemStacks[0].decayTime = 0L;
                chestLocation.inventory.itemStacks[0].count = 0;
                world.notifyChunk(x,y,z);
            }
        }





        MouseListener.rightButtonClicked();
    }

    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        super.onLeftClick(x,y,z,world,player);
        if(player.isBlockSpawnPoint(x,y,z)){
            player.spawnX = CosmicEvolution.instance.save.spawnX;
            player.spawnY = CosmicEvolution.instance.save.spawnY;
            player.spawnZ = CosmicEvolution.instance.save.spawnZ;
            GuiInGame.setMessageText("Spawn Point Destroyed", 16777215);
        }
    }

    @Override
    public void generateParticles(int x, int y, int z) {
        double xPos = x + 0.5;
        double yPos = y;
        double zPos = z + 0.5;
        int particleCount = CosmicEvolution.globalRand.nextInt(1,5);
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
        CampfireState campfireState = (CampfireState) world.getBlockState(x,y,z, MultiState.CAMPFIRE_STATE);
        if(campfireState == null)return;
        if(!campfireState.isLit)return;


        this.generateParticles(x,y,z);
        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.fireCrackling, false, 1f), CosmicEvolution.globalRand.nextFloat(0.75f, 1));


        HeatableBlockLocation heatableBlockLocation = (HeatableBlockLocation) world.getBlockState(x,y,z, MultiState.HEATABLE_BLOCK_STATE);
        ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
        if(heatableBlockLocation == null || chestLocation == null)return;
        if(chestLocation.inventory.itemStacks[0].item == null)return;
        if(!heatableBlockLocation.heating)return;
        if(CosmicEvolution.instance.save.time > heatableBlockLocation.heatingFinishTime)return;

        double xPos = x + 0.5;
        double yPos = y;
        double zPos = z + 0.5;
        int particleCount = CosmicEvolution.globalRand.nextInt(1,5);
        EntityParticle particle;
        Chunk chunk = CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        for(int i = 0; i < particleCount; i++){
            particle = new EntityParticle(xPos + CosmicEvolution.globalRand.nextDouble(-0.125, 0.125), yPos + CosmicEvolution.globalRand.nextDouble(0.125), zPos + CosmicEvolution.globalRand.nextDouble(-0.125, 0.125), false, CosmicEvolution.globalRand.nextInt(120, 240),  Block.fire.ID, false, false, false, true, 1, 1);
            particle.size *= CosmicEvolution.globalRand.nextFloat(1f, 5f);
            chunk.addEntityToList(particle);
        }
    }

    @Override
    public boolean isLightBlock(int x, int y, int z, World world){
        CampfireState campfireState = (CampfireState) world.getBlockState(x,y,z, MultiState.CAMPFIRE_STATE);
        if(campfireState == null)return false;

        return campfireState.isLit;
    }

    @Override
    public boolean canDamage(int x, int y, int z, World world) {
        CampfireState campfireState = (CampfireState)world.getBlockState(x,y,z, MultiState.CAMPFIRE_STATE);
        return campfireState != null && campfireState.isLit;
    }

    @Override
    public void addBlockStates(int x, int y, int z, World world, EntityPlayer player, Chunk chunk){
        world.addBlockState(x,y,z, MultiState.CHEST_STATE , new ChestLocation(Chunk.getBlockIndexFromCoordinates(x,y,z), new Inventory(1, 2), world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5)));
        world.addBlockState(x,y,z, MultiState.CAMPFIRE_STATE, new CampfireState(Chunk.getBlockIndexFromCoordinates(x,y,z), false, 0,0));
    }


    @Override
    public void registerBlockTooltips(){
        //In order of unlit, and lit, unlit/lit will have to be duplicated and placed into the array twice
        this.tooltips = new ToolTipGroup[5][2];

        for(int i = 0; i < this.tooltips.length; i++){
            for(int k = 0; k < this.tooltips[i].length; k++){
                this.tooltips[i][k] = new ToolTipGroup();
            };
        }

        ToolTip lightWithRockTooltip = new ToolTip();

        lightWithRockTooltip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        lightWithRockTooltip.addText("with");
        lightWithRockTooltip.addItemID(ItemIDList.STONE_FRAGMENTS);
        lightWithRockTooltip.addText("to light fire");


        ToolTip lightWithTorchTooltip = new ToolTip();

        lightWithRockTooltip.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        lightWithRockTooltip.addText("with");
        lightWithRockTooltip.addBlockID(BlockIDList.TORCH);
        lightWithRockTooltip.addText("to light fire");



        ToolTip rightClickWithFireWood = new ToolTip();

        rightClickWithFireWood.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        rightClickWithFireWood.addText("with");
        rightClickWithFireWood.addItemID(ItemIDList.FIREWOOD);
        rightClickWithFireWood.addText("to add fuel");


        ToolTip rightClickWithSticks = new ToolTip();

        rightClickWithSticks.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        rightClickWithSticks.addText("with");
        rightClickWithSticks.addBlockID(BlockIDList.ITEM_STICK);
        rightClickWithSticks.addText("to build cooking setup");


        ToolTip cookingFood = new ToolTip();

        cookingFood.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        cookingFood.addText("with");
        cookingFood.addItemID(ItemIDList.RAW_GAME_MEAT);
        cookingFood.addText("to cook");


        ToolTip craftingTorch = new ToolTip();

        craftingTorch.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        craftingTorch.addText("with");
        craftingTorch.addItemID(BlockIDList.TORCH_UNLIT);
        craftingTorch.addText("to craft");
        craftingTorch.addItemID(BlockIDList.TORCH);


        ToolTip setSpawn = new ToolTip();

        setSpawn.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        setSpawn.addText("to set your spawn point");


        this.tooltips[0][0].addToolTip(lightWithRockTooltip);
        this.tooltips[0][0].addToolTip(lightWithTorchTooltip);

        this.tooltips[1][0].addToolTip(rightClickWithFireWood);

        this.tooltips[2][0].addToolTip(rightClickWithSticks);
        this.tooltips[2][0].addToolTip(setSpawn);
        this.tooltips[2][0].addToolTip(craftingTorch);

        this.tooltips[3][0].addToolTip(cookingFood);
        this.tooltips[3][0].addToolTip(setSpawn);
        this.tooltips[3][0].addToolTip(craftingTorch);

        this.tooltips[4][0].addToolTip(setSpawn);
        this.tooltips[4][0].addToolTip(craftingTorch);

    }


    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        CampfireState campfireState = (CampfireState) world.getBlockState(x,y,z, MultiState.CAMPFIRE_STATE);
        if(campfireState == null)return null;

        if(!campfireState.isLit){
            if(campfireState.logCount == 4){
                return this.tooltips[0];
            }
            if(campfireState.logCount < 4){
               return this.tooltips[1];
            }
        } else { //This is lit
            if(campfireState.cookingStickCount < 5){
                return this.tooltips[2];
            }

            if(campfireState.cookingStickCount == 5){
                ChestLocation inventoryAtCampfire = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
                if(inventoryAtCampfire == null) {
                   return this.tooltips[4];
                }

                if(inventoryAtCampfire.inventory.itemStacks[0].item == null){
                    return this.tooltips[3];
                }
            }

            return this.tooltips[4];
        }


        return null;
    }
}
