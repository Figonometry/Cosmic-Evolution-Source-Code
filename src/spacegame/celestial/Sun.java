package spacegame.celestial;

import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.ShadowMap;

public final class Sun extends CelestialObject{
    public static int sunFlare;
    public ShadowMap shadowMap;
    public int lightColor;

    public Sun(CelestialObject parentObject, double semiMajorAxis, double apoapsis, double periapsis, double eccentricity, float incliniation, float argumentofPeriapsis, float longitudeOfAscendingNode, long radius, double meanAnomaly, double surfaceGravity, long rotationPeriod, long sphereOfInfluence, boolean tidallyLocked, double mass, int layer, float axialTiltX, float axialTiltZ, int lightColor) {
        super(parentObject, semiMajorAxis, apoapsis, periapsis, eccentricity, incliniation, argumentofPeriapsis, longitudeOfAscendingNode, radius, meanAnomaly, surfaceGravity, rotationPeriod, sphereOfInfluence, tidallyLocked, mass, layer, axialTiltX, axialTiltZ);
        this.lightColor = lightColor;
        this.createShadowMap();
    }

    public static void initSunFlare(){
        sunFlare = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiUniverse/sun/sunHalo.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    public void createShadowMap(){
        this.shadowMap = new ShadowMap(32768, 32768);
    }
}
