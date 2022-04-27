package de.undefinedhuman.projectcreate.engine.resources.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureValue {

    public boolean remove = false;

    private int usages = 1;
    private Texture texture;
    private TextureRegion textureRegion;
    private Pixmap pixmap;

    public TextureValue(Texture texture) {
        if(texture == null)
            return;
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.texture = texture;
        this.textureRegion = new TextureRegion(texture);
        if(!texture.getTextureData().isPrepared())
            texture.getTextureData().prepare();
        pixmap = texture.getTextureData().consumePixmap();
        pixmap.setFilter(Pixmap.Filter.BiLinear);
        pixmap.setBlending(Pixmap.Blending.None);
    }

    public void add() {
        usages++;
    }

    public Texture getTexture() {
        return texture;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public void remove() {
        usages--;
        if (usages > 0) return;
        remove = true;
    }

    public void delete() {
        if(!pixmap.isDisposed())
            pixmap.dispose();
        texture.dispose();
    }

}
