package spacegame.world.weather;

import spacegame.core.CosmicEvolution;
import spacegame.nbt.NBTTagCompound;
import spacegame.util.MathUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Cloud {
    public double x;
    public double y;
    public double z;
    public float width; //x
    public float height; //y
    public float depth; //z
    public static int texture;



    public Cloud(double x, double y, double z, float width, float height, float depth){
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public Cloud(NBTTagCompound cloudTag){
        this.x = cloudTag.getDouble("x");
        this.y = cloudTag.getDouble("y");
        this.z = cloudTag.getDouble("z");
        this.width = cloudTag.getFloat("width");
        this.height = cloudTag.getFloat("height");
        this.depth = cloudTag.getFloat("depth");
    }

    public void saveCloudToFile(NBTTagCompound cloudTag){
        cloudTag.setDouble("x", this.x);
        cloudTag.setDouble("y", this.y);
        cloudTag.setDouble("z", this.z);
        cloudTag.setFloat("width", this.width);
        cloudTag.setFloat("height", this.height);
        cloudTag.setFloat("depth", this.depth);
    }

    public void addCloudToRenderData(FloatBuffer vertexBuffer, IntBuffer elementBuffer, CloudFormation parentFormation){
        //Shift the coordinates into model space relative by subtracting the parent position from this position


        float xMin = (float) ((this.x - parentFormation.x) - (this.width * 0.5f));
        float xMax = (float) ((this.x - parentFormation.x) + (this.width * 0.5f));
        float yMin = (float) ((this.y - parentFormation.y) - (this.height * 0.5f));
        float yMax = (float) ((this.y - parentFormation.y) + (this.height * 0.5f));
        float zMin = (float) ((this.z - parentFormation.z) - (this.depth * 0.5f));
        float zMax = (float) ((this.z - parentFormation.z) + (this.depth * 0.5f));

         //xMin = MathUtil.positiveMod(xMin, 32);
         //yMin = MathUtil.positiveMod(yMin, 32);
         //zMin = MathUtil.positiveMod(zMin, 32);
         //xMax = MathUtil.positiveMod(xMax, 32);
         //yMax = MathUtil.positiveMod(yMax, 32);
         //zMax = MathUtil.positiveMod(zMax, 32);

        //General shape is applied here, it's scaled via a uniform whenever scale is updated
        //top
        this.addVertex(vertexBuffer, xMax, yMax, zMin, 3, 0, 1, 0);
        this.addVertex(vertexBuffer, xMin, yMax, zMax, 1, 0, 1, 0);
        this.addVertex(vertexBuffer, xMax, yMax, zMax, 2, 0, 1, 0);
        this.addVertex(vertexBuffer, xMin, yMax, zMin, 0, 0, 1, 0);
        this.addElementsCW(elementBuffer, parentFormation);
        //Bottom
        this.addVertex(vertexBuffer, xMin, yMin, zMin, 3, 0, -1, 0);
        this.addVertex(vertexBuffer, xMax, yMin, zMax, 1, 0, -1, 0);
        this.addVertex(vertexBuffer, xMin, yMin, zMax, 2, 0, -1, 0);
        this.addVertex(vertexBuffer, xMax, yMin, zMin, 0, 0, -1, 0);
        this.addElementsCW(elementBuffer, parentFormation);
        //North
        this.addVertex(vertexBuffer, xMin, yMin, zMin, 3, -1, 0, 0);
        this.addVertex(vertexBuffer, xMin, yMax, zMax, 1, -1, 0, 0);
        this.addVertex(vertexBuffer, xMin, yMax, zMin, 2, -1, 0, 0);
        this.addVertex(vertexBuffer, xMin, yMin, zMax, 0, -1, 0, 0);
        this.addElementsCW(elementBuffer, parentFormation);
        //South
        this.addVertex(vertexBuffer, xMax, yMin, zMax, 3, 1, 0, 0);
        this.addVertex(vertexBuffer, xMax, yMax, zMin, 1, 1, 0, 0);
        this.addVertex(vertexBuffer, xMax, yMax, zMax, 2, 1, 0, 0);
        this.addVertex(vertexBuffer, xMax, yMin, zMin, 0, 1, 0, 0);
        this.addElementsCW(elementBuffer, parentFormation);
        //East
        this.addVertex(vertexBuffer, xMax, yMin, zMin, 3, 0, 0, -1);
        this.addVertex(vertexBuffer, xMin, yMax, zMin, 1, 0, 0, -1);
        this.addVertex(vertexBuffer, xMax, yMax, zMin, 2, 0, 0, -1);
        this.addVertex(vertexBuffer, xMin, yMin, zMin, 0, 0, 0, -1);
        this.addElementsCW(elementBuffer, parentFormation);
        //West
        this.addVertex(vertexBuffer, xMin, yMin, zMax, 3, 0, 0, 1);
        this.addVertex(vertexBuffer, xMax, yMax, zMax, 1, 0, 0, 1);
        this.addVertex(vertexBuffer, xMin, yMax, zMax, 2, 0, 0, 1);
        this.addVertex(vertexBuffer, xMax, yMin, zMax, 0, 0, 0, 1);
        this.addElementsCW(elementBuffer, parentFormation);
    }

    private void addVertex(FloatBuffer vertexBuffer, float x, float y, float z, int corner, float normalX, float normalY, float normalZ){


        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.put(z);
        switch (corner) {
            case 0 -> {
                vertexBuffer.put(1f);
                vertexBuffer.put(1f);
            }
            case 1 -> {
               vertexBuffer.put(1f);
               vertexBuffer.put(0f);
            }
            case 2 -> {
                vertexBuffer.put(0f);
                vertexBuffer.put(0f);
            }
            case 3 -> {
                vertexBuffer.put(0F);
                vertexBuffer.put(1F);
            }
        }
        vertexBuffer.put(normalX);
        vertexBuffer.put(normalY);
        vertexBuffer.put(normalZ);
    }

    private void addElementsCW(IntBuffer elementBuffer, CloudFormation cloudFormation){
        elementBuffer.put(cloudFormation.elementOffset + 2);
        elementBuffer.put(cloudFormation.elementOffset + 1);
        elementBuffer.put(cloudFormation.elementOffset + 0);
        elementBuffer.put(cloudFormation.elementOffset + 0);
        elementBuffer.put(cloudFormation.elementOffset + 1);
        elementBuffer.put(cloudFormation.elementOffset + 3);
        cloudFormation.elementOffset += 4;
    }
}
