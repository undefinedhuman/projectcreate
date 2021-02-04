package de.undefinedhuman.sandboxgame.gui.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4i;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;

public class GuiTexture {

    private GuiTemplate template = null;
    private int cornerSize = 0;
    private Color color = Color.WHITE;
    private ArrayList<String> textureNames = new ArrayList<>();
    private ArrayList<Vector4i> bounds = new ArrayList<>();

    private Vector4i prevBounds = new Vector4i();

    private FrameBuffer guiTextureFBO;
    private TextureRegion m_fboRegion;

    private SpriteBatch batch = new SpriteBatch();

    private int x, y;

    public GuiTexture() {
        addTexture();
    }

    public GuiTexture(String texture) {
        addTexture(texture);
    }

    public GuiTexture(GuiTemplate template) {
        this.template = template;
        this.cornerSize = template.cornerSize;
        addTexture(template.textures);
    }

    private void addTexture(String... textures) {
        TextureManager.instance.addTexture(textures);
        for(String texture : textures) {
            textureNames.add(texture);
            bounds.add(new Vector4i());
        }

        m_fboRegion = new TextureRegion();
        m_fboRegion.flip(false, true);
    }

    public void resize(int x, int y, int width, int height) {

        if (template == null)
            for(Vector4i bound : bounds)
                bound.set(x, y, width, height);
        else {
            this.cornerSize = template.cornerSize * Main.guiScale;
            int centerWidth = Math.max(0, width - cornerSize * 2);
            int centerHeight = Math.max(0, height - cornerSize * 2);
            int index, e, r, o, a;

            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++) {
                    e = Tools.isEqual(i, j);
                    r = i ^ j;
                    o = i | j;
                    a = i & j;
                    index = (3 - (i + j)) * e + 2 * r * i;
                    bounds.get(index).set(
                            x + (centerWidth + cornerSize) * i,
                            y + (centerHeight + cornerSize) * j,
                            cornerSize,
                            cornerSize
                    );

                    bounds.get(4 + index).set(
                            x + cornerSize * o + centerWidth * a,
                            y + cornerSize * (e | j) + centerHeight * (r & j),
                            cornerSize * e + centerWidth * r,
                            cornerSize * r + centerHeight * e
                    );
                }

            bounds.get(8).set(x + cornerSize, y + cornerSize, centerWidth, centerHeight);
        }

    }

    private void createFrameBuffer() {
        if(guiTextureFBO != null)
            guiTextureFBO.dispose();
        guiTextureFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        //m_fboRegion.setRegion(guiTextureFBO.getColorBufferTexture());
        m_fboRegion = new TextureRegion(guiTextureFBO.getColorBufferTexture());
        m_fboRegion.flip(false, true);
    }

    public void drawTextureToFBO(String texture, int x, int y, int width, int height) {
        batch.draw(TextureManager.instance.getTexture(texture), x, y, width, height);
    }

    public void render(SpriteBatch batch, float alpha) {
        Color batchColor = batch.getColor();
        batch.setColor(color.r, color.g, color.b, alpha);
        for(int i = 0; i < textureNames.size(); i++) {
            Vector4i bound = bounds.get(i);
            batch.draw(TextureManager.instance.getTexture(textureNames.get(i)), bound.x, bound.y, bound.z, bound.w);
        }
        batch.setColor(batchColor);
        /*if(m_fboRegion == null)
            return;
        batch.draw(m_fboRegion, 0, 0);*/
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setTexture(String texture) {
        if (template != null)
            template = null;
        for (String name : textureNames)
            TextureManager.instance.removeTexture(name);
        this.textureNames.clear();
        this.bounds.clear();
        addTexture(texture);
    }

    public int getCornerSize() { return cornerSize; }

    public int getBaseCornerSize() { return template == null ? 0 : template.cornerSize; }

    public Vector2 getOffset() { return new Vector2(cornerSize, cornerSize); }

    public void delete() {
        for (String s : textureNames)
            TextureManager.instance.removeTexture(s);
        textureNames.clear();
        bounds.clear();
    }

    private void clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

}
