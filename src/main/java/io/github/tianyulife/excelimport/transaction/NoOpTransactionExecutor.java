package io.github.tianyulife.excelimport.transaction;

import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 15:08
 * @Description: io.github.tianyulife.excelimport.transaction
 */
public class NoOpTransactionExecutor extends TransactionExecutor {

    public NoOpTransactionExecutor(TaskExecutor taskExecutor) {
        super(null, taskExecutor); // 注意：传 null 进去不使用
    }

    @Override
    public void runInTransaction(Runnable action) {
        action.run(); // 不包事务直接执行
    }

    @Override
    public <T> T callInTransaction(Supplier<T> action) {
        return action.get(); // 不包事务直接返回
    }

    @Override
    public void runAsyncTransaction(Runnable action) {
        super.getTaskExecutor().execute(action);
    }

    @Override
    public <T> CompletableFuture<T> callAsyncTransaction(Supplier<T> action) {
        return CompletableFuture.supplyAsync(action, super.getTaskExecutor());
    }

    @Override
    public void runWithCustom(TransactionDefinition def, Runnable action) {
        action.run();
    }

    @Override
    public <T> T callWithCustom(TransactionDefinition def, Supplier<T> action) {
        return action.get();
    }

    @Override
    public void rollbackManually(TransactionCallback<?> callback) {
        callback.doInTransaction(null); // 无事务状态也执行
    }
}

