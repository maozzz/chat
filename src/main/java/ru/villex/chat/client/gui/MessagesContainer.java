package ru.villex.chat.client.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.common.Writeable;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by maoz on 19.09.16.
 */
@Component
public class MessagesContainer extends JTextPane implements Writeable {
    ApplicationContext context;

    public MessagesContainer(ApplicationContext context) {
        super();
        setEditable(false);
        this.context = context;
    }

    @PostConstruct
    private void init() {
        writeln("Start chat!");
    }

    @Override
    public void writeln(String message) {
        setText(getText() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM uuuu H:mm:ss", new Locale("ru"))) + ": " + message + "\r\n\r\n");
        repaint();
    }
}
