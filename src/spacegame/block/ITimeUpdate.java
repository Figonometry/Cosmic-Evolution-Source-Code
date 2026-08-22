package spacegame.block;

import spacegame.world.World;

public interface ITimeUpdate {
    void onTimeUpdate(int x, int y, int z, World world);
    long getUpdateTime(int x, int y, int z, World world);
    String getDisplayStringText(int x, int y, int z, World world);
}
