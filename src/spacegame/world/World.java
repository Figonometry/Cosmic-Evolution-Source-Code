package spacegame.world;

import spacegame.core.SpaceGame;

import java.io.File;

public abstract class World {
    public SpaceGame sg;
    public WorldFace activeWorldFace;
    public byte skyLightLevel;
    public float[] skyColor; //used for glClear on the color buffer
    public float[] defaultSkyColor;
    public float[] skyLightColor; //Do not make any RGB component 0
    public final int size;
    public static int worldLoadPhase = 0;
    public static int noiseMapsCompleted = 0;
    public static int totalMaps;
    public File worldFolder;

    public World(SpaceGame spaceGame, int size) {
        this.sg = spaceGame;
        this.size = size;
    }

    public void tick() {
        this.activeWorldFace.tick();
    }

    public void handleLeftClick() {
        this.activeWorldFace.handleLeftClick();
    }

    public void handleRightClick() {
        this.activeWorldFace.handleRightClick();
    }

    public abstract void initNoiseMaps();

    public void renderWorld() {
        this.activeWorldFace.renderWorld();
    }

    public abstract void saveWorld();

    public abstract void saveWorldWithoutUnload();

    public abstract void loadWorld();

}
