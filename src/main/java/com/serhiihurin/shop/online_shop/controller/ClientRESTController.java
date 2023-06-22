package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/clients")
public class ClientRESTController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> showAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @PostMapping
    public Client addNewClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return client;
    }

    @PutMapping
    public Client updateClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return client;
    }

    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "Client with id = " + id + " was deleted";
    }
}
