package spacegame.render;

import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.celestial.Sun;
import spacegame.core.CosmicEvolution;
import spacegame.gui.Gui;
import spacegame.util.MathUtil;
import spacegame.gui.GuiUniverseMap;

public final class RenderCelestialBody {

    public void renderCelestialObject(CelestialObject celestialObject) {
        float x;
        float y;
        float z;
        CelestialObject selectedObject = ((GuiUniverseMap) CosmicEvolution.instance.currentGui).selectedObject;
        if(celestialObject.equals(selectedObject)){
            x = 0;
            y = 0;
            z = 0;
        } else {
            Vector3d positionDifference = new Vector3d();
            int layerDif = celestialObject.layer - selectedObject.layer;
            switch (layerDif) {
                case -1 -> positionDifference.sub(selectedObject.position);
                case -2 -> positionDifference.sub(selectedObject.position).sub(selectedObject.parentObject.position);
                case 1 -> positionDifference.add(celestialObject.position);
                case 2 -> positionDifference.add(celestialObject.position).add(celestialObject.parentObject.position);
            }
            x = (float) positionDifference.x;
            y = (float) positionDifference.y;
            z = (float) positionDifference.z;
        }
        x *= GuiUniverseMap.mapScale;
        y *= GuiUniverseMap.mapScale;
        z *= GuiUniverseMap.mapScale;


        Vector3f position = new Vector3f(x,y,z);
        Shader.universeShaderCubeMapTexture.uploadVec3f("position", position);

        if(celestialObject instanceof Sun){
            GuiUniverseMap.starPositions.add(position);
            GuiUniverseMap.stars.add((Sun) celestialObject);
        }

        Shader.universeShaderCubeMapTexture.uploadBoolean("isStar", celestialObject instanceof Sun);

        if(!(celestialObject instanceof Sun)) {
            RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
            float sizeX = (float) celestialObject.semiMajorAxis * GuiUniverseMap.mapScale;
            float sizeZ = (float) celestialObject.semiMinorAxis * GuiUniverseMap.mapScale;
            Vector3d positionDifference = new Vector3d();
            int layerDif = celestialObject.parentObject.layer - selectedObject.layer;
            switch (layerDif) {
                case -1 -> positionDifference.sub(selectedObject.position);
                case -2 -> positionDifference.sub(selectedObject.position).sub(selectedObject.parentObject.position);
                case 1 -> positionDifference.add(celestialObject.position);
                case 2 -> positionDifference.add(celestialObject.position).add(celestialObject.parentObject.position);
            }
            x = (float) positionDifference.x;
            y = (float) positionDifference.y;
            z = (float) positionDifference.z;
            x *= GuiUniverseMap.mapScale;
            y *= GuiUniverseMap.mapScale;
            z *= GuiUniverseMap.mapScale;

            if(this.doesOrbitLineNeedToRender(celestialObject)) {
                double inclinationInRads = Math.toRadians(celestialObject.inclination);
                double cosInclination = MathUtil.cos(inclinationInRads);
                double sinInclination = MathUtil.sin(inclinationInRads);

                float y1 = (float) (0 * cosInclination - (z - sizeZ) * sinInclination);
                float z1 = (float) (0 * sinInclination + (z - sizeZ) * cosInclination);
                float y2 = (float) (0 * cosInclination - (z + sizeZ) * sinInclination);
                float z2 = (float) (0 * sinInclination + (z + sizeZ) * cosInclination);

                tessellator.addVertex2DTexture(9983, (float) (x + sizeX - (celestialObject.focalDistance * GuiUniverseMap.mapScale)), y1, z1, 3);
                tessellator.addVertex2DTexture(9983, (float) (x - sizeX - (celestialObject.focalDistance * GuiUniverseMap.mapScale)), y2, z2, 1);
                tessellator.addVertex2DTexture(9983, (float) (x + sizeX - (celestialObject.focalDistance * GuiUniverseMap.mapScale)), y2, z2, 2);
                tessellator.addVertex2DTexture(9983, (float) (x - sizeX - (celestialObject.focalDistance * GuiUniverseMap.mapScale)), y1, z1, 0);
                tessellator.addElements();
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.drawTexture2D(GuiUniverseMap.orbitLine, Shader.universeShader2DTexture, GuiUniverseMap.universeCamera);
                GL46.glDisable(GL46.GL_BLEND);
            }

            if (Math.sqrt(position.x * position.x + position.y * position.y + position.z * position.z) >= 0.00000000000001F && x != 0 && y != 0 && z != 0) {
                return;
            }
        }

        this.setStarlightAndLightDir(new Vector3f());

        float rotationAmountY = (float) ((float) Math.toRadians( 360 * (double) (CosmicEvolution.instance.save.time % celestialObject.rotationPeriod) /celestialObject.rotationPeriod) - (0.5 * Math.PI));
        float rotationAmountX = (float) Math.toRadians(celestialObject.axialTiltX);
        float rotationAmountZ = (float) Math.toRadians(celestialObject.axialTiltZ);
        Matrix4f modelMatrix = new Matrix4f().identity().rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);

