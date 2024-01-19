package com.example.tareas.controlador;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class ErrorControlador implements ErrorController {
    private final ErrorAttributes errorAttributes;
    public ErrorControlador(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
    @RequestMapping("/error")
    public String manejarError(WebRequest webRequest, ModelMap modelo){
        modelo.addAllAttributes(errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults()));
        return "vistaError.html";
    }
}