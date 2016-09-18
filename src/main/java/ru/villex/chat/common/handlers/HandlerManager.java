package ru.villex.chat.common.handlers;

import org.springframework.stereotype.Component;
import ru.villex.chat.common.messages.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maoz on 18.09.16.
 */
@Component
public class HandlerManager {

    Map<Class<? extends Handler>, Handler> handlers = new HashMap<>();

    public HandlerManager(List<Handler> handlers) {
        for (Handler handler : handlers) {
            this.handlers.put(handler.getClass(), handler);
        }
    }

    public void handle(Message message) {
        try {
            handlers.get(message.handlerClass()).handle(message);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
}
