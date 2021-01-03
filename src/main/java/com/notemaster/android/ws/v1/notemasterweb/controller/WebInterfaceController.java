package com.notemaster.android.ws.v1.notemasterweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/* @Controller annotation send the web page back from the template location
   @RestController annotation sends back "path" as a String value     
 */
@Controller
public class WebInterfaceController {
    
	@GetMapping("/index")
    public String path() {
        return "home";
    }

	@GetMapping("/")
    public String root() {
        return "home";
    }	

	@GetMapping("/hello")
    public String hello() {
        return "hello";
    }

	@GetMapping("/login")
    public String login() {
        return "login";
    }

	// if you use '/error' it will override the error handling of the rest controllers
	// and return the web page as answer no tJSON. This we do not want. Therefore we use '/error2'
	// also see the page that returns the error login.html
	
	@GetMapping("/error2")
    public String error() {
        return "error";
    }	
	
}
