package com.agroknow.search.web.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author aggelos
 */
@Controller
public class SimpleController {

    @RequestMapping(value = { "/", "" }, method = { RequestMethod.GET })
    public String home(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "index";
    }

}
