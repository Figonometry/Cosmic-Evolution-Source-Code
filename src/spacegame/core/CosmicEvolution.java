package spacegame.core;

import org.joml.Matrix4d;
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
import spacegame.block.Block;
import spacegame.celestial.Sun;
import spacegame.celestial.Universe;
import spacegame.core.eventlisteners.CharListener;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.eventlisteners.WindowResizeListener;
import spacegame.entity.*;
import spacegame.gui.*;
import spacegame.item.ItemStack;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.*;
import spacegame.util.Logger;
import spacegame.util.ScreenshotHandler;
import spacegame.world.*;
import spacegame.world.threads.ThreadChunkJobScheduler;
import spacegame.world.worldtypes.World;
import spacegame.world.worldtypes.earthlike.WorldEarth;
import spacegame.world.worldtypes.testworld.WorldTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class CosmicEvolution implements Runnable {
    public static CosmicEvolution instance;
    public final File launcherDirectory;
    public final String launcherFilepath;
    public static final Random globalRand = new Random();
    public volatile boolean running;
    private static final boolean TEST_WORLD = false;
    public String title;
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
    public RenderEngine renderEngine = new RenderEngine();
    public SoundPlayer soundPlayer = new SoundPlayer(this);
    private EntityDeer modelTest;
    public static ExecutorService threadPool;
    public static final AtomicInteger threadJobs = new AtomicInteger();
    public Thread dirtyChunksSchedulerThread;
    public String osName;
    public Button currentButtonOnMouse;


    public static void main(String[] args) {
        if(args.length != 0) {
            startMainThread(new Thread(new CosmicEvolution(args[0])));
        } else {
            new Logger("Failed to provide launcher directory", true);
        }
    }

    private static void startMainThread(Thread mainThread) {
        mainThread.setName("Cosmic Evolution Main Thread");
        mainThread.setPriority(10);
        mainThread.start();
    }

    private CosmicEvolution(String launcherFilepath) {
        if (instance != null) {
            throw new RuntimeException("Main Class already initialized");
        }
        this.launcherFilepath = launcherFilepath;
        this.launcherDirectory = new File(launcherFilepath);
        instance = this;
    }

    @Override
    public void run() {
        this.running = true;
        try {
            this.startGame();
        } catch(Throwable t){
            new Logger(t, true);
            if(this.save != null){
                this.save.saveDataToFile();
                this.save.activeWorld.saveWorld();
                this.save.thePlayer.savePlayerToFile();
            }
            this.shutdown();
        }
    }

    private void startGame() {
        this.osName = this.verifyOperatingSystem();
        int numCores = Runtime.getRuntime().availableProcessors();
        int workerCount = Math.max(1, numCores - 1);
        threadPool = new ThreadPoolExecutor(workerCount, workerCount, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
        this.dirtyChunksSchedulerThread = new Thread(new ThreadChunkJobScheduler());
        this.dirtyChunksSchedulerThread.start();
        this.title = "Cosmic Evolution Alpha v0.56";
        GameSettings.loadOptionsFromFile(this.launcherDirectory);
        Block.registerAllBlockTooltips();
        EntityLiving.registerEntityLivingToolTip();
        this.clearLogFiles(new File(this.launcherDirectory + "/crashReports"));
        this.initLWJGL();
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

    private String verifyOperatingSystem() {
        String operatingSystem = System.getProperty("os.name");
        operatingSystem = operatingSystem.toLowerCase();
        this.osName = operatingSystem;
        if (this.osName.contains("win")) {
            return "win";
        } else if (this.osName.contains("mac")) {
            return "mac";
        } else if (this.osName.contains("linux") || this.osName.contains("unix")) {
            return "linux";
        } else {
            return "unknown";
        }
    }

    private void initLWJGL() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4); // or 8


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

        GLFW.glfwSetCharCallback(this.window, CharListener::charCallBack);


        GLFW.glfwMakeContextCurrent(this.window);

        GLFW.glfwSwapInterval(GameSettings.vsync ? 1 : 0);

        GLFW.glfwShowWindow(this.window);

        GL.createCapabilities();

        Shader.loadShaders();

        Shader.terrainShader.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
        Shader.worldShader2DTexture.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);
        Shader.worldShaderTextureArray.uploadBoolean("shadowMapSetting", GameSettings.shadowMap);

        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glDepthFunc(GL46.GL_LESS);

        GL46.glClearColor(0,0,0,0);

        //Sound setup code needs to change to handle whenever the sound device no longer points to a valid output device or when no device was previously provided and one becomes available
        //This may be some kind of event listener I can hook into
        long device = ALC11.alcOpenDevice((ByteBuffer) null);

        if (device != MemoryUtil.NULL) {
            long context = ALC11.alcCreateContext(device, (IntBuffer) null);
            if (context != MemoryUtil.NULL) {
                ALC11.alcMakeContextCurrent(context);
                AL.createCapabilities(ALC.createCapabilities(device));
            }
        } else {
            Sound.canPlaySound = false;
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
        camera = new Camera(new Vector3d(), this, 0.1D);
        GuiUniverseMap.universeCamera = new Camera(new Vector3d(), this, 0.0000000000000000001D);
        GuiUniverseMap.universeCamera.setFarPlaneDistance(512);
        GuiUniverseMap.universeCamera.viewMatrix.translate(-0.000000000000000001, 0, 0);
        this.setNewGui(new GuiMainMenu(this));
    }

    private void mainLoop() {
        long lastFrameTime = System.nanoTime();
        byte fpsTimer = 30;

        while (this.running) {
            this.timer.advanceTime();

            for (int i = 0; i < this.timer.ticks; i++) {
                this.tick();
            }

            this.framePulse();
            this.render();

            // FPS calculation every 30 frames
            fpsTimer--;
            if (fpsTimer <= 0) {
                long now = System.nanoTime();
                long frameTime = now - lastFrameTime;
                lastFrameTime = now;

                this.fps = (int)(60 * 1_000_000_000.0 / frameTime);
                fpsTimer = 60;
            }
        }

        this.shutdown();
    }




    public void startNewSave(int saveSlotNumber, String saveName, long seed, SaveSettings saveSettings) {
        double x = -150;
        double z = 0;
        this.save = new Save(this, saveSlotNumber, saveName, seed, x, z, saveSettings);
        this.everything = new Universe();
        this.save.thePlayer = new EntityPlayer(this, 0, 0, 0);
        this.save.thePlayer.setPlayerActualPos(x,2, z);
        this.setNewGui(new GuiWorldLoading(this));
        this.save.setActiveWorld(TEST_WORLD ? new WorldTest(this, 400704) : new WorldEarth(this, 400704));
        this.save.activeWorld.paused = true;

        if(!(this.save.activeWorld instanceof WorldTest)) {
            Thread textureLoadThread = new Thread(new ThreadGenerateCelestialBodyTextures(this.everything.earth, true));
            textureLoadThread.setName("Celestial Load Thread");
            textureLoadThread.setPriority(10);
            textureLoadThread.start();
        } else {
            World.worldLoadPhase = 2;
        }
    }

    public void startSave(File saveFile) {
        this.save = new Save(this, saveFile);
        this.everything = new Universe();
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
        this.setNewGui(new GuiWorldLoading(this));
        this.save.setActiveWorld(TEST_WORLD ? new WorldTest(this, 400704) : new WorldEarth(this, 400704));
        this.save.activeWorld.paused = true;


        if(!(this.save.activeWorld instanceof WorldTest)) {
            Thread textureLoadThread = new Thread(new ThreadGenerateCelestialBodyTextures(this.everything.earth, false));
            textureLoadThread.setName("Celestial Load Thread");
            textureLoadThread.setPriority(10);
            textureLoadThread.start();
        } else {
            World.worldLoadPhase = 2;
        }
    }

    private void tick() {
        if(this.save != null) {
            this.save.tick();
            this.renderEngine.loadTexturesFromList();
            if(!this.save.activeWorld.paused){
                if(this.save.time % 3 == 0) {
                    this.renderEngine.updateTextureFX();
                }
                this.incrementTextureTimers();
            }
            ToolTipGroup.altToolTipIndex = this.save.time / (Timer.REAL_SECOND * 2);
            GuiInGame.fadeMessageText();
            if(this.save.time % 18000 == 0 && this.currentGui instanceof GuiInGame){
                this.save.saveDataToFileWithoutChunkUnload();
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
                        float x = (float) (MouseListener.instance.xPos - CosmicEvolution.width / 2D);
                        float y = (float) ((MouseListener.instance.yPos - CosmicEvolution.height / 2D) * -1);
                        this.currentlySelectedMoveableObject.x = x;
                        this.currentlySelectedMoveableObject.y = y;
                    } else {
                        this.currentlySelectedMoveableObject.pickedUp = false;
                    }
                }
            }
        }
        if(this.currentGui instanceof GuiSelectAssetPackMainMenu){
            File[] folderContents = ((GuiSelectAssetPackMainMenu) this.currentGui).assetPackFolder.listFiles();
            if(folderContents == null){
                ((GuiSelectAssetPackMainMenu) this.currentGui).folderCount = 0;
                ((GuiSelectAssetPackMainMenu) this.currentGui).rebuildAssetPackArray(null);
            } else {
                int folderCount = 0;
                for (int i = 0; i < folderContents.length; i++) {
                    if (folderContents[i].isDirectory()) {
                        folderCount++;
                    }
                }

                if(((GuiSelectAssetPackMainMenu) this.currentGui).folderCount != folderCount) {
                    ((GuiSelectAssetPackMainMenu) this.currentGui).folderCount = folderCount;
                    ((GuiSelectAssetPackMainMenu) this.currentGui).rebuildAssetPackArray(folderContents);
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
        this.currentGui.handleInput();

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK) && KeyListener.keyReleased[GLFW.GLFW_KEY_CAPS_LOCK]){
            KeyListener.capsLockEnabled = !KeyListener.capsLockEnabled;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_CAPS_LOCK);
        }


        if(!MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)){
            if(this.currentButtonOnMouse != null &&
                    !this.currentButtonOnMouse.name.equals(EnumButtonEffects.SAVE_1.name())
            && !this.currentButtonOnMouse.name.equals(EnumButtonEffects.SAVE_2.name())
            && !this.currentButtonOnMouse.name.equals(EnumButtonEffects.SAVE_3.name())
            && !this.currentButtonOnMouse.name.equals(EnumButtonEffects.SAVE_4.name())
            && !this.currentButtonOnMouse.name.equals(EnumButtonEffects.SAVE_5.name())
            && !this.currentButtonOnMouse.name.equals(EnumButtonEffects.DELETE.name())) {
                this.currentButtonOnMouse.clicked = false;
                this.currentButtonOnMouse = null;
            }
        }

        if(this.save != null) {
            if(this.save.saveSettings.testingMode) {
                if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_MINUS)) {
                    this.save.time -= 216000;
                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_MINUS);
                }
                if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_EQUAL)) {
                    this.save.time += 216000;
                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_EQUAL);
                }
                if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_COMMA)) {
                    this.save.time -= 1000;
                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_COMMA);
                }
                if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_PERIOD)) {
                    this.save.time += 1000;
                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_PERIOD);
                }



                if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_U) && KeyListener.keyReleased[GLFW.GLFW_KEY_U]){


                  //  if(this.modelTest == null){
                  //      this.modelTest = new EntityDeer(this.save.thePlayer.x, this.save.thePlayer.y, this.save.thePlayer.z, true, true);
                  //      this.modelTest.health = 1f;
                  //      this.save.activeWorld.addEntity(this.modelTest);
                  //  } else {
                  //      this.save.activeWorld.findChunkFromChunkCoordinates(MathUtil.floorDouble(this.modelTest.x) >> 5, MathUtil.floorDouble(this.modelTest.y) >> 5, MathUtil.floorDouble(this.modelTest.z) >> 5).removeEntity(this.modelTest);
                  //      this.modelTest = null;
                  //  }
                  //  Shader.terrainShader = this.renderEngine.reloadShader(Shader.terrainShader);

                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_U);
                }
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F2) && KeyListener.keyReleased[GLFW.GLFW_KEY_F2]) {
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_F2);
            String message = ScreenshotHandler.takeScreenshot(this.launcherDirectory, width, height);
            if(this.save != null){
                if(this.save.activeWorld != null){
                    GuiInGame.setMessageText(message, 16777215);
                }
            }
        }


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F3) && KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)){
            if(this.save != null) {
                if (this.save.activeWorld != null) {
                    this.save.activeWorld.chunkController.markAllChunksDirty();
                }
            }
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

        MouseListener.instance.scrollY = 0;
        MouseListener.endFrame();
        GLFW.glfwPollEvents();
    }


    private void checkKeyBindStates() {
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE) && this.save != null && this.currentGui instanceof GuiInGame && KeyListener.keyReleased[GLFW.GLFW_KEY_ESCAPE]) {
            this.save.activeWorld.paused = true;
            this.setNewGui(new GuiPauseInGame(this));
            this.save.saveDataToFileWithoutChunkUnload();
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_ESCAPE);
        }


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE) && this.currentGui instanceof GuiPauseInGame && KeyListener.keyReleased[GLFW.GLFW_KEY_ESCAPE]) {
            GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            this.save.activeWorld.paused = false;
            this.setNewGui(new GuiInGame(this));
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_ESCAPE);
        }


        if (!MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            MouseListener.leftClickReleased = true;
            MouseListener.timeHeldLeftClick = 0;
        }

        if (!MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            MouseListener.rightClickReleased = true;
            MouseListener.timeHeldRightClick = 0;
        }

        if(MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && MouseListener.timeHeldLeftClick == 0 && this.save != null){
            MouseListener.timeHeldLeftClick = this.save.time;
        }

        if(MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && MouseListener.timeHeldRightClick == 0 && this.save != null){
            MouseListener.timeHeldRightClick = this.save.time;
        }

        KeyListener.checkIfKeysArePressed();
    }

    //This function needs to be cleaned up, it does 4 things which should be sub functioned,
    // and if possible the item stack code should be simplified so it's not impossible to read
    //Delegate to the GUI class tree
    private void leftClick() {
        if (this.save != null) {
            this.save.handleLeftClick();
        }
        this.currentGui.handleLeftClick();


        Button button = this.currentGui.getActiveButton();
        if (button != null) {
            if (MouseListener.leftClickReleased) {
                if (!button.clicked) {
                    if (this.save != null) {
                        CosmicEvolution.instance.soundPlayer.playSound(this.save.thePlayer.x, this.save.thePlayer.y, this.save.thePlayer.z, new Sound("src/spacegame/assets/sound/buttonPress.ogg", false, 1f), 1);
                    } else {
                        CosmicEvolution.instance.soundPlayer.playSound(CosmicEvolution.camera.position.x, CosmicEvolution.camera.position.y, CosmicEvolution.camera.position.z, new Sound("src/spacegame/assets/sound/buttonPress.ogg", false, 1f), 1);
                    }
                }
                button.onLeftClick();
            }
        }


        if(this.currentButtonOnMouse != null && this.currentButtonOnMouse instanceof Slider){
            this.currentButtonOnMouse.onLeftClick();
        }

        if (this.currentButtonOnMouse == null || !this.currentButtonOnMouse.clicked) {
            this.currentButtonOnMouse = button;
        }

        TextField textField = this.currentGui.getTextField();
        if (textField != null) {
            if (MouseListener.leftClickReleased) {
                if (textField != this.currentlySelectedField) {
                    if (this.currentlySelectedField != null) {
                        this.currentlySelectedField.typing = false;
                    }
                }
                this.currentlySelectedField = textField;
                textField.onLeftClick();
            }
        }


        MouseListener.leftClickReleased = button instanceof Slider;
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
                    if (stack.item == null || (stack.item == ItemStack.itemStackOnMouse.item && stack.durability == ItemStack.itemStackOnMouse.durability && stack.metadata == ItemStack.itemStackOnMouse.metadata && stack.count < stack.item.stackLimit)) {
                        if(ItemStack.itemStackOnMouse.item != null) {
                            if (stack.usesExclusiveItem && stack.exclusiveItemType.equals(ItemStack.itemStackOnMouse.item.itemType)) {
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
                            } else if (!stack.usesExclusiveItem) {
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
        }

        MouseListener.rightClickReleased = false;
    }

    private void framePulse() {
        SoundPlayer.checkAllSoundStates();
        if(this.save != null) {
            this.save.thePlayer.resetCamera();
            if(World.worldLoadPhase >= 2) {
                this.save.activeWorld.chunkController.update();
            }
            if(this.currentGui instanceof GuiWorldLoading){
                if(World.worldLoadPhase == 3){
                    if(Assets.blockTextureArray == 0 || Assets.itemTextureArray == 0) {
                        Assets.enableBlockTextureArray();
                        Assets.enableItemTextureArray();
                    }
                    if(threadJobs.get() == 0) {
                        World.worldLoadPhase = 4;
                        GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                        this.setNewGui(new GuiInGame(this));
                        this.save.activeWorld.paused = false;
                    }
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

        //Setup the tooltip shader uniforms now, they will not change
        Shader.toolTipShader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
        Shader.toolTipShader.uploadMat4d("uView", new Matrix4d());

        Shader.toolTipShader.uploadInt("fontAtlas", 0);
        Shader.toolTipShader.uploadInt("textBoxAtlas", 1);
        Shader.toolTipShader.uploadInt("blockArray", 2);
        Shader.toolTipShader.uploadInt("itemArray", 3);
        Shader.toolTipShader.uploadInt("mouseIconAtlas", 4);

        Shader.toolTipShader.uploadInt("width", width);
        Shader.toolTipShader.uploadInt("height", height);
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
        if(this.save != null && !(this.currentGui instanceof GuiWorldLoading || this.currentGui instanceof GuiUniverseMap || this.currentGui instanceof GuiSavingWorld)) {
            this.save.activeWorld.renderWorld();
            this.save.thePlayer.renderShadow();
        }
        GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);
        this.currentGui.drawGui();
        if(this.currentGui instanceof GuiInventory) {
            if (ItemStack.itemStackOnMouse != null) {
                ItemStack.itemStackOnMouse.x = (float) (MouseListener.instance.xPos - CosmicEvolution.width/2D);
                ItemStack.itemStackOnMouse.y = (float) ((MouseListener.instance.yPos - CosmicEvolution.height/2D) * -1);
                ItemStack.itemStackOnMouse.renderItemStack(false);
                ((GuiInventory) this.currentGui).renderHoveredItemStackName(ItemStack.itemStackOnMouse);
            }
        }
        GLFW.glfwSwapBuffers(this.window);
        Timer.elapseFrames++;
    }

    public void setNewGui(Gui gui) {
        if(this.currentGui instanceof GuiInventory && !(gui instanceof GuiInventory)){
            ItemStack.dropItemStackFromMouse(this);
        }
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
    }

    private void incrementTextureTimers(){
        EntityDeer.ticksSinceLastRender++;
        if(EntityDeer.ticksSinceLastRender >= 600 && EntityDeer.texture != RenderEngine.NULL_TEXTURE){
            this.renderEngine.deleteTexture(EntityDeer.texture);
            EntityDeer.texture = RenderEngine.NULL_TEXTURE;
        }
        EntityModelTest.ticksSinceLastRender++;
        if(EntityModelTest.ticksSinceLastRender >= 60 && EntityModelTest.texture != RenderEngine.NULL_TEXTURE){
            this.renderEngine.deleteTexture(EntityModelTest.texture);
            EntityModelTest.texture = RenderEngine.NULL_TEXTURE;
        }
        EntityWolf.ticksSinceLastRender++;
        if(EntityWolf.ticksSinceLastRender >= 600 && EntityWolf.texture != RenderEngine.NULL_TEXTURE){
            this.renderEngine.deleteTexture(EntityWolf.texture);
            EntityWolf.texture = RenderEngine.NULL_TEXTURE;
        }
    }

    public void reloadAllTextures(){
        this.renderEngine.deleteTexture(Entity.shadow);
        this.renderEngine.deleteTexture(Sun.sunFlare);
        this.renderEngine.deleteTexture(GuiUniverseMap.skybox);

        Assets.disableItemTextureArray();
        Assets.disableBlockTextureArray();
        Assets.disableFontTextureAtlas();
        Assets.disableMouseIconAtlas();
        Assets.disableTextBoxAtlas();

        this.currentGui.deleteTextures();

        if(EntityDeer.texture != RenderEngine.NULL_TEXTURE){
            this.renderEngine.deleteTexture(EntityDeer.texture);
            EntityDeer.texture = RenderEngine.NULL_TEXTURE;
        }

        if(EntityWolf.texture != RenderEngine.NULL_TEXTURE){
            this.renderEngine.deleteTexture(EntityWolf.texture);
            EntityWolf.texture = RenderEngine.NULL_TEXTURE;
        }

        this.initAllBufferObjects();

        Assets.enableTextBoxAtlas();
        Assets.enableMouseIconAtlas();
        Assets.enableFontTextureAtlas();
        Assets.enableItemTextureArray();
        Assets.enableBlockTextureArray();

        this.currentGui.loadTextures();
    }

    private void initAllGlobalAssets(){
        Assets.enableFontTextureAtlas();
        Assets.enableTextBoxAtlas();
        Assets.enableMouseIconAtlas();
    }

    public static void addJobToThreadPool(ChunkJob chunkJob){
        threadPool.execute(chunkJob);
    }

    public void shutdown() {
        this.running = false;
        GameSettings.saveOptions();
        threadPool.shutdown();
    }

}
