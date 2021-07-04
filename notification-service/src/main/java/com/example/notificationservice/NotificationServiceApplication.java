package com.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @Bean
//    public Consumer<String> notificationEventSupplier() {//use exact same name as the binder for this bean which is notificationEventSupplier
//        return message -> log.info("****" + message);
//    }
    //wrap with Message<String> to get the out of box support of spring
    public Consumer<Message<String>> notificationEventSupplier() {//use exact same name as the binder for this bean which is notificationEventSupplier
        return message -> log.info("****" + message.getPayload());
    }
}
