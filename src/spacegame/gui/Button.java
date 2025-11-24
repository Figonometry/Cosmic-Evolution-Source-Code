package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.util.MathUtil;
import spacegame.core.MouseListener;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.item.Item;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.*;
import spacegame.world.weather.Cloud;
import spacegame.world.Save;
import spacegame.world.Tech;

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
    private CosmicEvolution ce;
    public static int buttonTextureLoader = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/button.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    public static TextureAtlas buttonTextureAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(256,256, 256, 64, 3, 0);


    public Button(String name, int width, int height, int x, int y, Gui Gui, CosmicEvolution cosmicEvolution){
        this.ce = cosmicEvolution;
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
               this.ce.setNewGui(new GuiWorldSelect(this.ce));
            }
            case MULTI_PLAYER -> {
                //Put effect here later
            }
            case SETTINGS -> {
                if(this.ce.currentGui instanceof GuiMainMenu) {
                    this.ce.setNewGui(new GuiSettingsMainMenu(this.ce));
                } else if(this.ce.currentGui instanceof GuiPauseInGame){
                    this.ce.setNewGui(new GuiSettingsInGame(this.ce));
                }
            }
            case QUIT_GAME -> {
                this.ce.running = false;
            }
            case BUG_REPORT -> {
                if(this.Gui.subMenu)return;
                this.Gui.subMenu2 = !this.Gui.subMenu2;
                this.clicked = !this.clicked;
            }
            case INFORMATION -> {
                if(this.Gui.subMenu2)return;
                this.Gui.subMenu = !this.Gui.subMenu;
                this.clicked = !this.clicked;
            }
            case VIDEO_SETTINGS -> {
                if(this.ce.currentGui instanceof GuiSettingsMainMenu) {
                    this.ce.setNewGui(new GuiVideoSettingsMainMenu(this.ce));
                } else if(this.ce.currentGui instanceof GuiSettingsInGame){
                    this.ce.setNewGui(new GuiVideoSettingsInGame(this.ce));
                }
            }
            case KEYBINDS -> {
                if(this.Gui instanceof GuiSettingsMainMenu) {
                    this.ce.setNewGui(new GuiControlsMainMenu(this.ce));
                } else if(this.Gui instanceof GuiSettingsInGame){
                    this.ce.setNewGui(new GuiControlsInGame(this.ce));
                }
            }
            case BACK_TO_MAIN_MENU -> {
                this.ce.setNewGui(new GuiMainMenu(this.ce));
                Assets.disableBlockTextureArray();
                Assets.disableItemTextureArray();
            }
            case BACK -> {
                if(this.Gui instanceof GuiVideoSettingsMainMenu || this.Gui instanceof GuiControlsMainMenu){
                    this.ce.setNewGui(new GuiSettingsMainMenu(this.ce));
                } else if(this.Gui instanceof GuiSettingsInGame){
                    this.ce.setNewGui(new GuiPauseInGame(this.ce));
                } else if(this.Gui instanceof GuiVideoSettingsInGame || this.Gui instanceof GuiControlsInGame){
                    this.ce.setNewGui(new GuiSettingsInGame(this.ce));
                } else if(this.Gui instanceof GuiWorldSelect){
                    this.ce.setNewGui(new GuiMainMenu(this.ce));
                } else if(this.Gui instanceof GuiCreateNewWorld || this.Gui instanceof GuiRenameWorld){
                    this.ce.setNewGui(new GuiWorldSelect(this.ce));
                }
            }
            case FOV -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.decreaseFOV();
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.increaseFOV();
                }
                CosmicEvolution.camera.adjustProjection(GameSettings.fov, 0.1D);
            }
            case VSYNC -> {
                GameSettings.vsync = !GameSettings.vsync;
                GLFW.glfwSwapInterval(GameSettings.vsync ? 1 : 0);
            }
            case INVERT_MOUSE -> {
                GameSettings.invertMouse = !GameSettings.invertMouse;
            }
            case FULLSCREEN -> {
                GameSettings.fullscreen = !GameSettings.fullscreen;
                this.ce.toggleFullscreen();
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
                    GameSettings.changeHorizontalViewDistance(false);
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.changeHorizontalViewDistance(true);
                }
                if(this.ce.save != null) {
                    this.ce.save.activeWorld.chunkController.resetChunkLoading();
                }
                Shader.terrainShader.uploadFloat("fogDistance", GameSettings.renderDistance * 20f);
                Shader.worldShader2DTexture.uploadFloat("fogDistance", GameSettings.renderDistance * 20f);
            }
            case CHUNK_VIEW_VERTICAL -> {
                if(this.sideOfButtonBeingClicked() == 0){
                    GameSettings.changeVerticalViewDistance(false);
                } else if(this.sideOfButtonBeingClicked() == 1){
                    GameSettings.changeVerticalViewDistance(true);
                }
                if(this.ce.save != null) {
                    this.ce.save.activeWorld.chunkController.resetChunkLoading();
                }
            }
            case SHADOW_MAP -> {
                GameSettings.shadowMap = !GameSettings.shadowMap;
                Shader.terrainShader.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
                Shader.worldShader2DTexture.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
                Shader.worldShaderTextureArray.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
            }
            case VIEW_BOB -> {
                GameSettings.viewBob = !GameSettings.viewBob;
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
                GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                this.ce.save.activeWorld.paused = false;
                this.ce.setNewGui(new GuiInGame(this.ce));
            }
            case QUIT_TO_MAIN_MENU -> {
                this.ce.save.saveDataToFile();
                while (Thread.activeCount() > 3){
                }
                this.ce.save = null;
                this.ce.setNewGui(new GuiMainMenu(this.ce));
                CosmicEvolution.setGLClearColor(0,0,0,0);
                CosmicEvolution.instance.renderEngine.deleteTexture(Cloud.texture);
                CosmicEvolution.instance.renderEngine.deleteTexture(RenderWorldScene.rainTexture);
            }
            case SAVE_1 -> {
                if(this.clicked) {
                    if (this.Gui instanceof GuiWorldSelect) {
                        File save = new File(this.ce.launcherDirectory + "/saves/save1");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.ce.startSave(save);
                            } else {
                                this.ce.setNewGui(new GuiCreateNewWorld(this.ce, 1));
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
                        File save = new File(this.ce.launcherDirectory + "/saves/save2");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.ce.startSave(save);
                            } else {
                                this.ce.setNewGui(new GuiCreateNewWorld(this.ce, 2));
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
                        File save = new File(this.ce.launcherDirectory + "/saves/save3");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.ce.startSave(save);
                            } else {
                                this.ce.setNewGui(new GuiCreateNewWorld(this.ce, 3));
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
                        File save = new File(this.ce.launcherDirectory + "/saves/save4");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.ce.startSave(save);
                            } else {
                                this.ce.setNewGui(new GuiCreateNewWorld(this.ce, 4));
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
                        File save = new File(this.ce.launcherDirectory + "/saves/save5");
                        if (((GuiWorldSelect) this.Gui).deletingFile) {
                            if (save.exists()) {
                                this.deleteDirectory(save);
                            }
                        } else {
                            GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                            if (save.exists()) {
                                this.ce.startSave(save);
                            } else {
                                this.ce.setNewGui(new GuiCreateNewWorld(this.ce, 5));
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
                ((GuiCreateNewWorld)this.Gui).nameWorld.typing = false;
                ((GuiCreateNewWorld)this.Gui).setSeed.typing = false;
                this.ce.startSave(((GuiCreateNewWorld)this.Gui).saveSlot, ((GuiCreateNewWorld)this.Gui).nameWorld.text, ((GuiCreateNewWorld)this.Gui).getSeed());
            }
            case RENAME_WORLD -> {
                File file = new File(this.ce.launcherDirectory + "/saves/save" + ((GuiRenameWorld)this.Gui).saveSlot + "/save.dat");
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
                ((GuiRenameWorld)this.Gui).nameWorld.typing = false;
                this.ce.setNewGui(new GuiWorldSelect(this.ce));
            }
            case WORLD_INFO -> {
                this.ce.setNewGui(new GuiRenameWorld(this.ce, ((GuiWorldSelect)this.Gui).saveSelected()));
            }
            case RESPAWN -> {
                this.ce.save.thePlayer.x = this.ce.save.thePlayer.spawnX + 1.5;
                this.ce.save.thePlayer.y = this.ce.save.thePlayer.spawnY + 1.5;
                this.ce.save.thePlayer.z = this.ce.save.thePlayer.spawnZ;
                this.ce.save.thePlayer.health = this.ce.save.thePlayer.maxHealth;
                GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                this.ce.save.activeWorld.paused = false;
                this.ce.setNewGui(new GuiInGame(this.ce));
            }
            case CRAFT -> {
                GLFW.glfwSetInputMode(this.ce.window,GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                if(this.Gui instanceof GuiCraftingStoneTools) {
                    int x = ((GuiCraftingStoneTools) this.Gui).x;
                    int y = ((GuiCraftingStoneTools) this.Gui).y;
                    int z = ((GuiCraftingStoneTools) this.Gui).z;
                    short outputItem = ((GuiCraftingStoneTools) this.Gui).outputItemID;
                    this.ce.save.activeWorld.setBlockWithNotify(x,y,z, Block.air.ID, false);
                    this.ce.save.activeWorld.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, outputItem, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[outputItem].durability));

                    for(int i = 0; i < ((GuiCraftingStoneTools) this.Gui).activeRecipe.requiredItemCount.length; i++){
                        for(int j = 0; j <  ((GuiCraftingStoneTools) this.Gui).activeRecipe.requiredItemCount[i]; j++){
                            this.ce.save.thePlayer.removeSpecificItemFromInventory(((GuiCraftingStoneTools) this.Gui).activeRecipe.requiredItems[i]);
                        }
                    }

                    this.ce.save.activeWorld.delayWhenExitingUI = 60;
                    Tech.techUpdateEvent(Tech.UPDATE_EVENT_CRAFT_STONE_HAND_TOOL);
                }

                if(this.Gui instanceof GuiCraftingPottery){
                    int x = ((GuiCraftingPottery) this.Gui).x;
                    int y = ((GuiCraftingPottery) this.Gui).y;
                    int z = ((GuiCraftingPottery) this.Gui).z;
                    short outputItem = ((GuiCraftingPottery) this.Gui).outputItemID;
                    short outputBlockID = ((GuiCraftingPottery)this.Gui).outputBlockID;
                    this.ce.save.activeWorld.setBlockWithNotify(x,y,z, Block.air.ID, false);
                    if(outputItem == Item.block.ID){
                        this.ce.save.activeWorld.addEntity(new EntityBlock(x + 0.5, y + 0.5, z + 0.5, outputBlockID, (byte)1));
                    } else {
                        this.ce.save.activeWorld.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, outputItem, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[outputItem].durability));
                    }
                    for(int i = 0; i < ((GuiCraftingPottery) this.Gui).activeRecipe.requiredItemCount.length; i++){
                        for(int j = 0; j <  ((GuiCraftingPottery) this.Gui).activeRecipe.requiredItemCount[i]; j++){
                            this.ce.save.thePlayer.removeSpecificItemFromInventory(((GuiCraftingPottery) this.Gui).activeRecipe.requiredItems[i]);
                        }
                    }
                    this.ce.save.activeWorld.delayWhenExitingUI = 60;
                }

                this.ce.setNewGui(new GuiInGame(this.ce));
            }
            case CLOSE -> {
                GLFW.glfwSetInputMode(this.ce.window,GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                this.ce.setNewGui(new GuiInGame(this.ce));
            }
            case PAGE_LEFT -> {
                if(this.Gui instanceof GuiVideoSettingsMainMenu){
                    ((GuiVideoSettingsMainMenu)this.Gui).page--;
                }
                if(this.Gui instanceof GuiVideoSettingsInGame){
                    ((GuiVideoSettingsInGame)this.Gui).page--;
                }
            }
            case PAGE_RIGHT -> {
                if(this.Gui instanceof GuiVideoSettingsMainMenu){
                    ((GuiVideoSettingsMainMenu)this.Gui).page++;
                }
                if(this.Gui instanceof GuiVideoSettingsInGame){
                    ((GuiVideoSettingsInGame)this.Gui).page++;
                }
            }
            case WAVY_WATER -> {
                GameSettings.wavyWater = !GameSettings.wavyWater;
                Shader.terrainShader.uploadBoolean("wavyWater", GameSettings.wavyWater);
                Block.water.canGreedyMesh = !GameSettings.wavyWater;
            }
            case WAVY_LEAVES -> {
                GameSettings.wavyLeaves = !GameSettings.wavyLeaves;
                Block.leaf.canGreedyMesh = !GameSettings.wavyLeaves;
                Shader.shadowMapShader.uploadBoolean("wavyLeaves", GameSettings.wavyLeaves);
                Shader.terrainShader.uploadBoolean("wavyLeaves", GameSettings.wavyLeaves);
            }
            case TRANSPARENT_LEAVES -> {
                GameSettings.transparentLeaves = !GameSettings.transparentLeaves;
                this.ce.save.activeWorld.chunkController.updateChunksWithLeaves();
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
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
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

        tessellator.addVertex2DTextureWithAtlas(16777215,this.x - this.width/2, this.y - this.height/2, -20,3, buttonTexture, textureID, 255);
        tessellator.addVertex2DTextureWithAtlas(16777215,this.x + this.width/2, this.y + this.height/2, -20,1, buttonTexture, textureID, 255);
        tessellator.addVertex2DTextureWithAtlas(16777215,this.x - this.width/2, this.y + this.height/2, -20,2, buttonTexture, textureID, 255);
        tessellator.addVertex2DTextureWithAtlas(16777215,this.x + this.width/2, this.y - this.height/2, -20,0, buttonTexture, textureID, 255);
        tessellator.addElements();
        tessellator.drawTexture2DWithAtlas(buttonTextureLoader, Shader.screen2DTextureAtlas, CosmicEvolution.camera);
        tessellator.toggleOrtho();
        this.drawCenteredString();

        if((this.name.equals(EnumButtonEffects.SINGLE_PLAYER.name()) || this.name.equals(EnumButtonEffects.QUIT_GAME.name())) && this.active && this.isMouseHoveredOver() || this.name.equals(EnumButtonEffects.SETTINGS.name()) && this.active && this.isMouseHoveredOver() && this.Gui instanceof GuiMainMenu){
            int texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16711680,this.x - 350, this.y - 50, -10,3);
            tessellator.addVertex2DTexture(16711680,this.x - 250, this.y + 50, -10,1);
            tessellator.addVertex2DTexture(16711680,this.x - 350, this.y + 50, -10,2);
            tessellator.addVertex2DTexture(16711680,this.x - 250, this.y -50, -10,0);
            tessellator.addElements();
            tessellator.drawTexture2D(texture, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            CosmicEvolution.instance.renderEngine.deleteTexture(texture);
        }

        if(this.name.equals(EnumButtonEffects.BUG_REPORT.name())){
            int wrench = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/wrench.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16777215,this.x - 30, this.y - 30, -9,3);
            tessellator.addVertex2DTexture(16777215,this.x + 30, this.y + 30, -9,1);
            tessellator.addVertex2DTexture(16777215,this.x - 30, this.y + 30, -9,2);
            tessellator.addVertex2DTexture(16777215,this.x + 30, this.y - 30, -9,0);
            tessellator.addElements();
            tessellator.drawTexture2D(wrench, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            CosmicEvolution.instance.renderEngine.deleteTexture(wrench);
        }

        if(this.name.equals(EnumButtonEffects.INFORMATION.name())){
            int info = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/info.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16711680,this.x - 30, this.y - 30, -9,3);
            tessellator.addVertex2DTexture(16711680,this.x + 30, this.y + 30, -9,1);
            tessellator.addVertex2DTexture(16711680,this.x - 30, this.y + 30, -9,2);
            tessellator.addVertex2DTexture(16711680,this.x + 30, this.y - 30, -9,0);
            tessellator.addElements();
            tessellator.drawTexture2D(info, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            CosmicEvolution.instance.renderEngine.deleteTexture(info);
        }
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - CosmicEvolution.width/2D;
        double y = (MouseListener.instance.yPos - CosmicEvolution.height/2D) * -1;
        float adjustedButtonX = MathUtil.adjustXPosBasedOnScreenWidth(this.x);
        float adjustedButtonY = MathUtil.adjustYPosBasedOnScreenHeight(this.y);
        float adjustedButtonWidth = MathUtil.adjustWidthBasedOnScreenWidth(this.width);
        float adjustedButtonHeight = MathUtil.adjustHeightBasedOnScreenHeight(this.height);
        return x > adjustedButtonX - (double) adjustedButtonWidth / 2 && x < adjustedButtonX + (double) adjustedButtonWidth / 2 && y > adjustedButtonY - (double) adjustedButtonHeight / 2 && y < adjustedButtonY + (double) adjustedButtonHeight / 2;
    }

    public int sideOfButtonBeingClicked(){
        double x = MouseListener.instance.xPos - CosmicEvolution.width/2D;
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
            case SHADOW_MAP -> {
                string = "Shadows: " + GameSettings.shadowMap;
            }
            case VIEW_BOB -> {
                string = "View Bobbing: " + GameSettings.viewBob;
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
                        File file = new File(this.ce.launcherDirectory + "/saves/save" + 1 + "/save.dat");
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
                        File file = new File(this.ce.launcherDirectory + "/saves/save" + 2 + "/save.dat");
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
                        File file = new File(this.ce.launcherDirectory + "/saves/save" + 3 + "/save.dat");
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
                        File file = new File(this.ce.launcherDirectory + "/saves/save" + 4 + "/save.dat");
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
                        File file = new File(this.ce.launcherDirectory + "/saves/save" + 5 + "/save.dat");
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
                string = "i";
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
            case CLOSE -> {
                string = "X";
            }
            case PAGE_LEFT -> {
                string = "<---";
            }
            case PAGE_RIGHT -> {
                string = "--->";
            }
            case WAVY_WATER -> {
                string = "Water Waves: " + GameSettings.wavyWater;
            }
            case WAVY_LEAVES -> {
                string = "Leaf Movement: " + GameSettings.wavyLeaves;
            }
            case TRANSPARENT_LEAVES -> {
                string = "Transparent Leaves: " + GameSettings.transparentLeaves;
            }

            default -> string = "";
        }

        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString(string, this.x, this.y - (this.height/2.5f), -15,16777215, 50, 255);
    }

}

