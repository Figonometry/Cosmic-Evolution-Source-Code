package spacegame.entity.animations;

import spacegame.entity.EntityPlayer;

public final class PlayerAnimationThrustingSpearHold extends PlayerAnimationHold {
    public PlayerAnimationThrustingSpearHold(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }

    @Override
    public boolean onAnimationComplete(EntityPlayer player) {
        player.playerAnimation = new PlayerAnimationThrustingSpear(false, false, false, 30);
        return true;
    }
}
