package spacegame.render.blocktextureFX;

import spacegame.render.texturelists.BlockTextureList;

import java.util.Arrays;

public class FireTextureFX extends BlockTextureFX {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 40;
    private static final int VISIBLE_HEIGHT = 32;

    private int[] red = new int[1024];
    private int[] green = new int[1024];
    private int[] blue = new int[1024];
    private int[] alpha = new int[1024];

    // 32 × 40 simulation
    private float[] currentFire = new float[WIDTH * HEIGHT];
    private float[] nextFire = new float[WIDTH * HEIGHT];

    @Override
    public void update() {

        Arrays.fill(this.red, 0);
        Arrays.fill(this.green, 0);
        Arrays.fill(this.blue, 0);
        Arrays.fill(this.alpha, 0);

        // Fire simulation
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                int sampleCount = 18;

                float value = 0.0f;

                // Pull energy from the row below
                if (y < HEIGHT - 1) {
                    value =
                            currentFire[x + (y + 1) * WIDTH]
                                    * sampleCount;
                }

                // Sample neighbors
                for (int sampleX = x - 1; sampleX <= x + 1; sampleX++) {
                    for (int sampleY = y - 1; sampleY <= y + 1; sampleY++) {

                        if (sampleX >= 0 &&
                                sampleX < WIDTH &&
                                sampleY >= 0 &&
                                sampleY < HEIGHT) {

                            value += currentFire[
                                    sampleX + sampleY * WIDTH
                                    ];
                        }

                        sampleCount++;
                    }
                }

                nextFire[x + y * WIDTH] =
                        value / (sampleCount * 1.06f);

                // Bottom 8 rows act as fuel source
                if (y >= HEIGHT - 8) {

                    nextFire[x + y * WIDTH] =
                            (float)(
                                    Math.random()
                                            * Math.random()
                                            * Math.random()
                                            * 4.0
                                            + Math.random() * 0.2
                                            + 0.2
                            );
                }
            }
        }

        // Swap buffers
        float[] temp = currentFire;
        currentFire = nextFire;
        nextFire = temp;

        // Render visible 32x32 section
        int startY = 1;

        for (int y = 0; y < VISIBLE_HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                int simIndex =
                        x + (y + startY) * WIDTH;

                float intensity =
                        currentFire[simIndex] * 1.8f;

                intensity = Math.max(
                        0.0f,
                        Math.min(1.0f, intensity)
                );


                int red =
                        (int)(intensity * 155.0f + 100.0f);

                int green =
                        (int)(intensity * intensity * intensity * 255.0f);

                int blue =
                        (int)(
                                Math.pow(intensity * intensity, 10.0)
                                        * 255.0f
                        );

                int alpha =
                        (int)(intensity * 255.0f);

                int textureIndex =
                        x + y * WIDTH;

                this.red[textureIndex] = red;
                this.green[textureIndex] = green;
                this.blue[textureIndex] = blue;
                this.alpha[textureIndex] = alpha;
            }
        }

        this.updateTexture(BlockTextureList.FIRE_TEXTURE, this.red, this.green, this.blue, this.alpha);
    }
}