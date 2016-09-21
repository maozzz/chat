package ru.villex.chat.server.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.server.Clients;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by maoz on 16.09.16.
 */
@Component
public class SocketListener implements Runnable {

    private boolean stopped = false;
    ApplicationContext context;
    Clients clients;

    private int port;
    private Thread thread;

    public SocketListener(@Value("${socket.port}") int port, ApplicationContext context) {
        this.port = port;
        this.context = context;
        this.clients = context.getBean(Clients.class);
    }

    @PostConstruct
    private void init() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (!stopped) {
            System.out.println(this.getClass().getName() + ": Ожидаю нового клиента!");
            try (ServerSocket socket = new ServerSocket(port)) {
                Socket clientSocket = socket.accept();
                SimpleSocketClient client = new SimpleSocketClient(clientSocket, context);
                client.start();
                clients.registerClient(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
