package spacegame.render;

import org.joml.Vector2f;


public final class Texture {

    public TextureLoader textureLoader;
    public Vector2f[] texCoords;

    public Texture(TextureLoader textureLoader) {
        this.textureLoader = textureLoader;
        Vector2f[] texCoords = {
                new Vector2f(1, 1), //Bottom Left
                new Vector2f(1, 0), //Top Left
                new Vector2f(0, 0), //Top Right
                new Vector2f(0, 1) //Bottom Right
        };
        this.texCoords = texCoords;
    }

    public Texture(TextureLoader textureLoader, Vector2f[] texCoords) {
        this.textureLoader = textureLoader;
        this.texCoords = texCoords;
    }

    public TextureLoader getTexture() {
        return this.textureLoader;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

}
