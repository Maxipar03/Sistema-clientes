package com.coderhouse.clients_system.services;

import com.coderhouse.clients_system.entities.Client;
import com.coderhouse.clients_system.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client) {
       return clientRepository.save(client);
    }
}
