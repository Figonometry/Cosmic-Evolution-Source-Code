package spacegame.gui;

import spacegame.core.MathUtil;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;

public final class RecipeSelector {
    public short itemID;
    public float x;
    public float y;
    public float width;
    public float height;


    public RecipeSelector(short itemID, float x, float y, float width, float height){
        this.itemID = itemID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        float adjustedButtonX = MathUtil.adjustXPosBasedOnScreenWidth(this.x);
        float adjustedButtonY = MathUtil.adjustYPosBasedOnScreenHeight(this.y);
        float adjustedButtonWidth = MathUtil.adjustWidthBasedOnScreenWidth(this.width);
        float adjustedButtonHeight = MathUtil.adjustHeightBasedOnScreenHeight(this.height);
        return x > adjustedButtonX - (double) adjustedButtonWidth / 2 && x < adjustedButtonX + (double) adjustedButtonWidth / 2 && y > adjustedButtonY - (double) adjustedButtonHeight / 2 && y < adjustedButtonY + (double) adjustedButtonHeight / 2;
    }
}
