package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ClientResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ClientRequestDTO;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public ClientResponseDTO getClient(@PathVariable Long id) {
        return modelMapper.map(clientFacade.getClient(id), ClientResponseDTO.class);
    }

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

    @PatchMapping("/info/username")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<ClientResponseDTO> updateUsername(@RequestBody ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.ok(clientFacade.updateUsername(clientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientFacade.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
