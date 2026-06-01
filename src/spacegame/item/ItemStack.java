package spacegame.item;


import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.gui.FontRenderer;
import spacegame.gui.GuiInGame;
import spacegame.gui.GuiInventory;
import spacegame.gui.GuiInventoryPlayer;
import spacegame.item.itemstate.ItemState;
import spacegame.item.itemstate.SeedState;
import spacegame.render.*;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;

public final class ItemStack {
    public Item item;
    public ItemState itemState;
    public String exclusiveItemType;
    public boolean usesExclusiveItem;
    public short metadata;
    public byte count;
    public short durability;
    public long decayTime;
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
            int z = -800;
            if(this.usesExclusiveItem && this.item != null){
                tessellator.addVertex2DTexture(7500402, this.x - (float) this.width / 2, this.y - (float) this.height / 2, z - 5, 3);
                tessellator.addVertex2DTexture(7500402, this.x + (float) this.width / 2, this.y + (float) this.height / 2, z - 5, 1);
                tessellator.addVertex2DTexture(7500402, this.x - (float) this.width / 2, this.y + (float) this.height / 2, z - 5, 2);
                tessellator.addVertex2DTexture(7500402, this.x + (float) this.width / 2, this.y - (float) this.height / 2, z - 5, 0);
                tessellator.addElementsCW();
                tessellator.drawTexture2D(GuiInventoryPlayer.fillableColor, Shader.screen2DTexture, CosmicEvolution.camera);
            }
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.addVertex2DTexture(0, this.x - (float) this.width / 2, this.y - (float) this.height / 2, z, 3);
            tessellator.addVertex2DTexture(0, this.x + (float) this.width / 2, this.y + (float) this.height / 2, z, 1);
            tessellator.addVertex2DTexture(0, this.x - (float) this.width / 2, this.y + (float) this.height / 2, z, 2);
            tessellator.addVertex2DTexture(0, this.x + (float) this.width / 2, this.y - (float) this.height / 2, z, 0);
            tessellator.addElementsCW();
            tessellator.drawTexture2D(GuiInventory.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
            GL46.glDisable(GL46.GL_BLEND);
            tessellator.toggleOrtho();
        }

