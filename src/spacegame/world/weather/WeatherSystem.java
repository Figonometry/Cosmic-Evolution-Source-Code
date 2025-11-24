package spacegame.world.weather;


import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.util.MathUtil;

import java.util.ArrayList;

public final class WeatherSystem {
    public static final int WEATHER_SYSTEM_TYPE_CLEAR = 0;
    public static final int WEATHER_SYSTEM_TYPE_PARTLY_CLOUDY = 1; //With a chance of meatballs
    public static final int WEATHER_SYSTEM_TYPE_OVERCAST = 2;
    public static final int WEATHER_SYSTEM_TYPE_RAIN_STRATUS = 3;
    public static final int WEATHER_SYSTEM_TYPE_RAIN_THUNDERSTORM = 4;
    public float cloudCoverage;
    public int type;
    public double x;
    public double y;
    public double z;
    public long killTime;
    public CloudGenerator[] generators;
    public ArrayList<CloudFormation> cloudFormations = new ArrayList<>();

    public WeatherSystem(int type, double x, double y, double z, long duration){ //assume y = 0
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.killTime = CosmicEvolution.instance.save.time + duration;

        if(this.type == WEATHER_SYSTEM_TYPE_PARTLY_CLOUDY){
            this.cloudCoverage = CosmicEvolution.globalRand.nextFloat(0.75f);
        }

        this.createCloudGenerators(x,y,z, duration);
    }


    public WeatherSystem(NBTTagCompound weatherTag){
        this.cloudCoverage = weatherTag.getFloat("cloudCoverage");
        this.type = weatherTag.getInteger("type");
        this.x = weatherTag.getDouble("x");
        this.y = weatherTag.getDouble("y");
        this.z = weatherTag.getDouble("z");
        this.killTime = weatherTag.getLong("killTime");

        int numberOfGenerators = weatherTag.getInteger("generatorCount");
        this.generators = new CloudGenerator[numberOfGenerators + 1];
        NBTTagCompound generatorTag;
        for(int i = 0; i < numberOfGenerators; i++){
            generatorTag = weatherTag.getCompoundTag("generator " + i);
            if(generatorTag != null) {
                this.generators[i] = new CloudGenerator(generatorTag, this);
            }
        }

        int numberOfCloudFormations = weatherTag.getInteger("formationCount");
        NBTTagCompound cloudFormationTag;
        for(int i = 0; i < numberOfCloudFormations; i++){
            cloudFormationTag = weatherTag.getCompoundTag("formation " + i);
            if(cloudFormationTag != null) {
                this.cloudFormations.add(new CloudFormation(cloudFormationTag));
            }
        }

    }


    public static int getRandomWeatherSystemType(int rand){
        switch (rand){
            case WEATHER_SYSTEM_TYPE_CLEAR -> {
                return WEATHER_SYSTEM_TYPE_CLEAR;
            }
            case WEATHER_SYSTEM_TYPE_PARTLY_CLOUDY -> {
                return WEATHER_SYSTEM_TYPE_PARTLY_CLOUDY;
            }
            case WEATHER_SYSTEM_TYPE_OVERCAST -> {
                return WEATHER_SYSTEM_TYPE_OVERCAST;
            }
            case WEATHER_SYSTEM_TYPE_RAIN_STRATUS -> {
                return WEATHER_SYSTEM_TYPE_RAIN_STRATUS;
            }
            case WEATHER_SYSTEM_TYPE_RAIN_THUNDERSTORM -> {
                return WEATHER_SYSTEM_TYPE_RAIN_THUNDERSTORM;
            }
        }

        return WEATHER_SYSTEM_TYPE_CLEAR;
    }

