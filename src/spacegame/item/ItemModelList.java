package spacegame.item;

import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;

//This class stores the base models for mutli model items
public abstract class ItemModelList {
    public static String modelFolderPath = "src/spacegame/assets/models/itemModels/";
    //Technically this isnt true but the texture replacement is done at runtime and is dynamic
    public static final ModelLoader woodenPanFilled = new ModelLoader(modelFolderPath + "filledWoodenPan.obj", true);


    //These are all the base stone tools, the variants are constructed from these
    public static final ModelLoader stoneHoeHead = new ModelLoader(modelFolderPath + "stoneHoeHead.obj", true);
    public static final ModelLoader stoneHoe = new ModelLoader(modelFolderPath + "stoneHoe.obj", true);
    public static final ModelLoader stoneFragments = new ModelLoader(modelFolderPath + "stoneFragments.obj", true);
    public static final ModelLoader stoneHandAxe = new ModelLoader(modelFolderPath + "stoneHandAxe.obj", true);
    public static final ModelLoader stoneHandKnifeBlade = new ModelLoader(modelFolderPath + "stoneKnifeBlade.obj", true);
    public static final ModelLoader stoneHandShovel = new ModelLoader(modelFolderPath + "stoneHandShovel.obj", true);
    public static final ModelLoader stoneAxe = new ModelLoader(modelFolderPath + "stoneAxe.obj", true);
    public static final ModelLoader stoneKnife = new ModelLoader(modelFolderPath + "stoneKnife.obj", true);
    public static final ModelLoader stoneShovel = new ModelLoader(modelFolderPath + "stoneShovel.obj", true);
    public static final ModelLoader stoneSpearHead = new ModelLoader(modelFolderPath + "stoneSpearHead.obj", true);
    public static final ModelLoader stoneSpear = new ModelLoader(modelFolderPath + "stoneSpear.obj", true);

    //Serves as the base for the ore chunks, all ore chunks are copied from these models
    public static final ModelLoader andesitePoorCopperOreChunks = new ModelLoader(modelFolderPath + "copperChunksPoor.obj", true);
    public static final ModelLoader andesiteMediumCopperOreChunks = new ModelLoader(modelFolderPath + "copperChunksMedium.obj", true);
    public static final ModelLoader andesiteRichCopperOreChunks = new ModelLoader(modelFolderPath  + "copperChunksHigh.obj", true);
    public static final ModelLoader andesiteBountifulCopperOreChunks = new ModelLoader(modelFolderPath + "copperChunksBountiful.obj", true);

}
