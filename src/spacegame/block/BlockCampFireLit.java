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
import spacegame.item.Item;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class BlockCampFireLit extends BlockCampFire implements ITickable, IParticleGenerator {
    public BlockCampFireLit(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        super.onLeftClick(x,y,z,world,player);
        if(player.isBlockSpawnPoint(x,y,z)){
            player.spawnX = (int) CosmicEvolution.instance.save.spawnX;
            player.spawnY = (int) CosmicEvolution.instance.save.spawnY;
            player.spawnZ = (int) CosmicEvolution.instance.save.spawnZ;
            GuiInGame.setMessageText("Spawn Point Destroyed", 16777215);
        }
    }
    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem == Item.unlitTorch.ID) {
            CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
            if (!CosmicEvolution.instance.save.thePlayer.addItemToInventory(Item.torch.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY)) {
                world.addEntity(new EntityItem(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, Item.torch.ID, Item.NULL_ITEM_METADATA, (byte) 1, Item.NULL_ITEM_DURABILITY));
                MouseListener.rightClickReleased = false;
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT]){
            player.spawnX = x;
            player.spawnY = y;
            player.spawnZ = z;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            GuiInGame.setMessageText("Spawn Point Set", 16777215);
        }
    }

    @Override
    public void generateParticles(int x, int y, int z) {
        double xPos = x + 0.5;
        double yPos = y;
        double zPos = z + 0.5;
        int particleCount = 5;
        EntityParticle particle;
        Chunk chunk = CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        for(int i = 0; i < particleCount; i++){
            particle = new EntityParticle(xPos + CosmicEvolution.globalRand.nextDouble(-0.125, 0.125), yPos + CosmicEvolution.globalRand.nextDouble(0.125), zPos + CosmicEvolution.globalRand.nextDouble(-0.125, 0.125), false, CosmicEvolution.globalRand.nextInt(30, 180), this.ID, false, false);
            chunk.addEntityToList(particle);
        }
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        this.generateParticles(x,y,z);
        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.fireCrackling, false, 1f), CosmicEvolution.globalRand.nextFloat(0.75f, 1));
    }

}
