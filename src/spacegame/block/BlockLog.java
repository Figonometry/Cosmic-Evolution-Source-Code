package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.ItemAxe;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class BlockLog extends Block {
    public BlockLog(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        this.handleSpecialLeftClickFunctions(x,y,z, world, player);
        world.setBlockWithNotify(x,y,z, Block.air.ID);
        this.notifyNearbyLeafBlocks(x,y,z, world);
        player.reduceHeldItemDurability();
    }

    @Override
    protected void handleSpecialLeftClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.NULL_ITEM_REFERENCE && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            if(Item.list[playerHeldItem] instanceof ItemAxe){
                for(int i = 0; i < 4; i++){
                    world.addEntity(new EntityItem(x + CosmicEvolution.globalRand.nextDouble(), y + 0.5, z + CosmicEvolution.globalRand.nextDouble(), Item.fireWood.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
                }
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }
        } else {
            if (list[world.getBlockID(x,y,z)].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityBlock(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[world.getBlockID(x,y,z)].itemMetadata, (byte) 1));
            }
        }
    }

    private void notifyNearbyLeafBlocks(int x, int y, int z, World world){
        int minBoxX = x - 1;
        int minBoxY = y - 1;
        int minBoxZ = z - 1;
        int maxBoxX = x + 1;
        int maxBoxY = y + 1;
        int maxBoxZ = z + 1;

        for(x = minBoxX; x <= maxBoxX; x++){
            for(y = minBoxY; y <= maxBoxY; y++){
                for(z = minBoxZ; z <= maxBoxZ; z++){
                    if(Block.list[world.getBlockID(x,y,z)] instanceof BlockLeaf){
                       world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addDecayableLeafToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
                    }
                }
            }
        }
    }

    @Override
    public int getBlockTexture(int face) {
       switch (this.ID){
           case 12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27 -> {
               return switch (face) {
                   case 0, 1 -> this.textureID + 1;
                   default -> this.textureID;
               };
           }
           case 28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43 -> {
               return switch (face) {
                   case 2, 3 -> this.textureID + 1;
                   default -> this.textureID;
               };
           }
           case 44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59 -> {
               return switch (face) {
                   case 4, 5 -> this.textureID + 1;
                   default -> this.textureID;
               };
           }
           default -> {
               return this.textureID;
           }
       }
    }

    public static int facingDirectionOfLog(short ID){
        switch (ID){
            case 12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27 -> {
                return 1;
            }
            case 28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43 -> {
                return 2;
            }
            case 44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,597 -> {
                return 3;
            }
            default -> {
                return 0;
            }
        }
    }

    public static int sizeOfLog(short ID){
        switch (ID){
            case 13, 29, 45:
                return 15;
            case 14, 30, 46:
                return 14;
            case 15, 31, 47:
                return 13;
            case 16, 32, 48:
                return 12;
            case 17, 33, 49:
                return 11;
            case 18, 34, 50:
                return 10;
            case 19, 35, 51:
                return 9;
            case 20, 36, 52:
                return 8;
            case 21, 37, 53:
                return 7;
            case 22, 38, 54:
                return 6;
            case 23, 39, 55:
                return 5;
            case 24, 40, 56:
                return 4;
            case 25, 41, 57:
                return 3;
            case 26, 42, 58:
                return 2;
            case 27, 43, 59:
                return 1;
            default:
                return 16;
        }
    }

    public static byte getIDFromParameters(int size, int faceDirection){
        switch (faceDirection){
            case 1:
                return switch (size){
                    case 1 -> 27;
                    case 2 -> 26;
                    case 3 -> 25;
                    case 4 -> 24;
                    case 5 -> 23;
                    case 6 -> 22;
                    case 7 -> 21;
                    case 8 -> 20;
                    case 9 -> 19;
                    case 10 -> 18;
                    case 11 -> 17;
                    case 12 -> 16;
                    case 13 -> 15;
                    case 14 -> 14;
                    case 15 -> 13;
                    case 16 -> 12;
                    default -> throw new IllegalStateException("Unexpected value: " + size);
                };
            case 2:
                return switch (size){
                    case 1 -> 43;
                    case 2 -> 42;
                    case 3 -> 41;
                    case 4 -> 40;
                    case 5 -> 39;
                    case 6 -> 38;
                    case 7 -> 37;
                    case 8 -> 36;
                    case 9 -> 35;
                    case 10 -> 34;
                    case 11 -> 33;
                    case 12 -> 32;
                    case 13 -> 31;
                    case 14 -> 30;
                    case 15 -> 29;
                    case 16 -> 28;
                    default -> throw new IllegalStateException("Unexpected value: " + size);
                };
            case 3:
                return switch (size){
                    case 1 -> 59;
                    case 2 -> 58;
                    case 3 -> 57;
                    case 4 -> 56;
                    case 5 -> 55;
                    case 6 -> 54;
                    case 7 -> 53;
                    case 8 -> 52;
                    case 9 -> 51;
                    case 10 -> 50;
                    case 11 -> 49;
                    case 12 -> 48;
                    case 13 -> 47;
                    case 14 -> 46;
                    case 15 -> 45;
                    case 16 -> 44;
                    default -> throw new IllegalStateException("Unexpected value: " + size);
                };
        }
        return 0;
    }
}
