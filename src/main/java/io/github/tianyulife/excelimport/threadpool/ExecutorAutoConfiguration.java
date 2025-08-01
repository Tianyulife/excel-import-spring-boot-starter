package io.github.tianyulife.excelimport.threadpool;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池统一配置类<br>
 * corePoolSize 核心线程池大小<br>
 * maxPoolSize 最大线程池大小<br>
 * queueCapacity 队列长度<br>
 * worker 列满时，才会创建>corePoolSize <=maxPoolSize之间的线程worker，所以如果maxPoolSize > corePoolSize，则需要配置worker队列长度<br>
 * 
 */
@Configuration
public class ExecutorAutoConfiguration {
	private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
	private static final int DEFAULT_CORE_POOL_SIZE = AVAILABLE_PROCESSORS * 6;
	private static final int DEFAULT_MAX_POOL_SIZE = AVAILABLE_PROCESSORS * 8;
	private static final int DEFAULT_QUEUE_CAPACITY = 2000;
	private static final ThreadPoolSetting DEFAULT_SETTING = new ThreadPoolSetting(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_QUEUE_CAPACITY);
	
	/**
	 * 全局使用线程池
	 */
	@Bean
	@ConditionalOnMissingBean(Executor.class)
	public Executor globalThreadPool(ThreadPoolProperties threadPoolProperties) {
		ThreadPoolSetting setting = threadPoolProperties.getSettings() == null ? DEFAULT_SETTING : threadPoolProperties.getSettings().getOrDefault("global", DEFAULT_SETTING);
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(setting.getCorePoolSize());
		executor.setMaxPoolSize(setting.getMaxPoolSize());
		executor.setThreadNamePrefix("excel-import-global-thread-");
		executor.setKeepAliveSeconds(1800);
		executor.setQueueCapacity(setting.getQueueCapacity());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		//执行初始化
		executor.initialize();
		return executor;
	}
	
	@Configuration
	@ConfigurationProperties(prefix = "thread-pool")
	@Data
	public static class ThreadPoolProperties {
		private Map<String, ThreadPoolSetting> settings;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class ThreadPoolSetting {
		
		private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
		
		private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
		
		private int queueCapacity = DEFAULT_QUEUE_CAPACITY;
	}
}
