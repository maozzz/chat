package ru.villex.chat.server.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.common.clients.Client;
import ru.villex.chat.common.handlers.Handler;
import ru.villex.chat.common.messages.Message;
import ru.villex.chat.common.messages.TextMessage;
import ru.villex.chat.server.Clients;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by maoz on 21.09.16.
 */
@Component
public class TextHandler implements Handler {

    ApplicationContext context;
    BlockingQueue<TextMessage> queue;
    Clients clients;

    public TextHandler(ApplicationContext context, @Value("${handler.text_handler.capacity:5}") int capacity) {
        this.context = context;
        queue = new ArrayBlockingQueue<TextMessage>(capacity, true);
        clients = context.getBean(Clients.class);
    }

    private boolean stopped = false;
    private Thread thread;

    @Override
    public Class<? extends Message> handlerOf() {
        return TextMessage.class;
    }

    @PostConstruct
    private void init() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                TextMessage message = queue.take();
                for (Client client : clients.getClients()){
                    if (client != message.getAuthor()){
                        client.send(message);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(Message message) throws IllegalArgumentException {
        if (message.getAuthor() == null)
            throw new IllegalArgumentException("Для обработки сообщения требуется сведения об отправителе!");
        if (message instanceof TextMessage)
            queue.add((TextMessage) message);
        else
            throw new IllegalArgumentException("Требуется TextMessage. Получено: " + message.getClass().getName());
    }
}
