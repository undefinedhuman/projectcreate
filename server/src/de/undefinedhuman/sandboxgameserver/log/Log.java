package de.undefinedhuman.sandboxgameserver.log;

import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.FsFile;
import de.undefinedhuman.sandboxgameserver.file.Paths;
import de.undefinedhuman.sandboxgameserver.utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log {

    public static Log instance;
    private static List<String> logMessages;

    private String fileName;
    private FsFile file;

    public Log() {

        logMessages = new ArrayList<>();
        fileName = Tools.getTime() + ".txt";

    }

    public void log(Object msg) {

        String message = generateMessage("Log", String.valueOf(msg));

        System.out.println(message);
        addMessage(message);

    }

    public void info(Object msg) {
        String message = generateMessage("Info", String.valueOf(msg));
        System.out.println(message);
        addMessage(message);
    }

    private String generateMessage(String prefix, String msg) {
        return "[" + Tools.getTime() + "] [" + prefix + "] " + msg;
    }

    private void addMessage(Object obj) {
        logMessages.add(String.valueOf(obj));
    }

    public void error(Object msg) {
        String message = generateMessage("Error", String.valueOf(msg));
        System.out.println(message);
        addMessage(message);
    }

    public void info(Object... values) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < values.length; i++) s.append(values[i]).append(i < values.length - 1 ? ", " : "");
        String message = generateMessage("Info", s.toString());
        System.out.println(message);
        addMessage(message);
    }

    public void load() {
        checkLogs();
        file = new FsFile(Paths.LOG_PATH, fileName, false);
        if (file.exists()) info("Log file successfully created!");
    }

    private void checkLogs() {

        ArrayList<File> fileToRemove = new ArrayList<>();
        File dir = new File(Paths.LOG_PATH.getPath());
        if (dir.exists()) if (dir.isDirectory()) {

            File[] files = dir.listFiles();

            if (files != null) if (files.length != 0) {

                for (File file : files) {

                    long diff = new Date().getTime() - file.lastModified();
                    if (diff > 2 * 24 * 60 * 60 * 1000) fileToRemove.add(file);

                }

                int dFiles = 0;
                for (File file : fileToRemove) if (file.delete()) dFiles++;

                info("Deleted " + dFiles + " log files!");
                fileToRemove.clear();

            }

        }

    }

    public void crash() {
        save();
        System.exit(0);
    }

    public void crash(String message) {
        error(message);
        save();
        System.exit(0);
    }

    public void save() {
        if (file != null && file.exists()) {
            FileWriter writer = file.getFileWriter(false, "", true);
            info("Log file successfully saved!");
            for (String s : logMessages) writer.writeString(s).nextLine();
            logMessages.clear();
            writer.close();
        }
    }

}
