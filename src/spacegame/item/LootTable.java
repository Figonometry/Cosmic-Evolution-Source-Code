package spacegame.item;

import spacegame.core.CosmicEvolution;
import spacegame.item.itemstate.ItemState;

import java.util.ArrayList;
import java.util.HashMap;

public final class LootTable {
    private int key = 0;
    private ArrayList<int[]> keyRanges = new ArrayList<>();
    private HashMap<Integer, ItemStack> lootTemplates = new HashMap();

    //Adds a key range in the order min, max, and the key value
    private void addKeyRange(int min, int max){
        this.keyRanges.add(new int[]{min, max, this.key});
    }

    //Creates a new ItemStack object and copies incoming parameters into the object and puts in  the hashmap
    public void addLoot(short itemID, short metadata, byte count, short durability, long decayTime, ItemState itemState, int min, int max){
        this.addKeyRange(min, max);

        ItemStack template = new ItemStack(Item.list[itemID], count, 0, 0);
        template.metadata = metadata;
        template.durability = durability;
        template.decayTime = decayTime;
        template.itemState = itemState == null ? null : itemState.copy();

        this.lootTemplates.put(this.key, template);
        this.key++;
    }

    //Pulls the template from the hashmap after normalizing the key and copies to a return object
    public ItemStack getLoot(){
        int chance = CosmicEvolution.globalRand.nextInt(100);

        ItemStack template = this.lootTemplates.get(this.normalizeToKey(chance));
        if(template == null)return null;


        ItemStack itemStack = new ItemStack((Item) null, (byte) 0, 0, 0);

        itemStack.copyDataToStack(template);

        return itemStack;
    }


    //Linearly searaches the arraylist to check if the chance is within the min and max stored in keyranges, if true for both returns the key value stored in index 2 of the int array
    private int normalizeToKey(int chance){
        int[] keyRange;
        for(int i = 0; i < this.keyRanges.size(); i++){
            keyRange = this.keyRanges.get(i);
            if(chance >= keyRange[0] && chance <= keyRange[1]){
                return keyRange[2];
            }
        }

        return Integer.MAX_VALUE;//Default to max value
    }



}
