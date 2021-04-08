package de.undefinedhuman.projectcreate.launcher.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class Tools {

    public static final int AVAILABLE_MAX_MEMORY_IN_MB_HALVED = calculateAvailableMaxMemory();

    private static final float MB = 1024f * 1024f;

    public static int calculateAvailableMaxMemory() {
        float totalMemory = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        float memoryInMB = totalMemory / MB;
        float memoryHalved = memoryInMB / 2f;
        return (int) Math.ceil(memoryHalved);
    }

}
