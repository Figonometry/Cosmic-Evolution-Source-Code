package spacegame.render;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;


public final class Shader {
    public String vertexShaderSrc;
    public String fragmentShaderSrc;
    public String computeShaderSrc;
    public int computeID;
    public int vertexID;
    public int fragmentID;
    public int shaderProgramID;
    public String filepath;
    public static final Shader worldShaderTextureArray = new Shader("src/spacegame/assets/shader/worldTextureArray.glsl");
    public static final Shader worldShader2DTexture = new Shader("src/spacegame/assets/shader/world2DTexture.glsl");
    public static final Shader worldShader2DTextureWithAtlas = new Shader("src/spacegame/assets/shader/world2DTextureWithAtlas.glsl");
    public static final Shader worldShaderCubeMapTexture = new Shader("src/spacegame/assets/shader/worldCubeMapTexture.glsl");
    public static final Shader universeShaderCubeMapTexture = new Shader("src/spacegame/assets/shader/universeCubeMapTexture.glsl");
    public static final Shader universeShader2DTexture = new Shader("src/spacegame/assets/shader/universe2DTexture.glsl");
    public static final Shader screen2DTexture = new Shader("src/spacegame/assets/shader/screen2DTexture.glsl");
    public static final Shader screen2DTextureAtlas = new Shader("src/spacegame/assets/shader/screen2DTextureAtlas.glsl");
    public static final Shader screenTextureArray = new Shader("src/spacegame/assets/shader/screenTextureArray.glsl");


    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                this.vertexShaderSrc = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                this.fragmentShaderSrc = splitString[1];
            } else {
                throw new IOException("Unexepcted token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                this.vertexShaderSrc = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                this.fragmentShaderSrc = splitString[2];
            } else {
                throw new IOException("Unexepcted token '" + secondPattern + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not open the shader file: " + filepath);

        }

        this.compileShaders();
    }

    public Shader(String filepath, boolean isCompute) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String pattern = source.substring(index, eol).trim();


            if (pattern.equals("compute")) {
                this.computeShaderSrc = splitString[1];
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not open the shader file: " + filepath);

        }

        this.compileComputeShaders();
    }


    public void compileShaders() {
        //load and compile shader
        this.vertexID = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);

        //Pass the shader source to the GPU
        GL46.glShaderSource(this.vertexID, this.vertexShaderSrc);
        GL46.glCompileShader(this.vertexID);

        //error check
        int success = GL46.glGetShaderi(this.vertexID, GL46.GL_COMPILE_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetShaderi(this.vertexID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'GLSL Shader.glsl' \n\tVertex shader compilation failed.");
            System.out.println(GL46.glGetShaderInfoLog(this.vertexID, len));
            throw new RuntimeException();
        }

        //load and compile shader
        this.fragmentID = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);

        //Pass the shader source to the GPU
        GL46.glShaderSource(this.fragmentID, this.fragmentShaderSrc);
        GL46.glCompileShader(this.fragmentID);

        //error check
        success = GL46.glGetShaderi(this.fragmentID, GL46.GL_COMPILE_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetShaderi(this.fragmentID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'GLSL Shader.glsl' \n\tFragment shader compilation failed.");
            System.out.println(GL46.glGetShaderInfoLog(this.fragmentID, len));
            throw new RuntimeException();
        }

        //Link shader and check for errors
        this.shaderProgramID = GL46.glCreateProgram();
        GL46.glAttachShader(this.shaderProgramID, this.vertexID);
        GL46.glAttachShader(this.shaderProgramID, this.fragmentID);
        GL46.glLinkProgram(this.shaderProgramID);


        //check for link errors
        success = GL46.glGetProgrami(this.shaderProgramID, GL46.GL_LINK_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetProgrami(this.shaderProgramID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'GLSL Shader.glsl' \n\tLinking of shaders failed.");
            System.out.println(GL46.glGetProgramInfoLog(this.shaderProgramID, len));
            throw new RuntimeException();
        }
    }

    public void compileComputeShaders() {
        //load and compile shader
        this.computeID = GL46.glCreateShader(GL46.GL_COMPUTE_SHADER);

        //Pass the shader source to the GPU
        GL46.glShaderSource(this.computeID, this.computeShaderSrc);
        GL46.glCompileShader(this.computeID);

        //error check
        int success = GL46.glGetShaderi(this.computeID, GL46.GL_COMPILE_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetShaderi(this.computeID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'GLSL Shader.glsl' \n\tVertex shader compilation failed.");
            System.out.println(GL46.glGetShaderInfoLog(this.computeID, len));
            throw new RuntimeException();
        }

        //Link shader and check for errors
        this.shaderProgramID = GL46.glCreateProgram();
        GL46.glAttachShader(this.shaderProgramID, this.computeID);
        GL46.glLinkProgram(this.shaderProgramID);


        //check for link errors
        success = GL46.glGetProgrami(this.shaderProgramID, GL46.GL_LINK_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetProgrami(this.shaderProgramID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'GLSL Shader.glsl' \n\tLinking of shaders failed.");
            System.out.println(GL46.glGetProgramInfoLog(this.shaderProgramID, len));
            throw new RuntimeException();
        }
    }


    public void use() {
        GL46.glUseProgram(this.shaderProgramID);
    }


    public void detach() {
        GL46.glUseProgram(0);
    }


    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        GL46.glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat4d(String varName, Matrix4d mat4) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        DoubleBuffer matBuffer = BufferUtils.createDoubleBuffer(16);
        mat4.get(matBuffer);
        GL46.glUniformMatrix4dv(varLocation, false, matBuffer);
    }


    public void uploadTexture(String varName, int slot) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform1i(varLocation, slot);
    }

    public void uploadVec3d(String varName, Vector3d vec) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform3d(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec3i(String varName, Vector3i vec) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform3i(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform1iv(varLocation, array);
    }

    public void uploadFloatArray(String varName, float[] array){
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform1fv(varLocation, array);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL46.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL46.glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform1f(varLocation, val);
    }

    public void uploadBoolean(String varName, boolean val) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        int val1;
        if (val) {
            val1 = 1;
        } else {
            val1 = 0;
        }
        GL46.glUniform1i(varLocation, val1);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform1i(varLocation, val);
    }

    public void uploadDouble(String varName, double val){
        int varLocation = GL46.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL46.glUniform1d(varLocation, val);
    }
}