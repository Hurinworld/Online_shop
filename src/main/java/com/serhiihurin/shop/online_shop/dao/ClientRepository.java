package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
