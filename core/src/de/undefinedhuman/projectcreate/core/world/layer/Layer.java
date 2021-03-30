package de.undefinedhuman.projectcreate.core.world.layer;

import de.undefinedhuman.projectcreate.core.utils.Tools;

public class Layer {

    public int maxY;
    public byte blockID;
    public LayerTransition trans;

    public Layer(int maxY, byte blockID, LayerTransition trans) {
        this.maxY = maxY;
        this.blockID = blockID;
        this.trans = trans;
    }

    public boolean isMaxY(byte worldLayer, int x, int y) { return Tools.getLayerTransitionMaxY(worldLayer, x, y, maxY, trans); }

}
