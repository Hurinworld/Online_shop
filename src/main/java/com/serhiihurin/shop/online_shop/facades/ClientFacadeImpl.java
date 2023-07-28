package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
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

    @Override
    public Client getClientByEmail(String email) {
        return clientService.getClientByEmail(email);
    }

    @Override
    public Client createClient(RegisterRequest registerRequest) {
        return clientService.createClient(registerRequest);
    }


    //TODO change this update to patch format //done
    //TODO reformat this method for facade style //done
    @Override
    public Client updateClient(ClientRequestDTO clientRequestDTO) {
        Client oldClient = clientService.getClient(clientRequestDTO.getId());
        return clientService.updateClient(clientRequestDTO, oldClient);
    }

    //TODO work only with username in args //done
    @Override
    public ClientResponseDTO updateUsername(Client currentAuthenticatedClient, String email) {
        currentAuthenticatedClient = clientService.updateUsername(currentAuthenticatedClient, email);

        ClientResponseDTO clientResponseDTO = modelMapper.map(
                currentAuthenticatedClient, ClientResponseDTO.class
        );

        clientResponseDTO.setAccessToken(jwtService.generateAccessToken(currentAuthenticatedClient));
        clientResponseDTO.setRefreshToken(jwtService.generateRefreshToken(currentAuthenticatedClient));

        return clientResponseDTO;
    }

    @Override
    public void deleteClient(Long id) {
        clientService.deleteClient(id);
    }
}
