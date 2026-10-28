package spacegame.world.blockstructureio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.BlockPos;
import spacegame.world.blockstructure.CharcoalPit;
import spacegame.world.Chunk;
import spacegame.world.blockstate.MultiState;

public final class CharcoalPitIO {

    public void saveCharcoalPit(Chunk chunk, NBTTagCompound nbtTagCompound){
        CharcoalPit charcoalPitState;
        int totalCount = chunk.charcoalPits.size();
        CharcoalPit[] charcoalPitStates = this.getAllCharcoalPitsInArray(chunk);
        int charcoalPitStateCount = 0;
        NBTTagCompound[] charcoalPitStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < charcoalPitStateTags.length; i++){

            charcoalPitState = charcoalPitStates[i];
            charcoalPitStateTags[i] = new NBTTagCompound();

            charcoalPitStateTags[i].setLong("startTime", charcoalPitState.getStartTime());

            charcoalPitStateTags[i].setInteger("minX", charcoalPitState.getMin().x());
            charcoalPitStateTags[i].setInteger("minY", charcoalPitState.getMin().y());
            charcoalPitStateTags[i].setInteger("minZ", charcoalPitState.getMin().z());

            charcoalPitStateTags[i].setInteger("maxX", charcoalPitState.getMax().x());
            charcoalPitStateTags[i].setInteger("maxY", charcoalPitState.getMax().y());
            charcoalPitStateTags[i].setInteger("maxZ", charcoalPitState.getMax().z());

            charcoalPitStateTags[i].setInteger("firePitX", charcoalPitState.getFirePit().x());
            charcoalPitStateTags[i].setInteger("firePitY", charcoalPitState.getFirePit().y());
            charcoalPitStateTags[i].setInteger("firePitZ", charcoalPitState.getFirePit().z());



            nbtTagCompound.setTag("charcoalPitState" + charcoalPitStateCount, charcoalPitStateTags[i]);
            charcoalPitStateCount++;
        }
        nbtTagCompound.setInteger("charcoalPitStateCount", charcoalPitStateCount);
    }

    public void loadCharcoalPits(Chunk chunk, NBTTagCompound nbtTagCompound){
        int charcoalPitStateCount = nbtTagCompound.getInteger("charcoalPitStateCount");
        NBTTagCompound charcoalPitLoadedTag;
        for (int i = 0; i < charcoalPitStateCount; i++) {
            charcoalPitLoadedTag = nbtTagCompound.getCompoundTag("charcoalPitState" + i);

            long startTime = charcoalPitLoadedTag.getLong("startTime");

            int minX = charcoalPitLoadedTag.getInteger("minX");
            int minY = charcoalPitLoadedTag.getInteger("minY");
            int minZ = charcoalPitLoadedTag.getInteger("minZ");

            int maxX = charcoalPitLoadedTag.getInteger("maxX");
            int maxY = charcoalPitLoadedTag.getInteger("maxY");
            int maxZ = charcoalPitLoadedTag.getInteger("maxZ");

            int firePitX = charcoalPitLoadedTag.getInteger("firePitX");
            int firePitY = charcoalPitLoadedTag.getInteger("firePitY");
            int firePitZ = charcoalPitLoadedTag.getInteger("firePitZ");


            CharcoalPit charcoalPit = new CharcoalPit(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ), new BlockPos(firePitX, firePitY, firePitZ));
            charcoalPit.setStartTime(startTime);

            chunk.addCharcoalPit(charcoalPit);
        }
    }
    private CharcoalPit[] getAllCharcoalPitsInArray(Chunk chunk){
        Object[] objects = chunk.charcoalPits.toArray();
        CharcoalPit[] charcoalPits = new CharcoalPit[objects.length];

        for(int i = 0; i < objects.length; i++){
            charcoalPits[i] = (CharcoalPit) objects[i];
        }

        return charcoalPits;
    }
}
