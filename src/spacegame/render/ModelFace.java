package spacegame.render;

public final class ModelFace {
    public float[][] vertices = new float[5][3]; //4 vertices plus the face normal
    public final int faceType;

    public ModelFace(int faceType) {
        this.faceType = faceType;
    }


    public void setFloatValue(int index, int subIndex, float value) {
        this.vertices[index][subIndex] = value;
    }
}
