package de.undefinedhuman.sandboxgame.world.shift;

import java.util.ArrayList;

public class ShiftList {

    private ArrayList<Shift> shifts;

    public ShiftList() {
        this.shifts = new ArrayList<>();
    }

    public ShiftList addShift(Shift shift) {
        this.shifts.add(shift);
        return this;
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }

}
