package spacegame.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagShortArray extends NBTBase {
    public short[] shortArray;

    public NBTTagShortArray(){

    }
    public NBTTagShortArray(short[] value) {
        this.shortArray = value;
    }

    public void writeTagContents(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.shortArray.length);

        for(int i = 0; i < this.shortArray.length; i++){
            dataOutput.writeShort(this.shortArray[i]);
        }
    }

    public void readTagContents(DataInput dataInput) throws IOException {
        int value = dataInput.readInt();
        this.shortArray = new short[value];

        for(int i = 0; i < this.shortArray.length; i++){
            this.shortArray[i] = dataInput.readShort();
        }
    }

    public byte getType() {
        return (byte)12;
    }

    public String toString() {
        return "[" + this.shortArray.length + " shorts]";
    }
}
