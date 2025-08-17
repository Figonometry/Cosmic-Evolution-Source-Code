package spacegame.celestial;

import spacegame.core.SpaceGame;
import spacegame.render.RenderEngine;
import spacegame.render.ShadowMap;

public final class Sun extends CelestialObject{
    public static int sunFlare;
    public ShadowMap shadowMap;

    public Sun(CelestialObject parentObject, double semiMajorAxis, double apoapsis, double periapsis, double eccentricity, float incliniation, float argumentofPeriapsis, float longitudeOfAscendingNode, long radius, double meanAnomaly, double surfaceGravity, long rotationPeriod, long sphereOfInfluence, boolean tidallyLocked, double mass, int layer, float axialTiltX, float axialTiltZ) {
        super(parentObject, semiMajorAxis, apoapsis, periapsis, eccentricity, incliniation, argumentofPeriapsis, longitudeOfAscendingNode, radius, meanAnomaly, surfaceGravity, rotationPeriod, sphereOfInfluence, tidallyLocked, mass, layer, axialTiltX, axialTiltZ);
        this.createShadowMap();
    }

    public static void initSunFlare(){
        sunFlare = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiUniverse/sun/sunHalo.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    public void createShadowMap(){
        this.shadowMap = new ShadowMap(32768, 32768);
    }
}
