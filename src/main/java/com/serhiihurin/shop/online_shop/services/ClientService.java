package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client getClient(Long id);

    Client getClientByEmail(String email);

    void saveClient(Client client);

    Client updateClient(ClientRequestDTO clientRequestDTO, Client client);

    Client updateUsername(ClientRequestDTO clientRequestDTO, Client client);

    void deleteClient(Long id);
}
