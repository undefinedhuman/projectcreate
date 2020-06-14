package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4i;

public class CollisionScreen implements Screen {

    // Make Blocks turn to Edges and not full hitboxes
    // Limit Player Velocity \/
    /*do {
        Handle Movement/Collision
    } while((velX = velX-8) > 0);*/

    // MAKE Sure Collision Goes from BOTTOM TO TOP

    /*
        Collision X -> nur X Response zurückgeben, keine Slope nach oben verschiebung
        Collision Y -> Immer -Y also runterdrücke mitzählen/überschreibe, Slope bzw. normale Blöcke dann hoch tuen
        Collision X/Slopes -> Falls immernoch in einer Slope zur Seite rausdrücken, hier nochmal schauen welche Axis genommen wird, wenn die äußerste Seite des Spielers genommen wird ok, I guess?, wenn die untere schräge kante genommen wird muss das in eine straighte linie konvertiert werden
     */

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Hitbox player;

    private byte[][][] world = new byte[40][40][2];

    private static final int BLOCK_WIDTH = 32;

    private float velocityY = 0;

    private boolean onSlope = false;

    private byte[] collisionMasks = new byte[] {
            0, 0, 0, 2, 0, 0, 3, 1, 0, 4, 0, 1, 5, 1, 1, 0
    };

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.batch = new SpriteBatch();

        this.player = new Hitbox(500, 600, 100, 200, -1);

        for(int i = 0; i < 20; i++)
            placeHitboxBlock(i, 0);

        checkMap();

    }

    private int blockID = 1;

    @Override
    public void render(float delta) {

        Vector2i mouseBlockPosition = new Vector2i(Gdx.input.getX()/(BLOCK_WIDTH*2), (Gdx.graphics.getHeight() - Gdx.input.getY())/(BLOCK_WIDTH*2));

        if(Gdx.input.isButtonJustPressed(0)) placeHitboxBlock(mouseBlockPosition.x, mouseBlockPosition.y);
        if(Gdx.input.isButtonJustPressed(1)) destroyHitboxBLock(mouseBlockPosition.x, mouseBlockPosition.y);

        if(Gdx.input.isKeyPressed(Input.Keys.D)) player.addPosition(250f * delta, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.A)) player.addPosition(-250f * delta, 0);

        Vector2 response = calculateCollisionResponseX();
        player.addPosition(response.x, response.y);

        velocityY -= 250f * delta;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) velocityY = 250f;
        velocityY = Math.max(velocityY, -250f);

        player.addPosition(0, velocityY * delta);
        response = calculateCollisionResponseY();
        if(!response.isZero() && velocityY > 0) velocityY = 0;
        player.addPosition(response.x, response.y);

        response = calculateSlopeYCollision();
        player.addPosition(0, response.y);

        response = calculateSlopeCollision();
        player.addPosition(response.x, 0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[0].length; j++) {
                byte state = world[i][j][1];
                if(state == 0) continue;
                Hitbox hitbox = new Hitbox(i * BLOCK_WIDTH, j * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH, state);
                hitbox.render(batch);
            }
        }
        batch.end();
    }

    private Vector2 calculateCollisionResponseX() {

        float currentResLength = 0;
        Vector2 response = new Vector2(0, 0);

        Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));

        for(int j = 0; j < world[0].length; j++)

            for(int i = 0; i < world.length; i++) {

                byte state = world[i][j][1];

                if(state == 0) continue;
                Hitbox hitbox = new Hitbox(i * BLOCK_WIDTH, j * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH, state);
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap == null) continue;
                hitbox.setColor(Color.RED);
                Vector2 normal = new Vector2(overlap.x, overlap.y);
                Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                Vector2 c1c2 = new Vector2(c1).sub(c2);
                if (c1c2.nor().dot(normal) < 0) normal.scl(-1f);
                Vector2 displacement = new Vector2(normal).scl(overlap.z + 1);

                if(overlap.z >= currentResLength) {
                    currentResLength = overlap.z;
                    if(displacement.x != 0 && displacement.y != 0) {
                        response.set(0, 0);
                    } else response.set(displacement.x, 0);
                }

            }

        return response;
    }

    private Vector2 calculateCollisionResponseY() {

        float responseValue = 0;
        Vector2 response = new Vector2(0, 0);

        Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));

        for(int j = 0; j < world[0].length; j++)

            for(int i = 0; i < world.length; i++) {

                byte state = world[i][j][1];

                if(state == 0) continue;
                Hitbox hitbox = new Hitbox(i * BLOCK_WIDTH, j * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH, state);
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap == null) continue;
                hitbox.setColor(Color.RED);
                Vector2 normal = new Vector2(overlap.x, overlap.y);
                Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                Vector2 c1c2 = new Vector2(c1).sub(c2);
                if (c1c2.nor().dot(normal) < 0) normal.scl(-1f);
                Vector2 displacement = new Vector2(normal).scl(overlap.z + 1);

                if(overlap.z >= responseValue) {
                    responseValue = overlap.z;
                    if(displacement.x != 0 && displacement.y != 0) {
                        onSlope = true;
                        response.set(0, ((displacement.x * displacement.x) / displacement.y) + displacement.y);
                    } else {
                        response.set(0, displacement.y);
                        if(onSlope) onSlope = false;
                    }
                }

            }

        return response;

    }

    // Maybe gibt es wenn man nach links läuft einen Offset von 1 was das ding nach außen drückt?

    private Vector2 calculateSlopeYCollision() {

        float responseValue = 0;
        Vector2 response = new Vector2(0, 0);

        Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));

        for(int j = 0; j < world[0].length; j++) {

            for(int i = 0; i < world.length; i++) {

                byte state = world[i][j][1];

                if (state == 0) continue;
                Hitbox hitbox = new Hitbox(i * BLOCK_WIDTH, j * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH, state);
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if (overlap == null) continue;
                hitbox.setColor(Color.RED);
                Vector2 normal = new Vector2(overlap.x, overlap.y);
                Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                Vector2 c1c2 = new Vector2(c1).sub(c2);
                if (c1c2.nor().dot(normal) < 0)
                    normal.scl(-1f);
                Vector2 displacement = new Vector2(normal).scl(overlap.z + 1);

                if (displacement.y < 0 && onSlope) {
                    if (overlap.z > responseValue) {
                        responseValue = overlap.z;
                        response.set(0, displacement.y);
                    }
                }

            }

        }

        return response;
    }

    private Vector2 calculateSlopeCollision() {

        float currentResLength = 0;
        Vector2 response = new Vector2(0, 0);

        Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));

        for(int j = 0; j < world[0].length; j++)

            for(int i = 0; i < world.length; i++) {

                byte state = world[i][j][1];
                if(state == 0) continue;

                Hitbox hitbox = new Hitbox(i * BLOCK_WIDTH, j * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH, state);
                if(hitbox == null) continue;
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap == null) continue;
                hitbox.setColor(Color.RED);
                Vector2 normal = new Vector2(overlap.x, overlap.y);
                Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                Vector2 c1c2 = new Vector2(c1).sub(c2);
                if (c1c2.nor().dot(normal) < 0) normal.scl(-1f);
                Vector2 displacement = new Vector2(normal).scl(overlap.z + 1);

                //if(hitbox.getState() == 3 || hitbox.getState() == 2) {
                    if(overlap.z > currentResLength && displacement.x != 0) {
                        currentResLength = overlap.z;
                        response.set((displacement.y * displacement.y) / displacement.x + displacement.x, 0);
                        if(onSlope) onSlope = false;
                    }
                //}

            }

        return response;
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
