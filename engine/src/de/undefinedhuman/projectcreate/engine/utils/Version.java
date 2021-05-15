package de.undefinedhuman.projectcreate.engine.utils;

public class Version implements Comparable<Version>{

    private final Stage stage;
    private final int[] data;

    public Version(Stage stage, int major, int minor, int patch, int rc) {
        this.stage = stage;
        data = new int[] { major, minor, patch, rc };
    }

    public int[] getData() {
        return data;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public String toString() {
        if(stage == null || data.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(stage.name().toLowerCase()).append("-");
        if(stage == Stage.SNAPSHOT) builder.append(data[0]).append("w").append(data[1]).append("b").append(data[2]);
        else builder.append(data[0]).append(".").append(data[1]).append(".").append(data[2]).append(data[3] != Integer.MAX_VALUE ? "-rc" + data[3] : "");
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
        int length = Math.max(data.length, other.getData().length);
        for(int i = 0; i < length; i++) {
            int data = i < this.data.length ? this.data[i] : 0;
            int otherData = i < other.getData().length ? other.getData()[i] : 0;
            if(data < otherData) return -1;
            if(data > otherData) return 1;
        }
        return 0;
    }

    public static Version parse(String version) {
        String[] stageAndDataSplit = version.split("-");
        if(!Tools.isInRange(stageAndDataSplit.length, 2, 3))
            return new Version(Stage.SNAPSHOT, 0, 0, 0, 0);
        String[] data = stageAndDataSplit[1].split("\\.|([wb])");
        if(data.length != 3 || Tools.isInteger(data[0]) == null || Tools.isInteger(data[1]) == null || Tools.isInteger(data[2]) == null)
            return new Version(Stage.SNAPSHOT, 0, 0, 0, 0);
        return new Version(
                Stage.parse(stageAndDataSplit[0]),
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                stageAndDataSplit.length == 3 ? Integer.parseInt(stageAndDataSplit[2].substring(2)) : Integer.MAX_VALUE
        );
    }

}
