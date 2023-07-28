package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;

import java.util.List;

public interface ClientFacade {
    List<Client> getAllClients();

    Client getClient(Long id);

    Client getClientByEmail(String email);

    Client createClient(RegisterRequest registerRequest);

    Client updateClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO updateUsername(Client currentAuthenticatedClient, String email);

    void deleteClient(Long id);
}
