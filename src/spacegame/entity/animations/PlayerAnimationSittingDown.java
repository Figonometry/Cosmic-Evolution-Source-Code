package spacegame.entity.animations;

import spacegame.entity.EntityPlayer;

public final class PlayerAnimationSittingDown extends PlayerAnimation {
    public PlayerAnimationSittingDown(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }

    @Override
    public boolean onAnimationComplete(EntityPlayer player) {
        player.sitting = true;
        player.height = 0.895;
        player.sitMovement = 0;
        player.y -= 0.4475;
        return false;
    }
}
