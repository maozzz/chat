package ru.villex.chat;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by maoz on 16.09.16.
 */


public class ServerApplication {

    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext();
        context.scan("ru.villex.chat.server");
        context.scan("ru.villex.chat.common");
        context.refresh();
    }
}