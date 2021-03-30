package de.undefinedhuman.projectcreate.core.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.projectcreate.core.engine.log.Log;
import de.undefinedhuman.projectcreate.core.engine.utils.Variables;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

    private FsFile file;
    private String separator;
    private BufferedWriter writer;
    private boolean base;

    public FileWriter(FsFile file, boolean base) {
        this(file, base, Variables.SEPARATOR);
    }

    public FileWriter(FsFile file, boolean base, String separator) {
        this.separator = separator;
        this.base = base;
        this.file = file;
        writer = FileUtils.getBufferedWriter(file);
    }

    public FileWriter writeString(String s) {
        return writeValue(s);
    }
    public FileWriter writeInt(int i) {
        return writeValue(i);
    }
    public FileWriter writeFloat(float f) {
        return writeValue(f);
    }
    public FileWriter writeLong(long l) {
        return writeValue(l);
    }
    public FileWriter writeBoolean(boolean b) {
        return writeValue(FileUtils.booleanToInt(b));
    }
    public FileWriter writeDouble(double d) {
        return writeValue(d);
    }
    public FileWriter writeVector2(Vector2 vector) {
        return writeFloat(vector.x).writeFloat(vector.y);
    }

    public FsFile getParentDirectory() {
        return new FsFile(file.parent().path(), file.type());
    }

    public FileWriter writeValue(Object v) {
        try { writer.write(base ? Base64Coder.encodeString(String.valueOf(v)) + this.separator : v + this.separator);
        } catch (IOException ex) { Log.instance.exit(ex.getMessage()); }
        return this;
    }

    public FileWriter nextLine() {
        try { writer.newLine();
        } catch (IOException ex) { Log.instance.exit(ex.getMessage()); }
        return this;
    }

    public void close() {
        FileUtils.closeWriter(writer);
    }

}
