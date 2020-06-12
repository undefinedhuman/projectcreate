package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;

import java.util.ArrayList;

public class CollisionScreen implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Hitbox player;

    private Hitbox[][] world = new Hitbox[20][10];

    private static final int BLOCK_WIDTH = 64;

    private float velocityY = 0;

    // When implementing make sure, blocks outside the world bounds will be hitboxes and not simple if conditions, NOT y < 0 = 0 do full BLOCK Collision

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.batch = new SpriteBatch();

        this.player = new Hitbox(500, 600, 100, 200, 0);

        for(int i = 0; i < 20; i++)
            world[i][0] = new Hitbox(i*BLOCK_WIDTH, 0, BLOCK_WIDTH, BLOCK_WIDTH, 1);

        //                          min(x, max(x, y))
        // -100, 0    -> -100       min(-100, max(-100, 0)) -> min(-100, 0) -> -100
        // -200, -100 -> -200       min(-200, max(-200, -100)) -> min(-200, -100) -> -200
        // -100, -200 -> -200       min(-100, max(-100, -200)) -> min(-100, -200) -> -100
        // 100, 0     -> 100        min(100, max(100, 0)) -> min(100, 0) -> 100
        // 100, 200   -> 200        min(100, max(200, 100)) -> min(100, 100) -> 100 X

        // 200, -100  -> 100        max(100, min(100, 0)) -> max(100, 0) -> 100
        // -200, 100  -> -100       (x, y) -> x + y

        Log.info(-100, 0, calculateOffset(-100, 0), -100);
        Log.info(-200, -100, calculateOffset(-200, -100), -200);
        Log.info(100, 0, calculateOffset(100, 0), 100);
        Log.info(100, 200, calculateOffset(100, 200), 200);

        Log.info(200, -100, calculateOffset(200, -100), 100);
        Log.info(-200, 100, calculateOffset(-200, 100), -100);

    }

    private int blockID = 1;

    @Override
    public void render(float delta) {

        Vector2i mouseBlockPosition = new Vector2i(Gdx.input.getX()/BLOCK_WIDTH, (Gdx.graphics.getHeight() - Gdx.input.getY())/BLOCK_WIDTH);

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) blockID = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) blockID = 2;
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)) blockID = 3;
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)) blockID = 4;
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)) blockID = 5;

        if(Gdx.input.isButtonJustPressed(0)) world[mouseBlockPosition.x][mouseBlockPosition.y] = new Hitbox(mouseBlockPosition.x * BLOCK_WIDTH, mouseBlockPosition.y * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH, blockID);
        if(Gdx.input.isButtonJustPressed(1)) world[mouseBlockPosition.x][mouseBlockPosition.y] = null;

        for(Hitbox[] hitboxList : world) {
            for(Hitbox hitbox : hitboxList) {
                if(hitbox == null) continue;
                hitbox.setColor(Variables.HITBOX_COLOR);
            }
        }

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
        player.addPosition(0, response.y);

        player.getPosition().y = Math.max(player.getPosition().y, 0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        for(Hitbox[] hitboxList : world)
            for(Hitbox hitbox : hitboxList) {
                if(hitbox == null) continue;
                hitbox.render(batch);
            }
        batch.end();
    }

    private Vector2 calculateCollisionResponseX() {

        float currentResLength = 0;
        Vector2 response = new Vector2(0, 0);

        ArrayList<Vector3> responses = new ArrayList<>();

        Log.info("---");

        Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));

        for(Hitbox[] hitboxList : world)
            for(Hitbox hitbox : hitboxList) {
                if(hitbox == null) continue;
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap == null) continue;
                hitbox.setColor(Color.RED);
                Vector2 normal = new Vector2(overlap.x, overlap.y);
                Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                Vector2 c1c2 = new Vector2(c1).sub(c2);
                if (c1c2.nor().dot(normal) < 0) normal.scl(-1f);
                Vector2 displacement = new Vector2(normal).scl(overlap.z < 0 ? overlap.z - 1 : overlap.z + 1);

                if(overlap.z > currentResLength) {
                    currentResLength = overlap.z;
                    if((hitbox.getState() == 2 || hitbox.getState() == 3) && (displacement.x != 0 && displacement.y != 0))
                        response.y = ((displacement.x * displacement.x) / displacement.y) + displacement.y;
                    else response.set(displacement.x, 0);
                }

            }

        Log.info("---");

        return response;
    }

    private Vector2 calculateCollisionResponseY() {

        float currentResLength = 0;
        Vector2 response = new Vector2(0, 0);

        Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));

        for(Hitbox[] hitboxList : world)
            for(Hitbox hitbox : hitboxList) {
                if(hitbox == null) continue;
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap == null) continue;
                hitbox.setColor(Color.RED);
                Vector2 normal = new Vector2(overlap.x, overlap.y);
                Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                Vector2 c1c2 = new Vector2(c1).sub(c2);
                if (c1c2.nor().dot(normal) < 0) normal.scl(-1f);
                Vector2 displacement = new Vector2(normal).scl(overlap.z < 0 ? overlap.z - 1 : overlap.z + 1);

                if(overlap.z > currentResLength) {
                    currentResLength = overlap.z;
                    if((hitbox.getState() == 2 || hitbox.getState() == 3) && (displacement.x != 0 && displacement.y != 0))
                        response.y = ((displacement.x * displacement.x) / displacement.y) + displacement.y;
                    response.set(0, displacement.y);
                }

            }

        return response;
    }

    private float calculateOffset(int x, int y) {
        return Math.max(x, Math.min(y, x));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    /*

            for(Hitbox[] hitboxList : world)
            for(Hitbox hitbox : hitboxList) {
                if(hitbox == null) continue;
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap != null) {
                    hitbox.setColor(Color.RED);
                    Vector2 normal = new Vector2(overlap.x, overlap.y);
                    Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));
                    Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                    Vector2 c1c2 = new Vector2(c1).sub(c2);
                    if (c1c2.dot(normal) < 0) normal.scl(-1f);

                    Vector2 displacement = new Vector2(normal).scl(overlap.z + 1);

                    player.addPosition(displacement.x, displacement.y);

                    //response.y = Math.max(response.y, displacement.y);
                    //response.set(Math.max(response.x, displacement.x), Math.max(response.y, displacement.y));

                if(overlap.z != 0) {
                    if(hitbox.getState() == 3 || hitbox.getState() == 2)
                        player.addPosition(0, ((displacement.x * displacement.x) / displacement.y) + displacement.y);
                    else player.addPosition(displacement);

                }

    //player.addPosition(0, displacement.y);

     */

    /*

            for(Hitbox[] hitboxList : world)
            for(Hitbox hitbox : hitboxList) {
                if(hitbox == null) continue;
                Vector3 overlap = Hitbox.collideSAT(player, hitbox);
                if(overlap != null) {
                    hitbox.setColor(Color.RED);
                    Vector2 normal = new Vector2(overlap.x, overlap.y);
                    Vector2 c1 = new Vector2(player.getPosition()).add(new Vector2(player.getSize()).scl(0.5f));
                    Vector2 c2 = new Vector2(hitbox.getPosition()).add(new Vector2(hitbox.getSize()).scl(0.5f));
                    Vector2 c1c2 = new Vector2(c1).sub(c2);
                    if (c1c2.dot(normal) < 0) normal.scl(-1f);

                    Vector2 displacement = new Vector2(normal).scl(overlap.z + 1);

                if(overlap.z != 0) {
                    if(hitbox.getState() == 3 || hitbox.getState() == 2)
                        player.addPosition(0, ((displacement.x * displacement.x) / displacement.y) + displacement.y);
                    else player.addPosition(displacement);

                }

                    if(displacement.x != 0 && displacement.y != 0) {
        response.set(0, Math.max(response.y, displacement.y));
    } else
            response.set(Math.max(response.x, displacement.x), Math.max(response.y, displacement.y));


                if(overlap.z != 0) {
                    if(displacement.x != 0 && displacement.y != 0) {
                        //player.addPosition(0, ((displacement.x * displacement.x) / displacement.y) + displacement.y);
                    } //player.addPosition(displacement);
                //}

}
            }

     */

}
