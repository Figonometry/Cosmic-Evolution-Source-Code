package spacegame.gui;

import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.render.Texture;

public final class Slider extends Button {
    public Slider(String name, int width, int height, int x, int y, spacegame.gui.Gui Gui, CosmicEvolution cosmicEvolution) {
        super(name, width, height, x, y, Gui, cosmicEvolution);
    }


    public void renderButton() {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        Texture buttonTexture;
        float textureID;
        buttonTexture = buttonTextureAtlas.getTexture(0);
        textureID = 0;

        tessellator.addVertex2DTextureWithAtlas(16777215, this.x - this.width / 2, this.y - this.height / 2, -20, 3, buttonTexture, textureID, 255);
        tessellator.addVertex2DTextureWithAtlas(16777215, this.x + this.width / 2, this.y + this.height / 2, -20, 1, buttonTexture, textureID, 255);
        tessellator.addVertex2DTextureWithAtlas(16777215, this.x - this.width / 2, this.y + this.height / 2, -20, 2, buttonTexture, textureID, 255);
        tessellator.addVertex2DTextureWithAtlas(16777215, this.x + this.width / 2, this.y - this.height / 2, -20, 0, buttonTexture, textureID, 255);
        tessellator.addElementsCW();
        tessellator.drawTexture2DWithAtlas(buttonTextureLoader, Shader.screen2DTextureAtlas, CosmicEvolution.camera);


        //Draw the slider position at -15
        float optionValue = this.getOptionValue();
        float optionMinValue = this.getOptionMinValue();
        float optionMaxValue = this.getOptionMaxValue();
        float sliderPos = this.x - ((float) this.width /2) + 15;

        float ratio = (optionValue - optionMinValue) / (optionMaxValue - optionMinValue);
        sliderPos += (this.width - 30) * ratio;

        tessellator.addVertex2DTexture(128 << 16 | 128 << 8 | 128, sliderPos - 7.5f, this.y - this.height / 2 + 4, -16, 3);
        tessellator.addVertex2DTexture(128 << 16 | 128 << 8 | 128, sliderPos + 7.5f, this.y + this.height / 2 - 4, -16, 1);
        tessellator.addVertex2DTexture(128 << 16 | 128 << 8 | 128, sliderPos - 7.5f, this.y + this.height / 2 - 4, -16, 2);
        tessellator.addVertex2DTexture(128 << 16 | 128 << 8 | 128, sliderPos + 7.5f, this.y - this.height / 2 + 4, -16, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(sliderPosition, Shader.screen2DTexture, CosmicEvolution.camera);


        tessellator.toggleOrtho();
        this.drawCenteredString();
    }
}
