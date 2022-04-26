package de.undefinedhuman.projectcreate.kamino.event.events.entity;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.kamino.event.events.base.WorldEntityEvent;

@KaminoEvent
public class EntityDamageEvent extends WorldEntityEvent {

    public final String damagedPlayerUUID;
    public final long attackerBlueprintID;
    public final String attackerPlayerUUID;

    public EntityDamageEvent(long damagedBlueprintID, Entity damagedEntity, String worldName, long attackerBlueprintID) {
        this(damagedBlueprintID, damagedEntity, worldName, "NOT_DAMAGED_A_PLAYER", attackerBlueprintID, "NOT_DAMAGED_BY_A_PLAYER");
    }

    public EntityDamageEvent(long damagedBlueprintID, Entity damagedEntity, String worldName, String damagedPlayerUUID, long attackerBlueprintID) {
        this(damagedBlueprintID, damagedEntity, worldName, damagedPlayerUUID, attackerBlueprintID, "NOT_DAMAGED_BY_A_PLAYER");
    }

    public EntityDamageEvent(long damagedBlueprintID, Entity damagedEntity, String worldName, long attackerBlueprintID, String attackerPlayerUUID) {
        this(damagedBlueprintID, damagedEntity, worldName, "NOT_DAMAGED_A_PLAYER", attackerBlueprintID, attackerPlayerUUID);
    }

    public EntityDamageEvent(long damagedBlueprintID, Entity damagedEntity, String worldName, String damagedPlayerUUID, long attackerBlueprintID, String attackerPlayerUUID) {
        super(damagedBlueprintID, damagedEntity, worldName);
        this.damagedPlayerUUID = damagedPlayerUUID;
        this.attackerBlueprintID = attackerBlueprintID;
        this.attackerPlayerUUID = attackerPlayerUUID;
    }
}
