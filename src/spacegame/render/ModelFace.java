package spacegame.render;

public final class ModelFace {
    public float[][] vertices = new float[4][3]; //4 vertices plus the face normal
    public float[] normal = new float[3];
    public final int faceType;

    public ModelFace(int faceType) {
        this.faceType = faceType;
    }


    public void setFloatValue(int index, int subIndex, float value) {
        this.vertices[index][subIndex] = value;
    }

    public void setNormal(float normalX, float normalY, float normalZ){
        this.normal[0] = normalX;
        this.normal[1] = normalY;
        this.normal[2] = normalZ;
    }
}
