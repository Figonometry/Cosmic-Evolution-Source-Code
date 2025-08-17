package spacegame.world;

import spacegame.block.Block;

public final class AxisAlignedBB {

    public double minX;
    public double maxX;
    public double minY;
    public double maxY;
    public double minZ;
    public double maxZ;
    public boolean air;

    public AxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public AxisAlignedBB(){}



    public void adjustBlockBoundingBox(int x, int y, int z, short blockID, double entityX, double entityZ) {
        if(!Block.list[blockID].isSolid){
            this.minX = x;
            this.maxX = x + 1;
            this.minY = y;
            this.maxY = y + 1;
            this.minZ = z;
            this.maxZ = z + 1;
            this.air = true;
        } else {
            this.minX = x;
            this.maxX = x + 1;
            this.minY = y;
            this.maxY = y + 1;
            this.minZ = z;
            this.maxZ = z + 1;
            this.air = false;
        }
    }



    public void adjustEntityBoundingBox(double x, double y, double z, double width, double height, double depth){
        this.minX = x - (width / 2.0);
        this.maxX = x + (width / 2);
        this.minY = y - (height / 2.0);
        this.maxY = y + (height / 2.0);
        this.minZ = z - (depth / 2.0);
        this.maxZ = z + (depth / 2.0);
    }

    public void scale(double scaleFactor){
        this.minX -= scaleFactor;
        this.minY -= scaleFactor;
        this.minZ -= scaleFactor;
        this.maxX += scaleFactor;
        this.maxY += scaleFactor;
        this.maxZ += scaleFactor;
    }

    public double clipXCollide(AxisAlignedBB entityBoundingBox, double deltaX) {
        if(entityBoundingBox.maxY > this.minY && entityBoundingBox.minY < this.maxY) {
            if(entityBoundingBox.maxZ > this.minZ && entityBoundingBox.minZ < this.maxZ) {
                double max;
                if(deltaX > 0.0 && entityBoundingBox.maxX <= this.minX) {
                    max = this.minX - entityBoundingBox.maxX;
                    if(max < deltaX) {
                        deltaX = max;
                    }
                }

                if(deltaX < 0.0 && entityBoundingBox.minX >= this.maxX) {
                    max = this.maxX - entityBoundingBox.minX;
                    if(max > deltaX) {
                        deltaX = max;
                    }
                }

                return deltaX;
            } else {
                return deltaX;
            }
        } else {
            return deltaX;
        }
    }

    public double clipYCollide(AxisAlignedBB entityBoundingBox, double deltaY) {
        if(entityBoundingBox.maxX > this.minX && entityBoundingBox.minX < this.maxX) {
            if(entityBoundingBox.maxZ > this.minZ && entityBoundingBox.minZ < this.maxZ) {
                double max;
                if(deltaY > 0.0 && entityBoundingBox.maxY <= this.minY) {
                    max = this.minY - entityBoundingBox.maxY;
                    if(max < deltaY) {
                        deltaY = max;
                    }
                }

                if(deltaY < 0.0 && entityBoundingBox.minY >= this.maxY) {
                    max = this.maxY - entityBoundingBox.minY;
                    if(max > deltaY) {
                        deltaY = max;
                    }
                }

                return deltaY;
            } else {
                return deltaY;
            }
        } else {
            return deltaY;
        }
    }

    public double clipZCollide(AxisAlignedBB entityBoundingBox, double deltaZ) {
        if(entityBoundingBox.maxX > this.minX && entityBoundingBox.minX < this.maxX) {
            if(entityBoundingBox.maxY > this.minY && entityBoundingBox.minY < this.maxY) {
                double max;
                if(deltaZ > 0.0 && entityBoundingBox.maxZ <= this.minZ) {
                    max = this.minZ - entityBoundingBox.maxZ;
                    if(max < deltaZ) {
                        deltaZ = max;
                    }
                }

                if(deltaZ < 0.0 && entityBoundingBox.minZ >= this.maxZ) {
                    max = this.maxZ - entityBoundingBox.minZ;
                    if(max > deltaZ) {
                        deltaZ = max;
                    }
                }

                return deltaZ;
            } else {
                return deltaZ;
            }
        } else {
            return deltaZ;
        }
    }
    
    public boolean clip(AxisAlignedBB boundingBox) {
        return ((boundingBox.minX <= this.maxX && boundingBox.maxX >= this.minX) && (boundingBox.minY <= this.maxY && boundingBox.maxY >= this.minY) && (boundingBox.minZ <= this.maxZ && boundingBox.maxZ >= this.minZ));
    }

    public boolean pointInsideBoundingBox(double x, double y, double z) {
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
    }

    public boolean clipNegX(double maxX) {
        return maxX >= this.minX;
    }

    public boolean clipPosX(double minX) {
        return minX <= this.maxX;
    }

    public boolean clipNegY(double maxY) {
        return maxY >= this.minY;
    }

    public boolean clipPosY(double minY) {
        return minY <= this.maxY;
    }

    public boolean clipNegZ(double maxZ) {
        return maxZ >= this.minZ;
    }

    public boolean clipPosZ(double minZ) {
        return minZ <= this.maxZ;
    }

    public AxisAlignedBB expand(double deltaX, double deltaY, double deltaZ) {
        double minX = this.minX;
        double minY = this.minY;
        double minZ = this.minZ;
        double maxX = this.maxX;
        double maxY = this.maxY;
        double maxZ = this.maxZ;
        if(deltaX < 0.0) {
            minX += deltaX;
        }

        if(deltaX > 0.0) {
            maxX += deltaX;
        }

        if(deltaY < 0.0) {
            minY += deltaY;
        }

        if(deltaY > 0.0) {
            maxY += deltaY;
        }

        if(deltaZ < 0.0) {
            minZ += deltaZ;
        }

        if(deltaZ > 0.0) {
            maxZ += deltaZ;
        }

        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void move(double deltaX, double deltaY, double deltaZ){
        this.minX += deltaX;
        this.minY += deltaY;
        this.minZ += deltaZ;
        this.maxX += deltaX;
        this.maxY += deltaY;
        this.maxZ += deltaZ;
    }
}
