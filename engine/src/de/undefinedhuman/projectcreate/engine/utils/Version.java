package de.undefinedhuman.projectcreate.engine.utils;

import de.undefinedhuman.projectcreate.engine.log.Log;

public class Version {

    public final int MAJOR, MINOR, PATCH;

    public Version(int major, int minor, int patch) {
        this.MAJOR = major;
        this.MINOR = minor;
        this.PATCH = patch;
    }

    @Override
    public String toString() {
        return "Version " + MAJOR + "." + MINOR + "." + PATCH;
    }

    public static Version generateVersionFromString(String version) {
        String[] versionStringData = version.split("\\.");
        if(versionStringData.length != 3) Log.error("Version must follow this format: MAJOR.MINOR.PATCH");
        return new Version(Integer.parseInt(versionStringData[0]), Integer.parseInt(versionStringData[1]), Integer.parseInt(versionStringData[2]));
    }

}
