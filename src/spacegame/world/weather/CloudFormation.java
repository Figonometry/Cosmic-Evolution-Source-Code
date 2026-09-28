package spacegame.world.weather;

import org.joml.Matrix4d;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.entity.EntityPlayer;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.MathUtil;
import spacegame.world.worldtypes.World;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public final class CloudFormation {
    public Cloud[] clouds;
    public Cloud centralCloud;
    public int formationType;
    public float scale = 1;
    public float strength = 0.001f;
    public boolean weaken;
    public float precipitation;
    public long killTime;
    public float maxStrength;
    public static final int CLOUD_TYPE_STRATUS = 0;
    public static final int CLOUD_TYPE_CIRRUS = 1;
    public static final int CLOUD_TYPE_CUMULUS = 2;
    public static final int CLOUD_TYPE_CUMULONIMBUS = 3;
    public static final int CLOUD_TYPE_NIMBOSTRATUS = 4;
    public long timeGenerated;
    private int vao;
    private int vbo;
    private int ebo;
    private int elementCount;
    protected int elementOffset = 0;
    public double x;
    public double y;
    public double z;
    private Vector3f chunkOffset = new Vector3f();


    public CloudFormation(double x, double y, double z, int type, long killTime, float precipitation){
        if(CosmicEvolution.instance.save.activeWorld.cloudCount >= World.CLOUD_LIMIT)return;
        this.x = x;
        this.y = y;
        this.z = z;
        this.formationType = type;
        this.precipitation = precipitation;
        this.centralCloud = this.buildCentralCloudFromType(x,y,z,killTime);
        this.clouds = this.buildCloudsFromType(x,y,z,killTime);
        this.timeGenerated = CosmicEvolution.instance.save.time;
        this.maxStrength = CosmicEvolution.globalRand.nextFloat();
        this.killTime = killTime;
        EntityPlayer player = CosmicEvolution.instance.save.thePlayer;
        this.chunkOffset.set(this.x - player.x, this.y - player.y, this.z - player.z);
        this.setupOpenGLState();
    }

    public CloudFormation(NBTTagCompound cloudFormationTag){
        this.formationType = cloudFormationTag.getInteger("formationType");
        this.precipitation = cloudFormationTag.getFloat("precipitation");
        this.timeGenerated = cloudFormationTag.getLong("timeGenerated");
        this.x = cloudFormationTag.getDouble("x");
        this.y = cloudFormationTag.getDouble("y");
        this.z = cloudFormationTag.getDouble("z");
        this.strength = cloudFormationTag.getFloat("strength");
        this.weaken = cloudFormationTag.getBoolean("weaken");
        this.precipitation = cloudFormationTag.getFloat("precipitation");
        this.killTime = cloudFormationTag.getLong("killTime");
        this.maxStrength = cloudFormationTag.getFloat("maxStrength");

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
        EntityPlayer player = CosmicEvolution.instance.save.thePlayer;
        this.chunkOffset.set(this.x - player.x, this.y - player.y, this.z - player.z);
        this.setupOpenGLState();
    }

    private void setupOpenGLState(){
        RenderEngine renderEngine = CosmicEvolution.instance.renderEngine;

        this.vao = renderEngine.createVAO();
        this.vbo = renderEngine.createBuffers();
        this.ebo = renderEngine.createBuffers();

        int positionsSize = 3;
        int texCoordsSize = 2;
        int normalSize = 3;
        int vertexSizeBytes;


        vertexSizeBytes = (positionsSize  + texCoordsSize + normalSize) * Float.BYTES;

        CosmicEvolution.instance.renderEngine.setVertexAttribute(this.vao, 0, positionsSize, vertexSizeBytes, 0, this.vbo);
        CosmicEvolution.instance.renderEngine.setVertexAttribute(this.vao, 1, texCoordsSize, vertexSizeBytes, (positionsSize) * Float.BYTES, this.vbo);
        CosmicEvolution.instance.renderEngine.setVertexAttribute(this.vao, 2, normalSize, vertexSizeBytes, (positionsSize  + texCoordsSize) * Float.BYTES, this.vbo);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(100000);
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(100000);

        if(this.centralCloud != null) {
            this.centralCloud.addCloudToRenderData(vertexBuffer, elementBuffer, this);
        }

        for(int i = 0; i < this.clouds.length; i++){
            if(this.clouds[i] == null)continue;

            this.clouds[i].addCloudToRenderData(vertexBuffer, elementBuffer, this);
        }

        vertexBuffer.flip();
        elementBuffer.flip();

        this.elementCount = elementBuffer.limit();

        GL46.glBindVertexArray(this.vao);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vbo);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL46.GL_STATIC_DRAW);


        GL46.glBindVertexArray(0);
    }

    public void clearOpenGLState(){
        RenderEngine renderEngine = CosmicEvolution.instance.renderEngine;

        renderEngine.deleteVAO(this.vao);
        renderEngine.deleteBuffers(this.vbo);
        renderEngine.deleteBuffers(this.ebo);
    }


    public void saveCloudFormationToFile(NBTTagCompound cloudFormationTag){
        cloudFormationTag.setInteger("formationType", this.formationType);
        cloudFormationTag.setFloat("precipitation", this.precipitation);
        cloudFormationTag.setLong("timeGenerated", this.timeGenerated);
        cloudFormationTag.setDouble("x", this.x);
        cloudFormationTag.setDouble("y", this.y);
        cloudFormationTag.setDouble("z", this.z);
        cloudFormationTag.setFloat("strength", this.strength);
        cloudFormationTag.setBoolean("weaken", this.weaken);
        cloudFormationTag.setFloat("precipitation", this.precipitation);
        cloudFormationTag.setLong("killTime", this.killTime);
        cloudFormationTag.setFloat("maxStrength", this.maxStrength);

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
                return new Cloud(x,y,z,width,height,depth);
            }
            case CLOUD_TYPE_CUMULONIMBUS -> {
                float width = 64;
                float height = 64;
                float depth = 64;
                return new Cloud(x,y,z,width,height,depth);
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
                    clouds[i] = new Cloud(x + xPos, y + CosmicEvolution.globalRand.nextDouble(-10, 10), z + zPos, width, height, depth);
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
                    clouds[i] = new Cloud(x + CosmicEvolution.globalRand.nextDouble(-128, 128), y + CosmicEvolution.globalRand.nextDouble(-10, 50), z + CosmicEvolution.globalRand.nextDouble(-128, 128), width, height, depth);
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
                    clouds[i] = new Cloud(x + CosmicEvolution.globalRand.nextDouble(-128, 128), y + CosmicEvolution.globalRand.nextDouble(-10, 50), z + CosmicEvolution.globalRand.nextDouble(-128, 128), width, height, depth);
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
                    clouds[i] = new Cloud(x + rand.nextInt(-4, 4), y + rand.nextInt(-4, 4), z + rand.nextInt(-4, 4), width, height, depth);
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
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(0, 64), z + zPos, width, height, depth);
                    } else if(i <= 31){
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(64, 128), z + zPos, width, height, depth);
                    } else if(i <= 47){
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(128, 196), z + zPos, width, height, depth);
                    } else {
                        clouds[i] = new Cloud(x + xPos, y + rand.nextInt(196, 256), z + zPos, width, height, depth);
                    }
                }

                return clouds;
            }
        }
        return null;
    }


    public void update(){
        this.weaken = CosmicEvolution.instance.save.time >= this.killTime;
        this.z -= 0.01f;

        this.strength += this.weaken ? -0.001f : 0.001f;
        this.strength = Math.min(this.strength, this.maxStrength);


        EntityPlayer player = CosmicEvolution.instance.save.thePlayer;


       this.chunkOffset.set(this.x - player.x, this.y - player.y, this.z - player.z);
    }


    public void render(){
        GL46.glBindVertexArray(this.vao);

        Shader.cloudShader.uploadFloat("scale", this.scale);
        Shader.cloudShader.uploadFloat("strength", this.strength);
        Shader.cloudShader.uploadFloat("precipitation", this.precipitation);
        Shader.cloudShader.uploadInt("uTexture", 0);

        Shader.cloudShader.uploadVec3f("chunkOffset", this.chunkOffset);

        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementCount, GL46.GL_UNSIGNED_INT, 0);
    }

    public void scale(float scaleFactor){ //For cumulus clouds
        if(this.formationType != CLOUD_TYPE_CUMULUS)return;
        if(CosmicEvolution.instance.save.time >= this.timeGenerated + Timer.REAL_MINUTE)return;
        this.scale *= scaleFactor;
    }
}
