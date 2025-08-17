package spacegame.render;

import org.joml.Vector3f;
import org.joml.Vector4f;

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
}
