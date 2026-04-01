package spacegame.util;

import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class GeneralUtil {

    public static void deleteDirectory(File directoryToBeDeleted){
        File[] allContents = directoryToBeDeleted.listFiles();
        File file;
        if(allContents != null){
            for(int i = 0; i < allContents.length; i++){
                file = allContents[i];
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }

    public static int[] getFilePixelsRGB(String filepath, int imageWidth, int imageHeight) {
        try {
            int[] pixels = new int[imageWidth * imageHeight];
            BufferedImage image = ImageIO.read(new File(filepath));
            image.getRGB(0,0, imageWidth, imageHeight, pixels, 0, imageWidth);
            for(int i = 0; i < pixels.length; i++){
                pixels[i] = pixels[i] & 0x00FFFFFF;
            }
            return pixels;
        } catch (IOException e){
            return null;
        }
    }

    public static int[] getFilePixelsARGB(String filepath, int imageWidth, int imageHeight){
        try {
            int[] pixels = new int[imageWidth * imageHeight];
            BufferedImage image = ImageIO.read(new File(filepath));
            image.getRGB(0,0, imageWidth, imageHeight, pixels, 0, imageWidth);
            return pixels;
        } catch (IOException e){
            return null;
        }
    }

    //Primarily for even numbered images
    public static int[] rotateImagePixels(int[] src, double deg, int width, int height) {
        int[] dst = new int[src.length];

        double rad = Math.toRadians(deg);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        int cx = width / 2;
        int cy = height / 2;

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {

                // Destination → centered
                double x = dx - cx;
                double y = dy - cy;

                // Apply inverse rotation
                double sx =  x * cos + y * sin;
                double sy = -x * sin + y * cos;

                // Back to source coordinates
                sx += cx;
                sy += cy;

                // Nearest neighbor sampling
                int ix = (int)Math.round(sx);
                int iy = (int)Math.round(sy);

                int dstIndex = dy * width + dx;

                if (ix >= 0 && ix < width && iy >= 0 && iy < height) {
                    dst[dstIndex] = src[iy * width + ix];
                } else {
                    dst[dstIndex] = 0; // transparent
                }
            }
        }

        return dst;
    }

}
