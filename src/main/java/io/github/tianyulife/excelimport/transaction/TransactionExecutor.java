package io.github.tianyulife.excelimport.transaction;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 11:58
 * @Description: io.github.tianyulife.excelimport.transaction
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Slf4j
public class TransactionExecutor {

    private final TransactionTemplate defaultTemplate;
    private final TaskExecutor taskExecutor;

    public TransactionExecutor(TransactionTemplate defaultTemplate, TaskExecutor taskExecutor) {
        this.defaultTemplate = defaultTemplate;
        this.taskExecutor = taskExecutor;
    }

    public void runInTransaction(Runnable action) {
        defaultTemplate.executeWithoutResult(status -> action.run());
    }

    public <T> T callInTransaction(Supplier<T> action) {
        return defaultTemplate.execute(status -> action.get());
    }

    public void runAsyncTransaction(Runnable action) {
        taskExecutor.execute(() -> runInTransaction(action));
    }

    public <T> CompletableFuture<T> callAsyncTransaction(Supplier<T> action) {
        return CompletableFuture.supplyAsync(() -> callInTransaction(action), taskExecutor);
    }

    public void runWithCustom(TransactionDefinition def, Runnable action) {
        TransactionTemplate customTemplate = new TransactionTemplate(defaultTemplate.getTransactionManager(), def);
        customTemplate.executeWithoutResult(status -> action.run());
    }

    public <T> T callWithCustom(TransactionDefinition def, Supplier<T> action) {
        TransactionTemplate customTemplate = new TransactionTemplate(defaultTemplate.getTransactionManager(), def);
        return customTemplate.execute(status -> action.get());
    }

    public void rollbackManually(TransactionCallback<?> callback) {
        defaultTemplate.execute(status -> {
            status.setRollbackOnly();
            return callback.doInTransaction(status);
        });
    }

    protected Executor getTaskExecutor() {
        return taskExecutor;
    }
}

