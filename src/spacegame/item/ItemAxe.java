package spacegame.item;

import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public final class ItemAxe extends Item {
    public ItemAxe(short ID, int textureID, String filepath, Material material) {
        super(ID, textureID, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(!(Block.list[world.getBlockID(x,y,z)] instanceof BlockLog))return;
        world.setBlockWithNotify(x,y,z, Block.air.ID);

        for(int i = 0; i < 4; i++){
            world.addEntity(new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5, z + CosmicEvolution.globalRand.nextDouble(), Item.fireWood.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
        }

        player.reduceHeldItemDurability();
    }
}
