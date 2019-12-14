package de.undefinedhuman.sandboxgame.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.utils.Variables;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

    private String separator = Variables.SEPARATOR;

    private BufferedWriter writer;
    private boolean base;

    public FileWriter(FsFile file, boolean base) {

        this.base = base;
        writer = FileUtils.getBufferedWriter(file);

    }

    public FileWriter(FsFile file, boolean base, String separator) {

        this.separator = separator;

        this.base = base;
        writer = FileUtils.getBufferedWriter(file);

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
        writeValue(FileUtils.booleanToInt(b));
    }
    public void writeDouble(double d) {
        writeValue(d);
    }

    public void writeVector2(Vector2 vec2) {
        writeFloat(vec2.x);
        writeFloat(vec2.y);
    }

    public void writeVector3(Vector3 vec3) {
        writeFloat(vec3.x);
        writeFloat(vec3.y);
        writeFloat(vec3.z);
    }

    public void nextLine() {
        try { writer.newLine(); } catch(IOException ex) { Log.instance.crash(ex.getMessage()); }
    }

    public void writeValue(Object v) {
        try { writer.write(base ? Base64Coder.encodeString(String.valueOf(v)) + this.separator : v + this.separator); } catch (IOException ex) { Log.instance.crash(ex.getMessage()); }
    }

    public void close() {
        FileUtils.closeWriter(writer);
    }

}
