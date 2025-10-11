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
        float y = -192;
        for(int i = 0; i < this.itemStacks.length; i++){
            this.itemStacks[i] = new ItemStack(null, (byte)0,x,y);
            x += 65;
            if(x == 329){
                y += i < 9 ? 128 : 65;
                x = -256;
            }
        }
    }

    public Inventory(int inventoryWidth, int inventoryHeight, int extraSpaces){ //Player inventory
        this.inventoryHeight = inventoryHeight;
        this.inventoryWidth = inventoryWidth;
        this.itemStacks = new ItemStack[(inventoryHeight * inventoryWidth) + extraSpaces];
        float x = 22;
        float y = -192;
        int i = 0;
        for(i = 0; i < this.itemStacks.length - extraSpaces; i++){
            this.itemStacks[i] = new ItemStack(null, (byte)0,x,y);
            x += 65;
            if(x == 607){
                y += i < 9 ? 128 : 65;
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
