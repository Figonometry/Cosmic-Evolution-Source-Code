package spacegame.item;

public final class ItemCraftingTemplates {
    public static final ItemCraftingTemplates stoneHandAxe = new ItemCraftingTemplates(new short[]{10,11,19,27,28,35,36,43,44,45,51,52,55}, 2, 2, 0, 1);
    public static final ItemCraftingTemplates unlitTorch = new ItemCraftingTemplates(new short[]{173,174,175,176,205,206,207,208,237,238,239,240,269,270,271,272,301,302,303,304,333,334,335,336,365,366,367,368,397,398,399,400,429,430,431,432,461,462,463,464,493,494,495,496,525,526,527,528,557,558,559,560,589,590,591,592,621,622,623,624,653,654,655,656,685,686,687,688,717,718,719,720,749,750,751,752,781,782,783,784,813,814,815,816,845,846,847,848,877,878,879,880},0,0,0,0);
    public short[] indices;
    public int translateLeftLimit;
    public int translateRightLimit;
    public int translateUpLimit;
    public int translateDownLimit;
    public int translationCount;
    public ItemCraftingTemplates(short[] indices, int translateLeftLimit, int translateRightLimit, int translateUpLimit, int translateDownLimit){
        this.indices = indices;
        this.translateLeftLimit = translateLeftLimit;
        this.translateRightLimit = translateRightLimit;
        this.translateUpLimit = translateUpLimit;
        this.translateDownLimit = translateDownLimit;
        int verticalVariation = translateDownLimit + translateUpLimit;
        int horizontalVariation = translateLeftLimit + translateRightLimit;
        this.translationCount = (horizontalVariation * (verticalVariation + 1)) + verticalVariation;
    }

    public short[] translate(int x, int y, short[] originalArray){
        short[] array = new short[originalArray.length];
        for(int i = 0; i < array.length; i++){
            array[i] = originalArray[i];
        }
        int translateY = y * 8;
        for(int i = 0; i < array.length; i++){
            array[i] += x;
            array[i] += translateY;
        }

        return array;
    }
}
