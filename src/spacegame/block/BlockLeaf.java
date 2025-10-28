package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class BlockLeaf extends Block {
    public BlockLeaf(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        super.onLeftClick(x,y,z, world, player);
        if(CosmicEvolution.globalRand.nextInt(100) <= 1){
            world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.treeSeed.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
        }
        this.notifySurroundingLeafBlocks(x,y,z, world);
    }

    @Override
    public int getBlockTexture(int face){
        return GameSettings.transparentLeaves ? this.textureID + 14 : this.textureID;
    }


    public void decayLeaf(int x, int y, int z, World world) {
        if(CosmicEvolution.globalRand.nextInt(100) != 0)return;
        if(!this.canLeafDecay(x,y,z, world))return;

        if(CosmicEvolution.globalRand.nextInt(100) <= 1){
            world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.treeSeed.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
        }
        if (this.itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), this.droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[this.droppedItemID].durability));
        }
        world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeDecayableLeafFromArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        world.setBlockWithNotify(x,y,z, Block.air.ID);
        this.notifySurroundingLeafBlocks(x,y,z, world);
    }

    private void notifySurroundingLeafBlocks(int x, int y, int z, World world){
        int minBoxX = x - 1;
        int minBoxY = y - 1;
        int minBoxZ = z - 1;
        int maxBoxX = x + 1;
        int maxBoxY = y + 1;
        int maxBoxZ = z + 1;

        for(x = minBoxX; x <= maxBoxX; x++){
            for(y = minBoxY; y <= maxBoxY; y++){
                for(z = minBoxZ; z <= maxBoxZ; z++){
                    if(Block.list[world.getBlockID(x,y,z)] instanceof BlockLeaf){
                        world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addDecayableLeafToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
                    }
                }
            }
        }
    }



    private boolean canLeafDecay(int x, int y, int z, World world){
        int minBoxX = x - 1;
        int minBoxY = y - 1;
        int minBoxZ = z - 1;
        int maxBoxX = x + 1;
        int maxBoxY = y + 1;
        int maxBoxZ = z + 1;

        for(x = minBoxX; x <= maxBoxX; x++){
            for(y = minBoxY; y <= maxBoxY; y++){
                for(z = minBoxZ; z <= maxBoxZ; z++){
                    if(Block.list[world.getBlockID(x,y,z)] instanceof BlockLog){
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
