package de.undefinedhuman.projectcreate.engine.ecs;

public class ComponentParam {

    private Class<? extends Component> type;

    public ComponentParam(Class<? extends Component> type) {
        this.type = type;
    }

    public Class<? extends Component> getType() {
        return type;
    }

}
