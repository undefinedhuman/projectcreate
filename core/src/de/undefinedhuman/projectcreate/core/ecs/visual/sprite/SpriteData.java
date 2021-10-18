package de.undefinedhuman.projectcreate.core.ecs.visual.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.base.Transform;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class SpriteData {

    private Sprite sprite;
    private float alpha = 1;
    private Color color = Color.WHITE;
    private int renderLevel;
    private Vector2 positionOffset = new Vector2();
    public boolean isVisible, turned = true;
    private TextureRegion[] textures;

    public SpriteData(String textureName, int frameCount, int renderLevel, boolean defaultVisibility) {
        this.sprite = new Sprite();
        this.renderLevel = renderLevel;
        this.isVisible = defaultVisibility;
        setTexture(textureName, frameCount);
    }

    public void setTexture(String textureName) {
        setTexture(textureName, 1);
    }

    public void setTexture(String textureName, int frameCount) {
        TextureRegion texture = TextureManager.getInstance().getTexture(textureName);
        this.textures = texture.split(texture.getRegionWidth() / frameCount, texture.getRegionHeight())[0];
        setFrameIndex(0);
    }

    public void setFrameIndex(int index) {
        if(!Utils.isInRange(index, 0, textures.length-1))
            return;
        sprite.setRegion(textures[index]);
    }

    public void setOrigin(Vector2 origin) {
        setOrigin(origin.x, origin.y);
    }
    public void setOrigin(float originX, float originY) {
        this.sprite.setOrigin((int) originX, (int) originY);
    }
    public void setRotation(float rotation) {
        this.sprite.setRotation(rotation);
    }
    public void setVisible(boolean visible) { this.isVisible = visible; }
    public void setSize(Vector2 size) { setSize(size.x, size.y); }
    public void setSize(float width, float height) { this.sprite.setSize(width, height); }
    public void setTurned(boolean turned) {
        this.turned = turned;
    }
    public void setPositionOffset(float x, float y) {
        this.positionOffset.set(x, y);
    }

    public void setPositionOffset(Vector2 offset) {
        this.setPositionOffset(offset.x, offset.y);
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Sprite getSprite() {
        return sprite;
    }
    public Color getColor() {
        return color;
    }
    public int getRenderLevel() {
        return renderLevel;
    }

    public void render(SpriteBatch batch, Transform transform, int renderOffset) {
        if(!isVisible)
            return;
        sprite.setFlip(!turned, false);
        if(sprite.getWidth() == 0 || sprite.getHeight() == 0)
            sprite.setSize(transform.getWidth(), transform.getHeight());
        sprite.setPosition(transform.getX() + positionOffset.x + renderOffset, transform.getY() + positionOffset.y);
        sprite.draw(batch, alpha);
    }

    public void delete() {
        textures = new TextureRegion[0];
    }

}
