package ru.villex.chat.common.handlers;

import ru.villex.chat.common.messages.Message;

/**
 * Created by maoz on 18.09.16.
 */
public interface Handler extends Runnable {
    public void handle(Message message) throws IllegalArgumentException;
    public Class<? extends Message> handlerOf();
}
