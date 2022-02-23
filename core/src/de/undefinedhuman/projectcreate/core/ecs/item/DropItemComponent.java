package de.undefinedhuman.projectcreate.core.ecs.item;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;

public class DropItemComponent implements Component {

    private static final float LIFE_LENGTH = 300;
    private static final float SPEED = 120;
    private static final float GRAVITY = 1200f;

    private int itemID = -1, amount = 0;
    private float time = 0;
    private Entity target = null;
    private String spriteDataName;
    private boolean init = false;

    public DropItemComponent() {
    }

    public void init() {

    }

}
