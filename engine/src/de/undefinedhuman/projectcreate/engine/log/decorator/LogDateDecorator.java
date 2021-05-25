package de.undefinedhuman.projectcreate.engine.log.decorator;

import de.undefinedhuman.projectcreate.engine.log.Level;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogDateDecorator extends LogMessageDecorator {

    private final DateFormat dateFormat;

    public LogDateDecorator(LogMessage logMessage, String datePattern) {
        super(logMessage);
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    @Override
    public String createMessage(Level logLevel, String message) {
        return dateFormat.format(Calendar.getInstance().getTime()) + super.createMessage(logLevel, message);
    }

}
