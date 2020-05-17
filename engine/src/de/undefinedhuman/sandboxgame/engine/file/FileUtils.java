package de.undefinedhuman.sandboxgame.engine.file;

import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;

public class FileUtils {

    public static BufferedReader getBufferedReader(FsFile file) {
        try {
            return new BufferedReader(new FileReader(new File(file.getPath())));
        } catch (Exception ex) {
            Log.instance.crash("Can't create file reader \n" + ex.getMessage());
        }
        return null;
    }

    public static BufferedWriter getBufferedWriter(FsFile file) {
        try {
            return new BufferedWriter(new FileWriter(new File(file.getPath())));
        } catch (Exception ex) {
            Log.instance.crash("Can't create file writer \n" + ex.getMessage());
        }
        return null;
    }

    public static void closeReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (Exception ex) {
            Log.instance.crash(ex.getMessage());
        }
    }

    public static void closeWriter(BufferedWriter writer) {
        try {
            writer.close();
        } catch (Exception ex) {
            Log.instance.crash(ex.getMessage());
        }
    }

    public static void deleteFile(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            if (files.length != 0) for (File file1 : files) deleteFile(file1);
        }
        boolean del = file.delete();
        if (del) Log.info("Successfully deleted: " + file.getName());
    }

    public static void deleteFile(FsFile file) {
        deleteFile(file.getFile());
    }

    public static boolean readBoolean(String value) {
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) return Boolean.parseBoolean(value);
        else if(Tools.isDigit(value)) return Integer.parseInt(value) == 1;
        else return false;
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

}
