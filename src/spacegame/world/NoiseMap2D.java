package spacegame.world;

import org.joml.SimplexNoise;

import java.util.Random;

public final class NoiseMap2D {
    public int width;
    public int height;
    public float[][] elevation;
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
        this.elevation = new float[width][height];

        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                float noise = 0;

                int tx = x + width;
                int tz = z + width;

                double nx = (double) tx / width - 0.5;
                double nz = (double) tz / height - 0.5;

                for (int i = 0; i < this.octaves; i++) {
                    noise += (1f / Math.pow(2, i)) *
                            tileableSimplex(
                                    (float)(Math.pow(2, i) * nx + offset[i]),
                                    (float)(Math.pow(2, i) * nz + offset[i]),
                                    1.0f, 1.0f
                            );

                }

                noise = (float) Math.pow(noise, exponent);
                noise *= scalingFactor;
                noise += floor;

                this.elevation[x][z] = noise;
            }
        }

        float[][] out = new float[width][height];

        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                double c = this.elevation[x][z];
                double l = this.elevation[(x + width - 1) % width][z];
                double r = this.elevation[(x + 1) % width][z];
                double u = this.elevation[x][(z + height - 1) % height];
                double d = this.elevation[x][(z + 1) % height];

                out[x][z] = (float) ((c + l + r + u + d) / 5.0);
            }
        }

        this.elevation = out;

    }


    public NoiseMap2D(int width, int height, float[][] noise){
        this.width = width;
        this.height = height;
        this.elevation = noise;
    }


    public void scaleByExponent(double exponent){
        for(int i = 0; i < this.elevation.length; i++){
            for(int j = 0; j < this.elevation[i].length; j++){
                this.elevation[i][j] = (float) Math.pow(this.elevation[i][j], exponent);
            }
        }
    }

    float tileableSimplex(float x, float y, float w, float h) {
        float nx = x % w;
        float ny = y % h;

        if (nx < 0) nx += w;
        if (ny < 0) ny += h;

        float fx = nx / w;
        float fy = ny / h;

        float a = SimplexNoise.noise(nx, ny);
        float b = SimplexNoise.noise(nx + w, ny);
        float c = SimplexNoise.noise(nx, ny + h);
        float d = SimplexNoise.noise(nx + w, ny + h);

        float i1 = a + fx * (b - a);
        float i2 = c + fx * (d - c);

        return i1 + fy * (i2 - i1);
    }





    public int getNoiseIntCasted(double x, double z) {
        double value = getNoiseRaw(x, z);
        return (int)Math.round(value);
    }




    public double getNoiseRaw(double x, double z) {
        // Wrap coordinates into the map range (continuous wrap)
        double gx = x % this.width;
        double gz = z % this.height;

        if (gx < 0) gx += this.width;
        if (gz < 0) gz += this.height;

        // Integer pixel positions
        int x0 = (int)Math.floor(gx);
        int z0 = (int)Math.floor(gz);

        // Next pixel (wrap around edges)
        int x1 = (x0 + 1) % this.width;
        int z1 = (z0 + 1) % this.height;

        // Fractional part for interpolation
        double tx = gx - x0;
        double tz = gz - z0;

        // Sample the four surrounding pixels
        double n00 = this.elevation[x0][z0];
        double n10 = this.elevation[x1][z0];
        double n01 = this.elevation[x0][z1];
        double n11 = this.elevation[x1][z1];

        // Interpolate horizontally
        double nx0 = n00 * (1 - tx) + n10 * tx;
        double nx1 = n01 * (1 - tx) + n11 * tx;

        // Interpolate vertically
        return nx0 * (1 - tz) + nx1 * tz;
    }

    private int mirror(int i, int max) {
        int period = max * 2;
        int m = i % period;
        if (m < 0) m += period;
        return m < max ? m : period - m - 1;
    }



}
