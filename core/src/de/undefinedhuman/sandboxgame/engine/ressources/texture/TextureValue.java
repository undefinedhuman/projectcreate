package de.undefinedhuman.sandboxgame.engine.ressources.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class TextureValue {

    public boolean remove = false;
    private int usages = 1;
    private TextureRegion texture;

    public TextureValue(Texture texture) {
        this.texture = Tools.fixBleeding(new TextureRegion(texture));
    }

    public void add() {
        usages++;
    }

    public TextureRegion get() {
        return texture;
    }

    public void remove() {
        usages--;
        if (usages <= 0) {
            delete();
            remove = true;
        }
    }

    public void delete() {
        texture.getTexture().dispose();
    }

}
