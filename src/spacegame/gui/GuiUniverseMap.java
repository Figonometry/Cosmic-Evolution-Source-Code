package spacegame.gui;

import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.core.GameSettings;
import spacegame.core.MathUtils;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.render.*;

import java.util.ArrayList;

public final class GuiUniverseMap extends Gui {
    public CelestialObject selectedObject;
    public static Camera universeCamera;
    public static float yaw;
    public static float pitch;
    public static float prevDeltaYaw;
    public static float prevDeltaPitch;
    public static TextureLoader orbitLine;
    public static TextureCubeMapLoader skybox;
    public static float mapScale = 0.00000000000000000000001F;
    public static ArrayList<Vector3f> starPositions = new ArrayList<>();


    public GuiUniverseMap(SpaceGame spaceGame) {
        super(spaceGame);
        this.selectedObject = SpaceGame.instance.everything.earth;
    }

    @Override
    public void loadTextures() {
        orbitLine = new TextureLoader("src/spacegame/assets/textures/gui/guiUniverse/orbitLine.png", 2048, 2048);
    }

    public static void initSkyboxTexture(){
        skybox = new TextureCubeMapLoader("src/spacegame/assets/textures/gui/guiUniverse/skybox");
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(orbitLine.texID);
        orbitLine = null;
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
        int leftSide = -SpaceGame.width / 2 - 10;
        fontRenderer.drawString(SpaceGame.instance.title + " (" + SpaceGame.instance.fps * -1 + " FPS)", leftSide, 460, 16777215);
        fontRenderer.drawString("Current Selected Object: " + this.selectedObject, leftSide, 430, 16777215);


        this.sg.everything.sun.render(this.sg.everything.sun.mappedTexture);
        this.sg.everything.earth.render(this.sg.everything.earth.mappedTexture);
        this.sg.everything.moon.render(this.sg.everything.moon.mappedTexture);
        starPositions.clear();

        fontRenderer.drawString("Yaw: " + yaw, leftSide, 400, 16777215);
        fontRenderer.drawString("Pitch: " + pitch, leftSide, 370, 16777215);
        fontRenderer.drawString("Orbital Velocity: " + this.selectedObject.orbitalVelocity, leftSide, 340, 16777215);

    }


    private void renderSkybox(){
        Tessellator tessellator = Tessellator.instance;
        float x = 0;
        float y = 0;
        float z = 0;
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
                tessellator.addVertexCubeMap(16777215, (vertex4.x + x), (vertex4.y + y),(vertex4.z + z));
                tessellator.addVertexCubeMap(16777215, (vertex1.x + x), (vertex1.y + y),(vertex1.z + z));
                tessellator.addVertexCubeMap(16777215, (vertex2.x + x), (vertex2.y + y),(vertex2.z + z));
                tessellator.addVertexCubeMap(16777215, (vertex3.x + x), (vertex3.y + y),(vertex3.z + z));
                tessellator.addElements();
            }
        }

        Shader.universeShaderCubeMapTexture.uploadBoolean("skybox", true);
        Shader.universeShaderCubeMapTexture.uploadVec3f("position", new Vector3f());
        tessellator.drawCubeMapTexture(skybox.texID, Shader.universeShaderCubeMapTexture, GuiUniverseMap.universeCamera);
        Shader.universeShaderCubeMapTexture.uploadBoolean("skybox", false);
    }

    public Vector3f getPositionOnSphere(int latitude, int longitude, float R){
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtils.cos(latRad) * MathUtils.cos(lonRad));
        position.y = (float) (R * MathUtils.sin(latRad));
        position.z = (float) (R * MathUtils.cos(latRad) * MathUtils.sin(lonRad));
        return position;
    }

    public void updateCamera(){
        if(MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            float rawDeltaYaw = (MouseListener.getDeltaX() / 9) * GameSettings.sensitivity;
            float rawDeltaPitch = (MouseListener.getDeltaY() / 9) * GameSettings.sensitivity;

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
        if(this.selectedObject.equals(SpaceGame.instance.everything.earth)){
            this.selectedObject = SpaceGame.instance.everything.moon;
        } else if(this.selectedObject.equals(SpaceGame.instance.everything.moon)){
            this.selectedObject = SpaceGame.instance.everything.sun;
        } else if(this.selectedObject.equals(SpaceGame.instance.everything.sun)){
            this.selectedObject = SpaceGame.instance.everything.earth;
        }

    }

    @Override
    public Button getActiveButton() {
        return null;
    }
}
