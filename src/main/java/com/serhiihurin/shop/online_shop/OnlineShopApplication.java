package com.serhiihurin.shop.online_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO use @Slf4j instead //done
//TODO add handler for Exception.class //done
//TODO create gitignore for logs //done
//TODO add postman collection
@SpringBootApplication
public class OnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

}
