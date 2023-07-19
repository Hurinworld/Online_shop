package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ClientRepository;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
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
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client updateClient(ClientRequestDTO clientRequestDTO, Client client) {
        if (clientRequestDTO.getFirstName() != null) {
            client.setFirstName(clientRequestDTO.getFirstName());
        }
        if (clientRequestDTO.getLastName() != null) {
            client.setLastName(clientRequestDTO.getLastName());
        }
        if (clientRequestDTO.getCash() != null) {
            client.setCash(clientRequestDTO.getCash());
        }
        if (clientRequestDTO.getEmail() != null) {
            client.setEmail(clientRequestDTO.getEmail());
        }
        if (clientRequestDTO.getPassword() != null) {
            client.setPassword(clientRequestDTO.getPassword());
        }

        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
