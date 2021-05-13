package de.undefinedhuman.projectcreate.game.config;

import de.undefinedhuman.projectcreate.engine.config.ConfigValidator;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;

public class GameConfigVa extends ConfigValidator<GameConfig> {

    public GameConfigVa() {
        super(new ValidationRule<>(config -> config.displayWidth.getValue() > 0, "wefewf", gameConfig -> gameConfig.displayWidth.setValue(0f)));
    }

}
