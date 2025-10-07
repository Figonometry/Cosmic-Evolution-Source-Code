package spacegame.gui;

import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Tech;

import java.util.ArrayList;

public final class GuiTechTree extends Gui {
    public int eraDisplayed = 1;
    public int background;
    public int techBlock;
    public int line;
    public int treeArea;
    public int transparentBackground;
    public ArrayList<TechBlock> currentTechsToDisplay;
    public ButtonTechTree neolithicButton;
    public ButtonTechTree age2;
    public ButtonTechTree age3;
    public ButtonTechTree age4;
    public ButtonTechTree age5;
    public ButtonTechTree age6;
    public ButtonTechTree age7;
    public ButtonTechTree age8;
    public ButtonTechTree age9;
    public ButtonTechTree age10;
    public ButtonTechTree age11;
    public ButtonTechTree age12;
    public ButtonTechTree age13;
    public static final int baseLayerX = -700;
    public static final int baseLayerY = 400;

    public GuiTechTree(SpaceGame spaceGame) {
        super(spaceGame);
        this.neolithicButton = new ButtonTechTree("Neolithic", 240, 64, -840, -412,  Tech.NEOLITHIC_ERA, this);
        this.age2 = new ButtonTechTree("???", 240, 64, -600, -412,  2, this);
        this.age3 = new ButtonTechTree("???", 240, 64, -360, -412,  3, this);
        this.age4 = new ButtonTechTree("???", 240, 64, -120, -412,  4, this);
        this.age5 = new ButtonTechTree("???", 240, 64, 120, -412,  5, this);
        this.age6 = new ButtonTechTree("???", 240, 64, 360, -412,  6, this);
        this.age7 = new ButtonTechTree("???", 240, 64, 600, -412,  7, this);
        this.age8 = new ButtonTechTree("???", 240, 64, 840, -412,  8, this);
        this.age9 = new ButtonTechTree("???", 384, 64, -768, -476,  9, this);
        this.age10 = new ButtonTechTree("???", 384, 64, -384, -476,  10, this);
        this.age11 = new ButtonTechTree("???", 384, 64, 0, -476,  11, this);
        this.age12 = new ButtonTechTree("???", 384, 64, 384, -476,  12, this);
        this.age13 = new ButtonTechTree("???", 384, 64, 768, -476,  13, this);
        this.age2.active = false;
        this.age3.active = false;
        this.age4.active = false;
        this.age5.active = false;
        this.age6.active = false;
        this.age7.active = false;
        this.age8.active = false;
        this.age9.active = false;
        this.age10.active = false;
        this.age11.active = false;
        this.age12.active = false;
        this.age13.active = false;
    }

    @Override
    public void loadTextures() {
        this.techBlock = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/techBlock.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.line = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/line.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        switch (this.eraDisplayed){
            case Tech.NEOLITHIC_ERA -> {
                this.background = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/neolithicBackGround.png", RenderEngine.TEXTURE_TYPE_2D, 0);
                this.treeArea = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/clayTexture.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            }
        }
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.techBlock);
        SpaceGame.instance.renderEngine.deleteTexture(this.line);
        SpaceGame.instance.renderEngine.deleteTexture(this.transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(this.background);
        SpaceGame.instance.renderEngine.deleteTexture(this.treeArea);
        SpaceGame.instance.renderEngine.deleteTexture(this.age2.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age3.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age4.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age5.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age6.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age7.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age8.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age9.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age10.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age11.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age12.texture);
        SpaceGame.instance.renderEngine.deleteTexture(this.age13.texture);
    }

    private void loadButtonTextures(){
        this.age2.reloadTexture();
        this.age3.reloadTexture();
        this.age4.reloadTexture();
        this.age5.reloadTexture();
        this.age6.reloadTexture();
        this.age7.reloadTexture();
        this.age8.reloadTexture();
        this.age9.reloadTexture();
        this.age10.reloadTexture();
        this.age11.reloadTexture();
        this.age12.reloadTexture();
        this.age13.reloadTexture();
    }

