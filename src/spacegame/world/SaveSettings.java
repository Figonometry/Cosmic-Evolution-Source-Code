package spacegame.world;

import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class SaveSettings {
    public boolean dropInventoryOnDeath = true;
    public boolean testingMode = false;


    public SaveSettings(NBTTagCompound saveOptions){
        this.dropInventoryOnDeath = saveOptions.getBoolean("dropInventoryOnDeath");
        this.testingMode = saveOptions.getBoolean("testingMode");
    }

    public SaveSettings(){}

    public void saveSettingsToFile(NBTTagCompound saveOptions){
        saveOptions.setBoolean("dropInventoryOnDeath", this.dropInventoryOnDeath);
        saveOptions.setBoolean("testingMode", this.testingMode);
    }

    public void changeDropInventoryOnDeath(boolean value){
        this.dropInventoryOnDeath = value;
    }

    public void changeTestingMode(boolean value){
        this.testingMode = value;
    }

    public static SaveSettings loadSaveSettingsFromSaveFile(File saveFolder){
        File saveFile = new File(saveFolder + "/save.dat");

        try {
            FileInputStream inputStream = new FileInputStream(saveFile);
            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);

            return new SaveSettings(compoundTag.getCompoundTag("SaveSettings"));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
