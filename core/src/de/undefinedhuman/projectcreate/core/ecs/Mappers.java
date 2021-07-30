package de.undefinedhuman.projectcreate.core.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import de.undefinedhuman.projectcreate.core.ecs.angle.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.core.ecs.combat.CombatComponent;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.food.FoodComponent;
import de.undefinedhuman.projectcreate.core.ecs.health.HealthComponent;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionComponent;
import de.undefinedhuman.projectcreate.core.ecs.mana.ManaComponent;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.name.NameComponent;
import de.undefinedhuman.projectcreate.core.ecs.rightarm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.ecs.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.type.TypeComponent;

public class Mappers {
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
    public static final ComponentMapper<AngleComponent> ANGLE = ComponentMapper.getFor(AngleComponent.class);
    public static final ComponentMapper<TypeComponent> TYPE = ComponentMapper.getFor(TypeComponent.class);
}
