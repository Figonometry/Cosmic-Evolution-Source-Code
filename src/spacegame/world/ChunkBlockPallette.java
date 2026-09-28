package spacegame.world;

import java.util.concurrent.ConcurrentHashMap;

public class ChunkBlockPallette {
    public ConcurrentHashMap<Short, Short> blocksInPallette = new ConcurrentHashMap<>(); //The key pair is in the order of blockID (defined in Block.java) and the local chunk blockID
    private short[] blockIDs; //This stores the direct numeric IDs of the Block
    private short numberOfUniqueBlocks = 0;
    private byte bitStride = 1;
    private Chunk chunk;


    public ChunkBlockPallette(Chunk chunk){
        this.chunk = chunk;
        this.initializeStorageForLoading();
    }


    protected boolean isBlockAlreadyInPallette(short blockID){
        return this.blocksInPallette.containsKey(blockID);
    }

    //Place a new key-value pair in the hashmap using the current number of unique blocks
    //Check if the blockIDs are null (will be the case for fully air chunks for example) if it is initialize to a size of 1
    //
    protected void addNewBlockIDToPallete(short blockID){
        synchronized (this.chunk) {
            this.blocksInPallette.put(blockID, this.numberOfUniqueBlocks);

            short[] newBlockIDs = new short[this.numberOfUniqueBlocks + 1];
            for (int i = 0; i < this.numberOfUniqueBlocks; i++) {
                newBlockIDs[i] = this.blockIDs[i];
            }
            newBlockIDs[this.numberOfUniqueBlocks] = blockID;
            this.blockIDs = newBlockIDs;

            this.numberOfUniqueBlocks++;

            if (this.numberOfUniqueBlocks > (1 << this.bitStride)) {
                this.bitStride++;
                this.repackBlockArray();
            }
        }
    }






    //Determines if a value at an index will be split between two shorts
    private boolean isValueSplit(int blockIndex, byte bitStride, int storageLength) {
        int bitStart = blockIndex * bitStride;
        int bitEnd   = bitStart + bitStride;

        int totalBits = storageLength * Short.SIZE;

        // If the value would spill past the end of storage, treat as NOT split
        if (bitEnd > totalBits) return false;

        // Normal per-short split check
        int localBitStart = bitStart & (Short.SIZE - 1);
        return localBitStart + bitStride > Short.SIZE;
    }






    private void repackBlockArray() {
        synchronized (this.chunk) {
            // Old stride is always (newStride - 1)
            byte oldBitStride = (byte) (this.bitStride - 1);

            // Old packed block array
            short[] oldStorage = this.chunk.blocks;

            // Compute new storage size (always divisible by 16 because NUMBER_OF_BLOCKS is divisible by 16)
            int newSize = (Chunk.NUMBER_OF_BLOCKS * this.bitStride) / Short.SIZE;
            short[] newStorage = new short[newSize];

            // Repack every block
            for (int blockIndex = 0; blockIndex < Chunk.NUMBER_OF_BLOCKS; blockIndex++) {

                // Read old palette index using old stride
                short oldValue = this.readBlockValue(oldStorage, oldBitStride, blockIndex);

                // Write palette index using new stride
                this.writeBlockValue(newStorage, this.bitStride, blockIndex, oldValue);
            }

            // Replace chunk storage with repacked version
            this.chunk.blocks = newStorage;
        }
    }


    //localBitStart in the following functions refers to the position of the least significant bit of the value encoded
    protected void writeBlockValue(short[] storage, byte bitStride, int blockIndex, short blockValue){
        int storageLength = storage.length;

        if (!this.isValueSplit(blockIndex, bitStride, storageLength)) {
            int shortIndex = (blockIndex * bitStride) / Short.SIZE;
            byte localBitStart = (byte)((blockIndex * bitStride) & (Short.SIZE - 1));
            storage[shortIndex] = this.writeBlockValueNoSplit(storage[shortIndex], bitStride, localBitStart, blockValue);
        } else {
            this.writeBlockValueSplit(storage, bitStride,
                    (byte)((blockIndex * bitStride) & (Short.SIZE - 1)),
                    blockIndex, blockValue);
        }
    }



