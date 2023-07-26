package com.serhiihurin.shop.online_shop.controller_advice;

import com.serhiihurin.shop.online_shop.entity.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;

@ControllerAdvice
public class ClientControllerAdvice {

    @ModelAttribute("currentClient")
    public Client getCurrentClient(WebRequest request, Principal principal) {
        if (request == null) {
            return null;
        }
        if(!(principal instanceof Authentication authentication)) {
            return null;
        }

        return (Client) authentication.getPrincipal();
    }
}