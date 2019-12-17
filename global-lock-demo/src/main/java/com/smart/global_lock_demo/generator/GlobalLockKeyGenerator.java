package com.smart.global_lock_demo.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.smart.global_lock_demo.annotation.GlobalLock;
import com.smart.global_lock_demo.annotation.KeyParam;

/**
 * 全局锁 key 生成器
 * @author wangzhi802
 *
 */
@Component
public class GlobalLockKeyGenerator implements KeyGenerator {

	@Override
	public String generateKey(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		GlobalLock lockAnnotation = method.getAnnotation(GlobalLock.class);
		final Object[] args = pjp.getArgs();
		final Parameter[] parameters = method.getParameters();
		StringBuilder builder = new StringBuilder();
		// 默认解析方法里面带 CacheParam 注解的属性,如果没有尝试着解析实体对象中的
		for (int i = 0; i < parameters.length; i++) {
			final KeyParam annotation = parameters[i].getAnnotation(KeyParam.class);
			if (annotation == null) {
				continue;
			}
			builder.append(lockAnnotation.delimiter()).append(args[i]);
		}
		if (StringUtils.isEmpty(builder.toString())) {
			final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			for (int i = 0; i < parameterAnnotations.length; i++) {
				final Object object = args[i];
				final Field[] fields = object.getClass().getDeclaredFields();
				for (Field field : fields) {
					final KeyParam annotation = field.getAnnotation(KeyParam.class);
					if (annotation == null) {
						continue;
					}
					field.setAccessible(true);
					builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
				}
			}
		}
		String prefix=lockAnnotation.prefix();
		 if (StringUtils.isEmpty(prefix)) {
	        	//没有设置key的前缀时，使用类全限定名加方法名作为前缀
			 prefix = pjp.getTarget().getClass().getName()+lockAnnotation.delimiter()+method.getName();
	     }
		
		return prefix + builder.toString();
	}
}
