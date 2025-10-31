package spacegame.world;

import org.joml.SimplexNoise;

import java.util.Random;

public final class NoiseMap2D {
    public int width;
    public int height;
    public double[][] elevation;
    public int octaves;

    public NoiseMap2D(int width, int height, int octaves, double scalingFactor, double exponent, double floor, long seed) {
        Random rand = new Random(seed);
        double[] offset = new double[octaves];
        for (int i = 0; i < offset.length; i++) {
            offset[i] = rand.nextInt(1000) + rand.nextDouble();
        }
        this.width = width;
        this.height = height;
        this.octaves = octaves;
        this.elevation = new double[width][height];

        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                float noise = 0;

                int tx = x + width;
                int tz = z + width;

                double nx = (double) tx / width - 0.5;
                double nz = (double) tz / height - 0.5;

                for (int i = 0; i < this.octaves; i++) {
                    noise += ((float) 1 / Math.pow(2, i)) *
                            SimplexNoise.noise((float)(Math.pow(2, i) * nx + offset[i]),
                                    (float)(Math.pow(2, i) * nz + offset[i]));
                }

                noise = (float) Math.pow(noise, exponent);
                noise *= scalingFactor;
                noise += floor;

                this.elevation[x][z] = noise;
            }
        }

    }

    public NoiseMap2D(int width, int height, double[][] noise){
        this.width = width;
        this.height = height;
        this.elevation = noise;
    }



    public int getNoise(int x, int z) {
        if (x < 0) {
            x *= -1;
        }
        if (z < 0) {
            z *= -1;
        }

        x %= this.width * 2;
        z %= this.height * 2;
        if (x > this.width - 1) {
            x = x - ((x - (this.width - 1)) * 2);
            x++;
        }
        if (z > this.width - 1) {
            z = z - ((z - (this.width - 1)) * 2);
            z++;
        }

        return (int) this.elevation[x][z];
    }

    public double getNoiseRaw(int x, int z) {
        if (x < 0) {
            x *= -1;
        }
        if (z < 0) {
            z *= -1;
        }

        x %= this.width * 2;
        z %= this.height * 2;
        if (x > this.width - 1) {
            x = x - ((x - (this.width - 1)) * 2);
            x++;
        }
        if (z > this.width - 1) {
            z = z - ((z - (this.width - 1)) * 2);
            z++;
        }

        return this.elevation[x][z];
    }

}
