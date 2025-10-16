package spacegame.block;

import spacegame.core.Sound;
import spacegame.core.SoundPlayer;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityParticle;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class BlockPitKilnLit extends BlockPitKilnUnlit implements ITimeUpdate, ITickable, IParticleGenerator {
    public static final long updateTime = 90000;
    public BlockPitKilnLit(short ID, int textureID, String filepath, int inventorySize) {
        super(ID, textureID, filepath, inventorySize);
    }

    @Override
    public void generateParticles(int x, int y, int z) {
        double xPos = x + 0.5;
        double yPos = y + 1;
        double zPos = z + 0.5;
        int particleCount = 5;
        EntityParticle particle;
        Chunk chunk = SpaceGame.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        for(int i = 0; i < particleCount; i++){
            particle = new EntityParticle(xPos + SpaceGame.globalRand.nextDouble(-0.125, 0.125), yPos + SpaceGame.globalRand.nextDouble(0.125), zPos + SpaceGame.globalRand.nextDouble(-0.125, 0.125), false, SpaceGame.globalRand.nextInt(30, 180), Block.campfireLit.ID, false);
            chunk.addEntityToList(particle);
        }
    }


    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        short blockID = world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).getChestLocation(x,y,z).inventory.itemStacks[0].metadata;
        if(blockID == Block.rawRedClayCookingPot.ID){
            world.setBlockWithNotify(x,y,z, Block.redClayCookingPot.ID);
        }
        world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeChestLocation((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        this.generateParticles(x, y, z);
        new SoundPlayer(SpaceGame.instance).playSound(x, y, z, new Sound(Sound.fireCrackling, false), SpaceGame.globalRand.nextFloat(0.75f, 1));
    }
}
