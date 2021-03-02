package de.undefinedhuman.sandboxgame.gui.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;

import java.util.ArrayList;
import java.util.Collections;

public class GuiTexture {

    public boolean remove = false;

    private GuiTemplate template = null;
    private int cornerSize = 0, usages = 1;
    private Color color = Color.WHITE;
    private ArrayList<String> textureNames = new ArrayList<>();
    private Texture internalTexture;

    public GuiTexture() {}

    public GuiTexture(String texture) {
        textureNames.add(texture);
    }

    public GuiTexture(GuiTemplate template) {
        this.template = template;
        this.cornerSize = template.cornerSize;
        Collections.addAll(textureNames, template.textures);
    }

    public GuiTexture init() {
        for(String texture : textureNames)
            TextureManager.instance.addTexture(texture);
        return this;
    }

    public void resize(int width, int height, int scale) {
        deleteInternalTexture();
        if(width <= 0 || height <= 0)
            return;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
        if(template == null)
            for (String textureName : textureNames) {
                Pixmap source = TextureManager.instance.getPixmap(textureName);
                pixmap.drawPixmap(source, 0, 0, source.getWidth(), source.getHeight(), 0, 0, width, height);
            }
        else {
            this.cornerSize = template.cornerSize * Main.guiScale;
            int centerWidth = Math.max(0, width - cornerSize * 2), centerHeight = Math.max(0, height - cornerSize * 2);

            drawToPixmap(pixmap, 0, 0, 0, cornerSize, cornerSize, scale);
            drawToPixmap(pixmap, 1, cornerSize + centerWidth, 0, cornerSize, cornerSize, scale);
            drawToPixmap(pixmap, 2, cornerSize + centerWidth, height - cornerSize, cornerSize, cornerSize, scale);
            drawToPixmap(pixmap, 3, 0, height - cornerSize, cornerSize, cornerSize, scale);
            drawToPixmap(pixmap, 4, cornerSize, 0, centerWidth, cornerSize, scale);
            drawToPixmap(pixmap, 5, width - cornerSize, cornerSize, cornerSize, centerHeight, scale);
            drawToPixmap(pixmap, 6, cornerSize, height - cornerSize, centerWidth, cornerSize, scale);
            drawToPixmap(pixmap, 7, 0, cornerSize, cornerSize, centerHeight, scale);
            drawToPixmap(pixmap, 8, cornerSize, cornerSize, centerWidth, centerHeight, scale);
        }
        internalTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch, int x, int y, float alpha) {
        if(textureNames.size() == 0 || internalTexture == null)
            return;
        Color batchColor = batch.getColor();
        batch.setColor(color.r, color.g, color.b, alpha);
        batch.draw(internalTexture, x, y);
        batch.setColor(batchColor);
    }

    public void delete() {
        if(template != null)
            template = null;
        for (String textureName : textureNames)
            TextureManager.instance.removeTexture(textureName);
        deleteInternalTexture();
        textureNames.clear();
    }

    public void add() {
        usages++;
    }

    public void remove() {
        usages--;
        if (usages <= 0)
            remove = true;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setTexture(String... textures) {
        delete();
        Collections.addAll(textureNames, textures);
        init();
    }

    public int getCornerSize() { return cornerSize; }

    public int getBaseCornerSize() { return template == null ? 0 : template.cornerSize; }

    public Vector2 getOffset() { return new Vector2(cornerSize, cornerSize); }

    private void drawToPixmap(Pixmap pixmap, int textureIndex, int x, int y, int width, int height, int scale) {
        Pixmap source = TextureManager.instance.getPixmap(textureNames.get(textureIndex));
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                pixmap.drawPixel(x + i, y + j, source.getPixel((i / scale) % source.getWidth(), (j / scale) % source.getHeight()));
    }

    private void deleteInternalTexture() {
        if(internalTexture == null)
            return;
        internalTexture.dispose();
    }

}
