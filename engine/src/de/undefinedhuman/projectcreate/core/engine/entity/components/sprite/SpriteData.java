package de.undefinedhuman.projectcreate.core.engine.entity.components.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.engine.base.GameObject;
import de.undefinedhuman.projectcreate.core.engine.resources.texture.TextureManager;

public class SpriteData {

    public GameObject entity;

    private Sprite sprite;
    private float alpha = 1;
    private Color color = Color.WHITE;
    private int renderLevel, currentFrameID;
    private Vector2 positionOffset = new Vector2();
    private boolean isVisible = true, turned = true;
    private TextureRegion[] textures;

    public SpriteData(String textureName, int frameCount, int renderLevel) {
        this.sprite = new Sprite();
        this.renderLevel = renderLevel;
        setTexture(textureName, frameCount);
    }

    public void init(GameObject entity, Vector2 size) {
        this.entity = entity;
        sprite.setSize(size.x, size.y);
    }

    public void setTexture(String textureName) {
        setTexture(textureName, 1);
    }

    public void setTexture(String textureName, int frameCount) {
        TextureRegion texture = TextureManager.instance.getTexture(textureName);
        this.textures = texture.split(texture.getRegionWidth() / frameCount, texture.getRegionHeight())[0];
        setFrameIndex(0);
    }

    public void setFrameIndex(int index) {
        if(index < textures.length) currentFrameID = index;
        else currentFrameID = textures.length-1;
        currentFrameID = Math.max(currentFrameID, 0);
        sprite.setRegion(textures[currentFrameID]);
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
    public void setSize(Vector2 size) { this.sprite.setSize(size.x, size.y); }
    public void setTurned(boolean turned) {
        this.turned = turned;
    }
    public void setPositionOffset(float x, float y) {
        this.positionOffset.set(x, y);
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

    public void render(SpriteBatch batch, int renderOffset) {
        if(!isVisible) return;
        sprite.setFlip(!turned, false);
        sprite.setPosition(entity.getX() + positionOffset.x + renderOffset, entity.getY() + positionOffset.y);
        sprite.draw(batch, alpha);
    }

    public void delete() {
        textures = new TextureRegion[0];
    }

}
