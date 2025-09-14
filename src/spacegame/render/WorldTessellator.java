package spacegame.render;

import org.joml.Matrix4d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

//All vertices within this class are assumed to use vertex normals and the skylighting values for lighting, block lighting is encoded within the color values in the vertex buffer
public final class WorldTessellator {
    public static final WorldTessellator instance = new WorldTessellator(8192);
    public FloatBuffer vertexBuffer;
    public IntBuffer elementBuffer;
    private int elementOffset = 0;
    private int vaoID;
    private int vboID;
    private int eboID;
    private int[] texSlots = new int[256];
    public boolean isOrtho;
    private int boundTexture;



    private WorldTessellator(int quadLimit){
        this.vertexBuffer = BufferUtils.createFloatBuffer(quadLimit * 40);
        this.elementBuffer = BufferUtils.createIntBuffer(quadLimit * 6);
        this.vaoID = GL46.glGenVertexArrays();
        this.vboID = GL46.glGenBuffers();
        this.eboID = GL46.glGenBuffers();
    }

    public void addVertex2DTextureWithAtlas(int colorValue, float x, float y, float z, int corner, Texture textureID, float blockID, float normalX, float normalY, float normalZ, float skyLightValue){
        if(colorValue > 16777215){
            colorValue = 1677215;
        }
        if(colorValue < 0){
            colorValue = 0;
        }
        Color color = new Color(colorValue);
        final float red = color.getRed()/256F;
        final float green = color.getGreen()/256F;
        final float blue = color.getBlue()/256F;
        final float alpha = color.getAlpha()/256F;

        this.vertexBuffer.put(x);
        this.vertexBuffer.put(y);
        this.vertexBuffer.put(z);
        this.vertexBuffer.put(red);
        this.vertexBuffer.put(green);
        this.vertexBuffer.put(blue);
        this.vertexBuffer.put(alpha);
        this.vertexBuffer.put(textureID.texCoords[corner].x);
        this.vertexBuffer.put(textureID.texCoords[corner].y);
        this.vertexBuffer.put(blockID);
        this.vertexBuffer.put(normalX);
        this.vertexBuffer.put(normalY);
        this.vertexBuffer.put(normalZ);
        this.vertexBuffer.put(skyLightValue);
    }

    public void addVertex2DTexture(int colorValue, float x, float y, float z, int corner, float normalX, float normalY, float normalZ, float skyLightValue){
        if(colorValue > 16777215){
            colorValue = 1677215;
        }
        if(colorValue < 0){
            colorValue = 0;
        }
        Color color = new Color(colorValue);
        final float red = color.getRed()/256F;
        final float green = color.getGreen()/256F;
        final float blue = color.getBlue()/256F;
        final float alpha = color.getAlpha()/256F;

        this.vertexBuffer.put(x);
        this.vertexBuffer.put(y);
        this.vertexBuffer.put(z);
        this.vertexBuffer.put(red);
        this.vertexBuffer.put(green);
        this.vertexBuffer.put(blue);
        this.vertexBuffer.put(alpha);

        switch (corner) {
            case 0 -> {
                this.vertexBuffer.put(1f);
                this.vertexBuffer.put(1f);
            }
            case 1 -> {
                this.vertexBuffer.put(1f);
                this.vertexBuffer.put(0f);
            }
            case 2 -> {
                this.vertexBuffer.put(0f);
                this.vertexBuffer.put(0f);
            }
            case 3 -> {
                this.vertexBuffer.put(0F);
                this.vertexBuffer.put(1F);
            }
        }

        this.vertexBuffer.put(normalX);
        this.vertexBuffer.put(normalY);
        this.vertexBuffer.put(normalZ);
        this.vertexBuffer.put(skyLightValue);
    }

    public void addVertex2DTextureWithSampling(int colorValue, float x, float y, float z, int corner, float xSample, float ySample, float normalX, float normalY, float normalZ, float skyLightValue){
        if(colorValue > 16777215){
            colorValue = 1677215;
        }
        if(colorValue < 0){
            colorValue = 0;
        }
        Color color = new Color(colorValue);
        final float red = color.getRed()/256F;
        final float green = color.getGreen()/256F;
        final float blue = color.getBlue()/256F;
        final float alpha = color.getAlpha()/256F;

        this.vertexBuffer.put(x);
        this.vertexBuffer.put(y);
        this.vertexBuffer.put(z);
        this.vertexBuffer.put(red);
        this.vertexBuffer.put(green);
        this.vertexBuffer.put(blue);
        this.vertexBuffer.put(alpha);

        switch (corner) {
            case 0 -> {
                this.vertexBuffer.put(1f + xSample);
                this.vertexBuffer.put(1f + ySample);
            }
            case 1 -> {
                this.vertexBuffer.put(1f + xSample);
                this.vertexBuffer.put(0f + ySample);
            }
            case 2 -> {
                this.vertexBuffer.put(0f + xSample);
                this.vertexBuffer.put(0f + ySample);
            }
            case 3 -> {
                this.vertexBuffer.put(0f + xSample);
                this.vertexBuffer.put(1f + ySample);
            }
        }

        this.vertexBuffer.put(normalX);
        this.vertexBuffer.put(normalY);
        this.vertexBuffer.put(normalZ);
        this.vertexBuffer.put(skyLightValue);
    }

