package spacegame.item;

public final class Material {
    public static final Material RAW_STONE = new Material(0.1f,4);
    public float hardnessValue;
    public float durabilityModifier;
    public Material(float hardnessValue, float durabilityModifier){
        this.hardnessValue = hardnessValue;
        this.durabilityModifier = durabilityModifier;
    }

}
