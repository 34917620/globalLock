package com.smart.global_lock_demo.generator;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * key生成器
 * @author wangzhi802
 *
 */
public interface KeyGenerator {

	/**
	 * 生成Key
	 * @param pjp
	 * @return
	 */
	String generateKey(ProceedingJoinPoint pjp);
}
