package spacegame.world;

import spacegame.core.SpaceGame;
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
    private SpaceGame sg;
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

    public Save(SpaceGame spaceGame, int saveSlotNumber, String saveName, long seed, double x, double z) {
        this.sg = spaceGame;
        this.saveFolder = new File(this.sg.launcherDirectory + "/saves/save" + saveSlotNumber);
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

    public Save(SpaceGame spaceGame, File saveFolder){
        this.sg = spaceGame;
        this.saveFolder = saveFolder;
        this.saveFile = new File(this.saveFolder + "/save.dat");
        File playerFile = new File(this.saveFolder + "/player.dat");
        if(playerFile.exists()) {
            this.thePlayer = new EntityPlayer(this.sg, playerFile, 0, 0 ,0);
        } else {
            this.thePlayer = new EntityPlayer(this.sg, 0, 0, 0);
        }
        this.loadDataFromFile();
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
            save.setTag("Save", saveData);
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
            NBTIO.writeCompressed(save, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.thePlayer.savePlayerToFile();
        this.activeWorld.saveWorld();
    }

    public void saveDataToFileWithoutChunkUnload(){
        try {
            FileOutputStream outputStream = new FileOutputStream(this.saveFile);
            NBTTagCompound save = new NBTTagCompound();
            NBTTagCompound saveData = new NBTTagCompound();
            save.setTag("Save", saveData);
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
            NBTIO.writeCompressed(save, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.thePlayer.savePlayerToFile();
        this.activeWorld.saveWorldWithoutUnload();
    }

    public void saveInitDataToFile(String saveName, double x, double z){
        try {
            FileOutputStream outputStream = new FileOutputStream(this.saveFile);
            NBTTagCompound save = new NBTTagCompound();
            NBTTagCompound saveData = new NBTTagCompound();
            save.setTag("Save", saveData);
            saveData.setLong("time", this.time);
            saveData.setString("saveName", saveName);
            Calendar calendar = new GregorianCalendar();
            saveData.setString("dateLastModified", calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
            saveData.setString("dateCreated", this.dateCreated);
            saveData.setLong("seed", this.seed);
            saveData.setDouble("spawnX", x);
            saveData.setDouble("spawnZ", z);
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
    }

    public void handleLeftClick() {
        this.activeWorld.handleLeftClick();
        this.thePlayer.isSwinging = true;
    }

    public void handleRightClick() {
        this.activeWorld.handleRightClick();
    }

    public static int doesSaveSlotExist(int saveNumber){
        File save = new File(SpaceGame.instance.launcherDirectory + "/saves/save" + saveNumber);
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

}
