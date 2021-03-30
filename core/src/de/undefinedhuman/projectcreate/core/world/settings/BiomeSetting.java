package de.undefinedhuman.projectcreate.core.world.settings;

import de.undefinedhuman.projectcreate.core.engine.utils.Variables;

public enum BiomeSetting {

    DEV(Variables.CHUNK_SIZE * 2, Variables.CHUNK_SIZE), SMALL(Variables.CHUNK_SIZE * 5, Variables.CHUNK_SIZE * 2), NORMAL(Variables.CHUNK_SIZE * 10, Variables.CHUNK_SIZE * 2), BIG(Variables.CHUNK_SIZE * 15, Variables.CHUNK_SIZE * 2);

    private int size, transition;

    BiomeSetting(int size, int transition) {

        this.size = size;
        this.transition = transition;

    }

    public int getSize() {
        return size;
    }

    public int getTransition() {
        return transition;
    }

    public int getWidth() { return size + transition; }

}
