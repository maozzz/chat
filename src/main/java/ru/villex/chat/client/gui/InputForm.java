package ru.villex.chat.client.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.villex.chat.client.AudioPlayer;
import ru.villex.chat.client.AudioRecorder;
import ru.villex.chat.common.Sendable;
import ru.villex.chat.common.messages.AudioMessage;
import ru.villex.chat.common.messages.TextMessage;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by maoz on 19.09.16.
 */
@Component
public class InputForm extends JPanel {
    ApplicationContext context;

    private JTextField field;
    private JButton submit;
    private JButton audio;
    private Sendable sender;
    AudioRecorder recorder;
    AudioPlayer player;

    public InputForm(ApplicationContext context, Sendable sender) {
        super();
        this.sender = sender;
        this.context = context;
        field  = new JTextField();
        submit = new JButton("Отправить");
        audio = new JButton("Push-to-talk");
        recorder = context.getBean(AudioRecorder.class);
        player = context.getBean(AudioPlayer.class);
    }

    @PostConstruct
    private void init() {
        setLayout(new BorderLayout());
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(submit, BorderLayout.EAST);


        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendMessage();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        add(field, BorderLayout.CENTER);

        audio.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                recorder.startRecord();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                byte[] record = recorder.stopRecord();
                try {
                    sender.send(new AudioMessage(record));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(audio, BorderLayout.WEST);
    }

    private void sendMessage() throws IOException {
        if (field.getText().equals("")) return;
        sender.send(new TextMessage(field.getText()));
        String a = field.getText();
        field.setText("");
    }
}
