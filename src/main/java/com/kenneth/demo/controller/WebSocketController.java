package com.kenneth.demo.controller;

import com.kenneth.demo.processor.MyWebSocketHander;
import com.kenneth.demo.websocketHandler.SpringWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class WebSocketController {

    @Autowired
    private SpringWebSocketHandler websocket;


    @RequestMapping("/websocket/loginPage")
    public String loginPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/websocket/login";
    }

    @RequestMapping("/websocket/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username+"登录");
        HttpSession session = request.getSession(false);
        session.setAttribute("SESSION_USERNAME", username); //一般直接保存user实体
        return "redirect:/sendMsg";
    }

    @RequestMapping("/websocket/setUser")
    public String setUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username+"登录");
        HttpSession session = request.getSession(false);
        session.setAttribute("SESSION_USERNAME", username); //一般直接保存user实体
        return "success";
    }

    @RequestMapping("/websocket/send")
    @ResponseBody
    public String send(HttpServletRequest request) {
        String username = request.getParameter("username");
        websocket.sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
        return null;
    }


    @RequestMapping("/websocket/broad")
    @ResponseBody
    public  String broad() {
        websocket.sendMessageToUsers(new TextMessage("发送一条小Broad"));
        System.out.println("群发成功");
        return "broad";
    }

}
