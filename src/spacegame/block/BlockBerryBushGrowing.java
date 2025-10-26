package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.world.World;

public final class BlockBerryBushGrowing extends Block implements ITimeUpdate {
    public BlockBerryBushGrowing(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {

        switch (this.ID){
            case 107 ->{ //berry seed
                world.setBlockWithNotify(x,y,z, Block.berryBushGrowth1.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 108 -> { //berry growth 1
                world.setBlockWithNotify(x,y,z, Block.berryBushGrowth2.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 109 -> {//berry growth 2
                world.setBlockWithNotify(x,y,z, Block.berryBushGrowth3.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 110 -> {//berry growth 3
                world.setBlockWithNotify(x,y,z, Block.berryBushGrowth4.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 111 -> {//berry growth 4
                world.setBlockWithNotify(x,y,z, Block.berryBushGrowth5.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            }
            case 112 -> {
                world.setBlockWithNotify(x,y,z, Block.berryBushNoBerries.ID);
                world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + ((ITimeUpdate)Block.berryBushNoBerries).getUpdateTime());
            }
        }
    }

    public float getBerryBushScale(){
        return switch (this.ID) {
            case 108 -> 0.16f; //growth 1
            case 109 -> 0.32f; //growth 2
            case 110 -> 0.46f; //growth 3
            case 111 -> 0.64f; //growth 4
            case 112 -> 0.8f; //growth 5
            default -> 1;
        };
    }

    public float getBerryBushTranslation(){
        return switch (this.ID){
            case 108 -> 0.42f; //growth 1
            case 109 -> 0.34f; //growth 2
            case 110 -> 0.26f; //growth 3
            case 111 -> 0.18f; //growth 4
            case 112 -> 0.1f; //growth 5
            default -> 1;
        };
    }

    @Override
    public long getUpdateTime() {
        return Timer.DAY;
    }

    @Override
    public String getDisplayStringText() {
        return this.ID == Block.berrySeed.ID ?  "Will Sprout In: "  : "Next Growth Stage: ";
    }
}
