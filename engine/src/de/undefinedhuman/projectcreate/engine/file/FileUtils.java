package de.undefinedhuman.projectcreate.engine.file;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

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
            Log.error("Error while creating file reader for file " + file.name() + ":", ex);
        }
        return null;
    }

    public static BufferedWriter getBufferedWriter(FsFile file) {
        try {
            return new BufferedWriter(file.writer(false));
        } catch (Exception ex) {
            Log.error("Error while creating file writer for file " + file.name() + ":", ex);
        }
        return null;
    }

    public static void closeReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (Exception ex) {
            Log.error("Error while closing file reader: ", ex);
        }
    }

    public static void closeWriter(BufferedWriter writer) {
        try {
            writer.close();
        } catch (Exception ex) {
            Log.error("Error while closing file writer", ex);
        }
    }

    private static void deleteFile(ArrayList<String> deletedFileNames, File fileToDelete) {
        if (!fileToDelete.exists()) return;
        if (fileToDelete.isDirectory()) {
            File[] files = fileToDelete.listFiles();
            assert files != null;
            if (files.length != 0) for (File file1 : files) deleteFile(deletedFileNames, file1);
        }
        if(fileToDelete.delete())
            deletedFileNames.add(fileToDelete.getName());
    }

    public static void deleteFile(FsFile... filesToDelete) {
        ArrayList<String> deletedFileNames = new ArrayList<>();
        for(FsFile file : filesToDelete)
            deleteFile(deletedFileNames, file.file());
        Collections.reverse(deletedFileNames);
        if(deletedFileNames.size() > 0)
            Log.info("File" + Tools.appendSToString(deletedFileNames.size()) + " deleted successfully: " + Arrays.toString(deletedFileNames.toArray()));
        deletedFileNames.clear();
    }

    public static boolean readBoolean(String value) {
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) return Boolean.parseBoolean(value);
        else if(Tools.isInteger(value) != null) return Integer.parseInt(value) == 1;
        else return false;
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

}
