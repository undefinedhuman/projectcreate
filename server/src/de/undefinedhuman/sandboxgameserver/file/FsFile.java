package de.undefinedhuman.sandboxgameserver.file;

import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.utils.Variables;

import java.io.File;
import java.io.IOException;

public class FsFile {

    private String name, path;
    private boolean isDirectory;

    private File file;

    public FsFile(String filename, boolean isDirectory) {

        this.path = filename;
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

            Log.instance.error(ex.getMessage());
            Log.instance.crash();

        }

    }

    private void createNewFile(boolean isDirectory) throws IOException {

        if (exists()) return;
        if (!file.getParentFile().exists()) if (!file.getParentFile().mkdirs()) Log.instance.crash();
        if (isDirectory) if (file.mkdir()) Log.instance.info("Successfully created dir: " + file.getPath());
        else Log.instance.crash();
        if (!isDirectory) if (file.createNewFile()) Log.instance.info("Successfully created " + file.getName());
        else Log.instance.crash();

    }

    public boolean exists() {
        return file.exists();
    }

    public FsFile(Paths path, boolean isDirectory) {

        this.path = path.getPath();
        this.isDirectory = isDirectory;
        String[] dirs = this.path.split(Variables.FILE_SEPARATOR);
        this.name = dirs[dirs.length - 1];

        file = new File(this.path);
        createFile(isDirectory);

    }

    public FsFile(Paths path, String filename, boolean isDirectory) {

        this.path = path.getPath() + filename;
        this.isDirectory = isDirectory;
        String[] dirs = this.path.split(Variables.FILE_SEPARATOR);
        this.name = dirs[dirs.length - 1];

        file = new File(this.path);
        createFile(isDirectory);

    }

    public FsFile(FsFile file, String filename, boolean isDirectory) {

        this.path = file.getPath() + Variables.FILE_SEPARATOR + filename;
        this.isDirectory = isDirectory;
        String[] dirs = this.path.split(Variables.FILE_SEPARATOR);
        this.name = dirs[dirs.length - 1];

        this.file = new File(this.path);
        createFile(isDirectory);

    }

    public String getPath() {
        return this.path;
    }

    public FsFile(Paths path, String filename, boolean isDirectory, boolean create) {

        this.path = path.getPath() + filename;
        this.isDirectory = isDirectory;
        String[] dirs = this.path.split(Variables.FILE_SEPARATOR);
        this.name = dirs[dirs.length - 1];

        file = new File(this.path);
        if (create) createFile(isDirectory);

    }

    public FileReader getFileReader(boolean base) {
        return new FileReader(this, base);
    }

    public FileReader getFileReader(boolean base, String seperator) {
        return new FileReader(this, base, seperator);
    }

    public FileWriter getFileWriter(boolean base) {
        return new FileWriter(this, base);
    }

    public FileWriter getFileWriter(boolean base, String seperator) {
        return new FileWriter(this, base, seperator);
    }

    public FileWriter getFileWriter(boolean base, boolean append) {
        return new FileWriter(this, base, append);
    }

    public FileWriter getFileWriter(boolean base, String seperator, boolean append) {
        return new FileWriter(this, base, append, seperator);
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isEmpty() {
        return file.length() == 0;
    }

    public void delete() {
        FileManager.deleteFile(getFile());
    }

    public File getFile() {
        return file;
    }

    public boolean hasChildren(String name) {

        boolean hasChild = false;
        String[] sList = file.list();
        if (sList == null) return false;
        for (String s : sList) if (name.equalsIgnoreCase(s)) hasChild = true;
        return hasChild;

    }

}
