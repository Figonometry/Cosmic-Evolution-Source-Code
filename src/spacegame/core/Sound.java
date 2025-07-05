package spacegame.core;

import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;


public final class Sound {
    public static String grass = "src/spacegame/assets/sound/stepGrass.ogg";
    public static String sand = "src/spacegame/assets/sound/stepSand.ogg";
    public static String dirt = "src/spacegame/assets/sound/stepDirt.ogg";
    public static String stone = "src/spacegame/assets/sound/stepStone.ogg";
    public static String waterSplash = "src/spacegame/assets/sound/waterSplash.ogg";
    public static String snow = "src/spacegame/assets/sound/stepSnow.ogg";
    public static String wood = "src/spacegame/assets/sound/stepWood.ogg";
    public static String itemPickup = "src/spacegame/assets/sound/itemPickup.ogg";
    public static String fallDamage = "src/spacegame/assets/sound/fallDamage.ogg";
    public String filepath;
    public int bufferID;
    public int sourceID;
    public boolean isPlaying;


    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;
        if (this.filepath == null) {
            return;
        }


        MemoryStack.stackPush();
        IntBuffer channelsBuffer = MemoryStack.stackCallocInt(1);
        MemoryStack.stackPush();
        IntBuffer sampleRateBuffer = MemoryStack.stackCallocInt(1);

        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(this.filepath, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            MemoryStack.stackPop();
            MemoryStack.stackPop();
            return;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        int format = -1;
        if (channels == 1) {
            format = AL11.AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL11.AL_FORMAT_STEREO16;
        }


        this.bufferID = AL11.alGenBuffers();
        AL11.alBufferData(this.bufferID, format, rawAudioBuffer, sampleRate);

        this.sourceID = AL11.alGenSources();
        AL11.alSourcei(this.sourceID, AL11.AL_BUFFER, this.bufferID);
        AL11.alSourcei(this.sourceID, AL11.AL_LOOPING, loops ? 1 : 0);
        AL11.alSourcef(this.sourceID, AL11.AL_GAIN, 0.5F);

        MemoryStack.stackPop();
        MemoryStack.stackPop();
    }
}