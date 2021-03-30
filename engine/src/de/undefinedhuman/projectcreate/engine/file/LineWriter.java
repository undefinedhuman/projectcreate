package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

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

    private void writeData(Object o) {
        data.append(base ? Base64Coder.encodeString(String.valueOf(o)) : o).append(separator);
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

    public void writeVector2(Vector2 vector) {
        writeData(vector.x);
        writeData(vector.y);
    }

    public void writeVector3(Vector3 vector) {
        writeData(vector.x);
        writeData(vector.y);
        writeData(vector.z);
    }

    public String getData() {
        return data.toString();
    }

}
