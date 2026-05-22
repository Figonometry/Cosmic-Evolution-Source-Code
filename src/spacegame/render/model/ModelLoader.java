package spacegame.render.model;

import org.joml.Vector3f;
import spacegame.render.RenderBlocks;
import spacegame.render.RenderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelLoader{
    public ModelFace[] modelFaces = new ModelFace[512];
    public String filepath;
    private int sortType = 0;
    public boolean usesMultipleTextures;

    public ModelLoader(String filepath, boolean usesMultipleTextures) {
        this.filepath = filepath;
        this.usesMultipleTextures = usesMultipleTextures;

        this.loadModelFromOBJFile();
    }



    /*
    Top Face vertex order
    xMax
    zLeast

    xLeast
    zMax

    xMax
    zMax

    xLeast
    zLeast

    Bottom Face vertex order
    xLeast
    zLeast

    xMax
    zMax

    xLeast
    zMax

    xMax
    zLeast

    North Face vertex order
    yLeast
    zLeast

    yMax
    zMax

    yMax
    zLeast

    yLeast
    zMax

    South Face vertex order
    yLeast
    zMax

    yMax
    zLeast

    yMax
    zMax

    yLeast
    zLeast

    East Face vertex order
    xMax
    yLeast

    xLeast
    yMax

    xMax
    yMax

    xLeast
    yLeast

    West Face vertex order
    xLeast
    yLeast

    xMax
    yMax

    xLeast
    yMax

    xMax
    yLeast
     */

    private ModelLoader(){}

    public void loadModelFromOBJFile() {
        File modelFile = new File(this.filepath);

        List<float[]> vertexList = new ArrayList<>();
        List<float[]> uvList     = new ArrayList<>();
        List<float[]> normalList = new ArrayList<>();
        List<ModelFace> faces    = new ArrayList<>();

        int faceTexture = RenderEngine.NULL_TEXTURE;

        try (BufferedReader reader = new BufferedReader(new FileReader(modelFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // ----- object / face name -----
                if (line.startsWith("o ")) {
                    String name = line.substring(2);

                    if (this.usesMultipleTextures) {
                        String[] parts = name.split("-");
                        faceTexture = Integer.parseInt(parts[0]);
                        switch (parts[1]) {
                            case "topFace"    -> this.sortType = RenderBlocks.TOP_FACE;
                            case "bottomFace" -> this.sortType = RenderBlocks.BOTTOM_FACE;
                            case "northFace"  -> this.sortType = RenderBlocks.NORTH_FACE;
                            case "southFace"  -> this.sortType = RenderBlocks.SOUTH_FACE;
                            case "eastFace"   -> this.sortType = RenderBlocks.EAST_FACE;
                            case "westFace"   -> this.sortType = RenderBlocks.WEST_FACE;
                        }
                    } else {
                        faceTexture = RenderEngine.NULL_TEXTURE;
                        switch (name) {
                            case "topFace"    -> this.sortType = RenderBlocks.TOP_FACE;
                            case "bottomFace" -> this.sortType = RenderBlocks.BOTTOM_FACE;
                            case "northFace"  -> this.sortType = RenderBlocks.NORTH_FACE;
                            case "southFace"  -> this.sortType = RenderBlocks.SOUTH_FACE;
                            case "eastFace"   -> this.sortType = RenderBlocks.EAST_FACE;
                            case "westFace"   -> this.sortType = RenderBlocks.WEST_FACE;
                        }
                    }
                    continue;
                }

                // ----- vertices -----
                if (line.startsWith("v ")) {
                    String[] s = line.split("\\s+");
                    float x = this.clampFloat(Float.parseFloat(s[1]));
                    float y = this.clampFloat(Float.parseFloat(s[2]));
                    float z = this.clampFloat(Float.parseFloat(s[3]));
                    vertexList.add(new float[]{x, y, z});
                    continue;
                }

                // ----- UVs -----
                if (line.startsWith("vt ")) {
                    String[] s = line.split("\\s+");
                    float u = Float.parseFloat(s[1]);
                    float v = Float.parseFloat(s[2]);
                    uvList.add(new float[]{u, v});
                    continue;
                }

                // ----- normals -----
                if (line.startsWith("vn ")) {
                    String[] s = line.split("\\s+");
                    float nx = this.clampFloat(Float.parseFloat(s[1]));
                    float ny = this.clampFloat(Float.parseFloat(s[2]));
                    float nz = this.clampFloat(Float.parseFloat(s[3]));
                    normalList.add(new float[]{nx, ny, nz});
                    continue;
                }

                // ----- faces -----
                if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length != 5) {
                        // expecting quads only
                        continue;
                    }

                    float[][] faceVerts  = new float[4][3];
                    float[][] faceUVs    = new float[4][2];
                    float[]   faceNormal = new float[3];

                    for (int i = 0; i < 4; i++) {
                        String[] idx = parts[i + 1].split("/");

                        int vIdx  = Integer.parseInt(idx[0]) - 1;
                        int vtIdx = Integer.parseInt(idx[1]) - 1;
                        int vnIdx = Integer.parseInt(idx[2]) - 1;



                        float[] v = vertexList.get(vIdx);
                        float[] t = uvList.get(vtIdx);
                        float[] n = normalList.get(vnIdx);

                        faceVerts[i][0] = v[0];
                        faceVerts[i][1] = v[1];
                        faceVerts[i][2] = v[2];

                        faceUVs[i][0] = t[0];
                        faceUVs[i][1] = 1.0f - t[1]; //This is needed because of the difference between blockbench model UVs and what OpenGL expects

                        faceNormal[0] = n[0];
                        faceNormal[1] = n[1];
                        faceNormal[2] = n[2];
                    }

                    SortedFace sorted = this.sortFace(faceVerts, faceUVs, faceNormal);

                    ModelFace mf = new ModelFace(this.sortType);
                    for (int i = 0; i < 4; i++) {
                        mf.setFloatValue(i, 0, sorted.verts[i][0]);
                        mf.setFloatValue(i, 1, sorted.verts[i][1]);
                        mf.setFloatValue(i, 2, sorted.verts[i][2]);

                        mf.setUV(i, 0, sorted.uvs[i][0]);
                        mf.setUV(i, 1, sorted.uvs[i][1]);
                    }
                    mf.setNormal(faceNormal[0], faceNormal[1], faceNormal[2]);
                    mf.texture = faceTexture;

                    faces.add(mf);
                }
            }
        } catch (IOException e) {
            System.out.println(this.filepath); //I need to know which model threw since these are all loaded at class load
            throw new RuntimeException(e);
        }

        // shrink to exact size
        this.modelFaces = faces.toArray(new ModelFace[0]);
    }


    private float clampFloat(float value) {
        if (value <= 0.00000000001F && value >= -0.000000000001F) {
            return 0;
        } else if (value >= 0.999999F & value <= 1.000000001F) {
            return 1;
        }
        return value;
    }


    public static class SortedFace {
        public float[][] verts;
        public float[][] uvs;
    }

    private SortedFace sortFace(float[][] verts, float[][] uvs, float[] normalArr) {

        // Convert verts to Vector3f for math
        Vector3f[] v = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            v[i] = new Vector3f(verts[i][0], verts[i][1], verts[i][2]);
        }

        // Convert normal
        Vector3f normal = new Vector3f(normalArr[0], normalArr[1], normalArr[2]).normalize();

        // Compute centroid
        Vector3f centroid = new Vector3f();
        for (Vector3f p : v) centroid.add(p);
        centroid.div(4f);

        // Build tangent/bitangent basis
        Vector3f up = new Vector3f(0, 1, 0);
        if (Math.abs(normal.dot(up)) > 0.9f) {
            up.set(1, 0, 0);
        }

        Vector3f tangent = new Vector3f();
        normal.cross(up, tangent).normalize();

        Vector3f bitangent = new Vector3f();
        tangent.cross(normal, bitangent).normalize();

        // Project verts into 2D
        float[][] pts2D = new float[4][2];
        for (int i = 0; i < 4; i++) {
            Vector3f rel = new Vector3f(v[i]).sub(centroid);
            pts2D[i][0] = rel.dot(tangent);
            pts2D[i][1] = rel.dot(bitangent);
        }

        // Sort by angle
        Integer[] order = {0, 1, 2, 3};
        Arrays.sort(order, (a, b) -> {
            float angleA = (float) Math.atan2(pts2D[a][1], pts2D[a][0]);
            float angleB = (float) Math.atan2(pts2D[b][1], pts2D[b][0]);
            return Float.compare(angleA, angleB);
        });

        // Reorder verts
        Vector3f[] sortedVec = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            sortedVec[i] = v[order[i]];
        }

        // Enforce CCW winding
        Vector3f a = new Vector3f(sortedVec[1]).sub(sortedVec[0]);
        Vector3f b = new Vector3f(sortedVec[2]).sub(sortedVec[0]);
        Vector3f cross = new Vector3f(a).cross(b);

        if (cross.dot(normal) < 0) {
            // Reverse 1 and 3
            Vector3f tmp = sortedVec[1];
            sortedVec[1] = sortedVec[3];
            sortedVec[3] = tmp;

            // Also reverse UVs
            float[] tmpUV = uvs[order[1]];
            uvs[order[1]] = uvs[order[3]];
            uvs[order[3]] = tmpUV;
        }

        // Build output
        SortedFace out = new SortedFace();
        out.verts = new float[4][3];
        out.uvs   = new float[4][2];

        for (int i = 0; i < 4; i++) {
            out.verts[i][0] = sortedVec[i].x;
            out.verts[i][1] = sortedVec[i].y;
            out.verts[i][2] = sortedVec[i].z;

            out.uvs[i][0] = uvs[order[i]][0];
            out.uvs[i][1] = uvs[order[i]][1];
        }

        return out;
    }



    public void addModelFace(ModelFace modelFace){
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] == null){
                this.modelFaces[i] = modelFace;
                break;
            }
        }
    }


    public float getModelHeight(){
        float highest = 0f;
        float lowest = 0f;

        for(int i = 0; i < this.modelFaces.length; i++){
            for(int j = 0; j < this.modelFaces[i].vertices.length; j++){
                if(this.modelFaces[i].vertices[j].y < lowest){
                    lowest = this.modelFaces[i].vertices[j].y;
                }

                if(this.modelFaces[i].vertices[j].y > highest){
                    highest = this.modelFaces[i].vertices[j].y;
                }
            }
        }


        return highest - lowest;
    }


    public ModelLoader scaleModel(float scaleFactor){
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                for(int j = 0; j < this.modelFaces[i].vertices.length; j++){
                    this.modelFaces[i].vertices[j].x = this.modelFaces[i].vertices[j].x * scaleFactor;
                    this.modelFaces[i].vertices[j].y = this.modelFaces[i].vertices[j].y * scaleFactor;
                    this.modelFaces[i].vertices[j].z = this.modelFaces[i].vertices[j].z * scaleFactor;
                }
            }
        }
        return this;
    }

    public ModelLoader translateModel(float x, float y, float z){
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                for(int j = 0; j < this.modelFaces[i].vertices.length; j++){
                    this.modelFaces[i].vertices[j].x = this.modelFaces[i].vertices[j].x + x;
                    this.modelFaces[i].vertices[j].y = this.modelFaces[i].vertices[j].y + y;
                    this.modelFaces[i].vertices[j].z = this.modelFaces[i].vertices[j].z + z;
                }
            }
        }
        return this;
    }

    public ModelLoader rotateModel(float deg, float x, float y, float z){
        float rad = (float) Math.toRadians(deg);
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                for(int j = 0; j < this.modelFaces[i].vertices.length; j++){

                    if(x == 1){
                        this.modelFaces[i].vertices[j].rotateX(rad);
                    }
                    if(y == 1) {
                        this.modelFaces[i].vertices[j].rotateY(rad);
                    }
                    if(z == 1){
                        this.modelFaces[i].vertices[j].rotateZ(rad);
                    }

                }

                if(x == 1){
                    this.modelFaces[i].normal.rotateX(rad);
                }
                if(y == 1) {
                    this.modelFaces[i].normal.rotateY(rad);
                }
                if(z == 1){
                    this.modelFaces[i].normal.rotateZ(rad);
                }
            }
        }
        return this;
    }

    public ModelLoader alterStandardBlockModel(int xFactor, int yFactor, int zFactor) {
        final float c = 0.03125f; // 1/32
        final float center = 0.5f;

        ModelLoader out = new ModelLoader();
        out.usesMultipleTextures = this.usesMultipleTextures;
        for (int i = 0; i < this.modelFaces.length; i++) {
            ModelFace src = this.modelFaces[i];
            if (src == null) continue;

            ModelFace f = src.copyFace();

            for (int v = 0; v < 4; v++) {
                float x = f.vertices[v].x;
                float y = f.vertices[v].y;
                float z = f.vertices[v].z;

                // shrink in X around center
                if (xFactor != 0) {
                    if (x < center) x += c * xFactor;
                    else            x -= c * xFactor;
                }

                // shrink in Z around center
                if (zFactor != 0) {
                    if (z < center) z += c * zFactor;
                    else            z -= c * zFactor;
                }

                // shrink in Y only for top/bottom faces
                if (yFactor != 0) {
                    if (f.faceType == RenderBlocks.TOP_FACE) {
                        // move top downward
                        y -= c * yFactor;
                    } else if (f.faceType == RenderBlocks.BOTTOM_FACE) {
                        // move bottom upward
                        y += c * yFactor;
                    }
                }

                f.vertices[v].x = x;
                f.vertices[v].y = y;
                f.vertices[v].z = z;
            }

            out.addModelFace(f);
        }

        // trim nulls if you still want the compact array
        int count = 0;
        for (ModelFace mf : out.modelFaces) if (mf != null) count++;
        ModelFace[] compact = new ModelFace[count];
        int idx = 0;
        for (ModelFace mf : out.modelFaces) if (mf != null) compact[idx++] = mf;
        out.modelFaces = compact;

        return out;
    }



    public ModelLoader extendTopFace(){
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    alteredFace.UVs[j][0] = this.modelFaces[i].UVs[j][0];
                    alteredFace.UVs[j][1] = this.modelFaces[i].UVs[j][1];
                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredFace.texture = this.modelFaces[i].texture;
                alteredModel.addModelFace(alteredFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < alteredModel.modelFaces.length; i++) {
            if (alteredModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = alteredModel.modelFaces[i];
        }

        alteredModel.modelFaces = newModelFaces;

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            alteredFace = alteredModel.modelFaces[i];
            switch (alteredFace.faceType){
                case RenderBlocks.TOP_FACE -> {
                    for(int j = 0; j < alteredFace.vertices.length; j++){
                        alteredFace.vertices[j].y += 0.5F;
                    }
                }
                case RenderBlocks.NORTH_FACE, RenderBlocks.SOUTH_FACE, RenderBlocks.EAST_FACE, RenderBlocks.WEST_FACE -> {
                    alteredFace.vertices[1].y += 0.5F;
                    alteredFace.vertices[2].y += 0.5F;
                }
            }
        }

        return alteredModel;
    }

    public ModelLoader extendBottomFace(){
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    alteredFace.UVs[j][0] = this.modelFaces[i].UVs[j][0];
                    alteredFace.UVs[j][1] = this.modelFaces[i].UVs[j][1];
                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredFace.texture = this.modelFaces[i].texture;
                alteredModel.addModelFace(alteredFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < alteredModel.modelFaces.length; i++) {
            if (alteredModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = alteredModel.modelFaces[i];
        }

        alteredModel.modelFaces = newModelFaces;

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            alteredFace = alteredModel.modelFaces[i];
            switch (alteredFace.faceType){
                case RenderBlocks.BOTTOM_FACE -> {
                    for(int j = 0; j < alteredFace.vertices.length; j++){
                        alteredFace.vertices[j].y -= 0.5F;
                    }
                }
                case RenderBlocks.NORTH_FACE, RenderBlocks.SOUTH_FACE, RenderBlocks.EAST_FACE, RenderBlocks.WEST_FACE -> {
                    alteredFace.vertices[0].y -= 0.5F;
                    alteredFace.vertices[3].y -= 0.5F;
                }
            }
        }

        return alteredModel;
    }

    public ModelLoader extendNorthFace(){
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    alteredFace.UVs[j][0] = this.modelFaces[i].UVs[j][0];
                    alteredFace.UVs[j][1] = this.modelFaces[i].UVs[j][1];

                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredFace.texture = this.modelFaces[i].texture;
                alteredModel.addModelFace(alteredFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < alteredModel.modelFaces.length; i++) {
            if (alteredModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = alteredModel.modelFaces[i];
        }

        alteredModel.modelFaces = newModelFaces;

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            alteredFace = alteredModel.modelFaces[i];
            switch (alteredFace.faceType){
                case RenderBlocks.TOP_FACE, RenderBlocks.EAST_FACE -> {
                    alteredFace.vertices[1].x -= 0.5F;
                    alteredFace.vertices[3].x -= 0.5F;
                }
                case RenderBlocks.BOTTOM_FACE, RenderBlocks.WEST_FACE -> {
                    alteredFace.vertices[0].x -= 0.5F;
                    alteredFace.vertices[2].x -= 0.5F;
                }
                case RenderBlocks.NORTH_FACE -> {
                    for(int j = 0; j < alteredFace.vertices.length; j++){
                        alteredFace.vertices[j].x -= 0.5F;

                    }
                }
            }
        }

        return alteredModel;
    }

    public ModelLoader extendSouthFace(){
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    alteredFace.UVs[j][0] = this.modelFaces[i].UVs[j][0];
                    alteredFace.UVs[j][1] = this.modelFaces[i].UVs[j][1];
                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredFace.texture = this.modelFaces[i].texture;
                alteredModel.addModelFace(alteredFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < alteredModel.modelFaces.length; i++) {
            if (alteredModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = alteredModel.modelFaces[i];
        }

        alteredModel.modelFaces = newModelFaces;

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            alteredFace = alteredModel.modelFaces[i];
            switch (alteredFace.faceType){
                case RenderBlocks.BOTTOM_FACE, RenderBlocks.WEST_FACE  -> {
                    alteredFace.vertices[1].x += 0.5F;
                    alteredFace.vertices[3].x += 0.5F;
                }
                case RenderBlocks.EAST_FACE, RenderBlocks.TOP_FACE -> {
                    alteredFace.vertices[0].x += 0.5F;
                    alteredFace.vertices[2].x += 0.5F;
                }
                case RenderBlocks.SOUTH_FACE -> {
                    for(int j = 0; j < alteredFace.vertices.length; j++){
                        alteredFace.vertices[j].x += 0.5F;

                    }
                }
            }
        }

        return alteredModel;
    }

    public ModelLoader extendEastFace(){
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    alteredFace.UVs[j][0] = this.modelFaces[i].UVs[j][0];
                    alteredFace.UVs[j][1] = this.modelFaces[i].UVs[j][1];
                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredFace.texture = this.modelFaces[i].texture;
                alteredModel.addModelFace(alteredFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < alteredModel.modelFaces.length; i++) {
            if (alteredModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = alteredModel.modelFaces[i];
        }

        alteredModel.modelFaces = newModelFaces;

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            alteredFace = alteredModel.modelFaces[i];
            switch (alteredFace.faceType){
                case RenderBlocks.TOP_FACE, RenderBlocks.BOTTOM_FACE -> {
                    alteredFace.vertices[0].z -= 0.5F;
                    alteredFace.vertices[3].z -= 0.5F;
                }
                case RenderBlocks.NORTH_FACE -> {
                    alteredFace.vertices[0].z -= 0.5F;
                    alteredFace.vertices[2].z -= 0.5F;
                }
                case RenderBlocks.SOUTH_FACE -> {
                    alteredFace.vertices[1].z -= 0.5F;
                    alteredFace.vertices[3].z -= 0.5F;
                }
                case RenderBlocks.EAST_FACE -> {
                    for(int j = 0; j < alteredFace.vertices.length; j++){
                        alteredFace.vertices[j].z -= 0.5F;
                    }
                }
            }
        }

        return alteredModel;
    }

    public ModelLoader extendWestFace(){
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    alteredFace.UVs[j][0] = this.modelFaces[i].UVs[j][0];
                    alteredFace.UVs[j][1] = this.modelFaces[i].UVs[j][1];
                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredFace.texture = this.modelFaces[i].texture;
                alteredModel.addModelFace(alteredFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < alteredModel.modelFaces.length; i++) {
            if (alteredModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = alteredModel.modelFaces[i];
        }

        alteredModel.modelFaces = newModelFaces;

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            alteredFace = alteredModel.modelFaces[i];
            switch (alteredFace.faceType){
                case RenderBlocks.TOP_FACE, RenderBlocks.BOTTOM_FACE -> {
                    alteredFace.vertices[1].z += 0.5F;
                    alteredFace.vertices[2].z += 0.5F;
                }
                case RenderBlocks.NORTH_FACE -> {
                    alteredFace.vertices[1].z += 0.5F;
                    alteredFace.vertices[3].z += 0.5F;
                }
                case RenderBlocks.SOUTH_FACE -> {
                    alteredFace.vertices[0].z += 0.5F;
                    alteredFace.vertices[2].z += 0.5F;
                }
                case RenderBlocks.WEST_FACE -> {
                    for(int j = 0; j < alteredFace.vertices.length; j++){
                        alteredFace.vertices[j].z += 0.5F;
                    }
                }
            }
        }

        return alteredModel;
    }

    //meant for the default block model
    public ModelFace getModelFace(int face){
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i].faceType == face){
                return this.modelFaces[i];
            }
        }
        return null;
    }

    public ModelFace[] getModelFaceOfType(int face){
        ModelFace[] faces = new ModelFace[this.modelFaces.length];
        int index = 0;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i].faceType == face){
                faces[index] = this.modelFaces[i];
                index++;
            }
        }

        return faces;
    }


    public ModelLoader copyModel(){
        ModelLoader returnModel = new ModelLoader();
        returnModel.modelFaces = new ModelFace[this.modelFaces.length];
        returnModel.usesMultipleTextures = this.usesMultipleTextures;

        for(int i = 0; i < this.modelFaces.length; i++){

            returnModel.modelFaces[i] = new ModelFace(this.modelFaces[i].faceType);

            returnModel.modelFaces[i].normal.x = this.modelFaces[i].normal.x;
            returnModel.modelFaces[i].normal.y = this.modelFaces[i].normal.y;
            returnModel.modelFaces[i].normal.z = this.modelFaces[i].normal.z;

            returnModel.modelFaces[i].texture = this.modelFaces[i].texture;

            for(int j = 0; j < returnModel.modelFaces[i].vertices.length; j++){
                returnModel.modelFaces[i].vertices[j].x = this.modelFaces[i].vertices[j].x;
                returnModel.modelFaces[i].vertices[j].y = this.modelFaces[i].vertices[j].y;
                returnModel.modelFaces[i].vertices[j].z = this.modelFaces[i].vertices[j].z;

                returnModel.modelFaces[i].UVs[j][0] = this.modelFaces[i].UVs[j][0];
                returnModel.modelFaces[i].UVs[j][1] = this.modelFaces[i].UVs[j][1];
            }
        }

        return returnModel;
    }




}


