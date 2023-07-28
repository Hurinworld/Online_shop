package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client getClient(Long id);

    Client getClientByEmail(String email);

    Client createClient(RegisterRequest registerRequest);

    void saveClient(Client client);

    Client updateClient(ClientRequestDTO clientRequestDTO, Client client);

    Client updateUsername(Client currenAuthenticatedtClient, String email);

    void deleteClient(Long id);
}
