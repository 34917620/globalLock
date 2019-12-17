/**
 * 
 */
package com.smart.global_lock_demo.service;

import org.springframework.stereotype.Service;

import com.smart.global_lock_demo.annotation.GlobalLock;
import com.smart.global_lock_demo.annotation.KeyParam;


/**
 * 测试全局锁的服务类
 * @author wangzhi802
 *
 */
@Service
public class LockTestServiceImpl implements LockTestService {


	@Override
	@GlobalLock(prefix="globalLock",expire=100,delimiter="-")
	public void doSomething(@KeyParam String token) {  //加上注解 @KeyParam 
		try {
        	System.out.println(Thread.currentThread().getName()+"--进入LockTestServiceImpl");
			Thread.sleep(1000*20);  //让线程停留一段时间，方便测试效果
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
