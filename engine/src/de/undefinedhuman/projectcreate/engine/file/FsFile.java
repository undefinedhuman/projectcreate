package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.io.File;

public class FsFile {

    private String name, path;
    private FileHandle file;
    private boolean isDirectory;

    public FsFile(String path, String fileName, Files.FileType type, boolean isDirectory) {
        this(path + fileName, type, isDirectory);
    }

    public FsFile(String fileName, Files.FileType type, boolean isDirectory) {
        this.path = fileName;
        this.isDirectory = isDirectory;
        String[] dirs = this.path.split(Variables.FILE_SEPARATOR);
        this.name = dirs[dirs.length - 1];

        file = Gdx.files.getFileHandle(fileName, type);
        if(type == Files.FileType.Internal && !file.exists())
            Log.instance.crash("Can't find internal file: " + name);
    }

    public boolean exists() {
        return file.exists();
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

    public FileHandle getFileHandle() {
        return file;
    }

    public File getFile() {
        return file.file();
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isEmpty() {
        return file.length() == 0;
    }

}
