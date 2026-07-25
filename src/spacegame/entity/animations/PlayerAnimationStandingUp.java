package spacegame.entity.animations;

import spacegame.entity.EntityPlayer;

public final class PlayerAnimationStandingUp extends PlayerAnimation {
    public PlayerAnimationStandingUp(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }

    @Override
    public boolean onAnimationComplete(EntityPlayer player) {
        player.sitting = false;
        player.height = 1.79;
        player.sitMovement = 0;
        player.y += 0.4475;
        return false;
    }
}
