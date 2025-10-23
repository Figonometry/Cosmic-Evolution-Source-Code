package spacegame.gui;

import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Tech;

public final class ButtonTechTree extends Button {
    public int texture;
    public boolean active = true;
    public int width;
    public int height;
    public int x;
    public int y;
    public String name;
    public int era;
    private GuiTechTree techTree;

    public ButtonTechTree(String name, int width, int height, int x, int y, int era, GuiTechTree techTree){
        super(name, width, height, x, y, techTree, CosmicEvolution.instance);
        this.name = name;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.era = era;
        this.techTree = techTree;
        switch (this.era){
            case Tech.NEOLITHIC_ERA -> {
                this.texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/neolithicButton.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            }
            default -> {
                this.texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/emptyButton.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            }
        }
    }

    public void reloadTexture(){
        switch (this.era){
            case Tech.NEOLITHIC_ERA -> {
                this.texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/neolithicButton.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            }
            default -> {
                this.texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/emptyButton.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            }
        }
    }

    @Override
    public void onLeftClick(){
        if(this.era == this.techTree.eraDisplayed && !this.active)return;
        this.techTree.switchEraDisplayed(this.era);
    }
    @Override
    public void renderButton(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        if(this.era != this.techTree.eraDisplayed){
            if(this.isMouseHoveredOver()){
                tessellator.addVertex2DTexture(12713983, this.x - this.width/2, this.y - this.height/2, -20,3);
                tessellator.addVertex2DTexture(12713983, this.x + this.width/2, this.y + this.height/2, -20,1);
                tessellator.addVertex2DTexture(12713983, this.x - this.width/2, this.y + this.height/2, -20,2);
                tessellator.addVertex2DTexture(12713983, this.x + this.width/2, this.y - this.height/2, -20,0);
                tessellator.addElements();
            } else {
                tessellator.addVertex2DTexture(16777215, this.x - this.width/2, this.y - this.height/2, -20,3);
                tessellator.addVertex2DTexture(16777215, this.x + this.width/2, this.y + this.height/2, -20,1);
                tessellator.addVertex2DTexture(16777215, this.x - this.width/2, this.y + this.height/2, -20,2);
                tessellator.addVertex2DTexture(16777215, this.x + this.width/2, this.y - this.height/2, -20,0);
                tessellator.addElements();
            }
        } else {
            tessellator.addVertex2DTexture(5000268, this.x - this.width/2, this.y - this.height/2, -20,3);
            tessellator.addVertex2DTexture(5000268, this.x + this.width/2, this.y + this.height/2, -20,1);
            tessellator.addVertex2DTexture(5000268, this.x - this.width/2, this.y + this.height/2, -20,2);
            tessellator.addVertex2DTexture(5000268, this.x + this.width/2, this.y - this.height/2, -20,0);
            tessellator.addElements();
        }
        tessellator.drawTexture2D(this.texture, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString(this.name, this.x - 25, this.y - 25, -15,16777215, 50, 255);
    }

}
