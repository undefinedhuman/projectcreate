package de.undefinedhuman.sandboxgame.gui.texture;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;

public enum GuiTemplate {

    BIG_BUTTON("Big Button", 14),
    HOTBAR("Hotbar", 6),
    PANEL("Panel", 27),
    SLIDER("Slider", 1),
    SCROLL_BAR("Scroll Panel", 2),
    SMALL_PANEL("Small Panel", 19),
    SMALL_BUTTON("Small Button", 12),
    SLOT("Slot", 2);

    public String templateName;
    public int cornerSize;
    public String[] textures = new String[9];

    GuiTemplate(String templateName, int cornerSize) {
        this.templateName = templateName;
        this.cornerSize = cornerSize;
    }

    public void load() {
        for (int i = 0; i < 9; i++)
            textures[i] = Paths.GUI_PATH + "template/" + templateName + "/" + i + ".png";
        TextureManager.instance.addTexture(textures);
    }

    public Vector2 getOffset() {
        return new Vector2(cornerSize, cornerSize);
    }

    public void delete() {
        TextureManager.instance.removeTexture(textures);
    }

}