        if(this.item != null){
            tessellator.toggleOrtho();
            if(this.item.renderItemWithBlockModel){
                ModelLoader model = Block.list[this.metadata].blockModel.copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
                if(this.metadata == Block.itemStone.ID || this.metadata == Block.itemStick.ID){
                   model.translateModel(0.5f, 0, 0.5f);
                   model.scaleModel(2f);
                }
                if(this.metadata == Block.itemClay.ID){
                    model.scaleModel(2f);
                    model.translateModel(0, 0.25f, 0);
                }
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
                for(int face = 0; face < 6; face++){
                    faces = model.getModelFaceOfType(face);
                    for(int i = 0; i < faces.length; i++){
                        if(faces[i] == null)continue;
                        textureID = Block.list[this.metadata].getBlockTexture(this.metadata, face);
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);

                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex1.x, vertex1.y, vertex1.z, textureID, faces[i].UVs[0][0], faces[i].UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex2.x, vertex2.y, vertex2.z, textureID, faces[i].UVs[1][0], faces[i].UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex3.x, vertex3.y, vertex3.z, textureID, faces[i].UVs[2][0], faces[i].UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex4.x, vertex4.y, vertex4.z, textureID, faces[i].UVs[3][0], faces[i].UVs[3][1]);
                        tessellator.addElementsCCW();

                        red -= 10;
                        green -= 10;
                        blue -= 10;
                    }
                }
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
            } else {
                ModelLoader model = this.item.itemModel.copyModel();
                model.scaleModel(39f);
                model.rotateModel(45, 0, 1, 0);
                model.rotateModel(36, 1, 0, 0);

                if(this.item instanceof ItemSeed){
                    model.scaleModel(2f);
                }

                Vector3f position = new Vector3f(this.x, this.y, -70);
                model.translateModel(position.x, position.y, position.z);
                ModelFace face;
                float textureID;

                int colorVal = 255;

                int colorRGB = 0;

                int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

                for (int i = 0; i < model.modelFaces.length; i++) {
                    face = model.modelFaces[i];
                    if (face == null) continue;
                    textureID = face.texture;

                    switch (face.faceType){
                        case RenderBlocks.TOP_FACE -> {
                            colorRGB = colorTop;
                        }
                        case RenderBlocks.BOTTOM_FACE -> {
                            colorRGB = colorBottom;
                        }
                        case RenderBlocks.NORTH_FACE -> {
                            colorRGB = colorNorth;
                        }
                        case RenderBlocks.SOUTH_FACE -> {
                            colorRGB = colorSouth;
                        }
                        case RenderBlocks.EAST_FACE -> {
                            colorRGB = colorEast;
                        }
                        case RenderBlocks.WEST_FACE -> {
                            colorRGB = colorWest;
                        }
                    }

                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z, textureID, face.UVs[0][0], face.UVs[0][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z, textureID, face.UVs[1][0], face.UVs[1][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z, textureID, face.UVs[2][0], face.UVs[2][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z, textureID, face.UVs[3][0], face.UVs[3][1]);
                    tessellator.addElementsCCW();
                }

                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
            }
            tessellator.toggleOrtho();
            if(this.count > 1) {
                FontRenderer fontRenderer = FontRenderer.instance;
                fontRenderer.drawString(Byte.toString(this.count), (int) this.x - 48, (int) (this.y - 48),-15, 16777215, 50, 255);
            }

            this.renderDurabilityBar();
        }
    }

    public void renderItemStackOnHotbar() {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

        if (this.item != null) {
            tessellator.toggleOrtho();
            if (this.item.renderItemWithBlockModel) {
                ModelLoader model = Block.list[this.metadata].blockModel.copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
                if (this.metadata == Block.itemStone.ID || this.metadata == Block.itemStick.ID) {
                   model.translateModel(0.5f, 0, 0.5f);
                   model.scaleModel(2f);
                }
                if (this.metadata == Block.itemClay.ID) {
                   model.scaleModel(2f);
                }
                ModelFace[] faces;
                float textureID;
                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                Vector3f position = new Vector3f(this.x + 4, this.y - 12, -70);
                int colorVal = 255;

                int colorRGB = 0;

                int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

                for (int face = 0; face < 6; face++) {
                    faces = model.getModelFaceOfType(face);
                    for (int i = 0; i < faces.length; i++) {
                        if (faces[i] == null) continue;
                        textureID = Block.list[this.metadata].getBlockTexture(this.metadata, face);
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);

                        switch (faces[i].faceType) {
                            case RenderBlocks.TOP_FACE -> {
                                colorRGB = colorTop;
                            }
                            case RenderBlocks.BOTTOM_FACE -> {
                                colorRGB = colorBottom;
                            }
                            case RenderBlocks.NORTH_FACE -> {
                                colorRGB = colorNorth;
                            }
                            case RenderBlocks.SOUTH_FACE -> {
                                colorRGB = colorSouth;
                            }
                            case RenderBlocks.EAST_FACE -> {
                                colorRGB = colorEast;
                            }
                            case RenderBlocks.WEST_FACE -> {
                                colorRGB = colorWest;
                            }
                        }

                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex1.x, vertex1.y, vertex1.z, textureID, faces[i].UVs[0][0], faces[i].UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex2.x, vertex2.y, vertex2.z, textureID, faces[i].UVs[1][0], faces[i].UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex3.x, vertex3.y, vertex3.z, textureID, faces[i].UVs[2][0], faces[i].UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex4.x, vertex4.y, vertex4.z, textureID, faces[i].UVs[3][0], faces[i].UVs[3][1]);
                        tessellator.addElementsCCW();
                    }
                }
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
            } else {
                ModelLoader model = this.item.itemModel.copyModel();
               model.scaleModel(57f);
               model.rotateModel(45, 0, 1, 0);
               model.rotateModel(36, 1, 0, 0);

               if(this.item instanceof ItemSeed){
                   model.scaleModel(2f);
               }

                Vector3f position = new Vector3f(this.x + 4, this.y, -70);
                model.translateModel(position.x, position.y, position.z);
                ModelFace face;
                float textureID;
                int colorVal = 255;

                int colorRGB = 0;

                int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;
                for (int i = 0; i < model.modelFaces.length; i++) {
                    face = model.modelFaces[i];
                    if (face == null) continue;
                    textureID = face.texture;

                    switch (face.faceType) {
                        case RenderBlocks.TOP_FACE -> {
                            colorRGB = colorTop;
                        }
                        case RenderBlocks.BOTTOM_FACE -> {
                            colorRGB = colorBottom;
                        }
                        case RenderBlocks.NORTH_FACE -> {
                            colorRGB = colorNorth;
                        }
                        case RenderBlocks.SOUTH_FACE -> {
                            colorRGB = colorSouth;
                        }
                        case RenderBlocks.EAST_FACE -> {
                            colorRGB = colorEast;
                        }
                        case RenderBlocks.WEST_FACE -> {
                            colorRGB = colorWest;
                        }

                    }
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z, textureID, face.UVs[0][0], face.UVs[0][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z, textureID, face.UVs[1][0], face.UVs[1][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z, textureID, face.UVs[2][0], face.UVs[2][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z, textureID, face.UVs[3][0], face.UVs[3][1]);
                    tessellator.addElementsCCW();
                }

                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
            }
            tessellator.toggleOrtho();
        }
        if (this.count > 1) {
            FontRenderer fontRenderer = FontRenderer.instance;
            fontRenderer.drawString(Byte.toString(this.count), (int) this.x - 48, (int) (this.y - 48), -15, 16777215, 50, 255);
        }
        this.renderDurabilityBar();
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
        this.itemState = incomingStack.itemState != null ? incomingStack.itemState.copy() : null;
        this.decayTime = (this.decayTime + incomingStack.decayTime) / 2;
    }

    public ItemStack splitStack(){
        ItemStack newStack =  new ItemStack(this.item, this.count, this.x, this.y);
        newStack.setMetadata(this.metadata);
        newStack.decayTime = this.decayTime;
        newStack.itemState = this.itemState != null ? this.itemState.copy() : null;
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
        double x = MouseListener.instance.xPos - CosmicEvolution.width/2D;
        double y = (MouseListener.instance.yPos - CosmicEvolution.height/2D) * -1;
        return x > this.x - (double) this.width /2 && x < this.x + (double) this.width /2 && y > this.y - (double) this.height /2 && y < this.y + (double) this.height /2;
    }

    private void renderDurabilityBar(){
        if(this.item == null)return;
        float ratio = (float) this.durability / Item.list[this.item.ID].durability;
        if(ratio > 1 || ratio < 0){return;}
        if(ratio == 1 || this.durability <= 0){return;}
        int texID = 0;
         if(CosmicEvolution.instance.currentGui instanceof GuiInventory){
            texID = GuiInventory.fillableColor;
        } else {
            texID = GuiInGame.fillableColor;
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
        int color = finalR << 16 | finalG << 8 | finalB;
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTexture(0, x, y, z, 3);
        tessellator.addVertex2DTexture(0, x + (64), y + 5, z, 1);
        tessellator.addVertex2DTexture(0, x, y + 5, z, 2);
        tessellator.addVertex2DTexture(0, x + (64), y, z, 0);
        tessellator.addElementsCW();
        z += 5;
        tessellator.addVertex2DTexture(color, x, y, z, 3);
        tessellator.addVertex2DTexture(color, x + (64 * ratio), y + 5, z, 1);
        tessellator.addVertex2DTexture(color, x, y + 5, z, 2);
        tessellator.addVertex2DTexture(color, x + (64 * ratio), y, z, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(texID, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }


    public boolean doStatesMatch(ItemState incomingState){
        if(this.itemState instanceof SeedState currentSeedState && incomingState instanceof SeedState incomingSeedState){
            return currentSeedState.canMutate == incomingSeedState.canMutate && currentSeedState.percentToTargetCrop == incomingSeedState.percentToTargetCrop &&
                    currentSeedState.targetCrop.equals(incomingSeedState.targetCrop);
         }

        return this.itemState == null && incomingState == null;
    }


}
