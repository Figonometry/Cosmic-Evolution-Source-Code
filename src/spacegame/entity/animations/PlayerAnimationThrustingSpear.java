package spacegame.entity.animations;

import spacegame.entity.EntityPlayer;

public final class PlayerAnimationThrustingSpear extends PlayerAnimation {
    public PlayerAnimationThrustingSpear(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }

    @Override
    public boolean onAnimationComplete(EntityPlayer player) {
        return false;
        //Do nothing, this needs to serve mainly as a timer
    }
}
