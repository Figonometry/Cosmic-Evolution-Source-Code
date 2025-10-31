package spacegame.world;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public final class NoiseMap3D {
    public int width;
    public int height;
    public int depth;
    public float[] noise;
    public int octaves;
    public double yScale = -0.02;

    public NoiseMap3D(int width, int height, int depth, int octaves, long seed){
        Random rand = new Random(seed);
        float[] offset = new float[octaves];
        for (int i = 0; i < offset.length; i++) {
            offset[i] = rand.nextInt(1000) + rand.nextFloat();
        }
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.octaves = octaves;
        this.noise = new float[width * height * depth];

        int[] p = { -105, -96, -119, 91, 90, 15, -125, 13, -55, 95, 96, 53, -62, -23, 7, -31, -116, 36, 103, 30, 69, -114, 8, 99, 37, -16,
                21, 10, 23, -66, 6, -108, -9, 120, -22, 75, 0, 26, -59, 62, 94, -4, -37, -53, 117, 35, 11, 32, 57, -79, 33, 88, -19, -107, 56, 87, -82, 20, 125,
                -120, -85, -88, 68, -81, 74, -91, 71, -122, -117, 48, 27, -90, 77, -110, -98, -25, 83, 111, -27, 122, 60, -45, -123, -26, -36, 105, 92, 41, 55, 46,
                -11, 40, -12, 102, -113, 54, 65, 25, 63, -95, 1, -40, 80, 73, -47, 76, -124, -69, -48, 89, 18, -87, -56, -60, -121, -126, 116, -68, -97, 86, -92,
                100, 109, -58, -83, -70, 3, 64, 52, -39, -30, -6, 124, 123, 5, -54, 38, -109, 118, 126, -1, 82, 85, -44, -49, -50, 59, -29, 47, 16, 58, 17, -74,
                -67, 28, 42, -33, -73, -86, -43, 119, -8, -104, 2, 44, -102, -93, 70, -35, -103, 101, -101, -89, 43, -84, 9, -127, 22, 39, -3, 19, 98, 108, 110,
                79, 113, -32, -24, -78, -71, 112, 104, -38, -10, 97, -28, -5, 34, -14, -63, -18, -46, -112, 12, -65, -77, -94, -15, 81, 51, -111, -21, -7, 14, -17,
                107, 49, -64, -42, 31, -75, -57, 106, -99, -72, 84, -52, -80, 115, 121, 50, 45, 127, 4, -106, -2, -118, -20, -51, 93, -34, 114, 67, 29, 24, 72,
                -13, -115, -128, -61, 78, 66, -41, 61, -100, -76 };
        int permBufferID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_SHADER_STORAGE_BUFFER, permBufferID);
        IntBuffer permData = BufferUtils.createIntBuffer(512);
        IntBuffer permMod12Data = BufferUtils.createIntBuffer(512);

        for (int i = 0; i < 512; i++) {
            permData.put(p[i & 255]);
            permMod12Data.put((p[i & 255] & 0xFF) % 12);
        }
        permData.flip();
        permMod12Data.flip();

        GL46.glBufferData(GL46.GL_SHADER_STORAGE_BUFFER, permData, GL46.GL_DYNAMIC_DRAW);
        GL46.glBindBufferBase(GL46.GL_SHADER_STORAGE_BUFFER, 1, permBufferID);

        int permModBufferID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_SHADER_STORAGE_BUFFER, permModBufferID);
        GL46.glBufferData(GL46.GL_SHADER_STORAGE_BUFFER, permMod12Data, GL46.GL_DYNAMIC_DRAW);
        GL46.glBindBufferBase(GL46.GL_SHADER_STORAGE_BUFFER, 2, permModBufferID);

        Shader noiseShader = new Shader("src/spacegame/assets/shader/NoiseMap3D.glsl", true);
        noiseShader.uploadInt("width", this.width);
        noiseShader.uploadInt("height", this.height);
        noiseShader.uploadInt("depth", this.depth);
        noiseShader.uploadInt("octaves", this.octaves);
        noiseShader.uploadFloatArray("offsets", offset);

        int bufferID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_SHADER_STORAGE_BUFFER, bufferID);
        GL46.glBufferData(GL46.GL_SHADER_STORAGE_BUFFER, (long) Float.BYTES * width * height * depth, GL46.GL_DYNAMIC_DRAW);
        GL46.glBindBufferBase(GL46.GL_SHADER_STORAGE_BUFFER, 0 , bufferID);

        GL46.glDispatchCompute((width + 7) / 8, (height + 7) / 8, (depth + 7) / 8);
        GL46.glMemoryBarrier(GL46.GL_SHADER_STORAGE_BARRIER_BIT);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(this.width * this.height * this.depth);

        GL46.glGetBufferSubData(GL46.GL_SHADER_STORAGE_BUFFER, 0, buffer);

        buffer.rewind();

        float[] noiseArray = new float[this.width * this.height * this.depth];
        buffer.get(noiseArray);

        this.noise = noiseArray;
        GL46.glDeleteShader(noiseShader.shaderProgramID);
        CosmicEvolution.instance.renderEngine.deleteBuffers(permModBufferID);
        CosmicEvolution.instance.renderEngine.deleteBuffers(bufferID);
    }


    public double getNoise(int x, int y, int z, int sample, double continentalScale, double yScale){
        final int rawY = y;

        int x1 = x + sample;
        int y1 = y + sample;
        int z1 = z + sample;




        if(y < 0){
            y *= -1;
        }

        if (x < 0) {
            x *= -1;
        }

        if (z < 0) {
            z *= -1;
        }

        x %= this.width * 2;
        y %= this.height * 2;
        z %= this.depth * 2;
        if (x > this.width - 1) {
            x = x - ((x - (this.width - 1)) * 2);
            x++;
        }
        if (y > this.height - 1) {
            y = y - ((y - (this.height - 1)) * 2);
            y++;
        }
        if (z > this.depth - 1) {
            z = z - ((z - (this.depth - 1)) * 2);
            z++;
        }

        if(y1 < 0){
            y1 *= -1;
        }

        if (x1 < 0) {
            x1 *= -1;
        }

        if (z1 < 0) {
            z1 *= -1;
        }

        x1 %= this.width * 2;
        y1 %= this.height * 2;
        z1 %= this.depth * 2;
        if (x1 > this.width - 1) {
            x1 = x1 - ((x1 - (this.width - 1)) * 2);
            x1++;
        }
        if (y1 > this.height - 1) {
            y1 = y1 - ((y1 - (this.height - 1)) * 2);
            y1++;
        }
        if (z1 > this.depth - 1) {
            z1 = z1 - ((z1 - (this.depth - 1)) * 2);
            z1++;
        }

        double noise = this.noise[this.getNoiseIndex(x,y,z)] + (rawY * (this.yScale + yScale) + continentalScale);
        double highNoise = this.noise[this.getNoiseIndex(x1,y1,z1)] + (rawY * (this.yScale + yScale) + continentalScale);

        return  (noise + highNoise) / 2;
    }

    public int getNoiseIndex(int x, int y, int z){
        return x + (y * (this.width * this.depth)) + (z * this.depth);
    }
}
