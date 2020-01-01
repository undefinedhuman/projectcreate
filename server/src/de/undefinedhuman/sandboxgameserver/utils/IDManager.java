package de.undefinedhuman.sandboxgameserver.utils;

import java.util.ArrayList;

public class IDManager {

    private int maxID = -1;
    private ArrayList<Integer> freeIDs;

    public IDManager() {
        this.freeIDs = new ArrayList<>();
    }

    public int getID() {
        if (freeIDs.size() > 0) return freeIDs.remove(0);
        else return ++maxID;
    }

    public void addID(int id) {
        this.freeIDs.add(id);
    }

    public void delete() {
        freeIDs.clear();
    }

}
