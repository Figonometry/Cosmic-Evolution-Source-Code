package spacegame.entity.animations;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;

public final class PlayerAnimationPanning extends PlayerAnimation {
    public PlayerAnimationPanning(boolean leftClick, boolean rightClick, boolean heldRequired, int timer) {
        super(leftClick, rightClick, heldRequired, timer);
    }

    @Override
    public boolean onAnimationComplete(EntityPlayer player) {
        if(player.getHeldItem() != Item.NULL_ITEM_REFERENCE){
            Item.list[player.getHeldItem()].onFinishRightClickAnimation(0,0,0, CosmicEvolution.instance.save.activeWorld, player);
        }
        return false;
    }
}
