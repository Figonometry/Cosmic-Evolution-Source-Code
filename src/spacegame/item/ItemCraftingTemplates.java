package spacegame.item;

public final class ItemCraftingTemplates {
    public static final ItemCraftingTemplates stoneHandAxe = new ItemCraftingTemplates(new short[]{10,11,12,13,18,19,20,21,26,27,28,29,34,35,36,37,41,42,43,44,45,46,49,50,51,52,53,54,57,58,59,60,61,62});
    public static final ItemCraftingTemplates knifeBlade = new ItemCraftingTemplates(new short[]{3,4,5,11,12,13,19,20,21,27,28,29,35,36,43,44,51,59});
    public static final ItemCraftingTemplates stoneHandShovel = new ItemCraftingTemplates(new short[]{9,10,11,12,13,14,17,18,19,20,21,22,25,26,27,28,29,30,33,34,35,36,37,38,41,42,43,44,45,46,49,50,51,52,53,54,58,59,60,61});
    public static final ItemCraftingTemplates stoneFragments = new ItemCraftingTemplates(new short[]{12,41,54});
    public static final ItemCraftingTemplates unlitTorch = new ItemCraftingTemplates(new short[]{173,174,175,176,205,206,207,208,237,238,239,240,269,270,271,272,301,302,303,304,333,334,335,336,365,366,367,368,397,398,399,400,429,430,431,432,461,462,463,464,493,494,495,496,525,526,527,528,557,558,559,560,589,590,591,592,621,622,623,624,653,654,655,656,685,686,687,688,717,718,719,720,749,750,751,752,781,782,783,784,813,814,815,816,845,846,847,848,877,878,879,880});
    public short[] indices;
    public ItemCraftingTemplates(short[] indices){
        this.indices = indices;
    }


}
