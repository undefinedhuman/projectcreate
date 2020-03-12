package de.undefinedhuman.sandboxgame.engine;

import de.undefinedhuman.sandboxgame.engine.file.Paths;

public class Engine {

    public static Engine instance;

    public Paths externalPath, assetPath;

    public Engine(Paths externalPath, Paths assetPath) {
        this.externalPath = externalPath;
        this.assetPath = assetPath;
    }

}
