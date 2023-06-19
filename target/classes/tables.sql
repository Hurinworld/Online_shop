USE online_shop;

CREATE TABLE online_shop.clients (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(30),
    cash int,
    PRIMARY KEY(id)
);

CREATE TABLE online_shop.shop (
    id int NOT NULL AUTO_INCREMENT,
    name varchar (30),
    income int,
    PRIMARY KEY (id)
);

CREATE TABLE online_shop.feedbacks (
    id int NOT NULL AUTO_INCREMENT,
    text varchar(500),
    time time,
    rate int,
    product_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES online_shop.products(id)
);

CREATE TABLE online_shop.products (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(50),
    description varchar(200),
    price int,
    count int,
    shop_id int,
    feedback_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (shop_id) REFERENCES online_shop.shop (id),
    FOREIGN KEY (feedback_id) REFERENCES online_shop.feedbacks (id)
);

CREATE TABLE online_shop.purchases (
    id int NOT NULL AUTO_INCREMENT,
    time time,
    product_id int,
    client_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES online_shop.clients(id),
    FOREIGN KEY (product_id) REFERENCES online_shop.products(id),
);