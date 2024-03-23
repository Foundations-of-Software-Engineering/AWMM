package com.awmm.messageserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for handling initial website GET.
 * @author AWMM
 */
@Controller
public class ClientServer {

    /**
     * Request mapping for the root URL ("/").
     * Returns the homepage for the client.
     *
     * @return The HTML page to display as the homepage.
     */
    @RequestMapping(path="/")
    public String getHomePage(){
        return "index.html";
    }

}