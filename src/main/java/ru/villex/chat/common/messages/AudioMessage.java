package ru.villex.chat.common.messages;

import com.alibaba.fastjson.JSON;
import ru.villex.chat.client.handlers.AudioHandler;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by maoz on 20.09.16.
 */
public class AudioMessage extends TextMessage {
    private String text;

    public AudioMessage() {
    }

    public AudioMessage(String text) {
        this.text = text;
    }

    public AudioMessage(byte[] bytes){
        text = new BASE64Encoder().encode(bytes);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public byte[] getBytes(){
        try {
            return new BASE64Decoder().decodeBuffer(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[] {};
    }

    @Override
    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public Class handlerClass() {
        return AudioHandler.class;
    }
}
