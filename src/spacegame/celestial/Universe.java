package spacegame.celestial;

import spacegame.render.TextureCubeMapLoader;
import spacegame.world.World;

public final class Universe {
    public Earth earth;
    public Moon moon;
    public Sun sun;
    public CelestialObject[] objects = new CelestialObject[3];
    public TextureCubeMapLoader[] cubeMaps = new TextureCubeMapLoader[this.objects.length];

    public Universe(){
        this.sun = new Sun(null,0, 0,0, 0, 0, 0, 0, 69520200, 0, CelestialObject.surfaceGravity(27.9), 216000, Long.MAX_VALUE, true, 1.9885e30, 1, 0, 0);
        this.earth = new Earth(this.sun, 14959802300L, 0,0, 0.016, 0, 0, 0, 637675, Math.PI * 2, CelestialObject.surfaceGravity(1), 216000, 0, false, 5.972168e24, 2, 23.4F,0); //216000
        this.moon = new Moon(this.earth, 38439900,  0,0, 0.0549,5.145F, 0, 0, 173810, Math.PI * 2, CelestialObject.surfaceGravity(0.1654), 6480000, 0, true, 7.7346e10, 3,1.5424F,0);
        this.objects[0] = this.sun;
        this.objects[1] = this.earth;
        this.objects[2] = this.moon;
        this.cubeMaps[0] = new TextureCubeMapLoader("src/spacegame/assets/textures/gui/guiUniverse/sun");
        this.cubeMaps[1] = new TextureCubeMapLoader("src/spacegame/assets/textures/gui/guiUniverse/earth");
        this.cubeMaps[2] = new TextureCubeMapLoader("src/spacegame/assets/textures/gui/guiUniverse/moon");
        this.objects[0].mappedTexture = this.cubeMaps[0].texID;
        this.objects[1].mappedTexture = this.cubeMaps[1].texID;
        this.objects[2].mappedTexture = this.cubeMaps[2].texID;
    }

    public void updateCelestialObjects(){
        this.earth.update();
        this.moon.update();
    }

    public CelestialObject getObjectAssociatedWithWorld(World world){
        return this.earth;
    }


}
