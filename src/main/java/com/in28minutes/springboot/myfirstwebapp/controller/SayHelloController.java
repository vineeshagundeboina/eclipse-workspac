package com.in28minutes.springboot.myfirstwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SayHelloController {
	
	@ResponseBody
	@RequestMapping("/say-hello")
	public String sayHello() {
		return "hello";
	}
	
	
	@ResponseBody
	@RequestMapping("/say-hellohtml")
	public String sayHelloHtml() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title> My First HTML Page - Changed</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("My first html page with body - Changed");
		sb.append("</body>");
		sb.append("</html>");
		
		return sb.toString();
	}
	//@ResponseBody
	@RequestMapping("/say-hello-jsp")
	public String sayHelloJsp() {
		
		return "sayHello";
	}

}
