package spacegame.item;

import spacegame.block.Block;
import spacegame.block.BlockSoil;
import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.TilledSoilState;

public final class ItemHoe extends ItemTool {
    public ItemHoe(short ID, String modelFilePath, String filepath, Material material) {
        super(ID, modelFilePath, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }



    public void onRightClick(int x, int y, int z, World world, EntityPlayer player) {
        if(!MouseListener.rightClickReleased)return;
        player.rightClickAnimateTimer = 60;
        player.animateRightClick = true;
        MouseListener.rightClickReleased = false;
    }

    public void onFinishRightClickAnimation(int x, int y, int z, World world, EntityPlayer player){
        int[] coordinatesPlayerIsLookingAt = player.getPlayerLookingAtBlockCoords();
        x = coordinatesPlayerIsLookingAt[0];
        y = coordinatesPlayerIsLookingAt[1];
        z = coordinatesPlayerIsLookingAt[2];
        short blockID = world.getBlockID(x,y,z);

        if(blockID != Block.grass.ID && blockID != Block.dirt.ID)return;
        if(world.getBlockID(x, y + 1, z) != Block.air.ID)return;

        world.setBlockWithNotify(x,y,z, Block.tilledSoil.ID, true);
        world.addTimeEvent(x,y,z, world.ce.save.time + ((BlockSoil)Block.tilledSoil).getUpdateTime());
        world.addTilledSoilState(new TilledSoilState(Chunk.getBlockIndexFromCoordinates(x,y,z), 0.5f, 0.5f, 0.5f, 0.5f, TilledSoilState.NO_FERTILIZER), x,y,z);
        CosmicEvolution.instance.soundPlayer.playSound(player.x, player.y, player.z, new Sound(Sound.dirt, false, 1f), 1f);
        player.reduceHeldItemDurability();
    }
}
