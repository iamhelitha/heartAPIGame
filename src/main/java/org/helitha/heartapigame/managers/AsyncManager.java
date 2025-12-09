package org.helitha.heartapigame.managers;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * AsyncManager - Centralized thread management for background operations
 * Uses ExecutorService for proper lifecycle management instead of raw threads
 */
public class AsyncManager {

    private static AsyncManager instance;
    private final ExecutorService executor;

    private AsyncManager() {
        // Cached thread pool for flexible background tasks
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public static AsyncManager getInstance() {
        if (instance == null) {
            instance = new AsyncManager();
        }
        return instance;
    }

    /**
     * Execute a background task with UI callback on success
     */
    public <T> void runAsync(AsyncTask<T> task, Consumer<T> onSuccess, Consumer<Exception> onError) {
        executor.submit(() -> {
            try {
                T result = task.execute();
                Platform.runLater(() -> onSuccess.accept(result));
            } catch (Exception e) {
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }

    /**
     * Execute a simple background task without return value
     */
    public void runAsync(Runnable task) {
        executor.submit(task);
    }

    /**
     * Execute a background task and update UI when complete
     */
    public void runAsync(Runnable task, Runnable onComplete) {
        executor.submit(() -> {
            task.run();
            Platform.runLater(onComplete);
        });
    }

    /**
     * Shutdown the executor (call when application exits)
     */
    public void shutdown() {
        executor.shutdown();
    }

    @FunctionalInterface
    public interface AsyncTask<T> {
        T execute() throws Exception;
    }
}
