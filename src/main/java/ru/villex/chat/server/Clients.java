package ru.villex.chat.server;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.common.clients.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoz on 21.09.16.
 */
@Component
public class Clients {

    private final ApplicationContext context;
    private List<Client> clients = new ArrayList<>();

    public Clients(ApplicationContext context) {
        this.context = context;
    }

    public void registerClient(Client client) {
        clients.add(client);
    }

    public List<Client> getClients() {
        return clients;
    }
}
