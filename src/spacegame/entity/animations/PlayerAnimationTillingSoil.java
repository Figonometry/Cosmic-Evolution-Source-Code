package spacegame.entity.animations;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;

public final class PlayerAnimationTillingSoil extends PlayerAnimation {
    public PlayerAnimationTillingSoil(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }

    @Override
    public void onAnimationComplete(EntityPlayer player) {
        Item.list[player.getHeldItem()].onFinishRightClickAnimation(0,0,0, CosmicEvolution.instance.save.activeWorld, player);
    }
}
