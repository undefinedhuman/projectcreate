package de.undefinedhuman.sandboxgame.engine.log;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.utils.Manager;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log extends Manager {

    public static Log instance;
    private static List<String> logMessages;

    private String fileName;
    private FsFile file;

    public Log() {
        logMessages = new ArrayList<>();
        if(instance == null) instance = this;
    }

    @Override
    public void init() {
        fileName = Tools.getTime() + ".txt";
        load();
    }

    public static void log(Object msg) {
        createMessage("Log", msg);
    }
    public static void error(Object msg) {
        createMessage("Error", msg);
    }
    public static void info(Object msg) {
        createMessage("Info", msg);
    }

    public static void info(Object... values) {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < values.length; i++) s.append(values[i]).append(i < values.length - 1 ? ", " : "");
        createMessage("Info", s.toString());
    }

    public void load() {
        checkLogs();
        file = new FsFile(Paths.LOG_PATH, fileName, false);
        if(file.exists()) info("Log file successfully created!");
    }

    public void save() {

        if(file != null && file.exists()) {

            FileWriter writer = file.getFileWriter(false, "");
            info("Log file successfully saved!");
            for(String s : logMessages) { writer.writeString(s); writer.nextLine(); }
            logMessages.clear();
            writer.close();

        }

    }

    private void checkLogs() {

        ArrayList<File> fileToRemove = new ArrayList<>();
        File dir = new File(Paths.LOG_PATH.getPath());

        if(dir.exists()) if(dir.isDirectory()) {

            File[] files = dir.listFiles();

            if(files != null) if(files.length != 0) {

                for(File file : files) if ((new Date().getTime() - file.lastModified()) > 2 * 24 * 60 * 60 * 1000) fileToRemove.add(file);
                int dFiles = 0;
                for(File file : fileToRemove) if(file.delete()) dFiles++;
                info("Deleted " + dFiles + " log files!");
                fileToRemove.clear();

            }

        }

    }

    private static String generateMessage(String prefix, String msg) {
        return "[" + Tools.getTime() + "] [" + prefix + "] " + msg;
    }

    private static void createMessage(String title, Object msg) {
        String message = generateMessage(title, String.valueOf(msg));
        System.out.println(message);
        logMessages.add(message);
    }

    public void crash() {
        save();
        System.exit(0);
    }

    public void crash(String errorMessage) {
        error(errorMessage);
        save();
        System.exit(1);
    }

    @Override
    public void delete() {
        save();
    }

}
