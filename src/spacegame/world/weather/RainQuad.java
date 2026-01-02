package spacegame.world.weather;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.util.MathUtil;
import spacegame.world.World;

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

        int x = MathUtil.floorDouble(this.x);
        int y = MathUtil.floorDouble(this.y);
        int z = MathUtil.floorDouble(this.z);

        short blockID = CosmicEvolution.instance.save.activeWorld.getBlockID(x,y,z);

        this.extinguishFireBlocks(blockID,x,y,z);

        if(blockID == Block.air.ID)return;

        if(Block.list[blockID].isSolid || blockID == Block.water.ID){
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

    private void extinguishFireBlocks(short blockID, int x, int y, int z){
        if(blockID != Block.torchStandard.ID && blockID != Block.torchNorth.ID && blockID != Block.torchSouth.ID && blockID
                != Block.torchEast.ID && blockID != Block.torchWest.ID && blockID != Block.campfireLit.ID)return;

        World world = CosmicEvolution.instance.save.activeWorld;
        world.setBlockWithNotify(x,y,z, Block.air.ID, false);
        switch (blockID){
            case 2 -> { //Torch
                world.setBlockWithNotify(x,y,z, Block.torchStandardUnlit.ID, false);
            }
            case 3 -> { //Torch North
                world.setBlockWithNotify(x,y,z, Block.torchNorthUnlit.ID, false);
            }
            case 4 -> { //Torch South
                world.setBlockWithNotify(x,y,z, Block.torchSouthUnlit.ID, false);
            }
            case 5 -> { //Torch East
                world.setBlockWithNotify(x,y,z, Block.torchEastUnlit.ID, false);
            }
            case 6 -> { //Torch West
                world.setBlockWithNotify(x,y,z, Block.torchWestUnlit.ID, false);
            }
            case 68 -> { //Lit campfire
                world.removeHeatableBlock(x,y,z);
                world.setBlockWithNotify(x,y,z, Block.campFire4FireWood.ID, false);
            }
            case 99 -> { //Pit Kiln Lit
                world.setBlockWithNotify(x,y,z, Block.pitKilnUnlit.ID, false);
                world.removeTimeEvent(x,y,z);
            }
        }

        CosmicEvolution.instance.soundPlayer.playSound(x,y,z, new Sound(Sound.extinguish, false, 1), 1f);
    }


}
