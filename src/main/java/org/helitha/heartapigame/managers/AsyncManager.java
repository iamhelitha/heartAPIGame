package org.helitha.heartapigame.managers;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
public class AsyncManager {

    private static AsyncManager instance;
    private final ExecutorService executor;

    private AsyncManager() {
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

    public void runAsync(Runnable task) {
        executor.submit(task);
    }

    public void runAsync(Runnable task, Runnable onComplete) {
        executor.submit(() -> {
            task.run();
            Platform.runLater(onComplete);
        });
    }

    public void shutdown() {
        executor.shutdown();
    }

    @FunctionalInterface
    public interface AsyncTask<T> {
        T execute() throws Exception;
    }
}
