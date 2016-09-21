package ru.villex.chat.common.clients;

import ru.villex.chat.common.Sendable;

/**
 * Created by maoz on 18.09.16.
 */
public interface Client extends Sendable {
    public int getHashCode();
}
