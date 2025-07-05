package spacegame.world;

import java.util.Arrays;

public final class ChunkColumnSkylightMap {
    public final int x;
    public final int z;
    public int[] lightMap = new int[1024];

    public ChunkColumnSkylightMap(int x, int z) {
        this.x = x;
        this.z = z;
        Arrays.fill(this.lightMap, -1024);
    }

    public int getBlockXFromIndex(int index) {
        return ((index % 32) + (this.x << 5));
    }

    public int getBlockZFromIndex(int index) {
        return ((index % 1024) >> 5) + (this.z << 5);
    }


    public void updateLightMap(int x, int y, int z) {
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        this.lightMap[(x % 32) + ((z % 32) << 5)] = y;
    }

    public boolean isHeight(int x, int y, int z) {
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        return this.lightMap[x % 32 + ((z % 32) << 5)] == y;
    }

    public boolean isHeightGreater(int x, int y, int z) {
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        return this.lightMap[x % 32 + ((z % 32) << 5)] < y;
    }

    public boolean isHeightGreaterOrEqual(int x, int y, int z) {
        if (x < 0) {
            x %= 32;
            x += 32;
        }
        if (z < 0) {
            z %= 32;
            z += 32;
        }
        return this.lightMap[x % 32 + ((z % 32) << 5)] <= y;
    }
}
