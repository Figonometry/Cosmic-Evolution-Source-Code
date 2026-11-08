package spacegame.entity.animations;

import spacegame.block.Block;
import spacegame.block.BlockItem;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;

public final class PlayerAnimationSwingHammer extends PlayerAnimation {
    public PlayerAnimationSwingHammer(boolean leftClick, boolean rightClick, boolean heldRequired, int timer, int minTimer) {
        super(leftClick, rightClick, heldRequired, timer, minTimer);
    }

    @Override
    public boolean onAnimationComplete(EntityPlayer player) {
        //Execute right click, right click on BlockItem will break chunks
        Block block = Block.list[player.blockIDPlayerLookingAt];
        if(block instanceof BlockItem){
            block.handleSpecialRightClickFunctions(player.blockLookingAtCoords[0], player.blockLookingAtCoords[1], player.blockLookingAtCoords[2], CosmicEvolution.instance.save.activeWorld, player);
            CosmicEvolution.instance.soundPlayer.playSound(player.x, player.y, player.z, new Sound(Sound.oreChunkBreak, false, 1), 1);
        }
        return false;
    }
}
