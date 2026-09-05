package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Timer;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.Crop;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.TilledSoilState;

public final class BlockTilledSoil extends Block implements ITimeUpdate, ITickable  {
    public BlockTilledSoil(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    public int getBlockTexture(int x, int y, int face, int z){
        if(face != Block.FACE_UP)return this.textureID;

        TilledSoilState tilledSoilState = (TilledSoilState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.TILLED_SOIL_STATE);

        if(tilledSoilState == null)return this.textureID;

        switch (tilledSoilState.fertilizerID){
            case TilledSoilState.BONEMEAL -> {
                return BlockTextureList.FERTILIZER_TEXTURE;
            }
            default -> {
                return BlockTextureList.TILLED_SOIL_TEXTURE;
            }
        }
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short heldItem = player.getHeldItem();

        if(heldItem != Item.NULL_ITEM_REFERENCE && MouseListener.rightClickReleased){
            if(heldItem == Item.boneMeal.ID){
                TilledSoilState tilledSoilState = (TilledSoilState) world.getBlockState(x,y,z, MultiState.TILLED_SOIL_STATE);
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
        TilledSoilState tilledSoilState = (TilledSoilState) world.getBlockState(x,y,z, MultiState.TILLED_SOIL_STATE);
        if(tilledSoilState == null){
            world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
            return;
        }

        boolean cropAbove = world.getBlockID(x,y + 1, z) == Block.cropGrowth.ID;

        if(cropAbove){
            CropState cropState = (CropState) world.getBlockState(x, y + 1, z, MultiState.CROP_STATE);
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

            if(tilledSoilState.nitrogenPercent > tilledSoilState.maxNutrientLevel)tilledSoilState.nitrogenPercent = tilledSoilState.maxNutrientLevel;
            if(tilledSoilState.phosphorusPercent > tilledSoilState.maxNutrientLevel)tilledSoilState.phosphorusPercent = tilledSoilState.maxNutrientLevel;
            if(tilledSoilState.potassiumPercent > tilledSoilState.maxNutrientLevel)tilledSoilState.potassiumPercent = tilledSoilState.maxNutrientLevel;
        }

        world.addTimeEvent(x,y,z, world.ce.save.time + this.getUpdateTime(x,y,z, world));
    }

    @Override
    public long getUpdateTime(int x, int y, int z, World world) {
        return Timer.GAME_DAY;
    }

    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        return null;
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        TilledSoilState tilledSoilState = (TilledSoilState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.TILLED_SOIL_STATE);
        if(tilledSoilState == null)return "null";

        return "K: " + tilledSoilState.potassiumPercent * 100 + "%   N: " + tilledSoilState.nitrogenPercent * 100 + "%   P: " +
                tilledSoilState.phosphorusPercent * 100 + "%   Moisture: " + tilledSoilState.moisturePercent * 100 + "%";
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if((world.ce.save.time & 59) != 0)return;


        boolean isWatered = world.raining || world.getBlockID(x - 1, y, z) == Block.water.ID || world.getBlockID(x + 1, y, z) == Block.water.ID ||
                world.getBlockID(x, y, z - 1) == Block.water.ID || world.getBlockID(x, y, z + 1) == Block.water.ID;

        TilledSoilState tilledSoilState = (TilledSoilState) world.getBlockState(x,y,z,MultiState.TILLED_SOIL_STATE);
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
