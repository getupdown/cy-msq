package cn.cy.common;

import java.util.concurrent.*;

/**
 * <p>
 * the sequence below will not happen
 * <p>
 * Thread a: get(key) == null   ->   new value   ->   set it
 * Thread b:                    get(key) == null   ->   new value  ->  set it
 */
public class ConcurrentFinalCache<K extends Comparable, V> {

    protected ConcurrentMap<K, FutureTask<V>> futureMap = new ConcurrentHashMap<>();

    /**
     * @param key             key
     * @param buildFunction the function to generate the value
     *
     * @return value
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
     protected V compute(K key, Callable<V> buildFunction)
            throws ExecutionException, InterruptedException {

        FutureTask<V> task = futureMap.getOrDefault(key, null);

        if (task != null) {
            return task.get();
        }

        FutureTask<V> wrappedFutureTask = new FutureTask<>(buildFunction);

        FutureTask<V> previous = futureMap.putIfAbsent(key, wrappedFutureTask);

        if (previous == null) {
            wrappedFutureTask.run();
            return wrappedFutureTask.get();
        } else {
            return previous.get();
        }
    }
}
