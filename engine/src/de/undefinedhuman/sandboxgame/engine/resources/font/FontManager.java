package de.undefinedhuman.sandboxgame.engine.resources.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.resources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FontManager extends Manager {

    public static FontManager instance;

    private HashMap<Font, ArrayList<BitmapFont>> fonts;

    public FontManager() {
        if (instance == null) instance = this;
        this.fonts = new HashMap<>();
    }

    @Override
    public void init() {
        loadFonts(Font.values());
    }

    public void loadFonts(Font... fonts) {

        boolean loaded = false;

        for (Font font : fonts) {
            ArrayList<BitmapFont> fontList = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                BitmapFont bitmapFont = ResourceManager.loadFont(font.name());
                if (bitmapFont == null) continue;
                else loaded = true;
                bitmapFont.getData().markupEnabled = true;
                bitmapFont.getData().setScale(i + 1);
                bitmapFont.getData().spaceXadvance = i + 1;
                fontList.add(bitmapFont);
            }
            this.fonts.put(font, fontList);
        }

        if (loaded) Log.info("Font" + Tools.appendSToString(fonts.length) + " loaded: " + Arrays.toString(fonts));

    }

    @Override
    public void delete() {
        for (ArrayList<BitmapFont> list : fonts.values()) {
            for (BitmapFont font : list) font.dispose();
            list.clear();
        }
        fonts.clear();
    }

    public BitmapFont getFont(Font font, float scale) {
        return fonts.get(font).get((int) (Math.max(Math.min(10, scale), 1) - 1));
    }

}