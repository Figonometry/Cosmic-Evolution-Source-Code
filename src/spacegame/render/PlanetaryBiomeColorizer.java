package spacegame.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class PlanetaryBiomeColorizer {
    public static final int[] EARTH_LIKE_PLANET_BIOME_COLOR_BUFFER = new int[65536];


    public static int getEarthLikePlanetBiomeColor(double temperature, double rainfall){
        int temp = (int) (temperature * 255f);
        int rain = (int) (rainfall * 255f);
        temp = Math.max(temp, 0);
        temp = Math.min(temp, 255);
        rain = Math.max(rain, 0);
        rain = Math.min(rain, 255);
        return EARTH_LIKE_PLANET_BIOME_COLOR_BUFFER[rain << 8 | temp];
    }

    static {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/spacegame/assets/textures/misc/earthLikeBiomeColor.png"));
            image.getRGB(0, 0, 256, 256, EARTH_LIKE_PLANET_BIOME_COLOR_BUFFER, 0, 256);
            for(int i = 0; i < EARTH_LIKE_PLANET_BIOME_COLOR_BUFFER.length; i++){
                EARTH_LIKE_PLANET_BIOME_COLOR_BUFFER[i] = EARTH_LIKE_PLANET_BIOME_COLOR_BUFFER[i] & 0x00FFFFFF; //Strips the alpha channel off the color values
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
