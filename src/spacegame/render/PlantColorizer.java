package spacegame.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class PlantColorizer {
    private static final int[] grassColor = new int[65536];
    private static final int[] oakTreeColor = new int[2048]; //These should be smaller in order to prevent too much memory being allocated to various plants, grassColor being 2^16 is fine

    public static int getGrassColor(double temperature, double rainfall){
        int temp = (int) (temperature * 255f);
        int rain = (int) (rainfall * 255f);
        temp = Math.max(temp, 0);
        temp = Math.min(temp, 255);
        rain = Math.max(rain, 0);
        rain = Math.min(rain, 255);
        return grassColor[rain << 8 | temp];
    }

    public static int getOakLeafColor(double temperature, double rainfall){
        int temp = (int) (temperature * 127f);
        int rain = (int) (rainfall * 15f);
        temp = Math.max(temp, 0);
        temp = Math.min(temp, 127);
        rain = Math.max(rain, 0);
        rain = Math.min(rain, 15);
        return oakTreeColor[rain << 7 | temp]; //Bitshift the vertical component by the width of the horizontal component in powers of 2
    }

    static {
        try {
            BufferedImage image = ImageIO.read(new File("src/spacegame/assets/textures/misc/grassColor.png"));
            image.getRGB(0, 0, 256, 256, grassColor, 0, 256);
            for(int i = 0; i < grassColor.length; i++){
                grassColor[i] = grassColor[i] & 0x00FFFFFF; //Strips the alpha channel off the color values
            }
            BufferedImage image2 = ImageIO.read(new File("src/spacegame/assets/textures/misc/oakColor.png"));
            image2.getRGB(0, 0, 128, 16, oakTreeColor, 0, 128);
            for(int i = 0; i < oakTreeColor.length; i++){
                oakTreeColor[i] = oakTreeColor[i] & 0x00FFFFFF; //Strips the alpha channel off the color values
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
