package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

import java.util.Random;

public final class GuiCreateNewWorld extends Gui {
    private SpaceGame sg;
    public TextureLoader background;
    public TextureLoader title;
    public TextureLoader star;
    public TextureLoader earth;
    public TextField nameWorld;
    public TextField setSeed;
    public Button createWorld;
    public Button back;
    public int saveSlot;

    public GuiCreateNewWorld(SpaceGame spaceGame, int saveSlot) {
        super(spaceGame);
        this.sg = spaceGame;
        this.saveSlot = saveSlot;
        this.createWorld = new Button(EnumButtonEffects.CREATE_NEW_WORLD.name(), 512, 64, -320, -200, this, this.sg);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 320, -200, this, this.sg);
        this.nameWorld = new TextField(512, 64, 0, 100);
        this.setSeed = new TextField(512, 64, 0, 0);
    }


    @Override
    public void loadTextures() {
        this.star = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/star.png", 64,  64);
        this.earth = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", 512,512);
        this.title = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/createNewWorld.png", 1204,144);
        this.background = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32,32);
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.star.texID);
        GL46.glDeleteTextures(this.earth.texID);
        GL46.glDeleteTextures(this.title.texID);
        GL46.glDeleteTextures(this.background.texID);
        this.star = null;
        this.earth = null;
        this.title = null;
        this.background = null;
    }

    @Override
    public void drawGui() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        for(int i = 0; i < GuiMainMenu.starCount; i++){
            GuiMainMenu.renderStar(i, tessellator);
        }
        tessellator.drawTexture2D(this.star.texID, Shader.screen2DTexture, SpaceGame.camera);

        int titleWidth = 1204;
        int titleHeight = 144;
        int titleX = 0;
        int titleY = 400;
        int titleZ = -50;
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY - titleHeight/2, titleZ, 3);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY + titleHeight/2, titleZ, 1);
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY + titleHeight/2, titleZ, 2);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY - titleHeight/2, titleZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.title.texID, Shader.screen2DTextureAtlas, SpaceGame.camera);

        int earthSize = 256;
        int earthX = 0;
        int earthY = 0;
        int earthZ = -900;
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY - earthSize, earthZ, 3);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY + earthSize, earthZ, 1);
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY + earthSize, earthZ, 2);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY - earthSize, earthZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.earth.texID, Shader.worldShader2DTexture, SpaceGame.camera);

        int backgroundWidth = SpaceGame.width;
        int backgroundHeight = SpaceGame.height;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -100;
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.background.texID, Shader.worldShader2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();

        if(this.nameWorld.text.length() == 0 || this.doesStringContainAllSpaces(this.nameWorld.text)){
            this.createWorld.active = false;
        } else {
            this.createWorld.active = true;
        }

        this.createWorld.renderButton();
        this.back.renderButton();
        this.nameWorld.renderTextFieldAndText();
        this.setSeed.renderTextFieldAndText();
        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Name World", -384, 75, 16777215);
        fontRenderer.drawCenteredString("Set Seed", -384, -25, 16777215);
    }

    @Override
    public Button getActiveButton() {
        if(this.createWorld.isMouseHoveredOver() && this.createWorld.active){
            return this.createWorld;
        }
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        return null;
    }

    @Override
    public TextField getTextField(){
        if(this.setSeed.isMouseHoveredOver()){
            return this.setSeed;
        }
        if(this.nameWorld.isMouseHoveredOver()){
            return this.nameWorld;
        }
        return null;
    }

    public boolean doesStringContainAllSpaces(String string){
        if(string.length() == 0){
            return true;
        } else {
            for(int i = 0; i < string.length(); i++){
                if(string.charAt(i) != ' '){
                    return false;
                }
            }
        }
        return true;
    }

    public long getSeed() {
        if (this.setSeed.text.length() == 0 || this.doesStringContainAllSpaces(this.setSeed.text)) {
            return new Random().nextLong();
        } else {
            String string = "";
            for (int i = 0; i < this.setSeed.text.length(); i++) {
                if (this.setSeed.text.charAt(i) != '0' && this.setSeed.text.charAt(i) != '1' && this.setSeed.text.charAt(i) != '2' && this.setSeed.text.charAt(i) != '3' && this.setSeed.text.charAt(i) != '4' && this.setSeed.text.charAt(i) != '5' && this.setSeed.text.charAt(i) != '6' && this.setSeed.text.charAt(i) != '7' && this.setSeed.text.charAt(i) != '8' && this.setSeed.text.charAt(i) != '9'){
                    string += this.convertLetterToNumber(this.setSeed.text.charAt(i));
                } else {
                    string += this.setSeed.text.charAt(i);
                }
            }
            return Long.parseLong(string);
        }
    }

    private int convertLetterToNumber(char character){
        switch (character){
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            case 'i':
                return 9;
            case 'j':
                return 10;
            case 'k':
                return 11;
            case 'l':
                return 12;
            case 'm':
                return 13;
            case 'n':
                return 14;
            case 'o':
                return 15;
            case 'p':
                return 16;
            case 'q':
                return 17;
            case 'r':
                return 18;
            case 's':
                return 19;
            case 't':
                return 20;
            case 'u':
                return 21;
            case 'v':
                return 22;
            case 'w':
                return 23;
            case 'x':
                return 24;
            case 'y':
                return 25;
            case 'z':
                return 26;
            case 'A':
                return 27;
            case 'B':
                return 28;
            case 'C':
                return 29;
            case 'D':
                return 30;
            case 'E':
                return 31;
            case 'F':
                return 32;
            case 'G':
                return 33;
            case 'H':
                return 34;
            case 'I':
                return 35;
            case 'J':
                return 36;
            case 'K':
                return 37;
            case 'L':
                return 38;
            case 'M':
                return 39;
            case 'N':
                return 40;
            case 'O':
                return 41;
            case 'P':
                return 42;
            case 'Q':
                return 43;
            case 'R':
                return 44;
            case 'S':
                return 45;
            case 'T':
                return 46;
            case 'U':
                return 47;
            case 'V':
                return 48;
            case 'W':
                return 49;
            case 'X':
                return 50;
            case 'Y':
                return 51;
            case 'Z':
                return 52;
            case ' ':
                return 53;

        }
        return 1200;
    }
}
