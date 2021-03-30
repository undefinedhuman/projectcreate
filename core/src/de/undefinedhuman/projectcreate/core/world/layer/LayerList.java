package de.undefinedhuman.projectcreate.core.world.layer;

import java.util.ArrayList;

public class LayerList {

    private ArrayList<Layer> layers;

    public LayerList() {
        this.layers = new ArrayList<>();
    }

    public LayerList addLayer(int maxY, byte blockID, LayerTransition trans) {
        this.layers.add(new Layer(maxY, blockID, trans));
        return this;
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

}
