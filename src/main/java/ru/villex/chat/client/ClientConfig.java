package ru.villex.chat.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by maoz on 17.09.16.
 */
@Configuration
@Profile("default")
@PropertySource(value = "classpath:client.properties")
public class ClientConfig {
}
