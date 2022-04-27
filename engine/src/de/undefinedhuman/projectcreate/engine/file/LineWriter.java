package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class LineWriter {

    // TODO WRITE FUNCTIONS THAT CAN SEND ONE AND TWO DIMENSIONAL ARRAYS

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

    private LineWriter writeData(Object o) {
        data.append(base ? Base64Coder.encodeString(String.valueOf(o)) : o).append(separator);
        return this;
    }

    public LineWriter writeString(String s) {
        return writeData(s);
    }

    public LineWriter writeInt(int i) {
        return writeData(i);
    }

    public LineWriter writeFloat(float f) {
        return writeData(f);
    }

    public LineWriter writeLong(long l) {
        return writeData(l);
    }

    public LineWriter writeString(double d) {
        return writeData(d);
    }

    public LineWriter writeBoolean(boolean b) {
        return writeData(b ? 1 : 0);
    }

    public LineWriter writeVector2(Vector2 vector) {
        writeData(vector.x);
        writeData(vector.y);
        return this;
    }

    public LineWriter writeVector3(Vector3 vector) {
        writeData(vector.x);
        writeData(vector.y);
        writeData(vector.z);
        return this;
    }

    public String getData() {
        return data.toString();
    }

    public LineWriter clear() {
        data.setLength(0);
        return this;
    }

}
