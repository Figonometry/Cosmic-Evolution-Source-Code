package spacegame.render;

import org.joml.Vector3f;
import spacegame.core.MathUtils;

public final class ModelSegment {
    public float width;
    public float depth;
    public float height;
    public boolean isRoot;
    public ModelSegment parentSegment;
    public ModelSegment[] childrenSegments;
    public Vector3f rotationPoint;
    public Vector3f position;
    public Vector3f[] topFace = new Vector3f[4];
    public Vector3f[] bottomFace = new Vector3f[4];
    public Vector3f[] northFace = new Vector3f[4];
    public Vector3f[] southFace = new Vector3f[4];
    public Vector3f[] eastFace = new Vector3f[4];
    public Vector3f[] westFace = new Vector3f[4];

    public ModelSegment(float width, float height, float depth, boolean isRoot, ModelSegment parentSegment, Vector3f rotationPoint, Vector3f position){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.isRoot = isRoot;
        this.parentSegment = parentSegment;
        this.rotationPoint = rotationPoint;
        this.position = position;

        this.topFace[0] = new Vector3f(width/2, height/2, -depth/2);
        this.topFace[1] = new Vector3f(-width/2, height/2, depth/2);
        this.topFace[2] = new Vector3f(width/2, height/2, depth/2);
        this.topFace[3] = new Vector3f(-width/2, height/2, -depth/2);

        this.bottomFace[0] = new Vector3f(-width/2, -height/2, -depth/2);
        this.bottomFace[1] = new Vector3f(width/2, -height/2, depth/2);
        this.bottomFace[2] = new Vector3f(-width/2, -height/2, depth/2);
        this.bottomFace[3] = new Vector3f(width/2, -height/2, -depth/2);

        this.northFace[0] = new Vector3f(-width/2, -height/2, -depth/2);
        this.northFace[1] = new Vector3f(-width/2, height/2, depth/2);
        this.northFace[2] = new Vector3f(-width/2, height/2, -depth/2);
        this.northFace[3] = new Vector3f(-width/2, -height/2, depth/2);

        this.southFace[0] = new Vector3f(width/2, -height/2, depth/2);
        this.southFace[1] = new Vector3f(width/2, height/2, -depth/2);
        this.southFace[2] = new Vector3f(width/2, height/2, depth/2);
        this.southFace[3] = new Vector3f(width/2, -height/2, -depth/2);

        this.eastFace[0] = new Vector3f(width/2, -height/2, -depth/2);
        this.eastFace[1] = new Vector3f(-width/2, height/2, -depth/2);
        this.eastFace[2] = new Vector3f(width/2, height/2, -depth/2);
        this.eastFace[3] = new Vector3f(-width/2, -height/2, -depth/2);

        this.westFace[0] = new Vector3f(-width/2, -height/2, depth/2);
        this.westFace[1] = new Vector3f(width/2, height/2, depth/2);
        this.westFace[2] = new Vector3f(-width/2, height/2, depth/2);
        this.westFace[3] = new Vector3f(width/2, -height/2, depth/2);
   }

   public void setChildSegments(ModelSegment[] segments){
        this.childrenSegments = segments;
   }


    public void scale(float scaleFactor){
        for(int i = 0; i < 4; i++){
            this.topFace[i].x *= scaleFactor;
            this.topFace[i].y *= scaleFactor;
            this.topFace[i].z *= scaleFactor;

            this.bottomFace[i].x *= scaleFactor;
            this.bottomFace[i].y *= scaleFactor;
            this.bottomFace[i].z *= scaleFactor;

            this.northFace[i].x *= scaleFactor;
            this.northFace[i].y *= scaleFactor;
            this.northFace[i].z *= scaleFactor;

            this.southFace[i].x *= scaleFactor;
            this.southFace[i].y *= scaleFactor;
            this.southFace[i].z *= scaleFactor;

            this.eastFace[i].x *= scaleFactor;
            this.eastFace[i].y *= scaleFactor;
            this.eastFace[i].z *= scaleFactor;

            this.westFace[i].x *= scaleFactor;
            this.westFace[i].y *= scaleFactor;
            this.westFace[i].z *= scaleFactor;
        }
    }

