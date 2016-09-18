package ru.villex.chat.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by maoz on 16.09.16.
 */
@Configuration
@Profile("default")
@PropertySource(value = "classpath:server.properties")
public class ServerConfig {
}
