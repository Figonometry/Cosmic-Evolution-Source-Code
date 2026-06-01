package spacegame.world.blockstate;

import spacegame.core.Timer;
import spacegame.gui.RecipeSelector;
import spacegame.item.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Crop {
    public static ConcurrentHashMap<String, Crop> cropMap = new ConcurrentHashMap<>();
    public static final Crop wildGrass = new Crop("wildGrass", 0.5f, 0.5f, 0.5f, 0.5f, 8,
            Timer.GAME_DAY * 14, new short[]{Item.seedWildGrass.ID}, null, new int[]{1}, "Wild Grass", null);
    public static final Crop einkornWheat = new Crop("einkornWheat", 0.5f, 0.5f, 0.5f, 0.5f, 8,
            Timer.GAME_DAY * 14, new short[]{Item.seedEinkornWheat.ID, Item.einkornWheat.ID}, null, new int[]{1, 1}, "Einkorn Wheat", new Crop[]{wildGrass});
    public String name;
    public String displayName;
    public float potassiumThreshold;
    public float nitrogenThreshold;
    public float phosphorusThreshold;
    public float moistureThreshold;
    public long totalGrowthTime;
    public int maxStages;
    public short[] finishedItemIDs;
    public short[] finishedBlockIDs;
    public int[] finishedItemQuantities;
    public Crop[] domesticatedFrom;
    public Crop(String name, float potassiumThreshold, float nitrogenThreshold, float phosphorusThreshold,
                float moistureThreshold, int maxStages, long totalGrowthTime, short[] finishedItemIDs, short[] finishedBlockIDs, int[] finishedItemQuantities,
                String displayName, Crop[] domesticatedFrom){
        this.name = name;
        this.potassiumThreshold = potassiumThreshold;
        this.nitrogenThreshold = nitrogenThreshold;
        this.phosphorusThreshold = phosphorusThreshold;
        this.moistureThreshold = moistureThreshold;
        this.maxStages = maxStages;
        this.totalGrowthTime = totalGrowthTime;
        this.finishedItemIDs = finishedItemIDs;
        this.finishedBlockIDs = finishedBlockIDs;
        this.finishedItemQuantities = finishedItemQuantities;
        this.displayName = displayName;
        this.domesticatedFrom = domesticatedFrom;

        cropMap.put(this.name, this);
    }

    public static Crop getCropFromName(String name){
      return cropMap.get(name);
    }

    public void addAllDomesticatableCropsWithOnlyThisAsInput(ArrayList<RecipeSelector> recipeSelectors){
        Iterator<Map.Entry<String, Crop>> iterator = cropMap.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Crop> crop = iterator.next();
            if(crop != null){
                Crop thisCrop = crop.getValue();

                if(thisCrop.domesticatedFrom != null){

                    if(thisCrop.domesticatedFrom.length == 1){

                        if(thisCrop.domesticatedFrom[0].equals(this)){

                            recipeSelectors.add(new RecipeSelector(thisCrop.finishedItemIDs[0], 0,0,0,0, thisCrop.displayName,
                                    null, null, null));

                        }

                    }

                }

            }
        }
    }

    public void addAllDomesticatableCropsWithTwoInputs(ArrayList<RecipeSelector> recipeSelectors, Crop inputCrop){
        Iterator<Map.Entry<String, Crop>> iterator = cropMap.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Crop> crop = iterator.next();
            if(crop != null){
                Crop thisCrop = crop.getValue();

                if(thisCrop.domesticatedFrom != null){

                    if(thisCrop.domesticatedFrom.length == 2){

                        if(thisCrop.domesticatedFrom[0].equals(this) && thisCrop.domesticatedFrom[0].equals(inputCrop)){

                            recipeSelectors.add(new RecipeSelector(thisCrop.finishedItemIDs[0], 0,0,0,0, thisCrop.displayName,
                                    null, null, null));

                        }

                    }

                }

            }
        }
    }






}
