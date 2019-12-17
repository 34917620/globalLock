/**
 * 
 */
package com.smart.global_lock_demo.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.smart.global_lock_demo.config.ZooConfig;
import com.smart.global_lock_demo.exception.GlobalLockException;

/**
 * 由于 InterProcessMutex 是与path绑定的，所有每个key需要一个实例 ，因此要用缓存保存相关实例。
 * zookeepr锁的实现类
 * @author wangzhi802
 *
 */
@Component("zooLock")
public class ZooLock extends AbstractLock {

	private final static Map<String,InterProcessMutex> CACHE = new ConcurrentHashMap<String, InterProcessMutex>();
	
	@Autowired
	private CuratorFramework curatorFramework;
	
	@Autowired
	private ZooConfig zooConfig;
	
	
	@Override
	public boolean tryLock(String key, int expire,TimeUnit timeUnit) {
		String lockPath = zooConfig.getLockPath()+key;
		synchronized (lockPath) {
			if (CACHE.get(lockPath) == null) {
				CACHE.put(lockPath, new InterProcessMutex(curatorFramework, lockPath));
			}
		}
		InterProcessMutex interProcessMutex = CACHE.get(lockPath);
		boolean flag = true;
		try {
			flag =interProcessMutex.acquire(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		if(flag){
			System.out.println(Thread.currentThread().getName()+"--上锁成功： "+lockPath);
		}else{
			System.out.println(Thread.currentThread().getName()+"--上锁失败： "+lockPath);
		}
		
		return flag;
	}
	@Override
	public void releaseLock(String key) throws Exception {
		String lockPath = zooConfig.getLockPath()+key;
		synchronized (lockPath) {
			if(CACHE.get(lockPath)==null){
				throw new GlobalLockException("此方法必须与tryLock() 成对出现");
			}
			CACHE.get(lockPath).release();
		}
		System.out.println(Thread.currentThread().getName()+"--释放锁："+lockPath);
	}


}
