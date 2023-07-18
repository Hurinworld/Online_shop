package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ClientDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.services.ClientService;
import com.serhiihurin.shop.online_shop.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientFacadeImpl implements ClientFacade{
    private final ClientService clientService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

    //TODO change this update to patch format
    //TODO reformat this method for facade style
    @Override
    public ClientDTO updateClient(ClientRequestDTO clientRequestDTO) {
        Client oldClient = clientService.getClient(clientRequestDTO.getId());

        Client client = new Client();

        client.setId(oldClient.getId());
        client.setRole(oldClient.getRole());

        client.setFirstName(
                clientRequestDTO.getFirstName() != null ? clientRequestDTO.getFirstName() : oldClient.getFirstName()
        );
        client.setLastName(
                clientRequestDTO.getLastName() != null ? clientRequestDTO.getLastName() : oldClient.getLastName()
        );
        client.setCash(
                clientRequestDTO.getCash() != null ? clientRequestDTO.getCash() : oldClient.getCash()
        );
        client.setEmail(
                clientRequestDTO.getEmail() != null ? clientRequestDTO.getEmail() : oldClient.getEmail()
        );
        client.setPassword(
                clientRequestDTO.getPassword() != null ? clientRequestDTO.getPassword() : oldClient.getPassword()
        );

        ClientDTO clientDTO = modelMapper.map(
                clientService.saveClient(client), ClientDTO.class
        );

        clientDTO.setAccessToken(jwtService.generateAccessToken(client));
        clientDTO.setRefreshToken(jwtService.generateRefreshToken(client));

        return clientDTO;
    }
}
