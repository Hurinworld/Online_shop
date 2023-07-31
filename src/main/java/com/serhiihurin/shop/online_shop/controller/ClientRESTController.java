package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.facades.ClientFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public ClientResponseDTO getClient(@PathVariable Long id) {
        return modelMapper.map(clientFacade.getClient(id), ClientResponseDTO.class);
    }

    //TODO rename path to "/me" //done
    //TODO divide into 2 endpoints //done
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info', 'shop owner view info')")
    public ClientResponseDTO getClient(@ModelAttribute("currentClient") Client currentAuthenticatedClient) {
        return modelMapper.map(
                clientFacade.getClient(currentAuthenticatedClient.getId()), ClientResponseDTO.class
        );
    }
//TODO delete //done

//TODO add usernameUpdate //done
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

    //TODO change http-method to put //done
    @PutMapping("/info/username")
    @PreAuthorize("hasAuthority('account management')")
    //TODO retrieve userDetails for id //done
    //TODO change requestBody to requestParam(username) //done
    public ResponseEntity<ClientResponseDTO> updateUsername(
            @ModelAttribute("currentClient") Client currentAuthenticatedClient,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(clientFacade.updateUsername(currentAuthenticatedClient, email));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('super admin info deletion')")
    public ResponseEntity<Void> deleteClient(@RequestParam(required = false) Long id) {
        if (id == null) {
            throw new ApiRequestException("Invalid URL");
        }
        Client client = clientFacade.getClient(id);
        clientFacade.deleteClient(client.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my-account")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteClient(@ModelAttribute("currentClient") Client currentClient) {
        clientFacade.deleteClient(currentClient.getId());
        return ResponseEntity.ok().build();
    }
}
