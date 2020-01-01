package de.undefinedhuman.sandboxgame.engine.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.utils.Variables;

import java.io.BufferedReader;
import java.io.IOException;

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

        this.reader = FileUtils.getBufferedReader(file);
        this.base = base;
        this.fileName = file.getName();

    }

    public FileReader(FsFile file, boolean base, String separator) {

        this.reader = FileUtils.getBufferedReader(file);
        this.base = base;
        this.separator = separator;
        this.fileName = file.getName();

    }

    public String nextLine() {

        String line = null;

        try { line = reader.readLine(); } catch(IOException ex) { Log.error(ex.getMessage()); }

        if(line != null) {
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

    public Vector2 getNextVector2() {
        return splitter.getNextVector2();
    }

    public Vector3 getNextVector3() {
        return splitter.getNextVector3();
    }

    public double getNextDouble() {
        return splitter.getNextDouble();
    }

    public boolean getNextBoolean() {
        return splitter.getNextBoolean();
    }

    public String getData() {
        StringBuilder builder = new StringBuilder();
        while(!isEndOfLine()) builder.append(getNextString()).append(";");
        return builder.toString();
    }

    public int getID() {
        String[] data = fileName.split("-");
        return Integer.parseInt(data[0]);
    }

    public boolean isEndOfLine() {
        return !splitter.hasMoreValues();
    }

    public void close() {
        FileUtils.closeReader(reader);
    }

}
