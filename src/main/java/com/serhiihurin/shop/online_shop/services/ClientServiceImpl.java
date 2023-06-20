package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ClientRepository;
import com.serhiihurin.shop.online_shop.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client getClient(Long id) {
        Client client = null;
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            client = optionalClient.get();
        }
        return client;
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
