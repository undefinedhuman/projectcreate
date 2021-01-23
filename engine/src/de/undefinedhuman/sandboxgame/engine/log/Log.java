package de.undefinedhuman.sandboxgame.engine.log;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Log extends Manager {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(Variables.LOG_DATE_FORMAT);

    public static Log instance;
    private static List<String> logMessages;

    private String fileName;
    private FsFile file;

    public Log() {
        if (instance == null) instance = this;
        logMessages = new ArrayList<>();
    }

    public void init() {
        fileName = getTime() + ".txt";
        load();
    }

    public void delete() {
        save();
    }

    public void save() {
        if(file == null || !file.exists()) return;
        FileWriter writer = file.getFileWriter(false, "");
        info("Log file successfully saved!");
        for (String message : logMessages)
            writer.writeString(message).nextLine();
        logMessages.clear();
        writer.close();
    }

    public void load() {
        checkLogs();
        file = new FsFile(Paths.LOG_PATH + fileName, false);
        if (file.exists()) info("Log file successfully created!");
    }

    public void crash() {
        close();
        save();
        System.exit(0);
    }

    public void crash(String errorMessage) {
        error(errorMessage);
        crash();
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
        for (int i = 0; i < values.length; i++) s.append(values[i]).append(i < values.length - 1 ? ", " : "");
        createMessage("Info", s.toString());
    }

    private static void createMessage(String prefix, Object msg) {
        String logMessage = Variables.LOG_MESSAGE_FORMAT
                .replaceAll("%prefix%", prefix)
                .replaceAll("%time%", getTime())
                .replaceAll("%message%", String.valueOf(msg))
                .replaceAll("%name%", Variables.NAME)
                .replaceAll("%version%", Variables.VERSION.toString());
        Log.instance.displayMessage(logMessage);
        System.out.println(logMessage);
        logMessages.add(logMessage);
    }

    private void checkLogs() {
        ArrayList<File> filesToRemove = new ArrayList<>();
        File dir = new File(Paths.LOG_PATH);
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

    public static String getTime() {
        return DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    public void displayMessage(String msg) {}

    public void close() {}

}
