package com.agroknow.search.web.controllers;

import com.agroknow.search.config.json.AgroObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author aggelos
 */
@Controller
public class SimpleMappingController {

    @Autowired
    private AgroObjectMapper objectMapper;

    @RequestMapping(value = { "/", "" }, method = { RequestMethod.GET })
    public String home(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return "index";
    }
}
