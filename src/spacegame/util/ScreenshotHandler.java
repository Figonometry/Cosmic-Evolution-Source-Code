package spacegame.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import spacegame.gui.GuiInGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class ScreenshotHandler {


    public static String takeScreenshot(File launcherDirectory, int width, int height){
        try {
            //Ensure that the screenshot directory exists
            File screenshotDirectory = new File(launcherDirectory, "screenshots");
            screenshotDirectory.mkdirs();

            //Allocate a byte buffer, a byte array, and an int array for image storage
            ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(width * height * 3);
            byte[] pixelData = new byte[width * height * 3];
            int[] imageData = new int[width * height];

            //Ensure proper pixel alignment and then read pixels from the framebuffer and return the data in pixelBuffer
            GL46.glPixelStorei(GL46.GL_PACK_ALIGNMENT, 1);
            GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);

            GL46.glReadPixels(0,0, width, height, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, pixelBuffer);

            //Construct the output file using system date/time
            Calendar calendar = new GregorianCalendar();
            String fileName = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY)  + calendar.get(Calendar.MINUTE)  + calendar.get(Calendar.SECOND) + ".png";
            File outputFile = new File(screenshotDirectory + "/" + fileName);

            //Pull the pixel data into a byte array from the byte buffer
            pixelBuffer.get(pixelData);

            //Copy each byte into the format ARGB in each int value
            for(int i = 0; i < width; i++) {
                for(int k = 0; k < height; k++) {
                    int rawPixel = i + (height - k - 1) * width;
                    int r = pixelData[rawPixel * 3 + 0] & 255;
                    int g = pixelData[rawPixel * 3 + 1] & 255;
                    int b = pixelData[rawPixel * 3 + 2] & 255;
                    int pixel = 0xFF000000 | r << 16 | g << 8 | b;
                    imageData[i + k * width] = pixel;
                }
            }

            //Construct the output image using the imageData array and write the output image to the file, return successful screenshot
            BufferedImage outputImage = new BufferedImage(width, height, 1);
            outputImage.setRGB(0,0, width, height, imageData, 0, width);
            ImageIO.write(outputImage, "png", outputFile);
            return "Saved screenshot";
        } catch (Exception e){
            e.printStackTrace();
            //If for whatever reason a screenshot fails return that it failed
            return "Failed to save screenshot: " + e;
        }

    }
}
