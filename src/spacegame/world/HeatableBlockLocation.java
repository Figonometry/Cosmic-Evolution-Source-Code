package spacegame.world;

import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.item.IFuel;
import spacegame.item.IHeatable;
import spacegame.item.Item;

public final class HeatableBlockLocation { //Trigger every 15 ticks, also is a "chest" slot 0 is fuel, slot 1 is input, slot 2 is output
    public short index;
    public float currentTemperature;
    public short currentFuelBurning;
    public long fuelBurnoutTime;
    public long heatingFinishTime;
    public boolean heating;
    public long fuelStartTime;
    public long heatStartTime;
    public HeatableBlockLocation(short index){
        this.index = index;
    }

    private void consumeFuel(IFuel fuel, Chunk chunk){
        ChestLocation location = chunk.getChestLocation(this.index);

        if(location.inventory.itemStacks[0].item != null) {
            this.currentFuelBurning = fuel.getFuelItemID();
            this.fuelBurnoutTime = CosmicEvolution.instance.save.time + fuel.getBurnTime();
            this.fuelStartTime = CosmicEvolution.instance.save.time;
            this.heating = true;
            location.inventory.itemStacks[0].count--;
            if (location.inventory.itemStacks[0].count <= 0) {
                location.inventory.itemStacks[0].item = null;
            }
        }
    }

    private void increaseTemperature(ChestLocation chestLocation){
        if(Item.list[this.currentFuelBurning] instanceof IFuel && chestLocation.inventory.itemStacks[1].item != null) {
            if (this.currentTemperature >= ((IFuel) Item.list[this.currentFuelBurning]).getHeatingTemperature()) return;

            this.currentTemperature += 2.5f;
        }
    }

    private void decreaseTemperature(ChestLocation chestLocation){
        if(this.heating && chestLocation.inventory.itemStacks[1].item != null)return;

        this.currentTemperature -= 2.5f;
    }

    public void heatItem(Chunk chunk){
        this.heating = CosmicEvolution.instance.save.time < this.fuelBurnoutTime;
        ChestLocation chestLocation = chunk.getChestLocation(this.index);
        if(chestLocation == null)return;

        if(chestLocation.inventory.itemStacks[1].item == null){
            this.currentTemperature = 0f;
            this.heatStartTime = CosmicEvolution.instance.save.time;
            this.heatingFinishTime = this.heatStartTime + Timer.REAL_MINUTE * 10;
            this.decreaseTemperature(chestLocation);
        }

        if(chestLocation.inventory.itemStacks[1].item instanceof IHeatable) {
            IHeatable heatableItem = (IHeatable) chestLocation.inventory.itemStacks[1].item;
            if (!this.heating) {
                this.consumeFuel((IFuel) chestLocation.inventory.itemStacks[0].item, chunk);
            }
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

    private void cookItem(Chunk chunk, IHeatable heatableItem){ //Transfer from chest slot 1 to slot 2, if slot 2 is empty change item in slot 2 to correct output item. block cooking if the current output item does not match the expected item
        ChestLocation chestLocation = chunk.getChestLocation(this.index);


        short outputItem = heatableItem.getOutputItem();

        if(chestLocation.inventory.itemStacks[2].item == null || chestLocation.inventory.itemStacks[2].item.ID == outputItem){

            if(chestLocation.inventory.itemStacks[2].item == null){
                chestLocation.inventory.itemStacks[2].item = Item.list[outputItem];
                chestLocation.inventory.itemStacks[2].count = 0;
            }

            if(chestLocation.inventory.itemStacks[1].item != null) {
                if (chestLocation.inventory.itemStacks[2].count < chestLocation.inventory.itemStacks[2].item.stackLimit) {
                    chestLocation.inventory.itemStacks[1].count--;
                    chestLocation.inventory.itemStacks[2].count++;

                    if (chestLocation.inventory.itemStacks[1].count <= 0) {
                        chestLocation.inventory.itemStacks[1].item = null;
                    }
                }
            }
        }

        this.currentTemperature = 0f;

        this.heatStartTime = CosmicEvolution.instance.save.time;
        this.heatingFinishTime = this.heatStartTime + Timer.REAL_MINUTE * 10;
    }

}
