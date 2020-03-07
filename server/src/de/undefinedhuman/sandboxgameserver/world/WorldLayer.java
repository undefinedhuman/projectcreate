package de.undefinedhuman.sandboxgameserver.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgameserver.log.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class WorldLayer {

    public int width, height;
    public boolean isMain;

    public byte[][] blocks;

    public WorldLayer(boolean isMain, int width, int height) {

        this.isMain = isMain;

        this.width = width;
        this.height = height;

        blocks = new byte[this.width][this.height];

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

    private int getPositionX(int x) {

        if (x < 0) x = (this.width + x);
        else if (x > this.width - 1) x = (x - this.width);
        return x;

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

    public void setBlock(int x, int y, byte cell) {

        if (!(y < 0 || y >= this.height)) {

            if ((x < 0) || (x >= this.width)) {

                x = getPositionX(x);
                if (this.blocks[x][y] != cell) this.blocks[x][y] = cell;

            } else if (this.blocks[x][y] != cell) {
                this.blocks[x][y] = cell;
            }

        }

    }

    public String getLayer() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);

        String s = "";

        try {

            for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) dos.write(blocks[x][y] & 0x000000FF);
            dos.finish();
            s = new String(Base64Coder.encode(baos.toByteArray()));
            baos.close();

        } catch (IOException e) {
            Log.instance.error(e.getMessage());
        }

        return s;

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
