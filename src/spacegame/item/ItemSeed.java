package spacegame.item;

import spacegame.world.blockstate.Crop;

public final class ItemSeed extends Item {
    public ItemSeed(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }


    public String getCropName(){
        switch (this.itemName){
            case "SEED_WILD_GRASS" -> {
                return Crop.wildGrass.name;
            }
            case "SEED_EINKORN_WHEAT" -> {
                return Crop.einkornWheat.name;
            }
        }
        throw new IllegalArgumentException("There is no crop that corresponds to the item name: " + this.itemName);
    }


}
