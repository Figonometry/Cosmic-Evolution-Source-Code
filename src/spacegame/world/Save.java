package spacegame.world;

import spacegame.core.CosmicEvolution;
import spacegame.gui.GuiInGame;
import spacegame.entity.EntityPlayer;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class Save {
    private CosmicEvolution ce;
    public World activeWorld;
    public EntityPlayer thePlayer;
    public final File saveFolder;
    public final File saveFile;
    public long time;
    public long seed;
    public String dateCreated;
    public String saveName;
    public double spawnX;
    public double spawnY = Double.NEGATIVE_INFINITY;
    public double spawnZ;
    public byte savedSkyLightLevel = 0;
    public boolean loadedFromFile;
    public SaveSettings saveSettings;

    public Save(CosmicEvolution cosmicEvolution, int saveSlotNumber, String saveName, long seed, double x, double z, SaveSettings saveSettings) {
        this.ce = cosmicEvolution;
        this.saveSettings = saveSettings;
        this.saveFolder = new File(this.ce.launcherDirectory + "/saves/save" + saveSlotNumber);
        this.saveFile = new File(this.saveFolder + "/save.dat");
        this.saveFolder.mkdirs();
        Calendar calendar = new GregorianCalendar();
        this.dateCreated =  calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
        try {
            this.saveFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.seed = seed;
        this.saveName = saveName;
        this.savedSkyLightLevel = 15;
        this.spawnX = x;
        this.spawnZ = z;
        this.saveInitDataToFile(saveName, x, z);
    }

    public Save(CosmicEvolution cosmicEvolution, File saveFolder){
        this.ce = cosmicEvolution;
        this.saveFolder = saveFolder;
        this.saveFile = new File(this.saveFolder + "/save.dat");
        File playerFile = new File(this.saveFolder + "/player.dat");
        if(playerFile.exists()) {
            this.thePlayer = new EntityPlayer(this.ce, playerFile, 0, 0 ,0);
        } else {
            this.thePlayer = new EntityPlayer(this.ce, 0, 0, 0);
        }
        this.loadDataFromFile();
        this.loadedFromFile = true;
    }

    public void loadDataFromFile(){
        try {
            FileInputStream inputStream = new FileInputStream(this.saveFile);
            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
            this.time = compoundTag.getCompoundTag("Save").getLong("time");
            this.dateCreated = compoundTag.getCompoundTag("Save").getString("dateCreated");
            this.seed = compoundTag.getCompoundTag("Save").getLong("seed");
            this.saveName = compoundTag.getCompoundTag("Save").getString("saveName");
            this.savedSkyLightLevel = compoundTag.getCompoundTag("Save").getByte("skyLightLevel");
            this.spawnX = compoundTag.getCompoundTag("Save").getDouble("spawnX");
            this.spawnY = compoundTag.getCompoundTag("Save").getDouble("spawnY");
            this.spawnZ = compoundTag.getCompoundTag("Save").getDouble("spawnZ");
            this.saveSettings = new SaveSettings(compoundTag.getCompoundTag("SaveSettings"));
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDataToFile(){
        try {
            FileOutputStream outputStream = new FileOutputStream(this.saveFile);
            NBTTagCompound save = new NBTTagCompound();
            NBTTagCompound saveData = new NBTTagCompound();
            NBTTagCompound saveSettings = new NBTTagCompound();
            save.setTag("Save", saveData);
            save.setTag("SaveSettings", saveSettings);
            saveData.setLong("time", this.time);
            Calendar calendar = new GregorianCalendar();
            saveData.setString("dateLastModified", calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
            saveData.setString("dateCreated", this.dateCreated);
            saveData.setLong("seed", this.seed);
            saveData.setString("saveName", this.saveName);
            saveData.setByte("skyLightLevel", this.activeWorld != null ? this.activeWorld.skyLightLevel : this.savedSkyLightLevel);
            saveData.setDouble("spawnX", this.spawnX);
            saveData.setDouble("spawnY", this.spawnY);
            saveData.setDouble("spawnZ", this.spawnZ);
            this.saveSettings.saveSettingsToFile(saveSettings);
            NBTIO.writeCompressed(save, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(this.activeWorld != null && this.thePlayer != null) {
            this.thePlayer.savePlayerToFile();
            this.activeWorld.saveWorld();
        }
    }

    public void saveDataToFileWithoutChunkUnload(){
        try {
            FileOutputStream outputStream = new FileOutputStream(this.saveFile);
            NBTTagCompound save = new NBTTagCompound();
            NBTTagCompound saveData = new NBTTagCompound();
            NBTTagCompound saveSettings = new NBTTagCompound();
            save.setTag("Save", saveData);
            save.setTag("SaveSettings", saveSettings);
            saveData.setLong("time", this.time);
            Calendar calendar = new GregorianCalendar();
            saveData.setString("dateLastModified", calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
            saveData.setString("dateCreated", this.dateCreated);
            saveData.setLong("seed", this.seed);
            saveData.setString("saveName", this.saveName);
            saveData.setByte("skyLightLevel", this.activeWorld.skyLightLevel);
            saveData.setDouble("spawnX", this.spawnX);
            saveData.setDouble("spawnY", this.spawnY);
            saveData.setDouble("spawnZ", this.spawnZ);
            this.saveSettings.saveSettingsToFile(saveSettings);
            NBTIO.writeCompressed(save, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(this.activeWorld != null && this.thePlayer != null) {
            this.thePlayer.savePlayerToFile();
            this.activeWorld.saveWorldWithoutUnload();
        }
    }

    public void saveInitDataToFile(String saveName, double x, double z){
        try {
            FileOutputStream outputStream = new FileOutputStream(this.saveFile);
            NBTTagCompound save = new NBTTagCompound();
            NBTTagCompound saveData = new NBTTagCompound();
            NBTTagCompound saveSettings = new NBTTagCompound();
            save.setTag("Save", saveData);
            save.setTag("SaveSettings", saveSettings);
            saveData.setLong("time", this.time);
            saveData.setString("saveName", saveName);
            Calendar calendar = new GregorianCalendar();
            saveData.setString("dateLastModified", calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
            saveData.setString("dateCreated", this.dateCreated);
            saveData.setLong("seed", this.seed);
            saveData.setDouble("spawnX", x);
            saveData.setDouble("spawnZ", z);
            this.saveSettings.saveSettingsToFile(saveSettings);
            NBTIO.writeCompressed(save, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void tick() {
        this.activeWorld.tick();
        this.thePlayer.tick();
        this.activeWorld.chunkController.renderWorldScene.hasTickPassed = true;
    }

    public void setActiveWorld(World world) {
        this.activeWorld = world;
        this.activeWorld.skyLightLevel = this.savedSkyLightLevel;
        this.activeWorld.initNoiseMaps();
        this.activeWorld.loadWorld();
    }

    public void handleLeftClick() {
        this.activeWorld.handleClick(true);
        this.thePlayer.isSwinging = this.ce.currentGui instanceof GuiInGame;
    }

    public void handleRightClick() {
        this.activeWorld.handleClick(false);
    }

    public static int doesSaveSlotExist(int saveNumber){
        File save = new File(CosmicEvolution.instance.launcherDirectory + "/saves/save" + saveNumber);
        File savePlayerDat = new File(save + "/player.dat");
        File saveWorldDat = new File(save + "/worlds");
        File saveSaveDat = new File(save + "/save.dat");
        if(saveSaveDat.exists() && saveWorldDat.exists() && savePlayerDat.exists()){
            return 2;
        } else if((!saveSaveDat.exists() && saveWorldDat.exists() && savePlayerDat.exists()) || (saveSaveDat.exists() && !saveWorldDat.exists() && savePlayerDat.exists()) || (saveSaveDat.exists() && saveWorldDat.exists() && !savePlayerDat.exists())){
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean isSaveFileCorrupted(int saveSlot){
        switch (doesSaveSlotExist(saveSlot)) {
            case 0 -> {
                return false;
            }
            case 1 -> {
                return true;
            }
            case 2 -> {
                File file = new File(CosmicEvolution.instance.launcherDirectory + "/saves/save" + saveSlot + "/save.dat");
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                    NBTTagCompound save = compoundTag.getCompoundTag("Save");
                    return save == null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }


}
