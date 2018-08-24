package org.jleopard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 31, 2018 12:29:30 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
@RestController
public class IndexController {
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
}
