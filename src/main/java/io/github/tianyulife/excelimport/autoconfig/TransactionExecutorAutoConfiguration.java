package io.github.tianyulife.excelimport.autoconfig;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 11:48
 * @Description: io.github.tianyulife.excelimport.autoconfig
 */
import io.github.tianyulife.excelimport.transaction.NoOpTransactionExecutor;
import io.github.tianyulife.excelimport.transaction.TransactionExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


@Configuration
public class TransactionExecutorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(PlatformTransactionManager.class)
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionExecutor.class)
    @ConditionalOnBean(TransactionTemplate.class)
    public TransactionExecutor transactionExecutor(TransactionTemplate transactionTemplate, TaskExecutor taskExecutor) {
        return new TransactionExecutor(transactionTemplate, taskExecutor);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionExecutor.class)
    public TransactionExecutor noOpTransactionExecutor(TaskExecutor taskExecutor) {
        return new NoOpTransactionExecutor(taskExecutor);
    }

}