    public void translatePreRotation(){
        for(int i = 0; i < 4; i++){
            this.topFace[i].sub(this.rotationPoint);
            this.bottomFace[i].sub(this.rotationPoint);
            this.northFace[i].sub(this.rotationPoint);
            this.southFace[i].sub(this.rotationPoint);
            this.eastFace[i].sub(this.rotationPoint);
            this.westFace[i].sub(this.rotationPoint);
        }
    }

    public void translatePostRotation(){
        for(int i = 0; i < 4; i++){
            this.topFace[i].add(this.rotationPoint);
            this.bottomFace[i].add(this.rotationPoint);
            this.northFace[i].add(this.rotationPoint);
            this.southFace[i].add(this.rotationPoint);
            this.eastFace[i].add(this.rotationPoint);
            this.westFace[i].add(this.rotationPoint);
        }
    }

    public void translateModelSegment(float x, float y, float z){
        Vector3f translationVector = new Vector3f(x,y,z);
        this.position.add(translationVector);
        for(int i = 0; i < 4; i++){
            this.topFace[i].add(translationVector);
            this.bottomFace[i].add(translationVector);
            this.northFace[i].add(translationVector);
            this.southFace[i].add(translationVector);
            this.eastFace[i].add(translationVector);
            this.westFace[i].add(translationVector);
        }
    }

    public void rotateModelSegmentX(float angleDeg){
        this.updateChildrenPositionX(angleDeg);
        this.rotateSegmentX(angleDeg);
    }

    public void rotateModelSegmentY(float angleDeg){
        this.updateChildrenPositionY(angleDeg);
        this.rotateSegmentY(angleDeg);
    }

    public void rotateModelSegmentZ(float angleDeg){
        this.updateChildrenPositionZ(angleDeg);
        this.rotateSegmentZ(angleDeg);
    }


    public void rotatePositionX(float angleRad){
        this.position.rotateX(angleRad);
    }

    public void rotatePositionY(float angleRad){
        this.position.rotateY(angleRad);
    }

    public void rotatePositionZ(float angleRad){
        this.position.rotateZ(angleRad);
    }

    public void updateChildrenPositionX(float angleDeg){
        if(this.childrenSegments == null){return;}
        angleDeg = angleDeg < 0 ? 360 + angleDeg : angleDeg;
        float angleRad = (float) Math.toRadians(angleDeg);
        for(int i = 0; i < this.childrenSegments.length; i++){
            this.childrenSegments[i].position.add((float) 0, (float) (MathUtils.sin(angleRad) * -this.height), Math.abs((float) ((MathUtils.sin(angleRad) * 0.125f) * this.depth)));
        }
    }

    public void updateChildrenPositionY(float angleDeg){
        if(this.childrenSegments == null){return;}
        angleDeg = angleDeg < 0 ? 360 + angleDeg : angleDeg;
        float angleRad = (float) Math.toRadians(angleDeg);
        for(int i = 0; i < this.childrenSegments.length; i++){ //This is likely some trig function
            this.childrenSegments[i].position.add((float) (MathUtils.sin(angleRad) * this.width), (float) 0, Math.abs((float) ((MathUtils.sin(angleRad) * 0.4f) * this.depth)));
        }
        //0 deg: width = 0, depth = 0
        //90 deg: width = width, depth = 1/2 depth
    }

    public void updateChildrenPositionZ(float angleDeg){

    }

    public void rotateSegmentX(float angleDeg){
        float angleRad = (float) Math.toRadians(angleDeg);
        this.translatePreRotation();
        for(int i = 0; i < 4; i++){
            this.topFace[i].rotateX(angleRad);
            this.bottomFace[i].rotateX(angleRad);
            this.northFace[i].rotateX(angleRad);
            this.southFace[i].rotateX(angleRad);
            this.eastFace[i].rotateX(angleRad);
            this.westFace[i].rotateX(angleRad);
        }
        this.translatePostRotation();
        if(this.childrenSegments == null){return;}
        for(int i = 0; i < this.childrenSegments.length; i++){
            this.childrenSegments[i].rotateSegmentX(angleDeg);
            this.childrenSegments[i].rotatePositionX(angleRad);
        }
    }

