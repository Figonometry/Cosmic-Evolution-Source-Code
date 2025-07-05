package spacegame.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class NBTIO {
    public static NBTTagCompound readCompressed(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(inputStream));

        NBTTagCompound nBTTagCompound;
        try {
            nBTTagCompound = read(dataInputStream);
        } finally {
            dataInputStream.close();
        }

        return nBTTagCompound;
    }

    public static void writeCompressed(NBTTagCompound compoundTag, OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(outputStream));

        try {
            write(compoundTag, dataOutputStream);
        } finally {
            dataOutputStream.close();
        }

    }

    public static NBTTagCompound decompress(byte[] data) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data)));

        NBTTagCompound nBTTagCompound;
        try {
            nBTTagCompound = read(dataInputStream);
        } finally {
            dataInputStream.close();
        }

        return nBTTagCompound;
    }

    public static byte[] compress(NBTTagCompound compoundTag) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(byteArrayOutputStream));

        try {
            write(compoundTag, dataOutputStream);
        } finally {
            dataOutputStream.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static NBTTagCompound read(DataInput dataInput) throws IOException {
        NBTBase nBTBase = NBTBase.readNamedTag(dataInput);
        if(nBTBase instanceof NBTTagCompound) {
            return (NBTTagCompound)nBTBase;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTTagCompound compoundTag, DataOutput dataOutput) throws IOException {
        NBTBase.writeNamedTag(compoundTag, dataOutput);
    }
}

