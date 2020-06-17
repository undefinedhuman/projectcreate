package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public class CollisionUtils {

    public static Hitbox[] blockCollisionMask = new Hitbox[] {
            null,
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(Variables.COLLISION_SIZE, 0), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2(0, Variables.COLLISION_SIZE) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(0, Variables.COLLISION_SIZE), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, Variables.COLLISION_SIZE), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2(Variables.COLLISION_SIZE, 0) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(Variables.COLLISION_SIZE, 0), new Vector2(0, Variables.COLLISION_SIZE) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(Variables.COLLISION_SIZE, 0), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE) })
    };

    /*public static Vector3 calculateCollisionX(Hitbox entity) {
        entity.update();
        Vector3 response = new Vector3();

        for(int j = 0; j < world[0].length; j++)
            for(int i = 0; i < world.length; i++) {
                Vector3 mtv = new Vector3();
                if(!calculateMTV(entity, i, j, mtv)) continue;
                if(mtv.z >= response.z) response.set((mtv.x != 0 && mtv.y != 0) ? 0 : mtv.x, 0, mtv.z);
            }

        return response;
    }

    public static Vector3 calculateCollisionY(Hitbox entity) {
        entity.update();
        Vector3 response = new Vector3();

        for(int j = 0; j < world[0].length; j++)
            for(int i = 0; i < world.length; i++) {
                Vector3 mtv = new Vector3();
                if(!calculateMTV(entity, i, j, mtv)) continue;
                if(mtv.z >= response.z) {
                    onSlope = mtv.x != 0 && mtv.y != 0;
                    response.set(0, onSlope ? ((mtv.x * mtv.x) / mtv.y) + mtv.y : mtv.y, mtv.z);
                }
            }

        return response;
    }

    public static Vector3 calculateSlopeCollisionX(Hitbox entity) {
        entity.update();
        Vector3 response = new Vector3();

        for(int j = 0; j < world[0].length; j++)
            for(int i = 0; i < world.length; i++) {
                Vector3 mtv = new Vector3();
                if(!calculateMTV(entity, i, j, mtv)) continue;
                if(mtv.z > response.z && mtv.x != 0) {
                    response.set((mtv.y * mtv.y) / mtv.x + mtv.x, 0, mtv.z);
                    onSlope = false;
                }
            }

        return response;
    }

    public static Vector3 calculateSlopeCollisionY(Hitbox entity) {
        entity.update();
        Vector3 response = new Vector3();

        for(int j = 0; j < world[0].length; j++)
            for(int i = 0; i < world.length; i++) {
                Vector3 mtv = new Vector3();
                if(!calculateMTV(entity, i, j, mtv)) continue;
                if (onSlope && mtv.z > response.z && mtv.y < 0) response.set(0, mtv.y, mtv.z);
            }

        return response;
    }

    private static Vector2 tempCenter = new Vector2();

    public static boolean calculateMTV(byte[][][] collisionLayer, Hitbox entity, int x, int y, Vector3 mtv) {
        byte state = collisionLayer[x][y][1];
        if(state == 0) return false;
        Hitbox hitbox = blockCollisionMask[state];
        hitbox.update(x * Variables.BLOCK_SIZE, y * Variables.BLOCK_SIZE);
        Vector3 overlap = SAT.collide(entity, hitbox);
        if(overlap == null) return false;
        tempCenter.set(entity.getCenter()).sub(hitbox.getCenter()).nor();
        if (Vector2.dot(tempCenter.x, tempCenter.y, overlap.x, overlap.y) < 0)
            overlap.scl(-1f, -1f, 1f);
        mtv.set(overlap.x * overlap.z, overlap.y * overlap.z, overlap.z);
        return true;
    }*/

}
