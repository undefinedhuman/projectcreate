package de.undefinedhuman.sandboxgame.engine.resources.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureValue {

    public boolean remove = false;
    private int usages = 1;
    private TextureRegion texture;

    public TextureValue(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.texture = new TextureRegion(texture);
    }

    public void add() {
        usages++;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void remove() {
        usages--;
        if (--usages > 0) return;
        delete();
        remove = true;
    }

    public void delete() {
        texture.getTexture().dispose();
    }

}
