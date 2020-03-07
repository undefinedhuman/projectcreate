package de.undefinedhuman.sandboxgameserver.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;

public class LineSplitter {

    private int pointer = 0;
    private String[] data;

    private boolean base;

    public LineSplitter(String string, boolean base, String separator) {
        this.data = string.split(separator);
        this.base = base;
    }

    public String getNextString() {

        String s = getNextData();

        if (s != null) return s;
        return "";

    }

    private String getNextData() {

        if (hasMoreValues()) {
            String s = this.data[this.pointer++];
            return (base ? Base64Coder.decodeString(s) : s);
        }

        return null;

    }

    public boolean hasMoreValues() {

        return this.pointer < this.data.length;

    }

    public int getNextInt() {

        String s = getNextData();

        if (s != null) return Integer.parseInt(s);
        return 0;

    }

    public long getNextLong() {

        String s = getNextData();

        if (s != null) return Long.parseLong(s);
        return 0;

    }

    public double getNextDouble() {

        String s = getNextData();

        if (s != null) return Double.parseDouble(s);
        return 0;

    }

    public boolean getNextBoolean() {

        String s = getNextData();

        if (s != null) return FileManager.readBoolean(s);
        return false;

    }

    public Vector2 getNextVector2() {
        return new Vector2(getNextFloat(), getNextFloat());
    }

    public float getNextFloat() {

        String s = getNextData();

        if (s != null) return Float.parseFloat(s);
        return 0;

    }

}