    public void addVertexCubeMap(int colorValue, float x, float y, float z, float normalX, float normalY, float normalZ, float skyLightValue){
        if(colorValue > 16777215){
            colorValue = 1677215;
        }
        if(colorValue < 0){
            colorValue = 0;
        }
        Color color = new Color(colorValue);
        final float red = color.getRed()/256F;
        final float green = color.getGreen()/256F;
        final float blue = color.getBlue()/256F;
        final float alpha = color.getAlpha()/256F;

        this.vertexBuffer.put(x);
        this.vertexBuffer.put(y);
        this.vertexBuffer.put(z);
        this.vertexBuffer.put(red);
        this.vertexBuffer.put(green);
        this.vertexBuffer.put(blue);
        this.vertexBuffer.put(alpha);
        this.vertexBuffer.put(normalX);
        this.vertexBuffer.put(normalY);
        this.vertexBuffer.put(normalZ);
        this.vertexBuffer.put(skyLightValue);
    }

    public void addVertexTextureArray(int colorValue, float x, float y, float z, int corner, float blockID, int faceType, float normalX, float normalY, float normalZ, float skyLightValue){
        if(colorValue > 16777215){
            colorValue = 1677215;
        }
        if(colorValue < 0){
            colorValue = 0;
        }
        Color color = new Color(colorValue);
        final float red = color.getRed()/256F;
        final float green = color.getGreen()/256F;
        final float blue = color.getBlue()/256F;
        final float alpha = color.getAlpha()/256F;

        float[] texCoords = this.texCoords(corner);
        this.vertexBuffer.put(x);
        this.vertexBuffer.put(y);
        this.vertexBuffer.put(z);
        this.vertexBuffer.put(red);
        this.vertexBuffer.put(green);
        this.vertexBuffer.put(blue);
        this.vertexBuffer.put(alpha);
        this.vertexBuffer.put(texCoords[0]);
        this.vertexBuffer.put(texCoords[1]);
        this.vertexBuffer.put(blockID);
        this.vertexBuffer.put(normalX);
        this.vertexBuffer.put(normalY);
        this.vertexBuffer.put(normalZ);
        this.vertexBuffer.put(skyLightValue);
    }

    public void addVertexTextureArrayWithSampling(int colorValue, float x, float y, float z, int corner, float blockID, float xSample, float ySample, float normalX, float normalY, float normalZ, float skyLightValue){
        if(colorValue > 16777215){
            colorValue = 1677215;
        }
        if(colorValue < 0){
            colorValue = 0;
        }
        Color color = new Color(colorValue);
        final float red = color.getRed()/256F;
        final float green = color.getGreen()/256F;
        final float blue = color.getBlue()/256F;
        final float alpha = color.getAlpha()/256F;

        float[] texCoords = this.texCoords(corner);
        this.vertexBuffer.put(x);
        this.vertexBuffer.put(y);
        this.vertexBuffer.put(z);
        this.vertexBuffer.put(red);
        this.vertexBuffer.put(green);
        this.vertexBuffer.put(blue);
        this.vertexBuffer.put(alpha);
        this.vertexBuffer.put(texCoords[0] + xSample);
        this.vertexBuffer.put(texCoords[1] + ySample);
        this.vertexBuffer.put(blockID);
        this.vertexBuffer.put(normalX);
        this.vertexBuffer.put(normalY);
        this.vertexBuffer.put(normalZ);
        this.vertexBuffer.put(skyLightValue);
    }

    public void addElements(){
        this.elementBuffer.put(this.elementOffset + 2);
        this.elementBuffer.put(this.elementOffset + 1);
        this.elementBuffer.put(this.elementOffset + 0);
        this.elementBuffer.put(this.elementOffset + 0);
        this.elementBuffer.put(this.elementOffset + 1);
        this.elementBuffer.put(this.elementOffset + 3);
        this.elementOffset += 4;
    }

    private float[] texCoords(int corner) {

        switch (corner) {
            case 3 -> {
                return new float[]{0, 1};
            }
            case 1 -> {
                return new float[]{1, 0};
            }
            case 2 -> {
                return new float[2];
            }
            case 0 -> {
                return new float[]{1, 1};
            }
        }
        return new float[2];
    }

