package spacegame.world.weather;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.MathUtil;

import java.awt.*;

public final class Cloud {
    public double x;
    public double y;
    public double z;
    public float width; //x
    public float height; //y
    public float depth; //z
    public float strength = 0.001f;
    public static int texture;
    public boolean weaken;
    public float precipitation;
    public long killTime;
    public float maxStrength;



    public Cloud(double x, double y, double z, float width, float height, float depth, float precipitation, long killTime, float maxStrength){
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.precipitation = precipitation;
        this.killTime = killTime;
        this.maxStrength = maxStrength;
    }

    public Cloud(NBTTagCompound cloudTag){
        this.x = cloudTag.getDouble("x");
        this.y = cloudTag.getDouble("y");
        this.z = cloudTag.getDouble("z");
        this.width = cloudTag.getFloat("width");
        this.height = cloudTag.getFloat("height");
        this.depth = cloudTag.getFloat("depth");
        this.strength = cloudTag.getFloat("strength");
        this.weaken = cloudTag.getBoolean("weaken");
        this.precipitation = cloudTag.getFloat("precipitation");
        this.killTime = cloudTag.getLong("killTime");
        this.maxStrength = cloudTag.getFloat("maxStrength");
    }

    public void saveCloudToFile(NBTTagCompound cloudTag){
        cloudTag.setDouble("x", this.x);
        cloudTag.setDouble("y", this.y);
        cloudTag.setDouble("z", this.z);
        cloudTag.setFloat("width", this.width);
        cloudTag.setFloat("height", this.height);
        cloudTag.setFloat("depth", this.depth);
        cloudTag.setFloat("strength", this.strength);
        cloudTag.setBoolean("weaken", this.weaken);
        cloudTag.setFloat("precipitation", this.precipitation);
        cloudTag.setLong("killTime", this.killTime);
        cloudTag.setFloat("maxStrength", this.maxStrength);
    }

    private int calculateAlpha(){
        return this.strength <= 1f ? MathUtil.floatToIntRGBA(this.strength) : 255;
    }

    private int calculateColor(){
        return  255 - MathUtil.floatToIntRGBA(this.precipitation * 0.5f);
    }

    public void render(float skyLightValue, float sunRed, float sunGreen, float sunBlue, RenderEngine.WorldTessellator tessellator){
        int alphaVal = this.calculateAlpha();
        int colorVal = this.calculateColor();
        float red = MathUtil.intToFloatRGBA(colorVal);
        float green = MathUtil.intToFloatRGBA(colorVal);
        float blue = MathUtil.intToFloatRGBA(colorVal);
        if(CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.sunYVector <= -0.13){
            float upperColorR = 1;
            float upperColorG = 1;
            float upperColorB = 1;
            float lowerColorR = 1;
            float lowerColorG = 1;
            float lowerColorB = 1;

            upperColorR = 255f / 255f;
            upperColorG = 77f / 255f;
            upperColorB = 53f / 255f;
            lowerColorR = 0;
            lowerColorG = 0;
            lowerColorB = 0;

            float yVecComponent = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.sunYVector;

            yVecComponent += 0.2;
            yVecComponent %= 0.07;

            float colorDifR = upperColorR - lowerColorR;
            float colorDifG = upperColorG - lowerColorG;
            float colorDifB = upperColorB - lowerColorB;

            float ratio = yVecComponent / 0.07f;

            sunRed = lowerColorR + (colorDifR * ratio);
            sunGreen = lowerColorG + (colorDifG * ratio);
            sunBlue = lowerColorB + (colorDifB * ratio);
        } else if(CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.sunYVector <= -0.2){
            sunRed = 0.25f;
            sunGreen = 0.25f;
            sunBlue = 0.25f;
        }
        red *= sunRed;
        green *= sunGreen;
        blue *= sunBlue;
        int color = (MathUtil.floatToIntRGBA(red) << 16) | (MathUtil.floatToIntRGBA(green) << 8) | MathUtil.floatToIntRGBA(blue);
        float xMin = (float) ((this.x - CosmicEvolution.instance.save.thePlayer.x) - this.width * 0.5f);
        float xMax = (float) ((this.x - CosmicEvolution.instance.save.thePlayer.x) + this.width * 0.5f);
        float yMin = (float) ((this.y - CosmicEvolution.instance.save.thePlayer.y) - this.height * 0.5f);
        float yMax = (float) ((this.y - CosmicEvolution.instance.save.thePlayer.y) + this.height * 0.5f);
        float zMin = (float) ((this.z - CosmicEvolution.instance.save.thePlayer.z) - this.depth * 0.5f);
        float zMax = (float) ((this.z - CosmicEvolution.instance.save.thePlayer.z) + this.depth * 0.5f);
        //top
        tessellator.addVertex2DTexture(color, xMax, yMax, zMin, 3, 0, 1, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMax, zMax, 1, 0, 1, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMax, zMax, 2, 0, 1, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMax, zMin, 0, 0, 1, 0, skyLightValue, alphaVal);
        tessellator.addElements();
        //Bottom
        tessellator.addVertex2DTexture(color, xMin, yMin, zMin, 3, 0, -1, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMin, zMax, 1, 0, -1, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMin, zMax, 2, 0, -1, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMin, zMin, 0, 0, -1, 0, skyLightValue, alphaVal);
        tessellator.addElements();
        //North
        tessellator.addVertex2DTexture(color, xMin, yMin, zMin, 3, -1, 0, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMax, zMax, 1, -1, 0, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMax, zMin, 2, -1, 0, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMin, zMax, 0, -1, 0, 0, skyLightValue, alphaVal);
        tessellator.addElements();
        //South
        tessellator.addVertex2DTexture(color, xMax, yMin, zMax, 3, 1, 0, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMax, zMin, 1, 1, 0, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMax, zMax, 2, 1, 0, 0, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMin, zMin, 0, 1, 0, 0, skyLightValue, alphaVal);
        tessellator.addElements();
        //East
        tessellator.addVertex2DTexture(color, xMax, yMin, zMin, 3, 0, 0, -1, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMax, zMin, 1, 0, 0, -1, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMax, zMin, 2, 0, 0, -1, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMin, zMin, 0, 0, 0, -1, skyLightValue, alphaVal);
        tessellator.addElements();
        //West
        tessellator.addVertex2DTexture(color, xMin, yMin, zMax, 3, 0, 0, 1, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMax, zMax, 1, 0, 0, 1, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMin, yMax, zMax, 2, 0, 0, 1, skyLightValue, alphaVal);
        tessellator.addVertex2DTexture(color, xMax, yMin, zMax, 0, 0, 0, 1, skyLightValue, alphaVal);
        tessellator.addElements();
    }

    public void update(){
        this.weaken = CosmicEvolution.instance.save.time >= this.killTime;
        this.z -= 0.01f;

        this.strength += this.weaken ? -0.001f : 0.001f;
        this.strength = Math.min(this.strength, this.maxStrength);
    }


    public void scale(float scaleFactor){
        this.width *= scaleFactor;
        this.height *= scaleFactor;
        this.depth *= scaleFactor;
    }
}
