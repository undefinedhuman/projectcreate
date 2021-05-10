package de.undefinedhuman.projectcreate.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.formdev.flatlaf.FlatDarculaLaf;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.editor.editor.EditorType;
import de.undefinedhuman.projectcreate.editor.editor.entity.EntityEditor;
import de.undefinedhuman.projectcreate.editor.editor.item.ItemEditor;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {

    private static Window instance;
    public Editor editor;

    public JMenuBar menuBar;
    public float errorTime = 0;
    public JLabel errorMessage;
    public JMenu fileMenu, editorMenu;

    private Container container;
    private boolean hasError = false;

    private Window() {
        FlatDarculaLaf.install();
        setUIComponentProperties();
        LwjglAWTCanvas canvas = new LwjglAWTCanvas(new Main());
        canvas.getCanvas().setBounds(25, 300, 480, 345);

        errorMessage = new JLabel();
        errorMessage.setBounds(22, 1002, 1880, 25);
        errorMessage.setForeground(new Color(255, 85, 85));

        Log.getInstance().addLogEvent((level, message) -> {
            if(level != Level.ERROR)
                return;
            errorMessage.setText(message);
            hasError = true;
        });
        Log.getInstance().init();
        Log.getInstance().load();
        setResizable(false);
        setSize(1920, 1080);
        container = getContentPane();
        container.setBackground(new Color(60, 63, 65));
        addMenu();

        setEditor(EditorType.ENTITY);

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Log.getInstance().save();
                Gdx.app.exit();
                System.exit(0);
            }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    }

    private void addMenu() {
        menuBar = new JMenuBar();

        fileMenu = addMenu("File");
        addMenuItem(fileMenu, "Load", e -> editor.load());
        addMenuItem(fileMenu, "Save", e -> editor.save());

        editorMenu = addMenu("Editor");
        addMenuItem(editorMenu, "Item", e -> setEditor(EditorType.ITEM));
        addMenuItem(editorMenu, "Entity", e -> setEditor(EditorType.ENTITY));

        setJMenuBar(menuBar);
    }

    private JMenu addMenu(String name) {
        JMenu menu = new JMenu(name);
        menuBar.add(menu);
        return menu;
    }

    private void addMenuItem(JMenu menu, String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(listener);
        menu.add(item);
    }

    public void setEditor(EditorType type) {
        container.removeAll();
        container.add(errorMessage);
        container.setLayout(null);
        switch (type) {
            case ITEM:
                editor = new ItemEditor(container);
                break;
            case ENTITY:
                editor = new EntityEditor(container);
                break;
        }
        revalidate();
        repaint();
    }

    public void updateErrorTime(float delta) {
        if(!hasError) return;
        this.errorTime -= delta;
        if(errorTime <= 0) {
            errorMessage.setText("");
            hasError = false;
            errorTime = 15;
        }
    }

    private void setUIComponentProperties() {
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 0);
        UIManager.put("CheckBox.arc", 0);
        UIManager.put("ProgressBar.arc", 0);

        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Component.focusWidth", 1);
    }

    public static Window getInstance() {
        if (instance == null) {
            synchronized (Window.class) {
                if (instance == null)
                    instance = new Window();
            }
        }
        return instance;
    }

}
