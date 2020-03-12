package de.undefinedhuman.sandboxgame.editor.engine.window;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import de.undefinedhuman.sandboxgame.editor.Main;
import de.undefinedhuman.sandboxgame.editor.editor.Editor;
import de.undefinedhuman.sandboxgame.editor.editor.EditorType;
import de.undefinedhuman.sandboxgame.editor.editor.EntityEditor;
import de.undefinedhuman.sandboxgame.editor.editor.ItemEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {

    public static Window instance;

    private Container container;

    private LwjglAWTCanvas canvas;
    private ApplicationListener main;

    public Editor editor;

    public Window() {

        main = new Main();
        canvas = new LwjglAWTCanvas(main);

        setResizable(false);
        setSize(1280, 720);
        container = getContentPane();
        addMenu();

        canvas.getCanvas().setBounds(25,300,480,345);

        setEditor(EditorType.ENTITY);

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) { System.exit(0); }

        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    }

    private JMenuBar menuBar;

    private void addMenu() {

        menuBar = new JMenuBar();

        JMenu fileMenu = addMenu("File");

        addMenuItem(fileMenu, "Load", e -> editor.load());
        addMenuItem(fileMenu, "Save", e -> editor.save());

        JMenu editorMenu = addMenu("Editor");

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
        container.setLayout(null);

        switch (type) {
            case ITEM:
                editor = new ItemEditor(container);
                break;
            case ENTITY:
                editor = new EntityEditor(container);
                break;
        }

        add(canvas.getCanvas());

        revalidate();
        repaint();

        canvas.getCanvas().revalidate();
        canvas.getCanvas().repaint();

    }

}
