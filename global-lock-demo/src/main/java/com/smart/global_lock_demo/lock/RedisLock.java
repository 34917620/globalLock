/**
 * 
 */
package com.smart.global_lock_demo.lock;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author wangzhi802
 *
 */
@Component("redisLock")
public class RedisLock extends AbstractLock {
	
	@Autowired
	private  StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean tryLock(String lockPath, int expire,TimeUnit timeUnit) {
		boolean flag = true;
		try{
	        if (stringRedisTemplate.opsForValue().setIfAbsent(lockPath, "")) {
	        	if(expire>0){  //当超时时间设置大于0时，才设置超时时间
	               stringRedisTemplate.expire(lockPath, expire, timeUnit); //设置超时时间
	        	}
	        	System.out.println(Thread.currentThread().getName()+"--上锁成功： "+lockPath);
	        	
	        	//这1218
	        } else {
	        	
	            //设置失败，代表已经被锁
	        	System.out.println(Thread.currentThread().getName()+"--上锁失败： "+lockPath);
	            flag = false;
	        }
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public void releaseLock(String lockPath) throws Exception {
		stringRedisTemplate.delete(lockPath);
	}

}
