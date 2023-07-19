package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;

public interface ClientFacade {
    ClientResponseDTO updateClient(ClientRequestDTO clientRequestDTO);
}
