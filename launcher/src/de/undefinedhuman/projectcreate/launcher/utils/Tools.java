package de.undefinedhuman.projectcreate.launcher.utils;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

public class Tools {

    public static final int AVAILABLE_MAX_MEMORY_IN_MB_HALVED = calculateAvailableMaxMemory();

    private static final float MB = 1024f * 1024f;

    public static int calculateAvailableMaxMemory() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        long availableMemory = hal.getMemory().getTotal();
        float memoryInMB = availableMemory / MB;
        float memoryHalved = memoryInMB / 2f;
        return (int) Math.ceil(memoryHalved);
    }

}
