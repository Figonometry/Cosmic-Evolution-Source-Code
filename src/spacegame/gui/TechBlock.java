package spacegame.gui;

import org.lwjgl.opengl.GL46;
import spacegame.core.MathUtil;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

import java.awt.*;
import java.util.ArrayList;

public final class TechBlock {
    public Tech tech;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean clicked;
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

    public static void adjustZoom(boolean zoomIn){
        zoomFactor += zoomIn ? 0.05f : -0.05f;
        if(zoomFactor < 0.01f){
            zoomFactor = 0.01f;
        }
    }


    public void renderTechBlock(){
        if(this.tech.isRootNode)return;
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTexture(this.getTechBlockColor(), (this.x - this.width/2) * zoomFactor, (this.y - this.height/2) * zoomFactor, -95,3);
        tessellator.addVertex2DTexture(this.getTechBlockColor(), (this.x + this.width/2) * zoomFactor, (this.y + this.height/2) * zoomFactor, -95,1);
        tessellator.addVertex2DTexture(this.getTechBlockColor(), (this.x - this.width/2) * zoomFactor, (this.y + this.height/2) * zoomFactor, -95,2);
        tessellator.addVertex2DTexture(this.getTechBlockColor(), (this.x + this.width/2) * zoomFactor, (this.y - this.height/2) * zoomFactor, -95,0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.techTree.techBlock, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString(this.tech.getTechName(), this.x * zoomFactor - (25f * zoomFactor), this.y * zoomFactor - (25f * zoomFactor), (float) -93, 16777215, (int) (35f * zoomFactor));
        this.drawConnectingLinesToChildren();
    }

    private void drawConnectingLinesToChildren(){
        ArrayList<TechBlock> childrenBlocks = new ArrayList<>();
        for(int i = 0; i < this.tech.unlockedTech.size(); i++){
            childrenBlocks.add(this.techTree.getTechBlockFromName(this.tech.unlockedTech.get(i), this.techTree.currentTechsToDisplay));
        }
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();


        int lineThickness = 25;
        for(int i = 0; i < childrenBlocks.size(); i++){
            tessellator.addVertex2DTexture(this.getLineColor(), (this.x + this.width/2) * zoomFactor, (this.y - lineThickness/2) * zoomFactor, -96, 3);
            tessellator.addVertex2DTexture(this.getLineColor(), (childrenBlocks.get(i).x - childrenBlocks.get(i).width/2) * zoomFactor, (childrenBlocks.get(i).y + lineThickness/2) * zoomFactor, -96, 1);
            tessellator.addVertex2DTexture(this.getLineColor(), (this.x + this.width/2) * zoomFactor, (this.y + lineThickness/2) * zoomFactor, -96, 2);
            tessellator.addVertex2DTexture(this.getLineColor(), (childrenBlocks.get(i).x - childrenBlocks.get(i).width/2) * zoomFactor, (childrenBlocks.get(i).y - lineThickness/2) * zoomFactor, -96, 0);
            tessellator.addElements();
        }

        tessellator.drawTexture2D(this.techTree.line, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    public void drawInfoBox(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();

        final float topLeftX = -960;
        final float topLeftY = 508.5f;
        final float width = 400;
        final float height = 700;

        tessellator.addVertex2DTexture(0, topLeftX, topLeftY - height, -50f, 3);
        tessellator.addVertex2DTexture(0, topLeftX + width, topLeftY + height, -50f, 1);
        tessellator.addVertex2DTexture(0, topLeftX, topLeftY + height, -50f, 2);
        tessellator.addVertex2DTexture(0, topLeftX + width, topLeftY - height, -50f, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.techTree.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();


        FontRenderer fontRenderer = FontRenderer.instance;//45 character limit at size 25 font
        if(this.tech.state == Tech.UNKNOWN){
            fontRenderer.drawCenteredString("???", topLeftX + width * 0.4f, 460,-49,16777215, 50);
            fontRenderer.toggleItalics();
            fontRenderer.drawString("This knowledge is shrouded in mystery", topLeftX, 400,  -49, 16777215, 25);
            fontRenderer.toggleItalics();
        } else {
            fontRenderer.drawCenteredString(this.tech.techName, topLeftX + width * 0.4f, 460, -49, this.tech.state == Tech.UNLOCKED ? 5046016 : 16766976, 50);
            fontRenderer.toggleItalics();
            int textHeight = 400;
            for(int i = 0; i < this.tech.infoText.size(); i++){
                fontRenderer.drawString(this.tech.infoText.get(i), topLeftX, textHeight, -49, 16777215, 25);
                textHeight -= 30;
            }
            fontRenderer.toggleItalics();

            if(this.tech.state != Tech.UNLOCKED) {
                int y = 60;
                for (int i = 0; i < this.tech.unlockDescriptions.size(); i++) {
                    fontRenderer.drawString("Unlocks: " + this.tech.unlockDescriptions.get(i), topLeftX, y, -49, 16777215, 30);
                    y -= 30;
                }
            }

            if(this.tech.state == Tech.LOCKED_KNOWN){
                textHeight -= 500;
                fontRenderer.drawCenteredString(this.tech.techUpdateEvent, topLeftX + width * 0.45f, textHeight, -49, 16766976, 40);
                textHeight -= 60;
                fontRenderer.drawCenteredString(this.tech.progressAmountCompleted + " / " + this.tech.unlockRequirementAmount, topLeftX + width * 0.45f, textHeight, -49, 16766976, 40);
            }

            if(this.tech.state == Tech.LOCKED){
                textHeight -= 500;
                fontRenderer.drawCenteredString("???", topLeftX + width * 0.45f, textHeight, -49, 16766976, 40);
                textHeight -= 60;
                fontRenderer.drawCenteredString("??? / ???", topLeftX + width * 0.45f, textHeight, -49, 16766976, 40);
            }

        }
    }

    private int getLineColor(){
        switch (this.tech.state){
            case Tech.UNKNOWN -> {
                return 0; //Black
            }
            case Tech.LOCKED_KNOWN, Tech.LOCKED -> {
                return 16721967; //Blue
            }
            case Tech.UNLOCKED -> {
                return 5766980; //Gold
            }
            default -> {
                return Color.red.getRGB();  //This should never have to execute, bright red for catching visually
            }
        }
    }

    private int getTechBlockColor(){
        switch (this.tech.state){
            case Tech.UNKNOWN -> {
                return 0; //Black
            }
            case Tech.LOCKED_KNOWN, Tech.LOCKED -> {
                return 5011199; //Blue
            }
            case Tech.UNLOCKED -> {
                return 16766976; //Gold
            }
            default -> {
                return Color.red.getRGB();  //This should never have to execute, bright red for catching visually
            }
        }
    }

    public boolean isMouseHoveredOver(){
        double x = (MouseListener.instance.xPos - SpaceGame.width/2D);
        double y = ((MouseListener.instance.yPos - SpaceGame.height/2D) * -1);
        float adjustedButtonX = MathUtil.adjustXPosBasedOnScreenWidth(this.x) * zoomFactor;
        float adjustedButtonY = MathUtil.adjustYPosBasedOnScreenHeight(this.y) * zoomFactor;
        float adjustedButtonWidth = MathUtil.adjustWidthBasedOnScreenWidth(this.width) * zoomFactor;
        float adjustedButtonHeight = MathUtil.adjustHeightBasedOnScreenHeight(this.height) * zoomFactor;
        return x > adjustedButtonX - (double) adjustedButtonWidth / 2 && x < adjustedButtonX + (double) adjustedButtonWidth / 2 && y > adjustedButtonY - (double) adjustedButtonHeight / 2 && y < adjustedButtonY + (double) adjustedButtonHeight / 2;
    }
}
