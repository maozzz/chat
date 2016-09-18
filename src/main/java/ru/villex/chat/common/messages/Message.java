package ru.villex.chat.common.messages;

import ru.villex.chat.common.clients.Client;

/**
 * Created by maoz on 17.09.16.
 */
public interface Message {
    public String toJson();

    public String getMessageClass();

    public Class handlerClass();

    public void setAuthor(Client author);

    public Client getAuthor();
}
