package spacegame.core;

import org.lwjgl.openal.AL11;

import java.util.ArrayList;

public final class SoundPlayer {
    public CosmicEvolution ce;
    public static final double MAX_SOUND_DISTANCE = 32D;
    private static ArrayList<Sound> sounds = new ArrayList<Sound>();

    public SoundPlayer(CosmicEvolution cosmicEvolution){
        this.ce = cosmicEvolution;
    }


    public static void checkAllSoundStates(){
        for(int i = 0; i < sounds.size(); i++){
            checkSoundState(sounds.get(i));
        }
    }

    private static void checkSoundState(Sound sound){
        if(AL11.alGetSourcei(sound.sourceID, AL11.AL_SOURCE_STATE) != AL11.AL_PLAYING){
            AL11.alDeleteSources(sound.sourceID);
            AL11.alDeleteBuffers(sound.bufferID);
            sounds.remove(sound);
        }
        if(sounds.isEmpty()){
            sounds.trimToSize();
        }
    }

    public void playSound(double x, double y, double z, Sound sound, float pitch) {
        if (sound == null) {return;}
        //AL11.alSourcef(sound.sourceID, AL11.AL_GAIN, 3);
        AL11.alSourcef(sound.sourceID, AL11.AL_GAIN, calculateGain(distanceFromCameraToSound(x,y,z), sound.soundMultiplier));
        AL11.alSourcef(sound.sourceID, AL11.AL_PITCH, pitch);
        AL11.alSourcePlay(sound.sourceID);
        sounds.add(sound);
    }

    public void stopSound(Sound sound){
        for(int i = 0; i < sounds.size(); i++){
            if(sound.equals(sounds.get(i))){
                AL11.alSourceStop(sound.sourceID);
                AL11.alDeleteSources(sound.sourceID);
                AL11.alDeleteBuffers(sound.bufferID);
                sounds.remove(sound);
            }
        }
    }

    public void stopSound(String soundString){
        Sound sound;
        for(int i = 0; i < sounds.size(); i++){
            sound = sounds.get(i);
            if(sound.filepath.equals(soundString)){
                AL11.alSourceStop(sound.sourceID);
                AL11.alDeleteSources(sound.sourceID);
                AL11.alDeleteBuffers(sound.bufferID);
                sounds.remove(sound);
            }
        }
    }

    public float distanceFromCameraToSound(double x, double y, double z){
        float xDif;
        float yDif;
        float zDif;
        if(this.ce.save != null) {
             xDif = (float) (this.ce.save.thePlayer.x - x);
             yDif = (float) (this.ce.save.thePlayer.y - y);
             zDif = (float) (this.ce.save.thePlayer.z - z);
        } else {
            xDif = 0;
            yDif = 0;
            zDif = 0;
        }
        return (float)Math.sqrt(xDif * xDif + zDif * zDif + yDif * yDif);
    }

    private static float calculateGain(float distanceFromPlayer, float soundMultiplier){
        if(1 - distanceFromPlayer/MAX_SOUND_DISTANCE > 0){
            return (float) (((1 - distanceFromPlayer/MAX_SOUND_DISTANCE)/4) * (GameSettings.volume * soundMultiplier));
        } else {
            return 0;
        }
    }

}
