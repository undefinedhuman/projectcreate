package de.undefinedhuman.sandboxgame.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.world.layer.topLayer.TopLayerManager;
import de.undefinedhuman.sandboxgame.world.layer.topLayer.TopLayerType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class WorldLayer {

    public int width, height;
    public byte[][][] blockData;

    private Color color = new Color();

    private static final byte BLOCK_LAYER_ID = 0;
    private static final byte STATE_LAYER_ID = 1;

    public WorldLayer(int width, int height) {
        this.width = width;
        this.height = height;
        blockData = new byte[width][height][2];
    }

    public byte getBlock(Vector2 pos) {
        return this.getBlock((int) pos.x, (int) pos.y);
    }

    public byte getBlock(int x, int y) {
        if(isOutsideYBounds(y)) return 0;
        return this.blockData[calculateXPosition(x)][y][BLOCK_LAYER_ID];
    }

    public void setBlock(int x, int y, byte cell) {
        if(isOutsideYBounds(y)) return;
        this.blockData[calculateXPosition(x)][y][BLOCK_LAYER_ID] = cell;
    }

    public byte getState(int x, int y) {
        if(getBlock(x, y) == 0 || isOutsideYBounds(y)) return 0;
        return this.blockData[calculateXPosition(x)][y][STATE_LAYER_ID];
    }

    public void setState(int x, int y, byte state) {
        if(isOutsideYBounds(y)) return;
        this.blockData[calculateXPosition(x)][y][STATE_LAYER_ID] = state;
    }

    private int calculateXPosition(float x) {
        return (width + (int) x) % width;
    }

    private boolean isOutsideYBounds(int y) {
        return y < 0 || y >= height;
    }

    public void setLayer(String s) {

        byte[] bytes = Base64Coder.decode(s);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        InflaterInputStream is = new InflaterInputStream(bais);

        try {

            byte[] temp = new byte[1];
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {
                    is.read(temp);
                    blockData[x][y][0] = temp[0];
                }
            is.close();
            bais.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void renderBlock(SpriteBatch batch, Color col, int i, int j) {

        Block block = (Block) ItemManager.instance.getItem(getBlock(i, j));

        if (block != null && block.id.getInt() != 0) {

            color.set(1 * col.r, 1 * col.g, 1 * col.b, 1f);
            batch.setColor(color);
            batch.draw(block.blockTextures[getState(i, j)], i * Variables.BLOCK_SIZE, j * Variables.BLOCK_SIZE, Variables.BLOCK_SIZE, Variables.BLOCK_SIZE);

            if (block.id.getInt() == 3 && getBlock(i, j + 1) == 0)
                TopLayerManager.instance.render(batch, i, j, TopLayerType.GRASS, getBlock(i - 1, j) != 0, getBlock(i + 1, j) != 0);

        }

    }

}
