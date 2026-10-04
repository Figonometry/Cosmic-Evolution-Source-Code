package spacegame.gui;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.celestial.Sun;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.render.Camera;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.MathUtil;

import java.lang.Math;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public final class GuiUniverseMap extends Gui {
    public CelestialObject selectedObject;
    public static Camera universeCamera;
    public static float yaw;
    public static float pitch;
    public static float prevDeltaYaw;
    public static float prevDeltaPitch;
    public static int orbitLine;
    public static int skybox;
    public static float mapScale = 0.00000000000000000000001F;
    public static ArrayList<Vector3f> starPositions = new ArrayList<>();
    public static ArrayList<Sun> stars = new ArrayList<>();
    private static int vao = -1;
    private static int vbo = -1;
    private static int ebo = -1;
    private static int elementCount = -1;

    public GuiUniverseMap(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.selectedObject = CosmicEvolution.instance.everything.earth;



        if(vao == -1 || vbo == -1 || ebo == -1 || elementCount == -1){
            RenderEngine renderEngine = CosmicEvolution.instance.renderEngine;

            vao = renderEngine.createVAO();
            vbo = renderEngine.createBuffers();
            ebo = renderEngine.createBuffers();

            FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(262144);
            IntBuffer elementBuffer = BufferUtils.createIntBuffer(262144);
            int elementOffset = 0;

            int positionSize = 3;
            int vertexSizeBytes = 3 * Float.BYTES;

            renderEngine.setVertexAttribute(vao, 0, positionSize, vertexSizeBytes, 0, vbo);


            Vector3f vertex1;
            Vector3f vertex2;
            Vector3f vertex3;
            Vector3f vertex4;
            for(int latitude = -90; latitude < 90; latitude += 5){
                for(int longitude = 0; longitude < 360; longitude += 5){
                    vertex1 = this.getPositionOnSphere(latitude + 5, longitude, 50000);
                    vertex2 = this.getPositionOnSphere(latitude + 5, longitude + 5, 50000);
                    vertex3 = this.getPositionOnSphere(latitude, longitude,50000);
                    vertex4 = this.getPositionOnSphere(latitude, longitude + 5, 50000);

                    vertexBuffer.put(vertex4.x);
                    vertexBuffer.put(vertex4.y);
                    vertexBuffer.put(vertex4.z);
                    vertexBuffer.put(vertex1.x);
                    vertexBuffer.put(vertex1.y);
                    vertexBuffer.put(vertex1.z);
                    vertexBuffer.put(vertex2.x);
                    vertexBuffer.put(vertex2.y);
                    vertexBuffer.put(vertex2.z);
                    vertexBuffer.put(vertex3.x);
                    vertexBuffer.put(vertex3.y);
                    vertexBuffer.put(vertex3.z);


                    elementBuffer.put(elementOffset + 2);
                    elementBuffer.put(elementOffset + 1);
                    elementBuffer.put(elementOffset + 0);
                    elementBuffer.put(elementOffset + 0);
                    elementBuffer.put(elementOffset + 1);
                    elementBuffer.put(elementOffset + 3);
                    elementOffset += 4;
                }
            }

            vertexBuffer.flip();
            elementBuffer.flip();

            elementCount = elementBuffer.limit();


            GL46.glBindVertexArray(vao);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ebo);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindVertexArray(0);


            Shader.universeSkybox.uploadVec3f("position", new Vector3f());
            Shader.universeSkybox.uploadMat4f("uModel", new Matrix4f());
            Shader.universeSkybox.uploadInt("cubeTexture", 0);
        }
    }

    @Override
    public void loadTextures() {
        orbitLine = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiUniverse/orbitLine.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    public static void initSkyboxTexture(){
        skybox = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiUniverse/skybox", RenderEngine.TEXTURE_TYPE_CUBEMAP, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(orbitLine);
    }


    @Override
    public void handleInput(){
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK) && KeyListener.keyReleased[GLFW.GLFW_KEY_CAPS_LOCK]) {
            this.switchObject();
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_CAPS_LOCK);
        }

        this.updateCamera();

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_TAB) && KeyListener.keyReleased[GLFW.GLFW_KEY_TAB]) {
            this.ce.setNewGui(new GuiInGame(this.ce));
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_TAB);
        }

    }

    @Override
    public void drawGui() {
        GL46.glDisable(GL46.GL_DEPTH_BUFFER_BIT);
        GL46.glDisable(GL46.GL_DEPTH_TEST);
        GL46.glDepthMask(false);
        this.renderSkybox();
        GL46.glDepthMask(true);
        GL46.glEnable(GL46.GL_DEPTH_BUFFER_BIT);
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        FontRenderer fontRenderer = FontRenderer.instance;
        int leftSide = -970;
        fontRenderer.drawString(CosmicEvolution.instance.title + " (" + CosmicEvolution.instance.fps + " FPS)", leftSide, 460,-15, 16777215, 50, 255);
        fontRenderer.drawString("Current Selected Object: " + this.selectedObject, leftSide, 430,-15, 16777215, 50, 255);


        this.ce.everything.sun.render();
        this.ce.everything.earth.render();
        this.ce.everything.moon.render();
        starPositions.clear();
        stars.clear();

        fontRenderer.drawString("Yaw: " + yaw, leftSide, 400,-15, 16777215, 50, 255);
        fontRenderer.drawString("Pitch: " + pitch, leftSide, 370,-15, 16777215, 50, 255);
        fontRenderer.drawString("Orbital Velocity: " + this.selectedObject.orbitalVelocity, leftSide, 340,-15, 16777215, 50, 255);

    }



    private void renderSkybox(){
        GL46.glBindVertexArray(vao);
        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, skybox);
        Shader.universeSkybox.uploadMat4d("uProjection", universeCamera.projectionMatrix);
        Shader.universeSkybox.uploadMat4d("uView", universeCamera.viewMatrix);

        GL46.glDrawElements(GL46.GL_TRIANGLES, elementCount, GL46.GL_UNSIGNED_INT, 0);

        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, 0);
        GL46.glBindVertexArray(0);
    }

    public Vector3f getPositionOnSphere(int latitude, int longitude, float R){
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtil.cos(latRad) * MathUtil.cos(lonRad));
        position.y = (float) (R * MathUtil.sin(latRad));
        position.z = (float) (R * MathUtil.cos(latRad) * MathUtil.sin(lonRad));
        return position;
    }

    public void updateCamera(){
        if(MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            float rawDeltaYaw = (MouseListener.getDeltaX()) * GameSettings.sensitivity;
            float rawDeltaPitch = (MouseListener.getDeltaY()) * GameSettings.sensitivity;

            if (GameSettings.invertMouse) {
                rawDeltaPitch *= -1;
            }

            float deltaYaw = (float) Math.toRadians(rawDeltaYaw);
            float deltaPitch = (float) Math.toRadians(rawDeltaPitch);

            if (deltaYaw != prevDeltaYaw) {
                yaw -= rawDeltaYaw;
                universeCamera.viewMatrix.rotateY(-deltaYaw);
            }

            if ((deltaPitch != prevDeltaPitch) && ((pitch + rawDeltaPitch < 90) && (pitch + rawDeltaPitch > -90))) {
                pitch += rawDeltaPitch;
                Vector3d right = new Vector3d(universeCamera.viewMatrix.m00(), universeCamera.viewMatrix.m10(), universeCamera.viewMatrix.m20());
                right.normalize();
                Matrix4d pitchRotation = new Matrix4d();
                pitchRotation.rotate(-deltaPitch, right);
                universeCamera.viewMatrix.mul(pitchRotation);
            }

            if (pitch < -90) {
                pitch = -90;
            }

            if (pitch > 90) {
                pitch = 90;
            }

            if (yaw < 0) {
                yaw += 360;
            }
            if (yaw >= 360) {
                yaw %= 360;
            }


            prevDeltaYaw = deltaYaw;
            prevDeltaPitch = deltaPitch;
        }

        Vector3d forward = new Vector3d(-universeCamera.viewMatrix.m02(), -universeCamera.viewMatrix.m12(), -universeCamera.viewMatrix.m22());
        forward.normalize();
        Vector3d cameraPosition = new Vector3d();
        cameraPosition.add(GuiUniverseMap.universeCamera.viewMatrix.getTranslation(new Vector3d()));
        cameraPosition.rotate(GuiUniverseMap.universeCamera.viewMatrix.getNormalizedRotation(new Quaterniond()));
        double distance = Math.sqrt(cameraPosition.x * cameraPosition.x + cameraPosition.y * cameraPosition.y + cameraPosition.z * cameraPosition.z);
        double movementSpeed = 0.00000000000000001 + (distance / 16);
        Vector3d translation = new Vector3d(forward).mul(-MouseListener.getScrollY() * movementSpeed);
        universeCamera.viewMatrix.translate(translation.x, translation.y, translation.z);
        universeCamera.position.sub(translation);

    }

    public void switchObject(){
        if(this.selectedObject.equals(CosmicEvolution.instance.everything.earth)){
            this.selectedObject = CosmicEvolution.instance.everything.moon;
        } else if(this.selectedObject.equals(CosmicEvolution.instance.everything.moon)){
            this.selectedObject = CosmicEvolution.instance.everything.sun;
        } else if(this.selectedObject.equals(CosmicEvolution.instance.everything.sun)){
            this.selectedObject = CosmicEvolution.instance.everything.earth;
        }

    }

    @Override
    public Button getActiveButton() {
        return null;
    }
}
