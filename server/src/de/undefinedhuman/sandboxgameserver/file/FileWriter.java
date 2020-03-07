package de.undefinedhuman.sandboxgameserver.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.utils.Variables;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

    private String separator = Variables.SEPARATOR;

    private BufferedWriter writer;
    private boolean base;

    public FileWriter(FsFile file, boolean base) {
        this(file, base, false, Variables.SEPARATOR);
    }

    public FileWriter(FsFile file, boolean base, boolean append) {
        this(file, base, append, Variables.SEPARATOR);
    }

    public FileWriter(FsFile file, boolean base, String separator) {
        this(file, base, false, separator);
    }

    public FileWriter(FsFile file, boolean base, boolean append, String separator) {
        this.separator = separator;
        this.base = base;
        writer = FileManager.getBufferedWriter(file, append);
    }

    public FileWriter writeString(String s) {
        return writeValue(s);
    }

    public FileWriter writeInt(int i) {
        return writeValue(i);
    }

    public FileWriter writeLong(long l) {
        return writeValue(l);
    }

    public FileWriter writeBoolean(boolean b) {
        return writeValue(FileManager.booleanToInt(b));
    }

    public FileWriter writeDouble(double d) {
        return writeValue(d);
    }

    public FileWriter writeVector2(Vector2 vector) {
        writeFloat(vector.x);
        writeFloat(vector.y);
        return this;
    }

    public FileWriter writeFloat(float f) {
        return writeValue(f);
    }

    public FileWriter writeValue(Object v) {
        try {
            writer.write(base ? Base64Coder.encodeString(String.valueOf(v)) + this.separator : v + this.separator);
        } catch (IOException ex) {
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
        }
        return this;
    }

    public FileWriter nextLine() {
        try {
            writer.newLine();
        } catch (IOException ex) {
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
        }
        return this;
    }

    public void close() {
        FileManager.closeWriter(writer);
    }

}
