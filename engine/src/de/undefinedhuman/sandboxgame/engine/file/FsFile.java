package de.undefinedhuman.sandboxgame.engine.file;

import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

import java.io.File;
import java.io.IOException;

public class FsFile {

    private String name, path;
    private File file;
    private boolean isDirectory;

    public FsFile(File file, String fileName, boolean isDirectory) {
        this(file.getPath() + fileName, isDirectory);
    }

    public FsFile(Paths path, String fileName, boolean isDirectory) {
        this(path.getPath() + fileName, isDirectory);
    }

    public FsFile(String fileName, boolean isDirectory) {
        this.path = fileName;
        this.isDirectory = isDirectory;
        String[] dirs = this.path.split(Variables.FILE_SEPARATOR);
        this.name = dirs[dirs.length - 1];

        file = new File(this.path);
        createFile(isDirectory);
    }

    public void createFile(boolean isDirectory) {
        try {
            createNewFile(isDirectory);
        } catch (IOException ex) {
            Log.instance.crash(ex.getMessage());
        }
    }

    private void createNewFile(boolean isDirectory) throws IOException {
        if (exists()) return;
        if (!file.getParentFile().exists()) if (!file.getParentFile().mkdirs()) Log.instance.crash();
        if (isDirectory) if (file.mkdir()) Log.info("Successfully created dir: " + file.getPath());
        else Log.instance.crash();
        if (!isDirectory) if (file.createNewFile()) Log.info("Successfully created " + file.getName());
        else Log.instance.crash();
    }

    public boolean exists() {
        return file.exists();
    }

    public FsFile(FsFile file, String fileName, boolean isDirectory) {
        this(file.getPath() + Variables.FILE_SEPARATOR + fileName, isDirectory);
    }

    public String getPath() {
        return this.path;
    }

    public FileReader getFileReader(boolean base) {
        return new FileReader(this, base);
    }

    public FileReader getFileReader(boolean base, String separator) {
        return new FileReader(this, base, separator);
    }

    public FileWriter getFileWriter(boolean base) {
        return new FileWriter(this, base);
    }

    public FileWriter getFileWriter(boolean base, String separator) {
        return new FileWriter(this, base, separator);
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isEmpty() {
        return file.length() == 0;
    }

}
