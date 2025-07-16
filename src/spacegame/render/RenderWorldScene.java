package spacegame.render;

import org.joml.*;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.celestial.Sun;
import spacegame.core.GameSettings;
import spacegame.core.MathUtils;
import spacegame.core.SpaceGame;
import spacegame.core.Timer;
import spacegame.gui.GuiInGame;
import spacegame.gui.GuiUniverseMap;
import spacegame.world.Chunk;
import spacegame.world.ChunkController;

import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;

public final class RenderWorldScene {
    public ChunkController controller;
    public ArrayList<Chunk> chunksToRender = new ArrayList<>();
    public ArrayList<Chunk> chunksThatContainEntities = new ArrayList<>();
    public ArrayList<Chunk> chunksThatContainItems = new ArrayList<>();
    public boolean recalculateQueries = true;

    public RenderWorldScene(ChunkController controller){
        this.controller = controller;
    }

    public void renderWorld(Chunk[] sortedChunks) {
        this.renderNearbyCelestialObjects(); //This must be first in order to set the directional vector for the sun

        if (this.controller.parentWorldFace.sg.currentGui instanceof GuiInGame) {
            GuiInGame.renderBlockOutline();
            GuiInGame.renderBlockBreakingOutline();
        }

        this.controller.drawCalls = 0;

        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_FRONT);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, Assets.blockTextureArray.arrayID);

        GL46.glUseProgram(Shader.terrainShader.shaderProgramID);

        Shader.terrainShader.uploadMat4d("uProjection", SpaceGame.camera.projectionMatrix);
        Shader.terrainShader.uploadMat4d("uView", SpaceGame.camera.viewMatrix);
        Shader.terrainShader.uploadInt("textureArray", 0);
        Shader.terrainShader.uploadBoolean("useFog", true);
        Shader.terrainShader.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
        Shader.terrainShader.uploadFloat("fogRed", this.controller.parentWorldFace.parentWorld.skyColor[0]);
        Shader.terrainShader.uploadFloat("fogGreen", this.controller.parentWorldFace.parentWorld.skyColor[1]);
        Shader.terrainShader.uploadFloat("fogBlue", this.controller.parentWorldFace.parentWorld.skyColor[2]);
        Shader.terrainShader.uploadDouble("time", (double) Timer.elapsedTime % 8388608);

        Chunk chunk;
        int xOffset;
        int yOffset;
        int zOffset;
        int playerChunkX = MathUtils.floorDouble(SpaceGame.instance.save.thePlayer.x) >> 5;
        int playerChunkY = MathUtils.floorDouble(SpaceGame.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = MathUtils.floorDouble(SpaceGame.instance.save.thePlayer.z) >> 5;

        for (int i = 0; i < sortedChunks.length; i++) {
            chunk = sortedChunks[i];

            if (!chunk.shouldRender)continue;
            if (chunk.empty)continue;

            xOffset = chunk.x - playerChunkX;
            yOffset = chunk.y - playerChunkY;
            zOffset = chunk.z - playerChunkZ;
            xOffset <<= 5;
            yOffset <<= 5;
            zOffset <<= 5;

            if (chunk.queryID != -10) {
                if (GL46.glGetQueryObjecti(chunk.queryID, GL46.GL_QUERY_RESULT_AVAILABLE) == GL46.GL_TRUE) {
                    int[] result = new int[1];
                    GL46.glGetQueryObjectiv(chunk.queryID, GL46.GL_QUERY_RESULT, result);
                    chunk.occluded = (result[0] == 0);
                    GL46.glDeleteQueries(chunk.queryID);
                    chunk.queryID = -10;
                }
            }

            if (!chunk.occluded) {
                this.controller.drawCalls++;
                chunk.queryID = GL46.glGenQueries();

                GL46.glBeginQuery(GL46.GL_ANY_SAMPLES_PASSED, chunk.queryID);
                chunk.renderOpaque(xOffset, yOffset, zOffset);
                GL46.glEndQuery(GL46.GL_ANY_SAMPLES_PASSED);

                if (chunk.doesChunkContainEntities()) {
                    chunksThatContainEntities.add(chunk);
                }

                if (chunk.containsItems) {
                    chunksThatContainItems.add(chunk);
                }
            }

            if (chunk.vertexBufferTransparent != null && chunk.vertexBufferTransparent.limit() != 0) {
                chunksToRender.add(chunk);
            }
            chunk.checkIfEntitiesAreStillInChunk();
        }

        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        for (Chunk transparentChunk : chunksToRender) {
            this.controller.drawCalls++;
            transparentChunk.renderTransparent();
        }

        GL46.glDisable(GL46.GL_BLEND);
        GL46.glDisable(GL46.GL_ALPHA_TEST);

        GL46.glBindVertexArray(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);
        GL46.glDisable(GL46.GL_CULL_FACE);
        GL46.glUseProgram(0);

        for (Chunk entityChunk : chunksThatContainEntities) {
            entityChunk.renderEntities();
        }

        for(Chunk itemChunk : this.chunksThatContainItems){
            itemChunk.renderItems();
        }

        this.chunksThatContainItems.clear();
        this.chunksThatContainEntities.clear();
        this.chunksToRender.clear();
    }

    private void setShadowMap(Chunk[] sortedChunks){
        // this method will need to take an array of chunks sorted to the Frustum of the sun's POV.
        //The sun will use an orthographic projection matrix, the rotation and translation for the view matrix can be calculated using the direction vector as calculated in renderNearbyCelestialObjects
        //Switch the framebuffer to the shadowmap and alter the viewport to the shadow width/height.
        //draw each chunk from the sun's perspective and write to the shadowmap in this framebuffer using a lightweight shader to write
        //rebind to the default framebuffer and reset the viewport based on the screen width and height
    }

    public void renderNearbyCelestialObjects(){
        Matrix4d preservedViewMatrix = SpaceGame.camera.viewMatrix.get(new Matrix4d());
        Quaterniond viewMatrixRotation = SpaceGame.camera.viewMatrix.getUnnormalizedRotation(new Quaterniond());
        SpaceGame.camera.viewMatrix = new Matrix4d();
        SpaceGame.camera.viewMatrix.rotate(viewMatrixRotation);
        int worldSizeRadius = SpaceGame.instance.save.activeWorld.size / 2;
        double playerLat = 0;
        double playerLon = 0;
        double xThreshold = (double) worldSizeRadius / 90;
        double xAbs = Math.abs(SpaceGame.instance.save.thePlayer.x);
        double zThreshold = (double) (worldSizeRadius * 2) / 360;

        if(SpaceGame.instance.save.thePlayer.x < 0){
            playerLat = (xAbs / xThreshold);
        } else if(SpaceGame.instance.save.thePlayer.x >= 0){
            playerLat = -(xAbs / xThreshold);
        }

        playerLon =  (worldSizeRadius + SpaceGame.instance.save.thePlayer.z) / zThreshold;

        ArrayList<Vector3f> starPositions = new ArrayList<>();
        CelestialObject currentCelestialObject = SpaceGame.instance.everything.getObjectAssociatedWithWorld(this.controller.parentWorldFace.parentWorld);
        this.renderSkybox(currentCelestialObject, playerLon, playerLat);
        for(int i = 0; i < SpaceGame.instance.everything.objects.length; i++) {
            CelestialObject renderingObject = SpaceGame.instance.everything.objects[i];
            if (!(currentCelestialObject.equals(renderingObject))) {
                Vector3d positionDifference = new Vector3d();
                int layerDif = currentCelestialObject.layer - renderingObject.layer;
                switch (layerDif) {
                    case -1 -> positionDifference.sub(renderingObject.position);
                    case -2 -> positionDifference.sub(renderingObject.position).sub(renderingObject.parentObject.position);
                    case 1 -> positionDifference.add(currentCelestialObject.position);
                    case 2 -> positionDifference.add(currentCelestialObject.position).add(currentCelestialObject.parentObject.position);
                }

                double distance = Math.sqrt(positionDifference.x * positionDifference.x + positionDifference.y * positionDifference.y + positionDifference.z * positionDifference.z);

                double baseScale = 1.0f;
                double distanceThreshold = 0.0001;
                double k = 0.0001;

                double adjustedDistance = Math.max(distanceThreshold, distance);
                double scaleFactor = baseScale * (k / adjustedDistance);
                scaleFactor = Math.max(0.0001, scaleFactor);
                scaleFactor = 0.1;

                if(renderingObject instanceof Sun){
                    scaleFactor = 1000;
                }
                positionDifference.mul(0.00001);


                Vector3f celestialObjectPosition = new Vector3f((float) positionDifference.y, (float) positionDifference.z, (float) positionDifference.x);
                celestialObjectPosition.rotateX((float) (Math.toRadians(playerLon) + Math.toRadians((360 * ((double) SpaceGame.instance.save.time / currentCelestialObject.rotationPeriod)) % 360)));
                celestialObjectPosition.rotateZ((float) ((float)  Math.toRadians(playerLat) + Math.toRadians((currentCelestialObject.axialTiltX * MathUtils.sin(((double) (SpaceGame.instance.save.time % currentCelestialObject.orbitalPeriod) / currentCelestialObject.orbitalPeriod) * 2 * Math.PI)))));

                if(renderingObject instanceof Sun){
                    starPositions.add(new Vector3f(celestialObjectPosition));
                }



                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                float rotationAmountY = (float) Math.toRadians( 360 * (double) (SpaceGame.instance.save.time % renderingObject.rotationPeriod) /renderingObject.rotationPeriod);
                float rotationAmountX = (float) Math.toRadians(renderingObject.axialTiltX);
                float rotationAmountZ = (float) Math.toRadians(renderingObject.axialTiltZ);

                if(SpaceGame.camera.doesSphereIntersectFrustum(celestialObjectPosition.x, celestialObjectPosition.y, celestialObjectPosition.z, (currentCelestialObject.radius * 0.000000)  * 2)) {
                    Tessellator tessellator = Tessellator.instance;
                    for (int latitude = -90; latitude < 90; latitude += 10) {
                        for (int longitude = 0; longitude < 360; longitude += 10) {
                            if (renderingObject instanceof Sun) {
                                vertex1 = this.getPositionOnSphere(latitude + 10, longitude, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                                vertex2 = this.getPositionOnSphere(latitude + 10, longitude + 10, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                                vertex3 = this.getPositionOnSphere(latitude, longitude, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                                vertex4 = this.getPositionOnSphere(latitude, longitude + 10, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                            } else {
                                vertex1 = this.getPositionOnSphereNonStar(latitude + 10, longitude, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                                vertex2 = this.getPositionOnSphereNonStar(latitude + 10, longitude + 10, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                                vertex3 = this.getPositionOnSphereNonStar(latitude, longitude, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                                vertex4 = this.getPositionOnSphereNonStar(latitude, longitude + 10, renderingObject.radius * scaleFactor).rotateY(-rotationAmountY).rotateX(rotationAmountX).rotateZ(rotationAmountZ);
                            }
                            if (renderingObject instanceof Sun) {
                                tessellator.addVertexCubeMap(16777215, vertex4.x + celestialObjectPosition.x, vertex4.y + celestialObjectPosition.y, vertex4.z + celestialObjectPosition.z); //All non star objects need to have their lighting values calculated
                                tessellator.addVertexCubeMap(16777215, vertex1.x + celestialObjectPosition.x, vertex1.y + celestialObjectPosition.y, vertex1.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(16777215, vertex2.x + celestialObjectPosition.x, vertex2.y + celestialObjectPosition.y, vertex2.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(16777215, vertex3.x + celestialObjectPosition.x, vertex3.y + celestialObjectPosition.y, vertex3.z + celestialObjectPosition.z);
                                tessellator.addElements();
                            } else {
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex4, 16777215, starPositions), vertex4.x + celestialObjectPosition.x, vertex4.y + celestialObjectPosition.y, vertex4.z + celestialObjectPosition.z); //All non star objects need to have their lighting values calculated
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex1, 16777215, starPositions), vertex1.x + celestialObjectPosition.x, vertex1.y + celestialObjectPosition.y, vertex1.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex2, 16777215, starPositions), vertex2.x + celestialObjectPosition.x, vertex2.y + celestialObjectPosition.y, vertex2.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex3, 16777215, starPositions), vertex3.x + celestialObjectPosition.x, vertex3.y + celestialObjectPosition.y, vertex3.z + celestialObjectPosition.z);
                                tessellator.addElements();
                            }
                        }
                    }
                    Shader.worldShaderCubeMapTexture.uploadVec3f("position", celestialObjectPosition);
                    if(this.controller.parentWorldFace.parentWorld.skyLightLevel > 0) {
                        GL46.glEnable(GL46.GL_BLEND);
                        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE);
                    }
                    GL46.glEnable(GL46.GL_CULL_FACE);
                    GL46.glCullFace(GL46.GL_FRONT);
                    tessellator.drawCubeMapTexture(renderingObject.mappedTexture, Shader.worldShaderCubeMapTexture, SpaceGame.camera);
                    GL46.glDisable(GL46.GL_CULL_FACE);
                    if(this.controller.parentWorldFace.parentWorld.skyLightLevel > 0) {
                        GL46.glDisable(GL46.GL_BLEND);
                    }
                }

                if(renderingObject instanceof Sun){
                    Tessellator tessellator = Tessellator.instance;
                    float size = 100000000F * 0.0001F;
                    Quaterniond inverseRotation = new Quaterniond(viewMatrixRotation).invert();

                    Vector3d vertex1SunFlare = new Vector3d(-size, -size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    Vector3d vertex2SunFlare = new Vector3d(size, size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    Vector3d vertex3SunFlare = new Vector3d(-size, size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    Vector3d vertex4SunFlare = new Vector3d(size, -size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    tessellator.addVertex2DTexture(16777215, (float) vertex1SunFlare.x, (float) vertex1SunFlare.y, (float) vertex1SunFlare.z, 3); //Lighting doesnt need to be calculated because this is the light source in the system
                    tessellator.addVertex2DTexture(16777215, (float) vertex2SunFlare.x, (float) vertex2SunFlare.y, (float) vertex2SunFlare.z, 1);
                    tessellator.addVertex2DTexture(16777215, (float) vertex3SunFlare.x, (float) vertex3SunFlare.y, (float) vertex3SunFlare.z, 2);
                    tessellator.addVertex2DTexture(16777215, (float) vertex4SunFlare.x, (float) vertex4SunFlare.y, (float) vertex4SunFlare.z, 0);
                    tessellator.addElements();
                    GL46.glEnable(GL46.GL_BLEND);
                    GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE);
                    Shader.worldShader2DTexture.uploadBoolean("useFog", false);
                    tessellator.drawTexture2D(Sun.sunFlare.texID, Shader.worldShader2DTexture, SpaceGame.camera);
                    GL46.glDisable(GL46.GL_BLEND);
                    this.renderSunrise(new Vector3f(celestialObjectPosition).normalize().y, celestialObjectPosition);
                }
            }
        }
        this.setSkyLightLevel(starPositions);
        SpaceGame.camera.viewMatrix = preservedViewMatrix;
    }

    private void renderSkybox(CelestialObject currentCelestialObject, double playerLon, double playerLat) {
        if (this.controller.parentWorldFace.parentWorld.skyLightLevel < 13) {
            GL46.glDepthMask(false);
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_COLOR);

            Tessellator tessellator = Tessellator.instance;
            Vector3f vertex1;
            Vector3f vertex2;
            Vector3f vertex3;
            Vector3f vertex4;
            Matrix4f modelMatrix = new Matrix4f();
            modelMatrix.rotateX((float) -(Math.toRadians(playerLon) + Math.toRadians((360 * ((double) SpaceGame.instance.save.time / currentCelestialObject.rotationPeriod)) % 360)));
            modelMatrix.rotateZ((float) ((float)  Math.toRadians(playerLat) + Math.toRadians((-currentCelestialObject.axialTiltX * MathUtils.sin(((double) (SpaceGame.instance.save.time % currentCelestialObject.orbitalPeriod) / currentCelestialObject.orbitalPeriod) * 2 * Math.PI)))));
            for (int latitude = -90; latitude < 90; latitude += 45) {
                for (int longitude = 0; longitude < 360; longitude += 45) {
                    vertex1 = this.getPositionOnSphere(latitude + 45, longitude, 400000);
                    vertex2 = this.getPositionOnSphere(latitude + 45, longitude + 45, 400000);
                    vertex3 = this.getPositionOnSphere(latitude, longitude, 400000);
                    vertex4 = this.getPositionOnSphere(latitude, longitude + 45, 400000);
                    tessellator.addVertexCubeMap(16777215, (vertex4.x), (vertex4.y), (vertex4.z));
                    tessellator.addVertexCubeMap(16777215, (vertex1.x), (vertex1.y), (vertex1.z));
                    tessellator.addVertexCubeMap(16777215, (vertex2.x), (vertex2.y), (vertex2.z));
                    tessellator.addVertexCubeMap(16777215, (vertex3.x), (vertex3.y), (vertex3.z));
                    tessellator.addElements();
                }
            }

            Shader.worldShaderCubeMapTexture.uploadVec3f("position", new Vector3f());
            Shader.worldShaderCubeMapTexture.uploadBoolean("skybox", true);
            Shader.universeShaderCubeMapTexture.uploadMat4f("uModel", modelMatrix);
            tessellator.drawCubeMapTexture(GuiUniverseMap.skybox.texID, Shader.worldShaderCubeMapTexture, SpaceGame.camera);
            Shader.worldShaderCubeMapTexture.uploadBoolean("skybox", false);

            GL46.glDisable(GL46.GL_BLEND);
            GL46.glDepthMask(true);
        }
    }

    private void setSkyLightLevel(ArrayList<Vector3f> starPositions){
        Vector3f closestStar = null;
        float minDistance = Float.MAX_VALUE;

        for(Vector3f starPos : starPositions){
            float distance = starPos.distance(new Vector3f(0,0,0));
            if(distance < minDistance){
                minDistance = distance;
                closestStar = starPos;
            }
        }

        if(closestStar == null){
            this.controller.parentWorldFace.parentWorld.skyLightLevel = 0;
            return;
        }

        closestStar.normalize();

        byte calculatedSkyLightLevel = this.calculateSkyLightLevel(closestStar.y);
        this.setClearColor(closestStar.y);
        if(calculatedSkyLightLevel != this.controller.parentWorldFace.parentWorld.skyLightLevel){
            this.controller.parentWorldFace.parentWorld.skyLightLevel = calculatedSkyLightLevel;
            this.controller.markAllChunksDirty();
        }
    }

    private byte calculateSkyLightLevel(float yVecComponent){

        if(yVecComponent <= -0.2){
            return 0;
        } else if(yVecComponent >= -0.2 && yVecComponent <= -0.175){
            return 1;
        } else if(yVecComponent >= -0.175 && yVecComponent <= -0.15){
            return 2;
        } else if(yVecComponent >= -0.15 && yVecComponent <= -0.125){
            return 3;
        } else if(yVecComponent >= -0.125 && yVecComponent <= -0.1){
            return 4;
        } else if(yVecComponent >= -0.1 && yVecComponent <= -0.075){
            return 5;
        } else if(yVecComponent >= -0.075 && yVecComponent <= -0.05){
            return 6;
        } else if(yVecComponent >= -0.05 && yVecComponent <= -0.025){
            return 7;
        } else if(yVecComponent >= -0.025 && yVecComponent <= 0){
            return 8;
        } else if(yVecComponent >= 0 && yVecComponent <= 0.025){
            return 9;
        } else if(yVecComponent >= 0.025 && yVecComponent <= 0.05){
            return 10;
        } else if(yVecComponent >= 0.05 && yVecComponent <= 0.075){
            return 11;
        } else if(yVecComponent >= 0.075 && yVecComponent <= 0.1){
            return 12;
        } else if(yVecComponent >= 0.1 && yVecComponent <= 0.125){
            return 13;
        } else if(yVecComponent >= 0.125 && yVecComponent <= 0.15){
            return 14;
        } else if(yVecComponent >= 0.15){
            return 15;
        }

        return 0;
    }

    public void setClearColor(float yVecComponent){
        if(yVecComponent >= -0.2 && yVecComponent <= 0.15) {
            float correctedValue = yVecComponent + 0.2f;
            float normalizedValue = correctedValue / 0.35f;
            this.controller.parentWorldFace.parentWorld.skyColor[0] = this.controller.parentWorldFace.parentWorld.defaultSkyColor[0] * normalizedValue;
            this.controller.parentWorldFace.parentWorld.skyColor[1] = this.controller.parentWorldFace.parentWorld.defaultSkyColor[1] * normalizedValue;
            this.controller.parentWorldFace.parentWorld.skyColor[2] = this.controller.parentWorldFace.parentWorld.defaultSkyColor[2] * normalizedValue;
        }

        if(yVecComponent >= 0.15){
            this.controller.parentWorldFace.parentWorld.skyColor[0] = this.controller.parentWorldFace.parentWorld.defaultSkyColor[0];
            this.controller.parentWorldFace.parentWorld.skyColor[1] = this.controller.parentWorldFace.parentWorld.defaultSkyColor[1];
            this.controller.parentWorldFace.parentWorld.skyColor[2] = this.controller.parentWorldFace.parentWorld.defaultSkyColor[2];
        } else if(yVecComponent <= -0.2) {
            this.controller.parentWorldFace.parentWorld.skyColor[0] = 0;
            this.controller.parentWorldFace.parentWorld.skyColor[1] = 0;
            this.controller.parentWorldFace.parentWorld.skyColor[2] = 0;
        }

        SpaceGame.seGLClearColor(this.controller.parentWorldFace.parentWorld.skyColor[0], this.controller.parentWorldFace.parentWorld.skyColor[1],this.controller.parentWorldFace.parentWorld.skyColor[2],0);
    }

    private void renderSunrise(float yVecComponent, Vector3f starPosition){
        if(!(yVecComponent >= 0.15 || yVecComponent <= -0.2)) {
            starPosition.mul(0.02f);
            Tessellator tessellator = Tessellator.instance;
            float width = 3000f;
            float height = 1000f;
            int sunriseColor = this.calcSunriseColor(yVecComponent);
            tessellator.addVertex2DTexture(sunriseColor, starPosition.x - width, starPosition.y  - height, starPosition.z, 3);
            tessellator.addVertex2DTexture(sunriseColor, starPosition.x + width, starPosition.y  + height, starPosition.z, 1);
            tessellator.addVertex2DTexture(sunriseColor, starPosition.x - width, starPosition.y  + height, starPosition.z, 2);
            tessellator.addVertex2DTexture(sunriseColor, starPosition.x + width, starPosition.y  - height, starPosition.z, 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE);
            Shader.worldShader2DTexture.uploadBoolean("useFog", false);
            tessellator.drawTexture2D(Sun.sunFlare.texID, Shader.worldShader2DTexture, SpaceGame.camera);
            GL46.glDisable(GL46.GL_BLEND);
        }
    }

    private int calcSunriseColor(float yVecComponent) {
        float upperColorR = 0;
        float upperColorG = 0;
        float upperColorB = 0;
        float lowerColorR = 0;
        float lowerColorG = 0;
        float lowerColorB = 0;

        if(yVecComponent <= 0.15 && yVecComponent > 0.08){
            upperColorR = 0;
            upperColorG = 0;
            upperColorB = 0;
            lowerColorR = Color.yellow.getRed() / 255f;
            lowerColorG = Color.yellow.getGreen() / 255f;
            lowerColorB = Color.yellow.getBlue() / 255f;
        } else if(yVecComponent <= 0.08 && yVecComponent > 0.01){
            upperColorR = Color.yellow.getRed() / 255f;
            upperColorG = Color.yellow.getGreen() / 255f;
            upperColorB = Color.yellow.getBlue() / 255f;
            lowerColorR = Color.orange.getRed() / 255f;
            lowerColorG = Color.orange.getGreen() / 255f;
            lowerColorB = Color.orange.getBlue() / 255f;
        } else if(yVecComponent <= 0.01 && yVecComponent > -0.06){
            upperColorR = Color.orange.getRed() / 255f;
            upperColorG = Color.orange.getGreen() / 255f;
            upperColorB = Color.orange.getBlue() / 255f;
            lowerColorR = 255f / 255f;
            lowerColorG = 77f / 255f;
            lowerColorB = 53f / 255f;
        } else if(yVecComponent <= -0.06 && yVecComponent > -0.13){
            upperColorR = 255f / 255f;
            upperColorG = 77f / 255f;
            upperColorB = 53f / 255f;
            lowerColorR = 87f / 255f;
            lowerColorG = 0f / 255f;
            lowerColorB = 127f / 255f;
        } else if(yVecComponent <= -0.13 && yVecComponent > -0.2){
            upperColorR = 87f / 255f;
            upperColorG = 0f / 255f;
            upperColorB = 127f / 255f;
            lowerColorR = 0;
            lowerColorG = 0;
            lowerColorB = 0;
        }

        yVecComponent += 0.2;
        yVecComponent %= 0.07;

        float colorDifR = upperColorR - lowerColorR;
        float colorDifG = upperColorG - lowerColorG;
        float colorDifB = upperColorB - lowerColorB;

        float ratio = yVecComponent / 0.07f;

        float interpolatedR = lowerColorR + (colorDifR * ratio);
        float interpolatedG = lowerColorG + (colorDifG * ratio);
        float interpolatedB = lowerColorB + (colorDifB * ratio);

        int red = (int) (interpolatedR * 255);
        int green = (int) (interpolatedG * 255);
        int blue = (int) (interpolatedB * 255);

        return (red << 16) | (green << 8) | blue;
    }

    public Vector3f getPositionOnSphere(double latitude, double longitude, double R){
        R *= 0.0000001;
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtils.cos(latRad) * MathUtils.cos(lonRad));
        position.y = (float) (R * MathUtils.sin(latRad));
        position.z = (float) (R * MathUtils.cos(latRad) * MathUtils.sin(lonRad));
        return position;
    }

    public Vector3f getPositionOnSphereNonStar(double latitude, double longitude, double R){
        R *= 0.001;
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtils.cos(latRad) * MathUtils.cos(lonRad));
        position.y = (float) (R * MathUtils.sin(latRad));
        position.z = (float) (R * MathUtils.cos(latRad) * MathUtils.sin(lonRad));
        return position;
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

    private int calculateLighting(Vector3f vertexPosOnObject, int baseColor, ArrayList<Vector3f> starPositions){
        Vector3f closestStar = null;
        float minDistance = Float.MAX_VALUE;

        for(Vector3f starPos : starPositions){
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


        float intensity = Math.max(0, (float) MathUtils.cos(Math.toRadians(angle)));

        int red = (baseColor >> 16) & 0xFF;
        int green = (baseColor >> 8) & 0xFF;
        int blue = baseColor & 0xFF;

        red = Math.min(255, Math.max(0, (int)(red * intensity)));
        green = Math.min(255, Math.max(0, (int)(green * intensity)));
        blue = Math.min(255, Math.max(0, (int)(blue * intensity)));

        return (red << 16) | (green << 8) | blue;
    }

}
