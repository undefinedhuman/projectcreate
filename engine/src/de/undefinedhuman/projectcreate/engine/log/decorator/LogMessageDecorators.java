package de.undefinedhuman.projectcreate.engine.log.decorator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogMessageDecorators {

    public static String withDate(String message, String datePattern) {
        return "[" + new SimpleDateFormat(datePattern).format(Calendar.getInstance().getTime()) + "] " + message;
    }

    public static String withModuleName(String message, String moduleName) {
        return "[" + moduleName + "] " + message;
    }

}
