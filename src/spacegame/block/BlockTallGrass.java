package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.itemstate.SeedState;
import spacegame.world.World;

public final class BlockTallGrass extends Block {
    public BlockTallGrass(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
       if(CosmicEvolution.globalRand.nextInt(100) > 10){
           super.onLeftClick(x,y,z, world, player);
           return;
       }

        SeedState seedState = new SeedState(false, 0, "no target");


        EntityItem entityItem = new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5, z + CosmicEvolution.globalRand.nextDouble(),
                Item.seedWildGrass.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0L, seedState);

        world.addEntity(entityItem);
        super.onLeftClick(x,y,z, world, player);
    }
}