        Shader.universeShaderCubeMapTexture.uploadMat4f("uModel", modelMatrix);

        GL46.glBindVertexArray(celestialObject.vaoID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, celestialObject.vboID);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, celestialObject.eboID);

        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, celestialObject.mappedTexture);

        GL46.glUseProgram(Shader.universeShaderCubeMapTexture.shaderProgramID);

        Shader.universeShaderCubeMapTexture.uploadMat4d("uProjection", GuiUniverseMap.universeCamera.projectionMatrix);
        Shader.universeShaderCubeMapTexture.uploadMat4d("uView", GuiUniverseMap.universeCamera.viewMatrix);

        Shader.universeShaderCubeMapTexture.uploadInt("cubeTexture", 0);

        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1f);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_FRONT);

        GL46.glDrawElements(GL46.GL_TRIANGLES, celestialObject.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisable(GL46.GL_ALPHA_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);

        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, 0);



        if(celestialObject instanceof Sun){
            RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
            float size = 500000000F * GuiUniverseMap.mapScale;
            Quaterniond viewMatrixRotation = new Quaterniond();
            GuiUniverseMap.universeCamera.viewMatrix.getNormalizedRotation(viewMatrixRotation);

            Quaterniond inverseRotation = new Quaterniond(viewMatrixRotation).invert();

            Vector3d vertex1SunFlare = new Vector3d(-size, -size, 0).rotate(inverseRotation).add(position);
            Vector3d vertex2SunFlare = new Vector3d(size, size, 0).rotate(inverseRotation).add(position);
            Vector3d vertex3SunFlare = new Vector3d(-size, size, 0).rotate(inverseRotation).add(position);
            Vector3d vertex4SunFlare = new Vector3d(size, -size, 0).rotate(inverseRotation).add(position);
            tessellator.addVertex2DTexture(((Sun) celestialObject).lightColor, (float) vertex1SunFlare.x, (float) vertex1SunFlare.y, (float) vertex1SunFlare.z, 3); //Lighting doesnt need to be calculated because this is the light source in the system
            tessellator.addVertex2DTexture(((Sun) celestialObject).lightColor, (float) vertex2SunFlare.x, (float) vertex2SunFlare.y, (float) vertex2SunFlare.z, 1);
            tessellator.addVertex2DTexture(((Sun) celestialObject).lightColor, (float) vertex3SunFlare.x, (float) vertex3SunFlare.y, (float) vertex3SunFlare.z, 2);
            tessellator.addVertex2DTexture(((Sun) celestialObject).lightColor, (float) vertex4SunFlare.x, (float) vertex4SunFlare.y, (float) vertex4SunFlare.z, 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_COLOR);
            tessellator.drawTexture2D(Sun.sunFlare, Shader.universeShader2DTexture, GuiUniverseMap.universeCamera);
            GL46.glDisable(GL46.GL_BLEND);
        }
    }

    private boolean doesOrbitLineNeedToRender(CelestialObject renderingObject) {
        Vector3d cameraPosition = new Vector3d();
        cameraPosition.add(GuiUniverseMap.universeCamera.viewMatrix.getTranslation(new Vector3d()));
        cameraPosition.rotate(GuiUniverseMap.universeCamera.viewMatrix.getNormalizedRotation(new Quaterniond()));
        double cameraDistance = Math.sqrt(cameraPosition.x * cameraPosition.x + cameraPosition.y * cameraPosition.y + cameraPosition.z * cameraPosition.z);

        return !(cameraDistance <= (renderingObject.semiMajorAxis * 0.1) * GuiUniverseMap.mapScale);
    }

    private Vector3f calculateNormal(Vector3f vertexPosOnObject){
        return new Vector3f(vertexPosOnObject).normalize();
    }

    private void setStarlightAndLightDir(Vector3f objectPos){
        Vector3f closestStar = null;
        float minDistance = Float.MAX_VALUE;

        Sun star = null;
        Vector3f starPos;

        for(int i = 0; i < GuiUniverseMap.starPositions.size(); i++) {
            starPos = GuiUniverseMap.starPositions.get(i);
            float distance = starPos.distance(objectPos);
            if (distance < minDistance) {
                minDistance = distance;
                closestStar = starPos;
                star = GuiUniverseMap.stars.get(i);
            }
        }

        if(closestStar == null){
            closestStar = new Vector3f();
        }

        Vector3f lightDir = new Vector3f(closestStar).normalize();
        Shader.universeShaderCubeMapTexture.uploadVec3f("normalizedLightDir", lightDir);
        Shader.universeShaderCubeMapTexture.uploadInt("lightColor", star == null ? 0 : star.lightColor);
    }

    public Vector3f getPositionOnSphere(int latitude, int longitude, float R){
        R *= GuiUniverseMap.mapScale;
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtil.cos(latRad) * MathUtil.cos(lonRad));
        position.y = (float) (R * MathUtil.sin(latRad));
        position.z = (float) (R * MathUtil.cos(latRad) * MathUtil.sin(lonRad));
        return position;
    }


}
