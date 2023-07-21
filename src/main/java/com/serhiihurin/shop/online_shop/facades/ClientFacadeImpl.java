package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.services.ClientService;
import com.serhiihurin.shop.online_shop.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientFacadeImpl implements ClientFacade{
    private final ClientService clientService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @Override
    public Client getClient(Long id) {
        return clientService.getClient(id);
    }

    //TODO change this update to patch format //done
    //TODO reformat this method for facade style //done
    @Override
    public Client updateClient(ClientRequestDTO clientRequestDTO) {
        Client oldClient = clientService.getClient(clientRequestDTO.getId());
        return clientService.updateClient(clientRequestDTO, oldClient);
    }

    @Override
    public ClientResponseDTO updateUsername(ClientRequestDTO clientRequestDTO) {
        Client oldClient = clientService.getClient(clientRequestDTO.getId());
        Client newClient = clientService.updateUsername(clientRequestDTO, oldClient);

        ClientResponseDTO clientResponseDTO = modelMapper.map(
                newClient, ClientResponseDTO.class
        );

        clientResponseDTO.setAccessToken(jwtService.generateAccessToken(newClient));
        clientResponseDTO.setRefreshToken(jwtService.generateRefreshToken(newClient));

        return clientResponseDTO;
    }

    @Override
    public void deleteClient(Long id) {
        clientService.deleteClient(id);
    }
}
