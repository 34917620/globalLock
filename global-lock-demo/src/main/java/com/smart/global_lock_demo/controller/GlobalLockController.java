package com.smart.global_lock_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.global_lock_demo.exception.GlobalLockException;
import com.smart.global_lock_demo.service.LockTestService;


@RestController
public class GlobalLockController {
	
	@Autowired
	private LockTestService lockTestService;

    @RequestMapping("/locktest")
    public String locktest(@RequestParam String token) {
    	String msg = "OK - " + token;
    	try {
    		lockTestService.doSomething(token);
		} catch (GlobalLockException e) {
			msg = e.getMessage();
		}
        return msg;
    }

}