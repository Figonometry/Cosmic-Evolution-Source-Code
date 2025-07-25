package spacegame.item;

import spacegame.block.Block;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingStick;
import spacegame.gui.GuiLightFire;
import spacegame.world.WorldFace;

public final class ItemStoneFragments extends Item {
    public ItemStoneFragments(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onRightClick(int x, int y, int z, WorldFace worldFace, EntityPlayer player){
        if(worldFace.getBlockID(x,y,z) == Block.campfireUnLit.ID){
            SpaceGame.instance.setNewGui(new GuiLightFire(SpaceGame.instance, x, y, z));
        }
        if(worldFace.getItemInChunk(x,y,z) == Item.rawStick.ID){
            SpaceGame.instance.setNewGui(new GuiCraftingStick(SpaceGame.instance, x, y, z));
        }
    }
}
