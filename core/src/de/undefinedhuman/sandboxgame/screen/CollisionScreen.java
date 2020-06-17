package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4i;

public class CollisionScreen implements Screen {

    /*do {
        Handle Movement/Collision
    } while((velX = velX-8) > 0);*/

    // MAKE Sure Collision Goes from BOTTOM TO TOP

    private static final int BLOCK_SIZE = 32;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Hitbox player;

    private byte[][][] world = new byte[40][40][2];

    private float velocityY = 0;
    private boolean onSlope = false;

    private Vector2 tempCenterVector = new Vector2();

    private byte[] collisionMasks = new byte[] {
            0, 0, 0, 2, 0, 0, 3, 1, 0, 4, 0, 1, 5, 1, 1, 0
    };

    private Hitbox[] hitboxes = new Hitbox[] {
            null,
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(BLOCK_SIZE, 0), new Vector2(BLOCK_SIZE, BLOCK_SIZE), new Vector2(0, BLOCK_SIZE) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(0, BLOCK_SIZE), new Vector2(BLOCK_SIZE, BLOCK_SIZE) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, BLOCK_SIZE), new Vector2(BLOCK_SIZE, BLOCK_SIZE), new Vector2(BLOCK_SIZE, 0) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(BLOCK_SIZE, 0), new Vector2(0, BLOCK_SIZE) }),
            new Hitbox(new Vector2(16, 16), new Vector2[] { new Vector2(0, 0), new Vector2(BLOCK_SIZE, 0), new Vector2(BLOCK_SIZE, BLOCK_SIZE) })
    };

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.batch = new SpriteBatch();

        this.player = new Hitbox(500, 600, new Vector2(50, 100), new Vector2[] {
                new Vector2(25, 0),
                new Vector2(75, 0),
                new Vector2(100, 25),
                new Vector2(100, 175),
                new Vector2(75, 200),
                new Vector2(25, 200),
                new Vector2(0, 175),
                new Vector2(0, 25)
        });

        for(int i = 0; i < 20; i++)
            placeHitboxBlock(i, 0);

        checkMap();

    }

    @Override
    public void render(float delta) {

        Vector2i mouseBlockPosition = new Vector2i(Gdx.input.getX()/(BLOCK_SIZE*2), (Gdx.graphics.getHeight() - Gdx.input.getY())/(BLOCK_SIZE*2));

        if(Gdx.input.isButtonJustPressed(0)) placeHitboxBlock(mouseBlockPosition.x, mouseBlockPosition.y);
        if(Gdx.input.isButtonJustPressed(1)) destroyHitboxBLock(mouseBlockPosition.x, mouseBlockPosition.y);

        if(Gdx.input.isKeyPressed(Input.Keys.D)) player.addPosition(250f * delta, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.A)) player.addPosition(-250f * delta, 0);

        Vector3 response = calculateCollisionX(player);
        player.addPosition(response.x, response.y);

        velocityY -= 250f * delta;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) velocityY = 250f;
        velocityY = Math.max(velocityY, -250f);

        player.addPosition(0, velocityY * delta);
        response = calculateCollisionY(player);
        if(!response.isZero() && velocityY > 0) velocityY = 0;
        player.addPosition(response.x, response.y);

        player.addPosition(0, calculateSlopeCollisionY(player).y);
        player.addPosition(calculateSlopeCollisionX(player).x, 0);

        player.update();

        /*batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[0].length; j++) {
                byte state = world[i][j][1];
                if(state == 0) continue;
                Hitbox hitbox = hitboxes[state];
                hitbox.update(i * BLOCK_SIZE, j * BLOCK_SIZE);
                hitbox.render(batch);
            }
        }
        batch.end();*/
    }

    private Vector3 calculateCollisionX(Hitbox entity) {
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

    private Vector3 calculateCollisionY(Hitbox entity) {
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

    private Vector3 calculateSlopeCollisionX(Hitbox entity) {
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

    private Vector3 calculateSlopeCollisionY(Hitbox entity) {
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

    private boolean calculateMTV(Hitbox entity, int x, int y, Vector3 mtv) {
        byte state = world[x][y][1];
        if(state == 0) return false;
        Hitbox hitbox = hitboxes[state];
        hitbox.update(x * BLOCK_SIZE, y * BLOCK_SIZE); // Variables.COLLISION_SIZE needs to be in the actual game
        Vector3 overlap = SAT.collide(entity, hitbox);
        if(overlap == null) return false;
        tempCenterVector.set(entity.getCenter()).sub(hitbox.getCenter()).nor();
        if (Vector2.dot(tempCenterVector.x, tempCenterVector.y, overlap.x, overlap.y) < 0)
            overlap.scl(-1f, -1f, 1f);
        mtv.set(overlap.x * overlap.z, overlap.y * overlap.z, overlap.z);
        return true;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    private void checkMap() {
        for(int i = 0; i < world.length; i++)
            for(int j = 0; j < world[0].length; j++)
                checkCell(i, j);
    }

    private void checkBlock(int x, int y) {
        Vector4i bounds = new Vector4i(x*2-1, x*2+2, y*2-1, y*2+2);
        for(int j = bounds.z; j <= bounds.w; j++)
            for(int i = bounds.x; i <= bounds.y; i++)
                checkCell(i, j);
    }

    private void checkCell(int x, int y) {
        setCollision(x, y, collisionMasks[getCollisionValue(x-1, y, (byte) 1) + getCollisionValue(x, y+1, (byte) 2) + getCollisionValue(x+1, y, (byte) 4) + getCollisionValue(x, y-1, (byte) 8)]);
    }

    private byte getCollisionValue(int x, int y, byte value) {
        return getCollision(x, y) != 0 ? value : 0;
    }

    private void setCollision(int x, int y, byte state) {
        if(y < 0 || y >= world[0].length) return;
        world[(world.length + x) % world.length][y][1] = state;
    }

    private byte getCollision(int x, int y) {
        if(y < 0 || y >= world[0].length) return 1;
        return world[(world.length + x) % world.length][y][0];
    }

    private void placeHitboxBlock(int blockX, int blockY) {
        Vector4i bounds = new Vector4i(blockX*2, blockX*2+1, blockY*2, blockY*2+1);
        for(int x = bounds.x; x <= bounds.y; x++)
            for(int y = bounds.z; y <= bounds.w; y++) {
                world[x][y][0] = 1;
            }
        checkBlock(blockX, blockY);
    }

    private void destroyHitboxBLock(int blockX, int blockY) {
        Vector4i bounds = new Vector4i(blockX*2, blockX*2+1, blockY*2, blockY*2+1);
        for(int x = bounds.x; x <= bounds.y; x++)
            for(int y = bounds.z; y <= bounds.w; y++) {
                world[x][y][0] = 0;
            }
        checkBlock(blockX, blockY);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

}
