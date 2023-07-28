package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ClientRepository;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find user"));
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("Could not find user"));
    }

    @Override
    public Client createClient(RegisterRequest registerRequest) {
        Client client = Client.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .cash(registerRequest.getCash())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        return clientRepository.save(client);
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
        if (clientRequestDTO.getPassword() != null) {
            client.setPassword(clientRequestDTO.getPassword());
        }

        return clientRepository.save(client);
    }

    //TODO work only with username in args //done
    @Override
    public Client updateUsername(Client currenAuthenticatedtClient, String email) {
        if (!currenAuthenticatedtClient.getEmail().equals(email)) {
            currenAuthenticatedtClient.setEmail(email);
        }
        return clientRepository.save(currenAuthenticatedtClient);
    }


    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
