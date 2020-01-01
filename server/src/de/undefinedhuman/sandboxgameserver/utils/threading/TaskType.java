package de.undefinedhuman.sandboxgameserver.utils.threading;

public enum TaskType {

    NONE(1), WORLDLOAD(1), WORLDSAVE(1), WORLDGENERATE(1), ENTITY(1), WORLDSEND(1);

    public int numThreads;

    TaskType(int numThreads) {
        this.numThreads = numThreads;
    }

}
