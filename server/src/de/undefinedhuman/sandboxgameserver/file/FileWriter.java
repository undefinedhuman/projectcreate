package de.undefinedhuman.sandboxgameserver.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.utils.Variables;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

    private String seperator = Variables.SEPARATOR;

    private BufferedWriter writer;
    private boolean base;

    public FileWriter(FsFile file, boolean base) {
        this.base = base;
        writer = FileManager.getBufferedWriter(file);
    }

    public FileWriter(FsFile file, boolean base, boolean append) {
        this.base = base;
        writer = FileManager.getBufferedWriter(file, append);
    }

    public FileWriter(FsFile file, boolean base, String seperator) {
        this.seperator = seperator;
        this.base = base;
        writer = FileManager.getBufferedWriter(file);
    }

    public FileWriter(FsFile file, boolean base, boolean append, String seperator) {

        this.seperator = seperator;

        this.base = base;
        writer = FileManager.getBufferedWriter(file, append);

    }

    public void writeString(String s) {
        writeValue(s);
    }

    public void writeFloat(float f) {
        writeValue(f);
    }

    public void writeInt(int i) {
        writeValue(i);
    }

    public void writeLong(long l) {
        writeValue(l);
    }

    public void writeBoolean(boolean b) {
        writeValue(FileManager.booleanToInt(b));
    }

    public void writeDouble(double d) {
        writeValue(d);
    }

    public void writeVector2(Vector2 vector) {

        writeFloat(vector.x);
        writeFloat(vector.y);

    }

    public void nextLine() {

        try {
            writer.newLine();
        } catch (IOException ex) {
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    public void writeValue(Object v) {

        try {
            writer.write(base ? Base64Coder.encodeString(String.valueOf(v)) + this.seperator : v + this.seperator);
        } catch (IOException ex) {
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    public void close() {
        FileManager.closeWriter(writer);
    }

}
