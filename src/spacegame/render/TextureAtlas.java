package spacegame.render;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;


public final class TextureAtlas {
    public TextureLoader textureLoader;
    public List<Texture> textures;


    public TextureAtlas(TextureLoader textureLoader, int texWidth, int texHeight, int numTex, int spacing) {
        this.textures = new ArrayList<>();
        this.textureLoader = textureLoader;
        int currentX = 0;
        int currentY = this.textureLoader.height - texHeight;

        for (int i = 0; i < numTex; i++) {
            float topY = (currentY + texHeight) / (float) this.textureLoader.height;
            float rightX = (currentX + texWidth) / (float) this.textureLoader.height;
            float leftX = currentX / (float) this.textureLoader.height;
            float bottomY = currentY / (float) this.textureLoader.height;

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Texture texture = new Texture(this.textureLoader, texCoords);
            this.textures.add(texture);

            currentX += texWidth + spacing;
            if (currentX >= this.textureLoader.width) {
                currentX = 0;
                currentY -= texHeight + spacing;
            }
        }
    }

    public Texture getTexture(int index) {
        return this.textures.get(index);
    }
}
