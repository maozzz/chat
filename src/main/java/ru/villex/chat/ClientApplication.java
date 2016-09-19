package ru.villex.chat;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.villex.chat.client.socket.SocketConnection;
import ru.villex.chat.common.messages.Message;
import ru.villex.chat.common.messages.TextMessage;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by maoz on 17.09.16.
 */
public class ClientApplication {

    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("default", "client");
        context.scan("ru.villex.chat.client");
        context.scan("ru.villex.chat.common");
        context.refresh();

        Scanner sc = new Scanner(System.in);
        SocketConnection connection = context.getBean(SocketConnection.class);
        while (true) {
            System.out.println("Введите сообщение:");
            String line = sc.nextLine();
            Message message = new TextMessage(line);
            System.out.println(message.toJson());
            try {
                connection.send(message);
                System.out.println("OK");
            } catch (IOException e) {
                System.out.println("Ошибка при отправке сообщения!");
            }
        }
    }
}
