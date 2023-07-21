package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;

import java.util.List;

public interface ClientFacade {
    List<Client> getAllClients();

    Client getClient(Long id);

    Client updateClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO updateUsername(ClientRequestDTO clientRequestDTO);

    void deleteClient(Long id);
}
