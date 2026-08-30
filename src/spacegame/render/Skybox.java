package spacegame.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.core.CosmicEvolution;
import spacegame.gui.GuiUniverseMap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Skybox {
    //This used to rely on the tessellator and reassemble vertices every frame, it has been split into it's own class and decoupled from that system in order to increase efficiency
    //and to support a skybox at all times, this is to remove the reliance of basing the sky color on the values set in glClearColor and allow for a dynamic sky coloration
    private int innerVAO;
    private int innerVBO;
    private int innerEBO;
    public int innerElementOffset = 0;
    public Matrix4f modelMatrix = new Matrix4f();
    public FloatBuffer innerVertexBuffer = BufferUtils.createFloatBuffer(262144);
    public IntBuffer innerElementBuffer = BufferUtils.createIntBuffer(262144);

    private int outerVAO;
    private int outerVBO;
    private int outerEBO;
    public int outerElementOffset = 0;
    public FloatBuffer outerVertexBuffer = BufferUtils.createFloatBuffer(262144);
    public IntBuffer outerElementBuffer = BufferUtils.createIntBuffer(262144);


    public Skybox() {
        RenderEngine renderEngine = CosmicEvolution.instance.renderEngine;

        this.innerVAO = renderEngine.createVAO();
        this.innerVBO = renderEngine.createBuffers();
        this.innerEBO = renderEngine.createBuffers();

        this.outerVAO = renderEngine.createVAO();
        this.outerVBO = renderEngine.createBuffers();
        this.outerEBO = renderEngine.createBuffers();

        int positionsSize = 3;

        int vertexSizeBytes = positionsSize * Float.BYTES;
        renderEngine.setVertexAttribute(this.innerVAO, 0, positionsSize, vertexSizeBytes, 0, this.innerVBO);
        renderEngine.setVertexAttribute(this.outerVAO, 0, positionsSize, vertexSizeBytes, 0, this.outerVBO);
    }

    public void cleanupOpenGLState(){
        RenderEngine renderEngine = CosmicEvolution.instance.renderEngine;

        renderEngine.deleteVAO(this.innerVAO);
        renderEngine.deleteBuffers(this.innerVBO);
        renderEngine.deleteBuffers(this.innerEBO);

        renderEngine.deleteVAO(this.outerVAO);
        renderEngine.deleteBuffers(this.outerVBO);
        renderEngine.deleteBuffers(this.outerEBO);
    }

    public void addElementsCWInner(){
        this.innerElementBuffer.put(this.innerElementOffset + 2);
        this.innerElementBuffer.put(this.innerElementOffset + 1);
        this.innerElementBuffer.put(this.innerElementOffset + 0);
        this.innerElementBuffer.put(this.innerElementOffset + 0);
        this.innerElementBuffer.put(this.innerElementOffset + 1);
        this.innerElementBuffer.put(this.innerElementOffset + 3);
        this.innerElementOffset += 4;
    }

    public void addVertexCubeMapInner(float x, float y, float z){
        this.innerVertexBuffer.put(x);
        this.innerVertexBuffer.put(y);
        this.innerVertexBuffer.put(z);
    }

    public void addElementsCWOuter(){
        this.outerElementBuffer.put(this.outerElementOffset + 2);
        this.outerElementBuffer.put(this.outerElementOffset + 1);
        this.outerElementBuffer.put(this.outerElementOffset + 0);
        this.outerElementBuffer.put(this.outerElementOffset + 0);
        this.outerElementBuffer.put(this.outerElementOffset + 1);
        this.outerElementBuffer.put(this.outerElementOffset + 3);
        this.outerElementOffset += 4;
    }

    public void addVertexCubeMapOuter(float x, float y, float z){
        this.outerVertexBuffer.put(x);
        this.outerVertexBuffer.put(y);
        this.outerVertexBuffer.put(z);
    }

    //This should run once at the start of the world scene, this sphere is always centered around the camera
    public void setupRenderStates(RenderWorldScene renderWorldScene){
        Vector3f vertex1;
        Vector3f vertex2;
        Vector3f vertex3;
        Vector3f vertex4;
        for (int latitude = -90; latitude < 90; latitude += 5) {
            for (int longitude = 0; longitude < 365; longitude += 5) {
                vertex1 = renderWorldScene.getPositionOnSphere(latitude + 5, longitude, 400000);
                vertex2 = renderWorldScene.getPositionOnSphere(latitude + 5, longitude + 5, 400000);
                vertex3 = renderWorldScene.getPositionOnSphere(latitude, longitude, 400000);
                vertex4 = renderWorldScene.getPositionOnSphere(latitude, longitude + 5, 400000);
                this.addVertexCubeMapInner((vertex4.x), (vertex4.y), (vertex4.z));
                this.addVertexCubeMapInner((vertex1.x), (vertex1.y), (vertex1.z));
                this.addVertexCubeMapInner((vertex2.x), (vertex2.y), (vertex2.z));
                this.addVertexCubeMapInner((vertex3.x), (vertex3.y), (vertex3.z));
                this.addElementsCWInner();
            }
        }

        this.innerVertexBuffer.flip();
        this.innerElementBuffer.flip();


        GL46.glBindVertexArray(this.innerVAO);


        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.innerVBO);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.innerVertexBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.innerEBO);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.innerElementBuffer, GL46.GL_STATIC_DRAW);



        for (int latitude = -90; latitude < 90; latitude += 5) {
            for (int longitude = 0; longitude < 360; longitude += 5) {
                vertex1 = renderWorldScene.getPositionOnSphere(latitude + 5, longitude, 500000);
                vertex2 = renderWorldScene.getPositionOnSphere(latitude + 5, longitude + 5, 500000);
                vertex3 = renderWorldScene.getPositionOnSphere(latitude, longitude, 500000);
                vertex4 = renderWorldScene.getPositionOnSphere(latitude, longitude + 5, 500000);
                this.addVertexCubeMapOuter((vertex4.x), (vertex4.y), (vertex4.z));
                this.addVertexCubeMapOuter((vertex1.x), (vertex1.y), (vertex1.z));
                this.addVertexCubeMapOuter((vertex2.x), (vertex2.y), (vertex2.z));
                this.addVertexCubeMapOuter((vertex3.x), (vertex3.y), (vertex3.z));
                this.addElementsCWOuter();
            }
        }

        this.outerVertexBuffer.flip();
        this.outerElementBuffer.flip();


        GL46.glBindVertexArray(this.outerVAO);


        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.outerVBO);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.outerVertexBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.outerEBO);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.outerElementBuffer, GL46.GL_STATIC_DRAW);
    }

    //The current system of having only the stars render as a skybox will now need to have it further out than the daytime skybox
    public void renderSkybox(CelestialObject currentCelestialObject, double playerLon, double playerLat) {
        GL46.glDepthMask(false);

        this.modelMatrix.identity();
        this.modelMatrix.rotateX((float) -(Math.toRadians(playerLon) + Math.toRadians((360 * ((double) CosmicEvolution.instance.save.time / currentCelestialObject.rotationPeriod)) % 360)));

        float latRad = (float) Math.toRadians(playerLat);

        float orbitalPhase = (CosmicEvolution.instance.save.time % currentCelestialObject.orbitalPeriod) / (float) currentCelestialObject.orbitalPeriod;

        float declination = (float) (currentCelestialObject.axialTiltX * Math.sin((orbitalPhase * (2 * Math.PI))));

        float tiltAngle = (float) (latRad - Math.toRadians(declination));

        this.modelMatrix.rotateZ(tiltAngle);


        Shader.worldSkyboxOuter.uploadMat4f("uModel", this.modelMatrix);
        this.drawInnerSkybox(CosmicEvolution.camera);
        this.drawOuterSkybox(CosmicEvolution.camera);

        GL46.glDepthMask(true);
    }


        private void drawInnerSkybox(Camera camera) {
            GL46.glBindVertexArray(this.innerVAO);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.innerVBO);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.innerEBO);

            GL46.glUseProgram(Shader.worldSkyboxInner.shaderProgramID);

            Shader.worldSkyboxInner.uploadMat4d("uProjection", camera.projectionMatrix);
            Shader.worldSkyboxInner.uploadMat4d("uView", camera.viewMatrix);

            GL46.glDrawElements(GL46.GL_TRIANGLES, this.innerElementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);
        }

        private void drawOuterSkybox(Camera camera){
            GL46.glBindVertexArray(this.outerVAO);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.outerVBO);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.outerEBO);

            GL46.glUseProgram(Shader.worldSkyboxOuter.shaderProgramID);

            Shader.worldSkyboxOuter.uploadMat4d("uProjection", camera.projectionMatrix);
            Shader.worldSkyboxOuter.uploadMat4d("uView", camera.viewMatrix);
            Shader.worldSkyboxOuter.uploadInt("starTexture", 0);

            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, GuiUniverseMap.skybox);

            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE);
            GL46.glDrawElements(GL46.GL_TRIANGLES, this.outerElementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);
            GL46.glDisable(GL46.GL_BLEND);

            GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, 0);
        }



}
