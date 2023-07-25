package com.serhiihurin.shop.online_shop.controller_advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//@ControllerAdvice
public class ClientControllerAdvice {

//    @ModelAttribute("currentClient")
//    public UserDetails getCurrentClient() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//
//        return (UserDetails) principal;
//    }
}
