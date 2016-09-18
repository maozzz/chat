package ru.villex.chat.common.messages;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by maoz on 18.09.16.
 */
@Component
public class MessageFactory {
    ApplicationContext context;

    public MessageFactory(ApplicationContext context) {
        this.context = context;
    }

    public Message fromJson(String json) throws ClassNotFoundException {
        JSONObject jObj = JSON.parseObject(json);
        Class c = Class.forName((String) jObj.get("messageClass"));
        return (Message) jObj.toJavaObject(c);
    }
}
