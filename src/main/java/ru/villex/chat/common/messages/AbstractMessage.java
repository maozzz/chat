package ru.villex.chat.common.messages;

import ru.villex.chat.common.clients.Client;

/**
 * Created by maoz on 18.09.16.
 */
public abstract class AbstractMessage implements Message {

    Client author;

    @Override
    public String getMessageClass() {
        return getClass().getName();
    }

    @Override
    public Client getAuthor() {
        return author;
    }

    public void setAuthor(Client author) {
        this.author = author;
    }
}
