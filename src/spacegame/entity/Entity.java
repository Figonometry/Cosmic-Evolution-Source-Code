package spacegame.entity;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.GameSettings;
import spacegame.core.MathUtils;
import spacegame.core.SpaceGame;
import spacegame.render.ModelLoader;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;
import spacegame.world.AxisAlignedBB;

import java.util.ArrayList;

public abstract class Entity {
    public static ModelLoader standardBlock = new ModelLoader("src/spacegame/assets/models/entityModels/centeredStandardBlock.obj");
    public ModelLoader entityModel;
    public AxisAlignedBB boundingBox = new AxisAlignedBB();
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;
    public float roll;
    public double width;
    public double deltaX;
    public double deltaY;
    public double deltaZ;
    public double prevX;
    public double prevY;
    public double prevZ;
    public float rawDeltaX;
    public int waitTimer;
    public double targetX;
    public double targetY;
    public double targetZ;
    public boolean shouldMove;
    public int moveTimer = 10;
    public int timeFalling;
    public boolean isJumping;
    public boolean isOnGround;
    public boolean moveEntityUp;
    public double height;
    public double depth;
    public boolean canDamage;
    public double acceleration = 0.005D;
    public double speed = 0.1D;
    public boolean inWater;
    public boolean prevInWater;
    public boolean swimming;
    public boolean despawn = false;
    public boolean canMoveWithVector = false;
    public Vector3f movementVector = new Vector3f();
    public static TextureLoader shadow;
    public static ArrayList<AxisAlignedBB> surroundingBlocks = new ArrayList<>();

    public void tick() {
        return;
    }

    public void render() {
        return;
    }

    public void damage(Vector3f movementVector, float damage){
        if(!this.canDamage){
            return;
        }

        if(this instanceof EntityLiving){
            ((EntityLiving) this).damage(damage);
        }

        this.setMovementVector(movementVector);

    }

    protected void moveWithVector(){
        this.deltaX = this.movementVector.x;
        this.deltaY = this.movementVector.y;
        this.deltaZ = this.movementVector.z;
    }

    protected void updateAxisAlignedBB() {
        this.boundingBox .adjustEntityBoundingBox(this.x, this.y, this.z, this.width, this.height, this.depth);
    }

