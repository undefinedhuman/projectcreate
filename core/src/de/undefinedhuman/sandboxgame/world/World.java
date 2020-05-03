package de.undefinedhuman.sandboxgame.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.camera.CameraManager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;

import java.util.Random;

public class World {

    public static World instance;

    public String name;
    public int maxHeight, width, height, seed, blockWidth, blockHeight;
    public WorldLayer mainLayer, backLayer;
    public Random random;

    private Color batchColor = new Color();

    public World(String name, int maxHeight, int width, int height, int seed) {

        this.random = new Random(seed);
        this.mainLayer = new WorldLayer(true, width, height);
        this.backLayer = new WorldLayer(false, width, height);

        this.name = name;
        this.width = width;
        this.maxHeight = maxHeight * Variables.BLOCK_SIZE;
        this.height = height;
        this.blockWidth = width * Variables.BLOCK_SIZE;
        this.blockHeight = height * Variables.BLOCK_SIZE;
        this.seed = seed;

    }

    public void computeBounds(OrthographicCamera camera) {

        /*this.minX = ((int) ((((int) camera.position.x) - camera.zoom * camera.viewportWidth / 2 - Variables.BLOCK_SIZE * 2) / getTileWidth()));
        this.minY = ((int) ((((int) camera.position.y) - camera.zoom * camera.viewportHeight / 2 - Variables.BLOCK_SIZE * 2) / getTileHeight()));
        this.maxX = ((int) ((((int) camera.position.x) + camera.zoom * camera.viewportWidth / 2 + Variables.BLOCK_SIZE * 2) / getTileWidth()));
        this.maxY = ((int) ((((int) camera.position.y) + camera.zoom * camera.viewportHeight / 2 + Variables.BLOCK_SIZE * 2) / getTileHeight()));

        if (this.minY < 0) this.minY = 0;
        if (this.maxY > this.height - 2) this.maxY = (this.height - 2);
        if (minX < -width + 2) minX = -width + 2;
        if (maxX > width * 2 - 2) maxX = width * 2 - 2;*/

    }

    public float getTileWidth() {
        return Variables.BLOCK_SIZE;
    }

    public float getTileHeight() {
        return Variables.BLOCK_SIZE;
    }

    public void renderMainLayer(SpriteBatch batch) {
        for (int i = CameraManager.instance.blockBounds.x; i <= CameraManager.instance.blockBounds.z; i++)
            for (int j = CameraManager.instance.blockBounds.y; j <= CameraManager.instance.blockBounds.w; j++)
                mainLayer.renderBlock(batch, batchColor.set(Color.WHITE), i, j);
    }

    public void renderBackLayer(SpriteBatch batch) {
        for (int i = CameraManager.instance.blockBounds.x; i <= CameraManager.instance.blockBounds.z; i++)
            for (int j = CameraManager.instance.blockBounds.y; j <= CameraManager.instance.blockBounds.w; j++) {
                Block block = (Block) ItemManager.instance.getItem(World.instance.mainLayer.getBlock(i, j));
                if (block.id.getInt() == 0 || mainLayer.getState(i, j) != 0 || !block.isFull.getBoolean())
                    backLayer.renderBlock(batch, batchColor.set(0.45f, 0.45f, 0.45f, 1), i, j);
            }
    }

}
