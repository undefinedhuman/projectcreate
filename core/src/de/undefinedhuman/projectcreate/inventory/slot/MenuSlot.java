package de.undefinedhuman.projectcreate.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.gui.Gui;
import de.undefinedhuman.projectcreate.gui.event.ClickListener;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.offset.CenterOffset;

public class MenuSlot extends Slot {

    public ClickListener listener;

    private String previewTextureName;
    private Gui preview;

    public MenuSlot(Constraint x, Constraint y, String previewTextureName, ClickListener listener) {
        super(x, y);
        this.previewTextureName = previewTextureName;
        this.listener = listener;
        set(x, y, new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
        addListener(listener);
    }

    @Override
    public void init() {
        super.init();
        preview = (Gui) new Gui(previewTextureName)
                .set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE))
                .setOffset(new CenterOffset(), new CenterOffset());
        preview.parent = this;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        preview.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        preview.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        preview.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        preview.delete();
    }

}
