package de.undefinedhuman.sandboxgameserver.file;

import de.undefinedhuman.sandboxgameserver.log.Log;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;

public class FileManager {

    public static BufferedReader getBufferedReader(FsFile file) {

        try {
            return new BufferedReader(new FileReader(new File(file.getPath())));
        } catch (Exception ex) {
            Log.instance.error("Can't create file reader");
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
            return null;
        }

    }

    public static BufferedReader getBufferedReader(InputStream stream) {

        try {
            return new BufferedReader(new InputStreamReader(stream));
        } catch (Exception ex) {
            Log.instance.error("Can't create file reader");
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
            return null;
        }

    }

    public static BufferedWriter getBufferedWriter(FsFile file) {

        try {

            return new BufferedWriter(new FileWriter(new File(file.getPath())));

        } catch (Exception ex) {

            Log.instance.error("Can't create file writer");
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
            return null;

        }

    }

    public static BufferedWriter getBufferedWriter(FsFile file, boolean append) {

        try {

            return new BufferedWriter(new FileWriter(new File(file.getPath()), append));

        } catch (Exception ex) {

            Log.instance.error("Can't create file writer");
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
            return null;

        }

    }

    public static void closeReader(BufferedReader reader) {

        try {
            reader.close();
        } catch (Exception ex) {
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    public static void closeWriter(BufferedWriter writer) {

        try {
            writer.close();
        } catch (Exception ex) {
            Log.instance.error(ex.getMessage());
            Log.instance.crash();
        }

    }

    public static boolean deleteFile(File file) {

        if (!file.exists()) return true;

        if (file.isDirectory()) {

            File[] files = file.listFiles();

            assert files != null;
            for (File file1 : files) deleteFile(file1);

        }

        boolean deleted = file.delete();
        if (deleted) Log.instance.info("Successfully deleted: " + file.getName());
        return deleted;

    }

    public static boolean readBoolean(String value) {
        return Integer.parseInt(value) == 1;
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

}
