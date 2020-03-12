package de.undefinedhuman.sandboxgame.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgame.engine.items.ItemManager;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.world.layer.topLayer.TopLayerManager;
import de.undefinedhuman.sandboxgame.world.layer.topLayer.TopLayerType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class WorldLayer {

    // TODO Refactor whole class

    public int width, height;
    public boolean isMain;

    public byte[][] blocks;
    public byte[][] rot;
    public byte[][] state;

    private Color color = new Color();

    public WorldLayer(boolean isMain, int width, int height) {

        this.isMain = isMain;

        this.width = width;
        this.height = height;

        blocks = new byte[this.width][this.height];
        rot = new byte[this.width][this.height];
        state = new byte[this.width][this.height];

    }

    public byte getBlock(Vector2 pos) {

        if (pos.y < height - 1 && pos.y >= 0) {

            if ((pos.x < 0) || (pos.x >= this.width)) {
                pos.x = getPositionX((int) pos.x);
                return this.blocks[(int) pos.x][(int) pos.y];
            } else return this.blocks[(int) pos.x][(int) pos.y];

        }

        return 0;

    }

    private int getPositionX(int x) {

        if (x < 0) x = (width + x);
        else if (x > width - 1) x = (x - width);
        return x;

    }

    public void setBlock(int x, int y, byte cell) {

        if (!(y < 0 || y >= height)) {

            if ((x < 0) || (x >= width)) {

                x = getPositionX(x);
                if (this.blocks[x][y] != cell) this.blocks[x][y] = cell;

            } else if (this.blocks[x][y] != cell) {
                this.blocks[x][y] = cell;
            }

        }

    }

    public WorldLayer setState(int x, int y, int state) {

        if (!(y <= 0 || y >= height)) {

            if ((x < 0) || (x >= width)) {

                x = getPositionX(x);
                if (this.state[x][y] != state) {
                    this.state[x][y] = (byte) (state);
                }

            } else if ((this.state[x][y] != (byte) state)) {
                this.state[x][y] = (byte) (state);
            }

        }

        return this;

    }

    public void setRot(int x, int y, int rot) {

        if (!(y <= 0 || y >= height)) {

            if ((x < 0) || (x >= width)) {

                x = getPositionX(x);
                if (this.rot[x][y] != rot) {
                    this.rot[x][y] = (byte) rot;
                }

            } else if ((this.rot[x][y] != rot)) this.rot[x][y] = (byte) rot;

        }

    }

    public void renderBlock(SpriteBatch batch, Color col, int i, int j) {

        Block block = (Block) ItemManager.instance.getItem(getBlock(i, j));

        if (block != null && block.id != 0) {

            TextureRegion texture = block.blockTextures[getState(i, j)][getRot(i, j)];

            color.set(1 * col.r, 1 * col.g, 1 * col.b, 1f);// TODO Tools.getShadowColor(i, j);
            batch.setColor(color);
            batch.draw(texture, i * 16, j * 16, 8, 8, 16, 16, 1, 1, 0);

            if (block.id == 3 && getBlock(i, j + 1) == 0)
                TopLayerManager.instance.render(batch, i, j, TopLayerType.GRASS, getBlock(i - 1, j) != 0, getBlock(i + 1, j) != 0);

        }

    }

    public byte getBlock(int x, int y) {

        if (y < height - 1 && y >= 0) {

            if ((x < 0) || (x >= this.width)) {
                x = getPositionX(x);
                return this.blocks[x][y];
            } else return this.blocks[x][y];

        }

        return 0;

    }

    public byte getState(int x, int y) {

        x = getPositionX(x);
        if (y < height - 1 && y > 0) {
            return this.state[x][y];
        } else {
            return 0;
        }

    }

    public byte getRot(int x, int y) {

        x = getPositionX(x);
        if (y < height - 1 && y > 0) {
            return this.rot[x][y];
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

}
