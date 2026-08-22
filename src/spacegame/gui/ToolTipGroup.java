package spacegame.gui;

import java.util.ArrayList;

public final class ToolTipGroup {
    public static long altToolTipIndex; //Updated from the timer
    public ArrayList<ToolTip> toolTips = new ArrayList<>();



    public void addToolTip(ToolTip toolTip){
        this.toolTips.add(toolTip);
    }


    //Grabs the current tooltip from the full list of tooltips based on the index that updates once per second
    public ToolTip getCurrentTooltip(){
        return this.toolTips.get((int) (altToolTipIndex & (this.toolTips.size() - 1)));
    }
}
