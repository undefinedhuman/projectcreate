package de.undefinedhuman.projectcreate.engine.utils;

public class Version implements Comparable<Version>{

    private final Stage stage;
    private final int[] data;

    public Version(Stage stage, int... data) {
        this.stage = stage;
        this.data = data;
    }

    public Version(String... data) {
        stage = Stage.parse(data[0]);
        this.data = new int[data.length];
        for(int i = 1; i < data.length; i++)
            this.data[i] = Integer.parseInt(data[i]);
    }

    public Version(String stage, String... data) {
        this.stage = Stage.parse(stage);
        this.data = new int[data.length];
        for(int i = 1; i < data.length; i++)
            this.data[i] = Integer.parseInt(data[i]);
    }

    public Version(Stage stage, int major, int minor, int patch) {
        this.stage = stage;
        data = new int[] { major, minor, patch };
    }

    public int[] data() {
        return data;
    }

    @Override
    public String toString() {
        if(stage == null || data.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(stage.name().toLowerCase()).append("-");
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
        int stageComparison = stage.compareTo(other.stage);
        if(stageComparison != 0)
            return stageComparison;
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
        String[] stageAndDataSplit = version.split("-");
        if(stageAndDataSplit.length != 2)
            return new Version(Stage.INDEV, -1, -1, -1);
        return new Version(stageAndDataSplit[0], stageAndDataSplit[1].split("\\."));
    }

}
