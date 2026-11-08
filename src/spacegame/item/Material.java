package spacegame.item;

public final class Material {
    public static final Material RAW_STONE = new Material(0.1f,16f);
    public static final Material STONE = new Material(0.15f, 64f);
    public static final Material METALLIC_COPPER = new Material(0.2f, 128f);
    public float hardnessValue;
    public float durabilityModifier;
    public Material(float hardnessValue, float durabilityModifier){
        this.hardnessValue = hardnessValue;
        this.durabilityModifier = durabilityModifier;
    }

}
