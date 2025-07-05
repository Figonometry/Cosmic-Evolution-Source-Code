package spacegame.gui;

import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;

public final class MoveableObject {
    public float staringX;
    public float startingY;
    public long timePickedUp;
    public boolean pickedUp;
    public float x;
    public float y;
    public float width;
    public float height;

    public MoveableObject(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(int textureID){
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        float z = -50;
        float width = 96;
        float height = 96;
        tessellator.addVertex2DTexture(16777215, this.x - width/2, this.y - height/2, z, 3);
        tessellator.addVertex2DTexture(16777215, this.x + width/2, this.y + height/2, z, 1);
        tessellator.addVertex2DTexture(16777215, this.x - width/2, this.y + height/2, z, 2);
        tessellator.addVertex2DTexture(16777215, this.x + width/2, this.y - height/2, z, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(textureID, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }
}
