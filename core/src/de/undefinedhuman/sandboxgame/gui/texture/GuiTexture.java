package de.undefinedhuman.sandboxgame.gui.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4i;

import java.util.ArrayList;
import java.util.Collections;

public class GuiTexture {

    private GuiTemplate template = null;
    private int cornerSize = 0, centerWidth, centerHeight;
    private Color color = Color.WHITE;
    private ArrayList<String> textureNames = new ArrayList<>();
    private ArrayList<Vector4i> bounds = new ArrayList<>();

    private Pixmap pixmap;

    public GuiTexture() {}

    public GuiTexture(String texture) {
        textureNames.add(texture);
    }

    public GuiTexture(GuiTemplate template) {
        this.template = template;
        this.cornerSize = template.cornerSize;
        Collections.addAll(textureNames, template.textures);
    }

    public void init() {
        for(String texture : textureNames) {
            TextureManager.instance.addTexture(texture);
            bounds.add(new Vector4i());
        }
    }

    public void resize(int x, int y, int width, int height) {
        if (template == null)
            for(Vector4i bound : bounds)
                bound.set(x, y, width, height);
        else {
            this.cornerSize = template.cornerSize * Main.guiScale;
            centerWidth = Math.max(0, width - cornerSize * 2);
            centerHeight = Math.max(0, height - cornerSize * 2);

            bounds.get(0).set(x, y + height - cornerSize, cornerSize, cornerSize);
            bounds.get(1).set(x + width - cornerSize, y + height - cornerSize, cornerSize, cornerSize);
            bounds.get(2).set(x + width - cornerSize, y, cornerSize, cornerSize);
            bounds.get(3).set(x, y, cornerSize, cornerSize);
            bounds.get(4).set(x + cornerSize, y + cornerSize + centerHeight, centerWidth, cornerSize);
            bounds.get(5).set(x + centerWidth + cornerSize, y + cornerSize, cornerSize, centerHeight);
            bounds.get(6).set(x + cornerSize, y, centerWidth, cornerSize);
            bounds.get(7).set(x, y + cornerSize, cornerSize, centerHeight);
            bounds.get(8).set(x + cornerSize, y + cornerSize, centerWidth, centerHeight);
        }
    }

    public void render(SpriteBatch batch, float alpha) {
        Color batchColor = batch.getColor();
        batch.setColor(color.r, color.g, color.b, alpha);
        for(int i = 0; i < textureNames.size(); i++) {
            Vector4i bound = bounds.get(i);
            batch.draw(TextureManager.instance.getTexture(textureNames.get(i)), bound.x, bound.y, bound.z, bound.w);
        }
        batch.setColor(batchColor);
    }

    public void delete() {
        if(template != null)
            template = null;
        for (String textureName : textureNames)
            TextureManager.instance.removeTexture(textureName);
        textureNames.clear();
        bounds.clear();
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

}
