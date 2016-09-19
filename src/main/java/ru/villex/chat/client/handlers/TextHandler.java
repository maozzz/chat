package ru.villex.chat.client.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.common.Writeable;
import ru.villex.chat.common.handlers.Handler;
import ru.villex.chat.common.messages.Message;
import ru.villex.chat.common.messages.TextMessage;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by maoz on 19.09.16.
 */
@Component
public class TextHandler implements Handler {

    ApplicationContext context;
    BlockingQueue<TextMessage> queue;
    Writeable writer;

    public TextHandler(ApplicationContext context, @Value("${handler.text_handler.capacity:5}") int capacity) {
        this.context = context;
        queue = new ArrayBlockingQueue<TextMessage>(capacity, true);
        writer = context.getBean(Writeable.class);
    }

    private boolean stopped = false;
    private Thread thread;

    @PostConstruct
    private void init() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void handle(Message message) throws IllegalArgumentException {
        if (message instanceof TextMessage)
            queue.add((TextMessage) message);
        else
            throw new IllegalArgumentException("Требуется TextMessage. Получено: " + message.getClass().getName());
    }

    @Override
    public Class<? extends Message> handlerOf() {
        return TextMessage.class;
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                TextMessage message = queue.take();
                writer.writeln(message.getText());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
