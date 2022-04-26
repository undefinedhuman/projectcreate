package de.undefinedhuman.projectcreate.kamino.event.events.item;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.base.WorldItemEvent;

@KaminoEvent
public class ItemDropEvent extends WorldItemEvent {

    public final int amount;

    public ItemDropEvent(int itemID, String worldName, int amount) {
        super(itemID, worldName);
        this.amount = amount;
    }

}
