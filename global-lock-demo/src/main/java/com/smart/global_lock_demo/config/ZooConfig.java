/**
 * 
 */
package com.smart.global_lock_demo.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangzhi802
 *
 */
@Configuration
public class ZooConfig {
	
	@Value("${zookeeper.server}")
	private String server;
	
	@Value("${zookeeper.lockPath}")
	private String lockPath;
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getLockPath() {
		return lockPath;
	}
	public void setLockPath(String lockPath) {
		this.lockPath = lockPath;
	}
	
	@Bean
	public CuratorFramework curatorFramework(ZooConfig config){
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(config.getServer()).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		curatorFramework.start();
		return curatorFramework;
	}

}
