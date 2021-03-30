package de.undefinedhuman.projectcreate.core.desktop;

import de.undefinedhuman.projectcreate.core.engine.window.Window;

public class DesktopLauncher {
    public static void main(String[] args) {
        Window.instance = new Window();
    }
}