package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.*;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class TextField implements Runnable {
   public int width;
   public int height;
   public int x;
   public int y;
   public String text = "";
   public boolean breakLoop;
   public boolean typing;


    public TextField(int width, int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void renderTextFieldAndText(){
        Tessellator tessellator = Tessellator.instance;
        TextureLoader textFieldOutline = new TextureLoader("src/spacegame/assets/textures/gui/outline32.png", 32, 32);
        TextureLoader cursor = new TextureLoader("src/spacegame/assets/textures/gui/fillableColor.png", 32, 32);

        tessellator.toggleOrtho();

        int outlineZ = -49;
        tessellator.addVertex2DTexture(16777215, this.x - this.width/2, this.y - this.height/2, outlineZ, 3);
        tessellator.addVertex2DTexture(16777215, this.x + this.width/2, this.y + this.height/2, outlineZ, 1);
        tessellator.addVertex2DTexture(16777215, this.x - this.width/2, this.y + this.height/2, outlineZ, 2);
        tessellator.addVertex2DTexture(16777215, this.x + this.width/2, this.y - this.height/2, outlineZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(textFieldOutline.texID, Shader.screen2DTexture, SpaceGame.camera);

        if(this.typing && this.text.length() < 28) {
            if(Timer.elapsedTime % 60 <= 30) {
                int cursorZ = -48;
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17), this.y - this.height / 2 + 5, cursorZ, 3);
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17) + 9, this.y + this.height / 2 - 5, cursorZ, 1);
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17), this.y + this.height / 2 - 5, cursorZ, 2);
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17) + 9, this.y - this.height / 2 + 5, cursorZ, 0);
                tessellator.addElements();
                tessellator.drawTexture2D(cursor.texID, Shader.screen2DTexture, SpaceGame.camera);
            }
        }

        tessellator.toggleOrtho();

        GL46.glDeleteTextures(textFieldOutline.texID);
        GL46.glDeleteTextures(cursor.texID);

        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawString(this.text, this.x - this.width/2, this.y - this.height/2,-15, 16777215, 50);
    }

    public void scanForInputText(){
        String next = null;
        while (true) {
            if (this.text.length() != 28) {
                if (!KeyListener.isKeyPressed(KeyMappings.getKeyCodeFromMap(next, 599))) {
                    boolean capsOriginalValue = KeyListener.capsLockEnabled;
                    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                        KeyListener.capsLockEnabled = !KeyListener.capsLockEnabled;
                    }
                    next = KeyMappings.getKeyNameFromMapForTextFields();
                    KeyListener.capsLockEnabled = capsOriginalValue;
                    if (next != null) {
                        this.text = this.text + next;
                    }
                    if (this.breakLoop) {
                        break;
                    }
                }
            }
            if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE) && KeyListener.backSpaceReleased && this.text.length() > 0) {
                char[] newTextCharacters = new char[this.text.length() - 1];
                for (int i = 0; i < this.text.length() - 1; i++) {
                    newTextCharacters[i] = this.text.charAt(i);
                }
                this.text = new String(newTextCharacters);
                KeyListener.backSpaceReleased = false;
            }
        }
    }


    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }

    @Override
    public void run() {
        this.typing = true;
        this.scanForInputText();
    }

    public void onLeftClick(){
        new Thread(this).start();
    }


}
