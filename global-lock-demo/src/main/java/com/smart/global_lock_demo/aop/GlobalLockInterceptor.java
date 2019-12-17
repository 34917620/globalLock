package com.smart.global_lock_demo.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.smart.global_lock_demo.annotation.GlobalLock;
import com.smart.global_lock_demo.exception.GlobalLockException;
import com.smart.global_lock_demo.generator.KeyGenerator;
import com.smart.global_lock_demo.lock.AbstractLock;

import java.lang.reflect.Method;


/**
 * 全局锁的aop 实现拦截器
 * @author wangzhi802
 *
 */
@Aspect
@Component
public class GlobalLockInterceptor {
	
	@Autowired
	//@Qualifier("zooLock")  //使用zookeeper作为锁
	@Qualifier("redisLock")  //使用redis作为锁
	private AbstractLock lock;
	
    private final KeyGenerator keyGenerator;

	
    /**
     * spring容器自动注入
     * @param stringRedisTemplate
     * @param keyGenerator
     */
    @Autowired
    public GlobalLockInterceptor(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
    
     
    @Around("@annotation(com.smart.global_lock_demo.annotation.GlobalLock)") //拦截注解了GlobalLock的方法，加入上锁逻辑
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable{
    	System.out.println(Thread.currentThread().getName()+"--进入拦截器");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        GlobalLock lockAnnotation = method.getAnnotation(GlobalLock.class); //获取GlobalLock注解
        final String lockKey = keyGenerator.generateKey(pjp);

    	//setIfAbsent key不存在才能设置成功
        if (!lock.tryLock(lockKey, lockAnnotation.expire(),lockAnnotation.timeUnit()))  {
            //设置失败，代表已经被锁
            throw new GlobalLockException(Thread.currentThread().getName()+"--获取锁失败:  "+lockKey);
        }

        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }finally{
        	lock.releaseLock(lockKey);
        }
    }
}