    public void saveWeatherSystemToFile(NBTTagCompound weatherTag){
        weatherTag.setFloat("cloudCoverage", this.cloudCoverage);
        weatherTag.setInteger("type", this.type);
        weatherTag.setDouble("x", this.x);
        weatherTag.setDouble("y", this.y);
        weatherTag.setDouble("z", this.z);
        weatherTag.setLong("killTime", this.killTime);

        NBTTagCompound[] cloudGenerators = new NBTTagCompound[this.generators.length];
        int generatorNumber = 0;
        for(int i = 0; i < this.generators.length; i++){
            if(this.generators[i] == null)continue;
            cloudGenerators[i] = new NBTTagCompound();
            this.generators[i].saveCloudGeneratorToFile(cloudGenerators[i]);

            weatherTag.setTag( "generator " + generatorNumber, cloudGenerators[i]);
            generatorNumber++;
        }

        weatherTag.setInteger("generatorCount", generatorNumber);

        NBTTagCompound[] cloudFormations = new NBTTagCompound[this.cloudFormations.size()];
        int cloudFormationNumber = 0;
        for(int i = 0; i < this.cloudFormations.size(); i++){
            cloudFormations[i] = new NBTTagCompound();
            this.cloudFormations.get(i).saveCloudFormationToFile(cloudFormations[i]);

            weatherTag.setTag("formation " + cloudFormationNumber, cloudFormations[i]);
            cloudFormationNumber++;
        }

        weatherTag.setInteger("formationCount", cloudFormationNumber);
    }

    private void createCloudGenerators(double x, double y, double z, long duration){
        CloudGenerator[] generators = null;
        switch (this.type){
            case WEATHER_SYSTEM_TYPE_CLEAR -> { //There shouldnt be that many clouds up high, match the killtime to the periodicity
                generators = new CloudGenerator[3];
                if(CosmicEvolution.globalRand.nextBoolean()){
                    long firstPeriodicity = (long) (Timer.REAL_MINUTE * CosmicEvolution.globalRand.nextFloat(1,4));
                    long secondPeriodicity = (long) (Timer.REAL_MINUTE * CosmicEvolution.globalRand.nextFloat(1,4));
                    long thirdPeriodicity = (long) (Timer.REAL_MINUTE * CosmicEvolution.globalRand.nextFloat(1,4));
                    generators[0] = new CloudGenerator(x, y + 1024, z + 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CIRRUS, this, firstPeriodicity, firstPeriodicity / 2, 0);
                    generators[1] = new CloudGenerator(x - 256, y + 1024, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CIRRUS, this, secondPeriodicity, secondPeriodicity / 2, 0);
                    generators[2] = new CloudGenerator(x + 256, y + 1024, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CIRRUS, this, thirdPeriodicity, thirdPeriodicity / 2, 0);
                }
            }
            case WEATHER_SYSTEM_TYPE_PARTLY_CLOUDY -> {
                boolean useCirrus = CosmicEvolution.globalRand.nextBoolean();
                generators = new CloudGenerator[useCirrus ? 6 : 3];
                long firstPeriodicity = (long) (Timer.REAL_MINUTE * CosmicEvolution.globalRand.nextFloat(1,4));
                long secondPeriodicity = (long) (Timer.REAL_MINUTE * CosmicEvolution.globalRand.nextFloat(1,4));
                long thirdPeriodicity = (long) (Timer.REAL_MINUTE * CosmicEvolution.globalRand.nextFloat(1,4));
                generators[0] = new CloudGenerator(x, y + 128, z + 256, CosmicEvolution.instance.save.time + duration, CosmicEvolution.globalRand.nextBoolean() ? CloudFormation.CLOUD_TYPE_CUMULUS : CloudFormation.CLOUD_TYPE_STRATUS, this, (long) (firstPeriodicity / 2 + (-2.25f + this.cloudCoverage)), firstPeriodicity * 2, CosmicEvolution.globalRand.nextFloat(0.25f));
                generators[1] = new CloudGenerator(x - 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CosmicEvolution.globalRand.nextBoolean() ? CloudFormation.CLOUD_TYPE_CUMULUS : CloudFormation.CLOUD_TYPE_STRATUS, this, (long) (secondPeriodicity / 2 + (-2.25f + this.cloudCoverage)), secondPeriodicity * 2, CosmicEvolution.globalRand.nextFloat(0.25f));
                generators[2] = new CloudGenerator(x + 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CosmicEvolution.globalRand.nextBoolean() ? CloudFormation.CLOUD_TYPE_CUMULUS : CloudFormation.CLOUD_TYPE_STRATUS, this, (long) (thirdPeriodicity / 2 + (-2.25f + this.cloudCoverage)), thirdPeriodicity * 2, CosmicEvolution.globalRand.nextFloat(0.25f));
                if(useCirrus){
                    generators[3] = new CloudGenerator(x, y + 1024, z + 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CIRRUS, this, firstPeriodicity, firstPeriodicity, 0);
                    generators[4] = new CloudGenerator(x - 256, y + 1024, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CIRRUS, this, secondPeriodicity, secondPeriodicity, 0);
                    generators[5] = new CloudGenerator(x + 256, y + 1024, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CIRRUS, this, thirdPeriodicity, thirdPeriodicity, 0);
                }
            }
            case WEATHER_SYSTEM_TYPE_OVERCAST -> {
                generators = new CloudGenerator[3];
                long periodicity = Timer.REAL_MINUTE;
                float precipitation = CosmicEvolution.globalRand.nextFloat(0.25f, 0.75f);
                generators[0] = new CloudGenerator(x, y + 128, z + 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_STRATUS, this, periodicity, Timer.REAL_MINUTE * 10, precipitation);
                generators[1] = new CloudGenerator(x - 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_STRATUS, this, periodicity, Timer.REAL_MINUTE * 10, precipitation);
                generators[2] = new CloudGenerator(x + 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_STRATUS, this, periodicity, Timer.REAL_MINUTE * 10, precipitation);
            }
            case WEATHER_SYSTEM_TYPE_RAIN_STRATUS -> {
                generators = new CloudGenerator[3];
                long periodicity = Timer.REAL_MINUTE;
                float precipitation = CosmicEvolution.globalRand.nextFloat(0.5f, 1f);
                generators[0] = new CloudGenerator(x, y + 128, z + 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_NIMBOSTRATUS, this, periodicity, Timer.REAL_MINUTE * 10, precipitation);
                generators[1] = new CloudGenerator(x - 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_NIMBOSTRATUS, this, periodicity, Timer.REAL_MINUTE * 10, precipitation);
                generators[2] = new CloudGenerator(x + 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_NIMBOSTRATUS, this, periodicity, Timer.REAL_MINUTE * 10, precipitation);
            }
            case WEATHER_SYSTEM_TYPE_RAIN_THUNDERSTORM -> {
                generators = new CloudGenerator[3];
                long periodicity = Timer.REAL_MINUTE / 2;
                float precipitation = 1f;
                generators[0] = new CloudGenerator(x, y + 128, z + 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CUMULONIMBUS, this, periodicity, Timer.REAL_MINUTE * 5, precipitation);
                generators[1] = new CloudGenerator(x - 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CUMULONIMBUS, this, periodicity, Timer.REAL_MINUTE * 5, precipitation);
                generators[2] = new CloudGenerator(x + 256, y + 128, z - 256, CosmicEvolution.instance.save.time + duration, CloudFormation.CLOUD_TYPE_CUMULONIMBUS, this, periodicity, Timer.REAL_MINUTE * 5, precipitation);
            }
        }
        this.generators = generators;
    }

