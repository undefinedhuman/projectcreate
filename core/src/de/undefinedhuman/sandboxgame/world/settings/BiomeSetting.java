package de.undefinedhuman.sandboxgame.world.settings;

public enum BiomeSetting {

    // Immer ein Vielfaches von 20 aufgrund der Chunk Size für die Drop Items und der Entitys
    DEV(80, 20), SMALL(200, 20), NORMAL(400, 40), BIG(800, 80);

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