    public void switchEraDisplayed(int newEra){
        this.eraDisplayed = newEra;
        this.deleteTextures();
        switch (this.eraDisplayed){
            case Tech.NEOLITHIC_ERA -> {
                this.loadButtonTextures();
                this.loadTextures();
                this.currentTechsToDisplay = this.createTechBlocks();
            }
        }
    }



    private ArrayList<TechBlock> createTechBlocks(){
        ArrayList<Tech> techs = Tech.getEraTechList(this.eraDisplayed);
        ArrayList<TechBlock> techBlockList = new ArrayList<>();

        final int startingX = -700;
        final int startingY = 0;
        final int width = 256;
        final int height = 128;
        final int heightOffset = (int) (192 * 1.5f);
        final int widthOffset = (int) (400 * 1.5f);
        int x = 0;
        int y = 0;
        //Place root of the era Tree
        for(int i = 0; i < techs.size(); i++){
            if(!techs.get(i).isRootNode)continue;
            techBlockList.add(new TechBlock(techs.get(i), startingX, startingY, width, height, this));
            techs.remove(i);
            techs.trimToSize();
            break;
        }

        //Breadth first search to place each child node in the tree relative to the parent node, the default for a tech is to place it on the same y level as the parent node
        //if there are additional child nodes they will be placed by checking first above then below the first child node, moving outwards based on if those positions are occupied
        //Nodes must be checked if they already exist prior to placing new ones due to some child nodes having multiple parents
        for(int j = 0; j < techBlockList.size(); j++) {
            TechBlock rootNode = techBlockList.get(j);
            ArrayList<String> childNodeNames = rootNode.tech.unlockedTech;
            Tech tech;
            for (int i = 0; i < childNodeNames.size(); i++) {
                tech = this.getTechFromEraList(techs, childNodeNames.get(i));
                if (this.isTechAlreadyInList(techBlockList, tech.techName)) continue;

                if (!this.isPositionOccupiedByExistingTech(rootNode.x + widthOffset, rootNode.y, techBlockList)) {
                    techBlockList.add(new TechBlock(tech, rootNode.x + widthOffset, rootNode.y, width, height, this));
                } else {
                    int timesChecked = 1;
                    while (true) { //Forbidden code, I should probably find a better way to do this but it works
                        y = heightOffset * timesChecked;
                        if (!this.isPositionOccupiedByExistingTech(rootNode.x + widthOffset, rootNode.y + y, techBlockList)) {
                            techBlockList.add(new TechBlock(tech, rootNode.x + widthOffset, rootNode.y + y, width, height, this));
                            break;
                        }
                        y = -heightOffset * timesChecked;
                        if (!this.isPositionOccupiedByExistingTech(rootNode.x + widthOffset, rootNode.y + y, techBlockList)) {
                            techBlockList.add(new TechBlock(tech, rootNode.x + widthOffset, rootNode.y + y, width, height, this));
                            break;
                        }
                        timesChecked++;
                    }
                }
            }
        }


        return techBlockList;
    }

