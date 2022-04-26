package de.undefinedhuman.projectcreate.kamino.event.events.entity;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.kamino.event.events.base.WorldEntityEvent;

@KaminoEvent
public class EntityDeathEvent extends WorldEntityEvent {

    public final long attackerBlueprintID;
    public final String attackerUUID;

    public EntityDeathEvent(long blueprintID, Entity entity, String worldName, long attackerBlueprintID) {
        this(blueprintID, entity, worldName, attackerBlueprintID, "NOT_CAUSED_BY_PLAYER");
    }

    public EntityDeathEvent(long blueprintID, Entity entity, String worldName, long attackerBlueprintID, String attackerUUID) {
        super(blueprintID, entity, worldName);
        this.attackerBlueprintID = attackerBlueprintID;
        this.attackerUUID = attackerUUID;
    }

}
