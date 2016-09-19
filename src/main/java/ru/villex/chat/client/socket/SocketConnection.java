package ru.villex.chat.client.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.villex.chat.common.Sendable;
import ru.villex.chat.common.handlers.HandlerManager;
import ru.villex.chat.common.messages.Message;
import ru.villex.chat.common.messages.MessageFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by maoz on 17.09.16.
 */
@Component
public class SocketConnection implements Runnable, Sendable {

    private int port;
    private String host;
    private boolean stopped = false;
    private Thread thread;
    OutputStream os;

    @Autowired
    HandlerManager handlerManager;

    @Autowired
    MessageFactory messageFactory;

    public SocketConnection(@Value("${socket.host}") String host, @Value("${socket.port}") int port) {
        this.port = port;
        this.host = host;
    }

    @PostConstruct
    private void init() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {
            os = socket.getOutputStream();
            Scanner sc = new Scanner(socket.getInputStream());
            sc.useDelimiter(String.valueOf(Sendable.DELIMETER));

            while (!stopped) {
                String line = sc.next();
                Message message = messageFactory.fromJson(line);
                handlerManager.handle(message);
                Thread.sleep(50);
            }
            sc.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            System.out.println("Соединение разорвано!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            os = null;
        }
    }

    private void writeln(byte[] bytes) throws IOException {
        if (os != null) {
            os.write(bytes);
            os.write(Sendable.DELIMETER.getBytes());
            os.flush();
        }
    }

    @Override
    public void send(Message message) throws IOException {
        writeln(message.toJson().getBytes());
    }
}
