package spacegame.render.model;

import org.joml.Vector3f;
import spacegame.render.RenderBlocks;
import spacegame.render.RenderEngine;
import spacegame.util.MathUtil;

public final class ModelFace {
    public float[][] UVs = new float[4][2];
    public Vector3f[] vertices = new Vector3f[4];
    public Vector3f normal = new Vector3f();
    public final int faceType;
    public int texture = RenderEngine.NULL_TEXTURE;

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

    public void setUV(int index, int subIndex, float value){
        this.UVs[index][subIndex] = value;
    }

    public ModelFace translateFace(float x, float y, float z){
        ModelFace translatedFace = new ModelFace(this.faceType);
        Vector3f translation = new Vector3f(x,y,z);
        for(int i = 0; i < this.vertices.length; i++) {
            translatedFace.setFloatValue(i, 0, this.vertices[i].x);
            translatedFace.setFloatValue(i, 1, this.vertices[i].y);
            translatedFace.setFloatValue(i, 2, this.vertices[i].z);

            translatedFace.setUV(i, 0, this.UVs[i][0]);
            translatedFace.setUV(i, 1, this.UVs[i][1]);

            translatedFace.vertices[i].add(translation);
        }
        translatedFace.texture = this.texture;
        translatedFace.setNormal(this.normal.x, this.normal.y, this.normal.z);
        return translatedFace;
    }

    public ModelFace rotateFace(float x, float y, float z){
        ModelFace rotatedFace = new ModelFace(this.faceType);
        for(int i = 0; i < this.vertices.length; i++) {
            rotatedFace.setFloatValue(i, 0, this.vertices[i].x);
            rotatedFace.setFloatValue(i, 1, this.vertices[i].y);
            rotatedFace.setFloatValue(i, 2, this.vertices[i].z);

            rotatedFace.setUV(i, 0, this.UVs[i][0]);
            rotatedFace.setUV(i, 1, this.UVs[i][1]);


            rotatedFace.vertices[i].rotateX(x);
            rotatedFace.vertices[i].rotateY(y);
            rotatedFace.vertices[i].rotateZ(z);
        }
        rotatedFace.texture = this.texture;
        rotatedFace.setNormal(this.normal.x, this.normal.y, this.normal.z);
        return rotatedFace;
    }



    //Use x for width on top and bottom, use z for width on north/south, use x for width on east/west
    public int getFaceWidth(){
        return Math.abs((int) (MathUtil.distance3D(this.vertices[0].x, this.vertices[0].y, this.vertices[0].z, this.vertices[3].x, this.vertices[3].y, this.vertices[3].z) * 32));
    }

    //Use z for height on top and bottom, use y for height on north, south, east and west
    public int getFaceHeight(){
        return (int) Math.abs(MathUtil.distance3D(this.vertices[1].x, this.vertices[1].y, this.vertices[1].z, this.vertices[3].x, this.vertices[3].y, this.vertices[3].z) * 32);
    }

    public ModelFace getScaledFace(float scale){
        ModelFace scaledFace = new ModelFace(this.faceType);
        for(int i = 0; i < scaledFace.vertices.length; i++){
            scaledFace.setFloatValue(i, 0, this.vertices[i].x);
            scaledFace.setFloatValue(i, 1, this.vertices[i].y);
            scaledFace.setFloatValue(i, 2, this.vertices[i].z);
            scaledFace.vertices[i].mul(scale);

            scaledFace.setUV(i, 0, this.UVs[i][0]);
            scaledFace.setUV(i, 1, this.UVs[i][1]);
        }
        scaledFace.texture = this.texture;
        scaledFace.setNormal(this.normal.x, this.normal.y, this.normal.z);

        return scaledFace;
    }

    public ModelFace copyFace(){
        ModelFace modelFace = new ModelFace(this.faceType);

        for(int i = 0; i < modelFace.vertices.length; i++){
            modelFace.setFloatValue(i, 0, this.vertices[i].x);
            modelFace.setFloatValue(i, 1, this.vertices[i].y);
            modelFace.setFloatValue(i, 2, this.vertices[i].z);

            modelFace.setUV(i, 0, this.UVs[i][0]);
            modelFace.setUV(i, 1, this.UVs[i][1]);
        }
        modelFace.texture = this.texture;
        modelFace.setNormal(this.normal.x, this.normal.y, this.normal.z);

        return modelFace;
    }


        /**
         * meshSize[0] and meshSize[1] are 0..31 from the greedy mesher.
         * 0 means "just this block", so we use (size + 1) as the actual span.
         */
        public void applyGreedyMeshing(int[] meshSize) {

            if(meshSize[0] == 0 && meshSize[1] == 0)return;

            int w = meshSize[0] + 1;
            int h = meshSize[1] + 1;

            switch (this.faceType) {
                case RenderBlocks.TOP_FACE    -> applyTopGreedy(w, h);
                case RenderBlocks.BOTTOM_FACE -> applyBottomGreedy(w, h);
                case RenderBlocks.NORTH_FACE  -> applyNorthGreedy(w, h);
                case RenderBlocks.SOUTH_FACE  -> applySouthGreedy(w, h);
                case RenderBlocks.EAST_FACE   -> applyEastGreedy(w, h);
                case RenderBlocks.WEST_FACE   -> applyWestGreedy(w, h);
            }
        }

