package spacegame.world;

import org.joml.Vector3d;
import org.joml.Vector3f;
import spacegame.block.*;
import spacegame.core.*;
import spacegame.entity.*;
import spacegame.gui.*;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.render.RenderWorldScene;
import spacegame.render.Shader;
import spacegame.util.MathUtil;
import spacegame.world.weather.Cloud;
import spacegame.world.weather.CloudFormation;
import spacegame.world.weather.RainQuad;
import spacegame.world.weather.WeatherSystem;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class World {
    public int[] activeBlockLight = new int[3];
    public ArrayList<int[]> lightUpdateQueue = new ArrayList<>();
    public ArrayList<int[]> previouslyQueuedLightUpdate = new ArrayList<>();
    public ArrayList<int[]> darknessUpdateQueue = new ArrayList<>();
    public ArrayList<int[]> previouslyQueuedDarknessUpdate = new ArrayList<>();
    public ArrayList<int[]> lightSearchQueue = new ArrayList<>();
    public ArrayList<int[]> previousLightSearchQueue = new ArrayList<>();
    public ArrayList<int[]> roomCheckQueue = new ArrayList<>();
    public ArrayList<int[]> previousRoomCheckQueue = new ArrayList<>();
    public ArrayList<WeatherSystem> activeWeatherSystems = new ArrayList<>();
    public ArrayList<WeatherSystem> inactiveWeatherSystems = new ArrayList<>();
    public static final int CLOUD_LIMIT = 500;
    public int cloudCount;
    public int safetyThreshold = 0;
    public int resetLightX;
    public int resetLightY;
    public int resetLightZ;
    public byte delayWhenExitingUI;
    public float sunAngle;
    public CosmicEvolution ce;
    public byte skyLightLevel;
    public float[] skyColor; //used for glClear on the color buffer
    public float[] defaultSkyColor;
    public float[] skyLightColor; //Do not make any RGB component 0
    public final int size;
    public static volatile int worldLoadPhase = 0;
    public static int noiseMapsCompleted = 0;
    public static int totalMaps;
    public File worldFolder;
    public ChunkController chunkController;
    public boolean paused = false;
    public boolean raining;
    public boolean prevRaining;
    public long timeStartedRaining;
    public float averagePrecipitation;
    public float averageStrength;
    public float refWindDirection;
    public float refWindIntensity;
    public float windDirection;
    public float windIntensity;
    public float targetWindDirection;
    public float targetWindIntensity;
    public boolean windy;


    public World(CosmicEvolution cosmicEvolution, int size) {
        this.ce = cosmicEvolution;
        this.size = size;
        this.chunkController = new ChunkController(this);
        Cloud.texture = cosmicEvolution.renderEngine.createTexture("src/spacegame/assets/textures/misc/cloud.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        RenderWorldScene.rainTexture = cosmicEvolution.renderEngine.createTexture("src/spacegame/assets/textures/misc/rainTexture.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    public void tick() {
        WeatherSystem weatherSystem;
        CloudFormation cloudFormation;
        Cloud cloud;
        float totalPrecipitation = 0;
        float totalStrength = 0;
        int cloudCount = 0;
        this.cloudCount = 0;
        this.chunkController.renderWorldScene.cloudy = false;
        for (int i = 0; i < this.activeWeatherSystems.size(); i++) {
            weatherSystem = this.activeWeatherSystems.get(i);
            weatherSystem.update();
            if(this.ce.save.time >= weatherSystem.killTime){
                this.activeWeatherSystems.remove(weatherSystem);
            }

            for(int j = 0; j < weatherSystem.cloudFormations.size(); j++){
                cloudFormation = weatherSystem.cloudFormations.get(j);

                if(cloudFormation.centralCloud != null){
                    cloud = cloudFormation.centralCloud;

                        if(cloud.strength >= cloud.maxStrength) {
                            totalPrecipitation += cloud.precipitation;
                            totalStrength += cloud.strength;

                            cloudCount++;
                        }

                }

                for(int k = 0; k < cloudFormation.clouds.length; k++){
                    if(cloudFormation.clouds[k] == null)continue;

                    cloud = cloudFormation.clouds[k];

                        if(cloud.strength >= cloud.maxStrength) {
                            totalPrecipitation += cloud.precipitation;
                            totalStrength += cloud.strength;

                            cloudCount++;
                        }

                }
            }
        }

        float averagePrecipitation = totalPrecipitation / (float)cloudCount;
        float averageStrength = totalStrength / (float)cloudCount;

        this.averagePrecipitation = averagePrecipitation;
        this.averageStrength = averageStrength;

        this.chunkController.renderWorldScene.cloudy = averagePrecipitation > 0.5f && averageStrength > 0.25f;

        this.raining = this.chunkController.renderWorldScene.overrideSkyColor && averagePrecipitation > 0.5f && averageStrength > 0.5f;

        if(this.prevRaining != this.raining) {
            this.timeStartedRaining = this.ce.save.time;
        }

        if(this.chunkController.renderWorldScene.cloudy != this.chunkController.renderWorldScene.prevCloudy){
            if(this.chunkController.renderWorldScene.cloudy){
                this.chunkController.renderWorldScene.transitionSkyColor = true;

                float colorVal = (1 - (averagePrecipitation * 0.75f));

                this.chunkController.renderWorldScene.targetSkyColor[0] = colorVal;
                this.chunkController.renderWorldScene.targetSkyColor[1] = colorVal;
                this.chunkController.renderWorldScene.targetSkyColor[2] = colorVal;

                this.chunkController.renderWorldScene.originalSkyColor[0] = this.skyColor[0];
                this.chunkController.renderWorldScene.originalSkyColor[1] = this.skyColor[1];
                this.chunkController.renderWorldScene.originalSkyColor[2] = this.skyColor[2];

                this.chunkController.renderWorldScene.timeStartedSkyColorTransition = this.ce.save.time;
            }
            if(!this.chunkController.renderWorldScene.cloudy) {
                this.chunkController.renderWorldScene.transitionSkyColor = true;

                this.chunkController.renderWorldScene.targetSkyColor[0] = this.chunkController.renderWorldScene.unblendedSkyColor[0];
                this.chunkController.renderWorldScene.targetSkyColor[1] = this.chunkController.renderWorldScene.unblendedSkyColor[1];
                this.chunkController.renderWorldScene.targetSkyColor[2] = this.chunkController.renderWorldScene.unblendedSkyColor[2];

                this.chunkController.renderWorldScene.originalSkyColor[0] = this.skyColor[0];
                this.chunkController.renderWorldScene.originalSkyColor[1] = this.skyColor[1];
                this.chunkController.renderWorldScene.originalSkyColor[2] = this.skyColor[2];

                this.chunkController.renderWorldScene.timeStartedSkyColorTransition = this.ce.save.time;
            }
        }
        this.chunkController.renderWorldScene.prevCloudy = this.chunkController.renderWorldScene.cloudy;

        this.prevRaining = raining;

        this.chunkController.renderWorldScene.transitionSkyColor();

        this.activeWeatherSystems.trimToSize();

        if((MathUtil.floorDouble(this.ce.save.thePlayer.x) >> 5 != MathUtil.floorDouble(this.ce.save.thePlayer.prevX) >> 5) || (MathUtil.floorDouble(this.ce.save.thePlayer.z) >> 5 != MathUtil.floorDouble(this.ce.save.thePlayer.prevZ) >> 5)){
            this.moveWeatherSystemsToInactiveList();
        }

        if(this.ce.save.time % 3600 == 0){
            WeatherSystem weatherSystem1;
            for(int i = 0; i < this.inactiveWeatherSystems.size(); i++){
                weatherSystem1 = this.inactiveWeatherSystems.get(i);
                if(this.ce.save.time > weatherSystem1.killTime){
                    this.inactiveWeatherSystems.remove(weatherSystem1);
                }
            }
            this.inactiveWeatherSystems.trimToSize();
        }


        EntityParticle rainParticle;
        for(int i = 0; i < this.chunkController.renderWorldScene.rainParticles.size(); i++){
            rainParticle = this.chunkController.renderWorldScene.rainParticles.get(i);
            rainParticle.tick();
            if(rainParticle.despawn){
                this.chunkController.renderWorldScene.rainParticles.remove(rainParticle);
            }
        }

        this.chunkController.renderWorldScene.rainParticles.trimToSize();

        for(int i = 0; i < this.chunkController.renderWorldScene.rainQuads.length; i++){
            if(this.chunkController.renderWorldScene.rainQuads[i] == null)continue;

            this.chunkController.renderWorldScene.rainQuads[i].tick();

            double x = this.chunkController.renderWorldScene.rainQuads[i].x;
            double y = this.chunkController.renderWorldScene.rainQuads[i].y + 0.75;
            double z = this.chunkController.renderWorldScene.rainQuads[i].z;

            if(this.chunkController.renderWorldScene.rainQuads[i].remove){
                this.chunkController.renderWorldScene.rainQuads[i] = null;

                if(MathUtil.distance3D(x,y,z, this.ce.save.thePlayer.x, this.ce.save.thePlayer.y, this.ce.save.thePlayer.z) <= 5) {
                    EntityParticle particle1 = new EntityParticle(x, y, z, true, 120, Block.water.ID, true, true);
                    EntityParticle particle2 = new EntityParticle(x, y, z, true, 120, Block.water.ID, true, true);
                    EntityParticle particle3 = new EntityParticle(x, y, z, true, 120, Block.water.ID, true, true);

                    float xMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
                    xMove = CosmicEvolution.globalRand.nextBoolean() ? xMove : -xMove;
                    float zMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
                    zMove = CosmicEvolution.globalRand.nextBoolean() ? zMove : -zMove;
                    particle1.setMovementVector(new Vector3f(xMove, 0.5f, zMove));

                    xMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
                    xMove = CosmicEvolution.globalRand.nextBoolean() ? xMove : -xMove;
                    zMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
                    zMove = CosmicEvolution.globalRand.nextBoolean() ? zMove : -zMove;
                    particle2.setMovementVector(new Vector3f(xMove, 0.5f, zMove));

                    xMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
                    xMove = CosmicEvolution.globalRand.nextBoolean() ? xMove : -xMove;
                    zMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
                    zMove = CosmicEvolution.globalRand.nextBoolean() ? zMove : -zMove;
                    particle3.setMovementVector(new Vector3f(xMove, 0.5f, zMove));


                    this.chunkController.renderWorldScene.rainParticles.add(particle1);
                    this.chunkController.renderWorldScene.rainParticles.add(particle2);
                    this.chunkController.renderWorldScene.rainParticles.add(particle3);
                }
            }

        }

        this.addRainQuads();

        int currentRainSoundState = this.ce.save.thePlayer.rainSoundState;
        if(this.raining && (MathUtil.floorDouble(this.ce.save.thePlayer.x) != MathUtil.floorDouble(this.ce.save.thePlayer.prevX) || MathUtil.floorDouble(this.ce.save.thePlayer.y) != MathUtil.floorDouble(this.ce.save.thePlayer.prevY) || MathUtil.floorDouble(this.ce.save.thePlayer.z) != MathUtil.floorDouble(this.ce.save.thePlayer.prevZ))){
            if(this.isPlayerInRoom(MathUtil.floorDouble(this.ce.save.thePlayer.x), MathUtil.floorDouble(this.ce.save.thePlayer.y), MathUtil.floorDouble(this.ce.save.thePlayer.z))){
                this.ce.save.thePlayer.rainSoundState = 1;
            } else if(this.isPlayerUnderTree(MathUtil.floorDouble(this.ce.save.thePlayer.x), MathUtil.floorDouble(this.ce.save.thePlayer.z))){
                this.ce.save.thePlayer.rainSoundState = 2;
            } else {
                this.ce.save.thePlayer.rainSoundState = 3;
            }
            if(currentRainSoundState != this.ce.save.thePlayer.rainSoundState && this.raining && this.averagePrecipitation > 0.66f){
                this.ce.soundPlayer.stopSound(currentRainSoundState == 1 ? Sound.rainIndoors : currentRainSoundState == 2 ? Sound.rainUnderTree : Sound.rainOutside);
                this.ce.soundPlayer.playSound(this.ce.save.thePlayer.x, this.ce.save.thePlayer.y, this.ce.save.thePlayer.z, new Sound(this.ce.save.thePlayer.rainSoundState == 1 ? Sound.rainIndoors : this.ce.save.thePlayer.rainSoundState == 2 ? Sound.rainUnderTree : Sound.rainOutside, false, 1f), 1.0f);
            }
        }

        if(this.raining && this.ce.save.time % 120 == 0  && this.averagePrecipitation > 0.55f){
            this.ce.soundPlayer.playSound(this.ce.save.thePlayer.x, this.ce.save.thePlayer.y, this.ce.save.thePlayer.z, new Sound(this.ce.save.thePlayer.rainSoundState == 1 ? Sound.rainIndoors : this.ce.save.thePlayer.rainSoundState == 2 ? Sound.rainUnderTree : Sound.rainOutside, false, 1f), 1.0f);
        }

        this.setWindParameters();

    }

    public abstract void initNoiseMaps();

    public abstract void saveWorld();

    public abstract void saveWorldWithoutUnload();

    public abstract void loadWorld();



    public void toggleWorldPause(){
        this.paused = !this.paused;
    }

    public synchronized void addEntity(Entity entity){
        this.findChunkFromChunkCoordinates(MathUtil.floorDouble(entity.x) >> 5, MathUtil.floorDouble(entity.y) >> 5, MathUtil.floorDouble(entity.z) >> 5).addEntityToList(entity);
    }

    public synchronized void setBlock(int x, int y, int z, short blockID) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk.blocks == null){
            chunk.initChunk();
        }
        chunk.setBlock(x, y, z, blockID);

        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);

        if (Block.list[blockID].isSolid) {
            chunk.lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
            chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
        }

        if (blockID == Block.air.ID) {
            if (lightMap.isHeight(x, y, z)) {
                lightMap.updateLightMap(x, this.findNextHighestSolidBlock(x, y, z), z);
            }
        } else if (Block.list[blockID].isSolid) {
            if (lightMap.isHeightGreater(x, y, z)) {
                lightMap.updateLightMap(x, y, z);
            }
        }
        chunk.updateSkylight = true;
    }

    public synchronized void setBlockWithNotify(int x, int y, int z, short blockID, boolean playerInitiated) {
        boolean destroyLight = Block.list[this.getBlockID(x, y, z)].isLightBlock && blockID == Block.air.ID;
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        chunk.setBlockWithNotify(x, y, z, blockID);
        if (blockID == Block.air.ID) {
            this.resetNearestLight(x, y, z);
        }
        if (Block.list[blockID].isLightBlock) {
            this.propagateLightSource(x, y, z, Block.list[blockID].lightBlockValue);
        }
        if (Block.list[blockID].isSolid) {
            chunk.lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
            chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
            this.queueSurroundingLightBlocks(x, y, z);
        }
        this.notifySurroundingBlocks(x, y, z);
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);
        if (blockID == Block.air.ID) {
            if (lightMap.isHeight(x, y, z)) {
                lightMap.updateLightMap(x, this.findNextHighestSolidBlock(x, y, z), z);
            }
        } else if (Block.list[blockID].isSolid) {
            if (lightMap.isHeightGreater(x, y, z)) {
                lightMap.updateLightMap(x, y, z);
            }
        }
        if (destroyLight) {
            this.propagateDarkness(x, y, z);
        }
        chunk.updateSkylight = true;

        if(playerInitiated){
            this.setPlayerIsInRoomState();
        }
    }

    public Chunk findChunkFromChunkCoordinates(int x, int y, int z) {
        return this.chunkController.findChunkFromChunkCoordinates(x, y, z);
    }

    public ChunkColumnSkylightMap findChunkSkyLightMap(int x, int z) {
        return this.chunkController.findChunkSkyLightMap(x, z);
    }


    public void updateSkyLightMapChunks(int x, int y, int z){
        y >>= 5;
        y -= 1;
        Chunk chunk;
        while (y >= this.ce.save.thePlayer.chunkY - GameSettings.chunkColumnHeight) {
            chunk = this.findChunkFromChunkCoordinates(x, y, z);
            if (chunk != null) {
                chunk.updateSkylight = true;
                chunk.markDirty();
            }
            y--;
        }

    }

    private boolean doesWeatherSystemAlreadyExist(int weatherSystemX, int weatherSystemZ){
        WeatherSystem weatherSystem;
        for(int i = 0; i < this.activeWeatherSystems.size(); i++){
            weatherSystem = this.activeWeatherSystems.get(i);
            if(MathUtil.floorDouble(weatherSystem.x) >> 10 == weatherSystemX && MathUtil.floorDouble(weatherSystem.z) >> 10 == weatherSystemZ){
                return true;
            }
        }

        return false;
    }

    private boolean isWeatherSystemInInactiveList(int weatherSystemX, int weatherSystemZ){
        WeatherSystem weatherSystem;
        for(int i = 0; i < this.inactiveWeatherSystems.size(); i++){
            weatherSystem = this.inactiveWeatherSystems.get(i);
            if(MathUtil.floorDouble(weatherSystem.x) >> 10 == weatherSystemX && MathUtil.floorDouble(weatherSystem.z) >> 10 == weatherSystemZ){
                return true;
            }
        }
        return false;
    }

    //Weather systems cover a 512x512 range,
    // this will determine if a weather system exists for a chunk column and load from the inactive list if it finds it otherwise it will generate a new system
    public synchronized void generateWeatherSystems(int chunkColumnX, int chunkColumnZ){
        chunkColumnX >>= 5;
        chunkColumnZ >>= 5;
        if(this.doesWeatherSystemAlreadyExist(chunkColumnX, chunkColumnZ))return;

        if(this.isWeatherSystemInInactiveList(chunkColumnX, chunkColumnZ)){
            WeatherSystem weatherSystem;
            for(int i = 0; i < this.inactiveWeatherSystems.size(); i++){
                weatherSystem = this.inactiveWeatherSystems.get(i);
                if(MathUtil.floorDouble(weatherSystem.x) >> 10 == chunkColumnX && MathUtil.floorDouble(weatherSystem.z) >> 10 == chunkColumnZ){
                    this.activeWeatherSystems.add(weatherSystem);
                    this.inactiveWeatherSystems.remove(weatherSystem);
                }
            }
        } else {
            WeatherSystem weatherSystem = new WeatherSystem(WeatherSystem.getRandomWeatherSystemType(CosmicEvolution.globalRand.nextInt(5)), (chunkColumnX << 10) + 256, 0, (chunkColumnZ << 10) + 256, Timer.GAME_DAY);
            this.activeWeatherSystems.add(weatherSystem);
        }

        this.inactiveWeatherSystems.trimToSize();
    }

    private void setWindParameters(){
        if(this.ce.save.time % 600 == 0){
            this.targetWindDirection = (float) (this.windDirection + CosmicEvolution.globalRand.nextFloat(-0.5f, 0.5f) * Math.PI);
            this.targetWindIntensity = (this.averageStrength * 0.5f + this.averagePrecipitation * 0.5f) + CosmicEvolution.globalRand.nextFloat(-0.25f, 0.25f);
        }

        if(this.ce.save.time % 360 == 0) {
            this.ce.soundPlayer.playSound(this.ce.save.thePlayer.x, this.ce.save.thePlayer.y, this.ce.save.thePlayer.z, new Sound(Sound.wind, false, 3.5f + (this.averageStrength * 0.5f + this.averagePrecipitation * 0.5f)), 1f);
        }

        if(this.targetWindIntensity > 1){
            this.targetWindIntensity = 1;
        }

        this.refWindDirection = (float) (Math.PI * 0.5f);
        this.refWindIntensity = this.averageStrength * 0.5f + this.averagePrecipitation * 0.5f;

        boolean changeWindDirection = true;
        boolean changeWindIntensity = true;

        if(changeWindDirection){
            this.windDirection += this.targetWindDirection - this.windDirection > 0.0f ? 0.03125f : -0.03125f;
        }

        if(changeWindIntensity){
            this.windIntensity += this.targetWindIntensity - this.windIntensity > 0.0f ? 0.03125f : -0.03125f;
        }


        this.windIntensity = Math.max(0, this.windIntensity);
        this.windIntensity = Math.min(1, this.windIntensity);


        Shader.terrainShader.uploadBoolean("windy", true);
        Shader.terrainShader.uploadFloat("windDirection", this.windDirection);
        Shader.terrainShader.uploadFloat("windIntensity", this.windIntensity);
    }

    public void moveWeatherSystemsToInactiveList(){ //Triggers on player chunk boundary crossing
        int playerX = MathUtil.floorDouble(this.ce.save.thePlayer.x);
        int playerZ = MathUtil.floorDouble(this.ce.save.thePlayer.z);

        int weatherX;
        int weatherZ;
        WeatherSystem weatherSystem;

        for(int i = 0; i < this.activeWeatherSystems.size(); i++){
            weatherSystem = this.activeWeatherSystems.get(i);
            weatherX = MathUtil.floorDouble(weatherSystem.x);
            weatherZ = MathUtil.floorDouble(weatherSystem.z);
            if(Math.abs(playerX - weatherX) > 512 || Math.abs(playerZ - weatherZ) > 512){
                this.inactiveWeatherSystems.add(weatherSystem);
                this.activeWeatherSystems.remove(weatherSystem);
            }
        }
        this.activeWeatherSystems.trimToSize();

    }

    public void saveWeatherSystemsToFile(){
        this.inactiveWeatherSystems.addAll(this.activeWeatherSystems);
        File weatherFile = new File(this.ce.save.saveFolder + "/worlds/worldEarth/weather.dat");
        try {
            FileOutputStream outputStream = new FileOutputStream(weatherFile);
            NBTTagCompound data = new NBTTagCompound();
            NBTTagCompound weatherData = new NBTTagCompound();
            data.setTag("weatherData", weatherData);

            int numberWeatherSystems = 0;
            NBTTagCompound[] weatherSystemsTag = new NBTTagCompound[this.inactiveWeatherSystems.size()];

            for(int i = 0; i < this.inactiveWeatherSystems.size(); i++){
                weatherSystemsTag[i] = new NBTTagCompound();

                this.inactiveWeatherSystems.get(i).saveWeatherSystemToFile(weatherSystemsTag[i]);

                weatherData.setTag("weatherSystem " + numberWeatherSystems, weatherSystemsTag[i]);
                numberWeatherSystems++;
            }

            int numberOfRainQuads = 0;
            int rainQuadIndex = 0;
            NBTTagCompound[] rainQuadsTags = new NBTTagCompound[this.chunkController.renderWorldScene.rainQuads.length];
            for(int i = 0; i < this.chunkController.renderWorldScene.rainQuads.length; i++){
                if(this.chunkController.renderWorldScene.rainQuads[i] == null)continue;

                rainQuadsTags[rainQuadIndex] = new NBTTagCompound();
                rainQuadsTags[rainQuadIndex].setDouble("x", this.chunkController.renderWorldScene.rainQuads[i].x);
                rainQuadsTags[rainQuadIndex].setDouble("y", this.chunkController.renderWorldScene.rainQuads[i].y);
                rainQuadsTags[rainQuadIndex].setDouble("z", this.chunkController.renderWorldScene.rainQuads[i].z);
                weatherData.setTag("rainQuad " + numberOfRainQuads, rainQuadsTags[rainQuadIndex]);

                numberOfRainQuads++;
                rainQuadIndex++;
            }

            weatherData.setInteger("numberOfRainQuads", numberOfRainQuads);


            weatherData.setInteger("weatherSystemCount", numberWeatherSystems);
            weatherData.setBoolean("raining", this.raining);
            NBTIO.writeCompressed(data, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadWeatherSystemsFromFile(){
        File weatherFile = new File(this.ce.save.saveFolder + "/worlds/worldEarth/weather.dat");
        if(!weatherFile.exists())return;
        try {
            FileInputStream inputStream = new FileInputStream(weatherFile);
            NBTTagCompound data = NBTIO.readCompressed(inputStream);
            NBTTagCompound weatherData = data.getCompoundTag("weatherData");
            if(weatherData == null){
                inputStream.close();
                return;
            }

            int numberWeatherSystems = weatherData.getInteger("weatherSystemCount");

            NBTTagCompound weatherSystemTag;
            for(int i = 0; i < numberWeatherSystems; i++){
                weatherSystemTag = weatherData.getCompoundTag("weatherSystem " + i);
                if(weatherSystemTag != null) {
                    this.inactiveWeatherSystems.add(new WeatherSystem(weatherSystemTag));
                }
            }

            int numberOfRainQuads = weatherData.getInteger("numberOfRainQuads");
            int rainQuadIndex = 0;
            NBTTagCompound rainQuadTag;
            for(int i = 0; i < numberOfRainQuads; i++){
                rainQuadTag = weatherData.getCompoundTag("rainQuad " + i);
                if(rainQuadTag == null)continue;

                this.chunkController.renderWorldScene.rainQuads[rainQuadIndex] = new RainQuad(rainQuadTag.getDouble("x"), rainQuadTag.getDouble("y"), rainQuadTag.getDouble("z"));

                rainQuadIndex++;
            }


            this.raining = weatherData.getBoolean("raining");

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldRainQuadGenerate(){
        return CosmicEvolution.globalRand.nextFloat(0.51f) < this.averagePrecipitation - 0.5f;
    }

    private void addRainQuads(){
        if(this.ce.save.time % 7 != 0 || !this.raining)return;

        int y = MathUtil.floorDouble(this.ce.save.thePlayer.y + 8);
        int boxMinX = MathUtil.floorDouble(this.ce.save.thePlayer.x - 16);
        int boxMaxX = MathUtil.floorDouble(this.ce.save.thePlayer.x + 16);
        int boxMinZ = MathUtil.floorDouble(this.ce.save.thePlayer.z - 16);
        int boxMaxZ = MathUtil.floorDouble(this.ce.save.thePlayer.z + 16);

        for(int x = boxMinX; x < boxMaxX; x++){
            for(int z = boxMinZ; z < boxMaxZ; z++){

                if(!this.shouldRainQuadGenerate())continue;
                if(!this.doesBlockAllowRain(x,y,z))continue;

                this.addRainQuad(x + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),y + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25),z + CosmicEvolution.globalRand.nextDouble(-0.25, 0.25));

            }
        }
    }

    private void addRainQuad(double x, double y, double z){
        for(int i = 0; i < this.chunkController.renderWorldScene.rainQuads.length; i++){
            if(this.chunkController.renderWorldScene.rainQuads[i] != null)continue;

            this.chunkController.renderWorldScene.rainQuads[i] = new RainQuad(x,y,z);
            return;
        }

        throw new RuntimeException("Unable to add more rain quads, are there more quads attempting to generate than in the array size?");
    }


    public ArrayList<AxisAlignedBB> getBlockBoundingBoxes(AxisAlignedBB boundingBox, ArrayList<AxisAlignedBB> blockBoundingBoxes){

        int minX = MathUtil.floorDouble(boundingBox.minX);
        int minY = MathUtil.floorDouble(boundingBox.minY);
        int minZ = MathUtil.floorDouble(boundingBox.minZ);
        int maxX = MathUtil.floorDouble(boundingBox.maxX);
        int maxY = MathUtil.floorDouble(boundingBox.maxY);
        int maxZ = MathUtil.floorDouble(boundingBox.maxZ);

        AxisAlignedBB block;
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    block = new AxisAlignedBB();
                    block.adjustBlockBoundingBox(x,y,z, this.getBlockID(x,y,z), (boundingBox.maxX + boundingBox.minX) / 2, (boundingBox.maxZ + boundingBox.minZ) / 2);
                    if(!block.air) {
                        blockBoundingBoxes.add(block);
                    }
                }
            }
        }

        return blockBoundingBoxes;
    }

    public ArrayList<AxisAlignedBB> getBlockBoundingBoxesWhenPlayerIsShifting(AxisAlignedBB boundingBox, ArrayList<AxisAlignedBB> blockBoundingBoxes){

        int minX = MathUtil.floorDouble(boundingBox.minX) - 2;
        int minY = MathUtil.floorDouble(boundingBox.minY) - 2;
        int minZ = MathUtil.floorDouble(boundingBox.minZ) - 2;
        int maxX = MathUtil.floorDouble(boundingBox.maxX) + 2;
        int maxY = MathUtil.floorDouble(boundingBox.maxY) + 2;
        int maxZ = MathUtil.floorDouble(boundingBox.maxZ) + 2;

        int playerYFoot = MathUtil.floorDouble(this.ce.save.thePlayer.y - 0.1);

        AxisAlignedBB block;
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    block = new AxisAlignedBB();
                    block.adjustBlockBoundingBox(x,y,z, this.getBlockID(x,y,z), (boundingBox.maxX + boundingBox.minX) / 2, (boundingBox.maxZ + boundingBox.minZ) / 2);

                        if(y == playerYFoot - 1) {
                            if (Block.list[this.getBlockID(x, y, z)].isSolid) {
                                block.minX-= 0.25;
                                block.minZ-= 0.25;
                                block.maxX+= 0.25;
                                block.maxZ+= 0.25;
                            }
                        }

                           if(y == playerYFoot && !Block.list[this.getBlockID(x, y, z)].isSolid && !Block.list[this.getBlockID(x, y - 1, z)].isSolid) {

                               if (Block.list[this.getBlockID(x - 1, playerYFoot - 1, z)].isSolid) {
                                   block.minX += 0.49;
                                   if (!Block.list[this.getBlockID(x, playerYFoot - 1, z - 1)].isSolid) {
                                       block.minZ -= 0.25;
                                   }
                                   if (!Block.list[this.getBlockID(x, playerYFoot - 1, z + 1)].isSolid) {
                                       block.maxZ += 0.25;
                                   }
                                   block.air = false;
                               }
                               if (Block.list[this.getBlockID(x + 1, playerYFoot - 1, z)].isSolid) {
                                   block.maxX -= 0.49;
                                   if (!Block.list[this.getBlockID(x, playerYFoot - 1, z - 1)].isSolid) {
                                       block.minZ -= 0.25;
                                   }
                                   if (!Block.list[this.getBlockID(x, playerYFoot - 1, z + 1)].isSolid) {
                                       block.maxZ += 0.25;
                                   }
                                   block.air = false;
                               }
                               if (Block.list[this.getBlockID(x, playerYFoot - 1, z - 1)].isSolid) {
                                   block.minZ += 0.49;
                                   if (!Block.list[this.getBlockID(x - 1, playerYFoot - 1, z)].isSolid) {
                                       block.minX -= 0.25;
                                   }
                                   if (!Block.list[this.getBlockID(x + 1, playerYFoot - 1, z)].isSolid) {
                                       block.maxX += 0.25;
                                   }
                                   block.air = false;
                               }
                               if (Block.list[this.getBlockID(x, playerYFoot - 1, z + 1)].isSolid) {
                                   block.maxZ -= 0.49;
                                   if (!Block.list[this.getBlockID(x - 1, playerYFoot - 1, z)].isSolid) {
                                       block.minX -= 0.25;
                                   }
                                   if (!Block.list[this.getBlockID(x + 1, playerYFoot - 1, z)].isSolid) {
                                       block.maxX += 0.25;
                                   }
                                   block.air = false;
                               }
                           }

                    if(!block.air) {
                        blockBoundingBoxes.add(block);
                    }
                }
            }
        }

        return blockBoundingBoxes;
    }

    public synchronized void notifySurroundingBlocks(int x, int y, int z) {
        this.notifySurroundingBlock(x + 1, y, z);
        this.notifySurroundingBlock(x - 1, y, z);
        this.notifySurroundingBlock(x, y + 1, z);
        this.notifySurroundingBlock(x, y - 1, z);
        this.notifySurroundingBlock(x, y, z + 1);
        this.notifySurroundingBlock(x, y, z - 1);
    }

    public synchronized void notifySurroundingBlocksWithoutRebuild(int x, int y, int z) {
        this.notifySurroundingBlockWithoutRebuild(x + 1, y, z);
        this.notifySurroundingBlockWithoutRebuild(x - 1, y, z);
        this.notifySurroundingBlockWithoutRebuild(x, y + 1, z);
        this.notifySurroundingBlockWithoutRebuild(x, y - 1, z);
        this.notifySurroundingBlockWithoutRebuild(x, y, z + 1);
        this.notifySurroundingBlockWithoutRebuild(x, y, z - 1);
    }

    public void notifyGroundBelow(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, (y - 1) >> 5, z >> 5);
        for (int i = 1; i < 256; i++) {
            if ((y - i) % 32 == 0) {
                chunk = this.findChunkFromChunkCoordinates(x >> 5, (y - i) >> 5, z >> 5);
            }
            if (chunk != null && chunk.blocks != null) {
                if (Block.list[chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y - i, z)]].isSolid) {
                    chunk.markDirty();
                    return;
                }
            }
        }
    }

    public void notifySurroundingChunks(int x, int y, int z) {
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y - 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y - 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y - 1, z - 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y, z - 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y + 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y + 1, z - 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y + 1, z - 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y - 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y - 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y - 1, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y + 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y + 1, z));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y + 1, z));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y - 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y - 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y - 1, z + 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y, z + 1));

        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x - 1, y + 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x, y + 1, z + 1));
        this.chunkController.addChunkToRebuildQueue(this.findChunkFromChunkCoordinates(x + 1, y + 1, z + 1));
    }


    public void notifySurroundingBlock(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null){
            if(chunk.empty){
                chunk.initChunk();
            }
            chunk.notifyBlock(x, y, z);
        }
    }

    public void notifySurroundingBlockWithoutRebuild(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk.empty){
            chunk.initChunk();
        }
        chunk.notifyBlockWithoutRebuild(x, y, z);

    }

    private void resetNearestLight(int x, int y, int z) {
        this.resetLightX = x;
        this.resetLightY = y;
        this.resetLightZ = z;
        this.checkSurroundingBlocksToSearchForLight(x, y, z);
        ArrayList<int[]> localCopyLightSearchQueue = new ArrayList<>();
        int[] coordinates;
        lightSearch:
        while (!this.lightSearchQueue.isEmpty() && this.safetyThreshold < 100000) {
            for (int i = 0; i < this.lightSearchQueue.size(); i++) {
                coordinates = this.lightSearchQueue.get(i);
                if (Block.list[this.getBlockID(coordinates[0], coordinates[1], coordinates[2])].isLightBlock) {
                    this.propagateDarkness(coordinates[0], coordinates[1], coordinates[2]);
                    break lightSearch;
                }
            }
            localCopyLightSearchQueue.addAll(this.lightSearchQueue);
            this.lightSearchQueue.clear();
            for (int i = 0; i < localCopyLightSearchQueue.size(); i++) {
                this.checkSurroundingBlocksToSearchForLight(localCopyLightSearchQueue.get(i));
            }
            localCopyLightSearchQueue.clear();
            this.safetyThreshold++;
        }
        this.safetyThreshold = 0;
        this.previousLightSearchQueue.clear();
    }

    public void checkSurroundingBlocksToSearchForLight(int[] coordinates) {
        this.checkSurroundingBlocksToSearchForLight(coordinates[0], coordinates[1], coordinates[2]);
    }

    public void checkSurroundingBlocksToSearchForLight(int x, int y, int z) {
        if (this.shouldBlockAddToLightSearchQueue(x + 1, y, z)) {
            this.lightSearchQueue.add(new int[]{x + 1, y, z});
            this.previousLightSearchQueue.add(new int[]{x + 1, y, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x - 1, y, z)) {
            this.lightSearchQueue.add(new int[]{x - 1, y, z});
            this.previousLightSearchQueue.add(new int[]{x - 1, y, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y + 1, z)) {
            this.lightSearchQueue.add(new int[]{x, y + 1, z});
            this.previousLightSearchQueue.add(new int[]{x, y + 1, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y - 1, z)) {
            this.lightSearchQueue.add(new int[]{x, y - 1, z});
            this.previousLightSearchQueue.add(new int[]{x, y - 1, z});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y, z + 1)) {
            this.lightSearchQueue.add(new int[]{x, y, z + 1});
            this.previousLightSearchQueue.add(new int[]{x, y, z + 1});
        }
        if (this.shouldBlockAddToLightSearchQueue(x, y, z - 1)) {
            this.lightSearchQueue.add(new int[]{x, y, z - 1});
            this.previousLightSearchQueue.add(new int[]{x, y, z - 1});
        }
    }

    private boolean shouldBlockAddToLightSearchQueue(int x, int y, int z) {
        return !Block.list[this.getBlockID(x, y, z)].isSolid && !this.hasBlockAlreadyLightSearchQueued(x, y, z) && this.calculateBlockDistance(x, y, z) <= 5;
    }

    private int calculateBlockDistance(int x, int y, int z) {
        int[] returns = new int[3];
        returns[0] = this.resetLightX - x;
        returns[1] = this.resetLightY - y;
        returns[2] = this.resetLightZ - z;

        for (int i = 0; i < returns.length; i++) {
            if (returns[i] < 0) {
                returns[i] *= -1;
            }
        }

        Arrays.sort(returns);
        return returns[2];
    }

    private boolean hasBlockAlreadyLightSearchQueued(int x, int y, int z) {
        int[] comparedArray;
        for (int i = 0; i < this.previousLightSearchQueue.size(); i++) {
            comparedArray = this.previousLightSearchQueue.get(i);
            if (comparedArray[0] == x && comparedArray[1] == y && comparedArray[2] == z) {
                return true;
            }
        }
        return false;
    }

    private void queueSurroundingLightBlocks(int x, int y, int z) {
        final int xCenter = x;
        final int yCenter = y;
        final int zCenter = z;
        int boxDimension = 31;
        int boxXMax = 15 + x;
        int boxYMax = 15 + y;
        int boxZMax = 15 + z;
        ArrayList<int[]> lightSourceQueue = new ArrayList<>();

        for (x = xCenter - boxDimension / 2; x < boxXMax; x++) {
            for (y = yCenter - boxDimension / 2; y < boxYMax; y++) {
                for (z = zCenter - boxDimension / 2; z < boxZMax; z++) {
                    this.clearBlockLight(x, y, z);
                    if (Block.list[this.getBlockID(x, y, z)].isLightBlock) {
                        lightSourceQueue.add(new int[]{x, y, z});
                    }
                }
            }
        }
        int[] lightArray;
        for (int i = 0; i < lightSourceQueue.size(); i++) {
            lightArray = lightSourceQueue.get(i);
            this.propagateLightSource(lightArray[0], lightArray[1], lightArray[2], Block.list[this.getBlockID(lightArray[0], lightArray[1], lightArray[2])].lightBlockValue);
        }
    }

    private void repairDamagedLights(int x, int y, int z, int previousLight) {
        final int xCenter = x;
        final int yCenter = y;
        final int zCenter = z;
        int boxDimension = previousLight * 2 + 1;
        int boxXMax = previousLight + x;
        int boxYMax = previousLight + y;
        int boxZMax = previousLight + z;

        for (x = xCenter - boxDimension / 2; x < boxXMax; x++) {
            for (y = yCenter - boxDimension / 2; y < boxYMax; y++) {
                for (z = zCenter - boxDimension / 2; z < boxZMax; z++) {
                    if (Block.list[this.getBlockID(x, y, z)].isLightBlock) {
                        this.propagateLightSource(x, y, z, Block.list[this.getBlockID(x, y, z)].lightBlockValue);
                    }
                }
            }
        }
    }

    public void propagateDarkness(int x, int y, int z) {
        int previousLight = this.getBlockLightValue(x, y, z);
        this.clearBlockLight(x, y, z);
        this.checkSurroundingBlocksToPropagateDarkness(x, y, z, previousLight);
        ArrayList<int[]> localCopyDarknessQueue = new ArrayList<>();
        while (!this.darknessUpdateQueue.isEmpty() && this.safetyThreshold < 100000) {
            for (int i = 0; i < this.darknessUpdateQueue.size(); i++) {
                this.clearBlockLight(this.darknessUpdateQueue.get(i));
            }
            localCopyDarknessQueue.addAll(this.darknessUpdateQueue);
            this.darknessUpdateQueue.clear();
            for (int i = 0; i < localCopyDarknessQueue.size(); i++) {
                this.checkSurroundingBlocksToPropagateDarkness(localCopyDarknessQueue.get(i));
            }
            localCopyDarknessQueue.clear();
            this.safetyThreshold++;
        }
        this.safetyThreshold = 0;
        int[] updatedBlocks;
        for (int i = 0; i < this.previouslyQueuedDarknessUpdate.size(); i++) {
            updatedBlocks = this.previouslyQueuedDarknessUpdate.get(i);
            this.findChunkFromChunkCoordinates(updatedBlocks[0] >> 5, updatedBlocks[1] >> 5, updatedBlocks[2] >> 5).markDirty();
        }
        this.previouslyQueuedDarknessUpdate.clear();
        this.repairDamagedLights(x, y, z, previousLight);
    }

    public void checkSurroundingBlocksToPropagateDarkness(int[] coordinates) {
        this.checkSurroundingBlocksToPropagateDarkness(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    public void checkSurroundingBlocksToPropagateDarkness(int x, int y, int z, int neighborPreviousLightValue) {
        if (this.shouldBlockAddToDarknessQueue(x + 1, y, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x + 1, y, z, this.getBlockLightValue(x + 1, y, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x + 1, y, z, this.getBlockLightValue(x + 1, y, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x - 1, y, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x - 1, y, z, this.getBlockLightValue(x - 1, y, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x - 1, y, z, this.getBlockLightValue(x - 1, y, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y + 1, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y + 1, z, this.getBlockLightValue(x, y + 1, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y + 1, z, this.getBlockLightValue(x, y + 1, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y - 1, z, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y - 1, z, this.getBlockLightValue(x, y - 1, z)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y - 1, z, this.getBlockLightValue(x, y - 1, z)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y, z + 1, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y, z + 1, this.getBlockLightValue(x, y, z + 1)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y, z + 1, this.getBlockLightValue(x, y, z + 1)});
        }
        if (this.shouldBlockAddToDarknessQueue(x, y, z - 1, neighborPreviousLightValue)) {
            this.darknessUpdateQueue.add(new int[]{x, y, z - 1, this.getBlockLightValue(x, y, z - 1)});
            this.previouslyQueuedDarknessUpdate.add(new int[]{x, y, z - 1, this.getBlockLightValue(x, y, z - 1)});
        }
    }

    private boolean shouldBlockAddToDarknessQueue(int x, int y, int z, int neighborPreviousLightValue) {
        return !Block.list[this.getBlockID(x, y, z)].isSolid && !this.hasBlockAlreadyDarknessQueued(x, y, z) && (this.getBlockLightValue(x, y, z) < neighborPreviousLightValue);
    }

    private void clearBlockLight(int[] coordinates) {
        this.findChunkFromChunkCoordinates(coordinates[0] >> 5, coordinates[1] >> 5, coordinates[2] >> 5).setBlockLightValue(coordinates[0], coordinates[1], coordinates[2], (byte) 0);
    }

    private void clearBlockLight(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null){
            if(chunk.lighting == null){
                chunk.initChunk();
            }
            chunk.setBlockLightValue(x, y, z, (byte) 0);
            chunk.clearBlockLightColor(x, y, z);
        }
    }

    private boolean hasBlockAlreadyDarknessQueued(int x, int y, int z) {
        int[] comparedArray;
        for (int i = 0; i < this.previouslyQueuedDarknessUpdate.size(); i++) {
            comparedArray = this.previouslyQueuedDarknessUpdate.get(i);
            if (comparedArray[0] == x && comparedArray[1] == y && comparedArray[2] == z) {
                return true;
            }
        }
        return false;
    }

    public void propagateLightSource(int x, int y, int z, byte lightValue) {
        this.activeBlockLight[0] = x;
        this.activeBlockLight[1] = y;
        this.activeBlockLight[2] = z;
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = lightValue;
        this.checkSurroundingBlocksToPropagateLight(x, y, z);
        ArrayList<int[]> localCopyLightQueue = new ArrayList<>();
        while (!this.lightUpdateQueue.isEmpty() && this.safetyThreshold < 100000) {
            for (int i = 0; i < this.lightUpdateQueue.size(); i++) {
                this.setBlockLight(this.lightUpdateQueue.get(i));
            }
            localCopyLightQueue.addAll(this.lightUpdateQueue);
            this.lightUpdateQueue.clear();
            for (int i = 0; i < localCopyLightQueue.size(); i++) {
                this.checkSurroundingBlocksToPropagateLight(localCopyLightQueue.get(i));
            }
            localCopyLightQueue.clear();
            this.safetyThreshold++;
        }

        this.safetyThreshold = 0;
        int[] updatedBlocks;
        for (int i = 0; i < this.previouslyQueuedLightUpdate.size(); i++) {
            updatedBlocks = this.previouslyQueuedLightUpdate.get(i);
            this.findChunkFromChunkCoordinates(updatedBlocks[0] >> 5, updatedBlocks[1] >> 5, updatedBlocks[2] >> 5).markDirty();
        }
        this.previouslyQueuedLightUpdate.clear();
    }

    private void checkSurroundingBlocksToPropagateLight(int[] coordinates) {
        this.checkSurroundingBlocksToPropagateLight(coordinates[0], coordinates[1], coordinates[2]);
    }

    public void checkSurroundingBlocksToPropagateLight(int x, int y, int z) {
        if (this.getBlockLightValue(x, y, z) <= 0) {
            return;
        }
        int blockLightValue = this.getBlockLightValue(x, y, z);

        if (this.shouldBlockAddToLightQueue(x + 1, y, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x + 1, y, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x + 1, y, z});
        }
        if (this.shouldBlockAddToLightQueue(x - 1, y, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x - 1, y, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x - 1, y, z});
        }
        if (this.shouldBlockAddToLightQueue(x, y + 1, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y + 1, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y + 1, z});
        }
        if (this.shouldBlockAddToLightQueue(x, y - 1, z, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y - 1, z});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y - 1, z});
        }
        if (this.shouldBlockAddToLightQueue(x, y, z + 1, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y, z + 1});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y, z + 1});
        }
        if (this.shouldBlockAddToLightQueue(x, y, z - 1, blockLightValue)) {
            this.lightUpdateQueue.add(new int[]{x, y, z - 1});
            this.previouslyQueuedLightUpdate.add(new int[]{x, y, z - 1});
        }
    }

    private boolean shouldBlockAddToLightQueue(int x, int y, int z, int neighborLightValue) {
        return !Block.list[this.getBlockID(x, y, z)].isSolid && !this.hasBlockAlreadyLightQueued(x, y, z) && (this.getBlockLightValue(x, y, z) < neighborLightValue);
    }

    private boolean hasBlockAlreadyLightQueued(int x, int y, int z) {
        int[] comparedArray;
        for (int i = 0; i < this.previouslyQueuedLightUpdate.size(); i++) {
            comparedArray = this.previouslyQueuedLightUpdate.get(i);
            if (comparedArray[0] == x && comparedArray[1] == y && comparedArray[2] == z) {
                return true;
            }
        }
        return false;
    }


    private void setBlockLight(int[] coordinates) {
        Chunk chunk = this.chunkController.findChunkFromChunkCoordinates(coordinates[0] >> 5, coordinates[1] >> 5, coordinates[2] >> 5);
        if (chunk.lighting == null) {
            chunk.initChunk();
        }
        chunk.setBlockLightValue(coordinates[0], coordinates[1], coordinates[2], this.getPropagatedLightValue(coordinates[0], coordinates[1], coordinates[2]));
        chunk.setBlockLightColor(coordinates[0], coordinates[1], coordinates[2], Block.list[this.getBlockID(this.activeBlockLight[0], this.activeBlockLight[1], this.activeBlockLight[2])].lightColor);
    }

    private byte getPropagatedLightValue(int x, int y, int z) {
        byte[] nearbyLightLevels = new byte[6];
        byte currentLightLevel = this.getBlockLightValue(x, y, z);
        nearbyLightLevels[0] = this.getBlockLightValue(x + 1, y, z);
        nearbyLightLevels[1] = this.getBlockLightValue(x - 1, y, z);
        nearbyLightLevels[2] = this.getBlockLightValue(x, y, z + 1);
        nearbyLightLevels[3] = this.getBlockLightValue(x, y, z - 1);
        nearbyLightLevels[4] = this.getBlockLightValue(x, y + 1, z);
        nearbyLightLevels[5] = this.getBlockLightValue(x, y - 1, z);
        Arrays.sort(nearbyLightLevels);
        nearbyLightLevels[5]--;
        if (nearbyLightLevels[5] <= 0) {
            return 0;
        } else if (nearbyLightLevels[5] <= currentLightLevel) {
            return currentLightLevel;
        } else {
            return nearbyLightLevels[5];
        }
    }

    public int findNextHighestSolidBlock(int x, int y, int z) {
        for (int i = 1; i < 4096; i++) {
            if (Block.list[this.findChunkFromChunkCoordinates(x >> 5, (y - i) >> 5, z >> 5).blocks[Chunk.getBlockIndexFromCoordinates(x, y - i, z)]].isSolid) {
                return y - i;
            }
        }
        return y;
    }

    public synchronized short getBlockID(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.blocks != null) {
                return chunk.getBlockID(x, y, z);
            } else {
                return Block.air.ID;
            }
        } else {
            return Block.air.ID;
        }
    }


    private byte getSurroundingLightLevel(int x, int y, int z) {
        byte[] nearbyLightLevels = new byte[6];
        byte currentLightLevel = this.getBlockSkyLightValue(x, y, z);
        nearbyLightLevels[0] = this.getBlockSkyLightValue(x + 1, y, z);
        nearbyLightLevels[1] = this.getBlockSkyLightValue(x - 1, y, z);
        nearbyLightLevels[2] = this.getBlockSkyLightValue(x, y, z + 1);
        nearbyLightLevels[3] = this.getBlockSkyLightValue(x, y, z - 1);
        nearbyLightLevels[4] = this.getBlockSkyLightValue(x, y + 1, z);
        nearbyLightLevels[5] = this.getBlockSkyLightValue(x, y - 1, z);
        Arrays.sort(nearbyLightLevels);
        if (nearbyLightLevels[5] < 0) {
            return 0;
        } else if (nearbyLightLevels[5] - 1 <= currentLightLevel) {
            return currentLightLevel;
        } else {
            return (byte) (nearbyLightLevels[5] - 1);
        }
    }

    public void setSkyLight(int x, int y, int z, byte lightValue){
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null){
            chunk.setBlockSkyLightValue(x,y,z, lightValue);
        }
    }

    public void propagateSkyLight(int x, int y, int z, ArrayList<int[]> skyLightUpdateQueue, ArrayList<int[]> previousSkyLightUpdateQueue){
        this.setSkyLight(x,y,z, (byte)15);
        if(this.willSkyLightQueueMore(x,y,z, previousSkyLightUpdateQueue)) {
            skyLightUpdateQueue.add(new int[]{x, y, z});
            ArrayList<int[]> localCopySkyLightUpdateQueue = new ArrayList();
            int[] lightCoordinates;
            while (!skyLightUpdateQueue.isEmpty() && this.safetyThreshold < 100000) {
                for (int i = 0; i < skyLightUpdateQueue.size(); i++) {
                    lightCoordinates = skyLightUpdateQueue.get(i);
                    this.setSkyLight(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2], this.getSurroundingLightLevel(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2]));
                }
                localCopySkyLightUpdateQueue.addAll(skyLightUpdateQueue);
                skyLightUpdateQueue.clear();
                for (int i = 0; i < localCopySkyLightUpdateQueue.size(); i++) {
                    this.queueSurroundingBlocksToPropagateSkyLight(localCopySkyLightUpdateQueue.get(i), this.getBlockSkyLightValue(localCopySkyLightUpdateQueue.get(i)), skyLightUpdateQueue, previousSkyLightUpdateQueue);
                }
                localCopySkyLightUpdateQueue.clear();
                this.safetyThreshold++;
            }
            this.safetyThreshold = 0;
        }
    }

    public void queueSurroundingBlocksToPropagateSkyLight(int[] lightCoordinates, byte currentLightLevel, ArrayList<int[]> skyLightUpdateQueue, ArrayList<int[]> previousSkyLightUpdateQueue){
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1, previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1});
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) == 0 && !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1, previousSkyLightUpdateQueue)){
            skyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1});
            previousSkyLightUpdateQueue.add(new int[]{lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1});
        }
    }

    public boolean willSkyLightQueueMore(int x, int y, int z, ArrayList<int[]> previousSkyLightUpdateQueue){
        return this.willSkyLightQueueMore(new int[]{x,y,z}, this.getSurroundingLightLevel(x,y,z), previousSkyLightUpdateQueue);
    }

    public boolean willSkyLightQueueMore(int[] lightCoordinates, byte currentLightLevel, ArrayList<int[]> previousSkyLightUpdateQueue){
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0  && this.getBlockSkyLightValue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2]) == 0  &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] + 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2])].isSolid && currentLightLevel > 0  && this.getBlockSkyLightValue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2]) == 0 &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0] - 1, lightCoordinates[1], lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2])].isSolid && currentLightLevel > 0  && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2]) == 0 &&   !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] + 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2])].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2]) == 0  &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1] - 1, lightCoordinates[2], previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1) == 0 &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] + 1, previousSkyLightUpdateQueue)){
            return true;
        }
        if(!this.doesBlockHaveSkyAccess(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) && !Block.list[this.getBlockID(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1)].isSolid && currentLightLevel > 0 && this.getBlockSkyLightValue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1) == 0 &&  !this.blockHasEnteredSkyLightQueue(lightCoordinates[0], lightCoordinates[1], lightCoordinates[2] - 1, previousSkyLightUpdateQueue)){
            return true;
        }
        return false;
    }

    private boolean blockHasEnteredSkyLightQueue(int x, int y, int z, ArrayList<int[]> previousSkyLightUpdateQueue){
        int[] coordinates;
        for(int i = 0; i < previousSkyLightUpdateQueue.size(); i++){
            coordinates = previousSkyLightUpdateQueue.get(i);
            if(coordinates[0] == x && coordinates[1] == y && coordinates[2] == z){
                return true;
            }
        }
        return false;
    }

    public void setPlayerIsInRoomState() {
        int currentRainSoundState = this.ce.save.thePlayer.rainSoundState;

        if (this.isPlayerInRoom(MathUtil.floorDouble(this.ce.save.thePlayer.x), MathUtil.floorDouble(this.ce.save.thePlayer.y), MathUtil.floorDouble(this.ce.save.thePlayer.z))) {
            this.ce.save.thePlayer.rainSoundState = 1;
        } else if (this.isPlayerUnderTree(MathUtil.floorDouble(this.ce.save.thePlayer.x), MathUtil.floorDouble(this.ce.save.thePlayer.z))) {
            this.ce.save.thePlayer.rainSoundState = 2;
        } else {
            this.ce.save.thePlayer.rainSoundState = 3;
        }

        if(currentRainSoundState != this.ce.save.thePlayer.rainSoundState && this.raining && this.averagePrecipitation > 0.55f){
            this.ce.soundPlayer.stopSound(currentRainSoundState == 1 ? Sound.rainIndoors : currentRainSoundState == 2 ? Sound.rainUnderTree : Sound.rainOutside);
            this.ce.soundPlayer.playSound(this.ce.save.thePlayer.x, this.ce.save.thePlayer.y, this.ce.save.thePlayer.z, new Sound(this.ce.save.thePlayer.rainSoundState == 1 ? Sound.rainIndoors : this.ce.save.thePlayer.rainSoundState == 2 ? Sound.rainUnderTree : Sound.rainOutside, false, 1f), 1.0f);
        }
    }

    public boolean isPlayerInRoom(int x, int y, int z){

        if(this.doesBlockAllowRain(x,y,z))return false;

        this.previousRoomCheckQueue.clear();
        this.roomCheckQueue.clear();

        this.addBlockToRoomQueue(x,y,z);

        int[] roomCheckCoords;
        ArrayList<int[]> localCopyRoomCheckQueue = new ArrayList<>();
        while(!this.roomCheckQueue.isEmpty()){

            for(int i = 0; i < this.roomCheckQueue.size(); i++){
                roomCheckCoords = this.roomCheckQueue.get(i);
                if(this.doesBlockAllowRain(roomCheckCoords[0], roomCheckCoords[1], roomCheckCoords[2])){
                    return false;
                }
            }

            localCopyRoomCheckQueue.addAll(this.roomCheckQueue);
            this.roomCheckQueue.clear();

            for(int i = 0; i < localCopyRoomCheckQueue.size(); i++){
                this.addBlocksToRoomQueue(localCopyRoomCheckQueue.get(i));
            }
            localCopyRoomCheckQueue.clear();
        }

        return true;
    }

    private void addBlocksToRoomQueue(int[] coords){
        this.addBlocksToRoomQueue(coords[0], coords[1], coords[2]);
    }

    private void addBlocksToRoomQueue(int x, int y, int z){
        if(this.shouldBlockAddToRoomQueue(x - 1, y, z)){
            this.addBlockToRoomQueue(x - 1, y, z);
        }

        if(this.shouldBlockAddToRoomQueue(x + 1, y, z)){
            this.addBlockToRoomQueue(x + 1, y, z);
        }

        if(this.shouldBlockAddToRoomQueue(x, y - 1, z)){
            this.addBlockToRoomQueue(x, y - 1, z);
        }

        if(this.shouldBlockAddToRoomQueue(x, y + 1, z)){
            this.addBlockToRoomQueue(x, y + 1, z);
        }

        if(this.shouldBlockAddToRoomQueue(x, y, z - 1)){
            this.addBlockToRoomQueue(x, y, z - 1);
        }

        if(this.shouldBlockAddToRoomQueue(x, y, z + 1)){
            this.addBlockToRoomQueue(x, y, z + 1);
        }
    }

    private boolean shouldBlockAddToRoomQueue(int x, int y, int z){
        return !Block.list[this.getBlockID(x,y,z)].isSolid && !this.hasBlockAlreadyEnteredRoomQueue(x,y,z) && this.isBlockInRangeToCheckIfRoom(x,y,z);
    }

    private boolean isBlockInRangeToCheckIfRoom(int x, int y, int z){
        int playerX = MathUtil.floorDouble(this.ce.save.thePlayer.x);
        int playerY = MathUtil.floorDouble(this.ce.save.thePlayer.y);
        int playerZ = MathUtil.floorDouble(this.ce.save.thePlayer.z);

        int xDif = x - playerX;
        int yDif = y - playerY;
        int zDif = z - playerZ;

        return  xDif <= 8 && yDif <= 8 && zDif <= 8;
    }

    private boolean hasBlockAlreadyEnteredRoomQueue(int x, int y, int z){
        int[] coordinates;
        for(int i = 0; i < this.previousRoomCheckQueue.size(); i++){
            coordinates = this.previousRoomCheckQueue.get(i);
            if(coordinates[0] == x && coordinates[1] == y && coordinates[2] == z){
                return true;
            }
        }
        return false;
    }


    private void addBlockToRoomQueue(int x, int y, int z){
        this.roomCheckQueue.add(new int[]{x,y,z});
        this.previousRoomCheckQueue.add(new int[]{x,y,z});
    }

    private boolean isPlayerUnderTree(int x, int z){
        ChunkColumnSkylightMap skylightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);
        int y = skylightMap.getHeightValue(x,z);


        return Block.list[this.getBlockID(x,y,z)].ID == Block.leaf.ID && MathUtil.floorDouble(this.ce.save.thePlayer.y) < y;
    }

    public synchronized byte getBlockSkyLightValue(int[] coordinates){
        return this.getBlockSkyLightValue(coordinates[0], coordinates[1], coordinates[2]);
    }

    public synchronized byte getBlockSkyLightValue(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.skyLight != null) {
                return chunk.getSkyLightValue(x, y, z);
            } else {
                return 15;
            }
        } else {
            return 15;
        }
    }

    public synchronized byte getBlockLightValue(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.lighting != null) {
                return chunk.getBlockLightValue(x, y, z);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public double getAverageTemperature(int x, int y, int z){
        if(this instanceof WorldEarth){
            return ((((WorldEarth) this).globalTemperatureMap.getNoiseRaw(((WorldEarth) this).convertBlockZToGlobalMap(z), ((WorldEarth) this).convertBlockXToGlobalMap(x)) + (((WorldEarth) this).temperatureNoise1.getNoiseRaw(x / 4, z / 4)))) * 0.5;
        }
        return 0;
    }

    public double getAverageRainfall(int x, int z){
        if(this instanceof WorldEarth){
            return ((((WorldEarth) this).globalRainfallMap.getNoiseRaw(((WorldEarth) this).convertBlockZToGlobalMap(z), ((WorldEarth) this).convertBlockXToGlobalMap(x)) + (((WorldEarth) this).rainfallNoise1.getNoiseRaw(x / 4, z / 4)))) * 0.5;
        }
        return 0;
    }

    public double getRainfall(int x, int z){
        if(this instanceof WorldEarth) {
            return ((((WorldEarth) this).globalRainfallMap.getNoiseRaw(((WorldEarth) this).convertBlockZToGlobalMap(z), ((WorldEarth) this).convertBlockXToGlobalMap(x)) + (((WorldEarth) this).rainfallNoise1.getNoiseRaw(x / 4, z / 4)))) * 0.5;
        }
        return 0;
    }

    public double getTemperatureWithoutTimeOfDay(int x, int y, int z){
        if(this instanceof WorldEarth) {
            double temp = ((((WorldEarth) this).globalTemperatureMap.getNoiseRaw(((WorldEarth) this).convertBlockZToGlobalMap(z), ((WorldEarth) this).convertBlockXToGlobalMap(x)) + (((WorldEarth) this).temperatureNoise1.getNoiseRaw(x / 4, z / 4)))) * 0.5;
            if (y > 0) {
                temp -= ((y / 5d) * 0.01);
            }
            double latitude = Math.abs(x);
            latitude = (int) ((latitude / (this.size / 2f)) * 90f);
            double varianceFromLat = this.getTempVarianceFromLat(latitude);
            double tempChangeFromLat = this.getTempChangeFromLatitude(latitude);
            double partOfOrbit = this.getPortionOfOrbit(); //0 - 0.25 spring, 0.25 - 0.5 summer, 0.5 - 0.75 fall, 0.75 - 1.0/0.0 winter
            double radians = Math.toRadians(360 * partOfOrbit);

            temp += tempChangeFromLat;
            temp = x < 0 ? temp + (MathUtil.sin(radians) * varianceFromLat) : temp + (MathUtil.sin(radians + Math.PI) * varianceFromLat);


            return temp;
        }
        return 0;
    }

    public double getDisplayTemperature(int x, int y, int z){
        return Math.floor((this.getTemperatureWithTimeOfDay(x,y,z) * 100) * 10) / 10.0;
    }


    public double getTemperatureWithTimeOfDay(int x, int y, int z){
        if(this instanceof WorldEarth) {
            double temp = ((((WorldEarth) this).globalTemperatureMap.getNoiseRaw(((WorldEarth) this).convertBlockZToGlobalMap(x), ((WorldEarth) this).convertBlockXToGlobalMap(x)) + (((WorldEarth) this).temperatureNoise1.getNoiseRaw(x / 4, z / 4)))) * 0.5;
            if (y > 0) {
                temp -= ((y / 5d) * 0.01);
            }
            double latitude = Math.abs(x);
            latitude = (int) ((latitude / (this.size / 2f)) * 90f);
            double varianceFromLat = this.getTempVarianceFromLat(latitude);
            double tempChangeFromLat = this.getTempChangeFromLatitude(latitude);
            double partOfOrbit = this.getPortionOfOrbit(); //0 - 0.25 spring, 0.25 - 0.5 summer, 0.5 - 0.75 fall, 0.75 - 1.0/0.0 winter
            double radians = Math.toRadians(360 * partOfOrbit);

            temp += tempChangeFromLat;
            temp = x < 0 ? temp + (MathUtil.sin(radians) * varianceFromLat) : temp + (MathUtil.sin(radians + Math.PI) * varianceFromLat);


            return temp;
        }
        return 0;
    }


    public double getTemperature(int x, int y, int z){
        if(this instanceof WorldEarth){
            return this.getTemperatureWithoutTimeOfDay(x,y,z);
        }
        return 0;
    }


    public double getPortionOfOrbit(){
        return ((double) (CosmicEvolution.instance.save.time % CosmicEvolution.instance.everything.earth.orbitalPeriod) / CosmicEvolution.instance.everything.earth.orbitalPeriod);
    }

    public double getTempVarianceFromLat(double latitude){
        return  ((latitude) / 90f);
    }

    public double getTempChangeFromLatitude(double latitude){
        return -((latitude/ 90f));
    }


    public synchronized float[] getBlockLightColor(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.lightColor != null && this.getBlockSkyLightValue(x,y,z) < 1) {
                return chunk.getBlockLightColor(x, y, z);
            } else {
                return this.skyLightColor;
            }
        } else {
            return this.skyLightColor;
        }
    }

    public int getBlockLightColorAsInt(int x, int y, int z) {
        Chunk chunk = this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if(chunk != null) {
            if (chunk.lightColor != null) {
                return chunk.lightColor[Chunk.getBlockIndexFromCoordinates(x, y, z)];
            } else {
                return new Color(this.skyLightColor[0], this.skyLightColor[1], this.skyLightColor[2],0).getRGB();
            }
        } else {
            return new Color(this.skyLightColor[0], this.skyLightColor[1], this.skyLightColor[2],0).getRGB();
        }
    }

    public synchronized boolean doesBlockHaveSkyAccess(int x, int y, int z) {
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        x %= 32;
        z %= 32;
        if(lightMap.lightMap[x + (z << 5)] < y){
            return this.isLineOfBlocksClear(x, y, z, lightMap);
        } else {
            return true;
        }
    }

    public boolean doesBlockAllowRain(int x, int y, int z){
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(x >> 5, z >> 5);
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        x %= 32;
        z %= 32;
        return lightMap.lightMap[x + (z << 5)] < y;
    }

    private boolean isLineOfBlocksClearForRain(int x, int y, int z, ChunkColumnSkylightMap lightMap){
        int endY = lightMap.lightMap[x + (z << 5)];

        for(int i = y; i <= endY; i++){
            if(this.doesBlockStopRain(x,i,z)){
                return false;
            }
        }

        return true;
    }

    private boolean doesBlockStopRain(int x, int y, int z){
        String blockName = Block.list[this.getBlockID(x,y,z)].blockName;
        switch (blockName){
            case "AIR", "WATER", "TALL_GRASS", "SAPLING", "TORCH", "BERRY_BUSH", "BERRY_BUSH_GROWING", "REEDS_GROWTH", "LOG_PILE", "ITEM_BLOCK", "ITEM_STICK", "ITEM_STONE", "BRICK_PILE", "CAMPFIRE_LIT", "CAMPFIRE":
                return false;
            default:
                return true;
        }
    }

    private synchronized boolean isLineOfBlocksClear(int x, int y, int z, ChunkColumnSkylightMap lightMap){
        int endY = lightMap.lightMap[x + (z << 5)];

        for(int i = y; i <= endY; i++){
            if(this.doesBlockStopSkyLight(x,i,z)){
                return false;
            }
        }

        return true;
    }

    private synchronized boolean doesBlockStopSkyLight(int x, int y, int z){
        String blockName = Block.list[this.getBlockID(x,y,z)].blockName;
        switch (blockName){
            case "LEAF", "AIR":
                return false;
            default:
                return true;
        }
    }

    public byte getNearestSkyLightValue(int x, int y, int z) {
        return 0;
    }

    public boolean chunkSurroundedSixSides(int x, int y, int z) {
        Chunk upperChunk = this.findChunkFromChunkCoordinates(x, y + 1, z);
        Chunk lowerChunk = this.findChunkFromChunkCoordinates(x, y - 1, z);
        Chunk northChunk = this.findChunkFromChunkCoordinates(x - 1, y, z);
        Chunk southChunk = this.findChunkFromChunkCoordinates(x + 1, y, z);
        Chunk eastChunk = this.findChunkFromChunkCoordinates(x, y, z - 1);
        Chunk westChunk = this.findChunkFromChunkCoordinates(x, y, z + 1);

        return upperChunk != null && lowerChunk != null && northChunk != null && southChunk != null && eastChunk != null && westChunk != null;
    }

    public boolean chunkFullySurrounded(int x, int y, int z) {
        Chunk chunk1 = this.findChunkFromChunkCoordinates(x - 1, y - 1, z - 1);
        Chunk chunk2 = this.findChunkFromChunkCoordinates(x, y - 1, z - 1);
        Chunk chunk3 = this.findChunkFromChunkCoordinates(x + 1, y - 1, z - 1);

        Chunk chunk4 = this.findChunkFromChunkCoordinates(x - 1, y, z - 1);
        Chunk chunk5 = this.findChunkFromChunkCoordinates(x, y, z - 1);
        Chunk chunk6 = this.findChunkFromChunkCoordinates(x + 1, y, z - 1);

        Chunk chunk7 = this.findChunkFromChunkCoordinates(x - 1, y + 1, z - 1);
        Chunk chunk8 = this.findChunkFromChunkCoordinates(x, y + 1, z - 1);
        Chunk chunk9 = this.findChunkFromChunkCoordinates(x + 1, y + 1, z - 1);

        Chunk chunk10 = this.findChunkFromChunkCoordinates(x - 1, y - 1, z);
        Chunk chunk11 = this.findChunkFromChunkCoordinates(x, y - 1, z);
        Chunk chunk12 = this.findChunkFromChunkCoordinates(x + 1, y - 1, z);

        Chunk chunk13 = this.findChunkFromChunkCoordinates(x - 1, y, z);

        Chunk chunk15 = this.findChunkFromChunkCoordinates(x + 1, y, z);

        Chunk chunk16 = this.findChunkFromChunkCoordinates(x - 1, y + 1, z);
        Chunk chunk17 = this.findChunkFromChunkCoordinates(x, y + 1, z);
        Chunk chunk18 = this.findChunkFromChunkCoordinates(x + 1, y + 1, z);

        Chunk chunk19 = this.findChunkFromChunkCoordinates(x - 1, y - 1, z + 1);
        Chunk chunk20 = this.findChunkFromChunkCoordinates(x, y - 1, z + 1);
        Chunk chunk21 = this.findChunkFromChunkCoordinates(x + 1, y - 1, z + 1);

        Chunk chunk22 = this.findChunkFromChunkCoordinates(x - 1, y, z + 1);
        Chunk chunk23 = this.findChunkFromChunkCoordinates(x, y, z + 1);
        Chunk chunk24 = this.findChunkFromChunkCoordinates(x + 1, y, z + 1);

        Chunk chunk25 = this.findChunkFromChunkCoordinates(x - 1, y + 1, z + 1);
        Chunk chunk26 = this.findChunkFromChunkCoordinates(x, y + 1, z + 1);
        Chunk chunk27 = this.findChunkFromChunkCoordinates(x + 1, y + 1, z + 1);

        return chunk1 != null && chunk2 != null && chunk3 != null && chunk4 != null && chunk5 != null
                && chunk6 != null && chunk7 != null && chunk8 != null && chunk9 != null
                && chunk10 != null && chunk11 != null && chunk12 != null && chunk13 != null
                && chunk15 != null && chunk16 != null && chunk17 != null && chunk18 != null
                && chunk19 != null && chunk20 != null && chunk21 != null && chunk22 != null
                && chunk23 != null && chunk24 != null && chunk25 != null && chunk26 != null && chunk27 != null;
    }


    public void renderWorld() {
        this.chunkController.renderWorld();
    }

    public void addInWorldCraftingBlock(int x, int y, int z, InWorldCraftingBlock craftingBlock){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addInWorldCraftingBlock(craftingBlock);
    }

    public void removeInWorldCraftingBlock(int x, int y, int z){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeInWorldCraftingBlock(Chunk.getBlockIndexFromCoordinates(x,y,z));
    }

    public InWorldCraftingBlock getInWorldCraftingBlock(int x, int y, int z){
        return this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).getInWorldCraftingBlock(Chunk.getBlockIndexFromCoordinates(x,y,z));
    }

    public void addTimeEvent(int x, int y, int z, long updateTime){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5,z >> 5).addTimeUpdateEvent(x,y,z, updateTime);
    }

    public TimeUpdateEvent getTimeEvent(int x, int y, int z){
        return this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).getTimeUpdateEvent(x,y,z);
    }

    public void removeTimeEvent(int x, int y, int z){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeTimeUpdateEvent(x,y,z);
    }

    public void updateTimeEventTime(int x, int y, int z, long updateTime){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).updateTimeEvent(x,y,z, updateTime);
    }

    public void addChestLocation(int x, int y, int z, Inventory inventory){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addChestLocation(x,y,z, inventory);
    }

    public void removeChestLocation(int x, int y, int z){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeChestLocation((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
    }

    public ChestLocation getChestLocation(int x, int y, int z){
        return this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).getChestLocation(x,y,z);
    }

    public void addHeatableBlock(int x, int y, int z){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addHeatableBlock(x,y,z);
    }

    public void removeHeatableBlock(int x, int y, int z){
        this.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeHeatableBlock(x,y,z);
    }

    public HeatableBlockLocation getHeatableBlock(int x, int y , int z){
        return this.findChunkFromChunkCoordinates(x >> 5 ,y >> 5, z >> 5).getHeatableBlock(x,y,z);
    }

    public void clearChestLocation(int x, int y, int z){ //Destroys the item before deleting the chest inventory in case a item data transfer occurs internally between two chest locations
        ChestLocation chestLocation = this.getChestLocation(x,y,z);
        chestLocation.inventory.itemStacks[0].item = null;
        chestLocation.inventory.itemStacks[0].count = 0;
        chestLocation.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
        chestLocation.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
        this.removeChestLocation(x,y,z);
    }

    public boolean isBlockSuitableForPitKiln(int x, int y, int z){
        return Block.list[this.getBlockID(x - 1, y, z)].blockModel.equals(Block.standardBlockModel) &&
                Block.list[this.getBlockID(x + 1, y, z)].blockModel.equals(Block.standardBlockModel) &&
                Block.list[this.getBlockID(x, y - 1, z)].blockModel.equals(Block.standardBlockModel) &&
                Block.list[this.getBlockID(x, y, z - 1)].blockModel.equals(Block.standardBlockModel) &&
                Block.list[this.getBlockID(x, y, z + 1)].blockModel.equals(Block.standardBlockModel);
    }

    public boolean isBlockAbleToBecomePitKiln(Block block, int x, int y, int z){
        if(block.ID == Block.rawRedClayCookingPot.ID){
            return true;
        }
        if(block.ID == Block.brickPile.ID){
            ChestLocation chestLocation = this.getChestLocation(x,y,z);
            return chestLocation.inventory.itemStacks[0].count <= 12 && chestLocation.inventory.itemStacks[0].item == Item.rawClayAdobeBrick;
        }

        return false;
    }

    public byte pitKilnItemCount(short blockID, int x, int y, int z){
        if(blockID == Block.rawRedClayCookingPot.ID){
            return 1;
        }
        if(blockID == Block.brickPile.ID){
            ChestLocation chestLocation = this.getChestLocation(x,y,z);
            return chestLocation.inventory.itemStacks[0].count;
        }
        return 1;
    }

    public short pitKilnItemType(short blockID, int x, int y, int z){
        if(blockID == Block.rawRedClayCookingPot.ID){
            return Item.block.ID;
        }
        if(blockID == Block.brickPile.ID){
            ChestLocation chestLocation = this.getChestLocation(x,y,z);
            return chestLocation.inventory.itemStacks[0].item.ID;
        }
        return 1;
    }

    private Chunk[] getSurroundingChunksAndCurrentChunk(int x, int y, int z){
        Chunk[] chunks = new Chunk[27];
        chunks[0] = this.findChunkFromChunkCoordinates(x - 1, y - 1, z - 1);
        chunks[1] = this.findChunkFromChunkCoordinates(x, y - 1, z - 1);
        chunks[2] = this.findChunkFromChunkCoordinates(x + 1, y - 1, z - 1);

        chunks[3] = this.findChunkFromChunkCoordinates(x - 1, y, z - 1);
        chunks[4] = this.findChunkFromChunkCoordinates(x, y, z - 1);
        chunks[5] = this.findChunkFromChunkCoordinates(x + 1, y, z - 1);

        chunks[6] = this.findChunkFromChunkCoordinates(x - 1, y + 1, z - 1);
        chunks[7] = this.findChunkFromChunkCoordinates(x, y + 1, z - 1);
        chunks[8] = this.findChunkFromChunkCoordinates(x + 1, y + 1, z - 1);

        chunks[9] = this.findChunkFromChunkCoordinates(x - 1, y - 1, z);
        chunks[10] = this.findChunkFromChunkCoordinates(x, y - 1, z);
        chunks[11] = this.findChunkFromChunkCoordinates(x + 1, y - 1, z);

        chunks[12] = this.findChunkFromChunkCoordinates(x - 1, y, z);
        chunks[13] = this.findChunkFromChunkCoordinates(x, y, z);
        chunks[14] = this.findChunkFromChunkCoordinates(x + 1, y, z);

        chunks[15] = this.findChunkFromChunkCoordinates(x - 1, y + 1, z);
        chunks[16] = this.findChunkFromChunkCoordinates(x, y + 1, z);
        chunks[17] = this.findChunkFromChunkCoordinates(x + 1, y + 1, z);

        chunks[18] = this.findChunkFromChunkCoordinates(x - 1, y - 1, z + 1);
        chunks[19] = this.findChunkFromChunkCoordinates(x, y - 1, z + 1);
        chunks[20] = this.findChunkFromChunkCoordinates(x + 1, y - 1, z + 1);

        chunks[21] = this.findChunkFromChunkCoordinates(x - 1, y, z + 1);
        chunks[22] = this.findChunkFromChunkCoordinates(x, y, z + 1);
        chunks[23] = this.findChunkFromChunkCoordinates(x + 1, y, z + 1);

        chunks[24] = this.findChunkFromChunkCoordinates(x - 1, y + 1, z + 1);
        chunks[25] = this.findChunkFromChunkCoordinates(x, y + 1, z + 1);
        chunks[26] = this.findChunkFromChunkCoordinates(x + 1, y + 1, z + 1);

        return chunks;
    }

    private ArrayList<Entity> getEntitiesInChunks(Chunk[] chunks){
        ArrayList<Entity> entities = new ArrayList<>();

        for(int i = 0; i < chunks.length; i++){
            if(chunks[i] != null) {
                entities.addAll(chunks[i].entities);
            }
        }

        return entities;
    }

    private boolean hasHitEntity(double x, double y, double z){
        int chunkX = MathUtil.floorDouble(x) >> 5;
        int chunkY = MathUtil.floorDouble(y) >> 5;
        int chunkZ = MathUtil.floorDouble(z) >> 5;

        Vector3f movementVector = new Vector3f((float) (x - this.ce.save.thePlayer.x), 1f, (float) (z - this.ce.save.thePlayer.z)).normalize();
        movementVector.mul(0.1f);
        ArrayList<Entity> entities = this.getEntitiesInChunks(this.getSurroundingChunksAndCurrentChunk(chunkX, chunkY, chunkZ));

        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i) instanceof EntityItem || entities.get(i) instanceof EntityBlock)continue;
            if(entities.get(i).boundingBox.pointInsideBoundingBox(x,y,z)) { //This determines if you hit an entity
                entities.get(i).damage(movementVector, this.ce.save.thePlayer.getAttackDamageValue());
                entities.get(i).setLastEntityToHit(this.ce.save.thePlayer);
                return true;
            }
        }
        return false;
    }

    public boolean intersectsBlockBoundingBox(Block block, double x, double y, double z) {
        double localX = x - MathUtil.floorDouble(x);
        double localY = y - MathUtil.floorDouble(y);
        double localZ = z - MathUtil.floorDouble(z);

        return block.standardCollisionBoundingBox.pointInsideBoundingBox(localX, localY, localZ);
    }


    public boolean wouldBlockIntersectPlayer(int x, int y, int z){
        AxisAlignedBB blockBoundingBox = new AxisAlignedBB(x,y,z,x + 1, y + 1,z + 1);
        return blockBoundingBox.clip(this.ce.save.thePlayer.boundingBox);
    }


    public void handleClick(boolean isLeftClick) {

        if (this.paused || !(this.ce.currentGui instanceof GuiInGame) || this.delayWhenExitingUI > 0) {
            return;
        }

        // 1. Compute ray direction
        double[] ray = CosmicEvolution.camera.rayCast(3);
        Vector3d dir = new Vector3d(
                (float)(ray[0] - ce.save.thePlayer.x),
                (float)(ray[1] - (ce.save.thePlayer.y + ce.save.thePlayer.height / 2)
                        - (this.ce.save.thePlayer.isShifting ? EntityPlayer.SHIFT_DISTANCE : 0)),
                (float)(ray[2] - ce.save.thePlayer.z)
        );

        double rayLength = dir.length();
        dir.normalize();

        final double step = 0.05f * rayLength;
        final int maxSteps = 30;

        double px = ce.save.thePlayer.x;
        double py = (ce.save.thePlayer.y + ce.save.thePlayer.height / 2) - (this.ce.save.thePlayer.isShifting ? EntityPlayer.SHIFT_DISTANCE : 0);
        double pz = ce.save.thePlayer.z;

        // 2. March along the ray
        for (int i = 0; i < maxSteps; i++) {

            double cx = px + dir.x * step * i;
            double cy = py + dir.y * step * i;
            double cz = pz + dir.z * step * i;

            // ENTITY HIT CHECK
            if (hasHitEntity(cx, cy, cz)) {
                return;
            }

            // BLOCK COORDS
            int bx = MathUtil.floorDouble(cx);
            int by = MathUtil.floorDouble(cy);
            int bz = MathUtil.floorDouble(cz);

            Block block = Block.list[getBlockID(bx, by, bz)];

            // BOUNDING BOX CHECK
            if (!this.intersectsBlockBoundingBox(block, cx, cy, cz)) {
                continue;
            }

            // --- SPECIAL CASE: 3D CRAFTING BLOCK ---
            if (block.ID == Block.crafting3DItem.ID) {
                double[] hit = AxisAlignedBB.intersectRayWithBlockAABB(px, py, pz, dir, bx, by, bz);

                if(hit != null) {

                    this.handleIntersectForInWorldCraftingBlock(
                            hit[0], hit[1], hit[2],
                            dir,
                            getInWorldCraftingBlock(bx, by, bz),
                            isLeftClick, bx, by, bz
                    );
                }
                return;
            }

            // --- LEFT CLICK: BREAK BLOCK ---
            if (isLeftClick) {

                if (GuiInGame.isBlockVisible(bx, by, bz) &&
                        block.ID != Block.air.ID &&
                        block.ID != Block.water.ID) {

                    this.handleBlockBreaking(block, bx, by, bz);
                }

                if (block.canBeBroken) {
                    break;
                }
            }

            // --- RIGHT CLICK: PLACE BLOCK  ---
            else if(this.getBlockID(bx, by, bz) != Block.air.ID && this.getBlockID(bx,by,bz) != Block.water.ID) {
                block.handleSpecialRightClickFunctions(bx, by, bz, this, CosmicEvolution.instance.save.thePlayer);
                this.handleBlockPlacement(bx, by, bz, cx, cy, cz);
                break;
            }
        }
    }

    private void handleBlockPlacement(int bx, int by, int bz, double hitX, double hitY, double hitZ) {

        EntityPlayer player = ce.save.thePlayer;
        int held = player.getHeldItem();

        if (held == Item.NULL_ITEM_REFERENCE) return;

        // Determine which face was hit
        int face = determineHitFace(bx, by, bz, hitX, hitY, hitZ);

        // Adjacent block position
        int px = bx + Block.faceOffsetX[face];
        int py = by + Block.faceOffsetY[face];
        int pz = bz + Block.faceOffsetZ[face];

        // Prevent placing inside player
        if (wouldBlockIntersectPlayer(px, py, pz)) return;

        // Place block

        Block.list[held].onRightClick(px, py, pz, this, player);
    }

    public int determineHitFace(int bx, int by, int bz, double hitX, double hitY, double hitZ) {

        double localX = hitX - bx;
        double localY = hitY - by;
        double localZ = hitZ - bz;

        // Clamp to [0,1]
        localX = Math.max(0f, Math.min(1f, localX));
        localY = Math.max(0f, Math.min(1f, localY));
        localZ = Math.max(0f, Math.min(1f, localZ));

        double distUp    = 1f - localY; // y = 1
        double distDown  = localY;      // y = 0

        double distNorth = localX;      // x = 0
        double distSouth = 1f - localX; // x = 1

        double distEast  = localZ;      // z = 0  (EAST = -Z)
        double distWest  = 1f - localZ; // z = 1  (WEST = +Z)

        double min = distUp;
        int face = Block.FACE_UP;

        if (distDown  < min) { min = distDown;  face = Block.FACE_DOWN;  }
        if (distNorth < min) { min = distNorth; face = Block.FACE_NORTH; }
        if (distSouth < min) { min = distSouth; face = Block.FACE_SOUTH; }
        if (distEast  < min) { min = distEast;  face = Block.FACE_EAST;  }
        if (distWest  < min) { min = distWest;  face = Block.FACE_WEST;  }

        return face;
    }



    public void handleIntersectForInWorldCraftingBlock(double worldX, double worldY, double worldZ, Vector3d dir, InWorldCraftingBlock craftingBlock, boolean isLeftClick, int bx, int by, int bz) {

        long now = System.currentTimeMillis();
        if (now - MouseListener.lastTimeClicked < 250) {
            return;
        }
        MouseListener.lastTimeClicked = now;


        // --- 2. Local coords ---
        double lx = worldX - bx;
        double ly = worldY - by;
        double lz = worldZ - bz;

        // --- 4. Ray stepping parameters ---
        double step = 0.001;     // high precision
        double maxDist = 2.5;    // enough to reach far side at shallow angles

        int layer = craftingBlock.activeCraftingLayer;

        double layerMinY = layer / 16.0;
        double layerMaxY = (layer + 1) / 16.0;

        double tol = 0.0;

        int highlightedIndex = -1;
        int highlightedX = -1;
        int highlightedZ = -1;


        // --- 5. Step along ray and find FIRST valid voxel ---
        for (double t = 0.0; t <= maxDist; t += step) {

            double x = lx + dir.x * t;
            double y = ly + dir.y * t;
            double z = lz + dir.z * t;

            // Only break if we leave the block vertically
            if (y < 0 || y > 1)
                break;


            // Ignore X/Z out of bounds — DO NOT break
            if (x < 0 || x > 1 || z < 0 || z > 1)
                continue;


            // Must be within the active layer (with tolerance)
            if (y < layerMinY - tol|| y > layerMaxY + tol)
                continue;

            // Must be inside crafting footprint

            if (x < 0.125 || x > 0.875) continue;
            if (z < 0.125 || z > 0.875) continue;

            int xIndex = (int)((x - 0.125) / 0.0625);
            int zIndex = (int)((z - 0.125) / 0.0625);

            // Clamp to avoid out-of-bounds
            xIndex = Math.max(0, Math.min(11, xIndex));
            zIndex = Math.max(0, Math.min(11, zIndex));

            int index = xIndex + zIndex * 12;

            // FIRST valid voxel wins — stop immediately
            highlightedIndex = index;
            highlightedX = xIndex;
            highlightedZ = zIndex;

            // No voxel under cursor
            if (highlightedIndex == -1) return;

            boolean filled = craftingBlock.subVoxelIndices[layer][highlightedIndex] != 0;

            // --- 6. LEFT CLICK: remove highlighted voxel if filled ---
            if (isLeftClick) {
                if (filled) {
                    craftingBlock.removeSubVoxel(highlightedIndex);
                } else {
                    continue;
                }
                return;
            }

            // --- 7. RIGHT CLICK: placement rules ---
            if (filled) return; // can't place on filled voxel

            // A. Required voxel?
            boolean required = craftingBlock.craftingRecipe.recipeIndices[layer][highlightedIndex] == 1;
            if (required) {
                craftingBlock.addSubVoxel(highlightedIndex);
                return;
            }

            // B. Adjacent support?
            boolean hasSupport = false;
            int[] layerData = craftingBlock.subVoxelIndices[layer];

            // left
            if (highlightedX > 0 && layerData[highlightedIndex - 1] != 0) hasSupport = true;
            // right
            if (highlightedX < 11 && layerData[highlightedIndex + 1] != 0) hasSupport = true;
            // up
            if (highlightedZ > 0 && layerData[highlightedIndex - 12] != 0) hasSupport = true;
            // down
            if (highlightedZ < 11 && layerData[highlightedIndex + 12] != 0) hasSupport = true;

            if (hasSupport) {
                craftingBlock.addSubVoxel(highlightedIndex);
            } else {
                continue;
            }

            break;
        }
    }







    private void handleBlockBreaking(Block block, int bx, int by, int bz) {
        EntityPlayer p = ce.save.thePlayer;

        if (p.blockLookingAt[0] == bx && p.blockLookingAt[1] == by && p.blockLookingAt[2] == bz) {

            short held = p.getHeldItem();
            if (held == Item.NULL_ITEM_REFERENCE) held = 0;

            p.breakTimer++;

            if (block.hardness > (p.hardnessThreshold + Item.list[held].hardness)) {
                p.breakTimer = 1;
            }

            if (block.requiresTool && !block.toolType.equals(Item.list[held].toolType)) {
                p.breakTimer = 1;
            }

            if (p.breakTimer >= block.getDynamicBreakTimer() && block.breakTimer >= 0) {
                block.onLeftClick(bx, by, bz, this, p);
                p.breakTimer = 0;
            } else if (ce.save.time % 20 == 0) {
                CosmicEvolution.instance.soundPlayer.playSound(
                        bx, by, bz,
                        new Sound(block.stepSound, false, 1f),
                        new Random().nextFloat(0.4F, 0.7F)
                );
            }

        } else {
            p.breakTimer = 0;
        }
    }


}
