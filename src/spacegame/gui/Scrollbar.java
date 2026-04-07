package spacegame.gui;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.render.Texture;
import spacegame.util.MathUtil;

public final class Scrollbar {
    public float x;
    public float y;
    public float width;
    public float height;
    public boolean clicked;
    public float upperBound;
    public float lowerBound;
    public float additionalY;
    public int scrolls = 0;
    public int maxScrolls;
    float pixelStep;

    public Scrollbar(float x, float y, float width, float height, float upperBound, float lowerBound, int maxScrolls){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.maxScrolls = maxScrolls;
    }

    public void calculatePixelStep(){
        float distanceInPixelsFromLowerBoundToBottomOfScrollbar = (this.y - this.height/2) - this.lowerBound;
        this.pixelStep = distanceInPixelsFromLowerBoundToBottomOfScrollbar / maxScrolls;
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - CosmicEvolution.width/2D;
        double y = (MouseListener.instance.yPos - CosmicEvolution.height/2D) * -1;
        float adjustedButtonX = MathUtil.adjustXPosBasedOnScreenWidth(this.x);
        float adjustedButtonY = MathUtil.adjustYPosBasedOnScreenHeight(this.y);
        float adjustedButtonWidth = MathUtil.adjustWidthBasedOnScreenWidth(this.width);
        float adjustedButtonHeight = MathUtil.adjustHeightBasedOnScreenHeight(this.height);
        return x > adjustedButtonX - (double) adjustedButtonWidth / 2 && x < adjustedButtonX + (double) adjustedButtonWidth / 2 && y > adjustedButtonY - (double) adjustedButtonHeight / 2 && y < adjustedButtonY + (double) adjustedButtonHeight / 2;
    }

    public void render(int image){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTexture( 128 << 16 | 128 << 8 | 128,this.x - this.width/2, this.y - this.height/2, -20,3);
        tessellator.addVertex2DTexture( 128 << 16 | 128 << 8 | 128,this.x + this.width/2, this.y + this.height/2, -20,1);
        tessellator.addVertex2DTexture( 128 << 16 | 128 << 8 | 128,this.x - this.width/2, this.y + this.height/2, -20,2);
        tessellator.addVertex2DTexture( 128 << 16 | 128 << 8 | 128,this.x + this.width/2, this.y - this.height/2, -20,0);
        tessellator.addElements();
        tessellator.drawTexture2D(image, Shader.screen2DTextureAtlas, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }

    public void move(boolean moveDown){
        this.scrolls += moveDown ? 1 : -1;

        if(this.scrolls > this.maxScrolls){
            this.scrolls = this.maxScrolls;
            return;
        }
        if(this.scrolls < 0){
            this.scrolls = 0;
            return;
        }

        float pixelMovement = 100;
        this.additionalY += moveDown ? pixelMovement : -pixelMovement;

        this.y += moveDown ? -this.pixelStep : this.pixelStep;

    }
}
