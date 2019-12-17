package com.smart.global_lock_demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 全局锁注解类，在所需要锁的方法前加上注解。 
 * 锁的key为prefix的值连接 @KeyParam 注解的参数， 连接符为delimiter 设置的值
 * @author wangzhi802
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GlobalLock {

	String prefix() default "";
	
	int expire() default 0; //默认过期时间0秒，表示永不过期。
	
	TimeUnit timeUnit() default TimeUnit.SECONDS;
	
	/**
	 * <p>key的分隔符(默认：)</p>
	 * @return
	 */
	String delimiter() default ":";
}
