package com.serhiihurin.shop.online_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO update postman collection //done
//TODO spring mail + thymeleaf //done
//TODO check cascades in entire project
//TODO add search for products by params with sort //done
//TODO add notifications //done
//TODO add work with files //done
//TODO move search to db


//TODO add trigger for mail-sending when product from wishlist is available for sale again! //done
//TODO add saving state of email sending queue
@SpringBootApplication
public class OnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

}
