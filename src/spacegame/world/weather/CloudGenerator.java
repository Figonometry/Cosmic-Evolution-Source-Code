package spacegame.world.weather;

import spacegame.core.CosmicEvolution;
import spacegame.nbt.NBTTagCompound;

public final class CloudGenerator {
    public double x;
    public double y;
    public double z;
    public long killTime;
    public int genCloudType;
    public WeatherSystem weatherSystem;
    public long periodicity;
    public long duration;
    public float precipitationLevel;



    public CloudGenerator(double x, double y, double z, long killTime, int genCloudType, WeatherSystem weatherSystem, long periodicity, long duration, float precipitationLevel){
        this.x = x;
        this.y = y;
        this.z = z;
        this.killTime = killTime;
        this.genCloudType = genCloudType;
        this.weatherSystem = weatherSystem;
        this.periodicity = periodicity;
        this.duration = duration;
        this.precipitationLevel = precipitationLevel;
    }

    public CloudGenerator(NBTTagCompound cloudGeneratorTag, WeatherSystem weatherSystem){
        this.weatherSystem = weatherSystem;
        this.x = cloudGeneratorTag.getDouble("x");
        this.y = cloudGeneratorTag.getDouble("y");
        this.z = cloudGeneratorTag.getDouble("z");
        this.killTime = cloudGeneratorTag.getLong("killTime");
        this.genCloudType = cloudGeneratorTag.getInteger("genCloudType");
        this.periodicity = cloudGeneratorTag.getLong("periodicity");
        this.duration = cloudGeneratorTag.getLong("duration");
        this.precipitationLevel = cloudGeneratorTag.getFloat("precipitationLevel");
    }

    public void saveCloudGeneratorToFile(NBTTagCompound cloudGeneratorTag){
        cloudGeneratorTag.setDouble("x", this.x);
        cloudGeneratorTag.setDouble("y", this.y);
        cloudGeneratorTag.setDouble("z", this.z);
        cloudGeneratorTag.setLong("killTime", this.killTime);
        cloudGeneratorTag.setInteger("genCloudType", this.genCloudType);
        cloudGeneratorTag.setLong("periodicity", this.periodicity);
        cloudGeneratorTag.setLong("duration", this.duration);
        cloudGeneratorTag.setFloat("precipitationLevel", this.precipitationLevel);
    }


    public void generateClouds(){
        double xPos = CosmicEvolution.globalRand.nextDouble(256);
        double yPos = CosmicEvolution.globalRand.nextDouble(10);
        double zPos = CosmicEvolution.globalRand.nextDouble(256);
        double x = CosmicEvolution.globalRand.nextBoolean() ? xPos : -xPos;
        double y = CosmicEvolution.globalRand.nextBoolean() ? yPos : -yPos;
        double z = CosmicEvolution.globalRand.nextBoolean() ? zPos : -zPos;
        this.weatherSystem.cloudFormations.add(new CloudFormation(this.x + x, this.y + y, this.z + z, this.genCloudType, CosmicEvolution.instance.save.time + this.duration, this.precipitationLevel));
    }
}
