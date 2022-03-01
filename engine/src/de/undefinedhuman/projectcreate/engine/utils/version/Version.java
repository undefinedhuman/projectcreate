package de.undefinedhuman.projectcreate.engine.utils.version;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.util.Arrays;

public class Version implements Comparable<Version> {

    private final Stage stage;
    private final int[] data;

    private Version(Stage stage, int[] data) {
        this.stage = stage;
        this.data = data;
    }

    public static Version createSnapshot(int year, int week, int build) {
        return new Version(Stage.SNAPSHOT, new int[]{ year, week, build });
    }

    public static Version createVersion(Stage stage, int major, int minor, int patch, int rc) {
        if (stage == Stage.SNAPSHOT) {
            Log.warn("Creating snapshot version through factory method to create normal version, probably an error! Please investigate!");
            return createSnapshot(major, minor, patch);
        }
        return new Version(stage, new int[] { major, minor, patch, rc });
    }

    public static Version createVersion(Stage stage, int major, int minor, int patch) {
        return createVersion(stage, major, minor, patch, Integer.MAX_VALUE);
    }

    public static Version createVersion(Stage stage, int major, int minor) {
        return createVersion(stage, major, minor, 0, Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        if (stage == null || data.length == 0 || (stage == Stage.SNAPSHOT && data.length < 3) || (stage != Stage.SNAPSHOT && !Utils.isInRange(data.length, 2, 4)))
            return "";
        StringBuilder version = new StringBuilder(stage.prefix());
        if(stage == Stage.SNAPSHOT) version.append(String.format("%02dw%02db%d", data[0], data[1], data[2]));
        else version.append(String.format("%d.%d", data[0], data[1]))
                .append(data.length >= 3 && data[2] != 0 ? "." + data[2] : "")
                .append((data.length >= 4 && data[3] != Integer.MAX_VALUE) ? "-rc" + data[3] : "");
        return version.toString();
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
        if (data.length == 0)
            return 0;
        int result = stage.ordinal();
        for (int datum : data) result *= 31 + datum;
        return result;
    }

    @Override
    public int compareTo(Version other) {
        if (other == null)
            return 1;
        int stageComparison = stage.compareTo(other.stage);
        if (stageComparison != 0)
            return stageComparison;
        int length = stage == Stage.SNAPSHOT ? 3 : Math.max(data.length, other.data.length);
        for (int i = 0; i < length; i++) {
            int data = i < this.data.length ? this.data[i] : 0;
            int otherData = i < other.data.length ? other.data[i] : 0;
            if (data < otherData) return -1;
            if (data > otherData) return 1;
        }
        return 0;
    }

    private static Integer parseVersionToken(Stage stage, String token) {
        if(stage == Stage.SNAPSHOT)
            return Utils.isInteger(token);
        if(token.startsWith("rc"))
            token = token.replaceFirst("rc", "");
        return Utils.isInteger(token);
    }

    public static Version parse(String input) {
        Stage stage = Stage.parse(input);
        String version = input.replaceFirst(stage.prefix(), "");
        int indexW = version.indexOf('w'), indexB = version.indexOf('b');
        if((stage == Stage.SNAPSHOT && (indexW == -1 || indexB == -1 || indexB < indexW)) || (stage != Stage.SNAPSHOT && !Utils.isInRange(Utils.countChar(version, '.'), 1, 2))) {
            Log.warn("Illegal version format, supplied: " + input);
            return null;
        }
        String[] rawData = version.replace(stage.prefix(), "").split("\\.|([wb])|(-)");
        int[] data = new int[stage == Stage.SNAPSHOT ? 3 : 4];
        Arrays.fill(data, Integer.MAX_VALUE);

        for(int i = 0; i < rawData.length; i++) {
            int index = i;
            String token = rawData[i];
            Integer number = parseVersionToken(stage, token);
            if(number == null || number < 0 || (token.startsWith("rc") && (!Utils.isInRange(i, 2, 3) || number == 0))) {
                Log.warn("Illegal version format, supplied: " + input);
                return null;
            }
            if(i == 2 && token.startsWith("rc"))
                data[index++] = 0;
            data[index] = number;
        }

        return new Version(stage, data);
    }

}
