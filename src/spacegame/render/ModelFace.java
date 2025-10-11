package spacegame.render;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;

public final class ModelFace {
    public Vector3f[] vertices = new Vector3f[4];
    public Vector3f normal = new Vector3f();
    public final int faceType;

    public ModelFace(int faceType) {
        this.faceType = faceType;
        this.vertices[0] = new Vector3f();
        this.vertices[1] = new Vector3f();
        this.vertices[2] = new Vector3f();
        this.vertices[3] = new Vector3f();
    }


    public void setFloatValue(int index, int subIndex, float value) {
        switch (subIndex) {
            case 0 -> this.vertices[index].x = value;
            case 1 -> this.vertices[index].y = value;
            case 2 -> this.vertices[index].z = value;
        }
    }

    public void setNormal(float normalX, float normalY, float normalZ){
        this.normal.x = normalX;
        this.normal.y = normalY;
        this.normal.z = normalZ;
    }

    public ModelFace translateFace(float x, float y, float z){
        ModelFace translatedFace = new ModelFace(this.faceType);
        Vector3f translation = new Vector3f(x,y,z);
        for(int i = 0; i < this.vertices.length; i++) {
            translatedFace.setFloatValue(i, 0, this.vertices[i].x);
            translatedFace.setFloatValue(i, 1, this.vertices[i].y);
            translatedFace.setFloatValue(i, 2, this.vertices[i].z);
            translatedFace.vertices[i].add(translation);
        }

        translatedFace.setNormal(this.normal.x, this.normal.y, this.normal.z);
        return translatedFace;
    }


    public int getFaceWidthX(){
        float[] xValues = new float[4];
        for(int i = 0; i < xValues.length; i++){
            xValues[i] = this.vertices[i].x;
        }
        Arrays.sort(xValues);
        return (int)((xValues[3] - xValues[0]) * 32);
    }
    public int getFaceWidthY(){
        float[] yValues = new float[4];
        for(int i = 0; i < yValues.length; i++){
            yValues[i] = this.vertices[i].y;
        }
        Arrays.sort(yValues);
        return (int)((yValues[3] - yValues[0]) * 32);
    }
    public int getFaceWidthZ(){
        float[] zValues = new float[4];
        for(int i = 0; i < zValues.length; i++){
            zValues[i] = this.vertices[i].z;
        }
        Arrays.sort(zValues);
        return (int)((zValues[3] - zValues[0]) * 32);
    }




}
