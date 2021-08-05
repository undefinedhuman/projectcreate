package de.undefinedhuman.projectcreate.engine.log;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.file.Serializable;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Log extends Manager implements ApplicationLogger, Serializable {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(Variables.LOG_DATE_FORMAT);

    private static volatile Log instance;

    private Level logLevel = Variables.LOG_LEVEL;
    private String fileName;
    private FsFile file;

    private List<String> logMessages;
    private List<LogEvent> logEvents;

    public Files.FileType fileLocation = Files.FileType.External;

    private Function<String, String> logMessage = new LogMessage();

    private Log() {
        logMessages = new ArrayList<>();
        logEvents = new ArrayList<>();
    }

    @Override
    public void init() {
        fileName = Variables.NAME + " - " + getTime() + ".txt";
    }

    @Override
    public void delete() {
        logEvents.clear();
        logMessages.clear();
    }

    @Override
    public void load() {
        checkLogs();
        file = new FsFile(Paths.LOG_PATH, fileName, fileLocation);
        if (file.exists())
            info("Log file successfully created!");
    }

    @Override
    public void save() {
        if(file == null)
            return;
        info("Log file successfully saved!");
        FileWriter writer = file.getFileWriter(false, "");
        for (String message : logMessages)
            writer.writeString(message).nextLine();
        writer.close();
    }

    public Log setLogMessageDecorator(Function<String, String> logMessageDecorator) {
        this.logMessage = logMessageDecorator;
        return getInstance();
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void addLogEvent(LogEvent logEvent) {
        if(logEvents.contains(logEvent))
            return;
        this.logEvents.add(logEvent);
    }

    public static void showErrorDialog(String message, boolean crash) {
        if(!crash) error(message);
        JOptionPane.showMessageDialog(null, message, Level.ERROR.getPrefix(), JOptionPane.ERROR_MESSAGE);
        if(!crash)
            return;
        crash(message);
    }

    public static void crash(String message) {
        Log.getInstance().createMessage(System.out, Level.CRASH, message);
        Log.getInstance().save();
        Gdx.app.exit();
        System.exit(0);
    }

    public static void error(Object... messages) {
        Log.getInstance().createMessage(System.err, Level.ERROR, messages);
    }

    public static void error(Object message, Exception ex) {
        if(ex != null)
            error(message, "\n", ex.getMessage());
        else error(message);
    }

    public static void info(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.INFO, messages);
    }

    public static void debug(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.DEBUG, messages);
    }

    public static void debug(Supplier<String> createMessages) {
        if(Log.getInstance().getLogLevel() != Level.DEBUG)
            return;
        String message = createMessages.get();
        if(message.equals(""))
            return;
        Log.getInstance().createMessage(System.out, Level.DEBUG, message);
    }

    private void createMessage(PrintStream console, Level logLevel, Object... messages) {
        if(logLevel.ordinal() > this.logLevel.ordinal())
            return;
        StringBuilder logMessage = new StringBuilder();
        for (int i = 0; i < messages.length; i++) logMessage.append(messages[i]).append(i < messages.length - 1 ? ", " : "");

        String fullMessage = logLevel.getPrefix() + this.logMessage.apply(logMessage.toString());

        console.println(fullMessage);
        console.flush();
        logMessages.add(fullMessage);
        logEvents.forEach(logEvent -> logEvent.log(logLevel, fullMessage, logMessage.toString()));
    }

    private void checkLogs() {
        FsFile dir = new FsFile(Paths.LOG_PATH, Files.FileType.External);
        if (!dir.exists() || !dir.isDirectory())
            return;
        FileHandle[] files = dir.list();
        if(files == null)
            return;
        long currentTime = new Date().getTime();
        Stream<FileHandle> filesToRemove = Arrays.stream(files).filter(file -> (currentTime - file.lastModified()) > TimeUnit.DAYS.toMillis(Variables.LOG_DELETION_TIME_DAYS));
        info("Deleted " + filesToRemove.filter(FileHandle::delete).count() + " log files!");
        filesToRemove.close();
    }

    public static String getTime() {
        return DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    public static Log getInstance() {
        if(instance != null)
            return instance;
        synchronized (Log.class) {
            if (instance == null)
                instance = new Log();
        }
        return instance;
    }

    @Override
    public void log(String tag, String message) {
        info(message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        error(message, exception);
    }

    @Override
    public void error(String tag, String message) {
        error(message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        error(message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        debug(message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        error(message, exception);
    }
}
