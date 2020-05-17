package entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.EntityType;
import de.undefinedhuman.sandboxgame.engine.entity.components.interaction.InteractionBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.name.NameBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.food.FoodBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.health.HealthBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.mana.ManaBlueprint;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.Blueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlueprintLoadingTest {

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();
    }

    @Test
    public void BlueprintLoading() {

        FsFile blueprintFile = new FsFile("test/entity/-1/settings.entity", false);
        assertNotNull(blueprintFile.getFile());

        Blueprint blueprint = BlueprintManager.loadBlueprint(blueprintFile);

        assertEquals(-1, blueprint.id.getInt());
        assertEquals("Test Entity", blueprint.name.getString());
        assertEquals(new Vector2(64, 32), blueprint.size.getVector2());
        assertEquals(EntityType.Static, blueprint.type.getEntityType());

        assertEquals(6, blueprint.getComponentBlueprints().size());
        ComponentBlueprint componentBlueprint;

        componentBlueprint = blueprint.getComponentBlueprint(ComponentType.HEALTH);
        assertNotNull(componentBlueprint);
        assertTrue(componentBlueprint instanceof HealthBlueprint);
        HealthBlueprint healthBlueprint = (HealthBlueprint) componentBlueprint;
        assertEquals(20, healthBlueprint.maxHealth.getInt());
        assertEquals(new Vector2(0, 0), healthBlueprint.profileImageOffset.getVector2());

        componentBlueprint = blueprint.getComponentBlueprint(ComponentType.NAME);
        assertNotNull(componentBlueprint);
        assertTrue(componentBlueprint instanceof NameBlueprint);
        NameBlueprint nameBlueprint = (NameBlueprint) componentBlueprint;
        assertEquals("Test entity name", nameBlueprint.name.getString());

        componentBlueprint = blueprint.getComponentBlueprint(ComponentType.MANA);
        assertNotNull(componentBlueprint);
        assertTrue(componentBlueprint instanceof ManaBlueprint);
        ManaBlueprint manaBlueprint = (ManaBlueprint) componentBlueprint;
        assertEquals(100, manaBlueprint.maxMana.getInt());

        componentBlueprint = blueprint.getComponentBlueprint(ComponentType.INTERACTION);
        assertNotNull(componentBlueprint);
        assertTrue(componentBlueprint instanceof InteractionBlueprint);
        InteractionBlueprint interactionBlueprint = (InteractionBlueprint) componentBlueprint;
        assertEquals(100, interactionBlueprint.range.getInt());
        assertEquals(Input.Keys.F, interactionBlueprint.inputKey.getInputKey());

        componentBlueprint = blueprint.getComponentBlueprint(ComponentType.FOOD);
        assertNotNull(componentBlueprint);
        assertTrue(componentBlueprint instanceof FoodBlueprint);
        FoodBlueprint foodBlueprint = (FoodBlueprint) componentBlueprint;
        assertEquals(100, foodBlueprint.maxFood.getInt());

        Log.test("Blueprint loading - PASSED");
    }

    @AfterAll
    static void afterAll() {
        Log.instance.save();
    }

}
