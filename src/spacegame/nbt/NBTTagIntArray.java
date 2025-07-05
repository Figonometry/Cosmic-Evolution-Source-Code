package spacegame.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagIntArray extends NBTBase {
    public int[] intArray;

    public NBTTagIntArray(){

    }
    public NBTTagIntArray(int[] value) {
        this.intArray = value;
    }

    public void writeTagContents(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.intArray.length);

        for(int i = 0; i < this.intArray.length; i++){
            dataOutput.writeInt(this.intArray[i]);
        }
    }

    public void readTagContents(DataInput dataInput) throws IOException {
        int value = dataInput.readInt();
        this.intArray = new int[value];

        for(int i = 0; i < this.intArray.length; i++){
            this.intArray[i] = dataInput.readInt();
        }
    }

    public byte getType() {
        return (byte)11;
    }

    public String toString() {
        return "[" + this.intArray.length + " ints]";
    }
}
