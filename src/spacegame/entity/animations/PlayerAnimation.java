package spacegame.entity.animations;

import spacegame.entity.EntityPlayer;

public abstract class PlayerAnimation {
    public int timer;
    public boolean leftClick;
    public boolean rightClick;
    public boolean heldRequired;


    public PlayerAnimation(boolean leftClick, boolean rightClick, boolean heldRequired, int timer){
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.heldRequired = heldRequired;
    }


    public abstract void onAnimationComplete(EntityPlayer player);
}
