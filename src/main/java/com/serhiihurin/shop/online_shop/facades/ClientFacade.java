package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import org.springframework.http.ResponseEntity;

public interface ClientFacade {
    ClientDTO updateClient(ClientRequestDTO clientRequestDTO);
}
