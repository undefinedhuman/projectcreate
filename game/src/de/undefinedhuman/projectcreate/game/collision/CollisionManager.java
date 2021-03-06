package de.undefinedhuman.projectcreate.game.collision;

import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.blocks.Block;
import de.undefinedhuman.projectcreate.core.items.blocks.BlockType;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;

public class CollisionManager {

    public static final int NO_COLLISION = Integer.MIN_VALUE;

    // TODO
    public static boolean blockCanBePlaced(Entity entity, int x, int y) {
        return true;
    }

    // TODO Refactor through
    public static boolean collide(int id) {
        return ((Block) ItemManager.getInstance().getItem(id)).blockType.getValue() == BlockType.Block;
    }

    public static boolean collideAABB(Vector4 bounds1, Vector4 bounds2) {
        return (bounds1.x < bounds2.z && bounds1.z > bounds2.x && bounds1.y < bounds2.w && bounds1.w > bounds2.y);
    }

}
