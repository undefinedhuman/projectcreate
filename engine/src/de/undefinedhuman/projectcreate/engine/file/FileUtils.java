package de.undefinedhuman.projectcreate.engine.file;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FileUtils {

    public static BufferedReader getBufferedReader(FsFile file) {
        try {
            return new BufferedReader(file.reader());
        } catch (Exception ex) {
            Log.instance.exit("Can't create file reader: \n" + ex.getMessage());
        }
        return null;
    }

    public static BufferedWriter getBufferedWriter(FsFile file) {
        try {
            return new BufferedWriter(file.writer(false));
        } catch (Exception ex) {
            Log.instance.exit("Can't create file writer: \n" + ex.getMessage());
        }
        return null;
    }

    public static void closeReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (Exception ex) {
            Log.instance.exit(ex.getMessage());
        }
    }

    public static void closeWriter(BufferedWriter writer) {
        try {
            writer.close();
        } catch (Exception ex) {
            Log.instance.exit(ex.getMessage());
        }
    }

    private static void deleteFile(ArrayList<String> deletedFileNames, File fileToDelete) {
        if (!fileToDelete.exists()) return;
        if (fileToDelete.isDirectory()) {
            File[] files = fileToDelete.listFiles();
            assert files != null;
            if (files.length != 0) for (File file1 : files) deleteFile(deletedFileNames, file1);
        }
        if(fileToDelete.delete()) deletedFileNames.add(fileToDelete.getName());
    }

    public static void deleteFile(FsFile... filesToDelete) {
        for(FsFile file : filesToDelete) {
            ArrayList<String> deletedFileNames = new ArrayList<>();
            deleteFile(deletedFileNames, file.file());
            Collections.reverse(deletedFileNames);
            Log.info("File" + Utils.appendSToString(deletedFileNames.size()) + " deleted successfully: " + Arrays.toString(deletedFileNames.toArray()));
        }
    }

    public static boolean readBoolean(String value) {
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) return Boolean.parseBoolean(value);
        else if(Utils.isDigit(value)) return Integer.parseInt(value) == 1;
        else return false;
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

}
