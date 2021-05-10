package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class FsFile extends FileHandle {

    public FsFile(String path, String fileName, Files.FileType type) {
        this(path + fileName, type);
    }

    public FsFile(FileHandle parentFile, String fileName, Files.FileType type) {
        super(parentFile.path() + Variables.FILE_SEPARATOR + fileName, type);
    }

    public FsFile(FsFile parentFile, String fileName, Files.FileType type) {
        super(parentFile.path() + Variables.FILE_SEPARATOR + fileName, type);
    }

    public FsFile(String fileName, Files.FileType type) {
        super(fileName, type);
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

}
