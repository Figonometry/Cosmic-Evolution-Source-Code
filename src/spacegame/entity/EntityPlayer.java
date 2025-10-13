package spacegame.entity;

import org.joml.AxisAngle4f;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.*;
import spacegame.gui.*;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemStack;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Chunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

public final class EntityPlayer extends EntityLiving {
    private SpaceGame sg;
    public static int selectedInventorySlot = 1;
    public int chunkX;
    public int chunkY;
    public int chunkZ;
    public int prevChunkX;
    public int prevChunkY;
    public int prevChunkZ;
    public short blockUnderPlayer;
    public short prevBlockUnderPlayer;
    public int breakTimer = 0;
    public int[] blockLookingAt = new int[3];
    public int[] prevBlockLookingAt = new int[3];
    public Inventory inventory;
    public final File playerFile;
    public boolean loadedFromFile;
    public double lastYOnGround;
    public int drowningTimer;
    public int damageTiltTimer;
    public boolean runDamageTilt;
    public byte swingTimer;
    public boolean isSwinging;
    public boolean startViewBob;
    public int viewBobTimer;
    public byte viewBobDelay;
    public byte inventoryUpgradeLevel = 1;
    public float hardnessThreshold = 0.1f;

    public EntityPlayer(SpaceGame spaceGame, double x, double y, double z) {
        super(Integer.MAX_VALUE);
        this.sg = spaceGame;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = 0.5;
        this.depth = 0.5;
        this.height = 1.79;
        this.chunkX = (int) (this.x / 32);
        this.chunkY = (int) (this.y / 32);
        this.chunkZ = (int) (this.z / 32);
        this.health = 100;
        this.maxHealth = 100;
        this.playerFile = new File(this.sg.save.saveFolder + "/player.dat");
        this.inventory = new Inventory(9,1, 10);
    }
    public EntityPlayer(SpaceGame spaceGame, File playerFile, double x, double y, double z) {
        super(Integer.MAX_VALUE);
        this.sg = spaceGame;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = 0.5;
        this.depth = 0.5;
        this.height = 1.79;
        this.chunkX = (int) (this.x / 32);
        this.chunkY = (int) (this.y / 32);
        this.chunkZ = (int) (this.z / 32);
        this.maxHealth = 100;
        this.playerFile = playerFile;
        this.loadedFromFile = true;
        this.loadPlayerFromFile();
        if(this.inventoryUpgradeLevel == 0){
            this.inventoryUpgradeLevel = 1;
        }
    }

