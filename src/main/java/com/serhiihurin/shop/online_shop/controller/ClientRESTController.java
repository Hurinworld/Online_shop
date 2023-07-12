package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.services.ClientService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

//    @PostMapping
//    public ResponseEntity<Client> addNewClient(@RequestBody Client client) {
//        return ResponseEntity.ok(clientService.saveClient(client));
//    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:update', 'client:update')")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'client:delete')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
