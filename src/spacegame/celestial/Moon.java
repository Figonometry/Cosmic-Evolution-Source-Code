package spacegame.celestial;

public final class Moon extends CelestialObject {


    public Moon(CelestialObject parentObject, double semiMajorAxis, double apoapsis, double periapsis, double eccentricity, float incliniation, float argumentofPeriapsis, float longitudeOfAscendingNode, long radius, double meanAnomaly, double surfaceGravity, long rotationPeriod, long sphereOfInfluence, boolean tidallyLocked, double mass, int layer, float axialTiltX, float axialTiltZ) {
        super(parentObject, semiMajorAxis, apoapsis, periapsis, eccentricity, incliniation, argumentofPeriapsis, longitudeOfAscendingNode, radius, meanAnomaly, surfaceGravity, rotationPeriod, sphereOfInfluence, tidallyLocked, mass, layer, axialTiltX, axialTiltZ);
    }
}
