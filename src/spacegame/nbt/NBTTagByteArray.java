package spacegame.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByteArray extends NBTBase {
    public byte[] byteArray;

    public NBTTagByteArray() {
    }

    public NBTTagByteArray(byte[] value) {
        this.byteArray = value;
    }

    public void writeTagContents(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.byteArray.length);
        dataOutput.write(this.byteArray);
    }

    public void readTagContents(DataInput dataInput) throws IOException {
        int value = dataInput.readInt();
        this.byteArray = new byte[value];
        dataInput.readFully(this.byteArray);
    }

    public byte getType() {
        return (byte)7;
    }

    public String toString() {
        return "[" + this.byteArray.length + " bytes]";
    }
}