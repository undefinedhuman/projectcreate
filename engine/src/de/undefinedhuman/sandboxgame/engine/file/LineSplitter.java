package de.undefinedhuman.sandboxgame.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public class LineSplitter {

    private int pointer = 0;
    private String[] data;

    private boolean base;
    private String separator;

    public LineSplitter(String string, boolean base) {
        this(string, base, Variables.SEPARATOR);
    }

    public LineSplitter(String string, boolean base, String separator) {
        this.data = string.split(separator);
        this.separator = separator;
        this.base = base;
    }

    public String getNextString() {
        String s = getNextData();
        return s != null ? s : "";
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
        return s != null ? Integer.parseInt(s) : 0;
    }

    public long getNextLong() {
        String s = getNextData();
        return s != null ? Long.parseLong(s) : 0;
    }

    public double getNextDouble() {
        String s = getNextData();
        return s != null ? Double.parseDouble(s) : 0;
    }

    public boolean getNextBoolean() {
        String s = getNextData();
        return s != null && FileUtils.readBoolean(s);
    }

    public Vector2 getNextVector2() {
        return new Vector2(getNextFloat(), getNextFloat());
    }

    public float getNextFloat() {
        String s = getNextData();
        return s != null ? Float.parseFloat(s) : 0;
    }

    public Vector3 getNextVector3() {
        return new Vector3(getNextFloat(), getNextFloat(), getNextFloat());
    }

    public String getData() {
        StringBuilder builder = new StringBuilder();
        while (hasMoreValues()) builder.append(getNextData()).append(separator);
        return builder.toString();
    }

    public String getRemainingRawData() {
        StringBuilder builder = new StringBuilder();
        while(hasMoreValues()) builder.append(this.data[pointer++]).append(separator);
        return builder.toString();
    }

    public String getDataString() {
        StringBuilder builder = new StringBuilder();
        for (String s : data) builder.append(s);
        return builder.toString();
    }

    public void setLine(String string, String separator) {
        this.data = string.split(separator);
        pointer = 0;
    }

}
