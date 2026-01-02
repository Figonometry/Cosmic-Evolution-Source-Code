package spacegame.world;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.entity.Entity;
import spacegame.entity.EntityLiving;

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
        if(!Block.list[blockID].isSolid && Block.list[blockID].standardCollisionBoundingBox.equals(Block.standardBlock)){
            this.minX = x;
            this.maxX = x + 1;
            this.minY = y;
            this.maxY = y + 1;
            this.minZ = z;
            this.maxZ = z + 1;
            this.air = true;
        } else {
            if(blockID == Block.logPile.ID){
                int logCount = CosmicEvolution.instance.save.activeWorld.getChestLocation(x,y,z).inventory.itemStacks[0].count / 2;
                if(logCount <= 4){
                    this.minX = x + Block.quarterBlock.minX;
                    this.maxX = x + Block.quarterBlock.maxX;
                    this.minY = y + Block.quarterBlock.minY;
                    this.maxY = y + Block.quarterBlock.maxY;
                    this.minZ = z + Block.quarterBlock.minZ;
                    this.maxZ = z + Block.quarterBlock.maxZ;
                } else if(logCount <= 8){
                    this.minX = x + Block.slab.minX;
                    this.maxX = x + Block.slab.maxX;
                    this.minY = y + Block.slab.minY;
                    this.maxY = y + Block.slab.maxY;
                    this.minZ = z + Block.slab.minZ;
                    this.maxZ = z + Block.slab.maxZ;
                } else if(logCount <= 12){
                    this.minX = x + Block.threeQuartersBlock.minX;
                    this.maxX = x + Block.threeQuartersBlock.maxX;
                    this.minY = y + Block.threeQuartersBlock.minY;
                    this.maxY = y + Block.threeQuartersBlock.maxY;
                    this.minZ = z + Block.threeQuartersBlock.minZ;
                    this.maxZ = z + Block.threeQuartersBlock.maxZ;
                } else {
                    this.minX = x + Block.standardBlock.minX;
                    this.maxX = x + Block.standardBlock.maxX;
                    this.minY = y + Block.standardBlock.minY;
                    this.maxY = y + Block.standardBlock.maxY;
                    this.minZ = z + Block.standardBlock.minZ;
                    this.maxZ = z + Block.standardBlock.maxZ;
                }
            } else if(blockID == Block.brickPile.ID){
                int brickCount = CosmicEvolution.instance.save.activeWorld.getChestLocation(x,y,z).inventory.itemStacks[0].count;
                if(brickCount <= 12){
                    this.minX = x + Block.quarterBlock.minX;
                    this.maxX = x + Block.quarterBlock.maxX;
                    this.minY = y + Block.quarterBlock.minY;
                    this.maxY = y + Block.quarterBlock.maxY;
                    this.minZ = z + Block.quarterBlock.minZ;
                    this.maxZ = z + Block.quarterBlock.maxZ;
                } else if(brickCount <= 24){
                    this.minX = x + Block.slab.minX;
                    this.maxX = x + Block.slab.maxX;
                    this.minY = y + Block.slab.minY;
                    this.maxY = y + Block.slab.maxY;
                    this.minZ = z + Block.slab.minZ;
                    this.maxZ = z + Block.slab.maxZ;
                } else if(brickCount <= 36){
                    this.minX = x + Block.threeQuartersBlock.minX;
                    this.maxX = x + Block.threeQuartersBlock.maxX;
                    this.minY = y + Block.threeQuartersBlock.minY;
                    this.maxY = y + Block.threeQuartersBlock.maxY;
                    this.minZ = z + Block.threeQuartersBlock.minZ;
                    this.maxZ = z + Block.threeQuartersBlock.maxZ;
                } else {
                    this.minX = x + Block.standardBlock.minX;
                    this.maxX = x + Block.standardBlock.maxX;
                    this.minY = y + Block.standardBlock.minY;
                    this.maxY = y + Block.standardBlock.maxY;
                    this.minZ = z + Block.standardBlock.minZ;
                    this.maxZ = z + Block.standardBlock.maxZ;
                }
            } else {
                this.minX = x + Block.list[blockID].standardCollisionBoundingBox.minX;
                this.maxX = x + Block.list[blockID].standardCollisionBoundingBox.maxX;
                this.minY = y + Block.list[blockID].standardCollisionBoundingBox.minY;
                this.maxY = y + Block.list[blockID].standardCollisionBoundingBox.maxY;
                this.minZ = z + Block.list[blockID].standardCollisionBoundingBox.minZ;
                this.maxZ = z + Block.list[blockID].standardCollisionBoundingBox.maxZ;
            }
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

    public double clipXCollide(AxisAlignedBB entityBoundingBox, double deltaX, Entity entity) {
        if(entityBoundingBox.maxY > this.minY && entityBoundingBox.minY < this.maxY) {
            if(entityBoundingBox.maxZ > this.minZ && entityBoundingBox.minZ < this.maxZ) {
                double max;
                if(deltaX > 0.0 && entityBoundingBox.maxX <= this.minX) {
                    max = this.minX - entityBoundingBox.maxX;
                    if(max < deltaX) {
                        deltaX = max;
                        if(this.maxY - this.minY <= 0.5 && entity instanceof EntityLiving) {
                            entity.y += (this.maxY - this.minY);
                        }
                    }
                }

                if(deltaX < 0.0 && entityBoundingBox.minX >= this.maxX) {
                    max = this.maxX - entityBoundingBox.minX;
                    if(max > deltaX) {
                        deltaX = max;
                        if(this.maxY - this.minY <= 0.5 && entity instanceof EntityLiving) {
                            entity.y += (this.maxY - this.minY);
                        }
                    }
                }

            }
        }
        return deltaX;
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

            }
        }
        return deltaY;
    }

    public double clipZCollide(AxisAlignedBB entityBoundingBox, double deltaZ, Entity entity) {
        if(entityBoundingBox.maxX > this.minX && entityBoundingBox.minX < this.maxX) {
            if(entityBoundingBox.maxY > this.minY && entityBoundingBox.minY < this.maxY) {
                double max;
                if(deltaZ > 0.0 && entityBoundingBox.maxZ <= this.minZ) {
                    max = this.minZ - entityBoundingBox.maxZ;
                    if(max < deltaZ) {
                        deltaZ = max;
                        if(this.maxY - this.minY <= 0.5 && entity instanceof EntityLiving) {
                            entity.y += (this.maxY - this.minY);
                        }
                    }
                }

                if(deltaZ < 0.0 && entityBoundingBox.minZ >= this.maxZ) {
                    max = this.maxZ - entityBoundingBox.minZ;
                    if(max > deltaZ) {
                        deltaZ = max;
                        if(this.maxY - this.minY <= 0.5 && entity instanceof EntityLiving) {
                            entity.y += (this.maxY - this.minY);
                        }
                    }
                }

            }
        }
        return deltaZ;
    }
    
    public boolean clip(AxisAlignedBB boundingBox) {
        return ((boundingBox.minX < this.maxX && boundingBox.maxX > this.minX) && (boundingBox.minY < this.maxY && boundingBox.maxY > this.minY) && (boundingBox.minZ < this.maxZ && boundingBox.maxZ > this.minZ));
    }

    public boolean pointInsideBoundingBox(double x, double y, double z) {
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY && z > this.minZ && z < this.maxZ;
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
