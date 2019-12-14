package de.undefinedhuman.sandboxgame.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;

public class ManagerList {

    ArrayList<Manager> managers;

    public ManagerList() {
        managers = new ArrayList<>();
    }

    public void addManager(Manager... managers) {
        this.managers.addAll(Arrays.asList(managers));
    }

    public void init() {
        for(Manager manager : managers) manager.init();
    }
    public void resize(float width, float height) { for(Manager manager : managers) manager.resize(width, height); }
    public void update(float delta) {
        for(Manager manager : managers) manager.update(delta);
    }
    public void render(SpriteBatch batch) {
        for(Manager manager : managers) manager.render(batch);
    }
    public void delete() {
        for(int i = managers.size()-1; i >= 0; i--) managers.get(i).delete();
        managers.clear();
    }

}
