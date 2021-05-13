package de.undefinedhuman.projectcreate.core.ecs.stats.mana;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public class ManaComponent extends Component {

    private float maxMana, currentMana;

    public ManaComponent(float maxMana) {
        this.maxMana = maxMana;
        this.currentMana = maxMana;
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
