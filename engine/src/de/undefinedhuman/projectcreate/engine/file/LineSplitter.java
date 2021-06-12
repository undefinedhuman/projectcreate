package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

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

    public boolean hasMoreValues() {
        return this.pointer < this.data.length;
    }

    public int getNextInt() {
        String s = getNextData();
        Integer i;
        if(s == null || (i = Tools.isInteger(s)) == null)
            return 0;
        return i;
    }

    public long getNextLong() {
        String s = getNextData();
        Long l;
        if(s == null || (l = Tools.isLong(s)) == null)
            return 0;
        return l;
    }

    public double getNextDouble() {
        String s = getNextData();
        Double d;
        if(s == null || (d = Tools.isDouble(s)) == null)
            return 0;
        return d;
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

    public LineSplitter getDataAsLineSplitter() {
        StringBuilder builder = new StringBuilder();
        while (hasMoreValues())
            builder.append(getNextData()).append(separator);
        return new LineSplitter(builder.toString(), false, separator);
    }

    public String getData() {
        StringBuilder builder = new StringBuilder();
        while (hasMoreValues())
            builder.append(getNextData()).append(separator);
        return builder.toString();
    }

    public String getRawData() {
        StringBuilder builder = new StringBuilder();
        while(hasMoreValues()) builder.append(this.data[pointer++]).append(separator);
        return builder.toString();
    }

    private String getNextData() {
        if (hasMoreValues()) {
            String s = this.data[this.pointer++];
            return (base ? Base64Coder.decodeString(s) : s);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < data.length; i++)
            builder.append((base ? Base64Coder.decodeString(data[i]) : data[i])).append(i != data.length - 1 ? ", " : "");
        return builder.toString();
    }

}
