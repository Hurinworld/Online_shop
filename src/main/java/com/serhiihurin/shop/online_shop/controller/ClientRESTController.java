package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import com.serhiihurin.shop.online_shop.facades.ClientFacade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/clients")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'CLIENT')")
@RequiredArgsConstructor
public class ClientRESTController {
    private final ClientFacade clientFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ClientResponseDTO> getAllClients() {
        return modelMapper.map(
                clientFacade.getAllClients(),
                new TypeToken<List<ClientResponseDTO>>(){}.getType()
        );
    }

//    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('admin view info')")
//    public ClientResponseDTO getClient(@PathVariable Long id) throws ApiRequestException {
//        return modelMapper.map(clientFacade.getClient(id), ClientResponseDTO.class);
//    }

    //TODO rename path to "/me"
    //TODO divide into 2 endpoints
    @GetMapping("/client")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info')")
    public ClientResponseDTO getClient(@ModelAttribute("currentClient") Client currentClient,
                                       @RequestParam(required = false) Long clientId) {
        if (!currentClient.getRole().equals(Role.ADMIN)
                && !currentClient.getRole().equals(Role.SUPER_ADMIN)
                && clientId != null) {
            throw new UnauthorizedAccessException("Access to the resource is not allowed");
        }
        if (
                (currentClient.getRole().equals(Role.ADMIN) || currentClient.getRole().equals(Role.SUPER_ADMIN))
                && clientId == null) {
            return modelMapper.map(
                    clientFacade.getClient(currentClient.getId()), ClientResponseDTO.class
            );
        } else if (clientId != null){
            return modelMapper.map(clientFacade.getClient(clientId), ClientResponseDTO.class);
        }

        return modelMapper.map(clientFacade.getClient(currentClient.getId()), ClientResponseDTO.class);
    }
//TODO delete
//    @PostMapping
//    public ResponseEntity<Client> addNewClient(@RequestBody Client client) {
//        return ResponseEntity.ok(clientService.saveClient(client));
//    }
//TODO add usernameUpdate
    @PatchMapping("/info")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        clientFacade.updateClient(clientRequestDTO),
                        ClientResponseDTO.class
                )
        );
    }

    //TODO change http-method to put
    @PatchMapping("/info/username")
    @PreAuthorize("hasAuthority('account management')")
    //TODO retrieve userDetails for id
    //TODO change requestBody to requestParam(username)
    public ResponseEntity<ClientResponseDTO> updateUsername(@RequestBody ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.ok(clientFacade.updateUsername(clientRequestDTO));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteClient(@ModelAttribute("currentClient") Client currentClient,
                                             @RequestParam(required = false) Long id) {
        if (!currentClient.getRole().equals(Role.ADMIN)
                && !currentClient.getRole().equals(Role.SUPER_ADMIN)
                && id != null) {
            throw new UnauthorizedAccessException("Access to the resource is not allowed");
        }
        //FIXME: 401 status when id is null and role 'SUPER_ADMIN'
        //TODO change order of args in equals(everywhere)
        if (currentClient.getRole().equals(Role.SUPER_ADMIN)) {
            clientFacade.deleteClient(id);
        }
        if (!currentClient.getRole().equals(Role.ADMIN)
                && !currentClient.getRole().equals(Role.SUPER_ADMIN)
                && id == null){
            clientFacade.deleteClient(currentClient.getId());
        }
        return ResponseEntity.ok().build();
    }
}
