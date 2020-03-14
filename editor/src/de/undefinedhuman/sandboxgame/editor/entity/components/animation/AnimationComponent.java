package de.undefinedhuman.sandboxgame.editor.entity.components.animation;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class AnimationComponent extends Component {

    private JTextField animationName;
    private JList listPanel;
    private JPanel dataPanel;
    private JScrollPane listScroller;

    private DefaultListModel listModel;
    private JButton addAnimation, removeAnimation;

    private HashMap<String, AnimationParam> animations;
    private String currentSelectedAnimation;

    private StringSetting spriteDataNames, playmode;
    private FloatSetting frameTime;
    private Vector2Setting animationBounds;

    public AnimationComponent(JPanel panel, String name) {

        super(panel, name);

        animations = new HashMap<>();

        animationName = new JTextField();
        animationName.setBounds(25, 125,150,25);

        listModel = new DefaultListModel();

        listPanel = new JList(listModel);
        listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.addListSelectionListener(e -> {

            if(currentSelectedAnimation != null && animations.containsKey(currentSelectedAnimation)) {

                AnimationParam param = animations.get(currentSelectedAnimation);

                param.spriteDataName = spriteDataNames.getValue().toString();
                param.frameTime = Float.parseFloat(frameTime.getValue().toString());
                param.animationBounds = animationBounds.getVector();
                param.mode = playmode.getValue().toString();

            }

            if(listPanel.getSelectedValue() != null) {

                currentSelectedAnimation = listPanel.getSelectedValue().toString();

                if(currentSelectedAnimation != null && animations.containsKey(currentSelectedAnimation)) {

                    AnimationParam param = animations.get(currentSelectedAnimation);

                    spriteDataNames.setValue(param.spriteDataName);
                    frameTime.setValue(param.frameTime);
                    animationBounds.setValue(param.animationBounds);
                    playmode.setValue(param.mode);

                }

            }

        });

        dataPanel = new JPanel(null);
        dataPanel.setBounds(200,25,190,525);
        dataPanel.setBackground(Color.WHITE);
        dataPanel.setOpaque(true);

        listScroller = new JScrollPane(listPanel);
        listScroller.setBounds(25,245,150,325);

        addAnimation = new JButton("Add");
        addAnimation.setBounds(25,205,150,25);
        addAnimation.addActionListener(e -> {

            if(!listModel.contains(animationName.getText())) {

                listModel.addElement(animationName.getText());
                animations.put(animationName.getText(), new AnimationParam());

            }

        });

        removeAnimation = new JButton("Remove");
        removeAnimation.setBounds(25,165,150,25);
        removeAnimation.addActionListener(e -> {

            if(listPanel.getSelectedValue() != null) {

                if(listModel.contains(listPanel.getSelectedValue().toString())) {

                    animations.remove(listPanel.getSelectedValue().toString());
                    listModel.removeElement(listPanel.getSelectedValue().toString());

                }

            }

        });

    }

    @Override
    public void update() {

    }

    @Override
    public void save(FileWriter writer) {

        writer.nextLine();

        if(currentSelectedAnimation != null && animations.containsKey(currentSelectedAnimation)) {

            AnimationParam param = animations.get(currentSelectedAnimation);

            param.spriteDataName = String.valueOf(spriteDataNames.getValue());
            param.frameTime = Float.valueOf(String.valueOf(frameTime.getValue()));
            param.animationBounds = animationBounds.getVector();
            param.mode = playmode.getValue().toString();

        }

        writer.writeString(animations.keySet().toArray()[0].toString());

        writer.writeInt(animations.size());

        for(String title : animations.keySet()) {

            AnimationParam param = animations.get(title);

            writer.writeString(title);

            String[] s = param.spriteDataName.split("-");

            writer.writeInt(s.length);
            for (String value : s) writer.writeString(value);
            writer.writeFloat(param.frameTime);
            writer.writeVector2(param.animationBounds);
            writer.writeString(param.mode);

        }

        writer.nextLine();

    }

    @Override
    public void load(FileReader reader, int id) {

        animations.clear();

        LineSplitter splitter = new LineSplitter(reader.nextLine(),true,";");

        splitter.getNextString();
        int size = splitter.getNextInt();

        for(int i = 0; i < size; i++) {

            String animationname = splitter.getNextString();

            int spriteDataSize = splitter.getNextInt();

            StringBuilder dataNames = new StringBuilder();
            for(int j = 0; j < spriteDataSize; j++) dataNames.append(splitter.getNextString()).append(j != spriteDataSize - 1 ? "-" : "");

            float frameTime = splitter.getNextFloat();
            Vector2 animationBounds = splitter.getNextVector2();
            String modePlay = splitter.getNextString();

            AnimationParam param = new AnimationParam();
            param.spriteDataName = dataNames.toString();
            param.frameTime = frameTime;
            param.animationBounds = animationBounds;
            param.mode = modePlay;

            if(!listModel.contains(animationname) && !animationname.equalsIgnoreCase("")) {

                listModel.addElement(animationname);
                animations.put(animationname, param);

            }

        }

    }

    @Override
    public void addMenuComponent() {

        settings.clear();

        panel.add(listScroller);
        panel.add(addAnimation);
        panel.add(removeAnimation);
        panel.add(animationName);
        panel.add(dataPanel);

        addSetting(spriteDataNames = new StringSetting(dataPanel,"SpriteDatas",""));
        addSetting(playmode = new StringSetting(dataPanel,"PlayMode",""));
        addSetting(frameTime = new FloatSetting(dataPanel,"Frame Time",0.175f));
        addSetting(animationBounds = new Vector2Setting(dataPanel, "Animation Bounds", new Vector2(0,0)));

        super.addMenuComponent();

    }

    @Override
    public void removeMenuComponent() {

        if(currentSelectedAnimation != null && animations.containsKey(currentSelectedAnimation)) {

            AnimationParam param = animations.get(currentSelectedAnimation);

            param.spriteDataName = String.valueOf(spriteDataNames.getValue());
            param.frameTime = Float.parseFloat(String.valueOf(frameTime.getValue()));
            param.animationBounds = animationBounds.getVector();
            param.mode = playmode.getValue().toString();

        }

        currentSelectedAnimation = null;

        dataPanel.removeAll();

        super.removeMenuComponent();

    }

    public class AnimationParam {

        public String spriteDataName = "";
        public Vector2 animationBounds = new Vector2(0,0);
        public float frameTime = 0;
        public String mode = "LOOP";

    }

}
