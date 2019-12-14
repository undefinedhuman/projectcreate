package de.undefinedhuman.sandboxgame.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class LineWriter {

    private StringBuilder data;

    private boolean base;
    private String separator;

    public LineWriter(boolean base) {
        this(base, Variables.SEPARATOR);
    }

    public LineWriter(boolean base, String separator) {
        data = new StringBuilder();
        this.base = base;
        this.separator = separator;
    }

    public void writeString(String s) {
        writeData(s);
    }
    public void writeInt(int i) {
        writeData(i);
    }
    public void writeFloat(float f) {
        writeData(f);
    }
    public void writeLong(long l) {
        writeData(l);
    }
    public void writeString(double d) {
        writeData(d);
    }
    public void writeBoolean(boolean b) {
        writeData(b ? 1 : 0);
    }

    public void writeVector2(Vector2 v) {
        writeData(v.x);
        writeData(v.y);
    }

    public void writeVector3(Vector3 v) {
        writeData(v.x);
        writeData(v.y);
        writeData(v.z);
    }

    private void writeData(Object o) {
        data.append(base ? Base64Coder.encodeString(String.valueOf(o)) : o).append(separator);
    }

    public String getData() {
        return data.toString();
    }

}
