package ru.villex.chat.client.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.client.AudioPlayer;
import ru.villex.chat.common.handlers.Handler;
import ru.villex.chat.common.messages.AudioMessage;
import ru.villex.chat.common.messages.Message;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by maoz on 20.09.16.
 */
@Component
public class AudioHandler implements Handler {

    ApplicationContext context;
    BlockingQueue<AudioMessage> queue;
    AudioPlayer player;

    public AudioHandler(ApplicationContext context, @Value("${handler.audio_handler.capacity:5}") int capacity) {
        this.context = context;
        queue = new ArrayBlockingQueue<AudioMessage>(capacity, true);
        player = context.getBean(AudioPlayer.class);
    }

    private boolean stopped = false;
    private Thread thread;

    @Override
    public Class<? extends Message> handlerOf() {
        return AudioMessage.class;
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
                AudioMessage message = queue.take();
                player.play(message.getBytes());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(Message message) throws IllegalArgumentException {
        System.out.println("Обрабатываю аудиосообщение!");
        if (message instanceof AudioMessage)
            queue.add((AudioMessage) message);
        else
            throw new IllegalArgumentException("Требуется AudioMessage. Получено: " + message.getClass().getName());
    }
}
