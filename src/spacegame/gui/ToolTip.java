package spacegame.gui;

import spacegame.render.texturelists.MouseAndKeyIconTextureList;

import java.util.ArrayList;

public final class ToolTip {
    public static final int FONT_ATLAS = 0;
    public static final int TEXT_BOX_ATLAS = 1;
    public static final int BLOCK_ARRAY = 2;
    public static final int ITEM_ARRAY = 3;
    public static final int MOUSE_ICON_ATLAS = 4;
    public ArrayList<Integer> tooltip = new ArrayList<>(); //The integers are in pairs with the constants above being used to determine the image used and the latter int being the index in the texture
    public int index = 0;

    //Determine the indices used by each char using the existing logic in FontRenderer
    public void addText(String text){
        int[] charIcons = FontRenderer.instance.convertStringToAtlasInts(text);

        for(int i = 0; i < charIcons.length; i++){
            this.tooltip.add(FONT_ATLAS);
            this.tooltip.add(charIcons[i]);
        }
    }

    //Pass the constant passed in for the mouse icon and append to the tooltip
    public void addMouseIcon(int mouseIcon){
        this.tooltip.add(MOUSE_ICON_ATLAS);
        this.tooltip.add(mouseIcon);
    }

    //Convert the string for the keyboard key into the indices for the char array then add the appropriate box outline for each type
    //For each char append the appropriate box type, the renderer will not move the x position when drawing the boxes but will move when drawing the mouse icon

    //The int order would be FONT_ATLAS, index, TEXT_BOX_ATLAS, index
    public void addKeyWithBoxOutline(String text){
        int[] charIcons = FontRenderer.instance.convertStringToAtlasInts(text);

        if(charIcons.length == 1){
            this.tooltip.add(FONT_ATLAS);
            this.tooltip.add(charIcons[0]);

            this.tooltip.add(TEXT_BOX_ATLAS);
            this.tooltip.add(MouseAndKeyIconTextureList.FULL_BOUND_BOX);
        } else {
            for(int i = 0; i < charIcons.length; i++){
                this.tooltip.add(FONT_ATLAS);
                this.tooltip.add(charIcons[i]);

                this.tooltip.add(TEXT_BOX_ATLAS);
                this.tooltip.add(i == 0 ? MouseAndKeyIconTextureList.BOUND_BOX_LEFT : i == charIcons.length - 1 ? MouseAndKeyIconTextureList.BOUND_BOX_RIGHT :
                        MouseAndKeyIconTextureList.BOUND_BOX_MIDDLE);
            }
        }
    }

    //This should add the blockID for now, it will be converted to the block model during the vertex assembly
    public void addBlockID(short blockID){
        this.tooltip.add(BLOCK_ARRAY);
        this.tooltip.add((int) blockID);
    }

    //This should add the itemID for now, it will be converted to the item model during the vertex assembly
    public void addItemID(short itemID){
        this.tooltip.add(ITEM_ARRAY);
        this.tooltip.add((int) itemID);
    }




}
