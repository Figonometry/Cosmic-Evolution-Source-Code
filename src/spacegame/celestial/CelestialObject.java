package spacegame.celestial;

import org.joml.Matrix3d;
import org.joml.Vector3d;
import spacegame.core.CosmicEvolution;
import spacegame.core.MathUtil;
import spacegame.render.RenderCelestialBody;

public abstract class CelestialObject {
    public final CelestialObject parentObject;
    public final double semiMajorAxis;
    public final double apoapsis;
    public final double periapsis;
    public final double eccentricity;
    public final float inclination;
    public final float argumentOfPeriapsis;
    public final float longitudeOfAscendingNode;
    public final long radius;
    public final double meanAnomaly;
    public final double surfaceGravity;
    public final long rotationPeriod;
    public final long sphereOfInfluence;
    public final boolean tidallyLocked;
    public final long orbitalPeriod;
    public final double mass;
    public final double semiMinorAxis;
    public final int layer;
    public final float axialTiltX;
    public final float axialTiltZ;
    public Vector3d position = new Vector3d();
    public Matrix3d rotation = new Matrix3d();
    public double altitude;
    public double focalDistance;
    public double orbitalVelocity;
    public int mappedTexture;

    public CelestialObject(CelestialObject parentObject, double semiMajorAxis, double apoapsis, double periapsis, double eccentricity, float inclination, float argumentOfPeriapsis, float longitudeOfAscendingNode, long radius, double meanAnomaly, double surfaceGravity, long rotationPeriod, long sphereOfInfluence, boolean tidallyLocked, double mass, int layer, float axialTiltX, float axialTiltZ){
        this.parentObject = parentObject;
        this.semiMajorAxis = semiMajorAxis;
        this.apoapsis = apoapsis;
        this.periapsis = periapsis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.argumentOfPeriapsis = argumentOfPeriapsis;
        this.longitudeOfAscendingNode = longitudeOfAscendingNode;
        this.radius = radius;
        this.meanAnomaly = meanAnomaly;
        this.surfaceGravity = surfaceGravity;
        this.sphereOfInfluence = sphereOfInfluence;
        this.tidallyLocked = tidallyLocked;
        this.mass = mass;
        this.layer = layer;
        this.axialTiltX = axialTiltX;
        this.axialTiltZ = axialTiltZ;
        this.semiMinorAxis = this.calculateSeminMinorAxis();
        if(this.tidallyLocked){
            if(!(this instanceof Sun)) {
                this.orbitalPeriod = this.calculateOrbitalPeriod();
                this.rotationPeriod = this.orbitalPeriod;
            } else {
                this.orbitalPeriod = rotationPeriod;
                this.rotationPeriod = rotationPeriod;
            }
        } else {
            this.orbitalPeriod = this.calculateOrbitalPeriod();
            this.rotationPeriod = rotationPeriod;
        }
        this.focalDistance = Math.sqrt(this.semiMajorAxis * this.semiMajorAxis - this.semiMinorAxis * this.semiMinorAxis);
    }

    public static double surfaceGravity(double g){
        return g * 9.18D;
    }

    public long calculateOrbitalPeriod(){
        long t = 0;
        double a = this.semiMajorAxis * 10;
        double G = 6.67430e-11;
        double M = this.parentObject.mass;
        a = Math.pow(a, 3);
        double GM = G * M;
        double aGM = a / GM;
        double sqrtAGM = Math.sqrt(aGM);
        t = (long) (2 * Math.PI * (sqrtAGM));
        return t;
    }

    public double calculateSeminMinorAxis(){
        double b = 0;
        double eSquared = this.eccentricity * this.eccentricity;
        double oneESquared = 1 - eSquared;
        double sqrtOneESquared = Math.sqrt(oneESquared);
        b = this.semiMajorAxis * sqrtOneESquared;

        return b;
    }

