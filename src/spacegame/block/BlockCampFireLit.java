package spacegame.block;

import spacegame.core.Sound;
import spacegame.core.SoundPlayer;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityParticle;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class BlockCampFireLit extends BlockCampFire implements ITickable, IParticleGenerator {
    public BlockCampFireLit(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void generateParticles(int x, int y, int z) {
        double xPos = x + 0.5;
        double yPos = y;
        double zPos = z + 0.5;
        int particleCount = 5;
        EntityParticle particle;
        Chunk chunk = SpaceGame.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        for(int i = 0; i < particleCount; i++){
            particle = new EntityParticle(xPos + SpaceGame.globalRand.nextDouble(-0.125, 0.125), yPos + SpaceGame.globalRand.nextDouble(0.125), zPos + SpaceGame.globalRand.nextDouble(-0.125, 0.125), false, SpaceGame.globalRand.nextInt(30, 180), this.ID, false);
            chunk.addEntityToList(particle);
        }
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        this.generateParticles(x,y,z);
        new SoundPlayer(SpaceGame.instance).playSound(x, y, z, new Sound(Sound.fireCrackling, false), SpaceGame.globalRand.nextFloat(0.75f, 1));
    }

}
