package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.engine.settings.SettingsList;

public abstract class BaseFunction {
    private SettingsList settingsList = new SettingsList();
    public abstract double calculateValue(double x, double y, double value);
}
