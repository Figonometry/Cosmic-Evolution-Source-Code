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
import spacegame.item.ItemStack;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.Assets;
import spacegame.render.Camera;
import spacegame.render.Tessellator;
import spacegame.world.Chunk;
import spacegame.world.Save;
import spacegame.world.World;
import spacegame.world.WorldEarth;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public final class SpaceGame implements Runnable {
    public static SpaceGame instance;
    public final File launcherDirectory;
    public static final boolean DEBUG_MODE = true;
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


    public static void main(String[] args) {
        if(args.length != 0) {
            startMainThread(new Thread(new SpaceGame(args[0])));
        } else {
            System.out.println("Incorrect number of arguments");
        }
    }

    private static void startMainThread(Thread mainThread) {
        mainThread.setName("SpaceTime Main Thread");
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
            Calendar calendar = new GregorianCalendar();
            File crashFile = new File(this.launcherDirectory + "/crashReports/crashReport" + "-" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY)  + calendar.get(Calendar.MINUTE)  + calendar.get(Calendar.SECOND) +  ".txt");
            PrintStream ps;
            try {
                ps = new PrintStream(crashFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            exception.printStackTrace(ps);
            exception.printStackTrace();
            ps.println("WARNING: FATAL EXCEPTION OCCURRED SHUTTING DOWN GAME");
            ps.println("Please forward the stacktrace to Fig, please include a description of what exactly you did to cause the crash");


            ps.println("Elapsed Time: " + Timer.elapsedTime + " Ticks");
            ps.println();

            ps.close();
            if(this.save != null){
                this.save.saveDataToFile();
                this.save.activeWorld.saveWorld();
                this.save.thePlayer.savePlayerToFile();
            }
            this.shutdown();
        }
    }

    private void startGame() {
        this.title = "Cosmic Evolution Alpha v0.17 WIP";
        this.initLWJGL();
        this.startMainLoop();
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

        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glDepthFunc(GL46.GL_LESS);

        long device = ALC11.alcOpenDevice((ByteBuffer) null);

        if (device != MemoryUtil.NULL) {
            long context = ALC11.alcCreateContext(device, (IntBuffer) null);
            if (context != MemoryUtil.NULL) {
                ALC11.alcMakeContextCurrent(context);
                AL.createCapabilities(ALC.createCapabilities(device));
            }
        }

        GLFW.glfwSetWindowSizeCallback(this.window, null);

        this.initAllBufferObjects();
        this.initAllGlobalAssets();
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

    private void startMainLoop() {
        this.everything = new Universe();
        camera = new Camera(new Vector3d(), this, 0.1D);
        GuiUniverseMap.universeCamera = new Camera(new Vector3d(), this, 0.0000000000000000001D);
        GuiUniverseMap.universeCamera.setFarPlaneDistance(512);
        GuiUniverseMap.universeCamera.viewMatrix.translate(-0.000000000000000001, 0, 0);
        this.setWindowIcon();
        this.setNewGui(new GuiMainMenu(this));
        long lastTime = System.nanoTime();
        byte fpsTimer = 30;
        try {
            while (running) {
                this.timer.advanceTime();
                for (int i = 0; i < this.timer.ticks; i++) {
                    this.tick();
                }
                fpsTimer--;
                if (fpsTimer <= 0) {
                    this.fps = (int) (1000000000.0 / (lastTime - System.nanoTime()));
                    fpsTimer = 30;
                }
                lastTime = System.nanoTime();
                this.framePulse();
                this.render();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.shutdown();
            System.exit(0);
        }
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
        this.save.activeWorld.activeWorldFace.paused = true;
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
        this.save.activeWorld.activeWorldFace.paused = true;
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
        this.incrementPlayerDamageTilt();
        this.processInput();
    }

    private void processInput() {
        GameSettings.switchKeyBinds();
        this.checkKeyBindStates();

        if(this.currentGui instanceof GuiUniverseMap) {
            if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK) && KeyListener.capsLockReleased) {
                ((GuiUniverseMap)this.currentGui).switchObject();
                KeyListener.capsLockReleased = false;
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK) && KeyListener.capsLockReleased){
            KeyListener.capsLockEnabled = !KeyListener.capsLockEnabled;
            KeyListener.capsLockReleased = false;
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_G) && KeyListener.gKeyReleased){
            if(this.save != null){
                if(this.save.thePlayer != null && this.save.activeWorld != null){
                    EntityDeer deer =  new EntityDeer(this.save.thePlayer.x,this.save.thePlayer.y - 0.5, this.save.thePlayer.z, false, false);
                   // deer.yaw = new Random().nextInt(360);
                    this.save.activeWorld.activeWorldFace.addEntity(deer);
                }
            }
            KeyListener.gKeyReleased = false;
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

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_TAB) && KeyListener.tabReleased){
            if(this.currentGui instanceof GuiInGame){
                this.setNewGui(new GuiUniverseMap(this));
            } else if(this.currentGui instanceof GuiUniverseMap){
                this.setNewGui(new GuiInGame(this));
            }
            KeyListener.tabReleased = false;
        }

        if(KeyListener.isKeyPressed(GameSettings.inventoryKey.keyCode) && KeyListener.inventoryKeyReleased){
            if(this.currentGui instanceof GuiInGame){
                this.setNewGui(new GuiPlayerInventory(this, this.save.thePlayer.inventory));
            } else if(this.currentGui instanceof GuiInventory){
                this.setNewGui(new GuiInGame(this));
                GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            }
            KeyListener.inventoryKeyReleased = false;
        }

        if(this.currentGui instanceof GuiUniverseMap){
            ((GuiUniverseMap)this.currentGui).updateCamera();
        }

        if(this.currentGui instanceof GuiInGame) {
            if (MouseListener.getScrollY() == -1) {
                EntityPlayer.selectedInventorySlot++;
                if (EntityPlayer.selectedInventorySlot > this.save.thePlayer.inventory.itemStacks.length - 1) {
                    EntityPlayer.selectedInventorySlot = 0;
                }
            } else if (MouseListener.getScrollY() == 1) {
                EntityPlayer.selectedInventorySlot--;
                if (EntityPlayer.selectedInventorySlot < 0) {
                    EntityPlayer.selectedInventorySlot = this.save.thePlayer.inventory.itemStacks.length - 1;
                }
            }
        }
        MouseListener.instance.scrollY = 0;

    }

    private void checkKeyBindStates() {
        GLFW.glfwPollEvents();
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE) && this.save != null && this.currentGui instanceof GuiInGame) {
            this.save.activeWorld.activeWorldFace.paused = true;
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

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK)){
            KeyListener.capsLockReleased = true;
        }

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_TAB)){
            KeyListener.tabReleased = true;
        }

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)){
            KeyListener.backSpaceReleased = true;
        }

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_G)){
            KeyListener.gKeyReleased = true;
        }

        if(!KeyListener.isKeyPressed(GameSettings.inventoryKey.keyCode)){
            KeyListener.inventoryKeyReleased = true;
        }

        if(!KeyListener.isKeyPressed(GameSettings.dropKey.keyCode)){
            KeyListener.dropKeyReleased = true;
        }
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
                        if(stack.count + ItemStack.itemStackOnMouse.count <= 100) {
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
                                    while(stack.count < 100){
                                        stack.count++;
                                        ItemStack.itemStackOnMouse.count--;
                                    }
                                }
                            }
                        }
                    } else if(stack.item == null){
                        if (MouseListener.leftClickReleased) {
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
                } else if(stack.item != null) {
                    if (MouseListener.leftClickReleased) {
                        ItemStack.itemStackOnMouse.item = stack.item;
                        ItemStack.itemStackOnMouse.count = stack.count;
                        ItemStack.itemStackOnMouse.metadata = stack.metadata;
                        ItemStack.itemStackOnMouse.durability = stack.durability;
                        stack.item = null;
                        stack.count = 0;
                        stack.metadata = 0;
                        stack.durability = 0;
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
        MouseListener.rightClickReleased = false;
    }

    private void framePulse() {
        SoundPlayer.checkAllSoundStates();
        if(this.save != null) {
            this.save.thePlayer.resetCamera();
            if(World.worldLoadPhase >= 1) {
                this.save.activeWorld.activeWorldFace.chunkController.update();
            }
            if(this.currentGui instanceof GuiWorldLoadingScreen){
                if(((double) this.save.activeWorld.activeWorldFace.chunkController.numberOfLoadedChunks / ((GameSettings.renderDistance * 2 + 1) * (GameSettings.renderDistance * 2 + 1) * (GameSettings.chunkColumnHeight * 2))) >= 0.9D && Thread.activeCount() < 5){
                    GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    if(Assets.blockTextureArray == null) {
                        Assets.enableBlockTextureArray();
                        Assets.enableItemTextureArray();
                    }
                    this.save.activeWorld.activeWorldFace.chunkController.addChunkToRebuildQueue(this.save.activeWorld.activeWorldFace.chunkController.findChunkFromChunkCoordinates(this.save.activeWorld.activeWorldFace.chunkController.playerChunkX, this.save.activeWorld.activeWorldFace.chunkController.playerChunkY, this.save.activeWorld.activeWorldFace.chunkController.playerChunkZ));
                    this.save.activeWorld.activeWorldFace.paused = false;
                    this.setNewGui(new GuiInGame(this));
                }
            }
        }
    }

    private void incrementPlayerDamageTilt() {
        if(this.save == null){return;}
        if (this.save.thePlayer == null) {return;}
        if (!this.save.thePlayer.runDamageTilt) {return;}

        if (this.save.thePlayer.damageTimer % 3 == 0) {
            if (this.save.thePlayer.damageTimer < 30) {
                this.save.thePlayer.roll++;
            } else {
                this.save.thePlayer.roll--;
            }
        }

        this.save.thePlayer.damageTimer++;
        if (this.save.thePlayer.damageTimer >= 60) {
            this.save.thePlayer.runDamageTilt = false;
            this.save.thePlayer.roll = 0;
            this.save.thePlayer.damageTimer = 0;
        }
    }

    private void render() {
        camera.setFrustum();
        GuiUniverseMap.universeCamera.setFrustum();
        if(this.save != null && !(this.currentGui instanceof GuiWorldLoadingScreen || this.currentGui instanceof GuiUniverseMap)) {
            GL46.glClearColor(this.save.activeWorld.skyColor[0], this.save.activeWorld.skyColor[1], this.save.activeWorld.skyColor[2], 0);
        } else {
            GL46.glClearColor(0,0,0,0);
        }
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
        GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);
        if(this.save != null && !(this.currentGui instanceof GuiWorldLoadingScreen || this.currentGui instanceof GuiUniverseMap)) {
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
        return GLFW.glfwCreateWindow(width, height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
    }



    private void initAllBufferObjects(){
        Chunk.initBuffers();
        Tessellator.initBuffers();
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
