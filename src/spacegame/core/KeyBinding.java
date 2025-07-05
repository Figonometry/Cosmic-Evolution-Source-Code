package spacegame.core;
public final class KeyBinding {
    public String name;
    public String key;
    public int keyCode;

    public KeyBinding(String name, String defaultKey, int defaultKeyCode){
        this.name = name;
        this.key = defaultKey;
        this.keyCode = defaultKeyCode;
    }
}
