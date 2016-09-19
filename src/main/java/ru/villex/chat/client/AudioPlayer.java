package ru.villex.chat.client;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Created by maoz on 20.09.16.
 */
@Component
public class AudioPlayer implements Runnable {
    ApplicationContext context;
    AudioFormat format;
    SourceDataLine sLine;
    byte[] bytes;
    Thread thread;
    private boolean stopped = false;

    public AudioPlayer(ApplicationContext context) throws LineUnavailableException {
        this.context = context;
        format = context.getBean(AudioFormat.class);
        sLine = AudioSystem.getSourceDataLine(format);
    }

    public void play(byte[] bytes) throws InterruptedException {
        stop();
        stopped = false;
        if (thread != null && thread.isAlive()){
            thread.join();
        }
        this.bytes = bytes;
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        stopped = true;
        sLine.close();
        if (thread!= null && thread.isAlive()){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            int offset = 0;
            int buffSize = sLine.getBufferSize();

            sLine.open(format);
            sLine.start();
            while (!stopped && offset < bytes.length) {
                sLine.write(bytes, offset, buffSize);
                offset += buffSize;
                if ((offset + buffSize) > bytes.length) {
                    buffSize = bytes.length - offset;
                }
            }
            sLine.drain();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            sLine.stop();
            sLine.close();
        }
    }
}
