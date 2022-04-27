package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

public enum FileError implements Predicate<FsFile> {
    NULL(0, Objects::isNull, "NULL", false),
    NON_EXISTENT(1, file -> !file.exists(), "NON_EXISTENT", false),
    EMPTY(2, file -> file.length() == 0, "EMPTY", true),
    NOT_WRITEABLE(3, file -> !file.file().canWrite(), "NOT_WRITEABLE", true),
    NO_DIRECTORY(4, file -> !file.isDirectory(), "NO_DIRECTORY", true),
    NO_FILE(4, FileHandle::isDirectory, "NO_FILE", true);

    private final Predicate<FsFile> condition;
    private final int errorCode;
    private final String errorMessage;
    private final boolean canContinueWithCheck;

    FileError(int errorCode, Predicate<FsFile> condition, String errorMessage, boolean canContinueWithCheck) {
        this.condition = condition;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.canContinueWithCheck = canContinueWithCheck;
    }

    @Override
    public boolean test(FsFile file) {
        return condition.test(file);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "(" + errorCode + ") " + errorMessage;
    }

    public static boolean checkFileForErrors(String errorMessage, FsFile file, FileError... errors) {
        ArrayList<String> errorMessages = new ArrayList<>();
        for (FileError error : errors) {
            if (!error.test(file))
                continue;
            errorMessages.add(error.toString());
            if (!error.canContinueWithCheck)
                break;
        }
        boolean hasErrors = errorMessages.size() > 0;
        if(hasErrors)
            Log.warn("Error" + Utils.appendSToString(errorMessages.size()) + " while " + errorMessage + " file: " + errorMessages);
        errorMessages.clear();
        return hasErrors;
    }

    public static ArrayList<String> checkFileForErrors(FsFile file, FileError... errors) {
        ArrayList<String> errorMessages = new ArrayList<>();
        for (FileError error : errors) {
            if (!error.test(file))
                continue;
            errorMessages.add(error.toString());
            if (!error.canContinueWithCheck)
                break;
        }
        return errorMessages;
    }
}
