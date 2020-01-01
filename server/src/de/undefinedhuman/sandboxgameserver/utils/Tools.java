package de.undefinedhuman.sandboxgameserver.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Tools {

    public static String getTime() {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy - HH-mm-ss");
        Calendar cal = Calendar.getInstance();

        return df.format(cal.getTime());

    }

    public static float lerp(float a, float b, float f) {
        return a * (1 - f) + (b * f);
    }

}
