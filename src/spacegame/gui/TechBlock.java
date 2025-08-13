package spacegame.gui;

import spacegame.core.MathUtils;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.world.Tech;

import java.util.ArrayList;

public final class TechBlock {
    public Tech tech;
    public int x;
    public int y;
    public int width;
    public int height;
    private GuiTechTree techTree;
    public static float zoomFactor = 1;

    public TechBlock(Tech tech, int x, int y, int width, int height, GuiTechTree techTree){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tech = tech;
        this.techTree = techTree;
    }

    public void movePosition(float x, float y){
        this.x += x;
        this.y += y;
    }

    public void adjustZoom(boolean zoomIn){
        zoomFactor += zoomIn ? 0.01f : -0.01f;
    }


    public void renderTechBlock(){
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTexture(16777215, (this.x - this.width/2) * zoomFactor, (this.y - this.height/2) * zoomFactor, -95,3);
        tessellator.addVertex2DTexture(16777215, (this.x + this.width/2) * zoomFactor, (this.y + this.height/2) * zoomFactor, -95,1);
        tessellator.addVertex2DTexture(16777215, (this.x - this.width/2) * zoomFactor, (this.y + this.height/2) * zoomFactor, -95,2);
        tessellator.addVertex2DTexture(16777215, (this.x + this.width/2) * zoomFactor, (this.y - this.height/2) * zoomFactor, -95,0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.techTree.techBlock.texID, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString(this.tech.techName, this.x * zoomFactor - (25f * zoomFactor), this.y * zoomFactor - (25f * zoomFactor), (float) -93, 16777215, (int) (35f * zoomFactor));
        this.drawConnectingLinesToChildren();
    }

    private void drawConnectingLinesToChildren(){
        ArrayList<TechBlock> childrenBlocks = new ArrayList<>();
        for(int i = 0; i < this.tech.unlockedTech.size(); i++){
            childrenBlocks.add(this.techTree.getTechBlockFromName(this.tech.unlockedTech.get(i), this.techTree.currentTechsToDisplay));
        }
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();


        int lineThickness = 25;
        for(int i = 0; i < childrenBlocks.size(); i++){
            tessellator.addVertex2DTexture(0, (this.x + this.width/2) * zoomFactor, (this.y - lineThickness/2) * zoomFactor, -96, 3);
            tessellator.addVertex2DTexture(0, (childrenBlocks.get(i).x - childrenBlocks.get(i).width/2) * zoomFactor, (childrenBlocks.get(i).y + lineThickness/2) * zoomFactor, -96, 1);
            tessellator.addVertex2DTexture(0, (this.x + this.width/2) * zoomFactor, (this.y + lineThickness/2) * zoomFactor, -96, 2);
            tessellator.addVertex2DTexture(0, (childrenBlocks.get(i).x - childrenBlocks.get(i).width/2) * zoomFactor, (childrenBlocks.get(i).y - lineThickness/2) * zoomFactor, -96, 0);
            tessellator.addElements();
        }

        tessellator.drawTexture2D(this.techTree.line.texID, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        float adjustedButtonX = MathUtils.adjustXPosBasedOnScreenWidth(this.x);
        float adjustedButtonY = MathUtils.adjustYPosBasedOnScreenHeight(this.y);
        float adjustedButtonWidth = MathUtils.adjustWidthBasedOnScreenWidth(this.width);
        float adjustedButtonHeight = MathUtils.adjustHeightBasedOnScreenHeight(this.height);
        return x > adjustedButtonX - (double) adjustedButtonWidth / 2 && x < adjustedButtonX + (double) adjustedButtonWidth / 2 && y > adjustedButtonY - (double) adjustedButtonHeight / 2 && y < adjustedButtonY + (double) adjustedButtonHeight / 2;
    }
}
