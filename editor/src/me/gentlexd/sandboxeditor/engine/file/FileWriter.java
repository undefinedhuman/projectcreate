package me.gentlexd.sandboxeditor.engine.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.utils.Base64Coder;
import me.gentlexd.sandboxeditor.utils.Variables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

    private String seperator = Variables.SEPARATOR;

    private BufferedWriter writer;
    private boolean base;

    public FileWriter(FileHandle file, boolean base) {

       this. writer = new BufferedWriter(file.writer(false));
        this.base = base;

    }

    public FileWriter(FsFile file, boolean base) {

        this.base = base;
        writer = FileManager.getBufferedWriter(file);

    }

    public FileWriter(FsFile file, boolean base, String seperator) {

        this.seperator = seperator;

        this.base = base;
        writer = FileManager.getBufferedWriter(file);

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

        try {
            writer.newLine();
        } catch(IOException ex) {
            Log.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    private void writeValue(Object v) {

        try {
            writer.write(base ? Base64Coder.encodeString(String.valueOf(v)) + this.seperator : v + this.seperator);
        } catch (IOException ex) {
            Log.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    public void close() {

        FileManager.closeWriter(writer);

    }

}
