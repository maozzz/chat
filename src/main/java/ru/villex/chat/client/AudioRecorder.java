package ru.villex.chat.client;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

/**
 * Created by maoz on 19.09.16.
 */
@Component
public class AudioRecorder implements Runnable {
    ApplicationContext context;
    AudioFormat format;
    DataLine.Info info;
    ByteArrayOutputStream out;
    byte[] buff;
    LinkedList<Byte> queue = new LinkedList<>();
    Thread currentThread;

    private boolean stopped = false;

    public AudioRecorder(ApplicationContext context) throws Exception {
        this.context = context;
        format = context.getBean(AudioFormat.class);
        info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new Exception("Unsupported audio format");
        }
    }

    public void startRecord() {
        stopped = false;
        currentThread = new Thread(this);
        currentThread.start();
    }

    public byte[] stopRecord() {
        stopped = true;
        try {
            currentThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ArrayUtils.toPrimitive(queue.toArray(new Byte[queue.size()]));
    }

    @Override
    public void run() {
        queue.clear();
        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {

            line.open(format);
            out = new ByteArrayOutputStream();
            int numBytesRead;
            buff = new byte[line.getBufferSize() / 5];

            //Begin audio capture
            line.start();
            while (!stopped) {
                numBytesRead = line.read(buff, 0, buff.length);
                for (byte b : buff) {
                    queue.add(b);
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
