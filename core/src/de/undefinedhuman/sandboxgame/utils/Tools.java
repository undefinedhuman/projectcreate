package de.undefinedhuman.sandboxgame.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tools {

    public static boolean isMac = System.getProperty("os.name").contains("OS X");
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");
    public static Random random = new Random();

    public static String getTime() {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy - HH-mm-ss");
        Calendar cal = Calendar.getInstance();

        return df.format(cal.getTime());

    }

}
