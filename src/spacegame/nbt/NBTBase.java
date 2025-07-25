package spacegame.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
    private String key = null;

    public abstract void writeTagContents(DataOutput dataOutput) throws IOException;

    public abstract void readTagContents(DataInput dataInput) throws IOException;

    public abstract byte getType();

    public String getKey() {
        return this.key == null ? "" : this.key;
    }

    public NBTBase setKey(String name) {
        this.key = name;
        return this;
    }

    public static NBTBase readNamedTag(DataInput dataInput) throws IOException {
        byte type = dataInput.readByte();
        if(type == 0) {
            return new NBTTagEnd();
        } else {
            NBTBase nBTBase = createTagOfType(type);
            nBTBase.key = dataInput.readUTF();
            nBTBase.readTagContents(dataInput);
            return nBTBase;
        }
    }

    public static void writeNamedTag(NBTBase tag, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(tag.getType());
        if(tag.getType() != 0) {
            dataOutput.writeUTF(tag.getKey());
            tag.writeTagContents(dataOutput);
        }
    }

    public static NBTBase createTagOfType(byte type) {
        switch(type) {
            case 0:
                return new NBTTagEnd();
            case 1:
                return new NBTTagByte();
            case 2:
                return new NBTTagShort();
            case 3:
                return new NBTTagInt();
            case 4:
                return new NBTTagLong();
            case 5:
                return new NBTTagFloat();
            case 6:
                return new NBTTagDouble();
            case 7:
                return new NBTTagByteArray();
            case 8:
                return new NBTTagString();
            case 9:
                return new NBTTagList();
            case 10:
                return new NBTTagCompound();
            case 11:
                return new NBTTagIntArray();
            case 12:
                return new NBTTagShortArray();
            default:
                return null;
        }
    }

    public static String getTagName(byte type) {
        switch(type) {
            case 0:
                return "TAG_End";
            case 1:
                return "TAG_Byte";
            case 2:
                return "TAG_Short";
            case 3:
                return "TAG_Int";
            case 4:
                return "TAG_Long";
            case 5:
                return "TAG_Float";
            case 6:
                return "TAG_Double";
            case 7:
                return "TAG_Byte_Array";
            case 8:
                return "TAG_String";
            case 9:
                return "TAG_List";
            case 10:
                return "TAG_Compound";
            case 11:
                return "TAG_Int_Array";
            case 12:
                return "Tag_Short_Array";
            default:
                return "UNKNOWN";
        }
    }

}

