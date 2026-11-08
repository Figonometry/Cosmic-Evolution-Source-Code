package spacegame.entity.animations;

import spacegame.entity.EntityPlayer;

public abstract class PlayerAnimation {
    public int timer;
    public int minTimer;
    public boolean leftClick;
    public boolean rightClick;
    public boolean heldRequired;


    public PlayerAnimation(boolean leftClick, boolean rightClick, boolean heldRequired, int timer, int minTimer){
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.heldRequired = heldRequired;
        this.timer = timer;
        this.minTimer = minTimer;
    }


    public abstract boolean onAnimationComplete(EntityPlayer player); //Returns true if another animation is queued from a previous one
}
