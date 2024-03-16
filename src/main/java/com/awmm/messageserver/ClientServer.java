package com.awmm.messageserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientServer {

    @RequestMapping(path="/")
    public String getHomePage(){
        return "TestPage.html";
    }

}