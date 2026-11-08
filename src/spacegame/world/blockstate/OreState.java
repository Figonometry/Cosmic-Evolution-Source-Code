package spacegame.world.blockstate;

public final class OreState extends BlockState {
    //Conversion from the ore and richness to the dropped item will be handled by BlockOre
    public static final int RICHNESS_POOR = 0;
    public static final int RICHNESS_MEDIUM = 1;
    public static final int RICHNESS_HIGH = 2;
    public static final int RICHNESS_BOUNTIFUL = 3;
    public static final int ORE_NATIVE_COPPER = 0;
    private final short parentStoneBlockID;
    private final int oreRichness;
    private final int ore;
    private final int index;

    public OreState(int index, int ore, int oreRichness, short parentStoneBlockID){
        this.index = index;
        this.ore = ore;
        this.oreRichness = oreRichness;
        this.parentStoneBlockID = parentStoneBlockID;
    }

    public short getParentStoneBlockID(){
        return this.parentStoneBlockID;
    }

    public int getOre(){
        return this.ore;
    }

    public int getOreRichness(){
        return this.oreRichness;
    }

    public int getIndex(){
        return this.index;
    }


}
