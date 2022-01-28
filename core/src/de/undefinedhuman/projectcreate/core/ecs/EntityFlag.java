package de.undefinedhuman.projectcreate.core.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;

public enum EntityFlag {
    IS_MAIN_PLAYER;

    public int value() {
        return 1 << ordinal();
    }

    public static int getBigMask(EntityFlag... flags) {
        int mask = 0;
        for(EntityFlag flag : flags)
            mask = flag.value() | mask;
        return mask;
    }

    public static boolean hasFlags(Entity entity, EntityFlag... entityFlags) {
        int flag;
        for(EntityFlag entityFlag : entityFlags) {
            flag = entityFlag.value();
            if((entity.flags & flag) != flag) return false;
        }
        return true;
    }

}