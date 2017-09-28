package com.iwg.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller1 {

	@RequestMapping("/hello")
	public String getHelloResponse() {
		return "Hello Dupa Jas!";
	}
}
