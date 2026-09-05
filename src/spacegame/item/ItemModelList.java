package spacegame.item;

import spacegame.render.model.ModelLoader;
import spacegame.render.texturelists.ItemTextureList;

public abstract class ItemModelList {
    public static String modelFolderPath = "src/spacegame/assets/models/itemModels/";
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

    public static final ModelLoader andesiteStoneHoeHead = stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneHoe = stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneFragments = stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneHandAxe = stoneHandAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneKnifeBlade = stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneHandShovel = stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneAxe = stoneAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneKnife = stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneShovel = stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneSpearHead = stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);
    public static final ModelLoader andesiteStoneSpear = stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.ANDESITE_TEXTURE);

    public static final ModelLoader graniteStoneHoeHead = stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneHoe = stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneFragments = stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneHandAxe = stoneHandAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneKnifeBlade = stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneHandShovel = stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneAxe = stoneAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneKnife = stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneShovel = stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneSpearHead = stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);
    public static final ModelLoader graniteStoneSpear = stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.GRANITE_TEXTURE);

    public static final ModelLoader basaltStoneHoeHead = stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneHoe = stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneFragments = stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneHandAxe = stoneHandAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneKnifeBlade = stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneHandShovel = stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneAxe = stoneAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneKnife = stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneShovel = stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneSpearHead = stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);
    public static final ModelLoader basaltStoneSpear = stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.BASALT_TEXTURE);

    public static final ModelLoader chertStoneHoeHead = stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneHoe = stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneFragments = stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneHandAxe = stoneHandAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneKnifeBlade = stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneHandShovel = stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneAxe = stoneAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneKnife = stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneShovel = stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneSpearHead = stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);
    public static final ModelLoader chertStoneSpear = stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.CHERT_TEXTURE);

    public static final ModelLoader obsidianStoneHoeHead = stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneHoe = stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneFragments = stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneHandAxe = stoneHandAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneKnifeBlade = stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneHandShovel = stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneAxe = stoneAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneKnife = stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneShovel = stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneSpearHead = stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);
    public static final ModelLoader obsidianStoneSpear = stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.OBSIDIAN_TEXTURE);

    public static final ModelLoader flintStoneHoeHead = stoneHoeHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneHoe = stoneHoe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneFragments = stoneFragments.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneHandAxe = stoneHandAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneKnifeBlade = stoneHandKnifeBlade.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneHandShovel = stoneHandShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneAxe = stoneAxe.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneKnife = stoneKnife.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneShovel = stoneShovel.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneSpearHead = stoneSpearHead.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
    public static final ModelLoader flintStoneSpear = stoneSpear.copyModel().replaceTargetTexture(ItemTextureList.STONE_TEXTURE, ItemTextureList.FLINT_TEXTURE);
}
