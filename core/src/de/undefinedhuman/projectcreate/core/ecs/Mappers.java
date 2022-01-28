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
    public static final ComponentMapper<IDComponent> ID = ComponentMapper.getFor(IDComponent.class);
    public static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<ShoulderComponent> SHOULDER = ComponentMapper.getFor(ShoulderComponent.class);
    public static final ComponentMapper<RightArmComponent> RIGHT_ARM = ComponentMapper.getFor(RightArmComponent.class);
    public static final ComponentMapper<NameComponent> NAME = ComponentMapper.getFor(NameComponent.class);
    public static final ComponentMapper<MovementComponent> MOVEMENT = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<ManaComponent> MANA = ComponentMapper.getFor(ManaComponent.class);
    public static final ComponentMapper<InteractionComponent> INTERACTION = ComponentMapper.getFor(InteractionComponent.class);
    public static final ComponentMapper<HealthComponent> HEALTH = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<FoodComponent> FOOD = ComponentMapper.getFor(FoodComponent.class);
    public static final ComponentMapper<EquipComponent> EQUIP = ComponentMapper.getFor(EquipComponent.class);
    public static final ComponentMapper<CombatComponent> COMBAT = ComponentMapper.getFor(CombatComponent.class);
    public static final ComponentMapper<CollisionComponent> COLLISION = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<AnimationComponent> ANIMATION = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<TypeComponent> TYPE = ComponentMapper.getFor(TypeComponent.class);
    public static final ComponentMapper<MouseComponent> MOUSE = ComponentMapper.getFor(MouseComponent.class);
    public static final ComponentMapper<InventoryComponent> INVENTORY = ComponentMapper.getFor(InventoryComponent.class);
}
