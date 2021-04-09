package de.undefinedhuman.projectcreate.core.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.camera.CameraManager;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.core.item.ItemManager;
import de.undefinedhuman.projectcreate.core.world.layer.topLayer.TopLayerManager;
import de.undefinedhuman.projectcreate.core.world.layer.topLayer.TopLayerType;
import de.undefinedhuman.projectcreate.engine.items.type.blocks.Block;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.screen.CollisionUtils;
import de.undefinedhuman.projectcreate.engine.collision.Hitbox;

import java.util.Random;

public class World {

    public static final byte MAIN_LAYER = 0;
    public static final byte BACK_LAYER = 2;

    private static final byte BLOCK_LAYER = 0;
    private static final byte STATE_LAYER = 1;

    public static final byte COLLISION_BASE_LAYER = 0;
    public static final byte COLLISION_STATE_LAYER = 1;

    public static World instance;

    public String name;
    public int maxHeight, seed;
    public Vector2i size = new Vector2i(), pixelSize = new Vector2i(), collisionSize = new Vector2i();
    public Random random = new Random();

    private Color batchColor = new Color();

    private byte[][][] blockLayer;       // Y, X, DATA LAYER (BLOCK, STATE)
    private byte[][][] collisionLayer;   // Y, X, COLLISION LAYER

    public World(String name, int maxHeight, int width, int height, int seed) {
        this.name = name;
        this.random.setSeed(this.seed = seed);
        this.maxHeight = maxHeight * Variables.BLOCK_SIZE;

        this.size.set(width, height);
        this.pixelSize.set(width * Variables.BLOCK_SIZE, height * Variables.BLOCK_SIZE);
        this.collisionSize.set(width*2, height*2);

        this.blockLayer = new byte[size.y][size.x][4];
        this.collisionLayer = new byte[collisionSize.y][collisionSize.x][2];
    }

    public void renderMainLayer(SpriteBatch batch) {
        for (int i = CameraManager.getInstance().blockBounds.x; i <= CameraManager.getInstance().blockBounds.z; i++)
            for (int j = CameraManager.getInstance().blockBounds.y; j <= CameraManager.getInstance().blockBounds.w; j++) {
                renderBlock(batch, Color.WHITE, i, j, MAIN_LAYER);
            }

        if(Variables.DEBUG)
            for (int i = CameraManager.getInstance().blockBounds.x*2; i <= CameraManager.getInstance().blockBounds.z*2; i++)
                for (int j = CameraManager.getInstance().blockBounds.y*2; j <= CameraManager.getInstance().blockBounds.w*2; j++) {
                    byte state = getCollision(i, j, COLLISION_STATE_LAYER);
                    if(state == 0) continue;
                    Hitbox hitbox = CollisionUtils.blockCollisionMask[state];
                    hitbox.update(i * Variables.COLLISION_SIZE, j * Variables.COLLISION_SIZE);
                    hitbox.render(batch);
                }
    }

    public void renderBackLayer(SpriteBatch batch) {
        for (int i = CameraManager.getInstance().blockBounds.x; i <= CameraManager.getInstance().blockBounds.z; i++)
            for (int j = CameraManager.getInstance().blockBounds.y; j <= CameraManager.getInstance().blockBounds.w; j++) {
                Block block = (Block) ItemManager.getInstance().getItem(getBlock(i, j, MAIN_LAYER));
                if (block.id.getInt() != 0 || getState(i, j, MAIN_LAYER) == 0 || block.isFull.getBoolean()) return;
                renderBlock(batch, batchColor.set(0.45f, 0.45f, 0.45f, 1), i, j, BACK_LAYER);
            }
    }

    private void renderBlock(SpriteBatch batch, Color color, int x, int y, byte worldLayer) {
        Block block = (Block) ItemManager.getInstance().getItem(getBlock(x, y, worldLayer));
        if (block == null || block.id.getInt() == 0) return;

        batch.setColor(color);
        batch.draw(block.blockTextures[getState(x, y, worldLayer)], x * Variables.BLOCK_SIZE, y * Variables.BLOCK_SIZE, Variables.BLOCK_SIZE, Variables.BLOCK_SIZE);

        if (block.id.getInt() == 3 && getBlock(x, y + 1, worldLayer) == 0)
            TopLayerManager.instance.render(batch, x, y, TopLayerType.GRASS, getBlock(x - 1, y, worldLayer) != 0, getBlock(x + 1, y, worldLayer) != 0);
    }

    public byte getBlock(int x, int y, byte worldLayer) {
        if(y < 0 || y >= size.y) return 0;
        return getBlockData(x, y, worldLayer, BLOCK_LAYER);
    }

   public void setBlock(int x, int y, byte worldLayer, byte blockID) {
        if(y < 0 || y >= size.y) return;
        setBlockData(x, y, worldLayer, BLOCK_LAYER, blockID);
        if(worldLayer == MAIN_LAYER) setCollisionBlock(x, y, blockID != 0 && ((Block) ItemManager.getInstance().getItem(blockID)).hasCollision.getBoolean());
    }

    public byte getState(int x, int y, byte worldLayer) {
        if(getBlock(x, y, worldLayer) == 0) return 0;
        return getBlockData(x, y, worldLayer, STATE_LAYER);
    }

    public void setState(int x, int y, byte worldLayer, byte state) {
        if(y < 0 || y >= size.y) return;
        setBlockData(x, y, worldLayer, STATE_LAYER, state);
    }

    public byte getCollision(int x, int y, byte collisionLayer) {
        if(y < 0 || y >= collisionSize.y) return 1;
        return this.collisionLayer[y][calculateXPosition(x, collisionSize.x)][collisionLayer];
    }

    public void setCollision(int x, int y, byte collisionLayer, byte collisionState) {
        if(y < 0 || y >= collisionSize.y) return;
        this.collisionLayer[y][calculateXPosition(x, collisionSize.x)][collisionLayer] = collisionState;
    }

    private void setCollisionBlock(int blockX, int blockY, boolean solid) {
        for(int i = 0; i <=1; i++)
            for(int j = 0; j <=1; j++)
                setCollision(blockX*2+i, blockY*2+j, COLLISION_BASE_LAYER, (byte) (solid ? 1 : 0));
    }

    private byte getBlockData(int x, int y, byte worldLayer, byte dataLayer) {
        return this.blockLayer[y][calculateXPosition(x, size.x)][worldLayer + dataLayer];
    }

    private void setBlockData(int x, int y, byte worldLayer, byte dataLayer, byte blockID) {
        this.blockLayer[y][calculateXPosition(x, size.x)][worldLayer + dataLayer] = blockID;
    }

    private int calculateXPosition(int x, int width) {
        return (width + x) % width;
    }

}
