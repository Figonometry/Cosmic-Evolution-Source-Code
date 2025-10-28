package spacegame.gui;

import spacegame.core.CosmicEvolution;
import spacegame.core.MathUtil;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;

public final class RecipeSelector {
    public short itemID;
    public short blockID;
    public float x;
    public float y;
    public float width;
    public float height;
    public String displayName;
    public boolean isBlock;
    public short[] requiredItems;
    public int[] requiredItemCount;
    public short[] template;


    public RecipeSelector(short itemID, float x, float y, float width, float height, String displayName, short[] requiredItems, int[] requiredItemCount, short[] template){
        this.itemID = itemID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayName = displayName;
        this.requiredItems = requiredItems;
        this.requiredItemCount = requiredItemCount;
        this.template = template;
    }

    public RecipeSelector(short itemID, short blockTextureID, float x, float y, float width, float height, String displayName, short[] requiredItems, int[] requiredItemCount, short[] template){
        this.itemID = itemID;
        this.blockID = blockTextureID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayName = displayName;
        this.requiredItems = requiredItems;
        this.requiredItemCount = requiredItemCount;
        this.template = template;
        this.isBlock = true;
    }

    public boolean isMouseHoveredOver(){
        double x = MouseListener.instance.xPos - CosmicEvolution.width/2D;
        double y = (MouseListener.instance.yPos - CosmicEvolution.height/2D) * -1;
        float adjustedButtonX = MathUtil.adjustXPosBasedOnScreenWidth(this.x);
        float adjustedButtonY = MathUtil.adjustYPosBasedOnScreenHeight(this.y);
        float adjustedButtonWidth = MathUtil.adjustWidthBasedOnScreenWidth(this.width);
        float adjustedButtonHeight = MathUtil.adjustHeightBasedOnScreenHeight(this.height);
        return x > adjustedButtonX - (double) adjustedButtonWidth / 2 && x < adjustedButtonX + (double) adjustedButtonWidth / 2 && y > adjustedButtonY - (double) adjustedButtonHeight / 2 && y < adjustedButtonY + (double) adjustedButtonHeight / 2;
    }

    public boolean meetsCriteriaToMakeRecipe(EntityPlayer player){
        for(int i = 0; i < this.requiredItemCount.length; i++){
            if(!player.containsAmountOfItem(this.requiredItems[i], this.requiredItemCount[i]))return false;
        }
        return true;
    }


    public boolean isMaterialRequired(int index){
        return this.isIndexSupposedToBeFilled(index);
    }

    private boolean isIndexSupposedToBeFilled(int testedIndex){
        for(int i = 0; i < this.template.length; i++){
            if(testedIndex == this.template[i]){
                return true;
            }
        }
        return false;
    }


}
