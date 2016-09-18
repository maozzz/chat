package ru.villex.chat.server.socket;

import org.springframework.context.ApplicationContext;
import ru.villex.chat.common.Sendable;
import ru.villex.chat.common.clients.Client;
import ru.villex.chat.common.handlers.HandlerManager;
import ru.villex.chat.common.messages.Message;
import ru.villex.chat.common.messages.MessageFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by maoz on 16.09.16.
 */
public class SimpleSocketClient implements Runnable, Client {
    OutputStream os;
    Scanner sc;
    private boolean stopped = false;
    Thread thread = new Thread();
    ApplicationContext context;

    MessageFactory messageFactory;

    HandlerManager handlerManager;


    public SimpleSocketClient(Socket socket, ApplicationContext context) throws IOException {
        System.out.println(this.getClass().getName() + ": Подключился новый клиент!");
        this.os = socket.getOutputStream();
        this.sc = new Scanner(socket.getInputStream());
        sc.useDelimiter(Sendable.DELIMETER);
        this.context = context;
        messageFactory = context.getBean(MessageFactory.class);
        handlerManager = context.getBean(HandlerManager.class);
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                String line = sc.next();
                System.out.println("Сообщение от клиента: " + line);
                Message message = messageFactory.fromJson(line);
                message.setAuthor(this);
                handlerManager.handle(message);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchElementException ex) {
                System.out.println("Отключение клиента!");
                stopped = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        sc.close();
        try {
            if (os != null)
                os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        os = null;
        System.out.println("Клиент отключен!");
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void writeln(byte[] bytes) throws IOException {
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