        // +Y, plane y = 1, expands in +X and +Z, CCW when viewed from above
        private void applyTopGreedy(int w, int h) {
            // Flip winding compared to previous version
            vertices[0].set(0, 1, 0);
            vertices[1].set(0, 1, h);
            vertices[2].set(w, 1, h);
            vertices[3].set(w, 1, 0);

            UVs[0][0] = 0; UVs[0][1] = 0;
            UVs[1][0] = 0; UVs[1][1] = h;
            UVs[2][0] = w; UVs[2][1] = h;
            UVs[3][0] = w; UVs[3][1] = 0;
        }



        // -Y, plane y = 0, expands in +X and +Z, CCW when viewed from below
        private void applyBottomGreedy(int w, int h) {
            // CCW when viewed from below (normal = -Y)
            vertices[0].set(0, 0, 0);
            vertices[1].set(w, 0, 0);
            vertices[2].set(w, 0, h);
            vertices[3].set(0, 0, h);

            UVs[0][0] = 0; UVs[0][1] = 0;
            UVs[1][0] = w; UVs[1][1] = 0;
            UVs[2][0] = w; UVs[2][1] = h;
            UVs[3][0] = 0; UVs[3][1] = h;
        }


        // -X (north), plane x = 0, expands in +Z and +Y
        private void applyNorthGreedy(int w, int h) {
            // CCW when viewed from -X
            vertices[0].set(0, 0, 0);
            vertices[1].set(0, 0, w);
            vertices[2].set(0, h, w);
            vertices[3].set(0, h, 0);

            UVs[0][0] = 0; UVs[0][1] = h;
            UVs[1][0] = w; UVs[1][1] = h;
            UVs[2][0] = w; UVs[2][1] = 0;
            UVs[3][0] = 0; UVs[3][1] = 0;

        }


        // +X (south), plane x = 1, expands in +Z and +Y
        private void applySouthGreedy(int w, int h) {
            // CCW when viewed from +X
            vertices[0].set(1, 0, w);
            vertices[1].set(1, 0, 0);
            vertices[2].set(1, h, 0);
            vertices[3].set(1, h, w);

            UVs[0][0] = w; UVs[0][1] = h;
            UVs[1][0] = 0; UVs[1][1] = h;
            UVs[2][0] = 0; UVs[2][1] = 0;
            UVs[3][0] = w; UVs[3][1] = 0;

        }


        // -Z (east), plane z = 0, expands in +X and +Y
        private void applyEastGreedy(int w, int h) {
            // CCW when viewed from -Z
            vertices[0].set(w, 0, 0);
            vertices[1].set(0, 0, 0);
            vertices[2].set(0, h, 0);
            vertices[3].set(w, h, 0);

            UVs[0][0] = w; UVs[0][1] = h;
            UVs[1][0] = 0; UVs[1][1] = h;
            UVs[2][0] = 0; UVs[2][1] = 0;
            UVs[3][0] = w; UVs[3][1] = 0;

        }


        // +Z (west), plane z = 1, expands in +X and +Y
        private void applyWestGreedy(int w, int h) {
            // CCW when viewed from +Z
            vertices[0].set(0, 0, 1);
            vertices[1].set(w, 0, 1);
            vertices[2].set(w, h, 1);
            vertices[3].set(0, h, 1);

            UVs[0][0] = 0; UVs[0][1] = h;
            UVs[1][0] = w; UVs[1][1] = h;
            UVs[2][0] = w; UVs[2][1] = 0;
            UVs[3][0] = 0; UVs[3][1] = 0;

        }

    public int findVertex(float x, float y, float z) {
        for (int i = 0; i < 4; i++) {
            if (vertices[i].x == x && vertices[i].y == y && vertices[i].z == z) {
                return i;
            }
        }
        return -1;
    }

    public int minX(ModelFace f) {
        int idx = 0;
        float v = f.vertices[0].x;
        for (int i = 1; i < 4; i++) {
            if (f.vertices[i].x < v) {
                v = f.vertices[i].x;
                idx = i;
            }
        }
        return idx;
    }

    public int maxX(ModelFace f) {
        int idx = 0;
        float v = f.vertices[0].x;
        for (int i = 1; i < 4; i++) {
            if (f.vertices[i].x > v) {
                v = f.vertices[i].x;
                idx = i;
            }
        }
        return idx;
    }

    public int minY(ModelFace f) {
        int idx = 0;
        float v = f.vertices[0].y;
        for (int i = 1; i < 4; i++) {
            if (f.vertices[i].y < v) {
                v = f.vertices[i].y;
                idx = i;
            }
        }
        return idx;
    }

    public int maxY(ModelFace f) {
        int idx = 0;
        float v = f.vertices[0].y;
        for (int i = 1; i < 4; i++) {
            if (f.vertices[i].y > v) {
                v = f.vertices[i].y;
                idx = i;
            }
        }
        return idx;
    }

    public int minZ(ModelFace f) {
        int idx = 0;
        float v = f.vertices[0].z;
        for (int i = 1; i < 4; i++) {
            if (f.vertices[i].z < v) {
                v = f.vertices[i].z;
                idx = i;
            }
        }
        return idx;
    }

    public int maxZ(ModelFace f) {
        int idx = 0;
        float v = f.vertices[0].z;
        for (int i = 1; i < 4; i++) {
            if (f.vertices[i].z > v) {
                v = f.vertices[i].z;
                idx = i;
            }
        }
        return idx;
    }



}









