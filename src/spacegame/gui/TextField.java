package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import spacegame.core.*;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class TextField {
   public int width;
   public int height;
   public int x;
   public int y;
   public String text = "";
   public String next = null;
   public boolean typing;


    public TextField(int width, int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void renderTextFieldAndText(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        int textFieldOutline = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/outline32.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        int cursor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);

        tessellator.toggleOrtho();

        int outlineZ = -49;
        tessellator.addVertex2DTexture(16777215, this.x - this.width/2, this.y - this.height/2, outlineZ, 3);
        tessellator.addVertex2DTexture(16777215, this.x + this.width/2, this.y + this.height/2, outlineZ, 1);
        tessellator.addVertex2DTexture(16777215, this.x - this.width/2, this.y + this.height/2, outlineZ, 2);
        tessellator.addVertex2DTexture(16777215, this.x + this.width/2, this.y - this.height/2, outlineZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(textFieldOutline, Shader.screen2DTexture, CosmicEvolution.camera);

        if(this.typing && this.text.length() < 28) {
            if(Timer.elapsedTime % 60 <= 30) {
                int cursorZ = -48;
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17), this.y - this.height / 2 + 5, cursorZ, 3);
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17) + 9, this.y + this.height / 2 - 5, cursorZ, 1);
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17), this.y + this.height / 2 - 5, cursorZ, 2);
                tessellator.addVertex2DTexture(16777215, this.x - this.width / 2 + ((this.text.length() + 1.2F) * 17) + 9, this.y - this.height / 2 + 5, cursorZ, 0);
                tessellator.addElements();
                tessellator.drawTexture2D(cursor, Shader.screen2DTexture, CosmicEvolution.camera);
            }
        }

        tessellator.toggleOrtho();

        CosmicEvolution.instance.renderEngine.deleteTexture(textFieldOutline);
        CosmicEvolution.instance.renderEngine.deleteTexture(cursor);

        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawString(this.text, this.x - this.width/2, this.y - this.height/2,-15, 16777215, 50, 255);
    }

    public void scanForInputText() {
        if (this.text.length() != 28) {
            if (!KeyListener.isKeyPressed(KeyMappings.getKeyCodeFromMap(next, 599))) {
                boolean capsOriginalValue = KeyListener.capsLockEnabled;
                if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                    KeyListener.capsLockEnabled = !KeyListener.capsLockEnabled;
                }
                this.next = KeyMappings.getKeyNameFromMapForTextFields();
                KeyListener.capsLockEnabled = capsOriginalValue;
                if (this.next != null) {
                    this.text = this.text + this.next;
                }
            }
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE) && KeyListener.keyReleased[GLFW.GLFW_KEY_BACKSPACE] && this.text.length() > 0) {
            char[] newTextCharacters = new char[this.text.length() - 1];
            for (int i = 0; i < this.text.length() - 1; i++) {
                newTextCharacters[i] = this.text.charAt(i);
            }
            this.text = new String(newTextCharacters);
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_BACKSPACE);
        }
    }


    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - CosmicEvolution.width/2D;
        double y = (MouseListener.instance.yPos - CosmicEvolution.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }


    public void onLeftClick(){
       this.typing = true;
    }


}
