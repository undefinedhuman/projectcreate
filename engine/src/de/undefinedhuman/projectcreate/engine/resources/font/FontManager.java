package de.undefinedhuman.projectcreate.engine.resources.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

import java.util.HashMap;

public class FontManager extends Manager {

    public static FontManager instance;

    private final HashMap<Font, HashMap<Integer, BitmapFont>> fonts;

    public FontManager() {
        if (instance == null) instance = this;
        this.fonts = new HashMap<>();
    }

    @Override
    public void init() {
        for(Font font : Font.values())
            fonts.put(font, new HashMap<>());
    }

    @Override
    public void delete() {
        for(HashMap<Integer, BitmapFont> fontList : fonts.values()) {
            for(BitmapFont font : fontList.values())
                font.dispose();
            fontList.clear();
        }
        fonts.clear();
    }

    public BitmapFont getFont(Font font, int size) {
        HashMap<Integer, BitmapFont> fontList = fonts.get(font);
        BitmapFont bitmapFont = fontList != null ? fontList.get(size) : null;
        if(bitmapFont == null) bitmapFont = generateFont("gui/font/" + font.name() + ".ttf", size);
        assert fontList != null;
        fontList.put(size, bitmapFont);
        return bitmapFont;
    }

    private BitmapFont generateFont(String path, int fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

}
