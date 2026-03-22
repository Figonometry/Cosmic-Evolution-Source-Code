package spacegame.world;

import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class SaveSettings {
    public boolean dropInventoryOnDeath = true;


    public SaveSettings(NBTTagCompound saveOptions){
        this.dropInventoryOnDeath = saveOptions.getBoolean("dropInventoryOnDeath");
    }

    public SaveSettings(){}

    public void saveSettingsToFile(NBTTagCompound saveOptions){
        saveOptions.setBoolean("dropInventoryOnDeath", this.dropInventoryOnDeath);
    }

    public void changeDropInventoryOnDeath(boolean value){
        this.dropInventoryOnDeath = value;
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
