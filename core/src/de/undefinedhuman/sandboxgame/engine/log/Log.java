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
        if (instance == null) instance = this;
    }

    public static void log(Object msg) {
        createMessage("Log", msg);
    }

    public static void error(Object msg) {
        createMessage("Error", msg);
    }

    public static void info(Object... values) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < values.length; i++) s.append(values[i]).append(i < values.length - 1 ? ", " : "");
        createMessage("Info", s.toString());
    }

    private static void createMessage(String title, Object msg) {
        String message = generateMessage(title, String.valueOf(msg));
        System.out.println(message);
        logMessages.add(message);
    }

    private static String generateMessage(String prefix, String msg) {
        return "[" + Tools.getTime() + "] [" + prefix + "] " + msg;
    }

    @Override
    public void init() {
        fileName = Tools.getTime() + ".txt";
        load();
    }

    @Override
    public void delete() {
        save();
    }

    public void save() {
        if(file == null || !file.exists()) return;
        FileWriter writer = file.getFileWriter(false, "");
        info("Log file successfully saved!");
        for (String message : logMessages) writer.writeString(message).nextLine();
        logMessages.clear();
        writer.close();
    }

    public static void info(Object msg) {
        createMessage("Info", msg);
    }

    public void load() {
        checkLogs();
        file = new FsFile(Paths.LOG_PATH, fileName, false);
        if (file.exists()) info("Log file successfully created!");
    }

    private void checkLogs() {
        ArrayList<File> filesToRemove = new ArrayList<>();
        File dir = new File(Paths.LOG_PATH.getPath());
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] files = dir.listFiles();
        if(files == null || files.length == 0) return;
        for (File file : files)
            if ((new Date().getTime() - file.lastModified()) > 172800000) filesToRemove.add(file);
        int dFiles = 0;
        for (File file : filesToRemove) if (file.delete()) dFiles++;
        info("Deleted " + dFiles + " log files!");
        filesToRemove.clear();
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

}
