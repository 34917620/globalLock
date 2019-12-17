/**
 * 
 */
package com.smart.global_lock_demo.exception;

/**
 * @author wangzhi802
 *
 */
public class GlobalLockException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GlobalLockException(String desc){
		super(desc);
	}
}
