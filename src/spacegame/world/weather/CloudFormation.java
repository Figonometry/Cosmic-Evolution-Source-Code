package spacegame.world.weather;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.world.World;

import java.util.Random;

public final class CloudFormation {
    public Cloud[] clouds;
    public Cloud centralCloud;
    public int formationType;
    public float precipitation;
    public static final int CLOUD_TYPE_STRATUS = 0;
    public static final int CLOUD_TYPE_CIRRUS = 1;
    public static final int CLOUD_TYPE_CUMULUS = 2;
    public static final int CLOUD_TYPE_CUMULONIMBUS = 3;
    public static final int CLOUD_TYPE_NIMBOSTRATUS = 4;
    public long timeGenerated;


    public CloudFormation(double x, double y, double z, int type, long killTime, float precipitation){
        if(CosmicEvolution.instance.save.activeWorld.cloudCount >= World.CLOUD_LIMIT)return;
        this.formationType = type;
        this.precipitation = precipitation;
        this.centralCloud = this.buildCentralCloudFromType(x,y,z,killTime);
        this.clouds = this.buildCloudsFromType(x,y,z,killTime);
        this.timeGenerated = CosmicEvolution.instance.save.time;
    }

    public CloudFormation(NBTTagCompound cloudFormationTag){
        this.formationType = cloudFormationTag.getInteger("formationType");
        this.precipitation = cloudFormationTag.getFloat("precipitation");
        this.timeGenerated = cloudFormationTag.getLong("timeGenerated");

        NBTTagCompound centralCloudTag = cloudFormationTag.getCompoundTag("centralCloud");
        if(centralCloudTag != null){
            this.centralCloud = new Cloud(centralCloudTag);
        }

        int cloudNumber = cloudFormationTag.getInteger("cloudNumber");
        this.clouds = new Cloud[cloudNumber + 1];
        NBTTagCompound cloudTag;
        for(int i = 0; i < cloudNumber; i++){
            cloudTag = cloudFormationTag.getCompoundTag("cloud " + i);
            if(cloudTag != null) {
                this.clouds[i] = new Cloud(cloudTag);
            }
        }
    }


    public void saveCloudFormationToFile(NBTTagCompound cloudFormationTag){
        cloudFormationTag.setInteger("formationType", this.formationType);
        cloudFormationTag.setFloat("precipitation", this.precipitation);
        cloudFormationTag.setLong("timeGenerated", this.timeGenerated);

        if(this.centralCloud != null){
            NBTTagCompound centralCloud = new NBTTagCompound();
            this.centralCloud.saveCloudToFile(centralCloud);
            cloudFormationTag.setTag("centralCloud", centralCloud);
        }

        int cloudNumber = 0;
        NBTTagCompound[] cloudTags = new NBTTagCompound[this.clouds.length];
        for(int i = 0; i < this.clouds.length; i++){
            if(this.clouds[i] == null){
                cloudNumber++;
                continue;
            }
            cloudTags[i] = new NBTTagCompound();

            this.clouds[i].saveCloudToFile(cloudTags[i]);

            cloudFormationTag.setTag("cloud " + cloudNumber, cloudTags[i]);
            cloudNumber++;
        }

        cloudFormationTag.setInteger("cloudNumber" , cloudNumber);
    }


