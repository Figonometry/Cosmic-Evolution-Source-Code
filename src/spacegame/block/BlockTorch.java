package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Timer;
import spacegame.entity.EntityPlayer;
import spacegame.render.model.ModelLoader;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.TorchState;

public final class BlockTorch extends Block implements ITimeUpdate {
    public BlockTorch(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        TorchState torchState = (TorchState) world.getBlockState(x,y,z, MultiState.TORCH_STATE);
        if(torchState == null)return;

        torchState.isBurnedOut = true;
        torchState.isLit = false;

        world.notifyChunk(x,y,z);
    }

    @Override
    public long getUpdateTime(int x, int y, int z, World world) {
        return Timer.GAME_DAY * 2;
    }

    @Override
    public String getDisplayStringText(int x, int y, int z, World world) {
        return null;
    }


    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        //Method lights unlit torches
        if(!MouseListener.rightClickReleased && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))return;
        short playerHeldItem = player.getHeldItem();

        if(player.getHeldBlock() != Block.torch.ID)return;

        TorchState torchState = (TorchState) world.getBlockState(x,y,z, MultiState.TORCH_STATE);
        if(torchState == null)return;

        torchState.isLit = true;
        world.notifyChunk(x,y,z);
    }

    @Override
    public void addBlockStates(int x, int y, int z, World world, EntityPlayer player, Chunk chunk){
       int torchFacingDirection = switch (facingDirection) {
            case FACE_NORTH -> TorchState.TORCH_FACE_NORTH;
            case FACE_SOUTH -> TorchState.TORCH_FACE_SOUTH;
            case FACE_EAST -> TorchState.TORCH_FACE_EAST;
            case FACE_WEST -> TorchState.TORCH_FACE_WEST;
            default -> TorchState.TORCH_FACE_STANDARD;
        };

        int key = Chunk.getBlockIndexFromCoordinates(x,y,z);
        chunk.addBlockState(x,y,z, MultiState.TORCH_STATE, new TorchState(true, false, torchFacingDirection, key));
    }


    @Override
    public boolean isLightBlock(int x, int y, int z, World world){
       TorchState torchState = (TorchState) world.getBlockState(x,y,z, MultiState.TORCH_STATE);
       if(torchState == null)return false;

       return torchState.isLit && !torchState.isBurnedOut;
    }

    @Override
    public ModelLoader getBlockModel(int x, int y, int z, World world){
        TorchState torchState = (TorchState) world.getBlockState(x,y,z, MultiState.TORCH_STATE);
        if(torchState == null)return this.blockModel;

        switch (torchState.facingDirection){
            case TorchState.TORCH_FACE_STANDARD -> {
                return BlockModelList.torchBlockModel;
            }
            case TorchState.TORCH_FACE_NORTH -> {
                return BlockModelList.torchNorthBlockModel;
            }
            case TorchState.TORCH_FACE_SOUTH -> {
                return BlockModelList.torchSouthBlockModel;
            }
            case TorchState.TORCH_FACE_EAST -> {
                return BlockModelList.torchEastBlockModel;
            }
            case TorchState.TORCH_FACE_WEST -> {
                return BlockModelList.torchWestBlockModel;
            }
            default -> {
                return this.blockModel;
            }
        }
    }
}
