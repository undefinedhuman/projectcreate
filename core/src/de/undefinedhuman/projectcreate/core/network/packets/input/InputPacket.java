package de.undefinedhuman.projectcreate.core.network.packets.input;

import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.inventory.SlotInfo;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionUtils;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import javax.crypto.Cipher;

public class InputPacket implements Packet {

    public static final int DIRECTION = 0x00;
    public static final int JUMP = 0x01;
    public static final int MOUSE_POSITION = 0x02;
    public static final int SELECTOR = 0x03;

    private int code;
    private String data;

    public static InputPacket createDirectionPacket(Cipher encryptionCipher, String sessionID, int direction) {
        return serialize(encryptionCipher, DIRECTION, DirectionData.serialize(sessionID, direction));
    }

    public static InputPacket createJumpPacket(Cipher encryptionCipher, String sessionID) {
        return serialize(encryptionCipher, JUMP, sessionID);
    }

    public static InputPacket createMousePacket(Cipher encryptionCipher, String sessionID, MouseComponent component) {
        return serialize(encryptionCipher, MOUSE_POSITION, MouseData.serialize(sessionID, component));
    }

    public static InputPacket createSelectionPacket(Cipher encryptionCipher, String sessionID, String inventoryName, int selectedIndex) {
        return serialize(encryptionCipher, SELECTOR, sessionID + ":" + inventoryName + ":" + selectedIndex);
    }

    public static InputPacket createInvSelectionPacker(Cipher encryptionCipher, String sessionID, SlotInfo slotInfo) {
        return serialize(encryptionCipher, SELECTOR, InvSelectionData.serialize(sessionID, slotInfo));
    }

    private static InputPacket serialize(Cipher encryptionCipher, int code, String data) {
        if(encryptionCipher == null) {
            Log.warn("No encryption cipher supplied while sending input packet!");
            return null;
        }
        InputPacket packet = new InputPacket();
        packet.code = code;
        packet.data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, data));
        return packet;
    }

    public static String parse(Cipher decryptionCipher, InputPacket packet) {
        if(decryptionCipher == null) {
            Log.warn("No decryption cipher supplied while parsing input packet!");
            return null;
        }
        return EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
    }

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public static class DirectionData {
        public String sessionID;
        public int direction;
        private DirectionData(String sessionID, int direction) {
            this.sessionID = sessionID;
            this.direction = direction;
        }
        public static String serialize(String sessionID, int direction) {
            return sessionID + ":" + direction;
        }
        public static DirectionData parse(String input) {
            String[] data = input.split(":");
            Integer direction;
            if(data.length != 2 || data[0].equalsIgnoreCase("") || (direction = Utils.isInteger(data[1])) == null) return null;
            return new DirectionData(data[0], direction);
        }
    }

    public static class MouseData {
        public String sessionID;
        public float mouseX, mouseY;
        public boolean left, right, canShake;
        private MouseData(String sessionID, float mouseX, float mouseY, boolean left, boolean right, boolean canShake) {
            this.sessionID = sessionID;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.left = left;
            this.right = right;
            this.canShake = canShake;
        }
        public static String serialize(String sessionID, MouseComponent mouseComponent) {
            return sessionID + ":" + mouseComponent.mousePosition.x + ":" + mouseComponent.mousePosition.y + ":" + FileUtils.booleanToInt(mouseComponent.isLeftMouseButtonDown) + ":" + FileUtils.booleanToInt(mouseComponent.isRightMouseButtonDown) + ":" + FileUtils.booleanToInt(mouseComponent.canShake);
        }
        public static MouseData parse(String input) {
            String[] data = input.split(":");
            Float mouseX, mouseY;
            if(data.length != 6 || data[0].equalsIgnoreCase("") || (mouseX = Utils.isFloat(data[1])) == null || (mouseY = Utils.isFloat(data[2])) == null) return null;
            return new MouseData(data[0], mouseX, mouseY, FileUtils.readBoolean(data[3]), FileUtils.readBoolean(data[4]), FileUtils.readBoolean(data[5]));
        }
    }

    public static class InvSelectionData {
        public String sessionID;
        public String inventoryName;
        public long worldID;
        public int row, col;

        private InvSelectionData(String sessionID, long worldID, String inventoryName, int row, int col) {
            this.sessionID = sessionID;
            this.worldID = worldID;
            this.inventoryName = inventoryName;
            this.row = row;
            this.col = col;
        }
        public static String serialize(String sessionID, SlotInfo slotInfo) {
            return sessionID + ":" + slotInfo.getLinkedEntityID() + ":" + slotInfo.getLinkInventoryName() + ":" + slotInfo.getRow() + ":" + slotInfo.getCol();
        }
        public static InvSelectionData parse(String input) {
            String[] data = input.split(":");
            Long worldID;
            Integer row, col;
            if(data.length != 5 || data[0].equalsIgnoreCase("") || (worldID = Utils.isLong(data[1])) == null || (row = Utils.isInteger(data[3])) == null || (col = Utils.isInteger(data[4])) == null) return null;
            return new InvSelectionData(data[0], worldID, data[2], row, col);
        }
    }

}
