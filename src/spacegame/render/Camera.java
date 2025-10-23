package spacegame.render;

import org.joml.Matrix4d;
import org.joml.Vector3d;
import spacegame.core.CosmicEvolution;

public final class Camera {
    private CosmicEvolution ce;
    public Matrix4d projectionMatrix;
    public Matrix4d guiProjectionMatrix;
    public Matrix4d viewMatrix;
    public Matrix4d frustum;
    public Vector3d position;
    public FrustumIntersectionDouble frustumInt = new FrustumIntersectionDouble();
    public FrustumRayBuilderDouble ray = new FrustumRayBuilderDouble();
    public double farPlaneDistance = 1048576D;

    public Camera(Vector3d position, CosmicEvolution ce, double nearDistance) {
        this.ce = ce;
        this.position = position;
        this.projectionMatrix = new Matrix4d();
        this.guiProjectionMatrix = new Matrix4d();
        this.viewMatrix = new Matrix4d();
        this.frustum = new Matrix4d();
        this.setViewMatrix();
        this.adjustProjection(0.7D, nearDistance);
        this.adjustGuiMatrix();
    }

    public void setFarPlaneDistance(double farPlaneDistance) {
        this.farPlaneDistance = farPlaneDistance;
    }


    public void adjustProjection(double fov, double nearDistance) {
        fov *= 100;
        this.projectionMatrix.setPerspective(Math.toRadians(fov), (double) CosmicEvolution.width / (double) CosmicEvolution.height, nearDistance, this.farPlaneDistance); //znear MUST NEVER BE 0
        Shader.terrainShader.uploadMat4d("uProjection", this.projectionMatrix);
    }

    public void adjustGuiMatrix() {
        this.guiProjectionMatrix.setOrtho((double) -CosmicEvolution.width / 2, (double) CosmicEvolution.width / 2, (double) -CosmicEvolution.height / 2, (double) CosmicEvolution.height / 2, 0.1F, this.farPlaneDistance);
    }

    public void setViewMatrix() {
        Vector3d cameraFront = new Vector3d(-1.0D, 0.0D, 0.0D);
        Vector3d cameraUp = new Vector3d(0.0D, 1.0D, 0.0D);
        this.viewMatrix.identity();
        this.viewMatrix = this.viewMatrix.lookAt(new Vector3d(this.position.x, this.position.y, this.position.z),
                cameraFront.add(this.position.x, this.position.y, this.position.z), cameraUp);
    }


    //This method only exists because I can't figure out how to prevent the camera from disconnecting from the player
    public void resetViewMatrix() {
        Vector3d cameraFront = new Vector3d(-1.0D, 0.0D, 0.0D);
        Vector3d cameraUp = new Vector3d(0.0D, 1.0D, 0.0D);
        this.position = new Vector3d();
        this.viewMatrix = new Matrix4d();
        this.viewMatrix = this.viewMatrix.lookAt(new Vector3d(this.position.x, this.position.y, this.position.z),
                cameraFront.add(this.position.x, this.position.y, this.position.z), cameraUp);
    }


    public double[] rayCast(double distance) {
        ray.set(this.viewMatrix);
        Vector3d currentPosition = new Vector3d(this.ce.save.thePlayer.x, this.ce.save.thePlayer.y + this.ce.save.thePlayer.height/2, this.ce.save.thePlayer.z);
        Vector3d rayDirection = new Vector3d();
        ray.dir(this.ce.save.thePlayer.yaw, this.ce.save.thePlayer.pitch, rayDirection);
        rayDirection.mul(new Vector3d(distance, distance, distance));

        rayDirection.add(currentPosition);

        return new double[]{rayDirection.x, rayDirection.y, rayDirection.z};
    }

    public void setFrustum(){
        this.frustum.set(this.projectionMatrix);
        this.frustum.mul(this.viewMatrix);
        this.frustumInt.set(this.frustum);
    }


    public boolean doesBoundingBoxIntersectFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        int intersect = this.frustumInt.intersectAab(minX, minY, minZ, maxX, maxY, maxZ);
        return intersect == -1 || intersect == -2;
    }

    public boolean doesSphereIntersectFrustum(double x, double y, double z, double radius){
        int intersect = this.frustumInt.intersectSphere(x,y,z,radius);
        return intersect == -1 || intersect == -2;
    }
}

