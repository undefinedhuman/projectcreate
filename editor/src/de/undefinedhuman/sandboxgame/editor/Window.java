package de.undefinedhuman.sandboxgame.editor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.formdev.flatlaf.FlatDarculaLaf;
import de.undefinedhuman.sandboxgame.editor.editor.Editor;
import de.undefinedhuman.sandboxgame.editor.editor.EditorType;
import de.undefinedhuman.sandboxgame.editor.editor.entity.EntityEditor;
import de.undefinedhuman.sandboxgame.editor.editor.item.ItemEditor;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {

    public static Window instance;
    public Editor editor;

    public JMenuBar menuBar;
    public float errorTime = 0;
    public JLabel errorMessage;
    public JMenu fileMenu, editorMenu;

    private ApplicationListener main;
    private LwjglAWTCanvas canvas;
    private Container container;
    private boolean hasError = false;

    public Window() {

        FlatDarculaLaf.install();

        main = new Main();
        canvas = new LwjglAWTCanvas(main);
        canvas.getCanvas().setBounds(25, 300, 480, 345);

        errorMessage = new JLabel();
        errorMessage.setBounds(22, 1007, 1880, 25);
        errorMessage.setForeground(new Color(255, 85, 85));

        Log.instance = new Log() {
            @Override
            public void displayMessage(String msg) {
                if(!msg.contains("Error")) return;
                errorMessage.setText(msg);
                hasError = true;
            }
        };
        Log.instance.init();
        setResizable(false);
        setSize(1920, 1080);
        container = getContentPane();
        container.setBackground(new Color(60, 63, 65));
        addMenu();

        setEditor(EditorType.ENTITY);

        FileReader reader = new FsFile("old entity/0/settings.entity", false).getFileReader(true);
        while(reader.nextLine() != null) System.out.println(reader.getData());
        reader.close();

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Log.instance.save();
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

}
