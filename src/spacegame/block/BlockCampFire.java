package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityParticle;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiInGame;
import spacegame.item.IFuel;
import spacegame.item.IHeatable;
import spacegame.item.Item;
import spacegame.item.ItemRawGameMeat;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.CampfireState;
import spacegame.world.blockstate.HeatableBlockLocation;

import java.util.Random;

public final class BlockCampFire extends BlockHeating implements ITickable, IParticleGenerator {


    public BlockCampFire(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }



    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player) {
        if (!MouseListener.rightClickReleased) return;

        CampfireState campfireState = world.getCampfireState(x,y,z);

        if(campfireState == null)return;

        short playerHeldItem = player.getHeldItem();
        short playerHeldBlock = player.getHeldBlock();
        if (playerHeldItem != Item.NULL_ITEM_REFERENCE && playerHeldItem == Item.fireWood.ID) {
            int logCount = campfireState.logCount;
            switch (logCount) {
                case 0, 2, 1 -> campfireState.logCount++;
                case 3 -> {
                    campfireState.logCount++;

                    ChestLocation chestLocation = world.getChestLocation(x,y,z);

                    chestLocation.inventory.itemStacks[1].item = Item.fireWood;
                    chestLocation.inventory.itemStacks[1].metadata = Item.NULL_ITEM_METADATA;
                    chestLocation.inventory.itemStacks[1].count = 4;

                    MouseListener.rightClickReleased = false;
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
            if (playerHeldItem == Item.stoneFragments.ID || player.getHeldBlock() == Block.torchStandard.ID) {
                campfireState.isLit = true;
                world.propagateLightSource(x,y,z, this.lightBlockValue);
                world.addHeatableBlock(x,y,z);
                world.notifyChunk(x,y,z);
            }
        }


        if(campfireState.cookingStickCount < 5 && playerHeldBlock == Block.itemStick.ID){
            campfireState.cookingStickCount++;
            player.removeItemFromInventory();
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
            world.notifyChunk(x,y,z);
        }



        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT] && playerHeldBlock == Block.torchStandardUnlit.ID) {
            CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
            if (!CosmicEvolution.instance.save.thePlayer.addItemToInventory(Item.block.ID, Block.torchStandard.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0, null)) {
                world.addEntity(new EntityItem(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, Item.block.ID, Block.torchStandard.ID, (byte) 1, Item.NULL_ITEM_DURABILITY, 0, null));
            }
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT]){
            player.spawnX = x;
            player.spawnY = y;
            player.spawnZ = z;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            GuiInGame.setMessageText("Spawn Point Set", 16777215);
        }

        ChestLocation chestLocation = world.getChestLocation(x,y,z);

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
                MouseListener.rightClickReleased = false;
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
                MouseListener.rightClickReleased = false;
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





        MouseListener.rightClickReleased = false;
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
        CampfireState campfireState = world.getCampfireState(x,y,z);
        if(campfireState == null)return;
        if(!campfireState.isLit)return;


        this.generateParticles(x,y,z);
        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.fireCrackling, false, 1f), CosmicEvolution.globalRand.nextFloat(0.75f, 1));


        HeatableBlockLocation heatableBlockLocation = world.getHeatableBlock(x,y,z);
        ChestLocation chestLocation = world.getChestLocation(x,y,z);
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
        CampfireState campfireState = world.getCampfireState(x,y,z);
        if(campfireState == null)return false;

        return campfireState.isLit;
    }

}
