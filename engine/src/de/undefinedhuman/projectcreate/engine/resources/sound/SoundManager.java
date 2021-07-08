package de.undefinedhuman.projectcreate.engine.resources.sound;

import com.badlogic.gdx.audio.Sound;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

import java.util.HashMap;

public class SoundManager extends Manager {

    private static volatile SoundManager instance;

    private HashMap<String, Sound> sounds;

    private SoundManager() {
        sounds = new HashMap<>();
    }

    @Override
    public void init() {}

    public void addSound(String... names) {
        for (String s : names) if (!hasSound(s)) sounds.put(s, RessourceUtils.loadSound(s));
    }

    public boolean hasSound(String name) {
        return sounds.containsKey(name);
    }

    @Override
    public void delete() {
        for (Sound sound : sounds.values()) sound.dispose();
        sounds.clear();
    }

    public void removeSound(String... names) {
        for (String name : names)
            if (hasSound(name)) {
                sounds.get(name).dispose();
                sounds.remove(name);
            }
    }

    public Sound getSound(String name) {
        if (hasSound(name)) return sounds.get(name);
        return getSound("dirtSound.wav");
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null)
                    instance = new SoundManager();
            }
        }
        return instance;
    }

}
