package de.undefinedhuman.projectcreate.launcher.icon;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.ds.Key;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import java.util.HashMap;

public class IconManager extends Manager {

    private static IconManager instance;

    private static final Key<String, Vector2i> TEMP_KEY = new Key<>("", new Vector2i());
    private static final String ICON_PATH = "icon/NAME.svg";

    private HashMap<Key<String, Vector2i>, FlatSVGIcon> icons = new HashMap<>();

    private IconManager() { }

    @Override
    public void delete() {
        icons.clear();
    }

    public FlatSVGIcon addIcon(String name, int width, int height) {
        FlatSVGIcon icon = new FlatSVGIcon(ICON_PATH.replace("NAME", name), width, height);
        icons.put(new Key<>(name, new Vector2i(width, height)), icon);
        return icon;
    }

    public FlatSVGIcon getIcon(String name, int width, int height) {
        updateKey(name, width, height);
        FlatSVGIcon icon = icons.get(TEMP_KEY);
        if(icon != null)
            return icon;
        return addIcon(name, width, height);
    }

    private void updateKey(String name, int width, int height) {
        TEMP_KEY.key1 = name;
        TEMP_KEY.key2.set(width, height);
    }

    public static IconManager getInstance() {
        if (instance == null) {
            synchronized (IconManager.class) {
                if (instance == null)
                    instance = new IconManager();
            }
        }
        return instance;
    }

}
