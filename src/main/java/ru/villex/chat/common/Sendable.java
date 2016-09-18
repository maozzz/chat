package ru.villex.chat.common;

import ru.villex.chat.common.messages.Message;

import java.io.IOException;

/**
 * Created by maoz on 18.09.16.
 */
public interface Sendable {
    public static final String DELIMETER = "\r\n";

    public void send(Message message) throws IOException;
}
