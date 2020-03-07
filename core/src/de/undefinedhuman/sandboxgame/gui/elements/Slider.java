package de.undefinedhuman.sandboxgame.gui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.event.ChangeEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.utils.Mouse;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;

public class Slider extends Gui {

    private float progress = 1f, progressWidth = 0, progressHeight = 0;
    private boolean grabbed = false;
    private GuiComponent pointer;

    private Texture progressTexture;

    private ArrayList<ChangeEvent> changeListeners;
    private String progressPath, pointerPath;
    private boolean wrapProgress;

    public Slider(GuiTexture backgroundTexture, String progressTexture, String pointerTexture, boolean wrapProgress) {

        super(backgroundTexture);
        this.progressPath = progressTexture;
        this.pointerPath = pointerTexture;
        this.wrapProgress = wrapProgress;
        this.changeListeners = new ArrayList<>();

    }

    @Override
    public void init() {
        initPointer(pointerPath);
        initProgress(progressPath, wrapProgress);
        notifyChangeListener();
    }

    private void initPointer(String texture) {
        pointer = new Gui(new GuiTexture(texture));
        pointer.set("r" + progress, "r0.5f", "p4", "p14").setCentered();
        addChild(pointer);
    }

    private void initProgress(String texture, boolean wrap) {
        progressTexture = new Texture(TextureManager.instance.getTexture(texture).getTexture().getTextureData());
        if (wrap) progressTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
    }

    private void notifyChangeListener() {
        for (ChangeEvent changeListener : changeListeners) changeListener.notify(progress);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        resizePointer();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        if ((isClicked(camera) || pointer.isClicked(camera)) && Mouse.isLeftClicked()) grabbed = true;
        else if (!Mouse.isLeftClicked()) grabbed = false;

        if (grabbed) {
            float mouseX = Mouse.getMouseCoords().x;
            this.progress = Tools.clamp((mouseX - position.x) / scale.x, 0, 1f);
            resizePointer();
            notifyChangeListener();
        }

        super.render(batch, camera);
        batch.draw(progressTexture, position.x + texture.getCornerSize(), position.y + texture.getCornerSize(), 0, 0, (int) progressWidth, (int) progressHeight);
        pointer.render(batch, camera);

    }

    private void resizePointer() {
        if (pointer == null) return;
        progressWidth = progress * (scale.x - texture.getCornerSize() * 2);
        progressHeight = scale.y - texture.getCornerSize() * 2;
        pointer.setValue(Axis.X, progressWidth / scale.x);
        pointer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        resizePointer();
        notifyChangeListener();
    }

    public void addChangeListener(ChangeEvent changeListener) {
        this.changeListeners.add(changeListener);
    }

}
