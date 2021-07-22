package de.undefinedhuman.projectcreate.game.enemy;

import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.enemy.EnemyFactory;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;

public class EnemySpawner {

    private EnemyFactory enemyFactory;

    public EnemySpawner(EnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
    }

    public void spawnEnemy(int x, int y) {
        Entity entity = BlueprintManager.getInstance().getBlueprint(enemyFactory.getEntityID()).createInstance();
        EquipComponent equipComponent = (EquipComponent) entity.getComponent(EquipComponent.class);
        if(equipComponent == null)
            return;
        equipComponent.setItemID(0, enemyFactory.createArmor());
        equipComponent.setItemID(5, enemyFactory.createWeapon());
        EntityManager.getInstance().addEntity(EntityManager.getInstance().getMaxEntityID(), entity);
    }

}