    public void rotateSegmentY(float angleDeg){
        float angleRad = (float) Math.toRadians(angleDeg);
        this.translatePreRotation();
        for(int i = 0; i < 4; i++){
            this.topFace[i].rotateY(angleRad);
            this.bottomFace[i].rotateY(angleRad);
            this.northFace[i].rotateY(angleRad);
            this.southFace[i].rotateY(angleRad);
            this.eastFace[i].rotateY(angleRad);
            this.westFace[i].rotateY(angleRad);
        }
        this.translatePostRotation();
        if(this.childrenSegments == null){return;}
        for(int i = 0; i < this.childrenSegments.length; i++){
            this.childrenSegments[i].rotateSegmentY(angleDeg);
            this.childrenSegments[i].rotatePositionY(angleRad);
        }
    }

    public void rotateSegmentZ(float angleDeg){
        float angleRad = (float) Math.toRadians(angleDeg);
        this.translatePreRotation();
        for(int i = 0; i < 4; i++){
            this.topFace[i].rotateZ(angleRad);
            this.bottomFace[i].rotateZ(angleRad);
            this.northFace[i].rotateZ(angleRad);
            this.southFace[i].rotateZ(angleRad);
            this.eastFace[i].rotateZ(angleRad);
            this.westFace[i].rotateZ(angleRad);
        }
        this.translatePostRotation();
        if(this.childrenSegments == null){return;}
        for(int i = 0; i < this.childrenSegments.length; i++){
            this.childrenSegments[i].rotateSegmentZ(angleDeg);
            this.rotatePositionZ(angleRad);
        }
    }

    public Vector3f[] getFaceClone(int face){
        Vector3f[] returnValue = new Vector3f[4];
        returnValue[0] = new Vector3f();
        returnValue[1] = new Vector3f();
        returnValue[2] = new Vector3f();
        returnValue[3] = new Vector3f();
        switch (face) {
            case 0 -> {
                returnValue[0] = new Vector3f(this.topFace[0]);
                returnValue[1] = new Vector3f(this.topFace[1]);
                returnValue[2] = new Vector3f(this.topFace[2]);
                returnValue[3] = new Vector3f(this.topFace[3]);
            }
            case 1 -> {
                returnValue[0] = new Vector3f(this.bottomFace[0]);
                returnValue[1] = new Vector3f(this.bottomFace[1]);
                returnValue[2] = new Vector3f(this.bottomFace[2]);
                returnValue[3] = new Vector3f(this.bottomFace[3]);
            }
            case 2 -> {
                returnValue[0]  = new Vector3f(this.northFace[0]);
                returnValue[1]  = new Vector3f(this.northFace[1]);
                returnValue[2]  = new Vector3f(this.northFace[2]);
                returnValue[3]  = new Vector3f(this.northFace[3]);
            }
            case 3 -> {
                returnValue[0]  = new Vector3f(this.southFace[0]);
                returnValue[1]  = new Vector3f(this.southFace[1]);
                returnValue[2]  = new Vector3f(this.southFace[2]);
                returnValue[3]  = new Vector3f(this.southFace[3]);
            }
            case 4 -> {
                returnValue[0]  = new Vector3f(this.eastFace[0]);
                returnValue[1]  = new Vector3f(this.eastFace[1]);
                returnValue[2]  = new Vector3f(this.eastFace[2]);
                returnValue[3]  = new Vector3f(this.eastFace[3]);
            }
            case 5 -> {
                returnValue[0]  = new Vector3f(this.westFace[0]);
                returnValue[1]  = new Vector3f(this.westFace[1]);
                returnValue[2]  = new Vector3f(this.westFace[2]);
                returnValue[3]  = new Vector3f(this.westFace[3]);
            }
        }

        return returnValue;
    }


}
