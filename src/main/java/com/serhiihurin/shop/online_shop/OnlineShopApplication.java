package com.serhiihurin.shop.online_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//TODO create exception (later) and userPrincipal controller advices
//TODO check urls for an hierarchy structure
//TODO create gitignore for target
//TODO delete .prop
//TODO add cors config
@SpringBootApplication
public class OnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

}
