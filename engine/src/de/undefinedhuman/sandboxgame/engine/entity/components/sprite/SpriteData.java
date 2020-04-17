package de.undefinedhuman.sandboxgame.engine.entity.components.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.base.GameObject;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

public class SpriteData {

    public GameObject entity;

    private String textureName;
    private float alpha = 1, rotation = 0;
    private Vector2 scale = new Vector2(1, 1), origin = new Vector2(), turnedOffset = new Vector2(0, 0), positionOffset = new Vector2(0, 0), size = new Vector2(0, 0);
    private Color color = Color.WHITE;
    private int renderLevel = 0, row = 1, col = 1;
    private Sprite sprite;
    private boolean hasRegion = false, hasOrigin = false, isVisible = true, isAnimated = true;
    private TextureRegion[][] regions;

    public SpriteData(String textureName) {
        this.textureName = textureName;
        this.sprite = new Sprite(Tools.fixBleeding(TextureManager.instance.getTexture(textureName)));
    }

    public void setAnimationBounds(Vector2 regionSize) {
        if(!isAnimated) return;
        this.row = (int) (regionSize.x == 0 ? 1 : regionSize.x);
        this.col = (int) (regionSize.y == 0 ? 1 : regionSize.y);
        this.hasRegion = true;
        createRegions();
    }

    public void createRegions() {
        TextureRegion region = new TextureRegion(TextureManager.instance.getTexture(textureName));
        this.regions = region.split(region.getRegionWidth() / row, region.getRegionHeight() / col);
        this.sprite.setRegion(Tools.fixBleeding(regions[0][0]));
    }

    public int getRegionSize() {
        return regions.length;
    }

    public void setFrameIndex(TextureRegion region) {
        sprite.setRegion(region);
    }

    public void setFrameIndex(float index) {
        if(!isAnimated) return;
        int size = regions.length;
        TextureRegion reg = regions[(int) index % size][(int) index / size];
        if (reg != null) sprite.setRegion(Tools.fixBleeding(reg));
    }

    public void setFrameIndex(float row, float col) {
        TextureRegion reg = regions[(int) row][(int) col];
        if (reg != null) sprite.setRegion(Tools.fixBleeding(reg));
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.sprite.setOrigin((int) origin.x, (int) origin.y);
    }

    public void setOrigin(float x, float y) {
        this.sprite.setOrigin((int) x, (int) y);
    }

    public Vector2 getTurnedOffset() {
        return turnedOffset;
    }

    public void setTurnedOffset(Vector2 turnedOffset) {
        this.turnedOffset = turnedOffset;
    }

    public boolean hasOrigin() {
        return hasOrigin;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public float getAlpha() { return alpha; }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void setSize(float width, float height) {
        this.size.set(width, height);
    }

    public Vector2 getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(Vector2 positionOffset) {
        this.positionOffset = positionOffset;
    }

    public void setPositionOffset(int x, int y) { this.positionOffset.set(x, y); }

    public String getTexture() {
        return textureName;
    }

    public void setTexture(String textureName) {
        if (!TextureManager.instance.hasTexture(textureName)) TextureManager.instance.addTexture(textureName);
        this.textureName = textureName;
        this.sprite.setRegion(Tools.fixBleeding(TextureManager.instance.getTexture(textureName)));
        if (hasRegion) createRegions();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getRenderLevel() {
        return renderLevel;
    }

    public void setRenderLevel(int renderLevel) { this.renderLevel = renderLevel; }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) { isAnimated = animated; }

    public TextureRegion getTextureByRegion(int row, int col) {
        return regions[row][col];
    }

    public TextureRegion[][] getRegions() {
        return regions;
    }

}
