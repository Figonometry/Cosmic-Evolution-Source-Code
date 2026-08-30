package spacegame.world.blockstate;

import spacegame.block.Block;
import spacegame.block.BlockCampFire;
import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.item.IDecayItem;
import spacegame.item.IFuel;
import spacegame.item.IHeatable;
import spacegame.item.Item;
import spacegame.world.Chunk;
import spacegame.world.ThreadUpdateLighting;

public final class HeatableBlockLocation extends BlockState { //Trigger every 15 ticks, also is a "chest" slot 0 is fuel, slot 1 is input, slot 2 is output
    public int index;
    public float currentTemperature;
    public short currentFuelBurning;
    public long fuelBurnoutTime;
    public long heatingFinishTime;
    public boolean heating;
    public long fuelStartTime;
    public long heatStartTime;

    public HeatableBlockLocation(int index) {
        this.index = index;
    }


    @Override
    public void onTick(Chunk callingChunk){
        if((CosmicEvolution.instance.save.time & 14) != 0)return;
        this.heatItem(callingChunk);
    }

    private void consumeFuel(IFuel fuel, Chunk chunk) {
        ChestLocation location = (ChestLocation) chunk.getBlockState(this.index, MultiState.CHEST_STATE);

        if (location.inventory.itemStacks[1].item != null) {
            this.currentFuelBurning = fuel.getFuelItemID();
            this.fuelBurnoutTime = CosmicEvolution.instance.save.time + fuel.getBurnTime();
            this.fuelStartTime = CosmicEvolution.instance.save.time;
            this.heating = true;
            location.inventory.itemStacks[1].count--;
            if (location.inventory.itemStacks[1].count <= 0) {
                location.inventory.itemStacks[1].item = null;
            }

            if(Block.list[chunk.blocks[index]] instanceof BlockCampFire){
                CampfireState campfireState = (CampfireState) chunk.getBlockState(index, MultiState.CAMPFIRE_STATE);
                if(campfireState == null)return;
                campfireState.logCount--;
                if(campfireState.logCount == 0){
                    campfireState.isLit = false;

                    chunk.parentWorld.chunkController.updateChunkLighting(
                            new ThreadUpdateLighting(chunk.parentWorld, chunk,
                                    chunk.getBlockXFromIndex(index), chunk.getBlockYFromIndex(index), chunk.getBlockZFromIndex(index), Block.campfire.ID, false));
                }
                chunk.markDirty();
            }
        }
    }

    private void increaseTemperature(ChestLocation chestLocation) {
        if (Item.list[this.currentFuelBurning] instanceof IFuel && chestLocation.inventory.itemStacks[0].item != null) {
            if (this.currentTemperature >= ((IFuel) Item.list[this.currentFuelBurning]).getHeatingTemperature()) return;

            this.currentTemperature += 2.5f;
        }
    }

    private void decreaseTemperature(ChestLocation chestLocation) {
        if (this.heating && chestLocation.inventory.itemStacks[1].item != null) return;

        this.currentTemperature -= 2.5f;
    }

    public void heatItem(Chunk chunk) {
        this.heating = CosmicEvolution.instance.save.time < this.fuelBurnoutTime;
        ChestLocation chestLocation = (ChestLocation) chunk.getBlockState(this.index, MultiState.CHEST_STATE);
        if (chestLocation == null) return;

        if (!this.heating) {
            this.consumeFuel((IFuel) chestLocation.inventory.itemStacks[1].item, chunk);
        }

        if (chestLocation.inventory.itemStacks[1].item == null) {
            this.currentTemperature = 0f;
            this.heatStartTime = CosmicEvolution.instance.save.time;
            this.heatingFinishTime = this.heatStartTime + Timer.REAL_MINUTE * 10;
            this.decreaseTemperature(chestLocation);
        }

        if (chestLocation.inventory.itemStacks[0].item instanceof IHeatable) {
            IHeatable heatableItem = (IHeatable) chestLocation.inventory.itemStacks[0].item;
            this.increaseTemperature(chestLocation);
            this.decreaseTemperature(chestLocation);

            if (this.currentTemperature < heatableItem.getTargetTemperature()) {
                this.heatStartTime = CosmicEvolution.instance.save.time;
                this.heatingFinishTime = CosmicEvolution.instance.save.time + heatableItem.getCookTime();
            } else {
                if (CosmicEvolution.instance.save.time >= this.heatingFinishTime) {
                    this.cookItem(chunk, heatableItem);
                }
            }
        }

    }

    private void cookItem(Chunk chunk, IHeatable heatableItem) { //Transfer from chest slot 1 to slot 2, if slot 2 is empty change item in slot 2 to correct output item. block cooking if the current output item does not match the expected item
        ChestLocation chestLocation = (ChestLocation) chunk.getBlockState(this.index, MultiState.CHEST_STATE);


        short outputItem = heatableItem.getOutputItem();

        chestLocation.inventory.itemStacks[0].item = Item.list[outputItem];
        chestLocation.inventory.itemStacks[0].count = 1;

        if (Item.list[outputItem] instanceof IDecayItem) {
            chestLocation.inventory.itemStacks[0].decayTime = CosmicEvolution.instance.save.time + ((IDecayItem) Item.list[outputItem]).getDecayTime();
        }

        this.currentTemperature = 0f;

        this.heatStartTime = CosmicEvolution.instance.save.time;
        this.heatingFinishTime = this.heatStartTime + Timer.REAL_MINUTE * 10;

        chunk.markDirty();
    }

}
