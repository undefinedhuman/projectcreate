package de.undefinedhuman.projectcreate.core.engine.file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.projectcreate.core.engine.log.Log;
import de.undefinedhuman.projectcreate.core.engine.utils.Variables;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {

    private LineSplitter splitter;
    private BufferedReader reader;
    private FsFile file;
    private boolean base;
    private String separator;

    public FileReader(FsFile file, boolean base) {
        this(file, base, Variables.SEPARATOR);
    }

    public FileReader(FsFile file, boolean base, String separator) {
        this.reader = FileUtils.getBufferedReader(file);
        this.file = file;
        this.base = base;
        this.separator = separator;
    }

    public String nextLine() {
        String line = null;
        try { line = reader.readLine(); } catch (IOException ex) { Log.error(ex.getMessage()); }
        if (line == null) return null;
        splitter = new LineSplitter(line, base, separator);
        return line;
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
        while (!isEndOfLine()) builder.append(getNextString()).append(Variables.SEPARATOR);
        return builder.toString();
    }

    public LineSplitter getRemainingRawData() { return new LineSplitter(splitter.getRemainingRawData(), base, separator); }

    public boolean isEndOfLine() {
        return !splitter.hasMoreValues();
    }

    public FsFile getParentDirectory() {
        return new FsFile(file.parent().path(), file.type());
    }

    public void close() {
        FileUtils.closeReader(reader);
    }

}
