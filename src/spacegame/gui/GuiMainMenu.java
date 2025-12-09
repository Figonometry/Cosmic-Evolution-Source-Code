package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.util.MathUtil;
import spacegame.core.Timer;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

import java.awt.*;
import java.util.Random;

public final class GuiMainMenu extends Gui {
    public static final int starCount;
    public static final int[] starColors;
    public static final int[] starSize;
    public static final int[][] starCoordinates;
    public static final int[] starColorTimer;
    private Button singlePlayer;
    private Button multiPlayer;
    private Button settings;
    private Button quitGame;
    private Button bugReport;
    private Button information;
    public int star;
    public int earth;
    public int title;
    public int background;


    static {
        Random rand = new Random();
        starCount = rand.nextInt(1000,1500);
        starColors = new int[starCount];
        starSize = new int[starCount];
        starCoordinates = new int[starCount][3];
        starColorTimer = new int[starCount];
        Color color;
        for(int i = 0; i < starColors.length; i++){
            color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255);
            starColors[i] = color.getRGB() * -1;
        }
        for(int i = 0; i < starSize.length; i++){
            starSize[i] = rand.nextInt(2,25);
        }
        for(int i = 0; i < starCoordinates.length; i++){
            starCoordinates[i][0] = rand.nextInt(-CosmicEvolution.width/2, CosmicEvolution.width/2);
            starCoordinates[i][1] = rand.nextInt(-CosmicEvolution.height/2, CosmicEvolution.height/2);
            starCoordinates[i][2] = rand.nextInt(-10000, -1000);
        }
        for(int i = 0; i < starColorTimer.length; i++){
            starColorTimer[i] = rand.nextInt(5, 20);
        }
    }

    public GuiMainMenu(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.ce = cosmicEvolution;
        this.singlePlayer = new Button(EnumButtonEffects.SINGLE_PLAYER.name(), 512, 64,0,0,this, this.ce);
        this.multiPlayer = new Button(EnumButtonEffects.MULTI_PLAYER.name(), 512, 64, 0, -100, this, this.ce);
        this.multiPlayer.active = false;
        this.settings = new Button(EnumButtonEffects.SETTINGS.name(), 512, 64, 0, -200, this, this.ce);
        this.quitGame = new Button(EnumButtonEffects.QUIT_GAME.name(), 512, 64, 0, - 300, this, this.ce);
        this.bugReport = new Button(EnumButtonEffects.BUG_REPORT.name(), 64,64, 910, -458, this, this.ce);
        this.information = new Button(EnumButtonEffects.INFORMATION.name(), 64,64, -910, -458, this, this.ce);
    }

    @Override
    public void loadTextures() {
        this.star = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.earth = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/logo.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.background = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.star);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.earth);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.title);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.background);
    }

    @Override
    public void drawGui() {
        FontRenderer fontRenderer = FontRenderer.instance;
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        for(int i = 0; i < starCount; i++){
           renderStar(i, tessellator);
        }

        tessellator.drawTexture2D(this.star, Shader.screen2DTexture, CosmicEvolution.camera);


        int earthSize = 256;
        int earthX = 0;
        int earthY = 0;
        int earthZ = -900;
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY - earthSize, earthZ, 3);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY + earthSize, earthZ, 1);
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY + earthSize, earthZ, 2);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY - earthSize, earthZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.earth, Shader.screen2DTexture, CosmicEvolution.camera);

        int titleWidth = 1476;
        int titleHeight = 144;
        int titleX = 0;
        int titleY = 400;
        int titleZ = -700;
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY - titleHeight/2, titleZ, 3);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY + titleHeight/2, titleZ, 1);
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY + titleHeight/2, titleZ, 2);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY - titleHeight/2, titleZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.title, Shader.screen2DTexture, CosmicEvolution.camera);

        tessellator.toggleOrtho();

        if(this.subMenu){
            int backgroundWidth = 1600;
            int backgroundHeight = 900;
            int backgroundX = 0;
            int backgroundY = 0;
            int backgroundZ = -100;
            int y = backgroundHeight/2 - 150;
            int x = -backgroundWidth/2;
            int depth = -15;
            int white = 16777215;
            int green = Color.magenta.getRGB() * -1;
            int red = 16711680;
            int yellow = Color.blue.getRGB() * -1;
            fontRenderer.drawString("This game is in early Alpha, expect there to be bugs, crashes and general instability", x, y, depth, red, 50, 255);
            y -= 60;
            fontRenderer.drawString(this.ce.title, x, y, depth, 16711875, 50, 255);
            y -= 60;
            fontRenderer.toggleItalics();
            fontRenderer.drawString("Added saturation, decreases passively over time, quicker when the player is hurt", x, y, depth, green, 50, 255);
            y -= 30;
            fontRenderer.drawString("Health now passively regens", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("The player can now starve if saturation is 0", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Food items now restore saturation", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Buffed the saturation increase of berries from 5 to 50", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Added saturation bar to the in game UI", x,y ,depth, green, 50, 255);
            y-= 30;
            fontRenderer.drawString("Reduced the size of the health bar on in game UI", x,y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Changed the color of the health bar", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Tall grass should no longer stretch too far from wind effect", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Correct sound now plays when standing on top of a tree", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Items can no longer be placed floating above the ground", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Torches, pit kilns, and campfires will now extinguish when under rainfall", x,y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Added unlit torch blocks, can be ignited by shift-right clicking while holding a lit torch", x, y, depth, green, 50, 255);
            y -= 30;
            fontRenderer.drawString("Torches will now burn out after two in game days", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Torches now have proper display name", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Raw venison can now be stacked up to 64", x, y,depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Torches can now be used to light fires", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Right clicking on a campfire will now open a UI to cook food", x, y, depth, yellow, 50, 255);
            y -= 30;
            fontRenderer.drawString("Added cooked venison, made by cooking venison on a campfire", x, y, depth, green, 50, 255);
            y -= 30;
            fontRenderer.drawString("Firewood can now be used as fuel in a campfire", x, y, depth, yellow, 50, 255);
            fontRenderer.toggleItalics();



            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2f, backgroundY - backgroundHeight/2f, backgroundZ, 3);
            tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2f, backgroundY + backgroundHeight/2f, backgroundZ, 1);
            tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2f, backgroundY + backgroundHeight/2f, backgroundZ, 2);
            tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2f, backgroundY - backgroundHeight/2f, backgroundZ, 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.background, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            GL46.glDisable(GL46.GL_BLEND);
        }

        if(!this.subMenu && !this.subMenu2) {
            this.singlePlayer.renderButton();
            this.multiPlayer.renderButton();
            this.settings.renderButton();
            this.quitGame.renderButton();
        }
        this.bugReport.renderButton();
        this.information.renderButton();


        if(this.subMenu2){
            int backgroundWidth = 1600;
            int backgroundHeight = 900;
            int backgroundX = 0;
            int backgroundY = 0;
            int backgroundZ = -100;
            int y = backgroundHeight/2 - 150;
            int x = -backgroundWidth/2;
            int depth = -15;
            int white = 16777215;
            int green = Color.magenta.getRGB() * -1;
            int red = 16711680;
            int yellow = Color.blue.getRGB() * -1;
            fontRenderer.drawString("Report all bugs to the following email address: cosmicevolution.bugreport@gmail.com", x, y, depth, red, 50, 255);
            y -= 60;
            fontRenderer.drawString("Please include screenshots and crash logs as applicable as well as a description", x, y, depth, green, 50, 255);
            y -= 60;

            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 3);
            tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 1);
            tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 2);
            tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.background, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            GL46.glDisable(GL46.GL_BLEND);
        }

        fontRenderer.drawString(this.ce.title, -970, 460, -15, Color.magenta.getRGB() * -1, 50, 255);

    }

    @Override
    public Button getActiveButton() {
        if(this.singlePlayer.isMouseHoveredOver() && this.singlePlayer.active && !this.subMenu){
            return this.singlePlayer;
        }
        if(this.multiPlayer.isMouseHoveredOver() && this.multiPlayer.active && !this.subMenu){
            return this.multiPlayer;
        }
        if(this.settings.isMouseHoveredOver() && this.settings.active && !this.subMenu){
            return this.settings;
        }
        if(this.quitGame.isMouseHoveredOver() && this.quitGame.active && !this.subMenu){
            return this.quitGame;
        }
        if(this.bugReport.isMouseHoveredOver() && this.bugReport.active){
            return this.bugReport;
        }
        if(this.information.isMouseHoveredOver() && this.information.active){
            return this.information;
        }
        return null;
    }

    public static void renderStar(int starNumber, RenderEngine.Tessellator tessellator){
        float x = starCoordinates[starNumber][0];
        float y = starCoordinates[starNumber][1];
        float z = starCoordinates[starNumber][2];
        float size = starSize[starNumber];
        int color = changeStarColor(starNumber);

        tessellator.addVertex2DTexture(color, x - size, y - size, z, 3);
        tessellator.addVertex2DTexture(color, x + size, y + size, z, 1);
        tessellator.addVertex2DTexture(color, x - size, y + size, z, 2);
        tessellator.addVertex2DTexture(color, x + size, y - size, z, 0);
        tessellator.addElements();
    }

    private static int changeStarColor(int starNumber){
        Color color = new Color(starColors[starNumber]);
        float[] colorComponents = new float[3];
        colorComponents = color.getColorComponents(colorComponents);
        float red = colorComponents[0];
        float green = colorComponents[1];
        float blue = colorComponents[2];



        red *= (MathUtil.sin((float) ((double) Timer.elapsedTime /starColorTimer[starNumber])) * 0.25F) + 0.75F;
        green *= (MathUtil.sin((float) ((double) Timer.elapsedTime / starColorTimer[starNumber])) * 0.25F) + 0.75F;
        blue *= (MathUtil.sin((float) ((double) Timer.elapsedTime / starColorTimer[starNumber])) * 0.25F) + 0.75F;

        color = new Color(red,green,blue);
        return color.getRGB() * -1;
    }
}
