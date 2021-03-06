package de.undefinedhuman.projectcreate.engine.utils.manager;

import de.undefinedhuman.projectcreate.engine.file.Serializable;

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
        managers.forEach(manager -> {
            manager.init();
            if(manager instanceof Serializable)
                ((Serializable) manager).load();
        });
    }

    public void resize(int width, int height) {
        managers.forEach(manager -> manager.resize(width, height));
    }

    public void update(float delta) {
        managers.forEach(manager -> manager.update(delta));
    }

    public void save() {
        managers.stream()
                .filter(Serializable.class::isInstance)
                .map(Serializable.class::cast)
                .forEach(Serializable::save);
    }

    public void delete() {
        for(int i = managers.size()-1; i >= 0; i--) {
            Manager manager = managers.get(i);
            if(manager instanceof Serializable) ((Serializable) manager).save();
            manager.delete();
        }
        managers.clear();
    }

}
