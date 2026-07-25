package spacegame.entity.animations;

public abstract class PlayerAnimationHold extends PlayerAnimation { //Meant to delay the start of any animation, essentially a timer
    public PlayerAnimationHold(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }
}
