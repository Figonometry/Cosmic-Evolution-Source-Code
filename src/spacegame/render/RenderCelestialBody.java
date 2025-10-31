package spacegame.render;

import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.celestial.Sun;
import spacegame.core.CosmicEvolution;
import spacegame.util.MathUtil;
import spacegame.gui.GuiUniverseMap;

public final class RenderCelestialBody {

    public void renderCelestialObject(int texture, CelestialObject celestialObject) {
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


        float distance = x * x + y * y + z * z;
        distance = (float) Math.sqrt(distance);
        Vector3f position = new Vector3f(x,y,z);
        Shader.universeShaderCubeMapTexture.uploadVec3f("position", position);

        if(celestialObject instanceof Sun){
            GuiUniverseMap.starPositions.add(position);
        }

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
        x = 0;
        y = 0;
        z = 0;
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        Vector3f vertex1;
        Vector3f vertex2;
        Vector3f vertex3;
        Vector3f vertex4;
        float baseScale = 1.0f;
        float distanceThreshold = 500000f;
        float maxDistanceEffect = Float.MAX_VALUE;
        float k = 500000f;

        float adjustedDistance = Math.max(distanceThreshold, distance);
        float scaleFactor = baseScale * (k / adjustedDistance);
        scaleFactor = Math.max(0.1f, scaleFactor);

        float rotationAmountY = (float) ((float) Math.toRadians( 360 * (double) (CosmicEvolution.instance.save.time % celestialObject.rotationPeriod) /celestialObject.rotationPeriod) - (0.5 * Math.PI));
        float rotationAmountX = (float) Math.toRadians(celestialObject.axialTiltX);
        float rotationAmountZ = (float) Math.toRadians(celestialObject.axialTiltZ);
        for(int latitude = -90; latitude < 90; latitude += 5){
            for(int longitude = 0; longitude < 360; longitude += 5){
                    vertex1 = this.getPositionOnSphere(latitude + 5, longitude, celestialObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                    vertex2 = this.getPositionOnSphere(latitude + 5, longitude + 5, celestialObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                    vertex3 = this.getPositionOnSphere(latitude, longitude, celestialObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                    vertex4 = this.getPositionOnSphere(latitude, longitude + 5, celestialObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                    if(celestialObject instanceof Sun){
                        tessellator.addVertexCubeMap(16777215, vertex4.x + x, vertex4.y + y, vertex4.z + z); //All non star objects need to have their lighting values calculated
                        tessellator.addVertexCubeMap(16777215, vertex1.x + x, vertex1.y + y, vertex1.z + z);
                        tessellator.addVertexCubeMap(16777215, vertex2.x + x, vertex2.y + y, vertex2.z + z);
                        tessellator.addVertexCubeMap(16777215, vertex3.x + x, vertex3.y + y, vertex3.z + z);
                        tessellator.addElements();
                    } else {
                        tessellator.addVertexCubeMap(this.calculateLighting(vertex4, 16777215), vertex4.x + x, vertex4.y + y, vertex4.z + z); //All non star objects need to have their lighting values calculated
                        tessellator.addVertexCubeMap(this.calculateLighting(vertex1, 16777215), vertex1.x + x, vertex1.y + y, vertex1.z + z);
                        tessellator.addVertexCubeMap(this.calculateLighting(vertex2, 16777215), vertex2.x + x, vertex2.y + y, vertex2.z + z);
                        tessellator.addVertexCubeMap(this.calculateLighting(vertex3, 16777215), vertex3.x + x, vertex3.y + y, vertex3.z + z);
                        tessellator.addElements();
                    }
            }
        }
        Matrix4f modelMatrix = new Matrix4f().identity().rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);

        Shader.universeShaderCubeMapTexture.uploadMat4f("uModel", modelMatrix);
        tessellator.drawCubeMapTexture(texture, Shader.universeShaderCubeMapTexture, GuiUniverseMap.universeCamera);

        if(celestialObject instanceof Sun){
            float size = 500000000F * GuiUniverseMap.mapScale;
            Quaterniond viewMatrixRotation = new Quaterniond();
            GuiUniverseMap.universeCamera.viewMatrix.getNormalizedRotation(viewMatrixRotation);

            Quaterniond inverseRotation = new Quaterniond(viewMatrixRotation).invert();

            Vector3d vertex1SunFlare = new Vector3d(-size, -size, 0).rotate(inverseRotation).add(position);
            Vector3d vertex2SunFlare = new Vector3d(size, size, 0).rotate(inverseRotation).add(position);
            Vector3d vertex3SunFlare = new Vector3d(-size, size, 0).rotate(inverseRotation).add(position);
            Vector3d vertex4SunFlare = new Vector3d(size, -size, 0).rotate(inverseRotation).add(position);
            tessellator.addVertex2DTexture(16777215, (float) vertex1SunFlare.x, (float) vertex1SunFlare.y, (float) vertex1SunFlare.z, 3); //Lighting doesnt need to be calculated because this is the light source in the system
            tessellator.addVertex2DTexture(16777215, (float) vertex2SunFlare.x, (float) vertex2SunFlare.y, (float) vertex2SunFlare.z, 1);
            tessellator.addVertex2DTexture(16777215, (float) vertex3SunFlare.x, (float) vertex3SunFlare.y, (float) vertex3SunFlare.z, 2);
            tessellator.addVertex2DTexture(16777215, (float) vertex4SunFlare.x, (float) vertex4SunFlare.y, (float) vertex4SunFlare.z, 0);
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

    private int calculateLighting(Vector3f vertexPosOnObject, int baseColor){
        Vector3f closestStar = null;
        float minDistance = Float.MAX_VALUE;

        for(Vector3f starPos : GuiUniverseMap.starPositions){
            float distance = starPos.distance(vertexPosOnObject);
            if(distance < minDistance){
                minDistance = distance;
                closestStar = starPos;
            }
        }

        if(closestStar == null){
            return 0;
        }


        Vector3f vertexDir = new Vector3f(vertexPosOnObject).normalize();
        Vector3f lightDir = new Vector3f(closestStar).normalize();

        float dotProduct = vertexDir.dot(lightDir);
        float angle = (float) Math.toDegrees(Math.acos(dotProduct));

        float intensity = Math.max(0, (float) MathUtil.cos(Math.toRadians(angle)));

        int red = (baseColor >> 16) & 0xFF;
        int green = (baseColor >> 8) & 0xFF;
        int blue = baseColor & 0xFF;

        // Scale each color channel by intensity
        red = Math.min(255, Math.max(0, (int)(red * intensity)));
        green = Math.min(255, Math.max(0, (int)(green * intensity)));
        blue = Math.min(255, Math.max(0, (int)(blue * intensity)));

        // Reconstruct new color value
        return (red << 16) | (green << 8) | blue;
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
