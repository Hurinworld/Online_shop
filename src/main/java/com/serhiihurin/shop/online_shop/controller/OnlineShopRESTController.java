package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop")
public class OnlineShopRESTController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public List<Client> showAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/clients/{id}")
    public Client getEmployee(@PathVariable int id) {
        return clientService.getClient(id);
    }

    @PostMapping("/clients")
    public Client addNewClient(@RequestBody Client client) {

        clientService.saveClient(client);

        return client;
    }

    @PutMapping("/clients")
    public Client updateClient(@RequestBody Client client) {

        clientService.saveClient(client);

        return client;
    }

    @DeleteMapping("/clients/{id}")
    public String deleteClient(@PathVariable int id) {
        clientService.deleteClient(id);
        return "Client with id = " + id + " was deleted";
    }
}
