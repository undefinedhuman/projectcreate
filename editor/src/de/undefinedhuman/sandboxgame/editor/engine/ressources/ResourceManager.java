package de.undefinedhuman.sandboxgame.editor.engine.ressources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.sandboxgame.engine.file.Paths;

public class ResourceManager {

    public static FileHandle loadDir(Paths path) {
        return Gdx.files.internal(path.getPath());
    }

    public static FileHandle loadFile(Paths path, String name) {
        return Gdx.files.internal(path.getPath() + name);
    }

}
