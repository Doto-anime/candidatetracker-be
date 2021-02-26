package com.dotoanime.candidatetracker.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ErrorHandler implements ErrorController {

    @Autowired
    ErrorAttributes errorAttributes;

    @Deprecated
    @RequestMapping("/error")
    ApiError handleError(WebRequest webRequest) {
        List<ErrorAttributeOptions.Include> attributeOptions =
                new ArrayList<>(Arrays.asList(
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.BINDING_ERRORS
                ));

        Map<String, Object> attributes = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(attributeOptions));

        String message = (String) attributes.get("message");
        String url = (String) attributes.get("path");
        int status = (int) attributes.get("status");

        return new ApiError(status, message, url);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
