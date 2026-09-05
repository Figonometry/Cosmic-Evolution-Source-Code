package spacegame.item;

import spacegame.render.model.ModelLoader;

public interface IMultiModel {
    ModelLoader getModelLoaderFromItemMetadata(short itemMetadata);
}
