package de.undefinedhuman.projectcreate.core.engine.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
        for (Manager manager : managers) manager.init();
    }

    public void resize(int width, int height) { for (Manager manager : managers) manager.resize(width, height); }

    public void update(float delta) {
        for (Manager manager : managers) manager.update(delta);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for (Manager manager : managers)
            manager.render(batch, camera);
    }

    public void renderGui(SpriteBatch batch, OrthographicCamera camera) {
        for (Manager manager : managers)
            manager.renderGui(batch, camera);
    }

    public void delete() {
        for (int i = managers.size() - 1; i >= 0; i--) managers.get(i).delete();
        managers.clear();
    }

}
