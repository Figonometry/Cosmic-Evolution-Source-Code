package spacegame.item;


import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.gui.FontRenderer;
import spacegame.gui.GuiInGame;
import spacegame.gui.GuiInventory;
import spacegame.gui.GuiInventoryPlayer;
import spacegame.render.*;

import java.awt.*;

public final class ItemStack {
    public Item item;
    public String exclusiveItemType;
    public boolean usesExclusiveItem;
    public short metadata;
    public byte count;
    public short durability;
    public float x;
    public float y;
    public final float originalX;
    public final float originalY;
    public static ItemStack itemStackOnMouse = new ItemStack(null, (byte) 0, 0, 0);
    public int width = 64;
    public int height = 64;


    public ItemStack(Item item, byte count, float x, float y){
        this.item = item;
        this.count = count;
        if(this.item != null) {
            this.durability = this.item.durability;
        }
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
    }

    public ItemStack(Item item, byte count, float x, float y, String exclusiveItemType){
        this.item = item;
        this.count = count;
        if(this.item != null) {
            this.durability = this.item.durability;
        }
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.usesExclusiveItem = true;
        this.exclusiveItemType = exclusiveItemType;
    }

    public void setMetadata(short metadata){
        this.metadata = metadata;
    }




    public void renderItemStack(boolean renderWithBackground){ //If the special inventory slots have an item draw a square directly behind it with the same color as the background for the player UI.
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        if(renderWithBackground) {
            tessellator.toggleOrtho();
            int z = -80;
            if(this.usesExclusiveItem && this.item != null){
                tessellator.addVertex2DTexture(7500402, this.x - (float) this.width / 2, this.y - (float) this.height / 2, z - 5, 3);
                tessellator.addVertex2DTexture(7500402, this.x + (float) this.width / 2, this.y + (float) this.height / 2, z - 5, 1);
                tessellator.addVertex2DTexture(7500402, this.x - (float) this.width / 2, this.y + (float) this.height / 2, z - 5, 2);
                tessellator.addVertex2DTexture(7500402, this.x + (float) this.width / 2, this.y - (float) this.height / 2, z - 5, 0);
                tessellator.addElements();
                tessellator.drawTexture2D(GuiInventoryPlayer.fillableColor, Shader.screen2DTexture, SpaceGame.camera);
            }
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.addVertex2DTexture(0, this.x - (float) this.width / 2, this.y - (float) this.height / 2, z, 3);
            tessellator.addVertex2DTexture(0, this.x + (float) this.width / 2, this.y + (float) this.height / 2, z, 1);
            tessellator.addVertex2DTexture(0, this.x - (float) this.width / 2, this.y + (float) this.height / 2, z, 2);
            tessellator.addVertex2DTexture(0, this.x + (float) this.width / 2, this.y - (float) this.height / 2, z, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(GuiInventory.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
            GL46.glDisable(GL46.GL_BLEND);
            tessellator.toggleOrtho();
        }

        if(this.item != null){
            tessellator.toggleOrtho();
            if(this.item.renderItemWithBlockModel){
                ModelLoader model = Block.list[this.metadata].blockModel.copyModel().translateModel(-0.5f, 0, -0.5f).getScaledModel(38);
                ModelFace[] faces;
                float textureID;
                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                Vector3f position = new Vector3f(this.x, this.y - 16,-70);
                int red = 255;
                int green = 255;
                int blue = 255;
                float[] UVSamples;
                for(int face = 0; face < 6; face++){
                    faces = model.getModelFaceOfType(face);
                    for(int i = 0; i < faces.length; i++){
                        if(faces[i] == null)continue;
                        textureID = Block.list[this.metadata].getBlockTexture(this.metadata, face);
                        UVSamples = face == RenderBlocks.TOP_FACE || face == RenderBlocks.BOTTOM_FACE ? RenderBlocks.autoUVTopBottom(RenderBlocks.getFaceWidth(faces[i]), RenderBlocks.getFaceHeight(faces[i])) : RenderBlocks.autoUVNSEW(RenderBlocks.getFaceWidth(faces[i]), RenderBlocks.getFaceHeight(faces[i]));
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);

                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex1.x, vertex1.y, vertex1.z, 3, textureID, UVSamples[0], UVSamples[1]);
                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex2.x, vertex2.y, vertex2.z, 1, textureID, UVSamples[2], UVSamples[3]);
                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex3.x, vertex3.y, vertex3.z, 2, textureID, UVSamples[4], UVSamples[5]);
                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex4.x, vertex4.y, vertex4.z, 0, textureID, UVSamples[6], UVSamples[7]);
                        tessellator.addElements();

                        red -= 10;
                        green -= 10;
                        blue -= 10;
                    }
                }
                tessellator.drawVertexArray(Assets.blockTextureArray, Shader.screenTextureArray, SpaceGame.camera);
            } else {
                int z = -70;
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.addVertexTextureArray(16777215, this.x - (float) this.width / 2, this.y - (float) this.height / 2, z, 3, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.x + (float) this.width / 2, this.y + (float) this.height / 2, z, 1, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.x - (float) this.width / 2, this.y + (float) this.height / 2, z, 2, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.x + (float) this.width / 2, this.y - (float) this.height / 2, z, 0, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addElements();
                Shader.screenTextureArray.uploadInt("textureArray", 0);
                tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, SpaceGame.camera);
                GL46.glDisable(GL46.GL_BLEND);
            }
            tessellator.toggleOrtho();
            if(this.count > 1) {
                FontRenderer fontRenderer = FontRenderer.instance;
                fontRenderer.drawString(Byte.toString(this.count), (int) this.x - 48, (int) (this.y - 48),-15, 16777215, 50);
            }

            this.renderDurabilityBar();
        }
    }

    public void renderItemStackOnHotbar(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

        if(this.item != null){
            tessellator.toggleOrtho();
            if(this.item.renderItemWithBlockModel){
                ModelLoader model = Block.list[this.metadata].blockModel.copyModel().translateModel(-0.5f, 0, -0.5f).getScaledModel(38);
                ModelFace[] faces;
                float textureID;
                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                Vector3f position = new Vector3f(this.x + 4, this.y - 12,-70);
                int red = 255;
                int green = 255;
                int blue = 255;
                float[] UVSamples;
                for(int face = 0; face < 6; face++){
                    faces = model.getModelFaceOfType(face);
                    for(int i = 0; i < faces.length; i++){
                        if(faces[i] == null)continue;
                        textureID = Block.list[this.metadata].getBlockTexture(this.metadata, face);
                        UVSamples = face == RenderBlocks.TOP_FACE || face == RenderBlocks.BOTTOM_FACE ? RenderBlocks.autoUVTopBottom(RenderBlocks.getFaceWidth(faces[i]), RenderBlocks.getFaceHeight(faces[i])) : RenderBlocks.autoUVNSEW(RenderBlocks.getFaceWidth(faces[i]), RenderBlocks.getFaceHeight(faces[i]));
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);

                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex1.x, vertex1.y, vertex1.z, 3, textureID, UVSamples[0], UVSamples[1]);
                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex2.x, vertex2.y, vertex2.z, 1, textureID, UVSamples[2], UVSamples[3]);
                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex3.x, vertex3.y, vertex3.z, 2, textureID, UVSamples[4], UVSamples[5]);
                        tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex4.x, vertex4.y, vertex4.z, 0, textureID, UVSamples[6], UVSamples[7]);
                        tessellator.addElements();

                        red -= 10;
                        green -= 10;
                        blue -= 10;
                    }
                }
                tessellator.drawVertexArray(Assets.blockTextureArray, Shader.screenTextureArray, SpaceGame.camera);
            } else {
                int z = -70;
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.addVertexTextureArray(16777215, this.x - (float) this.width / 2 + 5, this.y - (float) this.height / 2 - 2, z, 3, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.x + (float) this.width / 2 + 5, this.y + (float) this.height / 2 - 2, z, 1, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.x - (float) this.width / 2 + 5, this.y + (float) this.height / 2 - 2, z, 2, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.x + (float) this.width / 2 + 5, this.y - (float) this.height / 2 - 2, z, 0, this.item.getTextureID(this.item.ID, (byte)1, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addElements();
                Shader.screenTextureArray.uploadInt("textureArray", 0);
                tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, SpaceGame.camera);
                GL46.glDisable(GL46.GL_BLEND);
            }
            tessellator.toggleOrtho();
            if(this.count > 1) {
                FontRenderer fontRenderer = FontRenderer.instance;
                fontRenderer.drawString(Byte.toString(this.count), (int) this.x - 48, (int) (this.y - 48),-15, 16777215, 50);
            }
            this.renderDurabilityBar();
        }
    }

    public void adjustStackPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void resetStackPosition(){
        this.x = this.originalX;
        this.y = this.originalY;
    }

    public void mergeStack(ItemStack incomingStack){
        this.count += incomingStack.count;
    }

    public ItemStack splitStack(){
        ItemStack newStack =  new ItemStack(this.item, this.count, this.x, this.y);
        newStack.setMetadata(this.metadata);
        if((this.count & 1) == 1){
            newStack.count = (byte) (this.count/2);
            newStack.count++;
            this.count /= 2;
        } else {
            newStack.count = (byte) (this.count/2);
            this.count /= 2;
        }
        return newStack;
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - SpaceGame.width/2D;
        double y = (MouseListener.instance.yPos - SpaceGame.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }

    private void renderDurabilityBar(){
        float ratio = (float) this.durability / Item.list[this.item.ID].durability;
        if(ratio > 1 || ratio < 0){return;}
        if(ratio == 1 || this.durability <= 0){return;}
        int texID = 0;
        if(SpaceGame.instance.currentGui instanceof GuiInGame){
            texID = GuiInGame.fillableColor;
        } else if(SpaceGame.instance.currentGui instanceof GuiInventory){
            texID = GuiInventory.fillableColor;
        }
        float x = this.x - 32;
        float y = this.y - 32;
        float z = -65;
        int maxR = 76;
        int maxG = 255;
        int maxB = 0;
        int minR = 127;
        int minG = 0;
        int minB = 0;
        int rDif = maxR - minR;
        int gDif = maxG - minG;
        int bDif = maxB - minB;
        int finalR = (int) (minR + (rDif * ratio));
        int finalG = (int) (minG + (gDif * ratio));
        int finalB = (int) (minB + (bDif * ratio));
        int color = new Color(finalR, finalG, finalB, 0).getRGB();
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTexture(0, x, y, z, 3);
        tessellator.addVertex2DTexture(0, x + (64), y + 5, z, 1);
        tessellator.addVertex2DTexture(0, x, y + 5, z, 2);
        tessellator.addVertex2DTexture(0, x + (64), y, z, 0);
        tessellator.addElements();
        z += 5;
        tessellator.addVertex2DTexture(color, x, y, z, 3);
        tessellator.addVertex2DTexture(color, x + (64 * ratio), y + 5, z, 1);
        tessellator.addVertex2DTexture(color, x, y + 5, z, 2);
        tessellator.addVertex2DTexture(color, x + (64 * ratio), y, z, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(texID, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

}
