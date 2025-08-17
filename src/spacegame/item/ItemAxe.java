package spacegame.item;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.Chunk;
import spacegame.world.World;

import java.util.Random;

public final class ItemAxe extends Item {
    public ItemAxe(short ID, int textureID, String filepath, Material material) {
        super(ID, textureID, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }
}
