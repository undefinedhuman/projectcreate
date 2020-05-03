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

    public static int collideHor(Vector2 from, Vector2 towards) {
        int     startX = (int) (from.x/Variables.BLOCK_SIZE),
                endX = (int) ((towards.x-1)/Variables.BLOCK_SIZE),
                tileY = (int) (from.y/Variables.BLOCK_SIZE);

        for(int tileX = startX; tileX <= endX; tileX++)
            if(WorldManager.instance.isObstacle(new Vector2(tileX, tileY)))
                return tileY;
        return Integer.MIN_VALUE;
    }

    public static int collideVer(Vector2 from, Vector2 towards) {
        int     startY = (int) (from.y/Variables.BLOCK_SIZE),
                endY = (int) (towards.y/Variables.BLOCK_SIZE),
                tileX = (int) (from.x/Variables.BLOCK_SIZE);

        for(int tileY = startY; tileY <= endY; tileY++)
            if(WorldManager.instance.isObstacle(new Vector2(tileX, tileY)))
                return tileX;
        return Integer.MIN_VALUE;
    }

}
