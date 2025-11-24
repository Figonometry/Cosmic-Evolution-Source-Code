package spacegame.world.weather;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.util.MathUtil;

public final class RainQuad {
    public double x;
    public double y;
    public double z;
    public boolean remove;

    public RainQuad(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void tick(){
        this.y -= 0.01666666666666666666666666666667 * 8;

        short blockID = CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(this.x), MathUtil.floorDouble(this.y), MathUtil.floorDouble(this.z));

        if(blockID == Block.air.ID)return;

        if(Block.list[blockID].standardCollisionBoundingBox.equals(Block.standardBlock)){
            if(this.y <= MathUtil.floorDouble(this.y) + 0.5){
                this.remove = true;
            }
        }

        int distanceX = Math.abs(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) - MathUtil.floorDouble(this.x));
        int distanceZ = Math.abs(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) - MathUtil.floorDouble(this.z));

        if(distanceX > 8 || distanceZ > 8){
            this.remove = true;
        }

    }


}
