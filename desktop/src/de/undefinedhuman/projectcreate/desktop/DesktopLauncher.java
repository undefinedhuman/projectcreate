package de.undefinedhuman.projectcreate.desktop;

import de.undefinedhuman.projectcreate.core.window.Window;

public class DesktopLauncher {
    public static void main(String[] args) {
        Window.instance = new Window();
    }
}