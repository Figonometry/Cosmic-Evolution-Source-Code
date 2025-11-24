package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.core.Timer;
import spacegame.entity.EntityParticle;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.world.ChestLocation;
import spacegame.world.Chunk;
import spacegame.world.Tech;
import spacegame.world.World;

public final class BlockPitKilnLit extends BlockPitKilnUnlit implements ITimeUpdate, ITickable, IParticleGenerator {
    public BlockPitKilnLit(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
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
            particle = new EntityParticle(xPos + CosmicEvolution.globalRand.nextDouble(-0.5, 0.5), yPos + CosmicEvolution.globalRand.nextDouble(0.125), zPos + CosmicEvolution.globalRand.nextDouble(-0.5, 0.5), false, CosmicEvolution.globalRand.nextInt(30, 180), Block.campfireLit.ID, false, false);
            chunk.addEntityToList(particle);
        }
    }


    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        ChestLocation chestLocation = world.getChestLocation(x,y,z);
        short itemID = chestLocation.inventory.itemStacks[0].item.ID;
        short blockID = chestLocation.inventory.itemStacks[0].metadata;
        byte itemQuantity = chestLocation.inventory.itemStacks[0].count;
        chestLocation.inventory.itemStacks[0].count = 0;
        chestLocation.inventory.itemStacks[0].item = null;
        chestLocation.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
        chestLocation.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;

        Tech.techUpdateEvent(Tech.UPDATE_EVENT_COOK_CLAY);

        world.removeChestLocation(x,y,z);

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
            world.addChestLocation(x,y,z, pileInventory);
        }
    }

    @Override
    public long getUpdateTime() {
        return 10 * Timer.GAME_HOUR; //10 in game hours
    }

    @Override
    public String getDisplayStringText() {
        return "Time left: ";
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        this.generateParticles(x, y, z);
        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.fireCrackling, false, 1f), CosmicEvolution.globalRand.nextFloat(0.75f, 1));
    }
}