    public void update(){
        long currentTimeInOrbit = CosmicEvolution.instance.save.time % this.orbitalPeriod;
        double percentageOfOrbit = (double) currentTimeInOrbit / (double)this.orbitalPeriod;
        double degrees = percentageOfOrbit * 360;
        double currentDegreeInRads = Math.toRadians(degrees);

        // Compute current distance r from parent body
        double r = Math.sqrt(
                Math.pow((this.semiMajorAxis * MathUtil.cos((float) currentDegreeInRads)) - this.focalDistance, 2) +
                        Math.pow(this.semiMinorAxis * MathUtil.sin((float) currentDegreeInRads), 2)
        );

        // Compute dynamic velocity using vis-viva equation
        double gravitationalParameter = 6.67430e-11 * this.parentObject.mass; // μ = GM
        double orbitalVelocity = Math.sqrt(gravitationalParameter * ((2 / r) - (1 / this.semiMajorAxis)));
        this.orbitalVelocity = orbitalVelocity;

        // Compute angular velocity (ω = v / r)
        double angularVelocity = orbitalVelocity / r;

        // Time step (assuming deltaTime is available)
        double deltaTime = 1; // Time step per update
        double deltaAngle = angularVelocity * deltaTime; // Change in angle per update

        // Update current degree in radians using angular velocity instead of percentage
        currentDegreeInRads += deltaAngle;

        // Normalize angle to keep within bounds
        currentDegreeInRads = currentDegreeInRads % (2 * Math.PI);

        // Recalculate position based on updated angle
        this.position.x = (this.semiMajorAxis * MathUtil.cos((float) currentDegreeInRads)) - this.focalDistance;
        this.position.y = 0;
        this.position.z = this.semiMinorAxis * MathUtil.sin((float) currentDegreeInRads);

        // Apply inclination and rotations
        double inclinationInRads = Math.toRadians(this.inclination + 180);
        this.rotation.identity();
        this.rotation.rotateY(Math.toRadians(this.longitudeOfAscendingNode));
        this.rotation.rotateX(inclinationInRads);
        this.position.mul(this.rotation);
    }

    public void updateOld(){
        long currentTimeInOrbit = CosmicEvolution.instance.save.time % this.orbitalPeriod;
        double percentageOfOrbit = (double) currentTimeInOrbit / (double)this.orbitalPeriod;
        double degrees = percentageOfOrbit * 360;

        double currentDegreeInRads = Math.toRadians(degrees);

        this.position.x = (this.semiMajorAxis * MathUtil.cos((float) currentDegreeInRads)) - this.focalDistance;
        this.position.y = 0;
        this.position.z = this.semiMinorAxis * MathUtil.sin((float) currentDegreeInRads);

        double inclinationInRads = Math.toRadians(this.inclination + 180);

        this.rotation.identity();
        this.rotation.rotateY(Math.toRadians(this.longitudeOfAscendingNode));
        this.rotation.rotateX(inclinationInRads);

        this.position.mul(this.rotation);
    }

    public Vector3d calculatePreviousFramePosition(){
        long currentTimeInOrbit = (CosmicEvolution.instance.save.time - 1) % this.orbitalPeriod;
        double percentageOfOrbit = (double) currentTimeInOrbit / (double)this.orbitalPeriod;
        double degrees = percentageOfOrbit * 360;

        double currentDegreeInRads = Math.toRadians(degrees);

        Vector3d oldFramePos = new Vector3d();

        oldFramePos.x = this.semiMajorAxis  * MathUtil.cos(currentDegreeInRads);
        oldFramePos.y = 0;
        oldFramePos.z = this.semiMinorAxis * MathUtil.sin(currentDegreeInRads);

        double inclinationInRads = Math.toRadians(this.inclination);

        this.rotation.identity();
        this.rotation.rotateY(Math.toRadians(this.longitudeOfAscendingNode));
        this.rotation.rotateX(inclinationInRads);

        oldFramePos.mul(this.rotation);

        return new Vector3d(this.position.x - oldFramePos.x, this.position.y - oldFramePos.y, this.position.z - oldFramePos.z);
    }

    public Vector3d calculateDifferenceFromSelectedObject(CelestialObject celestialObject){
        Vector3d positionDif = new Vector3d();
        positionDif.x = this.position.x - celestialObject.position.x;
        positionDif.y = this.position.y - celestialObject.position.y;
        positionDif.z = this.position.z - celestialObject.position.z;
        return positionDif;
    }

    public Vector3d calculateSumFromObject(Vector3d currentAdjustedPosition){
        Vector3d distanceFromParentObject = new Vector3d();
        distanceFromParentObject.x = this.position.x + currentAdjustedPosition.x;
        distanceFromParentObject.y = this.position.y + currentAdjustedPosition.y;
        distanceFromParentObject.z = this.position.z + currentAdjustedPosition.z;
        return distanceFromParentObject;
    }

    public Vector3d calculateDifferenceFromObject(Vector3d currentAdjustedPosition){
        Vector3d distanceFromParentObject = new Vector3d();
        distanceFromParentObject.x = this.position.x - currentAdjustedPosition.x;
        distanceFromParentObject.y = this.position.y - currentAdjustedPosition.y;
        distanceFromParentObject.z = this.position.z - currentAdjustedPosition.z;
        return distanceFromParentObject;
    }


    public void render(int texture){
        new RenderCelestialBody().renderCelestialObject(texture,this);
    }
}
