package de.undefinedhuman.projectcreate.engine.log;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Log extends Manager implements ApplicationLogger {

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
        fileName = Variables.NAME + " - " + getTime() + ".txt";
        load();
    }

    public void delete() {
        save();
    }

    public void save() {
        if(file == null) return;
        FileWriter writer = file.getFileWriter(false, "");
        info("Log file successfully saved!");
        for (String message : logMessages)
            writer.writeString(message).nextLine();
        logMessages.clear();
        writer.close();
    }

    public void load() {
        checkLogs();
        file = new FsFile(Paths.LOG_PATH, fileName, Files.FileType.External);
        if (file.exists()) info("Log file successfully created!");
    }

    public void exit() {
        close();
        save();
        System.exit(0);
    }

    public void exit(String errorMessage) {
        error(errorMessage);
        exit();
    }

    @Override
    public void log(String tag, String message) {
        createMessage(System.out, tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        createMessage(System.out, tag, message + "\n" + exception.getMessage());
    }

    @Override
    public void error(String tag, String message) {
        createMessage(System.err, tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        createMessage(System.err, tag, message + "\n" + exception.getMessage());
    }

    @Override
    public void debug(String tag, String message) {
        createMessage(System.out, tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        createMessage(System.out, tag, message + "\n" + exception.getMessage());
    }

    public static void error(Object msg) {
        createMessage(System.err, "Error", msg);
    }

    public static void info(Object msg) {
        createMessage(System.out, "Info", msg);
    }

    public static void info(Object... values) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < values.length; i++) s.append(values[i]).append(i < values.length - 1 ? ", " : "");
        createMessage(System.out, "Info", s.toString());
    }

    private static void createMessage(PrintStream console, String prefix, Object msg) {
        String logMessage = Variables.LOG_MESSAGE_FORMAT
                .replace("%prefix%", prefix)
                .replace("%time%", getTime())
                .replace("%message%", String.valueOf(msg))
                .replace("%name%", Variables.NAME)
                .replace("%version%", Variables.VERSION.toString());
        Log.instance.displayMessage(logMessage);
        console.println(logMessage);
        logMessages.add(logMessage);
    }

    private void checkLogs() {
        ArrayList<FileHandle> filesToRemove = new ArrayList<>();
        FsFile dir = new FsFile(Paths.LOG_PATH, Files.FileType.External);
        if (!dir.exists() || !dir.isDirectory()) return;
        FileHandle[] files = dir.list();
        if(files == null || files.length == 0) return;
        for (FileHandle file : files)
            if ((new Date().getTime() - file.lastModified()) > TimeUnit.DAYS.toMillis(Variables.LOG_DELETION_TIME_DAYS)) filesToRemove.add(file);
        int dFiles = 0;
        for (FileHandle file : filesToRemove) if (file.delete()) dFiles++;
        info("Deleted " + dFiles + " log files!");
        filesToRemove.clear();
    }

    public static String getTime() {
        return DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    public void displayMessage(String msg) {}

    public void close() {}
}
