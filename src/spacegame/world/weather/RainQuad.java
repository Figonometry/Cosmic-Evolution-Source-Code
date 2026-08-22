package spacegame.world.weather;

import spacegame.block.Block;
import spacegame.block.BlockIDList;
import spacegame.block.BlockWater;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.util.MathUtil;
import spacegame.world.World;
import spacegame.world.blockstate.CampfireState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.PitKilnState;
import spacegame.world.blockstate.TorchState;

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

        if(blockID == Block.air.ID)return;

        this.extinguishFireBlocks(blockID,x,y,z);

        if(Block.list[blockID].isSolid || Block.list[blockID] instanceof BlockWater){
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
        if(blockID != Block.torch.ID && blockID != Block.campfire.ID)return;

        World world = CosmicEvolution.instance.save.activeWorld;
        world.setBlockWithNotify(x,y,z, Block.air.ID, false);
        switch (blockID){
            case BlockIDList.TORCH -> { //Torch
                TorchState torchState = (TorchState) world.getBlockState(x,y,z, MultiState.TORCH_STATE);
                if(torchState == null)break;
                torchState.isLit = false;
                world.notifyChunk(x,y,z);
            }
            case BlockIDList.CAMPFIRE -> { //Lit campfire
                CampfireState campfireState = (CampfireState) world.getBlockState(x,y,z, MultiState.CAMPFIRE_STATE);
                if(campfireState == null)break;
                campfireState.isLit = false;
                world.notifyChunk(x,y,z);
            }
            case BlockIDList.PIT_KILN -> { //Pit Kiln Lit
                PitKilnState pitKilnState = (PitKilnState)world.getBlockState(x,y,z, MultiState.PIT_KILN_STATE);
                if(pitKilnState == null)break;
                pitKilnState.isLit = false;
                world.removeTimeEvent(x,y,z);
                world.notifyChunk(x,y,z);
            }
        }

        CosmicEvolution.instance.soundPlayer.playSound(x,y,z, new Sound(Sound.extinguish, false, 1), 1f);
    }


}
