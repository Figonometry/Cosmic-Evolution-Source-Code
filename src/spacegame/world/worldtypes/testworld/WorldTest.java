package spacegame.world.worldtypes.testworld;

import spacegame.core.CosmicEvolution;
import spacegame.world.worldtypes.ChunkTerrainHandler;
import spacegame.world.worldtypes.World;

import java.io.File;


//Meant for testing new blocks, allows me to quickly load into and out of, end user should never see this since it'll be compiled in a dev environment
public final class WorldTest extends World {
    public WorldTest(CosmicEvolution cosmicEvolution, int size) {
        super(cosmicEvolution, size);
        this.skyLightLevel = 15;
        this.skyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.defaultSkyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.skyLightColor = new float[]{1, 1, 1, 0}; //ANY COLOR CANNOT BE 0
        this.worldFolder = new File(this.ce.save.saveFolder + "/worlds/worldTest");
        if(!this.worldFolder.exists()){
            this.worldFolder.mkdirs();
        }
    }

    @Override
    public void initNoiseMaps() {
        World.worldLoadPhase = 1;
    }

    @Override
    public void saveWorld() {
        this.chunkController.saveAllRegions();
    }

    @Override
    public void saveWorldWithoutUnload() {
        this.chunkController.saveAllRegionsWithoutUnload();
    }

    @Override
    public void loadWorld() {

    }


    @Override
    public void tick() {
        this.chunkController.tick();
        if (this.delayWhenExitingUI > 0) {
            this.delayWhenExitingUI--;
        }
        if(!this.paused) {
            super.tick();
        }
    }

    @Override
    public void loadGeologicProvinces() {

    }

    @Override
    public ChunkTerrainHandler getChunkTerrainHandler() {
        return new ChunkTestWorldHandler(this.chunkController, this);
    }
}
