package de.undefinedhuman.projectcreate.engine.utils;

public class Version implements Comparable<Version>{

    private final int[] data;

    public Version(int... data) {
        this.data = data;
    }

    public Version(String... data) {
        this.data = new int[data.length];
        for(int i = 0; i < data.length; i++)
            this.data[i] = Integer.parseInt(data[i]);
    }

    public Version(int major, int minor, int patch) {
        data = new int[] { major, minor, patch };
    }

    public int[] data() {
        return data;
    }

    @Override
    public String toString() {
        if(data.length == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        builder.append("v");
        for(int i = 0; i < data.length-1; i++)
            builder.append(data[i]).append(".");
        builder.append(data[data.length-1]);
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Version version = (Version) other;
        return this.compareTo(version) == 0;
    }

    @Override
    public int hashCode() {
        if(data.length == 0)
            return 0;
        int result = data[0];
        for(int i = 1; i < data.length; i++)
            result *= 31 + data[i];
        return result;
    }

    @Override
    public int compareTo(Version other) {
        if(other == null)
            return 1;
        int length = Math.max(data.length, other.data().length);
        for(int i = 0; i < length; i++) {
            int data = i < this.data.length ? this.data[i] : 0;
            int otherData = i < other.data().length ? other.data()[i] : 0;
            if(data < otherData) return -1;
            if(data > otherData) return 1;
        }
        return 0;
    }

    public static Version parse(String version) {
        String[] data;
        if(version.startsWith("v"))
            data = version.substring(1).split("\\.");
        else data = version.split("\\.");
        return new Version(data);
    }

}
