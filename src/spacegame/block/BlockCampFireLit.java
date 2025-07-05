package spacegame.block;

import spacegame.core.SpaceGame;
import spacegame.entity.EntityParticle;
import spacegame.world.Chunk;

public final class BlockCampFireLit extends Block implements ITickable, IParticleGenerator {
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
        Chunk chunk = SpaceGame.instance.save.activeWorld.activeWorldFace.chunkController.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        for(int i = 0; i < particleCount; i++){
            particle = new EntityParticle(xPos + rand.nextDouble(-0.125, 0.125), yPos + rand.nextDouble(0.125), zPos + rand.nextDouble(-0.125, 0.125), false, rand.nextInt(30, 180), this.ID, false);
            chunk.addEntityToList(particle);
        }
    }

    @Override
    public void tick(int x, int y, int z) {
        this.generateParticles(x,y,z);
        this.reduceFireLightLevel(x,y,z);
    }

    private void reduceFireLightLevel(int x, int y, int z){
        if(SpaceGame.instance.save.time % 7200 != 0){return;}
        SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, air.ID);
        switch (this.lightBlockValue) {
            case 14 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight13.ID);
            case 13 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight12.ID);
            case 12 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight11.ID);
            case 11 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight10.ID);
            case 10 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight9.ID);
            case 9 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight8.ID);
            case 8 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight7.ID);
            case 7 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight6.ID);
            case 6 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight5.ID);
            case 5 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight4.ID);
            case 4 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight3.ID);
            case 3 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight2.ID);
            case 2 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireLitLight1.ID);
            case 1 ->
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, campFireBurnedOut.ID);
        }
    }
}