    public void drawTexture2DWithAtlas(int texID, Shader shader, Camera camera){
        this.boundTexture = texID;

        GL46.glBindVertexArray(vaoID);

        this.vertexBuffer.flip();
        this.elementBuffer.flip();

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int texIndexSize = 1;
        int texCoordsSize = 2;
        int normalSize = 3;
        int skyLightValueSize = 1;
        int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize + normalSize + skyLightValueSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, texCoordsSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, texIndexSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glVertexAttribPointer(4, normalSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(4);

        GL46.glVertexAttribPointer(5, skyLightValueSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize + skyLightValueSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(5);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

        GL46.glUseProgram(shader.shaderProgramID);

        if(this.isOrtho) {
            shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
            shader.uploadMat4d("uView", new Matrix4d());
        } else {
            shader.uploadMat4d("uProjection", camera.projectionMatrix);
            shader.uploadMat4d("uView", camera.viewMatrix);
        }
        shader.uploadIntArray("uTextures", this.texSlots);
        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
        //bind the VAO being used


        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(2);
        GL46.glDisableVertexAttribArray(3);
        GL46.glDisableVertexAttribArray(4);
        GL46.glDisableVertexAttribArray(5);


        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL46.glUseProgram(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
        GL46.glDisable(GL46.GL_ALPHA_TEST);
        this.reset();
    }

    public void drawTexture2D(int texID, Shader shader, Camera camera){
        this.boundTexture = texID;

        GL46.glBindVertexArray(vaoID);

        this.vertexBuffer.flip();
        this.elementBuffer.flip();

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int texCoordsSize = 2;
        int normalSize = 3;
        int skyLightValueSize = 1;
        int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + normalSize + skyLightValueSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, texCoordsSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, normalSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glVertexAttribPointer(4, skyLightValueSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + normalSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(4);


        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

        if(SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.size() > 0) {
            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.get(0).shadowMap.depthMap);
        }

        //bind shader program
        GL46.glUseProgram(shader.shaderProgramID);

        //upload texture to shader
        Shader.worldShader2DTexture.uploadInt("shadowMap", 1);
        if(this.isOrtho) {
            shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
            shader.uploadMat4d("uView", new Matrix4d());
        } else {
            shader.uploadMat4d("uProjection", camera.projectionMatrix);
            shader.uploadMat4d("uView", camera.viewMatrix);
        }
        shader.uploadInt("uTexture", 0);
        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
        //bind the VAO being used



        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(2);
        GL46.glDisableVertexAttribArray(3);
        GL46.glDisableVertexAttribArray(4);


        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL46.glUseProgram(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
        GL46.glDisable(GL46.GL_ALPHA_TEST);
        this.reset();
    }

    public void drawTextureArray(int texID, Shader shader, Camera camera){
        this.boundTexture = texID;

        GL46.glBindVertexArray(vaoID);

        this.vertexBuffer.flip();
        this.elementBuffer.flip();

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertexBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL46.GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int texIndexSize = 1;
        int texCoordsSize = 2;
        int normalSize = 3;
        int skyLightSize = 1;
        int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize + normalSize + skyLightSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, texCoordsSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, texIndexSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glVertexAttribPointer(4, normalSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(4);

        GL46.glVertexAttribPointer(5, skyLightSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize + texIndexSize + normalSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(5);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.boundTexture);

        if(SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.size() > 0) {
            GL46.glActiveTexture(GL46.GL_TEXTURE1);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.nearbyStars.get(0).shadowMap.depthMap);
        }

        //bind shader program
        GL46.glUseProgram(shader.shaderProgramID);

        //upload texture to shader
        Shader.worldShaderTextureArray.uploadInt("shadowMap", 1);
        if(this.isOrtho) {
            shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
            shader.uploadMat4d("uView", new Matrix4d());
        } else {
            shader.uploadMat4d("uProjection", camera.projectionMatrix);
            shader.uploadMat4d("uView", camera.viewMatrix);
        }
        shader.uploadInt("textureArray", 0);

        //enable the vertex attribute pointers


        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(2);
        GL46.glDisableVertexAttribArray(3);
        GL46.glDisableVertexAttribArray(4);
        GL46.glDisableVertexAttribArray(5);


        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL46.glUseProgram(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

        this.reset();
    }


    private void reset(){
        if(this.vertexBuffer != null){
            this.vertexBuffer.clear();
        }

        if(this.elementBuffer != null){
            this.elementBuffer.clear();
        }
        this.elementOffset = 0;
    }

    public void toggleOrtho(){
        this.isOrtho = !this.isOrtho;
    }


}
