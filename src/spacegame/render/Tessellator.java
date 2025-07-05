package spacegame.render;

import org.joml.Matrix4d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Tessellator {
    public SpaceGame sg;
    public static final Tessellator instance = new Tessellator();
    public FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(524288);
    public IntBuffer elementBuffer = BufferUtils.createIntBuffer(524288);
    private int elementOffset = 0;
    private static int vaoID;
    private static int vboID;
    private static int eboID;
    private int[] texSlots = new int[256];
    public boolean isOrtho;
    private int boundTexture;

    //top, bottom, north, south, east, west

    public static void initBuffers(){
        vaoID = GL46.glGenVertexArrays();
        vboID = GL46.glGenBuffers();
        eboID = GL46.glGenBuffers();
    }


    public void addVertex2DTextureWithAtlas(int colorValue, float x, float y, float z, int corner, Texture textureID, float blockID){
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
    }

    public void addVertex2DTexture(int colorValue, float x, float y, float z, int corner){
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

        switch (corner) {
            case 0 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(1f);
                this.vertexBuffer.put(1f);
            }
            case 1 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(1f);
                this.vertexBuffer.put(0f);
            }
            case 2 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(0f);
                this.vertexBuffer.put(0f);
            }
            case 3 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(0F);
                this.vertexBuffer.put(1F);
            }
        }
    }
    //Negative x shifts to the left, positive x shifts to the right, negative y shifts up, positive y shifts down
    public void addVertex2DTextureWithSampling(int colorValue, float x, float y, float z, int corner, float xSample, float ySample){
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

        switch (corner) {
            case 0 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(1f + xSample);
                this.vertexBuffer.put(1f + ySample);
            }
            case 1 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(1f + xSample);
                this.vertexBuffer.put(0f + ySample);
            }
            case 2 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(0f + xSample);
                this.vertexBuffer.put(0f + ySample);
            }
            case 3 -> {
                this.vertexBuffer.put(x);
                this.vertexBuffer.put(y);
                this.vertexBuffer.put(z);
                this.vertexBuffer.put(red);
                this.vertexBuffer.put(green);
                this.vertexBuffer.put(blue);
                this.vertexBuffer.put(alpha);
                this.vertexBuffer.put(0f + xSample);
                this.vertexBuffer.put(1f + ySample);
            }
        }
    }

    public void addVertexCubeMap(int colorValue, float x, float y, float z){
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
    }

    public void addVertexTextureArray(int colorValue, float x, float y, float z, int corner, float blockID, int faceType){
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
    }

    public void addVertexTextureArrayWithSampling(int colorValue, float x, float y, float z, int corner, float blockID, float xSample, float ySample){
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
        int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, texCoordsSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, texIndexSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

        //bind shader program
        GL46.glUseProgram(shader.shaderProgramID);

        //upload texture to shader
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

        //enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);
        GL46.glEnableVertexAttribArray(2);
        GL46.glEnableVertexAttribArray(3);


        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(2);
        GL46.glDisableVertexAttribArray(3);


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
        int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, texCoordsSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);


        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.boundTexture);

        //bind shader program
        GL46.glUseProgram(shader.shaderProgramID);

        //upload texture to shader
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

        //enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);
        GL46.glEnableVertexAttribArray(2);


        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(2);


        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL46.glUseProgram(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
        GL46.glDisable(GL46.GL_ALPHA_TEST);
        this.reset();
    }
    
    public void drawVertexArray(int texID, Shader shader, Camera camera){
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
        int vertexSizeBytes = (positionsSize + colorSize + texCoordsSize + texIndexSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, texCoordsSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

        GL46.glVertexAttribPointer(3, texIndexSize, GL46.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize + texCoordsSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(3);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.boundTexture);

        //bind shader program
        GL46.glUseProgram(shader.shaderProgramID);

        //upload texture to shader
        if(this.isOrtho) {
            shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
            shader.uploadMat4d("uView", new Matrix4d());
        } else {
            shader.uploadMat4d("uProjection", camera.projectionMatrix);
            shader.uploadMat4d("uView", camera.viewMatrix);
        }
        shader.uploadInt("textureArray", 0);

        //enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);
        GL46.glEnableVertexAttribArray(2);
        GL46.glEnableVertexAttribArray(3);


        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(2);
        GL46.glDisableVertexAttribArray(3);


        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL46.glUseProgram(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);

        this.reset();
    }
    

    public void drawCubeMapTexture(int texID, Shader shader, Camera camera) {
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
        int vertexSizeBytes = (positionsSize + colorSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionsSize, GL46.GL_FLOAT, false, vertexSizeBytes, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);


        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, this.boundTexture);

        //bind shader program
        GL46.glUseProgram(shader.shaderProgramID);

        //upload texture to shader

        if (this.isOrtho) {
            shader.uploadMat4d("uProjection", camera.guiProjectionMatrix);
            shader.uploadMat4d("uView", new Matrix4d());
        } else {
            shader.uploadMat4d("uProjection", camera.projectionMatrix);
            shader.uploadMat4d("uView", camera.viewMatrix);
        }
        shader.uploadInt("cubeTexture", 0);
        
        GL46.glEnable(GL46.GL_ALPHA_TEST);
        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
        //bind the VAO being used

        //enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);


        GL46.glDrawElements(GL46.GL_TRIANGLES, this.elementBuffer.limit(), GL46.GL_UNSIGNED_INT, 0);

        //unbind
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);


        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL46.glUseProgram(0);

        GL46.glBindTexture(GL46.GL_TEXTURE_CUBE_MAP, 0);
        GL46.glDisable(GL46.GL_ALPHA_TEST);
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

