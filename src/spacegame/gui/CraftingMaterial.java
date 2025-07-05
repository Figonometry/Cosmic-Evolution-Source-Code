package spacegame.gui;

import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;

public final class CraftingMaterial {
    public int width;
    public int height;
    public int x;
    public int y;
    public String type;
    public int color;
    public boolean active = true;

    public CraftingMaterial(int width, int height, int x, int y, String type){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        return this.color;
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }

    public void deactivate(){
        this.active = false;
    }
}
