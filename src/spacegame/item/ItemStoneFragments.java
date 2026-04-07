package spacegame.item;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiLightFire;
import spacegame.world.World;

@Deprecated
public final class ItemStoneFragments extends Item { //This likely doesnt need to exist, will deprecate in the future
    public ItemStoneFragments(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


}
