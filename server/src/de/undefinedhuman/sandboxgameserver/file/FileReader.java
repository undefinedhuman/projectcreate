package de.undefinedhuman.sandboxgameserver.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.utils.Variables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {

    private LineSplitter splitter;
    private BufferedReader reader;

    private boolean base;
    private String separator = "", fileName;

    public FileReader(FileHandle file, boolean base) {

        this.reader = new BufferedReader(file.reader());
        this.base = base;
        this.fileName = file.name();

    }

    public FileReader(FsFile file, boolean base) {

        this.reader = FileManager.getBufferedReader(file);
        this.base = base;
        this.fileName = file.getName();

    }

    public FileReader(FsFile file, boolean base, String separator) {

        this.reader = FileManager.getBufferedReader(file);
        this.base = base;
        this.separator = separator;
        this.fileName = file.getName();

    }

    public FileReader(InputStream stream, boolean base) {

        this.reader = FileManager.getBufferedReader(stream);
        this.base = base;
        this.fileName = "";

    }

    public String nextLine() {

        String line = null;

        try {
            line = reader.readLine();
        } catch (IOException ex) {
            Log.instance.error(ex.getMessage());
        }

        if (line != null) {
            splitter = new LineSplitter(line, base, separator.equalsIgnoreCase("") ? Variables.SEPARATOR : separator);
            return line;
        }

        return null;

    }

    public String getNextString() {
        return splitter.getNextString();
    }

    public int getNextInt() {
        return splitter.getNextInt();
    }

    public float getNextFloat() {
        return splitter.getNextFloat();
    }

    public long getNextLong() {
        return splitter.getNextLong();
    }

    public double getNextDouble() {
        return splitter.getNextDouble();
    }

    public boolean getNextBoolean() {
        return splitter.getNextBoolean();
    }

    public Vector2 getNextVector2() {
        return new Vector2(splitter.getNextFloat(), splitter.getNextFloat());
    }

    public int getID() {
        String[] data = fileName.split("-");
        return Integer.parseInt(data[0]);
    }

    public boolean isEndOfLine() {
        return !splitter.hasMoreValues();
    }

    public void close() {
        FileManager.closeReader(reader);
    }

}
