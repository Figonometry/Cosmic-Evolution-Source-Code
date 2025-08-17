package spacegame.render;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;


public final class TextureAtlas {
    public List<Texture> textures;



    public TextureAtlas(int imageWidth, int imageHeight, int texWidth, int texHeight, int numTex, int spacing) {
        this.textures = new ArrayList<>();
        int currentX = 0;
        int currentY = imageHeight - texHeight;

        for (int i = 0; i < numTex; i++) {
            float topY = (currentY + texHeight) / (float) imageHeight;
            float rightX = (currentX + texWidth) / (float) imageHeight;
            float leftX = currentX / (float) imageHeight;
            float bottomY = currentY / (float) imageHeight;

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Texture texture = new Texture(texCoords);
            this.textures.add(texture);

            currentX += texWidth + spacing;
            if (currentX >= imageWidth) {
                currentX = 0;
                currentY -= texHeight + spacing;
            }
        }
    }

    public Texture getTexture(int index) {
        return this.textures.get(index);
    }
}