    protected void renderShadow(){
        int x = MathUtils.floorDouble(this.x);
        int y = MathUtils.floorDouble(this.y - (this.height/2) - 0.1);
        int z = MathUtils.floorDouble(this.z);

        if(Block.list[SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x,y,z)].isSolid) {
            Shader.worldShader2DTexture.uploadBoolean("useFog", true);
            Shader.worldShader2DTexture.uploadFloat("fogRed", SpaceGame.instance.save.activeWorld.skyColor[0]);
            Shader.worldShader2DTexture.uploadFloat("fogGreen", SpaceGame.instance.save.activeWorld.skyColor[1]);
            Shader.worldShader2DTexture.uploadFloat("fogBlue", SpaceGame.instance.save.activeWorld.skyColor[2]);
            Shader.worldShader2DTexture.uploadFloat("fogDistance", GameSettings.renderDistance << 5);
            y = MathUtils.floorDouble(this.y);
            int playerChunkX = MathUtils.floorDouble(SpaceGame.instance.save.thePlayer.x) >> 5;
            int playerChunkY = MathUtils.floorDouble(SpaceGame.instance.save.thePlayer.y) >> 5;
            int playerChunkZ = MathUtils.floorDouble(SpaceGame.instance.save.thePlayer.z) >> 5;
            int xOffset = (x >> 5) - playerChunkX;
            int yOffset = (y >> 5) - playerChunkY;
            int zOffset = (z >> 5) - playerChunkZ;
            xOffset <<= 5;
            yOffset <<= 5;
            zOffset <<= 5;
            Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
            Shader.worldShader2DTexture.uploadVec3f("chunkOffset", chunkOffset);
            Tessellator tessellator = Tessellator.instance;
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) + (this.width)), (float) ((this.y % 32) - (this.height / 2) + 0.01F), (float) ((this.z % 32) - (this.width)), 3);
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) - (this.width)), (float) ((this.y % 32) - (this.height / 2) + 0.01F), (float) ((this.z % 32) + (this.width)), 1);
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) + (this.width)), (float) ((this.y % 32) - (this.height / 2) + 0.01F), (float) ((this.z % 32) + (this.width)), 2);
            tessellator.addVertex2DTexture(16777215, (float) ((this.x % 32) - (this.width)), (float) ((this.y % 32) - (this.height / 2) + 0.01F), (float) ((this.z % 32) - (this.width)), 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(shadow.texID, Shader.worldShader2DTexture, SpaceGame.camera);
            GL46.glDisable(GL46.GL_BLEND);
        }
    }

 //  protected void setBoundingBoxSizeFromYaw(){
 //      if((this.yaw  <= 45 || this.yaw >= 315) || (this.yaw >= 135 && this.yaw <= 225)){
 //          this.width = 0.5;
 //          this.depth = 1.5;
 //      } else {
 //          this.width = 1.5;
 //          this.depth = 0.5;
 //      }
 //  }

    protected void updateGroundPosition(float rawDeltaX, float rawDeltaY, float rawDeltaZ){
        this.deltaZ = 0.0F;
        this.deltaX = 0.0F;
        this.deltaY = 0.0F;

        double speed = this.speed;

        if(this instanceof EntityLiving){
           if(((EntityLiving) this).alerted){
               speed *= 4;
           }
        }

        float distance = rawDeltaX * rawDeltaX + rawDeltaZ * rawDeltaZ;
        if (distance >= 0.01F) {
            distance = (float) (speed / Math.sqrt(distance));
            rawDeltaX *= distance;
            rawDeltaZ *= distance;
            float sine = (float) MathUtils.sin((float) Math.toRadians(this.yaw));
            float cosine = (float) MathUtils.cos((float) Math.toRadians(this.yaw));
            this.deltaX += rawDeltaX * cosine - rawDeltaZ * sine;
            this.deltaZ += rawDeltaZ * cosine + rawDeltaX * sine;
        }

    }

    protected void doGravity(){
        if(!this.moveEntityUp) {
            this.timeFalling++;
            this.deltaY -= this.timeFalling * this.acceleration;
            if(this.timeFalling > 120){
                this.timeFalling = 120;
            }
        }
    }


    protected void moveAndHandleCollision(){
        this.boundingBox.adjustEntityBoundingBox(this.x, this.y, this.z, this.width, this.height, this.depth);
        surroundingBlocks.clear();
        surroundingBlocks = SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockBoundingBoxes(this.boundingBox.expand(this.deltaX, this.deltaY, this.deltaZ), surroundingBlocks);

        AxisAlignedBB block;
        for(int i = 0; i < surroundingBlocks.size(); i++) {
            block = surroundingBlocks.get(i);
            this.deltaX = block.clipXCollide(this.boundingBox, this.deltaX);
        }

        this.boundingBox.move(this.deltaX, 0, 0);

        for(int i = 0; i < surroundingBlocks.size(); i++) {
            block = surroundingBlocks.get(i);
            this.deltaY = block.clipYCollide(this.boundingBox, this.deltaY);
            if(block.maxY == this.boundingBox.minY) {
                this.isOnGround = true;
                this.timeFalling = 0;
            }
        }


        this.boundingBox.move(0, this.deltaY, 0);

        for(int i = 0; i < surroundingBlocks.size(); i++) {
            block = surroundingBlocks.get(i);
            this.deltaZ = block.clipZCollide(this.boundingBox, this.deltaZ);
        }

        this.boundingBox.move(0, 0, this.deltaZ);

        if(this.deltaY == 0 && this.canMoveWithVector) {
            this.canMoveWithVector = false;
            this.deltaX = 0;
            this.deltaY = 0;
            this.deltaZ = 0;
        }


        this.x += this.deltaX;
        this.y += this.deltaY;
        this.z += this.deltaZ;
    }


    public void setMovementVector(Vector3f movementVector){
        this.movementVector = movementVector;
        this.canMoveWithVector = true;
    }

    public static void initShadow(){
        shadow = new TextureLoader("src/spacegame/assets/textures/item/shadow.png", 32, 32);
    }

}
