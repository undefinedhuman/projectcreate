package de.undefinedhuman.sandboxgame.gui.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.utils.Vector5;

import java.util.ArrayList;

public class GuiTexture {

    private GuiTemplate template = null;
    private int cornerSize = 0;
    private Vector5 tempVal = new Vector5();
    private ArrayList<String> textureNames = new ArrayList<>();
    private ArrayList<TextureRegion> textures = new ArrayList<>();

    public GuiTexture() {
        addTexture("Unknown.png");
    }

    public GuiTexture(String texture) {
        addTexture(texture);
    }

    public GuiTexture(GuiTemplate template) {
        this.template = template;
        addTexture(template.textures);
    }

    public void resize(float localScale) {
        if(template == null) return;
        this.cornerSize = (int) (template.cornerSize * localScale);
    }

    public void render(SpriteBatch batch, int x, int y, int width, int height, Color color) {
        Color col = batch.getColor();
        batch.setColor(color);
        if(template == null) batch.draw(textures.get(0), x, y, width, height);
        else {
            tempVal.set(width - cornerSize*2, height - cornerSize*2, x + width - cornerSize, y + height - cornerSize, y + cornerSize);

            renderSprite(batch,0, cornerSize, cornerSize, x, tempVal.w, color);
            renderSprite(batch,1, cornerSize, cornerSize, tempVal.z, tempVal.w, color);
            renderSprite(batch,2, cornerSize, cornerSize, tempVal.z, y, color);
            renderSprite(batch,3, cornerSize, cornerSize, x, y, color);

            renderSprite(batch,4, Math.max(0, tempVal.x), cornerSize, x + cornerSize, tempVal.w, color);
            renderSprite(batch,5, cornerSize, Math.max(0, tempVal.y), tempVal.z, tempVal.v, color);
            renderSprite(batch,6, Math.max(0, tempVal.x), cornerSize, x + cornerSize, y, color);
            renderSprite(batch,7, cornerSize, Math.max(0, tempVal.y), x, tempVal.v, color);

            renderSprite(batch,8, Math.max(0, tempVal.x), Math.max(0, tempVal.y), x + cornerSize, tempVal.v, color);
        }
        batch.setColor(col);
    }

    private void addTexture(String... names) {
        for(String s : names) {
            textureNames.add(s);
            textures.add(TextureManager.instance.getTexture(s));
        }
    }

    public int getCornerSize() { return cornerSize; }
    public ArrayList<TextureRegion> getTextures() { return textures; }

    public void delete() {
        for(String s : textureNames) TextureManager.instance.removeTexture(s);
        textureNames.clear();
        textures.clear();
    }

    private void renderSprite(SpriteBatch batch, int id, float width, float height, float x, float y, Color color) {
        Color col = batch.getColor();
        batch.setColor(color);
        batch.draw(textures.get(id), x, y, width, height);
        batch.setColor(col);
    }

}
