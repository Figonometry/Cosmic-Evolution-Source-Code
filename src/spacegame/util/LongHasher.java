package spacegame.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class LongHasher {

    private final MessageDigest digest;

    public LongHasher(){
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public long hash(long baseSeed, String inputs){
        this.digest.reset();


        this.digest.update(this.longToBytes(baseSeed));

        this.digest.update(inputs.getBytes(StandardCharsets.UTF_8));

        byte[] hash = this.digest.digest();
        return this.bytesToLong(hash);
    }



    private byte[] longToBytes(long val){
        return ByteBuffer.allocate(Long.BYTES).putLong(val).array();
    }

    private byte[] intToBytes(int val){
        return ByteBuffer.allocate(Integer.BYTES).putInt(val).array();
    }

    private long bytesToLong(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();
    }
}
