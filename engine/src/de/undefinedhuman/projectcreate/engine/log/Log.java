package de.undefinedhuman.projectcreate.engine.log;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.file.Serializable;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import javax.swing.*;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Log implements Manager, Serializable {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(Variables.LOG_DATE_FORMAT);

    private static volatile Log instance;

    private Level logLevel = Variables.LOG_LEVEL;
    private String fileName;
    private FsFile file;

    private final List<String> logMessages;
    private final List<LogEvent> logEvents;

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
        file = new FsFile(Paths.getInstance().getDirectory(), Paths.LOG_PATH + fileName);
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

    public boolean isLevelEnabled(Level logLevel) {
        return this.logLevel.ordinal() >= logLevel.ordinal();
    }

    public Log setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
        return getInstance();
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

    public static void crash() {
        crash(null);
    }

    public static void crash(String message) {
        if(message != null)
            Log.getInstance().createMessage(System.out, Level.ERROR, message);
        Log.getInstance().save();
        Gdx.app.exit();
        System.exit(0);
    }

    public static void error(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.ERROR, messages);
    }

    public static void warn(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.WARN, messages);
    }

    public static void error(Object message, Exception ex) {
        StringBuilder builder = new StringBuilder(message.toString());
        if(ex == null) {
            error(builder.toString());
            return;
        }
        builder.append(" Exception:").append("\n").append(ex);
        if(getInstance().getLogLevel() == Level.DEBUG)
            builder.append("\n").append("Value: ").append(ex.getCause()).append("\n").append("Stacktrace:").append("\n").append(Utils.getStackTrace(ex));
        error(builder.toString());
    }

    public static void info(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.INFO, messages);
    }

    public static void trace(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.TRACE, messages);
    }

    public static void debug(Object... messages) {
        Log.getInstance().createMessage(System.out, Level.DEBUG, messages);
    }

    public static <T> T debug(String errorMessage, Supplier<T> returnValue) {
        Log.getInstance().createMessage(System.out, Level.DEBUG, errorMessage);
        return returnValue.get();
    }

    public static void debug(Supplier<String> createMessages) {
        if(Log.getInstance().getLogLevel() != Level.DEBUG)
            return;
        String message = createMessages.get();
        if(message.equals(""))
            return;
        Log.getInstance().createMessage(System.out, Level.DEBUG, message);
    }

    public static void log(Level level, Object... messages) {
        Log.getInstance().createMessage(System.out, level, messages);
    }

    public static void log(Level level, String category, Object... messages) {
        Log.getInstance().createMessage(System.out, level, category, messages);
    }

    private void createMessage(PrintStream console, Level logLevel, Object... messages) {
        this.createMessage(console, logLevel, "", messages);
    }

    private void createMessage(PrintStream console, Level logLevel, String category, Object... messages) {
        if(logLevel.ordinal() > this.logLevel.ordinal()) return;
        StringBuilder logMessage = new StringBuilder();
        if(category!= null && !category.equals(""))
            logMessage.append("[").append(category).append("]").append(" ");
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
}
