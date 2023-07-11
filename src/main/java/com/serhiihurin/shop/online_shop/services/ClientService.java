package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client saveClient(Client client);

    Client getClient(Long id);

    void deleteClient(Long id);
}
