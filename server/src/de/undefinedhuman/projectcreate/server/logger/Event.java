package de.undefinedhuman.projectcreate.server.logger;

import java.util.Calendar;
import java.util.Date;

public class Event<T extends Enum> {
    private Date timestamp = Calendar.getInstance().getTime();
}
