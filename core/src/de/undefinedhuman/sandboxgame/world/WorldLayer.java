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

    public byte[][] blocks;
    public byte[][] state;

    private Color color = new Color();

    public WorldLayer(int width, int height) {
        this.width = width;
        this.height = height;
        blocks = new byte[this.width][this.height];
        state = new byte[this.width][this.height];
    }

    private int getPositionX(float x) {
        return (width + (int) x) % width;
    }

    public void setBlock(int x, int y, byte cell) {

        if (!(y < 0 || y >= height)) {

            if ((x < 0) || (x >= width)) {

                x = getPositionX(x);
                if (this.blocks[x][y] != cell) {
                    this.blocks[x][y] = cell;
                }

            } else if ((this.blocks[x][y] != cell)) {
                this.blocks[x][y] = cell;
            }

        }
    }

    public void setState(int x, int y, byte state) {

        if (!(y < 0 || y >= height)) {

            if ((x < 0) || (x >= width)) {

                x = getPositionX(x);
                if (this.state[x][y] != state) {
                    this.state[x][y] = state;
                }

            } else if ((this.state[x][y] != state)) {
                this.state[x][y] = state;
            }

        }

    }

    public byte getBlock(Vector2 pos) {
        return this.getBlock((int) pos.x, (int) pos.y);
    }

    public byte getBlock(int x, int y) {

        if (y < height && y >= 0) {
            if ((x < 0) || (x >= this.width)) x = getPositionX(x);
            return this.blocks[x][y];
        }

        return 0;

    }

    public byte getState(int x, int y) {

        if(getBlock(x, y) == 0) return 0;

        x = getPositionX(x);
        if (y < height && y >= 0) {
            return this.state[x][y];
        } else {
            return 0;
        }

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
                    blocks[x][y] = temp[0];
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
