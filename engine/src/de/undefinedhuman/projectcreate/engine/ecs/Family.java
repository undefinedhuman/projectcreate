package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Bits;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.Exclude;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.One;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.Optional;
import de.undefinedhuman.projectcreate.engine.utils.GenericBuilder;

import java.util.HashMap;

public class Family {
    private static final HashMap<String, Family> FAMILIES = new HashMap<>();
    private static int FAMILY_INDEX = 0;
    private static final Bits ZERO_BITS = new Bits();

    private Bits all = ZERO_BITS;
    private Bits one = ZERO_BITS;
    private Bits exclude = ZERO_BITS;
    private Bits optional = ZERO_BITS;
    private int index = 0;

    private Family() {}

    final void all(All all) {
        if(all == null) return;
        this.all = ComponentType.getBitsFor(all.value());
    }

    final void one(One one) {
        if(one == null) return;
        this.one = ComponentType.getBitsFor(one.value());
    }

    final void exclude(Exclude exclude) {
        if(exclude == null) return;
        this.exclude = ComponentType.getBitsFor(exclude.value());
    }

    final void optional(Optional optional) {
        if(optional == null) return;
        this.optional = ComponentType.getBitsFor(optional.value());
    }

    public int getIndex() {
        return index;
    }

    public Bits getOptional() {
        return optional;
    }

    public boolean matches(Entity entity) {
        Bits entityComponentBits = entity.getComponentBits();
        if(!entityComponentBits.containsAll(all)) return false;
        if(!one.isEmpty() && !one.intersects(entityComponentBits)) return false;
        return exclude.isEmpty() || !exclude.intersects(entityComponentBits);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return createFamilyHashFromBits(all, one, exclude, optional);
    }

    static class FamilyBuilder extends GenericBuilder<Family> {

        public FamilyBuilder() {
            super(Family::new);
        }

        @Override
        public Family build() {
            Family newFamily = super.build();
            String key = newFamily.toString();
            Family oldFamily = FAMILIES.get(key);
            if(oldFamily != null)
                return oldFamily;
            FAMILIES.put(key, newFamily);
            newFamily.index = FAMILY_INDEX++;
            return newFamily;
        }

    }

    public static String createFamilyHashFromBits(Bits all, Bits one, Bits exclude, Bits optional) {
        StringBuilder hash = new StringBuilder();
        if (!all.isEmpty()) hash.append("{all:").append(getBitsString(all)).append("}");
        if (!one.isEmpty()) hash.append("{one:").append(getBitsString(one)).append("}");
        if (!exclude.isEmpty()) hash.append("{exclude:").append(getBitsString(exclude)).append("}");
        if (!optional.isEmpty()) hash.append("{optional:").append(getBitsString(optional)).append("}");
        return hash.toString();
    }

    private static String getBitsString(Bits bits) {
        StringBuilder bitString = new StringBuilder();
        int length = bits.length();
        for (int i = 0; i < length; ++i)
            bitString.append(bits.get(i) ? "1" : "0");
        return bitString.toString();
    }

}
