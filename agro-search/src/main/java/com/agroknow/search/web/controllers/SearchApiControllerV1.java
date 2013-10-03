package com.agroknow.search.web.controllers;

import com.agroknow.search.config.json.AgroObjectMapper;
import com.agroknow.search.domain.SearchRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author aggelos
 */
@Controller("/v1/akif")
public class SearchApiControllerV1 {

    @Autowired
    private AgroObjectMapper objectMapper;

    @RequestMapping(value = { "/", "" }, method = { RequestMethod.GET })
    public String root(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        SearchRequest searchReq = buildSearchRequest(request);
        return "index";
    }

    @RequestMapping(value = "/{id}", method = { RequestMethod.GET })
    public String fetch(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        return "index";
    }

    private SearchRequest buildSearchRequest(HttpServletRequest request) {
        SearchRequest searchReq = new SearchRequest();
        Enumeration<String> params = request.getAttributeNames();
        String param, paramValue;

        while (params.hasMoreElements()) {
            param = params.nextElement();
            paramValue = request.getAttribute(param).toString();

            if("q".equalsIgnoreCase(param)) {
                searchReq.addSearchFields(param, paramValue);
            } else if("sort_by".equalsIgnoreCase(param)) {
                searchReq.setSortBy(Arrays.asList(paramValue.split(",")));
            } else if("sort_order".equalsIgnoreCase(param)) {
                searchReq.setSortOrder("desc");
                if("asc".equalsIgnoreCase(paramValue)) {
                    searchReq.setSortOrder("asc");
                }
            } else if("facets".equalsIgnoreCase(param)) {
                searchReq.setFacetFields(Arrays.asList(paramValue.split(",")));
            } else if("page".equalsIgnoreCase(param)) {
                searchReq.setPage(Integer.parseInt(paramValue));
            } else if("page_size".equalsIgnoreCase(param)) {
                searchReq.setPageSize(Integer.parseInt(paramValue));
            } else {
                searchReq.addSearchFields(param, paramValue);
            }
        }

        return searchReq;
    }

}