    private void loadPlayerFromFile(){
        try{
            FileInputStream inputStream = new FileInputStream(this.playerFile);
            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
            NBTTagCompound player = compoundTag.getCompoundTag("Player");
            NBTTagCompound inventory = player.getCompoundTag("Inventory");

            this.pitch = player.getFloat("pitch");
            this.yaw = player.getFloat("yaw");
            this.deltaX = player.getDouble("deltaX");
            this.deltaY= player.getDouble("deltaY");
            this.deltaZ = player.getDouble("deltaZ");
            this.prevDeltaPitch = player.getFloat("prevDeltaPitch");
            this.prevDeltaYaw = player.getFloat("prevDeltaYaw");
            this.timeFalling = player.getInteger("timeFalling");
            this.jumpTime = player.getInteger("jumpTime");
            this.isOnGround = player.getBoolean("isOnGround");
            this.isJumping = player.getBoolean("isJumping");
            this.inWater = player.getBoolean("inWater");
            this.swimming = player.getBoolean("swimming");
            this.health = player.getFloat("health");
            this.lastYOnGround = player.getDouble("lastYOnGround");
            this.drowningTimer = player.getInteger("drowningTimer");
            this.inventoryUpgradeLevel = player.getByte("inventoryUpgradeLevel");

            this.inventory = new Inventory(9,this.inventoryUpgradeLevel, 10);

            NBTTagCompound item;
            for(int i = 0; i < this.inventory.itemStacks.length; i++) {
                item = inventory.getCompoundTag("slot " + i);
                if (item != null) {
                    short id = item.getShort("id");
                    byte count = item.getByte("count");
                    short durability = item.getShort("durability");
                    short metadata = item.getShort("metadata");
                    this.inventory.loadItemToInventory(id, metadata, count, durability, i);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void savePlayerToFile(){
        try{
            FileOutputStream outputStream = new FileOutputStream(this.playerFile);
            NBTTagCompound data = new NBTTagCompound();
            NBTTagCompound playerData = new NBTTagCompound();
            NBTTagCompound inventory = new NBTTagCompound();
            data.setTag("Player", playerData);
            playerData.setTag("Inventory", inventory);

            playerData.setDouble("x", this.x);
            playerData.setDouble("y", this.y);
            playerData.setDouble("z", this.z);
            playerData.setFloat("pitch", this.pitch);
            playerData.setFloat("yaw", this.yaw);
            playerData.setDouble("deltaX", this.deltaX);
            playerData.setDouble("deltaY", this.deltaY);
            playerData.setDouble("deltaZ", this.deltaZ);
            playerData.setFloat("prevDeltaPitch", this.prevDeltaPitch);
            playerData.setFloat("prevDeltaYaw", this.prevDeltaYaw);
            playerData.setInteger("timeFalling", this.timeFalling);
            playerData.setInteger("jumpTime", this.jumpTime);
            playerData.setBoolean("isOnGround", this.isOnGround);
            playerData.setBoolean("isJumping", this.isJumping);
            playerData.setBoolean("inWater", this.inWater);
            playerData.setBoolean("swimming", this.swimming);
            playerData.setFloat("health", this.health);
            playerData.setDouble("lastYOnGround", this.lastYOnGround);
            playerData.setInteger("drowningTimer", this.drowningTimer);
            playerData.setByte("inventoryUpgradeLevel", this.inventoryUpgradeLevel);

            ItemStack stack;
            NBTTagCompound[] items = new NBTTagCompound[this.inventory.itemStacks.length];
            int slotNumber = 0;
            for(int i = 0; i < this.inventory.itemStacks.length; i++){
                stack = this.inventory.itemStacks[i];
                if(stack.item != null) {
                    items[i] = new NBTTagCompound();
                    items[i].setShort("id", stack.item.ID);
                    items[i].setByte("count", stack.count);
                    items[i].setShort("metadata", stack.metadata);
                    items[i].setShort("durability", stack.durability);
                    inventory.setTag("slot " + slotNumber, items[i]);
                }
                slotNumber++;
            }

            NBTIO.writeCompressed(data, outputStream);
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkItemDurability() {
        for (int i = 0; i < this.inventory.itemStacks.length; i++) {
            if (this.inventory.itemStacks[i].item != null) {
                if (Item.list[this.inventory.itemStacks[i].item.ID].durability != -1) {
                    if (this.inventory.itemStacks[i].durability <= 0) {
                        this.inventory.itemStacks[i].item.onDestroy(this.inventory.itemStacks[i], i);
                    }
                }
            }
        }
    }

    public short getHeldItem(){
        if(this.inventory.itemStacks[selectedInventorySlot].item != null) {
            return this.inventory.itemStacks[selectedInventorySlot].item.ID;
        }
        return Item.NULL_ITEM_REFERENCE;
    }

    public int getHeldItemCount(){
        if(this.inventory.itemStacks[selectedInventorySlot].item != null){
            return this.inventory.itemStacks[selectedInventorySlot].count;
        }
        return 0;
    }

    public boolean containsAmountOfItem(short item, int count){
        for(int i = 0; i < this.inventory.itemStacks.length; i++){
            if(this.inventory.itemStacks[i].item == null)continue;

            if(this.inventory.itemStacks[i].item.ID == item && this.inventory.itemStacks[i].count >= count){
                return true;
            }
        }

        return false;
    }

    public void removeSpecificItemFromInventory(short item){
        for(int i = 0; i < this.inventory.itemStacks.length; i++){
            if(this.inventory.itemStacks[i].item == null)continue;

            if(this.inventory.itemStacks[i].item.ID == item){
                this.inventory.itemStacks[i].count--;
                break;
            }
        }
    }

    public short getHeldItemDurability(){
        if(this.inventory.itemStacks[selectedInventorySlot].item != null) {
            return this.inventory.itemStacks[selectedInventorySlot].durability;
        }
        return Item.NULL_ITEM_DURABILITY;
    }

    public boolean isHoldingBlock(){
        return this.inventory.itemStacks[selectedInventorySlot].item == Item.block;
    }

    public void setPlayerStorageLevel(byte storageLevel){
        if(this.inventoryUpgradeLevel == storageLevel)return;

        //Perform inventory data copy


        if(this.inventoryUpgradeLevel < storageLevel){
            this.inventoryUpgradeLevel = storageLevel;
            Inventory newInventory = new Inventory(9, this.inventoryUpgradeLevel, 10);
            for(int i = 0; i < this.inventory.itemStacks.length - 10; i++){
                newInventory.itemStacks[i] = this.inventory.itemStacks[i];
            }
            newInventory.itemStacks[newInventory.itemStacks.length - 1] = this.inventory.itemStacks[this.inventory.itemStacks.length - 1];
            newInventory.itemStacks[newInventory.itemStacks.length - 2] = this.inventory.itemStacks[this.inventory.itemStacks.length - 2];
            newInventory.itemStacks[newInventory.itemStacks.length - 3] = this.inventory.itemStacks[this.inventory.itemStacks.length - 3];
            newInventory.itemStacks[newInventory.itemStacks.length - 4] = this.inventory.itemStacks[this.inventory.itemStacks.length - 4];
            newInventory.itemStacks[newInventory.itemStacks.length - 5] = this.inventory.itemStacks[this.inventory.itemStacks.length - 5];
            newInventory.itemStacks[newInventory.itemStacks.length - 6] = this.inventory.itemStacks[this.inventory.itemStacks.length - 6];
            newInventory.itemStacks[newInventory.itemStacks.length - 7] = this.inventory.itemStacks[this.inventory.itemStacks.length - 7];
            newInventory.itemStacks[newInventory.itemStacks.length - 8] = this.inventory.itemStacks[this.inventory.itemStacks.length - 8];
            newInventory.itemStacks[newInventory.itemStacks.length - 9] = this.inventory.itemStacks[this.inventory.itemStacks.length - 9];
            newInventory.itemStacks[newInventory.itemStacks.length - 10] = this.inventory.itemStacks[this.inventory.itemStacks.length - 10];

            this.inventory = newInventory;
            this.sg.setNewGui(new GuiInventoryPlayer(this.sg, this.inventory));
        } else {
            this.inventoryUpgradeLevel = storageLevel;
            Inventory newInventory = new Inventory(9, this.inventoryUpgradeLevel, 10);
            int i;
            for(i = 0; i < newInventory.itemStacks.length - 10; i++){
                newInventory.itemStacks[i] = this.inventory.itemStacks[i];
            }

            while(i < this.inventory.itemStacks.length){
                if(this.inventory.itemStacks[i].item != null) {

                    if (this.inventory.itemStacks[i].item.ID == Item.block.ID) {
                        EntityBlock block = new EntityBlock(this.x, this.y, this.z, this.inventory.itemStacks[i].metadata, this.inventory.itemStacks[i].count);
                        double[] vector = SpaceGame.camera.rayCast(1);
                        Vector3d difVector = new Vector3d(vector[0] - this.x, (vector[1] - this.y) + this.height/2, vector[2] - this.z);
                        difVector.normalize();
                        block.setMovementVector(new Vector3f((float) difVector.x, (float) difVector.y, (float) difVector.z));
                        this.sg.save.activeWorld.addEntity(block);
                    } else {
                        EntityItem item = new EntityItem(this.x, this.y, this.z, this.inventory.itemStacks[i].item.ID, Item.NULL_ITEM_METADATA, this.inventory.itemStacks[i].count, this.inventory.itemStacks[i].durability);
                        double[] vector = SpaceGame.camera.rayCast(1);
                        Vector3d difVector = new Vector3d(vector[0] - this.x, (vector[1] - this.y) + this.height/2, vector[2] - this.z);
                        difVector.normalize();
                        item.setMovementVector(new Vector3f((float) difVector.x, (float) difVector.y, (float) difVector.z));
                        this.sg.save.activeWorld.addEntity(item);
                    }

                }
                i++;
            }


            newInventory.itemStacks[newInventory.itemStacks.length - 1] = this.inventory.itemStacks[this.inventory.itemStacks.length - 1];
            newInventory.itemStacks[newInventory.itemStacks.length - 2] = this.inventory.itemStacks[this.inventory.itemStacks.length - 2];
            newInventory.itemStacks[newInventory.itemStacks.length - 3] = this.inventory.itemStacks[this.inventory.itemStacks.length - 3];
            newInventory.itemStacks[newInventory.itemStacks.length - 4] = this.inventory.itemStacks[this.inventory.itemStacks.length - 4];
            newInventory.itemStacks[newInventory.itemStacks.length - 5] = this.inventory.itemStacks[this.inventory.itemStacks.length - 5];
            newInventory.itemStacks[newInventory.itemStacks.length - 6] = this.inventory.itemStacks[this.inventory.itemStacks.length - 6];
            newInventory.itemStacks[newInventory.itemStacks.length - 7] = this.inventory.itemStacks[this.inventory.itemStacks.length - 7];
            newInventory.itemStacks[newInventory.itemStacks.length - 8] = this.inventory.itemStacks[this.inventory.itemStacks.length - 8];
            newInventory.itemStacks[newInventory.itemStacks.length - 9] = this.inventory.itemStacks[this.inventory.itemStacks.length - 9];
            newInventory.itemStacks[newInventory.itemStacks.length - 10] = this.inventory.itemStacks[this.inventory.itemStacks.length - 10];

            this.inventory = newInventory;
            this.sg.setNewGui(new GuiInventoryPlayer(this.sg, this.inventory));
        }
    }


    public short getHeldBlock() {
        if(this.inventory.itemStacks[selectedInventorySlot].item != null){
            if(this.inventory.itemStacks[selectedInventorySlot].item.ID == Item.block.ID) {
                return this.inventory.itemStacks[selectedInventorySlot].metadata;
            }
        }
        return 0;
    }

    public boolean addItemToInventory(short itemID, short metadata, byte count, short durability){
       return this.inventory.addItemToInventory(itemID, metadata, count, durability);
    }

    public void removeItemFromInventory(){
        this.inventory.removeItemFromInventory();
    }

    private void dropItemFromInventory(){
        short itemID = this.getHeldItem();
        short blockID = this.getHeldBlock();
        if(itemID == Item.block.ID) {
            if (blockID != Block.air.ID) {
                EntityBlock droppedBlock = new EntityBlock(this.x, this.y, this.z, blockID, (byte) 1);
                this.removeItemFromInventory();
                double[] vector = SpaceGame.camera.rayCast(1);
                Vector3d difVector = new Vector3d(vector[0] - this.x, (vector[1] - this.y) + this.height/2, vector[2] - this.z);
                difVector.normalize();
                droppedBlock.setMovementVector(new Vector3f((float) difVector.x, (float) difVector.y, (float) difVector.z));
                this.sg.save.activeWorld.findChunkFromChunkCoordinates(this.chunkX, this.chunkY, this.chunkZ).addEntityToList(droppedBlock);
            }
        } else if(itemID != Item.NULL_ITEM_REFERENCE) {
            EntityItem droppedItem = new EntityItem(this.x, this.y, this.z, itemID, (byte)1,(byte) 1, this.getHeldItemDurability());
            this.removeItemFromInventory();
            double[] vector = SpaceGame.camera.rayCast(1);
            Vector3d difVector = new Vector3d(vector[0] - this.x, (vector[1] - this.y) + this.height/2, vector[2] - this.z);
            difVector.normalize();
            droppedItem.setMovementVector(new Vector3f((float) difVector.x, (float) difVector.y, (float) difVector.z));
            this.sg.save.activeWorld.findChunkFromChunkCoordinates(this.chunkX, this.chunkY, this.chunkZ).addEntityToList(droppedItem);
        }
    }


    @Override
    public void tick() {
        if (this.sg.save.activeWorld.chunkController.findChunkFromChunkCoordinates((int) (this.x) >> 5, (int) (this.y) >> 5, (int) (this.z) >> 5) != null && !this.sg.save.activeWorld.paused) {
            if(this.sg.currentGui instanceof GuiInGame) {
                this.updateYawAndPitch();
            }
            this.setMovementAmount();
            this.doGravity();
            if(!(this.sg.currentGui instanceof GuiInGame || this.sg.currentGui instanceof GuiInventory || this.sg.currentGui instanceof GuiAction || this.sg.currentGui instanceof GuiCrafting)){
                this.deltaX = 0;
                this.deltaY = 0;
                this.deltaZ = 0;
                this.isJumping = false;
                this.moveEntityUp = false;
                this.moveEntityUpDistance = 0;
            }
            this.handleViewBob();

            this.prevX = this.x;
            this.prevY = this.y;
            this.prevZ = this.z;

            this.setEntityState();
            this.moveAndHandleCollision();
            this.performStepSound();
            this.updateAxisAlignedBB();
            this.moveCamera();
            this.setBlockPlayerLookingAt();
            this.checkHealth();
            this.updateSwingTimer();
            this.checkItemDurability();
        }
    }

    private void updateYawAndPitch() {
        float rawDeltaYaw = (MouseListener.getDeltaX() / 9) * GameSettings.sensitivity;
        float rawDeltaPitch = (MouseListener.getDeltaY() / 9) * GameSettings.sensitivity;

        if(GameSettings.invertMouse){
            rawDeltaPitch *= -1;
        }

        float deltaYaw = (float) Math.toRadians(rawDeltaYaw);
        float deltaPitch = (float) Math.toRadians(rawDeltaPitch);

        if (deltaYaw != this.prevDeltaYaw) {
            this.yaw -= rawDeltaYaw;
            SpaceGame.camera.viewMatrix.rotateAround(new Quaterniond(0, 0, 0, 1).rotationAxis(new AxisAngle4f(-deltaYaw, 0.0F, 1.0F, 0.0F)), this.x - (this.chunkX * 32), this.y - (this.chunkY * 32), this.z - (this.chunkZ * 32)); //This needs to be fixed
            if(SpaceGame.instance.save.activeWorld != null) {
                SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.recalculateQueries = true;
            }
        }

        if ((deltaPitch != this.prevDeltaPitch) && ((this.pitch + rawDeltaPitch < 90) && (this.pitch + rawDeltaPitch > -90))) {
            this.pitch += rawDeltaPitch;
            SpaceGame.camera.viewMatrix.rotateLocalX(-deltaPitch);
            if(SpaceGame.instance.save.activeWorld != null) {
                SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.recalculateQueries = true;
            }
        }

        if (this.pitch < -90) {
            this.pitch = -90;
        }

        if (this.pitch > 90) {
            this.pitch = 90;
        }

        if (this.yaw < 0) {
            this.yaw += 360;
        }
        if (this.yaw >= 360) {
            this.yaw %= 360;
        }


        this.prevDeltaYaw = deltaYaw;
        this.prevDeltaPitch = deltaPitch;
    }


    private void setMovementAmount() {
        float rawDeltaZ = 0.0F;
        float rawDeltaX = 0.0F;
        float rawDeltaY = 0.0F;
        if (KeyListener.isKeyPressed(GameSettings.forwardKey.keyCode)) {
            --rawDeltaX;
        }

        if (KeyListener.isKeyPressed(GameSettings.backwardKey.keyCode)) {
            ++rawDeltaX;
        }

        if (KeyListener.isKeyPressed(GameSettings.leftKey.keyCode)) {
            ++rawDeltaZ;
        }

        if (KeyListener.isKeyPressed(GameSettings.rightKey.keyCode)) {
            --rawDeltaZ;
        }

        if(((KeyListener.isKeyPressed(GameSettings.backwardKey.keyCode) || KeyListener.isKeyPressed(GameSettings.leftKey.keyCode) || KeyListener.isKeyPressed(GameSettings.rightKey.keyCode)) && (!KeyListener.isKeyPressed(GameSettings.forwardKey.keyCode))) || this.inWater){
            this.speed = 0.05D;
        }

        if(KeyListener.isKeyPressed(GameSettings.dropKey.keyCode) && KeyListener.keyReleased[GameSettings.dropKey.keyCode]){
            this.dropItemFromInventory();
            KeyListener.setKeyReleased(GameSettings.dropKey.keyCode);
        }

        //this.developerDebugMovement();


        if ((KeyListener.isKeyPressed(GameSettings.jumpKey.keyCode) && this.isOnGround && !this.inWater)) {
            this.moveEntityUp = true;
            this.moveEntityUpDistance = 1.1D;
            this.isOnGround = false;
            this.isJumping = true;
        }

        if ((KeyListener.isKeyPressed(GameSettings.jumpKey.keyCode) && this.inWater)) {
            this.swimming = true;
            this.isOnGround = false;
        } else {
            this.swimming = false;
        }


        this.updateGroundPosition(rawDeltaX, rawDeltaY, rawDeltaZ);
    }

    private void developerDebugMovement(){
           if ((KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))) {
               this.y -= 1F * this.speed;
               SpaceGame.camera.viewMatrix.translate(0, 1 * this.speed, 0);
           }

           if ((KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE))) {
               this.y += 1F * this.speed;
               SpaceGame.camera.viewMatrix.translate(0, -1 * this.speed, 0);
           }
    }

    private void setEntityState(){
        int playerX = MathUtil.floorDouble(this.x);
        int playerY = MathUtil.floorDouble(this.y);
        int playerYHead = MathUtil.floorDouble(this.y + (this.height * 0.25));
        int playerYFoot = MathUtil.floorDouble(this.y - (this.height * 0.25));
        int playerZ = MathUtil.floorDouble(this.z);
        this.blockUnderPlayer = this.sg.save.activeWorld.getBlockID(playerX, MathUtil.floorDouble(this.y - (this.height/2) - 0.1), playerZ);
        if(Block.list[this.blockUnderPlayer].isSolid && !this.inWater){
            this.handleFallDamage();
            this.lastYOnGround = this.y;
        }
        this.inWater = Block.list[this.sg.save.activeWorld.getBlockID(playerX, playerY, playerZ)].ID == Block.water.ID;
        short headBlock = Block.list[this.sg.save.activeWorld.getBlockID(playerX, playerYHead, playerZ)].ID;
        if(headBlock == Block.water.ID){
            Shader.worldShaderTextureArray.uploadBoolean("underwater", true);
            this.drowningTimer++;
            if(this.drowningTimer >= 300){
                this.damage(5);
                this.drowningTimer -= 60;
            }
        } else if(Block.list[headBlock].isSolid){
            this.damage(0.1f);
        } else {
            Shader.worldShaderTextureArray.uploadBoolean("underwater", false);
            this.drowningTimer = 0;
        }

        if(this.inWater){
            this.isOnGround = false;
        }

        if(this.inWater){
            if(!this.isOnGround && !this.moveEntityUp) {
                this.deltaY = -0.05D;
                this.timeFalling = 0;
            }
            this.speed = 0.05D;
        } else {
            this.speed = 0.1D;
        }


        if(this.swimming){
            this.isOnGround = false;
            this.isJumping = false;
            this.jumpTime = 0;
            this.deltaY = 0.1;
            SpaceGame.camera.viewMatrix.translate(0, -0.1D, 0);
        }


        if(this.prevInWater && !this.inWater && !this.moveEntityUp && this.deltaY > 0.0){
            this.moveEntityUp = true;
            this.moveEntityUpDistance = 1.1D;
            this.timeFalling = 0;
            this.isJumping = true;
        }

        if (this.moveEntityUp) {
            if(!this.isJumping) {
                this.speed = 0.01D;
            }
            if (this.moveEntityUpDistance <= 0D) {
                this.moveEntityUp = false;
                this.moveEntityUpDistance = 0;
                this.speed = 0.1D;
                this.isJumping = false;
            } else {
                this.deltaY = 0.05D;
                SpaceGame.camera.viewMatrix.translate(0, -0.05D, 0);
                this.moveEntityUpDistance -= 0.05D;
            }
        }

        if(this.inWater && !this.prevInWater){
            new SoundPlayer(this.sg).playSound(this.x, this.y - 2 ,this.z, new Sound(Sound.waterSplash, false), new Random().nextFloat(0.4F, 0.7F));
        }

        if(this.prevBlockUnderPlayer != this.blockUnderPlayer){
            new SoundPlayer(this.sg).playSound(this.x, this.y - 2 ,this.z, new Sound(Block.list[this.blockUnderPlayer].stepSound, false), new Random().nextFloat(0.4F, 0.7F));
        }

        this.damageTimer--;
        if(this.damageTimer <= 0){
            this.canDamage = true;
        }

        this.prevInWater = this.inWater;
        this.prevBlockUnderPlayer = this.blockUnderPlayer;
    }


    private void handleViewBob(){
        if(this.x != this.prevX || this.z != this.prevZ){
            this.startViewBob = true;
        }

        if(this.blockUnderPlayer == Block.air.ID){
            this.startViewBob = false;
        }

        if(this.x == this.prevX || this.z == this.prevZ){
            if(this.viewBobDelay >= 15) {
                this.startViewBob = false;
                this.viewBobDelay = 0;
            } else {
                this.viewBobDelay++;
            }
        }

        if(this.startViewBob) {
            if (this.viewBobTimer < 60) {
                this.viewBobTimer++;
            } else {
                this.viewBobTimer = 0;
            }
        } else {
            this.viewBobTimer--;
            if(this.viewBobTimer < 0){
                this.viewBobTimer = 0;
            }
        }
    }

    private void setCameraPerFrame() {
        this.x += this.deltaX;
        this.y += this.deltaY;
        this.z += this.deltaZ;

        if(this.x != this.prevX || this.y != this.prevY || this.z != this.prevZ){
            if(SpaceGame.instance.save.activeWorld != null) {
                SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.recalculateQueries = true;
            }
        }

        this.chunkX = MathUtil.floorDouble(this.x) >> 5;
        this.chunkY = MathUtil.floorDouble(this.y) >> 5;
        this.chunkZ = MathUtil.floorDouble(this.z) >> 5;

        if (this.chunkX != this.prevChunkX) {
            SpaceGame.camera.viewMatrix.translate((this.chunkX - this.prevChunkX) * 32, 0, 0);
        }
        if (this.chunkY != this.prevChunkY) {
            SpaceGame.camera.viewMatrix.translate(0, (this.chunkY - this.prevChunkY) * 32, 0);
        }
        if (this.chunkZ != this.prevChunkZ) {
            SpaceGame.camera.viewMatrix.translate(0, 0, (this.chunkZ - this.prevChunkZ) * 32);
        }

        SpaceGame.camera.viewMatrix.translate(0, -this.height/2, 0);

        this.prevChunkX = this.chunkX;
        this.prevChunkY = this.chunkY;
        this.prevChunkZ = this.chunkZ;
    }

    private void moveCamera() {
        SpaceGame.camera.viewMatrix.translate(-this.deltaX, -this.deltaY, -this.deltaZ);
    }

    public void setPlayerActualPos(double x, double y, double z) {
        this.deltaX = x;
        this.deltaY = y;
        this.deltaZ = z;
        this.setCameraPerFrame();
        this.moveCamera();
    }

    public void resetCamera() {
        final double x = this.x;
        final double y = this.y;
        final double z = this.z;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.prevChunkX = 0;
        this.prevChunkY = 0;
        this.prevChunkZ = 0;
        SpaceGame.camera.resetViewMatrix();
        this.setPlayerActualPos(x, y, z);
        if(!this.sg.save.activeWorld.paused && GameSettings.viewBob) {
            double xShift = 0.25 * ((MathUtil.sin((float) (((this.sg.save.thePlayer.viewBobTimer / 60f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
            double yShift = 0.125 * ((MathUtil.sin((float) (((this.sg.save.thePlayer.viewBobTimer / 60f) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
            SpaceGame.camera.viewMatrix.translateLocal(-xShift, -yShift, 0);
        }
        this.setRotation();
    }

    public void setRotation() {
        float pitch = (float) Math.toRadians(this.pitch);
        float yaw = (float) Math.toRadians(this.yaw);
        float roll = (float) Math.toRadians(this.roll);
        SpaceGame.camera.viewMatrix.rotateLocalX(-pitch);
        SpaceGame.camera.viewMatrix.rotateAround(new Quaterniond(0, 0, 0, 1).rotationAxis(new AxisAngle4f(yaw, 0.0F, 1.0F, 0.0F)), this.x - (this.chunkX * 32), this.y - (this.chunkY * 32), this.z - (this.chunkZ * 32));
        SpaceGame.camera.viewMatrix.rotateLocalZ(-roll);
    }

    private void handleFallDamage(){
        if(this.lastYOnGround - this.y > 3){
            this.health -= this.lastYOnGround - this.y;
            this.runDamageTilt = true;
            new SoundPlayer(this.sg).playSound(this.x, this.y, this.z, new Sound(Sound.fallDamage, false), new Random().nextFloat(0.4F, 0.7F));
        }
    }

    public float getAttackDamageValue(){
       short heldItem = this.getHeldItem();
       if(heldItem == Item.NULL_ITEM_REFERENCE){
           return 5f;
       } else {
           if(this.inventory.itemStacks[selectedInventorySlot].durability != Item.NULL_ITEM_DURABILITY){
               this.reduceHeldItemDurability();
           }
           return 5f + Item.list[heldItem].attackDamage;
       }
    }

    public void setBlockPlayerLookingAt(){
        if (!this.sg.save.activeWorld.paused) {
            double[] rayCast = SpaceGame.camera.rayCast(3);
            final double multiplier = 0.05D;
            final double xDif = (rayCast[0] - this.sg.save.thePlayer.x);
            final double yDif = (rayCast[1] - (this.sg.save.thePlayer.y + this.sg.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - this.sg.save.thePlayer.z);

            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            for (int loopPass = 0; loopPass < 30; loopPass++) {
                blockX = MathUtil.floorDouble(this.sg.save.thePlayer.x + xDif * multiplier * loopPass);
                blockY = MathUtil.floorDouble(this.sg.save.thePlayer.y  + this.sg.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                blockZ = MathUtil.floorDouble(this.sg.save.thePlayer.z + zDif * multiplier * loopPass);

                if (GuiInGame.isBlockVisible(blockX, blockY, blockZ) && (Block.list[this.sg.save.activeWorld.getBlockID(blockX, blockY, blockZ)].ID != Block.air.ID && Block.list[this.sg.save.activeWorld.getBlockID(blockX, blockY, blockZ)].ID != Block.water.ID)) {
                    this.blockLookingAt[0] = blockX;
                    this.blockLookingAt[1] = blockY;
                    this.blockLookingAt[2] = blockZ;

                    if(this.blockLookingAt[0] != this.prevBlockLookingAt[0] || this.blockLookingAt[1] != this.prevBlockLookingAt[1] || this.blockLookingAt[2] != this.prevBlockLookingAt[2]){
                        this.breakTimer = 0;
                    }

                    this.prevBlockLookingAt[0] = this.blockLookingAt[0];
                    this.prevBlockLookingAt[1] = this.blockLookingAt[1];
                    this.prevBlockLookingAt[2] = this.blockLookingAt[2];
                    break;
                }
            }
        }
    }

    @Override
    public void renderShadow(){
        int x = MathUtil.floorDouble(this.x);
        int y = MathUtil.floorDouble(this.y - (this.height/2) - 0.1);
        int z = MathUtil.floorDouble(this.z);
        if(Block.list[this.sg.save.activeWorld.getBlockID(x,y,z)].isSolid) {
            Shader.worldShader2DTexture.uploadBoolean("useFog", true);
            Shader.worldShader2DTexture.uploadFloat("fogRed", SpaceGame.instance.save.activeWorld.skyColor[0]);
            Shader.worldShader2DTexture.uploadFloat("fogGreen", SpaceGame.instance.save.activeWorld.skyColor[1]);
            Shader.worldShader2DTexture.uploadFloat("fogBlue", SpaceGame.instance.save.activeWorld.skyColor[2]);
            Shader.worldShader2DTexture.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
            Vector3f chunkOffset = new Vector3f(0,0,0);
            Shader.worldShader2DTexture.uploadVec3f("chunkOffset", chunkOffset);
            RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) + (this.width / 1.5)), (float) ((this.y % 32) - (this.height/2) + 0.01F), (float) ((this.z % 32) - (this.width / 1.5)), 3);
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) - (this.width / 1.5)), (float) ((this.y % 32) - (this.height/2) + 0.01F), (float) ((this.z % 32) + (this.width / 1.5)), 1);
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) + (this.width / 1.5)), (float) ((this.y % 32) - (this.height/2) + 0.01F), (float) ((this.z % 32) + (this.width / 1.5)), 2);
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) - (this.width / 1.5)), (float) ((this.y % 32) - (this.height/2) + 0.01F), (float) ((this.z % 32) - (this.width / 1.5)), 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(shadow, Shader.worldShader2DTexture, SpaceGame.camera);
            GL46.glDisable(GL46.GL_BLEND);
        }
    }

    @Override
    public void checkHealth() {
        if(this.health <= 0){
            this.handleDeath();
        }
    }
// Har + Jar #4evar #livingthelife #girlboss
    @Override
    public void handleDeath() {
        this.clearInventory();
        this.inventoryUpgradeLevel = 1;
        this.sg.setNewGui(new GuiDeathScreen(this.sg));
        this.sg.save.activeWorld.paused = true;
    }

    @Override
    public void damage(float healthReduction){
        this.health -= healthReduction;
        this.runDamageTilt = true;
        this.damageTimer = 30;
        this.roll = 0;
        this.canDamage = false;
        new SoundPlayer(this.sg).playSound(this.x, this.y, this.z, new Sound(Sound.fallDamage, false), new Random().nextFloat(0.4F, 0.7F));
    }

    @Override
    public String getHurtSound() {
        return Sound.fallDamage;
    }

    @Override
    public String getAmbientSound() {
        return null;
    }

    private void clearInventory(){
        Chunk chunk = this.sg.save.activeWorld.chunkController.findChunkFromChunkCoordinates(this.chunkX, this.chunkY, this.chunkZ);
        Random rand = new Random();
        for(int i = 0; i < this.inventory.itemStacks.length; i++) {
            if(this.inventory.itemStacks[i].item == Item.block) {
                EntityBlock block = new EntityBlock(this.x, this.y, this.z, this.inventory.itemStacks[i].metadata, this.inventory.itemStacks[i].count);
                block.setMovementVector(new Vector3f(rand.nextFloat(-1, 1), rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)));
                chunk.addEntityToList(block);
            } else if(this.inventory.itemStacks[i].item != null){
                EntityItem item = new EntityItem(this.x, this.y, this.z,this.inventory.itemStacks[i].item.ID ,this.inventory.itemStacks[i].metadata, this.inventory.itemStacks[i].count, this.inventory.itemStacks[i].durability);
                item.setMovementVector(new Vector3f(rand.nextFloat(-1, 1), rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)));
                chunk.addEntityToList(item);
            }
            this.inventory.itemStacks[i].item = null;
            this.inventory.itemStacks[i].count = 0;
            this.inventory.itemStacks[i].durability = 0;
            this.inventory.itemStacks[i].metadata = 0;
        }
    }

    public void reduceHeldItemDurability(){
        if (this.inventory.itemStacks[selectedInventorySlot].durability != -1) {
            this.inventory.itemStacks[selectedInventorySlot].durability--;
            if(this.inventory.itemStacks[selectedInventorySlot].durability == 0){
                this.removeItemFromInventory();
            }
        }
    }

    public void updateSwingTimer(){
        if(this.isSwinging){
            this.swingTimer++;
        }
        if(this.swingTimer >= 15){
            this.swingTimer = 0;
            this.isSwinging = false;
        }
    }
}
