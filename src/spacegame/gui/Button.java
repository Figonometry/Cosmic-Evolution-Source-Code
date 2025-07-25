package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.GameSettings;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.*;
import spacegame.world.Save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Button {
    public boolean active = true;
    public boolean clickable;
    public boolean clicked;
    public int width;
    public int height;
    public int x;
    public int y;
    public String name;
    private Gui Gui;
    private SpaceGame sg;
    public static TextureLoader buttonTextureLoader = new TextureLoader("src/spacegame/assets/textures/gui/button.png", 256, 256);
    public static TextureAtlas buttonTextureAtlas = new TextureAtlas(buttonTextureLoader, 256, 64, 3, 0);


    public Button(String name, int width, int height, int x, int y, Gui Gui, SpaceGame spaceGame){
        this.sg = spaceGame;
        this.name = name;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.Gui = Gui;
    }

    public void onLeftClick(){
        switch (EnumButtonEffects.valueOf(this.name)){
            case SINGLE_PLAYER -> {
               this.sg.setNewGui(new GuiWorldSelect(this.sg));
            }
            case MULTI_PLAYER -> {
                //Put effect here later
            }
            case SETTINGS -> {
                if(this.sg.currentGui instanceof GuiMainMenu) {
                    this.sg.setNewGui(new GuiSettingsMainMenu(this.sg));
                } else if(this.sg.currentGui instanceof GuiPauseInGame){
                    this.sg.setNewGui(new GuiSettingsInGame(this.sg));
                }
            }
            case QUIT_GAME -> {
                this.sg.running = false;
            }
            case BUG_REPORT -> {
                //Bug
            }
            case INFORMATION -> {
                this.Gui.subMenu = !this.Gui.subMenu;
                this.clicked = !this.clicked;
            }
            case VIDEO_SETTINGS -> {
                if(this.sg.currentGui instanceof GuiSettingsMainMenu) {
                    this.sg.setNewGui(new GuiVideoSettingsMainMenu(this.sg));
                } else if(this.sg.currentGui instanceof GuiSettingsInGame){
                    this.sg.setNewGui(new GuiVideoSettingsInGame(this.sg));
                }
            }
            case KEYBINDS -> {
                if(this.Gui instanceof GuiSettingsMainMenu) {
                    this.sg.setNewGui(new GuiControlsMainMenu(this.sg));
                } else if(this.Gui instanceof GuiSettingsInGame){
                    this.sg.setNewGui(new GuiControlsInGame(this.sg));
                }
            }
            case BACK_TO_MAIN_MENU -> {
                this.sg.setNewGui(new GuiMainMenu(this.sg));
            }
            case BACK -> {
                if(this.Gui instanceof GuiVideoSettingsMainMenu || this.Gui instanceof GuiControlsMainMenu){
                    this.sg.setNewGui(new GuiSettingsMainMenu(this.sg));
                } else if(this.Gui instanceof GuiSettingsInGame){
                    this.sg.setNewGui(new GuiPauseInGame(this.sg));
                } else if(this.Gui instanceof GuiVideoSettingsInGame || this.Gui instanceof GuiControlsInGame){
                    this.sg.setNewGui(new GuiSettingsInGame(this.sg));
                } else if(this.Gui instanceof GuiWorldSelect){
                    this.sg.setNewGui(new GuiMainMenu(this.sg));
                } else if(this.Gui instanceof GuiCreateNewWorld || this.Gui instanceof GuiRenameWorld){
                    this.sg.setNewGui(new GuiWorldSelect(this.sg));
                }
            }
            case FOV -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.decreaseFOV();
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.increaseFOV();
                }
                SpaceGame.camera.adjustProjection(GameSettings.fov, 0.1D);
            }
            case VSYNC -> {
                GameSettings.vsync = !GameSettings.vsync;
                this.sg.vsync = GameSettings.vsync;
                GLFW.glfwSwapInterval(this.sg.vsync ? 1 : 0);
            }
            case INVERT_MOUSE -> {
                GameSettings.invertMouse = !GameSettings.invertMouse;
            }
            case FULLSCREEN -> {
                GameSettings.fullscreen = !GameSettings.fullscreen;
            }
            case MOUSE_SENSITIVITY -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.decreaseSensitivity();
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.increaseSensitivity();
                }
            }
            case VOLUME_SOUNDS -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.decreaseVolume();
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.increaseVolume();
                }
            }
            case VOLUME_MUSIC -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.decreaseMusicVolume();
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.increaseMusicVolume();
                }
            }
            case SHOW_FPS -> {
                GameSettings.showFPS = !GameSettings.showFPS;
            }
            case CHUNK_VIEW_HORIZONTAL -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.renderDistance--;
                    if(GameSettings.renderDistance < 4){
                        GameSettings.renderDistance = 4;
                    }
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.renderDistance++;
                    if(GameSettings.renderDistance > 8){
                        GameSettings.renderDistance = 8;
                    }
                }
                if(this.sg.save != null) {
                    this.sg.save.activeWorld.activeWorldFace.chunkController.resetChunkLoading();
                }
            }
            case CHUNK_VIEW_VERTICAL -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.chunkColumnHeight--;
                    if(GameSettings.chunkColumnHeight < 5){
                        GameSettings.chunkColumnHeight = 5;
                    }
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.chunkColumnHeight++;
                    if(GameSettings.chunkColumnHeight > 10){
                        GameSettings.chunkColumnHeight = 10;
                    }
                }
                if(this.sg.save != null) {
                    this.sg.save.activeWorld.activeWorldFace.chunkController.resetChunkLoading();
                }
            }
            case KEY_FORWARD -> {
                GameSettings.setKeyBeingModified(GameSettings.forwardKey);
            }
            case KEY_BACKWARD -> {
                GameSettings.setKeyBeingModified(GameSettings.backwardKey);
            }
            case KEY_LEFT -> {
                GameSettings.setKeyBeingModified(GameSettings.leftKey);
            }
            case KEY_RIGHT -> {
                GameSettings.setKeyBeingModified(GameSettings.rightKey);
            }
            case KEY_JUMP -> {
                GameSettings.setKeyBeingModified(GameSettings.jumpKey);
            }
            case KEY_INVENTORY -> {
                GameSettings.setKeyBeingModified(GameSettings.inventoryKey);
            }
            case KEY_DROP -> {
                GameSettings.setKeyBeingModified(GameSettings.dropKey);
            }
            case BACK_TO_GAME -> {
                GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                this.sg.save.activeWorld.activeWorldFace.paused = false;
                this.sg.setNewGui(new GuiInGame(this.sg));
            }
            case QUIT_TO_MAIN_MENU -> {
                this.sg.save.saveDataToFile();
                while (Thread.activeCount() > 3){
                }
                this.sg.save = null;
                this.sg.setNewGui(new GuiMainMenu(this.sg));
                SpaceGame.seGLClearColor(0,0,0,0);
            }
            case SAVE_1 -> {
                if(this.clicked) {
                    if (this.Gui instanceof GuiWorldSelect) {
                        File save = new File(this.sg.launcherDirectory + "/saves/save1");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.sg.startSave(save);
                            } else {
                                this.sg.setNewGui(new GuiCreateNewWorld(this.sg, 1));
                            }
                        }
                    }
                } else {
                    this.clicked = true;
                    ((GuiWorldSelect)this.Gui).save2.clicked = false;
                    ((GuiWorldSelect)this.Gui).save3.clicked = false;
                    ((GuiWorldSelect)this.Gui).save4.clicked = false;
                    ((GuiWorldSelect)this.Gui).save5.clicked = false;
                    ((GuiWorldSelect)this.Gui).worldInfo.y = this.y;
                }
            }
            case SAVE_2 -> {
                if(this.clicked) {
                    if (this.Gui instanceof GuiWorldSelect) {
                        File save = new File(this.sg.launcherDirectory + "/saves/save2");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.sg.startSave(save);
                            } else {
                                this.sg.setNewGui(new GuiCreateNewWorld(this.sg, 2));
                            }
                        }
                    }
                } else {
                    this.clicked = true;
                    ((GuiWorldSelect)this.Gui).save1.clicked = false;
                    ((GuiWorldSelect)this.Gui).save3.clicked = false;
                    ((GuiWorldSelect)this.Gui).save4.clicked = false;
                    ((GuiWorldSelect)this.Gui).save5.clicked = false;
                    ((GuiWorldSelect)this.Gui).worldInfo.y = this.y;
                }
            }
            case SAVE_3 -> {
                if(this.clicked) {
                    if (this.Gui instanceof GuiWorldSelect) {
                        File save = new File(this.sg.launcherDirectory + "/saves/save3");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.sg.startSave(save);
                            } else {
                                this.sg.setNewGui(new GuiCreateNewWorld(this.sg, 3));
                            }
                        }
                    }
                } else {
                    this.clicked = true;
                    ((GuiWorldSelect)this.Gui).save1.clicked = false;
                    ((GuiWorldSelect)this.Gui).save2.clicked = false;
                    ((GuiWorldSelect)this.Gui).save4.clicked = false;
                    ((GuiWorldSelect)this.Gui).save5.clicked = false;
                    ((GuiWorldSelect)this.Gui).worldInfo.y = this.y;
                }
            }
            case SAVE_4 -> {
                if(this.clicked) {
                    if (this.Gui instanceof GuiWorldSelect) {
                        File save = new File(this.sg.launcherDirectory + "/saves/save4");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.sg.startSave(save);
                            } else {
                                this.sg.setNewGui(new GuiCreateNewWorld(this.sg, 4));
                            }
                        }
                    }
                } else {
                    this.clicked = true;
                    ((GuiWorldSelect)this.Gui).save1.clicked = false;
                    ((GuiWorldSelect)this.Gui).save2.clicked = false;
                    ((GuiWorldSelect)this.Gui).save3.clicked = false;
                    ((GuiWorldSelect)this.Gui).save5.clicked = false;
                    ((GuiWorldSelect)this.Gui).worldInfo.y = this.y;
                }
            }
            case SAVE_5 -> {
                if(this.clicked) {
                    if (this.Gui instanceof GuiWorldSelect) {
                        File save = new File(this.sg.launcherDirectory + "/saves/save5");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.sg.startSave(save);
                            } else {
                                this.sg.setNewGui(new GuiCreateNewWorld(this.sg, 5));
                            }
                        }
                    }
                } else {
                    this.clicked = true;
                    ((GuiWorldSelect)this.Gui).save1.clicked = false;
                    ((GuiWorldSelect)this.Gui).save2.clicked = false;
                    ((GuiWorldSelect)this.Gui).save3.clicked = false;
                    ((GuiWorldSelect)this.Gui).save4.clicked = false;
                    ((GuiWorldSelect)this.Gui).worldInfo.y = this.y;
                }
            }
            case DELETE -> {
                if(this.Gui instanceof GuiWorldSelect) {
                    if(!((GuiWorldSelect) this.Gui).deletingFile) {
                        ((GuiWorldSelect) this.Gui).deletingFile = true;
                        this.clicked = true;
                    } else {
                        ((GuiWorldSelect) this.Gui).deletingFile = false;
                        this.clicked = false;
                    }
                }
            }
            case CREATE_NEW_WORLD -> {
                ((GuiCreateNewWorld)this.Gui).nameWorld.breakLoop = true;
                ((GuiCreateNewWorld)this.Gui).setSeed.breakLoop = true;
                this.sg.startSave(((GuiCreateNewWorld)this.Gui).saveSlot, ((GuiCreateNewWorld)this.Gui).nameWorld.text, ((GuiCreateNewWorld)this.Gui).getSeed());
            }
            case RENAME_WORLD -> {
                File file = new File(this.sg.launcherDirectory + "/saves/save" + ((GuiRenameWorld)this.Gui).saveSlot + "/save.dat");
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                    long time = compoundTag.getCompoundTag("Save").getLong("time");
                    String dateCreated = compoundTag.getCompoundTag("Save").getString("dateCreated");
                    long seed = compoundTag.getCompoundTag("Save").getLong("seed");
                    inputStream.close();

                    FileOutputStream outputStream = new FileOutputStream(file);
                    NBTTagCompound save = new NBTTagCompound();
                    NBTTagCompound saveData = new NBTTagCompound();
                    save.setTag("Save", saveData);
                    saveData.setString("saveName", ((GuiRenameWorld)this.Gui).nameWorld.text);
                    saveData.setString("dateCreated", dateCreated);
                    saveData.setLong("time", time);
                    saveData.setLong("seed", seed);
                    NBTIO.writeCompressed(save, outputStream);
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ((GuiRenameWorld)this.Gui).nameWorld.breakLoop = true;
                this.sg.setNewGui(new GuiWorldSelect(this.sg));
            }
            case WORLD_INFO -> {
                this.sg.setNewGui(new GuiRenameWorld(this.sg, ((GuiWorldSelect)this.Gui).saveSelected()));
            }
            case RESPAWN -> {
                this.sg.save.thePlayer.x = this.sg.save.spawnX;
                this.sg.save.thePlayer.y = this.sg.save.spawnY;
                this.sg.save.thePlayer.z = this.sg.save.spawnZ;
                this.sg.save.thePlayer.health = this.sg.save.thePlayer.maxHealth;
                GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                this.sg.save.activeWorld.activeWorldFace.paused = false;
                this.sg.setNewGui(new GuiInGame(this.sg));
            }
            case CRAFT -> {
                GLFW.glfwSetInputMode(this.sg.window,GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                if(this.Gui instanceof GuiCraftingStoneTools) {
                    int x = ((GuiCraftingStoneTools) this.Gui).x;
                    int y = ((GuiCraftingStoneTools) this.Gui).y;
                    int z = ((GuiCraftingStoneTools) this.Gui).z;
                    short outputItem = ((GuiCraftingStoneTools) this.Gui).outputItemID;
                    this.sg.save.activeWorld.activeWorldFace.modifyItemID(x, y, z, outputItem);
                    this.sg.save.activeWorld.activeWorldFace.delayWhenExitingUI = 60;
                    if (this.sg.save.thePlayer.inventory.itemStacks[EntityPlayer.selectedInventorySlot].item != null) {
                        this.sg.save.thePlayer.inventory.itemStacks[EntityPlayer.selectedInventorySlot].durability--;
                    }
                    this.sg.setNewGui(new GuiInGame(this.sg));
                }
                if(this.Gui instanceof GuiCraftingStick){
                    GLFW.glfwSetInputMode(this.sg.window,GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    int x = ((GuiCraftingStick)this.Gui).x;
                    int y = ((GuiCraftingStick)this.Gui).y;
                    int z = ((GuiCraftingStick)this.Gui).z;
                    short outputItem = ((GuiCraftingStick)this.Gui).outputItemID;
                    this.sg.save.activeWorld.activeWorldFace.modifyItemID(x,y,z, outputItem);
                    this.sg.save.activeWorld.activeWorldFace.delayWhenExitingUI = 60;
                    if(this.sg.save.thePlayer.inventory.itemStacks[EntityPlayer.selectedInventorySlot].item != null) {
                        this.sg.save.thePlayer.inventory.itemStacks[EntityPlayer.selectedInventorySlot].durability--;
                    }
                    this.sg.setNewGui(new GuiInGame(this.sg));
                }
            }
        }
        GameSettings.saveOptions();
    }

    private void deleteDirectory(File directoryToBeDeleted){
        File[] allContents = directoryToBeDeleted.listFiles();
        File file;
        if(allContents != null){
            for(int i = 0; i < allContents.length; i++){
                file = allContents[i];
                this.deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }

    public void renderButton(){
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        Texture buttonTexture;
        float textureID;
        if(this.active && !this.isMouseHoveredOver()) {
            buttonTexture = buttonTextureAtlas.getTexture(1);
            textureID = 1/16F;
        } else if(this.active && this.isMouseHoveredOver()){
            buttonTexture = buttonTextureAtlas.getTexture(2);
            textureID = 2/16F;
        } else {
            buttonTexture = buttonTextureAtlas.getTexture(0);
            textureID = 0;
        }

        if(this.clicked){
            buttonTexture = buttonTextureAtlas.getTexture(2);
            textureID = 2/16F;
        }

        tessellator.addVertex2DTextureWithAtlas(16777215,this.x - this.width/2, this.y - this.height/2, -20,3, buttonTexture, textureID);
        tessellator.addVertex2DTextureWithAtlas(16777215,this.x + this.width/2, this.y + this.height/2, -20,1, buttonTexture, textureID);
        tessellator.addVertex2DTextureWithAtlas(16777215,this.x - this.width/2, this.y + this.height/2, -20,2, buttonTexture, textureID);
        tessellator.addVertex2DTextureWithAtlas(16777215,this.x + this.width/2, this.y - this.height/2, -20,0, buttonTexture, textureID);
        tessellator.addElements();
        tessellator.drawTexture2DWithAtlas(buttonTextureLoader.texID, Shader.screen2DTextureAtlas, SpaceGame.camera);
        tessellator.toggleOrtho();
        this.drawCenteredString();

        if((this.name.equals(EnumButtonEffects.SINGLE_PLAYER.name()) || this.name.equals(EnumButtonEffects.QUIT_GAME.name())) && this.active && this.isMouseHoveredOver() || this.name.equals(EnumButtonEffects.SETTINGS.name()) && this.active && this.isMouseHoveredOver() && this.Gui instanceof GuiMainMenu){
            TextureLoader texture = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/star.png", 64,  64);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16711680,this.x - 350, this.y - 50, -10,3);
            tessellator.addVertex2DTexture(16711680,this.x - 250, this.y + 50, -10,1);
            tessellator.addVertex2DTexture(16711680,this.x - 350, this.y + 50, -10,2);
            tessellator.addVertex2DTexture(16711680,this.x - 250, this.y -50, -10,0);
            tessellator.addElements();
            tessellator.drawTexture2D(texture.texID, Shader.screen2DTexture, SpaceGame.camera);
            tessellator.toggleOrtho();
            GL46.glDeleteTextures(texture.texID);
        }

        if(this.name.equals(EnumButtonEffects.BUG_REPORT.name())){
            TextureLoader wrench = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/wrench.png", 64,  64);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16777215,this.x - 30, this.y - 30, -9,3);
            tessellator.addVertex2DTexture(16777215,this.x + 30, this.y + 30, -9,1);
            tessellator.addVertex2DTexture(16777215,this.x - 30, this.y + 30, -9,2);
            tessellator.addVertex2DTexture(16777215,this.x + 30, this.y - 30, -9,0);
            tessellator.addElements();
            tessellator.drawTexture2D(wrench.texID, Shader.screen2DTexture, SpaceGame.camera);
            tessellator.toggleOrtho();
            GL46.glDeleteTextures(wrench.texID);
        }

        if(this.name.equals(EnumButtonEffects.INFORMATION.name())){
            TextureLoader info = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/info.png", 64,  64);
            TextureAtlas infoAtlas = new TextureAtlas(info, 64,64,1,0);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16711680,this.x - 30, this.y - 30, -9,3);
            tessellator.addVertex2DTexture(16711680,this.x + 30, this.y + 30, -9,1);
            tessellator.addVertex2DTexture(16711680,this.x - 30, this.y + 30, -9,2);
            tessellator.addVertex2DTexture(16711680,this.x + 30, this.y - 30, -9,0);
            tessellator.addElements();
            tessellator.drawTexture2D(info.texID, Shader.screen2DTexture, SpaceGame.camera);
            tessellator.toggleOrtho();
            GL46.glDeleteTextures(info.texID);
        }
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }

    public int sideOfButtonBeingClicked(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        if(x < this.x){
            return 0;
        } else {
            return 1;
        }
    }

    public void drawCenteredString(){
        String string = "";
        switch (EnumButtonEffects.valueOf(this.name)){
            case SINGLE_PLAYER -> {
                string = "Singleplayer";
            }
            case MULTI_PLAYER -> {
                string = "Multiplayer";
            }
            case SETTINGS ->  {
                string = "Settings";
            }
            case QUIT_GAME -> {
                string = "Quit Game";
            }
            case VIDEO_SETTINGS -> {
                string = "Video Settings";
            }
            case KEYBINDS -> {
                string = "Controls";
            }
            case BACK_TO_MAIN_MENU -> {
                string = "Back To Main Menu";
            }
            case BACK -> {
                string = "Back";
            }
            case VSYNC -> {
                string = "VSync: " + GameSettings.vsync;
            }
            case SHOW_FPS -> {
                string = "Show FPS: " + GameSettings.showFPS;
            }
            case INVERT_MOUSE -> {
                string = "Invert Mouse: " + GameSettings.invertMouse;
            }
            case FULLSCREEN -> {
                string = "Fullscreen: " + GameSettings.fullscreen;
            }
            case FOV -> {
                string = "- FOV: " + (int)Math.floor(GameSettings.fov * 100F) + "% +";
            }
            case MOUSE_SENSITIVITY -> {
                string = "- Mouse Sensitivity: " + (int)Math.floor(GameSettings.sensitivity * 100F) + "% +";
            }
            case VOLUME_SOUNDS -> {
                string = "- Sound Volume: " + (int)Math.floor(GameSettings.volume * 100F) + "% +";
            }
            case VOLUME_MUSIC -> {
                string = "- Music Volume: " + (int)Math.floor(GameSettings.musicVolume * 100F) + "% +";
            }
            case CHUNK_VIEW_HORIZONTAL -> {
                string = "- Horizontal View: " + GameSettings.renderDistance + " +";
            }
            case CHUNK_VIEW_VERTICAL -> {
                string = "- Vertical View: " + GameSettings.chunkColumnHeight + " +";
            }
            case KEY_FORWARD -> {
                string = "Forward: " + GameSettings.forwardKey.key;
            }
            case KEY_BACKWARD -> {
                string = "Backward: " + GameSettings.backwardKey.key;
            }
            case KEY_LEFT -> {
                string = "Left: " + GameSettings.leftKey.key;
            }
            case KEY_RIGHT -> {
                string = "Right: " + GameSettings.rightKey.key;
            }
            case KEY_JUMP -> {
                string = "Jump: " + GameSettings.jumpKey.key;
            }
            case KEY_INVENTORY -> {
                string = "Inventory: " + GameSettings.inventoryKey.key;
            }
            case KEY_DROP -> {
                string = "Drop: " + GameSettings.dropKey.key;
            }
            case BACK_TO_GAME -> {
                string = "Back To Game";
            }
            case QUIT_TO_MAIN_MENU -> {
                string = "Quit To Main Menu";
            }
            case SAVE_1 -> {
                string = "Save Slot 1";
                switch (Save.doesSaveSlotExist(1)) {
                    case 0 -> string = string + " - Empty";
                    case 1 -> string = string + " - MISSING FILES";
                    case 2 -> {
                        File file = new File(this.sg.launcherDirectory + "/saves/save" + 1 + "/save.dat");
                        try {
                            FileInputStream inputStream = new FileInputStream(file);
                            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                            string = compoundTag.getCompoundTag("Save").getString("saveName");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            case SAVE_2 -> {
                string = "Save Slot 2";
                switch (Save.doesSaveSlotExist(2)) {
                    case 0 -> string = string + " - Empty";
                    case 1 -> string = string + " - MISSING FILES";
                    case 2 -> {
                        File file = new File(this.sg.launcherDirectory + "/saves/save" + 2 + "/save.dat");
                        try {
                            FileInputStream inputStream = new FileInputStream(file);
                            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                            string = compoundTag.getCompoundTag("Save").getString("saveName");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            case SAVE_3 -> {
                string = "Save Slot 3";
                switch (Save.doesSaveSlotExist(3)) {
                    case 0 -> string = string + " - Empty";
                    case 1 -> string = string + " - MISSING FILES";
                    case 2 -> {
                        File file = new File(this.sg.launcherDirectory + "/saves/save" + 3 + "/save.dat");
                        try {
                            FileInputStream inputStream = new FileInputStream(file);
                            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                            string = compoundTag.getCompoundTag("Save").getString("saveName");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            case SAVE_4 -> {
                string = "Save Slot 4";
                switch (Save.doesSaveSlotExist(4)) {
                    case 0 -> string = string + " - Empty";
                    case 1 -> string = string + " - MISSING FILES";
                    case 2 -> {
                        File file = new File(this.sg.launcherDirectory + "/saves/save" + 4 + "/save.dat");
                        try {
                            FileInputStream inputStream = new FileInputStream(file);
                            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                            string = compoundTag.getCompoundTag("Save").getString("saveName");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            case SAVE_5 -> {
                string = "Save Slot 5";
                switch (Save.doesSaveSlotExist(5)) {
                    case 0 -> string = string + " - Empty";
                    case 1 -> string = string + " - MISSING FILES";
                    case 2 -> {
                        File file = new File(this.sg.launcherDirectory + "/saves/save" + 5 + "/save.dat");
                        try {
                            FileInputStream inputStream = new FileInputStream(file);
                            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
                            string = compoundTag.getCompoundTag("Save").getString("saveName");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            case DELETE -> {
                string = "Delete Save Slot";
            }
            case WORLD_INFO -> {
                string = " i";
            }
            case CREATE_NEW_WORLD -> {
                string = "Create Save";
            }
            case RENAME_WORLD -> {
                string = "Rename Save";
            }
            case RESPAWN -> {
                string = "Respawn";
            }
            case CRAFT -> {
                string = "Craft";
            }

            default -> string = "";
        }

        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString(string, this.x - 25, y - 25, 16777215);
    }

}

