package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ClientDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
import com.serhiihurin.shop.online_shop.facades.ClientFacade;
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
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'CLIENT')")
@RequiredArgsConstructor
public class ClientRESTController {
    private final ClientFacade clientFacade;
    private final ClientService clientService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ClientDTO> getAllClients() {
        return modelMapper.map(
                clientService.getAllClients(),
                new TypeToken<List<ClientDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public ClientDTO getClient(@PathVariable Long id) {
        return modelMapper.map(clientService.getClient(id), ClientDTO.class);
    }

//    @PostMapping
//    public ResponseEntity<Client> addNewClient(@RequestBody Client client) {
//        return ResponseEntity.ok(clientService.saveClient(client));
//    }

    @PutMapping
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<ClientDTO> updateClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.ok(clientFacade.updateClient(clientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
