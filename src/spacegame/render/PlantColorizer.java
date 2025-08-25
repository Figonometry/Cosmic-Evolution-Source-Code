package spacegame.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class PlantColorizer {
    private static final int[] grassColor = new int[65536];

    public static int getGrassColor(double temperature, double rainfall){
        int temp = (int) (temperature * 255);
        int rain = (int) (rainfall * 255);
        int index = 0;
        return grassColor[index];
    }

    static {
        try {
            BufferedImage image = ImageIO.read(new File("src/spacegame/assets/textures/misc/grassColor.png"));
            image.getRGB(0, 0, 256, 256, grassColor, 0, 256);
            for(int i = 0; i < grassColor.length; i++){
                grassColor[i] = grassColor[i] & 0x00FFFFFF; //Strips the alpha channel off the color values
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
