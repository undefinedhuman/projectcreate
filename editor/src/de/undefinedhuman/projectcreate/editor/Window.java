package de.undefinedhuman.projectcreate.editor;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.formdev.flatlaf.FlatDarculaLaf;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.editor.editor.item.ItemEditor;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Window extends JFrame {

    private static Window instance;

    private static Class<?>[] EDITOR_CLASSES = new Class[] { ItemEditor.class };
    private static final ArrayList<Editor> EDITOR_INSTANCES = new ArrayList<>();
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    public static final int MENU_HEIGHT = 40;

    public Editor editor;
    public JLabel errorMessage;

    private float errorTime = 0;
    private Container container;
    private boolean hasError = false;

    private Window() {
        FlatDarculaLaf.install();
        SettingsUtils.setCustomUIComponentProperties();

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(new Main());
        canvas.getCanvas().setBounds(25, 300, 480, 345);

        initLogger();

        errorMessage = new JLabel("");
        errorMessage.setForeground(new Color(255, 85, 85));

        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        setContentPane(container = new JPanel(new BorderLayout()));
        container.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        container.setPreferredSize(new Dimension(1980, 1080));

        JPanel menuButtons = new JPanel(new GridLayout(1, 3, 5, 0));
        menuButtons.setBorder(new EmptyBorder(5, 5, 5, 5));

        JTabbedPane editorMenu = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        editorMenu.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT - MENU_HEIGHT));
        editorMenu.setFont(editorMenu.getFont().deriveFont(Font.BOLD));
        createEditorMenuTabs(editorMenu, EDITOR_CLASSES);
        editorMenu.addChangeListener(e -> createEditorMenuButtons(editorMenu.getSelectedIndex(), menuButtons));

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, MENU_HEIGHT));
        menuPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, MENU_HEIGHT));
        createEditorMenuButtons(0, menuButtons);

        menuPanel.add(errorMessage, BorderLayout.CENTER);
        menuPanel.add(menuButtons, BorderLayout.EAST);

        container.add(editorMenu, BorderLayout.CENTER);
        container.add(menuPanel, BorderLayout.SOUTH);

        pack();
        setIcon();
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

    private void initLogger() {
        Log.getInstance()
                .setLogMessageDecorator(
                        new LogMessage().andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT)).andThen(value -> LogMessageDecorators.withModuleName(value, "Editor"))
                )
                .addLogEvent((level, decoratedMessage, message) -> {
                    if(level != Level.ERROR)
                        return;
                    errorMessage.setText("  " + message);
                    hasError = true;
                });
        Log.getInstance().init();
        Log.getInstance().load();
    }

    private void createEditorMenuButtons(int index, JPanel menuButtonPanel) {
        if(Tools.isInRange(index, 0, EDITOR_INSTANCES.size()-1))
        menuButtonPanel.removeAll();
        EDITOR_INSTANCES.get(index).createMenuButtonsPanel(menuButtonPanel);
        menuButtonPanel.revalidate();
        menuButtonPanel.repaint();
    }

    private void createEditorMenuTabs(JTabbedPane editorTabMenu, Class<?>... editorClasses) {
        for(Class<?> editorClass : editorClasses) {
            if(editorClass.isInstance(Editor.class))
                continue;
            String name = editorClass.getSimpleName().split("Editor")[0];
            Editor editor = createNewEditorInstance((Class<? extends Editor>) editorClass);
            if(editor == null) {
                Log.showErrorDialog("Error while creating editor class instance!", false);
                continue;
            }
            EDITOR_INSTANCES.add(editor);
            editorTabMenu.addTab(name, editor);
        }
    }

    private Editor createNewEditorInstance(Class<? extends Editor> editor) {
        try {
            return editor.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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

    private void setIcon() {
        try {
            setIconImage(ImageIO.read(new FsFile("logo/96x96.png", Files.FileType.Internal).file()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