    private Cloud buildCentralCloudFromType(double x, double y, double z, long killTime){
        switch (this.formationType){
            case CLOUD_TYPE_CUMULUS -> {
                float width = 2f;
                float height = 2f;
                float depth = 2f;
                return new Cloud(x,y,z,width,height,depth, this.precipitation, killTime, 1);
            }
            case CLOUD_TYPE_CUMULONIMBUS -> {
                float width = 64;
                float height = 64;
                float depth = 64;
                return new Cloud(x,y,z,width,height,depth, this.precipitation, killTime, 1);
            }
            default -> {
                return null;
            }
        }
    }
    private Cloud[] buildCloudsFromType(double x, double y, double z, long killTime){
        switch (this.formationType){
            case CLOUD_TYPE_CIRRUS -> {
                Cloud[] clouds = new Cloud[16];

                float width;
                float depth;
                float height;

                for(int i = 0; i < clouds.length; i++){
                    width = CosmicEvolution.globalRand.nextFloat(256, 2048);
                    depth = CosmicEvolution.globalRand.nextFloat(8, 64);
                    height = CosmicEvolution.globalRand.nextFloat(2, 10);
                    double xPos = CosmicEvolution.globalRand.nextDouble(128, 256);
                    double zPos = CosmicEvolution.globalRand.nextDouble(128, 256);
                    xPos = CosmicEvolution.globalRand.nextBoolean() ? xPos : -xPos;
                    zPos = CosmicEvolution.globalRand.nextBoolean() ? zPos : -zPos;
                    clouds[i] = new Cloud(x + xPos, y + CosmicEvolution.globalRand.nextDouble(-10, 10), z + zPos, width, height, depth, this.precipitation, killTime, CosmicEvolution.globalRand.nextFloat());
                }

                return clouds;
            }
            case CLOUD_TYPE_STRATUS -> {
                Cloud[] clouds = new Cloud[16];

                float width;
                float depth ;
                float height;

                for(int i = 0; i < clouds.length; i++){
                    width = CosmicEvolution.globalRand.nextFloat(128, 256);
                    depth = CosmicEvolution.globalRand.nextFloat(64, 128);
                    height = CosmicEvolution.globalRand.nextFloat(5, 20);
                    width *= 5;
                    depth *= 5;
                    clouds[i] = new Cloud(x + CosmicEvolution.globalRand.nextDouble(-128, 128), y + CosmicEvolution.globalRand.nextDouble(-10, 50), z + CosmicEvolution.globalRand.nextDouble(-128, 128), width, height, depth, this.precipitation, killTime, 1);
                }

                return clouds;
            }
            case CLOUD_TYPE_NIMBOSTRATUS -> {
                Cloud[] clouds = new Cloud[16];

                float width;
                float depth ;
                float height;

                for(int i = 0; i < clouds.length; i++){
                    width = CosmicEvolution.globalRand.nextFloat(128, 256);
                    depth = CosmicEvolution.globalRand.nextFloat(64, 128);
                    height = CosmicEvolution.globalRand.nextFloat(30, 50);
                    width *= 5;
                    depth *= 5;
                    clouds[i] = new Cloud(x + CosmicEvolution.globalRand.nextDouble(-128, 128), y + CosmicEvolution.globalRand.nextDouble(-10, 50), z + CosmicEvolution.globalRand.nextDouble(-128, 128), width, height, depth, this.precipitation, killTime, 1);
                }

                return clouds;
            }
            case CLOUD_TYPE_CUMULUS -> {
                Random rand = new Random();
                Cloud[] clouds = new Cloud[16];
                float width;
                float height;
                float depth;
                for(int i = 0; i < clouds.length; i++){
                    width = rand.nextFloat(2, 6);
                    height = width;
                    depth = width;
                    clouds[i] = new Cloud(x + rand.nextInt(-4, 4), y + rand.nextInt(-4, 4), z + rand.nextInt(-4, 4), width, height, depth, this.precipitation,killTime, 1);
                }

                return clouds;
            }
            case CLOUD_TYPE_CUMULONIMBUS -> {
                Random rand = new Random();
                Cloud[] clouds = new Cloud[64];
                float width;
                float height;
                float depth;
                for(int i = 0; i < clouds.length; i++){
                    width = rand.nextFloat(64, 96);
                    height = width;
                    depth = width;
                    double xPos = CosmicEvolution.globalRand.nextDouble(0, 96);
                    double zPos = CosmicEvolution.globalRand.nextDouble(0, 96);
                    xPos = CosmicEvolution.globalRand.nextBoolean() ? xPos : -xPos;
                    zPos = CosmicEvolution.globalRand.nextBoolean() ? zPos : -zPos;
                    if(i <= 15) {
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(0, 64), z + zPos, width, height, depth, this.precipitation, killTime, 1);
                    } else if(i <= 31){
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(64, 128), z + zPos, width, height, depth, this.precipitation, killTime, 1);
                    } else if(i <= 47){
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(128, 196), z + zPos, width, height, depth, this.precipitation, killTime, 1);
                    } else {
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(196, 256), z + zPos, width, height, depth, this.precipitation, killTime, 1);
                    }
                }

                return clouds;
            }
        }
        return null;
    }


    public void update(){

        if(this.centralCloud != null) {
            this.centralCloud.update();
            if (this.centralCloud.strength < 0) {
                this.centralCloud = null;
            }
        }

        for(int i = 0; i < this.clouds.length; i++){
            if(this.clouds[i] == null)continue;

            this.clouds[i].update();
            if(this.clouds[i].strength < 0){
                this.clouds[i] = null;
            }
        }

    }

    public void render(float skyLightValue, float sunRed, float sunGreen, float sunBlue, RenderEngine.WorldTessellator tessellator){
        if(this.centralCloud != null) {
            this.centralCloud.render(skyLightValue, sunRed, sunGreen, sunBlue, tessellator);
        }
        for(int i = 0; i < this.clouds.length; i++){
            if(this.clouds[i] == null)continue;
            this.clouds[i].render(skyLightValue,sunRed,sunGreen,sunBlue, tessellator);
        }
    }

    public void scale(float scaleFactor){ //For cumulus clouds
        if(this.formationType != CLOUD_TYPE_CUMULUS)return;
        if(CosmicEvolution.instance.save.time >= this.timeGenerated + Timer.REAL_MINUTE)return;
        for(int i = 0; i < this.clouds.length; i++){
            if(this.clouds[i] == null)continue;
            double xDif =  this.clouds[i].x - this.centralCloud.x;
            double yDif = this.clouds[i].y - this.centralCloud.y;
            double zDif = this.clouds[i].z - this.centralCloud.z;

            xDif *= scaleFactor;
            yDif *= scaleFactor;
            zDif *= scaleFactor;

            this.clouds[i].x = this.centralCloud.x + xDif;
            this.clouds[i].y = this.centralCloud.y + yDif;
            this.clouds[i].z = this.centralCloud.z + zDif;

            this.clouds[i].scale(scaleFactor);
        }
        float currentHeight = 0;
        float newHeight = 0;
        if(this.centralCloud != null) {
            currentHeight = this.centralCloud.height;
            this.centralCloud.scale(scaleFactor);
            newHeight = this.centralCloud.height;

            this.centralCloud.y += (newHeight - currentHeight);
        }


        for(int i = 0; i < this.clouds.length; i++){
            if(this.clouds[i] == null)continue;
            this.clouds[i].y += (newHeight - currentHeight);
        }
    }
}
