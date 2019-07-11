package com.kenneth.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//@Controller
@RestController
@RequestMapping("/queryItems")
public class ItemsController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/say")
    public String say(Model model) {
        model.addAttribute("name","porschan");
        return "views/itemsList";
    }

    @RequestMapping("/testHello")
    @ResponseBody
    public String testHello(){
        System.out.println("test hello");
        request.getSession().setAttribute("SESSION_USERNAME", "Mr Hello");
        return "success";
    }

}