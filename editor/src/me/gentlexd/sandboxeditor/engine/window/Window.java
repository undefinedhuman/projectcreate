package me.gentlexd.sandboxeditor.engine.window;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import me.gentlexd.sandboxeditor.editor.Editor;
import me.gentlexd.sandboxeditor.editor.EntityEditor;
import me.gentlexd.sandboxeditor.editor.ItemEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame implements ActionListener {

    public static Window instance;

    public Editor editor;

    public Window(LwjglCanvas canvas) {

        setResizable(false);
        setSize(1280, 720);
        Container container = getContentPane();
        container.setLayout(null);

        addMenu();

        canvas.getCanvas().setBounds(25, 305, 480, 360);

        this.editor = new EntityEditor(container);
        add(canvas.getCanvas());

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) { System.exit(0); }

        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    }

    private JMenuItem loadMenuItem, saveMenuItem;

    private void addMenu() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        loadMenuItem = new JMenuItem("Load");
        loadMenuItem.addActionListener(this);
        fileMenu.add(loadMenuItem);

        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        fileMenu.add(saveMenuItem);

        setJMenuBar(menuBar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == loadMenuItem) {

            if(editor instanceof ItemEditor) ((ItemEditor) editor).loadItem();
            if(editor instanceof EntityEditor) ((EntityEditor) editor).loadEntity();

        }

        if(e.getSource() == saveMenuItem) {

            if(editor instanceof ItemEditor) ((ItemEditor) editor).saveItem();
            if(editor instanceof EntityEditor) ((EntityEditor) editor).saveEntity();

        }

    }

    public Editor getEditor() {
        return editor;
    }

}
