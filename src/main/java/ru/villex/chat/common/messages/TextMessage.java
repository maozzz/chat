package ru.villex.chat.common.messages;

import com.alibaba.fastjson.JSON;
import ru.villex.chat.common.handlers.EchoHandler;

/**
 * Created by maoz on 17.09.16.
 */
public class TextMessage extends AbstractMessage {
    private String text;

    public TextMessage() {
    }

    public TextMessage(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public Class handlerClass() {
        return EchoHandler.class;
    }
}
