package spacegame.render.blocktextureFX;

import spacegame.core.CosmicEvolution;
import spacegame.render.texturelists.BlockTextureList;

public final class WaterTextureFX extends BlockTextureFX {
    private float[] current = new float[1024];
    private float[] next = new float[1024];
    private float[] velocity = new float[1024];
    private float[] impulse = new float[1024];
    private int[] red = new int[1024];
    private int[] green = new int[1024];
    private int[] blue = new int[1024];
    private int[] alpha = new int[1024];
    private int[] sideRed = new int[1024];
    private int[] sideGreen = new int[1024];
    private int[] sideBlue = new int[1024];
    private int[] sideAlpha = new int[1024];

    @Override
    public void update() {

        // Simulation pass
        for (int x = 0; x < 32; x++) {
            for (int z = 0; z < 32; z++) {

                float sum = 0;

                for (int sampleX = x - 2; sampleX <= x + 2; sampleX++) {

                    for(int sampleZ = z - 2; sampleZ <= z + 2; sampleZ++){
                        int wrappedX = sampleX & 31;
                        int wrappedZ = sampleZ & 31;

                        sum += this.current[wrappedX + wrappedZ * 32];
                    }
                }

                this.next[x + z * 32] =
                        sum / 27f
                                + this.velocity[x + z * 32] * 0.8f;
            }
        }

        // Velocity pass
        for (int i = 0; i < 1024; i++) {

            this.velocity[i] += this.impulse[i] * 0.05f;


            if (this.velocity[i] < 0f) {
                this.velocity[i] = 0f;
            }

            this.impulse[i] -= 0.1f;

            if (CosmicEvolution.globalRand.nextFloat() < 0.05f) {
                this.impulse[i] = 0.5f;
            }
        }

        // Swap buffers
        float[] temp = this.next;
        this.next = this.current;
        this.current = temp;

        // Generate colors
        // Generate colors
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {

                int srcIndex = x + y * 32;

                float wave = this.current[srcIndex];

                if (wave < 0f) wave = 0f;
                if (wave > 1f) wave = 1f;

                float intensity = wave * wave;

                int r = (int)(25 + intensity * 25);
                int g = (int)(80 + intensity * 60);
                int b = (int)(60 + intensity * 75);
                int a = (int)(225);

                // Top texture
                this.red[srcIndex] = r;
                this.green[srcIndex] = g;
                this.blue[srcIndex] = b;
                this.alpha[srcIndex] = a;

                // Rotated side texture
                int rotatedIndex = rotateIndex90(x, y);

                this.sideRed[rotatedIndex] = r;
                this.sideGreen[rotatedIndex] = g;
                this.sideBlue[rotatedIndex] = b;
                this.sideAlpha[rotatedIndex] = a;
            }
        }

        this.updateTexture(BlockTextureList.WATER_TOP_TEXTURE, this.red, this.green, this.blue, this.alpha);
        this.updateTexture(BlockTextureList.WATER_SIDE_TEXTURE, this.sideRed, this.sideGreen, this.sideBlue, this.sideAlpha);
        this.updateTexture(BlockTextureList.WATER_SIDE_TEXTURE_2, this.sideRed, this.sideGreen, this.sideBlue, this.sideAlpha);
        this.updateTexture(BlockTextureList.WATER_BOTTOM_TEXTURE, this.red, this.green, this.blue, this.alpha);
        this.updateTexture(BlockTextureList.WATER_NORTH_FLOW_TEXTURE, this.red, this.green, this.blue, this.alpha);
        this.updateTexture(BlockTextureList.WATER_SOUTH_FLOW_TEXTURE, this.red, this.green, this.blue, this.alpha);
        this.updateTexture(BlockTextureList.WATER_EAST_FLOW_TEXTURE, this.sideRed, this.sideGreen, this.sideBlue, this.sideAlpha);
        this.updateTexture(BlockTextureList.WATER_WEST_FLOW_TEXTURE, this.sideRed, this.sideGreen, this.sideBlue, this.sideAlpha);
    }

    private int rotateIndex90(int x, int y) {
        int dstX = 31 - y;
        int dstY = x;

        return dstX + dstY * 32;
    }
}
