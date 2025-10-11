package spacegame.core;

import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;
import spacegame.celestial.Sun;
import spacegame.celestial.Universe;
import spacegame.entity.Entity;
import spacegame.entity.EntityDeer;
import spacegame.entity.EntityPlayer;
import spacegame.gui.*;
import spacegame.item.Item;
import spacegame.item.ItemStack;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.Assets;
import spacegame.render.Camera;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Save;
import spacegame.world.Tech;
import spacegame.world.World;
import spacegame.world.WorldEarth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public final class SpaceGame implements Runnable {
    public static SpaceGame instance;
    public final File launcherDirectory;
    public static final boolean DEBUG_MODE = false;
    public static final Random globalRand = new Random();
    public volatile boolean running;
    public String title;
    public boolean vsync;
    public static int width = 1920;
    public static int height = 1080;
    public long window;
    public static Camera camera;
    public long fps;
    public final Timer timer = new Timer(60F);
    public Gui currentGui;
    public Save save;
    public static String imageFallbackPath;
    public TextField currentlySelectedField;
    public MoveableObject currentlySelectedMoveableObject;
    public Universe everything;
    public RenderEngine renderEngine;


    public static void main(String[] args) {
        if(args.length != 0) {
            startMainThread(new Thread(new SpaceGame(args[0])));
        } else {
            System.out.println("Incorrect number of arguments");
        }
    }

    private static void startMainThread(Thread mainThread) {
        mainThread.setName("Cosmic Evolution Main Thread");
        mainThread.setPriority(10);
        mainThread.start();
    }

    private SpaceGame(String launcherDirectory) {
        if(instance != null){
            throw new RuntimeException("Main Class already initialized");
        }
        this.launcherDirectory = new File(launcherDirectory);
        GameSettings.loadOptionsFromFile(this.launcherDirectory);
        this.vsync = GameSettings.vsync;
        instance = this;
    }

    @Override
    public void run() {
        this.running = true;
        try {
            this.startGame();
        } catch(Exception exception){
            new Logger(exception);
            if(this.save != null){
                this.save.saveDataToFile();
                this.save.activeWorld.saveWorld();
                this.save.thePlayer.savePlayerToFile();
            }
            this.shutdown();
        }
    }

    private void startGame() {
        this.title = "Cosmic Evolution Alpha v0.26";
        this.clearLogFiles(new File(this.launcherDirectory + "/crashReports"));
        this.initLWJGL();
        this.renderEngine = new RenderEngine();
        this.initAllBufferObjects();
        this.initAllGlobalAssets();
        this.initAllGlobalObjects();
        this.setAllGlobalShaderUniforms();
        this.mainLoop();
    }

    private void clearLogFiles(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (int i = 0; i < allContents.length; i++) {
                allContents[i].delete();
            }
        }
    }

    private void initLWJGL() {
        GLFWErrorCallback.createPrint(System.err);

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        imageFallbackPath = "src/spacegame/assets/textures/fallback.png";
        File imageFallback = new File(imageFallbackPath);
        if (!imageFallback.exists()) {
            throw new RuntimeException("Missing fallback image at: " + imageFallbackPath);
        }

        this.window = this.createWindow();

        int[] windowWidth = new int[1];
        int[] windowHeight = new int[1];
        GLFW.glfwGetWindowSize(this.window, windowWidth,windowHeight);
        width = windowWidth[0];
        height = windowHeight[0];

        if (this.window == MemoryUtil.NULL) {
            System.out.println("Failed to create game window");
        }

        GLFW.glfwSetCursor(this.window, GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
        GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GLFW.glfwSetCursorPosCallback(this.window, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(this.window, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(this.window, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(this.window, KeyListener::keyCallback);


        GLFW.glfwMakeContextCurrent(this.window);

        GLFW.glfwSwapInterval(this.vsync ? 1 : 0);

        GLFW.glfwShowWindow(this.window);

        GL.createCapabilities();

        Shader.terrainShader.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
        Shader.worldShader2DTexture.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
        Shader.worldShaderTextureArray.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);

        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glDepthFunc(GL46.GL_LESS);

        GL46.glClearColor(0,0,0,0);

        long device = ALC11.alcOpenDevice((ByteBuffer) null);

        if (device != MemoryUtil.NULL) {
            long context = ALC11.alcCreateContext(device, (IntBuffer) null);
            if (context != MemoryUtil.NULL) {
                ALC11.alcMakeContextCurrent(context);
                AL.createCapabilities(ALC.createCapabilities(device));
            }
        }

        GLFW.glfwSetWindowSizeCallback(this.window, WindowResizeListener::resizeCallback);
        this.setWindowIcon();
    }


    private void setWindowIcon() {
        String filepath = "src/spacegame/assets/textures/icon.png";
        int imageWidth = 32;
        int imageHeight = 32;
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 4);
        GLFWImage image1 = GLFWImage.malloc();
        GLFWImage.Buffer icon = GLFWImage.malloc(1);
        assert image != null;
        image1.set(imageWidth, imageHeight, image);
        icon.put(0, image1);
        GLFW.glfwSetWindowIcon(this.window, icon);
        STBImage.stbi_image_free(image);
        image1.free();
        icon.free();
    }

    private void initAllGlobalObjects(){
        this.everything = new Universe();
        camera = new Camera(new Vector3d(), this, 0.1D);
        GuiUniverseMap.universeCamera = new Camera(new Vector3d(), this, 0.0000000000000000001D);
        GuiUniverseMap.universeCamera.setFarPlaneDistance(512);
        GuiUniverseMap.universeCamera.viewMatrix.translate(-0.000000000000000001, 0, 0);
        this.setNewGui(new GuiMainMenu(this));
        Tech.loadEraBaseNodes();
    }

    private void mainLoop() {
        long lastTime = System.nanoTime();
        byte fpsTimer = 30;
        try {
            while (this.running) {
                this.timer.advanceTime();
                for (int i = 0; i < this.timer.ticks; i++) {
                    this.tick();
                    fpsTimer--;
                    if (fpsTimer <= 0) {
                        this.fps = (int) (1000000000.0 / (lastTime - System.nanoTime()));
                        fpsTimer = 30;
                    }
                }
                lastTime = System.nanoTime();
                this.framePulse();
                this.render();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.shutdown();
        System.exit(0);
    }

    public void startSave(int saveSlotNumber, String saveName, long seed) {
        Random rand = new Random(seed);
        double x = rand.nextInt(1024, 32768) + 0.5D;
        double z = rand.nextInt(1024, 32768) + 0.5D;
        this.save = new Save(this, saveSlotNumber, saveName, seed, x, z);
        this.save.thePlayer = new EntityPlayer(this, 0, 0, 0);
        this.save.thePlayer.setPlayerActualPos(x,2, z);
        this.save.setActiveWorld(new WorldEarth(this, 4006976));
        this.setNewGui(new GuiWorldLoadingScreen(this));
        this.save.activeWorld.paused = true;
    }

    public void startSave(File saveFile) {
        this.save = new Save(this, saveFile);
        double x = 0;
        double y = 0;
        double z = 0;
        try {
            FileInputStream inputStream = new FileInputStream(this.save.thePlayer.playerFile);
            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
            NBTTagCompound player = compoundTag.getCompoundTag("Player");
            x = player.getDouble("x");
            y = player.getDouble("y");
            z = player.getDouble("z");
        } catch (IOException e){
            e.printStackTrace();
        }
        this.save.thePlayer.setPlayerActualPos(x, y, z); //This needs to read and set the position using the player's actual location read from file
        this.save.setActiveWorld(new WorldEarth(this, 4006976));
        this.setNewGui(new GuiWorldLoadingScreen(this));
        this.save.activeWorld.paused = true;
    }

    private void tick() {
        if(this.save != null) {
            this.save.tick();
            if(this.save.time % 18000 == 0 && this.currentGui instanceof GuiInGame){
                this.save.saveDataToFileWithoutChunkUnload();
                this.save.thePlayer.savePlayerToFile();
            }
            if(this.everything != null){
                this.everything.updateCelestialObjects();
            }

            if(this.currentGui instanceof GuiAction){
                MoveableObject object = ((GuiAction)this.currentGui).getHoveredObject();
                if(object != null) {
                    this.currentlySelectedMoveableObject = object;
                    if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                        if (!this.currentlySelectedMoveableObject.pickedUp) {
                            this.currentlySelectedMoveableObject.staringX = this.currentlySelectedMoveableObject.x;
                            this.currentlySelectedMoveableObject.startingY = this.currentlySelectedMoveableObject.y;
                            this.currentlySelectedMoveableObject.timePickedUp = this.save.time;
                            this.currentlySelectedMoveableObject.pickedUp = true;
                        }
                        float x = (float) (MouseListener.instance.xPos - SpaceGame.width / 2D);
                        float y = (float) ((MouseListener.instance.yPos - SpaceGame.height / 2D) * -1);
                        this.currentlySelectedMoveableObject.x = x;
                        this.currentlySelectedMoveableObject.y = y;
                    } else {
                        this.currentlySelectedMoveableObject.pickedUp = false;
                    }
                }
            }
        }
        camera.setFrustum();
        GuiUniverseMap.universeCamera.setFrustum();
        this.incrementPlayerDamageTilt();
        this.processInput();
    }

    private void processInput() {
        GameSettings.switchKeyBinds();
        this.checkKeyBindStates();

        if(this.currentGui instanceof GuiUniverseMap) {
            if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK) && KeyListener.keyReleased[GLFW.GLFW_KEY_CAPS_LOCK]) {
                ((GuiUniverseMap)this.currentGui).switchObject();
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_CAPS_LOCK);
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK) && KeyListener.keyReleased[GLFW.GLFW_KEY_CAPS_LOCK]){
            KeyListener.capsLockEnabled = !KeyListener.capsLockEnabled;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_CAPS_LOCK);
        }

     //  if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_G) && KeyListener.keyReleased[GLFW.GLFW_KEY_G]){
     //      if(this.save != null){
     //          if(this.save.thePlayer != null && this.save.activeWorld != null){
     //              EntityDeer deer =  new EntityDeer(this.save.thePlayer.x,this.save.thePlayer.y - 0.5, this.save.thePlayer.z, false, false);
     //              this.save.activeWorld.addEntity(deer);
     //          }
     //      }
     //      KeyListener.setKeyReleased(GLFW.GLFW_KEY_G);
     //  }

     //   if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_MINUS)){
     //       this.save.time -= 216000;
     //       KeyListener.setKeyReleased(GLFW.GLFW_KEY_MINUS);
     //   }
     //   if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_EQUAL)){
     //       this.save.time += 216000;
     //       KeyListener.setKeyReleased(GLFW.GLFW_KEY_EQUAL);
     //   }
     //   if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_COMMA)){
     //       this.save.time -= 1000;
     //       KeyListener.setKeyReleased(GLFW.GLFW_KEY_COMMA);
     //   }
     //   if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_PERIOD)){
     //       this.save.time += 1000;
     //       KeyListener.setKeyReleased(GLFW.GLFW_KEY_PERIOD);
     //   }


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_1) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 0;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_2) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 1;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_3) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 2;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_4) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 3;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_5) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 4;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_6) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 5;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_7) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 6;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_8) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 7;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_9) && this.currentGui instanceof GuiInGame){
            EntityPlayer.selectedInventorySlot = 8;
        }


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_GRAVE_ACCENT) && KeyListener.keyReleased[GLFW.GLFW_KEY_GRAVE_ACCENT]){
            if(this.currentGui instanceof GuiInGame){
                this.save.activeWorld.toggleWorldPause();
                GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                this.setNewGui(new GuiTechTree(this));
                ((GuiTechTree)this.currentGui).switchEraDisplayed(Tech.NEOLITHIC_ERA);
            } else if(this.currentGui instanceof GuiTechTree){
                this.save.activeWorld.toggleWorldPause();
                GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                this.setNewGui(new GuiInGame(this));
            }
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_GRAVE_ACCENT);
        }


        if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            this.leftClick();
        } else {
            if(this.save != null) {
                if (this.save.thePlayer != null) {
                    this.save.thePlayer.breakTimer = 0;
                }
            }
        }
        if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            this.rightClick();
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_TAB) && KeyListener.keyReleased[GLFW.GLFW_KEY_TAB]){
            if(this.currentGui instanceof GuiInGame){
                this.setNewGui(new GuiUniverseMap(this));
                setGLClearColor(0,0,0,0);
            } else if(this.currentGui instanceof GuiUniverseMap){
                this.setNewGui(new GuiInGame(this));
            }
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_TAB);
        }

        if(KeyListener.isKeyPressed(GameSettings.inventoryKey.keyCode) && KeyListener.keyReleased[GameSettings.inventoryKey.keyCode]){
            if(this.currentGui instanceof GuiInGame){
                this.setNewGui(new GuiPlayerInventory(this, this.save.thePlayer.inventory));
            } else if(this.currentGui instanceof GuiInventory){
                this.setNewGui(new GuiInGame(this));
                GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            }
            KeyListener.setKeyReleased(GameSettings.inventoryKey.keyCode);
        }

        if(this.currentGui instanceof GuiUniverseMap){
            ((GuiUniverseMap)this.currentGui).updateCamera();
        }

        if(this.currentGui instanceof GuiInGame) {
            if (MouseListener.getScrollY() == -1) {
                EntityPlayer.selectedInventorySlot++;
                if (EntityPlayer.selectedInventorySlot > 9) {
                    EntityPlayer.selectedInventorySlot = 0;
                }
            } else if (MouseListener.getScrollY() == 1) {
                EntityPlayer.selectedInventorySlot--;
                if (EntityPlayer.selectedInventorySlot < 0) {
                    EntityPlayer.selectedInventorySlot = 9;
                }
            }
        }
        if(this.currentGui instanceof GuiTechTree) {
            if (MouseListener.getScrollY() == -1) {
                ((GuiTechTree) this.currentGui).zoomTechBlocks(-1);
            } else if (MouseListener.getScrollY() == 1) {
                ((GuiTechTree) this.currentGui).zoomTechBlocks(1);
            }
        }
        MouseListener.instance.scrollY = 0;
        MouseListener.endFrame();
        GLFW.glfwPollEvents();
    }

    private void checkKeyBindStates() {
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE) && this.save != null && this.currentGui instanceof GuiInGame) {
            this.save.activeWorld.paused = true;
            this.setNewGui(new GuiPauseInGame(this));
            this.save.saveDataToFileWithoutChunkUnload();
            this.save.thePlayer.savePlayerToFile();
        }
        if (!MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            MouseListener.leftClickReleased = true;
        }

        if (!MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            MouseListener.rightClickReleased = true;
        }
        KeyListener.checkIfKeysArePressed();
    }

    private void leftClick() {
        if(this.save != null) {
            this.save.handleLeftClick();
        }
        if(this.currentGui instanceof GuiInventory){
            ItemStack stack;
            stack = ((GuiInventory)this.currentGui).getHoveredItemStack();
            if(stack != null) {
                if(ItemStack.itemStackOnMouse.item != null) {
                    if(ItemStack.itemStackOnMouse.item.equals(stack.item)){
                        if(stack.count + ItemStack.itemStackOnMouse.count <= stack.item.stackLimit) {
                            if (stack.metadata == ItemStack.itemStackOnMouse.metadata) {
                                if (MouseListener.leftClickReleased) {
                                    stack.mergeStack(ItemStack.itemStackOnMouse);
                                    ItemStack.itemStackOnMouse.item = null;
                                    ItemStack.itemStackOnMouse.count = 0;
                                    ItemStack.itemStackOnMouse.metadata = 0;
                                    ItemStack.itemStackOnMouse.durability = 0;
                                }
                            }
                        } else {
                            if (stack.metadata == ItemStack.itemStackOnMouse.metadata) {
                                if (MouseListener.leftClickReleased) {
                                    while(stack.count < stack.item.stackLimit){
                                        stack.count++;
                                        ItemStack.itemStackOnMouse.count--;
                                    }
                                }
                            }
                        }
                    } else if(stack.item == null){
                        if (MouseListener.leftClickReleased) {
                            if (stack.usesExclusiveItem && stack.exclusiveItemType.equals(ItemStack.itemStackOnMouse.item.itemType)) {
                                stack.item = ItemStack.itemStackOnMouse.item;
                                stack.count = ItemStack.itemStackOnMouse.count;
                                stack.metadata = ItemStack.itemStackOnMouse.metadata;
                                stack.durability = ItemStack.itemStackOnMouse.durability;
                                ItemStack.itemStackOnMouse.item = null;
                                ItemStack.itemStackOnMouse.count = 0;
                                ItemStack.itemStackOnMouse.metadata = 0;
                                ItemStack.itemStackOnMouse.durability = 0;
                                if(stack.exclusiveItemType.equals(Item.ITEM_TYPE_PLAYER_STORAGE)){
                                    this.save.thePlayer.setPlayerStorageLevel((byte) stack.item.storageLevel);
                                }
                            } else if(!stack.usesExclusiveItem){
                                stack.item = ItemStack.itemStackOnMouse.item;
                                stack.count = ItemStack.itemStackOnMouse.count;
                                stack.metadata = ItemStack.itemStackOnMouse.metadata;
                                stack.durability = ItemStack.itemStackOnMouse.durability;
                                ItemStack.itemStackOnMouse.item = null;
                                ItemStack.itemStackOnMouse.count = 0;
                                ItemStack.itemStackOnMouse.metadata = 0;
                                ItemStack.itemStackOnMouse.durability = 0;
                            }
                        }
                    }
                } else if(stack.item != null) {
                    if (MouseListener.leftClickReleased) {
                        ItemStack.itemStackOnMouse.item = stack.item;
                        ItemStack.itemStackOnMouse.count = stack.count;
                        ItemStack.itemStackOnMouse.metadata = stack.metadata;
                        ItemStack.itemStackOnMouse.durability = stack.durability;
                        ItemStack.itemStackOnMouse.exclusiveItemType = stack.exclusiveItemType;
                        stack.item = null;
                        stack.count = 0;
                        stack.metadata = 0;
                        stack.durability = 0;
                        if(stack.usesExclusiveItem && stack.exclusiveItemType.equals(Item.ITEM_TYPE_PLAYER_STORAGE)){
                            this.save.thePlayer.setPlayerStorageLevel((byte) 1);
                        }
                    }
                }
            }
        }
        Button button =  this.currentGui.getActiveButton();
        if(button != null){
            if(MouseListener.leftClickReleased) {
                if(this.save != null) {
                    new SoundPlayer(SpaceGame.instance).playSound(this.save.thePlayer.x, this.save.thePlayer.y, this.save.thePlayer.z, new Sound("src/spacegame/assets/sound/buttonPress.ogg", false), 1);
                } else {
                    new SoundPlayer(SpaceGame.instance).playSound(SpaceGame.camera.position.x, SpaceGame.camera.position.y, SpaceGame.camera.position.z, new Sound("src/spacegame/assets/sound/buttonPress.ogg", false), 1);
                }
                    button.onLeftClick();
            }
        }

        TextField textField = this.currentGui.getTextField();
        if(textField != null){
            if(MouseListener.leftClickReleased) {
                if(textField != this.currentlySelectedField){
                    if(this.currentlySelectedField != null){
                        this.currentlySelectedField.typing = false;
                        this.currentlySelectedField.breakLoop = true;
                    }
                }
                this.currentlySelectedField = textField;
                textField.breakLoop = false;
                textField.onLeftClick();
            }
        }

        if(this.currentGui instanceof GuiCrafting) {
            CraftingMaterial material = ((GuiCrafting) this.currentGui).getHoveredCraftingMaterial();
            if (material != null) {
                if (material.active) {
                    material.deactivate();
                }
            }
        }

        if(this.currentGui instanceof GuiCraftingStoneTools){
            RecipeSelector recipeSelector = ((GuiCraftingStoneTools)this.currentGui).getSelectedRecipeSelector();
            if(recipeSelector != null){
                ((GuiCraftingStoneTools)this.currentGui).selectedItemID = recipeSelector.itemID;
                ((GuiCraftingStoneTools)this.currentGui).selectableRecipes = null;
                ((GuiCraftingStoneTools)this.currentGui).activateRecipeOverlay();
            }
        }

        if(this.currentGui instanceof GuiCraftingStrawStorage){
            RecipeSelector recipeSelector = ((GuiCraftingStrawStorage)this.currentGui).getSelectedRecipeSelector();
            if(recipeSelector != null){
                ((GuiCraftingStrawStorage)this.currentGui).setRecipeSelected(recipeSelector);
            }
        }

        MouseListener.leftClickReleased = false;
    }


    private void rightClick() {
        if(this.save != null) {
            this.save.handleRightClick();
        }

        stack:
        if(MouseListener.rightClickReleased) {
            if (this.currentGui instanceof GuiInventory) {
                ItemStack stack;
                stack = ((GuiInventory) this.currentGui).getHoveredItemStack();
                if (stack != null) {
                    if (stack.count > 1 && ItemStack.itemStackOnMouse.item == null) {
                        if (MouseListener.rightClickReleased) {
                            ItemStack.itemStackOnMouse = stack.splitStack();
                            break stack;
                        }
                    }
                }
                if (ItemStack.itemStackOnMouse != null && stack != null) {
                    if (stack.item == null || (stack.item == ItemStack.itemStackOnMouse.item && stack.durability == ItemStack.itemStackOnMouse.durability && stack.metadata == ItemStack.itemStackOnMouse.metadata)) {
                        if(stack.usesExclusiveItem && stack.exclusiveItemType.equals(ItemStack.itemStackOnMouse.item.itemType)) {
                            stack.item = ItemStack.itemStackOnMouse.item;
                            stack.durability = ItemStack.itemStackOnMouse.durability;
                            stack.metadata = ItemStack.itemStackOnMouse.metadata;
                            stack.count++;
                            ItemStack.itemStackOnMouse.count--;
                            if (ItemStack.itemStackOnMouse.count <= 0) {
                                ItemStack.itemStackOnMouse.item = null;
                                ItemStack.itemStackOnMouse.count = 0;
                                ItemStack.itemStackOnMouse.durability = 0;
                                ItemStack.itemStackOnMouse.metadata = 0;
                            }
                        } else if(!stack.usesExclusiveItem){
                            stack.item = ItemStack.itemStackOnMouse.item;
                            stack.durability = ItemStack.itemStackOnMouse.durability;
                            stack.metadata = ItemStack.itemStackOnMouse.metadata;
                            stack.count++;
                            ItemStack.itemStackOnMouse.count--;
                            if (ItemStack.itemStackOnMouse.count <= 0) {
                                ItemStack.itemStackOnMouse.item = null;
                                ItemStack.itemStackOnMouse.count = 0;
                                ItemStack.itemStackOnMouse.durability = 0;
                                ItemStack.itemStackOnMouse.metadata = 0;
                            }
                        }
                    }
                }
            }
        }

            if(this.currentGui instanceof GuiTechTree){
                float deltaX = MouseListener.getDeltaX();
                float deltaY = -MouseListener.getDeltaY();
                ((GuiTechTree) this.currentGui).moveTechBlocks(deltaX, deltaY);
            }

        MouseListener.rightClickReleased = false;
    }

    private void framePulse() {
        SoundPlayer.checkAllSoundStates();
        if(this.save != null) {
            this.save.thePlayer.resetCamera();
            if(World.worldLoadPhase >= 1) {
                this.save.activeWorld.chunkController.update();
            }
            if(this.currentGui instanceof GuiWorldLoadingScreen){
                if(Thread.activeCount() < 5){
                    GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    if(Assets.blockTextureArray == 0) {
                        Assets.enableBlockTextureArray();
                        Assets.enableItemTextureArray();
                    }
                    this.save.activeWorld.chunkController.addChunkToRebuildQueue(this.save.activeWorld.chunkController.findChunkFromChunkCoordinates(this.save.activeWorld.chunkController.playerChunkX, this.save.activeWorld.chunkController.playerChunkY, this.save.activeWorld.chunkController.playerChunkZ));
                    this.save.activeWorld.paused = false;
                    this.setNewGui(new GuiInGame(this));
                }
            }
        }
    }

    private void setAllGlobalShaderUniforms(){
        Shader.terrainShader.uploadBoolean("useFog", true);
        Shader.terrainShader.uploadInt("textureArray", 0);
        Shader.terrainShader.uploadInt("shadowMap", 1);
        Shader.terrainShader.uploadBoolean("wavyWater", GameSettings.wavyWater);
        Shader.terrainShader.uploadBoolean("wavyLeaves", GameSettings.wavyLeaves);
    }

    private void incrementPlayerDamageTilt() {
        if (this.save == null) {
            return;
        }
        if (this.save.thePlayer == null) {
            return;
        }
        if (!this.save.thePlayer.runDamageTilt) {
            return;
        }

        if (this.save.thePlayer.damageTiltTimer < 30) {
            this.save.thePlayer.roll += 0.1666f;
        } else {
            this.save.thePlayer.roll -= 0.1666f;
        }

        this.save.thePlayer.damageTiltTimer++;
        if (this.save.thePlayer.damageTiltTimer >= 60) {
            this.save.thePlayer.runDamageTilt = false;
            this.save.thePlayer.roll = 0;
            this.save.thePlayer.damageTiltTimer = 0;
        }
    }

    public static void setGLClearColor(float red, float green, float blue, float alpha){
        GL46.glClearColor(red, green, blue, alpha);
        Shader.terrainShader.uploadFloat("fogRed", red);
        Shader.terrainShader.uploadFloat("fogGreen", green);
        Shader.terrainShader.uploadFloat("fogBlue", blue);
    }

    private void render() {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
        GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);
        if(this.save != null && !(this.currentGui instanceof GuiWorldLoadingScreen || this.currentGui instanceof GuiUniverseMap || this.currentGui instanceof GuiTechTree)) {
            this.save.activeWorld.renderWorld();
            this.save.thePlayer.renderShadow();
        }
        GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);
        this.currentGui.drawGui();
        if(this.currentGui instanceof GuiInventory) {
            if (ItemStack.itemStackOnMouse != null) {
                ItemStack.itemStackOnMouse.x = (float) (MouseListener.instance.xPos - SpaceGame.width/2D);
                ItemStack.itemStackOnMouse.y = (float) ((MouseListener.instance.yPos - SpaceGame.height/2D) * -1);
                ItemStack.itemStackOnMouse.renderItemStack(false);
                ((GuiInventory) this.currentGui).renderHoveredItemStackName(ItemStack.itemStackOnMouse);
            }
        }
        GLFW.glfwSwapBuffers(this.window);
        Timer.elapseFrames++;
    }

    public void setNewGui(Gui gui) {
        if (this.currentGui != null) {
            this.currentGui.deleteTextures();
        }
        this.currentGui = gui;
        this.currentGui.loadTextures();
    }

    public static void setWidth(int newWidth) {
        width = newWidth;
    }

    public static void setHeight(int newHeight) {
        height = newHeight;
    }

    private long createWindow() {
        return GameSettings.fullscreen ? GLFW.glfwCreateWindow(width, height, this.title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL) :  GLFW.glfwCreateWindow(width, height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
    }

    public void toggleFullscreen(){
        if(GameSettings.fullscreen) {
            GLFW.glfwSetWindowMonitor(this.window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, GLFW.GLFW_REFRESH_RATE);
        } else {
            GLFW.glfwSetWindowMonitor(this.window, MemoryUtil.NULL, 0, 0, width, height, GLFW.GLFW_REFRESH_RATE);
        }
    }

    private void initAllBufferObjects(){
        Entity.initShadow();
        Sun.initSunFlare();
        GuiUniverseMap.initSkyboxTexture();
        EntityDeer.loadTexture();
    }

    private void initAllGlobalAssets(){
        Assets.enableFontTextureAtlas();
    }

    public void shutdown() {
        this.running = false;
        GameSettings.saveOptions();
    }

}
