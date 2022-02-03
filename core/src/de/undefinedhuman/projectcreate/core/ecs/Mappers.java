package de.undefinedhuman.projectcreate.core.ecs;

import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.base.type.TypeComponent;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.combat.CombatComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.rightarm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.food.FoodComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.health.HealthComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.mana.ManaComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentMapper;

public class Mappers {
    public static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper.create(TransformComponent.class);
    public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.create(SpriteComponent.class);
    public static final ComponentMapper<ShoulderComponent> SHOULDER = ComponentMapper.create(ShoulderComponent.class);
    public static final ComponentMapper<RightArmComponent> RIGHT_ARM = ComponentMapper.create(RightArmComponent.class);
    public static final ComponentMapper<NameComponent> NAME = ComponentMapper.create(NameComponent.class);
    public static final ComponentMapper<MovementComponent> MOVEMENT = ComponentMapper.create(MovementComponent.class);
    public static final ComponentMapper<ManaComponent> MANA = ComponentMapper.create(ManaComponent.class);
    public static final ComponentMapper<InteractionComponent> INTERACTION = ComponentMapper.create(InteractionComponent.class);
    public static final ComponentMapper<HealthComponent> HEALTH = ComponentMapper.create(HealthComponent.class);
    public static final ComponentMapper<FoodComponent> FOOD = ComponentMapper.create(FoodComponent.class);
    public static final ComponentMapper<EquipComponent> EQUIP = ComponentMapper.create(EquipComponent.class);
    public static final ComponentMapper<CombatComponent> COMBAT = ComponentMapper.create(CombatComponent.class);
    public static final ComponentMapper<CollisionComponent> COLLISION = ComponentMapper.create(CollisionComponent.class);
    public static final ComponentMapper<AnimationComponent> ANIMATION = ComponentMapper.create(AnimationComponent.class);
    public static final ComponentMapper<TypeComponent> TYPE = ComponentMapper.create(TypeComponent.class);
    public static final ComponentMapper<MouseComponent> MOUSE = ComponentMapper.create(MouseComponent.class);
    public static final ComponentMapper<InventoryComponent> INVENTORY = ComponentMapper.create(InventoryComponent.class);
}
