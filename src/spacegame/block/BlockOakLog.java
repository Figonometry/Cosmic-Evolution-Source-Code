package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;

public final class BlockOakLog extends BlockLog {
    public BlockOakLog(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public int getBlockTexture(int x, int y, int z, int face) {
        LogState logState = (LogState) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.LOG_STATE);
        if(logState == null)return this.textureID;

        switch (logState.facingDirection){
            case LogState.FACE_DIRECTION_TOP_AND_BOTTOM -> {
                switch (face) {
                    case Block.FACE_UP, Block.FACE_DOWN -> {
                        return BlockTextureList.OAK_LOG_TOP_TEXTURE;
                    }
                    default -> {
                        return this.textureID;
                    }
                }
            }
            case LogState.FACE_DIRECTION_NORTH_AND_SOUTH -> {
                switch (face) {
                    case Block.FACE_NORTH, Block.FACE_SOUTH -> {
                        return BlockTextureList.OAK_LOG_TOP_TEXTURE;
                    }
                    default -> {
                        return this.textureID;
                    }
                }
            }
            case LogState.FACE_DIRECTION_EAST_AND_WEST -> {
                switch (face) {
                    case Block.FACE_EAST, Block.FACE_WEST -> {
                        return BlockTextureList.OAK_LOG_TOP_TEXTURE;
                    }
                    default -> {
                        return this.textureID;
                    }
                }
            }
        }

        return this.textureID;
    }
}
