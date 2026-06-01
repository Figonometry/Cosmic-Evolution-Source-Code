package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Timer;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.RenderBlocks;
import spacegame.world.World;
import spacegame.world.blockstate.Crop;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.TilledSoilState;

public final class BlockSoil extends Block implements ITimeUpdate, ITickable  {
    public BlockSoil(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    public int getBlockTexture(int x, int y, int z, int face){
        if(face != RenderBlocks.TOP_FACE)return this.textureID;

        TilledSoilState tilledSoilState = CosmicEvolution.instance.save.activeWorld.getTilledSoilState(x,y,z);

        if(tilledSoilState == null)return this.textureID;

        switch (tilledSoilState.fertilizerID){
            case TilledSoilState.BONEMEAL -> {
                return 77;
            }
            default -> {
                return 70;
            }
        }
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short heldItem = player.getHeldItem();

        if(heldItem != Item.NULL_ITEM_REFERENCE && MouseListener.rightClickReleased){
            if(heldItem == Item.boneMeal.ID){
                TilledSoilState tilledSoilState = world.getTilledSoilState(x,y,z);
                if(tilledSoilState == null)return;

                tilledSoilState.fertilizerID = TilledSoilState.BONEMEAL;
                player.removeItemFromInventory();
                world.notifyChunk(x,y,z);
                MouseListener.rightClickReleased = false;
            }
        }
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        TilledSoilState tilledSoilState = world.getTilledSoilState(x,y,z);
        if(tilledSoilState == null){
            world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime());
            return;
        }

        boolean cropAbove = world.getBlockID(x,y + 1, z) == Block.cropGrowth.ID;

        if(cropAbove){
            CropState cropState = world.getCropState(x, y + 1, z);
            if(cropState == null)return;
            Crop crop = Crop.getCropFromName(cropState.name);
            if(crop == null)return;

            tilledSoilState.nitrogenPercent -= 0.01f;
            tilledSoilState.phosphorusPercent -= 0.01f;
            tilledSoilState.potassiumPercent -= 0.01f;
        } else {
            tilledSoilState.nitrogenPercent += 0.01f;
            tilledSoilState.phosphorusPercent += 0.01f;
            tilledSoilState.potassiumPercent += 0.01f;

            if(tilledSoilState.nitrogenPercent > 1f)tilledSoilState.nitrogenPercent = 1f;
            if(tilledSoilState.phosphorusPercent > 1f)tilledSoilState.phosphorusPercent = 1f;
            if(tilledSoilState.potassiumPercent > 1f)tilledSoilState.potassiumPercent = 1f;
        }

        world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime());
    }

    @Override
    public long getUpdateTime() {
        return Timer.GAME_DAY;
    }

    @Override
    public String getDisplayStringText() {
        return null;
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        TilledSoilState tilledSoilState = CosmicEvolution.instance.save.activeWorld.getTilledSoilState(x,y,z);
        if(tilledSoilState == null)return "null";

        return "K: " + tilledSoilState.potassiumPercent * 100 + "%   N: " + tilledSoilState.nitrogenPercent * 100 + "%   P: " +
                tilledSoilState.phosphorusPercent * 100 + "%   Moisture: " + tilledSoilState.moisturePercent * 100 + "%";
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if((world.ce.save.time & 59) != 0)return;


        boolean isWatered = world.raining || world.getBlockID(x - 1, y, z) == Block.water.ID || world.getBlockID(x + 1, y, z) == Block.water.ID ||
                world.getBlockID(x, y, z - 1) == Block.water.ID || world.getBlockID(x, y, z + 1) == Block.water.ID;

        TilledSoilState tilledSoilState = world.getTilledSoilState(x,y,z);
        if(tilledSoilState == null)return;
        if(isWatered){

            tilledSoilState.moisturePercent += 0.01f;
        } else {

            tilledSoilState.moisturePercent -= 0.01f;
        }
        if(tilledSoilState.moisturePercent > 1f)tilledSoilState.moisturePercent = 1f;
        if(tilledSoilState.moisturePercent < 0f)tilledSoilState.moisturePercent = 0f;
    }
}
