package me.gentlexd.sandboxeditor.editor.settings;

import me.gentlexd.sandboxeditor.engine.file.FileManager;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.engine.window.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class SoundSetting extends Setting {

    private JLabel label;
    private File sound;

    public SoundSetting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        sound = new File("./core/assets/sounds/dirtSound.wav");

        label = new JLabel();
        label.setSize(50, 50);
        label.setLocation(250, 40);
        label.setBackground(Color.LIGHT_GRAY);

        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {

                String[] name;

                JFileChooser chooser = new JFileChooser();

                chooser.setCurrentDirectory(new File("./core/assets/sounds"));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Sound Files", "wav");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {

                    name = chooser.getSelectedFile().getName().split("\\.");

                    String soundPath;

                    if(chooser.getSelectedFile() != null) {

                        soundPath = "./core/assets/sounds/" + name[0] + ".wav";
                        setValue(name[0] + ".wav");

                    } else {

                        soundPath = "./core/assets/sounds/dirtSound.wav";
                        setValue("dirtSound.wav");

                    }

                    sound = new File(soundPath);

                }

            }

        });

        panel.add(label);

    }

    @Override
    public void update() {

        label.setBounds((int) position.x + 110, (int) position.y, 25, 25);

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(String.valueOf(getValue()));

        try {

            File file = new File(Window.instance.editor.getPath().getPath() + Window.instance.editor.settings.get(0).getValue() + "/" + getValue());
            FileManager.copyFile(sound, file);
            if(file.createNewFile()) Log.info("Sound: " + file.getName() + " wurde erfolgreich erstellt!");

        } catch (IOException e) {

            System.out.println(e.getMessage());
            System.exit(3);

        }

    }

    @Override
    public void load(FileReader reader, int id) {

        String soundName = reader.getNextString();

        try {

            sound = new File(Window.instance.editor.getPath().getPath() + Window.instance.editor.settings.get(0).getValue() + "/" + soundName);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        setValue(soundName);

    }

    @Override
    protected void addGuiSetting() {
        panel.add(label);
    }

}
