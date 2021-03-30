package de.undefinedhuman.projectcreate.core.collision;

import de.undefinedhuman.projectcreate.core.engine.items.type.blocks.Block;
import de.undefinedhuman.projectcreate.core.engine.items.type.blocks.BlockType;
import de.undefinedhuman.projectcreate.core.engine.utils.math.Vector4;
import de.undefinedhuman.projectcreate.core.entity.Entity;
import de.undefinedhuman.projectcreate.core.item.ItemManager;

public class CollisionManager {

    public static final int NO_COLLISION = Integer.MIN_VALUE;

    // TODO
    public static boolean blockCanBePlaced(Entity entity, int x, int y) {
        return true;
    }

    // TODO Refactor through
    public static boolean collide(byte id) {
        return ((Block) ItemManager.instance.getItem(id)).blockType.getBlockType() == BlockType.Block;
    }

    public static boolean collideAABB(Vector4 bounds1, Vector4 bounds2) {
        return (bounds1.x < bounds2.z && bounds1.z > bounds2.x && bounds1.y < bounds2.w && bounds1.w > bounds2.y);
    }

}
