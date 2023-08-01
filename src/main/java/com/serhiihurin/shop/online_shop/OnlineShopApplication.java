package com.serhiihurin.shop.online_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

//TODO use @Slf4j instead
//TODO add handler for Exception.class
//TODO create gitignore for logs
//TODO add postman collection
@SpringBootApplication
public class OnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

}
