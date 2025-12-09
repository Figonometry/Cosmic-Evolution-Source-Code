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
        float x = 0;
        float y = -256;
        for(int i = 0; i < this.itemStacks.length; i++){
            this.itemStacks[i] = new ItemStack(null, (byte)0,x,y);
            x += 65;
            if(x == 65 * inventoryWidth){
                y += 65;
                x = 0;
            }
        }
    }

    public Inventory(int inventoryWidth, int inventoryHeight, int extraSpaces){ //Player inventory
        this.inventoryHeight = inventoryHeight;
        this.inventoryWidth = inventoryWidth;
        this.itemStacks = new ItemStack[(inventoryHeight * inventoryWidth) + extraSpaces];
        float x = 22;
        float y = -192;
        int i;
        for(i = 0; i < this.itemStacks.length - extraSpaces; i++){
            this.itemStacks[i] = new ItemStack(null, (byte)0,x,y);
            x += 65;
            if(x == 22 + (65 * inventoryWidth)){
                y += i < inventoryWidth ? 128 : 65;
                x = 22;
            }
        }
        y = 261;
        this.itemStacks[i] = new ItemStack(null, (byte)0, -290, -192, Item.ITEM_TYPE_OFFHAND);
        this.itemStacks[i + 1] = new ItemStack(null, (byte)0, -488, -192, Item.ITEM_TYPE_PLAYER_STORAGE);
        this.itemStacks[i + 2] = new ItemStack(null, (byte)0, -196, y - 65, Item.ITEM_TYPE_CLOTHING_HEAD);
        this.itemStacks[i + 3] = new ItemStack(null, (byte)0, -96, y - 65, Item.ITEM_TYPE_ARMOR_HEAD);
        this.itemStacks[i + 4] = new ItemStack(null, (byte)0, -196, y - 162, Item.ITEM_TYPE_CLOTHING_TORSO);
        this.itemStacks[i + 5] = new ItemStack(null, (byte)0, -96, y - 162, Item.ITEM_TYPE_ARMOR_TORSO);
        this.itemStacks[i + 6] = new ItemStack(null, (byte)0, -196, y - 259, Item.ITEM_TYPE_CLOTHING_LEGS);
        this.itemStacks[i + 7] = new ItemStack(null, (byte)0, -96, y - 259, Item.ITEM_TYPE_ARMOR_LEGS);
        this.itemStacks[i + 8] = new ItemStack(null, (byte)0, -196, y - 356, Item.ITEM_TYPE_CLOTHING_FEET);
        this.itemStacks[i + 9] = new ItemStack(null, (byte)0, -96, y - 356, Item.ITEM_TYPE_ARMOR_FEET);
    }

    public void shiftAllItemStacks(int x, int y){
        for(int i = 0; i < this.itemStacks.length; i++){
            this.itemStacks[i].x += x;
            this.itemStacks[i].y += y;
        }
    }

    public void shiftPlayerHotbar(int x, int y){
        for(int i = 0; i < 9; i++){
            this.itemStacks[i].x += x;
            this.itemStacks[i].y += y;
        }
    }

    public void resetAllItemStacks(){
        for(int i = 0; i < this.itemStacks.length; i++){
          this.itemStacks[i].resetStackPosition();
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
                    if (stack.count + count <= Item.list[itemID].stackLimit) {
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
            if(stack.item == null && !stack.usesExclusiveItem){
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
