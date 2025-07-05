package spacegame.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTBase {
    public long longValue;

    public NBTTagLong() {
    }

    public NBTTagLong(long value) {
        this.longValue = value;
    }

    public void writeTagContents(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.longValue);
    }

    public void readTagContents(DataInput dataInput) throws IOException {
        this.longValue = dataInput.readLong();
    }

    public byte getType() {
        return (byte)4;
    }

    public String toString() {
        return "" + this.longValue;
    }
}
