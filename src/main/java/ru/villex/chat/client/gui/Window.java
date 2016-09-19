package ru.villex.chat.client.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

/**
 * Created by maoz on 18.09.16.
 */
@Component
public class Window extends JFrame {
    private final String title = "Maozype";
    private ApplicationContext context;

    JPanel north, south, west, east, center;

    public Window(ApplicationContext context) {
        super();
        this.context = context;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(
                Math.round((float) (screenRes.getWidth() / 20)),
                Math.round((float) (screenRes.getHeight() / 20)),
                Math.round((float) (screenRes.width * 0.9)),
                Math.round((float) (screenRes.height * 0.9))
        );
        setVisible(true);
    }

    @PostConstruct
    private void init() {
        setLayout(new BorderLayout());
        south = new JPanel();
        east = new JPanel();
        west = new JPanel();
        north = new JPanel();
        center = new JPanel();

        south.add(new JLabel("south"));
        east.add(new JLabel("east"));
        north.add(new JLabel("north"));
        west.add(new JLabel("west"));

        center.setLayout(new BorderLayout());
        center.add(context.getBean(MessagesContainer.class), BorderLayout.CENTER);
        center.add(context.getBean(InputForm.class), BorderLayout.SOUTH);


        add(north, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
        add(west, BorderLayout.WEST);
        add(east, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        validate();
        repaint();
    }
}
