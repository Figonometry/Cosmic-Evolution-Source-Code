package spacegame.render;

import org.joml.SimplexNoise;
import org.joml.Vector3f;
import spacegame.celestial.CelestialObject;
import spacegame.core.CosmicEvolution;
import spacegame.util.MathUtil;
import spacegame.world.NoiseMap2D;
import spacegame.world.World;
import spacegame.world.WorldEarth;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public final class ThreadGenerateCelestialBodyTextures implements Runnable {
    public long seed;
    public CelestialObject celestialObject;
    public boolean forWorldInit;
    public ThreadGenerateCelestialBodyTextures(CelestialObject celestialObject, boolean forWorldInit) {
        this.seed = celestialObject.seed;
        this.celestialObject = celestialObject;
        this.forWorldInit = forWorldInit;
    }


    @Override
    public void run() {
        if(this.forWorldInit) {
            this.createMercatorImage(this.seed, 1);
        } else {
            this.loadExistingImagesIntoNoiseMaps(CosmicEvolution.instance.save.activeWorld);
        }

        synchronized (CosmicEvolution.instance.renderEngine.loadableTexturesForCelestialObjects) {
            CosmicEvolution.instance.renderEngine.loadableTexturesForCelestialObjects.add(new LoadableTexture(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/cubeMap/", RenderEngine.TEXTURE_TYPE_CUBEMAP, 0, CosmicEvolution.instance.everything.earth));
        }

    }

    public void createMercatorImage(long seed, double scale) {
        int width = 8192;
        int height = 4096;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[width * height];

        Random rand = new Random(seed);
        double offsetX = rand.nextDouble() * 1000;
        double offsetY = rand.nextDouble() * 1000;
        double offsetZ = rand.nextDouble() * 1000;


        double[][] elevation = new double[width][height];
        int octaves = 16;

        // Step 1: Generate elevation using spherical 3D noise
        for (int y = 0; y < height; y++) {
            double lat = Math.PI * ((double) y / height - 0.5); // -π/2 to π/2

            for (int x = 0; x < width; x++) {
                double lon = 2 * Math.PI * ((double) x / width); // 0 to 2π

                double nx = Math.cos(lat) * Math.cos(lon);
                double ny = Math.sin(lat);
                double nz = Math.cos(lat) * Math.sin(lon);

                double noise = 0;
                double amplitude = 1;
                double frequency = 1;

                for (int i = 0; i < octaves; i++) {
                    noise += amplitude * SimplexNoise.noise(
                            (float)(nx * scale * frequency + offsetX),
                            (float)(ny * scale * frequency + offsetY),
                            (float)(nz * scale * frequency + offsetZ)
                    );
                    amplitude *= 0.5;
                    frequency *= 2;
                }


                double elevationVal = (noise + 1) / 2.0; // normalize to [0, 1]
                elevation[x][y] = Math.pow(elevationVal, 1.5); // shape terrain
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = elevation[x][y];
                double normalized = Math.max(0.0, Math.min(1.0, val));
                elevation[x][y] = normalized;
            }
        }

        double[][] rainfall = this.generateMercatorImageNoiseMap(this.seed + 12, 1);
        double[][] temperature = this.generateMercatorImageNoiseMap(this.seed - 12, 1);

        for (int y = 0; y < height; y++) {
            double lat = Math.PI * ((double) y / height - 0.5); // -π/2 to π/2
            double latitudeFactor = MathUtil.cos(lat); // 1.0 at equator, 0.0 at poles

            for (int x = 0; x < width; x++) {
                double noise = temperature[x][y]; // existing noise-based temperature
                double val = latitudeFactor * 0.7 + noise * 0.3; // blend latitude + noise
                temperature[x][y] = Math.max(0.0, Math.min(1.0, val)); // clamp to [0, 1]
            }
        }

        //Write the 3 mercator images to the world folder to be buffered in
        BufferedImage rawElevation = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rawTemperature = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rawRainfall = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] elevationPixels = new int[width * height];
        int[] temperaturePixels = new int[width * height];
        int[] rainfallPixels = new int[width * height];


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = elevation[x][y];
                double normalized = Math.max(0.0, Math.min(1.0, val));
                int argb;
                int colorComponent = MathUtil.floatToIntRGBA((float) normalized);

                argb = ((255 << 24) | (colorComponent << 16) | (colorComponent << 8) | colorComponent);
                elevationPixels[y * width + x] = argb;

                colorComponent = MathUtil.floatToIntRGBA((float) temperature[x][y]);
                argb = ((255 << 24) | (colorComponent << 16) | (colorComponent << 8) | colorComponent);
                temperaturePixels[y * width + x] = argb;

                colorComponent = MathUtil.floatToIntRGBA((float) rainfall[x][y]);
                argb = ((255 << 24) | (colorComponent << 16) | (colorComponent << 8) | colorComponent);
                rainfallPixels[y * width + x] = argb;


                //Yes this seems dumb but this also occurs during the loading of the map so this will ensure that both the initially generated map and the loaded mamp are identical
                val = MathUtil.intToFloatRGBA(elevationPixels[y * width + x] & 255);
                elevation[x][y] = val;

                val = MathUtil.intToFloatRGBA(temperaturePixels[y * width + x] & 255);
                temperature[x][y] = val;

                val = MathUtil.intToFloatRGBA(rainfallPixels[y * width + x] & 255);
                rainfall[x][y] = val;
            }
        }

        rawElevation.setRGB(0, 0, width, height, elevationPixels, 0, width);
        rawTemperature.setRGB(0,0, width, height, temperaturePixels, 0, width);
        rawRainfall.setRGB(0,0,width, height, rainfallPixels, 0, width);

        try {
            File output = new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/rawElevation.png");
            ImageIO.write(rawElevation, "png", output);

            output = new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/rawTemperature.png");
            ImageIO.write(rawTemperature, "png", output);

            output = new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/rawRainfall.png");
            ImageIO.write(rawRainfall, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Step 3: Normalize and convert to grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = elevation[x][y];
                double normalized = Math.max(0.0, Math.min(1.0, val));
                int gray = MathUtil.floatToIntRGBA((float) normalized);
                int argb;
                int r = gray / 2;
                int g;
                int b;// boost green
                if (gray < 128) {
                    // Blue-shifted: reduce red and green, emphasize blue
                    g = gray / 2;
                    b = Math.min(255, gray + 64);
                } else {
                    int targetColor = PlanetaryBiomeColorizer.getEarthLikePlanetBiomeColor(temperature[x][y], rainfall[x][y]);
                    int biomeR = (targetColor >> 16) & 255;
                    int biomeG = (targetColor >> 8) & 255;
                    int biomeB = targetColor & 255;

                    r = gray;
                    g = gray;
                    b = gray;

                    float alpha = 0.6f;
                    r = (int)(r * (1 - alpha) + biomeR * alpha);
                    g = (int)(g * (1 - alpha) + biomeG * alpha);
                    b = (int)(b * (1 - alpha) + biomeB * alpha);
                }
                if(temperature[x][y] <= 0.25){
                    r = 255;
                    g = 255;
                    b = 255;
                }
                argb = (255 << 24) | (r << 16) | (g << 8) | b;

                pixels[y * width + x] = argb;
            }
        }

        // Step 4: Write pixels to image
        image.setRGB(0, 0, width, height, pixels, 0, width);



        //Set the 3 noisemaps here, set back to range -1, 1
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = elevation[x][y] * 2.0 - 1.0;
                elevation[x][y] = val;

                val = temperature[x][y] * 2.0 - 1.0;
                temperature[x][y] = val;


                val = rainfall[x][y] * 2.0 - 1.0;
                rainfall[x][y] = val;
            }
        }

        ((WorldEarth)(CosmicEvolution.instance.save.activeWorld)).globalElevationMap = new NoiseMap2D(width, height, elevation);
        ((WorldEarth)(CosmicEvolution.instance.save.activeWorld)).globalTemperatureMap = new NoiseMap2D(width, height, temperature);
        ((WorldEarth)(CosmicEvolution.instance.save.activeWorld)).globalRainfallMap = new NoiseMap2D(width, height, rainfall);



        // Step 5: Save image
        try {
            File output = new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/worldMercator.png");
            ImageIO.write(image, "png", output);
            this.splitIntoCubemap(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private double[][] generateMercatorImageNoiseMap(long seed, double scale){
        int width = 8192;
        int height = 4096;

        Random rand = new Random(seed);
        double offsetX = rand.nextDouble() * 1000;
        double offsetY = rand.nextDouble() * 1000;
        double offsetZ = rand.nextDouble() * 1000;


        double[][] elevation = new double[width][height];
        int octaves = 4;

        // Step 1: Generate elevation using spherical 3D noise
        for (int y = 0; y < height; y++) {
            double lat = Math.PI * ((double) y / height - 0.5); // -π/2 to π/2

            for (int x = 0; x < width; x++) {
                double lon = 2 * Math.PI * ((double) x / width); // 0 to 2π

                double nx = Math.cos(lat) * Math.cos(lon);
                double ny = Math.sin(lat);
                double nz = Math.cos(lat) * Math.sin(lon);

                double noise = 0;
                double amplitude = 1;
                double frequency = 1;

                for (int i = 0; i < octaves; i++) {
                    noise += amplitude * SimplexNoise.noise(
                            (float)(nx * scale * frequency + offsetX),
                            (float)(ny * scale * frequency + offsetY),
                            (float)(nz * scale * frequency + offsetZ)
                    );
                    amplitude *= 0.5;
                    frequency *= 2;
                }


                double elevationVal = (noise + 1) / 2.0; // normalize to [0, 1]
                elevation[x][y] = elevationVal;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = elevation[x][y];
                double normalized = Math.max(0.0, Math.min(1.0, val));
                elevation[x][y] = normalized;
            }
        }

        return elevation;
    }

    private void splitIntoCubemap(File imageFile) {
        try {
            BufferedImage mercator = ImageIO.read(imageFile);
            int faceSize = 2048;
            String[] faceNames = { "posX", "negX", "posY", "negY", "posZ", "negZ" };

            // Define face directions
            Vector3f[] faceDirs = {
                    new Vector3f(1, 0, 0),   // right
                    new Vector3f(-1, 0, 0),  // left
                    new Vector3f(0, 1, 0),   // top
                    new Vector3f(0, -1, 0),  // bottom
                    new Vector3f(0, 0, 1),   // front
                    new Vector3f(0, 0, -1)   // back
            };

            Vector3f[] faceUps = {
                    new Vector3f(0, -1, 0),  // right
                    new Vector3f(0, -1, 0),  // left
                    new Vector3f(0, 0, 1),   // top
                    new Vector3f(0, 0, -1),  // bottom
                    new Vector3f(0, -1, 0),  // front
                    new Vector3f(0, -1, 0)   // back
            };

            for (int f = 0; f < 6; f++) {
                BufferedImage face = new BufferedImage(faceSize, faceSize, BufferedImage.TYPE_INT_ARGB);
                Vector3f faceDir = faceDirs[f];
                Vector3f faceUp = faceUps[f];
                Vector3f faceRight = new Vector3f();
                faceDir.cross(faceUp, faceRight).normalize();

                for (int y = 0; y < faceSize; y++) {
                    for (int x = 0; x < faceSize; x++) {
                        float u = (2f * x) / (faceSize - 1) - 1f;
                        float v = (2f * y) / (faceSize - 1) - 1f;

                        Vector3f dir = new Vector3f(faceDir)
                                .add(new Vector3f(faceRight).mul(u))
                                .add(new Vector3f(faceUp).mul(v))
                                .normalize();

                        double lat = Math.asin(dir.y);
                        double lon = Math.atan2(dir.z, dir.x);

                        double fx = (lon + Math.PI) / (2 * Math.PI) * mercator.getWidth();
                        double fy = (1 - (lat + Math.PI / 2) / Math.PI) * mercator.getHeight();

                        int px = Math.min(mercator.getWidth() - 1, Math.max(0, (int) fx));
                        int py = Math.min(mercator.getHeight() - 1, Math.max(0, (int) fy));

                        int color = mercator.getRGB(px, py);
                        face.setRGB(x, y, color);
                    }
                }

                File outFile = new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/cubeMap/" + faceNames[f] + ".png");
                outFile.mkdirs();
                ImageIO.write(face, "png", outFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        World.worldLoadPhase = 2;
    }


    private void loadExistingImagesIntoNoiseMaps(World world){
        WorldEarth earth = (WorldEarth)world;

        int width = 8192;
        int height = 4096;
        int mercatorSize = width * height;
        int[] elevationPixels = new int[mercatorSize];
        int[] temperaturePixels = new int[mercatorSize];
        int[] rainfallPixels = new int[mercatorSize];


        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/rawElevation.png"));
            image.getRGB(0, 0, width, height, elevationPixels, 0, width);

            image = ImageIO.read(new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/rawTemperature.png"));
            image.getRGB(0, 0, width, height, temperaturePixels, 0, width);

            image = ImageIO.read(new File(CosmicEvolution.instance.save.saveFolder + "/worlds/worldEarth/rawRainfall.png"));
            image.getRGB(0, 0, width, height, rainfallPixels, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double[][] elevation = new double[width][height];
        double[][] temperature = new double[width][height];
        double[][] rainfall = new double[width][height];


        //converts each pixel to a corresponding float val and pushes it into the range -1 to 1
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = MathUtil.intToFloatRGBA(elevationPixels[y * width + x] & 255);
                elevation[x][y] = val;

                val = MathUtil.intToFloatRGBA(temperaturePixels[y * width + x] & 255);
                temperature[x][y] = val;

                val = MathUtil.intToFloatRGBA(rainfallPixels[y * width + x] & 255);
                rainfall[x][y] = val;

                val = elevation[x][y] * 2.0 - 1.0;
                elevation[x][y] = val;

            }
        }

        earth.globalElevationMap = new NoiseMap2D(width, height, elevation);
        earth.globalTemperatureMap = new NoiseMap2D(width, height, temperature);
        earth.globalRainfallMap = new NoiseMap2D(width, height, rainfall);

        World.worldLoadPhase = 2;
    }
}
