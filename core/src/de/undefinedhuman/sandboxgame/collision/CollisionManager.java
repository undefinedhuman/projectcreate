package de.undefinedhuman.sandboxgame.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.BlockType;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.world.WorldManager;

public class CollisionManager {

    public static final int NO_COLLISION = Integer.MIN_VALUE;

    // TODO
    public static boolean blockCanBePlaced(Entity entity, Vector2 blockPosition, byte cell) {
        return true;
    }

    public static boolean collide(byte id) {
        return ((Block) ItemManager.instance.getItem(id)).blockType.getBlockType() == BlockType.Block;
    }

    public static boolean collideAABB(Vector4 bounds1, Vector4 bounds2) {
        return (bounds1.x < bounds2.z && bounds1.z > bounds2.x && bounds1.y < bounds2.w && bounds1.w > bounds2.y);
    }

    public static int collideHor(Vector2 from, Vector2 towards, boolean withoutSlopes) {
        int     startX = (int) (from.x/Variables.BLOCK_SIZE),
                endX = (int) ((towards.x-1)/Variables.BLOCK_SIZE),
                tileY = (int) (from.y/Variables.BLOCK_SIZE);

        for(int tileX = startX; tileX <= endX; tileX++)
            if(withoutSlopes ? WorldManager.instance.isObstacleWithOutSlope(tileX, tileY) : WorldManager.instance.isObstacleWithSlope(tileX, tileY))
                return tileY;
        return NO_COLLISION;
    }

    public static int collideVer(Vector2 from, Vector2 towards) {
        int     fromY = (int) (from.y/Variables.BLOCK_SIZE),
                towardsY = (int) (towards.y/Variables.BLOCK_SIZE),
                tileX = (int) (from.x/Variables.BLOCK_SIZE);

        if(WorldManager.instance.isObstacleWithOutSlope(tileX, fromY))
            return tileX;

        for(int tileY = fromY + 1; tileY <= towardsY; tileY++)
            if(WorldManager.instance.isObstacleWithSlope(tileX, tileY))
                return tileX;
        return NO_COLLISION;
    }

}
