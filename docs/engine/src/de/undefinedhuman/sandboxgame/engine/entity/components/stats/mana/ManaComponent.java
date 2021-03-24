package de.undefinedhuman.sandboxgame.engine.entity.components.stats.mana;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public class ManaComponent extends Component {

    private float maxMana, currentMana;

    public ManaComponent(float maxMana) {
        this.maxMana = maxMana;
        this.currentMana = maxMana;
        this.type = ComponentType.MANA;
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(float currentMana) {
        this.currentMana = currentMana;
    }

    @Override
    public void receive(LineSplitter splitter) {
        this.currentMana = splitter.getNextFloat();
    }

    @Override
    public void send(LineWriter writer) {
    }

}
