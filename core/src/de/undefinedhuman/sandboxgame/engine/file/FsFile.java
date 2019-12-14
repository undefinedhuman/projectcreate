package de.undefinedhuman.sandboxgame.engine.file;

import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.utils.Variables;

import java.io.*;

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


    public String getPath() {

        return this.path;

    }

    public String getName() {

        return name;

    }

    public void createFile(boolean isDirectory) {

        try {
            createNewFile(isDirectory);
        } catch(IOException ex) {
            Log.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    private void createNewFile(boolean isDirectory) throws IOException {

        if(exists()) return;
        if(!file.getParentFile().exists()) if(!file.getParentFile().mkdirs()) Log.instance.crash();
        if(isDirectory) if(file.mkdir()) Log.info("Successfully created dir: " + file.getPath()); else Log.instance.crash();
        if(!isDirectory) if(file.createNewFile()) Log.info("Successfully created " + file.getName()); else Log.instance.crash();

    }

    public File getFile() {
        return file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean isEmpty() {
        return file.length() == 0;
    }

}
