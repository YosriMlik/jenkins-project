package tn.iit.jenkinsProj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
@CrossOrigin(origins = "*")
public class HelloController {
	
	
	
	@GetMapping("/hi")
	public String sayHello() {
		return "hello from controller";
	}
	@GetMapping("/hiiiii")
	public String sayHello11() {
		return "hello from controller";
	}
	@GetMapping("/bonjour")
	public String sayBonjour() {
		return "bonjour from controller";
	}
	@GetMapping("/bhi2")
	public String sayHi() {
		return "Hi from controller";
	}
	@GetMapping("/bhi23")
	public String sayHiii() {
		return "Hi from controller";
	}
	

}