    private short writeBlockValueNoSplit(short storage, byte bitStride, byte localBitStart, short blockValue){
        int mask = ((1 << bitStride) - 1) << (16 - localBitStart - bitStride);
        int cleared = storage & ~mask;
        int inserted = cleared | ((blockValue << (16 - localBitStart - bitStride)) & mask);
        return (short) inserted;
    }

    private void writeBlockValueSplit(short[] storage, byte bitStride, byte localBitStart, int blockIndex, short blockValue){
        int lowerIndex = (blockIndex * bitStride) / Short.SIZE;
        int upperIndex = lowerIndex + 1;

        int lower = storage[lowerIndex] & 0xFFFF;
        int upper = storage[upperIndex] & 0xFFFF;
        int window = (lower << 16) | upper;

        int bitOffset = (blockIndex * bitStride) % Short.SIZE;
        int shift = 32 - bitOffset - bitStride;

        //build mask for the bit region
        int mask = ((1 << bitStride) - 1) << shift;

        //clear the region
        window &= ~mask;

        //insert the new value
        window |= (blockValue << shift);

        //split back into two shorts
        storage[lowerIndex] = (short) (window >> 16);
        storage[upperIndex] = (short) (window & 0xFFFF);
    }



    protected short readBlockValue(short[] storage, byte bitStride, int blockIndex){
        int storageLength = storage.length;

        if (!this.isValueSplit(blockIndex, bitStride, storageLength)) {
            int shortIndex = (blockIndex * bitStride) / Short.SIZE;
            byte localBitStart = (byte)((blockIndex * bitStride) & (Short.SIZE - 1));
            return this.readBlockValueFromBitsNoSplit(storage[shortIndex], bitStride, localBitStart);
        } else {
            int lowerIndex = (blockIndex * bitStride) / Short.SIZE;
            int upperIndex = lowerIndex + 1;

            int lower = storage[lowerIndex] & 0xFFFF;
            int upper = storage[upperIndex] & 0xFFFF;
            int window = (lower << 16) | upper;

            int bitOffset = (blockIndex * bitStride) % Short.SIZE;
            int shift = 32 - bitOffset - bitStride;
            int mask = (1 << bitStride) - 1;

            return (short)((window >> shift) & mask);
        }
    }



    //The read isnt exactly correct, the value should be 1 in all the appropriate bits
    private short readBlockValueFromBitsNoSplit(short storage, byte bitStride, byte localBitStart){
        int shift = 16 - localBitStart - bitStride;
        return (short) ((storage >> shift) & this.createBitMask(bitStride));
    }


    protected short getBlockID(int blockIndex){
        return this.blockIDs[this.readBlockValue(this.chunk.blocks, this.bitStride, blockIndex)];
    }

    public void initializeStorageForLoading() {
        // Reset palette
        this.blocksInPallette.clear();
        this.numberOfUniqueBlocks = 0;
        this.bitStride = 1;
        this.blockIDs = null;

        // Allocate packed storage using initial bitStride
        int size = (Chunk.NUMBER_OF_BLOCKS * this.bitStride) / Short.SIZE;
        this.chunk.blocks = new short[size];
    }


    public void setBlockID(int blockIndex, short blockID){
        if (!this.isBlockAlreadyInPallette(blockID)) {
            this.addNewBlockIDToPallete(blockID);
        }

        short paletteIndex = this.blocksInPallette.get(blockID);

        this.writeBlockValue(this.chunk.blocks, this.bitStride, blockIndex, paletteIndex);
    }



    public short[] decompressBlocks() {
        short[] full = new short[Chunk.NUMBER_OF_BLOCKS];

        for (int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++) {
            short paletteIndex = this.readBlockValue(this.chunk.blocks, this.bitStride, i);
            full[i] = this.blockIDs[paletteIndex];
        }

        return full;
    }






    private int createBitMask(byte bitStride){
        return (1 << bitStride) - 1;
    }




}
