package de.undefinedhuman.sandboxgameserver.utils.threading;

import java.util.HashMap;

public class ThreadManager {

    public static ThreadManager instance;
    public HashMap<TaskType, ThreadPool> threadPools;

    public ThreadManager() {

        this.threadPools = new HashMap<>();
        for (TaskType taskType : TaskType.values())
            threadPools.put(taskType, new ThreadPool(taskType, taskType.numThreads));

    }

    public void addTask(TaskType taskType, ThreadTask task) {

        if (threadPools.containsKey(taskType)) this.threadPools.get(taskType).addTask(task);
        else threadPools.get(TaskType.NONE).addTask(task);

    }

    public void saveDelete() {
        for (ThreadPool pool : threadPools.values()) pool.join();
    }

    public void delete() {
        for (ThreadPool pool : threadPools.values()) pool.delete();
    }

}

