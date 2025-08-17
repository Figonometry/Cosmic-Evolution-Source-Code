package spacegame.item;

import spacegame.block.Block;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingStick;
import spacegame.gui.GuiLightFire;
import spacegame.world.World;

public final class ItemStoneFragments extends Item {
    public ItemStoneFragments(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(world.getBlockID(x,y,z) == Block.campfireUnLit.ID){
            SpaceGame.instance.setNewGui(new GuiLightFire(SpaceGame.instance, x, y, z));
        }
    }
}
