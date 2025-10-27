package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.world.World;

public final class BlockReedGrowing extends Block implements ITimeUpdate {
    public BlockReedGrowing(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {

        switch (this.ID){
            case 113 -> {
                world.setBlockWithNotify(x,y,z, Block.reedGrowth1.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 114 -> {
                world.setBlockWithNotify(x,y,z, Block.reedGrowth2.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 115 -> {
                world.setBlockWithNotify(x,y,z, Block.reedGrowth3.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 116 -> {
                world.setBlockWithNotify(x,y,z, Block.reedGrowth4.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 117 -> {
                world.setBlockWithNotify(x,y,z, Block.reedGrowth5.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 118 -> {
                world.setBlockWithNotify(x,y,z, Block.reedLower.ID);
                world.setBlockWithNotify(x,y + 1,z, Block.reedUpper.ID);
                world.removeTimeEvent(x,y,z);
            }
        }
    }

    public float getReedGrowthScale(){
        return switch (this.ID) {
            case 114 -> 0.16f; //growth 1
            case 115 -> 0.32f; //growth 2
            case 116 -> 0.46f; //growth 3
            case 117 -> 0.64f; //growth 4
            case 118 -> 0.8f; //growth 5
            default -> 1;
        };
    }

    public float getReedTranslation(){
        return switch (this.ID){
            case 114 -> 0.42f; //growth 1
            case 115 -> 0.34f; //growth 2
            case 116 -> 0.26f; //growth 3
            case 117 -> 0.18f; //growth 4
            case 118 -> 0.1f; //growth 5
            default -> 1;
        };
    }

    @Override
    public long getUpdateTime() {
        return Timer.DAY;
    }

    @Override
    public String getDisplayStringText() {
        return this.ID == Block.reedSeed.ID ? "Will Sprout In: " : "Next Growth Stage: ";
    }
}
