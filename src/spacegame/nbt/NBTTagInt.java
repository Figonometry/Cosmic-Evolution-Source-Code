package spacegame.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase {
    public int intValue;

    public NBTTagInt() {
    }

    public NBTTagInt(int value) {
        this.intValue = value;
    }

    public void writeTagContents(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.intValue);
    }

    public void readTagContents(DataInput dataInput) throws IOException {
        this.intValue = dataInput.readInt();
    }

    public byte getType() {
        return (byte)3;
    }

    public String toString() {
        return "" + this.intValue;
    }
}
