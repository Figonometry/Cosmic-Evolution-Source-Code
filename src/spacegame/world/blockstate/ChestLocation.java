package spacegame.world.blockstate;

import org.joml.Vector3f;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.item.IDecayItem;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.world.Chunk;

public final class ChestLocation extends BlockState {
    public int index;
    public Inventory inventory;
    public Chunk chunk;
    public ChestLocation(int index, Inventory inventory, Chunk chunk){
        this.index = index;
        this.inventory = inventory;
        this.chunk = chunk;
    }


    @Override
    public void onTick(Chunk callingChunk){
        for(int j = 0; j < this.inventory.itemStacks.length; j++){
            if(this.inventory.itemStacks[j].item instanceof IDecayItem){
                if(CosmicEvolution.instance.save.time >= this.inventory.itemStacks[j].decayTime){
                    this.inventory.itemStacks[j].item = Item.rot;
                }
            }
        }
    }
    @Override
    public void onStateRemoval(){
        for(int i = 0; i < inventory.itemStacks.length; i++) {
            if(inventory.itemStacks[i].item == Item.block) {
                EntityBlock block = new EntityBlock(this.chunk.getBlockXFromIndex(index) + 0.5, this.chunk.getBlockYFromIndex(index) + 0.5, this.chunk.getBlockZFromIndex(index) + 0.5, inventory.itemStacks[i].metadata, inventory.itemStacks[i].count);
                block.setMovementVector(new Vector3f(CosmicEvolution.globalRand.nextFloat(-1, 1), CosmicEvolution.globalRand.nextFloat(-1, 1), CosmicEvolution.globalRand.nextFloat(-1, 1)));
                this.chunk.addEntityToList(block);
            } else if(inventory.itemStacks[i].item != null){
                EntityItem item = new EntityItem(this.chunk.getBlockXFromIndex(index) + 0.5, this.chunk.getBlockYFromIndex(index) + 0.5, this.chunk.getBlockZFromIndex(index) + 0.5, inventory.itemStacks[i].item.ID, inventory.itemStacks[i].metadata, inventory.itemStacks[i].count, inventory.itemStacks[i].durability, 0, null);
                item.setMovementVector(new Vector3f(CosmicEvolution.globalRand.nextFloat(-1, 1), CosmicEvolution.globalRand.nextFloat(-1, 1), CosmicEvolution.globalRand.nextFloat(-1, 1)));
                this.chunk.addEntityToList(item);
            }
            inventory.itemStacks[i].item = null;
            inventory.itemStacks[i].count = 0;
            inventory.itemStacks[i].durability = 0;
            inventory.itemStacks[i].metadata = 0;
        }
    }
}
