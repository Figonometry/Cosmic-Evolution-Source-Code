package spacegame.entity;

import spacegame.core.CosmicEvolution;
import spacegame.render.model.Model;
import spacegame.render.model.ModelPlayer;
import spacegame.render.RenderEngine;
import spacegame.render.model.ModelWolf;

public final class EntityModelTest extends Entity {
    public static int ticksSinceLastRender = 0;
    public static int texture = RenderEngine.NULL_TEXTURE;
    public Model model;
    public int animationTimer = 0;
    public boolean animate = true;

    public EntityModelTest(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;

        this.width = 0.5;
        this.depth = 0.5;
        this.height = 1.79;
    }

    @Override
    public void tick(){
        this.doGravity();
        this.moveAndHandleCollision();
        this.updateAxisAlignedBB();
        this.animationTimer++;
    }


    public void loadTexture(){
        texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/entity/wolf.png", RenderEngine.TEXTURE_TYPE_2D, 0 , true);
    }



    @Override
    public void render(){
        if(texture == RenderEngine.NULL_TEXTURE){
            this.loadTexture();
        }
        this.model = ModelWolf.getBaseModel();
        this.model.animate(this.animationTimer, this.animate, this);
        this.model.renderModel(this);
        ticksSinceLastRender = 0;
        this.renderShadow();
    }



}
