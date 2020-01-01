package de.undefinedhuman.sandboxgame.world.layer;

import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.WorldLayer;

public class Layer {

    public int maxY;
    public byte blockID;
    public LayerTransition trans;

    public Layer(int maxY, byte blockID, LayerTransition trans) {
        this.maxY = maxY;
        this.blockID = blockID;
        this.trans = trans;
    }

    public boolean isMaxY(WorldLayer worldLayer, int x, int y) { return Tools.getLayerTransitionMaxY(worldLayer, x, y, maxY, trans); }

}
