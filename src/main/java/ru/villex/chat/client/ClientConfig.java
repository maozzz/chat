package ru.villex.chat.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sound.sampled.AudioFormat;

/**
 * Created by maoz on 17.09.16.
 */
@Configuration
@PropertySource(value = "classpath:client.properties")
public class ClientConfig {

    @Bean
    public AudioFormat audioFormat(){
        return new AudioFormat(16000.0F, 16, 1, true, false);
    }
}
