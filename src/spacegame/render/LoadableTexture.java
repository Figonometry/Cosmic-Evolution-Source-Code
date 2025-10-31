package spacegame.render;

import spacegame.celestial.CelestialObject;

public final class LoadableTexture {
    public String filepath;
    public int textureType;
    public int arraySize;
    public CelestialObject object;
    public LoadableTexture(String filepath, int textureType, int arraySize, CelestialObject object){
        this.filepath = filepath;
        this.textureType = textureType;
        this.arraySize = arraySize;
        this.object = object;
    }
}
