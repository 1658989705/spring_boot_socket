package com.study.thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiController {
	  @RequestMapping("index")
      public String index() {
    	  return "index";
      }
}
