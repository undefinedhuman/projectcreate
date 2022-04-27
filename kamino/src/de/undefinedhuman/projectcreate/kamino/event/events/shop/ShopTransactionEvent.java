package de.undefinedhuman.projectcreate.kamino.event.events.shop;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.kamino.event.events.base.ItemEvent;

@KaminoEvent
public class ShopTransactionEvent extends ItemEvent {

    @Metadata
    public final String playerUUID;
    public final String shopID;
    public final int amount;
    public final int price;

    public ShopTransactionEvent(int itemID, String playerUUID, String shopID, int amount, int price) {
        super(itemID);
        this.playerUUID = playerUUID;
        this.shopID = shopID;
        this.amount = amount;
        this.price = price;
    }

}
