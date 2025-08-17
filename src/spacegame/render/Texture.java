package spacegame.render;

import org.joml.Vector2f;


public final class Texture {

    public Vector2f[] texCoords;


    public Texture(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }


    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

}
