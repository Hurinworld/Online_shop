package com.serhiihurin.shop.online_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

//TODO create exception (later) and userPrincipal controller advices
//TODO check urls for a hierarchy structure //done
//TODO create gitignore for target //done
//TODO delete .prop //done
//TODO add cors config //done
@SpringBootApplication
public class OnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

}
