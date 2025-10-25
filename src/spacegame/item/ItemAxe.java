package spacegame.item;

import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public final class ItemAxe extends Item {
    public ItemAxe(short ID, int textureID, String filepath, Material material) {
        super(ID, textureID, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }

}
