package spacegame.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public final class ModelLoader{
    public ModelFace[] modelFaces = new ModelFace[512];
    public String filepath;
    private int sortType = 0;
    private float[][] topFaceTemplate = new float[4][3];
    private float[][] bottomFaceTemplate = new float[4][3];
    private float[][] northFaceTemplate = new float[4][3];
    private float[][] southFaceTemplate = new float[4][3];
    private float[][] eastFaceTemplate = new float[4][3];
    private float[][] westFaceTemplate = new float[4][3];
    private float[][] topFaceUnsortedTemplate = new float[4][3];
    private float[][] bottomFaceUnsortedTemplate = new float[4][3];
    private float[][] northFaceUnsortedTemplate = new float[4][3];
    private float[][] southFaceUnsortedTemplate = new float[4][3];
    private float[][] eastFaceUnsortedTemplate = new float[4][3];
    private float[][] westFaceUnsortedTemplate = new float[4][3];

    public ModelLoader(String filepath) {
        this.filepath = filepath;

        this.loadModelFromOBJFile();
    }

    private ModelLoader(){}

    public void loadModelFromOBJFile() {
        File modelFile = new File(this.filepath);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(modelFile));
            String line = " ";
            int index = 0;
            int modelFaceIndex = 0;
            while ((line = reader.readLine()) != null) {

                if (line != null && line.length() > 0) {

                    switch (line) {
                        case "o topFace" -> this.sortType = RenderBlocks.TOP_FACE;
                        case "o bottomFace" -> this.sortType = RenderBlocks.BOTTOM_FACE;
                        case "o northFace" -> this.sortType = RenderBlocks.NORTH_FACE;
                        case "o southFace" -> this.sortType = RenderBlocks.SOUTH_FACE;
                        case "o eastFace" -> this.sortType = RenderBlocks.EAST_FACE;
                        case "o westFace" -> this.sortType = RenderBlocks.WEST_FACE;
                        case "o topFaceUnsorted" -> this.sortType = RenderBlocks.TOP_FACE_UNSORTED;
                        case "o bottomFaceUnsorted" -> this.sortType = RenderBlocks.BOTTOM_FACE_UNSORTED;
                        case "o northFaceUnsorted" -> this.sortType = RenderBlocks.NORTH_FACE_UNSORTED;
                        case "o southFaceUnsorted" -> this.sortType = RenderBlocks.SOUTH_FACE_UNSORTED;
                        case "o eastFaceUnsorted" -> this.sortType = RenderBlocks.EAST_FACE_UNSORTED;
                        case "o westFaceUnsorted" -> this.sortType = RenderBlocks.WEST_FACE_UNSORTED;
                    }

                    if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {
                        String[] vertexString = line.split(" ");
                        String[] rawVertices = new String[3];

                        rawVertices[0] = vertexString[1];
                        rawVertices[1] = vertexString[2];
                        rawVertices[2] = vertexString[3];

                        float[] vertex = new float[3];
                        vertex[0] = Float.parseFloat(rawVertices[0]);
                        vertex[1] = Float.parseFloat(rawVertices[1]);
                        vertex[2] = Float.parseFloat(rawVertices[2]);

                        vertex[0] = this.clampFloat(vertex[0]);
                        vertex[1] = this.clampFloat(vertex[1]);
                        vertex[2] = this.clampFloat(vertex[2]);

                        switch (this.sortType) {
                            case RenderBlocks.TOP_FACE -> this.topFaceTemplate[index] = vertex;
                            case RenderBlocks.BOTTOM_FACE -> this.bottomFaceTemplate[index] = vertex;
                            case RenderBlocks.NORTH_FACE -> this.northFaceTemplate[index] = vertex;
                            case RenderBlocks.SOUTH_FACE -> this.southFaceTemplate[index] = vertex;
                            case RenderBlocks.EAST_FACE -> this.eastFaceTemplate[index] = vertex;
                            case RenderBlocks.WEST_FACE -> this.westFaceTemplate[index] = vertex;
                            case RenderBlocks.TOP_FACE_UNSORTED -> this.topFaceUnsortedTemplate[index] = vertex;
                            case RenderBlocks.BOTTOM_FACE_UNSORTED -> this.bottomFaceUnsortedTemplate[index] = vertex;
                            case RenderBlocks.NORTH_FACE_UNSORTED -> this.northFaceUnsortedTemplate[index] = vertex;
                            case RenderBlocks.SOUTH_FACE_UNSORTED -> this.southFaceUnsortedTemplate[index] = vertex;
                            case RenderBlocks.EAST_FACE_UNSORTED -> this.eastFaceUnsortedTemplate[index] = vertex;
                            case RenderBlocks.WEST_FACE_UNSORTED -> this.westFaceUnsortedTemplate[index] = vertex;
                        }

                        index++;
                        if (index == 4) {
                            switch (this.sortType) {
                                case RenderBlocks.TOP_FACE -> this.topFaceTemplate = this.sortTopFace();
                                case RenderBlocks.BOTTOM_FACE -> this.bottomFaceTemplate = this.sortBottomFace();
                                case RenderBlocks.NORTH_FACE -> this.northFaceTemplate = this.sortNorthFace();
                                case RenderBlocks.SOUTH_FACE -> this.southFaceTemplate = this.sortSouthFace();
                                case RenderBlocks.EAST_FACE -> this.eastFaceTemplate = this.sortEastFace();
                                case RenderBlocks.WEST_FACE -> this.westFaceTemplate = this.sortWestFace();
                            }

                            switch (this.sortType) {
                                case RenderBlocks.TOP_FACE -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(0);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.topFaceTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.topFaceTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.topFaceTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.topFaceTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.topFaceTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.topFaceTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.topFaceTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.topFaceTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.topFaceTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.topFaceTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.topFaceTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.topFaceTemplate[3][2]);
                                }
                                case RenderBlocks.BOTTOM_FACE -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(1);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.bottomFaceTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.bottomFaceTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.bottomFaceTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.bottomFaceTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.bottomFaceTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.bottomFaceTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.bottomFaceTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.bottomFaceTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.bottomFaceTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.bottomFaceTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.bottomFaceTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.bottomFaceTemplate[3][2]);
                                }
                                case RenderBlocks.NORTH_FACE -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(2);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.northFaceTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.northFaceTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.northFaceTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.northFaceTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.northFaceTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.northFaceTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.northFaceTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.northFaceTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.northFaceTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.northFaceTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.northFaceTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.northFaceTemplate[3][2]);
                                }
                                case RenderBlocks.SOUTH_FACE -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(3);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.southFaceTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.southFaceTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.southFaceTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.southFaceTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.southFaceTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.southFaceTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.southFaceTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.southFaceTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.southFaceTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.southFaceTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.southFaceTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.southFaceTemplate[3][2]);
                                }
                                case RenderBlocks.EAST_FACE -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(4);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.eastFaceTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.eastFaceTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.eastFaceTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.eastFaceTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.eastFaceTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.eastFaceTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.eastFaceTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.eastFaceTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.eastFaceTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.eastFaceTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.eastFaceTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.eastFaceTemplate[3][2]);
                                }
                                case RenderBlocks.WEST_FACE -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(5);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.westFaceTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.westFaceTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.westFaceTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.westFaceTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.westFaceTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.westFaceTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.westFaceTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.westFaceTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.westFaceTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.westFaceTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.westFaceTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.westFaceTemplate[3][2]);
                                }
                                case RenderBlocks.TOP_FACE_UNSORTED -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(6);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.topFaceUnsortedTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.topFaceUnsortedTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.topFaceUnsortedTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.topFaceUnsortedTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.topFaceUnsortedTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.topFaceUnsortedTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.topFaceUnsortedTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.topFaceUnsortedTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.topFaceUnsortedTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.topFaceUnsortedTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.topFaceUnsortedTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.topFaceUnsortedTemplate[3][2]);
                                }
                                case RenderBlocks.BOTTOM_FACE_UNSORTED -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(7);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.bottomFaceUnsortedTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.bottomFaceUnsortedTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.bottomFaceUnsortedTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.bottomFaceUnsortedTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.bottomFaceUnsortedTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.bottomFaceUnsortedTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.bottomFaceUnsortedTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.bottomFaceUnsortedTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.bottomFaceUnsortedTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.bottomFaceUnsortedTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.bottomFaceUnsortedTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.bottomFaceUnsortedTemplate[3][2]);
                                }
                                case RenderBlocks.NORTH_FACE_UNSORTED -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(8);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.northFaceUnsortedTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.northFaceUnsortedTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.northFaceUnsortedTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.northFaceUnsortedTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.northFaceUnsortedTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.northFaceUnsortedTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.northFaceUnsortedTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.northFaceUnsortedTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.northFaceUnsortedTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.northFaceUnsortedTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.northFaceUnsortedTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.northFaceUnsortedTemplate[3][2]);
                                }
                                case RenderBlocks.SOUTH_FACE_UNSORTED -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(9);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.southFaceUnsortedTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.southFaceUnsortedTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.southFaceUnsortedTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.southFaceUnsortedTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.southFaceUnsortedTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.southFaceUnsortedTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.southFaceUnsortedTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.southFaceUnsortedTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.southFaceUnsortedTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.southFaceUnsortedTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.southFaceUnsortedTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.southFaceUnsortedTemplate[3][2]);
                                }
                                case RenderBlocks.EAST_FACE_UNSORTED -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(10);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.eastFaceUnsortedTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.eastFaceUnsortedTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.eastFaceUnsortedTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.eastFaceUnsortedTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.eastFaceUnsortedTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.eastFaceUnsortedTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.eastFaceUnsortedTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.eastFaceUnsortedTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.eastFaceUnsortedTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.eastFaceUnsortedTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.eastFaceUnsortedTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.eastFaceUnsortedTemplate[3][2]);
                                }
                                case RenderBlocks.WEST_FACE_UNSORTED -> {
                                    this.modelFaces[modelFaceIndex] = new ModelFace(11);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 0, this.westFaceUnsortedTemplate[0][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 1, this.westFaceUnsortedTemplate[0][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(0, 2, this.westFaceUnsortedTemplate[0][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 0, this.westFaceUnsortedTemplate[1][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 1, this.westFaceUnsortedTemplate[1][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(1, 2, this.westFaceUnsortedTemplate[1][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 0, this.westFaceUnsortedTemplate[2][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 1, this.westFaceUnsortedTemplate[2][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(2, 2, this.westFaceUnsortedTemplate[2][2]);

                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 0, this.westFaceUnsortedTemplate[3][0]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 1, this.westFaceUnsortedTemplate[3][1]);
                                    this.modelFaces[modelFaceIndex].setFloatValue(3, 2, this.westFaceUnsortedTemplate[3][2]);
                                }
                            }
                            index = 0;
                        }
                    }

                    if(line.charAt(0) == 'v' && line.charAt(1) == 'n'){
                        String[] normalString = line.split(" ");
                        String[] rawNormal = new String[3];

                        rawNormal[0] = normalString[1];
                        rawNormal[1] = normalString[2];
                        rawNormal[2] = normalString[3];

                        float[] normal = new float[3];
                        normal[0] = Float.parseFloat(rawNormal[0]);
                        normal[1] = Float.parseFloat(rawNormal[1]);
                        normal[2] = Float.parseFloat(rawNormal[2]);

                        normal[0] = this.clampFloat(normal[0]);
                        normal[1] = this.clampFloat(normal[1]);
                        normal[2] = this.clampFloat(normal[2]);

                        this.modelFaces[modelFaceIndex].setNormal(normal[0], normal[1], normal[2]);
                        modelFaceIndex++;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int modelFacesSize = 0;
        for (int i = 0; i < this.modelFaces.length; i++) {
            if (this.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = this.modelFaces[i];
        }

        this.modelFaces = newModelFaces;

    }

    private float clampFloat(float value) {
        if (value <= 0.00000000001F && value >= -0.000000000001F) {
            return 0;
        } else if (value >= 0.999999F & value <= 1.000000001F) {
            return 1;
        }
        return value;
    }

    private float[][] sortTopFace() {
        float[][] topFace = new float[4][3];
        float[] xValues = new float[4];
        float[] zValues = new float[4];
        int xValueIndex = 0;
        int zValueIndex = 0;

        for (int i = 0; i < this.topFaceTemplate.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    xValues[xValueIndex] = this.topFaceTemplate[i][j];
                    xValueIndex++;
                } else if (j == 2) {
                    zValues[zValueIndex] = this.topFaceTemplate[i][j];
                    zValueIndex++;
                }
            }
        }

        Arrays.sort(xValues);
        Arrays.sort(zValues);

        float xLeast = xValues[0];
        float xLess = xValues[1];
        float xGreater = xValues[2];
        float xGreatest = xValues[3];
        float zLeast = zValues[0];
        float zLess = zValues[1];
        float zGreater = zValues[2];
        float zGreatest = zValues[3];


        if (this.topFaceTemplate[0][0] == xGreatest && this.topFaceTemplate[0][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xGreatest && this.topFaceTemplate[1][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xGreatest && this.topFaceTemplate[2][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xGreatest && this.topFaceTemplate[3][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xGreater && this.topFaceTemplate[0][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xGreater && this.topFaceTemplate[1][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xGreater && this.topFaceTemplate[2][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xGreater && this.topFaceTemplate[3][2] == zLeast) {
            topFace[0] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xGreater && this.topFaceTemplate[0][2] == zLess) {
            topFace[0] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xGreater && this.topFaceTemplate[1][2] == zLess) {
            topFace[0] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xGreater && this.topFaceTemplate[2][2] == zLess) {
            topFace[0] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xGreater && this.topFaceTemplate[3][2] == zLess) {
            topFace[0] = this.topFaceTemplate[3];
        }


        if (this.topFaceTemplate[0][0] == xLeast && this.topFaceTemplate[0][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xLeast && this.topFaceTemplate[1][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xLeast && this.topFaceTemplate[2][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xLeast && this.topFaceTemplate[3][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xLess && this.topFaceTemplate[0][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xLess && this.topFaceTemplate[1][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xLess && this.topFaceTemplate[2][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xLess && this.topFaceTemplate[3][2] == zGreatest) {
            topFace[1] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xLess && this.topFaceTemplate[0][2] == zGreater) {
            topFace[1] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xLess && this.topFaceTemplate[1][2] == zGreater) {
            topFace[1] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xLess && this.topFaceTemplate[2][2] == zGreater) {
            topFace[1] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xLess && this.topFaceTemplate[3][2] == zGreater) {
            topFace[1] = this.topFaceTemplate[3];
        }


        if (this.topFaceTemplate[0][0] == xGreatest && this.topFaceTemplate[0][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xGreatest && this.topFaceTemplate[1][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xGreatest && this.topFaceTemplate[2][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xGreatest && this.topFaceTemplate[3][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xGreater && this.topFaceTemplate[0][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xGreater && this.topFaceTemplate[1][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xGreater && this.topFaceTemplate[2][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xGreater && this.topFaceTemplate[3][2] == zGreatest) {
            topFace[2] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xGreater && this.topFaceTemplate[0][2] == zGreater) {
            topFace[2] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xGreater && this.topFaceTemplate[1][2] == zGreater) {
            topFace[2] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xGreater && this.topFaceTemplate[2][2] == zGreater) {
            topFace[2] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xGreater && this.topFaceTemplate[3][2] == zGreater) {
            topFace[2] = this.topFaceTemplate[3];
        }


        if (this.topFaceTemplate[0][0] == xLeast && this.topFaceTemplate[0][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xLeast && this.topFaceTemplate[1][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xLeast && this.topFaceTemplate[2][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xLeast && this.topFaceTemplate[3][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xLess && this.topFaceTemplate[0][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xLess && this.topFaceTemplate[1][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xLess && this.topFaceTemplate[2][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xLess && this.topFaceTemplate[3][2] == zLeast) {
            topFace[3] = this.topFaceTemplate[3];
        } else if (this.topFaceTemplate[0][0] == xLess && this.topFaceTemplate[0][2] == zLess) {
            topFace[3] = this.topFaceTemplate[0];
        } else if (this.topFaceTemplate[1][0] == xLess && this.topFaceTemplate[1][2] == zLess) {
            topFace[3] = this.topFaceTemplate[1];
        } else if (this.topFaceTemplate[2][0] == xLess && this.topFaceTemplate[2][2] == zLess) {
            topFace[3] = this.topFaceTemplate[2];
        } else if (this.topFaceTemplate[3][0] == xLess && this.topFaceTemplate[3][2] == zLess) {
            topFace[3] = this.topFaceTemplate[3];
        }


        return topFace;
    }


    private float[][] sortBottomFace() {
        float[][] bottomFace = new float[4][3];
        float[] xValues = new float[4];
        float[] zValues = new float[4];
        int xValueIndex = 0;
        int zValueIndex = 0;

        for (int i = 0; i < this.bottomFaceTemplate.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    xValues[xValueIndex] = this.bottomFaceTemplate[i][j];
                    xValueIndex++;
                } else if (j == 2) {
                    zValues[zValueIndex] = this.bottomFaceTemplate[i][j];
                    zValueIndex++;
                }
            }
        }

        Arrays.sort(xValues);
        Arrays.sort(zValues);

        float xLeast = xValues[0];
        float xLess = xValues[1];
        float xGreater = xValues[2];
        float xGreatest = xValues[3];
        float zLeast = zValues[0];
        float zLess = zValues[1];
        float zGreater = zValues[2];
        float zGreatest = zValues[3];


        if (this.bottomFaceTemplate[0][0] == xLeast && this.bottomFaceTemplate[0][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xLeast && this.bottomFaceTemplate[1][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xLeast && this.bottomFaceTemplate[2][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xLeast && this.bottomFaceTemplate[3][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xLess && this.bottomFaceTemplate[0][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xLess && this.bottomFaceTemplate[1][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xLess && this.bottomFaceTemplate[2][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xLess && this.bottomFaceTemplate[3][2] == zLeast) {
            bottomFace[0] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xLess && this.bottomFaceTemplate[0][2] == zLess) {
            bottomFace[0] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xLess && this.bottomFaceTemplate[1][2] == zLess) {
            bottomFace[0] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xLess && this.bottomFaceTemplate[2][2] == zLess) {
            bottomFace[0] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xLess && this.bottomFaceTemplate[3][2] == zLess) {
            bottomFace[0] = this.bottomFaceTemplate[3];
        }


        if (this.bottomFaceTemplate[0][0] == xGreatest && this.bottomFaceTemplate[0][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xGreatest && this.bottomFaceTemplate[1][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xGreatest && this.bottomFaceTemplate[2][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xGreatest && this.bottomFaceTemplate[3][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xGreater && this.bottomFaceTemplate[0][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xGreater && this.bottomFaceTemplate[1][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xGreater && this.bottomFaceTemplate[2][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xGreater && this.bottomFaceTemplate[3][2] == zGreatest) {
            bottomFace[1] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xGreater && this.bottomFaceTemplate[0][2] == zGreater) {
            bottomFace[1] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xGreater && this.bottomFaceTemplate[1][2] == zGreater) {
            bottomFace[1] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xGreater && this.bottomFaceTemplate[2][2] == zGreater) {
            bottomFace[1] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xGreater && this.bottomFaceTemplate[3][2] == zGreater) {
            bottomFace[1] = this.bottomFaceTemplate[3];
        }


        if (this.bottomFaceTemplate[0][0] == xLeast && this.bottomFaceTemplate[0][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xLeast && this.bottomFaceTemplate[1][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xLeast && this.bottomFaceTemplate[2][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xLeast && this.bottomFaceTemplate[3][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xLess && this.bottomFaceTemplate[0][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xLess && this.bottomFaceTemplate[1][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xLess && this.bottomFaceTemplate[2][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xLess && this.bottomFaceTemplate[3][2] == zGreatest) {
            bottomFace[2] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xLess && this.bottomFaceTemplate[0][2] == zGreater) {
            bottomFace[2] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xLess && this.bottomFaceTemplate[1][2] == zGreater) {
            bottomFace[2] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xLess && this.bottomFaceTemplate[2][2] == zGreater) {
            bottomFace[2] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xLess && this.bottomFaceTemplate[3][2] == zGreater) {
            bottomFace[2] = this.bottomFaceTemplate[3];
        }


        if (this.bottomFaceTemplate[0][0] == xGreatest && this.bottomFaceTemplate[0][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xGreatest && this.bottomFaceTemplate[1][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xGreatest && this.bottomFaceTemplate[2][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xGreatest && this.bottomFaceTemplate[3][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xGreater && this.bottomFaceTemplate[0][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xGreater && this.bottomFaceTemplate[1][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xGreater && this.bottomFaceTemplate[2][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xGreater && this.bottomFaceTemplate[3][2] == zLeast) {
            bottomFace[3] = this.bottomFaceTemplate[3];
        } else if (this.bottomFaceTemplate[0][0] == xGreater && this.bottomFaceTemplate[0][2] == zLess) {
            bottomFace[3] = this.bottomFaceTemplate[0];
        } else if (this.bottomFaceTemplate[1][0] == xGreater && this.bottomFaceTemplate[1][2] == zLess) {
            bottomFace[3] = this.bottomFaceTemplate[1];
        } else if (this.bottomFaceTemplate[2][0] == xGreater && this.bottomFaceTemplate[2][2] == zLess) {
            bottomFace[3] = this.bottomFaceTemplate[2];
        } else if (this.bottomFaceTemplate[3][0] == xGreater && this.bottomFaceTemplate[3][2] == zLess) {
            bottomFace[3] = this.bottomFaceTemplate[3];
        }


        return bottomFace;
    }


    private float[][] sortNorthFace() {
        float[][] northFace = new float[4][3];
        float[] yValues = new float[4];
        float[] zValues = new float[4];
        int yValueIndex = 0;
        int zValueIndex = 0;

        for (int i = 0; i < this.northFaceTemplate.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) {
                    yValues[yValueIndex] = this.northFaceTemplate[i][j];
                    yValueIndex++;
                } else if (j == 2) {
                    zValues[zValueIndex] = this.northFaceTemplate[i][j];
                    zValueIndex++;
                }
            }
        }

        Arrays.sort(yValues);
        Arrays.sort(zValues);

        float yLeast = yValues[0];
        float yLess = yValues[1];
        float yGreater = yValues[2];
        float yGreatest = yValues[3];
        float zLeast = zValues[0];
        float zLess = zValues[1];
        float zGreater = zValues[2];
        float zGreatest = zValues[3];


        if (this.northFaceTemplate[0][1] == yLeast && this.northFaceTemplate[0][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yLeast && this.northFaceTemplate[1][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yLeast && this.northFaceTemplate[2][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yLeast && this.northFaceTemplate[3][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yLess && this.northFaceTemplate[0][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yLess && this.northFaceTemplate[1][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yLess && this.northFaceTemplate[2][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yLess && this.northFaceTemplate[3][2] == zLeast) {
            northFace[0] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yLess && this.northFaceTemplate[0][2] == zLess) {
            northFace[0] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yLess && this.northFaceTemplate[1][2] == zLess) {
            northFace[0] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yLess && this.northFaceTemplate[2][2] == zLess) {
            northFace[0] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yLess && this.northFaceTemplate[3][2] == zLess) {
            northFace[0] = this.northFaceTemplate[3];
        }


        if (this.northFaceTemplate[0][1] == yGreatest && this.northFaceTemplate[0][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yGreatest && this.northFaceTemplate[1][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yGreatest && this.northFaceTemplate[2][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yGreatest && this.northFaceTemplate[3][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yGreater && this.northFaceTemplate[0][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yGreater && this.northFaceTemplate[1][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yGreater && this.northFaceTemplate[2][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yGreater && this.northFaceTemplate[3][2] == zGreatest) {
            northFace[1] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yGreater && this.northFaceTemplate[0][2] == zGreater) {
            northFace[1] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yGreater && this.northFaceTemplate[1][2] == zGreater) {
            northFace[1] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yGreater && this.northFaceTemplate[2][2] == zGreater) {
            northFace[1] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yGreater && this.northFaceTemplate[3][2] == zGreater) {
            northFace[1] = this.northFaceTemplate[3];
        }


        if (this.northFaceTemplate[0][1] == yGreatest && this.northFaceTemplate[0][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yGreatest && this.northFaceTemplate[1][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yGreatest && this.northFaceTemplate[2][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yGreatest && this.northFaceTemplate[3][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yGreater && this.northFaceTemplate[0][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yGreater && this.northFaceTemplate[1][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yGreater && this.northFaceTemplate[2][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yGreater && this.northFaceTemplate[3][2] == zLeast) {
            northFace[2] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yGreater && this.northFaceTemplate[0][2] == zLess) {
            northFace[2] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yGreater && this.northFaceTemplate[1][2] == zLess) {
            northFace[2] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yGreater && this.northFaceTemplate[2][2] == zLess) {
            northFace[2] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yGreater && this.northFaceTemplate[3][2] == zLess) {
            northFace[2] = this.northFaceTemplate[3];
        }


        if (this.northFaceTemplate[0][1] == yLeast && this.northFaceTemplate[0][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yLeast && this.northFaceTemplate[1][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yLeast && this.northFaceTemplate[2][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yLeast && this.northFaceTemplate[3][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yLess && this.northFaceTemplate[0][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yLess && this.northFaceTemplate[1][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yLess && this.northFaceTemplate[2][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yLess && this.northFaceTemplate[3][2] == zGreatest) {
            northFace[3] = this.northFaceTemplate[3];
        } else if (this.northFaceTemplate[0][1] == yLess && this.northFaceTemplate[0][2] == zGreater) {
            northFace[3] = this.northFaceTemplate[0];
        } else if (this.northFaceTemplate[1][1] == yLess && this.northFaceTemplate[1][2] == zGreater) {
            northFace[3] = this.northFaceTemplate[1];
        } else if (this.northFaceTemplate[2][1] == yLess && this.northFaceTemplate[2][2] == zGreater) {
            northFace[3] = this.northFaceTemplate[2];
        } else if (this.northFaceTemplate[3][1] == yLess && this.northFaceTemplate[3][2] == zGreater) {
            northFace[3] = this.northFaceTemplate[3];
        }


        return northFace;
    }


    private float[][] sortSouthFace() {
        float[][] southFace = new float[4][3];
        float[] yValues = new float[4];
        float[] zValues = new float[4];
        int yValueIndex = 0;
        int zValueIndex = 0;

        for (int i = 0; i < this.southFaceTemplate.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) {
                    yValues[yValueIndex] = this.southFaceTemplate[i][j];
                    yValueIndex++;
                } else if (j == 2) {
                    zValues[zValueIndex] = this.southFaceTemplate[i][j];
                    zValueIndex++;
                }
            }
        }

        Arrays.sort(yValues);
        Arrays.sort(zValues);

        float yLeast = yValues[0];
        float yLess = yValues[1];
        float yGreater = yValues[2];
        float yGreatest = yValues[3];
        float zLeast = zValues[0];
        float zLess = zValues[1];
        float zGreater = zValues[2];
        float zGreatest = zValues[3];


        if (this.southFaceTemplate[0][1] == yLeast && this.southFaceTemplate[0][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yLeast && this.southFaceTemplate[1][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yLeast && this.southFaceTemplate[2][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yLeast && this.southFaceTemplate[3][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yLess && this.southFaceTemplate[0][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yLess && this.southFaceTemplate[1][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yLess && this.southFaceTemplate[2][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yLess && this.southFaceTemplate[3][2] == zGreatest) {
            southFace[0] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yLess && this.southFaceTemplate[0][2] == zGreater) {
            southFace[0] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yLess && this.southFaceTemplate[1][2] == zGreater) {
            southFace[0] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yLess && this.southFaceTemplate[2][2] == zGreater) {
            southFace[0] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yLess && this.southFaceTemplate[3][2] == zGreater) {
            southFace[0] = this.southFaceTemplate[3];
        }


        if (this.southFaceTemplate[0][1] == yGreatest && this.southFaceTemplate[0][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yGreatest && this.southFaceTemplate[1][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yGreatest && this.southFaceTemplate[2][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yGreatest && this.southFaceTemplate[3][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yGreater && this.southFaceTemplate[0][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yGreater && this.southFaceTemplate[1][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yGreater && this.southFaceTemplate[2][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yGreater && this.southFaceTemplate[3][2] == zLeast) {
            southFace[1] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yGreater && this.southFaceTemplate[0][2] == zLess) {
            southFace[1] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yGreater && this.southFaceTemplate[1][2] == zLess) {
            southFace[1] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yGreater && this.southFaceTemplate[2][2] == zLess) {
            southFace[1] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yGreater && this.southFaceTemplate[3][2] == zLess) {
            southFace[1] = this.southFaceTemplate[3];
        }


        if (this.southFaceTemplate[0][1] == yGreatest && this.southFaceTemplate[0][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yGreatest && this.southFaceTemplate[1][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yGreatest && this.southFaceTemplate[2][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yGreatest && this.southFaceTemplate[3][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yGreater && this.southFaceTemplate[0][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yGreater && this.southFaceTemplate[1][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yGreater && this.southFaceTemplate[2][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yGreater && this.southFaceTemplate[3][2] == zGreatest) {
            southFace[2] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yGreater && this.southFaceTemplate[0][2] == zGreater) {
            southFace[2] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yGreater && this.southFaceTemplate[1][2] == zGreater) {
            southFace[2] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yGreater && this.southFaceTemplate[2][2] == zGreater) {
            southFace[2] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yGreater && this.southFaceTemplate[3][2] == zGreater) {
            southFace[2] = this.southFaceTemplate[3];
        }


        if (this.southFaceTemplate[0][1] == yLeast && this.southFaceTemplate[0][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yLeast && this.southFaceTemplate[1][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yLeast && this.southFaceTemplate[2][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yLeast && this.southFaceTemplate[3][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yLess && this.southFaceTemplate[0][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yLess && this.southFaceTemplate[1][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yLess && this.southFaceTemplate[2][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yLess && this.southFaceTemplate[3][2] == zLeast) {
            southFace[3] = this.southFaceTemplate[3];
        } else if (this.southFaceTemplate[0][1] == yLess && this.southFaceTemplate[0][2] == zLess) {
            southFace[3] = this.southFaceTemplate[0];
        } else if (this.southFaceTemplate[1][1] == yLess && this.southFaceTemplate[1][2] == zLess) {
            southFace[3] = this.southFaceTemplate[1];
        } else if (this.southFaceTemplate[2][1] == yLess && this.southFaceTemplate[2][2] == zLess) {
            southFace[3] = this.southFaceTemplate[2];
        } else if (this.southFaceTemplate[3][1] == yLess && this.southFaceTemplate[3][2] == zLess) {
            southFace[3] = this.southFaceTemplate[3];
        }


        return southFace;
    }


    private float[][] sortEastFace() {
        float[][] eastFace = new float[4][3];
        float[] yValues = new float[4];
        float[] xValues = new float[4];
        int yValueIndex = 0;
        int xValueIndex = 0;

        for (int i = 0; i < this.eastFaceTemplate.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) {
                    yValues[yValueIndex] = this.eastFaceTemplate[i][j];
                    yValueIndex++;
                } else if (j == 0) {
                    xValues[xValueIndex] = this.eastFaceTemplate[i][j];
                    xValueIndex++;
                }
            }
        }

        Arrays.sort(yValues);
        Arrays.sort(xValues);

        float yLeast = yValues[0];
        float yLess = yValues[1];
        float yGreater = yValues[2];
        float yGreatest = yValues[3];
        float xLeast = xValues[0];
        float xLess = xValues[1];
        float xGreater = xValues[2];
        float xGreatest = xValues[3];


        if (this.eastFaceTemplate[0][1] == yLeast && this.eastFaceTemplate[0][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLeast && this.eastFaceTemplate[1][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLeast && this.eastFaceTemplate[2][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLeast && this.eastFaceTemplate[3][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLess && this.eastFaceTemplate[0][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLess && this.eastFaceTemplate[1][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLess && this.eastFaceTemplate[2][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLess && this.eastFaceTemplate[3][0] == xGreatest) {
            eastFace[0] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLess && this.eastFaceTemplate[0][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLess && this.eastFaceTemplate[1][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLess && this.eastFaceTemplate[2][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLess && this.eastFaceTemplate[3][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLeast && this.eastFaceTemplate[0][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLeast && this.eastFaceTemplate[1][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLeast && this.eastFaceTemplate[2][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLeast && this.eastFaceTemplate[3][0] == xGreater) {
            eastFace[0] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLeast && this.eastFaceTemplate[0][0] == xLess) {
            eastFace[0] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLeast && this.eastFaceTemplate[1][0] == xLess) {
            eastFace[0] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLeast && this.eastFaceTemplate[2][0] == xLess) {
            eastFace[0] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLeast && this.eastFaceTemplate[3][0] == xLess) {
            eastFace[0] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLeast && this.eastFaceTemplate[0][0] == xLeast) {
            eastFace[0] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLeast && this.eastFaceTemplate[1][0] == xLeast) {
            eastFace[0] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLeast && this.eastFaceTemplate[2][0] == xLeast) {
            eastFace[0] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLeast && this.eastFaceTemplate[3][0] == xLeast) {
            eastFace[0] = this.eastFaceTemplate[3];
        }


        if (this.eastFaceTemplate[0][1] == yGreatest && this.eastFaceTemplate[0][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreatest && this.eastFaceTemplate[1][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreatest && this.eastFaceTemplate[2][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreatest && this.eastFaceTemplate[3][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yGreater && this.eastFaceTemplate[0][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreater && this.eastFaceTemplate[1][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreater && this.eastFaceTemplate[2][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreater && this.eastFaceTemplate[3][0] == xLeast) {
            eastFace[1] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yGreater && this.eastFaceTemplate[0][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreater && this.eastFaceTemplate[1][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreater && this.eastFaceTemplate[2][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreater && this.eastFaceTemplate[3][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yGreatest && this.eastFaceTemplate[0][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreatest && this.eastFaceTemplate[1][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreatest && this.eastFaceTemplate[2][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreatest && this.eastFaceTemplate[3][0] == xLess) {
            eastFace[1] = this.eastFaceTemplate[3];
        }


        if (this.eastFaceTemplate[0][1] == yGreatest && this.eastFaceTemplate[0][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreatest && this.eastFaceTemplate[1][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreatest && this.eastFaceTemplate[2][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreatest && this.eastFaceTemplate[3][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yGreater && this.eastFaceTemplate[0][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreater && this.eastFaceTemplate[1][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreater && this.eastFaceTemplate[2][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreater && this.eastFaceTemplate[3][0] == xGreatest) {
            eastFace[2] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yGreater && this.eastFaceTemplate[0][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreater && this.eastFaceTemplate[1][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreater && this.eastFaceTemplate[2][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreater && this.eastFaceTemplate[3][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yGreatest && this.eastFaceTemplate[0][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yGreatest && this.eastFaceTemplate[1][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yGreatest && this.eastFaceTemplate[2][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yGreatest && this.eastFaceTemplate[3][0] == xGreater) {
            eastFace[2] = this.eastFaceTemplate[3];
        }


        if (this.eastFaceTemplate[0][1] == yLeast && this.eastFaceTemplate[0][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLeast && this.eastFaceTemplate[1][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLeast && this.eastFaceTemplate[2][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLeast && this.eastFaceTemplate[3][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLess && this.eastFaceTemplate[0][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLess && this.eastFaceTemplate[1][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLess && this.eastFaceTemplate[2][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLess && this.eastFaceTemplate[3][0] == xLeast) {
            eastFace[3] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLess && this.eastFaceTemplate[0][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLess && this.eastFaceTemplate[1][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLess && this.eastFaceTemplate[2][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLess && this.eastFaceTemplate[3][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[3];
        } else if (this.eastFaceTemplate[0][1] == yLeast && this.eastFaceTemplate[0][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[0];
        } else if (this.eastFaceTemplate[1][1] == yLeast && this.eastFaceTemplate[1][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[1];
        } else if (this.eastFaceTemplate[2][1] == yLeast && this.eastFaceTemplate[2][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[2];
        } else if (this.eastFaceTemplate[3][1] == yLeast && this.eastFaceTemplate[3][0] == xLess) {
            eastFace[3] = this.eastFaceTemplate[3];
        }

        return eastFace;
    }

    private float[][] sortWestFace() {
        float[][] westFace = new float[4][3];
        float[] yValues = new float[4];
        float[] xValues = new float[4];
        int yValueIndex = 0;
        int xValueIndex = 0;

        for (int i = 0; i < this.westFaceTemplate.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) {
                    yValues[yValueIndex] = this.westFaceTemplate[i][j];
                    yValueIndex++;
                } else if (j == 0) {
                    xValues[xValueIndex] = this.westFaceTemplate[i][j];
                    xValueIndex++;
                }
            }
        }

        Arrays.sort(yValues);
        Arrays.sort(xValues);

        float yLeast = yValues[0];
        float yLess = yValues[1];
        float yGreater = yValues[2];
        float yGreatest = yValues[3];
        float xLeast = xValues[0];
        float xLess = xValues[1];
        float xGreater = xValues[2];
        float xGreatest = xValues[3];


        if (this.westFaceTemplate[0][1] == yLeast && this.westFaceTemplate[0][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLeast && this.westFaceTemplate[1][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLeast && this.westFaceTemplate[2][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLeast && this.westFaceTemplate[3][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yLess && this.westFaceTemplate[0][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLess && this.westFaceTemplate[1][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLess && this.westFaceTemplate[2][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLess && this.westFaceTemplate[3][0] == xLeast) {
            westFace[0] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yLess && this.westFaceTemplate[0][0] == xLess) {
            westFace[0] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLess && this.westFaceTemplate[1][0] == xLess) {
            westFace[0] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLess && this.westFaceTemplate[2][0] == xLess) {
            westFace[0] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLess && this.westFaceTemplate[3][0] == xLess) {
            westFace[0] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yLeast && this.westFaceTemplate[0][0] == xLess) {
            westFace[0] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLeast && this.westFaceTemplate[1][0] == xLess) {
            westFace[0] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLeast && this.westFaceTemplate[2][0] == xLess) {
            westFace[0] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLeast && this.westFaceTemplate[3][0] == xLess) {
            westFace[0] = this.westFaceTemplate[3];
        }


        if (this.westFaceTemplate[0][1] == yGreatest && this.westFaceTemplate[0][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreatest && this.westFaceTemplate[1][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreatest && this.westFaceTemplate[2][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreatest && this.westFaceTemplate[3][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yGreater && this.westFaceTemplate[0][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreater && this.westFaceTemplate[1][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreater && this.westFaceTemplate[2][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreater && this.westFaceTemplate[3][0] == xGreatest) {
            westFace[1] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yGreater && this.westFaceTemplate[0][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreater && this.westFaceTemplate[1][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreater && this.westFaceTemplate[2][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreater && this.westFaceTemplate[3][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yGreatest && this.westFaceTemplate[0][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreatest && this.westFaceTemplate[1][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreatest && this.westFaceTemplate[2][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreatest && this.westFaceTemplate[3][0] == xGreater) {
            westFace[1] = this.westFaceTemplate[3];
        }


        if (this.westFaceTemplate[0][1] == yGreatest && this.westFaceTemplate[0][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreatest && this.westFaceTemplate[1][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreatest && this.westFaceTemplate[2][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreatest && this.westFaceTemplate[3][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yGreater && this.westFaceTemplate[0][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreater && this.westFaceTemplate[1][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreater && this.westFaceTemplate[2][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreater && this.westFaceTemplate[3][0] == xLeast) {
            westFace[2] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yGreater && this.westFaceTemplate[0][0] == xLess) {
            westFace[2] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreater && this.westFaceTemplate[1][0] == xLess) {
            westFace[2] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreater && this.westFaceTemplate[2][0] == xLess) {
            westFace[2] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreater && this.westFaceTemplate[3][0] == xLess) {
            westFace[2] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yGreatest && this.westFaceTemplate[0][0] == xLess) {
            westFace[2] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yGreatest && this.westFaceTemplate[1][0] == xLess) {
            westFace[2] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yGreatest && this.westFaceTemplate[2][0] == xLess) {
            westFace[2] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yGreatest && this.westFaceTemplate[3][0] == xLess) {
            westFace[2] = this.westFaceTemplate[3];
        }


        if (this.westFaceTemplate[0][1] == yLeast && this.westFaceTemplate[0][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLeast && this.westFaceTemplate[1][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLeast && this.westFaceTemplate[2][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLeast && this.westFaceTemplate[3][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yLess && this.westFaceTemplate[0][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLess && this.westFaceTemplate[1][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLess && this.westFaceTemplate[2][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLess && this.westFaceTemplate[3][0] == xGreatest) {
            westFace[3] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yLess && this.westFaceTemplate[0][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLess && this.westFaceTemplate[1][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLess && this.westFaceTemplate[2][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLess && this.westFaceTemplate[3][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[3];
        } else if (this.westFaceTemplate[0][1] == yLeast && this.westFaceTemplate[0][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[0];
        } else if (this.westFaceTemplate[1][1] == yLeast && this.westFaceTemplate[1][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[1];
        } else if (this.westFaceTemplate[2][1] == yLeast && this.westFaceTemplate[2][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[2];
        } else if (this.westFaceTemplate[3][1] == yLeast && this.westFaceTemplate[3][0] == xGreater) {
            westFace[3] = this.westFaceTemplate[3];
        }


        return westFace;
    }

    public void addModelFace(ModelFace modelFace){
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] == null){
                this.modelFaces[i] = modelFace;
                break;
            }
        }
    }


    public ModelLoader getScaledModel(float scaleFactor){
        ModelLoader scaledModel = new ModelLoader();
        ModelFace scaledFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                scaledFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < scaledFace.vertices.length; j++){
                    scaledFace.vertices[j].x = this.modelFaces[i].vertices[j].x * scaleFactor;
                    scaledFace.vertices[j].y = this.modelFaces[i].vertices[j].y * scaleFactor;
                    scaledFace.vertices[j].z = this.modelFaces[i].vertices[j].z * scaleFactor;
                }
                scaledFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                scaledModel.addModelFace(scaledFace);
            }
        }

        int modelFacesSize = 0;
        for (int i = 0; i < scaledModel.modelFaces.length; i++) {
            if (scaledModel.modelFaces[i] != null) {
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for (int i = 0; i < newModelFaces.length; i++) {
            newModelFaces[i] = scaledModel.modelFaces[i];
        }

        scaledModel.modelFaces = newModelFaces;
        return scaledModel;
    }

    public ModelLoader translateModel(float x, float y, float z){
        ModelLoader translatedModel = new ModelLoader();
        ModelFace translatedFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                translatedFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < translatedFace.vertices.length; j++){
                    translatedFace.vertices[j].x = this.modelFaces[i].vertices[j].x + x;
                    translatedFace.vertices[j].y = this.modelFaces[i].vertices[j].y + y;
                    translatedFace.vertices[j].z = this.modelFaces[i].vertices[j].z + z;
                }
                translatedFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                translatedModel.addModelFace(translatedFace);
            }
        }

        int modelFacesSize = 0;
        for(int i = 0; i < translatedModel.modelFaces.length; i++){
            if(translatedModel.modelFaces[i] != null){
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for(int i = 0; i < newModelFaces.length; i++){
            newModelFaces[i] = translatedModel.modelFaces[i];
        }
        translatedModel.modelFaces = newModelFaces;
        return translatedModel;
    }

    public ModelLoader rotateModel(float deg, float x, float y, float z){
        float rad = (float) Math.toRadians(deg);
        ModelLoader rotatedModel = new ModelLoader();
        ModelFace rotatedFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
                rotatedFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < rotatedFace.vertices.length; j++){
                    rotatedFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    rotatedFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    rotatedFace.vertices[j].z = this.modelFaces[i].vertices[j].z;

                    if(x == 1){
                        rotatedFace.vertices[j].rotateX(rad);
                    }
                    if(y == 1) {
                        rotatedFace.vertices[j].rotateY(rad);
                    }
                    if(z == 1){
                        rotatedFace.vertices[j].rotateZ(rad);
                    }
                }
                rotatedFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                if(x == 1){
                    rotatedFace.normal.rotateX(rad);
                }
                if(y == 1) {
                    rotatedFace.normal.rotateY(rad);
                }
                if(z == 1){
                    rotatedFace.normal.rotateZ(rad);
                }
                rotatedModel.addModelFace(rotatedFace);
            }
        }

        int modelFacesSize = 0;
        for(int i = 0; i < rotatedModel.modelFaces.length; i++){
            if(rotatedModel.modelFaces[i] != null){
                modelFacesSize++;
            }
        }

        ModelFace[] newModelFaces = new ModelFace[modelFacesSize];
        for(int i = 0; i < newModelFaces.length; i++){
            newModelFaces[i] = rotatedModel.modelFaces[i];
        }
        rotatedModel.modelFaces = newModelFaces;
        return rotatedModel;
    }

    public ModelLoader alterStandardBlockModel(int xFactor, int yFactor, int zFactor){
        final float changeConstant = 0.03125F;
        ModelLoader alteredModel = new ModelLoader();
        ModelFace alteredFace;
        for(int i = 0; i < this.modelFaces.length; i++){
            if(this.modelFaces[i] != null){
               alteredFace = new ModelFace(this.modelFaces[i].faceType);
                for(int j = 0; j < alteredFace.vertices.length; j++){
                    alteredFace.vertices[j].x = this.modelFaces[i].vertices[j].x;
                    alteredFace.vertices[j].y = this.modelFaces[i].vertices[j].y;
                    alteredFace.vertices[j].z = this.modelFaces[i].vertices[j].z;
                }
                alteredFace.setNormal(this.modelFaces[i].normal.x, this.modelFaces[i].normal.y, this.modelFaces[i].normal.z);
                alteredModel.addModelFace(alteredFace);
            }
        }

        for(int i = 0; i < alteredModel.modelFaces.length; i++){
            if(alteredModel.modelFaces[i] != null){
                alteredFace = alteredModel.modelFaces[i];
                switch (alteredFace.faceType){
                    case RenderBlocks.TOP_FACE -> {
                        alteredFace.vertices[0].x = alteredFace.vertices[0].x - (changeConstant * xFactor);
                        alteredFace.vertices[0].y = alteredFace.vertices[0].y - (changeConstant * yFactor);
                        alteredFace.vertices[0].z = alteredFace.vertices[0].z + (changeConstant * zFactor);

                        alteredFace.vertices[1].x = alteredFace.vertices[1].x + (changeConstant * xFactor);
                        alteredFace.vertices[1].y = alteredFace.vertices[1].y - (changeConstant * yFactor);
                        alteredFace.vertices[1].z = alteredFace.vertices[1].z - (changeConstant * zFactor);

                        alteredFace.vertices[2].x = alteredFace.vertices[2].x - (changeConstant * xFactor);
                        alteredFace.vertices[2].y = alteredFace.vertices[2].y - (changeConstant * yFactor);
                        alteredFace.vertices[2].z = alteredFace.vertices[2].z - (changeConstant * zFactor);

                        alteredFace.vertices[3].x = alteredFace.vertices[3].x + (changeConstant * xFactor);
                        alteredFace.vertices[3].y = alteredFace.vertices[3].y - (changeConstant * yFactor);
                        alteredFace.vertices[3].z = alteredFace.vertices[3].z + (changeConstant * zFactor);
                    }
                    case RenderBlocks.BOTTOM_FACE -> {
                        alteredFace.vertices[0].x = alteredFace.vertices[0].x + (changeConstant * xFactor);
                        alteredFace.vertices[0].y = alteredFace.vertices[0].y + (changeConstant * yFactor);
                        alteredFace.vertices[0].z = alteredFace.vertices[0].z + (changeConstant * zFactor);

                        alteredFace.vertices[1].x = alteredFace.vertices[1].x - (changeConstant * xFactor);
                        alteredFace.vertices[1].y = alteredFace.vertices[1].y + (changeConstant * yFactor);
                        alteredFace.vertices[1].z = alteredFace.vertices[1].z - (changeConstant * zFactor);

                        alteredFace.vertices[2].x = alteredFace.vertices[2].x + (changeConstant * xFactor);
                        alteredFace.vertices[2].y = alteredFace.vertices[2].y + (changeConstant * yFactor);
                        alteredFace.vertices[2].z = alteredFace.vertices[2].z - (changeConstant * zFactor);

                        alteredFace.vertices[3].x = alteredFace.vertices[3].x - (changeConstant * xFactor);
                        alteredFace.vertices[3].y = alteredFace.vertices[3].y + (changeConstant * yFactor);
                        alteredFace.vertices[3].z = alteredFace.vertices[3].z + (changeConstant * zFactor);
                    }
                    case RenderBlocks.NORTH_FACE -> {
                        alteredFace.vertices[0].x = alteredFace.vertices[0].x + (changeConstant * xFactor);
                        alteredFace.vertices[0].y = alteredFace.vertices[0].y + (changeConstant * yFactor);
                        alteredFace.vertices[0].z = alteredFace.vertices[0].z + (changeConstant * zFactor);

                        alteredFace.vertices[1].x = alteredFace.vertices[1].x + (changeConstant * xFactor);
                        alteredFace.vertices[1].y = alteredFace.vertices[1].y - (changeConstant * yFactor);
                        alteredFace.vertices[1].z = alteredFace.vertices[1].z - (changeConstant * zFactor);

                        alteredFace.vertices[2].x = alteredFace.vertices[2].x + (changeConstant * xFactor);
                        alteredFace.vertices[2].y = alteredFace.vertices[2].y - (changeConstant * yFactor);
                        alteredFace.vertices[2].z = alteredFace.vertices[2].z + (changeConstant * zFactor);

                        alteredFace.vertices[3].x = alteredFace.vertices[3].x + (changeConstant * xFactor);
                        alteredFace.vertices[3].y = alteredFace.vertices[3].y + (changeConstant * yFactor);
                        alteredFace.vertices[3].z = alteredFace.vertices[3].z - (changeConstant * zFactor);
                    }
                    case RenderBlocks.SOUTH_FACE -> {
                        alteredFace.vertices[0].x = alteredFace.vertices[0].x - (changeConstant * xFactor);
                        alteredFace.vertices[0].y = alteredFace.vertices[0].y + (changeConstant * yFactor);
                        alteredFace.vertices[0].z = alteredFace.vertices[0].z - (changeConstant * zFactor);

                        alteredFace.vertices[1].x = alteredFace.vertices[1].x - (changeConstant * xFactor);
                        alteredFace.vertices[1].y = alteredFace.vertices[1].y - (changeConstant * yFactor);
                        alteredFace.vertices[1].z = alteredFace.vertices[1].z + (changeConstant * zFactor);

                        alteredFace.vertices[2].x = alteredFace.vertices[2].x - (changeConstant * xFactor);
                        alteredFace.vertices[2].y = alteredFace.vertices[2].y - (changeConstant * yFactor);
                        alteredFace.vertices[2].z = alteredFace.vertices[2].z - (changeConstant * zFactor);

                        alteredFace.vertices[3].x = alteredFace.vertices[3].x - (changeConstant * xFactor);
                        alteredFace.vertices[3].y = alteredFace.vertices[3].y + (changeConstant * yFactor);
                        alteredFace.vertices[3].z = alteredFace.vertices[3].z + (changeConstant * zFactor);
                    }
                    case RenderBlocks.EAST_FACE -> {
                        alteredFace.vertices[0].x = alteredFace.vertices[0].x - (changeConstant * xFactor);
                        alteredFace.vertices[0].y = alteredFace.vertices[0].y + (changeConstant * yFactor);
                        alteredFace.vertices[0].z = alteredFace.vertices[0].z + (changeConstant * zFactor);

                        alteredFace.vertices[1].x = alteredFace.vertices[1].x + (changeConstant * xFactor);
                        alteredFace.vertices[1].y = alteredFace.vertices[1].y - (changeConstant * yFactor);
                        alteredFace.vertices[1].z = alteredFace.vertices[1].z + (changeConstant * zFactor);

                        alteredFace.vertices[2].x = alteredFace.vertices[2].x - (changeConstant * xFactor);
                        alteredFace.vertices[2].y = alteredFace.vertices[2].y - (changeConstant * yFactor);
                        alteredFace.vertices[2].z = alteredFace.vertices[2].z + (changeConstant * zFactor);

                        alteredFace.vertices[3].x = alteredFace.vertices[3].x + (changeConstant * xFactor);
                        alteredFace.vertices[3].y = alteredFace.vertices[3].y + (changeConstant * yFactor);
                        alteredFace.vertices[3].z = alteredFace.vertices[3].z + (changeConstant * zFactor);
                    }
                    case RenderBlocks.WEST_FACE -> {
                        alteredFace.vertices[0].x = alteredFace.vertices[0].x + (changeConstant * xFactor);
                        alteredFace.vertices[0].y = alteredFace.vertices[0].y + (changeConstant * yFactor);
                        alteredFace.vertices[0].z = alteredFace.vertices[0].z - (changeConstant * zFactor);

                        alteredFace.vertices[1].x = alteredFace.vertices[1].x - (changeConstant * xFactor);
                        alteredFace.vertices[1].y = alteredFace.vertices[1].y - (changeConstant * yFactor);
                        alteredFace.vertices[1].z = alteredFace.vertices[1].z - (changeConstant * zFactor);

                        alteredFace.vertices[2].x = alteredFace.vertices[2].x + (changeConstant * xFactor);
                        alteredFace.vertices[2].y = alteredFace.vertices[2].y - (changeConstant * yFactor);
                        alteredFace.vertices[2].z = alteredFace.vertices[2].z - (changeConstant * zFactor);

                        alteredFace.vertices[3].x = alteredFace.vertices[3].x - (changeConstant * xFactor);
                        alteredFace.vertices[3].y = alteredFace.vertices[3].y + (changeConstant * yFactor);
                        alteredFace.vertices[3].z = alteredFace.vertices[3].z - (changeConstant * zFactor);
                    }
                }
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
        return alteredModel;
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
                }
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
                }
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
                }
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
                }
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
                }
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
                }
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

        for(int i = 0; i < this.modelFaces.length; i++){

            returnModel.modelFaces[i] = new ModelFace(this.modelFaces[i].faceType);

            returnModel.modelFaces[i].normal.x = this.modelFaces[i].normal.x;
            returnModel.modelFaces[i].normal.y = this.modelFaces[i].normal.y;
            returnModel.modelFaces[i].normal.z = this.modelFaces[i].normal.z;

            for(int j = 0; j < returnModel.modelFaces[i].vertices.length; j++){
                returnModel.modelFaces[i].vertices[j].x = this.modelFaces[i].vertices[j].x;
                returnModel.modelFaces[i].vertices[j].y = this.modelFaces[i].vertices[j].y;
                returnModel.modelFaces[i].vertices[j].z = this.modelFaces[i].vertices[j].z;
            }
        }

        return returnModel;
    }




}


