package spacegame.item;

public final class ItemCraftingTemplates {
    public static final ItemCraftingTemplates stoneHandAxe = new ItemCraftingTemplates(new short[]{10,11,12,13,18,19,20,21,26,27,28,29,34,35,36,37,41,42,43,44,45,46,49,50,51,52,53,54,57,58,59,60,61,62});
    public static final ItemCraftingTemplates knifeBlade = new ItemCraftingTemplates(new short[]{3,4,5,11,12,13,19,20,21,27,28,29,35,36,43,44,51,59});
    public static final ItemCraftingTemplates stoneHandShovel = new ItemCraftingTemplates(new short[]{9,10,11,12,13,14,17,18,19,20,21,22,25,26,27,28,29,30,33,34,35,36,37,38,41,42,43,44,45,46,49,50,51,52,53,54,58,59,60,61});
    public static final ItemCraftingTemplates stoneFragments = new ItemCraftingTemplates(new short[]{12,41,54});
    public static final ItemCraftingTemplates brick = new ItemCraftingTemplates(new short[]{19,20,27,28,35,36,43,44});
    public static final ItemCraftingTemplates cookingPot = new ItemCraftingTemplates(new short[]{0,1,2,3,4,5,6,7,8,15,16,23,24,31,32,39,40,47,48,55,56,57,58,59,60,61,62,63});
    public short[] indices;
    public ItemCraftingTemplates(short[] indices){
        this.indices = indices;
    }


}