    private Tech getTechFromEraList(ArrayList<Tech> list, String name){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).techName.equals(name)){
                return list.get(i);
            }
        }
        throw new RuntimeException("Error at Tech name lookup tool in: " + name);
    }

    private boolean isPositionOccupiedByExistingTech(int x, int y, ArrayList<TechBlock> list){
        TechBlock block;
        for(int i = 0; i < list.size(); i++){
            block = list.get(i);
            if(block.x == x && block.y == y){
                return true;
            }
        }
        return false;
    }

    private boolean isTechAlreadyInList(ArrayList<TechBlock> list, String techName){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).tech.techName.equals(techName)){
                return true;
            }
        }
        return false;
    }

    public TechBlock getTechBlockFromName(String name, ArrayList<TechBlock> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).tech.techName.equals(name)){
                return list.get(i);
            }
        }
        throw new RuntimeException("Error at Techblock Name Lookup Function");
    }


    @Override
    public void drawGui() {
        switch (this.eraDisplayed){
            case Tech.NEOLITHIC_ERA -> {
                this.drawNeolithicTechTreeBackgroundElements();
            }
        }

        this.neolithicButton.renderButton();
        this.age2.renderButton();
        this.age3.renderButton();
        this.age4.renderButton();
        this.age5.renderButton();
        this.age6.renderButton();
        this.age7.renderButton();
        this.age8.renderButton();
        this.age9.renderButton();
        this.age10.renderButton();
        this.age11.renderButton();
        this.age12.renderButton();
        this.age13.renderButton();

        TechBlock techBlock = null;
        TechBlock hoveredBlock = null;
        for(int i = 0; i < this.currentTechsToDisplay.size(); i++){
            techBlock = this.currentTechsToDisplay.get(i);
            techBlock.renderTechBlock();
            if(techBlock.isMouseHoveredOver()){
                hoveredBlock = techBlock; //The hovered block needs to be assigned here so it can be drawn after the rest of the tree renders, sometime I hate OpenGL
            }
        }

        if(hoveredBlock != null) {
            hoveredBlock.drawInfoBox();
        }
    }

    private void drawNeolithicTechTreeBackgroundElements(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int backgroundWidth = 1920;
        int backgroundHeight = 1017;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -90;
        tessellator.addVertex2DTexture(16777215, backgroundX - backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.background, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        backgroundY = 75;
        backgroundHeight = 800;
        backgroundZ = -100;
        tessellator.addVertex2DTexture(16777215, backgroundX - backgroundWidth, backgroundY - backgroundHeight, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + backgroundWidth, backgroundY + backgroundHeight, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - backgroundWidth, backgroundY + backgroundHeight, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + backgroundWidth, backgroundY - backgroundHeight, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.treeArea, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();


    }

    @Override
    public Button getActiveButton() {
        if(this.neolithicButton.active && this.neolithicButton.isMouseHoveredOver()){
            return this.neolithicButton;
        }
        if(this.age2.active && this.age2.isMouseHoveredOver()){
            return this.age2;
        }
        if(this.age3.active && this.age3.isMouseHoveredOver()){
            return this.age3;
        }
        if(this.age4.active && this.age4.isMouseHoveredOver()){
            return this.age4;
        }
        if(this.age5.active && this.age5.isMouseHoveredOver()){
            return this.age5;
        }
        if(this.age6.active && this.age6.isMouseHoveredOver()){
            return this.age6;
        }
        if(this.age7.active && this.age7.isMouseHoveredOver()){
            return this.age7;
        }
        if(this.age8.active && this.age8.isMouseHoveredOver()){
            return this.age8;
        }
        if(this.age9.active && this.age9.isMouseHoveredOver()){
            return this.age9;
        }
        if(this.age10.active && this.age10.isMouseHoveredOver()){
            return this.age10;
        }
        if(this.age11.active && this.age11.isMouseHoveredOver()){
            return this.age11;
        }
        if(this.age12.active && this.age12.isMouseHoveredOver()){
            return this.age12;
        }
        if(this.age13.active && this.age13.isMouseHoveredOver()){
            return this.age13;
        }
        return null;
    }

    public void moveTechBlocks(float deltaX, float deltaY){
        for(int i = 0; i < this.currentTechsToDisplay.size(); i++){
            this.currentTechsToDisplay.get(i).movePosition(-deltaX, -deltaY);
        }
    }

    public TechBlock getHoveredTechBlock(){
        TechBlock returnBlock;
        for(int i = 0; i < this.currentTechsToDisplay.size(); i++){
            returnBlock = this.currentTechsToDisplay.get(i);
            if(returnBlock.isMouseHoveredOver()){
                return  returnBlock;
            }
        }
        return null;
    }

    public void zoomTechBlocks(float zoom) {
        TechBlock.adjustZoom(zoom == 1);
    }
}
