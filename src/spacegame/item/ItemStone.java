package spacegame.item;

import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingStoneTools;
import spacegame.world.World;

public final class ItemStone extends Item{
    public ItemStone(short ID, int textureID, String filepath, Material material) {
        super(ID, textureID, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.material = material;
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(world.getItemInChunk(x,y,z) == this.ID){
            SpaceGame.instance.setNewGui(new GuiCraftingStoneTools(SpaceGame.instance,x,y,z));
        }
    }
}
