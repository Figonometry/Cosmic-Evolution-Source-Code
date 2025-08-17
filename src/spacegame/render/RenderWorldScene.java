package spacegame.render;

import org.joml.*;
import org.lwjgl.opengl.GL46;
import spacegame.celestial.CelestialObject;
import spacegame.celestial.Sun;
import spacegame.core.GameSettings;
import spacegame.core.MathUtil;
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
    public ArrayList<Sun> nearbyStars = new ArrayList<>();
    public ArrayList<Vector3f> nearbyStarPos = new ArrayList<>();
    public boolean recalculateQueries = true;
    public int sunX;
    public int sunY;
    public int sunZ;
    public Matrix4f sunViewMatrix = new Matrix4f();
    public Vector3f lastDir = new Vector3f();
    public boolean shouldSkyboxRender;
    public boolean shouldShadowsRender;
    public boolean blendCelestialObjects;
    public double playerLat;
    public double playerLon;
    public boolean hasTickPassed;
    public float sunRed;
    public float sunGreen;
    public float sunBlue;
    public float baseLight;
    public int frameSinceFrustumRecalc = 0;
    public boolean renderWithChunks = true;
    public int[] opaqueChunks;
    public int[] transparentChunks;

    public RenderWorldScene(ChunkController controller){
        this.controller = controller;
    }

    public void renderWorldWithChunks(Chunk[] sortedChunks) {
        this.frameSinceFrustumRecalc++;
        for(int i = 0; i < this.nearbyStars.size(); i++){
            this.setShadowMap(this.nearbyStars.get(i),this.nearbyStarPos.get(i));
        }

        this.nearbyStars.clear();
        this.nearbyStarPos.clear();

        this.renderNearbyCelestialObjects();

        if (SpaceGame.instance.currentGui instanceof GuiInGame) {
            GuiInGame.renderBlockOutline();
            GuiInGame.renderBlockBreakingOutline();
        }

        this.controller.drawCalls = 0;

        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_FRONT);
        //The block array is bound to texture 0, all shadowmaps are bound from texture unit 1 up, they must all be properly active and unbound during cleanup
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, Assets.blockTextureArray);

        GL46.glUseProgram(Shader.terrainShader.shaderProgramID);

        if(this.nearbyStars.size() > 0) {
            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.nearbyStars.get(0).shadowMap.depthMap);
        }

        Shader.terrainShader.uploadVec3f("playerPositionInChunk", new Vector3f((float) (SpaceGame.instance.save.thePlayer.x % 32), (float) (SpaceGame.instance.save.thePlayer.y % 32), (float) (SpaceGame.instance.save.thePlayer.z % 32)));
        Shader.worldShaderTextureArray.uploadVec3f("playerPositionInChunk", new Vector3f((float) (SpaceGame.instance.save.thePlayer.x % 32), (float) (SpaceGame.instance.save.thePlayer.y % 32), (float) (SpaceGame.instance.save.thePlayer.z % 32)));
        Shader.worldShader2DTexture.uploadVec3f("playerPositionInChunk", new Vector3f((float) (SpaceGame.instance.save.thePlayer.x % 32), (float) (SpaceGame.instance.save.thePlayer.y % 32), (float) (SpaceGame.instance.save.thePlayer.z % 32)));
        Shader.terrainShader.uploadInt("shadowMap", 1);
        Shader.terrainShader.uploadMat4d("uProjection", SpaceGame.camera.projectionMatrix);
        Shader.terrainShader.uploadMat4d("uView", SpaceGame.camera.viewMatrix);
        Shader.terrainShader.uploadInt("textureArray", 0);
        Shader.terrainShader.uploadBoolean("useFog", true);
        Shader.terrainShader.uploadFloat("fogDistance", GameSettings.renderDistance * 20f);
        Shader.terrainShader.uploadFloat("fogRed", this.controller.parentWorld.skyColor[0]);
        Shader.terrainShader.uploadFloat("fogGreen", this.controller.parentWorld.skyColor[1]);
        Shader.terrainShader.uploadFloat("fogBlue", this.controller.parentWorld.skyColor[2]);
        Shader.terrainShader.uploadDouble("time", (double) Timer.elapsedTime % 8388608);

        Chunk chunk;
        int xOffset;
        int yOffset;
        int zOffset;
        int playerChunkX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x) >> 5;
        int playerChunkY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z) >> 5;

        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);

        for (int i = 0; i < sortedChunks.length; i++) {
            chunk = sortedChunks[i];

            xOffset = (chunk.x - playerChunkX) << 5;
            yOffset = (chunk.y - playerChunkY) << 5;
            zOffset = (chunk.z - playerChunkZ) << 5;

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
                chunk.renderOpaque(xOffset, yOffset, zOffset,this.sunX,this.sunY,this.sunZ);
                GL46.glEndQuery(GL46.GL_ANY_SAMPLES_PASSED);

                if (chunk.doesChunkContainEntities()) {
                    chunksThatContainEntities.add(chunk);
                }
            }

            if (chunk.vertexBufferTransparent != null && chunk.vertexBufferTransparent.limit() != 0) {
                chunksToRender.add(chunk);
            }
        }

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        for (Chunk transparentChunk : chunksToRender) {
            this.controller.drawCalls++;
            transparentChunk.renderTransparent(this.sunX,this.sunY,this.sunZ);
        }

        GL46.glDisable(GL46.GL_BLEND);
        GL46.glDisable(GL46.GL_ALPHA_TEST);

        //Texture unit 1 is still the active texture from the shadowmap
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindVertexArray(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);
        GL46.glDisable(GL46.GL_CULL_FACE);
        GL46.glUseProgram(0);

        for (Chunk entityChunk : chunksThatContainEntities) {
            entityChunk.renderEntities(this.sunX, this.sunY, this.sunZ);
        }

        this.chunksToRender.clear();
        this.renderSkybox(SpaceGame.instance.everything.getObjectAssociatedWithWorld(this.controller.parentWorld), playerLon, playerLat);
        for(int i = 0; i < this.nearbyStarPos.size(); i++) {
            this.renderSunrise(new Vector3f(this.nearbyStarPos.get(i)).normalize().y, this.nearbyStarPos.get(i));
        }

        if(this.frameSinceFrustumRecalc == 2){
            this.renderWithChunks = false;
            this.frameSinceFrustumRecalc = 0;

            int opaqueChunkCount = 0;
            int transparentChunkCount = 0;
            for(int i = 0; i < sortedChunks.length; i++){
                if(!sortedChunks[i].occluded && sortedChunks[i].elementBufferOpaque != null){
                    if(sortedChunks[i].elementBufferOpaque.limit() != 0) {
                        opaqueChunkCount++;
                    }
                }

                if(sortedChunks[i].elementBufferTransparent != null){
                    if(sortedChunks[i].elementBufferTransparent.limit() != 0) {
                        transparentChunkCount++;
                    }
                }
            }

            this.opaqueChunks = new int[opaqueChunkCount * 10]; //Multiplied by the number of ints needed for the renderer
            this.transparentChunks = new int[transparentChunkCount * 10];



            int sunOffsetX;
            int sunOffsetY;
            int sunOffsetZ;

            int opaqueIndex = 0;
            int transparentIndex = 0;
            int baseIndex = 0;

            for(int i = 0; i < sortedChunks.length; i++){
                xOffset = (sortedChunks[i].x - playerChunkX) << 5;
                yOffset = (sortedChunks[i].y - playerChunkY) << 5;
                zOffset = (sortedChunks[i].z - playerChunkZ) << 5;

                sunOffsetX = (sortedChunks[i].x - this.sunX) << 5;
                sunOffsetY = (sortedChunks[i].y - this.sunY) << 5;
                sunOffsetZ = (sortedChunks[i].z - this.sunZ) << 5;


                if(!sortedChunks[i].occluded && sortedChunks[i].elementBufferOpaque != null){
                    if(sortedChunks[i].elementBufferOpaque.limit() != 0) {
                        baseIndex = opaqueIndex * 10;
                        opaqueIndex++;
                        this.opaqueChunks[baseIndex] = sortedChunks[i].opaqueVBOID;
                        this.opaqueChunks[baseIndex + 1] = sortedChunks[i].opaqueEBOID;
                        this.opaqueChunks[baseIndex + 2] = xOffset;
                        this.opaqueChunks[baseIndex + 3] = yOffset;
                        this.opaqueChunks[baseIndex + 4] = zOffset;
                        this.opaqueChunks[baseIndex + 5] = sunOffsetX;
                        this.opaqueChunks[baseIndex + 6] = sunOffsetY;
                        this.opaqueChunks[baseIndex + 7] = sunOffsetZ;
                        this.opaqueChunks[baseIndex + 8] = sortedChunks[i].elementBufferOpaque.limit();
                        this.opaqueChunks[baseIndex + 9] = sortedChunks[i].opaqueVAOID;
                    }
                }

                if(sortedChunks[i].elementBufferTransparent != null){
                    if(sortedChunks[i].elementBufferTransparent.limit() != 0) {
                        baseIndex = transparentIndex * 10;
                        transparentIndex++;
                        this.transparentChunks[baseIndex] = sortedChunks[i].transparentVBOID;
                        this.transparentChunks[baseIndex + 1] = sortedChunks[i].transparentEBOID;
                        this.transparentChunks[baseIndex + 2] = xOffset;
                        this.transparentChunks[baseIndex + 3] = yOffset;
                        this.transparentChunks[baseIndex + 4] = zOffset;
                        this.transparentChunks[baseIndex + 5] = sunOffsetX;
                        this.transparentChunks[baseIndex + 6] = sunOffsetY;
                        this.transparentChunks[baseIndex + 7] = sunOffsetZ;
                        this.transparentChunks[baseIndex + 8] = sortedChunks[i].elementBufferTransparent.limit();
                        this.transparentChunks[baseIndex + 9] = sortedChunks[i].transparentVAOID;
                    }
                }
            }
        }
    }

    public void renderWorldWithoutChunks(){
        Shader.terrainShader.uploadMat4d("uView", SpaceGame.camera.viewMatrix);
        for(int i = 0; i < this.nearbyStars.size(); i++){
            this.setShadowMap(this.nearbyStars.get(i),this.nearbyStarPos.get(i));
        }


        this.nearbyStars.clear();
        this.nearbyStarPos.clear();

        this.renderNearbyCelestialObjects();

        if (SpaceGame.instance.currentGui instanceof GuiInGame) {
            GuiInGame.renderBlockOutline();
            GuiInGame.renderBlockBreakingOutline();
        }


        this.controller.drawCalls = 0;

        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_FRONT);
        //The block array is bound to texture 0, all shadowmaps are bound from texture unit 1 up, they must all be properly active and unbound during cleanup
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, Assets.blockTextureArray);

        GL46.glUseProgram(Shader.terrainShader.shaderProgramID);

        if(this.nearbyStars.size() > 0) {
            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.nearbyStars.get(0).shadowMap.depthMap);
        }

        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);

        //Opaque
        for(int i = 0 ; i < this.opaqueChunks.length; i += 10){ //These arrays must ensure that the objects are not null pointers
            Shader.terrainShader.uploadVec3f("chunkOffset", this.opaqueChunks[i + 2], this.opaqueChunks[i + 3], this.opaqueChunks[i + 4]);
            Shader.terrainShader.uploadVec3f("sunChunkOffset", this.opaqueChunks[i + 5], this.opaqueChunks[i + 6], this.opaqueChunks[i + 7]);
            GL46.glBindVertexArray(this.opaqueChunks[i + 9]);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.opaqueChunks[i]);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.opaqueChunks[i + 1]);
            GL46.glDrawElements(GL46.GL_TRIANGLES, this.opaqueChunks[i + 8], GL46.GL_UNSIGNED_INT, 0);
            this.controller.drawCalls++;
        }

        for(int i = 0; i < this.chunksThatContainEntities.size(); i++){
            chunksThatContainEntities.get(i).renderEntities(this.sunX, this.sunY, this.sunZ);
        }

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        //transparent
        for(int i = 0 ; i < this.transparentChunks.length; i += 10){
            Shader.terrainShader.uploadVec3f("chunkOffset", this.transparentChunks[i + 2], this.transparentChunks[i + 3], this.transparentChunks[i + 4]);
            Shader.terrainShader.uploadVec3f("sunChunkOffset", this.transparentChunks[i + 5], this.transparentChunks[i + 6], this.transparentChunks[i + 7]);
            GL46.glBindVertexArray(this.transparentChunks[i + 9]);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.transparentChunks[i]);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.transparentChunks[i + 1]);
            GL46.glDrawElements(GL46.GL_TRIANGLES, this.transparentChunks[i + 8], GL46.GL_UNSIGNED_INT, 0);
            this.controller.drawCalls++;
        }



        GL46.glDisable(GL46.GL_BLEND);
        GL46.glDisable(GL46.GL_ALPHA_TEST);

        //Texture unit 1 is still the active texture from the shadowmap
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glActiveTexture(GL46.GL_TEXTURE0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);
        GL46.glDisable(GL46.GL_CULL_FACE);


        this.renderSkybox(SpaceGame.instance.everything.getObjectAssociatedWithWorld(this.controller.parentWorld), playerLon, playerLat);
        for(int i = 0; i < this.nearbyStarPos.size(); i++) {
            this.renderSunrise(new Vector3f(this.nearbyStarPos.get(i)).normalize().y, this.nearbyStarPos.get(i));
        }

    }

    private void setShadowMap(Sun sun, Vector3f dir){
        if(this.hasTickPassed) {
            Vector3f normalizedDir = dir.normalize();
            Shader.terrainShader.uploadVec3f("normalizedLightVector", dir); //This needs be called in order to set the direction vector even if shadows are turned off otherwise vertex normals will not work
            if (GameSettings.shadowMap) {
                float lightDist = 3000f;
                float orthoSize = 256;
                Matrix4f sunProjectionMatrix = new Matrix4f().setOrtho(-orthoSize, orthoSize, -orthoSize, orthoSize, 1, 10000);
                Matrix4f sunViewMatrix = new Matrix4f();
                Vector3d sunPosition = new Vector3d(normalizedDir.x, normalizedDir.y, normalizedDir.z);
                sunPosition.mul(lightDist);
                float texelSize = ((orthoSize * 2)) / (float) sun.shadowMap.width;
                sunPosition.x = MathUtil.floorDouble(sunPosition.x / texelSize) * texelSize; //a smaller texel size is needed to reduce the jitter of the shadow as the sun moves. The tradeoff being the frustum of the projection matrix can only see a very small part of the world
                sunPosition.y = MathUtil.floorDouble(sunPosition.y / texelSize) * texelSize;
                sunPosition.z = MathUtil.floorDouble(sunPosition.z / texelSize) * texelSize;


                sunViewMatrix.identity();
                sunViewMatrix = sunViewMatrix.lookAt(new Vector3f((float) sunPosition.x, (float) sunPosition.y, (float) sunPosition.z), new Vector3f(), new Vector3f(0.0f, 1.0f, 0.0f));
                sunPosition.add(new Vector3d(SpaceGame.instance.save.thePlayer.x, SpaceGame.instance.save.thePlayer.y, SpaceGame.instance.save.thePlayer.z));

                int sunX = MathUtil.floorDouble(sunPosition.x) >> 5;
                int sunY = MathUtil.floorDouble(sunPosition.y) >> 5;
                int sunZ = MathUtil.floorDouble(sunPosition.z) >> 5;

                Matrix4f frustum = new Matrix4f();
                frustum.set(sunProjectionMatrix);
                frustum.mul(sunViewMatrix);
                FrustumIntersection frustumInt = new FrustumIntersection();
                frustumInt.set(frustum);

                Matrix4f lightViewProjectionMatrix = sunProjectionMatrix.mul(sunViewMatrix);
                Shader.terrainShader.uploadMat4f("lightViewProjectionMatrix", lightViewProjectionMatrix);
                Shader.worldShaderTextureArray.uploadMat4f("lightViewProjectionMatrix", lightViewProjectionMatrix);
                Shader.worldShaderTextureArray.uploadVec3f("normalizedLightVector", dir);
                Shader.worldShader2DTexture.uploadMat4f("lightViewProjectionMatrix", lightViewProjectionMatrix);
                Shader.worldShader2DTexture.uploadVec3f("normalizedLightVector", dir);


                this.sunX = sunX;
                this.sunY = sunY;
                this.sunZ = sunZ;

                Shader.shadowMapShader.uploadDouble("time", (double) Timer.elapsedTime % 8388608);
                GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, sun.shadowMap.fboID);
                GL46.glViewport(0, 0, sun.shadowMap.width, sun.shadowMap.height);
                GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);
                Shader.shadowMapShader.uploadMat4f("combinedViewProjectionMatrix", lightViewProjectionMatrix);
                GL46.glUseProgram(Shader.shadowMapShader.shaderProgramID);
                Shader.shadowMapShader.uploadInt("textureArray", 0);

                GL46.glEnable(GL46.GL_ALPHA_TEST);
                GL46.glAlphaFunc( GL46.GL_GREATER, 0.1f);

                GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, Assets.blockTextureArray);
                int xOffset;
                int yOffset;
                int zOffset;
                for (int i = 0; i < this.controller.regions.length; i++) {
                    if (this.controller.regions[i] == null) continue;
                    for (int j = 0; j < this.controller.regions[i].chunks.length; j++) {
                        if (this.controller.regions[i].chunks[j] == null) continue;
                        if (!this.controller.regions[i].chunks[j].shouldRender) continue;
                        xOffset = (this.controller.regions[i].chunks[j].x - sunX) << 5;
                        yOffset = (this.controller.regions[i].chunks[j].y - sunY) << 5;
                        zOffset = (this.controller.regions[i].chunks[j].z - sunZ) << 5;
                        if (!this.doesChunkIntersectSunFrustum(frustumInt, xOffset, yOffset, zOffset, ((xOffset + 31)), ((yOffset + 31)), ((zOffset + 31))))
                            continue;

                        this.controller.regions[i].chunks[j].renderShadowMap(sunX, sunY, sunZ);
                    }
                }

                GL46.glUseProgram(0);
                GL46.glBindVertexArray(0);
                GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

                GL46.glDisable(GL46.GL_ALPHA_TEST);

                GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
                GL46.glViewport(0, 0, SpaceGame.width, SpaceGame.height);
                this.hasTickPassed = false;
            }
        }
    }

    private boolean doesChunkIntersectSunFrustum(FrustumIntersection frustumIntersection, float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
        int intersect = frustumIntersection.intersectAab(minX, minY, minZ, maxX, maxY, maxZ);
        return intersect == -1 || intersect == -2;
    }

    public void renderNearbyCelestialObjects(){
        Matrix4d preservedViewMatrix = SpaceGame.camera.viewMatrix.get(new Matrix4d());
        Quaterniond viewMatrixRotation = SpaceGame.camera.viewMatrix.getUnnormalizedRotation(new Quaterniond());
        SpaceGame.camera.viewMatrix = new Matrix4d();
        SpaceGame.camera.viewMatrix.rotate(viewMatrixRotation);
        int worldSizeRadius = SpaceGame.instance.save.activeWorld.size / 2;
        double playerLat = 0;
        double playerLon = 0;
        double zThreshold = (double) (worldSizeRadius * 2) / 360;

        playerLat = -(SpaceGame.instance.save.thePlayer.x / (double)worldSizeRadius) * 90;
        playerLon = ((worldSizeRadius + SpaceGame.instance.save.thePlayer.z) / zThreshold);

        ArrayList<Vector3f> starPositions = new ArrayList<>();
        CelestialObject currentCelestialObject = SpaceGame.instance.everything.getObjectAssociatedWithWorld(this.controller.parentWorld);
        this.playerLat = playerLat;
        this.playerLon = playerLon;
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

                float zRotation = (float) ((float)  Math.toRadians(playerLat) + Math.toRadians((currentCelestialObject.axialTiltX)));
                float orbitRatio = (float) MathUtil.sin(((double) (SpaceGame.instance.save.time % currentCelestialObject.orbitalPeriod) / currentCelestialObject.orbitalPeriod) * 2 * Math.PI);

                zRotation *= orbitRatio;
                // MathUtils.sin(((double) (SpaceGame.instance.save.time % currentCelestialObject.orbitalPeriod) / currentCelestialObject.orbitalPeriod) * 2 * Math.PI)))
                SpaceGame.instance.save.activeWorld.sunAngle = zRotation;
                celestialObjectPosition.rotateZ(zRotation);

                float absoluteDistance = celestialObjectPosition.distance(new Vector3f());
                float latRatio = (float) (playerLat / 90f);

                celestialObjectPosition.y += ((absoluteDistance * latRatio) * orbitRatio);
                orbitRatio = (float) MathUtil.sin(((double) ((SpaceGame.instance.save.time % currentCelestialObject.orbitalPeriod) / currentCelestialObject.orbitalPeriod) * 2 * Math.PI) + (0.5f * Math.PI));

                if(playerLat > 0.0){
                    celestialObjectPosition.x += (celestialObjectPosition.x * ((latRatio) * orbitRatio));
                    celestialObjectPosition.z += (celestialObjectPosition.z *  ((latRatio) * orbitRatio));
                } else {
                    celestialObjectPosition.x -= (celestialObjectPosition.x * ((latRatio) * orbitRatio));
                    celestialObjectPosition.z -= (celestialObjectPosition.z *  ((latRatio) * orbitRatio));
                }

                //Take the value for calculating the rotation z and use it to determine the angle of the sun relative to being directly overhead, use to determine temp falloff, logarithm function
                if(renderingObject instanceof Sun){
                    starPositions.add(new Vector3f(celestialObjectPosition));
                    this.nearbyStarPos.add(new Vector3f(celestialObjectPosition));
                    this.nearbyStars.add((Sun) renderingObject);
                }

                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                float rotationAmountY = (float) Math.toRadians( 360 * (double) (SpaceGame.instance.save.time % renderingObject.rotationPeriod) /renderingObject.rotationPeriod);
                float rotationAmountX = (float) Math.toRadians(renderingObject.axialTiltX);
                float rotationAmountZ = (float) Math.toRadians(renderingObject.axialTiltZ);

                if(SpaceGame.camera.doesSphereIntersectFrustum(celestialObjectPosition.x, celestialObjectPosition.y, celestialObjectPosition.z, (currentCelestialObject.radius * 0.000000)  * 2)) {
                    RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
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
                                tessellator.addVertexCubeMap(this.getSunColor(), vertex4.x + celestialObjectPosition.x, vertex4.y + celestialObjectPosition.y, vertex4.z + celestialObjectPosition.z); //All non star objects need to have their lighting values calculated
                                tessellator.addVertexCubeMap(this.getSunColor(), vertex1.x + celestialObjectPosition.x, vertex1.y + celestialObjectPosition.y, vertex1.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(this.getSunColor(), vertex2.x + celestialObjectPosition.x, vertex2.y + celestialObjectPosition.y, vertex2.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(this.getSunColor(), vertex3.x + celestialObjectPosition.x, vertex3.y + celestialObjectPosition.y, vertex3.z + celestialObjectPosition.z);
                                tessellator.addElements();
                            } else {
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex4, this.getSunColor(), starPositions), vertex4.x + celestialObjectPosition.x, vertex4.y + celestialObjectPosition.y, vertex4.z + celestialObjectPosition.z); //All non star objects need to have their lighting values calculated
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex1, this.getSunColor(), starPositions), vertex1.x + celestialObjectPosition.x, vertex1.y + celestialObjectPosition.y, vertex1.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex2, this.getSunColor(), starPositions), vertex2.x + celestialObjectPosition.x, vertex2.y + celestialObjectPosition.y, vertex2.z + celestialObjectPosition.z);
                                tessellator.addVertexCubeMap(this.calculateLighting(vertex3, this.getSunColor(), starPositions), vertex3.x + celestialObjectPosition.x, vertex3.y + celestialObjectPosition.y, vertex3.z + celestialObjectPosition.z);
                                tessellator.addElements();
                            }
                        }
                    }
                    Shader.worldShaderCubeMapTexture.uploadVec3f("position", celestialObjectPosition);
                    if(this.blendCelestialObjects) {
                        GL46.glEnable(GL46.GL_BLEND);
                        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE);
                    }
                    GL46.glEnable(GL46.GL_CULL_FACE);
                    GL46.glCullFace(GL46.GL_FRONT);
                    tessellator.drawCubeMapTexture(renderingObject.mappedTexture, Shader.worldShaderCubeMapTexture, SpaceGame.camera);
                    GL46.glDisable(GL46.GL_CULL_FACE);
                    if(this.blendCelestialObjects) {
                        GL46.glDisable(GL46.GL_BLEND);
                    }
                }

                if(renderingObject instanceof Sun){ //Sun corona
                    RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                    float size = 100000000F * 0.0001F;
                    Quaterniond inverseRotation = new Quaterniond(viewMatrixRotation).invert();

                    Vector3d vertex1SunFlare = new Vector3d(-size, -size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    Vector3d vertex2SunFlare = new Vector3d(size, size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    Vector3d vertex3SunFlare = new Vector3d(-size, size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    Vector3d vertex4SunFlare = new Vector3d(size, -size, 0).rotate(inverseRotation).add(celestialObjectPosition);
                    tessellator.addVertex2DTexture(this.getSunColor(), (float) vertex1SunFlare.x, (float) vertex1SunFlare.y, (float) vertex1SunFlare.z, 3); //Lighting doesnt need to be calculated because this is the light source in the system
                    tessellator.addVertex2DTexture(this.getSunColor(), (float) vertex2SunFlare.x, (float) vertex2SunFlare.y, (float) vertex2SunFlare.z, 1);
                    tessellator.addVertex2DTexture(this.getSunColor(), (float) vertex3SunFlare.x, (float) vertex3SunFlare.y, (float) vertex3SunFlare.z, 2);
                    tessellator.addVertex2DTexture(this.getSunColor(), (float) vertex4SunFlare.x, (float) vertex4SunFlare.y, (float) vertex4SunFlare.z, 0);
                    tessellator.addElements();
                    GL46.glEnable(GL46.GL_BLEND);
                    GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_COLOR);
                    Shader.worldShader2DTexture.uploadBoolean("useFog", false);
                    tessellator.drawTexture2D(Sun.sunFlare, Shader.worldShader2DTexture, SpaceGame.camera);
                    GL46.glDisable(GL46.GL_BLEND);
                }
            }
        }
        this.setSkyLightLevel(starPositions);
        SpaceGame.camera.viewMatrix = preservedViewMatrix;
    }

    private int getSunColor(){
        int red = (int) (this.sunRed * 255);
        int green = (int) (this.sunGreen * 255);
        int blue = (int) (this.sunBlue * 255);

        return (red << 16) | (green << 8) | blue;
    }

    private void renderSkybox(CelestialObject currentCelestialObject, double playerLon, double playerLat) {
        if (this.shouldSkyboxRender) {
            GL46.glDepthMask(false);
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_COLOR);

            RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
            Vector3f vertex1;
            Vector3f vertex2;
            Vector3f vertex3;
            Vector3f vertex4;
            Matrix4f modelMatrix = new Matrix4f();
            modelMatrix.rotateX((float) -(Math.toRadians(playerLon) + Math.toRadians((360 * ((double) SpaceGame.instance.save.time / currentCelestialObject.rotationPeriod)) % 360)));
            modelMatrix.rotateZ(-(float) ((float)  Math.toRadians(playerLat) + Math.toRadians(currentCelestialObject.axialTiltX)));
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
            Shader.worldShaderCubeMapTexture.uploadMat4f("uModel", modelMatrix);
            tessellator.drawCubeMapTexture(GuiUniverseMap.skybox, Shader.worldShaderCubeMapTexture, SpaceGame.camera);
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
            this.controller.parentWorld.skyLightLevel = 0;
            return;
        }

        closestStar.normalize();

        byte calculatedSkyLightLevel = this.calculateSkyLightLevel(closestStar.y);
        this.setClearColor(closestStar.y);
        this.shouldSkyboxRender = this.shouldSkyboxRender(closestStar.y);
        this.blendCelestialObjects = this.shouldCelestialObjectsBlend(closestStar.y); //This uses the exact same logic
        this.shouldShadowsRender = this.shouldShadowsRender(closestStar.y);
        Shader.terrainShader.uploadBoolean("renderShadows", this.shouldShadowsRender);
        Shader.terrainShader.uploadBoolean("performNormals", this.shouldShadowsRender);
        Shader.worldShaderTextureArray.uploadBoolean("renderShadows", this.shouldShadowsRender);
        Shader.worldShaderTextureArray.uploadBoolean("performNormals", this.shouldShadowsRender);
        Shader.worldShader2DTexture.uploadBoolean("performNormals", this.shouldShadowsRender);
        Shader.worldShader2DTexture.uploadBoolean("renderShadows", this.shouldShadowsRender);
        this.setBaseLight(closestStar.y);
        this.setLightColorInShader(closestStar.y);
        this.controller.parentWorld.skyLightLevel = calculatedSkyLightLevel;
    }

    private boolean shouldSkyboxRender(float yVecComponent){
        return !(yVecComponent >= -0.1f);
    }

    private boolean shouldCelestialObjectsBlend(float yVecComponent){
        return (yVecComponent >= -0.2f);
    }

    private boolean shouldShadowsRender(float yVecComponent){
        return yVecComponent >= -0.2f;
    }


    private void setBaseLight(float yVecComponent){ //Base light only decreases when the vector is below 0, otherwise it is 1. if it is below 0 decrease it logarithmically between 0 and -0.2 minimum value should be 0.1 corresponding shadow math will be in the terrain shader
        float baseLight = 1;
        if(yVecComponent > 0){
            baseLight = 1;
        } else if (yVecComponent < -0.2) { //Range is from 0 to 1.36943841 radians.
            baseLight = 0.1f;
        } else {
            float minAngle = 1.36943841f;
            float maxAngle = (float) Math.PI / 2f;
            float normalized = (float) ((Math.acos(yVecComponent * -1) - minAngle) / (maxAngle - minAngle));
            baseLight = 0.1f + (1.0f - (float) Math.log10(1.0f + 9.0f * (1.0f - normalized)) / (float) Math.log10(10.0f)) * 0.9f;
        }

        Shader.terrainShader.uploadFloat("baseLight", baseLight);
        Shader.worldShaderTextureArray.uploadFloat("baseLight", baseLight);
        Shader.worldShader2DTexture.uploadFloat("baseLight", baseLight);
        this.baseLight = baseLight;
    }

    private void setLightColorInShader(float yVecComponent) {
        float upperColorR = 1;
        float upperColorG = 1;
        float upperColorB = 1;
        float lowerColorR = 1;
        float lowerColorG = 1;
        float lowerColorB = 1;

        if(yVecComponent <= 0.15 && yVecComponent > 0.08){
            upperColorR = 1;
            upperColorG = 1;
            upperColorB = 1;
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
            lowerColorR = 255f / 255f;
            lowerColorG = 77f / 255f;
            lowerColorB = 53f / 255f;
        } else if(yVecComponent <= -0.13 && yVecComponent > -0.2){
            upperColorR = 255f / 255f;
            upperColorG = 77f / 255f;
            upperColorB = 53f / 255f;
            lowerColorR = 1;
            lowerColorG = 1;
            lowerColorB = 1;
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

        Vector4f lightColor = new Vector4f(interpolatedR, interpolatedG, interpolatedB, 1.0f);

        Shader.terrainShader.uploadVec4f("lightColor",lightColor);
        Shader.worldShaderTextureArray.uploadVec4f("lightColor", lightColor);
        Shader.worldShader2DTexture.uploadVec4f("lightColor", lightColor);
        this.sunRed = interpolatedR;
        this.sunGreen = interpolatedG;
        this.sunBlue = interpolatedB;
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
        float upperColorR = 1;
        float upperColorG = 1;
        float upperColorB = 1;
        float lowerColorR = 1;
        float lowerColorG = 1;
        float lowerColorB = 1;

        if(yVecComponent <= 0.15 && yVecComponent > 0.08){
            upperColorR = this.controller.parentWorld.defaultSkyColor[0];
            upperColorG = this.controller.parentWorld.defaultSkyColor[1];
            upperColorB = this.controller.parentWorld.defaultSkyColor[2];
            lowerColorR = 255f / 255f;
            lowerColorG = 233f / 255f;
            lowerColorB = 127f / 255f;
        } else if(yVecComponent <= 0.08 && yVecComponent > 0.01){
            upperColorR = 255f / 255f;
            upperColorG = 233f / 255f;
            upperColorB = 127f / 255f;
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
            lowerColorR = 255f / 255f;
            lowerColorG = 77f / 255f;
            lowerColorB = 53f / 255f;
        } else if(yVecComponent <= -0.13 && yVecComponent > -0.2){
            upperColorR = 255f / 255f;
            upperColorG = 77f / 255f;
            upperColorB = 53f / 255f;
            lowerColorR = 0;
            lowerColorG = 0;
            lowerColorB = 0;
        } else if(yVecComponent > 0.15){
            upperColorR = this.controller.parentWorld.defaultSkyColor[0];
            upperColorG = this.controller.parentWorld.defaultSkyColor[1];
            upperColorB = this.controller.parentWorld.defaultSkyColor[2];
            lowerColorR = this.controller.parentWorld.defaultSkyColor[0];
            lowerColorG = this.controller.parentWorld.defaultSkyColor[1];
            lowerColorB = this.controller.parentWorld.defaultSkyColor[2];
        } else if(yVecComponent < -0.2){
            upperColorR = 0;
            upperColorG = 0;
            upperColorB = 0;
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

        float interpolatedR = (lowerColorR + (colorDifR * ratio)) * this.baseLight;
        float interpolatedG = (lowerColorG + (colorDifG * ratio)) * this.baseLight;
        float interpolatedB = (lowerColorB + (colorDifB * ratio)) * this.baseLight;
        this.controller.parentWorld.skyColor[0] = interpolatedR;
        this.controller.parentWorld.skyColor[1] = interpolatedG;
        this.controller.parentWorld.skyColor[2] = interpolatedB;
        SpaceGame.setGLClearColor( this.controller.parentWorld.skyColor[0],  this.controller.parentWorld.skyColor[1],  this.controller.parentWorld.skyColor[2], 0.0f);
    }

    private void renderSunrise(float yVecComponent, Vector3f starPosition){
        if(!(yVecComponent >= 0.15 || yVecComponent <= -0.2)) {
            starPosition.mul(0.02f);
            RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
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
            tessellator.drawTexture2D(Sun.sunFlare, Shader.worldShader2DTexture, SpaceGame.camera);
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
            lowerColorR = 255f / 255f;
            lowerColorG = 233f / 255f;
            lowerColorB = 127f / 255f;
        } else if(yVecComponent <= 0.08 && yVecComponent > 0.01){
            upperColorR = 255f / 255f;
            upperColorG = 233f / 255f;
            upperColorB = 127f / 255f;
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
        R *= 0.00000001;
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtil.cos(latRad) * MathUtil.cos(lonRad));
        position.y = (float) (R * MathUtil.sin(latRad));
        position.z = (float) (R * MathUtil.cos(latRad) * MathUtil.sin(lonRad));
        return position;
    }

    public Vector3f getPositionOnSphereNonStar(double latitude, double longitude, double R){
        R *= 0.0001;
        Vector3f position = new Vector3f();
        float latRad = (float) Math.toRadians(latitude);
        float lonRad = (float) Math.toRadians(longitude);
        position.x = (float) (R * MathUtil.cos(latRad) * MathUtil.cos(lonRad));
        position.y = (float) (R * MathUtil.sin(latRad));
        position.z = (float) (R * MathUtil.cos(latRad) * MathUtil.sin(lonRad));
        return position;
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


        float intensity = Math.max(0, (float) MathUtil.cos(Math.toRadians(angle)));

        int red = (baseColor >> 16) & 0xFF;
        int green = (baseColor >> 8) & 0xFF;
        int blue = baseColor & 0xFF;

        red = Math.min(255, Math.max(0, (int)(red * intensity)));
        green = Math.min(255, Math.max(0, (int)(green * intensity)));
        blue = Math.min(255, Math.max(0, (int)(blue * intensity)));

        return (red << 16) | (green << 8) | blue;
    }

}