    private boolean inRangeOfPlayer(){
        int playerX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x);
        int playerZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);

        int weatherX = MathUtil.floorDouble(this.x);
        int weatherZ = MathUtil.floorDouble(this.z);

        return Math.abs(weatherX - playerX) < 512 && Math.abs(weatherZ - playerZ) < 512;
    }

    public void update(){
        if(!this.inRangeOfPlayer())return;
        CloudFormation formation;
        for(int i = 0; i < this.cloudFormations.size(); i++){
            formation = this.cloudFormations.get(i);
            formation.update();
            if(CosmicEvolution.instance.save.time % 15 == 0) {
                formation.scale(1.01f);
            }

            deleteFormation:
            if(formation.centralCloud == null){
                for(int j = 0; j < formation.clouds.length; j++){
                    if(formation.clouds[j] != null){
                        break deleteFormation;
                    }
                }
                this.cloudFormations.remove(formation);
            }
        }

        this.cloudFormations.trimToSize();

        for(int i = 0; i < this.generators.length; i++){
            if(this.generators[i] == null)continue;
            if(CosmicEvolution.instance.save.time % this.generators[i].periodicity != 0) continue;
            this.generators[i].generateClouds();
        }
    }

    public void render(float skyLightValue, float sunRed, float sunGreen, float sunBlue, RenderEngine.WorldTessellator tessellator){
        CloudFormation formation;
        for(int i = 0; i < this.cloudFormations.size(); i++){
            formation = this.cloudFormations.get(i);
            formation.render(skyLightValue, sunRed, sunGreen, sunBlue, tessellator);
        }
    }
}
