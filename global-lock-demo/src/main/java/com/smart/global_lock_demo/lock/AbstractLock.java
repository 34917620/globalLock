/**
 * 
 */
package com.smart.global_lock_demo.lock;

import java.util.concurrent.TimeUnit;

/**
 * 锁的抽象类
 * @author wangzhi802
 *
 */
public abstract class AbstractLock {

    public abstract boolean tryLock(String lockPath,int expire,TimeUnit timeUnti);

    public abstract void releaseLock(String lockPath) throws Exception;
    
    
}

