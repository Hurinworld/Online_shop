package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ClientDTO;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/clients")
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
@RequiredArgsConstructor
public class ClientRESTController {
    private final ClientService clientService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<ClientDTO> getAllClients() {
        return modelMapper.map(
                clientService.getAllClients(),
                new TypeToken<List<ClientDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ClientDTO getClient(@PathVariable Long id) {
        return modelMapper.map(clientService.getClient(id), ClientDTO.class);
    }

//    @PostMapping
//    public ResponseEntity<Client> addNewClient(@RequestBody Client client) {
//        return ResponseEntity.ok(clientService.saveClient(client));
//    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:update', 'client:update')")
    public ResponseEntity<ClientDTO> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(
                modelMapper.map(
                        clientService.saveClient(client), ClientDTO.class
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'client:delete')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
