package de.undefinedhuman.sandboxgame.gui.texture;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.resources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;

public enum GuiTemplate {

    BIG_BUTTON("Big Button"),
    HOTBAR("Hotbar"),
    PANEL("Panel"),
    SCROLL_PANEL("Scroll Panel"),
    SMALL_PANEL("Small Panel"),
    SMALL_BUTTON("Small Button"),
    SLOT("Slot");

    public String templateName;
    public int cornerSize;
    public String[] textures = new String[9];

    GuiTemplate(String templateName) {
        this.templateName = templateName;
    }

    public void load() {

        FileHandle file = ResourceManager.loadFile(Paths.GUI_PATH, "template/" + templateName + "/settings.gui");
        FileReader reader = new FileReader(file, false);
        reader.nextLine();
        this.cornerSize = reader.getNextInt();
        reader.close();
        for (int i = 0; i < 9; i++)
            textures[i] = Paths.GUI_PATH.getPath() + "template/" + templateName + "/" + i + ".png";
        TextureManager.instance.addTexture(textures);

    }

    public Vector2 getOffset() {
        return new Vector2(cornerSize, cornerSize);
    }

    public void delete() {
        TextureManager.instance.removeTexture(textures);
    }

}
