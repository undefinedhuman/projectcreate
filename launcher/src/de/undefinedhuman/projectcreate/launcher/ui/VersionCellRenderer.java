package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.utils.version.Version;
import de.undefinedhuman.projectcreate.launcher.icon.IconManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class VersionCellRenderer extends DefaultListCellRenderer {

    public ArrayList<Version> versionDownloaded = new ArrayList<>();

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value != null && !versionDownloaded.contains((Version) value) && index != -1 && label.getIcon() == null)
            label.setIcon(IconManager.getInstance().getIcon("download", 16, 16));
        else label.setIcon(null);
        return label;
    }

    public void setVersionDownloaded(Stream<Version> versions) {
        versionDownloaded.clear();
        versions.forEach(versionDownloaded::add);
    }

}
