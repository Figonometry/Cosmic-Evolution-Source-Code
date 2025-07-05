package spacegame.item;

import spacegame.entity.EntityPlayer;

public final class Inventory {
    public ItemStack[] itemStacks;
    public int inventoryWidth;
    public int inventoryHeight;


    public Inventory(int inventoryWidth, int inventoryHeight){
        this.inventoryHeight = inventoryHeight;
        this.inventoryWidth = inventoryWidth;
        this.itemStacks = new ItemStack[inventoryHeight * inventoryWidth];
        float x = -256;
        float y = 0;
        for(int i = 0; i < this.itemStacks.length; i++){
            this.itemStacks[i] = new ItemStack(null, (byte)0,x,y);
            x += 65;
        }
    }

    public void renderInventory(){
        ItemStack stack;
        for(int i = 0; i < this.itemStacks.length; i++){
            stack = this.itemStacks[i];
            if(stack != null){
                stack.renderItemStack(true);
            }
        }
    }

    public void loadItemToInventory(short itemID, short metadata, byte count, short durability, int slot){
        this.itemStacks[slot].item = Item.list[itemID];
        this.itemStacks[slot].metadata = metadata;
        this.itemStacks[slot].durability = durability;
        this.itemStacks[slot].count = count;
    }

    public boolean addItemToInventory(short itemID, short metadata, byte count, short durability){
        ItemStack stack;
        for(int i = 0; i < this.itemStacks.length; i++) {
            stack = this.itemStacks[i];
            if (stack.item != null) {
                if (stack.item.ID == itemID && stack.metadata == metadata && stack.durability == durability) {
                    if (stack.count + count < Item.list[itemID].stackLimit) {
                        stack.count += count;
                        return true;
                    } else {
                        while (stack.count != Item.list[itemID].stackLimit) {
                            stack.count++;
                            count--;
                        }
                    }
                }
            }
        }

        for(int i = 0; i < this.itemStacks.length; i++){
            stack = this.itemStacks[i];
            if(stack.item == null){
                stack.item = Item.list[itemID];
                stack.count = count;
                stack.setMetadata(metadata);
                stack.durability = durability;
                return true;
            }
        }
        return false;
    }

    public void removeItemFromInventory() {
        this.itemStacks[EntityPlayer.selectedInventorySlot].count--;
        if(this.itemStacks[EntityPlayer.selectedInventorySlot].count <= 0){
            this.itemStacks[EntityPlayer.selectedInventorySlot].item.onDestroy(this.itemStacks[EntityPlayer.selectedInventorySlot], EntityPlayer.selectedInventorySlot);
        }
    }

}
