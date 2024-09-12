package com.example.application.errors;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@PermitAll

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = status != null ? Integer.parseInt(status.toString()) : null;

        if (statusCode != null) {
            switch (statusCode) {
                case 400:
                    return "forward:/error-400";
                case 401:
                    return "forward:/error-401";
                case 403:
                    return "forward:/error-403";
                case 404:
                    return "forward:/error-404";
                case 408:
                    return "forward:/error-408";
                case 500:
                    return "forward:/error-500";
                case 502:
                    return "forward:/error-502";
                case 503:
                    return "forward:/error-503";
                case 504:
                    return "forward:/error-504";
                default:
                    return "forward:/error-generic";
            }
        }
        return "forward:/error-generic";
    }
}
