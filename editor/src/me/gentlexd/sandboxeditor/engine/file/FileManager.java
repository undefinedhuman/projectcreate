package me.gentlexd.sandboxeditor.engine.file;

import me.gentlexd.sandboxeditor.engine.log.Log;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {

    public static BufferedReader getBufferedReader(FsFile file) {

        try {

            return new BufferedReader(new FileReader(new File(file.getPath())));

        } catch(Exception ex) {

            Log.error("Can't create file reader");
            Log.error(ex.getMessage());
            Log.instance.crash();
            return null;

        }

    }

    public static BufferedWriter getBufferedWriter(FsFile file) {

        try {

            return new BufferedWriter(new FileWriter(new File(file.getPath())));

        } catch(Exception ex) {

            Log.error("Can't create file writer");
            Log.error(ex.getMessage());
            Log.instance.crash();
            return null;

        }

    }

    public static void closeReader(BufferedReader reader) {

        try {

            reader.close();

        } catch(Exception ex) {

            Log.error(ex.getMessage());
            Log.instance.crash();

        }

    }

    public static void closeWriter(BufferedWriter writer) {

        try {

            writer.close();

        } catch(Exception ex) {

            Log.error(ex.getMessage());
            Log.instance.crash();

        }

    }

    public static boolean deleteFile(File file) {

        if(!file.exists()) {

            return true;

        }

        if(file.isDirectory()) {

            File[] files = file.listFiles();

            assert files != null;
            for (File file1 : files) {

                deleteFile(file1);

            }

        }

        if(file.delete()) {

            Log.info("Successfully deleted: " + file.getName());
            return true;

        }

        return false;

    }

    public static boolean readBoolean(String value) {

        return Integer.parseInt(value) == 1;

    }

    public static int booleanToInt(boolean bool) {

        return bool ? 1 : 0;

    }

    public static void copyFile(File oldLocation, File newLocation) throws IOException {

        if ( oldLocation.exists( )) {
            BufferedInputStream reader = new BufferedInputStream( new FileInputStream(oldLocation) );
            BufferedOutputStream writer = new BufferedOutputStream( new FileOutputStream(newLocation, false));
            try {
                byte[]  buff = new byte[8192];
                int numChars;
                while ( (numChars = reader.read(  buff, 0, buff.length ) ) != -1) {
                    writer.write( buff, 0, numChars );
                }
            } catch( IOException ex ) {
                throw new IOException("IOException when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
            } finally {
                try {
                    writer.close();
                    reader.close();
                } catch( IOException ex ){
                    Log.error("Error closing files when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() );
                }
            }
        } else {
            throw new IOException("Old location does not exist when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() );
        }

    }

}
